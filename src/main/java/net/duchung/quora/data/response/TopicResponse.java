package net.duchung.quora.data.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.duchung.quora.data.entity.Topic;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TopicResponse {
    private Long id;
    private String name;
    private int questionCount;
    private int followerCount;

    public TopicResponse(Topic topic, int questionCount, int followerCount) {
        this.id = topic.getId();
        this.name = topic.getName();
        this.questionCount = questionCount;
        this.followerCount = followerCount;

    }

}
