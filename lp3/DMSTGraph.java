package cs6301.g36.lp3;


import java.util.*;

public class DMSTGraph extends Graph {
    public int dmst=0;

    public void addEdge(DMSTEdge e) {
        Vertex from  = e.from;
        Vertex to = e.to;
        if(this.directed) {
            from.adj.add(e);
            to.revAdj.add(e);
        } else {
            from.adj.add(e);
            to.adj.add(e);
        }
    }

    static class DMSTVertex extends Vertex {
        boolean isDisabled=false;
        boolean seen=false;
        public int hashCode() {
            return name;
        }
        //if current vertex is supervertex, store its included vertex/supervertex
        List<DMSTVertex> shrunkVertexList;
        //used for generate the dmst sorted
        List<Edge> dmst=new LinkedList<>();
        //O(1) RT outgoingEdges
        Map<Vertex,Edge> outgoingEdges = new HashMap<>();
        //the supervertex the vertex belongs to
        DMSTVertex shrunkTo;
        DMSTVertex(int n) {
            super(n);
        }
        boolean isDisabled() { return isDisabled; }
        @Override
        public Iterator<Edge> iterator(){return new DMSTEdgeIterator(this);};
        static class DMSTEdgeIterator implements Iterator<Edge> {
            Iterator<Edge> it;
            DMSTEdge cur;
            boolean ready;
            public DMSTEdgeIterator(DMSTVertex node) {
                    it=node.adj.iterator();
            }

            @Override
            public boolean hasNext() {
                if(ready) { return true; }
                if(!it.hasNext()) { return false; }
                cur = (DMSTEdge) it.next();
                while(cur.isDisabled() && it.hasNext()) {
                    cur = (DMSTEdge) it.next();
                }
                ready = true;
                return !cur.isDisabled();
            }

            @Override
            public Edge next() {
                if(!ready) {
                    if(!hasNext()) {
                        throw new NoSuchElementException();
                    }
                }
                ready = false;
                return cur;
            }
        }
    }

    static class DMSTEdge extends Edge {
        boolean isDisabled=false;
        DMSTEdge image = null;
        int w;
        /**
         * Constructor for Edge
         *
         * @param u : Vertex - Vertex from which edge starts
         * @param v : Vertex - Vertex on which edge lands
         * @param w
         */
        DMSTEdge(Vertex u, Vertex v, int w) {
            super(u, v, w);
        }
        boolean isDisabled() {
            DMSTVertex xfrom = (DMSTVertex) from;
            DMSTVertex xto = (DMSTVertex) to;
            return isDisabled || (xfrom.isDisabled() && xto.isDisabled()) || xto.isDisabled();
        }

        public void disable() {
            isDisabled=true;
            DMSTVertex xfrom = (DMSTVertex) from;
            DMSTVertex xto = (DMSTVertex) to;
            xfrom.isDisabled=true;
            xto.isDisabled=true;
        }


    }

    public DMSTGraph(int n) {
        super(n);
    }

    @Override
    public void buildGraph(int n){
        this.n = n;
        this.v = new DMSTVertex[2*n]; //reserve space for vitual vertex
        this.directed = false;  // default is undirected graph
        // create an array of Vertex objects
        for (int i = 0; i < n; i++)
            v[i] = new DMSTVertex(i);

    }

    @Override
    public Iterator<Vertex> iterator() { return new DMSTVertexIterator(this); }


    public DMSTVertex addDMSTVertex(){
        super.n++;
        DMSTVertex dv=new DMSTVertex(size()-1);
        v[size()-1]=dv;
        return dv;
    }

    public void transformWeight(DMSTVertex root){
        for(Vertex vertex:this){
            if(vertex==root){
                continue;
            }
            Edge min = null;
            //ignore disabled edge which is replace with new edge
            for(Edge e :vertex.revAdj){
                if(((DMSTEdge)e).isDisabled){
                    continue;
                }
                if(min==null || e.weight<min.weight){
                    min=e;
                }
            }
            int miminumWeight=min==null?0:min.weight;
            if(miminumWeight==0){
                continue;
            }else{
                //update all enabled edges
                for(Edge edge:vertex.revAdj){
                    if(((DMSTEdge)edge).isDisabled){
                        continue;
                    }
                    edge.weight-=miminumWeight;
                }
            }
        }
    }

    public void transformWeight(List<DMSTVertex> list,Graph.Vertex root){
        for(Vertex vertex:list){
            if(vertex==root){
                continue;
            }
            Edge min = null;
            //ignore disabled edge which is replace with new edge
            for(Edge e :vertex.revAdj){
                if(((DMSTEdge)e).isDisabled){
                    continue;
                }
                if(min==null || e.weight<min.weight){
                    min=e;
                }
            }
            int miminumWeight=min==null?0:min.weight;
            if(miminumWeight==0){
                continue;
            }else{
                //update all enabled edges
                for(Edge edge:vertex.revAdj){
                    if(((DMSTEdge)edge).isDisabled){
                        continue;
                    }
                    edge.weight-=miminumWeight;
                }
            }
        }
    }

    public static DMSTGraph readGraph(Scanner in, boolean directed) {
        // read the graph related parameters
        int n = in.nextInt(); // number of vertices in the graph
        int m = in.nextInt(); // number of edges in the graph

        // create a graph instance
        DMSTGraph g = new DMSTGraph(n);
        g.directed = directed;
        for (int i = 0; i < m; i++) {
            int u = in.nextInt();
            int v = in.nextInt();
            int w = in.nextInt();
            g.addEdge(g.getVertex(u), g.getVertex(v), w);
        }
        return g;
    }

    @Override
    public void addEdge(Vertex from, Vertex to, int weight) {
        DMSTEdge e = new DMSTEdge(from, to, weight);
        e.w=weight;
        if(this.directed) {
            from.adj.add(e);
            to.revAdj.add(e);
        } else {
            from.adj.add(e);
            to.adj.add(e);
        }
    }

    public DMSTEdge addEdge(Vertex from, Vertex to, int weight, DMSTEdge image){
        DMSTEdge e = new DMSTEdge(from, to, weight);
        e.image=image;
        e.w=image.w;
        if(this.directed) {
            from.adj.add(e);
            to.revAdj.add(e);
        } else {
            from.adj.add(e);
            to.adj.add(e);
        }
        return e;
    }

    static class DMSTVertexIterator implements Iterator<Vertex> {
        Iterator<Vertex> it;
        DMSTVertex xcur;
        public DMSTVertexIterator(DMSTGraph g) {
            it = new ArrayIterator<Vertex>(g.v, 0, g.size()-1);  // Iterate over existing elements only
        }

        @Override
        public boolean hasNext() {
            if(!it.hasNext()) { return false; }
            xcur = (DMSTVertex) it.next();
            while(xcur.isDisabled() && it.hasNext()) {
                xcur = (DMSTVertex) it.next();
            }
            return !xcur.isDisabled();
        }

        @Override
        public Vertex next() {
            return xcur;
        }
    }
}
