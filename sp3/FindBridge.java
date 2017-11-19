package cs6301.g36.sp3; /**
 * Group 36
 *
 * Team Member:
 * @author Wenjie Li
 * @author Meng-Ju Lu
 * @author Xin Tong
 */

import java.util.Iterator;
import java.util.LinkedList;


/**
 * solution to problem 5  {@link #findCutVertices(Graph)}
 *
 *  For a connected, undirected graph G=(V,E), an edge e in E is
 * called a bridge if the removal of e from G breaks the graph into 2
 * components.  A vertex u in V is called a cut vertex if the removal
 * of u, along with its incident edges from G breaks it into 2 or more
 * components.  The problem of finding bridges and cut vertices of a
 * given graph will be discussed in class (see also Problem 22-2 in
 * Cormen et al's Introduction to Algorithms, 3rd ed).
 */
public class FindBridge {
    // problem 5 - cut vertices & bridge
    /**
     * label cut vertices and return lists of bridges
     * @param g: input graph
     * reuse dfs method in DFS Class  {@link DFS#dfs(Iterator)} }
     * Return a list of bridges
     * */
    static LinkedList<Graph.Edge> findCutVertices(Graph g) {
        DFS dfs = new DFS(g);
        dfs.dfs(g.iterator());
        return dfs.bridegs;
    }


    static public void main(String[] args) {
        Graph g1 = new Graph(5);
        g1.addEdge(g1.getVertex(2), g1.getVertex(1), 1);
        g1.addEdge(g1.getVertex(1), g1.getVertex(3), 1);
        g1.addEdge(g1.getVertex(3), g1.getVertex(2), 1);
        g1.addEdge(g1.getVertex(1), g1.getVertex(4), 1);
        g1.addEdge(g1.getVertex(4), g1.getVertex(5), 1);
        findCutVertices(g1);

        Graph g2 = new Graph(4);
        g2.addEdge(g2.getVertex(1), g2.getVertex(2), 1);
        g2.addEdge(g2.getVertex(2), g2.getVertex(3), 1);
        g2.addEdge(g2.getVertex(3), g2.getVertex(4), 1);
        findCutVertices(g2);

        Graph g3 = new Graph(7);
        g3.addEdge(g3.getVertex(1), g3.getVertex(2), 1);
        g3.addEdge(g3.getVertex(2), g3.getVertex(3), 1);
        g3.addEdge(g3.getVertex(3), g3.getVertex(1), 1);
        g3.addEdge(g3.getVertex(2), g3.getVertex(4), 1);
        g3.addEdge(g3.getVertex(2), g3.getVertex(5), 1);
        g3.addEdge(g3.getVertex(2), g3.getVertex(7), 1);
        g3.addEdge(g3.getVertex(4), g3.getVertex(6), 1);
        g3.addEdge(g3.getVertex(5), g3.getVertex(6), 1);
        findCutVertices(g3);

        Graph g4 = new Graph(5);
        g4.addEdge(g4.getVertex(2), g4.getVertex(1), 1);
        g4.addEdge(g4.getVertex(1), g4.getVertex(3), 1);
        g4.addEdge(g4.getVertex(3), g4.getVertex(2), 1);
        g4.addEdge(g4.getVertex(1), g4.getVertex(4), 1);
        g4.addEdge(g4.getVertex(4), g4.getVertex(5), 1);

        System.out.println(findCutVertices(g4).toString());

        Graph g5 = new Graph(4);
        g5.addEdge(g5.getVertex(1), g5.getVertex(2), 1);
        g5.addEdge(g5.getVertex(2), g5.getVertex(3), 1);
        g5.addEdge(g5.getVertex(3), g5.getVertex(4), 1);

        System.out.println(findCutVertices(g5).toString());

        Graph g6 = new Graph(7);
        g6.addEdge(g6.getVertex(1), g6.getVertex(2), 1);
        g6.addEdge(g6.getVertex(2), g6.getVertex(3), 1);
        g6.addEdge(g6.getVertex(3), g6.getVertex(1), 1);
        g6.addEdge(g6.getVertex(2), g6.getVertex(4), 1);
        g6.addEdge(g6.getVertex(2), g6.getVertex(5), 1);
        g6.addEdge(g6.getVertex(2), g6.getVertex(7), 1);
        g6.addEdge(g6.getVertex(4), g6.getVertex(6), 1);
        g6.addEdge(g6.getVertex(5), g6.getVertex(6), 1);

        System.out.println(findCutVertices(g6).toString());

    }
}
