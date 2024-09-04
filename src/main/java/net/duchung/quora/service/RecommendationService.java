package net.duchung.quora.service;

import net.duchung.quora.data.response.AnswerResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RecommendationService {

    List<AnswerResponse> getRecommendationAnswers();
}
