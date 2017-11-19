/**
 *  Group 36
 *
 * Team Member:
 * @author Wenjie Li
 * @author Meng-Ju Lu
 * @author Xin Tong
 */


package cs6301.g36.lp2;

import java.util.*;

/**
 * A directed graph is called Eulerian if it is strongly connected,
 * and the number of incoming edges at each node is equal to the
 * number of outgoing edges from it. It is known that such a graph
 * aways has a tour (a cycle that may not be simple) that goes through
 * every edge of the graph exactly once. Such a tour (sometimes
 * called a circuit) is called an Euler tour. In this project,
 * write code that finds an Euler tour in a given graph using the
 * algorithm described in class.
 */
public class Euler {
    int VERBOSE;
    List<Graph.Edge> tour;
    Graph graph;
    Graph.Vertex start;

    // Constructor
    Euler(Graph g, Graph.Vertex start) {
        graph=g;
        VERBOSE = 1;
        this.start=start;
        tour = new LinkedList<>();
    }

    // To do: function to find an Euler tour

    /**
     *  prerequisite: the graph needs to be Eulerian
     *  checked by {@link #isEulerian()} as prerequisite
     * @return
     */
    public List<Graph.Edge> findEulerTour() {
        findTours(start);
        if(VERBOSE > 9) { printTours(start); }
        List<Graph.Edge> tour= new LinkedList();
        stitchTours(start,tour);
        return tour;
    }

    /* To do: test if the graph is Eulerian.
     * If the graph is not Eulerian, it prints the message:
     * "Graph is not Eulerian" and one reason why, such as
     * "inDegree = 5, outDegree = 3 at Vertex 37" or
     * "Graph is not strongly connected"
     */
    boolean isEulerian() {
        List<String> reasons = new LinkedList();
        boolean isEulerian = EulerianUtil.testEulerian(graph,reasons);
        if(!isEulerian){
            for(String str:reasons ){
                System.out.println(str);
            }
        }
        return isEulerian;
    }

    // Find tours starting at vertices with unexplored edges
    void findTours(Graph.Vertex start){
        findTour(start); //find tour for start at first
        for(Graph.Vertex v:graph){ //find other vertex's tour
            findTour(v);
        }
    }


    void findTour(Graph.Vertex u) {
        //find a thisTour starting at u
        List<Graph.Edge> thisTour=u.subtours;
        while(hasUnexploredEdge(u)){ //keep searching the unexplored other end
            Graph.Edge edge = u.it.next();
            thisTour.add(edge);
            u=edge.to;
        }
    }


    private boolean hasUnexploredEdge(Graph.Vertex u) {
        if(u.it==null){
            u.it=u.iterator();  //use iterator instead of index it to save space
        }
        return u.it.hasNext();
    }


    /* Print tours found by findTours() using following format:
     * Start vertex of tour: list of edges with no separators
     * Example: lp2-in1.txt, with start vertex 3, following tours may be found.
     * 3: (3,1)(1,2)(2,3)(3,4)(4,5)(5,6)(6,3)
     * 4: (4,7)(7,8)(8,4)
     * 5: (5,7)(7,9)(9,5)
     *
     * Just use System.out.print(u) and System.out.print(e)
     */
    void printTours(Graph.Vertex start) {
        //print the subtour begin with start
        printSubTour(start.subtours);
        //while there exists a node u with unexplored adjacent edges
        for(Graph.Vertex v:graph){
            if(!v.subtours.isEmpty() && v!=start){
                printSubTour(v.subtours);
            }
        }



    }


    private void printSubTour(List<Graph.Edge> subTour) {
        System.out.print(subTour.get(0).from+": ");
        for(Graph.Edge edge:subTour){
            System.out.print(edge);
        }
        System.out.println();
    }

    // Stitch tours into a single tour using the algorithm discussed in class
    void stitchTours(Graph.Vertex start,List<Graph.Edge> tour) {
        explore(start,tour);
    }

    private void explore(Graph.Vertex u,List<Graph.Edge> tour) {
        u.isTourExplored =true;
        Graph.Vertex tmp = u;
        for(Graph.Edge edge:u.subtours){  //keep explore the other unexplored end
            tour.add(edge);
            tmp=edge.to;
            if(hasUnexploredTour(tmp)){
                explore(tmp,tour);
            }
        }
    }

    private boolean hasUnexploredTour(Graph.Vertex tmp) {
        return !tmp.isTourExplored && !tmp.subtours.isEmpty();
    }


    void setVerbose(int v) {
	VERBOSE = v;
    }

    public static void main(String[] args){
        Graph g = new Graph(6);
        g.directed=true;
        g.addEdge(g.getVertex(1),g.getVertex(2),1);
        g.addEdge(g.getVertex(2),g.getVertex(3),1);
        g.addEdge(g.getVertex(3),g.getVertex(1),1);
        g.addEdge(g.getVertex(2),g.getVertex(5),1);
        g.addEdge(g.getVertex(5),g.getVertex(4),1);
        g.addEdge(g.getVertex(4),g.getVertex(2),1);
        g.addEdge(g.getVertex(3),g.getVertex(4),1);
        g.addEdge(g.getVertex(4),g.getVertex(6),1);
        g.addEdge(g.getVertex(6),g.getVertex(5),1);
        g.addEdge(g.getVertex(5),g.getVertex(3),1);

        Euler euler = new Euler(g,g.getVertex(1));
        euler.isEulerian();
        euler.findEulerTour();







    }
}
