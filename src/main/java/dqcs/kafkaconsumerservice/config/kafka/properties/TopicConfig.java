package dqcs.kafkaconsumerservice.config.kafka.properties;

public record TopicConfig(
        String name,
        int capacity,
        int batchSize
) { }
