package net.duchung.quora.service;

import net.duchung.quora.data.response.AnswerResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface ElasticSearchService {
    List<AnswerResponse> searchByContent(String content) throws IOException;
}
