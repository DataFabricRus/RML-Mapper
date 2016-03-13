package be.ugent.mmlab.rml.processor;

import be.ugent.mmlab.rml.metadata.MetadataGenerator;
import be.ugent.mmlab.rml.model.RDFTerm.GraphMap;
import be.ugent.mmlab.rml.model.RDFTerm.SubjectMap;
import be.ugent.mmlab.rml.model.TriplesMap;
import be.ugent.mmlab.rml.model.dataset.MetadataRMLDataset;
import be.ugent.mmlab.rml.model.dataset.RMLDataset;
import java.util.List;
import java.util.Set;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.model.vocabulary.RDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RML Processor
 *
 * @author andimou
 */
public class MetadataSubjectMapProcessor extends StdSubjectMapProcessor implements SubjectMapProcessor {
    // Log
    private static final Logger log = 
            LoggerFactory.getLogger(
            MetadataSubjectMapProcessor.class.getSimpleName());
    
    
    @Override
    public void processSubjectTypeMap(RMLDataset originalDataset, 
    Resource subject, TriplesMap map, Object node) {
        
        SubjectMap subjectMap = map.getSubjectMap();
        boolean flag = false;
        Set<org.openrdf.model.URI> classIRIs = subjectMap.getClassIRIs();
        MetadataRMLDataset dataset = (MetadataRMLDataset) originalDataset ;
        MetadataGenerator metadataGenerator = 
                new MetadataGenerator(dataset.getTarget().toString());
        
        List vocabs = dataset.getMetadataVocab();
        //TODO: Decide if I keep that here or if I move it to separate class
        if (vocabs != null) {
            if (vocabs.isEmpty()) {
                flag = true;
            } else {
                for (Object vocab : vocabs) {
                    if (vocab.toString().equals("prov")) {
                        flag = true;
                    }
                }
            }
        } 
        if (subject != null) {
            for (org.openrdf.model.URI classIRI : classIRIs) {
                if (subjectMap.getGraphMaps().isEmpty()) {
                    List<Statement> triples =
                            dataset.tuplePattern(subject, RDF.TYPE, classIRI);
                    if (triples.isEmpty()) {
                        if(vocabs.contains("void") && 
                                dataset.getMetadataLevel().equals("triplesmap")){
                            dataset.addToRepository(
                                    map, subject, RDF.TYPE, classIRI);
                        }
                        else{
                            log.debug("Adding to general repository...");
                            dataset.add(subject, RDF.TYPE, classIRI);
                        }

                        if (dataset.getMetadataLevel().equals("triple")) {
                            if (flag == true) {
                                metadataGenerator.generateTripleMetaData(dataset,
                                        subject, RDF.TYPE, classIRI);
                            }
                        }
                    }
                } else {
                    for (GraphMap graphMap : subjectMap.getGraphMaps()) {
                        if (graphMap.getConstantValue() != null) {
                            dataset.add(
                                    subject, RDF.TYPE, classIRI,
                                    new URIImpl(graphMap.getConstantValue().toString()));
                        }
                    }
                }
            }
        }
    }
}
