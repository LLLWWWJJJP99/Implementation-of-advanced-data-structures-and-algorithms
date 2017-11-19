package cs6301.g36.lp1;

import java.io.File;
import java.math.BigInteger;
import java.util.Scanner;

public class Verification {


    public static void main(String[] args) throws Exception {
        String p = File.separator;
        String currentDir = System.getProperty("user.dir")+p+"src"+p+"cs6301"+p+"g36"+p+"lp1";
        String filePath2 =currentDir+p+"test1.txt";

        Scanner scanner = new Scanner(new File(filePath2));
        String l1 = scanner.nextLine();
        String l2 = scanner.nextLine();
        BigInteger b1 = new BigInteger(l1);
        BigInteger b2 = new BigInteger(l2);
        System.out.println("l1 ,l2 length: "+l1.length()+" , "+l2.length());
        Num n1 = new Num(l1,1000000000);
        Num n2 = new Num(l2,1000000000);

        Timer t1 = new Timer();
        b1=b1.multiply(b2);
        t1.end();
        System.out.println(b1);
        System.out.println(t1);

        Timer t2 = new Timer();
        n1= Num.product(n1,n2);
        t2.end();
        System.out.println(n1);
        System.out.println(t2);
        //scanner = new Scanner(new File(filePath3));
        //illustratesMethods(scanner,base);


    }
}
