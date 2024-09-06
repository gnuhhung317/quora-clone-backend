package net.duchung.quora.data.mapper;

import net.duchung.quora.data.response.AnswerInteractionResponse;
import net.duchung.quora.data.entity.Answer;
import net.duchung.quora.data.entity.vote.AnswerVote;
import net.duchung.quora.data.response.AnswerResponse;
import net.duchung.quora.data.response.QuestionResponse;
import net.duchung.quora.data.response.UserResponse;

import java.util.Set;


public class AnswerMapper {

    public static AnswerResponse toAnswerResponse(Answer answer, Long currentUserId) {
        AnswerResponse answerResponse = new AnswerResponse();
        answerResponse.setId(answer.getId());
        answerResponse.setContent(answer.getContent());
        answerResponse.setViralPoints(answer.getViralPoints());

        UserResponse user = new UserResponse(answer.getUser());

        answerResponse.setAuthor(user);

        QuestionResponse question = new QuestionResponse(answer.getQuestion(),currentUserId);

        answerResponse.setQuestion(question);

        AnswerInteractionResponse interactionDto = new AnswerInteractionResponse();

        interactionDto.setUpvotes(countUpvotes(answer.getVotes()));
        interactionDto.setDownvotes(countDownvotes(answer.getVotes()));
        interactionDto.setCommentCount((long) answer.getComments().size());

        answerResponse.setInteractions(interactionDto);


        for (AnswerVote answerVote : answer.getVotes()) {
            if (answerVote.getVoter().getId().equals(currentUserId)) {
                answerResponse.setIsUpvoted(answerVote.getIsUpvote());

            }
        }


        answerResponse.setCreatedAt(answer.getCreatedAt());
        answerResponse.setUpdatedAt(answer.getUpdatedAt());

        return answerResponse;
    }

    // Phương thức tùy chỉnh để đếm upvotes
    public static Long countUpvotes(Set<AnswerVote> votes) {
        if (votes == null) return 0L;
        return votes.stream().filter(AnswerVote::getIsUpvote).count();
    }

    // Phương thức tùy chỉnh để đếm downvotes
    public static Long countDownvotes(Set<AnswerVote> votes) {
        if (votes == null) return 0L;
        return votes.stream().filter(vote -> !vote.getIsUpvote()).count();
    }
}

