package net.duchung.quora.data.response;

import lombok.Data;
import net.duchung.quora.data.dto.BaseDto;
import net.duchung.quora.data.entity.Question;
import net.duchung.quora.data.entity.Topic;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class QuestionResponse extends BaseDto {

    private Long id;
    private String title;
    private UserResponse author;
    private Long followCount;
    private Long answerCount;
    private boolean isFollowed;
    private Set<Long> topicIds;

    public QuestionResponse(Question question, Long userId) {
        this.id = question.getId();
        this.setId(question.getId());
        this.setTitle(question.getTitle());
        UserResponse user = new UserResponse(question.getUser());
        this.setAuthor(user);
        if (question.getFollowQuestions() != null) {
            this.setFollowCount((long) question.getFollowQuestions().size());
            this.setFollowed(question.getFollowQuestions().stream().anyMatch(followQuestion -> followQuestion.getFollower().getId().equals(userId)));
        }else this.setFollowCount(0L);
        if(question.getAnswers() != null) this.setAnswerCount((long) question.getAnswers().size());
        else this.setAnswerCount(0L);
        this.setCreatedAt(question.getCreatedAt());
        this.setUpdatedAt(question.getUpdatedAt());
        this.setTopicIds(question.getTopics().stream().map(Topic::getId).collect(Collectors.toSet()));
        ;
    }
}
