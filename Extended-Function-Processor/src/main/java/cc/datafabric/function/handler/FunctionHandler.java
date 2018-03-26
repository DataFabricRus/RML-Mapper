package cc.datafabric.function.handler;

import cc.datafabric.function.AbstractFunctionImpl;
import cc.datafabric.function.FunctionDescription;
import cc.datafabric.function.FunctionImplementationResolution;
import cc.datafabric.function.utils.Utils;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.util.FileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class FunctionHandler {

    // Log
    private static final Logger log = LoggerFactory.getLogger(FunctionHandler.class);

    public String basePath;

    public Map<String, AbstractFunctionImpl> functions = new HashMap();

    public FunctionHandler(String path) {
        this.basePath = path;
        loadFunctionDescription();
    }


    private void loadFunctionDescription() {
        File folder = new File(basePath + "/definitions");
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                Model model = FileManager.get().loadModel(file.getAbsolutePath());


                List<FunctionDescription> functionDescriptions = getCommonFunctionDescription(model);

                for (FunctionDescription functionModel : functionDescriptions) {

                    functionModel.setParameters(
                            getFunctionPartialDescription(
                                    "/sparql/function_parameters.sparql", functionModel.getURI(), model
                            )
                    );


                    functionModel.setOutputs(
                            getFunctionPartialDescription(
                                    "/sparql/function_returns.sparql", functionModel.getURI(), model
                            )
                    );
                }

                try {
                    for (FunctionDescription functionModel : functionDescriptions) {
                        this.put(FunctionImplementationResolution.INSTANCE.load(functionModel));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private List<FunctionDescription> getCommonFunctionDescription(Model model) {
        ParameterizedSparqlString query = new ParameterizedSparqlString();
        query.setCommandText(Utils.resourceToString("/sparql/function_description.sparql"));
        QueryExecution queryExecution = QueryExecutionFactory.create(query.asQuery(), model);
        ResultSet results = queryExecution.execSelect();

        List<FunctionDescription> functionDescriptions = new ArrayList<>();
        for (; results.hasNext(); ) {
            QuerySolution soln = results.nextSolution();
            functionDescriptions.add(
                    new FunctionDescription(
                            soln.getResource("function").getURI(),
                            soln.getLiteral("name").toString()
                    )
            );
        }
        return functionDescriptions;
    }

    private Map<String, String> getFunctionPartialDescription(
            String path,
            String iri,
            Model model
    ) {
        ParameterizedSparqlString query = new ParameterizedSparqlString();
        query.setCommandText(Utils.resourceToString(path));
        query.setIri("iri", iri);
        QueryExecution queryExecution = QueryExecutionFactory.create(query.asQuery(), model);
        ResultSet results = queryExecution.execSelect();
        if (results.hasNext()) {
            QuerySolution soln = results.nextSolution();
            String[] predicates = soln.getLiteral("predicates").toString().split(",");
            String[] types = soln.getLiteral("types").toString().split(",");
            Map<String, String> map = new LinkedHashMap<>();
            for (int i = 0; i < predicates.length; i++) {
                map.put(predicates[i], types[i]);
            }
            return map;
        }
        return null;
    }


    private void put(AbstractFunctionImpl fn) {
        functions.put(fn.getFunctionModelPOJO().getURI(), fn);
    }

    public AbstractFunctionImpl get(String URI) {
        AbstractFunctionImpl res = null;
        if (this.functions.containsKey(URI)) {
            res = this.functions.get(URI);
        } else {
            // TODO download
        }
        return res;
    }

}
