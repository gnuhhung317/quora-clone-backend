package net.duchung.quora.service;

import net.duchung.quora.data.request.CommentRequest;
import net.duchung.quora.data.response.CommentResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {
    CommentResponse createComment(CommentRequest commentDto);
    CommentResponse updateComment(Long id,CommentRequest commentDto);
    void deleteCommentById(Long id);
    CommentResponse getCommentById(Long id);

    List<CommentResponse> getCommentsByAnswerId(Long answerId);
}
