//package net.duchung.quora.data.document;
//
//import jakarta.persistence.Id;
//import lombok.Data;
//import net.duchung.quora.data.entity.FollowUser;
//import net.duchung.quora.data.entity.User;
//import org.springframework.data.elasticsearch.annotations.Document;
//import org.springframework.data.elasticsearch.annotations.Field;
//import org.springframework.data.elasticsearch.annotations.FieldType;
//
//@Data
//@Document(indexName = "user")
//public class UserDocument {
//    @Id
//    private Long id;
//    @Field(type = FieldType.Text)
//    private String fullName;
//    private String avatarUrl;
//    private boolean isFollowed;
//
//    public UserDocument(User user) {
//        this.id = user.getId();
//        this.fullName = user.getFullName();
//        this.avatarUrl = user.getAvatarUrl();
//        for (FollowUser follower : user.getFollowers()) {
//            if (follower.getFollower().getId().equals(user.getId())) {
//                this.isFollowed = true;
//                break;
//            }
//        }
//    }
//}
