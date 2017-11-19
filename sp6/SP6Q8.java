
package cs6301.g36.sp6;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;


/**
 * Compare the running times of the following two algorithms for the problem of
 * finding the k largest elements of a stream:
 */
public class SP6Q8 {

    /**
     * Use Java's priority queue to keep track of the k largest elements seen
     * @param input
     * @param k
     * @return
     */
    static List<Integer> kLargestJavaPQ(int[] input,int k){
        return SelectAlgorithm.findKLargest2(input,k);
    }

    /**
     * Use your priority queue implementation (problem 5) using the replace()
     * operation in that implementation, instead of delete+add to update PQ.
     * @param input
     * @param k
     * @return
     */
    static List<Integer> kLargestCustomizedPQ(int[] input,int k){
        List<Integer> result = new LinkedList<>();
        Integer[] arr = new Integer[k];
        BinaryHeap<Integer> heap = new BinaryHeap<>(arr, Comparator.naturalOrder(),0);
        for(int i:input){
            if(heap.size<k){
                heap.add(i);
            }else if(heap.size==k){
                heap.replace(i);
            }
        }
        for(int i:heap.pq){
         result.add(i);
        }
        return result;
    }

    public static void testJavaPQRandom(int[] input,int k){
        Shuffle.shuffle(input);
        Timer t1 = new Timer();
        t1.start();
        List<Integer> list = kLargestJavaPQ(input,k);
        t1.end();
        System.out.println("java pq random"+t1);
        for(Integer i:list){
           System.out.print(i+" ");
        }
        System.out.println();
    }
    public static void testJavaPQAscend(int[] input,int k){
        for(int i=0;i<input.length;i++){
            input[i]=i;
        }
        Timer t2 = new Timer();
        t2.start();
        List<Integer> list =kLargestJavaPQ(input,k);
        t2.end();
        System.out.println("java pq ascend"+t2);
        for(Integer i:list){
            System.out.print(i+" ");
        }
        System.out.println();
    }
    public static void testCustomizedPQAscend(int[] input,int k){
        for(int i=0;i<input.length;i++){
            input[i]=i;
        }
        Timer t3 = new Timer();
        t3.start();
        List<Integer> list =kLargestCustomizedPQ(input,k);
        t3.end();
        System.out.println("customize pq ascend"+t3);
        for(Integer i:list){
            System.out.print(i+" ");
        }
        System.out.println();
    }
    public static void testCustomizedPQRandom(int[] input,int k){
        Shuffle.shuffle(input);
        Timer t4 = new Timer();
        t4.start();
        List<Integer> list =kLargestCustomizedPQ(input,k);
        t4.end();
        System.out.println("customized pq random"+t4);
        for(Integer i:list){
            System.out.print(i+" ");
        }
        System.out.println();
    }

    public static void main(String[] args){
        int len = 500000;
        int k=5;
        int[] input = new int[len];
        for(int i=0;i<len;i++){
            input[i]=i;
        }
        testJavaPQAscend(input,k);
        testJavaPQRandom(input,k);
        testCustomizedPQAscend(input,k);
        testCustomizedPQRandom(input,k);

    }



}
