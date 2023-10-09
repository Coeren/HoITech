package elliot.hoi.tech.Tech;

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

            assertEquals(area.getId(), 12, "Incorrect area Id");
            assertEquals(area.getName(), "TECH_AIR_DOC_NAME");
            assertEquals(area.getDetails(), "TECH_AIR_DOC_DESC");
            assertEquals(area.getCategory(), "air_doctrine");
            assertEquals(area.getLevels().size(), 6);

            TechLevel level = area.getLevels().stream().toList().get(4);
            assertEquals(level.getId(), 12400);
            assertEquals(level.getName(), "TECH_LEVEL_AIR_DOC_5_NAME");
            assertEquals(level.getDetails(), "TECH_LEVEL_AIR_DOC_5_DESC");
            assertEquals(level.getCost(), 12);
            assertEquals(level.getTime(), 720);
            assertEquals(level.getApplications().size(), 4);
        } catch (Exception e) {
            assertTrue(false, e.getMessage());
        }
    }

    @Test
    @Disabled
    void serialize() {
        try {
            InputStream stream = getClass().getClassLoader().getResourceAsStream("air_doctrine_tech.txt");
            stream.mark(1024*1024);
            TechArea area = prepareArea(stream);

            byte[] serialized = (new TechAreaSerializer()).serialize(area);

            stream.reset();
            assertArrayEquals(serialized, stream.readAllBytes());
        } catch (Exception e) {
            assertTrue(false, e.getMessage());
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