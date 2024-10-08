package net.duchung.quora.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "questions")
public class Question extends BaseEntity {


    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "question_topic",
    joinColumns = @JoinColumn(name = "question_id"),
    inverseJoinColumns = @JoinColumn(name = "topic_id"))
    private Set<Topic> topics;

    @OneToMany (mappedBy = "question",cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Answer> answers;

    @OneToMany(mappedBy = "question",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    private Set<FollowQuestion> followQuestions;


}
