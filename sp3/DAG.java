package cs6301.g36.sp3; /**
 * Group 36
 *
 * Team Member:
 * @author Wenjie Li
 * @author Meng-Ju Lu
 * @author Xin Tong
 */


import java.util.Iterator;

/**
 *
 * solution to problem 4 {@link #isDAG(Graph)}
 *
 *    Is a given directed graph a DAG (directed, acyclic graph)?
 *    Solve the problem by running DFS on the given graph, and checking
 *    if there are any back edges.
 */
public class DAG {
    /**
     * Decide if graph is DAG (Directed Acyclic Graph)
     *
     * @param g: input graph
     * reuse dfs method in DFS Class  {@link DFS#dfs(Iterator)} }
     *
     * Return a list of vertices with topological order
     * */
    static boolean isDAG(Graph g) {
        DFS dfs = new DFS(g);
        dfs.dfs(g.iterator());
        return dfs.isDAG;
    }

    static public void main(String[] args) {
        Graph g = new Graph(3);
        g.directed = true;
        g.addEdge(g.getVertex(1), g.getVertex(2), 1);
        g.addEdge(g.getVertex(2), g.getVertex(3), 1);
        g.addEdge(g.getVertex(3), g.getVertex(1), 1);


        System.out.printf("Is DAG: %s", isDAG(g));
    }
}
