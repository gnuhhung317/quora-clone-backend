package net.duchung.quora.data.response;

import lombok.*;
@Data
public class QuestionInteractionResponse {
    private Long upvotes;
    private Long downvotes;
    private Long commentCount;
}
