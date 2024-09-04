package net.duchung.quora.controller;

import net.duchung.quora.data.request.CommentRequest;
import net.duchung.quora.data.response.CommentResponse;
import net.duchung.quora.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.base.url}/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("")
    public ResponseEntity<CommentResponse> createComment(@RequestBody CommentRequest commentDto) {
        CommentResponse comment = commentService.createComment(commentDto);
        return ResponseEntity.ok(comment);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> getCommentById(@PathVariable Long id) {
        CommentResponse commentDto = commentService.getCommentById(id);
        return ResponseEntity.ok(commentDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long id, @RequestBody CommentRequest commentDto) {
        CommentResponse comment = commentService.updateComment(id, commentDto);
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommentById(@PathVariable Long id) {
        commentService.deleteCommentById(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping("")
    public ResponseEntity<List<CommentResponse>> getCommentsByAnswerId(@RequestParam Long answerId) {
        List<CommentResponse> commentDtos = commentService.getCommentsByAnswerId(answerId);
        return ResponseEntity.ok(commentDtos);
    }
}
