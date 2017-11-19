/**
 * Group 36
 *
 * Team Member:
 * @author Wenjie Li
 * @author Meng-Ju Lu
 * @author Xin Tong
 */
package cs6301.g36.sp2;

import cs6301.g36.sp2.SinglyLinkedList.Entry;

public class ReverseSinglyLinkedList {
	
	public static void main(String[] args){
		SinglyLinkedList<Integer> list = new SinglyLinkedList<>();
		int size = 20;
		for(int i = 1;i<=size;i++){
			list.add(i);
		}
		list.printList();
		reverseNonRecursion(list);
		list.printList();
		reverseRecursion(list);
		list.printList();
	}
	/**
	* reverse SinglyLinkedList with Non-resursion method
	* @param list that need to be reversed
	*/
	public static void reverseNonRecursion(SinglyLinkedList<Integer> list){
		Entry<Integer> head = list.head;
		Entry<Integer> cur = head.next;
		Entry<Integer> pre = head.next;
		while(cur.next!=null){
			Entry<Integer> temp = cur.next;
			if(pre!=cur){
				cur.next = pre;
			}
			pre = cur;
			cur = temp;				
			
		}
		if(pre!=cur){
			cur.next = pre;
		}
		head.next.next = null;
		list.tail = head.next;
		list.head.next = cur;
	}
	/**
	* reverse SinglyLinkedList with resursion method
	* @param list that need to be reversed
	*/
	public static void reverseRecursion(SinglyLinkedList<Integer> list){
		Entry<Integer> head = list.head;
		Entry<Integer> cur = head.next;
		Entry<Integer> pre = head.next;
		cur = helper(pre, cur);
		head.next.next = null;
		list.tail = head.next;
		list.head.next = cur;
	}
	
	/**
	* helper function that helps to modify current entry and the rest of the list
	* @param previous visited entry
	* @param current entry
	* @return previous tail entry
	*/
	
	//how about change help to reverseRecursion here
	public static Entry<Integer> helper(Entry<Integer> pre,Entry<Integer> cur){
		if(cur.next==null){
			if(pre!=cur){
				cur.next = pre;
			}
			return cur;
		}
		Entry<Integer> temp = cur.next;
		if(pre!=cur){
			cur.next = pre;
		}
		return helper(cur, temp);
	}
}
