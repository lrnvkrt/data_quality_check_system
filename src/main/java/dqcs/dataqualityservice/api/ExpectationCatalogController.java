package dqcs.dataqualityservice.api;

import dqcs.dataqualityservice.api.dto.ExpectationCatalogDto;
import dqcs.dataqualityservice.application.ExpectationCatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/expectations/catalog")
public class ExpectationCatalogController {
    private final ExpectationCatalogService expectationCatalogService;

    @Autowired
    public ExpectationCatalogController(ExpectationCatalogService expectationCatalogService) {
        this.expectationCatalogService = expectationCatalogService;
    }

    @GetMapping
    public List<ExpectationCatalogDto> getExpectations() {
        return expectationCatalogService.getAll();
    }
}
