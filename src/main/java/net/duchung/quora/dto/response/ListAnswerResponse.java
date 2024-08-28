package net.duchung.quora.dto.response;

import lombok.AllArgsConstructor;
import net.duchung.quora.dto.AnswerDto;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@AllArgsConstructor
public class ListAnswerResponse {
    List<AnswerDto> answers;
    Long totalPages;
}
