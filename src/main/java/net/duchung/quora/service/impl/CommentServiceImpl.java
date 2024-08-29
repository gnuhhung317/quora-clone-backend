package net.duchung.quora.service.impl;

import jakarta.transaction.Transactional;
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
    @Override
    @Transactional
    public CommentDto createComment(CommentDto commentDto) {
//        Comment comment = toEntity(commentDto);
        Optional<User> userOpt = userRepository.findById(commentDto.getUserId());
        Optional<Answer> answerOpt = answerRepository.findById(commentDto.getAnswerId());

        if(userOpt.isEmpty()) {
            throw new DataNotFoundException("User with id "+commentDto.getUserId()+" not found");
        }
        if(answerOpt.isEmpty()) {
            throw new DataNotFoundException("Answer with id "+commentDto.getAnswerId()+" not found");
        }
        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        if(commentDto.getParentId() != null) {
            Comment parentComment = commentRepository.findById(commentDto.getParentId()).orElseThrow(() -> new DataNotFoundException("Comment with id "+commentDto.getParentId()+" not found"));
            comment.setParentComment(parentComment);
        }
        User user = userOpt.get();
        Answer answer = answerOpt.get();

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
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Comment with id "+commentDto.getId()+" not found"));
        Optional<User> userOpt = userRepository.findById(comment.getUser().getId());
        Optional<Answer> answerOpt = answerRepository.findById(comment.getAnswer().getId());

        if(userOpt.isEmpty()) {
            throw new DataNotFoundException("User with id "+commentDto.getUserId()+" not found");
        }
        if(answerOpt.isEmpty()) {
            throw new DataNotFoundException("Answer with id "+commentDto.getAnswerId()+" not found");
        }

        User user = userOpt.get();
        Answer answer = answerOpt.get();



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
        Comment comment =commentRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Comment with id "+id+" not found"));

        commentRepository.deleteById(id);

        Answer answer = comment.getAnswer();
        answer.setViralPoints(answer.getViralPoints()- Utils.COMMENT_POINTS);
        answerRepository.save(answer);
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
    private Comment toEntity(CommentDto commentDto) {
        Comment comment = COMMENT_MAPPER.toComment(commentDto);

        return comment;
    }

}
