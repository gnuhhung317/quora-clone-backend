package net.duchung.quora;

import jakarta.transaction.Transactional;
import net.duchung.quora.data.document.AnswerDocument;
import net.duchung.quora.data.entity.Answer;
import net.duchung.quora.data.response.AnswerResponse;
import net.duchung.quora.repository.AnswerRepository;
import net.duchung.quora.repository.elastic.EsAnswerRepository;
import net.duchung.quora.service.AnswerService;
import net.duchung.quora.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;

@SpringBootApplication
@EnableJpaAuditing
public class QuoraApplication {
	@Autowired
	private AnswerRepository answerRepository;

	@Autowired
	private AnswerService answerService;
	@Autowired

	private RecommendationService recommendationService;
	public static void main(String[] args) {
		SpringApplication.run(QuoraApplication.class, args);




	}
	@Bean
	ApplicationRunner applicationRunner() {
		return args -> {
			answerService.test();
		};}
}
