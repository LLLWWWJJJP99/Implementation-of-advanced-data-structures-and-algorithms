/**
 *  Group 36
 *
 * Team Member:
 * @author Wenjie Li
 * @author Meng-Ju Lu
 * @author Xin Tong
 */
package cs6301.g36.sp6;



import java.util.*;

/**
 *   Perfect powers: Write an algorithm to output exact powers (numbers of the
 *   form a^b, b>1), in the range 2 to n.
 *   Given an array of prime numbers, output numbers in [2,n] all of whose
 *   prime factors are only from the given set of prime numbers.
 *   For example, given {3,7}, the program outputs {3,7,9,21,27,49,63,...}.
 *   Make sure that your program outputs each number only once, in sorted order.
 */
public class PerfectPowers {

    private static class Power {
        int n;
        int power=2;
        int value;
        private Power(int num){
            this.n=num;
            this.value= (int) Math.pow(n,power);
        }
        private Power(int num, int pow){
            this.n=num;
            this.power=pow;
            this.value= (int) Math.pow(n,power);
        }
        private void next(){
            power++;
            this.value= (int) Math.pow(n,power);
        }
    }

    public static void main(String[] args){
        Timer t1 = new Timer();
        List<Integer> res = perfectPowers(10240);
        t1.end();
        System.out.println(t1);
        for(int i:res){
            System.out.print(i+", ");
        }
    }

    public static List<Integer> perfectPowers(int n){
        List<Integer> result = new LinkedList<>();
        PriorityQueue<Power> heap = new PriorityQueue<Power>(1, new Comparator<Power>() {
            @Override
            public int compare(Power o1, Power o2) {
                return o1.value-o2.value;
            }
        });
        heap.add(new Power(2));
        while(!heap.isEmpty()){
            System.out.println("heap size: "+heap.size());
            Power num1 = heap.poll();
            if(num1.value<=n){
                if(result.isEmpty() || result.get(result.size()-1)!=num1.value){
                    result.add(num1.value);
                }
            }else{
                return result;
            }
            if(num1.n==2){
                heap.add(new Power(2,num1.power+1));
            }
            heap.add(new Power(num1.n+1,num1.power));

        }
        return result;
    }



    /**
     * generate an array of prime from 2 to n
     * @param n
     * @return
     */
    public static int[] primeNumber(int n){
        boolean[] prime = new boolean[n+1];
        Arrays.fill(prime,true);
        for(int i=2;i<=Math.sqrt(n);i++){  //Math.sqrt() is optimized way
            if(prime[i]){
                for(int j=i*i;j<=n;j+=i){  //starts from i*i is optimized way
                    prime[j]=false;
                }
            }
        }
        int count=0;
        for(int i=2;i<prime.length;i++){
            if(prime[i])count++;
        }
        int[] result = new int[count];
        count=0;
        for(int i=2;i<prime.length;i++){
            if(prime[i]){
                result[count++]=i;
            }
        }
        return result;
    }




}
