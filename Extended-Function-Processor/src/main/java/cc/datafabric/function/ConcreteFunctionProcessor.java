package cc.datafabric.function;

import cc.datafabric.function.handler.FunctionHandler;
import org.eclipse.rdf4j.model.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Map;

public class ConcreteFunctionProcessor {
    private static ConcreteFunctionProcessor instance = null;
    private static FunctionHandler handler;

    protected ConcreteFunctionProcessor() {
        // Exists only to defeat instantiation.
    }

    public static ConcreteFunctionProcessor getInstance() {
        if (instance == null) {
            String basePath = "";
            try {
                basePath = System.getProperty("user.dir");
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            log.debug("basePath: " + basePath);
            handler = new FunctionHandler(basePath + "/resources/functions");
            instance = new ConcreteFunctionProcessor();
        }
        return instance;
    }

    // Log
    private static final Logger log =
            LoggerFactory.getLogger(ConcreteFunctionProcessor.class);

    public ArrayList<Value> processFunction(
            String function, Map<String, String> parameters) {
        AbstractFunctionImpl fn = handler.get(function);

        if (fn == null) {
            log.error("An implementation of function " + function + " was not found in `resources/functions`.");
            //TODO: wmaroy:
            return new ArrayList<>();
        }
        log.debug(parameters.toString());
        ArrayList<Value> result = fn.execute(parameters);
        if(result != null) {
            return result;
        } else {
            return new ArrayList<>();
        }
    }
}
