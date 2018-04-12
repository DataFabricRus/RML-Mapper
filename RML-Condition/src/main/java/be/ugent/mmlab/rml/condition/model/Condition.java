package be.ugent.mmlab.rml.condition.model;

import be.ugent.mmlab.rml.model.PredicateObjectMap;
import be.ugent.mmlab.rml.model.RDFTerm.FunctionTermMap;
import be.ugent.mmlab.rml.model.RDFTerm.ReferencingObjectMap;

import java.util.Set;

/**
 * *************************************************************************
 * <p>
 * RML - Conditions : Condition
 *
 * @author andimou
 * <p>
 * **************************************************************************
 */
public interface Condition {

    /**
     * @return
     */
    String getCondition();

    /**
     * @return
     */
    String getReference();

    /**
     * @return
     */
    Set<Condition> getNestedConditions();

    /**
     * @return
     */
    ReferencingObjectMap getReferencingObjectMap();

    /**
     * @return
     */
    Set<BindingCondition> getBindingConditions();

    /**
     * @param nestedConditions
     */
    void setBindingConditions(Set<BindingCondition> nestedConditions);

    /**
     * @return
     */
    PredicateObjectMap getFallback();

    /**
     * @param predicateObjectMap
     */
    void setFallback(PredicateObjectMap predicateObjectMap);

    Set<FunctionTermMap> getFunctionTermMaps();

    void setFunctionTermMaps(Set<FunctionTermMap> functionTermMaps);

}
