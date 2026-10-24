
package in.startupready.controller;

import java.util.Map;

import in.startupready.service.StepStatusService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/assessments")
public class AssessmentActionController {

    private final StepStatusService stepStatusService;

    public AssessmentActionController(
            StepStatusService stepStatusService) {
        this.stepStatusService = stepStatusService;
    }

    @PostMapping("/{startupId}/steps/{stepNumber}/skip")
    public Map<String, Object> skipStep(
            @PathVariable Long startupId,
            @PathVariable int stepNumber) {
        return updateStatus(startupId, stepNumber, "SKIPPED_FOR_NOW");
    }

    @PostMapping("/{startupId}/steps/{stepNumber}/complete-later")
    public Map<String, Object> completeLater(
            @PathVariable Long startupId,
            @PathVariable int stepNumber) {
        return updateStatus(startupId, stepNumber, "INCOMPLETE");
    }

    @PostMapping("/{startupId}/steps/{stepNumber}/complete")
    public Map<String, Object> completeStep(
            @PathVariable Long startupId,
            @PathVariable int stepNumber) {
        return updateStatus(startupId, stepNumber, "COMPLETED");
    }

    private Map<String, Object> updateStatus(
            Long startupId, int stepNumber, String status) {
        try {
            return stepStatusService.updateStatus(
                    startupId, stepNumber, status);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }
}
