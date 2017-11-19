package cs6301.g36.lp3;



import java.util.*;

public class Utilities {
    int rootVertex;
    public Utilities(Graph.Vertex root){
        rootVertex=root.name;
    }

    List<DMSTGraph.DMSTEdge> BFStraversal(DMSTGraph g, DMSTGraph.DMSTVertex root){
        clearVisit(g);
        List<DMSTGraph.DMSTEdge> result = new LinkedList<>();
        Queue<DMSTGraph.DMSTVertex> queue = new LinkedList<>();
        queue.add(root);
        while(!queue.isEmpty()){
            DMSTGraph.DMSTVertex dv = queue.poll();
            dv.seen=true;
            for(Graph.Edge edge:dv){
                if(edge.weight==0){
                    DMSTGraph.DMSTVertex tmp = (DMSTGraph.DMSTVertex) edge.to;
                    if(!tmp.seen){
                        result.add((DMSTGraph.DMSTEdge) edge);
                        queue.add(tmp);
                    }
                }
            }
        }
        return result;
    }

    void getDFSOrder(DMSTGraph g, DMSTGraph.DMSTVertex root,List<DMSTGraph.DMSTVertex> list){
        root.seen=true;
        for(Graph.Edge e:root){
            if(e.weight==0){
                DMSTGraph.DMSTVertex dv = (DMSTGraph.DMSTVertex) e.to;
                if(!dv.seen){
                    getDFSOrder(g,dv,list);
                }
            }
        }
        list.add(0,root);
    }

    void clearVisit(Graph g){
        for(DMSTGraph.Vertex v:g.v){
            if(v==null){
                break;
            }
            DMSTGraph.DMSTVertex dv = (DMSTGraph.DMSTVertex) v;
            dv.seen=false;
        }
    }



    /**
     * mark the DMSTVertex with corresponding SCC id by using Kosaraju’s algorithm
     * @param g
     */
    List<DMSTGraph.DMSTVertex> runSCC(DMSTGraph g){
        //find the superVertex which contains more than one vertex/supervertex
        List<DMSTGraph.DMSTVertex> res = markSCC(g);
        //meld the outgoing edges of the included vertexs inside supervetex
        shrinkSCC(g,res,true);
        //meld the incoming edges of the included vertexs inside supervetex
        shrinkSCC(g,res,false);
        return res;
    }


    boolean check(Collection<DMSTGraph.DMSTEdge> list,DMSTGraph g){
       int i = countVertex(list).size();
       int cnt = 0;
       for(Graph.Vertex v:g){
           cnt++;
       }
       return cnt==i;
    }


     Set<DMSTGraph.DMSTVertex> countVertex(Collection<DMSTGraph.DMSTEdge> list){
        Set<DMSTGraph.DMSTVertex> set = new HashSet<>();
        if(list==null || list.size()==0){
            return set;
        }
        for(Graph.Edge edge:list){
            DMSTGraph.DMSTVertex from = (DMSTGraph.DMSTVertex) edge.from;
            DMSTGraph.DMSTVertex to = (DMSTGraph.DMSTVertex) edge.to;
            set.add(from);
            set.add(to);
        }
        return set;
    }
    private List<Graph.Vertex> shrinkSCC(DMSTGraph g , List<DMSTGraph.DMSTVertex> list, boolean isReverse) {
        List<Graph.Vertex> output = new LinkedList<>();
        List<DMSTGraph.DMSTEdge> tmp = new LinkedList<>();
        for(DMSTGraph.DMSTVertex dv:list){
            for(DMSTGraph.DMSTVertex dv0:dv.shrunkVertexList){
                for(Graph.Edge e:isReverse?dv0.revAdj:dv0.adj){
                    DMSTGraph.DMSTEdge curr = (DMSTGraph.DMSTEdge) e;
                    if(curr.isDisabled){
                        continue;
                    }
                    DMSTGraph.DMSTVertex xfrom = (DMSTGraph.DMSTVertex) curr.from;
                    DMSTGraph.DMSTVertex xto = (DMSTGraph.DMSTVertex) curr.to;
                    if(xfrom.shrunkTo==null && xto.shrunkTo==null){
                        continue;
                    }
                    DMSTGraph.DMSTVertex xf = xfrom.shrunkTo!=null?xfrom.shrunkTo:xfrom;
                    DMSTGraph.DMSTVertex xt = xto.shrunkTo!=null?xto.shrunkTo:xto;
                    if(xf==xt){
                        curr.disable();
                    }else{
                        //if outgoingEdges contains the xedge, compare weight
                        if(!xf.outgoingEdges.containsKey(xt) || xf.outgoingEdges.get(xt).weight>curr.weight){
                            DMSTGraph.DMSTEdge xedge = new DMSTGraph.DMSTEdge(xf,xt,curr.weight);
                            xedge.image=curr;
                            tmp.add(xedge);
                            xf.outgoingEdges.put(xt,xedge);
                        }
                        curr.isDisabled=true;
                    }
                    //prepare the coming edges transform
                    if(curr.weight==0){
                        output.add(curr.to);
                    }
                }
            }
        }
        for(DMSTGraph.DMSTEdge xedge:tmp){
            xedge.isDisabled=false;
            g.addEdge(xedge);
        }
        return output;
    }

    private List<DMSTGraph.DMSTVertex> markSCC(DMSTGraph g){
        clearVisit(g);
        List<DMSTGraph.DMSTVertex> output = new LinkedList<>();
        Iterator<DMSTGraph.DMSTVertex> it = getDecreasedDFSOrderIterator(g);
        clearVisit(g);
        while(it.hasNext()){
            DMSTGraph.DMSTVertex dv = it.next();
            if(!dv.seen){
                //create a super vertex here
                List<DMSTGraph.DMSTVertex> list = new LinkedList<>();
                dfsVisit(dv,list);
                if(list.size()>1){
                    DMSTGraph.DMSTVertex superVertex = g.addDMSTVertex();
                    superVertex.shrunkVertexList=list;
                    output.add(superVertex);
                    for(DMSTGraph.DMSTVertex v0:list){
                        v0.shrunkTo=superVertex;
                    }
                }
            }
        }
        return output;
    }

    void dfsVisit(DMSTGraph.DMSTVertex root, List<DMSTGraph.DMSTVertex> list){
            root.seen=true;
            list.add(root);
            for(Graph.Edge edge:root.revAdj){
                DMSTGraph.DMSTEdge xedge = (DMSTGraph.DMSTEdge) edge;
                if(xedge.isDisabled || xedge.weight!=0){
                    continue;
                }
                DMSTGraph.DMSTVertex neighbor = (DMSTGraph.DMSTVertex) xedge.from;
                if(!neighbor.seen){
                    dfsVisit(neighbor,list);
                }
            }
    }

    Iterator<DMSTGraph.DMSTVertex> getDecreasedDFSOrderIterator(DMSTGraph g){
        List<DMSTGraph.DMSTVertex> list = new LinkedList();
        for(Graph.Vertex v:g){
            DMSTGraph.DMSTVertex dv = (DMSTGraph.DMSTVertex)v;
            if(!dv.seen){
                getDFSOrder(g,dv,list);
            }
        }
        return list.iterator();
    }

     List<DMSTGraph.DMSTEdge> expand(List<DMSTGraph.DMSTEdge> path, List<DMSTGraph.DMSTEdge> unexpanableList){
        Set<Graph.Vertex> visited = new HashSet<>();
        List<DMSTGraph.DMSTEdge> output = new LinkedList<>();
        for(Graph.Edge e:path){
            DMSTGraph.DMSTEdge edge = (DMSTGraph.DMSTEdge) e;
            DMSTGraph.DMSTVertex dv = (DMSTGraph.DMSTVertex) edge.to;
            if(visited.contains(dv)){
                continue;
            }
            if(edge.image!=null){
                output.add(edge.image);
                if(edge.image.to!=edge.to){
                    output.addAll(findPath(dv.shrunkVertexList,edge.image.to,visited));
                }
            }else{
                unexpanableList.add(edge);
            }
            visited.add(dv);
        }

        return output;
    }
      List<DMSTGraph.DMSTEdge> findPath(List<DMSTGraph.DMSTVertex> set, Graph.Vertex root, Set<Graph.Vertex> expandList) {
          List<DMSTGraph.DMSTEdge> list = new LinkedList<>();
         if(!set.contains(root)){
            return list;
        }
         Set<Graph.Vertex> visited = new HashSet<>();
        if(set==null){
            return list;
        }
        Stack<DMSTGraph.DMSTVertex> stack = new Stack<>();
        stack.add((DMSTGraph.DMSTVertex) root);
        while(!stack.isEmpty()){
            DMSTGraph.DMSTVertex dv = stack.pop();
            visited.add(dv);
            for(Graph.Edge e:dv.adj){
                if(e.weight!=0 || !set.contains(e.to)){
                    continue;
                }
                if(!visited.contains(e.to) ){
                    stack.add((DMSTGraph.DMSTVertex) e.to);
                    if(!expandList.contains(e.to)){
                        list.add((DMSTGraph.DMSTEdge) e);
                        expandList.add(e.to);
                    }
                }
            }
        }
        return list;
    }



}
