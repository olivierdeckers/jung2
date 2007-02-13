/*
 * Copyright (c) 2003, the JUNG Project and the Regents of the University of
 * California All rights reserved.
 * 
 * This software is open-source under the BSD license; see either "license.txt"
 * or http://jung.sourceforge.net/license.txt for a description.
 */
/*
 * Created on Jul 2, 2003
 *  
 */
package edu.uci.ics.jung.algorithms.generators.random;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.graph.DirectedGraph;
import edu.uci.ics.graph.Graph;
import edu.uci.ics.graph.UndirectedGraph;
import edu.uci.ics.graph.util.EdgeType;

/**
 * 
 * Generates a series of potentially useful test graphs.
 * 
 * @author danyelf
 *  
 */
public class TestGraphs {

	/**
	 * A series of pairs that may be useful for generating graphs. The
	 * miniature graph consists of 8 edges, 10 nodes, and is formed of two
	 * connected components, one of 8 nodes, the other of 2.
	 *  
	 */
	public static String[][] pairs = { { "a", "b", "3" }, {
			"a", "c", "4" }, {
			"a", "d", "5" }, {
			"d", "c", "6" }, {
			"d", "e", "7" }, {
			"e", "f", "8" }, {
			"f", "g", "9" }, {
			"h", "i", "1" }
	};

	/**
	 * Creates a small sample graph that can be used for testing purposes. The
	 * graph is as described in the section on {@link #pairs pairs}. If <tt>isDirected</tt>,
	 * the graph is a {@link DirectedSparseGraph DirectedSparseGraph},
	 * otherwise, it is an {@link UndirectedSparseGraph UndirectedSparseGraph}.
	 * 
	 * @param graph a directed or undirected graph
	 *            Is the graph directed?
	 * @return a graph consisting of eight edges and ten nodes.
	 */
	public static Graph<String, Number> createTestGraph(Graph<String,Number> graph) {

			for (int i = 0; i < pairs.length; i++) {
				String[] pair = pairs[i];
				createEdge(graph, pair[0], pair[1], Integer.parseInt(pair[2]));
			}
			return graph;
	}

    /**
     * Returns a graph consisting of a chain of <code>vertex_count - 1</code> vertices
     * plus one isolated vertex.
     */
    public static Graph<String,Number> createChainPlusIsolates(UndirectedGraph<String,Number> graph,
    		int chain_length, int isolate_count)
    {
        Graph<String, Number> g = graph;
//            new UndirectedSparseGraph<String, Number>();
        if (chain_length > 0)
        {
            String[] v = new String[chain_length];
            v[0] = "v"+0;
            g.addVertex(v[0]);
            for (int i = 1; i < chain_length; i++)
            {
                v[i] = "v"+i;
                g.addVertex(v[i]);
                g.addEdge(new Double(Math.random()), v[i], v[i-1]);
            }
        }
        for (int i = 0; i < isolate_count; i++) {
            String v = "v"+(chain_length+i);
            g.addVertex(v);
        }
        return g;
    }
    
	/**
	 * Creates a sample directed acyclic graph by generating several "layers",
	 * and connecting nodes (randomly) to nodes in earlier (but never later)
	 * layers. Each layer has some random number of nodes in it 1 less than n
	 * less than maxNodesPerLayer.
	 * 
	 * @return the created graph
	 */
	public static Graph<String,Number> createDirectedAcyclicGraph(
			DirectedGraph<String,Number> graph,
		int layers,
		int maxNodesPerLayer,
		double linkprob) {
		DirectedGraph<String,Number> dag = graph;
//            new DirectedSparseGraph<String,Number>();
//		StringLabeller sl = StringLabeller.getLabeller(dag);
		Set<String> previousLayers = new HashSet<String>();
		Set<String> inThisLayer = new HashSet<String>();
		for (int i = 0; i < layers; i++) {

			int nodesThisLayer = (int) (Math.random() * maxNodesPerLayer) + 1;
			for (int j = 0; j < nodesThisLayer; j++) {
                String v = i+":"+j;
				dag.addVertex(v);
				inThisLayer.add(v);
//				try {
//					sl.setLabel(v, i + ":" + j);
//				} catch (Exception e) {
//				}
				// for each previous node...
                for(String v2 : previousLayers) {
//				for (Iterator iter = previousLayers.iterator();
//					iter.hasNext();
//					) {
//					Vertex v2 = (Vertex) iter.next();
					if (Math.random() < linkprob) {
                        Double de = new Double(Math.random());
						dag.addEdge(de, v, v2);
					}
				}
			}

			previousLayers.addAll(inThisLayer);
			inThisLayer.clear();
		}
		return dag;
	}
	private static void createEdge(
			Graph<String, Number> g,
			String v1Label,
			String v2Label,
			int weight) {
			g.addEdge(new Double(Math.random()), v1Label, v2Label);
	}
	
	/**
	 * Returns a bigger, undirected test graph with a just one component. This
	 * graph consists of a clique of ten edges, a partial clique (randomly
	 * generated, with edges of 0.6 probability), and one series of edges
	 * running from the first node to the last.
	 * 
	 * @return the testgraph
	 */
	public static Graph<String,Number> getOneComponentGraph(UndirectedGraph<String,Number> g) {

		// let's throw in a clique, too
		for (int i = 1; i <= 10; i++) {
			for (int j = i + 1; j <= 10; j++) {
				String i1 = "" + i;
				String i2 = "" + j;
				createEdge(g, i1, i2, i + j);
			}
		}

		// and, last, a partial clique
		for (int i = 11; i <= 20; i++) {
			for (int j = i + 1; j <= 20; j++) {
				if (Math.random() > 0.6)
					continue;
				String i1 = "" + i;
				String i2 = "" + j;
				createEdge(g, i1, i2, i + j);
			}
		}

		List<String> index = new ArrayList<String>();
		index.addAll(g.getVertices());
		// and one edge to connect them all
//		Indexer ind = Indexer.getIndexer(g);
		for (int i = 0; i < index.size() - 1; i++) {
			try {
				g.addEdge(new Integer(i), index.get(i), index.get(i+1));
//				GraphUtils.addEdge(g, (Vertex)ind.getVertex(i), (Vertex) ind.getVertex(i + 1));
			} catch (IllegalArgumentException fe) {
			}
		}

		return g;
	}

	/**
	 * Returns a bigger test graph with a clique, several components, and other
	 * parts.
	 * 
	 * @return a demonstration graph of type <tt>UndirectedSparseGraph</tt>
	 *         with 28 vertices.
	 */
	public static Graph<String, Number> getDemoGraph(UndirectedGraph<String,Number> g) {
//		UndirectedGraph<String, Number> g = 
//            new UndirectedSparseGraph<String, Number>();

		for (int i = 0; i < pairs.length; i++) {
			String[] pair = pairs[i];
			createEdge(g, pair[0], pair[1], Integer.parseInt(pair[2]));
		}

		// let's throw in a clique, too
		for (int i = 1; i <= 10; i++) {
			for (int j = i + 1; j <= 10; j++) {
				String i1 = "clique" + i;
				String i2 = "clique" + j;
				createEdge(g, i1, i2, i + j);
			}
		}

		// and, last, a partial clique
		for (int i = 11; i <= 20; i++) {
			for (int j = i + 1; j <= 20; j++) {
				if (Math.random() > 0.6)
					continue;
				String i1 = "partial" + i;
				String i2 = "partial" + j;
				createEdge(g, i1, i2, i + j);
			}
		}
		return g;
	}

	/**
	 * Equivalent to <code>generateMixedRandomGraph(edge_weight, num_vertices, true)</code>.
	 */
	public static <V,E> Graph<V, E> generateMixedRandomGraph(
			Factory<Graph<V,E>> graphFactory,
			Factory<V> vertexFactory,
    		Factory<E> edgeFactory,
    		Map<E,Number> edge_weight, 
			int num_vertices, Set<V> seedVertices)
	{
		return generateMixedRandomGraph(graphFactory, vertexFactory, edgeFactory, 
				edge_weight, num_vertices, true, seedVertices);
	}

    /**
     * Returns a random mixed-mode graph.  Starts with a randomly generated 
     * Barabasi-Albert (preferential attachment) generator 
     * (4 initial vertices, 3 edges added at each step, and num_vertices - 4 evolution steps).
     * Then takes the resultant graph, replaces random undirected edges with directed
     * edges, and assigns random weights to each edge.
     */
    public static <V,E> Graph<V,E> generateMixedRandomGraph(
    		Factory<Graph<V,E>> graphFactory,
    		Factory<V> vertexFactory,
    		Factory<E> edgeFactory,
    		Map<E,Number> edge_weights, 
            int num_vertices, boolean parallel, Set<V> seedVertices)
    {
        int seed = (int)(Math.random() * 10000);
        BarabasiAlbertGenerator<V,E> bag = 
            new BarabasiAlbertGenerator<V,E>(graphFactory, vertexFactory, edgeFactory,
            		4, 3, false, parallel, seed, seedVertices);
        bag.evolveGraph(num_vertices - 4);
        Graph<V, E> ug = bag.generateGraph();

        // create a SparseGraph version of g
        Graph<V, E> g = graphFactory.create();
        	//new SparseGraph<V, E>();
        for(V v : ug.getVertices()) {
        	g.addVertex(v);
        }
        
        // randomly replace some of the edges by directed edges to 
        // get a mixed-mode graph, add random weights
        
        for(E e : ug.getEdges()) {
            V v1 = ug.getEndpoints(e).getFirst();
            V v2 = ug.getEndpoints(e).getSecond();

            E me = edgeFactory.create();
            g.addEdge(me, v1, v2, Math.random() < .5 ? EdgeType.DIRECTED : EdgeType.UNDIRECTED);
            edge_weights.put(me, Math.random());
        }
        
        return g;
    }
    
    public static Graph<Integer, Number> getSmallGraph(Graph<Integer,Number> graph) {
//        Graph<Integer, Number> graph = 
//            new SparseGraph<Integer, Number>();
        Integer[] v = new Integer[3];
        for (int i = 0; i < 3; i++) {
            v[i] = new Integer(i);
            graph.addVertex(v[i]);
        }
        graph.addEdge(new Double(0), v[0], v[1], EdgeType.DIRECTED);
        graph.addEdge(new Double(.1), v[0], v[1], EdgeType.DIRECTED);
        graph.addEdge(new Double(.2), v[0], v[1], EdgeType.DIRECTED);
        graph.addEdge(new Double(.3), v[1], v[0], EdgeType.DIRECTED);
        graph.addEdge(new Double(.4), v[1], v[0], EdgeType.DIRECTED);
        graph.addEdge(new Double(.5), v[1], v[2]);
        graph.addEdge(new Double(.6), v[1], v[2]);

        return graph;

    }

    
}