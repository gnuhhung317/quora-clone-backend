package net.duchung.quora.mapper;

import net.duchung.quora.dto.AnswerDto;
import net.duchung.quora.entity.Answer;
import net.duchung.quora.entity.Question;
import net.duchung.quora.entity.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.control.MappingControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class AnswerMapperTest {
    private AnswerMapper answerMapper = AnswerMapper.INSTANCE;

    @Test
    void toAnswerDTO() {
        Answer answer = new Answer("content", new Question("haha", new User(),null,null), new User() , new HashSet<>());

        AnswerDto answerDTO = answerMapper.toAnswerDTO(answer);
        System.out.println(answerDTO);
    }

    @Test
    void toAnswer() {
        AnswerDto answerDTO = new AnswerDto();
        Answer answer = answerMapper.toAnswer(answerDTO);
    }
}