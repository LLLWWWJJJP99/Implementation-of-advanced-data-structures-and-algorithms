/**
 *  Group 36
 *
 * Team Member:
 * @author Wenjie Li
 * @author Meng-Ju Lu
 * @author Xin Tong
 */

package cs6301.g36.sp6;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Implement Huffman Coding algorithm.  Create a class for representing coding
 * trees.  Use a priority queue to hold the trees.  In each step, the algorithm
 * removes two trees with the smallest frequencies, merges them, and inserts it
 * back into the priority queue.  At the end, there is a single coding tree.
 * Traverse the tree and output the binary codes for each symbol.
 */
public class HuffmanCoding {

    private static class TreeNode implements Comparable<TreeNode>{
        TreeNode left;
        TreeNode right;
        int n=-1;
        double val;
        private TreeNode(int c, double d){
            this.n=c;
            val=d;
        }
        private TreeNode(double d){
            val=d;
        }

        @Override
        public int compareTo(TreeNode o) {
            double res = this.val-o.val;
            return res>0?1:(res==0?0:-1);
        }
    }

    private static List<TreeNode> initCodingTree(double[] frequency){
        List<TreeNode> list = new LinkedList();
        int idx=0;
        for(double d:frequency){
            list.add(new TreeNode(idx++,d));
        }
        return list;
    }

    private static TreeNode mergeTreeNodes(List<TreeNode> list){
        PriorityQueue<TreeNode> heap = new PriorityQueue<>();
        for(TreeNode t:list){
            heap.add(t);
        }
        while(heap.size()>1){
            TreeNode left = heap.poll();
            TreeNode right = heap.poll();
            TreeNode root = new TreeNode(left.val+right.val);
            root.left=left;
            root.right=right;
            heap.add(root);
        }
        return heap.poll();
    }

    private static void traverseCodingTree(TreeNode root,String[] codec){
        traverseCodingTree(root,new StringBuilder(),codec);
    }

    /**
     * Internal coding tree traverse.
     * Using StringBuilder to optimize the performance
     * @param root
     * @param stb
     * @param codec
     */
    private static void traverseCodingTree(TreeNode root,StringBuilder stb,String[] codec){
        //base condition
        if(root==null){
            return;
        }
        if(root.n>=0){
            codec[root.n]=stb.toString();
            return;
        }
        if(root.left!=null){
            stb.append(0);
            traverseCodingTree(root.left,stb,codec);
            stb.setLength(stb.length()-1);
        }
        if(root.right!=null){
            stb.append(1);
            traverseCodingTree(root.right,stb,codec);
            stb.setLength(stb.length()-1);
        }
    }



    public static String[] haffmanCoding(double[] input){
        String[] output = new String[input.length];
        List<TreeNode> list = initCodingTree(input);
        TreeNode root = mergeTreeNodes(list);
        traverseCodingTree(root,output);
        return output;
    }

    public static void main(String[] args){
        double[] input = new double[]{0.2,0.1,0.15,0.3,0.25};
        String[] output = haffmanCoding(input);
        for(String str:output){
            System.out.println(str);
        }
    }


}
