
package in.startupready.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import in.startupready.catalog.StepCatalog;
import in.startupready.catalog.StepCatalog.AssessmentField;
import in.startupready.catalog.StepCatalog.AssessmentStep;

@Service
public class AssessmentValidationService {

    private final StepCatalog stepCatalog;
    private final EvaluationService evaluationService;

    public AssessmentValidationService(
            StepCatalog stepCatalog,
            EvaluationService evaluationService) {
        this.stepCatalog = stepCatalog;
        this.evaluationService = evaluationService;
    }

    public Map<String, Object> validateStep(
            Long startupId, int stepNumber) {

        AssessmentStep step = stepCatalog.get(stepNumber);
        Map<String, String> answers =
                evaluationService.getAnswers(startupId);

        List<String> missingFields = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        for (AssessmentField field : step.fields()) {
            String value = answers.get(field.key());

            if (field.required()
                    && (value == null || value.isBlank())) {
                missingFields.add(field.key());
                errors.add(field.label() + " is required.");
            }
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("startupId", startupId);
        result.put("stepNumber", stepNumber);
        result.put("valid", errors.isEmpty());
        result.put("missingFields", missingFields);
        result.put("errors", errors);

        return result;
    }
}
