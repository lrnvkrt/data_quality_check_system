package dqcs.kafkaconsumerservice.sender;

import dqcs.kafkaconsumerservice.messaging.dto.GenericEvent;

import java.util.List;

public interface BatchSender {
    void flush();
    void sendBatch(String topic, List<GenericEvent> batch);
}
