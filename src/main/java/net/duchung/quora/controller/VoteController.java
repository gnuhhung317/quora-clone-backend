package net.duchung.quora.controller;

import net.duchung.quora.data.request.CastVoteRequest;
import net.duchung.quora.data.response.CastVoteResponse;
import net.duchung.quora.data.response.VoteStatusResponse;
import net.duchung.quora.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.base.url}/votes")
public class VoteController {

    @Autowired
    @Qualifier("answerVoteServiceImpl")
    private VoteService answerVoteService;

    @Autowired
    @Qualifier("commentVoteServiceImpl")
    private VoteService commentVoteService;

    @PostMapping("")
    public CastVoteResponse castVote(@RequestParam("type") String type,@RequestBody CastVoteRequest voteRequest) {
        Boolean isUpvote = voteRequest.getIsUpvote();
        if(type.equals("answer")) {
            return answerVoteService.castVote(voteRequest.getContentId(), isUpvote);
        }else if (type.equals("comment")) {
            return commentVoteService.castVote(voteRequest.getContentId(), isUpvote);
        }
        return new CastVoteResponse(false, null ,"Vote type not found");
    }

    @GetMapping("/answer/{id}")
    public VoteStatusResponse getAnswerVoteStatus(@PathVariable Long id) {
        return answerVoteService.getVotesStatus(id);
    }
    @GetMapping("/comment/{id}")
    public VoteStatusResponse getCommentVoteStatus(@PathVariable Long id) {
        return commentVoteService.getVotesStatus(id);
    }

}
