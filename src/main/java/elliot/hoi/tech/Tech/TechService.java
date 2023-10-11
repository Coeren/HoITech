package elliot.hoi.tech.Tech;

import elliot.hoi.tech.Tech.Repositories.TechAppRepository;
import elliot.hoi.tech.Tech.Repositories.TechAreaRepository;
import elliot.hoi.tech.Tech.Repositories.TechLevelRepository;
import elliot.hoi.tech.Tech.Repositories.TechRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class TechService {
    private final TechAreaRepository techAreaRepo;
    private final TechLevelRepository techLevelRepo;
    private final TechAppRepository techAppRepo;
    private final TechRepository techRepo;

    public TechService(TechAreaRepository techAreaRepo, TechLevelRepository techLevelRepo, TechAppRepository techAppRepo, TechRepository techRepo) {
        this.techAreaRepo = techAreaRepo;
        this.techLevelRepo = techLevelRepo;
        this.techAppRepo = techAppRepo;
        this.techRepo = techRepo;
    }

    @Transactional(rollbackFor = { IOException.class, InvalidStructureException.class })
    public String importAreaFile(MultipartFile file) throws IOException, InvalidStructureException {
        TechAreaSerializer serializer = new TechAreaSerializer();
        TechArea area = serializer.deserialize(file.getInputStream());
        techAreaRepo.deleteById(area.getId());

        for (TechLevel lvl: area.getLevels()) {
            techLevelRepo.save(lvl);
        }

        techAreaRepo.save(area);

        return "Area " + area.getName() + " and all descendants are imported";
    }
    public TechBase getTechById(int id) {
        return techRepo.findById(id).orElseThrow();
    }
    public byte[] exportAreaFile(int id) throws IOException {
        TechArea area = techAreaRepo.findById(id).orElseThrow();
        TechAreaSerializer serializer = new TechAreaSerializer();

        return serializer.serialize(area);
    }
    public TechArea getAreaById(int id) {
        return techAreaRepo.findById(id).orElseThrow();
    }
    public void linkTechs() {
        techAppRepo.findAll().stream().forEach(app -> {
            for (Integer id: app.getReqs())
                app.addPredecessor(techRepo.findById(id).get());
            app.finishReq();
        });
    }
}
