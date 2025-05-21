package dqcs.kafkaconsumerservice.exception;

public class EventBufferOverflowException extends RuntimeException {
    public EventBufferOverflowException(String topic, Throwable cause) {
        super("Failed to flush overflow batch for topic: " + topic, cause);
    }
}
