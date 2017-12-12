// Starter code for LP7
package cs6301.g36;


import java.util.HashMap;
import java.util.Set;

public class Flow {
    public Flow(Graph g, Graph.Vertex s, Graph.Vertex t, HashMap<Graph.Edge, Integer> capacity) {
    }

    // Return max flow found by Dinitz's algorithm
    public int dinitzMaxFlow() {
        return 0;
    }

    // Return max flow found by relabelToFront algorithm
    public int relabelToFront() {
        return 0;
    }

    // flow going through edge e
    public int flow(Graph.Edge e) {
        return 0;
    }

    // capacity of edge e
    public int capacity(Graph.Edge e) {
        return 0;
    }

    /* After maxflow has been computed, this method can be called to
       get the "S"-side of the min-cut found by the algorithm
    */
    public Set<Graph.Vertex> minCutS() {
        return null;
    }

    /* After maxflow has been computed, this method can be called to
       get the "T"-side of the min-cut found by the algorithm
    */
    public Set<Graph.Vertex> minCutT() {
        return null;
    }
}