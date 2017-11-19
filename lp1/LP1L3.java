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
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;


/**
 * Solution to level 3  is {@link #illustratesMethods(Scanner, long)}
 *
 * Write a driver program that illustrates the methods, based on the following input/output specification. The operands of the expressions are names of variables (single lower case letter, a-z) and integer constants (0 or [1-9][0-9]*). Note that there is no unary minus operator. The operators are {+, -, *, /, %, ^, |} representing add, subtract, multiply, divide, mod, power, and square root, respectively. Tokens will be separated by spaces, and each expression will be ended by a semicolon. Each input line will have the form:
 * var ;
 * var = postfix expression ;
 ;
 */
public class LP1L3 {

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        LP1L3 x = new LP1L3();
        long base=10;
        System.out.println("Please paste the test file of lp1l3 :");
        while(in.hasNext()) {
            String word = in.next();
            File file = new File(word);
            Scanner scanner = new Scanner(file);
            Timer timer = new Timer();
            illustratesMethods(scanner,base);
            timer.end();
            System.out.println(timer);
        }


    }

    /**
     * Entry to the illustrate method
     * @param scanner scanner contains lines to instruments
     * @param b base to be used
     * @throws Exception
     */
    public static void illustratesMethods(Scanner scanner, long b) throws Exception {
        long base = b;
        Num[] vars = new Num[26];
        Stack<Integer> varHistory= new Stack();
        while(scanner.hasNext()){
            String input = scanner.nextLine();
            String expression = extractExpress(input);
            if(expression.contains("=")){
                handlePostfixAssignment(expression,vars,varHistory,base);
            }else if(expression.isEmpty()){
                if(!varHistory.isEmpty()){
                    handleVarOutput(vars,varHistory.peek(),true);
                }
                break;
            }else if(expression.length()==1 && Character.isLetter(expression.charAt(0))){
                handleVarOutput(vars,expression.charAt(0),false);
            }else{
                throw new Exception("Method format error: unknown method format!");
            }

        }


    }

    /**
     * parse the postfix expression and update varTable accordingly
     * @param postfixExpression
     * @param varTable
     * @param base
     * @return
     * @throws Exception
     */
    public static Num parsePostfix(String postfixExpression[], Num[] varTable, long base) throws Exception {
        Stack<Num> numbers = new Stack();
        for(String s:postfixExpression){
            Tokenizer.Token token = Tokenizer.tokenize(s);
            if(token== Tokenizer.Token.VAR){
                numbers.push(varTable[s.trim().charAt(0)-'a']);
            }else if(token== Tokenizer.Token.NUM){
                numbers.push(new Num(s,base));
            }else if(token== Tokenizer.Token.OP) {
                char c = s.trim().charAt(0);
                //+, -, *, /, %, ^, |
                if(c=='+'){
                    numbers.push(Num.add(numbers.pop(),numbers.pop()));
                }else if(c=='-'){
                    Num tmp = numbers.pop();
                    numbers.push(Num.subtract(numbers.pop(),tmp));
                }else if(c=='*'){
                    numbers.push(Num.product(numbers.pop(),numbers.pop()));
                }else if(c=='/'){
                    Num tmp = numbers.pop();
                    numbers.push(Num.divide(numbers.pop(),tmp));
                }else if(c=='%'){
                    Num tmp = numbers.pop();
                    numbers.push(Num.mod(numbers.pop(),tmp));
                }else if(c=='^'){
                    Num tmp = numbers.pop();
                    numbers.push(Num.power(numbers.pop(),tmp));
                }else if(c=='|'){
                    numbers.push(Num.squareRoot(numbers.pop()));
                }
            }
        }
        return numbers.pop();
    }

    /**
     * hanlde the assignment line type
     * @param expression
     * @param vars
     * @param varHistory
     * @param base
     * @throws Exception
     */
    private static void handlePostfixAssignment(String expression, Num[] vars, Stack<Integer> varHistory, long base) throws Exception {
        String[] inputs = expression.split("=");
        int var=-1;
        if(inputs.length!=2){
            return;
        }
        String tmpStr = inputs[0].trim();
        if(tmpStr.length()==1 && Character.isLetter(tmpStr.charAt(0))){
            var = tmpStr.charAt(0);
        }
        if(var-'a'<0 || var-'a'>25){
            return;
        }

        String[] postFixExpressions = inputs[1].trim().split("\\s+");
        vars[var-'a']=parsePostfix(postFixExpressions,vars,base);
        printOutput(vars[var-'a']+"");
        varHistory.push(var-'a');
    }

    private static void printOutput(String str){
        System.out.println(str);
    }

    /**
     * handle the line only with a var to print
     * @param vars
     * @param var
     * @param containsBase
     */
    private static void handleVarOutput(Num[] vars, int var,boolean containsBase){
        if(var<0 || var>25){
            return;
        }
        if(containsBase)
            vars[var].printList();
        else
            printOutput(vars[var]+"");
    }


    private static String extractExpress(String input) throws Exception {
        int pos = input.indexOf(';');
        if(pos==-1){
            throw new Exception("Method format error: no ending semicolon");
        }else{
            return input.substring(0,pos).trim();
        }
    }


}
