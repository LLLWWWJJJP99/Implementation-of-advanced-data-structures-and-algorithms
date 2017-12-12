/**
 * Group 36
 *
 * Team Member:
 * @author Wenjie Li
 * @author Meng-Ju Lu
 * @author Xin Tong
 */

package cs6301.g36;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class EulerianUtil {

    public static int stronglyConnectedComponents(Graph g) {
        return iterativeDFS(g);
    }

    private static int iterativeDFS(Graph g){
        List<Graph.Vertex> finishTimeDecreasedOrder = new LinkedList();
        Stack<Graph.Vertex> stack = new Stack();
        for(Graph.Vertex v:g){
            v.it = v.iterator();
        }
        //first dfs
        iterative(g,g.iterator(),finishTimeDecreasedOrder,stack,false);

        for(Graph.Vertex v:g){
           v.it = v.revAdj.iterator();
            v.visited=false;
        }

        stack.clear();
        int numOfSCC = iterative(g,finishTimeDecreasedOrder.iterator(),new LinkedList(),stack,true);
        return numOfSCC;
    }

    private static int iterative(Graph g, Iterator<Graph.Vertex> dfsOrder, List<Graph.Vertex> finishOrder, Stack<Graph.Vertex> stack, boolean isReverseGraph){
        int numOfDFSTree=0;
        while(dfsOrder.hasNext()){
            Graph.Vertex v = dfsOrder.next();
            if(!v.visited){
                numOfDFSTree++;
                stack.push(v);
                while(!stack.empty()){
                    Graph.Vertex tmp = stack.pop();
                    tmp.visited=true;
                    tmp.componentId=numOfDFSTree;
                    while(tmp.it.hasNext()){
                        Graph.Vertex next = isReverseGraph?tmp.it.next().from:tmp.it.next().to;
                        if(!next.visited){
                            stack.push(next);
                        }
                    }
                    finishOrder.add(0,tmp);
                }
            }
        }
        return numOfDFSTree;
    }

    private static int DFS(Graph g, Iterator<Graph.Vertex> dfsOrder, List<Graph.Vertex> finishOrder, boolean isReverseGraph) {
        Iterator<Graph.Vertex> it = dfsOrder;
        int DFSTreeCount=0;
        while (it.hasNext()) {
            Graph.Vertex v = it.next();
            if (v.visited) {
                continue;
            }
            DFSTreeCount++;
            DFSVisit(v,finishOrder,isReverseGraph);
        }
        return DFSTreeCount;
    }

    private static void DFSVisit(Graph.Vertex v, List<Graph.Vertex> finishOrder, boolean useReverseGraph){
        v.visited=true;
        for (Graph.Edge edge : useReverseGraph?v.revAdj:v.adj) {
            Graph.Vertex nextHop = useReverseGraph?edge.from:edge.to;
            if (!nextHop.visited) {
                DFSVisit(nextHop,finishOrder,useReverseGraph);
            }
        }
        if(!useReverseGraph){
            finishOrder.add(0,v);
        }
    }

    public static boolean  testEulerian(Graph g, List<String> reasons, List<Graph.Vertex> negative, List<Graph.Vertex> positive) {
        if(g==null)
            return false;
        int numSCC = EulerianUtil.stronglyConnectedComponents(g);
        if(numSCC!=1){
            reasons.add("Reason: Graph is not strongly connected");
            return false;
        }
        boolean  isEqual= true;
        for(Graph.Vertex v:g){
            if(!checkDegree(v,reasons,negative,positive)){
                isEqual= false;
            }
        }

        return isEqual;
    }

    /**
     * update inorder to add vertex with differen in/out defree into list
     * @param v
     * @param reasons
     * @param negative
     * @param positive
     * @return
     */
    private static boolean checkDegree(Graph.Vertex v, List<String> reasons, List<Graph.Vertex> negative, List<Graph.Vertex> positive){
        boolean res= (v.adj.size()==v.revAdj.size());
        if(!res){
            reasons.add("inDegree = "+v.adj.size()+", outDegree ="+v.revAdj.size()+" at Vertex "+(v.name+1));
            if(v.adj.size()>v.revAdj.size()){
                positive.add(v);
            }else{
                negative.add(v);
            }
        }
        return res;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner in;
        if (args.length > 0) {
            File inputFile = new File(args[0]);
            in = new Scanner(inputFile);
        } else {
            in = new Scanner(System.in);
        }
        int start = 1;
        if(args.length > 1) {
            start = Integer.parseInt(args[1]);
        }

        Graph g = Graph.readDirectedGraph(in);

        Timer timer = new Timer();
        stronglyConnectedComponents(g);





    }

}
