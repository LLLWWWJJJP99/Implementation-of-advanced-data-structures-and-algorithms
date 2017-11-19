/**
 *  Group 36
 *
 * Team Member:
 * @author Wenjie Li
 * @author Meng-Ju Lu
 * @author Xin Tong
 */
package cs6301.g36;

import java.util.ArrayList;
import java.util.TreeMap;

public class CountPairs<T> {
    public static int howMany(int[] A, int X) {
        TreeMap<Integer, ArrayList<Integer>> mp = new TreeMap<>();
        int res = 0;

        for (int i = 0; i < A.length; i++) {
            if (!mp.containsKey(A[i])) {
                mp.put(A[i], new ArrayList<>());
            }
            mp.get(A[i]).add(i);
        }

        for (int i = 0; i < A.length; i++) {
            int gap = X - A[i];
            if (mp.containsKey(gap)) {
                ArrayList<Integer> v = mp.get(gap);
                int p = binarySearch(v, i);
                res += v.size() - p;
            }
        }

        return res;
    }

    private static int binarySearch(ArrayList<Integer> v, int target) {
        int i = 0, j = v.size() - 1, mid;
        while (i <= j) {
            mid = (i + j) / 2;
            if (v.get(mid) <= target) {
                i = mid + 1;
            } else {
                j = mid - 1;
            }
        }
        return i;
    }

    public static void main(String[] argc) {
        int[] arr = {3, 5};
        System.out.println(howMany(arr, 8));
    }
}
