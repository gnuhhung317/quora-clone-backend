package net.duchung.quora.service.impl;

import net.duchung.quora.utils.Utils;
import net.duchung.quora.dto.response.CastVoteResponse;
import net.duchung.quora.dto.response.VoteStatusResponse;
import net.duchung.quora.entity.vote.CommentVote;
import net.duchung.quora.exception.DataNotFoundException;
import net.duchung.quora.repository.CommentRepository;
import net.duchung.quora.repository.CommentVoteRepository;
import net.duchung.quora.repository.UserRepository;
import net.duchung.quora.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentVoteServiceImpl implements VoteService {

    @Autowired
    private CommentVoteRepository commentVoteRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public CastVoteResponse castVote(Long commentId, Long userId, Boolean isUpvote) {
        Optional<CommentVote> commentVoteOpt = commentVoteRepository.findByCommentIdAndVoterId(commentId, userId);
        //insert vote if not exists
        //else update vote
        if (commentVoteOpt.isEmpty()) {
            CommentVote newCommentVote = new CommentVote();
            newCommentVote.setComment(commentRepository.findById(commentId).orElseThrow(() -> new DataNotFoundException("Answer not found")));
            newCommentVote.setVoter(userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException("User not found")));
            newCommentVote.setIsUpvote(isUpvote);
            //increase viral points
            newCommentVote.getComment().setViralPoints(newCommentVote.getComment().getViralPoints() + Utils.VOTE_POINTS);
            commentVoteRepository.save(newCommentVote);

            CastVoteResponse castVoteResponse = new CastVoteResponse();
            castVoteResponse.setVoteType(isUpvote);
            castVoteResponse.setSuccess(true);
            castVoteResponse.setMessage("Vote casted successfully");
            return castVoteResponse;
        } else {
            CommentVote commentVote = commentVoteOpt.get();
            if (commentVote.getIsUpvote() != isUpvote) {

                if(isUpvote) {
                    commentVote.getComment().setViralPoints(commentVote.getComment().getViralPoints() + Utils.VOTE_POINTS * 2);
                } else {
                    commentVote.getComment().setViralPoints(commentVote.getComment().getViralPoints() - Utils.VOTE_POINTS * 2);
                }
                commentVote.setIsUpvote(isUpvote);
                commentVoteRepository.save(commentVote);

                CastVoteResponse castVoteResponse = new CastVoteResponse();
                castVoteResponse.setVoteType(isUpvote);
                castVoteResponse.setSuccess(true);
                castVoteResponse.setMessage("Vote casted successfully");
                return castVoteResponse;
            } else {

                commentVoteRepository.delete(commentVote);
                //erase viral points
                if(isUpvote) {
                    commentVote.getComment().setViralPoints( commentVote.getComment().getViralPoints()- Utils.VOTE_POINTS);
                }else {
                    commentVote.getComment().setViralPoints( commentVote.getComment().getViralPoints()+ Utils.VOTE_POINTS);
                }

                isUpvote = null;
                CastVoteResponse castVoteResponse = new CastVoteResponse();
                castVoteResponse.setVoteType(isUpvote);
                castVoteResponse.setSuccess(true);
                castVoteResponse.setMessage("Vote removed successfully");
                return castVoteResponse;
            }
        }


    }

    @Override
    public void removeVote(Long contentId, Long userId) {
        commentVoteRepository.deleteByCommentIdAndVoterId(contentId, userId);
    }

    @Override
    public Long countUpvotes(Long contentId) {
        return commentVoteRepository.countUpvotesByCommentId(contentId);
    }


    @Override
    public Long countDownvotes(Long contentId) {
        return commentVoteRepository.countDownvotesByCommentId(contentId);
    }

    public VoteStatusResponse getVotesStatus(Long id) {
        VoteStatusResponse voteStatusResponse = new VoteStatusResponse();
        voteStatusResponse.setVoteType("commentVote");
        voteStatusResponse.setDownvoteCount(countDownvotes(id));
        voteStatusResponse.setUpvoteCount(countUpvotes(id));
        return voteStatusResponse;
    }
}