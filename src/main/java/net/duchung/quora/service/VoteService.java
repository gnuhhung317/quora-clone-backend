package net.duchung.quora.service;

import net.duchung.quora.dto.response.CastVoteResponse;
import net.duchung.quora.dto.response.VoteStatusResponse;
import org.springframework.stereotype.Service;

@Service
public interface VoteService {
    CastVoteResponse castVote(Long contentId, Long userId, Boolean isUpvote);

    void removeVote(Long contentId, Long userId);

    Long countUpvotes(Long contentId);
    Long countDownvotes(Long contentId);

    VoteStatusResponse getVotesStatus(Long id);

}
