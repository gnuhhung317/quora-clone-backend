package net.duchung.quora.service.impl;

import net.duchung.quora.entity.User;
import net.duchung.quora.exception.AccessDeniedException;
import net.duchung.quora.service.AuthService;
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

    @Autowired
    private AuthService authService;

    @Override
    public CastVoteResponse castVote(Long commentId,  Boolean isUpvote) {
        User user = authService.getCurrentUser();

        Optional<CommentVote> commentVoteOpt = commentVoteRepository.findByCommentIdAndVoterId(commentId, user.getId());
        //insert vote if not exists
        //else update vote
        if (commentVoteOpt.isEmpty()) {

            CommentVote newCommentVote = new CommentVote();
            newCommentVote.setComment(commentRepository.findById(commentId).orElseThrow(() -> new DataNotFoundException("Answer not found")));
            newCommentVote.setVoter(user);
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
    public void removeVote(Long contentId) {
        User user = authService.getCurrentUser();
        CommentVote commentVote = commentVoteRepository.findByCommentIdAndVoterId(contentId, user.getId()).orElseThrow(() -> new DataNotFoundException("Vote not found"));
        if (!commentVote.getVoter().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not allowed to remove this vote");
        }
        commentVoteRepository.deleteByCommentIdAndVoterId(contentId, user.getId());
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