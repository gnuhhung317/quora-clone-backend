package net.duchung.quora.controller;

import net.duchung.quora.dto.UserDto;
import net.duchung.quora.dto.request.RegisterRequest;
import net.duchung.quora.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.base.url}/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDto userDto) {

        return ResponseEntity.ok(authService.login(userDto.getEmail(), userDto.getPassword()));
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }
}
