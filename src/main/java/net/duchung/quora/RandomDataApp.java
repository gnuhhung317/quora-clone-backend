package net.duchung.quora;

import net.duchung.quora.common.exception.AccessDeniedException;
import net.duchung.quora.common.exception.DataNotFoundException;
import net.duchung.quora.common.utils.Constant;
import net.duchung.quora.data.entity.Answer;
import net.duchung.quora.data.entity.Comment;
import net.duchung.quora.data.entity.User;
import net.duchung.quora.repository.AnswerRepository;
import net.duchung.quora.repository.CommentRepository;
import net.duchung.quora.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@SpringBootApplication
public class RandomDataApp implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private CommentRepository commentRepository;

    public static void main(String[] args) {
        SpringApplication.run(RandomDataApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        gennerateComment();
    }

    private void gennerateComment() {
        Faker faker = new Faker();

        int totalRecords = 1000; // Tổng số lượng dữ liệu cần tạo
        for (int j =0 ; j< totalRecords;j++) {
            Long start = System.nanoTime();

            List<Comment> comments = new ArrayList<>();
            IntStream.range(0, 100).parallel().forEach(i -> {
                // Random User
                Long userId = (long) randomInt(300, 508);
                Long answerId = (long) randomInt(1000, 2000);
                String content = faker.lorem().sentence();

                try {
                    User user = userRepository.findById(userId).orElseThrow( ()->new DataNotFoundException("User not found"));
                    Answer answer = answerRepository.findById(answerId).orElseThrow(() -> new DataNotFoundException("Answer with id " + answerId + " not found"));

                    Comment comment = new Comment();
                    comment.setContent(content);

                    // Cập nhật viral points cho answer
                    answer.setViralPoints(answer.getViralPoints() + 10);
                    Answer savedAnswer = answerRepository.save(answer);

                    // Lưu comment
                    comment.setUser(user);
                    comment.setAnswer(savedAnswer);
                    comments.add(comment);
                    // Thêm comment vào danh sách
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            });
            commentRepository.saveAll(comments);
            long end = System.nanoTime();
            System.out.println("Total time: " + (end - start)  + "ms");
        }
        // Sau khi hoàn thành, lưu các comment vào cơ sở dữ liệu
    }

    // Hàm random int trong khoảng min và max
    public int randomInt(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }
}
