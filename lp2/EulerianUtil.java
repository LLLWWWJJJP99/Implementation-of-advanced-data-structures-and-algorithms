/**
 * Group 36
 *
 * Team Member:
 * @author Wenjie Li
 * @author Meng-Ju Lu
 * @author Xin Tong
 */

package cs6301.g36.lp2;


import java.util.*;

public class EulerianUtil {

    public static int stronglyConnectedComponents(Graph g) {
        return DFS(g);
    }

    private static int DFS(Graph g){
        Iterator<Graph.Vertex> defaultDfsOrder = g.iterator();
        List<Graph.Vertex> finishTimeDecreasedOrder = new LinkedList<>();
        DFS(g,defaultDfsOrder,finishTimeDecreasedOrder,false);
        for(Graph.Vertex v:g){
            v.visited=false;
        }
        int numDFSTree=DFS(g,finishTimeDecreasedOrder.iterator(),new LinkedList<>(),true);
        //restore the graph
        return numDFSTree;

    }

    private static int DFS(Graph g,Iterator<Graph.Vertex> dfsOrder,List<Graph.Vertex> finishOrder,boolean isReverseGraph) {
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

    private static void DFSVisit(Graph.Vertex v,List<Graph.Vertex> finishOrder,boolean useReverseGraph){
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

    public static boolean  testEulerian(Graph g,List<String> reasons) {
        if(g==null)
            return false;
        int numSCC = EulerianUtil.stronglyConnectedComponents(g);
        if(numSCC!=1){
            reasons.add("Reason: Graph is not strongly connected");
            return false;
        }
        for(Graph.Vertex v:g){
            if(!checkDegree(v,reasons)){
                return false;
            }
        }

        return true;
    }

    private static boolean checkDegree(Graph.Vertex v,List<String> reasons){
        boolean res= (v.adj.size()==v.revAdj.size());
        if(!res){
            reasons.add("inDegree = "+v.adj.size()+", outDegree ="+v.revAdj.size()+" at Vertex "+(v.name+1));
        }
        return res;
    }

    public static void main(String[] args){

    }

}
