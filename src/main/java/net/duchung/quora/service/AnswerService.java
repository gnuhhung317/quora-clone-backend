package net.duchung.quora.service;

import net.duchung.quora.dto.AnswerDto;
import net.duchung.quora.dto.QuestionDto;
import org.springframework.stereotype.Service;

@Service
public interface AnswerService {
    AnswerDto createAnswer(AnswerDto answerDto);

    AnswerDto updateAnswer(Long id, AnswerDto answerDto);

    void deleteAnswerById(Long id);

    AnswerDto getAnswerById(Long id);
}
