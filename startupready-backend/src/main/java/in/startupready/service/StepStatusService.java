
package in.startupready.service;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.startupready.catalog.StepCatalog;
import in.startupready.domain.Evaluation;
import in.startupready.domain.StepStatus;
import in.startupready.repository.EvaluationRepository;
import in.startupready.repository.StepStatusRepository;

@Service
public class StepStatusService {

    private final EvaluationRepository evaluationRepository;
    private final StepStatusRepository stepStatusRepository;
    private final StepCatalog stepCatalog;

    public StepStatusService(
            EvaluationRepository evaluationRepository,
            StepStatusRepository stepStatusRepository,
            StepCatalog stepCatalog) {
        this.evaluationRepository = evaluationRepository;
        this.stepStatusRepository = stepStatusRepository;
        this.stepCatalog = stepCatalog;
    }

    @Transactional
    public Map<String, Object> updateStatus(
            Long startupId, int stepNumber, String status) {

        stepCatalog.get(stepNumber);

        if (!status.equals("SKIPPED_FOR_NOW")
                && !status.equals("INCOMPLETE")
                && !status.equals("COMPLETED")) {
            throw new IllegalArgumentException("Invalid step status");
        }

        Evaluation evaluation = evaluationRepository
                .findByStartupId(startupId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Evaluation not found for startup: " + startupId));

        StepStatus stepStatus = stepStatusRepository
                .findByEvaluationIdAndStepNumber(
                        evaluation.getId(), stepNumber)
                .orElseGet(StepStatus::new);

        stepStatus.setEvaluation(evaluation);
        stepStatus.setStepNumber(stepNumber);
        stepStatus.setStatus(status);

        StepStatus saved = stepStatusRepository.save(stepStatus);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("startupId", startupId);
        result.put("stepNumber", saved.getStepNumber());
        result.put("status", saved.getStatus());
        result.put("updatedAt", saved.getUpdatedAt());
        result.put("message", "Assessment step status saved");

        return result;
    }
}
