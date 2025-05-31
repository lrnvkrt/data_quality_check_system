package dqcs.kafkaconsumerservice.config.kafka.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app.kafka")
public class KafkaTopicProperties {
    private List<TopicConfig> topics;

    public KafkaTopicProperties(List<TopicConfig> topics) {
        this.topics = topics;
    }

    public List<TopicConfig> getTopics() {
        return topics;
    }

    public void setTopics(List<TopicConfig> topics) {
        this.topics = topics;
    }
}
