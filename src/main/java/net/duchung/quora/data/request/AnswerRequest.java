package net.duchung.quora.data.request;

import lombok.*;
import net.duchung.quora.data.dto.BaseDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AnswerRequest extends BaseDto {
    private String content;

    private Long questionId;


}
