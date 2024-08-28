package net.duchung.quora.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.duchung.quora.entity.vote.AnswerVote;
import net.duchung.quora.entity.vote.CommentVote;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User extends BaseEntity {

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
    private Set<Follow> followings;

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL)
    private Set<Follow> followers;


    @OneToMany(mappedBy = "voter",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<AnswerVote> answerVotes;
    @OneToMany(mappedBy = "voter",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<CommentVote> commentVotes;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Question> questions;


}
