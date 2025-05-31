package dqcs.dataqualityservice.config.prometheus;

import io.prometheus.client.exporter.PushGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PushGatewayConfig {
    @Bean
    public PushGateway pushGateway(@Value("${metrics.pushgateway.host}") String host) {
        return new PushGateway(host);
    }
}
