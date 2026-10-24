
package in.startupready.controller;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import in.startupready.reporting.ReportBlock;
import in.startupready.reporting.ReportRenderer;
import in.startupready.service.ReportService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;
    private final ReportRenderer reportRenderer;

    public ReportController(
            ReportService reportService,
            ReportRenderer reportRenderer) {
        this.reportService = reportService;
        this.reportRenderer = reportRenderer;
    }

    @GetMapping("/{startupId}/{type}")
    public ResponseEntity<?> downloadReport(
            @PathVariable Long startupId,
            @PathVariable String type,
            @RequestParam(defaultValue = "pdf") String format) {

        List<ReportBlock> blocks = reportService.buildReport(startupId, type);

        byte[] content;
        String contentType;
        String extension;

        switch (format.toLowerCase()) {
            case "pdf" -> {
                try {
                    content = reportRenderer.pdf(blocks);
                } catch (Exception e) {
                    return ResponseEntity.internalServerError()
                            .body(Map.of("error", "Could not generate PDF report"));
                }
                contentType = "application/pdf";
                extension = "pdf";
            }
            case "docx" -> {
                try {
                    content = reportRenderer.docx(blocks);
                } catch (Exception e) {
                    return ResponseEntity.internalServerError()
                            .body(Map.of("error", "Could not generate Word report"));
                }
                contentType =
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
                extension = "docx";
            }
            case "md" -> {
                content = reportRenderer.markdown(blocks);
                contentType = "text/markdown; charset=UTF-8";
                extension = "md";
            }
            default -> {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Unsupported format. Use pdf, docx, or md."));
            }
        }

        String filename = "startup-" + startupId + "-" + type + "." + extension;

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType(contentType))
                .body(content);
    }
}
