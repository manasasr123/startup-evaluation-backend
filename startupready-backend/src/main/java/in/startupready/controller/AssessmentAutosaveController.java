
package in.startupready.controller;

import java.util.Map;

import in.startupready.service.EvaluationService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/assessments")
public class AssessmentAutosaveController {

    private final EvaluationService evaluationService;

    public AssessmentAutosaveController(
            EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @PutMapping("/{startupId}/steps/{stepNumber}/autosave")
    public Map<String, Object> autosave(
            @PathVariable Long startupId,
            @PathVariable int stepNumber,
            @RequestBody Map<String, String> answers) {

        try {
            evaluationService.saveAnswers(
                    startupId, stepNumber, answers);

            return Map.of(
                    "startupId", startupId,
                    "stepNumber", stepNumber,
                    "saved", true,
                    "message", "Assessment answers saved successfully"
            );
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }
}
