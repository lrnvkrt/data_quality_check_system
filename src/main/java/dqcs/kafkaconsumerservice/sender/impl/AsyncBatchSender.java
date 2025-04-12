package dqcs.kafkaconsumerservice.sender.impl;

import dqcs.kafkaconsumerservice.client.ValidatorClient;
import dqcs.kafkaconsumerservice.messaging.dto.GenericEvent;
import dqcs.kafkaconsumerservice.messaging.registry.TopicBufferRegistry;
import dqcs.kafkaconsumerservice.sender.BatchSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

@Component
public class AsyncBatchSender implements BatchSender {

    private final Logger logger = LoggerFactory.getLogger(AsyncBatchSender.class);

    private final TopicBufferRegistry bufferRegistry;
    private final ValidatorClient validatorClient;

    @Autowired
    public AsyncBatchSender(TopicBufferRegistry bufferRegistry, ValidatorClient validatorClient) {
        this.bufferRegistry = bufferRegistry;
        this.validatorClient = validatorClient;
    }


    @Override
    @Scheduled(fixedDelayString = "${app.buffer.flush-interval-ms}")
    public void flush() {
        for (String topic : bufferRegistry.allTopics()) {
            BlockingQueue<GenericEvent> queue = bufferRegistry.getQueue(topic);
            int batchSize = bufferRegistry.getBatchSize(topic);

            List<GenericEvent> batch = new ArrayList<>(batchSize);
            queue.drainTo(batch, batchSize);

            if (!batch.isEmpty()) {
                sendBatch(topic, batch);
            }
        }
    }

    @Override
    public void sendBatch(String topic, List<GenericEvent> batch) {
        try {
            validatorClient.sendBatch(topic, batch);
            logger.info("Sent {} events to topic {} (overflow)", batch.size(), topic);
        } catch (Exception e) {
            logger.error("Overflow send failed for topic {}: {}, batch size {}", topic, e.getMessage(), batch.size(), e);
        }
    }
}
