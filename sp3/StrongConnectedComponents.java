package cs6301.g36.sp3;
/**
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
 * Solution to problem 2  {@link #stronglyConnectedComponents(Graph)}
 * and problem 3  {@link #testEulerian(Graph)}
 *
 *
 *  Strongly connected components of a directed graph.  Implement the
 * algorithm for finding strongly connected components of a directed
 * graph (see page 617 of Cormen et al, Introduction to algorithms,
 * 3rd ed.).  Run DFS on G and create a list of nodes in decreasing
 * finish time order.  Find G^T, the graph obtained by reversing all
 * edges of G.  Note that the Graph class has a field revAdj that is
 * useful for this purpose.  Run DFS on G^T, but using the order of
 * the list output by the first DFS.  Each DSF tree in the second DFS
 * is a strongly connected component.
 * int stronglyConnectedComponents(Graph g) { ... }
 * Each node is marked with a component number, and the function returns
 * the number of strongly connected components of G.
 */
public class StrongConnectedComponents {

    /**
     * Get number of strongly connected components
     *   Each node is marked with a component number, and the function returns
     *   the number of strongly connected components of G.
     *   reuse dfs method in DFS Class  {@link DFS#dfs(Iterator)}
     * @param g: input graph
     *
     * Return number of connected components
     * */
    public static int stronglyConnectedComponents(Graph g) {
        DFS dfs = new DFS(g);
        dfs.dfs(g.iterator());

        Graph revGraph = DFS.getTransposeGraph(g);

        DFS dfs2 = new DFS(revGraph);

        // Create fin in decreasing order
        LinkedList<Graph.Vertex> finOrder = new LinkedList<>();
        Iterator<Graph.Vertex> it = dfs.decFinList.iterator();
        while (it.hasNext()) {
            finOrder.add(revGraph.getVertex(it.next().name + 1));
        }

        dfs2.dfs(finOrder);
        return dfs2.cno;
    }

    // Problem 3
    /**
     * Test if a graph is Eulerian
     *
     * @param g
     *           : input graph
     *
     * Return if a graph is Eulerian or noy
     * */
    static boolean testEulerian(Graph g) {
        if (!g.directed) {
            return false;
        }

        if (stronglyConnectedComponents(g) != 1) {
            return false;
        }

        Iterator<Graph.Vertex> v = g.iterator();

        while (v.hasNext()) {
            Graph.Vertex u = v.next();
            if (u.adj.size() != u.revAdj.size()) {
                return false;
            }
        }

        return true;
    }

    static public void main(String[] args) {
        Graph g = new Graph(5);
        g.directed = true;
        g.addEdge(g.getVertex(2), g.getVertex(1), 1);
        g.addEdge(g.getVertex(1), g.getVertex(3), 1);
        g.addEdge(g.getVertex(3), g.getVertex(2), 1);
        g.addEdge(g.getVertex(1), g.getVertex(4), 1);
        g.addEdge(g.getVertex(4), g.getVertex(5), 1);
        g.addEdge(g.getVertex(5), g.getVertex(1), 1);

        System.out.println(testEulerian(g));
    }
}
