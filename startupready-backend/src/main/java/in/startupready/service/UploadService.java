
package in.startupready.service;

import in.startupready.domain.Evaluation;
import in.startupready.domain.Upload;
import in.startupready.repository.UploadRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class UploadService {

    private static final long MAX_FILE_SIZE = 10L * 1024 * 1024;

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            "pdf", "png", "jpg", "jpeg", "xlsx", "docx", "pptx", "csv");

    private final UploadRepository uploadRepository;
    private final EvaluationService evaluationService;
    private final Path uploadDirectory;

    public UploadService(
            UploadRepository uploadRepository,
            EvaluationService evaluationService,
            @Value("${app.upload-dir:uploads/evidence}") String uploadDir) {

        this.uploadRepository = uploadRepository;
        this.evaluationService = evaluationService;
        this.uploadDirectory = Path.of(uploadDir)
                .toAbsolutePath()
                .normalize();
    }

    @Transactional
    public Upload upload(
            Long startupId,
            int stepNumber,
            String fieldKey,
            MultipartFile file) throws IOException {

        if (stepNumber < 1 || stepNumber > 15) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Step must be between 1 and 15");
        }

        if (fieldKey == null || fieldKey.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Field key is required");
        }

        if (file == null || file.isEmpty()
                || file.getSize() > MAX_FILE_SIZE) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "File must be between 1 byte and 10 MB");
        }

        String originalName = StringUtils.cleanPath(
                file.getOriginalFilename() == null
                        ? "evidence"
                        : file.getOriginalFilename());

        if (originalName.contains("..")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid filename");
        }

        String extension = getExtension(originalName);

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Unsupported file type");
        }

        Evaluation evaluation = evaluationService.getEvaluation(startupId);

        Files.createDirectories(uploadDirectory);

        String storedName = UUID.randomUUID() + "." + extension;
        Path destination = uploadDirectory.resolve(storedName).normalize();

        if (!destination.startsWith(uploadDirectory)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid file path");
        }

        file.transferTo(destination);

        Upload upload = new Upload();
        upload.setEvaluation(evaluation);
        upload.setStepNumber(stepNumber);
        upload.setFieldKey(fieldKey.trim());
        upload.setOriginalFilename(Path.of(originalName).getFileName().toString());
        upload.setStoredFilename(storedName);
        upload.setFileSize(file.getSize());

        try {
            return uploadRepository.save(upload);
        } catch (RuntimeException exception) {
            Files.deleteIfExists(destination);
            throw exception;
        }
    }

    @Transactional(readOnly = true)
    public List<Upload> list(Long startupId) {
        Evaluation evaluation = evaluationService.getEvaluation(startupId);
        return uploadRepository.findByEvaluationIdOrderByUploadedAtDesc(
                evaluation.getId());
    }

    private String getExtension(String filename) {
        int dot = filename.lastIndexOf('.');
        return dot < 0 ? "" : filename.substring(dot + 1).toLowerCase();
    }
}
