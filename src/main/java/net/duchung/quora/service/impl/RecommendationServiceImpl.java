package net.duchung.quora.service.impl;

import net.duchung.quora.data.entity.Answer;
import net.duchung.quora.data.entity.Question;
import net.duchung.quora.data.mapper.AnswerMapper;
import net.duchung.quora.data.response.AnswerResponse;
import net.duchung.quora.data.response.QuestionResponse;
import net.duchung.quora.repository.AnswerRepository;
import net.duchung.quora.repository.AnswerVoteRepository;
import net.duchung.quora.repository.QuestionRepository;
import net.duchung.quora.service.AuthService;
import net.duchung.quora.service.RecommendationService;
import net.duchung.quora.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@Service
public class RecommendationServiceImpl implements RecommendationService {
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerVoteRepository answerVoteRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private RedisService redisService;

    @Override
    public List<AnswerResponse> getRecommendationAnswers() {
        Long userId = authService.getCurrentUser().getId();

        Map<Long, Double> recommendations = redisService.getUserScoresSorted(userId).stream().collect(Collectors.toMap(x -> (Long) x.getValue(), x -> x.getScore()));
        if(recommendations.containsValue(-1.0)) saveSuggest(userId);
        return getAnswers(recommendations,userId);
    }

    @Override
    public void calculateAnswerSuggestions() {
        Long userId = authService.getCurrentUser().getId();
        saveSuggest(userId);
    }


    public void saveSuggest(Long userId) {
        Map<Long, Double> recommendations = getAnswerScores(userId);
        redisService.saveSuggest(userId, recommendations);
    }
    private List<AnswerResponse> getAnswers(Map<Long, Double> recommendations, Long userId) {
        List<Answer> answers = answerRepository.findAllById(recommendations.keySet());
        //  by weighted order value, remove duplicates, and limit to MAX_RECOMMENDATION
        return answers.stream()
                .distinct() // remove duplicates
                .map(ra -> AnswerMapper.toAnswerResponse(ra,userId))
                .collect(Collectors.collectingAndThen(Collectors.toList(), collectedList -> {
                    Collections.shuffle(collectedList); // shuffle the list
                    return collectedList;
                }));
    }

    private  Map<Long, Double> getAnswerScores(Long userId) {
        Map<Long, Double> recommendations = new HashMap<>();
        // Helper function to reduce repetition
        BiConsumer<List<Object[]>, Map<Long, Double>> addRecommendations = (results, map) -> {
            for (Object[] objects : results) {
                map.putIfAbsent((Long) objects[0], (Double) objects[1]);
            }
        };

        // Collect results from repository calls
        addRecommendations.accept(answerRepository.recommendationByFollowQuestions(userId), recommendations);
        addRecommendations.accept(answerRepository.recommendationByViralAnswer(userId), recommendations);
        addRecommendations.accept(answerRepository.recommendationByFollowingUserAnswers(userId), recommendations);
        addRecommendations.accept(answerRepository.recommendationByFollowingUserQuestions(userId), recommendations);
        addRecommendations.accept(answerRepository.recommendationByViralAnswerAllTopic(userId), recommendations);

        return recommendations;
    }

    @Override
    public List<QuestionResponse> getRecommendationQuestions() {
        Long userId = authService.getCurrentUser().getId();
        List<Question> questions = questionRepository.findBySuggestQuestion(userId);
        return         questions.stream().map(question -> new QuestionResponse(question,userId)).collect(Collectors.toList());

    }

    @Override
    public void calculateQuestionSuggestions() {

    }
}
