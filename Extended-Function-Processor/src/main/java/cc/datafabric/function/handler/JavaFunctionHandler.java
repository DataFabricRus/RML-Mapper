package cc.datafabric.function.handler;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class JavaFunctionHandler {

    public static final JavaFunctionHandler INSTANCE = new JavaFunctionHandler();


    public Class loadLibrary(String path, String name, String mime) throws IOException {
        return loadClass(path, name, mime);
    }


    public Method loadFunction(Class c, String functionName, Class... params) throws NoSuchMethodException {
        return c.getDeclaredMethod(functionName, params);
    }


    private Class loadClass(String path, String className, String mime) {
        switch (mime) {
            case "text/x-java-source":
                return this.getClassFromJAVA(new File(path + "/" + className + ".class"), path, className);
            case "application/java-archive":
                return this.getClassFromJAR(new File(path + "/" + className + ".jar"), className);
        }

        return null;
    }

    private Class getClassFromJAVA(File sourceFile, String path, String className) {
        Class<?> cls = null;

        // TODO let's not recompile every time
        // Compile source file.
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        int res = compiler.run(null, null, null, sourceFile.getPath());

        if (res != 0) {
            return null;
        }

        // Load and instantiate compiled class.
        URLClassLoader classLoader = null;
        try {
            classLoader = URLClassLoader.newInstance(new URL[]{(new File(path)).toURI().toURL()});
            cls = Class.forName(className, true, classLoader);
        } catch (MalformedURLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return cls;
    }

    private Class getClassFromJAR(File sourceFile, String className) {
        Class<?> cls = null;
        URLClassLoader child = null;
        try {
            child = URLClassLoader.newInstance(new URL[]{sourceFile.toURI().toURL()});
            cls = Class.forName(className, true, child);
        } catch (MalformedURLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return cls;
    }

    public static Class getParamType(String type) {
        switch (type) {
            case "xsd:string":
            case "http://www.w3.org/2001/XMLSchema#string":
                return String.class;
            case "xsd:integer":
            case "http://www.w3.org/2001/XMLSchema#integer":
                return int.class;
            case "xsd:decimal":
            case "http://www.w3.org/2001/XMLSchema#decimal":
                return double.class;
            case "xsd:boolean":
            case "http://www.w3.org/2001/XMLSchema#boolean":
            default:
                throw new Error("Couldn't derive type from " + type);
        }
    }
}
