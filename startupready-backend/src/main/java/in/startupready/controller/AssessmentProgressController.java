
package in.startupready.controller;

import java.util.Map;

import in.startupready.service.AssessmentProgressService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assessments")
public class AssessmentProgressController {

    private final AssessmentProgressService progressService;

    public AssessmentProgressController(
            AssessmentProgressService progressService) {
        this.progressService = progressService;
    }

    @GetMapping("/{startupId}/progress")
    public Map<String, Object> getProgress(
            @PathVariable Long startupId) {
        return progressService.getProgress(startupId);
    }
}
