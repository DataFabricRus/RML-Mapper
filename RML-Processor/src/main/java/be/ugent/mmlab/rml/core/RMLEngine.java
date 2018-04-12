package be.ugent.mmlab.rml.core;

import be.ugent.mmlab.rml.model.RMLMapping;
import be.ugent.mmlab.rml.model.TriplesMap;
import be.ugent.mmlab.rml.model.dataset.RMLDataset;
import be.ugent.mmlab.rml.processor.RMLProcessor;

import java.io.InputStream;
import java.util.Map;

/**
 * @author andimou
 */
public interface RMLEngine {

    void run(
            RMLMapping mapping,
            String outputFile,
            String outputFormat,
            String graphName,
            Map<String, String> parameters,
            String[] exeTriplesMap,
            String metadataLevel,
            String metadataFormat,
            String metadataVocab
    );

    RMLDataset runRMLMapping(
            RMLDataset dataset,
            RMLMapping rmlMapping,
            String baseIRI,
            Map<String, String> parameters,
            String[] exeTriplesMap
    );

    RMLDataset generateTriplesMapTriples(
            TriplesMap triplesMap,
            Map<String, String> parameters,
            String[] exeTriplesMap,
            RMLDataset dataset,
            InputStream input
    );

    RMLProcessor generateRMLProcessor(
            TriplesMap triplesMap,
            Map<String, String> parameters
    );

    RMLDataset chooseSesameDataSet(
            String repositoryID,
            String pathToNativeStore,
            String outputFormat
    );

}
