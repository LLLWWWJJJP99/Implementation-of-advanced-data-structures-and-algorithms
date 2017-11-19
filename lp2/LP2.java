/**
 *  Group 36
 *
 * Team Member:
 * @author Wenjie Li
 * @author Meng-Ju Lu
 * @author Xin Tong
 */
// change following line to your group number
package cs6301.g36.lp2;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;


public class LP2 {
    static int VERBOSE = 1;
    public static void main(String[] args) throws FileNotFoundException {
        Scanner in;
        if (args.length > 0) {
            File inputFile = new File(args[0]);
            in = new Scanner(inputFile);
        } else {
            in = new Scanner(System.in);
        }
        int start = 1;
        if(args.length > 1) {
            start = Integer.parseInt(args[1]);
        }
        if(args.length > 2) {
            VERBOSE = Integer.parseInt(args[2]);
        }
        Graph g = Graph.readDirectedGraph(in);
        Graph.Vertex startVertex = g.getVertex(start);

        Timer timer = new Timer();
        Euler euler = new Euler(g, startVertex);
        euler.setVerbose(VERBOSE);

        boolean eulerian = euler.isEulerian();

        if(!eulerian) {
            return;
        }
        List<Graph.Edge> tour = euler.findEulerTour();
        timer.end();

        if(VERBOSE > 0) {
            System.out.println("Output:\n_________________________");
            for(Graph.Edge e: tour) {
                System.out.print(e);
            }
            System.out.println();
            System.out.println("_________________________");
        }
        System.out.println(timer);
    }
}
