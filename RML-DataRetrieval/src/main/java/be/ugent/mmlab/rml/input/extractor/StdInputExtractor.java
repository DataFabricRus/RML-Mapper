package be.ugent.mmlab.rml.input.extractor;

import be.ugent.mmlab.rml.model.InputSource;
import be.ugent.mmlab.rml.input.std.StdInputSource;
import be.ugent.mmlab.rml.model.TriplesMap;
import be.ugent.mmlab.rml.model.std.StdTriplesMap;
import be.ugent.mmlab.rml.vocabulary.R2RMLVocabulary;
import be.ugent.mmlab.rml.vocabulary.RMLVocabulary;
import java.util.HashMap;
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
 * RML - Data Retrieval Handler : StdInputExtractor
 *
 * @author andimou
 */
public class StdInputExtractor implements InputExtractor {
    
    // Log
    private static final Logger log = LoggerFactory.getLogger(StdInputExtractor.class);
    
    /**
     *
     * @param rmlMappingGraph
     * @return
     */
    public Map<Resource, InputSource> extractInputResources(Repository repository) {
        Map<Resource, InputSource> inputResources = new HashMap<Resource, InputSource>();
        
        RepositoryResult<Statement> statements = getInputResources(repository);

        inputResources = putInputResources(repository, statements, inputResources);

        return inputResources;
    }
    
    /**
     *
     * @param rmlMappingGraph
     * @return
     */
    protected RepositoryResult<Statement> getInputResources(Repository repository) {
        RepositoryResult<Statement> inputStatements = null;
        try {
            
            RepositoryConnection connection = repository.getConnection();
            ValueFactory vf = connection.getValueFactory();
            
            URI p = vf.createURI(RMLVocabulary.RML_NAMESPACE
                    + RMLVocabulary.RMLTerm.SOURCE);
            inputStatements = connection.getStatements(null, p, null, true);
            
            connection.close();

        } catch (RepositoryException ex) {
            log.error("RepositoryException " + ex);
        }
        return inputStatements;
    }
    
    /**
     *
     * @param rmlMappingGraph
     * @param statements
     * @param inputResources
     * @return
     */
    protected Map<Resource, InputSource> putInputResources(Repository repository,
            RepositoryResult<Statement> statements, Map<Resource, InputSource> inputResources) {
        try {
            RepositoryConnection connection = repository.getConnection();
            ValueFactory vf = connection.getValueFactory();

            URI p = vf.createURI(RMLVocabulary.RML_NAMESPACE
                    + RMLVocabulary.RMLTerm.LOGICAL_SOURCE);

            while (statements.hasNext()) {
                RepositoryResult<Statement> triplesMapsStatements =
                        connection.getStatements(null, p, statements.next().getSubject(), true);

                while (triplesMapsStatements.hasNext()) {
                    try {
                        inputResources.put(
                                //triplesMap resource
                                triplesMapsStatements.next().getSubject(),
                                //input source
                                new StdInputSource(triplesMapsStatements.next().getObject().stringValue()));
                    } catch (Exception ex) {
                        log.error(StdInputExtractor.class.getName() + ex);
                    }
                }
            }
        } catch (RepositoryException ex) {
            log.error("RepositoryException " + ex);
        }
        return inputResources;
    }
    
    /**
     *
     * @param rmlMappingGraph
     * @param inputResource
     * @param inputResources
     */
    public void extractInputSource(
            Repository repository, Resource inputResource,
            Map<Resource, InputSource> inputResources) {
        log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                + "Extract Input Resource : "
                + inputResource.stringValue());
        
        InputSource result = inputResources.get(inputResource);

        // Extract TriplesMap properties
        Set<TriplesMap> triplesMaps =
                extractTriplesMaps(repository, inputResource);
        
        // Add triples maps
        for (TriplesMap triplesMap : triplesMaps) {
            result.setTriplesMap(triplesMap);
        }      

        log.info(Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                + "Extract of Input source : "
                + inputResource.stringValue() + " done.");
    }
    
    
    /**
     *
     * @param rmlMappingGraph
     * @param inputResource
     * @return
     */
    protected Set<TriplesMap> extractTriplesMaps(
            Repository repository, Resource inputResource) {
        Set<TriplesMap> triplesMaps = new HashSet<TriplesMap>();
        try {
            TriplesMap triplesMap ;
            
            RepositoryConnection connection = repository.getConnection();
            
            URI p = getTermURI(repository, RMLVocabulary.RMLTerm.LOGICAL_SOURCE);
            RepositoryResult<Statement> triplesMapStatements = 
                    connection.getStatements(null, p, inputResource, true);
            
            while(triplesMapStatements.hasNext()){
                triplesMap = new StdTriplesMap(
                        null, null, null, triplesMapStatements.next().getSubject().stringValue());
                triplesMaps.add(triplesMap);
            }
            connection.close();
            
        } catch (RepositoryException ex) {
            log.error("RepositoryException " + ex);
        }
        return triplesMaps;
    }
    
    /**
     *
     * @param rmlMappingGraph
     * @param term
     * @param resource
     * @param triplesMap
     * @return
     */
    protected RepositoryResult<Statement> getStatements(
            Repository repository, Enum term,  Resource resource, TriplesMap triplesMap){
        RepositoryResult<Statement> statements = null;
        try {
            URI p = getTermURI(repository, term);
            RepositoryConnection connection = repository.getConnection();
            statements = connection.getStatements(resource, p, null, true);
            
            connection.close();
        } catch (RepositoryException ex) {
            log.error("RepositoryException " + ex);
        }
        return statements;
    }
    
    /**
     *
     * @param rmlMappingGraph
     * @param term
     * @return
     */
    protected static URI getTermURI(Repository repository, Enum term) {
        String namespace = R2RMLVocabulary.R2RML_NAMESPACE;

        if (term instanceof RMLVocabulary.RMLTerm) {
            namespace = RMLVocabulary.RML_NAMESPACE;
        } else if ((term instanceof R2RMLVocabulary.R2RMLTerm)) 
            namespace = R2RMLVocabulary.R2RML_NAMESPACE;
        else
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                    + term + " is not valid.");
        
        RepositoryConnection connection;
        URI uri = null;
        try {
            connection = repository.getConnection();
            ValueFactory vf = connection.getValueFactory();
            uri = vf.createURI(namespace + term);
            connection.close();
        } catch (RepositoryException ex) {
            log.error("RepositoryException " + ex);
        }
        return uri;
    }  

    @Override
    public Set<InputSource> extractInput(Repository repository, Resource resource) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    /*public Map<Resource, InputSource> extractInputResources(SesameDataSet dataset) {
        Map<Resource, InputSource> inputResources = new HashMap<Resource, InputSource>();
        
       

        return inputResources;
    }*/

}
