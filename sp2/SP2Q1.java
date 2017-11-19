/**
 * Group 36
 *
 * Team Member:
 * @author Wenjie Li
 * @author Meng-Ju Lu
 * @author Xin Tong
 */
package cs6301.g36.sp2;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * SOLUTION TO Q1
 *
 */
public class SP2Q1 {
    /**
     *
     * Return elements common to l1 and l2, in sorted order.
     * outList is an empty list created by the calling
     * program and passed as a parameter.
     * Function should be efficient whether the List is
     * implemented using ArrayList or LinkedList.
     * Do not use HashSet/Map or TreeSet/Map or other complex
     * data structures.
     * @param l1 sorted List which may have duplicated items
     * @param l2 sorted List which may have duplicated items
     * @param outList the intersection of l1 and l2 without duplicated items
     * @param <T>
     */
    public static<T extends Comparable<? super T>> void intersect(List<T> l1, List<T> l2, List<T> outList) {
        Iterator<T> it1 = l1.iterator();
        Iterator<T> it2 = l2.iterator();
        T tmp1 = it1.hasNext()?it1.next():null;
        T tmp2 = it2.hasNext()?it2.next():null;
        //use two pointer to find the same items and added to outList
        while(tmp1!=null && tmp2!=null){
            if(tmp1.compareTo(tmp2)==0){
                if(!outList.isEmpty() && tmp1.compareTo(outList.get(outList.size()-1))==0){ //avoid duplicated item added to outList
                    continue;
                }
                outList.add(tmp1);
                tmp1=it1.hasNext()?it1.next():null;
                tmp2=it2.hasNext()?it2.next():null;
            }else if(tmp1.compareTo(tmp2)<0){
                tmp1=it1.hasNext()?it1.next():null; //iterate the smaller item
            }else{
                tmp2=it2.hasNext()?it2.next():null; //iterate the smaller item
            }
        }
    }

    public static void testIntersect(){
        List<Integer>  l1 = new LinkedList<>();
        List<Integer>  l2 = new LinkedList<>();
        l1.add(0);
        l1.add(1);
        l1.add(1);
        l2.add(2);
        l2.add(2);
        l1.add(2);
        l2.add(3);
        l2.add(3);
        List<Integer> result = new LinkedList<>();
        intersect(l1,l2,result);
        System.out.println("l1 has:"+ Arrays.toString(l1.toArray()));
        System.out.println("l2 has:"+ Arrays.toString(l2.toArray()));
        System.out.println("intersect result has:"+ Arrays.toString(result.toArray()));
    }

    public static void testUnion(){
        List<Integer>  l1 = new LinkedList<>();
        List<Integer>  l2 = new LinkedList<>();
        l1.add(1);
        l1.add(1);
        l2.add(2);
        l2.add(2);
        l1.add(2);
        l2.add(3);
        l2.add(3);
        List<Integer> result = new LinkedList<>();
        union(l1,l2,result);
        System.out.println("l1 has:"+ Arrays.toString(l1.toArray()));
        System.out.println("l2 has:"+ Arrays.toString(l2.toArray()));
        System.out.println("union result has:"+ Arrays.toString(result.toArray()));
    }

    public static void testDifference(){
        List<Integer>  l1 = new LinkedList<>();
        List<Integer>  l2 = new LinkedList<>();
        l1.add(1);
        l1.add(1);
        l2.add(2);
        l2.add(2);
        l1.add(2);
        l2.add(3);
        l2.add(3);
        List<Integer> result = new LinkedList<>();
        difference(l1,l2,result);
        System.out.println("l1 has:"+ Arrays.toString(l1.toArray()));
        System.out.println("l2 has:"+ Arrays.toString(l2.toArray()));
        System.out.println("difference result has:"+ Arrays.toString(result.toArray()));
    }


    /**
     *
     * Return the union of l1 and l2, in sorted order.
     * Output is a set, so it should have no duplicates.
     * @param l1 sorted List
     * @param l2 sorted List
     * @param outList the union set of l1 and l2 without duplicated items
     * @param <T>
     */
    public static<T extends Comparable<? super T>> void union(List<T> l1, List<T> l2, List<T> outList) {
        Iterator<T> it1 = l1.iterator();
        Iterator<T> it2 = l2.iterator();
        T tmp1 = it1.hasNext()?it1.next():null;
        T tmp2 = it2.hasNext()?it2.next():null;
        //use two pointer to union all items into outList
        while(tmp1!=null && tmp2!=null){
            if(tmp1.compareTo(tmp2)==0){
                if(outList.isEmpty() || tmp1.compareTo(outList.get(outList.size()-1))!=0){ //skip if the item has been put into outList
                    outList.add(tmp1);
                }
                tmp1 = it1.hasNext()?it1.next():null;
                tmp2 = it2.hasNext()?it2.next():null;
            }else if(tmp1.compareTo(tmp2)<0 ){
                if(outList.isEmpty() || !outList.isEmpty()&&tmp1.compareTo(outList.get(outList.size()-1))!=0){
                    outList.add(tmp1);
                }
                tmp1 = it1.hasNext()?it1.next():null;
            }else if(tmp2.compareTo(tmp2)>0){
                if(outList.isEmpty() || !outList.isEmpty()&&tmp2.compareTo(outList.get(outList.size()-1))!=0){
                    outList.add(tmp2);
                }
                tmp2 = it2.hasNext()?it2.next():null;
            }
        }
        while(tmp1!=null){
            if(outList.isEmpty() || tmp1.compareTo(outList.get(outList.size()-1))!=0){ //skip if the item has been put into outList
                outList.add(tmp1);
            }
            tmp1 = it1.hasNext()?it1.next():null;
        }
        while(tmp2!=null){
            if(outList.isEmpty() || tmp2.compareTo(outList.get(outList.size()-1))!=0){ //skip if the item has been put into outList
                outList.add(tmp2);
            }
            tmp2 = it2.hasNext()?it2.next():null;
        }
    }

    /**
     *  Return l1 - l2 (i.e, items in l1 that are not in l2), in sorted order.
     * Output is a set, so it should have no duplicates.
     * @param l1 sorted List
     * @param l2 sorted List
     * @param outList the items in l1 but not in l2, no duplicated items
     * @param <T>
     */
    public static<T extends Comparable<? super T>> void difference(List<T> l1, List<T> l2, List<T> outList) {
        Iterator<T> it1 = l1.iterator();
        Iterator<T> it2 = l2.iterator();
        T tmp1 = it1.hasNext()?it1.next():null;
        T tmp2 = it2.hasNext()?it2.next():null;
        //use two pointer to find the difference at put into outList
        while(tmp1!=null && tmp2!=null){
            if(tmp1.compareTo(tmp2)<0){
                if(outList.isEmpty() || tmp1.compareTo(outList.get(outList.size()-1))!=0){ //skip duplicated item
                    outList.add(tmp1);
                }
                tmp1 = it1.hasNext()?it1.next():null;
            }else if(tmp1.compareTo(tmp2)>0){
                tmp2 = it2.hasNext()?it2.next():null;
            }else{
                T tmp11=null;
                T tmp22 = null;
                if(it1.hasNext())
                    tmp11 = it1.next();
                while(it1.hasNext() && tmp11.compareTo(tmp1)==0){
                    tmp11=it1.next();
                }
                tmp1=tmp11;
                if(it2.hasNext())
                    tmp22 = it2.next();
                while(it2.hasNext() && tmp22.compareTo(tmp2)==0){
                    tmp22= it2.next();
                }
                tmp2=tmp22;
            }
        }

        while(tmp1!=null){
            if(outList.isEmpty() || tmp1.compareTo(outList.get(outList.size()-1))!=0){ //skip if the item has been put into outList
                outList.add(tmp1);
            }
            tmp1 = it1.hasNext()?it1.next():null;
        }
        while(tmp2!=null){
            if(outList.isEmpty() || tmp2.compareTo(outList.get(outList.size()-1))!=0){ //skip if the item has been put into outList
                outList.add(tmp2);
            }
            tmp2 = it2.hasNext()?it2.next():null;
        }
    }

    public static void main(String[] args){
        testIntersect();
        testUnion();
        testDifference();
    }


}
