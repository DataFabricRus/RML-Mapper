package be.ugent.mmlab.rml.condition.model;

/**
 * *************************************************************************
 * <p>
 * RML - Conditions : BindingCondition
 *
 * @author andimou
 * <p>
 * **************************************************************************
 */
public interface BindingCondition extends Condition {

    String getReference();

    String getVariable();
}
