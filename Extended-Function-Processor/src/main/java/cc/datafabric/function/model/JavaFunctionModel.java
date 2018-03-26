package cc.datafabric.function.model;

import cc.datafabric.function.AbstractFunctionImpl;
import cc.datafabric.function.FunctionDescription;
import org.eclipse.rdf4j.model.Value;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

public class JavaFunctionModel extends AbstractFunctionImpl {

    private Method function;

    public JavaFunctionModel(FunctionDescription functionModel, Method function) {
        super(functionModel);
        this.function = function;
    }

    public ArrayList<Value> execute(Map<String, String> args) {
        try {
            return this.toValue(this.function.invoke(null, prepareArguments(args)), getDataType(args));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }


    public Object[] prepareArguments(Map<String, String> args) {
        String[] parameterNames =
                getFunctionModelPOJO()
                        .getParameters()
                        .keySet()
                        .toArray(new String[getFunctionModelPOJO().getParameters().size()]);
        Object[] preparedArguments = new Object[parameterNames.length];
        Class[] paramTypes = function.getParameterTypes();
        for (int i = 0; i < getFunctionModelPOJO().getParameters().size(); i++) {
            if (args.get(parameterNames[i]) != null) {
                preparedArguments[i] = parseParameter(args.get(parameterNames[i]), paramTypes[i]);
            } else {
                preparedArguments[i] = null;
            }
        }
        return preparedArguments;
    }

    private Object parseParameter(String parameter, Class type) {
        switch (type.getName()) {
            case "java.lang.String":
                return parameter;
            case "int":
                return Integer.parseInt(parameter);
            case "double":
                return Double.parseDouble(parameter);
            default:
                throw new Error("Couldn't derive " + type.getName() + " from " + parameter);
        }
    }
}
