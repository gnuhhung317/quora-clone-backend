package net.duchung.quora.data.document;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.duchung.quora.data.entity.FollowUser;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "follow")
public class FollowDocument {

    @Id
    private Long id;
    @Field(type = FieldType.Long)
    private Long followerId;
    @Field(type = FieldType.Long)
    private Long followingId;

    public FollowDocument(FollowUser followUser) {
        this.setId(followUser.getId());
        this.setFollowerId(followUser.getFollower().getId());
        this.setFollowingId(followUser.getFollowing().getId());
    }
}
