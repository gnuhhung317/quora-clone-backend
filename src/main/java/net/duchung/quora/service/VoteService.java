package net.duchung.quora.service;

import org.springframework.stereotype.Service;

@Service
public interface VoteService {
    void castVote(Long contentId, Long userId, boolean isUpvote);

    void removeVote(Long contentId, Long userId);

    Integer countUpvotes(Long contentId);
    Integer countDownvotes(Long contentId);

    Boolean getVoteStatus(Long contentId, Long userId);

}
