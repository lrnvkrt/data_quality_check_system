package dqcs.dataqualityservice.infrastructure.prometheus.impl;

import dqcs.dataqualityservice.api.dto.expectation.ExpectationResultDto;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.PushGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.*;

class PrometheusDataQualityMetricsPusherTest {

    private PushGateway pushGateway;
    private PrometheusDataQualityMetricsPusher pusher;

    @BeforeEach
    void setUp() {
        pushGateway = mock(PushGateway.class);
        pusher = new PrometheusDataQualityMetricsPusher(pushGateway);
    }

    @Test
    @DisplayName("should push metrics to PushGateway when there are failed results")
    void shouldPushMetrics() throws Exception {
        UUID expectationId = UUID.randomUUID();
        var result = new ExpectationResultDto(
                "Check age range",
                "expect_column_values_to_be_between",
                false,
                "age",
                "Some rows failed",
                List.of(1, 3, 5),
                "ERROR",
                expectationId
        );

        pusher.push("orders_topic", List.of(result), 10);

        verify(pushGateway).pushAdd(
                any(CollectorRegistry.class),
                eq("data-quality"),
                eq(Map.of("instance", "orders_topic"))
        );
    }
}