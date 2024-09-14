package net.duchung.quora.service.impl;

import net.duchung.quora.common.exception.DataNotFoundException;
import net.duchung.quora.data.entity.VerificationToken;
import net.duchung.quora.data.request.RegisterRequest;
import net.duchung.quora.data.entity.User;
import net.duchung.quora.repository.UserRepository;
import net.duchung.quora.repository.VerificationTokenRepository;
import net.duchung.quora.service.AuthService;
import net.duchung.quora.common.security.jwt.JwtUtil;
import net.duchung.quora.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private MailService mailService;


    @Override
    public String login(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email or password incorrect"));
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Email or password incorrect");
        } else if (!user.isActive()) {
            throw new DataNotFoundException("User is not active");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password, Collections.emptySet());
        authenticationManager.authenticate(authenticationToken);
        return jwtUtil.generateToken(user);
    }

    @Override
    public String register(RegisterRequest registerRequest) {
        String email = registerRequest.getEmail();
        String password = registerRequest.getPassword();
        String retypePassword = registerRequest.getRetypePassword();
        String fullName = registerRequest.getFullName();
        if(!password.equals(retypePassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        if(userRepository.existsByEmail(email)) {
            throw new DataIntegrityViolationException("Email already exists");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFullName(fullName);
        User savedUser = userRepository.save(user);
        VerificationToken verificationToken = new VerificationToken(UUID.randomUUID().toString(),savedUser);
        verificationTokenRepository.save(verificationToken);
        mailService.sendVerificationLinkToEmail(email, verificationToken.getToken());
        return "User " + savedUser.getEmail() + " registered successfully! Please check your email for verification.";
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email or password incorrect"));
    }

    @Override
    public String verify(String code) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(code);
        if(verificationToken == null) {
            throw new DataNotFoundException("Verification token not found");
        }
        User user = verificationToken.getUser();
        user.setActive(true);
        userRepository.save(user);
        verificationTokenRepository.delete(verificationToken);
        return "User " + user.getEmail() + " verified successfully";
    }
}
