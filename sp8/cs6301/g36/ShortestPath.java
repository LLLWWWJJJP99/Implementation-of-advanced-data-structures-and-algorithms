package cs6301.g36;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class ShortestPath extends GraphAlgorithm<ShortestPath.SPVertex> {
    Graph.Vertex start;

    static class SPVertex implements Index {
        boolean seen;
        Graph.Vertex parent;
        Graph.Vertex u;
        int d;
        int index;
        int[] dist;
        int count;

        @Override
        public void putIndex(int index) {
            this.index = index;
        }

        @Override
        public int getIndex() {
            return this.index;
        }

        SPVertex(Graph.Vertex v) {
            u = v;
            parent = null;
            seen = false;
            d = Integer.MAX_VALUE;
            count = 0;
        }
    }

    /**
     * Constructor
     * */
    public ShortestPath(Graph g, Graph.Vertex s) {
        super(g);
        node = new SPVertex[g.size()];
        for (Graph.Vertex u : g) {
            node[u.name] = new SPVertex(u);
        }
        start = s;
    }

    /**
     * BFS to find non-weighted (i.e., all weights are 1) shortest path
     * RT: O(E + V)
     * */
    public void bfs() {
        initialize(getVertex(start));

        Queue<Graph.Vertex> q = new LinkedList<>();
        SPVertex s = getVertex(start);
        s.d = 0;
        s.seen = true;
        q.add(start);

        while (!q.isEmpty()) {
            Graph.Vertex u = q.poll();
            SPVertex spu = getVertex(u);

            for (Graph.Edge e : u) {
                Graph.Vertex v = e.otherEnd(u);
                SPVertex spv = getVertex(v);

                if (!spv.seen) {
                    spv.d = spu.d + 1;
                    spv.parent = u;
                    spv.seen = true;
                    q.add(v);
                }
            }
        }
    }

    /**
     * Find single source shortest path on DAG (No cycle)
     * Idea: Topological Sorting, Push algorithm
     * RT: O(E + V)
     * */
    public void dagShortestPaths() {
        List<Graph.Vertex> topo = TopologicalOrder.topologicalOrder2(g);
        initialize(getVertex(start));

        for (Graph.Vertex u : topo) {
            for (Graph.Edge e : u) {
                relax(e);
            }
        }
    }


    /**
     * Dijkstra's algorithm
     * Condition: No negative cycle
     * RT: O(ElogV)
     * */
    public void dijkstra() {
        initialize(getVertex(start));

        Comparator<SPVertex> comp = new Comparator<SPVertex>() {
            @Override
            public int compare(SPVertex o1, SPVertex o2) {
                return (o1.d - o2.d);
            }
        };

        SPVertex[] vertices = new SPVertex[g.size()];
        for (int i = 0; i < g.size(); i++) {
            vertices[i] = getVertex(g.getVertex(i + 1));
        }

        IndexedHeap<SPVertex> q = new IndexedHeap<SPVertex>(vertices, comp, vertices.length); // min heap

        while (!q.isEmpty()) {
            SPVertex spu = q.remove();
            spu.seen = true;

            for (Graph.Edge e : spu.u) {
                boolean changed = relax(e); // successfully relax or not
                if (changed) {
                    SPVertex spv = getVertex(e.otherEnd(spu.u));
                    q.decreaseKey(spv);
                }
            }
        }
    }


    /**
     * Bellman Ford's algorithm
     * Handle graph with negative edges
     * Take1
     * */
    public boolean bellmanFord1() {
        // Initialize
        for (SPVertex u : node) {
            u.dist = new int[g.size()];
            u.dist[0] = Integer.MAX_VALUE / 2;
            u.parent = null;
        }

        getVertex(start).dist[0] = 0;

        for (int k = 1; k < g.size(); k++) { // shortest path by at most k edges
            boolean nochanged = true;
            for (Graph.Vertex u : g) {
                SPVertex spu = getVertex(u);
                spu.dist[k] = spu.dist[k - 1]; // for comparing purpose

                for (Graph.Edge e : u) {
                    if (e.to == u) { // e : (p, u)
                        Graph.Vertex p = e.from;
                        SPVertex spp = getVertex(p);

                        if (spu.dist[k] > spp.dist[k - 1] + e.weight) {
                            spu.dist[k] = spp.dist[k - 1] + e.weight;
                            spu.parent = p;
                            nochanged = false;
                        }
                    }
                }
            }

            if (nochanged) {
                for (SPVertex u : node) { u.d = u.dist[k]; }
                return true;
            }
        }
        return false; // G has negative cycle --> continuously change
    }


    /**
     * Bellman Ford's algorithm
     * Handle graph with negative edges
     * Take2
     * */
    public boolean bellmanFord3() {
        // Initialize
        SPVertex s = getVertex(start);
        for (SPVertex u : node) {
            u.d = Integer.MAX_VALUE / 2;
            u.parent = null;
            u.count = 0;
            u.seen = false;
        }

        Queue<Graph.Vertex> q = new LinkedList<>();
        s.d = 0;
        s.seen = true;
        q.add(start);

        while (!q.isEmpty()) {
            Graph.Vertex u = q.poll();
            SPVertex spu = getVertex(u);
            spu.seen = false; // no longer in q
            spu.count += 1;

            if (spu.count >= g.size()) { return false; }

            for (Graph.Edge e : u.adj) {
                Graph.Vertex v = e.otherEnd(u);
                SPVertex spv = getVertex(v);
                if (spv.d > spu.d + e.weight) {
                    spv.d = spu.d + e.weight;
                    spv.parent = u;
                    if (!spv.seen) {
                        q.add(v);
                        spv.seen = true;
                    }
                }
            }
        }
        return true;
    }


    /**
     * For edge e (u, v), if v.d > u.d + e.w, then replace v.d with u.d + e.w.
     * */
    boolean relax(Graph.Edge e) {
        Graph.Vertex u = e.from;
        Graph.Vertex v = e.to;
        SPVertex spu = getVertex(u);
        SPVertex spv = getVertex(v);

        if (spv.d > spu.d + e.weight) {
            spv.d = spu.d + e.weight;
            spv.parent = u;
            return true;
        }
        return false;
    }


    /**
     * Utility function for initialization
     * */
    public void initialize(SPVertex s) {
        for (SPVertex u : node) {
            u.parent = null;
            u.seen = false;
            u.d = Integer.MAX_VALUE / 2;
        }
        s.d = 0;
    }


    /**
     * Utility function to print shortest path
     * */
    public void printShortestPath(Graph.Vertex v) {
        SPVertex spv = getVertex(v);

        while (true) {
            System.out.printf("%s ", spv.u.name);
            if (spv.parent == null) { break; }
            spv = getVertex(spv.parent);
        }
        System.out.println();
    }


    public static void main(String[] args) throws FileNotFoundException {
        Scanner in;

        if (args.length > 0) {
            File inputFile = new File(args[0]);
            in = new Scanner(inputFile);
        } else {
            in = new Scanner(System.in);
        }

        Graph g = Graph.readGraph(in);
        Graph.Vertex s = g.getVertex(1);
        ShortestPath shortestPath = new ShortestPath(g, s);


        shortestPath.dijkstra();

        for (Graph.Vertex v : g) {
            System.out.printf("%s: ", shortestPath.getVertex(v).d);
            shortestPath.printShortestPath(v);
        }

    }

}
