package net.duchung.quora.controller;

import net.duchung.quora.dto.UserDto;
import net.duchung.quora.dto.request.RegisterRequest;
import net.duchung.quora.dto.response.FollowResponse;
import net.duchung.quora.entity.User;
import net.duchung.quora.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.base.url}/users")
public class UserController {

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
        boolean result = userService.follow(followerId, followingId);
        FollowResponse followResponse = new FollowResponse();
        followResponse.setType("follow");
        followResponse.setSuccess(result);

        return ResponseEntity.ok(followResponse);
    }
    @PostMapping("/unfollow/{followerId}/{followingId}")
    public ResponseEntity<FollowResponse> unfollow(@PathVariable Long followerId, @PathVariable Long followingId) {
        boolean result = userService.unfollow(followerId, followingId);
        FollowResponse followResponse = new FollowResponse();
        followResponse.setType("unfollow");
        followResponse.setSuccess(result);
        return ResponseEntity.ok(followResponse);
    }
    @PostMapping("/followTopics")
    public ResponseEntity<String> chooseTopic(@RequestParam Long id,@RequestParam List<Long> topicIds) {
        userService.followTopics(id,topicIds);
        return ResponseEntity.ok("Follow successfully");
    }
    @PostMapping("/unfollowTopics")
    public ResponseEntity<String> unfollowTopics(@RequestParam Long id,@RequestParam List<Long> topicIds) {
        userService.unfollowTopics(id,topicIds);
        return ResponseEntity.ok("Unfollow successfully");
    }

}
