
package in.startupready.controller;

import java.util.Map;

import in.startupready.service.AssessmentValidationService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/assessments")
public class AssessmentValidationController {

    private final AssessmentValidationService validationService;

    public AssessmentValidationController(
            AssessmentValidationService validationService) {
        this.validationService = validationService;
    }

    @GetMapping("/{startupId}/steps/{stepNumber}/validate")
    public Map<String, Object> validateStep(
            @PathVariable Long startupId,
            @PathVariable int stepNumber) {
        try {
            return validationService.validateStep(startupId, stepNumber);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }
}
