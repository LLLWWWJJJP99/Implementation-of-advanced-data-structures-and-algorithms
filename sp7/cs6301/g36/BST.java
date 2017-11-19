/**
 *  Group 36
 *
 * Team Member:
 * @author Wenjie Li
 * @author Meng-Ju Lu
 * @author Xin Tong
 */

/**
 *
 * @author
 *  Binary search tree (starter code)
 **/

package cs6301.g36;

import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;

public class BST<T extends Comparable<? super T>> implements Iterable<T> {
    static class Entry<T> {
        T element;
        Entry<T> left, right;

        Entry(T x, Entry<T> left, Entry<T> right) {
            this.element = x;
	    this.left = left;
	    this.right = right;
        }
    }
    
    private Entry<T> root;
    int size;

    public BST() {
        root = null;
        size = 0;
    }


    /**
     * check whether x is contained in the BST in O(LogN)
     * @param x
     * @return boolean
     */
    public boolean contains(T x) {
        return contains(root,x);
    }

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
     * get the element in the tree which has the same value as x, return null otherwise
     * @param x
     * @return
     */
    public T get(T x) {
        return get(root,x);
    }

    protected T get(Entry<T> root, T x){
        if(root==null){
            return null;
        }
        int compareResult = x.compareTo(root.element);
        if(compareResult==0){
            return root.element;
        }else if(compareResult<0){
            return get(root.left,x);
        }else{
            return get(root.right,x);
        }
    }

    /** TO DO: Add x to tree. 
     *  If tree contains a node with same key, replace element by x.
     *  Returns true if x is a new element added to tree.
     */
    public boolean add(T x) {
        int sizeBeforeAdd = size;
        root=add(x,root);
        if(sizeBeforeAdd==size){
            return false;
        }else{
            return true;
        }
    }

    /**
     * Internal add method
     * @param x
     * @param node
     * @return
     */
    Entry<T> add(T x,Entry<T> node) {
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
        return node;
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
                node=(node.left!=null)?node.left:node.right;
            }
            size--;
        }else{  // x'value equals node's element's value, but not same instance
            return null;
        }
        return node;
    }

    /**
     * helper method for method {@link #remove(Comparable, Entry)}
     * find minimum node in binary search subtree with node as root
     * @param node
     * @param parent
     * @return
     */
     Entry<T> findMin(Entry<T> node,Entry<T> parent) {
        if(node.left!=null){
            return findMin(node.left,node);
        }else{
            parent.left=null;
            return node;
        }
    }

    /**
     * Iterate elements in sorted order of key (inorder traverse BST)
     * @return
     */
    public Iterator<T> iterator() {
	    return new TreeIterator<>(root);
    }

    static class TreeIterator<T> implements Iterator<T>{
        Stack<Entry<T>> stack;
        Entry<T> cursor;
        public TreeIterator(Entry<T> root){
            stack = new Stack();
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

    public static void main(String[] args) {
	BST<Integer> t = new BST<>();
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


    /**
     * Create an array with the elements using in-order traversal of tree
     * @return array with elements in ordered
     */
    public Comparable[] toArray() {
        Comparable[] arr = new Comparable[size];
        /* write code to place elements in array here */
        Iterator<T> it = iterator();
        int idx=0;
        while(it.hasNext()){
            arr[idx++]=it.next();
        }
        return arr;
    }

    public void printTree() {
        System.out.print("[" + size + "]");
        printTree(root);
        System.out.println();
    }

    // Inorder traversal of tree
    void printTree(Entry<T> node) {
        for(T t:this){
            System.out.print(" " + t);
        }
    }

}


/*
Sample input:
1 3 5 7 9 2 4 6 8 10 -3 -6 -3 0

Output:
Add 1 : [1] 1
Add 3 : [2] 1 3
Add 5 : [3] 1 3 5
Add 7 : [4] 1 3 5 7
Add 9 : [5] 1 3 5 7 9
Add 2 : [6] 1 2 3 5 7 9
Add 4 : [7] 1 2 3 4 5 7 9
Add 6 : [8] 1 2 3 4 5 6 7 9
Add 8 : [9] 1 2 3 4 5 6 7 8 9
Add 10 : [10] 1 2 3 4 5 6 7 8 9 10
Remove -3 : [9] 1 2 4 5 6 7 8 9 10
Remove -6 : [8] 1 2 4 5 7 8 9 10
Remove -3 : [8] 1 2 4 5 7 8 9 10
Final: 1 2 4 5 7 8 9 10 

*/
