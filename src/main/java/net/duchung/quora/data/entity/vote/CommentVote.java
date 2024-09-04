package net.duchung.quora.data.entity.vote;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.duchung.quora.data.entity.User;
import net.duchung.quora.data.entity.BaseEntity;
import net.duchung.quora.data.entity.Comment;

@Entity
@Table(name = "comment_votes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentVote extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User voter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    private Boolean isUpvote;
}

