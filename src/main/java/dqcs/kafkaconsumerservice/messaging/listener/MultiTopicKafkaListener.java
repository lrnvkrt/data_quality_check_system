package dqcs.kafkaconsumerservice.messaging.listener;

import com.fasterxml.jackson.databind.JsonNode;
import dqcs.kafkaconsumerservice.messaging.dto.GenericEvent;
import dqcs.kafkaconsumerservice.messaging.registry.TopicBufferRegistry;
import dqcs.kafkaconsumerservice.sender.BatchSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MultiTopicKafkaListener {

    private final Logger logger = LoggerFactory.getLogger(MultiTopicKafkaListener.class);

    private final TopicBufferRegistry bufferRegistry;
    private final BatchSender batchSender;

    @Autowired
    public MultiTopicKafkaListener(TopicBufferRegistry bufferRegistry, BatchSender batchSender) {
        this.bufferRegistry = bufferRegistry;
        this.batchSender = batchSender;
    }

    @KafkaListener(
            topics = "#{@apptopicList}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(List<JsonNode> events,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) List<String> topics) {

        logger.info("Received {} events", events.size());

        for (int i = 0; i < events.size(); i++) {
            String topic = topics.get(i);
            JsonNode event = events.get(i);

            GenericEvent genericEvent = new GenericEvent(topic, event);

            bufferRegistry.offer(topic, genericEvent, drainedBatch ->
                    batchSender.sendBatch(topic, drainedBatch)
            );
        }
    }
}
