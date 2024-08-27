package net.duchung.quora.service.impl;

import net.duchung.quora.entity.vote.AnswerVote;
import net.duchung.quora.exception.DataNotFoundException;
import net.duchung.quora.repository.AnswerRepository;
import net.duchung.quora.repository.AnswerVoteRepository;
import net.duchung.quora.repository.UserRepository;
import net.duchung.quora.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerVoteServiceImpl implements VoteService
{
    @Autowired
    private AnswerVoteRepository answerVoteRepository;


    @Override
    public void castVote(Long answerId, Long userId, boolean isUpvote) {
        AnswerVote answerVote = answerVoteRepository.findByAnswerIdAndVoterId(answerId, userId).orElseThrow(() -> new DataNotFoundException("AnswerVote not found"));
        if(answerVote.getIsUpvote() != isUpvote) {
            answerVote.setIsUpvote(isUpvote);
            answerVoteRepository.save(answerVote);
        }else {
            answerVoteRepository.delete(answerVote);
        }
    }

    @Override
    public void removeVote(Long answerId, Long userId) {
        answerVoteRepository.deleteByAnswerIdAndVoterId(answerId, userId);
    }

    @Override
    public Integer countUpvotes(Long answerId) {
        return answerVoteRepository.countUpvotesByAnswerId(answerId);
    }

    @Override
    public Integer countDownvotes(Long answerId) {
        return answerVoteRepository.countDownvotesByAnswerId(answerId);
    }

    @Override
    public Boolean getVoteStatus(Long contentId, Long userId) {
        AnswerVote answerVote = answerVoteRepository.findByAnswerIdAndVoterId(contentId, userId).orElseThrow(() -> new DataNotFoundException("AnswerVote not found"));
        return answerVote.getIsUpvote();
    }
}
