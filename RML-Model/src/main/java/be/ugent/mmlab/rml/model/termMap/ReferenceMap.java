
package be.ugent.mmlab.rml.model.termMap;

/**
 * RML - Model : Reference Map
 * <p>
 * This interface offers a method for replacing a template with the expression it holds
 *
 * @author mielvandersande, andimou
 */
public interface ReferenceMap {

    /**
     * @return
     */
    String getReference();

    /**
     * @param reference
     */
    void setReference(String reference);

    /**
     * @param reference
     * @return
     */
    ReferenceMap getReferenceValue(String reference);

    /**
     * @param refMap
     */
    void setReferenceValue(ReferenceMap refMap);


}
