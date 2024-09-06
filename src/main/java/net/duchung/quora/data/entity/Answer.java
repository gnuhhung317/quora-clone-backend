package net.duchung.quora.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.duchung.quora.data.entity.vote.AnswerVote;

import java.util.Set;

@Entity
@Table(name = "answers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SqlResultSetMapping(
        name = "AnswerWithScoreMapping",
        classes = @ConstructorResult(
                targetClass = AnswerScore.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),          // Map Answer ID if needed
                        @ColumnResult(name = "order_value", type = Double.class) // Map the calculated score
                }
        )
)

public class Answer extends BaseEntity {

    private String content;

    private long viralPoints;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id",nullable = false)
    private Question question;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private Set<AnswerVote> votes;
    @OneToMany(mappedBy = "answer", cascade = CascadeType.REMOVE,orphanRemoval = true,fetch = FetchType.LAZY)
    private Set<Comment> comments;

    public void addComment(Comment comment) {
        comment.setAnswer(this);
        comments.add(comment);

    }

}
