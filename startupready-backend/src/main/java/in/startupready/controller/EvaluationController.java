
package in.startupready.controller;

import in.startupready.domain.Evaluation;
import in.startupready.service.EvaluationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/evaluations")
public class EvaluationController {

    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> create(@RequestBody CreateEvaluationRequest request) {
        if (request.startupName() == null || request.startupName().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Startup name is required");
        }

        Evaluation evaluation = evaluationService.createEvaluation(
                request.startupName().trim(),
                request.industry() == null ? "" : request.industry().trim());

        return Map.of(
                "evaluationId", evaluation.getId(),
                "startupId", evaluation.getStartup().getId(),
                "startupName", evaluation.getStartup().getName(),
                "currentStep", evaluation.getCurrentStep());
    }

    @GetMapping("/{startupId}")
    public Map<String, Object> get(@PathVariable Long startupId) {
        Evaluation evaluation = evaluationService.getEvaluation(startupId);

        return Map.of(
                "evaluationId", evaluation.getId(),
                "startupId", evaluation.getStartup().getId(),
                "startupName", evaluation.getStartup().getName(),
                "currentStep", evaluation.getCurrentStep(),
                "answers", evaluationService.getAnswers(startupId));
    }

    @PutMapping("/{startupId}/steps/{step}")
    public Map<String, Object> save(
            @PathVariable Long startupId,
            @PathVariable int step,
            @RequestBody Map<String, String> answers) {

        try {
            Evaluation evaluation = evaluationService.saveAnswers(
                    startupId, step, answers);

            return Map.of(
                    "message", "Answers saved successfully",
                    "currentStep", evaluation.getCurrentStep(),
                    "answers", evaluationService.getAnswers(startupId));
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, exception.getMessage());
        }
    }

    public record CreateEvaluationRequest(String startupName, String industry) {
    }
}
