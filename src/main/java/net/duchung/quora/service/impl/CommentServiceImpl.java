package net.duchung.quora.service.impl;

import jakarta.transaction.Transactional;
import net.duchung.quora.common.exception.AccessDeniedException;
import net.duchung.quora.data.request.CommentRequest;
import net.duchung.quora.data.response.CommentResponse;
import net.duchung.quora.service.AuthService;
import net.duchung.quora.utils.Utils;
import net.duchung.quora.data.entity.Answer;
import net.duchung.quora.data.entity.Comment;
import net.duchung.quora.data.entity.User;
import net.duchung.quora.common.exception.DataNotFoundException;
import net.duchung.quora.data.mapper.CommentMapper;
import net.duchung.quora.repository.AnswerRepository;
import net.duchung.quora.repository.CommentRepository;
import net.duchung.quora.repository.UserRepository;
import net.duchung.quora.service.CommentService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public CommentResponse createComment(CommentRequest commentDto) {
        User user = authService.getCurrentUser();
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
        return new CommentResponse(savedComment);
    }

    @Override
    @Transactional
    public CommentResponse updateComment(Long id, CommentRequest commentDto) {

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
        return new CommentResponse(savedComment);
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
    public CommentResponse getCommentById(Long id) {
        return commentRepository.findById(id).map(CommentResponse::new).orElseThrow(() -> new DataNotFoundException("Comment with id "+id+" not found"));
    }

    @Override
    public List<CommentResponse> getCommentsByAnswerId(Long answerId) {
        return commentRepository.findByAnswerId(answerId).stream().map(CommentResponse::new).toList();
    }




}
