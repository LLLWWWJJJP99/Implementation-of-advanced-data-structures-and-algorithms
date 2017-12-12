/**
 * Class to represent a graph
 *  @author rbk
 *  Ver 1.1: 2017/08/28.  Updated some methods to public.  Added getName() to Vertex
 *  Ver 1.2: 2017/09/08.  Added getVertex() method for GraphAlgorithm.java
 *  Ver 1.3: 2017/09/28.  Added isDirected() and additional Vertex constructor
 *  Ver 2.0: 2017/10/11.  Added following OO features to allow the use of
 *  	cs6301.g00.Graph directly from cs6301.gXX, without having to copy the file:
 *	Renamed v to vertex (better name), but v is left as a synonym for vertex
 *	for backward compatibility.  Added a new field m for number of edges.
 *	Added name field to Edge to uniquely identify edges.
 *	Added hashCode, equals to Vertex and Edge for using them as keys in hashmaps.
 *	Changed == to equals() for Vertex and Edge checks for equality.
 *	Added methods fromVertex, toVertex, getWeight, setWeight, getName, setName,
 *	and field "name" to Edge class.
 *	Changed addEdge from void to return the Edge that it created.
 *	Added reverseIterator to Vertex and adj and revAdj fields changed to public.  
 *	Constructors of Vertex and Edge changed to public. 
 *	Added methods edgeSize, clear, setDirected, reverseGraph to Graph.
 */

package cs6301.g36;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


public class Graph implements Iterable<Graph.Vertex> {
	Vertex[] v, vertex; // vertices of graph
	int n; // number of verices in the graph
	int m; // number of edges in the graph
	boolean directed; // true if graph is directed, false otherwise
	//HashMap<Edge, Integer> map = new HashMap<>();
	/**
	 * Nested class to represent a vertex of a graph
	 */

	public static class Vertex implements Iterable<Edge> {
		int name; // name of the vertex
		public List<Edge> adj, revAdj; // adjacency list; use LinkedList or ArrayList

		/**
		 * Constructor for vertex
		 * 
		 * @param n
		 *            : int - name of the vertex
		 */
		public Vertex(int n) {
			name = n;
			adj = new LinkedList<Edge>();
			revAdj = new LinkedList<Edge>(); /* only for directed graphs */
		}

		/**
		 * Constructor for vertex, to be used in applications that need to extend vertex
		 * 
		 * @param u
		 *            : Vertex - the vertex to be cloned
		 */
		public Vertex(Vertex u) {
			name = u.name;
			adj = u.adj;
			revAdj = u.revAdj;
		}

		/**
		 * Method to get name of a vertex.
		 *
		 */
		public int getName() {
			return name;
		}

		/**
		 * hashCode of a vertex can be its name, since name is unique
		 */
		public int hashCode() {
			return name;
		}

		/**
		 * name of vertex is unique, so use that to implement equals
		 */
		@Override
		public boolean equals(Object other) {
			Vertex otherVertex = (Vertex) other;
			if (otherVertex == null) {
				return false;
			}
			return this.name == otherVertex.name;
		}

		/** Iterator to go through edges out of vertex */
		public Iterator<Edge> iterator() {
			return adj.iterator();
		}

		/** Iterator to go through edges into vertex */
		public Iterator<Edge> reverseIterator() {
			return revAdj.iterator();
		}

		// Helper function for parallel arrays used to store vertex attributes
		public static <T> T getVertex(T[] node, Vertex u) {
			return node[u.name];
		}

		/**
		 * Method to get vertex number. +1 is needed because [0] is vertex 1.
		 */
		public String toString() {
			return Integer.toString(name + 1);
		}
	}

	/**
	 * Nested class that represents an edge of a Graph
	 */

	public static class Edge {
		Vertex from; // head vertex
		Vertex to; // tail vertex
		int weight; // weight of edge
		int name; // name of edge

		/**
		 * Constructor for Edge
		 * 
		 * @param u
		 *            : Vertex - Vertex from which edge starts
		 * @param v
		 *            : Vertex - Vertex on which edge lands
		 * @param w
		 *            : int - Weight of edge
		 */
		public Edge(Vertex u, Vertex v, int w) {
			from = u;
			to = v;
			weight = w;
			name = -1; // This version of constructor is for backward compatibility
		}

		/**
		 * New constructor of Edge that sets name of edge also
		 */
		public Edge(Vertex u, Vertex v, int w, int n) {
			from = u;
			to = v;
			weight = w;
			name = n;
		}

		/**
		 * New constructor of Edge for extended edge classes
		 */
		public Edge(Edge e) {
			from = e.from;
			to = e.to;
			weight = e.weight;
			name = e.name;
		}

		/** Method to get vertex incident to edge at "from" end */
		public Vertex fromVertex() {
			return from;
		}

		/** Method to get vertex incident to edge at "to" end */
		public Vertex toVertex() {
			return to;
		}

		/* Get weight of edge */
		public int getWeight() {
			return weight;
		}

		/** Set weight of edge */
		public void setWeight(int newWeight) {
			weight = newWeight;
			;
		}

		/** Get name of edge */
		public int getName() {
			return name;
		}

		/** Set the name of an Edge */
		public void setName(int n) {
			name = n;
		}

		/**
		 * Method to find the other end end of an edge, given a vertex reference This
		 * method is used for undirected graphs
		 * 
		 * @param u
		 *            : Vertex
		 * @return : Vertex - other end of edge
		 */
		public Vertex otherEnd(Vertex u) {
			assert from.equals(u) || to.equals(u);
			// if the vertex u is the head of the arc, then return the tail else return the
			// head
			if (from.equals(u)) {
				return to;
			} else {
				return from;
			}
		}

		/**
		 * To use hashing with Edge as key, you need to ensure that name is unique
		 */
		public int hashCode() {
			return name;
		}

		/** Edges are equal if they have the same name and connect same ends */
		@Override
		public boolean equals(Object other) {
			if (other == null) {
				return false;
			}
			Edge otherEdge = (Edge) other;
			return this.name == otherEdge.name && this.from.equals(otherEdge.from) && this.to.equals(otherEdge.to);
		}

		/**
		 * /** Return the string "(x,y)", where edge goes from x to y
		 */
		public String toString() {
			return "(" + from + "," + to + ")";
		}

		public String stringWithSpaces() {
			return from + " " + to + " " + weight;
		}
	}

	/**
	 * Constructor for Graph
	 * 
	 * @param n
	 *            : int - number of vertices
	 */
	public Graph(int n) {
		this.n = n;
		m = 0;
		vertex = new Vertex[n];
		v = vertex; // for backward compatibility
		directed = false; // default is undirected graph
		// create an array of Vertex objects
		for (int i = 0; i < n; i++)
			vertex[i] = new Vertex(i);
	}

	/** More general constructor for applications that extend graphs */
	public Graph(Graph g) {
		n = g.n;
		m = g.m;
		vertex = g.vertex;
		v = vertex;
		directed = g.directed;
	}

	/** Additional constructor for applications that extend graphs */
	public Graph(Graph g, Vertex[] arr) {
		n = g.n;
		m = g.m;
		vertex = arr;
		v = vertex;
		directed = g.directed;
	}

	/**
	 * Find vertex no. n
	 * 
	 * @param n
	 *            : int
	 */
	public Vertex getVertex(int n) {
		return vertex[n - 1];
	}

	/* Method to get the whole array. Why is this needed? */
	public Vertex[] getVertexArray() {
		return vertex;
	}

	/**
	 * Method to add an edge to the graph This version is obsolete and kept for
	 * backward compatibility
	 * 
	 * @param a
	 *            : int - one end of edge
	 * @param b
	 *            : int - other end of edge
	 * @param weight
	 *            : int - the weight of the edge
	 */
	public Edge addEdge(Vertex from, Vertex to, int weight) {
		Edge e = new Edge(from, to, weight);
		if (directed) {
			from.adj.add(e);
			to.revAdj.add(e);
		} else {
			from.adj.add(e);
			to.adj.add(e);
		}
		m++; // Increment edge count
		return e;
	}

	/** Another version of addEdge to include name */
	public Edge addEdge(Vertex from, Vertex to, int weight, int name) {
		Edge e = new Edge(from, to, weight, name);
		//map.put(e, e.weight);
		if (directed) {
			from.adj.add(e);
			to.revAdj.add(e);
		} else {
			from.adj.add(e);
			to.adj.add(e);
		}
		m++; // Increment edge count
		return e;
	}

	/** Number of vertices in graph */
	public int size() {
		return n;
	}

	/** Number of edges in graph */
	public int edgeSize() {
		return m;
	}

	/** Method to clear all edges */
	public void clear() {
		m = 0;
		for (Vertex u : this) {
			u.adj = new LinkedList<>();
			u.revAdj = new LinkedList<>();
		}
	}

	/** Is the graph directed? */
	public boolean isDirected() {
		return directed;
	}

	/** Set directed field of graph */
	public void setDirected(boolean b) {
		directed = b;
	}

	/**
	 * Method to reverse the edges of a graph. Applicable to directed graphs only.
	 */
	public void reverseGraph() {
		if (directed) {
			for (Vertex u : this) {
				List<Edge> tmp = u.adj;
				u.adj = u.revAdj;
				u.revAdj = tmp;
			}
		}
	}

	/**
	 * Method to create iterator for vertices of graph
	 */
	public Iterator<Vertex> iterator() {
		return new ArrayIterator<Vertex>(vertex);
	}

	// read a directed graph using the Scanner interface
	public static Graph readDirectedGraph(Scanner in) {
		return readGraph(in, true);
	}

	// read an undirected graph using the Scanner interface
	public static Graph readGraph(Scanner in) {
		return readGraph(in, false);
	}

	public static Graph readGraph(Scanner in, boolean directed) {
		// read the graph related parameters
		int n = in.nextInt(); // number of vertices in the graph
		int m = in.nextInt(); // number of edges in the graph

		// create a graph instance
		Graph g = new Graph(n);
		g.directed = directed;
		for (int i = 0; i < m; i++) {
			int u = in.nextInt();
			int v = in.nextInt();
			int w = in.nextInt();
			g.addEdge(g.getVertex(u), g.getVertex(v), w, i + 1);
		}
		return g;
	}
}