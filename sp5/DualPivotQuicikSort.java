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

/**   Implement dual pivot partition and its version of quick sort.
 Compare its performance with regular quick sort.  Try inputs that
 are distinct, and inputs that have many duplicates.
 */
public class DualPivotQuicikSort {
    static Random rand = new Random(324234);

    public static void dualPivotQuickSort(int[] A,int p , int r){
        if(p>=r){
            return;
        }
        int[] res = dualPivotPartition(A,p,r);
        int x1 = res[0],x2=res[1],k=res[2],j=res[3];
        dualPivotQuickSort(A,p,k-1);
        dualPivotQuickSort(A,j+1,r);
        if(x1!=x2){
            dualPivotQuickSort(A,k+1,j-1);
        }

    }

    private static int[] dualPivotPartition(int[] A,int p, int r){
        int[] res = randomSelect(A,p,r);
        int x1 = A[res[0]], x2 = A[res[1]];

        swap(A,res[0],p);
        if(res[1]==p){
            swap(A,res[0],r);
        }else{
            swap(A,res[1],r);
        }
        int k=p+1,i=p+1,j=r-1;
        while(i<=j){
            if(A[i]>=x1 && A[i]<=x2){ //Case 1: x 1 ≤ A[ i ] ≤ x 2. S 2 grows by 1. i ++
                //S2 grows by 1
                i++;
            }else if(A[i]<x1){  //S 1 grows by 1. Exchange A[ i ] with A[ k ], the left-most element of S 2. i ++
                //S1 grows by 1
                swap(A,i,k);
                i++;
                k++; //?
            }else if(A[j]>x2){  //S 3 grows by 1. j −−
                j--;
            }else if(A[i]>x2 && A[j]<x1){  //Circular swap A[ k ] → A[ i ] → A[ j ] → A[ k ]. k ++ i ++ j −−
                if(k==i){
                  swap(A,k,j);
                }else if(i==j){
                    swap(A,i,k);
                }else if(j==k){
                    swap(A,i,j);
                }else if(i==j && j==k && k==i){

                }else{
                    int tmp = A[k];
                    A[k]=A[j];
                    A[j]=A[i];
                    A[i]=tmp;
                }
                k++;
                i++;
                j--;

            }else if(A[i]>x2 && A[j]>=x1 && A[j]<=x2){  //S 2 and S 3 grow by 1 each. Exchange A[ i ] ↔A[ j ]. i ++ j −−
                swap(A,i,j);
                i++;
                j--;
            }
        }
        //At the end of the algorithm, exchange A[ p ] ↔ A[ k −1 ], and, A[ j +1 ] ↔ A[ r ].
        swap(A,p,k-1);
        swap(A,j+1,r);

        return  new int[]{x1,x2,k-1,j+1};
    }

    private static int[] randomSelect(int[] A,int p, int r){
        int r1 = rand.nextInt(r-p+1)+p;
        int r2 = 0;
        do{
            r2=rand.nextInt(r-p+1)+p;
        }while(r2==r1);
        if(A[r1]<=A[r2]){
            return new int[]{r1,r2};
        }else{
            return new int[]{r2,r1};
        }
    }

    private static void swap(int[] A ,int a, int b){
        int tmp = A[a];
        A[a]=A[b];
        A[b] = tmp;
    }

    public static void main(String[] args){
            int j = 9;
            int[] input = new int[j];
            for(int i=0;i<j;i++){
                input[i]=i;
            }
            Shuffle.shuffle(input);
            dualPivotQuickSort(input,0,input.length-1);
            for(int i:input){
                System.out.print(i+"  ");
            }
            System.out.println();

    }
}
