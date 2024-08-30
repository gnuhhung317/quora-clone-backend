package net.duchung.quora.service;

import net.duchung.quora.dto.QuestionDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuestionService {

    QuestionDto createQuestion(QuestionDto questionDto);
    QuestionDto updateQuestion(Long id, QuestionDto questionDto);
    void deleteQuestionById(Long id);
    QuestionDto getQuestionById(Long id);

    List<QuestionDto> getQuestionsByUserId(Long id);

    List<QuestionDto> getQuestionsByCurrentUser();

}
