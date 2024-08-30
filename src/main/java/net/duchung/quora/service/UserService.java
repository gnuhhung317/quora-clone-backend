package net.duchung.quora.service;

import net.duchung.quora.dto.QuestionDto;
import net.duchung.quora.dto.UserDto;
import net.duchung.quora.dto.request.RegisterRequest;
import net.duchung.quora.dto.response.FollowResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface UserService {


    UserDto createUser(RegisterRequest userDto);

    UserDto updateUser(UserDto userDto);

    UserDto getUserById(Long id);

    void deleteUserById(Long id);

    FollowResponse follow( Long followingId);
    FollowResponse unfollow( Long followingId);




    String uploadAvatar(MultipartFile avatar);

    void followTopics(List<Long> topicIds);

    void unfollowTopics(List<Long> topicIds);

    List<UserDto> getFollowers(Long userId);

    List<UserDto> getFollowersByCurrentUser();
    List<UserDto> getFollowings(Long userId);
    List<UserDto> getFollowingsByCurrentUser();




}
