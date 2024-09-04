package net.duchung.quora.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.duchung.quora.data.entity.vote.AnswerVote;
import net.duchung.quora.data.entity.vote.CommentVote;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User extends BaseEntity {

    @Column(unique = true)
    private String email;
    private String password;
    private String fullName;
    private String avatarUrl;

    @ManyToMany
    @JoinTable(name = "user_topic",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "topic_id"))
    private Set<Topic> topics;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL)
    private Set<FollowUser> followings;

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL)
    private Set<FollowUser> followers;


    @OneToMany(mappedBy = "voter",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    private Set<AnswerVote> answerVotes;
    @OneToMany(mappedBy = "voter",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    private Set<CommentVote> commentVotes;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    private Set<Answer> answers;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    private Set<Question> questions;

    @OneToMany(mappedBy = "follower",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    private Set<FollowQuestion> followQuestions;


}
