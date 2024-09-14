package net.duchung.quora.data.response;

import lombok.*;
import net.duchung.quora.data.document.AnswerDocument;
import net.duchung.quora.data.dto.BaseDto;
import net.duchung.quora.data.entity.Answer;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AnswerResponse extends BaseDto {
    private String content;

    private QuestionResponse question;

    private UserResponse author;

    private long viralPoints;

    private AnswerInteractionResponse interactions;

    private Boolean isUpvoted;



}
