
// driver for lp9 copy from lp8 starter
package cs6301.g36;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;


public class LP9 {
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
        Graph g = Graph.readGraph(in,true);


        Timer timer = new Timer();
        Postman postman = new Postman(g);
        long minCost = postman.postmanTour();
        System.out.println(minCost);

        if(VERBOSE > 0) {
            for(Graph.Vertex u: g) {
                System.out.print(u + " : ");
                for(Graph.Edge e: u) {
                   // if(mcf.flow(e) != 0) { System.out.print(e + ":" + mcf.flow(e) + "/" + mcf.capacity(e) + "@" + mcf.cost(e) + "| "); }
                }
                System.out.println();
            }
        }

        System.out.println(timer.end());
    }
}