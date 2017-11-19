package cs6301.g36;

import org.omg.CORBA.OBJ_ADAPTER;

import java.util.Iterator;
import java.util.Random;

// Skeleton for skip list implementation.
public class SkipList<T extends Comparable<? super T>> {

    /**
     * LinkedList node
     * */
    static class Entry<T> {
        T element;
        Entry<T>[] next;
        int[] span;
        int level;

        /**
         * @size random size in [1, maxLevel]
         * */
        Entry(T x, int size) {
            element = x;
            next = new Entry[size];
            span = new int[size];
            level = size;
        }

        Entry(int size) {
            this(null, size);
        }
    }

    // dummy head/tail entry
    Entry<T> head;
    Entry<T> tail;
    Entry<T>[] prev;

    int maxLevel;
    int size;
    int threshold;

    Object minValue = Long.MIN_VALUE;
    Object maxValue = Long.MAX_VALUE;

    Random random = new Random();

    // Constructor
    public SkipList() {
        maxLevel = 5;
        size = 0;
        threshold = 10000;
        head = new Entry<>((T)minValue, maxLevel);
        tail = new Entry<>((T)maxValue, maxLevel);
        prev = new Entry[maxLevel];

        // connect head.next to tail.next
        for (int i = 0; i < maxLevel; i++) {
            head.next[i] = tail;
            head.span[i] = 1;
        }
    }

    // Add x to list. If x already exists, replace it. Returns true if new node is added to list
    public boolean add(T x) {
        if (size >= threshold) {
            threshold *= 2;
            rebuild();
        }

        find(prev, x);

        if (prev[0].next[0].element.compareTo(x) == 0) {
            prev[0].next[0].element = x;
            return false;
        } else {
            int level = chooseLevel();
            Entry<T> n = new Entry<>(x, level);

            for (int i = 0; i < level; i++) {
                // Update spans
                n.span[i] = i == 0? 1 : 0;
                Entry<T> tmp = prev[0].next[0];
                while (tmp != prev[i].next[i]) {
                    n.span[i]++;
                    tmp = tmp.next[0];
                }
                prev[i].span[i] = (prev[i].span[i] + 1) - n.span[i];

                // Update pointer
                n.next[i] = prev[i].next[i];
                prev[i].next[i] = n;
            }

            // Update spans greater than level
            for (int i = level; i < maxLevel; i++) {
                prev[i].span[i]++;
            }

            size++;
        }
        return true;
    }


    // Find smallest element that is greater or equal to x
    public T ceiling(T x) {
        find(prev, x);
        return prev[0].next[0].element;
    }


    // Does list contain x?
    public boolean contains(T x) {
        find(prev, x);
        return prev[0].next[0].element.compareTo(x) == 0;
    }


    // Return first element of list
    public T first() {
        return head.next[0].element;
    }


    // Find largest element that is less than or equal to x
    public T floor(T x) {
        find(prev, x);
        if (prev[0].next[0].element.compareTo(x) == 0) {
            return prev[0].next[0].element;
        }
        return prev[0].element;
    }


    // Return element at index n of list.  First element is at index 0.
    public T get(int n) {
        Entry<T> p = head;

        int sumGap = 0;
        for (int j = maxLevel - 1; j >= 0; j--) {
            while (sumGap + p.span[j] <= n) {
                sumGap += p.span[j];
                p = p.next[j];
            }
            if (sumGap == n) { return p.next[0].element; }
        }
        return null;
    }


    // Is the list empty?
    public boolean isEmpty() {
        return size == 0;
    }


    // Iterate through the elements of list in sorted order
    public Iterator<T> iterator() {
        return new SkipListIterator<T>(this);
    }


    // Return last element of list
    public T last() {
        return get(size - 1);
    }


    // Reorganize the elements of the list into a perfect skip list
    public void rebuild() {
        int newLevel = (int) (Math.log(size) / Math.log(2));
        setMaxLevel(newLevel);
        prev = new Entry[newLevel];

        Entry<T> p = head;
        while (p != null) {
            Entry<T> next = p.next[0];
            int level = (p == head || p == tail)? newLevel : 1;
            p.next = new Entry[level];
            p.span = new int[level];
            p.level = level;

            p.next[0] = next;
            p.span[0] = 1;
            p = p.next[0];
        }

        tail.span[0] = 0;
        rebuild(1, 2);
    }

    private void rebuild(int level, int span) {
        if (level >= maxLevel) { return; }

        Entry<T> p = head;
        Entry<T> curr = head;

        int i = 0;
        while (p != tail) {
            if (i++ % 2 == 0) {
                Entry<T>[] tmpNext = p.next;
                int[] tmpSpan = p.span;

                p.level = level + 1;
                p.next = new Entry[p.level];
                p.span = new int[p.level];

                System.arraycopy(tmpNext, 0, p.next, 0, level);
                System.arraycopy(tmpSpan, 0, p.span, 0, level);

                curr.next[level] = p;
                curr.span[level] = span;
                curr = curr.next[level];
            }
            p = p.next[level - 1];
        }

        if (curr != tail) {
            curr.next[level] = tail;
            p = curr;

            curr.span[level] = 0;
            while (p != tail) {
                curr.span[level]++;
                p = p.next[0];
            }
        }

        rebuild(level + 1, span * 2);
    }


    // Remove x from list.  Removed element is returned. Return null if x not in list
    public T remove(T x) {
        find(prev, x);
        Entry<T> n = prev[0].next[0];

        if (n.element.compareTo(x) != 0) { // x is not found
            return null;
        }

        int i = 0;
        for (; i < maxLevel; i++) {
            if (prev[i].next[i] == n) {
                prev[i].next[i] = n.next[i];
                prev[i].span[i] += n.span[i] - 1;
            } else {
                break;
            }
        }

        for (; i < maxLevel; i++) {
            prev[i].span[i]--;
        }

        size--;
        return x;
    }

    // Return the number of elements in the list
    public int size() {
        return size;
    }

    /**
     * Choose number of levels for a new node randomly
     * P(choosing level i) = 0.5 * P(choosing level i - 1)
     * */
    private int chooseLevel() {
        int mask = (1 << maxLevel) - 1;
        int level = Integer.numberOfTrailingZeros(random.nextInt() & mask);

        if (level > maxLevel) {
            return maxLevel;
        }

        if (level == 0) {
            return level + 1;
        }

        return level;
    }

    private int chooseLevel(int lev) {
        int i = 1;
        while (i < lev) {
            boolean b = random.nextBoolean();
            if (b) { i++; }
            else { break; }
        }
        return i;
    }


    /**
     * Return prev[0, maxLevel] of nodes
     * at which search went down one level,
     * looking for x.
     * */
    private void find(Entry<T>[] prev, T x) {
        Entry<T> p = head;

        for (int i = maxLevel - 1; i >= 0; i--) {
            while (p.next[i].element.compareTo(x) < 0) {
                p = p.next[i];
            } // p.next[i].element >= x
            prev[i] = p;
        }
    }

    private void setMaxLevel(int level) {
        maxLevel = level;
    }


    public static void main(String[] args) {
        SkipList<Long> sk = new SkipList<>();
        Timer timer = new Timer();
        int size = 22;
        Long[] arr = new Long[size];

        for (int i = 0; i < arr.length; i++) {
            arr[i] = new Long(i + 1);
        }

        Shuffle.shuffle(arr, 0, arr.length - 1);
        timer.start();
        for (int i = 0; i < size; i++) {
            sk.add(arr[i]);
        }
        System.out.println(timer.end());


        timer.start();
        sk.rebuild();
        System.out.println(timer.end());

        /*
        for (int i = 0; i < size; i++) {
            sk.add(arr[i]);
        }
        */

        /*
        sk.remove(new Long(50));
        sk.remove(new Long(0));

        Entry<Long> p = sk.head.next[0].next[0];


        while (sk.size() != 0) {
            System.out.println(sk.first());
            sk.remove(sk.first());
        }
        */


        /*
        sk.remove(new Long(50));
        sk.remove(new Long(9000));
        sk.remove(new Long(9999));
        sk.remove(new Long(100));

        System.out.println(sk.maxLevel);
        */


        System.out.println(sk.maxLevel);
        Entry<Long> p = sk.head;
        int level = 3;
        while (p != null) {
            System.out.printf("%s, %s, %s\n", p.element, p.span[level], p.level);
            p = p.next[level];
        }
        System.out.println();
        System.out.println(sk.last());


    }
}