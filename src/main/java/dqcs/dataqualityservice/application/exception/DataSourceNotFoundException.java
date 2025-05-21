package dqcs.dataqualityservice.application.exception;

public class DataSourceNotFoundException extends RuntimeException {
    public DataSourceNotFoundException(String message) {
        super(message);
    }
}
