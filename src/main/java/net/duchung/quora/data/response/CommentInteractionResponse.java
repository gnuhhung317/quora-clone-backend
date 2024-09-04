package net.duchung.quora.data.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.duchung.quora.data.entity.vote.CommentVote;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentInteractionResponse {
    long upvote;
    long downvote;
    public CommentInteractionResponse(Set<CommentVote> commentVotes) {
        if(commentVotes == null) return;
        upvote = commentVotes.stream().filter(CommentVote::getIsUpvote).count();
        downvote = commentVotes.size() - upvote;
    }
}
