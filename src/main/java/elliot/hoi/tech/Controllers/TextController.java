package elliot.hoi.tech.Controllers;

import elliot.hoi.tech.TextMapping.TextMapping;
import elliot.hoi.tech.TextMapping.TextMappingService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/text")
public class TextController {
    private final TextMappingService service;

    public TextController(TextMappingService textMappingService) {
        this.service = textMappingService;
    }

    @GetMapping
    public List<TextMapping> getAllEntities() {
        return service.getAllMappings();
    }
    @GetMapping("/file")
    public ResponseEntity<byte[]> getAsFile() {
        byte[] bytes;
        try {
            bytes = service.export();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(null);
        }
        return ResponseEntity
                .ok()
                .contentType(MediaType.TEXT_PLAIN)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"text.csv\"")
                .body(bytes);
    }
    @PostMapping("/file")
    public UploadFileResponse importMapping(@RequestParam("file") MultipartFile file) {
        String details;
        try {
            details = service.importFile(file);
        } catch (Exception e) {
            return new UploadFileResponse("File import error", e.getMessage());
        }

        return new UploadFileResponse("File imported successfully", details);
    }

    // Get mapping by ID
    @GetMapping("/{id}")
    public Optional<TextMapping> getMappingById(@PathVariable String id) {
        return service.getMapping(id);
    }

    // Update mapping by ID
    @PutMapping("/{id}")
    public TextMapping updateMapping(@PathVariable String id, @RequestBody TextMapping mapping) {
        return service.updateMapping(id, mapping);
    }

    // Delete all mappings
    @DeleteMapping
    public String deleteAllMappings() {
        service.deleteAllMappings();
        return "All mappings have been deleted successfully.";
    }

    // Delete mapping by ID
    @DeleteMapping("/{id}")
    public void deleteMapping(@PathVariable String id) {
        service.deleteMapping(id);
    }
}
