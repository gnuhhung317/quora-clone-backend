package net.duchung.quora.repository;

import net.duchung.quora.entity.vote.AnswerVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnswerVoteRepository extends JpaRepository<AnswerVote, Long> {

    @Query("SELECT COUNT(av) FROM AnswerVote av WHERE av.answer.id = :id AND av.isUpvote = true")
    Integer countUpvotesByAnswerId(@Param("id") Long id);

    @Query("SELECT COUNT(av) FROM AnswerVote av WHERE av.answer.id = :id AND av.isUpvote = false")
    Integer countDownvotesByAnswerId(@Param("id") Long id);

    Optional<AnswerVote> findByAnswerIdAndVoterId(Long answerId, Long userId);
    void deleteByAnswerIdAndVoterId(Long answerId, Long userId);

}
