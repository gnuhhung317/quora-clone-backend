package net.duchung.quora.service.impl;

import jakarta.transaction.Transactional;
import net.duchung.quora.dto.QuestionDto;
import net.duchung.quora.entity.Question;
import net.duchung.quora.entity.Topic;
import net.duchung.quora.exception.DataNotFoundException;
import net.duchung.quora.mapper.QuestionMapper;
import net.duchung.quora.repository.QuestionRepository;
import net.duchung.quora.repository.TopicRepository;
import net.duchung.quora.repository.UserRepository;
import net.duchung.quora.service.QuestionService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
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

    @Override
    @Transactional
    public QuestionDto createQuestion(QuestionDto questionDto) {

        if(!userRepository.existsById(questionDto.getUserId())){

            throw new DataNotFoundException("User with id "+" not found");
        }
//        List<Topic> topics = topicRepository.findAllById(questionDto.getTopicIds());
        Question question = QUESTION_MAPPER.toQuestion(questionDto);

        Question savedQuestion = questionRepository.save(question);

        return QUESTION_MAPPER.toQuestionDto(savedQuestion);
    }

    @Override
    @Transactional
    public QuestionDto updateQuestion(Long id,QuestionDto questionDto) {
        Question question = questionRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Question with id "+questionDto.getId()+" not found"));

        question.setTitle(questionDto.getTitle());
        question.setTopics(new HashSet<>(topicRepository.findAllById(questionDto.getTopicIds())));
        return QUESTION_MAPPER.toQuestionDto(questionRepository.save(question));
    }

    @Override
    @Transactional
    public void deleteQuestionById(Long id) {
questionRepository.deleteById(id);
    }

    @Override
    public QuestionDto getQuestionById(Long id) {
        Question question = questionRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Question with id "+id+" not found"));
        return QUESTION_MAPPER.toQuestionDto(question);
    }
}
