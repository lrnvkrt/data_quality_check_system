package dqcs.dataqualityservice.application;

import dqcs.dataqualityservice.api.dto.ExpectationCatalogDto;
import dqcs.dataqualityservice.infrastructure.entity.ExpectationCatalog;

import java.util.List;

public interface ExpectationCatalogService {
    List<ExpectationCatalogDto> getAll();
}
