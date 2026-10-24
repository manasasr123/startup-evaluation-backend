
package in.startupready.service;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.startupready.domain.Evaluation;
import in.startupready.domain.Startup;
import in.startupready.repository.EvaluationRepository;
import in.startupready.repository.StartupRepository;

@Service
public class StartupWorkflowService {

    private final StartupRepository startupRepository;
    private final EvaluationRepository evaluationRepository;

    public StartupWorkflowService(
            StartupRepository startupRepository,
            EvaluationRepository evaluationRepository) {
        this.startupRepository = startupRepository;
        this.evaluationRepository = evaluationRepository;
    }

    @Transactional
    public Map<String, Object> createStartupWithEvaluation(
            String name,
            String industry,
            String stage,
            String headquarters) {

        Startup startup = new Startup();
        startup.setName(name.trim());
        startup.setIndustry(industry);
        startup.setStage(stage);
        startup.setHeadquarters(headquarters);

        Startup savedStartup = startupRepository.save(startup);

        Evaluation evaluation = new Evaluation();
        evaluation.setStartup(savedStartup);
        evaluation.setCurrentStep(1);
        evaluation.setAnswersJson("{}");

        Evaluation savedEvaluation =
                evaluationRepository.save(evaluation);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("startupId", savedStartup.getId());
        result.put("startupName", savedStartup.getName());
        result.put("industry", savedStartup.getIndustry());
        result.put("stage", savedStartup.getStage());
        result.put("headquarters", savedStartup.getHeadquarters());
        result.put("evaluationId", savedEvaluation.getId());
        result.put("currentStep", savedEvaluation.getCurrentStep());
        result.put("totalSteps", 15);
        result.put("message",
                "Startup created and assessment initialized successfully");

        return result;
    }
}
