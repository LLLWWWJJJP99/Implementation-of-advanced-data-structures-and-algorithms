package cs6301.g36.sp2;

import java.util.*;

public class ShuntingYard {
    public static String shuntingYard(String s) {
        Stack<String> output = new Stack<String>();
        Stack<String> operator = new Stack<String>();
        Map<Character, Integer> precedence = new HashMap<Character, Integer>();
        Set<Character> leftAssociative = new HashSet<Character>();
        Set<Character> parantheses = new HashSet<Character>();

        precedence.put('+', 2);
        precedence.put('-', 2);
        precedence.put('*', 3);
        precedence.put('/', 3);
        precedence.put('^', 4);
        precedence.put('!', 5);

        leftAssociative.add('+');
        leftAssociative.add('-');
        leftAssociative.add('*');
        leftAssociative.add('/');

        parantheses.add('(');
        parantheses.add(')');
        parantheses.add(' ');

        int idx = 0;

        while (idx < s.length()) {
            if (!precedence.containsKey(s.charAt(idx)) && !parantheses.contains(s.charAt(idx))) {
                String opera = "";
                while (idx < s.length() && !precedence.containsKey(s.charAt(idx)) && !parantheses.contains(s.charAt(idx))) {
                    opera += s.charAt(idx);
                    idx++;
                }
                output.push(opera.toString());
            } else if (precedence.containsKey(s.charAt(idx))) {
                if (operator.isEmpty() || operator.peek().charAt(0) == '(' ||
                        precedence.get(s.charAt(idx)) > precedence.get(operator.peek().charAt(0))) {
                    operator.push(s.charAt(idx) + "");
                } else {
                    while (!operator.isEmpty() && precedence.get(operator.peek().charAt(0)) > precedence.get(s.charAt(idx))) {
                        output.push(operator.pop());
                    }
                    while (!operator.isEmpty() && precedence.get(operator.peek().charAt(0)) == precedence.get(s.charAt(idx))) {
                        if (leftAssociative.contains(operator.peek().charAt(0))) {
                            output.push(operator.pop());
                        } else {
                            break;
                        }
                    }
                    operator.push(s.charAt(idx) + "");
                }
                idx++;
            } else if (s.charAt(idx) == '(') {
                operator.push(s.charAt(idx) + "");
                idx++;
            } else if (s.charAt(idx) == ')') {
                while (!operator.isEmpty() && operator.peek().charAt(0) != '(') {
                    output.push(operator.pop());
                }
                operator.pop();
                idx++;
            } else {
                idx++;
            }
        }

        while (!operator.isEmpty()) {
            output.push(operator.pop());
        }

        String res = "";

        while (!output.isEmpty()) {
            res = output.pop() + res;
        }

        return res;
    }

    public static void main(String[] args) {
        String s = "a+b-c";
        System.out.println(shuntingYard(s));
    }
}
