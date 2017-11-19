package cs6301.g36;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TopologicalOrder {
    /**
     * Find topological order by removing edges
     *
     * @param g
     *            : input graph
     *
     * Return a list of vertices with topological order
     * */
    public static List<Graph.Vertex> toplogicalOrder1(Graph g) {
        LinkedList<Graph.Vertex> topList = new LinkedList<>();
        Queue<Graph.Vertex> q = new LinkedList<>();

        /* Reset in-degree */
        for (Graph.Vertex v : g) {
            v.inDegree = v.revAdj.size();
        }

        Iterator<Graph.Vertex> it = g.iterator();
        while (it.hasNext()) {
            Graph.Vertex v = it.next();
            if (v.getInDegree() == 0) {
                q.add(v);
            }
        }

        while (!q.isEmpty()) {
            Graph.Vertex v = q.poll();
            topList.add(v);
            for (int i = 0; i < v.adj.size(); i++) {
                Graph.Vertex neighbor = v.adj.get(i).to;
                neighbor.inDegree--;
                if (neighbor.inDegree == 0) {
                    q.add(neighbor);
                }
            }
        }
        return topList;
    }

    /**
     * Find topological with DFS
     *
     * @param g
     *            : input graph
     *
     * Return a list of vertices with topological order
     * */
    public static List<Graph.Vertex> topologicalOrder2(Graph g) {
        DFS dfs = new DFS(g);
        dfs.dfs(g.iterator());
        return dfs.decFinList;
    }


    static public void main(String[] args) {
        Graph g = new Graph(9);
        g.directed = true;
        g.addEdge(g.getVertex(8), g.getVertex(1), 1);
        g.addEdge(g.getVertex(8), g.getVertex(3), 1);
        g.addEdge(g.getVertex(8), g.getVertex(9), 1);
        g.addEdge(g.getVertex(1), g.getVertex(2), 1);
        g.addEdge(g.getVertex(1), g.getVertex(7), 1);
        g.addEdge(g.getVertex(3), g.getVertex(2), 1);
        g.addEdge(g.getVertex(3), g.getVertex(7), 1);
        g.addEdge(g.getVertex(9), g.getVertex(3), 1);
        g.addEdge(g.getVertex(9), g.getVertex(5), 1);
        g.addEdge(g.getVertex(2), g.getVertex(4), 1);
        g.addEdge(g.getVertex(7), g.getVertex(4), 1);
        g.addEdge(g.getVertex(7), g.getVertex(6), 1);
        g.addEdge(g.getVertex(5), g.getVertex(6), 1);

        System.out.println(topologicalOrder2(g));
        System.out.println(toplogicalOrder1(g));

    }
}
