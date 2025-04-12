package dqcs.kafkaconsumerservice.messaging.dto;

import com.fasterxml.jackson.databind.JsonNode;

public class GenericEvent{

    private String topic;

    private JsonNode payload;

    public GenericEvent(String topic, JsonNode payload) {
        this.topic = topic;
        this.payload = payload;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public JsonNode getPayload() {
        return payload;
    }

    public void setPayload(JsonNode payload) {
        this.payload = payload;
    }
}
