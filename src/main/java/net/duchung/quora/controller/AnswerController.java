package net.duchung.quora.controller;

import net.duchung.quora.data.request.AnswerRequest;
import net.duchung.quora.data.response.AnswerResponse;
import net.duchung.quora.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.base.url}/answers")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @GetMapping("/{id}")
    public ResponseEntity<AnswerResponse> getAnswerById(@PathVariable Long id) {
        return ResponseEntity.ok(answerService.getAnswerById(id));
    }

    @PostMapping("")
    public ResponseEntity<AnswerResponse> createAnswer(@RequestBody AnswerRequest answerDto) {
        return ResponseEntity.ok(answerService.createAnswer(answerDto));
    }
    @PutMapping("/{id}")
    public ResponseEntity<AnswerResponse> updateAnswer(@PathVariable Long id, @RequestBody AnswerRequest answerDto) {
        return ResponseEntity.ok(answerService.updateAnswer(id, answerDto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnswerById(@PathVariable Long id) {
        answerService.deleteAnswerById(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/question/{id}")
    public ResponseEntity<List<AnswerResponse>> getAnswersByQuestionId(@PathVariable("id") Long questionId) {
        return ResponseEntity.ok(answerService.getAnswersByQuestionId(questionId));
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<List<AnswerResponse>> getAnswersByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(answerService.getAnswersByUserId(id));
    }
}
