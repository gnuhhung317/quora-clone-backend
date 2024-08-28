package net.duchung.quora.service;

import net.duchung.quora.dto.UserDto;
import net.duchung.quora.dto.request.RegisterRequest;
import net.duchung.quora.dto.response.FollowResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    UserDto createUser(RegisterRequest userDto);

    UserDto updateUser(Long id,UserDto userDto);

    UserDto getUserById(Long id);

    void deleteUserById(Long id);

    FollowResponse follow(Long followerId, Long followingId);
    FollowResponse unfollow(Long followerId, Long followingId);


    void followTopics(Long userId, List<Long> topicIds);
    void unfollowTopics(Long userId, List<Long> topicIds);
}
