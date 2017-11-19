
// Starter code for LP4
// Do not rename this file or move it away from cs6301/g??

// change following line to your group number
package cs6301.g36;
import cs6301.g36.Graph.Vertex;
import cs6301.g36.Graph.Edge;

import java.util.*;

public class LP4 extends GraphAlgorithm<LP4.TPVertex> {
    Graph g;
    Vertex s;
    Queue<List<Integer>> paths;
    Queue<List<Integer>> shortestPaths;
    int numTopo;
    int maxReward;
    List<Vertex> maxRewardTour;


    public class TPVertex {
        boolean seen;
        Vertex parent;
        Vertex v;
        int d;
        int count;
        long numPath;
        int[] dist;

        TPVertex(Vertex u) {
            parent = null;
            v = u;
            seen = false;
            d = Integer.MAX_VALUE;
            count = 0;
            numPath = 0;
        }
    }

    // common constructor for all parts of LP4: g is graph, s is source vertex
    public LP4(Graph g, Vertex s) {
        super(g);
        this.g = g;
        this.s = s;
        numTopo = 0;
        maxReward = 0;
        paths = new LinkedList<>();
        shortestPaths = new LinkedList<>();
        node = new TPVertex[g.size()];

        for (Vertex u : g) {
            node[u.name] = new TPVertex(u);
        }
    }


    // Part a. Return number of topological orders of g
    public long countTopologicalOrders() {
        // To do
        initialize();
        List<Integer> path = new ArrayList<>();
        enumerateTopologicalOrders(path, 0);
        return numTopo;
    }


    // Part b. Print all topological orders of g, one per line, and 
    //	return number of topological orders of g
    public long enumerateTopologicalOrders() {
        initialize();
        List<Integer> path = new ArrayList<>();
        enumerateTopologicalOrders(path, 1);

        for (List<Integer> p : paths) {
            for (Integer e : p) {
                System.out.printf("%s ", e);
            }
            System.out.println();
        }

        return paths.size();
    }

    /**
     * Helper function to find all topological order
     *
     * @param path : topological path
     *
     * @param VERBOSE : 0: not store paths, 1: store paths
     * */
    private void enumerateTopologicalOrders(List<Integer> path, int VERBOSE) {
        boolean flag = false;

        for (Vertex u : g) {
            TPVertex tpu = getVertex(u);
            if (u.getInDegree() == 0 && !tpu.seen) {
                for (Edge e : u) { e.otherEnd(u).inDegree--; }

                if (VERBOSE == 1) { path.add(u.name + 1); }

                tpu.seen = true;
                enumerateTopologicalOrders(path, VERBOSE);
                tpu.seen = false;

                if (VERBOSE == 1) { path.remove(path.size() - 1); }

                for (Edge e : u) { e.otherEnd(u).inDegree++; }

                flag = true;
            }
        }

        if (!flag) {
            numTopo++;
            if (VERBOSE == 1) { paths.add(new ArrayList<>(path)); }
        }
    }


    /**
     * Initialization function
     * */
    private void initialize() {
        for (Vertex u : g) {
            TPVertex tpu = getVertex(u);
            u.inDegree = u.revAdj.size();
            tpu.seen = false;
        }
        numTopo = 0;
    }


    /**
     * Bellman-Ford algorithm to solve SSSP in directed graph with negative edges
     * Return true if there is no negative cycle
     * */
    private boolean bellmanFord() {
        TPVertex tps = getVertex(s);
        for (TPVertex u : node) {
            u.d = Integer.MAX_VALUE;
            u.parent = null;
            u.count = 0;
            u.seen = false;
        }

        Queue<Vertex> q = new LinkedList<>();
        tps.d = 0;
        tps.seen = true;
        q.add(s);

        while (!q.isEmpty()) {
            Vertex u = q.poll();
            TPVertex tpu = getVertex(u);
            tpu.seen = false; // no longer in q
            tpu.count++;

            if (tpu.count >= g.size()) { return false; } // negative cycle

            for (Edge e : u.adj) {
                Vertex v = e.otherEnd(u);
                TPVertex tpv = getVertex(v);
                if (tpv.d > tpu.d + e.weight) {
                    tpv.d = tpu.d + e.weight;
                    tpv.parent = u;
                    if (!tpv.seen) {
                        q.add(v);
                        tpv.seen = true;
                    }
                }
            }
        }
        return true;
    }


    // Part c. Return the number of shortest paths from s to t
    // Return -1 if the graph has a negative or zero cycle
    // Ref: iadsa-2017f-18-sp-skip-list
    public long countShortestPaths(Vertex t) {
        if (!bellmanFord()) { return -1; }

        TPVertex tps = getVertex(s);
        for (Vertex u : g) {
            TPVertex tpu = getVertex(u);
            tpu.seen = false;
            tpu.numPath = 0;
            u.inDegree = 0;
            for (Edge e : u.revAdj) { // e: (v, u) - Only add tight edges
                if (isTightEdge(e)) { u.inDegree++; }
            }
        }

        Queue<Vertex> q = new LinkedList<>();
        tps.seen = true;
        tps.numPath = 1;
        q.add(s);

        while (!q.isEmpty()) {
            Vertex u = q.poll();
            TPVertex tpu = getVertex(u);

            for (Edge e : u) { // e: (u, v)
                Vertex v = e.otherEnd(u);
                TPVertex tpv = getVertex(v);

                if (isTightEdge(e)) {
                    tpv.numPath += tpu.numPath;

                    if (--v.inDegree == 0 && !tpv.seen) {
                        q.add(v);
                    }
                }
            }
        }
        return getVertex(t).numPath;
    }


    // Part d. Print all shortest paths from s to t, one per line, and 
    //	return number of shortest paths from s to t.
    //	Return -1 if the graph has a negative or zero cycle.
    public long enumerateShortestPaths(Vertex t) {
        if (!bellmanFord()) { return -1; } // negative cycle

        ArrayList<Integer> path = new ArrayList<>();
        path.add(s.name + 1);
        enumerateShortestPaths(s, t, path);

        for (List<Integer> p : shortestPaths) {
            for (Integer e : p) {
                System.out.printf("%s ", e);
            }
            System.out.println();
        }

        return shortestPaths.size();
    }


    /**
     * Helper function to decide if an edge is tight or not
     *
     * @param e : edge on graph
     * */
    private boolean isTightEdge(Edge e) {
        return getVertex(e.to).d == getVertex(e.from).d + e.weight;
    }


    /**
     * Helper function to get shortest paths
     *
     * @param u : current node
     * @param t : target node
     * @param path: current path
     * */
    private void enumerateShortestPaths(Vertex u, Vertex t, ArrayList<Integer> path) {
        if (u == t) {
            shortestPaths.add(new ArrayList<>(path));
            return;
        }

        for (Edge e : u) {
            Vertex v = e.otherEnd(u);

            if (isTightEdge(e)) {
                path.add(v.name + 1);
                enumerateShortestPaths(v, t, path);
                path.remove(path.size() - 1);
            }
        }
    }


    // Part e. Return weight of shortest path from s to t using at most k edges
    public int constrainedShortestPath(Vertex t, int K) {
        // To do
        for (TPVertex u : node) {
            u.dist = new int[K + 1];
            u.dist[0] = Integer.MAX_VALUE / 2;
            u.parent = null;
        }

        getVertex(s).dist[0] = 0;

        for (int k = 1; k <= K; k++) {
            for (Vertex u : g) {
                TPVertex tpu = getVertex(u);
                tpu.dist[k] = tpu.dist[k - 1];

                for (Edge e : u.revAdj) {
                    Vertex p = e.from;
                    TPVertex tpp = getVertex(p);

                    if (tpu.dist[k] > tpp.dist[k - 1] + e.weight) {
                        tpu.dist[k] = tpp.dist[k - 1] + e.weight;
                        tpu.parent = p;
                    }
                }
            }
        }

        for (TPVertex u : node) { u.d = u.dist[K]; }
        return getVertex(t).d;
    }


    // Part f. Reward collection problem
    // Reward for vertices is passed as a parameter in a hash map
    // tour is empty list passed as a parameter, for output tour
    // Return total reward for tour
    public int reward(HashMap<Vertex,Integer> vertexRewardMap, List<Vertex> tour) {
        // To do
        if (!bellmanFord()) { return -1; }
        tour.add(s);
        reward(vertexRewardMap, tour, s, 0, 0);
        tour.clear();
        tour.addAll(maxRewardTour);
        return maxReward;
    }


    /**
     * Helper function to get maximum reward and tour
     *
     * @param vertexRewardMap
     *
     * @param tour
     *
     * @param u : current node
     *
     * @param cost : current path cost
     *
     * @param score : current reward
     * */
    private void reward(HashMap<Vertex,Integer> vertexRewardMap, List<Vertex> tour, Vertex u, int cost, int score) {
        if (u == s && cost > 0) {
            if (score > maxReward) {
                maxReward = score;
                maxRewardTour = new LinkedList<>(tour);
            }
            return;
        }

        if (cost == getVertex(u).d) {
            score += vertexRewardMap.get(u);
        }

        if (cost > getVertex(u).d && score < maxReward) { // prune!!
            return;
        }

        for (Edge e : u) {
            Vertex v = e.otherEnd(u);
            TPVertex tpv = getVertex(v);
            if (!tpv.seen) {
                tpv.seen = true;
                tour.add(v);
                reward(vertexRewardMap, tour, v, cost + e.weight, score);
                tour.remove(tour.size() - 1);
                tpv.seen = false;
            }
        }
    }


    // Do not modify this function
    static void printGraph(Graph g, HashMap<Vertex,Integer> map, Vertex s, Vertex t, int limit) {
        System.out.println("Input graph:");
        for(Vertex u: g) {
            if(map != null) {
                System.out.print(u + "($" + map.get(u) + ")\t: ");
            } else {
                System.out.print(u + "\t: ");
            }
            for(Edge e: u) {
                System.out.print(e + "[" + e.weight + "] ");
            }
            System.out.println();
        }
        if(s != null) { System.out.println("Source: " + s); }
        if(t != null) { System.out.println("Target: " + t); }
        if(limit > 0) { System.out.println("Limit: " + limit + " edges"); }
        System.out.println("___________________________________");
    }
}