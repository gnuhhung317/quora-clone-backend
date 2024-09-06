package net.duchung.quora.data.response;

import lombok.*;
import net.duchung.quora.data.dto.BaseDto;

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
