package dqcs.dataqualityservice.application.impl;

import app.grpc.Validation;
import app.grpc.ValidationServiceGrpc;
import com.fasterxml.jackson.databind.ObjectMapper;
import dqcs.dataqualityservice.infrastructure.entity.*;
import dqcs.dataqualityservice.infrastructure.repository.DataSourceRepository;
import dqcs.dataqualityservice.infrastructure.repository.ExpectationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class GrpcValidationServiceImplTest {

    private DataSourceRepository dataSourceRepository;
    private ExpectationRepository expectationRepository;
    private ValidationServiceGrpc.ValidationServiceBlockingStub validationStub;
    private ObjectMapper objectMapper;
    private GrpcValidationServiceImpl service;

    @BeforeEach
    void setUp() {
        dataSourceRepository = mock(DataSourceRepository.class);
        expectationRepository = mock(ExpectationRepository.class);
        validationStub = mock(ValidationServiceGrpc.ValidationServiceBlockingStub.class);
        objectMapper = new ObjectMapper();
        service = new GrpcValidationServiceImpl(dataSourceRepository, expectationRepository, validationStub, objectMapper);
    }

    @Test
    @DisplayName("should throw IllegalArgumentException if data source not found")
    void shouldThrowIfDataSourceNotFound() {
        UUID dataSourceId = UUID.randomUUID();
        when(dataSourceRepository.findById(dataSourceId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.validate(dataSourceId, List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid dataSource id");
    }

    @Test
    @DisplayName("should build request and call gRPC stub with correct arguments")
    void shouldBuildRequestAndCallGrpcStub() {
        UUID dataSourceId = UUID.randomUUID();
        DataSource dataSource = new DataSource();
        dataSource.setId(dataSourceId);

        Field field = new Field();
        field.setName("age");

        ExpectationCatalog catalog = new ExpectationCatalog();
        catalog.setCode("expect_column_values_to_be_between");
        catalog.setDefaultKwargs("{\"min_value\": 18}");
        catalog.setAllowedKwargs(List.of("min_value", "max_value"));

        Expectation expectation = new Expectation();
        expectation.setField(field);
        expectation.setExpectationType(catalog);
        expectation.setKwargs("{\"max_value\": 65}");
        expectation.setSeverity(Severity.INFO);
        expectation.setDescription("Age between 18 and 65");
        expectation.setRowCondition("age > 0");

        Validation.ValidationResponse expectedResponse = Validation.ValidationResponse.newBuilder().build();

        when(dataSourceRepository.findById(dataSourceId)).thenReturn(Optional.of(dataSource));
        when(expectationRepository.findAllByFieldDataSourceIdAndEnabledTrue(dataSourceId)).thenReturn(List.of(expectation));
        when(validationStub.validate(any())).thenReturn(expectedResponse);

        List<Map<String, Object>> rows = List.of(
                Map.of("age", 25, "name", "John"),
                Map.of("age", 40, "name", "Jane")
        );

        Validation.ValidationResponse result = service.validate(dataSourceId, rows);

        assertThat(result).isSameAs(expectedResponse);
        verify(validationStub).validate(any(Validation.ValidationRequest.class));
    }
}