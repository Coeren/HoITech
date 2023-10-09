package elliot.hoi.tech.Controllers;

import elliot.hoi.tech.Tech.TechBase;
import elliot.hoi.tech.Tech.TechService;
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

    @PostMapping("/area/file")
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
