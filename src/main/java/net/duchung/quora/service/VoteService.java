package net.duchung.quora.service;

import net.duchung.quora.data.response.CastVoteResponse;
import net.duchung.quora.data.response.VoteStatusResponse;
import org.springframework.stereotype.Service;

@Service
public interface VoteService {
    CastVoteResponse castVote(Long contentId, Boolean isUpvote);

    void removeVote(Long contentId);

    Long countUpvotes(Long contentId);
    Long countDownvotes(Long contentId);

    VoteStatusResponse getVotesStatus(Long id);

}
