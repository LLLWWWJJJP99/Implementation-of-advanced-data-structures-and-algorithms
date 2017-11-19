/**
 *  Group 36
 *
 * Team Member:
 * @author Wenjie Li
 * @author Meng-Ju Lu
 * @author Xin Tong
 */

package cs6301.g36.sp4;

/**
 *   Binary search: in class we saw a version of binary search that returned
 a boolean to indicate whether x occurs in the array or not.
 Rewrite the function to return the index of the largest element that
 is less than or equal to x.


 public static<T extends Comparable<? super T>> int binarySearch(T[] arr, T x)
 */
public class BinarySearch {

    /**
     * Preconditions: arr[0..n-1] is sorted, and arr[0] <= x < arr[n-1].
     * Returns index i such that arr[i] <= x < arr[i+1].
     * @param arr
     * @param x
     * @param <T>
     * @return
     */
    public static<T extends Comparable<? super T>> int binarySearch(T[] arr, T x){
        int low=0,high=arr.length-1;
        while(low<=high){
            int mid = (low+high)/2;
            if(arr[mid].compareTo(x)==0){
                return mid;
            }else if(arr[mid].compareTo(x)<0){
                low=mid+1;
            }else{
                high=mid-1;
            }
        }
        return low-1;
    }

    public static void main(String[] args){
        Integer[] input = new Integer[11];
        for(int i=0;i<=10;i++){
            input[i]=i*2;
        }
        for(int i:input){
            System.out.print(i+" ");
        }
        System.out.println();
        int res = binarySearch(input,15);
        System.out.println(res);
    }
}
