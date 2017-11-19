/**
 *  Group 36
 *
 * Team Member:
 * @author Wenjie Li
 * @author Meng-Ju Lu
 * @author Xin Tong
 */

// Starter code for lp1.

// Change following line to your group number
// Changed type of base to long: 1:15 PM, 2017-09-08.
package cs6301.g36.lp1;

import java.math.BigInteger;
import java.util.*;

/**
 * Solution to Level 1 and level 2
 *
 * Num is the class that used to store big number exceed long
 * boolean sign to store the big number's sign
 * List<Long> to store the digits from LSD TO MSD
 * base indicates the current base of the Num class
 *
 */
public class Num  implements Comparable<Num>,Iterable<Long> {


    private static final int KARATSUBA_THRESHOLD = 30;
    static long defaultBase = 10;  // This can be changed to what you want it to be.
    long base = defaultBase;  // Change as needed
    private List<Long> holder=null;
    private boolean sign = true; // negative is false, positive is true
    final static Num ZERO = new Num(0,defaultBase);
    final static Num ONE = new Num(1,defaultBase);
    final static Num TWO = new Num(2,defaultBase);
    public Num(List<Long> list,boolean sign,long b){
        holder = list;
        this.base=b;
        this.sign=sign;
    }
    /* Start of Level 1 */
    Num(String s) throws Exception {
        initNum(defaultBase,s);
    }

    Num(String s, long base)  {
        initNum(base,s);
    }

    private void initNum(long base,String str)  {
        String s = handleSign(str);
        if((Math.log10(base)%1)==0){ //if base is not 10, needs transform
            this.base=base;
            holder= convertFromDecimalBase(s,base);
        }else{
            holder = convertFromDecimalBase(s,10);
            holder = changeBase(base);
            this.base=base;
        }
    }

    public Num numWithNewBase(long nBase){
        if(isZero()){
            return new Num(0,nBase);
        }
        if(this.holder.size()==1 && this.eval()<2){
            this.base=nBase;
            return this;
        }
        List<Long> tmp = changeBase(nBase);
        Num res = new Num(tmp,sign,nBase);
        return res;
    }

    private List<Long> changeBase(long newBase) {
        if(this.holder.size()==1 && this.eval()<2){
            return this.holder;
        }
        List<Long> list = new LinkedList<>();
        Num nBase = new Num(newBase,10);
        Num dividend = this.clone();
        while(dividend.compareTo(ZERO)>0){
            Num remainder = mod(dividend,nBase);
            dividend=divide(dividend,nBase);
            list.add(remainder.evaluate());
        }
        return list;
    }

    private String handleSign(String s) {
        int pos=s.indexOf('-');
        sign=true;
        if(pos>=0){
            sign=false;
            return s.substring(pos+1);
        }
        pos = s.indexOf('+');
        if(pos>=0){
            return s.substring(pos+1);
        }else{
            return s;
        }

    }

    private void initNum(long x, long b){
        if((Math.log10(base)%1)==0){
            this.base=b;
        }

        List<Long> result = new LinkedList<>();
        if(x<0){
            sign=false;
            x=-x;
        }else{
            sign = true;
        }
        while(x>0){
            result.add(x%base);
            x/=base;
        }
        holder=result;
    }

    int length(){
        return holder.size();
    }

    /**
     *   Return number to a string in base 10
     * @return
     */
    @Override
    public String toString(){
        StringBuilder stb = new StringBuilder();
        if(holder.isEmpty()){
            return "0";
        }
        boolean isFirstSegment=true;
        ListIterator<Long> it = holder.listIterator(holder.size());
        stb.insert(0,(sign?"":"-"));
        while(it.hasPrevious()){
            long tmp = it.previous();
            if(isFirstSegment){
                isFirstSegment=false;
            }else{
                paddingZeros(stb,tmp,base);
            }
            stb.append(tmp);
        }

        return stb.toString();
    }

    /**
     * return number of digits in this number
     * @param l
     * @return
     */
    private long getNumberOfDigits(long l){
        if(l==0)
            return 1;
        int count=0;
        while(l>0){
            l/=10;
            count++;
        }
        return count;
    }

    /**
     * padding zero when display number in decimal
     * @param stb
     * @param l
     * @param base
     */
    private void paddingZeros(StringBuilder stb,long l,long base){
        long n1 = getNumberOfDigits(l);
        long n2 = getNumberOfDigits(base);
        long len = n2-1-n1;
        for(int i=0;i<len;i++){
            stb.append(0);
        }
    }

    public Num getNegativeNum(){
        Num res = this.clone();
        res.revereSign();
        return res;
    }

    public Num leftShift(long steps){
        for(int i=0;i<steps;i++){
            holder.add(0,(long) 0);
        }
        return this;
    }

    private List<Long> convertFromDecimalBase(String str, long b) {
        int len = Long.numberOfTrailingZeros(b);
        List<Long> list = new LinkedList();
        int end = str.length();
        int start = Math.max(end-len,0);
        while(start>=0){
            list.add(Long.parseLong(str.substring(start,end)));
            end-=len;
            if(end<=0){
                break;
            }
            start=Math.max(0,start-len);
        }
        return list;
    }

    public boolean isZero() {
        return holder.isEmpty();
    }

    @Override
    protected Num clone(){
        List<Long> list =new LinkedList();
        for(Long l:holder){
            list.add(l);
        }
        return new Num(list,sign,base);
    }

    Num(long x ,long b){
        initNum(x,b);
    }

    Num() {
        holder = new LinkedList();
    }

    Num(long x) {
        initNum(x,10);
    }

    public Num revereSign(){
        sign=!sign;
        return this;
    }
    public Num returnNumAfterSettingSign(boolean vSign){
        this.sign=vSign;
        return this;
    }
    public boolean isSameSign(Num b){
        return this.sign==b.sign;
    }
    public boolean isPositive(){
        return sign;
    }
    public boolean sign(){
        return sign;
    }


    /**
     *  Addition operation on a and b
     * @param a
     * @param b
     * @return  sum of two numbers stored as Num.
     * @throws Exception a and b need to with same base
     */
    static Num add(Num a, Num b) {
        long rBase = a.base;
        if(a.base!=b.base){
            throw new ArithmeticException("a and b has different base in addition operation");
        }
        if(!a.isSameSign(b)){
           if(a.isPositive()){
               return subtract(a,b.getNegativeNum());
           }else{
               return subtract(b,a.getNegativeNum());
           }
        }
        boolean localSign =a.sign();
        List<Long> result = new LinkedList();
        Iterator<Long> ita = a.iterator();
        Iterator<Long> itb = b.iterator();
        long carry = 0;
        while(ita.hasNext() && itb.hasNext()){
            long sum = carry +ita.next()+itb.next();
            carry = sum/rBase;
            result.add(sum%rBase);
        }
        while(ita.hasNext()){
            long sum = carry + ita.next();
            carry = sum/rBase;
            result.add(sum%rBase);
        }
        while(itb.hasNext()){
            long sum = carry + itb.next();
            carry = sum/rBase;
            result.add(sum%rBase);
        }
        if(carry>0){
            result.add(carry);
        }
        return new Num(result,localSign,a.base);
    }

    /**
     * Subtract
     * given two Num a and b as parameters, representing the numbers n1 and n2 repectively, returns the Num corresponding to n1-n2.
     * @param a
     * @param b
     * @return difference of a and b
     */
    static Num subtract(Num a, Num b){
        long rBase = a.base;
        if(a.base!=b.base){
            throw new ArithmeticException("a and b has different base in addition operation");
        }
        if(!a.isSameSign(b)){
            if(a.sign){
                return add(a,b.getNegativeNum());
            }else{
                return add(b,a.getNegativeNum()).revereSign();
            }
        }
        if(!a.sign){
            Num tmp = subtract(a.getNegativeNum(),b.getNegativeNum());
            tmp.revereSign();
            return tmp;
        }
        if(a.compareTo(b)<0){
            Num tmp = subtract(b,a);
            tmp.sign=!tmp.sign;
            return tmp;
        }
        List<Long> result = new LinkedList<>();
        Iterator<Long> ita = a.iterator();
        Iterator<Long> itb = b.iterator();
        long borrowed = 0;
        while(ita.hasNext() && itb.hasNext()){
            long sum = borrowed +ita.next()-itb.next();
            borrowed = sum<0?-1:0;
            result.add((sum+rBase)%rBase);
        }
        while(ita.hasNext()){
            long sum = borrowed + ita.next();
            borrowed = sum<0?-1:0;
            result.add((sum+rBase)%rBase);
        }
        while(itb.hasNext()){
            long sum = borrowed + itb.next();
            borrowed = sum<0?-1:0;
            result.add((sum+rBase)%rBase);
        }
        removeLeadingZero(result);
        return new Num(result,true,rBase);
    }


    private static void removeLeadingZero(List<Long> list){
        ListIterator<Long> it = list.listIterator(list.size());
        while(it.hasPrevious()){
            long tmp = it.previous();
            if(tmp==0){
                it.remove();
            }else{
                return;
            }
        }
    }


    /**
     *
     * @param a Num
     * @param b Num
     * @return  Product Num
     */
    static Num product(Num a, Num b) {
        a=a.clone();  //newly added, not in submission
        b=b.clone();  //newly added, not in submission
        boolean localSign = !(a.sign ^ b.sign);
        a.sign=true;
        b.sign=true;
        if(a.isZero() || b.isZero()){
            return ZERO.numWithNewBase(a.base);
        };
        if(a.length()==1 && b.length()==1){
            return new Num(a.eval()*b.eval(),a.base).returnNumAfterSettingSign(localSign);
        }
        if(a.length()>=KARATSUBA_THRESHOLD && b.length()>KARATSUBA_THRESHOLD){
            return karatsubaBase(a,b);
        }
        long[] a1 = numToArray(a);
        long[] b1 = numToArray(b);
        long[] result = productWithArray(a1,b1,a.base);
        List<Long> list = new LinkedList<>();
        for(int i=result.length-1;i>=0;i--){
            list.add(result[i]);
        }
        return new Num(list,localSign,a.base);
    }

    /**
     * need future optimizing
     * @param a
     * @param b
     * @return
     */
    static long[] productWithArray(long[] a, long[] b,long base) {
        if(a.length==1){
            return productByLong(a[0],b,base);
        }else if(b.length==1){
            return productByLong(b[0],a,base);
        }
        long[] result = productBase(a,b,base);
        result = removeLeadingZeros(result);
        return result;

    }

    private static long[] productBase(long[] a, long[] b,long base) {
        long[] result = new long[a.length+b.length];
        long carry = 0;
        int aStart = a.length - 1;
        int bStart = b.length - 1;
        for (int j=bStart, k=bStart+1+aStart; j >= 0; j--, k--) {
            long product = b[j] * a[aStart] + carry;
            result[k] = product%base;
            carry = product / base;
        }
        result[aStart] = carry;
        for (int i = aStart-1; i >= 0; i--) {
            carry = 0;
            for (int j=bStart, k=bStart+1+i; j >= 0; j--, k--) {
                long product = b[j]*a[i]+ result[k]  + carry;
                result[k] = product%base;
                carry = product/base;
            }
            result[i] = carry;
        }
        return result;

    }

    private static long[] removeLeadingZeros(long val[]) {
        int vlen = val.length;
        int keep;

        // Find first nonzero byte
        for (keep = 0; keep < vlen && val[keep] == 0; keep++)
            ;
        return keep == 0 ? val : java.util.Arrays.copyOfRange(val, keep, vlen);
    }

    private static long[] productByLong(long l, long[] b1,long base) {
        long carry = 0;
        long[] result = new long[b1.length+1];
        int resultIdx = result.length-1;
        for (int i = b1.length - 1; i >= 0; i--) {
            long product = b1[i]*l + carry;
            result[resultIdx--]=product%base;
            carry = product/base;
        }
        if(carry>0){
            result[0]=carry;
            return result;
        }else{
            return Arrays.copyOfRange(result,1,result.length);
        }

    }


    private static long[] numToArray(Num a){
        long[] res = new long[a.length()];
        int idxa=res.length-1;
        for(long l:a.holder){
           res[idxa--]=l;
        }
        return res;
    }



    /**
     * @deprecated
     * Implement product of Num and a long
     * @param a Num
     * @param b long
     * @return Num
     */
    @Deprecated
    static Num product(Num a, long b) {
        if(a.isZero())
            return ZERO.numWithNewBase(a.base);
        if(b==1){
            return a;
        }else if(b==0){
            return ZERO.numWithNewBase(a.base);
        }
        long b1 = b/2;
        long b2= b-b1;
        Num result = add(product(a,b1),product(a,b2));
        return result;
    }

    /**
     * need future optimizing
     * @param a
     * @param b
     * @param base
     * @return
     */
    @Deprecated
    static long[] karatsuba(long[] a, long[] b,long base){
        if(a.length<b.length){
            return karatsuba(b,a,base);
        }
        //base condition
        if(a.length==1 && b.length==1){
            return new long[]{a[0]*b[0]};
        }
        // split the Num
        int len = Math.max(a.length,b.length);
        int pos=len/2+len%2;
        long[] high1 = new long[a.length-pos];
        long[] low1 = new long[pos];
        long[] high2 = new long[a.length-pos];
        long[] low2 = new long[pos];
        split(a,pos,high1,low1); // num1[0] is higher part, num1[1] is lower part
        split(b,pos,high2,low2); // num2[0] is higher part, num2[1] is lower part
        long[] z0 = karatsuba(low1, low2,base);
        long[] z1 = karatsuba(add(high1,low1,base),add(high2,low2,base),base);
        long[] z2 = karatsuba(high1,high2,base);
        long[] r2 = subtract(subtract(z1,z2),z0);
        r2=leftShift(r2,pos);
        z2=leftShift(z2,pos*2);
        long[] product = add(add(r2,z2,base),z0,base);
        return product;
    }

    private static void removeLeadingZero(long[] product) {

    }

    private static long[] extendArray(long[] a,int length){
        long[] tmp = new long[length];
        System.arraycopy(a,0,tmp,0,a.length);
        return tmp;
    }

    private static long[] leftShift(long[] r2, int pos) {
        long[] tmp = new long[r2.length+pos];
        System.arraycopy(r2,0,tmp,pos,r2.length);
        return tmp;
    }

    private static long[] subtract(long[] z1, long[] z2) {
        if(z1.length<z2.length){
            long[] tmp = subtract(z2,z1);
            for(int i=0;i<tmp.length;i++){
                tmp[i]=tmp[i]*-1;
            }
            return tmp;
        }
        long[] result = new long[z1.length];
        for(int i=0;i<Math.min(z1.length,z2.length);i++){
            result[i]=z1[i]-z2[i];
        }
        return result;
    }

    private static long[] add(long[] high1, long[] low1,long base) {
        if(high1.length<low1.length){
            return add(low1,high1,base);
        }
        long[] result = new long[high1.length+1];
        long carry=0;
        long borrow=0;
        for(int i=0;i<low1.length;i++){
            long sum = high1[i]+low1[i]+carry+borrow;
            borrow=0;
            if(sum<0){
                sum+=base;
                borrow=-1;
            }
            result[i]=sum%base;
            carry=borrow<0?0:sum/base;
        }
        for(int i=low1.length;i<high1.length;i++){
            long sum = carry+high1[i];
            result[i]=sum%base;
            carry=sum/base;
        }
        if(carry>0){
            result[result.length-1]=carry;
            return result;
        }else{
            return Arrays.copyOfRange(result,0,result.length-1);
        }

    }


    /**
     * Implement Karatsuba algorithm
     * @param a Num
     * @param b Num
     * @return
     * @update optimized by using array
     */
    @Deprecated
    static Num karatsuba(Num a, Num b){
        //base condition
        if(a.base!=b.base){
            throw new ArithmeticException("a and b has different base in addition operation");
        }
        if(a.isZero() || b.isZero()){
            return ZERO.numWithNewBase(a.base);
        }
        boolean localSign = !(a.sign ^ b.sign);
        a.sign=true;
        b.sign=true;
        if(a.length()==1 && b.length()==1){
            return new Num(a.eval()*b.eval(),a.base).returnNumAfterSettingSign(localSign);
        }
        // split the Num
        int len = Math.max(a.length(),b.length());
        int pos=len/2+len%2;

        Num[] num1 = split(a,pos);  // num1[0] is higher part, num1[1] is lower part
        Num[] num2 = split(b,pos);  // num2[0] is higher part, num2[1] is lower part

        Num z0 = karatsuba(num1[1], num2[1]);
        Num z1 = karatsuba(add(num1[0],num1[1]),add(num2[0],num2[1]));
        Num z2 = karatsuba(num1[0],num2[0]);
        Num r2 = subtract(subtract(z1,z2),z0);

        r2.leftShift(pos);
        z2.leftShift(pos*2);
        Num product = add(add(r2,z2),z0);

        removeLeadingZero(product.holder);
        return product.returnNumAfterSettingSign(localSign);
    }

    static Num karatsubaBase(Num a, Num b){
        int len = Math.max(a.length(),b.length());
        int pos=len/2+len%2;

        Num[] num1 = split(a,pos);  // num1[0] is higher part, num1[1] is lower part
        Num[] num2 = split(b,pos);  // num2[0] is higher part, num2[1] is lower part

        Num z0 = product(num1[1], num2[1]);
        Num z1 = product(add(num1[0],num1[1]),add(num2[0],num2[1]));
        Num z2 = product(num1[0],num2[0]);
        Num r2 = subtract(subtract(z1,z2),z0);

        r2.leftShift(pos);
        z2.leftShift(pos*2);
        Num product = add(add(r2,z2),z0);
        return product;
    }


    /**
     * split the Num a in to two parts [0,a.len-pos] and [a.len-pos+1,a.length-1]
     * @param a
     * @param pos
     * @return Array of Num with length two, the first is the right part and the second is the left part.
     */
    private static Num[] split(Num a, int pos){
        Iterator<Long> slow = a.holder.iterator();
        List<Long> left = new LinkedList<>();
        List<Long> right = new LinkedList<>();
        while(slow.hasNext()){
            if(0==pos--){
                break;
            }else{
                left.add(slow.next());
            }
        }
        while(slow.hasNext()){
            right.add(slow.next());
        }
        //needs to remove duplicated zero?
        removeLeadingZero(left);
        removeLeadingZero(right);
        return new Num[]{new Num(right,a.sign,a.base),new Num(left,a.sign,a.base)};

    }

    private static void split(long[] input, int pos,long[] high, long[] low){
        try{
            System.arraycopy(input,0,low,0,pos);
            System.arraycopy(input,pos,high,0,input.length-pos);
        }catch(ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }


    }

    /**
     * only used in base condition when the value will not over flow
     * only return correct number if value not overflow
     * will throw exception is overflow, but may not happen if use properly
     * @return
     */
    private long evaluate(){
        long tmp = 0;
        Iterator<Long> it = holder.iterator();
        int k=0;
        while(it.hasNext()){
            long n = (long)Math.pow(base,k);
            long tmp1=it.next()*n;
            tmp+= tmp1;
            if(tmp<tmp1){   //check overflow
                throw new ArithmeticException("Evaluation of "+this+" overflow"); //may not happen in runtime
            }
            k++;
        }
        return tmp*(sign?1:-1);
    }

    /**
     *
     * @return the val int pos 0 of the list, if the list is empty, reply 0
     */
    private long eval(){
        return holder.isEmpty()?0:holder.get(0);
    }

    /**
     * given an Num x, and n, returns the Num corresponding to x^n (x to the power n). Assume that n is a nonnegative number. Use divide-and-conquer to implement power using O(log n) calls to product and add.
     * @param a Num
     * @param n long
     * @return Num
     * @throws Exception base of a and n is not the same
     */
    static Num power(Num a, long n){
	    if(n==0){
	        return ONE.numWithNewBase(a.base);
        }else if(n==1){
	        return a;
        }else if(n==2){ //needs to implement 2 to avoid overstack
            return product(a,a);
        }

        Num tmp = power(a,n/2);
        Num tmp1 = product(tmp,tmp);
        if(n%2==0){
            return tmp1;
        }else{
            return product(tmp1,a);
        }
    }
    /* End of Level 1 */

    /* Start of Level 2 */

    /**
     * Integer Divide a by b result. Fractional part is discarded (take just the quotient). If b is 0, raise an exception.
     * @param a Dividend
     * @param b Divisor
     * @return quotient,  fractional part is discarded
     * @throws Exception
     * @update fix the bug of not handling the sign correctly
     */
    static Num divide(Num a, Num b)  {
        boolean localSign = !(a.sign() ^ b.sign());
        if(a.length()==1 && b.length()==1){
            return new Num(a.eval()/b.eval(),a.base).returnNumAfterSettingSign(localSign);
        }
        if(b.length()==1 && b.eval()==2){
            return divideByTwo(a).returnNumAfterSettingSign(localSign);
        }
        if(b.isZero()){
            throw new ArithmeticException("divide by zero");
        }
        a=a.clone();
        a.sign=true;
        b=b.clone();
        b.sign=true;
        if(a.compareTo(b)<0){
            return ZERO.numWithNewBase(a.base);
        }
        Num start = ZERO.numWithNewBase(a.base);
        Num end = a;
        end.sign=true;
        if(a.length()==1 && b.length()==1){
            return new Num(a.eval()/b.eval(),a.base).returnNumAfterSettingSign(localSign);
        }
        while(start.compareTo(end)<=0){
            Num mid = divideByTwo(add(start,end));
            Num tmp = product(mid,b);
            //System.out.println("mid: "+mid+" start: "+start+" end: "+end);
            if(tmp.compareTo(a)==0){
                return mid.returnNumAfterSettingSign(localSign);
            }else if(tmp.compareTo(a)<0){
                start=add(mid,ONE.numWithNewBase(a.base));
            }else{
                end=subtract(mid,ONE.numWithNewBase(a.base));
            }
        }
        //when start == end, no need to minus one
        return subtract(start,ONE.numWithNewBase(a.base)).returnNumAfterSettingSign(localSign);
    }


    /**
     * divide the Num by TWO
     * used as helper function in Divide {@link #divide(Num, Num)}
     * @param a
     * @return quotient of Num divided by Two
     */
    private static Num divideByTwo(Num a) {
       Num res = new Num();
       res.base=a.base;
       res.sign=a.sign;
       ListIterator<Long> it = a.holder.listIterator(a.holder.size());
       long carry = 0;
       while(it.hasPrevious()){
           long tmp = it.previous();
           long sum = tmp+carry*res.base;
           carry = sum%2;
           res.holder.add(0,sum/2);
       }
       removeLeadingZero(res.holder);
       return res;
    }


    /**
     * modulation calculation
     * @param a
     * @param b
     * @return
     * @update fix bug that not handle truncating of a/b correctly when a.sign!=b.sign
     */
    static Num mod(Num a, Num b)  {
        Num res = divide(a,b);
        if(!res.isPositive()){
            res=subtract(res,ONE.numWithNewBase(a.base));
        }
        return subtract(a,product(b,res));
    }

    // Use divide and conquer

    /**
     * power calculation
     * @param a
     * @param n
     * @return
     * @update fix the bug of not handling sign of power
     */
    static Num power(Num a, Num n) {
        boolean localSign = a.sign || n.isEven();
        a=a.clone();
        a.sign=true;
        if(n.length()==0){
            return ONE.numWithNewBase(a.base);
        }else if(n.length()==1){
            return power(a,n.evaluate()).returnNumAfterSettingSign(localSign);
        }
        Num n1 = divideByTwo(n);
        Num tmp = power(a,n1);
        Num tmp1 = product(tmp,tmp);
        if(n.isEven()){
            return tmp;
        }else{
            return product(tmp1,a);
        }
    }

    /**
     * internal method for {@link #power(Num, Num)}
     * determin the sign of power
     * @return
     */
    private boolean isEven() {
        if(isZero()){
            return true;
        }
        long lastDigit = holder.get(0);
        return lastDigit%2==0;
    }

    /**
     * return the square root of a (truncated). Use binary search. Assume that a is non-negative.
     * @param a
     * @return
     */
    static Num squareRoot(Num a) {
        if(a.compareTo(ZERO.numWithNewBase(a.base))<0){
            throw new ArithmeticException("Num "+a+" is negative number");
        }
        Num high = divide(add(a,ONE.numWithNewBase(a.base)),TWO.numWithNewBase(a.base));
	    Num low = ONE.numWithNewBase(a.base);
	    Num mid=low;
	    while(low.compareTo(high)<0){
            mid = divideByTwo(add(high,low));
            if(power(mid,TWO.numWithNewBase(a.base)).compareTo(a)==0){
                return mid;
            }else if(power(mid,TWO.numWithNewBase(a.base)).compareTo(a)<0){
                low=add(mid,ONE.numWithNewBase(a.base));
            }else{
                high=subtract(mid,ONE.numWithNewBase(a.base));
            }
        }
	    return subtract(low,ONE.numWithNewBase(a.base));
    }
    /* End of Level 2 */


    // Utility functions
    // compare "this" to "other": return +1 if this is greater, 0 if equal, -1 otherwise
    public int compareTo(Num other) {
        if(this.isZero() && other.isZero()){
            return 0;
        }
        if(!isSameSign(other)){
            return this.sign?1:-1;
        }
        Iterator<Long> it1 = this.iterator();
        Iterator<Long> it2 = other.iterator();
        int res=0;
        while(it1.hasNext() && it2.hasNext()){
            long tmp1=it1.next();
            long tmp2=it2.next();
            if(tmp1>tmp2){
                res=1;
            }else if(tmp1<tmp2){
                res=-1;
            }
        }
        if(it1.hasNext()){
            res=1;
        }else if(it2.hasNext()){
            res=-1;
        }
        return res*(sign()?1:-1);
    }
    
    // Output using the format "base: elements of list ..."
    // For example, if base=100, and the number stored corresponds to 10965,
    // then the output is "100: 65 9 1"

    /**
     * Print the base + ":" + elements of the list, separated by spaces.
     * @update fix bug when num is zero, print extra empty string "base: "
     */
    void printList() {
        StringBuilder stb = new StringBuilder();
        if(holder.isEmpty()){
            printNumberInBaseFormat("0");
            return;
        }
        Iterator<Long> it = holder.iterator();
        stb.insert(0,(sign?"":"-"));
        while(it.hasNext()){
            long tmp = it.next();
            stb.append(tmp);
            stb.append(" ");
        }
        if(stb.length()>1)
            stb.setLength(stb.length()-1);
        printNumberInBaseFormat(stb.toString());
    }

    private void printNumberInBaseFormat(String str){
        System.out.println(base+" :"+str);
    }

    public long base() { return base; }

    public static void main(String[] args) {
        int base=100000;
        String s1 = "-839475923847";
        String s2 = "2435343";
        Num exp = new Num(541,base);
        Num n1 = new Num(s1,base);
        Num n2 = new Num(s2,base);
        BigInteger b1 = new BigInteger(s1);
        BigInteger b2 = new BigInteger(s2);
        long start = 0,end =0;


        //verify addition
        start = System.currentTimeMillis();
        Num sum = add(n1,n2);
        end = System.currentTimeMillis();
        BigInteger vSum = b1.add(b2);
        verifyOutput("addition",sum,vSum,base,end-start);

        //verify subtraction
        start = System.currentTimeMillis();
        Num difference = subtract(n1,n2);
        end = System.currentTimeMillis();
        BigInteger vDifference = b1.subtract(b2);
        verifyOutput("subtraction",difference,vDifference,base,end-start);


        //verify multiplication
        start = System.currentTimeMillis();
        Num product = product(n1,n2);
        end = System.currentTimeMillis();
        BigInteger vProduct = b1.multiply(b2);
        verifyOutput("product",product,vProduct,base,end-start);

        //verify power
        start = System.currentTimeMillis();
        Num power = power(n1,exp);
        end = System.currentTimeMillis();
        BigInteger vPower = b1.pow((int)exp.evaluate());
        verifyOutput("power",power,vPower,base,end-start);

        //verify divide
        start = System.currentTimeMillis();
        Num quotient = divide(n1,n2);
        end = System.currentTimeMillis();
        BigInteger vQuotient = b1.divide(b2);
        verifyOutput("Division",quotient,vQuotient,base,end-start);

        //verify Mod
        start = System.currentTimeMillis();
        Num remainder = mod(n1,n2);
        end = System.currentTimeMillis();
        BigInteger vRemainder = b1.mod(b2);
        verifyOutput("Mod",remainder,vRemainder,base,end-start);

        //Verify SquareRoot
        start = System.currentTimeMillis();
        Num squareroot = squareRoot(new Num(8888,base));
        BigInteger b3  = new BigInteger("94");
        end = System.currentTimeMillis();
        verifyOutput("squareroot",squareroot,b3,base,end-start);




    }

    private static void verifyOutput(String testcase,Num n,BigInteger b,int base,long elaspedTime){
        System.out.println("******************   "+testcase+"   **********");
        System.out.print("Verifying Num result:");
        n.printList();
        System.out.println("Supposed BigInteger result:"+b.toString(base));

        System.out.println("Elapsed Time in ms:"+elaspedTime);
        System.out.println("passed : "+n.toString().trim().equals(b.toString(base).trim()));
        System.out.print("printlist : ");
        n.printList();
        System.out.println();
    }

    @Override
    public Iterator<Long> iterator() {
        return holder.iterator();
    }
}
