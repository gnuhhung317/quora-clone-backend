package net.duchung.quora.data.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FollowUserResponse {

    private boolean isSuccess;

    private String type;

    private String message;

    public void setSuccess(boolean isSuccess) {
        if(type.equals("follow")) {
            if(isSuccess) {
                message = "Followed successfully";
            } else {
                message = "Can't follow this user";
            }
        } else if (type.equals("unfollow")) {
            if(isSuccess) {
                message = "Unfollowed successfully";
            } else {
                message = "Some errors happened when unfollow";
            }
        }
        this.isSuccess = isSuccess;
    }

}
