/**
 *  Group 36
 *
 * Team Member:
 * @author Wenjie Li
 * @author Meng-Ju Lu
 * @author Xin Tong
 */

package cs6301.g36;

import java.util.TreeSet;

public class LongestStreak {
    static int longestStreak(int[] A) {
        int res = 0;
        TreeSet<Integer> record = new TreeSet<>();

        for (int i : A) { record.add(i); }

        for (int e : A) {
            if (record.contains(e)) {
                int left = e - 1;
                int right = e + 1;
                while (record.contains(left)) {
                    record.remove(left--);
                }

                while (record.contains(right)) {
                    record.remove(right++);
                }
                res = Integer.max(res, right - left - 1);
            }
        }

        return res;
    }

    public static void main(String[] argc) {
        int[] arr = {1,7,9,4,1,7,4,8,7,1, 6, 10};
        System.out.println(longestStreak(arr));
    }
}
