// Replace gXX by your group name
package cs6301.g36;

//Driver program for skip list implementation.

import java.io.File;
import java.io.FileNotFoundException;

public class rbkSkipListDriver {
    static boolean VERBOSE = false;
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File(args[0]);
        java.util.Scanner in = new java.util.Scanner(file);
        if(args.length > 0) { VERBOSE = true; }
        String operation = "";
        long operand = 0;
        int modValue = 999983;
        long result = 0;
        Long returnValue = null;
        SkipList<Long> skipList = new SkipList<>();
        int lineno = 0;
        Timer timer = new Timer();

        timer.start();
        while (!((operation = in.next()).equals("End"))) {
            lineno++;
            switch (operation) {
                case "Add":
                    operand = in.nextLong();
                    returnValue = skipList.add(operand) ? 1L : 0L;
                    break;
                case "Ceiling":
                    operand = in.nextLong();
                    returnValue = skipList.ceiling(operand);
                    break;
                case "Contains":
                    operand = in.nextLong();
                    returnValue = skipList.contains(operand) ? 1L : 0L;
                    break;
                case "First":
                    returnValue = skipList.first();
                    break;
                case "Floor":
                    operand = in.nextLong();
                    returnValue = skipList.floor(operand);
                    break;
                case "Get":
                    int intOperand = in.nextInt();
                    returnValue = skipList.get(intOperand);
                    break;
                case "Last":
                    returnValue = skipList.last();
                    break;
                case "Remove":
                    operand = in.nextLong();
                    returnValue = skipList.remove(operand) != null ? 1L : 0L;
                    break;
                default:
                    System.out.println("Unknown operation: " + operation);
                    returnValue = null;
            }
            if (returnValue != null) {
                result = (result + returnValue) % modValue;
            }
            //if(VERBOSE) { System.out.println(lineno + " " + operation + " : " + (returnValue==null?0:returnValue)); }
        }
        System.out.println(result);
        System.out.println(timer.end());
    }
}