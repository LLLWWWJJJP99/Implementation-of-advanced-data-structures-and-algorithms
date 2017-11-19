package cs6301.g36;

import java.util.Iterator;

public class SkipListIterator<T extends Comparable<? super T>> implements Iterator<T> {
    //SkipList.Entry<T> head;
    SkipList<T> skipList;
    SkipList.Entry<T> head;

    public SkipListIterator(SkipList<T> sk) {
        this.skipList = sk;
        this.head = sk.head.next[0];
    }

    public boolean hasNext() {
        return head != skipList.tail;
    }

    public T next() {
        T val = head.element;
        head = head.next[0];
        return val;
    }
}
