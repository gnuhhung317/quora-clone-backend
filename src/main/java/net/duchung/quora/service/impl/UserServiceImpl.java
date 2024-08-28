package net.duchung.quora.service.impl;

import jakarta.transaction.Transactional;
import net.duchung.quora.dto.UserDto;
import net.duchung.quora.dto.request.RegisterRequest;
import net.duchung.quora.dto.response.FollowResponse;
import net.duchung.quora.entity.Follow;
import net.duchung.quora.entity.Topic;
import net.duchung.quora.entity.User;
import net.duchung.quora.exception.DataNotFoundException;
import net.duchung.quora.mapper.BaseMapper;
import net.duchung.quora.mapper.UserMapper;
import net.duchung.quora.repository.FollowRepository;
import net.duchung.quora.repository.TopicRepository;
import net.duchung.quora.repository.UserRepository;
import net.duchung.quora.service.UserService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private static final UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private FollowRepository followRepository;
    @Override
    @Transactional
    public UserDto createUser(RegisterRequest registerRequest) {

        if(!registerRequest.getPassword().equals(registerRequest.getRetypePassword()) ){
            throw new RuntimeException("Passwords do not match");
        }
        UserDto userDto = registerRequestToDto(registerRequest);

        if(userRepository.existsByEmail(userDto.getEmail())) {
            throw new DataIntegrityViolationException("Email "+userDto.getEmail()+" already exists");
        }
        User user = toEntity(userDto);
        User savedUser = userRepository.save(user);

        return toDto(savedUser);
    }

    @Override
    @Transactional
    public UserDto updateUser(Long id,UserDto userDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new DataNotFoundException("User not found"));
        user.setFullName(userDto.getFullName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setAvatarUrl(userDto.getAvatarUrl());
        user.setTopics(new HashSet<>(topicRepository.findAllById(userDto.getTopicIds())));
        User savedUser=userRepository.save(user);
        return toDto(savedUser);
    }

    @Override
    @Transactional
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new DataNotFoundException("User not found"));
        return toDto(user);
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public FollowResponse follow(Long followerId, Long followingId) {
        boolean isSuccess = false;

        if (followerId.equals(followingId)) {
            return new FollowResponse(isSuccess,"unfollow", "You cannot follow yourself.");
        }

        User followerOpt = userRepository.findById(followerId).orElseThrow(() -> new DataNotFoundException("User with id " + followerId + " not found"));
        User followingOpt = userRepository.findById(followingId).orElseThrow(() -> new DataNotFoundException("User with id " + followingId + " not found"));

        Optional<Follow> followOpt = followRepository.findByFollowerIdAndFollowingId(followerOpt.getId(), followingOpt.getId()); // <11,4>
        if (followOpt.isPresent()) {
            return new FollowResponse(isSuccess,"follow", "You are already following this user.");
        }else{
            Follow follow = new Follow();
            follow.setFollower(followerOpt);
            follow.setFollowing(followingOpt);
            followRepository.save(follow);
            isSuccess = true;
        }


        FollowResponse followResponse = new FollowResponse();
        followResponse.setType("follow");
        followResponse.setSuccess(isSuccess);
        return followResponse;
    }

    @Override
    public FollowResponse unfollow(Long followerId, Long followingId) {
        boolean isSuccess = false;

        if (followerId.equals(followingId)) {
            return new FollowResponse(isSuccess,"unfollow", "You cannot unfollow yourself.");
        }

        User followerOpt = userRepository.findById(followerId).orElseThrow(() -> new DataNotFoundException("User with id " + followerId + " not found"));
        User followingOpt = userRepository.findById(followingId).orElseThrow(() -> new DataNotFoundException("User with id " + followingId + " not found"));
        Optional<Follow> followOpt = followRepository.findByFollowerIdAndFollowingId(followerOpt.getId(), followingOpt.getId());
        if (followOpt.isEmpty()) {
            return new FollowResponse(isSuccess,"unfollow", "You are not following this user.");
        }else{
            followRepository.deleteById(followOpt.get().getId());
            isSuccess = true;
        }


        FollowResponse followResponse = new FollowResponse();
        followResponse.setType("unfollow");
        followResponse.setSuccess(isSuccess);
        return followResponse;
    }



    @Override
    public void followTopics(Long userId, List<Long> topicIds) {
        for(Long topicId:topicIds) {
            User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException("User not found"));
            Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new DataNotFoundException("Topic not found"));
            user.getTopics().add(topic);
            userRepository.save(user);
        }
    }

    @Override
    public void unfollowTopics(Long userId, List<Long> topicIds) {
        for(Long topicId:topicIds) {
            User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException("User not found"));
            Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new DataNotFoundException("Topic not found"));
            user.getTopics().remove(topic);
            userRepository.save(user);
        }
    }

    public UserDto registerRequestToDto(RegisterRequest registerRequest) {
        UserDto userDto = new UserDto();
        userDto.setFullName(registerRequest.getFullName());
        userDto.setEmail(registerRequest.getEmail());
        userDto.setPassword(registerRequest.getPassword());
        return userDto;
    }
    private UserDto toDto(User user) {
        UserDto userDto = USER_MAPPER.toUserDto(user);
        BaseMapper.getBaseDtoAttribute(userDto, user);
        return userDto;
    }

    private User toEntity(UserDto userDto) {
        User user = USER_MAPPER.toUser(userDto);
        BaseMapper.getBaseEntityAttribute(user, userDto);
        return user;
    }
}
