/**
 *  Group 36
 *
 * Team Member:
 * @author Wenjie Li
 * @author Meng-Ju Lu
 * @author Xin Tong
 */
package cs6301.g36.sp4;

import java.io.IOException;

public class MergeSortIntArray {
    public static int T = 5;

    /** (1) MergeSort in textbook */
    public static void mergeSort1(int[] arr) {
        mergeSort1(arr, 0, arr.length - 1);
    }

    private static void mergeSort1(int[] arr, int begin, int end) {
        int mid = begin + (end - begin) / 2;
        if (begin < end) {
            mergeSort1(arr, begin, mid);
            mergeSort1(arr, mid + 1, end);
        } else {
            return;
        }
        merge1(arr, begin, end, mid);
    }

    /** (2) MergeSort keeping only one tmp array */
    public static void mergeSort2(int[] arr, int[] tmp) {
        mergeSort2(arr, tmp, 0, arr.length - 1);
    }

    private static void mergeSort2(int[] arr, int[] tmp, int begin, int end) {
        int mid = begin + (end - begin) / 2;
        if (begin < end) {
            mergeSort2(arr, tmp, begin, mid);
            mergeSort2(arr, tmp, mid + 1, end);
        } else {
            return;
        }
        merge2(arr, tmp, begin, end, mid);
    }

    /** (3) MergeSort with threshold */
    public static void mergeSort3(int[] arr, int[] tmp) {
        mergeSort3(arr, tmp, 0, arr.length - 1);
    }

    private static void mergeSort3(int[] arr, int[] tmp, int begin, int end) {
        int mid = begin + (end - begin) / 2;
        if (begin < end) {
            mergeSort3(arr, tmp, begin, mid);
            mergeSort3(arr, tmp, mid + 1, end);
        } else {
            return;
        }
        merge3(arr, tmp, begin, end, mid);
    }

    /** (4) MergeSort (3) without copy */
    public static void mergeSort4(int[] arr, int[] tmp) {
        mergeSort4(arr, tmp, 0, arr.length - 1);
    }

    private static void mergeSort4(int[] arr, int[] tmp, int begin, int end) {
        int mid = begin + (end - begin) / 2;
        if (begin < end) {
            mergeSort4(tmp, arr, begin, mid);
            mergeSort4(tmp, arr, mid + 1, end);
        } else {
            return;
        }
        merge4(tmp, arr, begin, end, mid);
    }

    /** Merge method provided in textbook */
    private static void merge1(int[] arr, int begin, int end, int mid) {
        int[] L = new int[mid - begin + 2];
        int[] R = new int[end - mid + 1];

        System.arraycopy(arr, begin, L, 0, mid - begin + 1);
        System.arraycopy(arr, mid + 1, R, 0, end - mid);

        L[mid - begin + 1] = Integer.MAX_VALUE;
        R[end - mid] = Integer.MAX_VALUE;

        int i = 0, j = 0;

        for (int k = begin; k <= end; k++) {
            if (L[i] <= R[j]) {
                arr[k] = L[i++];
            } else {
                arr[k] = R[j++];
            }
        }
    }

    /** (2) keep only one tmp array */
    private static void merge2(int[] arr, int[] tmp, int begin, int end, int mid) {
        System.arraycopy(arr, begin, tmp, begin, end - begin + 1);

        int i = begin, j = mid + 1;

        for (int k = begin; k <= end; k++) {
            if (j > end || i <= mid && tmp[i] <= tmp[j]) {
                arr[k] = tmp[i++];
            } else {
                arr[k] = tmp[j++];
            }
        }
    }

    private static void merge3(int[] arr, int[] tmp, int begin, int end, int mid) {
        if (end - begin <= T) {
            insertionSort(arr, begin, end);
            return;
        }

        System.arraycopy(arr, begin, tmp, begin, end - begin + 1);

        int i = begin, j = mid + 1;

        for (int k = begin; k <= end; k++) {
            if (j > end || i <= mid && tmp[i] <= tmp[j]) {
                arr[k] = tmp[i++];
            } else {
                arr[k] = tmp[j++];
            }
        }
    }

    //
    private static void merge4(int[] arr, int[] tmp, int begin, int end, int mid) {
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

    static void printArray(int[] A) {
        for (int i = 0; i < A.length; i++) {
            System.out.printf("%s ", A[i]);
        }
        System.out.println();
    }

    // Main methos to test the code, using Integer Objects
    public static void main(String[] args) throws IOException {
        Timer timer = new Timer();
        /*
        int[] A = {8, 7, 6, 5, 4, 3, 2, 1};
        int[] tmp = new int[A.length];
        Shuffle.shuffle(A, 0, A.length - 1);
        mergeSort4(A, tmp);
        printArray(A);
        */

        for (int n = 8000000; n <= 50000000; n += 1000000) {
            int[] a;
            int[] tmp;

            a = new int[n];
            tmp = new int[n];

            for (int i = 0; i < n; i++) {
                a[i] = i;
            }

            Shuffle.shuffle(a, 0, a.length - 1);
            timer.start();
            mergeSort1(a);
            System.out.printf("v1: %s\n", timer.end());

            Shuffle.shuffle(a, 0, a.length - 1);
            timer.start();
            mergeSort2(a, tmp);
            System.out.printf("v2: %s\n", timer.end());

            Shuffle.shuffle(a, 0, a.length - 1);
            timer.start();
            mergeSort3(a, tmp);
            System.out.printf("v3: %s\n", timer.end());

            Shuffle.shuffle(a, 0, a.length - 1);
            System.arraycopy(a, 0, tmp, 0, a.length);
            timer.start();
            mergeSort4(a, tmp);
            System.out.printf("v4: %s\n", timer.end());
        }
    }
}
