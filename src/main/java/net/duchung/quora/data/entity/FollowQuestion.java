package net.duchung.quora.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "follow_question",uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id","following_id"}))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FollowQuestion{
    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;
    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime followedAt;

}
