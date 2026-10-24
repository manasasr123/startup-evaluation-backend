
package in.startupready.repository;

import in.startupready.domain.Upload;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UploadRepository extends JpaRepository<Upload, Long> {

    List<Upload> findByEvaluationIdOrderByUploadedAtDesc(Long evaluationId);
}
