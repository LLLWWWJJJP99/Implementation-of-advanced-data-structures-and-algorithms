package cs6301.g36;

import java.util.Iterator;
import java.util.LinkedList;

public class DFS extends GraphAlgorithm<DFS.DFSVertex> {
    // Class to store information about a vertex in this algorithm
    static class DFSVertex {
        boolean seen;
        boolean cutVertex;
        int inDegree;
        int cno;
        int dis;
        int fin;
        int top;
        int low;
        Graph.Vertex parent;
        Graph.Vertex u;

        DFSVertex(Graph.Vertex u) {
            seen = false;
            cutVertex = false;
            parent = null;
            inDegree = 0; // count in degree
            cno = 0; // number of connected component
            dis = 0; // discovery time
            fin = 0; // finish time
            top = 0;
            low = 0; // lowest descendant discovered time
            this.u = u;
        }

    }

    LinkedList<Graph.Vertex> decFinList; // finish time in decreasing order
    LinkedList<Graph.Edge> bridegs; // bridge to cut connected component
    int cno;
    int time;
    int topNum;
    boolean isDAG;

    /**
     * Constructor for DFS
     *
     * @param g
     *           : input graph
     * */
    public DFS(Graph g) {
        super(g);
        node = new DFSVertex[g.size()];
        // Create array for storing vertex properties
        for (Graph.Vertex u: g) {
            node[u.name] = new DFSVertex(u);
        }
        decFinList = new LinkedList<>();
        bridegs = new LinkedList<>();
        cno = 0;
        time = 0;
        topNum = g.size();
        isDAG = true;
    }

    /**
     * DFS with a list of vertices for finding SCC
     *
     * @param v
     *           : list of vertices
     * */
    public void dfs(LinkedList<Graph.Vertex> v) {
        for (DFSVertex u : node) {
            u.seen = false;
        }

        Iterator<Graph.Vertex> it = v.iterator();
        while (it.hasNext()) {
            Graph.Vertex u = it.next();
            if (!seen(u)) {
                cno++;
                dfsVisit(u);
            }
        }
    }

    /**
     * DFS with a graph iterator
     *
     * @param it
     *           : graph iterator
     * */
    public void dfs(Iterator<Graph.Vertex> it) {
        for (DFSVertex u : node) {
            u.seen = false;
        }

        while (it.hasNext()) {
            Graph.Vertex u = it.next();
            if (!seen(u)) {
                cno++;
                dfsVisit(u);
            }
        }

        //System.out.println(decFinList.toString());
    }

    /**
     * Utility function for DFS
     *
     * @param u
     *           : current vertex
     * */
    private void dfsVisit(Graph.Vertex u) {
        DFSVertex du = getVertex(u);
        du.dis = du.low = ++time;
        du.cno = cno;
        du.seen = true;
        int children = 0;

        for (int i = 0; i < u.adj.size(); i++) {
            Graph.Vertex v = u.adj.get(i).to;
            DFSVertex dv = getVertex(v);
            children++;

            if(!seen(v)) {
                dv.parent = u;
                dfsVisit(v);

                // Check if the subtree rooted at v has a connection to
                // one of the ancestor of u.
                // If so, dv.low < du.low. Since du's ancestor must be discovered earlier than du.
                // Else, dv.low >= du.low. There is no edge between dv subtree and du's ancestor.
                du.low = Math.min(du.low, dv.low);

                // bridge
                if (dv.low > du.dis) { // no back edge
                    bridegs.add(u.adj.get(i));
                } else {
                    isDAG = false;
                }

                // case1 - a root with at least two children
                if (du.parent == null && children > 1) {
                    du.cutVertex = true;
                }

                // case2 - a non rooted node without back edge from its subtree to its ancestor
                if (du.parent != null && dv.low >= du.dis) { // no back edge
                    du.cutVertex = true;
                }

                if (du.parent != null && dv.low < du.dis) { // with back edge, not DAG
                    isDAG = false;
                }

            } else if (v != du.parent) {
                du.low = Math.min(du.low, dv.dis);
            }
        }

        du.fin = ++time;
        du.top = topNum--;
        decFinList.addFirst(u);
    }

    /**
     * Get a transpose (reversed) directed graph
     *
     * @param g
     *           : input graph
     * */
    public static Graph getTransposeGraph(Graph g) {
        Graph revGraph = new Graph(g.size());
        revGraph.directed = true;

        for (int v = 1; v <= revGraph.size(); v++) {
            Iterator<Graph.Edge> it = g.getVertex(v).revAdj.iterator();
            while (it.hasNext()) {
                Graph.Edge e = it.next();
                revGraph.addEdge(revGraph.getVertex(e.to.name + 1), revGraph.getVertex(e.from.name + 1), e.weight);
            }
        }

        return revGraph;
    }

    boolean seen(Graph.Vertex u) {
        return getVertex(u).seen;
    }

    Graph.Vertex getParent(Graph.Vertex u) {
        return getVertex(u).parent;
    }

    // Visit a node v from u
    void visit(Graph.Vertex u, Graph.Vertex v) {
        DFSVertex dv = getVertex(v);
        dv.seen = true;
        dv.parent = u;
    }
}
