package net.duchung.quora.service.impl;

import jakarta.mail.internet.MimeMessage;
import net.duchung.quora.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;


    private final String BASE_URL = "http://localhost:8080/api/v1/auth/register";
    @Override
    public void sendVerificationLinkToEmail(String email, String token) {

        String subject = "Please verify your registration";
        String content = "Dear my friend,<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Your company name.";
        String verifyURL =  BASE_URL+ "/verify?code=" + token;
        content = content.replace("[[URL]]", verifyURL);

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(" mailtrap@demomailtrap.com"); // Set a valid 'from' address
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(content, true); // Enable HTML

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mailSender.send(message);

    }
}
