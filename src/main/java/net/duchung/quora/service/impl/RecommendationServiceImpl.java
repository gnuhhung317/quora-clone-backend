package net.duchung.quora.service.impl;

import net.duchung.quora.data.request.AnswerRequest;
import net.duchung.quora.data.entity.Answer;
import net.duchung.quora.data.entity.AnswerScore;
import net.duchung.quora.common.exception.DataNotFoundException;
import net.duchung.quora.data.mapper.AnswerMapper;
import net.duchung.quora.data.mapper.BaseMapper;
import net.duchung.quora.data.response.AnswerResponse;
import net.duchung.quora.repository.AnswerRepository;
import net.duchung.quora.repository.AnswerVoteRepository;
import net.duchung.quora.service.AuthService;
import net.duchung.quora.service.RecommendationService;
import net.duchung.quora.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationServiceImpl implements RecommendationService {
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private AnswerVoteRepository answerVoteRepository;
    @Autowired
    private AuthService authService;
    @Override
    public List<AnswerResponse> getRecommendationAnswers() {
        Long userId = authService.getCurrentUser().getId();
        List<AnswerScore> recommendations = getAnswerScores(userId);
        return rankingAnswers(recommendations,userId);
    }

    private List<AnswerResponse> rankingAnswers(List<AnswerScore> recommendations,Long userId) {
        for(AnswerScore answerScore : recommendations){
            System.out.print(answerScore.getScore()+" ");
        }
        // Sort by weighted order value, remove duplicates, and limit to MAX_RECOMMENDATION
        return recommendations.stream()
                .distinct() // remove duplicates
                .map(ra -> AnswerMapper.toAnswerResponse(ra.getAnswer(),userId))
                .collect(Collectors.collectingAndThen(Collectors.toList(), collectedList -> {
                    Collections.shuffle(collectedList); // shuffle the list
                    return collectedList;
                }));
    }

    private List<AnswerScore> getAnswerScores(Long userId) {
        List<AnswerScore> recommendations = new ArrayList<>();
        recommendations.addAll(mapToRecommendedAnswer(answerRepository.recommendationByViralAnswer(userId), 2.0));
        recommendations.addAll(mapToRecommendedAnswer(answerRepository.recommendationByFollowingUserFeed(userId), 1.8));
        recommendations.addAll(mapToRecommendedAnswer(answerRepository.recommendationByFollowingUserAnswers(userId), 1.5));
        recommendations.addAll(mapToRecommendedAnswer(answerRepository.recommendationByFollowingUserQuestions(userId), 1.2));
        recommendations.addAll(mapToRecommendedAnswer(answerRepository.recommendationByViralAnswerAllTopic(userId), 1.0));
        recommendations= recommendations.stream()
                .sorted((a, b) -> Double.compare(a.getScore(), b.getScore())) // sort
                .distinct() // remove duplicates
                .limit(Utils.MAX_RECOMMENDATION)
                .collect(Collectors.toList());
        // Add recommendations from recent answers in topic (this query doesn't return order_value)
        recommendations.addAll(answerRepository.recommendationByRecentAnswerInTopic(userId).stream()
                .map(answer -> new AnswerScore(answer, answer.getViralPoints()*0.5)) // Assign a lower weight
                .toList());
        return recommendations;
    }

    // add weight for each recommendation type
    private List<AnswerScore> mapToRecommendedAnswer(List<Object[]> results, double weight) {
        return results.stream().map(result -> toAnswerScore(result,weight)).collect(Collectors.toList());
    }

    public AnswerRequest toAnswerDto(Answer answer) {
//        AnswerDto answerDto = AnswerMapper.INSTANCE.toAnswerDTO(answer);
        AnswerRequest answerDto= new AnswerRequest();
        BaseMapper.getBaseDtoAttribute(answerDto,answer);
        return answerDto;
    }
    private AnswerScore toAnswerScore(Object[] objects,Double weight){
        Answer answer  = answerRepository.findById((Long) objects[0]).orElseThrow(()->{throw new DataNotFoundException("Answer not found");});
        Double score = (Double) objects[1] *weight;
        return new AnswerScore(answer,score);
    }

}
