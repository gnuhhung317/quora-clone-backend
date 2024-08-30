package net.duchung.quora.controller;

import net.duchung.quora.dto.AnswerDto;
import net.duchung.quora.dto.ViewDto;
import net.duchung.quora.service.RecommendationService;
import net.duchung.quora.service.ViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    @GetMapping("")
    public ResponseEntity<List<AnswerDto>> getRecommendation() {
        List<AnswerDto> answers = recommendationService.getRecommendationAnswers(102L);
        for (AnswerDto answer : answers){
            viewService.logView(new ViewDto(answer.getId(),102L,1d));
        }

        return ResponseEntity.ok(answers);
    }
}
