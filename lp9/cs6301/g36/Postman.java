// Starter code for LP9
package cs6301.g36.;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

// Find a minimum weight postman tour that goes through every edge of g at least once

public class Postman {
    Graph graph;
    boolean isEuler;
    List<Graph.Vertex> negative;
    List<Graph.Vertex> positive;
    Graph.Vertex source;
    Graph.Vertex sink;
    HashMap<Graph.Edge,Integer> rCapacity;
    HashMap<Graph.Edge,Integer> rCost;
    HashMap<Graph.Edge,Graph.Edge> rEdge;
    long totalEdgeSum ;
    public Postman(Graph g) {
        this.graph=g;
        this.negative = new LinkedList<>();
        this.positive = new LinkedList<>();
        rCapacity = new HashMap<>();
        rCost = new HashMap<>();
        for(Graph.Vertex u: g) {
            for(Graph.Edge e: u) {
                rCapacity.put(e, 1);
                rCost.put(e, e.getWeight());
            }
        }
        init();
    }

    void init(){
        List<String> reasons = new LinkedList<>();
        this.isEuler = EulerianUtil.testEulerian(graph,reasons,negative,positive);
        totalEdgeSum= totalEdgeSum();
        addSourceAndSink();
        MinCostFlow mcf = new MinCostFlow(graph, source, sink, rCapacity, rCost);
        int maxFlow = mcf.establishFeasibleFlow(graph,source,sink,new Graph.Edge[graph.size()]);
        int minCost = mcf.successiveSPMinCostFlow(maxFlow);
    }

    long totalEdgeSum(){
        long total=0;
        for(Graph.Vertex v:graph){
            for(Graph.Edge e:v){
                total+=rCost.get(e);
            }
        }
        return total;
    }

    public Postman(Graph g, Graph.Vertex startVertex) {
        this.source=startVertex;

    }

    /**
     * Create a new source node s, and connect − it to nodes with
     * positive excess
     * Create a new sink node t, and connect nodes
     * with negative excess to t
     */
    void addSourceAndSink(){
        source = new Graph.Vertex(graph.n+1);
        sink = new Graph.Vertex(graph.n+2);
        Graph.Vertex[] newArr = new Graph.Vertex[graph.n];
        System.arraycopy(graph.vertex,0,newArr,0,graph.size()-2);
        newArr[graph.size()-2]=source;
        newArr[graph.size()-1]=sink;
        for(Graph.Vertex v:positive){
            Graph.Edge edge = graph.addEdge(source,v,0,graph.m);
            rCapacity.put(edge,v.adj.size()-v.revAdj.size());
            rCost.put(edge,0);
        }
        for(Graph.Vertex v:negative){
            Graph.Edge edge = graph.addEdge(v,sink,0,graph.m);
            rCapacity.put(edge,v.revAdj.size()-v.adj.size());
            rCost.put(edge,0);
        }

    }

    
    // Get a postman tour
    public List<Graph.Edge> getTour() {
        return null;
    }

    // Find length of postman tour
    public long postmanTour() {
        return 0;
    }
}
