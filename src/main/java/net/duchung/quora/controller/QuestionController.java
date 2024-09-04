package net.duchung.quora.controller;

import net.duchung.quora.data.request.QuestionRequest;
import net.duchung.quora.data.response.FollowQuestionResponse;
import net.duchung.quora.data.response.QuestionResponse;
import net.duchung.quora.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.base.url}/questions")
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @PostMapping("")
    public ResponseEntity<QuestionResponse> createQuestion(@RequestBody QuestionRequest questionDto) {
        QuestionResponse question = questionService.createQuestion(questionDto);
        return ResponseEntity.ok(question);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionResponse> updateQuestion(@PathVariable Long id, @RequestBody QuestionRequest questionDto) {
        QuestionResponse question = questionService.updateQuestion(id, questionDto);
        return ResponseEntity.ok(question);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestionById(@PathVariable Long id) {
        questionService.deleteQuestionById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponse> getQuestionById(@PathVariable Long id) {
        QuestionResponse question = questionService.getQuestionById(id);
        return ResponseEntity.ok(question);
    }
    @GetMapping("")
    public ResponseEntity<List<QuestionResponse>> getQuestionsByUserId(@RequestParam(required = false) Long userId) {
        if(userId == null) {
            return ResponseEntity.ok(questionService.getQuestionsByCurrentUser());
        }
        List<QuestionResponse> questions = questionService.getQuestionsByUserId(userId);
        return ResponseEntity.ok(questions);
    }
    @PostMapping("/follow")
    public ResponseEntity<FollowQuestionResponse> follow(@RequestParam Long questionId) {
        return ResponseEntity.ok(questionService.followQuestion(questionId));
    }
    @PostMapping("/unfollow")
    public ResponseEntity<FollowQuestionResponse> unfollow(@RequestParam Long questionId) {
        return ResponseEntity.ok(questionService.unfollowQuestion(questionId));
    }


}
