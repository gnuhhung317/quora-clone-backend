package net.duchung.quora.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CastVoteResponse {

    private boolean isSuccess;
    private String voteType;
    private String message;

    public void setVoteType(Boolean isUpvote) {
        if(isUpvote==null){
            voteType = "removeVote";
        }
        else if(isUpvote) {
            voteType = "upvote";
        } else{
            voteType = "downvote";
        }
    }

}
