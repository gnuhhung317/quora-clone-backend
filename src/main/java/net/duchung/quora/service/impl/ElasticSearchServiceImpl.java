package net.duchung.quora.service.impl;

import net.duchung.quora.repository.elastic.EsAnswerRepository;
import net.duchung.quora.service.AnswerService;
import net.duchung.quora.service.ElasticSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ElasticSearchServiceImpl implements ElasticSearchService {
    @Autowired
    private EsAnswerRepository answerRepository;
    @Override
    public List<AnswerService> searchByContent(String content) {

        return null;
    }
}
