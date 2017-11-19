
/* Ver 1.0: Starter code for Kruskal's MST algorithm */

package cs6301.g36.sp6;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class KruskalMST {
    Graph graph;
    public KruskalMST(Graph g) {
        this.graph=g;
    }

    public int kruskal() {
        // SP6.Q7: Kruskal's algorithm:
        UnionFind uf = new UnionFind(graph.size());
        PriorityQueue<Graph.Edge> heap = new PriorityQueue<>(1, new Comparator<Graph.Edge>() {
            @Override
            public int compare(Graph.Edge o1, Graph.Edge o2) {
                return o1.weight-o2.weight;
            }
        });

        //add edges to heap
        for(Graph.Vertex v:graph){
            for(Graph.Edge e:v.adj){
                if(e.from==v){
                    heap.add(e);
                }
            }
        }
        int wmst = 0;
        while(!heap.isEmpty()){  //may improve the performance by change invariant to count of effective edge
            Graph.Edge curr = heap.poll();
            int p = uf.find(curr.from.name);
            int q = uf.find(curr.to.name);
            if(p!=q){
                uf.union(p,q);
                wmst+=curr.weight;
            }
        }
        return wmst;
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
	KruskalMST mst = new KruskalMST(g);
	int wmst = mst.kruskal();
	timer.end();
        System.out.println(wmst);
        System.out.println(timer);
    }
}
