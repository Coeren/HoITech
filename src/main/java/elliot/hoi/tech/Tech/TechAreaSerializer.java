package elliot.hoi.tech.Tech;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

public class TechAreaSerializer {
    public TechArea deserialize(InputStream file) throws IOException, InvalidStructureException {
        area = new TechArea();
        reader = new BufferedReader(new InputStreamReader(file, encoding));
        state = ParsingState.Outer;

        while (reader.ready()) {
            String line = reader.readLine();
            if (line.isBlank())
                continue;

            switch (state) {
                case Outer -> parseOuter(line);
                case Area -> parseTechnology(line);
                case Level -> parseLevel(line);
                case Application -> parseApplication(line);
//                case Required -> parseRequired(line);
                case Effects -> parseEffects(line);
            }
        }

        TechArea ret = area;
        area = null;
        return ret;
    }

    private void parseEffects(String line) {
        if (closingPattern.matcher(line).matches())
            state = ParsingState.Application;
        else {
            if (StringUtils.isNotBlank(app.getEffects()))
                app.appendEffect("\r\n");

            app.appendEffect(line);
        }
    }

    private void parseApplication(String line) throws InvalidStructureException {
        Matcher chanceMatcher = chancePattern.matcher(line);
        Matcher reqMatcher = reqPattern.matcher(line);

        if (parseTechBasics(line, app) || parseTechCommons(line, app))
            return;
        else if (chanceMatcher.matches())
            app.setChance(Integer.parseInt(chanceMatcher.group(1)));
        else if (reqMatcher.matches()) {
            for (String s: StringUtils.split(reqMatcher.group(1)))
                app.appendReq(Integer.parseInt(s));
        } else if (effectsPattern.matcher(line).matches()) {
            state = ParsingState.Effects;
        } else if (closingPattern.matcher(line).matches()) {
            if (!app.checkFields())
                throw new InvalidStructureException("Not all fields in tech app are filled");

            level.addApplication(app);
            app = null;
            state = ParsingState.Level;
        }
        else
            throw new InvalidStructureException("Unexpected '" + line + "' line during tech app parsing");
    }

    private void parseLevel(String line) throws InvalidStructureException {
        Matcher appMatcher = appPattern.matcher(line);

        if (parseTechBasics(line, level) || parseTechCommons(line, level))
            return;
        else if (appMatcher.matches()) {
            app = new TechApplication();
            app.setComment(appMatcher.group(1));
            app.setTheory(level);
            state = ParsingState.Application;
        }
        else if (closingPattern.matcher(line).matches()) {
            if (!level.checkFields())
                throw new InvalidStructureException("Not all fields in tech level are filled");

            area.addLevel(level);
            level = null;
            state = ParsingState.Area;
        }
        else
            throw new InvalidStructureException("Unexpected '" + line + "' line during tech level parsing");
    }
    private void parseTechnology(String line) throws InvalidStructureException {
        Matcher categoryMatcher = categoryPattern.matcher(line);
        Matcher levelMatcher = levelPattern.matcher(line);

        if (commentPattern.matcher(line).matches() || parseTechBasics(line, area))
            return;
        else if (categoryMatcher.matches())
            area.setCategory(categoryMatcher.group(1));
        else if (levelMatcher.matches()) {
            level = new TechLevel();
            level.setComment(levelMatcher.group(1));
            state = ParsingState.Level;
        }
        else if (closingPattern.matcher(line).matches()) {
            if (!area.checkFields())
                throw new InvalidStructureException("Not all fields in tech area are filled");

            state = ParsingState.Outer;
        }
        else
            throw new InvalidStructureException("Unexpected '" + line + "' line during tech area parsing");
    }
    private void parseOuter(String line) throws InvalidStructureException {
        if (commentPattern.matcher(line).matches())
            area.appendComment(line + "\r\n");
        else if (technologyPattern.matcher(line).matches())
            state = ParsingState.Area;
        else
            throw new InvalidStructureException("Unexpected '" + line + "' line during tech area parsing");
    }
    // parse out cost, time & offsets
    private boolean parseTechCommons(String line, TechBase object) {
        Matcher costMatcher = costPattern.matcher(line);
        Matcher timeMatcher = timePattern.matcher(line);
        Matcher negMatcher = negPattern.matcher(line);
        Matcher posMatcher = posPattern.matcher(line);

        if (costMatcher.matches())
            object.setCost(Integer.parseInt(costMatcher.group(1)));
        else if (timeMatcher.matches())
            object.setTime(Integer.parseInt(timeMatcher.group(1)));
        else if (negMatcher.matches())
            object.setNegOffset(Integer.parseInt(negMatcher.group(1)));
        else if (posMatcher.matches())
            object.setPosOffset(Integer.parseInt(posMatcher.group(1)));
        else
            return false;

        return true;
    }
    // parse out id, name & desc
    private boolean parseTechBasics(String line, TechObjectBasic object) {
        Matcher idMatcher = idPattern.matcher(line);
        Matcher nameMatcher = namePattern.matcher(line);
        Matcher descMatcher = descPattern.matcher(line);

        if (idMatcher.matches())
            object.setId(Integer.parseInt(idMatcher.group(1)));
        else if (nameMatcher.matches())
            object.setName(nameMatcher.group(1));
        else if (descMatcher.matches())
            object.setDetails(descMatcher.group(1));
        else
            return false;

        return true;
    }

    public byte[] serialize(TechArea area) {
        return null;
    }

    private enum ParsingState {
        Outer,
        Area,
        Level,
        Application,
//        Required,
        Effects
    }
    private final String encoding = "windows-1252";
    private final Pattern commentPattern = Pattern.compile("^\\s*#.*$");
    private final Pattern closingPattern = Pattern.compile("^\\s*}.*$");
    private final Pattern technologyPattern = Pattern.compile("^\\s*technology\\s*=\\s*\\{\\s*$");
    private final Pattern idPattern = Pattern.compile("^\\s*id\\s*=\\s*(\\d+)[^}]*$");
    private final Pattern categoryPattern = Pattern.compile("^\\s*category\\s*=\\s*(\\w+)[^}]*$");
    private final Pattern namePattern = Pattern.compile("^\\s*name\\s*=\\s*(\\w+)[^}]*$");
    private final Pattern descPattern = Pattern.compile("^\\s*desc\\s*=\\s*(\\w+)[^}]*$");
    private final Pattern levelPattern = Pattern.compile("^\\s*level\\s*=\\s*\\{\\s*(#.+)*\\s*$");
    private final Pattern costPattern = Pattern.compile("^\\s*cost\\s*=\\s*(\\d+)[^}]*$");
    private final Pattern timePattern = Pattern.compile("^\\s*time\\s*=\\s*(\\d+)[^}]*$");
    private final Pattern negPattern = Pattern.compile("^\\s*neg_offset\\s*=\\s*(\\d+)[^}]*$");
    private final Pattern posPattern = Pattern.compile("^\\s*pos_offset\\s*=\\s*(\\d+)[^}]*$");
    private final Pattern appPattern = Pattern.compile("^\\s*application\\s*=\\s*\\{\\s*(#.+)*\\s*$");
    private final Pattern chancePattern = Pattern.compile("^\\s*chance\\s*=\\s*(\\d+)[^}]*$");
    private final Pattern reqPattern = Pattern.compile("^\\s*required\\s*=\\s*\\{(.*?)}\\s*$");
    private final Pattern effectsPattern = Pattern.compile("^\\s*effects\\s*=\\s*\\{\\s*$");
    private BufferedReader reader;
    private ParsingState state;
    private TechArea area;
    private TechLevel level;
    private TechApplication app;
}
