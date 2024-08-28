package net.duchung.quora.service.impl;

import net.duchung.quora.utils.Utils;
import net.duchung.quora.dto.AnswerDto;
import net.duchung.quora.dto.response.ListAnswerResponse;
import net.duchung.quora.entity.Answer;
import net.duchung.quora.mapper.AnswerMapper;
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
    @Override
    public ListAnswerResponse search(String query, String topic, int page) {
        PageRequest pageRequest = PageRequest.of(page, Utils.SEARCH_RESULTS_PER_PAGE, Sort.by("viral_points").descending());
        Page<Answer> answers = answerRepository.searchByContent(query,topic,pageRequest);
        Long total = answers.getTotalElements();
        List<AnswerDto> answerDtos = answers.getContent().stream().map(AnswerMapper.INSTANCE::toAnswerDTO).toList();
        return new ListAnswerResponse(answerDtos,total);
    }
}
