/**
 *  Group 36
 *
 * Team Member:
 * @author Wenjie Li
 * @author Meng-Ju Lu
 * @author Xin Tong
 */
package cs6301.g36.sp4;

import java.util.LinkedList;
import java.util.List;

public class FindKMissingNumbers {

    /**
     * Given an array of n distinct integers, in sorted order, starting
     * at 1 and ending with n+k, find the k missing numbers in the sequence.
     * Your algorithm should run in O(k+log(n)) time.  Note that a simple
     * linear scan of the array can find the answer, but it will not meet
     * the requirement on the running time.
     * @param arr length of n
     * @param k
     * @return missing number in array with length k
     */
    static List<Integer> findingKMissingNumbers(int[] arr , int k){
        List<Integer> list = new LinkedList<>();
        findingKMissingNumbers(list,arr,0,arr.length-1,1,arr.length+k);
        return list;
    }

    private static void findingKMissingNumbers(List<Integer> list,int[] arr ,int start, int end,int left,int right){
        //base condition
        if(left>right){
            return;
        }
        //handle no missing number case
        if(arr[start]==left && arr[end]==right && right-left==end-start){
            return;
        }

        while(left<arr[start]){  //add missing numbers to the left of left boundary
            list.add(left++);
        }
        while(right>arr[end]){  //add missing numbers to the right of right boundary
            list.add(right--);
        }
        int mid = (start+end)/2;
        findingKMissingNumbers(list,arr,start,mid,left,arr[mid]);  //set arr[mid] as right boundary of left part
        findingKMissingNumbers(list,arr,mid+1,end,arr[mid]+1,right); //set arr[mid] as left boundary of right part

    }

    public static void main(String[] args){
        int len = 100000000;
        int skip = 88888888;
        int[] input = new int[len];
        for(int i=0;i<len;i++){
            if(i>=skip){
                input[i]=i+2;
            }else{
                input[i]=i+1;
            }

        }

        Timer t1 = new Timer();
        List<Integer> res = findingKMissingNumbers(input,1);
        t1.end();
        System.out.println(t1);
        for(Integer i:res){
            System.out.print(i+" ");
        }

    }

}
