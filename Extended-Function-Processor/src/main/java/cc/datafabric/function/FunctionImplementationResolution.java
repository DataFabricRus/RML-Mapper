package cc.datafabric.function;

import cc.datafabric.function.handler.ClojureFunctionHandler;
import cc.datafabric.function.handler.JavaFunctionHandler;
import cc.datafabric.function.model.ClojureFunctionModel;
import cc.datafabric.function.model.JavaFunctionModel;
import cc.datafabric.function.utils.Utils;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.util.FileManager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

public class FunctionImplementationResolution {

    public static final FunctionImplementationResolution INSTANCE = new FunctionImplementationResolution();

    private Model model;
    private String path;

    private FunctionImplementationResolution() {
        path = System.getProperty("user.dir") + "/resources/functions";
        File folder = new File(path + "/mappings");
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                model = FileManager.get().loadModel(file.getAbsolutePath());
            }
        }
    }

    public AbstractFunctionImpl load(FunctionDescription functionModel) throws IOException, NoSuchMethodException {
        switch (language(functionModel)) {
            case "https://w3id.org/datafabric.cc/ontologies/functionimpl#Clojure":
                return loadClojureFunction(functionModel);
            case "https://w3id.org/datafabric.cc/ontologies/functionimpl#Java":
                return loadJavaFunction(functionModel);
            default:
                return null;
        }
    }

    private AbstractFunctionImpl loadJavaFunction(FunctionDescription functionModel) throws IOException, NoSuchMethodException {
        QuerySolution solution =
                getQuerySolution(functionModel, "/sparql/compiled_function_implementation.sparql");
        Class c = JavaFunctionHandler.INSTANCE.loadLibrary(
                path + "/implementations",
                solution.getLiteral("path").toString(),
                solution.getLiteral("type").toString()
        );
        String[] paramters = functionModel.getParameters().values().toArray(new String[functionModel.getParameters().size()]);
        Class<?> params[] = new Class[paramters.length];
        for (int i = 0; i < functionModel.getParameters().size(); i++) {
            params[i] = JavaFunctionHandler.INSTANCE.getParamType(paramters[i]);
        }
        Method method = JavaFunctionHandler.INSTANCE.loadFunction(c, functionModel.getName(), params);
        JavaFunctionModel javaFunctionModel = new JavaFunctionModel(functionModel, method);
        return javaFunctionModel;
    }


    private AbstractFunctionImpl loadClojureFunction(FunctionDescription functionModel) throws IOException {
        QuerySolution solution =
                getQuerySolution(functionModel, "/sparql/interpreted_function_implementation.sparql");
        if (solution.get("script") != null) {
            ClojureFunctionHandler.INSTANCE.loadScript(solution.get("namespace").toString(), solution.get("script").toString());
        } else {
            ClojureFunctionHandler.INSTANCE.loadLibrary(path + "/implementations/" + solution.get("path").toString() + ".clj");
        }
        return new ClojureFunctionModel(
                functionModel,
                ClojureFunctionHandler.INSTANCE.loadFunction(solution.get("namespace").toString(), functionModel.getName())
        );
    }

    private String language(FunctionDescription functionModel) {
        return getQuerySolution(functionModel, "/sparql/function_implementation_language.sparql")
                .getResource("language")
                .getURI();
    }

    private QuerySolution getQuerySolution(FunctionDescription functionModel, String queryPath) {
        ParameterizedSparqlString query = new ParameterizedSparqlString();
        query.setCommandText(Utils.resourceToString(queryPath));
        query.setIri("iri", functionModel.getURI());
        QueryExecution queryExecution = QueryExecutionFactory.create(query.asQuery(), model);
        ResultSet results = queryExecution.execSelect();
        return results.nextSolution();
    }
}
