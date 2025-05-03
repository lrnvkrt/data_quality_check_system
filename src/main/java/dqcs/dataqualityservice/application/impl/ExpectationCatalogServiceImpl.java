package dqcs.dataqualityservice.application.impl;

import dqcs.dataqualityservice.api.dto.ExpectationCatalogDto;
import dqcs.dataqualityservice.application.ExpectationCatalogService;
import dqcs.dataqualityservice.infrastructure.entity.ExpectationCatalog;
import dqcs.dataqualityservice.infrastructure.repository.ExpectationCatalogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpectationCatalogServiceImpl implements ExpectationCatalogService {

    private final Logger logger = LoggerFactory.getLogger(ExpectationCatalogServiceImpl.class);

    private final ExpectationCatalogRepository expectationCatalogRepository;

    @Autowired
    public ExpectationCatalogServiceImpl(ExpectationCatalogRepository expectationCatalogRepository) {
        this.expectationCatalogRepository = expectationCatalogRepository;
    }

    @Override
    public List<ExpectationCatalogDto> getAll() {
        logger.info("[getAll] Getting all expectation catalogs]");
        return expectationCatalogRepository.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    private ExpectationCatalogDto mapToDto(ExpectationCatalog entity) {
        return new ExpectationCatalogDto(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                entity.getDescription(),
                entity.getAllowedKwargTypes(),
                entity.getAllowedKwargs(),
                entity.isRequiresNumeric(),
                entity.isSupportsRowCondition()
        );
    }
}
