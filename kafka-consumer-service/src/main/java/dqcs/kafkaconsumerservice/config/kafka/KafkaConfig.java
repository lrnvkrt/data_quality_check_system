package dqcs.kafkaconsumerservice.config.kafka;

import dqcs.kafkaconsumerservice.config.kafka.properties.KafkaTopicProperties;
import dqcs.kafkaconsumerservice.config.kafka.properties.TopicConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class KafkaConfig {
    @Bean("apptopicList")
    public List<String> appTopicList(KafkaTopicProperties props) {
        return props.getTopics().stream().map(TopicConfig::name).toList();
    }
}
