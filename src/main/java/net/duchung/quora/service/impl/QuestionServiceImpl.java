package net.duchung.quora.service.impl;

import jakarta.transaction.Transactional;
import net.duchung.quora.common.exception.AccessDeniedException;
import net.duchung.quora.common.exception.DataNotFoundException;
import net.duchung.quora.data.entity.FollowQuestion;
import net.duchung.quora.data.entity.Question;
import net.duchung.quora.data.entity.User;
import net.duchung.quora.data.request.QuestionRequest;
import net.duchung.quora.data.response.FollowQuestionResponse;
import net.duchung.quora.data.response.QuestionResponse;
import net.duchung.quora.repository.QuestionFollowRepository;
import net.duchung.quora.repository.QuestionRepository;
import net.duchung.quora.repository.TopicRepository;
import net.duchung.quora.repository.UserRepository;
import net.duchung.quora.service.AuthService;
import net.duchung.quora.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionFollowRepository questionFollowRepository;
    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private AuthService authService;
    @Override
    @Transactional
    public QuestionResponse createQuestion(QuestionRequest questionRequest) {
        User user = authService.getCurrentUser();

        Question question = new Question();
        question.setTitle(questionRequest.getTitle());
        question.setTopics(new HashSet<>(topicRepository.findAllById(questionRequest.getTopicIds())));

        question.setUser(user);
        Question savedQuestion = questionRepository.save(question);

        return new QuestionResponse(savedQuestion, user.getId());
    }

    @Override
    @Transactional
    public QuestionResponse updateQuestion(Long id,QuestionRequest questionDto) {
        User user = authService.getCurrentUser();
        Question question = questionRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Question with id "+id+" not found"));
        if(!user.getId().equals(question.getUser().getId())) {
            throw new AccessDeniedException("You don't have permission to update this question");
        }
        question.setTitle(questionDto.getTitle());
        question.setTopics(new HashSet<>(topicRepository.findAllById(questionDto.getTopicIds())));
        Question savedQuestion = questionRepository.save(question);

        return new QuestionResponse(savedQuestion, user.getId());
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
    public QuestionResponse getQuestionById(Long id) {
        Long userId = authService.getCurrentUser().getId();
        Question question = questionRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Question with id "+id+" not found"));
        return new QuestionResponse(question,userId);
    }

    @Override
    public List<QuestionResponse> getQuestionsByUserId(Long id) {
        return questionRepository.findByUserId(id).stream().map(question -> new QuestionResponse(question, id)).toList();
    }

    @Override
    public List<QuestionResponse> getQuestionsByCurrentUser() {
        User user=authService.getCurrentUser();
        return questionRepository.findByUserId(user.getId()).stream().map(question -> new QuestionResponse(question, user.getId())).toList();
    }

    @Override
    @Transactional
    public FollowQuestionResponse followQuestion(Long id) {
        User user = authService.getCurrentUser();
        Long followerId = user.getId();

        Question question = questionRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Question with id "+id+" not found"));

        if(question.getUser().getId().equals(followerId)) {
            return new FollowQuestionResponse(false,"follow", "You cannot follow your own question");
        }
        Optional<FollowQuestion> followQuestionOpt = questionFollowRepository.findByFollowerIdAndQuestionId(followerId, id);

        if(followQuestionOpt.isEmpty()) {
            FollowQuestion followQuestion = new FollowQuestion();
            followQuestion.setFollower(user);
            followQuestion.setQuestion(question);
            questionFollowRepository.save(followQuestion);
        }else {
            return new FollowQuestionResponse(false,"follow", "You have already followed this question");
        }
        FollowQuestionResponse followQuestionResponse = new FollowQuestionResponse();
        followQuestionResponse.setType("follow");
        followQuestionResponse.setSuccess(true);
        return followQuestionResponse;
    }



    @Override
    @Transactional
    public FollowQuestionResponse unfollowQuestion(Long id) {
        User user = authService.getCurrentUser();
        Question question = questionRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Question with id "+id+" not found"));

        if(question.getUser().getId().equals(user.getId())) {
            return new FollowQuestionResponse(false,"unfollow", "You cannot unfollow your own question");
        }
        Optional<FollowQuestion> followQuestionOpt = questionFollowRepository.findByFollowerIdAndQuestionId(user.getId(), id);
        if(followQuestionOpt.isPresent()) {
            questionFollowRepository.deleteById(followQuestionOpt.get().getId());
            return new FollowQuestionResponse(true,"unfollow", "You have unfollowed this question");
        }else {
            return new FollowQuestionResponse(false,"unfollow", "You have not followed this question");
        }

    }
    @Override
    public void test(){
        List<Question> questions = questionRepository.findByUserId(123l);
        for (Question question : questions){
            question.getAnswers();
        }

    }


}
