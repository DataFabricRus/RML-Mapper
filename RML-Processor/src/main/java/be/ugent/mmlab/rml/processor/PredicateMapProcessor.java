package be.ugent.mmlab.rml.processor;

import be.ugent.mmlab.rml.condition.model.Condition;
import be.ugent.mmlab.rml.logicalsourcehandler.termmap.TermMapProcessor;
import be.ugent.mmlab.rml.logicalsourcehandler.termmap.TermMapProcessorFactory;
import be.ugent.mmlab.rml.logicalsourcehandler.termmap.concrete.ConcreteTermMapFactory;
import be.ugent.mmlab.rml.model.RDFTerm.PredicateMap;
import be.ugent.mmlab.rml.model.TriplesMap;
import be.ugent.mmlab.rml.model.std.StdConditionPredicateMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.openrdf.model.URI;
import org.openrdf.model.impl.URIImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RML Processor
 *
 * @author andimou
 */
public class PredicateMapProcessor {
    private TermMapProcessor termMapProcessor ;
    
    // Log
    private static final Logger log = 
            LoggerFactory.getLogger(PredicateMapProcessor.class);
    
    public PredicateMapProcessor(TriplesMap map){
        TermMapProcessorFactory factory = new ConcreteTermMapFactory();
        this.termMapProcessor = 
                factory.create(map.getLogicalSource().getReferenceFormulation());
    }
    
    
    /**
     * process a predicate map
     *
     * @param predicateMap
     * @param node
     * @return the uri of the extracted predicate
     */
    
    public List<URI> processPredicateMap(PredicateMap predicateMap, Object node) {
        List<URI> uris = new ArrayList<>();
        boolean result = false;
        
        if (predicateMap.getClass().getSimpleName().equals("StdConditionPredicateMap")) {
            log.debug("Conditional Predicate Map");
            StdConditionPredicateMap condPreMap =
                    (StdConditionPredicateMap) predicateMap;
            Set<Condition> conditions = condPreMap.getConditions();
            ConditionProcessor condProcessor = new StdConditionProcessor();
            result = condProcessor.processConditions(node, termMapProcessor, conditions);
        }
        else{
            log.debug("Simple Predicate Map");
            result = true;
        }
        
        if (result == true) {
            // Get the value
            List<String> values =
                    this.termMapProcessor.processTermMap(predicateMap, node);

            for (String value : values) {
                //TODO: add better control
                if (value.startsWith("www.")) {
                    value = "http://" + value;
                }
                uris.add(new URIImpl(value));
            }
        }
        //return the uri
        return uris;
    }

}
