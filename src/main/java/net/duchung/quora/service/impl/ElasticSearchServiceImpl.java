package net.duchung.quora.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.FuzzyQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import net.duchung.quora.data.document.AnswerDocument;
import net.duchung.quora.data.entity.Answer;
import net.duchung.quora.data.mapper.AnswerMapper;
import net.duchung.quora.data.response.AnswerResponse;
import net.duchung.quora.repository.AnswerRepository;
import net.duchung.quora.repository.elastic.EsAnswerRepository;
import net.duchung.quora.service.AnswerService;
import net.duchung.quora.service.AuthService;
import net.duchung.quora.service.ElasticSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class ElasticSearchServiceImpl implements ElasticSearchService {
    @Autowired
    private EsAnswerRepository esAnswerRepository;
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private ElasticsearchClient elasticsearchClient;
    @Autowired
    private AuthService authService;

    @Override
    public List<AnswerResponse> searchByContent(String content) throws IOException {

        Supplier<Query>  supplier = ()->Query.of(q->q.fuzzy(createFuzzyQuery(content)));
        SearchRequest request = SearchRequest.of(s->s.index("answer").query(supplier.get()));
        SearchResponse<AnswerDocument> response = elasticsearchClient.search(request, AnswerDocument.class);
        System.out.println("elasticsearch supplier fuzzy query "+response.toString());

        List<Answer> answers = answerRepository.findAllById(response.hits().hits().stream().map(hit -> hit.source().getId()).collect(Collectors.toList()));

        return answers.stream().map(answer -> AnswerMapper.toAnswerResponse(answer,authService.getCurrentUser().getId())).collect(Collectors.toList());
    }

    public FuzzyQuery createFuzzyQuery(String approximateProductName){
        FuzzyQuery.Builder fuzzyQuery  = new FuzzyQuery.Builder();
        return  fuzzyQuery.field("content").value(approximateProductName).build();

    }
}
