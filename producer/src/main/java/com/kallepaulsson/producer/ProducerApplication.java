package com.kallepaulsson.producer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Instant;

@SpringBootApplication
public class ProducerApplication {

    private final static String TOPIC = "timestamps";

    private final KafkaTemplate<String, String> template;

    public ProducerApplication(KafkaTemplate<String, String> template) {
        this.template = template;
    }

    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class, args);
    }

    @Bean
    CommandLineRunner runner() {
        return args -> {
            while (true) {
                template.send(TOPIC, String.valueOf(Instant.now().getEpochSecond()));
                Thread.sleep(1000);
            }
        };
    }
}
