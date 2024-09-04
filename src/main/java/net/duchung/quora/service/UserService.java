package net.duchung.quora.service;

import net.duchung.quora.data.request.RegisterRequest;
import net.duchung.quora.data.request.UserRequest;
import net.duchung.quora.data.response.FollowUserResponse;
import net.duchung.quora.data.response.UserProfile;
import net.duchung.quora.data.response.UserResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface UserService {


    UserProfile createUser(RegisterRequest userDto);

    UserProfile updateUser(UserRequest userDto);

    UserProfile getUserById(Long id);
    UserProfile getProfile();

    void deleteUserById(Long id);

    FollowUserResponse follow(Long followingId);
    FollowUserResponse unfollow(Long followingId);




    String uploadAvatar(MultipartFile avatar);

    void followTopics(List<Long> topicIds);

    void unfollowTopics(List<Long> topicIds);

    List<UserResponse> getFollowers(Long userId);

    List<UserResponse> getFollowersByCurrentUser();
    List<UserResponse> getFollowings(Long userId);
    List<UserResponse> getFollowingsByCurrentUser();




}
