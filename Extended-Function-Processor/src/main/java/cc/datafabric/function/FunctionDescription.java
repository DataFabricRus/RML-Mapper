package cc.datafabric.function;

import java.util.Map;

public class FunctionDescription {
    private final String URI;
    private final String name;
    private Map<String, String> parameters;
    private Map<String, String> outputs;


    public FunctionDescription(String URI, String name) {
        this.URI = URI;
        this.name = name;
        this.setParameters(parameters);
        this.setOutputs(outputs);
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public Map<String, String> getOutputs() {
        return outputs;
    }

    public String getURI() {
        return URI;
    }

    public String getName() {
        return name;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public void setOutputs(Map<String, String> outputs) {
        this.outputs = outputs;
    }
}
