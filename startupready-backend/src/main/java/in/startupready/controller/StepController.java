
package in.startupready.controller;

import in.startupready.catalog.StepCatalog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/steps")
public class StepController {

    private final StepCatalog stepCatalog;

    public StepController(StepCatalog stepCatalog) {
        this.stepCatalog = stepCatalog;
    }

    @GetMapping
    public List<StepCatalog.AssessmentStep> getAllSteps() {
        return stepCatalog.all();
    }

    @GetMapping("/{number}")
    public StepCatalog.AssessmentStep getStep(@PathVariable int number) {
        try {
            return stepCatalog.get(number);
        } catch (IllegalArgumentException exception) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND,
                    exception.getMessage());
        }
    }
}
