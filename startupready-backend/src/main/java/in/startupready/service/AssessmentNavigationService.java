
package in.startupready.service;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.startupready.catalog.StepCatalog;
import in.startupready.domain.Evaluation;

@Service
public class AssessmentNavigationService {

    private final EvaluationService evaluationService;
    private final StepCatalog stepCatalog;

    public AssessmentNavigationService(
            EvaluationService evaluationService,
            StepCatalog stepCatalog) {
        this.evaluationService = evaluationService;
        this.stepCatalog = stepCatalog;
    }

    @Transactional
    public Map<String, Object> navigate(
            Long startupId, int targetStep) {

        stepCatalog.get(targetStep);

        Evaluation evaluation =
                evaluationService.getEvaluation(startupId);

        evaluation.setCurrentStep(targetStep);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("startupId", startupId);
        result.put("currentStep", targetStep);
        result.put("totalSteps", stepCatalog.all().size());
        result.put("step", stepCatalog.get(targetStep));
        result.put("message", "Assessment position updated");

        return result;
    }
}
