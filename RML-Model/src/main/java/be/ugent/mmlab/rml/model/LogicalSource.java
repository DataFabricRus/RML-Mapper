package be.ugent.mmlab.rml.model;

import be.ugent.mmlab.rml.vocabularies.QLVocabulary.QLTerm;

/**
 * *************************************************************************
 * <p>
 * RML - Model : LogicalSource Interface
 *
 * @author andimou
 * <p>
 * **************************************************************************
 */
public interface LogicalSource {

    /**
     * Every logical source has an Iterator.
     * The Iterator can be skipped when the data have tabular structure
     * and the iteration is implied that it is per row.
     *
     * @return String
     */
    String getIterator();

    /**
     * @param iterator
     */
    void setIterator(String iterator);

    /**
     * @return
     */
    String getTableName();

    /**
     * @param table
     */
    void setTableName(String table);

    /**
     * Every Logical Source has an Input Source
     * where the data reside.
     *
     * @return InputSource
     */
    Source getSource();

    /**
     * @param source
     */
    void setSource(Source source);

    /**
     * Every Logical Source has a Reference Formulation
     * that specifies the grammar to refer to the input data.
     *
     * @return QLVocabulary.QLTerm
     */
    QLTerm getReferenceFormulation();

    /**
     * @param referenceFormulation
     */
    void setReferenceFormulation(QLTerm referenceFormulation);

    /**
     * @return
     */
    ReferenceFormulation getCustomReferenceFormulation();

    /**
     * @param dialect
     */
    void setCustomReferenceFormulation(ReferenceFormulation dialect);

    /**
     * @param source
     * @return
     */
    String getSourceType(Source source);

    /**
     * @return
     */
    String getQuery();

    /**
     * @param query
     */
    void setQuery(String query);

}
