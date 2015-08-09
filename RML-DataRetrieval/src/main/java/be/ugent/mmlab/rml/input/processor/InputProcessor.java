package be.ugent.mmlab.rml.input.processor;

import be.ugent.mmlab.rml.model.InputSource;
import java.io.InputStream;

/**
 * RML - Data Retrieval Handler : InputProcessor
 *
 * @author andimou
 */
public interface InputProcessor {
    /**
     *
     * @param triplesMap
     * @param source
     * @param triplesMap
     * @return
     */
    public InputStream getInputStream(InputSource source);

}
