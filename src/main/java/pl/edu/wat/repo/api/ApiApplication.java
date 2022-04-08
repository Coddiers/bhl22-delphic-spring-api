package pl.edu.wat.repo.api;

import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.edu.wat.repo.api.entities.User;
import pl.edu.wat.repo.api.repositories.UserRepository;

@Slf4j
@SpringBootApplication
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }


    @Bean
    CommandLineRunner runner(UserRepository userRepository) {
        return args -> {


            User user = User.builder()
                    .roles(Set.of(User.ERole.ROLE_USER, User.ERole.ROLE_MODERATOR, User.ERole.ROLE_ADMIN))
                    .email("admin@admin.com")
                    .description("description 123")
                    .username("admin")
                    .password("password")
                    .isOrganization(false)
                    .build();
            userRepository.findByUsername("admin")
                    .ifPresentOrElse(
                            it -> log.info("found admin"),
                            () -> userRepository.save(user)
                    );

        };
    }
}
