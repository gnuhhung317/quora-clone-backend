package net.duchung.quora.service;

import net.duchung.quora.dto.request.RegisterRequest;
import net.duchung.quora.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    String login(String email, String password);

    String register(RegisterRequest registerRequest);

    User getCurrentUser();
}
