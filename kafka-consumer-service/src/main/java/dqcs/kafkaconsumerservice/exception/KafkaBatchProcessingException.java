package dqcs.kafkaconsumerservice.exception;

public class KafkaBatchProcessingException extends RuntimeException {
    public KafkaBatchProcessingException(String topic, Throwable cause) {
        super("Error while processing batch for topic: " + topic, cause);
    }
}
