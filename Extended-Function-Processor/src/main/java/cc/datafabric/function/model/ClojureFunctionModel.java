package cc.datafabric.function.model;

import cc.datafabric.function.AbstractFunctionImpl;
import cc.datafabric.function.FunctionDescription;
import clojure.lang.IFn;
import org.eclipse.rdf4j.model.Value;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class ClojureFunctionModel extends AbstractFunctionImpl {
    private IFn function;


    public ClojureFunctionModel(FunctionDescription functionModel, IFn function) {
        super(functionModel);
        this.function = function;
    }

    public ArrayList<Value> execute(Map<String, String> args) {
        try {
            Class<Object>[] params = new Class[args.size()];
            Arrays.fill(params, Object.class);
            Method invokeMethod = IFn.class.getDeclaredMethod(
                    "invoke",
                    params

            );
            return this.toValue(invokeMethod.invoke(function, args.values().toArray()), getDataType(args));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object[] prepareArguments(Map<String, String> args) {
        throw new UnsupportedOperationException();
    }

}
