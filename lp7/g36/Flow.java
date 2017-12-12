// Starter code for LP7
package cs6301.g36;

import cs6301.g36.Graph.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
/**
 *  Group 36
 *
 * Team Member:
 * @author Wenjie Li
 * @author Meng-Ju Lu
 * @author Xin Tong
 */

public class Flow extends GraphAlgorithm<Flow.FlowVertex>{
	FlowVertex source;
	FlowVertex sink;
	HashMap<Edge, FlowEdge> map;
	/**
	 * level: dinitz level 
	 * height: relabel push front height
	 * parent: store vertex that was visited right before this vertex
	 * excess: extra flow in vertex
	 * visited: whether this vertex is visited in bfs
	 * name: name of vertex
	 * adj, revAdj: similar to adj, revAdj in Graph.java
	 * self: reference of vertex
	 */
	static class FlowVertex{
		int name;
		int excess;
		int height;
		int level;
		boolean visited;
		List<FlowEdge> parent;
		Vertex self;
		List<FlowEdge> adj;
		List<FlowEdge> revAdj;
		public FlowVertex(Vertex self) {
			this.name = self.name;
			this.height = 0;
			this.self = self;
			this.excess = 0;
			adj = new LinkedList<>();
			revAdj = new LinkedList<>();
			parent = new LinkedList<>();
		}
		@Override
		public String toString() {
			return "Vertex [name=" + (name + 1) + ", excess=" + excess + ", height=" + height + "]";
		}
		
	}
	
	/**
	 * capacity: capacity of this edge
	 * flow:flow on this edge
	 * self: reference of self edge
	 */
	static class FlowEdge{
		int capacity;
		int flow;
		Edge self;
		public FlowEdge(int capacity, int flow, Edge self) {
			this.capacity = capacity;
			this.flow = flow;
			this.self = self;
		}
		@Override
		public String toString() {
			return "Edge [capacity=" + capacity + ", flow=" + flow + ", from" + self.from +":to"+ self.to + "]";
		}
		
		
		@Override
		public boolean equals(Object obj) {
			if(obj == null) return false;
			if(obj == this) return true;
			if(!(obj instanceof FlowEdge)) return false;
			FlowEdge other = (FlowEdge) obj;
			return this.self.equals(other.self);
		}
		@Override
		public int hashCode() {
			return this.self.hashCode();
		}
		
		public FlowVertex otherEnd(FlowVertex other, FlowVertex[] nodes) {
			int ee = this.self.otherEnd(other.self).name;
			FlowVertex end = nodes[ee];
			return end;
		}
	}
	
	public Flow(Graph g, Vertex s, Vertex t, HashMap<Edge, Integer> capacity) {
		super(g);
		node = new FlowVertex[g.size()];
		map = new HashMap<>();
		for(Vertex v : g) {
			node[v.name] = new FlowVertex(v);
			List<FlowEdge> out = new LinkedList<>();
			for(Edge e : v.adj) {
				FlowEdge edge = null;
				if(!map.containsKey(e)) { 
					edge = new FlowEdge(capacity.get(e), 0, e);
					map.put(e, edge);
				}
				else { edge = map.get(e); }
				out.add(edge);
			}
			
			List<FlowEdge> in = new LinkedList<>();
			for(Edge e : v.revAdj) {
				FlowEdge edge = null;
				if(!map.containsKey(e)) { 
					edge = new FlowEdge(capacity.get(e), 0, e);
					map.put(e, edge);
				}
				else { edge = map.get(e); }
				in.add(edge);
			}
			
			node[v.name].adj = out;
			node[v.name].revAdj = in;
		}
		source = node[s.name];
		sink = node[t.name];
	}
	
	/**
	 * initialize all vertex and edges for relabelToFront algo
	 */
	private void initializeRelabel() {
		for(FlowVertex v : node) {
			v.parent = new LinkedList<>();
			v.level = -1;
			v.visited = false;
			v.excess = 0;
			v.height = 0;
			for(FlowEdge edge : v.adj) { edge.flow = 0; }
			for(FlowEdge edge : v.revAdj) { edge.flow = 0; }
		}
		source.height = node.length;
		
		for(FlowEdge e : source.adj) {
			FlowVertex u = e.otherEnd(source, node);
			e.flow = e.capacity;
			source.excess = source.excess - e.capacity;
			u.excess = u.excess + e.capacity;
		}
	}
	
	/**
	 * initialize all vertex and edges for Dinitz algo
	 */
	private void initializeDinitz() {
		for(FlowVertex v : node) {
			v.parent = new LinkedList<>();
			v.level = 0;
			v.visited = false;
			for(FlowEdge edge : v.adj) { edge.flow = 0; }
			for(FlowEdge edge : v.revAdj) { edge.flow = 0; }
			v.adj.addAll(v.revAdj);
			v.revAdj = new LinkedList<>();
		}
	}
	
	/**
	 * push escess of u to v by following e
	 * @param u
	 * @param v
	 * @param e
	 */
	private void push(FlowVertex u, FlowVertex v, FlowEdge e) {
		int delta;
		if(e.self.from.equals(u.self)) {
			delta = e.capacity - e.flow;
		}else{
			delta = e.flow;
		}
		delta = Math.min(delta, u.excess);
		if(e.self.from.equals(u.self)) {
			e.flow += delta;
		}else {
			e.flow -= delta;
		}
		u.excess -= delta;
		v.excess += delta;
	}
	
	/**
	 * relabel vertex u
	 * @param u
	 */
	private void relabel(FlowVertex u) {
		int height = Integer.MAX_VALUE;
		for(FlowEdge e : u.adj) {
			if(e.capacity > e.flow) {
				FlowVertex v = e.otherEnd(u, node);
				height = Math.min(height, v.height);
			}
		}
		
		for(FlowEdge e : u.revAdj) {
			if(0 < e.flow) {
				FlowVertex v = e.otherEnd(u, node);
				height = Math.min(height, v.height);
			}
		}
		u.height = height + 1;
	}
	
	/**
	 * @param u vertex that points to another vertex in Gf
	 * @param e edge that u follows to point to another point
	 * @return whether an edge in Gf point to another vertex by following e
	 */
	private boolean inGf(FlowVertex u, FlowEdge e) {
		return u.self.equals(e.self.from) ? e.capacity > e.flow : e.flow > 0;
	}
	
	/**
	 * relabel or push a vertex
	 * @param u vertex that involves with relabel or push
	*/
	private void discharge(FlowVertex u) {
		while(u.excess > 0) {
			for(FlowEdge e : u.adj) {
				FlowVertex v = e.otherEnd(u, node);
				if(inGf(u, e) && u.height == v.height + 1) {
					push(u, v, e);
					if(u.excess == 0) return;
				}
			}
			
			for(FlowEdge e : u.revAdj) {
				FlowVertex v = e.otherEnd(u, node);
				if(inGf(u, e) && u.height == v.height + 1) {
					push(u, v, e);
					if(u.excess == 0) return;
				}
			}
			relabel(u);
			
		}
	}
	
	// Return max flow found by Dinitz's algorithm
	public int dinitzMaxFlow() {
		initializeDinitz();
		int flow = 0;
		while(bfs(source, null)) {
			for(FlowEdge p : sink.parent) {
				int minFlow = dfs(sink, p);
				flow += minFlow;
				sendFlow(sink, p, minFlow);
			}
		}
		return flow;
	}
	
	/**
	 * do a bfs in dintiz algo
	 * @return
	 */
	private boolean bfs(FlowVertex s, HashSet<Vertex> set) {
		for(FlowVertex v : node) {
			v.parent = new LinkedList<>();
			v.visited = false;
		}
		Queue<FlowVertex> q = new LinkedList<>();
		q.add(s);
		s.visited = true;
		if(set != null) { set.add(s.self); }
		boolean find = false;
		while (!q.isEmpty()) {
			FlowVertex u = q.remove();
			for (FlowEdge e : u.adj) {
				FlowVertex v = e.otherEnd(u, node);
				if (!v.visited && inGf(u, e)) {
					if(v.equals(sink)) find = true;
					visit(u, v, e);
					if(set != null) { set.add(v.self); }
					if(!v.equals(sink)) { q.add(v); }
				}
			}
		}
		return find;
	}
	
	/**
	 * visit v from u by following e
	 * @param u
	 * @param v
	 * @param e
	 */
	private void visit(FlowVertex u, FlowVertex v, FlowEdge e) {
		v.level = u.level + 1;
		if(!v.equals(sink)) { v.visited = true; }
		v.parent.add(e);
	}
	
	/**
	 * find mininal residual capacity on augumented path
	 * @param cur current vertex
	 * @param p edge that is used to visit parent vertex
	 * @return mininal residual capacity in augumented path
	 */
	private int dfs(FlowVertex cur, FlowEdge p) {
		if(p.otherEnd(cur, node).equals(source)) {// here parent of cur in Gf is not the vertex in other end
			return cur.self.equals(p.self.to) ? p.capacity - p.flow : p.flow;
		}
		FlowVertex other = p.otherEnd(cur, node);
		FlowEdge next = other.parent.get(0);
		int cf = (cur.self.equals(p.self.to) ? p.capacity - p.flow : p.flow);
		return Math.min(dfs(other, next), cf);
	}
	
	/**
	 * use mininal residual capacity found on augumented path to send flow along augumented path from s to t
	 * @param cur current vertex
	 * @param e edge that is used to visit parent vertex
	 * @param minFlow mininal residual capacity in augumented path
	 */
	private void sendFlow(FlowVertex cur, FlowEdge e, int minFlow) {
		if(e.otherEnd(cur, node).equals(source)) {
			if(cur.self.equals(e.self.to)) {
				e.flow += minFlow;
			}else {
				e.flow -= minFlow;
			}
			return;
		}
		
		if(cur.self.equals(e.self.to)) {
			e.flow += minFlow;
		}else {
			e.flow -= minFlow;
		}
		FlowVertex other = e.otherEnd(cur, node);
		FlowEdge next = other.parent.get(0);
		sendFlow(other, next, minFlow);
	}
	
	// Return max flow found by relabelToFront algorithm
	public int relabelToFront() {
		initializeRelabel();
		List<FlowVertex> L = new LinkedList<>();
		for(FlowVertex v : node) {
			if(!v.equals(source) && !v.equals(sink)) {
				L.add(v);
			}
		}
		boolean done = false;
		while(!done) {
			Iterator<FlowVertex> it = L.iterator();
			done = true;
			FlowVertex u = null;
			while(it.hasNext()) {
				u = it.next();
				if(u.excess == 0) continue;
				int oldHeight = u.height;
				discharge(u);
				if(oldHeight != u.height) {
					done = false;
					break;
				}
			}
			
			if(!done) {
				it.remove();
				L.add(0, u);
			}
		}
		return sink.excess;
	}

	// flow going through edge e
	public int flow(Edge e) {
		return map.get(e).flow;
	}

	// capacity of edge e
	public int capacity(Edge e) {
		return map.get(e).capacity;
	}

	/*
	 * After maxflow has been computed, this method can be called to get the
	 * "S"-side of the min-cut found by the algorithm
	 */
	public Set<Vertex> minCutS() {
		HashSet<Vertex> set = new HashSet<>();
		bfs(source, set);
		return set;
	}

	/*
	 * After maxflow has been computed, this method can be called to get the
	 * "T"-side of the min-cut found by the algorithm
	 */
	public Set<Vertex> minCutT() {
		HashSet<Vertex> set = new HashSet<>();
		Set<Vertex> sSet = minCutS();
		for(FlowVertex v : node) {
			if(!sSet.contains(v.self)) {
				set.add(v.self);
			}
		}
		return set;
	}
	// test
	public static void main(String args[]) {
	  /*6
		9
		1 2 16
		1 3 13
		2 4 12
		3 2 4
		4 3 9
		3 5 14
		5 6 4
		5 4 7
		4 6 20*/
		
/*		Graph g = Graph.readDirectedGraph(new Scanner(System.in));
		Vertex s = g.getVertex(1);
		Vertex t = g.getVertex(6);
		Flow flow = new Flow(g, s, t, g.map);
		System.out.println(flow.dinitzMaxFlow());
		Set<Vertex> set1 = flow.minCutS();
		Set<Vertex> set2 = flow.minCutT();
		System.out.println(flow.relabelToFront());*/
		
	}
}