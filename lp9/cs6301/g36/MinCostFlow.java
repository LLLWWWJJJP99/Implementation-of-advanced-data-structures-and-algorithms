// Starter code for LP8
/**
 *  Group 36
 *
 * Team Member:
 * @author Wenjie Li
 * @author Meng-Ju Lu
 * @author Xin Tong
 */

package cs6301.g36;

import java.util.*;

public class MinCostFlow {
    Graph graph;
    Graph.Vertex source;
    Graph.Vertex sink;
    HashMap<Graph.Edge,Integer> rCapacity;
    HashMap<Graph.Edge,Integer> capacity;
    HashMap<Graph.Edge,Integer> rCost;
    HashMap<Graph.Edge,Graph.Edge> rEdge;
    public MinCostFlow(Graph g, Graph.Vertex s, Graph.Vertex t, HashMap<Graph.Edge, Integer> capacity, HashMap<Graph.Edge, Integer> cost) {
        this.graph=g;
        this.source=s;
        this.sink=t;
        this.rCapacity=capacity;
        this.capacity = new HashMap<>();
        for(Map.Entry<Graph.Edge,Integer> entry:capacity.entrySet()){
            this.capacity.put(entry.getKey(),entry.getValue());
        }
        this.rCost=cost;
        this.rEdge = new HashMap<>();
    }


    // Return cost of d units of flow found by cycle cancellation algorithm
    int cycleCancellingMinCostFlow(int d) {
        Graph.Edge[] predecessor = new Graph.Edge[graph.size()];
        //Compute a flow of D units without considering costs
        int tmp = establishFeasibleFlow(graph,source,sink,predecessor);
        System.out.println("flow: "+tmp);
        //Stop when the residual graph does not have a negative cycle.
        while(true){
            int[] distance = new int[graph.size()];
            Arrays.fill(predecessor,null);
            Arrays.fill(distance,Integer.MAX_VALUE);
            boolean hasNegativeCycle = false;
            for(Graph.Vertex v:graph){
               hasNegativeCycle = bellmanFordsp2(graph,v,sink,predecessor,distance);
               if(hasNegativeCycle){
                   break;
               }
            }
            if(!hasNegativeCycle){
                break;
            }
            List<Graph.Edge> negativeCycles = findNegativeCycle(graph,distance,predecessor);
            if(negativeCycles==null){
                break; // no more negative cycle
            }
            //send flow around the cycle
            sendFlowOnCycle(graph,negativeCycles);
        }
        int totalCost = calculateCost(graph);
        return totalCost;
    }

    /**
     * calculate the optimal flow cost value
     * @param graph
     * @return
     */
    private int calculateCost(Graph graph) {
        int sum = 0;
        for(Map.Entry<Graph.Edge,Integer> entry:rCapacity.entrySet()){
            Graph.Edge edge = entry.getKey();
            int cost = rCost.get(edge);
            if(!rEdge.containsKey(edge)){
                continue;
            }
            int flow = (capacity.get(edge)-rCapacity.get(edge));
            sum+=flow*cost;
        }
        return sum;
    }

    /**
     * send flow around the cycle, until an edge on the cycle leaves the residual graph (a
     * forward edge e leaves G f when f(e) = c(e), and its reverse edge e R leaves G f when f(e) = 0).
     * @param graph
     * @param negativeCycles
     */
    private void sendFlowOnCycle(Graph graph, List<Graph.Edge> negativeCycles) {
        int minFlow = Integer.MAX_VALUE;
        for(Graph.Edge edge:negativeCycles){
            minFlow = Math.min(minFlow,rCapacity.get(edge));
        }
        for(Graph.Edge edge:negativeCycles){
            Graph.Edge r = null;
           if(!rEdge.containsKey(edge)){
               r=graph.addEdge(edge.to,edge.from,rCapacity.get(edge),graph.m);
               rCapacity.put(edge,rCapacity.get(edge)-minFlow);
               rCapacity.put(r,minFlow);
               rCost.put(r,-1*rCost.get(edge));
               rEdge.put(edge,r);
               r.from.adj.add(r);
           }else{
               r=rEdge.get(edge);
               rCapacity.put(edge,rCapacity.get(edge)-minFlow);
               rCapacity.put(r,rCapacity.get(r)+minFlow);
           }

        }
    }



    /**
     * establish a flow of D units without considering costs
     * @param graph
     * @param source
     * @param sink
     * @param d
     */
    public int establishFeasibleFlow(Graph graph, Graph.Vertex source, Graph.Vertex sink,Graph.Edge[] predecessor) {
        int count=0;
        while(true){
            boolean[] seen = new boolean[graph.size()];
            Queue<Graph.Vertex> queue = new LinkedList<>();
            queue.add(source);
            while(!queue.isEmpty()){
                Graph.Vertex v = queue.remove();
                seen[v.getName()]=true;
                for(Graph.Edge e:v.adj){
                    if(!seen[e.to.getName()] && rCapacity.get(e)>0){
                        queue.add(e.to);
                        predecessor[e.to.getName()]=e;
                        if(e.to==sink){
                            queue.clear();
                            break;
                        }
                    }
                }
            }
            Graph.Edge curr = predecessor[sink.getName()];
            if(curr==null){
                break;
            }
            while(curr!=null){
                Graph.Edge r = null;
                    if(!rEdge.containsKey(curr)){
                        r=graph.addEdge(curr.to,curr.from,rCapacity.get(curr),graph.m);
                        rCapacity.put(curr,rCapacity.get(curr)-1);
                        rCapacity.put(r,1);
                        rCost.put(r,-1*rCost.get(curr));
                        rEdge.put(curr,r);
                        r.from.adj.add(r);
                    }else{
                        r=rEdge.get(curr);
                        rCapacity.put(curr,rCapacity.get(curr)-1);
                        rCapacity.put(r,rCapacity.get(r)+1);
                    }
                    curr=predecessor[curr.from.getName()];
            }
            count++;
            Arrays.fill(predecessor,null);
        }
        return count;
    }




    // Return cost of d units of flow found by successive shortest paths
    int successiveSPMinCostFlow(int d) {
        int totalCost = 0;
        //Compute the residual graph
        createResidualNetwork(graph);
        int count= d;
        while(count>0){
           // System.out.println("count = "+count+" with cost = "+totalCost);
            List<Graph.Edge> shortestPath = new LinkedList<>();
            int flow = findShortestPath(graph,source,sink,shortestPath);
            if(shortestPath==null){
                System.out.println("no ssp");
                break;
            }
            totalCost+=pushFlow(graph,source,sink,shortestPath,flow);
            count-=1;
        }
        return totalCost;
    }

    /**
     * Transform network Graph by adding source and sink
     * @param graph
     * @return number of units needed
     */
    private int transformGraph(Graph graph){
        return 0;
    }

    /**
     * create residual network of Graph graph
     * Compute the residual graph as follows:
     * for edge e ∈ E do
     * add e to Gf with capacity c(e) − f(e), and cost b(e)
     * add e R to G f with capacity f(e), and cost − b(e)
     * Retain only edges with positive capacity in Gf.
     * @param graph directed graph
     */
    private void createResidualNetwork(Graph graph){
        //copy capacity


    }

    List<Graph.Edge> findNegativeCycle(Graph graph,int[] distance,Graph.Edge[] predecessor){
        List<Graph.Edge> result = new LinkedList<>();
        //step3: check for negative circle
        for(Map.Entry<Graph.Edge,Integer> entry:rCapacity.entrySet()){
            Graph.Edge edge = entry.getKey();
            Graph.Vertex u = edge.from;
            Graph.Vertex v = edge.to;
            if(distance[u.getName()]+rCost.get(edge)<distance[v.getName()]){
                //negatice cycle detected
                Graph.Edge curr = edge;
                do{
                    result.add(curr);
                    curr=predecessor[curr.from.getName()];
                }while(curr.from!=u);
                return result;
            }
        }
        return null;
    }

    private boolean bellmanFordsp2(Graph graph, Graph.Vertex source, Graph.Vertex sink, Graph.Edge[] predecessor, int[] distance){
        //init the distance of source
        distance[source.getName()]=0;
        for(int k=1;k<graph.size();k++){
            for(Map.Entry<Graph.Edge,Integer> entry:rCapacity.entrySet()){
                Graph.Edge edge = entry.getKey();
                if(rCapacity.get(edge)<=0){
                    continue;
                }
                Graph.Vertex from = edge.from;
                Graph.Vertex to = edge.to;
                if(distance[from.getName()]!=Integer.MAX_VALUE && distance[to.getName()]>distance[from.getName()]+rCost.get(edge)){
                    distance[to.getName()]=distance[from.getName()]+rCost.get(edge);
                    predecessor[to.getName()]=edge;
                }
            }
        }
        //check cycle
        boolean hasNegativeCycle=false;
        for(Map.Entry<Graph.Edge,Integer> entry:rCapacity.entrySet()){
            Graph.Edge edge = entry.getKey();
            if(rCapacity.get(edge)<=0){
                continue;
            }
            Graph.Vertex from = edge.from;
            Graph.Vertex to = edge.to;
            if(distance[from.getName()]!=Integer.MAX_VALUE && distance[to.getName()]>distance[from.getName()]+rCost.get(edge)){
                hasNegativeCycle=true;
            }
        }
        if(hasNegativeCycle){
           return true;
        }
        return false;
    }

    /**
     * calculate the shortest path via Bellman Ford algorithm
     * @param graph
     * @param source
     * @param sink
     * @param predecessor
     */
    private boolean bellmanFordsp3(Graph graph, Graph.Vertex source, Graph.Vertex sink, Graph.Edge[] predecessor, int[] distance){
        //Create a queue q to hold vertices waiting to be processed
        Queue<Graph.Vertex> queue = new LinkedList<>();
        //step 1: init graph
        //define the distance of vertex to sink

        //initialize the distance
        Arrays.fill(distance,Integer.MAX_VALUE);

        //init the distance of source
        distance[source.getName()]=0;
        boolean[] visited = new boolean[graph.size()];
        visited[source.getName()]=true;
        int[] count = new int[graph.size()];
        queue.add(source);
        while(!queue.isEmpty()){
            Graph.Vertex u = queue.remove();
            visited[u.getName()]=true;
            count[u.getName()]+=1;
            if(count[u.getName()]>=graph.size()){
                System.out.println("negative circle");
                return true;
            }
            for(Graph.Edge edge:u.adj){
                if(rCapacity.get(edge)<=0){
                    continue;
                }
                Graph.Vertex v = edge.to;
                if(distance[u.getName()]!=Integer.MAX_VALUE && distance[v.getName()]>distance[u.getName()]+rCost.get(edge)){
                    distance[v.getName()]=distance[u.getName()]+rCost.get(edge);
                    predecessor[v.getName()]=edge;
                    if(visited[v.getName()]==false){
                        queue.add(v);
                    }
                }
            }
        }
        return false;
    }

    /**
     * Using costs as weights of edges, find shortest paths from s,
     * using the Bellman-Ford Algorithm
     * @param graph
     * @return List of edges as path, return null if not exists ssp
     */
    private int findShortestPath(Graph graph, Graph.Vertex source, Graph.Vertex sink,List<Graph.Edge> shortestPath) {
        //define the predecessor of vertex in the shortest path
        Graph.Edge[] predecessor = new Graph.Edge[graph.size()];
        int[] distance = new int[graph.size()];
        bellmanFordsp3(graph,source,sink,predecessor,distance);
        //check whether ssp exists between source and sink

        int maxLoop = graph.size()-1;
        Graph.Vertex c = sink;
        while(maxLoop>0){
            if(predecessor[c.getName()]==null){
                break;
            }else{
                c=predecessor[c.getName()].from;
            }
        }
        if(c!=source){
            System.out.println("not ssp");
            return Integer.MIN_VALUE;
        }

        //step4: create path in format of list of edges
        Graph.Edge curr = predecessor[sink.getName()];
        int minFlow = rCapacity.get(curr);
        while(curr.from!=source){
            shortestPath.add(curr);
            curr=predecessor[curr.from.getName()];
            minFlow = Math.min(minFlow,rCapacity.get(curr));
        }
        shortestPath.add(curr);
        //System.out.println("pushed flow: "+minFlow);
        return minFlow;
    }



    /**
     * Push flow along these paths
     * @param graph
     * @param flow
     * @return cost of flow
     */
    private int pushFlow(Graph graph, Graph.Vertex source, Graph.Vertex sink, List<Graph.Edge> shortestPath, int flow){
        int costOfFlow=0;
        flow=1;
        if(source==sink){
            return costOfFlow;
        }
        for(Graph.Edge edge:shortestPath){
            int delta=rCost.get(edge)*flow;
            costOfFlow+=delta;
            if(delta<0){
               // System.out.println("negative edge: "+delta);
            }
            int tmp = rCapacity.get(edge)-flow;
            rCapacity.put(edge,tmp);
            //Retain only edges with positive capacity in Gf.
            //add reverse edge in residual graph
            Graph.Edge r = null;
            if(!rEdge.containsKey(edge)){
                r = graph.addEdge(edge.to,edge.from,rCapacity.get(edge),graph.m);
                rCost.put(r,-1*rCost.get(edge));
                rCapacity.put(r,flow);
                r.from.adj.add(r);
                rEdge.put(edge,r);
            }else{
                r = rEdge.get(edge);
                rCapacity.put(r,rCapacity.get(r)+flow);
            }

        }
        return costOfFlow;
    }

    // Return cost of d units of flow found by cost scaling algorithm
    int costScalingMinCostFlow(int d) {
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

    // cost of edge e
    public int cost(Graph.Edge e) {
        return 0;
    }
}