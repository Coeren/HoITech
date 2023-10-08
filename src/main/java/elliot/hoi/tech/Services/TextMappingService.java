package elliot.hoi.tech.Services;

import elliot.hoi.tech.Controllers.UploadFileResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TextMappingService {
    private final TextMappingRepository repo;

    public TextMappingService(TextMappingRepository repo) {
        this.repo = repo;
    }

    public List<TextMapping> getAllMappings() {
        return repo.findAll();
    }

    public UploadFileResponse importFile(MultipartFile file) {
        try {
            parseAndSave(file);
        } catch (IOException e) {
            return new UploadFileResponse("File import error", e.getMessage());
        }

        return new UploadFileResponse("File imported successfully", repo.count() + " record(s) processed");
    }

    @Transactional
    public void parseAndSave(MultipartFile file) throws IOException {
        repo.deleteAll();
        repo.saveAll(parseFile(file));
    }

    private Iterable<TextMapping> parseFile(MultipartFile file) throws IOException {
        Pattern pattern = Pattern.compile("^([^;]+);(.+?);", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(new String(file.getBytes()));
        List<TextMapping> res = new ArrayList<>();

        while(matcher.find()) {
            res.add(new TextMapping(matcher.group(1), matcher.group(2)));
        }

        return res;
    }

    public String export() {
        List<TextMapping> ll = repo.findAll();
        StringBuilder sb = new StringBuilder();
        for (TextMapping tm: ll) {
            sb.append(tm.getId()).append(";").append(tm.getValue()).append(";;;;;;;;;;X\r\n");
        }

        return sb.toString();
    }

    public Optional<TextMapping> getMapping(String id) {
        return repo.findById(id);
    }

    public TextMapping updateMapping(String id, TextMapping mapping) {
        return repo.save(mapping);
    }

    public void deleteAllMappings() {
        repo.deleteAll();
    }

    public void deleteMapping(String id) {
        repo.deleteById(id);
    }
}
