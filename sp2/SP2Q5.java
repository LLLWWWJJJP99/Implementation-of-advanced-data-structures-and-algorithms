/**
 *
 */
package cs6301.g36.sp2;




import java.lang.reflect.Array;
import java.util.Arrays;

public class SP2Q5{
    public static void main(String[] args){
        int len =200;
        ArrayBasedQueue<Integer> queue = new ArrayBasedQueue<>();
        for(int i=0;i<len/2;i++){
            queue.offer(i);
            System.out.println("add :"+queue.peek());
            System.out.println("size :"+queue.size());
        }
        for(int i=0;i<len/3;i++){
            System.out.println("poll :"+queue.poll());
        }
        for(int i=0;i<len/2;i++){
            queue.offer(i);
            System.out.println("add :"+queue.peek());
            System.out.println("size :"+queue.size());
        }
        for(int i=0;i<len/3;i++){
            System.out.println("poll :"+queue.poll());
        }
        System.out.println("is empty?"+queue.isEmpty());


    }
}

/**
 *
 * Implement array-based, bounded-sized queues, that support the following
 * operations: offer, poll, peek, isEmpty (same behavior as in Java's Queue
 * interface).  In addition, implement the method resize(), which doubles
 * the queue size if the queue is mostly full (over 90%, say), or halves it
 * if the queue is mostly empty (less then 25% occupied, say).  Let the
 * queue have a minimum size of 16, at all times.
 * not thread safe
 * @param <E>
 */
class ArrayBasedQueue<E>   {
    private E[] arr;
    private int length=16; //size of current array
    private int head=0; // head of queue
    private int end=0;  //end of queue
    private int emptyLevel = 25;
    private int fullLevel = 90;

    public ArrayBasedQueue(){
        arr =  (E[])new Object[length]; // WORK-AROUND to create array of generic
    }

    public ArrayBasedQueue(int s){
        if(s>0)
            length=s;
        arr =  (E[])new Object[length]; // WORK-AROUND to create array of generic
    }

    /**
     *
     * @param emptyLevel  percentage,the queue will halve itself if occupaction is less or equal than emptyLevel
     * @param fullLevel   percentage,the queue will double itself if occupaction is larger or equal than emptyLevel
     */
    public ArrayBasedQueue(int emptyLevel, int fullLevel){
        if(emptyLevel<fullLevel && fullLevel>0 && emptyLevel<100){
            this.emptyLevel=emptyLevel;
            this.fullLevel=fullLevel;
        }
        arr =  (E[])new Object[length]; // WORK-AROUND to create array of generic
    }

    private boolean mostlyEmpty(){
        return length>16 && size()<=(double)emptyLevel/100*length;
    }

    private boolean mostlyFull(){
        return size()>=(double)fullLevel/100*length;
    }

    public int size(){
        return (end+length-head)%length;
    }
    public synchronized boolean offer(E e) {
        resize();
        arr[end]=e;
        end=(end+length+1)%length;
        return true;
    }


    public synchronized E poll() {
        resize();
        E tmp = arr[head];
        head=(head+length+1)%length;
        return tmp;
    }


    public E peek() {
        return arr[head];
    }

    public boolean isEmpty() {
        return size()==0;
    }

    /**
     * doubles the queue size if the queue is mostly full (over 90%, say), or halves it
     * if the queue is mostly empty (less then 25% occupied, say).
     */
    public void resize(){
        if(mostlyFull()){
            resize(length*2);
        }else if(mostlyEmpty()){
            resize(length/2);
        }
    }

    private void resize(int s){
        int size=size();
        if(size<=s) //if s is smaller than the current queue size,do nothing
            return;
        //crate a new array then copy the value one by one
        E[] newArr =  (E[])new Object[s];
        //two pass to shift item from existing array to new array
        int curr=0; //ptr to newArr
        for(int i=0;i<size;i++){
            if(i>=head && i<=(end+length)%length){
                newArr[curr++]=arr[i];
            }
        }

        for(int i=0;i<size;i++){
            if(i<head && i>=head){
                newArr[curr++]=arr[i];
            }
        }
        this.head=0;
        this.end=curr;
        this.arr=newArr;
        this.length=s;
    }


}
