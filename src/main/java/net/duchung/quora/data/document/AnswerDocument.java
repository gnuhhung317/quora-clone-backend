package net.duchung.quora.data.document;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.Id;
import lombok.*;
import net.duchung.quora.common.utils.LocalDateTimeDeserializer;
import net.duchung.quora.common.utils.LocalDateTimeSerializer;
import net.duchung.quora.data.entity.Answer;
import net.duchung.quora.data.entity.vote.AnswerVote;
import net.duchung.quora.data.response.AnswerInteractionResponse;
import net.duchung.quora.data.response.QuestionResponse;
import net.duchung.quora.data.response.UserResponse;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "answer")
public class AnswerDocument {
    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String content;

    @Field(type = FieldType.Text)
    private String questionContent;



    public AnswerDocument(Answer answer) {
        this.setId(answer.getId());
        this.setContent(answer.getContent());
        this.setQuestionContent(answer.getQuestion().getTitle());
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
