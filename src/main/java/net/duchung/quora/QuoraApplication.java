package net.duchung.quora;

import net.duchung.quora.repository.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class QuoraApplication {
	@Autowired
	private AnswerRepository answerRepository;

	public static void main(String[] args) {
		SpringApplication.run(QuoraApplication.class, args);



	}

}
