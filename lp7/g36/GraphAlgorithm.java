package cs6301.g36;

import cs6301.g36.Graph;

public class GraphAlgorithm<T> {
	Graph g;
	// Algorithm uses a parallel array for storing information about vertices
	T[] node;

	public GraphAlgorithm(Graph g) {
		this.g = g;
	}

	T getVertex(Graph.Vertex u) {
		return Graph.Vertex.getVertex(node, u);
	}
}
