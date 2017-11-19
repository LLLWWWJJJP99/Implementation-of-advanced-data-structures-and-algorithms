/**
 *  Group 36
 *
 * Team Member:
 * @author Wenjie Li
 * @author Meng-Ju Lu
 * @author Xin Tong
 */
package cs6301.g36.sp6;


import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 *   Sort an array using k-way merge (normally used for external sorting):
 *   Split the array A into k fragments, sort them recursively, and merge them
 *   using a priority queue (of size k).
 *   [In external sorting applications, intermediate sorted subarrays are
 *   written to disk.  The algorithm sorts A by using buffers of size O(k).]
 */
public class KWayMerge<T extends Comparable<? super T>> {

    public static void main(String[] args){
        int len = 500000;
        Integer[] arr = new Integer[len];
        for(int i=0;i<len;i++){
            arr[i]=Integer.valueOf(len-i);
        }
        Shuffle.shuffle(arr);
        System.out.println();
        Timer t1 = new Timer();
        sort(arr,25);
        t1.end();
        System.out.println();
        System.out.println(t1);
    }

    /**
     * static inner class represents the k fragments
     */
    private static class Fragment implements Cloneable{
        int start;
        int end;
        private Fragment(int s,int e){
            start=s;
            end=e;
        }
        @Override
        protected Fragment clone(){
            return new Fragment(start,end);
        }
    }
    /**
     * sort the array using k-way merge algorithm
     * @param <T>
     * @param array
     */
    public static<T extends Comparable<?  super T>> void sort(T[] array, int k){
        sort(array,new Fragment(0,array.length-1),k);
    }

    /**
     * Internal method to sort array in the range of [fragment.start,fragment.end] with k way sort
     * @param array
     * @param fragment
     * @param k
     * @param <T>
     */
    private static<T extends Comparable<?  super T>> void sort(T[] array,Fragment fragment,int k){
        //base condition
        if(fragment.start==fragment.end){
            return;
        }
        int len=fragment.end-fragment.start+1;
        List<Fragment> fragments = splitFragment(fragment,len,Math.min(k,len));
        for(Fragment f:fragments){
            sort(array,f.clone(),k);
        }
        kWayMerge(fragments,fragment,array,k);
    }


    /**
     * Internal method to merge k fragments into one big fragment in arr
     * @param fragments
     * @param fragment
     * @param array
     * @param k
     * @param <T>
     */
    private static<T extends Comparable<?  super T>> void kWayMerge(List<Fragment> fragments,Fragment fragment, T[] array,int k) {
        int start = fragment.start;
        int end = fragment.end;
        int index=0;
        T[] tmp = (T[]) new Comparable[end-start+1];
        PriorityQueue<Fragment> heap = new PriorityQueue<>(k, Comparator.comparing(f1 -> array[f1.start]));
        for(Fragment f:fragments){
            heap.add(f);
        }
        while(!heap.isEmpty()){
            Fragment f = heap.poll();
            tmp[index++]=array[f.start++];
            if(f.start<=f.end){
                heap.add(f);
            }
        }
        System.arraycopy(tmp,0,array,start,end-start+1);
    }

    /**
     * Internal help function to split array from one big fragment into k smaller fragments
     * @param fragment
     * @param length
     * @param k
     * @param <T>
     * @return
     */
    private static<T extends Comparable<?  super T>> List<Fragment> splitFragment(Fragment fragment,int length, int k) {
        List<Fragment> list = new LinkedList();
        int step =length/k;
        int offset = fragment.start;
        for(int i=0;i<k;i++){
            if(i==k-1){
                list.add(new Fragment(i*step+offset,offset+length-1));
            }else{
                list.add(new Fragment(i*step+offset,offset+(i+1)*step-1));
            }
        }
        return list;
    }


}
