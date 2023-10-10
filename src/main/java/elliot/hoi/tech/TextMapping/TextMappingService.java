package elliot.hoi.tech.TextMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TextMappingService {
    private final TextMappingRepository repo;
    private final Logger logger = LoggerFactory.getLogger(TextMappingService.class);

    public TextMappingService(TextMappingRepository repo) {
        this.repo = repo;
    }

    public List<TextMapping> getAllMappings() {
        return repo.findAll();
    }

    @Transactional(rollbackFor = { IOException.class, RuntimeException.class })
    public String importFile(MultipartFile file) throws IOException, RuntimeException {
        repo.deleteAll();
        repo.saveAll(parseFile(file));

        return repo.count() + " record(s) processed";
    }

    private Iterable<TextMapping> parseFile(MultipartFile file) throws IOException, RuntimeException {
        logger.debug("File {} (content-type = {})", file.getName(), file.getContentType());
        if (logger.isDebugEnabled()) {
            Files.copy(file.getInputStream(), Path.of(file.getName()));
        }
        String fc = new String(file.getBytes(), Charset.forName("windows-1251"));
        Pattern pattern = Pattern.compile("^([^;]+);([^;]*);(.*)$", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(fc);
        List<TextMapping> res = new ArrayList<>();

        while(matcher.find()) {
            logger.debug("Found '{}' key with '{}' value", matcher.group(1), matcher.group(2));
            res.add(new TextMapping(matcher.group(1), matcher.group(2), matcher.group(3) + "\r\n"));
        }

        return res;
    }

    public byte[] export() throws IOException {
        List<TextMapping> ll = repo.findAll(Sort.by("lid").ascending());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream,Charset.forName("windows-1251"));

        for (TextMapping tm: ll) {
            writer.append(tm.getId()).append(";").append(tm.getValue()).append(";").append(tm.getRest());
        }
        writer.flush();

        return outputStream.toByteArray();
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
