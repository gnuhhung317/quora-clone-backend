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
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@Service
@Primary
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

    /**
     * Retrieves recommendation answers for the current user.
     * If the suggestions are “run out,” it repopulates the Redis ZSET.
     */
    @Override
    public List<AnswerResponse> getRecommendationAnswers() {
        Long userId = authService.getCurrentUser().getId();
        Map<Long, Double> recommendations = redisService.getUserScoresSorted(userId);

        if (redisService.isRunOutOfAvailableSuggest(userId)) {
            saveSuggest(userId);
            // Refresh recommendations after repopulating
            recommendations = redisService.getUserScoresSorted(userId);
        }

        return getAnswers(recommendations, userId);
    }

    /**
     * Forces a recalculation of answer suggestions for the current user.
     */
    @Override
    public void calculateAnswerSuggestions() {
        Long userId = authService.getCurrentUser().getId();
        saveSuggest(userId);
    }

    /**
     * Creates/updates the Redis suggestions ZSET for a user.
     */
    public void saveSuggest(Long userId) {
        Map<Long, Double> recommendations = getAnswerScores(userId);
        redisService.saveSuggest(userId, recommendations);
    }

    /**
     * Converts the recommendation map into a list of AnswerResponse objects.
     * Duplicates are removed and the final list is shuffled.
     */
    private List<AnswerResponse> getAnswers(Map<Long, Double> recommendations, Long userId) {
        List<Answer> answers = answerRepository.findAllById(recommendations.keySet());

        return answers.stream()
                .distinct()
                .map(ra -> AnswerMapper.toAnswerResponse(ra, userId))
                .collect(Collectors.collectingAndThen(Collectors.toList(), collectedList -> {
                    Collections.shuffle(collectedList);
                    return collectedList;
                }));
    }

    /**
     * Gathers scoring information from various repository methods for a user’s answers,
     * combining them into a single recommendations map.
     */
    private Map<Long, Double> getAnswerScores(Long userId) {
        Map<Long, Double> recommendations = new HashMap<>();

        BiConsumer<List<Object[]>, Map<Long, Double>> addRecommendations = (results, map) -> {
            for (Object[] objects : results) {
                Long answerId = (Long) objects[0];
                Double score = (Double) objects[1];
                map.putIfAbsent(answerId, score);
            }
        };

        addRecommendations.accept(answerRepository.recommendationByFollowQuestions(userId), recommendations);
        addRecommendations.accept(answerRepository.recommendationByViralAnswer(userId), recommendations);
        addRecommendations.accept(answerRepository.recommendationByFollowingUserAnswers(userId), recommendations);
        addRecommendations.accept(answerRepository.recommendationByFollowingUserQuestions(userId), recommendations);
        addRecommendations.accept(answerRepository.recommendationByViralAnswerAllTopic(userId), recommendations);
        addRecommendations.accept(answerRepository.recommendationByFollowingUserFeed(userId), recommendations);

        // If we have too few recommendations, add recent answers with a default score of 0.
        if (recommendations.size() < 20) {
            answerRepository.recommendationByRecentAnswerInTopic(userId)
                    .forEach(ans -> recommendations.putIfAbsent(((Answer) ans).getId(), 0.0));
        }

        return recommendations;
    }

    /**
     * Retrieves recommendation questions for the current user.
     */
    @Override
    public List<QuestionResponse> getRecommendationQuestions() {
        Long userId = authService.getCurrentUser().getId();
        List<Question> questions = questionRepository.findBySuggestQuestion(userId);
        return questions.stream()
                .map(question -> new QuestionResponse(question, userId))
                .collect(Collectors.toList());
    }

    @Override
    public void calculateQuestionSuggestions() {
        // Implementation pending
    }
}