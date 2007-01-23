/*
 * Copyright (c) 2003, the JUNG Project and the Regents of the University 
 * of California
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see either
 * "license.txt" or
 * http://jung.sourceforge.net/license.txt for a description.
 */
/*
 * Created on Feb 3, 2004
 */
package edu.uci.ics.jung.algorithms.blockmodel;

import edu.uci.ics.graph.Graph;



/**
 * Any blockmodel equivalence algorithm should implement this method;
 * it allows users to access EquivalenceAlgorithms more easily.
 * 
 * @author danyelf
 * @author Tom Nelson - converted to jung2
 * @since 1.3.0
 */
public interface EquivalenceAlgorithm<V,E> {

	/**
	 * Runs the equivalence algorithm on the given graph,
	 * and returns an equivalence relation.
	 * 
	 * @param g the graph to be checked for equivalence
	 * @return an EquivalenceRelation
	 */
	public EquivalenceRelation<V,E> getEquivalences(Graph<V,E> g);

}