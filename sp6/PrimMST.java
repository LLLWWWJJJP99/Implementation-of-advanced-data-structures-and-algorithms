
/* Ver 1.0: Starter code for Prim's MST algorithm */

package cs6301.g36.sp6;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class PrimMST extends GraphAlgorithm<PrimMST.PrimVertex>{
    static final int Infinity = Integer.MAX_VALUE;

    static class PrimVertex implements Index {
        Graph.Vertex v;
        boolean seen;
        Graph.Vertex parent;
        int d; // the weight of a smallest edge the connects v to some u
        int index;

        @Override
        public void putIndex(int index) {
            this.index = index;
        }

        @Override
        public int getIndex() {
            return this.index;
        }

        PrimVertex(Graph.Vertex u) {
            v = u;
            seen = false;
            parent = null;
            d = Infinity;
        }
    }

    public PrimMST(Graph g) {
        super(g);
        node = new PrimVertex[g.size()];
        for (Graph.Vertex u : g) {
            node[u.name] = new PrimVertex(u);
        }
    }

    public int prim1(Graph.Vertex s) {
        // Reset all nodes
        for (Graph.Vertex u : g) {
            PrimVertex pu = getVertex(u);
            pu.seen = false;
            pu.parent = null;
        }

        // Set source.seen = true
        int wmst = 0;
        PrimVertex ps = getVertex(s);
        ps.seen = true;

        // SP6.Q4: Prim's algorithm using PriorityQueue<Edge>:
        PriorityQueue<Graph.Edge> pq = new PriorityQueue<>(new Comparator<Graph.Edge>() {
            @Override
            public int compare(Graph.Edge o1, Graph.Edge o2) {
                if (o1.weight < o2.weight) {
                    return -1;
                } else if (o1.weight == o2.weight) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });

        // Put all source's outgoing edge
        for (Graph.Edge e : s) { pq.add(e); }

        // Main loop
        while (!pq.isEmpty()) {
            Graph.Edge e = pq.poll();
            Graph.Vertex u = e.from;
            Graph.Vertex v = e.to;
            PrimVertex pv = getVertex(v);

            if (pv.seen) { continue; }

            pv.seen = true;
            pv.parent = u;
            wmst += e.weight;

            for (Graph.Edge e2 : v) {
                PrimVertex p = getVertex(e2.otherEnd(v));
                if (!p.seen) { pq.add(e2); }
            }
        }

        return wmst;
    }

    public int prim2(Graph.Vertex s) {
        // Reset all nodes
        for (Graph.Vertex u : g) {
            PrimVertex pu = getVertex(u);
            pu.seen = false;
            pu.parent = null;
            pu.d = Infinity;
        }

        PrimVertex psrc = getVertex(s);
        int wmst = 0;
        psrc.d = 0;

        PrimVertex[] vertices = new PrimVertex[g.size()];

        for (int i = 0; i < g.size(); i++)  {
            vertices[i] = getVertex(g.getVertex(i + 1));
        }

        Comparator<PrimVertex> comp = new Comparator<PrimVertex>() {
            @Override
            public int compare(PrimVertex o1, PrimVertex o2) {
                return (o1.d - o2.d);
            }
        };

        // SP6.Q6: Prim's algorithm using IndexedHeap<PrimVertex>:
        IndexedHeap<PrimVertex> q = new IndexedHeap<PrimVertex>(vertices, comp, vertices.length);

        while (!q.isEmpty()) {
            PrimVertex u = q.remove();
            u.seen = true;
            wmst += u.d;

            for (Graph.Edge e : u.v) {
                Graph.Vertex v = e.otherEnd(u.v);
                PrimVertex pv = getVertex(v);

                if (!pv.seen && e.weight < pv.d) {
                    pv.d = e.weight;
                    pv.parent = u.v;
                    q.decreaseKey(pv);
                }
            }
        }
        return wmst;
    }

    public static void printArray(Integer[] arr) {
        for (Integer i : arr) {
            System.out.printf("%s ", i);
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

	    Timer timer = new Timer();
	    PrimMST mst = new PrimMST(g);
	    int wmst = mst.prim2(s);
	    timer.end();
        System.out.println(wmst+" "+timer);

        /**
         * prim1:
         * 499999 Time: 2230 msec.
         * Memory: 1458 MB / 1801 MB.
         *
         *
         * prim2:
         * 499999 Time: 1830 msec.
         Memory: 1384 MB / 1799 MB.
         */

    }
}
