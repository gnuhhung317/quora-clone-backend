package net.duchung.quora.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.duchung.quora.data.dto.InteractionRecord;
import net.duchung.quora.data.entity.Answer;
import net.duchung.quora.data.mapper.AnswerMapper;
import net.duchung.quora.data.response.AnswerResponse;
import net.duchung.quora.data.response.QuestionResponse;
import net.duchung.quora.repository.AnswerRepository;
import net.duchung.quora.service.AuthService;
import net.duchung.quora.service.RecommendationService;
import net.duchung.quora.service.RedisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SvdRecommendationServiceImpl implements RecommendationService {

    private final ObjectMapper jacksonObjectMapper;
    @Value("${recommendation.serviceUrl}")
    private String recommendationServiceUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    ;
    private final AuthService authService;
    private final RedisService redisService;
    private final AnswerRepository answerRepository;
    private final InteractionDataServiceImpl interactionDataService;

    @Override
    public List<AnswerResponse> getRecommendationAnswers() {
        Long userId = authService.getCurrentUser().getId();
        Map<Long, Double> recommendations = redisService.getUserScoresSorted(userId);
        return getAnswers(recommendations, userId);
    }

    private void saveSuggest(Long userId) {

    }

    @Override
//    @Scheduled(cron = "*/10 * * * * *")
    public void calculateAnswerSuggestions() {
        List<InteractionRecord> records = interactionDataService.getInteractionData();

        try {
            // Create a map to hold the data
            Map<String, List<InteractionRecord>> payloadMap = new HashMap<>();
            payloadMap.put("data", records);

            // Convert the map to JSON
            String payload = jacksonObjectMapper.writeValueAsString(payloadMap);
            String url = recommendationServiceUrl + "/train";

            // Create HttpClient and HttpRequest
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response: " + response.body());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<QuestionResponse> getRecommendationQuestions() {
        return List.of();
    }

    @Override
    public void calculateQuestionSuggestions() {

    }

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
}
