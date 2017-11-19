/**
 *  Group 36
 *
 * Team Member:
 * @author Wenjie Li
 * @author Meng-Ju Lu
 * @author Xin Tong
 */
/** Starter code for AVL Tree
 */
package cs6301.g36;

import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;

/**
 * refer some codes from 's Weiss, Mark Allen. text book "Data structures and algorithm analysis in Java / Mark Allen Weiss. – 3rd ed."
 * http://users.cis.fiu.edu/~weiss/dsaajava/code/DataStructures/AvlTree.java
 * @param <T>
 */
public class AVLTree<T extends Comparable<? super T>> extends BST<T> {
    private static final int ALLOWED_IMBALANCE=1;
    static class Entry<T> extends BST.Entry<T> {
        int height=0;
        Entry<T> left;
        Entry<T> right;
        Entry(T x, Entry<T> left, Entry<T> right) {
            super(x, left, right);
        }
    }
    Entry<T> root;

    AVLTree() {
	    super();
    }

    static class AVLTreeIterator<T> implements Iterator<T>{
        Stack<Entry<T>> stack;
        Entry<T> cursor;
        public AVLTreeIterator(Entry<T> root){
            stack = new Stack<>();
            cursor=root;
            while(cursor!=null){
                stack.push(cursor);
                cursor=cursor.left;
            }
        }
        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public T next() {
            cursor = stack.pop();
            T curr = cursor.element;
            if(cursor.right!=null){
                cursor=cursor.right;
                while(cursor!=null){
                    stack.push(cursor);
                    cursor=cursor.left;
                }
            }
            return curr;
        }
    }
    /** TO DO: Add x to tree.
     *  If tree contains a node with same key, replace element by x.
     *  Returns true if x is a new element added to tree.
     */
    @Override
    public boolean add(T x) {
        int currSize = size;
        root=add(x,root);
        if(currSize==size){
            return false;
        }else{
            return true;
        }

    }

    @Override
    public Iterator<T> iterator() {
        return new AVLTreeIterator<>(root);
    }


    void printTree(Entry<T> node) {
        for(T t:this){
            System.out.print(" " + t);
        }
    }

    @Override
    public void printTree() {
        System.out.print("[" + size + "]");
        printTree(root);
        System.out.println();
    }

    /**
     * internal method to add a node in AVL tree
     * @param x the item to insert
     * @param node the node that roots the subtree
     * @return the new root of the subtree
     */
     Entry<T> add(T x, Entry<T> node) {
        if(node==null){
            size++;
            return new Entry<>(x,null,null);
        }
        int compareResult = x.compareTo(node.element);
        if(compareResult==0){
            node.element=x;
            return node;
        }else if(compareResult<0){
            node.left=add(x,node.left);
        }else{
            node.right=add(x,node.right);
        }
        return balanced(node);
    }

    /**
     * check existance of element x
     * @param x
     * @return
     */
    public boolean contains(T x) {
        return contains(root,x);
    }

    /**
     * internal method to check existance of element x
     * @param root
     * @param x
     * @return
     */
    private boolean contains(Entry<T> root, T x){
        if(root==null){
            return false;
        }
        int compareResult = x.compareTo(root.element);
        if(x==root.element){
            return true;
        }else if(compareResult<0){
            return contains(root.left,x);
        }else if(compareResult>0){
            return contains(root.right,x);
        }else{
            return false;
        }
    }

    /**
     * internal method to balance the subtree to AVL subtree
     * @param node
     * @return
     */
    private Entry<T> balanced(Entry<T> node) {
        if(node==null){
            return node;
        }
        if(height(node.left)-height(node.right)>ALLOWED_IMBALANCE){
            if(height(node.left.left)>=height(node.left.right)){
                node = rotateWithLeftChild(node);
            }else{
                node = doubleWithLeftChild(node);
            }
        }else if(height(node.right)-height(node.left)>ALLOWED_IMBALANCE){
            if (height(node.right.right)>=height(node.right.left)) {
                node = rotateWithRightChild(node);
            }else{
                node = doubleWithRightChild(node);
            }
        }
        node.height=Math.max(height(node.right),height(node.left))+1;
        return node;
    }

    /**
     * rotate binary tree node with left child
     * for AVL, this is single rotation
     * @param node
     * @return
     */
    private Entry<T> doubleWithLeftChild(Entry<T> node) {
        node.left=rotateWithRightChild(node.left);
        return rotateWithLeftChild(node);
    }

    private Entry<T> rotateWithRightChild(Entry<T> node) {
        Entry<T> tmp = node.right;
        node.right=tmp.left;
        tmp.left=node;
        node.height=Math.max(height(node.left),height(node.right))+1;
        tmp.height=Math.max(height(tmp.right),node.height)+1;
        return tmp;
    }

    private Entry<T> doubleWithRightChild(Entry<T> node) {
        node.right=rotateWithLeftChild(node.right);
        return rotateWithRightChild(node);
    }

    private Entry<T> rotateWithLeftChild(Entry<T> node) {
        Entry<T> tmp = node.left;
        node.left=tmp.right;
        tmp.right=node;
        node.height=Math.max(height(node.left),height(node.right))+1;
        tmp.height=Math.max(height(tmp.left),node.height)+1;
        return tmp;
    }

    private int height(Entry<T> node){
        return node==null?-1:node.height;
    }

    /**
     * Remove x from tree. Return x if found, otherwise return null
     * @param x
     * @return
     */
    public T remove(T x) {
        int sizeBeforeRemove=size;
        root = remove(x,root);
        if(sizeBeforeRemove==size){
            return null;
        }else{
            return x;
        }
    }

    Entry<T> remove(T x, Entry<T> node){
        if(node==null){
            return node;
        }
        int compareResult = x.compareTo(node.element);
        if(compareResult<0){
            node.left=remove(x,node.left);
        }else if(compareResult>0){
            node.right=remove(x,node.right);
        }else if(x==node.element){
            if(node.left!=null && node.right!=null){
                node.element = findMin(node.right,node).element;
            }else{
                node=node.left!=null?node.left:node.right;
            }
            size--;
        }else{  // x'value equals node's element's value, but not same instance
            return null;
        }
        return balanced(node);
    }


    public static void main(String[] args){
        AVLTree<Integer> t = new AVLTree<>();
        Scanner in = new Scanner(System.in);
        while(in.hasNext()) {
            int x = in.nextInt();
            if(x > 0) {
                System.out.print("Add " + x + " : ");
                t.add(x);
                t.printTree();
            } else if(x < 0) {
                System.out.print("Remove " + x + " : ");
                t.remove(-x);
                t.printTree();
            } else {
                Comparable[] arr = t.toArray();
                System.out.print("Final: ");
                for(int i=0; i<t.size; i++) {
                    System.out.print(arr[i] + " ");
                }
                System.out.println();
                return;
            }
        }
    }


}

