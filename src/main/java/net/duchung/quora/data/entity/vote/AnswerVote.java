package net.duchung.quora.data.entity.vote;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.duchung.quora.data.entity.User;
import net.duchung.quora.data.entity.Answer;
import net.duchung.quora.data.entity.BaseEntity;

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
