/**
 * Group 36
 *
 * Team Member:
 * @author Wenjie Li
 * @author Meng-Ju Lu
 * @author Xin Tong
 */
package cs6301.g36.lp1;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class ShuntingYardAlgorithm {
    /**
     *  Implement the Shunting Yard algorithm:
     * https://en.wikipedia.org/wiki/Shunting-yard_algorithm
     * for parsing arithmetic expressions using the following precedence rules
     * (highest to the lowest).
     * Parenthesized expressions (...)
     * Unary operator: factorial (!)
     * Exponentiation (^), right associative.
     * Product (*), division (/).  These operators are left associative.
     * Sum (+), and difference (-).  These operators are left associative.
     * @param str
     * @return
     */
    public static String[]  parsingArithmeticExpressions(String str){
        char[] map = new char[256];  //precedenceMap
        map['+']=0;
        map['-']=0;
        map['*']=1;
        map['/']=1;
        map['^']=2;
        map['!']=3;
        Stack<String> operatorStack = new Stack();
        Stack<String> outputStack = new Stack();
        List<String> input = splitInputString(str);
        for(String s:input){
            if(Character.isLetterOrDigit(s.charAt(0))){
                outputStack.push(s);
            }else if(s.equals("(")){
                operatorStack.push("(");
            }else if(s.equals(")")){
                while(!operatorStack.peek().equals("(")){
                    outputStack.push(operatorStack.pop());
                }
                operatorStack.pop();
            }else if(isUnaryOps(s)) {
                outputStack.push(s);
            }else if(s.equals(" ")) {
                continue;
            }else{
                while(!operatorStack.isEmpty() && !operatorStack.peek().equals("(")){
                    char opa = operatorStack.peek().charAt(0);
                    char opb = s.charAt(0);
                    if(comparePrecedence(opa,opb,map)>0 || comparePrecedence(opa,opb,map)==0 && isLeftAssociate(opb) ){ //if op in stack has high precedence or equal and left associate, then
                        outputStack.push(operatorStack.pop());
                    }else{
                        break;
                    }
                }
                operatorStack.push(s);
            }
        }
        while(!operatorStack.isEmpty()){
            outputStack.push(operatorStack.pop());
        }
        String[] result = new String[outputStack.size()];
        int idx=result.length-1;
        while(!outputStack.isEmpty()){
            result[idx--]=outputStack.pop();
        }
        return result;
    }

    private static int comparePrecedence(char opa, char opb,char[] map){
        return map[opa]-map[opb];
    }

    private static boolean isUnaryOps(String s){
        return s.charAt(0)=='!';
    }

    private static boolean isLeftAssociate(char op){
        if(op=='^')
            return false;
        return true;
    }

    private static List<String> splitInputString(String str){
        List<String> result = new LinkedList<>();
        int prev=0;
        for(int i=0;i<str.length();i++){
            char c = str.charAt(i);
            if(!Character.isLetterOrDigit(c)){
                if(prev<i)
                    result.add(str.substring(prev,i));
                result.add(str.substring(i,i+1));
                prev=i+1;
            }
        }
        //if the last character in the string is digit
        if(prev!=str.length()){
            result.add(str.substring(prev,str.length()));
        }
        return result;
    }

    public static void testParseArithmeticExp(){
        String input1 = "E!-(A/(B-C))*C/D-T!+N^M^L*O";
        System.out.println("Input infix notation is :"+input1);
        String[] output1 = parsingArithmeticExpressions(input1);
        System.out.println("Output postfix notation is :"+output1);


        String input2 = "A * B";
        System.out.println("Input infix notation is :"+input2);
        String[] output2 = parsingArithmeticExpressions(input2);
        System.out.println("Output postfix notation is :"+output2);
    }

    public static void main(String[] args){

        testParseArithmeticExp();
    }
}


