/**
 * Group 36
 *
 * Team Member:
 * @author Wenjie Li
 * @author Meng-Ju Lu
 * @author Xin Tong
 */
package cs6301.g36.sp2;

import java.util.Arrays;
import java.util.Stack;

public class SP2Q7{
    public static void main(String[] args){
        int  len =1000000;
        int[] testInput = new int[len];
        for(int i=0;i<len;i++){
            testInput[i]=len-1-i;
        }
        long t1 = System.currentTimeMillis();
        mergeSortNonRecursive.mergeSortNonRecursive(testInput);
        long t2 = System.currentTimeMillis();
        long t = (t2-t1);
        System.out.println("run time:"+t+" ms");

        /**
         for(int i=0;i<len;i++){
         if(i!=testInput[i])
         System.out.println("wrong sort"+testInput[i]+"should be"+i);
         }
         System.out.println("correct sort");
         */
    }
}

class mergeSortNonRecursive {

    /**
     *  Write Merge sort algorithm without using recursion by maintaining your
     *  own stack and simulating how the compiler implements function calls.
     * @param input
     */
    public static void mergeSortNonRecursive(int[] input){
        int[] arr;
        Stack<int[]> unsortedStack = new Stack();
        Stack<int[]> sortedStack = new Stack();
        unsortedStack.push(input);
        //while !unsortedStack.isempty,  needs to pop int[] from stack , assign to arr,  pop status
        //if(arr.length>1), split arr to arr.left and arr.right,push left and right into stack, push false to stack twice
        //else if(arr.length<=1), push arr to sortedStack (while (!unsorted.isempty && peek().length==arr,merge and pushback to stack again)
        //

        while(!unsortedStack.empty()) {
                arr = unsortedStack.pop();
                if (arr.length > 1) {
                    int begin = 0,end=arr.length,mid=arr.length/2;
                    int[] left = Arrays.copyOfRange(arr, begin, mid);
                    int[] right = Arrays.copyOfRange(arr, mid, end);
                    unsortedStack.push(left);
                    unsortedStack.push(right);
                } else {
                    while(!sortedStack.isEmpty() && sortedStack.peek().length<=arr.length){
                        arr = merge(arr, sortedStack.pop());
                    }
                    sortedStack.push(arr);
                }
        }
        while(sortedStack.size()>1){
            arr = merge(sortedStack.pop(), sortedStack.pop());
            sortedStack.push(arr);
        }
        int[] tmp = sortedStack.pop();
        for(int i=0;i<input.length;i++){
            input[i]=tmp[i];
        }

    }

    public static int[] merge(int[] left,int[] right){
        if(left==null && right==null)
            return null;
        if(left==null)  return right;
        if(right==null) return left;
        int[] result = new int[left.length+right.length];
        int ptr1=0,ptr2=0,ptr=0;
        while(ptr1<left.length && ptr2<right.length){
            result[ptr++]=left[ptr1]<right[ptr2]?left[ptr1++]:right[ptr2++];
        }
        while(ptr1<left.length){
            result[ptr++]=left[ptr1++];
        }
        while(ptr2<right.length){
            result[ptr++]=right[ptr2++];
        }
        return result;
    }





}
