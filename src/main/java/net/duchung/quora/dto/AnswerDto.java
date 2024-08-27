package net.duchung.quora.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AnswerDto extends BaseDto {
    private String content;

    private Long questionId;

    private String questionTitle;

    private Long userId;

    private String userFullName;
    private String userAvatarUrl;

    private Long upvotes;

    private Long downvotes;

}
