package elliot.hoi.tech.Tech;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TechAreaSerializer {
    public TechArea deserialize(InputStream file) throws IOException, InvalidStructureException {
        techArea = new TechArea();
        BufferedReader reader = new BufferedReader(new InputStreamReader(file, encoding));
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

        TechArea ret = techArea;
        techArea = null;
        return ret;
    }
    public byte[] serialize(TechArea area) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        writer = new OutputStreamWriter(outputStream, encoding);
        indent = 0;

        writeArea(area);

        writer.flush();
        return outputStream.toByteArray();
    }

    private void writeArea(TechArea area) throws IOException {
        writeLine(area.getComment());
        writeLine("technology = {");

        indent++;
        writeBasics(area, "category = " + area.getCategory());

        int i = 1;
        for (TechLevel level: area.getLevels())
            writeLevel(level, i++);

        indent--;
        writeLine("}");
    }
    private void writeLevel(TechLevel level, int i) throws IOException {
        writeLine("level = { " + level.getComment());

        indent++;
        writeBasics(level);
        writeCommons(level);

        for (TechApplication app: level.getApplications())
            writeApplication(app);

        indent--;
        writeLine("} # Level " + i);
    }
    private void writeApplication(TechApplication app) throws IOException {
        writeLine("application = { " + app.getComment());

        indent++;
        writeBasics(app);
        writeLine("required = { " + StringUtils.join(app.getReqs(), " ") + " }");
        writeLine("chance = " + app.getChance());
        writeCommons(app);
        writeLine("effects = {");
        writer.append(app.getEffects() + newLine); // raw formatting for effects
        writeLine("}");

        indent--;
        writeLine("}");
    }
    private void writeCommons(TechBase base) throws IOException {
        writeLine("cost = " + base.getCost());
        writeLine("time = " + base.getTime());
        writeLine("neg_offset = " + base.getNegOffset());
        writeLine("pos_offset = " + base.getPosOffset());
        writer.append(newLine);
    }
    private void writeBasics(TechObjectBasic tech) throws IOException {
        writeBasics(tech, null);
    }
    private void writeBasics(TechObjectBasic tech, String s) throws IOException {
        writeLine("id = " + tech.getId());
        if (s != null)
            writeLine(s);
        writeLine("name = " + tech.getName());
        writeLine("desc = " + tech.getDetails());
        writer.append(newLine);
    }
    private void writeLine(String line) throws IOException {
        if (line == null)
            return;

        for (int i = 0; i < indent; i++)
            writer.append("\t");

        writer.append(line).append(newLine);
    }

    private void parseEffects(String line) {
        if (closingPattern.matcher(line).matches())
            state = ParsingState.Application;
        else {
            if (StringUtils.isNotBlank(techApplication.getEffects()))
                techApplication.appendEffect(newLine);

            techApplication.appendEffect(line);
        }
    }
    private void parseApplication(String line) throws InvalidStructureException {
        Matcher chanceMatcher = chancePattern.matcher(line);
        Matcher reqMatcher = reqPattern.matcher(line);

        if (parseTechBasics(line, techApplication) || parseTechCommons(line, techApplication))
            ;
        else if (chanceMatcher.matches())
            techApplication.setChance(Integer.parseInt(chanceMatcher.group(1)));
        else if (reqMatcher.matches()) {
            for (String s: StringUtils.split(reqMatcher.group(1)))
                techApplication.appendReq(Integer.parseInt(s));
        } else if (effectsPattern.matcher(line).matches()) {
            state = ParsingState.Effects;
        } else if (closingPattern.matcher(line).matches()) {
            if (!techApplication.checkFields())
                throw new InvalidStructureException("Not all fields in tech app are filled");

            techLevel.addApplication(techApplication);
            techApplication = null;
            state = ParsingState.Level;
        }
        else
            throw new InvalidStructureException("Unexpected '" + line + "' line during tech app parsing");
    }
    private void parseLevel(String line) throws InvalidStructureException {
        Matcher appMatcher = appPattern.matcher(line);

        if (parseTechBasics(line, techLevel) || parseTechCommons(line, techLevel))
            ;
        else if (appMatcher.matches()) {
            techApplication = new TechApplication();
            techApplication.setComment(appMatcher.group(1));
            techApplication.setTheory(techLevel);
            state = ParsingState.Application;
        }
        else if (closingPattern.matcher(line).matches()) {
            if (!techLevel.checkFields())
                throw new InvalidStructureException("Not all fields in tech level are filled");

            techArea.addLevel(techLevel);
            techLevel = null;
            state = ParsingState.Area;
        }
        else
            throw new InvalidStructureException("Unexpected '" + line + "' line during tech level parsing");
    }
    private void parseTechnology(String line) throws InvalidStructureException {
        Matcher categoryMatcher = categoryPattern.matcher(line);
        Matcher levelMatcher = levelPattern.matcher(line);

        if (commentPattern.matcher(line).matches() || parseTechBasics(line, techArea))
            ;
        else if (categoryMatcher.matches())
            techArea.setCategory(categoryMatcher.group(1));
        else if (levelMatcher.matches()) {
            techLevel = new TechLevel(techArea);
            techLevel.setComment(levelMatcher.group(1));
            state = ParsingState.Level;
        }
        else if (closingPattern.matcher(line).matches()) {
            if (!techArea.checkFields())
                throw new InvalidStructureException("Not all fields in tech area are filled");

            state = ParsingState.Outer;
        }
        else
            throw new InvalidStructureException("Unexpected '" + line + "' line during tech area parsing");
    }
    private void parseOuter(String line) throws InvalidStructureException {
        if (commentPattern.matcher(line).matches())
            techArea.appendComment(line + newLine);
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

    private enum ParsingState {
        Outer,
        Area,
        Level,
        Application,
//        Required,
        Effects
    }
    private final String encoding = "windows-1251";
    private final String newLine = "\r\n";
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
    private ParsingState state;
    private TechArea techArea;
    private TechLevel techLevel;
    private TechApplication techApplication;
    private OutputStreamWriter writer;
    private int indent;
}
