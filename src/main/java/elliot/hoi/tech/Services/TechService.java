package elliot.hoi.tech.Services;

import org.springframework.stereotype.Service;

@Service
public class TechService {
    private final TechRepository techRepo;

    public TechService(TechRepository techRepo) {
        this.techRepo = techRepo;
    }
}
