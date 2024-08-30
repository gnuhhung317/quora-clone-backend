package net.duchung.quora.service.impl;

import jakarta.transaction.Transactional;
import net.duchung.quora.exception.AccessDeniedException;
import net.duchung.quora.service.AuthService;
import net.duchung.quora.service.UserService;
import net.duchung.quora.utils.Utils;
import net.duchung.quora.dto.CommentDto;
import net.duchung.quora.entity.Answer;
import net.duchung.quora.entity.Comment;
import net.duchung.quora.entity.User;
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

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    private final static CommentMapper COMMENT_MAPPER = Mappers.getMapper(CommentMapper.class);
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthService authService;
    @Override
    @Transactional
    public CommentDto createComment(CommentDto commentDto) {
//        Comment comment = toEntity(commentDto);
        User user = userRepository.findById(commentDto.getUserId()).orElseThrow(() -> new DataNotFoundException("User with id "+commentDto.getUserId()+" not found"));
        Answer answer = answerRepository.findById(commentDto.getAnswerId()).orElseThrow(() -> new DataNotFoundException("Answer with id "+commentDto.getAnswerId()+" not found"));



        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        if(commentDto.getParentId() != null) {
            Comment parentComment = commentRepository.findById(commentDto.getParentId()).orElseThrow(() -> new DataNotFoundException("Comment with id "+commentDto.getParentId()+" not found"));
            comment.setParentComment(parentComment);
        }


        answer.setViralPoints(answer.getViralPoints()+Utils.VOTE_POINTS);
        Answer savedAnswer = answerRepository.save(answer);

        comment.setUser(user);
        comment.setAnswer(savedAnswer);

        Comment savedComment= commentRepository.save(comment);
        return toDto(savedComment);
    }

    @Override
    @Transactional
    public CommentDto updateComment(Long id,CommentDto commentDto) {

        Comment comment = commentRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Comment with id "+id+" not found"));
        User user = authService.getCurrentUser();
        Answer answer = answerRepository.findById(comment.getAnswer().getId()).orElseThrow(() -> new DataNotFoundException("Answer with id "+comment.getAnswer().getId()+" not found"));

        if(!comment.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You don't have permission to update this comment");
        }

        answer.setViralPoints(answer.getViralPoints()+Utils.VOTE_POINTS);
        Answer savedAnswer = answerRepository.save(answer);

        comment.setAnswer(savedAnswer);
        comment.setContent(commentDto.getContent());
        comment.setUser(user);

        Comment savedComment = commentRepository.save(comment);
        return toDto(savedComment);
    }

    @Override
    @Transactional
    public void deleteCommentById(Long id) {
        Long userId = authService.getCurrentUser().getId();

        Comment comment =commentRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Comment with id "+id+" not found"));
        if(!userId.equals(comment.getUser().getId())) {
            throw new AccessDeniedException("You don't have permission to delete this comment");
        }
        commentRepository.deleteById(id);

        Answer answer = comment.getAnswer();
        answer.setViralPoints(answer.getViralPoints()- Utils.COMMENT_POINTS);
        answerRepository.save(answer);
    }

    @Override
    public CommentDto getCommentById(Long id) {
        return commentRepository.findById(id).map(this::toDto).orElseThrow(() -> new DataNotFoundException("Comment with id "+id+" not found"));
    }

    @Override
    public List<CommentDto> getCommentsByAnswerId(Long answerId) {
        return commentRepository.findByAnswerId(answerId).stream().map(this::toDto).toList();
    }

    private CommentDto toDto(Comment comment) {
        CommentDto commentDto=COMMENT_MAPPER.toCommentDto(comment);
        BaseMapper.getBaseDtoAttribute(commentDto,comment);
        return commentDto;
    }
    private Comment toEntity(CommentDto commentDto) {
        Comment comment = COMMENT_MAPPER.toComment(commentDto);

        return comment;
    }

}
