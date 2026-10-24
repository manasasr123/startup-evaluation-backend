
package in.startupready.controller;

import in.startupready.service.AiService;
import in.startupready.service.EvaluationService;
import in.startupready.service.ScoreService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiService aiService;
    private final EvaluationService evaluationService;
    private final ScoreService scoreService;

    public AiController(
            AiService aiService,
            EvaluationService evaluationService,
            ScoreService scoreService) {
        this.aiService = aiService;
        this.evaluationService = evaluationService;
        this.scoreService = scoreService;
    }

    @GetMapping("/status")
    public Map<String, Object> status() {
        return Map.of(
                "available", aiService.isEnabled(),
                "mode", aiService.isEnabled() ? "AI" : "rule-based");
    }

    @PostMapping("/improve")
    public Map<String, Object> improve(
            @RequestBody Map<String, String> request) {

        String text = request.getOrDefault("text", "").trim();

        if (text.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Enter text to improve");
        }

        Optional<String> result = aiService.generate(
                "Improve the startup description professionally. "
                + "Preserve the original meaning and do not invent facts.",
                text);

        return result
                .map(value -> Map.<String, Object>of(
                        "mode", "AI",
                        "text", value))
                .orElseGet(() -> Map.<String, Object>of(
                        "mode", "rule-based",
                        "text", text,
                        "message", "AI is unavailable. Original text is unchanged."));
    }

    @PostMapping("/pitch/{startupId}")
    public Map<String, Object> generatePitch(
            @PathVariable Long startupId) {

        Map<String, String> answers =
                evaluationService.getAnswers(startupId);

        String context = "Startup name: "
                + answers.getOrDefault("startup_name", "")
                + "\nIndustry: "
                + answers.getOrDefault("industry", "")
                + "\nDescription: "
                + answers.getOrDefault(
                        "describe_your_startup_in_one_sentence", "")
                + "\nTarget customer: "
                + answers.getOrDefault("target_customer", "")
                + "\nSolution: "
                + answers.getOrDefault("solution_description", "")
                + "\nBusiness model: "
                + answers.getOrDefault("business_model", "")
                + "\nVision: "
                + answers.getOrDefault("vision", "")
                + "\nMission: "
                + answers.getOrDefault("mission", "");

        Optional<String> pitch = aiService.generate(
                "Write a concise startup elevator pitch using only the "
                + "provided information. Do not invent traction, customers, "
                + "revenue, or other facts.",
                context);

        return pitch
                .map(value -> Map.<String, Object>of(
                        "mode", "AI",
                        "pitch", value))
                .orElseGet(() -> Map.<String, Object>of(
                        "mode", "rule-based",
                        "pitch", context,
                        "message", "AI is unavailable; this is source information, not a generated pitch."));
    }

    @GetMapping("/evaluation/{startupId}")
    public Map<String, Object> finalEvaluation(
            @PathVariable Long startupId) {

        Map<String, Object> scorecard =
                scoreService.generateScorecard(startupId);

        String context = "Scorecard:\n" + scorecard
                + "\nAnswers:\n"
                + evaluationService.getAnswers(startupId);

        Optional<String> evaluation = aiService.generate(
                "Evaluate this startup's readiness using the supplied "
                + "evidence. Distinguish known facts from missing information. "
                + "Explain strengths, risks, and practical next steps. "
                + "Do not guarantee funding or invent facts.",
                context);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("scorecard", scorecard);
        response.put("mode", evaluation.isPresent() ? "AI" : "rule-based");
        response.put(
                "evaluation",
                evaluation.orElse(
                        "Live AI evaluation is unavailable. Review the rule-based scorecard and complete missing fields."));
        return response;
    }
}
