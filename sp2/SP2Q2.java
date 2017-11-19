/**
 * Group 36
 *
 * Team Member:
 * @author Wenjie Li
 * @author Meng-Ju Lu
 * @author Xin Tong
 */
package cs6301.g36.sp2;

import java.util.Iterator;

public class SP2Q2{
    public static void main(String[] args){
        int len =1000000;
        SortableList<Integer> sortableList = new SortableList<>();
        for(int i=len-1;i>=0;i--){
            sortableList.add(i);
        }

        long start = System.currentTimeMillis();
        sortableList.mergeSort();
        long end = System.currentTimeMillis();
        System.out.println("merge sort "+len+" length sortablelist"+" with run time:"+(end-start)+"ms");

    }
}


/**
 *  Write the Merge sort algorithm that works on linked lists.  This will
 * be a member function of a linked list class, so that it can work with
 * the internal details of the class.  The function should use only
 * O(log n) extra space (mainly for recursion), and not make copies of
 * elements unnecessarily.  You can start from the SinglyLinkedList class
 * provided or create your own.
 *
 * @param <T>
 */
class SortableList<T extends Comparable<? super T>> extends SinglyLinkedList<T> {
    /**
     * merge this list and otherList, both this list and other list has to be sorted
     * @param otherList previously sorted list
     */
    void merge(SortableList<T> otherList) {  // Merge this list with other list
        SortableList<T> left = new SortableList<>(); //use left to hold the entries in this list temporarily
        Iterator<T> it1 = this.iterator();
        while(it1.hasNext()){
            left.add(it1.next());
            it1.remove();
        }
        it1 = left.iterator();
        Iterator<T> it2 = otherList.iterator();
        //merge left and other , then add the results into this list;
        T tmp1 = it1.hasNext()?it1.next():null;
        T tmp2 = it2.hasNext()?it2.next():null;
        while(tmp1!=null && tmp2!=null){
            if(tmp1.compareTo(tmp2)==0){
                this.add(tmp1);
                this.add(tmp2);
                tmp1 = it1.hasNext()?it1.next():null;
                tmp2 = it2.hasNext()?it2.next():null;
            } else if (tmp1.compareTo(tmp2)<0) {
                this.add(tmp1);
                tmp1 = it1.hasNext()?it1.next():null;
            }else{
                this.add(tmp2);
                tmp2 = it2.hasNext()?it2.next():null;
            }
        }
        while(tmp1!=null){
            this.add(tmp1);
            tmp1 = it1.hasNext()?it1.next():null;
        }
        while(tmp2!=null){
            this.add(tmp2);
            tmp2 = it2.hasNext()?it2.next():null;
        }

    }

    /**
     * merge sort this list
     * recursively split the list into two parts(this and right), then merge back the right part with left part
     */
    void mergeSort() {
        //base
        if(this.size<=1){
            return;
        }
        //divide and conquer
        Iterator<T> fast = this.iterator();
        Iterator<T> slow = this.iterator();
        //use on fast iterator one slow iterator iterate the list to find the preMid iterator
        //show should be in the middle of list when fast ran out
        SortableList<T> right = new SortableList<>();
        while(fast.hasNext()){
            fast.next();
            if(fast.hasNext()){
                fast.next();
                slow.next();
            }
        }
        while(slow.hasNext()){
            right.add(slow.next());
            slow.remove();
        }
        //divide
        this.mergeSort();
        right.mergeSort();
        //merge
        this.merge(right);

    }

    /**
     * mergeSort the list
     * @param list
     * @param <T> the type of object held by SortableList
     */
    public static<T extends Comparable<? super T>> void mergeSort(SortableList<T> list) {
        list.mergeSort();
    }




}