
package in.startupready.controller;

import java.util.List;

import in.startupready.catalog.StepCatalog;
import in.startupready.catalog.StepCatalog.AssessmentStep;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/assessments")
public class AssessmentController {

    private final StepCatalog stepCatalog;

    public AssessmentController(StepCatalog stepCatalog) {
        this.stepCatalog = stepCatalog;
    }

    @GetMapping("/steps")
    public List<AssessmentStep> getAllSteps() {
        return stepCatalog.all();
    }

    @GetMapping("/steps/{stepNumber}")
    public AssessmentStep getStep(
            @PathVariable int stepNumber) {
        try {
            return stepCatalog.get(stepNumber);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Assessment step not found: " + stepNumber);
        }
    }
}
