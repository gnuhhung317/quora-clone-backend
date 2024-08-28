package net.duchung.quora.service.impl;

import jakarta.transaction.Transactional;
import net.duchung.quora.dto.AnswerDto;
import net.duchung.quora.dto.response.UserResponseDto;
import net.duchung.quora.entity.Answer;
import net.duchung.quora.exception.DataNotFoundException;
import net.duchung.quora.mapper.AnswerMapper;
import net.duchung.quora.mapper.BaseMapper;
import net.duchung.quora.repository.AnswerRepository;
import net.duchung.quora.repository.QuestionRepository;
import net.duchung.quora.repository.UserRepository;
import net.duchung.quora.service.AnswerService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;

@Service
public class AnswerServiceImpl implements AnswerService {
    private final static AnswerMapper ANSWER_MAPPER = Mappers.getMapper(AnswerMapper.class);
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    QuestionRepository questionRepository;;
    @Autowired
    UserRepository userRepository;
    @Override
    @Transactional
    public AnswerDto createAnswer(AnswerDto answerDto) {
        if(!questionRepository.existsById(answerDto.getQuestionId())) {
            throw new DataNotFoundException("Question with id "+answerDto.getQuestionId()+" not found");
        }
        if(!userRepository.existsById(answerDto.getUserId())) {
            throw new DataNotFoundException("User with id "+answerDto.getUserId()+" not found");
        }

        Answer answer = new Answer(answerDto.getContent(), 0,questionRepository.findById(answerDto.getQuestionId()).get(), userRepository.findById(answerDto.getUserId()).get(), Collections.emptySet(),Collections.emptySet());

        Answer savedAnswer = answerRepository.save(answer);
        return toDto(savedAnswer);
    }

    @Override
    @Transactional

    public AnswerDto updateAnswer(Long id, AnswerDto answerDto) {

        if(!questionRepository.existsById(answerDto.getQuestionId())) {
            throw new DataNotFoundException("Question with id "+answerDto.getQuestionId()+" not found");
        }
        if(!userRepository.existsById(answerDto.getUserId())) {
            throw new DataNotFoundException("User with id "+answerDto.getUserId()+" not found");
        }
        if(!answerRepository.existsById(id)) {
            throw new DataNotFoundException("Answer with id "+id+" not found");

        }else {
            Answer answer = answerRepository.findById(id).get();

            answer.setContent(answerDto.getContent());
            answer.setQuestion(questionRepository.findById(answerDto.getQuestionId()).get());
            answer.setUser(userRepository.findById(answerDto.getUserId()).get());
            Answer savedAnswer = answerRepository.save(answer);

            return toDto(savedAnswer);
        }
    }

    @Override
    @Transactional

    public void deleteAnswerById(Long id) {
        answerRepository.deleteById(id);
    }

    @Override
    public AnswerDto getAnswerById(Long id) {

        return answerRepository.findById(id).map(this::toDto).orElseThrow(() -> new DataNotFoundException("Answer with id "+id+" not found"));
    }

    private AnswerDto toDto(Answer answer) {
        AnswerDto answerDto = ANSWER_MAPPER.toAnswerDTO(answer);
        BaseMapper.getBaseDtoAttribute(answerDto,answer);
        return answerDto;
    }
    private Answer toEntity(AnswerDto answerDto) {
        Answer answer = ANSWER_MAPPER.toAnswer(answerDto);
        return answer;
    }
}
