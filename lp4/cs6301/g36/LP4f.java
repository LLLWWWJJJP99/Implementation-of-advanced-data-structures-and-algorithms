// Driver code for LP4, part f
// Do not rename this file or move it away from cs6301/g??
// Change following line to your group number.  Make no other changes.

package cs6301.g36;
import cs6301.g36.Graph.Vertex;

public class LP4f {
    static int VERBOSE = 0;
    public static void main(String[] args) {
        if(args.length > 0) { VERBOSE = Integer.parseInt(args[0]); }
        java.util.Scanner in = new java.util.Scanner(System.in);
        Graph g = Graph.readGraph(in);
        int source = in.nextInt();
        java.util.HashMap<Vertex,Integer> map = new java.util.HashMap<>();
        for(Vertex u: g) {
            map.put(u, in.nextInt());
        }
        java.util.LinkedList<Vertex> list = new java.util.LinkedList<>();

        cs6301.g36.Timer t = new cs6301.g36.Timer();
        LP4 handle = new LP4(g, g.getVertex(source));
        int result = handle.reward(map, list);
        if(VERBOSE > 0) { LP4.printGraph(g, map, g.getVertex(source), null, 0); }
        System.out.println(result);
        for(Vertex u: list) { System.out.print(u + " "); }
        System.out.println("\n" + t.end());
    }
}
