
package in.startupready.service;

import in.startupready.catalog.StepCatalog;
import in.startupready.domain.Evaluation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScoreService {

    private final EvaluationService evaluationService;
    private final StepCatalog stepCatalog;

    public ScoreService(
            EvaluationService evaluationService,
            StepCatalog stepCatalog) {
        this.evaluationService = evaluationService;
        this.stepCatalog = stepCatalog;
    }

    public Map<String, Object> generateScorecard(Long startupId) {
        Map<String, String> answers =
                evaluationService.getAnswers(startupId);

        Map<String, Integer> categoryScores =
                new LinkedHashMap<>();

        List<String> strengths = new ArrayList<>();
        List<String> weaknesses = new ArrayList<>();
        List<Map<String, Object>> recommendations =
                new ArrayList<>();

        int totalScore = 0;
        int categoryCount = 0;

        for (StepCatalog.AssessmentStep step : stepCatalog.all()) {
            if (step.number() == 15) {
                continue;
            }

            int answered = 0;
            int required = 0;

            for (StepCatalog.AssessmentField field : step.fields()) {
                if (field.required()) {
                    required++;

                    String value = answers.get(field.key());

                    if (value != null && !value.isBlank()) {
                        answered++;
                    }
                }
            }

            int score = required == 0
                    ? 0
                    : (int) Math.round(
                            answered * 100.0 / required);

            categoryScores.put(step.category(), score);
            totalScore += score;
            categoryCount++;

            if (score >= 70) {
                strengths.add(step.title()
                        + ": required information is mostly complete.");
            } else {
                weaknesses.add(step.title()
                        + ": important information is missing.");

                Map<String, Object> recommendation =
                        new LinkedHashMap<>();

                recommendation.put("priority",
                        score < 35 ? "HIGH" : "MEDIUM");
                recommendation.put("area", step.title());
                recommendation.put("currentScore", score);
                recommendation.put("action",
                        "Complete the missing required fields and "
                        + "add supporting evidence.");
                recommendation.put("effort",
                        score < 35 ? "Medium" : "Low");
                recommendation.put("owner", "Founder");

                recommendations.add(recommendation);
            }
        }

        int overallScore = categoryCount == 0
                ? 0
                : (int) Math.round(
                        totalScore * 1.0 / categoryCount);

        String readiness;

        if (overallScore >= 80) {
            readiness = "High readiness";
        } else if (overallScore >= 60) {
            readiness = "Moderate readiness";
        } else if (overallScore >= 40) {
            readiness = "Early readiness";
        } else {
            readiness = "Needs significant preparation";
        }

        Map<String, Object> scorecard =
                new LinkedHashMap<>();

        scorecard.put("overallScore", overallScore);
        scorecard.put("readiness", readiness);
        scorecard.put("categoryScores", categoryScores);
        scorecard.put("strengths", strengths);
        scorecard.put("weaknesses", weaknesses);
        scorecard.put("recommendations", recommendations);
        scorecard.put("scoringMethod",
                "Rule-based completeness score");

        return scorecard;
    }
}
