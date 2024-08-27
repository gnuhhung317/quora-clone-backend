package net.duchung.quora.entity.vote;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.duchung.quora.entity.Answer;
import net.duchung.quora.entity.BaseEntity;
import net.duchung.quora.entity.Comment;
import net.duchung.quora.entity.User;
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

