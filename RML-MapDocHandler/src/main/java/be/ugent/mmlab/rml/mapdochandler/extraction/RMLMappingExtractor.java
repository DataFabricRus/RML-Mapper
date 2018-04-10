package be.ugent.mmlab.rml.mapdochandler.extraction;

import be.ugent.mmlab.rml.model.TriplesMap;

import java.util.Map;

import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.repository.Repository;

/**
 * *************************************************************************
 * <p>
 * RML - Mapping Document Handler : RMLMappingExtractor
 *
 * @author andimou
 * <p>
 * **************************************************************************
 */
public interface RMLMappingExtractor {

    /**
     * @param repository
     * @return repository
     */
    Repository replaceShortcuts(Repository repository);

    /**
     * @param repository
     * @return repository
     */
    Repository skolemizeStatements(Repository repository);

    /**
     * @param repository
     * @return map of Resource on TriplesMap
     */
    Map<Resource, TriplesMap> extractTriplesMapResources(Repository repository);

}
