package net.duchung.quora.service.impl;

import jakarta.transaction.Transactional;
import net.duchung.quora.data.document.AnswerDocument;
import net.duchung.quora.data.document.FollowDocument;
import net.duchung.quora.data.entity.FollowUser;
import net.duchung.quora.data.request.AnswerRequest;
import net.duchung.quora.data.entity.Answer;
import net.duchung.quora.data.entity.Question;
import net.duchung.quora.data.entity.User;
import net.duchung.quora.common.exception.AccessDeniedException;
import net.duchung.quora.common.exception.DataNotFoundException;
import net.duchung.quora.data.mapper.AnswerMapper;
import net.duchung.quora.data.mapper.BaseMapper;
import net.duchung.quora.data.response.AnswerResponse;
import net.duchung.quora.repository.AnswerRepository;
import net.duchung.quora.repository.QuestionRepository;
import net.duchung.quora.repository.UserFollowRepository;
import net.duchung.quora.repository.elastic.EsAnswerRepository;
import net.duchung.quora.service.AnswerService;
import net.duchung.quora.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AnswerServiceImpl implements AnswerService {
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    UserFollowRepository userFollowRepository;
    @Autowired
    EsAnswerRepository esAnswerRepository;
    @Autowired
    QuestionRepository questionRepository;;

    @Autowired
    AuthService authService;
    @Override
    @Transactional
    public AnswerResponse createAnswer(AnswerRequest answerDto) {

        User user = authService.getCurrentUser();
        Question question = questionRepository.findById(answerDto.getQuestionId()).orElseThrow(() -> new DataNotFoundException("Question with id "+answerDto.getQuestionId()+" not found"));

        Answer answer = new Answer(answerDto.getContent(), 0,question, user, Collections.emptySet(),Collections.emptySet());
        Answer savedAnswer = answerRepository.save(answer);
        return AnswerMapper.toAnswerResponse(savedAnswer, user.getId());
    }

    @Override
    @Transactional

    public AnswerResponse updateAnswer(Long id, AnswerRequest answerDto) {

        User user = authService.getCurrentUser();
        Answer answer = answerRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Answer with id "+id+" not found"));

        if(!answer.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You don't have permission to update this answer");
        }
        answer.setContent(answerDto.getContent());

        Answer savedAnswer = answerRepository.save(answer);

        return AnswerMapper.toAnswerResponse(savedAnswer,user.getId());

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
    public AnswerResponse getAnswerById(Long id) {
        Long userId = authService.getCurrentUser().getId();

        return answerRepository.findById(id).map(answer -> AnswerMapper.toAnswerResponse(answer,userId)).orElseThrow(() -> new DataNotFoundException("Answer with id "+id+" not found"));
    }

    private AnswerRequest toDto(Answer answer) {
        AnswerRequest answerDto =new AnswerRequest();

        BaseMapper.getBaseDtoAttribute(answerDto,answer);
        return answerDto;
    }

    @Override
    public List<AnswerResponse> getAnswersByQuestionId(Long questionId) {
        Long userId = authService.getCurrentUser().getId();
        return answerRepository.findByQuestionId(questionId).stream().map(answer -> AnswerMapper.toAnswerResponse(answer,userId)).toList();
    }

    @Override
    public List<AnswerResponse> getAnswersByUserId(Long userId) {
        Long currentUserId = authService.getCurrentUser().getId();

        return answerRepository.findByUserId(userId).stream().map(answer -> AnswerMapper.toAnswerResponse(answer,currentUserId)).toList();
    }

    @Override
    @Transactional
    public void test() {
//       List<Answer> answers = answerRepository.findAll();

       List<Answer> answers = answerRepository.findAll();
       List<AnswerDocument> answerDocuments = answers.stream().map(answer -> new AnswerDocument(answer)).toList();
       for (AnswerDocument answerDocument : answerDocuments) {
           esAnswerRepository.save(answerDocument);
       }

    }
}
