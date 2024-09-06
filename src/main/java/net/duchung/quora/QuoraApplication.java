package net.duchung.quora;

import net.duchung.quora.data.entity.Answer;
import net.duchung.quora.data.response.AnswerResponse;
import net.duchung.quora.repository.AnswerRepository;
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

	private RecommendationService recommendationService;
	public static void main(String[] args) {
		SpringApplication.run(QuoraApplication.class, args);




	}
	@Bean
	ApplicationRunner applicationRunner() {
		return args -> {
//			SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("strin@gmai.gg", "admin"));
//		recommendationService.getRecommendationAnswers();
//			List<Answer> answers = answerRepository.findAll();
////			List<AnswerResponse> answerResponses = answers.stream().map(a -> new AnswerResponse(a   )).toList();
//			List<AnswerDocument> answerDocuments = answers.stream().map(AnswerDocument::new).toList();
//			esAnswerRepository.saveAll(answerDocuments);
		};}
}
