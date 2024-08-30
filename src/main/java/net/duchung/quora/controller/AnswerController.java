package net.duchung.quora.controller;

import net.duchung.quora.dto.AnswerDto;
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
    public ResponseEntity<AnswerDto> getAnswerById(@PathVariable Long id) {
        return ResponseEntity.ok(answerService.getAnswerById(id));
    }

    @PostMapping("")
    public ResponseEntity<AnswerDto> createAnswer(@RequestBody AnswerDto answerDto) {
        return ResponseEntity.ok(answerService.createAnswer(answerDto));
    }
    @PutMapping("/{id}")
    public ResponseEntity<AnswerDto> updateAnswer(@PathVariable Long id, @RequestBody AnswerDto answerDto) {
        return ResponseEntity.ok(answerService.updateAnswer(id, answerDto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnswerById(@PathVariable Long id) {
        answerService.deleteAnswerById(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("")
    public ResponseEntity<List<AnswerDto>> getAnswersByQuestionId(@RequestParam Long questionId) {
        return ResponseEntity.ok(answerService.getAnswersByQuestionId(questionId));
    }
}
