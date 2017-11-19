package cs6301.g36.sp2;

import java.util.Iterator;
import java.util.Scanner;
import java.util.NoSuchElementException;
import java.util.Stack;

public class SinglyLinkedList<T> implements Iterable<T> {

    /** Class Entry holds a single node of the list */
    static class Entry<T> {
        T element;
        Entry<T> next;

        /** constructor */
        Entry(T x, Entry<T> nxt) {
            element = x;
            next = nxt;
        }
    }

    // Dummy header is used.  tail stores reference of tail element of list
    Entry<T> head, tail;
    int size;

    /** constructor of SinglyLinkedList */
    public SinglyLinkedList() {
        head = new Entry<>(null, null);
        tail = head;
        size = 0;
    }

    public Iterator<T> iterator() { return new SLLIterator<>(this); }

    /** SinglyLinkedList (SLL) Iterator */
    private class SLLIterator<E> implements Iterator<E> {
        SinglyLinkedList<E> list;
        Entry<E> cursor, prev;
        boolean ready;  // is item ready to be removed?

        SLLIterator(SinglyLinkedList<E> list) {
            this.list = list;
            cursor = list.head;
            prev = null;
            ready = false;
        }

        public boolean hasNext() {
            return cursor.next != null;
        }

        public E next() {
            prev = cursor;
            cursor = cursor.next;
            ready = true;
            return cursor.element;
        }

        // Removes the current element (retrieved by the most recent next())
        // Remove can be called only if next has been called and the element has not been removed
        public void remove() {
            if(!ready) {
                throw new NoSuchElementException();
            }
            prev.next = cursor.next;
            // Handle case when tail of a list is deleted
            if(cursor == list.tail) {
                list.tail = prev;
            }
            cursor = prev;
            ready = false;  // Calling remove again without calling next will result in exception thrown
            size--;
        }
    } // SLLIterator

    // Add new elements to the end of the list
    public void add(T x) {
        tail.next = new Entry<>(x, null);
        tail = tail.next;
        size++;
    }

    public void printList() {
	/* Code without using implicit iterator in for each loop:

        Entry<T> x = head.next;
        while(x != null) {
            System.out.print(x.element + " ");
            x = x.next;
        }
	*/

        System.out.print(this.size + ": ");
        for(T item: this) {
            System.out.print(item + " ");
        }

        System.out.println();
    }

    public String toString() {
        String lst = new String("");

        Entry<T> x = head.next;
        while (x != null) {
            lst += x.element.toString() + " ";
            x = x.next;
        }
        return lst;
    }

    // Rearrange the elements of the list by linking the elements at even index
    // followed by the elements at odd index. Implemented by rearranging pointers
    // of existing elements without allocating any new elements.
    public void unzip() {
        if(size < 3) {  // Too few elements.  No change.
            return;
        }

        Entry<T> tail0 = head.next;
        Entry<T> head1 = tail0.next;
        Entry<T> tail1 = head1;
        Entry<T> c = tail1.next;
        int state = 0;

        // Invariant: tail0 is the tail of the chain of elements with even index.
        // tail1 is the tail of odd index chain.
        // c is current element to be processed.
        // state indicates the state of the finite state machine
        // state = i indicates that the current element is added after taili (i=0,1).
        while(c != null) {
            if(state == 0) {
                tail0.next = c;
                tail0 = c;
                c = c.next;
            } else {
                tail1.next = c;
                tail1 = c;
                c = c.next;
            }
            state = 1 - state;
        }
        tail0.next = head1;
        tail1.next = null;
    }

    public void multiUnzip(int k) {
        if (size < k + 1) {
            return;
        }

        Entry<T> [] tails = new Entry[k];
        Entry<T> [] heads = new Entry[k - 1];
        Entry<T> tmp = head.next;

        for (int i = 0; i < k; i++) {
            tails[i] = tmp;
            tmp = tmp.next;
            if (i < heads.length) {
                heads[i] = tmp;
            }
        }

        Entry<T> c = tmp;
        int state = 0;

        while (c != null) {
            tails[state].next = c;
            tails[state] = c;
            c = c.next;
            state = (state + 1) % k;
        }

        for (int i = 0; i < k - 1; i++) {
            tails[i].next = heads[i];
        }
        tails[tails.length - 1].next = null;
    }

    public void printReverseI() {
        Stack<Entry<T>> stk = new Stack<Entry<T>>();
        Entry<T> node = head.next;

        while (node != null) {
            stk.push(node);
            node = node.next;
        }

        while (!stk.isEmpty()) {
            System.out.print(stk.pop().element + " ");
        }
    }

    public void printReverseR() {
        Entry<T> node = head.next;
        printReverseRHelper(node);
    }

    private void printReverseRHelper(Entry<T> node) {
        if (node == null) {
            return;
        }
        printReverseRHelper(node.next);
        System.out.print(node.element + " ");
    }

    public void reverseR() {
        Entry<T> node = head.next;
        Entry<T> prev = null;
        reverseRHelper(prev, node);
    }

    private void reverseRHelper(Entry<T> prev, Entry<T> node) {
        if (node == null) {
            head.next = prev;
            return;
        }

        Entry<T> tmp = node.next;
        node.next = prev;
        prev = node;
        reverseRHelper(prev, tmp);
    }

    public void reverseI() {
        head.next = reverseIHelper();
    }

    private Entry<T> reverseIHelper() {
        Entry<T> prev = null;
        Entry<T> curr = head.next;
        Entry<T> node = head.next;

        while (node != null) {
            curr = node;
            node = node.next;
            curr.next = prev;
            prev = curr;
        }
        return prev;
    }

    public static void main(String[] args) throws NoSuchElementException {
        int n = 10;
        if(args.length > 0) {
            n = Integer.parseInt(args[0]);
        }

        SinglyLinkedList<Integer> lst1 = new SinglyLinkedList<>();
        SinglyLinkedList<Integer> lst2 = new SinglyLinkedList<>();
        SinglyLinkedList<Integer> lst3 = new SinglyLinkedList<>();
        SinglyLinkedList<Integer> lst4 = new SinglyLinkedList<>();
        for(int i = 1; i <= n; i++) {
            lst1.add(new Integer(i));
            lst2.add(new Integer(i));
            lst3.add(new Integer(i));
            lst4.add(new Integer(i));
        }

        lst1.multiUnzip(2);
        //System.out.println(lst1.toString());
        assert "1 3 5 7 9 2 4 6 8 10 ".equals(lst1.toString());

        lst2.multiUnzip(3);
        //System.out.println(lst2.toString());
        assert "1 4 7 10 2 5 8 3 6 9 ".equals(lst2.toString());

        lst3.multiUnzip(4);
        //System.out.println(lst3.toString());
        assert "1 5 9 2 6 10 3 7 4 8 ".equals(lst3.toString());

        //lst4.printReverseR();
        //System.out.println("\n");
        //lst4.printReverseI();
        lst4.printList();
        lst4.reverseR();
        lst4.printList();
    }
}
