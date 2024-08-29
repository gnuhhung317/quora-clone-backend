package net.duchung.quora.service;

import net.duchung.quora.dto.AnswerDto;
import net.duchung.quora.entity.Answer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RecommendationService {

    List<AnswerDto> getRecommendationAnswers(Long userId);
}
