package dqcs.kafkaconsumerservice.exception;

public class TopicNotConfiguredException extends RuntimeException {
    public TopicNotConfiguredException(String topicName) {
        super("Topic " + topicName + " is not configured");
    }
}
