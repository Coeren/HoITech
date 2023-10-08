package elliot.hoi.tech.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tech")
public class TechController {
    @GetMapping
    public String getAllTech() {
        return "here be techs";
    }
}
