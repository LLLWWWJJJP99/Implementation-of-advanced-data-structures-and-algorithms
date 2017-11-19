package cs6301.g36.sp6;

/**
 * Union Find Utitliy
 */
public class UnionFind {

    int[] parent;
    int[] height;

    public UnionFind(int n){
        parent = new int[n];
        height = new int[n];
        for(int i=0;i<parent.length;i++){
            parent[i]=i;
        }
    }

    public int find(int i){
        if(i<0 || i>=parent.length){
            throw new RuntimeException("parameter of i out of range!");
        }
        while(i!=parent[i]){
            parent[i]=parent[parent[i]]; //path compression
            i=parent[i];
        }
        return i;
    }

    public void union(int p, int q){
        int rootP = find(p);
        int rootQ = find(q);
        if(rootP==rootQ){
            return;
        }
        if(height[p]<height[q]){  //merge by height
            parent[p]=rootQ;
        }else if(height[p]>height[q]){
            parent[q]=rootP;
        }else{
            parent[rootP]=rootQ;
            height[rootQ]++;
        }

    }


}
