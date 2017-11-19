// Driver code for LP4, part e
// Do not rename this file or move it away from cs6301/g??
// Change following line to your group number.  Make no other changes.

package cs6301.g36;

public class LP4e {
    static int VERBOSE = 0;
    public static void main(String[] args) {
        if(args.length > 0) { VERBOSE = Integer.parseInt(args[0]); }
        java.util.Scanner in = new java.util.Scanner(System.in);
        Graph g = Graph.readDirectedGraph(in);
        int source = in.nextInt();
        int target = in.nextInt();
        int maxEdges = in.nextInt();
        cs6301.g36.Timer t = new cs6301.g36.Timer();
        LP4 handle = new LP4(g, g.getVertex(source));
        int result = handle.constrainedShortestPath(g.getVertex(target), maxEdges);
        if(VERBOSE > 0) { LP4.printGraph(g, null, g.getVertex(source), null, maxEdges); }
        System.out.println(result + "\n" + t.end());
    }
}