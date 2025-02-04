package net.duchung.quora.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import net.duchung.quora.common.exception.DataNotFoundException;
import net.duchung.quora.common.security.JwtBlacklistService;
import net.duchung.quora.common.security.jwt.JwtUtil;
import net.duchung.quora.data.entity.User;
import net.duchung.quora.data.entity.VerificationToken;
import net.duchung.quora.data.request.RegisterRequest;
import net.duchung.quora.data.response.LoginResponse;
import net.duchung.quora.repository.UserRepository;
import net.duchung.quora.repository.VerificationTokenRepository;
import net.duchung.quora.service.AuthService;
import net.duchung.quora.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final JwtBlacklistService jwtBlacklistService;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;


    @Override
    public LoginResponse login(String email, String password) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password, Collections.emptySet());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // No need to set SecurityContext because using JWT, a stateless authentication
//        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return new LoginResponse(jwtUtil.generateToken(userDetails));
    }

    @Override
    @Transactional
    public String register(RegisterRequest registerRequest) {
        String email = registerRequest.getEmail();
        String password = registerRequest.getPassword();
        String fullName = registerRequest.getFullName();
        if (userRepository.existsByEmail(email)) {
            throw new DataIntegrityViolationException("Email already exists");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFullName(fullName);
        User savedUser = userRepository.save(user);
        VerificationToken verificationToken = new VerificationToken(UUID.randomUUID().toString(), savedUser);
        verificationTokenRepository.save(verificationToken);
        //fixme: send mail maybe fail but user save to db
        mailService.sendVerificationLinkToEmail(email, verificationToken.getToken());
        return "User " + savedUser.getEmail() + " registered successfully! Please check your email for verification.";
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("There are some problems with your account"));
    }

    @Override
    @Transactional
    public String verify(String code) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(code);
        if (verificationToken == null) {
            throw new DataNotFoundException("Verification token not found");
        }
        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new DataNotFoundException("Verification token expired");
        }
        User user = verificationToken.getUser();
        user.setActive(true);
        userRepository.save(user);
        verificationTokenRepository.delete(verificationToken);
        return "User " + user.getEmail() + " verified successfully";
    }

    @Override
    public String logout(String authHeader) {
    String token = authHeader.substring(7);
    jwtBlacklistService.addToBlackList(token);
    return "Logout success";
    }
}
