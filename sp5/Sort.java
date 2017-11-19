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

public class Sort {
    public static int T = 5;
    public static Random rand = new Random();

    public static void mergeSort(int[] arr, int[] tmp) {
        System.arraycopy(arr, 0, tmp, 0, arr.length);
        mergeSort(arr, tmp, 0, arr.length - 1);
    }

    private static void mergeSort(int[] arr, int[] tmp, int begin, int end) {
        int mid = begin + (end - begin) / 2;
        if (begin < end) {
            mergeSort(tmp, arr, begin, mid);
            mergeSort(tmp, arr, mid + 1, end);
        } else {
            return;
        }
        merge(tmp, arr, begin, end, mid);
    }

    private static void merge(int[] arr, int[] tmp, int begin, int end, int mid) {
        if (end - begin <= T) {
            insertionSort(tmp, begin, end);
            return;
        }

        int i = begin, j = mid + 1;

        for (int k = begin; k <= end; k++) {
            if (j > end || (i <= mid && arr[i] <= arr[j])) {
                tmp[k] = arr[i++];
            } else {
                tmp[k] = arr[j++];
            }
        }
    }

    static void insertionSort(int[] arr, int p, int r) {
        for (int i = p + 1; i <= r; i++) {
            int key = arr[i];

            int j = i - 1;
            while (j >= p && key < arr[j]) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    static void quickSort(int[] arr) {
        quickSort(arr, 0, arr.length - 1);
    }

    static void quickSort(int[] arr, int p, int r) {
        if (p < r) {
            int q = partition(arr, p, r);
            quickSort(arr, p, q - 1);
            quickSort(arr, q + 1, r);
        }
    }

    static int partition(int[] arr, int p, int r) {
        int i = p, j = r + 1;
        int idx = p + rand.nextInt(r - p);
        exch(arr, p, idx);

        while (true) {
            while (arr[++i] < arr[p]) {
                if (i == r) { break; }
            }

            while (arr[--j] > arr[p]) {
                if (j == p) { break; }
            }

            if (i >= j) {
                break;
            }
            exch(arr, i, j);
        }
        exch(arr, p, j);
        return j;
    }

    static void exch(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
}
