
package in.startupready.controller;

import in.startupready.service.ScoreService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/scorecard")
public class ScoreController {

    private final ScoreService scoreService;

    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @GetMapping("/{startupId}")
    public Map<String, Object> getScorecard(
            @PathVariable Long startupId) {

        return scoreService.generateScorecard(startupId);
    }
}
