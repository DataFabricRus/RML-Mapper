/* 
 * Copyright 2011 Antidot opensource@antidot.net
 * https://github.com/antidot/db2triples
 * 
 * DB2Triples is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License as 
 * published by the Free Software Foundation; either version 2 of 
 * the License, or (at your option) any later version.
 * 
 * DB2Triples is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/***************************************************************************
 *
 * R2RML Model : Standard ObjectMap Class
 *
 * An object map is a specific term map used for 
 * representing RDF object. 
 * 
 * modified by mielvandersande, andimou
 * 
 ****************************************************************************/
package be.ugent.mmlab.rml.model.std;

import be.ugent.mmlab.rml.model.AbstractTermMap;
import be.ugent.mmlab.rml.model.ObjectMap;
import be.ugent.mmlab.rml.model.PredicateObjectMap;
import be.ugent.mmlab.rml.model.TermMap;
import be.ugent.mmlab.rml.model.TermType;
import be.ugent.mmlab.rml.model.TriplesMap;
import be.ugent.mmlab.rml.condition.model.BindCondition;
import be.ugent.mmlab.rml.condition.model.EqualCondition;
import be.ugent.mmlab.rml.condition.model.ProcessCondition;
import be.ugent.mmlab.rml.condition.model.SplitCondition;
import be.ugent.mmlab.rml.model.reference.ReferenceIdentifier;
import java.util.HashSet;
import java.util.Set;

import net.antidot.semantic.rdf.model.tools.RDFDataValidator;
import net.antidot.semantic.rdf.rdb2rdf.r2rml.exception.InvalidR2RMLStructureException;
import net.antidot.semantic.rdf.rdb2rdf.r2rml.exception.InvalidR2RMLSyntaxException;
import net.antidot.semantic.rdf.rdb2rdf.r2rml.exception.R2RMLDataError;

import org.openrdf.model.URI;
import org.openrdf.model.Value;

public final class StdObjectMap extends AbstractTermMap implements TermMap, ObjectMap {

	private PredicateObjectMap predicateObjectMap;

	public StdObjectMap(PredicateObjectMap predicateObjectMap,
			Value constantValue, URI dataType, String languageTag,
			String stringTemplate, URI termType, String inverseExpression,
			ReferenceIdentifier referenceValue) throws R2RMLDataError,
			InvalidR2RMLStructureException, InvalidR2RMLSyntaxException {
		super(constantValue, dataType, languageTag, stringTemplate, termType,
				inverseExpression, referenceValue);
		setPredicateObjectMap(predicateObjectMap);
	}
        
        public StdObjectMap(PredicateObjectMap predicateObjectMap,
			Value constantValue, URI dataType, String languageTag,
			String stringTemplate, URI termType, String inverseExpression,
			ReferenceIdentifier referenceValue, String split,
                        String process, String replace) throws R2RMLDataError,
			InvalidR2RMLStructureException, InvalidR2RMLSyntaxException {
		super(constantValue, dataType, languageTag, stringTemplate, termType,
				inverseExpression, referenceValue,
                                split, process, replace);
		setPredicateObjectMap(predicateObjectMap);
	}
        
        public StdObjectMap(PredicateObjectMap predicateObjectMap,
			Value constantValue, URI dataType, String languageTag,
			String stringTemplate, URI termType, String inverseExpression,
			ReferenceIdentifier referenceValue, String split,
                        String process, String replace, 
                        Set<EqualCondition> equalCondition, Set<ProcessCondition> processCondition, 
                        Set<SplitCondition> splitCondition, Set<BindCondition> bindCondition)
                throws R2RMLDataError, InvalidR2RMLStructureException, InvalidR2RMLSyntaxException {
		super(constantValue, dataType, languageTag, stringTemplate, termType,
				inverseExpression, referenceValue, split, process, replace, 
                                equalCondition, processCondition, splitCondition, bindCondition);
		setPredicateObjectMap(predicateObjectMap);
	}

        @Override
	protected void checkSpecificTermType(TermType tt)
			throws InvalidR2RMLStructureException {
		// If the term map is a subject map: rr:IRI or rr:BlankNode or
		// rr:Literal
		if ((tt != TermType.IRI) && (tt != TermType.BLANK_NODE)
				&& (tt != TermType.LITERAL)) {
			throw new InvalidR2RMLStructureException(
					"[StdObjectMap:checkSpecificTermType] If the term map is a "
							+ "object map: only rr:IRI or rr:BlankNode is required");
		}
	}

        @Override
	protected void checkConstantValue(Value constantValue)
			throws R2RMLDataError {
		if (!RDFDataValidator.isValidURI(constantValue.stringValue())
				&& !RDFDataValidator
						.isValidLiteral(constantValue.stringValue()))
			throw new R2RMLDataError(
					"[StdObjectMap:checkConstantValue] Not a valid URI or literal : "
							+ constantValue);
	}

        @Override
	public PredicateObjectMap getPredicateObjectMap() {
		return predicateObjectMap;
	}

        @Override
	public void setPredicateObjectMap(PredicateObjectMap predicateObjectMap) {
		/*
		 * if (predicateObjectMap.getObjectMaps() != null) { if
		 * (!predicateObjectMap.getObjectMaps().contains(this)) throw new
		 * IllegalStateException( "[StdObjectMap:setPredicateObjectMap] " +
		 * "The predicateObject map parent " +
		 * "already contains another Object Map !"); } else {
		 */
		// Update predicateObjectMap if not contains this object map
		if (predicateObjectMap != null) {
			if (predicateObjectMap.getObjectMaps() == null)
				predicateObjectMap.setObjectMaps(new HashSet<ObjectMap>());
			predicateObjectMap.getObjectMaps().add(this);
			// }
		}
		this.predicateObjectMap = predicateObjectMap;
	}
        
        @Override
	public void setOwnTriplesMap(TriplesMap ownTriplesMap)
			throws InvalidR2RMLStructureException {   
            this.ownTriplesMap = ownTriplesMap;
	}
}
