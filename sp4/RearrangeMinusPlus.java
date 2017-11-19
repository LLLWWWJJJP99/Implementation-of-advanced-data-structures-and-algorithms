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
 *  Reorder an int array A[] by moving negative elements to the front,
 * followed by its positive elements.  The relative order of positive numbers
 * must be the same as in the given array.  Similarly, the relative order of
 * its negative numbers should also be retained.  Write an algorithm that
 * runs in O(nlogn), and uses only O(1) extra space (for variables),
 * but can use O(log n) space for recursion.
 */
public class RearrangeMinusPlus {

    // Time: O(nlogn)
    // Space: O(logn) - recursion call
    static public void rearrangeMinusPlus(int[] arr) {
        rearrangeMinusPlus(arr, 0, arr.length - 1);
    }

    static private void rearrangeMinusPlus(int[] arr, int begin, int end) {
        int mid = begin + (end - begin) / 2;
        if (begin < end) {
            rearrangeMinusPlus(arr, begin, mid);
            rearrangeMinusPlus(arr, mid + 1, end);
        } else {
            return;
        }
        merge(arr, begin, end, mid);
    }

    // in-place merge
    static private void merge(int[] arr, int begin, int end, int mid) {
        int lp = begin, rp = mid + 1;

        while (lp <= mid && arr[lp] < 0) {
            lp++;
        }

        while (rp <= end && arr[rp] < 0) {
            rp++;
        }

        reverse(arr, lp, mid);
        reverse(arr, mid + 1, rp - 1);
        reverse(arr, lp, rp - 1);
    }

    static private void reverse(int[] arr, int p, int r) {
        while (p < r) {
            int tmp = arr[p];
            arr[p] = arr[r];
            arr[r] = tmp;
            p++;
            r--;
        }
    }

    static public void main(String [] argc) {
        int[] arr = {1, -1, 2, -2, 3, -3, 4, -4, 5, -5};
        rearrangeMinusPlus(arr);
        for (int i = 0; i < arr.length; i++) {
            System.out.printf("%s ", arr[i]);
        }
    }
}
