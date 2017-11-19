package cs6301.g36.sp1.Sort;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class MergeSort {
    // int array version
    public static void mergeSort(int[] nums, int[] tmp) {
        System.out.println("int array");
        mergeHelper(nums, tmp, 0, nums.length - 1);
    }
    private static void mergeHelper(int[] arr, int[] tmp, int begin, int end) {
        int mid = (begin + end) / 2;
        if (begin < end) {
            mergeHelper(arr, tmp, begin, mid);
            mergeHelper(arr, tmp,mid + 1, end);
        } else {
            return;
        }
        merge(arr, tmp, begin, end, mid);
    }

    private static void merge(int[] arr, int[] tmp, int begin, int end, int mid) {
        int idxTmp = 0, j = begin, k = mid + 1;
        while (j <= mid && k <= end) {
            tmp[idxTmp++] = Integer.min(arr[j], arr[k]);
            if (arr[j] < arr[k]) {
                j++;
            } else {
                k++;
            }
        }

        while (j <= mid) {
            tmp[idxTmp++] = arr[j++];
        }

        while (k <= end) {
            tmp[idxTmp++] = arr[k++];
        }

        for (int i = 0; i < idxTmp; i++) {
            arr[begin + i] = tmp[i];
        }
    }

    // generic array version
    public static<T extends Comparable<? super T>> void mergeSort(T[] arr, T[] tmp) {
        System.out.println("Generic");
        mergeSortHelper(arr, tmp, 0, arr.length - 1);
    }

    private static<T extends Comparable<? super T>> void mergeSortHelper(T[] arr, T[] tmp, int begin, int end) {
        int mid = begin + (end - begin) / 2;
        if (begin < end ) {
            mergeSortHelper(arr, tmp, begin, mid);
            mergeSortHelper(arr, tmp, mid + 1, end);
        } else {
            return;
        }
        merge(arr, tmp, begin, end, mid);
    }

    private static <T extends Comparable<? super T>> void merge(T[] arr, T[] tmp, int begin, int end, int mid) {
        int idxTmp = 0, j = begin, k = mid + 1;

        while (j <= mid && k <= end) {
            if (arr[j].compareTo(arr[k]) <= 0) {
                tmp[idxTmp++] = arr[j++];
            } else {
                tmp[idxTmp++] = arr[k++];
            }
        }

        while (j <= mid) {
            tmp[idxTmp++] = arr[j++];
        }

        while (k <= end) {
            tmp[idxTmp++] = arr[k++];
        }

        for (int i = 0; i < idxTmp; i++) {
            arr[i + begin] = tmp[i];
        }
    }

    // Insertion sort
    static<T extends Comparable<? super T>> void nSquareSort(T[] arr) {
        if (arr.length == 0) {
            return;
        }
        for (int i = 1; i < arr.length; i++) {
            T key = arr[i];

            int j = i - 1;
            while (j >= 0 && key.compareTo(arr[j]) < 0) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    // Main methos to test the code, using Integer Objects
    public static void main(String[] args) throws IOException {
        Integer[] a;
        Integer[] tmp;

        int[] a2;
        int[] tmp2;

        Integer[] a3;
        long start = 0, elapsedTime = 0;

        FileWriter fileWriter = new FileWriter("/Users/Jody/IdeaProjects/MergeSort/result.txt");
        PrintWriter printWriter = new PrintWriter(fileWriter);

        for (int k = 15; k <= 16; k++) {
            int size = 1000000 * k;
            a = new Integer[size];
            tmp = new Integer[size];
            a2 = new int[size];
            tmp2 = new int[size];
            a3 = new Integer[size];

            for (int i = 0; i < size; i++) {
                int val = 0;
                if (i > 9 * size/10) {
                    val = i % 1000000;
                }
                a[i] = val;
                a2[i] = val;
                a3[i] = val;
            }

            start = System.nanoTime();
            mergeSort(a, tmp);
            elapsedTime = System.nanoTime() - start;
            System.out.printf("(Generic) Elapsed time: %s ms\n", elapsedTime / 1000000);
            printWriter.printf("(Generic) Elapsed time: %s ms\n", elapsedTime / 1000000);

            start = System.nanoTime();
            mergeSort(a2, tmp2);
            elapsedTime = System.nanoTime() - start;
            System.out.printf("(int array) Elapsed time: %s ms\n", elapsedTime / 1000000);
            printWriter.printf("(int array) Elapsed time: %s ms\n", elapsedTime / 1000000);
            start = System.nanoTime();
            nSquareSort(a3);
            elapsedTime = System.nanoTime() - start;
            System.out.printf("(nSquare) Elapsed time: %s ms\n", elapsedTime / 1000000);
        }
        printWriter.close(); // flush out the result
    }
}
