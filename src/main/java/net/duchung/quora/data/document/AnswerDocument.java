package net.duchung.quora.data.document;

import jakarta.persistence.Id;
import lombok.Data;
import net.duchung.quora.data.entity.Answer;
import net.duchung.quora.data.entity.vote.AnswerVote;
import net.duchung.quora.data.response.AnswerInteractionResponse;
import net.duchung.quora.data.response.AnswerResponse;
import net.duchung.quora.data.response.QuestionResponse;
import net.duchung.quora.data.response.UserResponse;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Document(indexName = "answer")
public class AnswerDocument {
    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String content;

    @Field(type = FieldType.Object)
    private QuestionResponse question;

    @Field(type = FieldType.Object)
    private UserResponse author;

    @Field(type = FieldType.Long)
    private long viralPoints;

    @Field(type = FieldType.Object)
    private AnswerInteractionResponse interactions;

    @Field(type = FieldType.Boolean)
    private boolean isUpvoted;

    @Field(type = FieldType.Date)
    private LocalDateTime createdAt;

    @Field(type = FieldType.Date)
    private LocalDateTime updatedAt;



    public AnswerDocument(Answer answer) {
        this.setId(answer.getId());
        this.setContent(answer.getContent());
        this.setViralPoints(answer.getViralPoints());

        UserResponse user = new UserResponse(answer.getUser());

        this.setAuthor(user);

        QuestionResponse question = new QuestionResponse(answer.getQuestion(),null);

        this.setQuestion(question);

        AnswerInteractionResponse interactionDto = new AnswerInteractionResponse();

        interactionDto.setUpvotes(countUpvotes(answer.getVotes()));
        interactionDto.setDownvotes(countDownvotes(answer.getVotes()));
        interactionDto.setCommentCount((long) answer.getComments().size());

        this.setInteractions(interactionDto);





        this.setCreatedAt(answer.getCreatedAt());
        this.setUpdatedAt(answer.getUpdatedAt());

    }
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
