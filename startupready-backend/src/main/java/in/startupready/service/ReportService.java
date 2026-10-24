
package in.startupready.service;

import in.startupready.catalog.StepCatalog;
import in.startupready.domain.Evaluation;
import in.startupready.reporting.ReportBlock;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    private final EvaluationService evaluationService;
    private final ScoreService scoreService;
    private final StepCatalog stepCatalog;

    public ReportService(
            EvaluationService evaluationService,
            ScoreService scoreService,
            StepCatalog stepCatalog) {
        this.evaluationService = evaluationService;
        this.scoreService = scoreService;
        this.stepCatalog = stepCatalog;
    }

    public List<ReportBlock> buildReport(Long startupId, String type) {
        Evaluation evaluation = evaluationService.getEvaluation(startupId);
        Map<String, String> answers =
                evaluationService.getAnswers(startupId);
        Map<String, Object> scorecard =
                scoreService.generateScorecard(startupId);

        List<ReportBlock> blocks = new ArrayList<>();

        blocks.add(ReportBlock.heading("StartupReady Evaluation Report"));
        blocks.add(ReportBlock.paragraph(
                "Startup: " + evaluation.getStartup().getName()));
        blocks.add(ReportBlock.paragraph(
                "Report type: " + type));
        blocks.add(ReportBlock.paragraph(
                "Generated: " + evaluation.getUpdatedAt()));

        addScorecard(blocks, scorecard);
        addAssessmentAnswers(blocks, answers);

        return blocks;
    }

    private void addScorecard(
            List<ReportBlock> blocks,
            Map<String, Object> scorecard) {

        blocks.add(ReportBlock.subheading("Readiness Scorecard"));

        blocks.add(ReportBlock.paragraph(
                "Overall score: " + scorecard.get("overallScore") + "/100"));

        blocks.add(ReportBlock.paragraph(
                "Readiness: " + scorecard.get("readiness")));

        blocks.add(ReportBlock.subheading("Category Scores"));

        blocks.add(ReportBlock.tableHeader("Category", "Score"));

        @SuppressWarnings("unchecked")
        Map<String, Integer> scores =
                (Map<String, Integer>) scorecard.get("categoryScores");

        scores.forEach((category, score) ->
                blocks.add(ReportBlock.tableRow(
                        category, score + "/100")));

        addTextList(blocks, "Strengths", scorecard.get("strengths"));
        addTextList(blocks, "Areas to Improve", scorecard.get("weaknesses"));
    }

    private void addAssessmentAnswers(
            List<ReportBlock> blocks,
            Map<String, String> answers) {

        blocks.add(ReportBlock.subheading("Assessment Responses"));

        for (StepCatalog.AssessmentStep step : stepCatalog.all()) {
            blocks.add(ReportBlock.subheading(
                    step.number() + ". " + step.title()));

            for (StepCatalog.AssessmentField field : step.fields()) {
                String value = answers.get(field.key());

                if (value != null && !value.isBlank()) {
                    blocks.add(ReportBlock.paragraph(
                            field.label() + ": " + value));
                }
            }
        }
    }

    private void addTextList(
            List<ReportBlock> blocks,
            String heading,
            Object values) {

        blocks.add(ReportBlock.subheading(heading));

        if (values instanceof List<?> items && !items.isEmpty()) {
            for (Object item : items) {
                blocks.add(ReportBlock.bullet(String.valueOf(item)));
            }
        } else {
            blocks.add(ReportBlock.paragraph("No items available."));
        }
    }
}
