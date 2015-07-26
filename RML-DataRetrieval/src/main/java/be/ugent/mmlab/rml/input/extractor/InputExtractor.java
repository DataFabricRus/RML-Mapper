package be.ugent.mmlab.rml.input.extractor;

import be.ugent.mmlab.rml.model.InputSource;
import java.util.Set;
import org.openrdf.model.Resource;
import org.openrdf.repository.Repository;

/**
 * RML - Data Retrieval Handler : InputExtractor
 *
 * @author andimou
 */
public interface InputExtractor {

    /**
     *
     * @param source
     * @param triplesMap
     * @return
     */
    //InputStream getInputStream (String source, TriplesMap triplesMap);
    /**
     *
     * @return
     */
    public Set<InputSource> extractInput(Repository repository, Resource resource);
}
