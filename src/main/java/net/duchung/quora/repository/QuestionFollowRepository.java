package net.duchung.quora.repository;

import net.duchung.quora.data.entity.FollowQuestion;
import net.duchung.quora.data.entity.Question;
import net.duchung.quora.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionFollowRepository extends JpaRepository<FollowQuestion, Long> {
    Optional<FollowQuestion> findByFollowerIdAndQuestionId(Long followerId, Long questionId);
}
