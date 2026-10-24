
package in.startupready.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "evaluations",
    uniqueConstraints = @UniqueConstraint(
        columnNames = "startup_id"
    )
)
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "startup_id", nullable = false)
    private Startup startup;

    @Column(nullable = false)
    private int currentStep = 1;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String answersJson = "{}";

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Evaluation() {
    }

    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Startup getStartup() {
        return startup;
    }

    public void setStartup(Startup startup) {
        this.startup = startup;
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
    }

    public String getAnswersJson() {
        return answersJson;
    }

    public void setAnswersJson(String answersJson) {
        this.answersJson = answersJson;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
