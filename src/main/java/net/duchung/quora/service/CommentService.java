package net.duchung.quora.service;

import net.duchung.quora.dto.CommentDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {
    CommentDto createComment(CommentDto commentDto);
    CommentDto updateComment(Long id,CommentDto commentDto);
    void deleteCommentById(Long id);
    CommentDto getCommentById(Long id);

    List<CommentDto> getCommentsByAnswerId(Long answerId);
}
