package net.duchung.quora.data.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ListAnswerResponse {
    List<AnswerResponse> answers;
    Long totalPages;
}
