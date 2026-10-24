
package in.startupready.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "startups")
public class Startup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 100)
    private String industry;

    @Column(length = 100)
    private String stage;

    @Column(length = 150)
    private String headquarters;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Startup() {
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getHeadquarters() {
        return headquarters;
    }

    public void setHeadquarters(String headquarters) {
        this.headquarters = headquarters;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
