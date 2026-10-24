
package in.startupready.controller;

import java.util.Map;

import in.startupready.service.AssessmentNavigationService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/assessments")
public class AssessmentNavigationController {

    private final AssessmentNavigationService navigationService;

    public AssessmentNavigationController(
            AssessmentNavigationService navigationService) {
        this.navigationService = navigationService;
    }

    @PutMapping("/{startupId}/current-step/{stepNumber}")
    public Map<String, Object> navigate(
            @PathVariable Long startupId,
            @PathVariable int stepNumber) {
        try {
            return navigationService.navigate(startupId, stepNumber);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }
}
