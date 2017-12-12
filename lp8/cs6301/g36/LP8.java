
// Sample driver for LP8
package cs6301.g36;


import java.util.HashMap;
import java.util.Scanner;
import java.io.File;


public class LP8 {
    static int VERBOSE = 0;
    public static void main(String[] args) throws Exception {
        Scanner in;
        if (args.length > 0) {
            File inputFile = new File(args[0]);
            in = new Scanner(inputFile);
        } else {
            in = new Scanner(System.in);
        }
        if(args.length > 1) {
            VERBOSE = Integer.parseInt(args[1]);
        }

        Graph g = Graph.readDirectedGraph(in);
        int s = in.nextInt();
        int t = in.nextInt();
        HashMap<Graph.Edge,Integer> capacity = new HashMap<>();
        HashMap<Graph.Edge,Integer> cost = new HashMap<>();
        int[] arr = new int[1 + g.edgeSize()];
        for(int i=1; i<=g.edgeSize(); i++) {
            arr[i] = 1;   // default capacity is one.
        }
        while(in.hasNextInt()) {
            int i = in.nextInt();
            int cap = in.nextInt();
            arr[i] = cap;
        }

        Graph.Vertex src = g.getVertex(s);
        Graph.Vertex target = g.getVertex(t);

        for(Graph.Vertex u: g) {
            for(Graph.Edge e: u) {
                capacity.put(e, arr[e.getName()]);
                cost.put(e, e.getWeight());
            }
        }

        Timer timer = new Timer();

        // Find max-flow first and then a min-cost max-flow
       // Flow f = new Flow(g, src, target, capacity);
       // int value = f.relabelToFront();

        MinCostFlow mcf = new MinCostFlow(g, src, target, capacity, cost);
        //int minCost = mcf.costScalingMinCostFlow(value);
        //int minCost = mcf.successiveSPMinCostFlow(1843);
        int minCost = mcf.cycleCancellingMinCostFlow(1344);
        System.out.println(minCost);

        if(VERBOSE > 0) {
            for(Graph.Vertex u: g) {
                System.out.print(u + " : ");
                for(Graph.Edge e: u) {
                    if(mcf.flow(e) != 0) { System.out.print(e + ":" + mcf.flow(e) + "/" + mcf.capacity(e) + "@" + mcf.cost(e) + "| "); }
                }
                System.out.println();
            }
        }

        System.out.println(timer.end());
    }
}