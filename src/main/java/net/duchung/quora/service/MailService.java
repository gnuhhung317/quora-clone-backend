package net.duchung.quora.service;

import org.springframework.stereotype.Service;

@Service
public interface MailService {

    void sendVerificationLinkToEmail(String to, String subject);
}
