package dqcs.kafkaconsumerservice.messaging.registry;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dqcs.kafkaconsumerservice.config.kafka.properties.KafkaTopicProperties;
import dqcs.kafkaconsumerservice.config.kafka.properties.TopicConfig;
import dqcs.kafkaconsumerservice.messaging.dto.GenericEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TopicBufferRegistry Unit Tests")
class TopicBufferRegistryTest {

    private TopicBufferRegistry registry;
    private final String topic = "test-topic";
    private final ObjectMapper objectMapper = new ObjectMapper();

    private GenericEvent createEvent(String topic, String json) {
        ObjectNode payload = objectMapper.createObjectNode();
        payload.put("value", json);
        return new GenericEvent(topic, payload);
    }

    @BeforeEach
    void setUp() {
        TopicConfig config = new TopicConfig(topic, 2, 5);
        KafkaTopicProperties kafkaTopicProperties = new KafkaTopicProperties(List.of(config));
        registry = new TopicBufferRegistry(kafkaTopicProperties);
        registry.init();
    }

    @Test
    @DisplayName("should initialize topic queue with correct capacity")
    void shouldInitializeQueue() {
        BlockingQueue<GenericEvent> queue = registry.getQueue(topic);
        assertThat(queue).isNotNull();
        assertThat(queue.remainingCapacity()).isEqualTo(2);
    }

    @Test
    @DisplayName("should insert event when queue has space")
    void shouldOfferEventNormally() {
        GenericEvent event = createEvent(topic, "normal");
        AtomicBoolean called = new AtomicBoolean(false);
        registry.offer(topic, event, batch -> called.set(true));
        BlockingQueue<GenericEvent> queue = registry.getQueue(topic);
        assertThat(queue).containsExactly(event);
        assertThat(called).isFalse();
    }

    @Test
    @DisplayName("should trigger flush when queue overflows")
    void shouldTriggerOverflowFlush() {
        GenericEvent e1 = createEvent(topic, "1");
        GenericEvent e2 = createEvent(topic, "2");
        GenericEvent e3 = createEvent(topic, "3");
        GenericEvent e4 = createEvent(topic, "3");


        AtomicReference<List<GenericEvent>> flushed = new AtomicReference<>();

        registry.offer(topic, e1, flushed::set);
        registry.offer(topic, e2, flushed::set);
        registry.offer(topic, e3, flushed::set);
        registry.offer(topic, e4, flushed::set);

        assertThat(flushed.get()).containsExactly(e1, e2);
        BlockingQueue<GenericEvent> queue = registry.getQueue(topic);
        assertThat(queue).containsExactly(e4);
    }

    @Test
    @DisplayName("should return correct batch size from config")
    void shouldReturnBatchSize() {
        int batchSize = registry.getBatchSize(topic);
        assertThat(batchSize).isEqualTo(2);
    }

    @Test
    @DisplayName("should return all topic names")
    void shouldReturnAllTopics() {
        Set<String> topics = registry.allTopics();
        assertThat(topics).containsExactly(topic);
    }
}