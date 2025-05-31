package dqcs.dataqualityservice.application.impl;

import app.grpc.Validation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dqcs.dataqualityservice.api.dto.expectation.ExpectationResultDto;
import dqcs.dataqualityservice.api.dto.GenericEvent;
import dqcs.dataqualityservice.api.dto.expectation.ValidationResultDto;
import dqcs.dataqualityservice.application.BatchValidationService;
import dqcs.dataqualityservice.application.GrpcValidationService;
import dqcs.dataqualityservice.application.exception.DataSourceNotFoundException;
import dqcs.dataqualityservice.infrastructure.entity.DataSource;
import dqcs.dataqualityservice.infrastructure.entity.Expectation;
import dqcs.dataqualityservice.infrastructure.prometheus.DataQualityMetricsPusher;
import dqcs.dataqualityservice.infrastructure.repository.DataSourceRepository;
import dqcs.dataqualityservice.infrastructure.repository.ExpectationRepository;
import dqcs.dataqualityservice.infrastructure.repository.ValidationResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BatchValidationServiceImpl implements BatchValidationService {

    private final Logger logger = LoggerFactory.getLogger(BatchValidationServiceImpl.class);

    private final DataSourceRepository dataSourceRepository;
    private final ExpectationRepository expectationRepository;
    private final GrpcValidationService grpcValidationService;
    private final ValidationResultRepository validationResultRepository;
    private final DataQualityMetricsPusher dataQualityMetricsPusher;
    private final ObjectMapper objectMapper;

    @Autowired
    public BatchValidationServiceImpl(DataSourceRepository dataSourceRepository, ExpectationRepository expectationRepository, GrpcValidationService grpcValidationService, ValidationResultRepository validationResultRepository, DataQualityMetricsPusher dataQualityMetricsPusher, ObjectMapper objectMapper) {
        this.dataSourceRepository = dataSourceRepository;
        this.expectationRepository = expectationRepository;
        this.grpcValidationService = grpcValidationService;
        this.validationResultRepository = validationResultRepository;
        this.dataQualityMetricsPusher = dataQualityMetricsPusher;
        this.objectMapper = objectMapper;
    }

    @Override
    public ValidationResultDto validate(String topic, List<GenericEvent> events) {
        logger.info("[validate] Starting batch validation by topic: {}", topic);

        UUID dataSourceId = dataSourceRepository.findByName(topic).map(DataSource::getId)
                .orElseThrow(() -> new DataSourceNotFoundException("DataSource with topic '" + topic + "' not found"));

        logger.debug("[validate] Number of events received: {}", events.size());

        List<Map<String, Object>> rows = events.stream()
                .map(GenericEvent::getPayload)
                .map(json -> objectMapper.convertValue(json ,new TypeReference<Map<String, Object>>(){}))
                .toList();

        Validation.ValidationResponse validationResponse = grpcValidationService.validate(dataSourceId, rows);
        ValidationResultDto resultDto = mapToDto(validationResponse, expectationRepository.findAllByFieldDataSourceIdAndEnabledTrue(dataSourceId));

        UUID eventId = UUID.randomUUID();

        validationResultRepository.saveValidationCells(eventId, topic, rows, resultDto);
        logger.info("[validate] Validation results saved. Pushing metrics...");

        dataQualityMetricsPusher.push(topic, resultDto.results(), rows.size());
        logger.info("[validate] Metrics pushed successfully!");

        return resultDto;
    }

    private ValidationResultDto mapToDto(Validation.ValidationResponse grpc, List<Expectation> expectations) {
        List<ExpectationResultDto> results = new ArrayList<>();

        for (int i = 0; i < grpc.getResultsCount(); i++) {
            var grpcResult = grpc.getResults(i);
            var expectation = expectations.get(i);
            String severity = Optional.ofNullable(expectation.getSeverity())
                    .map(Enum::name)
                    .orElse("INFO");

            String description = Optional.ofNullable(expectation.getDescription()).orElse(expectation.getExpectationType().toString());
            UUID expectationId = expectation.getId();

            results.add(new ExpectationResultDto(
                    description,
                    grpcResult.getExpectationType(),
                    grpcResult.getSuccess(),
                    grpcResult.getColumn(),
                    grpcResult.getErrorMessage(),
                    grpcResult.getFailedRowIndicesList(),
                    severity,
                    expectationId
            ));
        }

        return new ValidationResultDto(
                grpc.getSuccess(),
                grpc.getEvaluatedExpectations(),
                grpc.getSuccessfulExpectations(),
                grpc.getUnsuccessfulExpectations(),
                results
        );
    }
}
