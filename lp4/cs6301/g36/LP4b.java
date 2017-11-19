// Driver code for LP4, part b
// Do not rename this file or move it away from cs6301/g??
// Change following line to your group number.  Make no other changes.

package cs6301.g36;

public class LP4b {
    static int VERBOSE = 0;
    public static void main(String[] args) {
        if(args.length > 0) { VERBOSE = Integer.parseInt(args[0]); }
        Graph g = Graph.readDirectedGraph(new java.util.Scanner(System.in));
        cs6301.g36.Timer t = new cs6301.g36.Timer();
        LP4 handle = new LP4(g, null);
        long result = handle.enumerateTopologicalOrders();
        if(VERBOSE > 0) { LP4.printGraph(g, null, null, null, 0); }
        System.out.println(result + "\n" + t.end());
    }
}
