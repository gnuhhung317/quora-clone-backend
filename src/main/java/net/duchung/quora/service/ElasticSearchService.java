package net.duchung.quora.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ElasticSearchService {
    List<AnswerService> searchByContent(String content);
}
