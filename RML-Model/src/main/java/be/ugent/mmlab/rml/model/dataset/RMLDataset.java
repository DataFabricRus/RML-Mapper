package be.ugent.mmlab.rml.model.dataset;

import be.ugent.mmlab.rml.model.RDFTerm.TermMap;
import java.io.OutputStream;
import java.util.List;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.rio.RDFFormat;

/**
 * RML Processor
 * 
 * @author andimou
 */
public interface RMLDataset {

    public void closeRepository();

    public void addFile(String file, RDFFormat NQUADS);

    public void dumpRDF(OutputStream out, RDFFormat outform);

    public boolean isEqualTo(RMLDataset assertMap);

    public int getSize();

    public void add(Resource s, URI p, Value o, Resource... contexts);    
    
    public List<Statement> tuplePattern(Resource s, URI p, Value o,
			Resource... contexts);

    public int getDistinctSubjects();
    
    public int getDistinctObjects();
    
    public void checkDistinctObject(TermMap map, Value o);
}
