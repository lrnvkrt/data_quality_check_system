package dqcs.dataqualityservice.api;

import app.grpc.Validation;
import dqcs.dataqualityservice.api.dto.GenericEvent;
import dqcs.dataqualityservice.api.dto.ValidationResultDto;
import dqcs.dataqualityservice.application.BatchValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/batch")
public class ValidationController {

    private final BatchValidationService batchValidationService;


    @Autowired
    public ValidationController(BatchValidationService batchValidationService) {
        this.batchValidationService = batchValidationService;
    }

    @PostMapping("/{topic}")
    public ValidationResultDto validate(
            @PathVariable String topic,
            @RequestBody List<GenericEvent> events
    ) {
        return batchValidationService.validate(topic, events);
    }
}
