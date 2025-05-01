package dqcs.dataqualityservice.config.grpc;

import app.grpc.ValidationServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfiguration {

    @Value("${grpc.validation.host}")
    private String validationHost;

    @Value("${grpc.validation.port}")
    private int validationPort;

    @Bean
    public ManagedChannel managedChannel() {
        return ManagedChannelBuilder.forAddress(validationHost, validationPort)
                .usePlaintext()
                .build();
    }

    @Bean
    public ValidationServiceGrpc.ValidationServiceBlockingStub validationServiceBlockingStub(ManagedChannel managedChannel) {
        return ValidationServiceGrpc.newBlockingStub(managedChannel);
    }
}
