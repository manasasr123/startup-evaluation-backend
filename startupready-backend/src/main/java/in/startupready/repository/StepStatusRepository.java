
package in.startupready.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import in.startupready.domain.StepStatus;

public interface StepStatusRepository
        extends JpaRepository<StepStatus, Long> {

    Optional<StepStatus> findByEvaluationIdAndStepNumber(
            Long evaluationId, int stepNumber);

    List<StepStatus> findByEvaluationIdOrderByStepNumberAsc(
            Long evaluationId);
}
