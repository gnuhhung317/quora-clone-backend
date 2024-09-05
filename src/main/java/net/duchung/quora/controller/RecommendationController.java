package net.duchung.quora.controller;

import net.duchung.quora.data.response.AnswerResponse;
import net.duchung.quora.data.response.QuestionResponse;
import net.duchung.quora.service.RecommendationService;
import net.duchung.quora.service.ViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.base.url}/recommendation")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;
    @Autowired
    private ViewService viewService;
    @GetMapping("/answer")
    public ResponseEntity<List<AnswerResponse>> getRecommendation() {
        List<AnswerResponse> answers = recommendationService.getRecommendationAnswers();
//        for (AnswerResponse answer : answers){
//            viewService.logView(new ViewDto(answer.getId(),1d));
//        }

        return ResponseEntity.ok(answers);
    }
    @PostMapping("")
    public ResponseEntity<Object> calculateSuggestion() {
        recommendationService.calculateAnswerSuggestions();
        return ResponseEntity.noContent().build();

    }
    @GetMapping("/question")
    public ResponseEntity<List<QuestionResponse>> getRecommendationQuestions() {
        List<QuestionResponse> questions = recommendationService.getRecommendationQuestions();
        return ResponseEntity.ok(questions);
    }

}
