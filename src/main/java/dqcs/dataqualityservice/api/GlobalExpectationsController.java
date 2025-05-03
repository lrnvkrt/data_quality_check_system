package dqcs.dataqualityservice.api;

import dqcs.dataqualityservice.api.dto.ExpectationDto;
import dqcs.dataqualityservice.application.ExpectationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/expectations")
public class GlobalExpectationsController {

    private final ExpectationService expectationService;

    @Autowired
    public GlobalExpectationsController(ExpectationService expectationService) {
        this.expectationService = expectationService;
    }

    @GetMapping("/{id}")
    public ExpectationDto get(@PathVariable UUID id) {
        return expectationService.getExpectation(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        expectationService.deleteExpectation(id);
    }

    @PostMapping("/{id}/toggle")
    public void toggle(@PathVariable UUID id) {
        expectationService.toggleExpectation(id);
    }
}
