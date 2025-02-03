package net.duchung.quora.repository;

import net.duchung.quora.data.entity.vote.CommentVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CommentVoteRepository extends JpaRepository<CommentVote, Long> {
    Optional<CommentVote> findByCommentIdAndVoterId(Long commentId,Long userId);
    void deleteByCommentIdAndVoterId(Long commentId,Long userId);

    @Query("SELECT cv FROM CommentVote cv WHERE cv.comment.id in :ids and cv.isUpvote = true and cv.voter.id = :userId")
    Set<CommentVote> findAllByUserCommentVote(@Param("ids") List<Long> ids, @Param("userId") Long userId);

    @Query("SELECT COUNT(cv) FROM CommentVote cv WHERE cv.comment.id = :id AND cv.isUpvote = true")
    Long countUpvotesByCommentId(@Param("id") Long id);

    @Query("SELECT COUNT(cv) FROM CommentVote cv WHERE cv.comment.id = :id AND cv.isUpvote = false")
    Long countDownvotesByCommentId(@Param("id") Long id);

}

