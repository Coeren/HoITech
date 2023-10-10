package elliot.hoi.tech.Tech;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class TechAreaSerializerTest {

    @Test
    void deserialize() {
        try {
            TechArea area = prepareArea("air_doctrine_tech.txt");

            assertEquals(12, area.getId());
            assertEquals("TECH_AIR_DOC_NAME", area.getName());
            assertEquals("TECH_AIR_DOC_DESC", area.getDetails());
            assertEquals("air_doctrine", area.getCategory());
            assertEquals(6, area.getLevels().size());

            TechLevel level = area.getLevels().get(4);
            assertEquals(12400, level.getId());
            assertEquals("TECH_LEVEL_AIR_DOC_5_NAME", level.getName());
            assertEquals("TECH_LEVEL_AIR_DOC_5_DESC", level.getDetails());
            assertEquals(12, level.getCost());
            assertEquals(720, level.getTime());
            assertEquals(4, level.getApplications().size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void serialize() {
        try {
            InputStream stream = getClass().getClassLoader().getResourceAsStream("air_doctrine_tech.txt");
            stream.mark(1024*1024);
            TechArea area = prepareArea(stream);

            String actual = new String((new TechAreaSerializer()).serialize(area));
            stream.reset();
            String expected = new String(stream.readAllBytes(), "windows-1251");

            assertEquals(StringUtils.normalizeSpace(expected), StringUtils.normalizeSpace(actual));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private TechArea prepareArea(String fileName) throws IOException, InvalidStructureException {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(fileName);

        return prepareArea(stream);
    }
    private static TechArea prepareArea(InputStream stream) throws IOException, InvalidStructureException {
        return (new TechAreaSerializer()).deserialize(stream);
    }
}