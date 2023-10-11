package elliot.hoi.tech.Controllers;

import elliot.hoi.tech.Tech.TechArea;
import elliot.hoi.tech.Tech.TechBase;
import elliot.hoi.tech.Tech.TechService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/tech")
public class TechController {
    public TechController(TechService service) {
        this.service = service;
    }

    @GetMapping
    public String getAllTech() {
        return "here be techs";
    }
    @GetMapping("/{id}")
    public TechBase getTechById(@PathVariable int id) {
        return service.getTechById(id);
    }
    @GetMapping("/area/{id}")
    public TechArea getAreaById(@PathVariable int id) {
        return service.getAreaById(id);
    }
    @GetMapping("/management/linkTechs")
    public void linkTechs() {
        service.linkTechs();
    }
    @GetMapping("/management/export-area/{id}")
    public ResponseEntity<byte[]> exportArea(@PathVariable int id) {
        byte[] bytes;
        String filename;

        try {
            bytes = service.exportAreaFile(id);
            filename = service.getAreaById(id).getCategory() + "_tech.txt";
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage().getBytes());
        }
        return ResponseEntity
                .ok()
                .contentType(MediaType.TEXT_PLAIN)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(bytes);
    }
    @PostMapping("/management/import-area")
    public UploadFileResponse importArea(@RequestParam("file") MultipartFile file) {
        String details;
        try {
            details = service.importAreaFile(file);
        } catch (Exception e) {
            return new UploadFileResponse("File import error", e.getMessage());
        }

        return new UploadFileResponse("File imported successfully", details);
    }

    private final TechService service;
}
