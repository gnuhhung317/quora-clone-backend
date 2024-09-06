package net.duchung.quora.data.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.duchung.quora.data.dto.BaseDto;
import net.duchung.quora.data.entity.Comment;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse extends BaseDto {

    private String content;
    private UserResponse author;
    private Long answerId;
    private CommentInteractionResponse interaction;

    public CommentResponse(Comment comment) {
        this.setId(comment.getId());
        this.setCreatedAt(comment.getCreatedAt());
        this.setUpdatedAt(comment.getUpdatedAt());
        this.content = comment.getContent();
        this.author = new UserResponse(comment.getUser());
        this.answerId = comment.getUser().getId();
        this.interaction = new CommentInteractionResponse(comment.getVotes());
    }
}
