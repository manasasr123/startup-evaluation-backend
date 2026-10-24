
package in.startupready.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.startupready.domain.Evaluation;
import in.startupready.domain.Startup;
import in.startupready.repository.EvaluationRepository;
import in.startupready.repository.StartupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final StartupRepository startupRepository;
    private final ObjectMapper objectMapper;

    public EvaluationService(
            EvaluationRepository evaluationRepository,
            StartupRepository startupRepository,
            ObjectMapper objectMapper) {
        this.evaluationRepository = evaluationRepository;
        this.startupRepository = startupRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public Evaluation createEvaluation(String startupName, String industry) {
        Startup startup = new Startup();
        startup.setName(startupName);
        startup.setIndustry(industry);

        Startup savedStartup = startupRepository.save(startup);

        Evaluation evaluation = new Evaluation();
        evaluation.setStartup(savedStartup);
        evaluation.setCurrentStep(1);
        evaluation.setAnswersJson("{}");

        return evaluationRepository.save(evaluation);
    }

    @Transactional(readOnly = true)
    public Evaluation getEvaluation(Long startupId) {
        return evaluationRepository.findByStartupId(startupId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Evaluation not found for startup: " + startupId));
    }

    @Transactional
    public Evaluation saveAnswers(Long startupId, int step, Map<String, String> answers) {
        if (step < 1 || step > 15) {
            throw new IllegalArgumentException("Step must be between 1 and 15");
        }

        Evaluation evaluation = getEvaluation(startupId);
        Map<String, String> allAnswers = readAnswers(evaluation);

        if (answers != null) {
            allAnswers.putAll(answers);
        }

        evaluation.setAnswersJson(writeAnswers(allAnswers));
        evaluation.setCurrentStep(Math.max(evaluation.getCurrentStep(), step));

        return evaluationRepository.save(evaluation);
    }

    @Transactional(readOnly = true)
    public Map<String, String> getAnswers(Long startupId) {
        return readAnswers(getEvaluation(startupId));
    }

    private Map<String, String> readAnswers(Evaluation evaluation) {
        try {
            return objectMapper.readValue(
                    evaluation.getAnswersJson(),
                    new TypeReference<LinkedHashMap<String, String>>() {});
        } catch (Exception exception) {
            throw new IllegalStateException(
                    "Could not read saved assessment answers", exception);
        }
    }

    private String writeAnswers(Map<String, String> answers) {
        try {
            return objectMapper.writeValueAsString(answers);
        } catch (Exception exception) {
            throw new IllegalStateException(
                    "Could not save assessment answers", exception);
        }
    }
}
