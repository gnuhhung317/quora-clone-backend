package net.duchung.quora.repository.elastic;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EsAnswerRepositoryTest {
    @Autowired
    private EsAnswerRepository esAnswerRepository;

    public void test() {
        esAnswerRepository.search("comment a").forEach(System.out::println);
    }

}