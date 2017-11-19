/**
 *  Group 36
 *
 * Team Member:
 * @author Wenjie Li
 * @author Meng-Ju Lu
 * @author Xin Tong
 */

package cs6301.g36.sp4;

import java.math.BigInteger;

/**
 * Write functions to compute the nth Fibonacci number and compare
 their running times.  Since the numbers grow fast, use BigInteger
 class to represent the numbers.

 // Do a simple linear scan algorithm: Fib[n] = Fin[n-1] + Fin[n-2];
 // Since numbers are stored with BigInteger class, use add for "+"
 static BigInteger linearFibonacci(int n) { ... }

 // Implement O(log n) algorithm described in class (Sep 15)
 static BigInteger logFibonacci(int n) { ... }
 */
public class FibonacciNumber {

    final static BigInteger BIG_ZERO = BigInteger.ZERO;
    final static BigInteger BIG_ONE = BigInteger.ONE;
    final static int THRESHOLD = 100000;

    /**
     * a simple linear scan algorithm: Fib[n] = Fin[n-1] + Fin[n-2];
     * @param n integer n
     * @return Fibonacci number of Integer n
     */
    static BigInteger linearFibonacci(int n) {
        if(n<0) throw new ArithmeticException("n should not be negative");
        BigInteger f0=new BigInteger("0");
        if(n==0)    return f0;
        BigInteger f1=new BigInteger("1");
        if(n==1)    return f1;
        for(int i=1;i<n;i++){
            f0=f0.add(f1);
            BigInteger tmp = f0;
            f0=f1;
            f1=tmp;
        }
        return f1;
    }

    /**
     * Implement O(log n) algorithm described in class (Sep 15)
     * @param n
     * @return
     */
    static BigInteger logFibonacci(int n) {

        BigInteger[][] base = new BigInteger[][]{{BIG_ONE, BIG_ONE},{BIG_ONE, BIG_ZERO}};
        BigInteger[][] c = new BigInteger[][]{{BIG_ONE},{BIG_ZERO}};
        BigInteger[][] res = multiplicate(power(base,n-1),c);
        return res[0][0];
    }

    static BigInteger combinedFibonacci(int n){
        if(n< THRESHOLD)
          return linearFibonacci(n);
        else
            return logFibonacci(n);
    }

    static private BigInteger[][] power(BigInteger[][] a,int n){
        if(n==1){
            return a;
        }else{
            return multiplicate(power(a,n/2),power(a,n-n/2));
        }
    }

    static private BigInteger[][] multiplicate(BigInteger[][] a, BigInteger[][] b){
        if(a==null || b==null || a.length==0 || b.length==0){
            throw new ArithmeticException("Matrix Multiplication error input");
        }
        int row=a.length,col=b[0].length;
        BigInteger[][] res = new BigInteger[row][col];
        for(int i=0;i<row;i++){
            for(int j=0;j<col;j++){
                res[i][j]=new BigInteger("0");
            }
        }
        for(int i=0;i<row;i++){
            for(int j=0;j<b.length;j++){
                for(int k=0;k<col;k++){
                    res[i][k]=res[i][k].add(a[i][j].multiply(b[j][k]));
                }
            }
        }
        return res;
    }

    public static void main(String[] args){

        for(int len=1000;len<10000000;len=len*10) {
            Timer t1 = new Timer();
            BigInteger res = linearFibonacci(len);
            t1.end();
            Timer t2 = new Timer();
            BigInteger res1 = logFibonacci(len);
            t2.end();
            System.out.println("t1 of n="+len+" "+ t1);
            System.out.println("fibonacci of " + len + " is " + res);
            System.out.println("t2 of n="+len+" "+t2);
            System.out.println("fibonacci of " + len + " is " + res1);
        }
    }
}
