package net.duchung.quora.service.impl;

import jakarta.transaction.Transactional;
import net.duchung.quora.data.request.RegisterRequest;
import net.duchung.quora.data.request.UserRequest;
import net.duchung.quora.data.response.FollowUserResponse;
import net.duchung.quora.data.entity.FollowUser;
import net.duchung.quora.data.entity.Topic;
import net.duchung.quora.data.entity.User;
import net.duchung.quora.common.exception.DataNotFoundException;
import net.duchung.quora.data.mapper.UserMapper;
import net.duchung.quora.data.response.UserProfile;
import net.duchung.quora.data.response.UserResponse;
import net.duchung.quora.repository.UserFollowRepository;
import net.duchung.quora.repository.TopicRepository;
import net.duchung.quora.repository.UserRepository;
import net.duchung.quora.service.AuthService;
import net.duchung.quora.service.UserService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private static final UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class);
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserFollowRepository followRepository;
    @Autowired
    private AuthService authService;
    @Override
    @Transactional
    public UserProfile createUser(RegisterRequest registerRequest) {

        if(!registerRequest.getPassword().equals(registerRequest.getRetypePassword()) ){
            throw new RuntimeException("Passwords do not match");
        }

        if(userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new DataIntegrityViolationException("Email "+registerRequest.getEmail()+" already exists");
        }
        User user=new User();

        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        user.setFullName(registerRequest.getFullName());


        User savedUser = userRepository.save(user);

        return new UserProfile(savedUser);
    }

    @Override
    @Transactional
    public UserProfile updateUser(UserRequest userDto) {
        User user = authService.getCurrentUser();
        user.setFullName(userDto.getFullName());
        user.setEmail(userDto.getEmail());
        user.setAvatarUrl(userDto.getAvatarUrl());
        User savedUser=userRepository.save(user);
        return new UserProfile(savedUser);
    }

    @Override
    public UserProfile getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new DataNotFoundException("User not found"));
        return new UserProfile(user);
    }

    @Override
    public UserProfile getProfile() {
        return new UserProfile(authService.getCurrentUser());
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public FollowUserResponse follow(Long followingId) {
        User user = authService.getCurrentUser();
        Long followerId = user.getId();
        boolean isSuccess = false;

        if (followerId.equals(followingId)) {
            return new FollowUserResponse(isSuccess,"unfollow", "You cannot follow yourself.");
        }

        User following = userRepository.findById(followingId).orElseThrow(() -> new DataNotFoundException("User with id " + followingId + " not found"));

        Optional<FollowUser> followOpt = followRepository.findByFollowerIdAndFollowingId(user.getId(), following.getId()); // <11,4>
        if (followOpt.isPresent()) {
            return new FollowUserResponse(isSuccess,"follow", "You are already following this user.");
        }else{
            FollowUser follow = new FollowUser();
            follow.setFollower(user);
            follow.setFollowing(following);
            followRepository.save(follow);
            isSuccess = true;
        }


        FollowUserResponse followResponse = new FollowUserResponse();
        followResponse.setType("follow");
        followResponse.setSuccess(isSuccess);
        return followResponse;
    }

    @Override
    public FollowUserResponse unfollow(Long followingId) {
        boolean isSuccess = false;
        User user = authService.getCurrentUser();
        Long followerId = user.getId();
        if (followerId.equals(followingId)) {
            return new FollowUserResponse(isSuccess,"unfollow", "You cannot unfollow yourself.");
        }

        User followingOpt = userRepository.findById(followingId).orElseThrow(() -> new DataNotFoundException("User with id " + followingId + " not found"));
        Optional<FollowUser> followOpt = followRepository.findByFollowerIdAndFollowingId(followerId, followingOpt.getId());
        if (followOpt.isEmpty()) {
            return new FollowUserResponse(isSuccess,"unfollow", "You are not following this user.");
        }else{
            followRepository.deleteById(followOpt.get().getId());
            isSuccess = true;
        }


        FollowUserResponse followResponse = new FollowUserResponse();
        followResponse.setType("unfollow");
        followResponse.setSuccess(isSuccess);
        return followResponse;
    }



    @Override
    public void followTopics(List<Long> topicIds) {
        User user = authService.getCurrentUser();
        for(Long topicId:topicIds) {
            Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new DataNotFoundException("Topic with id "+topicId+" not found"));
            user.getTopics().add(topic);
        }
        userRepository.save(user);

    }

    @Override
    public void unfollowTopics(List<Long> topicIds) {
        User user = authService.getCurrentUser();
        for(Long topicId:topicIds) {
            Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new DataNotFoundException("Topic not found"));
            user.getTopics().remove(topic);

        }
        userRepository.save(user);
    }

    @Override
    public List<UserResponse> getFollowers(Long userId) {

        return followRepository.getUserByFollowerId(userId).stream().map(UserResponse::new).collect(Collectors.toList());
    }

    @Override
    public List<UserResponse> getFollowings(Long userId) {
        return followRepository.getUserByFollowingId(userId).stream().map(UserResponse::new).collect(Collectors.toList());
    }

    @Override
    public List<UserResponse> getFollowersByCurrentUser() {
        User user = authService.getCurrentUser();
        return followRepository.getUserByFollowerId(user.getId()).stream().map(UserResponse::new).collect(Collectors.toList());
    }

    @Override
    public List<UserResponse> getFollowingsByCurrentUser() {
        User user = authService.getCurrentUser();
        return followRepository.getUserByFollowingId(user.getId()).stream().map(UserResponse::new).collect(Collectors.toList());
    }




    @Override
    @Transactional
    public String uploadAvatar(MultipartFile avatar) {

        String url =cloudinaryService.uploadImage(avatar).get("secure_url").toString();

        User user = authService.getCurrentUser();
        user.setAvatarUrl(url);
        userRepository.save(user);
        return url;

    }


    @Override
    public List<Long> getUserIds() {
        return userRepository.getIds();
    }
}
