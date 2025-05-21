package dqcs.dataqualityservice.api.validation.validator;

import dqcs.dataqualityservice.api.dto.ExpectationCreateRequest;
import dqcs.dataqualityservice.api.validation.annotation.ValidRowCondition;
import dqcs.dataqualityservice.infrastructure.entity.ExpectationCatalog;
import dqcs.dataqualityservice.infrastructure.repository.ExpectationCatalogRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RowConditionValidator implements ConstraintValidator<ValidRowCondition, ExpectationCreateRequest> {

    private final ExpectationCatalogRepository catalogRepository;

    @Autowired
    public RowConditionValidator(ExpectationCatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    @Override
    public boolean isValid(ExpectationCreateRequest req, ConstraintValidatorContext context) {
        if (req.expectationTypeId() == null) return true;

        ExpectationCatalog catalog = catalogRepository.findById(req.expectationTypeId()).orElse(null);
        if (catalog == null) return true;

        String condition = req.rowCondition();
        boolean hasCondition = condition != null && !condition.isBlank();

        return !hasCondition || catalog.isSupportsRowCondition();
    }
}
