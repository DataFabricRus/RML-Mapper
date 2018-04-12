
package be.ugent.mmlab.rml.processor;

import be.ugent.mmlab.rml.model.TriplesMap;
import be.ugent.mmlab.rml.vocabularies.QLVocabulary;

import java.util.Map;

/**
 * RML Processor
 * <p>
 * Interface for creating processors
 *
 * @author mielvandersande
 */
public interface RMLProcessorFactory {

    /**
     * @param term
     * @return
     */
    RMLProcessor create(
            QLVocabulary.QLTerm term,
            Map<String, String> parameters,
            TriplesMap map);

}
