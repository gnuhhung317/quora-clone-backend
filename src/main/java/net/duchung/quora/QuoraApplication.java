package net.duchung.quora;

import net.duchung.quora.repository.AnswerRepository;
import net.duchung.quora.service.QuestionService;
import net.duchung.quora.service.RecommendationService;
import net.duchung.quora.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootApplication
@EnableJpaAuditing
public class QuoraApplication {
	@Autowired
	private AnswerRepository answerRepository;

	@Autowired
	private RedisService redisService;
	@Autowired
	private RecommendationService recommendationService;
	public static void main(String[] args) {
		SpringApplication.run(QuoraApplication.class, args);




	}
//	@Bean
//	ApplicationRunner applicationRunner() {
//		return args -> {
//			SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("strin@gmai.gg", "admin"));
//		recommendationService.getRecommendationAnswers();
//		};}
}
