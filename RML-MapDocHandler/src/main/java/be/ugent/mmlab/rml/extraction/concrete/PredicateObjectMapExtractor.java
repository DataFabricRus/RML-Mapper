package be.ugent.mmlab.rml.extraction.concrete;

import be.ugent.mmlab.rml.model.RDFTerm.GraphMap;
import be.ugent.mmlab.rml.model.RDFTerm.ObjectMap;
import be.ugent.mmlab.rml.model.RDFTerm.PredicateMap;
import be.ugent.mmlab.rml.model.PredicateObjectMap;
import be.ugent.mmlab.rml.model.RDFTerm.ReferencingObjectMap;
import be.ugent.mmlab.rml.model.TriplesMap;
import be.ugent.mmlab.rml.model.std.StdPredicateObjectMap;
import be.ugent.mmlab.rml.vocabulary.R2RMLVocabulary;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;

/**
 * *************************************************************************
 *
 * RML - Mapping Document Handler : PredicateObjectMapExtractor
 *
 *
 * @author andimou
 *
 ***************************************************************************
 */
public class PredicateObjectMapExtractor {
    
    // Log
    static final Logger log = LoggerFactory.getLogger(PredicateObjectMapExtractor.class);
    
    public PredicateObjectMap extractPredicateObjectMap(
            Repository repository,
            Resource triplesMapSubject,
            Resource predicateObject,
            Set<GraphMap> savedGraphMaps,
            Map<Resource, TriplesMap> triplesMapResources,
            TriplesMap triplesMap) {
        RepositoryResult<Statement> predicate_statements;
        RepositoryResult<Statement> object_statements;
        PredicateObjectMap predicateObjectMap = null;
        try {
            RepositoryConnection connection = repository.getConnection();
            ValueFactory vf = connection.getValueFactory();
            
            URI p = vf.createURI(R2RMLVocabulary.R2RML_NAMESPACE
                    + R2RMLVocabulary.R2RMLTerm.PREDICATE_MAP);
            predicate_statements = connection.getStatements(predicateObject, p, null, true);
            
            Set<PredicateMap> predicateMaps = new HashSet<PredicateMap>();
            while (predicate_statements.hasNext()) {
                PredicateMapExtractor predMapExtractor = new PredicateMapExtractor();
                PredicateMap predicateMap = predMapExtractor.extractPredicateMap(
                        repository, predicate_statements.next(),
                        savedGraphMaps, triplesMap);
                predicateMaps.add(predicateMap);
            }
            
            URI o = vf.createURI(R2RMLVocabulary.R2RML_NAMESPACE
                    + R2RMLVocabulary.R2RMLTerm.OBJECT_MAP);
            // Extract object maps
            object_statements = connection.getStatements(predicateObject, o, null, true);
            
            Set<ObjectMap> objectMaps = new HashSet<ObjectMap>();
            Set<ReferencingObjectMap> refObjectMaps = new HashSet<ReferencingObjectMap>();
            while (object_statements.hasNext()) {
                ReferencingObjectMapExtractor refObjMapExtractor = new ReferencingObjectMapExtractor();
                refObjectMaps = refObjMapExtractor.processReferencingObjectMap(
                        repository, object_statements, savedGraphMaps,
                        triplesMapResources, triplesMap, triplesMapSubject, predicateObject);
                if (refObjectMaps.isEmpty()) {
                    ObjectMapExtractor objMapExtractor = new ObjectMapExtractor();
                    ObjectMap objectMap = objMapExtractor.extractObjectMap(repository,
                            (Resource) object_statements.next().getObject(), savedGraphMaps, triplesMap);
                    try {
                        objectMap.setOwnTriplesMap(triplesMapResources.get(triplesMapSubject));
                    } catch (Exception ex) {
                        log.error(Thread.currentThread().getStackTrace()[1].getMethodName() + ": " + ex);
                    }
                    objectMaps.add(objectMap);
                }
                predicateObjectMap = new StdPredicateObjectMap(
                        predicateMaps, objectMaps, refObjectMaps);
                GraphMapExtractor graphMapExtractor = new GraphMapExtractor();
                graphMapExtractor.processGraphMaps(
                        repository, predicateObject, triplesMap, predicateObjectMap, savedGraphMaps);

                log.debug(
                        Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                        + "Extract predicate-object map done.");

            }
            connection.close();
        } catch (RepositoryException ex) {
            log.error("RepositoryException " + ex);
        }
        return predicateObjectMap;
    }

}
