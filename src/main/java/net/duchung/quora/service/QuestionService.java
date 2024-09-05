package net.duchung.quora.service;

import net.duchung.quora.data.request.QuestionRequest;
import net.duchung.quora.data.response.FollowQuestionResponse;
import net.duchung.quora.data.response.QuestionResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuestionService {

    QuestionResponse createQuestion(QuestionRequest questionDto);
    QuestionResponse updateQuestion(Long id, QuestionRequest questionDto);
    void deleteQuestionById(Long id);
    QuestionResponse getQuestionById(Long id);

    List<QuestionResponse> getQuestionsByUserId(Long id);

    List<QuestionResponse> getQuestionsByCurrentUser();

    FollowQuestionResponse followQuestion(Long id);

    FollowQuestionResponse unfollowQuestion(Long id);

    void test();

}
