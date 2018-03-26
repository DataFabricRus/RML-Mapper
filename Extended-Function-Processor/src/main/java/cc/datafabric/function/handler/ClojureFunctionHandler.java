package cc.datafabric.function.handler;

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import clojure.lang.RT;

import java.io.IOException;
import java.io.StringReader;

public class ClojureFunctionHandler {

    public static final ClojureFunctionHandler INSTANCE = new ClojureFunctionHandler();

    private ClojureFunctionHandler() {
        try {
            RT.load("clojure/core");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void loadLibrary(String path) throws IOException {
        clojure.lang.Compiler.loadFile(path);
    }

    public void loadScript(String namespace, String functionString) {
        clojure.lang.Compiler.load(new StringReader("(ns " + namespace + ")" + functionString));
    }

    public IFn loadFunction(String namespace, String functionName) {
        return Clojure.var(namespace, functionName);
    }
}
