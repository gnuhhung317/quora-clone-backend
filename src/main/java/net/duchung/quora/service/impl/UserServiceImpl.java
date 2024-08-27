package net.duchung.quora.service.impl;

import jakarta.transaction.Transactional;
import net.duchung.quora.dto.UserDto;
import net.duchung.quora.dto.request.RegisterRequest;
import net.duchung.quora.entity.Topic;
import net.duchung.quora.entity.User;
import net.duchung.quora.exception.DataNotFoundException;
import net.duchung.quora.mapper.BaseMapper;
import net.duchung.quora.mapper.UserMapper;
import net.duchung.quora.repository.TopicRepository;
import net.duchung.quora.repository.UserRepository;
import net.duchung.quora.service.UserService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static final UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;
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
        user.setAvatarUrl(user.getAvatarUrl());
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
    public Boolean follow(Long followerId, Long followingId) {
        if(userRepository.existsById(followerId) && userRepository.existsById(followingId)) {
            if(followerId.equals(followingId)) {
                return false;
            }
            User user = userRepository.findById(followerId).get();
            User following = userRepository.findById(followingId).get();
            user.getFollowings().add(following);
            following.getFollowers().add(user);
            userRepository.save(user);
            userRepository.save(following);
            return true;
        }
        return null;
    }

    @Override
    public Boolean unfollow(Long followerId, Long followingId) {
        if(userRepository.existsById(followerId) && userRepository.existsById(followingId)) {
            if(followerId.equals(followingId)) {
                return false;
            }
            User user = userRepository.findById(followerId).get();
            User following = userRepository.findById(followingId).get();
            user.getFollowings().remove(following);
            following.getFollowers().remove(user);
            userRepository.save(user);
            userRepository.save(following);
            return true;
        }
        return null;
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
