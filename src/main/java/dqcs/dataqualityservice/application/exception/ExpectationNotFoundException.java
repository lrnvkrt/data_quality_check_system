package dqcs.dataqualityservice.application.exception;

public class ExpectationNotFoundException extends RuntimeException {
    public ExpectationNotFoundException(String message) {
        super(message);
    }
}
