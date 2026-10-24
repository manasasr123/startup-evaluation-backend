
package in.startupready.repository;

import in.startupready.domain.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    Optional<Evaluation> findByStartupId(Long startupId);
}
