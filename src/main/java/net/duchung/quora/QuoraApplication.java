package net.duchung.quora;

import net.duchung.quora.repository.elastic.EsAnswerRepository;
import net.duchung.quora.service.AnswerService;
import net.duchung.quora.service.ElasticSearchService;
import net.duchung.quora.service.MailService;
import net.duchung.quora.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class QuoraApplication {

	@Autowired
	MailService mailService;

	@Autowired
	private AnswerService answerService;
	@Autowired

	private ElasticSearchService elasticSearchService;
	@Autowired

	private RecommendationService recommendationService;
	public static void main(String[] args) {
		SpringApplication.run(QuoraApplication.class, args);




	}
	@Bean
	ApplicationRunner applicationRunner() {
		return args -> {
//			mailService.sendVerificationLinkToEmail("duchung04st@gmail.com", "token");
		};}
}
