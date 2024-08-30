package net.duchung.quora.service.impl;

import com.fasterxml.jackson.databind.ser.Serializers;
import jakarta.transaction.Transactional;
import net.duchung.quora.dto.QuestionDto;
import net.duchung.quora.entity.Question;
import net.duchung.quora.entity.Topic;
import net.duchung.quora.entity.User;
import net.duchung.quora.exception.AccessDeniedException;
import net.duchung.quora.exception.DataNotFoundException;
import net.duchung.quora.mapper.BaseMapper;
import net.duchung.quora.mapper.QuestionMapper;
import net.duchung.quora.repository.QuestionRepository;
import net.duchung.quora.repository.TopicRepository;
import net.duchung.quora.repository.UserRepository;
import net.duchung.quora.service.AuthService;
import net.duchung.quora.service.QuestionService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class QuestionServiceImpl implements QuestionService {
    private final static QuestionMapper QUESTION_MAPPER = Mappers.getMapper(QuestionMapper.class);

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private AuthService authService;
    @Override
    @Transactional
    public QuestionDto createQuestion(QuestionDto questionDto) {
        User user = authService.getCurrentUser();
        //        List<Topic> topics = topicRepository.findAllById(questionDto.getTopicIds());
        Question question = QUESTION_MAPPER.toQuestion(questionDto);
        question.setUser(user);
        Question savedQuestion = questionRepository.save(question);

        return toDto(savedQuestion);
    }

    @Override
    @Transactional
    public QuestionDto updateQuestion(Long id,QuestionDto questionDto) {
        User user = authService.getCurrentUser();
        Question question = questionRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Question with id "+questionDto.getId()+" not found"));
        if(!user.getId().equals(question.getUser().getId())) {
            throw new AccessDeniedException("You don't have permission to update this question");
        }
        question.setTitle(questionDto.getTitle());
        question.setTopics(new HashSet<>(topicRepository.findAllById(questionDto.getTopicIds())));
        Question savedQuestion = questionRepository.save(question);

        return toDto(savedQuestion);
    }

    @Override
    @Transactional
    public void deleteQuestionById(Long id) {
        User user = authService.getCurrentUser();
        Question question = questionRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Question with id "+id+" not found"));
        if(!user.getId().equals(question.getUser().getId())) {
            throw new AccessDeniedException("You don't have permission to delete this question");
        }
        questionRepository.deleteById(id);
    }

    @Override
    public QuestionDto getQuestionById(Long id) {
        Question question = questionRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Question with id "+id+" not found"));
        return toDto(question);
    }

    @Override
    public List<QuestionDto> getQuestionsByUserId(Long id) {
        return questionRepository.findByUserId(id).stream().map(this::toDto).toList();
    }

    @Override
    public List<QuestionDto> getQuestionsByCurrentUser() {
        User user=authService.getCurrentUser();
        return questionRepository.findByUserId(user.getId()).stream().map(this::toDto).toList();
    }

    public QuestionDto toDto(Question question) {
        QuestionDto questionDto = QUESTION_MAPPER.toQuestionDto(question);
        BaseMapper.getBaseDtoAttribute(questionDto, question);
        return questionDto;
    }

}
