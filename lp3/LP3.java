
// Starter code for LP3
// Do not rename this file or move it away from cs6301/g??

// change following line to your group number
package cs6301.g36.lp3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


public class LP3 {
    static int VERBOSE = 0;
    public static void main(String[] args) throws FileNotFoundException {
        Scanner in;
        if (args.length > 0) {
            File inputFile = new File(args[0]);
            in = new Scanner(inputFile);
        } else {
            in = new Scanner(System.in);
        }
        if(args.length > 1) {
            VERBOSE = Integer.parseInt(args[1]);
        }

    int start = in.nextInt();  // root node of the MST
    DMSTGraph g = DMSTGraph.readGraph(in,true);
    DMSTGraph.DMSTVertex startVertex = (DMSTGraph.DMSTVertex) g.getVertex(start);
	List<Graph.Edge> dmst = new ArrayList<>();
        Timer timer = new Timer();
	int wmst = directedMST(g, startVertex, dmst);
        timer.end();

	System.out.println("wmst is "+wmst);
        if(VERBOSE > 0) {
	    System.out.println("_________________________");
            for(Graph.Edge e: dmst) {
                System.out.print(e);
            }
	    System.out.println();
	    System.out.println("_________________________");
        }
        System.out.println(timer);
    }





    /** TO DO: List dmst is an empty list. When your algorithm finishes,
     *  it should have the edges of the directed MST of g rooted at the
     *  start vertex.  Edges must be ordered based on the vertex into
     *  which it goes, e.g., {(7,1),(7,2),null,(2,4),(3,5),(5,6),(3,7)}.
     *  In this example, 3 is the start vertex and has no incoming edges.
     *  So, the list has a null corresponding to Vertex 3.
     *  The function should return the total weight of the MST it found.
     */  
    public static int directedMST(Graph g, Graph.Vertex start, List<Graph.Edge> dmst) {
        Utilities dfsUtil = new Utilities(start);
        DMSTGraph graph = (DMSTGraph) g;
        DMSTGraph.DMSTVertex root = (DMSTGraph.DMSTVertex) start;
        int size = g.size();
        System.out.println("contract phase");
        graph.transformWeight(root);
        List<DMSTGraph.DMSTEdge> result = dfsUtil.BFStraversal(graph,root);
        while(!dfsUtil.check(result,graph)) {
            List<DMSTGraph.DMSTVertex> res = dfsUtil.runSCC(graph);
            graph.transformWeight(res,root);
            result = dfsUtil.BFStraversal(graph, root);
        }
        System.out.println("expasion phase");
        List<DMSTGraph.DMSTEdge> unexpanableList = new LinkedList<>();
        while(result.size()!=0){
            result=dfsUtil.expand(result,unexpanableList);
        }

        for(DMSTGraph.DMSTEdge xedge:unexpanableList){
            graph.dmst+=xedge.w;
        }
        //handle verbose case
        if(VERBOSE>0){
            for(DMSTGraph.DMSTEdge e:unexpanableList){
                ((DMSTGraph.DMSTVertex)e.to).dmst.add(e);
            }
            for(int i=1;i<=size;i++){
                DMSTGraph.DMSTVertex dv = (DMSTGraph.DMSTVertex) graph.getVertex(i);
                if(!dv.dmst.isEmpty()){
                    dmst.addAll(dv.dmst);
                }else{
                    dmst.add(null);
                }
            }
        }
        return graph.dmst;
    }






}
