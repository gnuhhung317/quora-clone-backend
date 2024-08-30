package net.duchung.quora.service.impl;

import jakarta.transaction.Transactional;
import net.duchung.quora.dto.AnswerDto;
import net.duchung.quora.dto.response.UserResponseDto;
import net.duchung.quora.entity.Answer;
import net.duchung.quora.entity.Question;
import net.duchung.quora.entity.User;
import net.duchung.quora.exception.AccessDeniedException;
import net.duchung.quora.exception.DataNotFoundException;
import net.duchung.quora.mapper.AnswerMapper;
import net.duchung.quora.mapper.BaseMapper;
import net.duchung.quora.repository.AnswerRepository;
import net.duchung.quora.repository.QuestionRepository;
import net.duchung.quora.repository.UserRepository;
import net.duchung.quora.service.AnswerService;
import net.duchung.quora.service.AuthService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class AnswerServiceImpl implements AnswerService {
    private final static AnswerMapper ANSWER_MAPPER = Mappers.getMapper(AnswerMapper.class);
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    QuestionRepository questionRepository;;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthService authService;
    @Override
    @Transactional
    public AnswerDto createAnswer(AnswerDto answerDto) {

        User user = authService.getCurrentUser();
        Question question = questionRepository.findById(answerDto.getQuestionId()).orElseThrow(() -> new DataNotFoundException("Question with id "+answerDto.getQuestionId()+" not found"));

        Answer answer = new Answer(answerDto.getContent(), 0,question, user, Collections.emptySet(),Collections.emptySet());
        Answer savedAnswer = answerRepository.save(answer);
        return toDto(savedAnswer);
    }

    @Override
    @Transactional

    public AnswerDto updateAnswer(Long id, AnswerDto answerDto) {

        User user = authService.getCurrentUser();
        Answer answer = answerRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Answer with id "+id+" not found"));

        if(!answer.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You don't have permission to update this answer");
        }
        answer.setContent(answerDto.getContent());

        Answer savedAnswer = answerRepository.save(answer);

        return toDto(savedAnswer);

    }

    @Override
    @Transactional
    public void deleteAnswerById(Long id) {
        Long userId = authService.getCurrentUser().getId();
        Answer answer = answerRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Answer with id "+id+" not found"));
        if(!answer.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You don't have permission to delete this answer");
        }
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

    @Override
    public List<AnswerDto> getAnswersByQuestionId(Long questionId) {
        return answerRepository.findByQuestionId(questionId).stream().map(this::toDto).toList();
    }
}
