package net.duchung.quora.service.impl;

import net.duchung.quora.dto.AnswerDto;
import net.duchung.quora.entity.Answer;
import net.duchung.quora.mapper.AnswerMapper;
import net.duchung.quora.mapper.BaseMapper;
import net.duchung.quora.repository.AnswerRepository;
import net.duchung.quora.service.RecommendationService;
import net.duchung.quora.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationServiceImpl implements RecommendationService {
    @Autowired
    private AnswerRepository answerRepository;
    @Override
    public List<AnswerDto> getRecommendationAnswers(Long userId) {
        List<Object[]> recommendations = new ArrayList<>();
        recommendations.addAll(mapToRecommendedAnswer(answerRepository.recommendationByViralAnswer(userId), 2.0));
        recommendations.addAll(mapToRecommendedAnswer(answerRepository.recommendationByFollowingUserFeed(userId), 1.8));
        recommendations.addAll(mapToRecommendedAnswer(answerRepository.recommendationByFollowingUserAnswers(userId), 1.5));
        recommendations.addAll(mapToRecommendedAnswer(answerRepository.recommendationByFollowingUserQuestions(userId), 1.2));
        recommendations.addAll(mapToRecommendedAnswer(answerRepository.recommendationByViralAnswerAllTopic(userId), 1.0));

        // Add recommendations from recent answers in topic (this query doesn't return order_value)
        recommendations.addAll(answerRepository.recommendationByRecentAnswerInTopic(userId).stream()
                .map(answer -> new Object[]{answer, 0.8}) // Assign a lower weight
                .toList());

        // Sort by weighted order value, remove duplicates, and limit to MAX_RECOMMENDATION
        return recommendations.stream()
                .sorted((a, b) -> Double.compare((Double) b[1], (Double) a[1])) // sort
                .distinct() // remove duplicates
                .limit(Utils.MAX_RECOMMENDATION) // limit
                .map(ra -> toAnswerDto((Answer) ra[0]))
                .collect(Collectors.toList());
    }

    // add weight for each recommendation type
    private List<Object[]> mapToRecommendedAnswer(List<Object[]> results, double weight) {
        return results.stream()
                .map(result -> {
                    result[1] = (Double)result[1] * weight;
                    return new Object[]{result[0], result[1]};
                })
                .collect(Collectors.toList());
    }

    public AnswerDto toAnswerDto(Answer answer) {
        AnswerDto answerDto = AnswerMapper.INSTANCE.toAnswerDTO(answer);
        BaseMapper.getBaseDtoAttribute(answerDto,answer);
        return answerDto;
    }
}
