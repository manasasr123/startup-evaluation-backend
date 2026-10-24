
package in.startupready.controller;

import java.util.List;
import java.util.Map;

import in.startupready.domain.Startup;
import in.startupready.repository.StartupRepository;
import in.startupready.service.StartupWorkflowService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/startups")
public class StartupController {

    private final StartupRepository startupRepository;
    private final StartupWorkflowService startupWorkflowService;

    public StartupController(
            StartupRepository startupRepository,
            StartupWorkflowService startupWorkflowService) {
        this.startupRepository = startupRepository;
        this.startupWorkflowService = startupWorkflowService;
    }

    public record CreateStartupRequest(
            @NotBlank String name,
            String industry,
            String stage,
            String headquarters) {
    }

    @PostMapping
    public Map<String, Object> createStartup(
            @Valid @RequestBody CreateStartupRequest request) {

        return startupWorkflowService.createStartupWithEvaluation(
                request.name(),
                request.industry(),
                request.stage(),
                request.headquarters());
    }

    @GetMapping
    public List<Map<String, Object>> listStartups() {
        return startupRepository.findAll().stream()
                .map(startup -> Map.<String, Object>of(
                        "startupId", startup.getId(),
                        "name", startup.getName(),
                        "industry", valueOrEmpty(startup.getIndustry()),
                        "stage", valueOrEmpty(startup.getStage()),
                        "headquarters", valueOrEmpty(startup.getHeadquarters())
                ))
                .toList();
    }

    private static String valueOrEmpty(String value) {
        return value == null ? "" : value;
    }
}
