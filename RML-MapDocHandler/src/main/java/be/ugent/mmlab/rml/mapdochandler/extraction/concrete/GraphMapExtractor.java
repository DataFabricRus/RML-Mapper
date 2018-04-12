package be.ugent.mmlab.rml.mapdochandler.extraction.concrete;

import be.ugent.mmlab.rml.extraction.TermExtractor;
import be.ugent.mmlab.rml.model.RDFTerm.GraphMap;
import be.ugent.mmlab.rml.model.PredicateObjectMap;
import be.ugent.mmlab.rml.model.TriplesMap;
import be.ugent.mmlab.rml.model.std.StdGraphMap;
import be.ugent.mmlab.rml.vocabularies.R2RMLVocabulary;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;

/**
 * *************************************************************************
 * <p>
 * RML - Mapping Document Handler : GraphMapExtractor
 *
 * @author andimou
 * <p>
 * **************************************************************************
 */

public class GraphMapExtractor extends StdTermMapExtractor {

    // Log
    static final Logger log = LoggerFactory.getLogger(GraphMapExtractor.class.getSimpleName());

    public Set<GraphMap> extractGraphMapValues(Repository repository, Set<Value> graphMapValues) {

        Set<GraphMap> graphMaps = new HashSet<>();

        if (graphMapValues != null)
            for (Value graphMap : graphMapValues) {
                // Create associated graphMap if it has not already created
                boolean found = false;
                GraphMap graphMapFound = null;

                if (found) {
                    graphMaps.add(graphMapFound);
                } else {
                    GraphMap newGraphMap;
                    newGraphMap = extractGraphMap(repository, (Resource) graphMap);
                    graphMaps.add(newGraphMap);
                }
            }

        return graphMaps;
    }

    protected GraphMap extractGraphMap(
            Repository repository, Resource graphMap) {
        GraphMap result = null;
        log.debug("Extract Graph Map...");
        try {
            RepositoryConnection connection = repository.getConnection();

            extractProperties(repository, graphMap);

            try {
                result = new StdGraphMap(constantValue, stringTemplate,
                        inverseExpression, referenceValue, termType);
                log.debug("Graph map extracted.");
            } catch (Exception ex) {
                log.error("Exception " + ex);
            }

            connection.close();
        } catch (RepositoryException ex) {
            log.error("RepositoryException " + ex);
        }
        return result;
    }

    public PredicateObjectMap processGraphMaps(
            Repository repository,
            Resource predicateObject,
            PredicateObjectMap predicateObjectMap) {
        // Add graphMaps
        Set<GraphMap> graphMaps = new HashSet<>();
        Set<Value> graphMapValues = TermExtractor.extractValuesFromResource(
                repository,
                predicateObject,
                R2RMLVocabulary.R2RMLTerm.GRAPH_MAP
        );

        if (graphMapValues != null) {
            graphMaps = extractGraphMapValues(repository, graphMapValues);
        }

        predicateObjectMap.setGraphMaps(graphMaps);
        return predicateObjectMap;
    }

}
