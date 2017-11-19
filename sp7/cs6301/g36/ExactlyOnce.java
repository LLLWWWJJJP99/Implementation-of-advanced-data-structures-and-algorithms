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

public class ExactlyOnce<T> {
    static<T extends Comparable<? super T>> T[] exactlyOnce(T[] A) {
        TreeMap<T, Integer> count = new TreeMap<>();

        for (T a : A) {
            if (!count.containsKey(a)) {
                count.put(a, 0);
            }
            count.put(a, count.get(a) + 1);
        }

        ArrayList<T> tmp = new ArrayList<>();
        for (T a : A) {
            if (count.get(a) == 1) {
                tmp.add(a);
            }
        }
        T[] res = (T[]) new Comparable[tmp.size()];
        tmp.toArray(res);
        return res;
    }

    public static void main(String[] argc) {
        Integer[] arr = {6,3,4,5,3,5};
        Comparable[] res = exactlyOnce(arr);

        for (int i = 0; i < res.length; i++) {
            System.out.println(res[i]);
        }
    }
}
