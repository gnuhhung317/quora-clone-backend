package net.duchung.quora.repository;

import net.duchung.quora.data.entity.vote.CommentVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentVoteRepository extends JpaRepository<CommentVote, Long> {
    Optional<CommentVote> findByCommentIdAndVoterId(Long commentId,Long userId);
    void deleteByCommentIdAndVoterId(Long commentId,Long userId);


    @Query("SELECT COUNT(cv) FROM CommentVote cv WHERE cv.comment.id = :id AND cv.isUpvote = true")
    Long countUpvotesByCommentId(@Param("id") Long id);

    @Query("SELECT COUNT(cv) FROM CommentVote cv WHERE cv.comment.id = :id AND cv.isUpvote = false")
    Long countDownvotesByCommentId(@Param("id") Long id);

}

