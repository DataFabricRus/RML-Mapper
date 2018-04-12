package be.ugent.mmlab.rml.input.processor;

import be.ugent.mmlab.rml.model.LogicalSource;

import java.io.InputStream;
import java.util.Map;

/**
 * RML - Data Retrieval Handler : InputProcessor
 *
 * @author andimou
 */
public interface SourceProcessor {
    /**
     * @param logicalSource
     * @param parameters
     * @return
     */
    InputStream getInputStream(LogicalSource logicalSource, Map<String, String> parameters);

    boolean hasNextInputStream();

}
