
package in.startupready.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import in.startupready.catalog.StepCatalog;
import in.startupready.catalog.StepCatalog.AssessmentField;
import in.startupready.catalog.StepCatalog.AssessmentStep;
import in.startupready.domain.Evaluation;
import in.startupready.domain.StepStatus;
import in.startupready.repository.StepStatusRepository;

@Service
public class AssessmentProgressService {

    private final EvaluationService evaluationService;
    private final StepCatalog stepCatalog;
    private final StepStatusRepository stepStatusRepository;

    public AssessmentProgressService(
            EvaluationService evaluationService,
            StepCatalog stepCatalog,
            StepStatusRepository stepStatusRepository) {
        this.evaluationService = evaluationService;
        this.stepCatalog = stepCatalog;
        this.stepStatusRepository = stepStatusRepository;
    }

    public Map<String, Object> getProgress(Long startupId) {
        Evaluation evaluation =
                evaluationService.getEvaluation(startupId);

        Map<String, String> answers =
                evaluationService.getAnswers(startupId);

        Map<Integer, String> savedStatuses = new LinkedHashMap<>();

        for (StepStatus status : stepStatusRepository
                .findByEvaluationIdOrderByStepNumberAsc(
                        evaluation.getId())) {
            savedStatuses.put(status.getStepNumber(), status.getStatus());
        }

        List<AssessmentStep> steps = stepCatalog.all();
        List<Map<String, Object>> stepProgress = new ArrayList<>();
        int completedSteps = 0;

        for (AssessmentStep step : steps) {
            int totalFields = step.fields().size();
            int answeredFields = 0;

            for (AssessmentField field : step.fields()) {
                String value = answers.get(field.key());

                if (value != null && !value.isBlank()) {
                    answeredFields++;
                }
            }

            boolean answersComplete = totalFields > 0
                    && answeredFields == totalFields;

            String status = savedStatuses.getOrDefault(
                    step.number(), "NOT_STARTED");

            if ("COMPLETED".equals(status) || answersComplete) {
                status = "COMPLETED";
                completedSteps++;
            }

            Map<String, Object> item = new LinkedHashMap<>();
            item.put("stepNumber", step.number());
            item.put("title", step.title());
            item.put("category", step.category());
            item.put("answeredFields", answeredFields);
            item.put("totalFields", totalFields);
            item.put("completionPercentage", totalFields == 0
                    ? 0 : Math.round(
                            answeredFields * 100.0 / totalFields));
            item.put("completed", "COMPLETED".equals(status));
            item.put("status", status);
            item.put("current",
                    step.number() == evaluation.getCurrentStep());

            stepProgress.add(item);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("startupId", startupId);
        result.put("currentStep", evaluation.getCurrentStep());
        result.put("totalSteps", steps.size());
        result.put("completedSteps", completedSteps);
        result.put("overallCompletionPercentage",
                steps.isEmpty() ? 0
                        : Math.round(
                                completedSteps * 100.0 / steps.size()));
        result.put("steps", stepProgress);

        return result;
    }
}
