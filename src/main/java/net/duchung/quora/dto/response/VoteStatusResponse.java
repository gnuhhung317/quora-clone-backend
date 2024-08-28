package net.duchung.quora.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoteStatusResponse {

    private String voteType;
    private Long upvoteCount;
    private Long downvoteCount;
}
