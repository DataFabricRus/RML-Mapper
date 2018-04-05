package be.ugent.mmlab.rml.model.std;

import be.ugent.mmlab.rml.model.RDFTerm.*;
import be.ugent.mmlab.rml.model.PredicateObjectMap;
import be.ugent.mmlab.rml.model.TriplesMap;
import be.ugent.mmlab.rml.model.termMap.ReferenceMap;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by andimou on 7/8/16.
 */
public class StdFunctionTermMap extends AbstractTermMap implements FunctionTermMap, ObjectMap, SubjectMap, TermMap {
    private IRI functionURI ;
    private TriplesMap functionTriplesMap;
    private Set<IRI> parameters;
    private PredicateObjectMap preObjMap = null;
    private Map<String,String> parameterRefs = null;

    protected Set<IRI> classIRIs;
    protected HashSet<GraphMap> graphMaps;


    public StdFunctionTermMap(Value constantValue, IRI dataType, String languageTag,
                              String stringTemplate, IRI termType, String inverseExpression,
                              ReferenceMap referenceValue, PredicateObjectMap predicateObjectMap, GraphMap graphMap,
                              TriplesMap functionTriplesMap, IRI functionURI,
                              Set<IRI> parameters, Map<String,String> parametersRefs){
        super(constantValue, dataType, languageTag, stringTemplate, termType, inverseExpression, referenceValue, graphMap);
        setPredicateObjectMap(predicateObjectMap);

        //this.functionTriplesMap = triplesMap;
        setFunctionTriplesMap(functionTriplesMap);
        setFunction(functionURI);
        setParameters(parameters);
        setParameterRefs(parametersRefs);
    }

    public void setFunctionTriplesMap(TriplesMap functionTriplesMap){
        this.functionTriplesMap = functionTriplesMap;
    }

    public TriplesMap getFunctionTriplesMap(){
        return this.functionTriplesMap;
    }

    public void setFunction(IRI functionValue){
        this.functionURI = functionValue ;
    }

    public IRI getFunction(){
        return functionURI;
    }

    public void setParameters(Set<IRI> parameters){
        this.parameters = parameters;
    }

    public Set<IRI> getParameters(){
        return this.parameters;
    }

    public PredicateObjectMap getPredicateObjectMap() { return this.preObjMap; }

    public void setPredicateObjectMap(PredicateObjectMap pom){ this.preObjMap = pom; };

    @Override
    protected void checkSpecificTermType(TermType tt) { }

    @Override
    protected void checkConstantValue(Value constantValue) { }

    public void setParameterRefs(Map<String,String> parameterRefs) {
        this.parameterRefs = parameterRefs;
    }

    public Map<String,String> getParameterRefs(){
        return this.parameterRefs;
    }

    @Override
    public Set<IRI> getClassIRIs() {
        return classIRIs;
    }

    @Override
    public void setClassIRIs(Set<IRI> classIRIs2) {
        this.classIRIs = new HashSet<IRI>();
        if (classIRIs2 != null) {
            classIRIs.addAll(classIRIs2);
        }
    }

    @Override
    public Set<GraphMap> getGraphMaps() {
        return graphMaps;
    }

    @Override
    public void setGraphMaps(Set<GraphMap> graphMap) {
        this.graphMaps = new HashSet<>();
        if (graphMaps != null)
            this.graphMaps.addAll(graphMaps);
    }

    protected void setTermType(IRI termType, IRI dataType) {
        if (termType == null) {
            setTermType(TermType.LITERAL);

        } else {
            setTermType(checkTermType(termType));
        }
    }



}
