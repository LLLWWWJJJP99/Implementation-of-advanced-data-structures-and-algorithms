package cs6301.g36;

import java.util.Arrays;

/**
 *  Group 36
 * 
 * Team Member:
 * @author Wenjie Li
 * @author Meng-Ju Lu
 * @author Xin Tong
 */


/**
 * Implement Knuth's L algorithm.
 *
 */
public class SP8_8 {

	public static void main(String[] args) {
		int[] arr = new int []{1, 2, 3, 4};
		knuthAlgo(arr);
	}
	
	/**
	 * entrance function for knuthAlgo
	 * @param A array that is used to generate permutation
	 */
	public static void knuthAlgo(int[] A) {
		int count = 1;
		Arrays.sort(A);
		showArray(A);
		while(!isDescending(A)) {
			int j = findJ(A);
			int k = findK(A, j);
			swap(A, k, j);
			reverse(A, j + 1, A.length - 1);
			showArray(A);
			count++;
		}
		System.out.println("Number of Permutations:"+count);
	}
	
	public static void reverse(int[] A, int i, int j) {
		while(i < j) {
			swap(A, i++, j--);
		}
	}
	
	public static void swap(int[] A,int i, int j) {
		int temp = A[i];
		A[i] = A[j];
		A[j] = temp;
	}
	
	
	/**
	 * @param A array that is used to generate permutation
	 * @param j max index such that A[j] < A[j+1]
	 * @return max index k such that A[j] < A[k]
	 */
	public static int findK(int[] A, int j) {
		for(int i = A.length - 1; i >= 0; i--) {
			if(A[i] > A[j]) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * @param A array that is used to generate permutation
	 * @return j max index such that A[j] < A[j+1]
	 */
	public static int findJ(int[] A) {
		for(int i = A.length - 2; i >= 0; i--) {
			if(A[i] < A[i+1]) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * @param arr array that is used to generate permutation
	 * @return whether the arr is in descending order
	 */
	public static boolean isDescending(int[] arr) {
		for(int i = 0; i < arr.length - 1; i++){
			if(arr[i] < arr[i+1]) {
				return false;
			}
		}
		return true;
	}
	
	public static void showArray(int[] A) {
		showArray(A, 0, A.length - 1);
	}
	
	public static void showArray(int[] arr, int start, int end) {
		int count = 0;
		for(int i = start; i <= end; i++) {
			if(count % 20 == 0) { System.out.println(); }
			System.out.print(arr[i]+" ");
			count++;
		}
	}
}
