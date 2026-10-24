
package in.startupready.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "evidence_uploads")
public class Upload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "evaluation_id", nullable = false)
    private Evaluation evaluation;

    @Column(nullable = false)
    private int stepNumber;

    @Column(nullable = false, length = 100)
    private String fieldKey;

    @Column(nullable = false, length = 255)
    private String originalFilename;

    @Column(nullable = false, unique = true, length = 100)
    private String storedFilename;

    @Column(nullable = false)
    private long fileSize;

    @Column(nullable = false)
    private LocalDateTime uploadedAt;

    public Upload() {
    }

    @PrePersist
    public void beforeInsert() {
        if (uploadedAt == null) {
            uploadedAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public Evaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getFieldKey() {
        return fieldKey;
    }

    public void setFieldKey(String fieldKey) {
        this.fieldKey = fieldKey;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getStoredFilename() {
        return storedFilename;
    }

    public void setStoredFilename(String storedFilename) {
        this.storedFilename = storedFilename;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }
}
