/**
 * Group 36
 *
 * Team Member:
 * @author Wenjie Li
 * @author Meng-Ju Lu
 * @author Xin Tong
 */
package cs6301.g36.sp2;

import java.util.EmptyStackException;


public class SP2Q6{
    public  static void main(String[] args) throws Exception {
        int len =10;
        ArrayBasedStack<Integer> stack = new ArrayBasedStack(10);
        for(int i=0;i<len;i++){
            stack.push(i);
            System.out.println("add :"+stack.peek());
        }
        for(int i=0;i<len/3;i++){
            System.out.println("poll :"+stack.pop());
        }
        for(int i=0;i<len/3;i++){
            System.out.println("poll :"+stack.pop());
        }
        System.out.println("is empty?"+stack.empty());
    }
}

/**
 * Implement array-based, bounded-sized stacks.  Array size is specified
 * in the constructor and is fixed.  When the stack gets full, push()
 * operation should throw an exception.
 * @param <E>
 */
class ArrayBasedStack<E> {
    private int length=16;
    private E[] arr;
    private int curr=0;
    public ArrayBasedStack(int s){
        if(s>0)
            length=s;
        arr = (E[])new Object[length];
    }

    public E push(E e) throws Exception {
        if (e == null) {
            throw new NullPointerException("Can't push a null element");
        }
        if(curr>=length){
            throw new Exception("The stack is full");
        }
        arr[curr++]=e;
        return e;
    }

    public E pop(){
        if(curr==0){
            throw new EmptyStackException();
        }
        E tmp = arr[--curr];
        return tmp;
    }

    public E peek(){
        if(curr==0){
            throw new EmptyStackException();
        }
        return arr[curr-1];
    }

    public boolean empty(){
        return curr==0;
    }





}
