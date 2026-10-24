
package in.startupready.controller;

import in.startupready.domain.Upload;
import in.startupready.service.UploadService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/evidence")
public class UploadController {

    private final UploadService uploadService;

    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping("/{startupId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> upload(
            @PathVariable Long startupId,
            @RequestParam int step,
            @RequestParam String field,
            @RequestParam("file") MultipartFile file) throws IOException {

        Upload saved = uploadService.upload(
                startupId, step, field, file);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Evidence uploaded successfully");
        response.put("id", saved.getId());
        response.put("filename", saved.getOriginalFilename());
        response.put("sizeBytes", saved.getFileSize());
        response.put("step", saved.getStepNumber());

        return response;
    }

    @GetMapping("/{startupId}")
    public List<Map<String, Object>> list(
            @PathVariable Long startupId) {

        return uploadService.list(startupId).stream()
                .map(upload -> {
                    Map<String, Object> item = new LinkedHashMap<>();
                    item.put("id", upload.getId());
                    item.put("filename", upload.getOriginalFilename());
                    item.put("step", upload.getStepNumber());
                    item.put("field", upload.getFieldKey());
                    item.put("sizeBytes", upload.getFileSize());
                    item.put("uploadedAt", upload.getUploadedAt());
                    return item;
                })
                .toList();
    }
}
