package dqcs.dataqualityservice.api;

import dqcs.dataqualityservice.api.dto.ExpectationCreateRequest;
import dqcs.dataqualityservice.api.dto.ExpectationDto;
import dqcs.dataqualityservice.application.ExpectationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/fields/{fieldId}/expectations")
public class ExpectationController {
    private final ExpectationService expectationService;


    @Autowired
    public ExpectationController(ExpectationService expectationService) {
        this.expectationService = expectationService;
    }

    @PostMapping
    public UUID create(
            @Valid
            @PathVariable UUID fieldId,
            @RequestBody ExpectationCreateRequest expectationCreateRequest
    ) {
        return expectationService.createExpectation(fieldId, expectationCreateRequest);
    }

    @GetMapping
    public List<ExpectationDto> getAll(@PathVariable UUID fieldId) {
        return expectationService.getExpectationsForFieldId(fieldId);
    }

}
