package net.duchung.quora.controller;

import net.duchung.quora.data.request.CastVoteRequest;
import net.duchung.quora.data.response.BaseResponse;
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
    public BaseResponse<CastVoteResponse> castVote(@RequestParam("type") String type, @RequestBody CastVoteRequest voteRequest) {
        Boolean isUpvote = voteRequest.getIsUpvote();
        if(type.equals("answer")) {
            return BaseResponse.success(answerVoteService.castVote(voteRequest.getContentId(), isUpvote));
        }else if (type.equals("comment")) {
            return BaseResponse.success(commentVoteService.castVote(voteRequest.getContentId(), isUpvote));
        }
        return BaseResponse.success(new CastVoteResponse(false, null ,"Vote type not found"));
    }

    @GetMapping("/answer/{id}")
    public BaseResponse<VoteStatusResponse> getAnswerVoteStatus(@PathVariable Long id) {
        return BaseResponse.success(answerVoteService.getVotesStatus(id));
    }
    @GetMapping("/comment/{id}")
    public BaseResponse<VoteStatusResponse> getCommentVoteStatus(@PathVariable Long id) {
        return BaseResponse.success(commentVoteService.getVotesStatus(id));
    }

}
