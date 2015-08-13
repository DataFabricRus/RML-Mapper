package be.ugent.mmlab.rml.processor.termmap.concrete;

import be.ugent.mmlab.rml.processor.termmap.TermMapProcessor;
import be.ugent.mmlab.rml.processor.termmap.TermMapProcessorFactory;
import be.ugent.mmlab.rml.vocabularies.QLVocabulary.QLTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RML Processor
 *
 * @author andimou
 */
public class ConcreteTermMapFactory implements TermMapProcessorFactory {
    
    // Log
    private static final Logger log = LoggerFactory.getLogger(ConcreteTermMapFactory.class);
    
    @Override
    public TermMapProcessor create(QLTerm term) {
        switch (term){
            case XPATH_CLASS:
                return new XPathTermMapProcessor();
            case CSV_CLASS:
                return new CSVTermMapProcessor();
            case JSONPATH_CLASS:
                return new JSONPathTermMapProcessor();
            case CSS3_CLASS:
                return new CSS3TermMapProcessor();
            default:
                log.error(Thread.currentThread().getStackTrace()[1].getMethodName() + ": "
                        + "The term " + term + "was not defined.");
                return null;
        }
    }

}
