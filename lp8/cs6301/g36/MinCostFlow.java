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
import cs6301.g36.lp8.Graph.*;

public class MinCostFlow {
    Graph graph;
    Vertex source;
    Vertex sink;
    public MinCostFlow(Graph g, Vertex s, Vertex t, HashMap<Edge, Integer> capacity, HashMap<Edge, Integer> cost) {
        this.graph=g;
        this.source=s;
        this.sink=t;
        initialGraph(capacity,cost);
    }

    void initialGraph(HashMap<Edge, Integer> capacity, HashMap<Edge, Integer> cost){
        //update capacity and capacity of Gf
        for(Map.Entry<Edge,Integer> entry:capacity.entrySet()){
            Edge edge = entry.getKey();
            edge.rCapacity=entry.getValue();
            edge.capacity=entry.getValue();
        }
        //update cost
        for(Map.Entry<Edge,Integer> entry:cost.entrySet()){
            Edge edge = entry.getKey();
            edge.rCost=entry.getValue();
        }
    }

    Edge addReverseEdge(Graph graph, Edge originalEdge,int weight){
        Vertex from = originalEdge.to;
        Vertex to = originalEdge.from;
        Edge reverseEdge =  addEdge(graph,from,to,weight,originalEdge.capacity,originalEdge.capacity-originalEdge.rCapacity,originalEdge.rCost);
        originalEdge.reverseEdge=reverseEdge;
        reverseEdge.reverseEdge=originalEdge;
        reverseEdge.isReverseEdge=true;
        return reverseEdge;
    }

    Edge addEdge(Graph graph, Vertex from, Vertex to, int weight, int capacity, int rCapacity, int cost){
        Edge newEdge = graph.addEdge(from,to,weight,graph.m);
        newEdge.capacity=capacity;
        newEdge.rCapacity=rCapacity;
        newEdge.rCost= -1*cost;
        return newEdge;
    }

    void addSourceAndSink(){
        source = new Vertex(graph.n);
        sink = new Vertex(graph.n+1);
        graph.n+=2;
        Vertex[] newArr = new Vertex[graph.n];
        System.arraycopy(graph.v,0,newArr,0,graph.size()-2);
        newArr[graph.size()-2]=source;
        newArr[graph.size()-1]=sink;
        graph.v=newArr;
        graph.vertex=newArr;
        for(Vertex v:graph){
            Edge edge1 = graph.addEdge(source,v,0,graph.m);
            edge1.rCapacity=graph.size();
            edge1.rCost=0;
            Edge edge2 = graph.addEdge(v,sink,0,graph.m);
            edge2.rCapacity=graph.size();
            edge2.rCost=0;
        }
    }
    // Return cost of d units of flow found by cycle cancellation algorithm
    int cycleCancellingMinCostFlow(int d) {
        //Compute a flow of D units without considering costs
        int tmp = establishFeasibleFlowDinitz(graph,source,sink,d);
        //flow the path
        System.out.println("flow: "+tmp);
        //Stop when the residual graph does not have a negative cycle.
        addSourceAndSink();
        while(true){
            boolean hasNegativeCycle = false;
            initGraphData();
            while(bellmanFordsp3(graph,source,sink)){
                hasNegativeCycle=true;
                LinkedList<Edge> negativeCycles = findNegativeCycle(graph);
                int minCost = minCostInPath(negativeCycles);
                //send flow around the cycle
                pushFlow(graph,negativeCycles.getFirst().fromVertex(),negativeCycles.getFirst().toVertex(),negativeCycles, minCost);
                //sendFlowAroundCycle(graph,negativeCycles);
            }
            if(!hasNegativeCycle){
                break;
            }
        }
        int totalCost = calculateCost(graph);
        return totalCost;
    }

    void initGraphData(){
        for(Vertex vertex:graph){
            vertex.predecessor=null;
            vertex.distance=Integer.MAX_VALUE;
            vertex.visited=false;
            vertex.count=0;
        }
    }


    /**
     * calculate the optimal flow cost value
     * @param graph
     * @return
     */
    private int calculateCost(Graph graph) {
        initGraphData();
        int sum = 0;
        for(Vertex v:graph){
            for(Edge edge:v){
                if(!edge.isReverseEdge && edge.rCapacity!=edge.capacity){
                    sum+=edge.rCost*(edge.capacity-edge.rCapacity);
                }
            }
        }
        return sum;
    }

    /**
     * send flow around the cycle, until an edge on the cycle leaves the residual graph (a
     * forward edge e leaves G f when f(e) = c(e), and its reverse edge e R leaves G f when f(e) = 0).
     * @param graph
     * @param negativeCycles
     */
    private void sendFlowAroundCycle(Graph graph, List<Edge> negativeCycles) {
        //need update
        int minFlow = 1;

        boolean isCycleLeft=false;
        while(!isCycleLeft){
            for(Edge edge:negativeCycles){
                addReverseEdge(graph,edge,edge.rCapacity);
                if(edge.rCapacity<=0){
                    isCycleLeft=true;
                }
            }
        }

    }

    /**
     * establish a flow of D units without considering costs using Dinitz algorithm
     * @param graph
     * @param source
     * @param sink
     * @param d
     * @return
     */
    public int establishFeasibleFlowDinitz(Graph graph, Vertex source, Vertex sink, int d) {
        int count=d; // abandoned because in most case it's max flow
        int totalCost = 0;
        while(true){
            initGraphData();
            boolean hasFeasiblePath = false;
            while(true){  //used to be count>0 but very inefficient
                boolean findPath = false;
                Queue<Vertex> queue = new LinkedList<>();
                source.distance=0;
                queue.add(source);
                while(!queue.isEmpty()){
                    Vertex v = queue.remove();
                    if(v==sink){
                        queue.clear();
                        hasFeasiblePath=true;
                        findPath = true;
                        break;
                    }
                    v.visited=true;
                    for(Edge e:v){
                        if(!e.to.visited && e.rCapacity>0){
                            queue.add(e.to);
                            e.to.predecessor=e;
                        }
                    }
                }
                List<Edge> feasiblePath = new LinkedList<>();
                Edge curr = sink.predecessor;
                if(!findPath){  //no feasible path
                    break;
                }
                while(curr!=null){
                    feasiblePath.add(curr);
                    curr=curr.from.predecessor;
                }
                int minCost = minCostInPath(feasiblePath);
                System.out.println("total cost: "+totalCost );
                totalCost += pushFlow(graph,source,sink,feasiblePath,minCost);

            }
            if(!hasFeasiblePath){
                break;
            }
        }

        return totalCost;

    }

    int minCostInPath(List<Edge> list){
        int minCost = Integer.MAX_VALUE;
        for(Edge edge:list){
            minCost = Math.min(minCost,edge.rCapacity);
        }
        return minCost;
    }


    // Return cost of d units of flow found by successive shortest paths
    int successiveSPMinCostFlow(int d) {
        int totalCost = 0;
        int count= d;
        while(count>0){
            List<Edge> shortestPath = new LinkedList<>();
            findShortestPath(graph,source,sink,shortestPath);
            if(shortestPath==null){
                System.out.println("no ssp");
                break;
            }
            int minCost = minCostInPath(shortestPath);
            totalCost+=pushFlow(graph,source,sink,shortestPath, minCost);
            count-=minCost;
            //System.out.println("mincost: "+minCost);
        }
        return totalCost;
    }


    LinkedList<Edge> findNegativeCycle(Graph graph){
        LinkedList<Edge> result = new LinkedList<>();
        //step3: check for negative circle
        for(Vertex v:graph){
            if(v.count>=graph.size()){
                Edge curr = v.predecessor;
                Set<Vertex> visitedSet = new HashSet<>();
                visitedSet.add(v);
                do{
                    visitedSet.add(curr.from);
                    curr=curr.from.predecessor;
                }while(!visitedSet.contains(curr.from));
                visitedSet.clear();
                visitedSet.add(curr.to);
                do{
                    result.add(curr);
                    visitedSet.add(curr.from);
                    curr=curr.from.predecessor;
                }while(!visitedSet.contains(curr.from));
                result.add(curr);
                return result;
            }
        }
        return null;
    }

    /**
     * calculate the shortest path via Bellman Ford algorithm
     * @param graph
     * @param source
     * @param sink
     */
    private boolean bellmanFordsp3(Graph graph, Vertex source, Vertex sink){
        //Create a queue q to hold vertices waiting to be processed
        Queue<Vertex> queue = new LinkedList<>();
        //step 1: init graph
        //define the distance of vertex to sink
        //init the distance of source
        initGraphData();
        source.distance=0;
        queue.add(source);
        while(!queue.isEmpty()){
            Vertex u = queue.remove();
            u.count++;
            if(u.count>=graph.size()){
                return true;
            }
            for(Edge edge:u){
                if(edge.rCapacity<=0){
                    continue;
                }
                Vertex v = edge.to;
                if(u.distance!=Integer.MAX_VALUE && v.distance>u.distance+edge.rCost){
                    v.distance=u.distance+edge.rCost;
                    v.predecessor=edge;
                    //if(visited[v.getName()]==false){  //unnecessary for push flow
                        queue.add(v);
                    //}
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
    private void findShortestPath(Graph graph, Vertex source, Vertex sink,List<Edge> shortestPath) {
        bellmanFordsp3(graph,source,sink);
        //check whether ssp exists between source and sink
        int maxLoop = graph.size()-1;
        Vertex c = sink;
        while(maxLoop>0){
            if(c.predecessor==null){
                break;
            }else{
                c=c.predecessor.from;
            }
            maxLoop--;
        }

        //step4: create path in format of list of edges
        Edge curr = sink.predecessor;
        while(curr.from!=source){
            shortestPath.add(curr);
            curr=curr.from.predecessor;
        }
        shortestPath.add(curr);

    }



    /**
     * Push flow along these paths
     * @param graph
     * @param minCost
     * @return cost of flow
     */
    private int pushFlow(Graph graph, Vertex source, Vertex sink, List<Edge> shortestPath, int minCost){
        int costOfFlow=0;
        int flow = minCost;
        if(source==sink){
            return costOfFlow;
        }
        for(Edge edge:shortestPath){
            costOfFlow+=flow(edge,flow);
        }
        return costOfFlow;
    }

    // Return cost of d units of flow found by cost scaling algorithm
    int costScalingMinCostFlow(int d) {
        return 0;
    }

    // flow going through edge e
    public int flow(Edge e) {
        e.rCapacity--;
        if(e.reverseEdge==null){
            addReverseEdge(graph,e,-1*e.rCost);
        }else{
            e.reverseEdge.rCapacity++;
        }
        return e.rCost;
    }

    // flow going through edge e
    public int flow(Edge e,int flow) {
        e.rCapacity-=flow;
        if(e.reverseEdge==null){
            addReverseEdge(graph,e,-1*e.rCost);
        }else{
            e.reverseEdge.rCapacity+=flow;
        }
        return e.rCost*flow;
    }

    // capacity of edge e
    public int capacity(Edge e) {
        return 0;
    }

    // cost of edge e
    public int cost(Edge e) {
        return 0;
    }
}