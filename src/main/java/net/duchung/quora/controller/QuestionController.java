package net.duchung.quora.controller;

import net.duchung.quora.dto.QuestionDto;
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
    public ResponseEntity<QuestionDto> createQuestion(@RequestBody QuestionDto questionDto) {
        questionDto = questionService.createQuestion(questionDto);
        return ResponseEntity.ok(questionDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionDto> updateQuestion(@PathVariable Long id, @RequestBody QuestionDto questionDto) {
        QuestionDto question = questionService.updateQuestion(id, questionDto);
        return ResponseEntity.ok(question);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestionById(@PathVariable Long id) {
        questionService.deleteQuestionById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionDto> getQuestionById(@PathVariable Long id) {
        QuestionDto question = questionService.getQuestionById(id);
        return ResponseEntity.ok(question);
    }
    @GetMapping("")
    public ResponseEntity<List<QuestionDto>> getQuestionsByUserId(@RequestParam Long userId) {
        if(userId == null) {
            return ResponseEntity.ok(questionService.getQuestionsByCurrentUser());
        }
        List<QuestionDto> questions = questionService.getQuestionsByUserId(userId);
        return ResponseEntity.ok(questions);
    }


}
