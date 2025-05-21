package dqcs.dataqualityservice.application.impl;

import app.grpc.Validation;
import app.grpc.ValidationServiceGrpc;
import com.fasterxml.jackson.databind.ObjectMapper;
import dqcs.dataqualityservice.application.GrpcValidationService;
import dqcs.dataqualityservice.application.exception.DataSourceNotFoundException;
import dqcs.dataqualityservice.application.exception.InvalidKwargsException;
import dqcs.dataqualityservice.infrastructure.entity.DataSource;
import dqcs.dataqualityservice.infrastructure.entity.Expectation;
import dqcs.dataqualityservice.infrastructure.repository.DataSourceRepository;
import dqcs.dataqualityservice.infrastructure.repository.ExpectationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GrpcValidationServiceImpl implements GrpcValidationService {

    private final Logger logger = LoggerFactory.getLogger(GrpcValidationServiceImpl.class);

    private final DataSourceRepository dataSourceRepository;
    private final ExpectationRepository expectationRepository;
    private final ValidationServiceGrpc.ValidationServiceBlockingStub validationServiceBlockingStub;
    private final ObjectMapper objectMapper;

    @Autowired
    public GrpcValidationServiceImpl(DataSourceRepository dataSourceRepository, ExpectationRepository expectationRepository, ValidationServiceGrpc.ValidationServiceBlockingStub validationServiceBlockingStub, ObjectMapper objectMapper) {
        this.dataSourceRepository = dataSourceRepository;
        this.expectationRepository = expectationRepository;
        this.validationServiceBlockingStub = validationServiceBlockingStub;
        this.objectMapper = objectMapper;
    }


    @Override
    public Validation.ValidationResponse validate(UUID dataSourceId, List<Map<String, Object>> rows) {
        logger.info("[validate] Start gRPC validation for data source: {}", dataSourceId);
        DataSource dataSource = dataSourceRepository.findById(dataSourceId)
                .orElseThrow(() -> new DataSourceNotFoundException("DataSource with id " + dataSourceId + " not found"));

        List<Expectation> expectations = expectationRepository.findAllByFieldDataSourceIdAndEnabledTrue(dataSourceId);

        Validation.ValidationRequest.Builder requestBuilder = Validation.ValidationRequest.newBuilder();

        for (Map<String, Object> row : rows) {
            Validation.Record.Builder record = Validation.Record.newBuilder();
            for (Map.Entry<String, Object> entry : row.entrySet()) {
                record.putFields(entry.getKey(), toFieldValue(entry.getValue()));
            }
            requestBuilder.addData(record);
        }

        logger.debug("[validate] Constructed {} records for validation", rows.size());

        Validation.ExpectationSuite.Builder suite = Validation.ExpectationSuite.newBuilder()
                .setExpectationSuiteName("ds_" + dataSourceId);

        for (Expectation e : expectations) {
            var expectationGrpc = app.grpc.Validation.Expectation.newBuilder()
                    .setExpectationType(e.getExpectationType().getCode())
                    .putAllMeta(Map.of(
                            "description", Optional.ofNullable(e.getDescription()).orElse("-"),
                            "severity", Optional.ofNullable(e.getSeverity().toString()).orElse("INFO")
                    ));

            Map<String, Object> defaultKwargs = Optional.ofNullable(e.getExpectationType().getDefaultKwargs())
                    .map(this::parseJsonMap)
                    .orElse(Collections.emptyMap());

            Map<String, Object> userKwargs = Optional.ofNullable(e.getKwargs())
                    .map(this::parseJsonMap)
                    .orElse(Collections.emptyMap());

            Map<String, Object> merged = new LinkedHashMap<>(defaultKwargs);
            merged.putAll(userKwargs);

            for (Map.Entry<String, Object> entry : merged.entrySet()) {
                expectationGrpc.putKwargs(entry.getKey(), toKwargValue(entry.getValue()));
            }

            expectationGrpc.putKwargs(
                    "column", toKwargValue(e.getField().getName()));

            if (e.getRowCondition() != null && !e.getRowCondition().isEmpty()) {
                expectationGrpc.putKwargs("row_condition", Validation.KwargValue.newBuilder()
                        .setStringValue(e.getRowCondition()).build());
                expectationGrpc.putKwargs("condition_parser", Validation.KwargValue.newBuilder()
                        .setStringValue("pandas").build());
            }

            suite.addExpectations(expectationGrpc.build());
        }

        logger.debug("[validate] Built expectation suite with {} expectations", expectations.size());

        requestBuilder.setExpectationSuite(suite.build());

        Validation.ValidationRequest validationRequest = requestBuilder.build();
        logger.info("[validate] Sending request to gRPC service...");

        Validation.ValidationResponse response = validationServiceBlockingStub.validate(validationRequest);
        logger.info("[validate] Received response from gRPC service. Success: {}", response.getSuccess());

        return response;
    }

    private Validation.FieldValue toFieldValue(Object value) {
        Validation.FieldValue.Builder b = Validation.FieldValue.newBuilder();
        if (value == null) return b.setStringValue("").build();
        if (value instanceof Integer i) return b.setIntValue(i).build();
        if (value instanceof Long l) return b.setIntValue(l).build();
        if (value instanceof Double d) return b.setDoubleValue(d).build();
        if (value instanceof Boolean bool) return b.setBoolValue(bool).build();
        return b.setStringValue(value.toString()).build();
    }

    private Validation.KwargValue toKwargValue(Object value) {
        Validation.KwargValue.Builder b = Validation.KwargValue.newBuilder();
        if (value == null) return b.setStringValue("").build();
        if (value instanceof Integer i) return b.setIntValue(i).build();
        if (value instanceof Long l) return b.setIntValue(l).build();
        if (value instanceof Double d) return b.setDoubleValue(d).build();
        if (value instanceof Boolean bool) return b.setBoolValue(bool).build();
        if (value instanceof List<?> list && list.stream().allMatch(v -> v instanceof String)) {
            Validation.ListValue.Builder listValue = Validation.ListValue.newBuilder();
            for (Object item : list) {
                listValue.addValues(Validation.FieldValue.newBuilder().setStringValue((String) item).build());
            }
            return b.setListValue(listValue.build()).build();
        }

        return b.setStringValue(value.toString()).build();
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> parseJsonMap(String json) {
        try {
            return objectMapper.readValue(json, Map.class);
        } catch (Exception e) {
            throw new InvalidKwargsException("Invalid JSON in kwargs: " + e.getMessage());
        }
    }
}
