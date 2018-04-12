package be.ugent.mmlab.rml.model.dataset;

import be.ugent.mmlab.rml.model.TriplesMap;

import java.io.OutputStream;
import java.util.List;

import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.rio.RDFFormat;

/**
 * RML Processor
 *
 * @author andimou
 */
public interface RMLDataset {

    void closeRepository();

    void addFile(String file, RDFFormat NQUADS);

    void dumpRDF(OutputStream out, RDFFormat outform);

    void dumpRDF(OutputStream out, String outform);

    boolean isEqualTo(RMLDataset assertMap);

    int getSize();

    String getMetadataLevel();

    List getMetadataVocab();

    void addToRepository(TriplesMap map, Resource s, IRI p, Value o, Resource... contexts);

    Repository getRepository();

    void add(Resource s, IRI p, Value o, Resource... contexts);

    List<Statement> tuplePattern(Resource s, IRI p, Value o, Resource... contexts);

    RDFFormat selectFormat(String outputFormat);
}
