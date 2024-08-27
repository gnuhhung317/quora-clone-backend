package net.duchung.quora.service.impl;

import jakarta.transaction.Transactional;
import net.duchung.quora.dto.CommentDto;
import net.duchung.quora.entity.Comment;
import net.duchung.quora.exception.DataNotFoundException;
import net.duchung.quora.mapper.BaseMapper;
import net.duchung.quora.mapper.CommentMapper;
import net.duchung.quora.repository.AnswerRepository;
import net.duchung.quora.repository.CommentRepository;
import net.duchung.quora.repository.UserRepository;
import net.duchung.quora.service.CommentService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {
    private final static CommentMapper COMMENT_MAPPER = Mappers.getMapper(CommentMapper.class);
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    UserRepository userRepository;
    @Override
    @Transactional
    public CommentDto createComment(CommentDto commentDto) {
        if(!userRepository.existsById(commentDto.getUserId())) {
            throw new DataNotFoundException("User with id "+commentDto.getUserId()+" not found");
        }
        if(!answerRepository.existsById(commentDto.getAnswerId())) {
            throw new DataNotFoundException("Answer with id "+commentDto.getAnswerId()+" not found");
        }
        Comment comment = toEntity(commentDto);
        Comment savedComment= commentRepository.save(comment);
        return toDto(savedComment);
    }

    @Override
    @Transactional
    public CommentDto updateComment(Long id,CommentDto commentDto) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Comment with id "+commentDto.getId()+" not found"));
        if(!userRepository.existsById(commentDto.getUserId())) {
            throw new DataNotFoundException("User with id "+commentDto.getUserId()+" not found");
        }
        if(!answerRepository.existsById(commentDto.getAnswerId())) {
            throw new DataNotFoundException("Answer with id "+commentDto.getAnswerId()+" not found");
        }

        comment.setContent(commentDto.getContent());
        Comment savedComment = commentRepository.save(comment);
        return toDto(savedComment);
    }

    @Override
    @Transactional

    public void deleteCommentById(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public CommentDto getCommentById(Long id) {
        return commentRepository.findById(id).map(this::toDto).orElseThrow(() -> new DataNotFoundException("Comment with id "+id+" not found"));
    }
    private CommentDto toDto(Comment comment) {
        CommentDto commentDto=COMMENT_MAPPER.toCommentDto(comment);
        BaseMapper.getBaseDtoAttribute(commentDto,comment);
        return commentDto;
    }
    private Comment toEntity(CommentDto commentDto) {   Comment comment = COMMENT_MAPPER.toComment(commentDto);
//        BaseMapper.getBaseEntityAttribute(comment,commentDto);
        return comment;
    }
}
