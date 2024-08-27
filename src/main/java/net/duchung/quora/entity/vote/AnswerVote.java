package net.duchung.quora.entity.vote;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.duchung.quora.entity.Answer;
import net.duchung.quora.entity.BaseEntity;
import net.duchung.quora.entity.User;

@Entity
@Table(name = "answer_votes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnswerVote extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User voter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id")
    private Answer answer;

    private Boolean isUpvote;

}
