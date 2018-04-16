package be.ugent.mmlab.rml.mapdochandler.extraction.source.concrete;

import be.ugent.mmlab.rml.mapdochandler.extraction.std.StdSourceExtractor;
import be.ugent.mmlab.rml.model.Source;
import be.ugent.mmlab.rml.model.source.std.StdInSource;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class NiFiStdInExtractor extends StdSourceExtractor {
    private static final Logger log = LoggerFactory.getLogger(NiFiStdInExtractor.class.getSimpleName());
    public static final String NAMESPACE = "http://datafabric.cc#";
    public static final String STREAM_NAME = "streamName";

    @Override
    public Set<Source> extractSources(Repository repository, Value resource) {
        Set<Source> inputSources = new HashSet<>();
        String streamName = extractTerm(repository, resource);
        inputSources.add(new StdInSource(streamName, "i do not know how it is used"));
        return inputSources;
    }


    private String extractTerm(Repository repository, Value resource) {
        String value = null;
        RepositoryConnection connection = null;
        try {
            connection = repository.getConnection();
            ValueFactory vf = connection.getValueFactory();
            IRI predicate = vf.createIRI(NAMESPACE + STREAM_NAME);
            RepositoryResult<Statement> statements = connection.getStatements((Resource) resource, predicate, null, true);
            if (statements.hasNext()) {
                return statements.next().getObject().stringValue();
            }
        } catch (RepositoryException ex) {
            log.error("Repository Exception " + ex);
        } finally {
            if (connection != null && connection.isOpen()) {
                connection.close();
            }
        }
        return value;
    }
}
