package net.duchung.quora.controller;

import net.duchung.quora.data.request.LoginRequest;
import net.duchung.quora.data.request.RegisterRequest;
import net.duchung.quora.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.base.url}/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {

        return ResponseEntity.ok(authService.login(loginRequest.getEmail(), loginRequest.getPassword()));
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }
    @GetMapping("/register/verify")
    public ResponseEntity<String> verify(@RequestParam("code") String code) {
        return ResponseEntity.ok(authService.verify(code));
    }

}
