package net.duchung.quora.data.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FollowQuestionResponse {
    private boolean isSuccess;

    private String type;

    private String message;

    public void setSuccess(boolean isSuccess) {
        if(type.equals("follow")) {
            if(isSuccess) {
                message = "Followed successfully";
            } else {
                message = "Can't follow this question";
            }
        } else if (type.equals("unfollow")) {
            if(isSuccess) {
                message = "Unfollowed successfully";
            } else {
                message = "Some errors happened when unfollow this question";
            }
        }
        this.isSuccess = isSuccess;
    }

}
