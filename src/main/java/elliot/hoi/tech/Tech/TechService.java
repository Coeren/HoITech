package elliot.hoi.tech.Tech;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class TechService {
    private final TechAreaRepository techAreaRepo;
    private final TechLevelRepository techLevelRepo;
    private final TechAppRepository techAppRepo;

    public TechService(TechAreaRepository techAreaRepo, TechLevelRepository techLevelRepo, TechAppRepository techAppRepo) {
        this.techAreaRepo = techAreaRepo;
        this.techLevelRepo = techLevelRepo;
        this.techAppRepo = techAppRepo;
    }

    @Transactional(rollbackFor = { IOException.class, RuntimeException.class })
    public String importAreaFile(MultipartFile file) throws IOException, RuntimeException {
        TechAreaSerializer serializer = new TechAreaSerializer();
        TechArea area = serializer.deserialize(file.getBytes());
        techAreaRepo.deleteById(area.getId());
        techAreaRepo.save(area);

        return "Area " + area.getName() + " and all descendants are imported";
    }
}
