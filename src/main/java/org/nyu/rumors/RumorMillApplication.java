package org.nyu.rumors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("org.nyu.rumors.service")
public class RumorMillApplication {
    public static void main(String[] args) {
        SpringApplication.run(RumorMillApplication.class, args);
    }
}
