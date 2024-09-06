package net.duchung.quora.data.response;

import lombok.*;
@Data
public class AnswerInteractionResponse {
    private Long upvotes;
    private Long downvotes;
    private Long commentCount;
}
