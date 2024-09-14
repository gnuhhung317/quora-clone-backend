package net.duchung.quora.service;

import net.duchung.quora.data.request.RegisterRequest;
import net.duchung.quora.data.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    String login(String email, String password);

    String register(RegisterRequest registerRequest);

    String verify(String code);

    User getCurrentUser();
}
