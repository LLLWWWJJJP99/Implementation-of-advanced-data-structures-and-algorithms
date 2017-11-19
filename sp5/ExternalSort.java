/**
 *  Group 36
 *
 * Team Member:
 * @author Wenjie Li
 * @author Meng-Ju Lu
 * @author Xin Tong
 */

package cs6301.g36.sp5;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;
import java.io.File;

public class ExternalSort {
    static class Cell {
        int val;
        Scanner in;
        Cell(int v, Scanner i) {
            val = v;
            in = i;
        }
    };

    static int chunkSize = 100000;
    static int numChunk = 1000;

    /**
     * Generate input.txt for external sort
     * */
    private static void generateInputFile() throws IOException {
        int size = chunkSize * numChunk;
        String input = "input.txt";
        FileWriter fileWriter = new FileWriter(input);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            printWriter.println(random.nextInt(size));
        }

        printWriter.close();
    }

    /**
     * MergeSort each chunk and write them into temp files
     * */
    private static void createInitialRuns() throws IOException {
        File inputFile = new File("input.txt");
        Scanner in = new Scanner(inputFile);
        int[] arr = new int[chunkSize];
        int[] tmp = new int[chunkSize];

        PrintWriter[] outputs = new PrintWriter[numChunk];

        for (int i = 0; i < numChunk; i++) {
            FileWriter fileWriter = new FileWriter(String.valueOf(i) + ".txt");
            outputs[i] = new PrintWriter(fileWriter);
        }

        int index = 0;
        while (in.hasNext()) {
            for (int i = 0; i < chunkSize; i++) {
                arr[i] = in.nextInt();
            }

            Sort.mergeSort(arr, tmp);
            //Sort.quickSort(arr);

            for (int i = 0; i < arr.length; i++) {
                outputs[index].println(arr[i]);
            }
            index++;

        }

        for (int i = 0; i < numChunk; i++) {
            outputs[i].close();
        }


    }

    /**
     * Merge temp files
     * */
    private static void mergeFiles() throws IOException {
        FileWriter fileWriter = new FileWriter("output.txt");
        PrintWriter printWriter = new PrintWriter(fileWriter);

        Scanner[] inputs = new Scanner[numChunk];


        for (int i = 0; i < numChunk; i++) {
            File inputFile = new File(String.valueOf(i) + ".txt");
            inputs[i] = new Scanner(inputFile);
        }

        PriorityQueue<Cell> pq = new PriorityQueue<>(numChunk, new Comparator<Cell>() {
            @Override
            public int compare(Cell o1, Cell o2) {
                if (o1.val < o2.val) {
                    return -1;
                } else if (o1.val == o2.val) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });

        for (int i = 0; i < numChunk; i++) {
            pq.add(new Cell(inputs[i].nextInt(), inputs[i]));
        }

        while (!pq.isEmpty()) {
            Cell curr = pq.poll();
            printWriter.println(curr.val);

            if (curr.in.hasNext()) {
                pq.add(new Cell(curr.in.nextInt(), curr.in));
            }
        }

        printWriter.close();

        for (int i = 0; i < numChunk; i++) {
            File file = new File(String.valueOf(i) + ".txt");
            file.delete();
        }
    }


    public static void main(String[] argc) throws IOException {
        //generateInputFile();

        Timer timer = new Timer();

        timer.start();
        createInitialRuns();
        mergeFiles();
        System.out.println(timer.end());
    }
}
