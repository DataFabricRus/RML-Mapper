package be.ugent.mmlab.rml.model.dataset;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.RepositoryConfig;
import org.openrdf.repository.config.RepositoryConfigException;
import org.openrdf.repository.manager.LocalRepositoryManager;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.repository.sail.config.SailRepositoryConfig;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFWriter;
import org.openrdf.rio.Rio;
import org.openrdf.sail.nativerdf.NativeStore;
import org.openrdf.sail.nativerdf.config.NativeStoreConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RML Processor
 * 
 * @author andimou
 */
public class FileDataset extends StdRMLDataset {

    // Log
    private static final Logger log = 
            LoggerFactory.getLogger(FileDataset.class);
    
    private File target;
    private RDFFormat format = RDFFormat.NTRIPLES;    ;

    public FileDataset(String target) {
        try {
            this.target = new File(target);
            String indexes = "spoc";
            
            // Get the operating system temporary directory
            String property = "java.io.tmpdir";
            String tempDir = System.getProperty(property) + "/RML-Processor";
            log.debug("OS current temporary directory is " + tempDir); 
            repository = new SailRepository(new NativeStore(new File(tempDir), indexes));
            repository.initialize();

        } catch (RepositoryException ex) {
            log.error("Repository Exception " + ex);
        }
    }
    
    /**
     *
     * @param target
     * @param outputFormat
     * @param manager
     * @param repositoryID
     */
    public FileDataset(String target, String outputFormat, 
            LocalRepositoryManager manager, String repositoryID) {

        this.target = new File(target);

        try {
            //TODO: Move that to super
            String indexes = "spoc";
            SailRepositoryConfig repositoryTypeSpec = 
                    new SailRepositoryConfig(new NativeStoreConfig(indexes));
            RepositoryConfig repConfig = 
                    new RepositoryConfig(repositoryID, repositoryTypeSpec);
            manager.addRepositoryConfig(repConfig);
            repository = manager.getRepository(repositoryID);
            repository.initialize();
            
            //Set the final output
            this.target = new File(target);
            
            //Clean up repo from previous use
            RepositoryConnection con = repository.getConnection();
            con.clear();
            con.commit();
            con.close();
            
            //TODO: Spring it!
            switch (outputFormat) {
                case "ntriples": 
                    this.format = RDFFormat.NTRIPLES; 
                    break;
                case "n3": 
                    this.format = RDFFormat.N3;
                    break;
                case "turtle": 
                    this.format = RDFFormat.TURTLE;
                    break;
                case "nquads": 
                    this.format = RDFFormat.NQUADS;
                    break;
                case "rdfxml": 
                    this.format = RDFFormat.RDFXML;
                    break;
                case "rdfjson": 
                    this.format = RDFFormat.RDFJSON;
                    break;
                case "jsonld": 
                    this.format = RDFFormat.JSONLD;
                    break;
            }
        } catch (RepositoryException ex) {
            log.error("Repository Exception " + ex);
        } catch (RepositoryConfigException ex) {
            log.error("Repository Config Exception " + ex);
        }

    }
    
    @Override
    public void add(Resource s, URI p, Value o, Resource... contexts) {
        log.debug("Add triple (" + s.stringValue()
                + ", " + p.stringValue() + ", " + o.stringValue() + ").");
        
        try {
            RepositoryConnection con = repository.getConnection();
            try {
                ValueFactory myFactory = con.getValueFactory();
                Statement st = myFactory.createStatement((Resource) s, p,
                        (Value) o);
                //Check if Subject does not exist in the dataset
                checkDistinctSubject(s);
                checkDistinctObject(o);
                con.add(st, contexts);
                triples++;
                con.commit();                
            } catch (Exception ex) {
                log.error("Exception " + ex);
            } finally {
                con.close();
            }
        } catch (Exception ex) {
            log.error("Exception " + ex);
        }
    }
    
    /**
     * Close current repository.
     *
     */
    @Override
    public void closeRepository() {
        try {
            RepositoryConnection con = repository.getConnection();
            RDFWriter writer;

            //Prepare writer
            if(this.target.exists()){
                this.target.delete();
                this.target.createNewFile();
            }

            //Prepare file output stream
            FileOutputStream output = 
                    new FileOutputStream(this.target);
            
            writer = Rio.createWriter(this.format, output);
            con.export(writer);
            con.commit();
            con.close();
            repository.shutDown();
            
            String property = "java.io.tmpdir";
            String tempDir = System.getProperty(property) + "/RML-Processor";
            File file = new File(tempDir);
            file.delete();

        } catch (RepositoryException ex) {
            log.error("Repository Exception " + ex);
        } catch (FileNotFoundException ex) {
            log.error("File Not Found Exception " + ex);
        } catch (RDFHandlerException ex) {
            log.error("RDF Handler Exception " + ex);
        } catch (IOException ex) {
            log.error("IO Exception " + ex);
        } 
    }
}