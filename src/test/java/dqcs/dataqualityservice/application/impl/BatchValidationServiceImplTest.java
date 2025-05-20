package dqcs.dataqualityservice.application.impl;

import app.grpc.Validation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dqcs.dataqualityservice.api.dto.GenericEvent;
import dqcs.dataqualityservice.application.GrpcValidationService;
import dqcs.dataqualityservice.infrastructure.entity.DataSource;
import dqcs.dataqualityservice.infrastructure.entity.Expectation;
import dqcs.dataqualityservice.infrastructure.entity.ExpectationCatalog;
import dqcs.dataqualityservice.infrastructure.entity.Severity;
import dqcs.dataqualityservice.infrastructure.prometheus.DataQualityMetricsPusher;
import dqcs.dataqualityservice.infrastructure.repository.DataSourceRepository;
import dqcs.dataqualityservice.infrastructure.repository.ExpectationRepository;
import dqcs.dataqualityservice.infrastructure.repository.ValidationResultRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BatchValidationServiceImplTest {

    @Mock
    private DataSourceRepository dataSourceRepository;

    @Mock
    private ExpectationRepository expectationRepository;

    @Mock
    private GrpcValidationService grpcValidationService;

    @Mock
    private ValidationResultRepository validationResultRepository;

    @Mock
    private DataQualityMetricsPusher dataQualityMetricsPusher;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private BatchValidationServiceImpl batchValidationService;

    @Test
    void shouldValidateAndPushResults() {
        String topic = "test-topic";
        UUID dataSourceId = UUID.randomUUID();

        ObjectNode payload = new ObjectMapper().createObjectNode().put("field", "value");
        GenericEvent event = new GenericEvent(topic, payload);
        List<GenericEvent> events = List.of(event);

        Map<String, Object> row = Map.of("field", "value");
        List<Map<String, Object>> rows = List.of(row);
        when(objectMapper.convertValue(eq(payload), any(TypeReference.class))).thenReturn(row);

        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getId()).thenReturn(dataSourceId);
        when(dataSourceRepository.findByName(topic)).thenReturn(Optional.of(dataSource));

        Validation.ExpectationResult grpcResult = Validation.ExpectationResult.newBuilder()
                .setExpectationType("non_null")
                .setSuccess(true)
                .setColumn("field")
                .setErrorMessage("")
                .build();

        Validation.ValidationResponse grpcResponse = Validation.ValidationResponse.newBuilder()
                .setSuccess(true)
                .setEvaluatedExpectations(1)
                .setSuccessfulExpectations(1)
                .setUnsuccessfulExpectations(0)
                .addResults(grpcResult)
                .build();

        when(grpcValidationService.validate(eq(dataSourceId), eq(rows)))
                .thenReturn(grpcResponse);

        ExpectationCatalog catalog = mock(ExpectationCatalog.class);

        Expectation expectation = mock(Expectation.class);
        when(expectation.getId()).thenReturn(UUID.randomUUID());
        when(expectation.getSeverity()).thenReturn(Severity.CRITICAL);
        when(expectation.getDescription()).thenReturn("desc");
        when(expectation.getExpectationType()).thenReturn(catalog);

        when(expectationRepository.findAllByFieldDataSourceIdAndEnabledTrue(dataSourceId))
                .thenReturn(List.of(expectation));

        var result = batchValidationService.validate(topic, events);

        assertThat(result.success()).isTrue();
        assertThat(result.results()).hasSize(1);
        verify(validationResultRepository).saveValidationCells(any(), eq(topic), eq(rows), any());
        verify(dataQualityMetricsPusher).push(eq(topic), any(), eq(1));
    }
}