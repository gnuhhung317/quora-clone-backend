package net.duchung.quora.service.impl;

import jakarta.transaction.Transactional;
import net.duchung.quora.data.entity.User;
import net.duchung.quora.common.exception.AccessDeniedException;
import net.duchung.quora.service.AuthService;
import net.duchung.quora.utils.Utils;
import net.duchung.quora.data.response.CastVoteResponse;
import net.duchung.quora.data.response.VoteStatusResponse;
import net.duchung.quora.data.entity.vote.AnswerVote;
import net.duchung.quora.common.exception.DataNotFoundException;
import net.duchung.quora.repository.AnswerRepository;
import net.duchung.quora.repository.AnswerVoteRepository;
import net.duchung.quora.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AnswerVoteServiceImpl implements VoteService
{
    @Autowired
    private AnswerVoteRepository answerVoteRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private AuthService authService;
    @Override
    @Transactional
    public CastVoteResponse castVote(Long answerId, Boolean isUpvote) {
        User user = authService.getCurrentUser();
        Optional<AnswerVote> answerVoteOpt = answerVoteRepository.findByAnswerIdAndVoterId(answerId, user.getId());
        //insert vote if not exists
        //else update vote
        if(answerVoteOpt.isEmpty()) {
            AnswerVote newAnswerVote = new AnswerVote();
            newAnswerVote.setAnswer(answerRepository.findById(answerId).orElseThrow(() -> new DataNotFoundException("Answer not found")));
            newAnswerVote.setVoter(user);
            newAnswerVote.setIsUpvote(isUpvote);
            //increase viral points
            newAnswerVote.getAnswer().setViralPoints(newAnswerVote.getAnswer().getViralPoints()+ Utils.VOTE_POINTS);
            answerVoteRepository.save(newAnswerVote);

            CastVoteResponse castVoteResponse = new CastVoteResponse();
            castVoteResponse.setVoteType(isUpvote);
            castVoteResponse.setSuccess(true);
            castVoteResponse.setMessage("Vote casted successfully");
            return castVoteResponse;
        }else {
            AnswerVote answerVote = answerVoteOpt.get();
            if(answerVote.getIsUpvote() != isUpvote) {
                answerVote.setIsUpvote(isUpvote);

                // change viral points;
                if(isUpvote) {
                    answerVote.getAnswer().setViralPoints(answerVote.getAnswer().getViralPoints()+ Utils.VOTE_POINTS*2);
                }else {
                    answerVote.getAnswer().setViralPoints(answerVote.getAnswer().getViralPoints()- Utils.VOTE_POINTS*2);
                }

                answerVoteRepository.save(answerVote);

                CastVoteResponse castVoteResponse = new CastVoteResponse();
                castVoteResponse.setVoteType(isUpvote);
                castVoteResponse.setSuccess(true);
                castVoteResponse.setMessage("Vote casted successfully");
                return castVoteResponse;
            }else {

                answerVoteRepository.delete(answerVote);
                //erase viral points
                if(isUpvote) {
                    answerVote.getAnswer().setViralPoints(answerVote.getAnswer().getViralPoints()- Utils.VOTE_POINTS);
                }else {
                    answerVote.getAnswer().setViralPoints(answerVote.getAnswer().getViralPoints()+ Utils.VOTE_POINTS);
                }

                isUpvote=null;
                CastVoteResponse castVoteResponse = new CastVoteResponse();
                castVoteResponse.setVoteType(isUpvote);
                castVoteResponse.setSuccess(true);
                castVoteResponse.setMessage("Vote removed successfully");
                return castVoteResponse;
            }
        }


    }

    @Override
    public void removeVote(Long answerId) {
        Long userId = authService.getCurrentUser().getId();
        AnswerVote answerVote = answerVoteRepository.findByAnswerIdAndVoterId(answerId, userId).orElseThrow(() -> new DataNotFoundException("Vote not found"));
        if(!answerVote.getVoter().getId().equals(userId)) {
           throw new AccessDeniedException("You are not allowed to remove this vote");
        }
        answerVoteRepository.deleteByAnswerIdAndVoterId(answerId, userId);
    }

    @Override
    public Long countUpvotes(Long answerId) {
        return answerVoteRepository.countUpvotesByAnswerId(answerId);
    }

    @Override
    public Long countDownvotes(Long answerId) {
        return answerVoteRepository.countDownvotesByAnswerId(answerId);
    }


    @Override
    public VoteStatusResponse getVotesStatus(Long id) {
        VoteStatusResponse voteStatusResponse = new VoteStatusResponse();
        voteStatusResponse.setVoteType("answerVote");
        voteStatusResponse.setDownvoteCount(countDownvotes(id));
        voteStatusResponse.setUpvoteCount(countUpvotes(id));
        return voteStatusResponse;

    }
}
