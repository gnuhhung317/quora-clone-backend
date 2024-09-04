package net.duchung.quora.data.response;

import lombok.*;
import net.duchung.quora.data.entity.FollowUser;
import net.duchung.quora.data.entity.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String fullName;
    private String avatarUrl;
    private boolean isFollowed;

    public UserResponse(User user){
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.avatarUrl = user.getAvatarUrl();
        for (FollowUser follower : user.getFollowers()) {
            if (follower.getFollower().getId().equals(user.getId())) {
                this.isFollowed = true;
                break;
            }
        }
    }

}
