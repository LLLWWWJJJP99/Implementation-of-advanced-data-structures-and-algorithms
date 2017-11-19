/**
 * Group 36
 *
 * Team Member:
 * @author Wenjie Li
 * @author Meng-Ju Lu
 * @author Xin Tong
 */

package cs6301.g36.sp1.Graph;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.LinkedList;

public class Solution2 {
	
	public static void main(String[] args) throws IOException {
		Graph g1 = new Graph(6);
		g1.addEdge(g1.getVertex(1),g1.getVertex(2) , 1);
		g1.addEdge(g1.getVertex(2),g1.getVertex(3) , 1);
		g1.addEdge(g1.getVertex(3),g1.getVertex(5) , 1);
		g1.addEdge(g1.getVertex(5),g1.getVertex(6) , 1);
		g1.addEdge(g1.getVertex(3),g1.getVertex(4) , 1);
		PrintWriter printWriter = new PrintWriter(new FileWriter("graphResult.txt", true));
		printWriter.write("\nInput Graph has " + g1.size() + " vertex:\n");
		for(Graph.Vertex u: g1) {
		    printWriter.write(u.name + ":[");
		    for(Graph.Edge e: u.adj) {
				Graph.Vertex v = e.otherEnd(u);
				printWriter.write(v.name + " ");
		    }
		    printWriter.write("] ");
		    printWriter.write("\n");
		}
		
		printWriter.write("path from u to a node at maximum distance from u:\n");
		LinkedList<Graph.Vertex> res = diameter(g1);
		for(Graph.Vertex vertex : res){
			printWriter.write(vertex.name+" ");
		}
		printWriter.flush();
		printWriter.close();
	}
	
	/**
	* Implement the bfs on the graph with the given root
	* @param source node
	* @param the DAG to BFS
	* @param int array used to record previous node
	* @return LinkedList<Graph.Vertex>: the path from uNode to the farest node 
	*/
	static Graph.Vertex bfs(Graph.Vertex root,Graph g,int[] previous){
		boolean[] visited = new boolean[g.size()];
		Graph.Vertex farestNode = root;
		LinkedList<Graph.Vertex> queue = new LinkedList<>();
		queue.offer(root);
		previous[root.name] = root.name;
		int size = 1;
		while(!queue.isEmpty()){
			Graph.Vertex current = queue.poll();
			visited[current.name] = true;
			for(Graph.Edge edge : current.adj){
				if(!visited[edge.otherEnd(current).name]){
					Graph.Vertex nextNode = edge.otherEnd(current);
					queue.offer(nextNode);
					previous[nextNode.name] = current.name;
				}
			}
			if(--size==0){
				size = queue.size();
				farestNode = current;
			}
		}
		return farestNode;
	}
	
	/**
	* Implement breadth-first search (BFS), and solve the problem of
    * finding the diameter of a tree that works as follows:
    * Run BFS, starting at an arbitrary node as root.  Let u be a node
    * at maximum distance from the root.  Run BFS again, with u as the root.
	* @input Graph to find the diameter
	* @return LinkedList<Graph.Vertex>: the path from uNode to the farest node 
	*/	
	static LinkedList<Graph.Vertex> diameter(Graph g) {
		int[] previous = new int[g.size()];

		LinkedList<Graph.Vertex> bfsResult = new LinkedList<>();
		Graph.Vertex root = g.getVertex(1);
		Graph.Vertex uNode = bfs(root,g,previous);	
		int rootName = uNode.name;
		
		Graph.Vertex farestNode = bfs(uNode,g,previous);
		
		bfsResult.add(farestNode);
		int preNode = previous[farestNode.name];
		while(preNode!=rootName){
			bfsResult.add(g.getVertex(preNode+1));
			preNode = previous[preNode];
		}
		bfsResult.add(g.getVertex(rootName+1));
		// because in 2nd bfs , the 2nd traversal begins from farest node to the uNode(found in 1st bfs), while the question is
		// to the path from uNode to the farest node, so I reverse the list to get the correct order.
		Collections.reverse(bfsResult);
		return bfsResult;
	}

}
