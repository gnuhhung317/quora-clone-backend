package net.duchung.quora.controller;

import net.duchung.quora.dto.UserDto;
import net.duchung.quora.dto.request.RegisterRequest;
import net.duchung.quora.dto.response.FollowResponse;
import net.duchung.quora.entity.User;
import net.duchung.quora.service.FileService;
import net.duchung.quora.service.UserService;
import net.duchung.quora.service.impl.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("${api.base.url}/users")
public class UserController {

    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private UserService userService;
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    @PostMapping("")
    public ResponseEntity<UserDto> createUser(@RequestBody RegisterRequest registerRequest) {

        UserDto user = userService.createUser(registerRequest);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        UserDto user = userService.updateUser(id, userDto);
        return ResponseEntity.ok(user);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/follow/{followerId}/{followingId}")
    public ResponseEntity<FollowResponse> follow(@PathVariable Long followerId, @PathVariable Long followingId) {
        FollowResponse followResponse = userService.follow(followerId, followingId);


        return ResponseEntity.ok(followResponse);
    }
    @PostMapping("/unfollow/{followerId}/{followingId}")
    public ResponseEntity<FollowResponse> unfollow(@PathVariable Long followerId, @PathVariable Long followingId) {
        FollowResponse followResponse = userService.unfollow(followerId, followingId);

        return ResponseEntity.ok(followResponse);
    }
    @PostMapping("/{id}/followTopics")
    public ResponseEntity<String> chooseTopic(@PathVariable Long id,@RequestParam List<Long> topicIds) {
        userService.followTopics(id,topicIds);
        return ResponseEntity.ok("Follow successfully");
    }
    @PostMapping("/{id}/unfollowTopics")
    public ResponseEntity<String> unfollowTopics(@PathVariable Long id,@RequestParam List<Long> topicIds) {
        userService.unfollowTopics(id,topicIds);
        return ResponseEntity.ok("Unfollow successfully");
    }

    @PostMapping("upload/avatar")
    public ResponseEntity<String> uploadAvatar(@RequestBody MultipartFile avatar) {
        String secureUrl =userService.uploadAvatar(avatar);
        return ResponseEntity.ok(secureUrl);
    }


}
