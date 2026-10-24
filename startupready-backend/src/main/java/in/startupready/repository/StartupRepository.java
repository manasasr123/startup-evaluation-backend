
package in.startupready.repository;

import in.startupready.domain.Startup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StartupRepository extends JpaRepository<Startup, Long> {
}
