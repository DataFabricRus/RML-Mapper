package be.ugent.mmlab.rml.input.processor;

import be.ugent.mmlab.rml.mapdochandler.extraction.source.concrete.NiFiStdInExtractor;
import be.ugent.mmlab.rml.model.LogicalSource;
import be.ugent.mmlab.rml.model.source.std.StdInSource;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class NiFiStdInProcessor extends AbstractInputProcessor {
    private static final Logger log = LoggerFactory.getLogger(NiFiStdInProcessor.class.getSimpleName());
    private static Map<String, String> paths;

    public NiFiStdInProcessor() {
        if (paths == null) {
            paths = new HashMap<>();
            try {
                InputStream inputStream = System.in;
                JSONParser jsonParser = new JSONParser(0);
                JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(inputStream));
                paths.put(jsonObject.getAsString("streamName"),
                        jsonObject.getAsString("absolute.path") + jsonObject.getAsString("filename")
                );
            } catch (ParseException e) {
                log.error("Could not parse file. Possibly it doesn't contain necessary attributes",e);
            }
        }
    }

    @Override
    public InputStream getInputStream(LogicalSource logicalSource, Map<String, String> parameters) {
        try {
            return new FileInputStream(paths.get(((StdInSource)logicalSource.getSource()).getName()));
        } catch (FileNotFoundException e) {
            log.error("Something wrong with the file",e);
        }
        return null;
    }
}
