/**
 *  Group 36
 *
 * Team Member:
 * @author Wenjie Li
 * @author Meng-Ju Lu
 * @author Xin Tong
 */
package cs6301.g36.lp1;

import java.io.File;
import java.util.*;

import static cs6301.g36.lp1.LineType.*;

/**
 * type of different lines may be found in input file
 */
enum LineType{
    INFIX,POSTFIX,IF,IFTHEN,END,PRINTVAR,ERROR;
}

/**
 * Solution to level 4
 * Complete levels 1-3 and the following additional functionality. Let the base have a default value, but allow it to be changed by passing a value in the command line. Your programs should continue to work, for any choice of base, between 2 and 10,000. For example, if your program is run as "java LP1L4 32", then all numbers should be stored in base 32. Note that input/output is always in base 10.
 */
public class LP1L4 {

    private static List<Line> lineList =new LinkedList<>();
    private static Map<Integer,Line> map=new HashMap();
    private static Num[] varsTable= new Num[26];


    /**
     * Entry to the main execution loop
     * @param scanner
     * @throws Exception
     */
    public static void execute(Scanner scanner,long base) throws Exception {
        parseToLineList(scanner);
        if(lineList.isEmpty()){
            return;
        }
        Stack<Line> executionHistory = new Stack();
        ListIterator<Line> it = lineList.listIterator();
        //main execution logic loop
        while(it.hasNext()){
            Line curr=it.next();
            if(curr.lineType==LineType.END){
                exitExecution(executionHistory);
            }
            if(curr.lineType== IF || curr.lineType== IFTHEN){
                it=curr.gotoNext(it);
            }else{
                executionHistory.push(curr);
                curr.executePostfixExpression(base);
            }
        }

    }

    /**
     * Execute when finish the main loop
     * @param executionHistory
     */
    private static void exitExecution(Stack<Line> executionHistory) {
        //print last var
        varsTable[executionHistory.pop().var].printList();
        lineList =new LinkedList<>();
        map=new HashMap();
        varsTable= new Num[26];;
    }

    /**
     * parse each lines into instance of Line and store in a linked list
     * Parse all infix format string into posfix format
     * Parse the condition line
     * similar to to compiler
     *
     * @param scanner
     * @throws Exception
     */
    private static void parseToLineList(Scanner scanner) throws Exception {
        while(scanner.hasNext()){
            String tmp = validCheck(scanner.nextLine());
            if(tmp==null){
                return;
            }
            Line newLine = new Line(tmp);
            newLine.listIndex=lineList.size(); // to fetch listiterator later
            if(newLine.lineNumber>0){
                map.put(newLine.lineNumber,newLine);
            }
            lineList.add(newLine);
        }
    }

    private static String validCheck(String s) {
        if(s.contains(";")){
            return s;
        }else{
            return null;
        }
    }

    /**
     * nested class of line represents each Line of statement
     */
    private static class Line{
        String[] postfixFormatStringArray;
        LineType lineType;
        int lineNumber;
        int var;
        int listIndex;
        int nextIfLines=-1; // if var!=0
        int nextThenLines=-1 ; // if var==0
        public Line(String nextLine) throws Exception {
            parseLine(nextLine);
        }

        /**
         * simple line pattern check just ok for this exaple
         * @param nextLine
         * @return
         */
        private LineType checkLineType(String nextLine) {
            if(nextLine == null) {
                return ERROR;
            }else if(!nextLine.contains(";")){
                return ERROR;
            }else if(nextLine.contains("=")){
                if(Character.isDigit(nextLine.charAt(0))){
                    return LineType.INFIX;
                }else{
                    return LineType.POSTFIX;
                }
            }else if(nextLine.contains(":")){
                return IFTHEN;
            }else if(nextLine.contains("?")){
                return IF;
            }else if(nextLine.trim().equals(";")){
                return LineType.END;
            }else{
                return PRINTVAR;
            }
        }

        /**
         *  check each line's type, then handle them with different method
         * @param nextLine
         * @throws Exception
         */
        private void parseLine(String nextLine) throws Exception {
            LineType lineType = checkLineType(nextLine);
            if(lineType==INFIX){
                parseInfix(nextLine);
            }else if(lineType==POSTFIX){
                parsePostfix(nextLine);
            }else if(lineType==IF){
                parseIf(nextLine);
            }else if(lineType==IFTHEN){
                parseIfThen(nextLine);
            }else if(lineType==PRINTVAR){
                parsePrintVar(nextLine);
            }else if(lineType==ERROR){
                return;
            }else if(lineType==END){
                this.lineType=END;
                return;
            }

        }

        private void parsePrintVar(String nextLine) {
            char tmp = nextLine.trim().charAt(0);
            var=tmp-'a';
            lineType= PRINTVAR;
        }

        private void parseIf(String nextLine) throws Exception {
            String[] inputs = nextLine.trim().split("\\?");
            String[] beforeEquation = inputs[0].trim().split("\\s++");
            lineNumber=Integer.valueOf(beforeEquation[0]);
            var=beforeEquation[1].trim().charAt(0)-'a';
            String[] expression = inputs[1].trim().split("\\s++");
            nextIfLines=Integer.parseInt(expression[0]);
            lineType= IF;
        }


        private void parseInfix(String nextLine) throws Exception {
            String[] inputs = nextLine.trim().split("=");
            String[] beforeEquation = inputs[0].trim().split("\\s++");
            lineNumber=Integer.valueOf(beforeEquation[0]);
            var=beforeEquation[1].trim().charAt(0)-'a';
            String[] tmp = inputs[1].trim().split(";");
            postfixFormatStringArray = ShuntingYardAlgorithm.parsingArithmeticExpressions(tmp[0].trim());
            lineType=LineType.POSTFIX;
        }

        private void parsePostfix(String nextLine) throws Exception {
            String[] inputs = nextLine.trim().split("=");
            var=inputs[0].trim().charAt(0)-'a';
            String[] tmp = inputs[1].trim().split(";");
            postfixFormatStringArray=tmp[0].split("\\s++");
            lineType=LineType.POSTFIX;
        }

        private void parseIfThen(String nextLine) throws Exception {
            String[] inputs = nextLine.trim().split("\\?");
            String[] beforeEquation = inputs[0].trim().split("\\s++");
            lineNumber=Integer.valueOf(beforeEquation[0]);
            var=beforeEquation[1].trim().charAt(0)-'a';
            String[] expression = inputs[1].trim().split(":");
            String[] tmp = expression[1].trim().split("\\s++");
            nextIfLines=Integer.parseInt(expression[0].trim());
            nextThenLines=Integer.parseInt(tmp[0].trim());
            lineType= IFTHEN;
        }


        public ListIterator<Line> gotoNext(ListIterator<Line> it) {
            //find next line number;
            int lineNumber = decideNextLine();
            if(lineNumber<0)
                return it;
            Line nextLine = map.get(lineNumber);
            return lineList.listIterator(nextLine.listIndex);

        }

        private int decideNextLine() {
            Num value = varsTable[var];
            if(value.isZero()){
               return  nextThenLines;
            }else{
                return nextIfLines;
            }
        }

        /**
         * update the varTable according current execution environment
         * @throws Exception
         */
        private  void executePostfixExpression(long base) throws Exception {
            if(lineType==POSTFIX){
                Num tmp = LP1L3.parsePostfix(postfixFormatStringArray,varsTable,base);
                if(varsTable[var]==null){
                    System.out.println(tmp);
                }
                varsTable[var]=tmp;
            }else if(lineType==PRINTVAR){
                System.out.println(varsTable[var]);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner in;
        int base=1000000000;
        if (args.length > 0) {
            base = Integer.parseInt(args[0]);
            // Use above base for all numbers (except I/O, which is in base 10)
        }
        in = new Scanner(System.in);
        LP1L4 x = new LP1L4();
        System.out.println("Please paste the test file of lp1l4 :");
        while(in.hasNext()) {
            String word = in.next();
            File file = new File(word);
            Scanner scanner = new Scanner(file);
            Timer timer = new Timer();
            execute(scanner,base);
            timer.end();
            System.out.println(timer);
        }

    }


}
