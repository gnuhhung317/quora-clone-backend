package net.duchung.quora.controller;

import net.duchung.quora.data.request.RegisterRequest;
import net.duchung.quora.data.request.UserRequest;
import net.duchung.quora.data.response.FollowUserResponse;
import net.duchung.quora.data.response.UserProfile;
import net.duchung.quora.data.response.UserResponse;
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
    @GetMapping("")
    public ResponseEntity<UserProfile> getUserById(@RequestParam(required = false) Long id) {
        if(id == null) {

            return ResponseEntity.ok(userService.getProfile());
        }
        UserProfile user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    @PostMapping("")
    public ResponseEntity<UserProfile> createUser(@RequestBody RegisterRequest registerRequest) {

        UserProfile user = userService.createUser(registerRequest);
        return ResponseEntity.ok(user);
    }

    @PutMapping("")
    public ResponseEntity<UserProfile> updateUser( @RequestBody UserRequest userDto) {
        UserProfile user = userService.updateUser( userDto);
        return ResponseEntity.ok(user);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/follow/{followingId}")
    public ResponseEntity<FollowUserResponse> follow(@PathVariable Long followingId) {
        FollowUserResponse followResponse = userService.follow(followingId);


        return ResponseEntity.ok(followResponse);
    }
    @PostMapping("/unfollow/{followingId}")
    public ResponseEntity<FollowUserResponse> unfollow(@PathVariable Long followingId) {
        FollowUserResponse followResponse = userService.unfollow( followingId);

        return ResponseEntity.ok(followResponse);
    }
    @PostMapping("/followTopics")
    public ResponseEntity<String> chooseTopic(@RequestParam List<Long> topicIds) {
        userService.followTopics(topicIds);
        return ResponseEntity.ok("Follow successfully");
    }
    @PostMapping("/unfollowTopics")
    public ResponseEntity<String> unfollowTopics(@RequestParam List<Long> topicIds) {
        userService.unfollowTopics(topicIds);
        return ResponseEntity.ok("Unfollow successfully");
    }

    @PostMapping("upload/avatar")
    public ResponseEntity<String> uploadAvatar(@RequestBody MultipartFile avatar) {
        String secureUrl =userService.uploadAvatar(avatar);
        return ResponseEntity.ok(secureUrl);
    }

    @GetMapping("/followers")
    public ResponseEntity<List<UserResponse>> getFollowers(@RequestParam(required = false) Long userId) {
        if(userId == null) {
            return ResponseEntity.ok(userService.getFollowersByCurrentUser());
        }
        List<UserResponse> userDtos = userService.getFollowers(userId);
        return ResponseEntity.ok(userDtos);
    }
    @GetMapping("/followings")
    public ResponseEntity<List<UserResponse>> getFollowings(@RequestParam(required = false) Long userId) {
        if(userId == null) {
            return ResponseEntity.ok(userService.getFollowingsByCurrentUser());
        }
        List<UserResponse> userDtos = userService.getFollowings(userId);
        return ResponseEntity.ok(userDtos);
    }


}
