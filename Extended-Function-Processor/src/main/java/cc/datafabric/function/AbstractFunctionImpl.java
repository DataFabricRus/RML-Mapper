package cc.datafabric.function;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class AbstractFunctionImpl {


    private final FunctionDescription functionModel;


    public AbstractFunctionImpl(FunctionDescription functionModel) {
        this.functionModel = functionModel;
    }

    public abstract ArrayList<Value> execute(Map<String, String> args) ;

    public ArrayList<Value> toValue(Object result, IRI type) {
        SimpleValueFactory vf = SimpleValueFactory.getInstance();
        ArrayList<Value> values = new ArrayList<>();
        if (!(result instanceof Collection<?>)) {
            ArrayList<Object> arr = new ArrayList<>();
            arr.add(result);
            result = arr;
        }
        ArrayList<Object> arr = new ArrayList<>();
        for (Object res : (List) result) {
            if (res != null) {
                arr.add(res);
            }
        }
        result = arr;
        switch (type.toString()) {
            case "http://www.w3.org/2001/XMLSchema#string":
                for (Object res : (List) result) {
                    values.add(vf.createLiteral((String) res));
                }
                break;
            case "http://www.w3.org/2001/XMLSchema#boolean":
                for (Object res : (List) result) {
                    values.add(vf.createLiteral((Boolean) res));
                }
                break;
            case "http://www.w3.org/2001/XMLSchema#anyURI":
                for (Object res : (List) result) {
                    values.add(vf.createIRI((String) res));
                }
                break;
            default:
                for (Object res : (List) result) {
                    values.add(vf.createLiteral(res.toString(), type));
                }
        }
        return values;
    }

    public IRI getDataType(Map<String, String> args) {
        SimpleValueFactory vf = SimpleValueFactory.getInstance();
        String type = null;
        String[] outTypes =
                functionModel
                        .getOutputs()
                        .values()
                        .toArray(new String[getFunctionModelPOJO().getParameters().size()]);

        if (outTypes.length > 0) {
            if (outTypes[0].startsWith("xsd:")) {
                type = outTypes[0].replace("xsd:", "http://www.w3.org/2001/XMLSchema#");
            } else if (outTypes[0].startsWith("owl:")) {
                type = outTypes[0].replace("owl:", "http://www.w3.org/2002/07/owl#");
            }else{
                type = outTypes[0];
            }
        }
        if ((type == null) && args.containsKey("http://dbpedia.org/function/unitParameter")) {
            type = "http://dbpedia.org/datatype/" + args.get("http://dbpedia.org/function/unitParameter");
        }
        if ((type == null) && args.containsKey("http://dbpedia.org/function/dataTypeParameter")) {
            if (args.get("http://dbpedia.org/function/dataTypeParameter").equals("owl:Thing")) {
                type = "http://www.w3.org/2001/XMLSchema#anyURI";
            }
        }
        if (type == null) {
            type = "http://www.w3.org/2001/XMLSchema#string";
        }
        return vf.createIRI(type);
    }

    public abstract Object[] prepareArguments(Map<String, String> parameters);

    public FunctionDescription getFunctionModelPOJO() {
        return functionModel;
    }
}
