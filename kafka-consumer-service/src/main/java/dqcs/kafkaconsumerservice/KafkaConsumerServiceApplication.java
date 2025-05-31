package dqcs.kafkaconsumerservice;

import dqcs.kafkaconsumerservice.config.kafka.properties.KafkaTopicProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties(KafkaTopicProperties.class)
@EnableFeignClients
@EnableScheduling
public class KafkaConsumerServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(KafkaConsumerServiceApplication.class, args);
    }
}
