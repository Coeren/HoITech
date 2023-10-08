package elliot.hoi.tech.Controllers;

import elliot.hoi.tech.Services.TextMapping;
import elliot.hoi.tech.Services.TextMappingService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<String> getAsFile() {
        return ResponseEntity
                .ok()
                .contentType(MediaType.TEXT_PLAIN)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"text.csv\"")
                .body(service.export());
    }
    @PostMapping("/file")
    public UploadFileResponse importMapping(@RequestParam("file") MultipartFile file) {
        return service.importFile(file);
    }

    // Get user by ID
    @GetMapping("/{id}")
    public Optional<TextMapping> getMappingById(@PathVariable String id) {
        return service.getMapping(id);
    }

    // Update user by ID
    @PutMapping("/{id}")
    public TextMapping updateMapping(@PathVariable String id, @RequestBody TextMapping mapping) {
        return service.updateMapping(id, mapping);
    }

    // Delete all users
    @DeleteMapping
    public String deleteAllMappings() {
        service.deleteAllMappings();
        return "All mappings have been deleted successfully.";
    }

    // Delete user by ID
    @DeleteMapping("/{id}")
    public void deleteMapping(@PathVariable String id) {
        service.deleteMapping(id);
    }
}
