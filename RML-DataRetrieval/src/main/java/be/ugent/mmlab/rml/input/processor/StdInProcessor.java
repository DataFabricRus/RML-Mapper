package be.ugent.mmlab.rml.input.processor;

import be.ugent.mmlab.rml.model.LogicalSource;

import java.io.InputStream;
import java.util.Map;

public class StdInProcessor extends AbstractInputProcessor {

    @Override
    public InputStream getInputStream(LogicalSource logicalSource, Map<String, String> parameters) {
        return System.in;
    }
}
