// Test driver for LP8
package cs6301.g36;
import java.util.HashMap;
import java.util.Scanner;


public class rbkLP8 {
    static int VERBOSE = 0;
    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        int d = Integer.parseInt(args[0]);
        int alg = Integer.parseInt(args[1]);
        if(args.length > 2) { VERBOSE = Integer.parseInt(args[2]); }
        Graph g = Graph.readDirectedGraph(in);
        int s = in.nextInt();
        int t = in.nextInt();
        HashMap<Graph.Edge,Integer> capacity = new HashMap<>();
        HashMap<Graph.Edge,Integer> cost = new HashMap<>();
        int[] arr = new int[1 + g.edgeSize()];
        for(int i=1; i<=g.edgeSize(); i++) {
            arr[i] = 1;   // default capacity
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
        MinCostFlow mcf = new MinCostFlow(g, src, target, capacity, cost);
        int result = 0;
        switch(alg) {
            case 1:
                result = mcf.cycleCancellingMinCostFlow(d);
                break;
            case 2:
                result = mcf.successiveSPMinCostFlow(d);
                break;
            default:
                result = mcf.costScalingMinCostFlow(d);
                break;
        }
        System.out.println(alg + " " + result);

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