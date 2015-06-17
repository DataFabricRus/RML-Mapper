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
 * R2RML Model : Referencing Object Map Interface
 *
 * A referencing object map allows using the subjects
 * of another triples map as the objects generated by 
 * a predicate-object map.
 * 
 * Modified by mielvandersande, andimou
 * 
 ****************************************************************************/
package be.ugent.mmlab.rml.model;

import be.ugent.mmlab.rml.condition.model.BindCondition;
import be.ugent.mmlab.rml.condition.model.BooleanCondition;
import be.ugent.mmlab.rml.condition.model.ProcessCondition;
import be.ugent.mmlab.rml.condition.model.SplitCondition;
import java.util.HashSet;
import java.util.Set;


public interface ReferencingObjectMap {
	
	
	/**
	 * A referencing object map has exactly one rr:parentTriplesMap property.
	 */
	public TriplesMap getParentTriplesMap();
	
	/**
	 * A referencing object map may have one or more rr:joinCondition 
	 * properties, whose values MUST be join conditions.
	 */
	public Set<JoinCondition> getJoinConditions();
	
	/**
	 * The effective reference of the logical source containing the 
	 * referencing object map.
	 */
	public String getChildReference();
	
	/**
	 * The effective reference of the logical source of its parent triples map.
	 */
	public String getParentReference();
	
        /*
         * Is this still relevant??
         */
        public String getJointReference();

        /**
         *
         * The effective reference of the Triples Map containing this Referencing Object Map
         */
        public TriplesMap getOwnTriplesMap();
        
         /**
        *
        * @return
        */
       public HashSet<BooleanCondition> getEqualConditions();
        
        /**
        *
        * @return
        */
       public HashSet<ProcessCondition> getProcessConditions();
        
        /**
        *
        * @return
        */
       public HashSet<SplitCondition> getSplitConditions();
        
        /**
        *
        * @return
        */
       public HashSet<BindCondition> getBindConditions();

        /**
         * A object map knows in own Predicate Object container.
         */
        public PredicateObjectMap getPredicateObjectMap();
        
	/**
        *
        * @param predicateObjectMap
        */
       public void setPredicateObjectMap(PredicateObjectMap predicateObjectMap);
        
        /**
        *
        * @param ownTriplesMap
        */
       public void setOwnTriplesMap(TriplesMap ownTriplesMap);
	
}
