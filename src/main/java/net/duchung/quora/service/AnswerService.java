package net.duchung.quora.service;

import net.duchung.quora.data.request.AnswerRequest;
import net.duchung.quora.data.response.AnswerResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AnswerService {
    AnswerResponse createAnswer(AnswerRequest answerDto);

    AnswerResponse updateAnswer(Long id, AnswerRequest answerDto);

    void deleteAnswerById(Long id);

    AnswerResponse getAnswerById(Long id);
    List<AnswerResponse> getAnswersByQuestionId(Long questionId);

    List<AnswerResponse> getAnswersByUserId(Long userId);

//    void test();
}
