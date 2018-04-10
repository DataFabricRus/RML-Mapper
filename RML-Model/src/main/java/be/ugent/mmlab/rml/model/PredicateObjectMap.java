package be.ugent.mmlab.rml.model;

import be.ugent.mmlab.rml.model.RDFTerm.*;

import java.util.Set;

/**
 * *************************************************************************
 * <p>
 * RML - Model : PredicateObjectMap Interface
 *
 * @author andimou
 * <p>
 * **************************************************************************
 */
public interface PredicateObjectMap {

    /**
     * A Predicate-Object Map is represented by a resource that references one
     * or more Predicate Maps.
     */
    Set<PredicateMap> getPredicateMaps();

    /**
     * @param predicateMaps
     */
    void setPredicateMaps(Set<PredicateMap> predicateMaps);

    /**
     * A Predicate-Object Map is represented by a resource that references one
     * or more Object Map or Referencing Object Map.
     */
    Set<ObjectMap> getObjectMaps();

    /**
     * @param objectMaps
     */
    void setObjectMaps(Set<ObjectMap> objectMaps);

    /**
     * A Predicate-Object Map is represented by a resource that references
     * exactly one Object Map or one Referencing Object Map.
     */
    Set<ReferencingObjectMap> getReferencingObjectMaps();

    /**
     * @param referencingOjectMap
     */
    void setReferencingObjectMap(Set<ReferencingObjectMap> referencingOjectMap);

    Set<FunctionTermMap> getFunctionTermMaps();

    void setFunctionTermMap(Set<FunctionTermMap> functionTermMaps);

    /**
     * Indicates if a PredicateObjectMap is associated with ReferencingObjectMap.
     */
    boolean hasReferencingObjectMaps();

    /**
     * A Predicate Object Map knows in own Triples Map container.
     */
    TriplesMap getOwnTriplesMap();

    /**
     * @param ownTriplesMap
     */
    void setOwnTriplesMap(TriplesMap ownTriplesMap);

    /**
     * Any Predicate-Object Map may have one or more associated Graph Maps.
     */
    Set<GraphMap> getGraphMaps();

    /**
     * @param graphmaps
     */
    void setGraphMaps(Set<GraphMap> graphmaps);

    /**
     * Get dcterms:type from predicate object map
     */
    String getDCTermsType();

    /**
     * Set dcterms:type in a predicate object map
     *
     * @param dcTermsType
     */
    void setDCTermsType(String dcTermsType);

}
