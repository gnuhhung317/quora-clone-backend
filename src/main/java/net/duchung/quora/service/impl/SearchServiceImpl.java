package net.duchung.quora.service.impl;

import net.duchung.quora.data.response.AnswerResponse;
import net.duchung.quora.service.AuthService;
import net.duchung.quora.common.utils.Constant;
import net.duchung.quora.data.response.ListAnswerResponse;
import net.duchung.quora.data.entity.Answer;
import net.duchung.quora.data.mapper.AnswerMapper;
import net.duchung.quora.repository.AnswerRepository;
import net.duchung.quora.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private AuthService authService;

    @Override
    public ListAnswerResponse search(String query, String topic, int page) {
        Long userId = authService.getCurrentUser().getId();
        PageRequest pageRequest = PageRequest.of(page, Constant.SEARCH_RESULTS_PER_PAGE, Sort.by("viralPoints").descending());

        Page<Answer> answers = answerRepository.searchByContent(query, topic, pageRequest);

        Long total = answers.getTotalElements();
        List<AnswerResponse> answerResponses = answers.getContent().stream().map((answer -> AnswerMapper.toAnswerResponse(answer, userId))).toList();

        return new ListAnswerResponse(answerResponses, total);
    }
}
