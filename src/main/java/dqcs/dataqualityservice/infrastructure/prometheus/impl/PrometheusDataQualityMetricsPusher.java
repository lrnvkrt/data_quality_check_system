package dqcs.dataqualityservice.infrastructure.prometheus.impl;

import dqcs.dataqualityservice.api.dto.ExpectationResultDto;
import dqcs.dataqualityservice.infrastructure.prometheus.DataQualityMetricsPusher;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.PushGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class PrometheusDataQualityMetricsPusher implements DataQualityMetricsPusher {
    private final Logger logger = LoggerFactory.getLogger(PrometheusDataQualityMetricsPusher.class);

    private final PushGateway pushGateway;

    public PrometheusDataQualityMetricsPusher(PushGateway pushGateway) {
        this.pushGateway = pushGateway;
    }


    @Override
    public void push(String topic, List<ExpectationResultDto> results, int totalRows) {
        CollectorRegistry registry = new CollectorRegistry();

        Counter severityCounter = Counter.build()
                .name("dq_expectation_severity_total")
                .help("Number of failed rows grouped by severity")
                .labelNames("topic", "severity")
                .register(registry);

        Gauge failureRatioGauge = Gauge.build()
                .name("dq_expectation_failure_ratio")
                .help("Failure ratio per expectation")
                .labelNames("expectation_id")
                .register(registry);

        Counter failedRulesCounter = Counter.build()
                .name("dq_failed_rules_total")
                .help("Failures grouped by expectation type")
                .labelNames("topic", "expectation_type")
                .register(registry);

        Counter failedFieldsCounter = Counter.build()
                .name("dq_failed_fields_total")
                .help("Failures grouped by field")
                .labelNames("topic", "field")
                .register(registry);

        for (ExpectationResultDto result : results) {
            int failedCount = result.failedRowIndices().size();
            if (failedCount == 0) continue;

            severityCounter.labels(topic, result.severity().toUpperCase()).inc(failedCount);
            failureRatioGauge.labels(result.expectationId().toString()).set((double) failedCount / totalRows);
            failedRulesCounter.labels(topic, result.expectationType()).inc(failedCount);
            failedFieldsCounter.labels(topic, result.column()).inc(failedCount);
        }

        try {
            logger.info("Pushing data quality metrics to Prometheus server");
            pushGateway.pushAdd(registry, "data-quality", Map.of("instance", topic));
        } catch (IOException e) {
            logger.error("Pushing data quality metrics to Prometheus server failed", e);
        }
    }
}
