package dqcs.kafkaconsumerservice.messaging.registry;

import dqcs.kafkaconsumerservice.config.kafka.properties.KafkaTopicProperties;
import dqcs.kafkaconsumerservice.config.kafka.properties.TopicConfig;
import dqcs.kafkaconsumerservice.exception.EventBufferOverflowException;
import dqcs.kafkaconsumerservice.exception.TopicNotConfiguredException;
import dqcs.kafkaconsumerservice.messaging.dto.GenericEvent;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Consumer;

@Component
public class TopicBufferRegistry {

    private final Logger logger = LoggerFactory.getLogger(TopicBufferRegistry.class);

    private final KafkaTopicProperties kafkaTopicProperties;

    private final Map<String, BlockingDeque<GenericEvent>> topicQueues = new ConcurrentHashMap<>();

    @Autowired
    public TopicBufferRegistry(KafkaTopicProperties kafkaTopicProperties) {
        this.kafkaTopicProperties = kafkaTopicProperties;
    }

    @PostConstruct
    public void init() {
        for (TopicConfig config : kafkaTopicProperties.getTopics()) {
            var queue = new LinkedBlockingDeque<GenericEvent>(config.capacity());
            topicQueues.put(config.name(), queue);
        }
    }

    public boolean offer(String topic, GenericEvent event, Consumer<List<GenericEvent>> onOverflowFlush) {
        var queue = topicQueues.get(topic);
        if (queue != null) {
            if (!queue.offer(event)) {
                List<GenericEvent> overflowBatch = new ArrayList<>();
                queue.drainTo(overflowBatch);
                logger.warn("Buffer for topic {} overflowed. Draining {} events", topic, overflowBatch.size());
                try {
                    onOverflowFlush.accept(overflowBatch);
                } catch (Exception e) {
                    logger.error("Failed to flush overflow batch for topic {}: {}", topic, e.getMessage(), e);
                    throw new EventBufferOverflowException(topic, e);
                }
                return false;
            }
            return true;
        } else {
            throw new TopicNotConfiguredException(topic);
        }
    }


    public BlockingQueue<GenericEvent> getQueue(String topic) {
        return topicQueues.get(topic);
    }

    public Set<String> allTopics() {
        return topicQueues.keySet();
    }

    public int getBatchSize(String topic) {
        return kafkaTopicProperties.getTopics().stream()
                .filter(t -> t.name().equals(topic))
                .findFirst()
                .map(TopicConfig::capacity)
                .orElseThrow(() -> new TopicNotConfiguredException(topic));
    }
}
