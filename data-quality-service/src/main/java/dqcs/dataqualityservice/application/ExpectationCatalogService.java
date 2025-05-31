package dqcs.dataqualityservice.application;

import dqcs.dataqualityservice.api.dto.expectation.ExpectationCatalogDto;

import java.util.List;

public interface ExpectationCatalogService {
    List<ExpectationCatalogDto> getAll();
}
