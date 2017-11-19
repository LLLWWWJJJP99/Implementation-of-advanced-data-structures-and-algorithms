package cs6301.g36.lp1;

import java.util.Scanner;

/**
 * update@20170914 XinTong: add IF and THEN in token as condition to express ternary operator
 *
 */
public class Tokenizer {
    public enum Token { VAR, NUM, OP, EQ , OPEN, CLOSE, EOL,IF,THEN }

    public static Token tokenize(String s) throws Exception {
        if(s.matches("\\d+")) {  // one or more digits
            return Token.NUM;
        } else if(s.matches("[a-z]")) {  // letter
            int index = s.charAt(0) - 'a';  // Convert var to index: a-z maps to 0-25
            return Token.VAR;
        } else if(s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/") || s.equals("%") || s.equals("^") || s.equals("|")) {
            return Token.OP;
        } else if(s.equals("=")) {
            return Token.EQ;
        } else if(s.equals("(")) {
            return Token.OPEN;
        } else if(s.equals(")")) {
            return Token.CLOSE;
        } else if(s.equals(";")) {
            return Token.EOL;
        } else if(s.equals("?")) {
            return Token.IF;
        } else if(s.equals(":")) {
            return Token.THEN;
        } else {  // Error
            throw new Exception("Unknown token: " + s);
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        while(in.hasNext()) {
            String s = in.next();
            Token t = tokenize(s);
            System.out.print(t + " ");
            if(t == Token.EOL) { System.out.println(); }
        }
    }
}