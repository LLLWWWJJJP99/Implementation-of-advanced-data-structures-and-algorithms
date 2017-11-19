package cs6301.g36;

public class Enumeration {
    static int countPerm;

    public static void permute(int[] arr, int k, int VERBOSE) {
        countPerm = 0;
        permute(arr, k, k, VERBOSE);
        System.out.println(countPerm);
    }

    public static void permute(int[] arr, int c, int k, int VERBOSE) {
        if (c == 0) {
            if (VERBOSE != 0) {
                for (int i = 0; i < k; i++) {
                    System.out.printf("%s ", arr[i]);
                }
                System.out.println();
            }
            countPerm++;
        } else {
            int d = k - c;
            permute(arr, c - 1, k, VERBOSE);
            for (int i = d + 1; i < arr.length; i++) {
                int tmp = arr[d];
                arr[d] = arr[i];
                arr[i] = tmp;
                permute(arr, c - 1, k, VERBOSE);
                arr[i] = arr[d];
                arr[d] = tmp;
            }
        }
    }

    public static void combinations(int[] arr, int k) {
        int[] chosen = new int[k];
        combinations(arr, chosen, 0, k, k);
    }

    public static void combinations(int[] arr, int[] chosen, int i, int c, int k) {
        if (c == 0) {
            for (int j = 0; j < k; j++) {
                System.out.printf("%s ", chosen[j]);
            }
            System.out.println();
        } else {
            chosen[k - c] = arr[i];
            combinations(arr, chosen, i + 1, c - 1, k);
            if (arr.length - i > c) {
                combinations(arr, chosen, i + 1, c, k);
            }
        }
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4};
        combinations(arr, 2);
        permute(arr, 3, 0);
    }
}
