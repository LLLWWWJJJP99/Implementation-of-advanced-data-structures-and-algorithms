/**
 *  Group 36
 *
 * Team Member:
 * @author Wenjie Li
 * @author Meng-Ju Lu
 * @author Xin Tong
 */

package cs6301.g36.sp5;


import java.util.Random;

/**
 *   Compare the performance of the two versions of partition discussed in class
 *   on the running time of Quick sort, on arrays with distinct elements.
 *   Try arrays that are randomly ordered (by shuffle) and arrays in
 *   descending order.
 */
public class SP5Q1 {
    static Random rand = new Random(4324234);
    public static void quickSort1(int[] A){
        quickSort1(A,0,A.length-1);
    }

    private static void quickSort1(int[] A,int p, int r){
        if(p>=r){
            return;
        }
        int q = partition1(A,p,r);
        quickSort1(A,p,q-1);
        quickSort1(A,q+1,r);
    }

    /**
     * implement the partition with method 1 taught in class
     * @param A
     * @param p
     * @param r
     * @return
     */
    private static int partition1(int[] A, int p, int r) {
        int i = selectRandom(p,r);
        exchangeValue(A,i,r);
        int pivot = A[r];
        i = p-1;
        for(int j=p;j<r;j++){
            if(A[j]<=pivot){
                i++;
                exchangeValue(A,i,j);
            }
        }
        i++;
        exchangeValue(A,i,r);
        return i;
    }

    private static int selectRandom(int p, int r) {
        return rand.nextInt(r-p+1)+p;
    }

    private static void exchangeValue(int[] A, int a, int b){
        int tmp = A[a];
        A[a]=A[b];
        A[b] = tmp;
    }

    public static void quickSort2(int[] A){
        quickSort2(A,0,A.length-1);
    }

    private static void quickSort2(int[] A, int p, int r) {
        if(p>=r){
            return;
        }
        int q = partition2(A,p,r);
        quickSort2(A,p,q);
        quickSort2(A,q+1,r);
    }

    /**
     * implement the partition with method 2 taught in class
     * @param A
     * @param p
     * @param r
     * @return
     */
    private static int partition2(int[] A, int p, int r) {
        int idx = selectRandom(p,r);
        int pivot = A[idx];
        int i= p-1,j=r+1;
        while(true){
            do{
                i++;
            }while(A[i]<pivot);

            do{
                j--;
            }while(A[j]>pivot);

            if(i>=j){
                return j;
            }
            exchangeValue(A,i,j);
        }
    }


    /**
     * Method 2: Sort decreased sorted  array length: 50000000 run time : Time: 3634 msec.
     Memory: 195 MB / 429 MB.
     Method 2: Sort randomly shuffled array length: 50000000 run time : Time: 7686 msec.
     Memory: 195 MB / 429 MB.
     Method 1: Sort decreased sorted  array length: 50000000 run time: Time: 3188 msec.
     Memory: 195 MB / 429 MB.
     Method 1: Sort randomly shuffled array length: 50000000 run time: Time: 5781 msec.
     Memory: 195 MB / 429 MB.
     * @param args
     */
    public static void main(String[] args){
        int len = 50000000;
        int[] A = new int[len];
        for(int i=0;i<len;i++){
            A[i]=len-i;
        }
        Timer t0 = new Timer();
        t0.start();
        quickSort2(A);
        t0.end();
        System.out.println("Method 2: Sort decreased sorted  array length: "+len+" run time : "+t0);
        Shuffle.shuffle(A);
        Timer t1 = new Timer();
        quickSort2(A);
        t1.end();
        System.out.println("Method 2: Sort randomly shuffled array length: "+len+" run time : "+t1);
        for(int i=0;i<len;i++){
            A[i]=len-i;
        }
        Timer t3 = new Timer();
        t3.start();
        quickSort1(A);
        t3.end();
        System.out.println("Method 1: Sort decreased sorted  array length: "+len+" run time: "+t3);
        Timer t2 = new Timer();
        Shuffle.shuffle(A);
        t2.start();
        quickSort1(A);
        t2.end();
        System.out.println("Method 1: Sort randomly shuffled array length: "+len+" run time: "+t2);
    }

}
