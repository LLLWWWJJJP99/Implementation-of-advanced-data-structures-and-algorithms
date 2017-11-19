/**
 * Group 36
 *
 * Team Member:
 * @author Wenjie Li
 * @author Meng-Ju Lu
 * @author Xin Tong
 */

package cs6301.g36.sp2;


import java.util.*;


public class SP2Q9 {
    public static void main(String[] args) throws Exception {
        String input = "3 0 -2 2";
        String input1 = "7  1 2 3";
        SparsePolynomial sparsePolynomial = new SparsePolynomial(input);
        SparsePolynomial sp = new SparsePolynomial(input1);
        SparsePolynomial.Term term = new SparsePolynomial.Term(33,33);
        System.out.println("Before insert term:"+sparsePolynomial);
        System.out.println("After insert term:"+sparsePolynomial);
        System.out.println("Before operation:"+sp+" and "+sparsePolynomial);
        SparsePolynomial result = SparsePolynomial.add(sp,sparsePolynomial);
        System.out.println("After add term:"+result);
        System.out.println("After multiplication term:"+SparsePolynomial.multiple(sp,sparsePolynomial));
        System.out.println("evaluate x=1.0001 --> "+sparsePolynomial.evaluate(2));

    }
}

class SparsePolynomial implements Iterable<SparsePolynomial.Term>{
    private Term dummyHead=new Term(-1,-1);
    private Term end=null;

    private int length;

    public SparsePolynomial(String str) throws Exception {
        parseSparsePolynomial(str);
    }
    public SparsePolynomial() {
    }
    public SparsePolynomial(Term term) {
        dummyHead.next=term;
        end=dummyHead.next;
        length++;
    }

    @Override
    public String toString(){
        StringBuilder stb = new StringBuilder();
        Iterator<Term> it = iterator();
        while(it.hasNext()){
            Term tmp = it.next();
            if(tmp.coefficient<0){
                stb.append(tmp);
            }else if(tmp.coefficient>0){
                if(stb.length()==0)
                    stb.append(tmp);
                else
                    stb.append("+"+tmp);
            }
        }
        return stb.toString();
    }

    @Override
    public Iterator<Term> iterator() {
        return new TermIterator(dummyHead);
    }



    private static class TermIterator implements Iterator<Term>{
        Term cursor;
        public TermIterator(Term dummyHead){
            this.cursor=dummyHead;
        }

        @Override
        public boolean hasNext() {
            return cursor.next!=null;
        }

        @Override
        public Term next() {
            cursor=cursor.next;
            return cursor;
        }
    }

    /**
     * nested class to represent term in sparse polynomials
     */
    public static class Term implements Cloneable{
        private int coefficient;
        private int power;
        private Term next;
        public Term(int coefficient, int power){
            this.coefficient=coefficient;
            this.power=power;
        }
        public void setNext(Term t){
            //defensive copy of Term
            this.next=new Term(t.coefficient,t.power);
        }

        @Override
        public Term clone(){
            //not cloen the the next pointer
            return new Term(coefficient,power);
        }

        @Override
        public String toString(){
            if(power==0){
                return coefficient+"";
            }else if(power==1){
                return coefficient+"x";
            }
            return coefficient+"x^"+power;
        }
    }


    /**
     * append it if the term is larger or equal than last term of polynomial
     * @param term
     */
    private void append(Term term){
        if(end==null){
            dummyHead.next=term.clone();
            end=dummyHead.next;
            length++;
        }else if(term.power==end.power){
            end.coefficient+=term.coefficient;
            return;
        }else if(term.power>end.power){
            end.next=term.clone();
            end=end.next;
            length++;
        }
    }

    /**
    public void insert(Term term){
        if(term==null)
            return;
        //
        Iterator<Term> it = iterator();
        //defensive copy
        term = term.clone();
        Term prev = dummyHead;
        //if result is null
        if(!it.hasNext()){
            dummyHead.next=term;
            end=term;
            return;
        }
        while(it.hasNext()){
            Term curr = it.next();
            if(curr.power==term.power){  //update
                curr.coefficient+=term.coefficient;
                return;
            }else if(curr.power>term.power){  //insert new term ahead
                Term tmp = prev.next;
                prev.next=term;
                term.next=tmp;
                return;
            }
            prev=prev.next;
        }


    }
    */

    /**
     * Parse incoming string format of sparse polynomial
     * @param str "3 0 -2 12 1 105" to present polynomial x^105-2x^12+3
     *
     */
    private void parseSparsePolynomial(String str) throws Exception {
        int len=0;
        String[] input = str.trim().split("\\s+");
        if(input.length%2!=0){
            throw new Exception("input format not right");
        }
        Integer c=null;
        Term curr=dummyHead;
        for(String s:input){
            if(c==null){
                c=Integer.valueOf(s);
            }else{
                curr.next=new Term(c,Integer.valueOf(s));
                len++;
                curr=curr.next;
                c=null;
            }
        }
        end=curr;
        length=len;
    }


    /**
     * Implement addition arithmetic with sparse polynomials addition
     * @param a sparse polynomial
     * @param b sparse polynomial
     * @return
     */
    public static SparsePolynomial add(SparsePolynomial a,SparsePolynomial b){
        Iterator<Term> ita = a.iterator();
        Iterator<Term> itb = b.iterator();
        SparsePolynomial result = new SparsePolynomial();
        Term termA=null;
        Term termB=null;
        if(ita.hasNext())
            termA=ita.next();
        if(itb.hasNext())
            termB=itb.next();
        while(termA!=null || termB!=null){
            if(termB==null){
                result.append(termA);
                termA=(ita.hasNext()?ita.next():null);
            }else if(termA==null){
                result.append(termB);
                termB=(itb.hasNext()?itb.next():null);
            }else if(termA.power==termB.power){   //if power is the same, add the coefficient
                result.append(new Term(termA.coefficient+termB.coefficient,termA.power));
                termA=(ita.hasNext()?ita.next():null);
                termB=(itb.hasNext()?itb.next():null);
            }else if(termA.power<termB.power){  // if termA has smaller power
                result.append(termA);
                termA=(ita.hasNext()?ita.next():null);
            }else{  //if termB has smaller power
                result.append(termB);
                termB=(itb.hasNext()?itb.next():null);
            }
        }
        return result;
    }

    /**
     * After split, the left and right sparse polynomial will hold the left and right part of existing polynomial
     * @param left new empty SparsePolynomial to hold the left part of existing sparse polynomial
     * @param right new empty SparsePolynomial to hold the right part of existing sparse polynomial
     */
    private void split(SparsePolynomial left, SparsePolynomial right){
        Iterator<Term> fast = iterator();
        Iterator<Term> slow = iterator();
        while(fast.hasNext()){
            fast.next();
            if(fast.hasNext()){
                left.append(slow.next());
                fast.next();
            }
        }
        while(slow.hasNext()){
            right.append(slow.next());
        }
    }


    /**
     * Implement multiplication without using HashMaps.
     * use divide and conquer to solve with O(N)=mnlogn time complexity
     * @param a sparse polynomial
     * @param b sparse polynomial
     * @return
     */
    public static SparsePolynomial multiple(SparsePolynomial a,SparsePolynomial b){
        //base condition
        if(a.length==0 || b.length==0){
            return new SparsePolynomial();
        }
        if(a.length==1 && b.length==1){
            return new SparsePolynomial(new Term(a.end.coefficient*b.end.coefficient,a.end.power+b.end.power));
        }
        //divide
        SparsePolynomial aLeft = new SparsePolynomial();
        SparsePolynomial aRight= new SparsePolynomial();
        a.split(aLeft,aRight);
        SparsePolynomial bLeft = new SparsePolynomial();
        SparsePolynomial bRight= new SparsePolynomial();
        b.split(bLeft,bRight);
        //merge
        SparsePolynomial tmp1 = add(multiple(aLeft,bRight),multiple(aLeft,bLeft));
        SparsePolynomial tmp2 = add(multiple(aRight,bLeft),multiple(aRight,bRight));
        SparsePolynomial result = add(tmp1,tmp2);
        return result;
    }


    /**
     * evaluate the sparse polynomial value by replace x with val
     * @param val
     * @return
     */
    public  double evaluate(double val){
        double result = 0;
        Iterator<Term> it = iterator();
        while(it.hasNext()){
            Term tmp = it.next();
            result+=tmp.coefficient*Math.pow(val,tmp.power);
        }
        return result;
    }




}
