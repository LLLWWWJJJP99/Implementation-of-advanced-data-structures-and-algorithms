// Ver 1.0:  Starter code for bounded size Binary Heap implementation

package cs6301.g36.sp6;
import java.util.Comparator;
import java.util.EmptyStackException;

public class BinaryHeap<T> {
    T[] pq;
    Comparator<T> c;
    int size;
    /** Build a priority queue with a given array q, using q[0..n-1].
     *  It is not necessary that n == q.length.
     *  Extra space available can be used to add new elements.
     */
    public BinaryHeap(T[] q, Comparator<T> comp, int n) {
	    pq = q;
	    c = comp;
	    size = n;
    }

    public void insert(T x) {
	add(x);
    }

    public T deleteMin() {
	return remove();
    }

    public T min() { 
	return peek();
    }

    public boolean isEmpty() {
        return (size == 0);
    }

    public boolean isFull() {
        return (size == pq.length);
    }

    public void add(T x) { /* TO DO. Throw exception if q is full. */
        if (isFull()) {
            throw new IndexOutOfBoundsException();
        }

        pq[size] = x;
        percolateUp(size);
        size++;
    }

    public T remove() { /* TO DO. Throw exception if q is empty. */
        if (isEmpty()) {
            throw new EmptyStackException();
        }

        T minValue = pq[0];
	    pq[0] = pq[--size];
	    percolateDown(0);
	    return minValue;
    }

    public T peek() { /* TO DO. Throw exception if q is empty. */
	    if (isEmpty()) {
	        throw new EmptyStackException();
        }

        return pq[0];
    }

    public void replace(T x) {
	/* TO DO.  Replaces root of binary heap by x if x has higher priority
	   (smaller) than root, and restore heap order.  Otherwise do nothing.
	   This operation is used in finding largest k elements in a stream.
	 */
	    pq[0] = x;
	    percolateDown(0);
    }

    /** pq[i] may violate heap order with parent */
    void percolateUp(int i) { /* to be implemented */
        T x = pq[i];
        while (i > 0 && c.compare(x, pq[parent(i)]) < 0) {
            move(pq, i, pq[parent(i)]);
            i = parent(i);
        }
        move(pq, i, x);
    }

    /** pq[i] may violate heap order with children */
    void percolateDown(int i) { /* to be implemented */
        T x = pq[i];
        int l = lson(i);

        while (l < size) {
            if (l < size - 1 && c.compare(pq[l], pq[l + 1]) > 0) {
                l++;
            }

            if (c.compare(x, pq[l]) < 0) {
                break;
            }

            move(pq, i, pq[l]);
            i = l;
            l = lson(i);
        }
        move(pq, i, x);
    }

    /** Create a heap.  Precondition: none. */
    void buildHeap() {
        for (int i = parent(size - 1); i >= 0; i--) {
            percolateDown(i);
        }
    }

    /* sort array A[].
       Sorted order depends on comparator used to buid heap.
       min heap ==> descending order
       max heap ==> ascending order
     */
    public static<T> void heapSort(T[] A, Comparator<T> comp) { /* to be implemented */
        BinaryHeap<T> q = new BinaryHeap<T>(A, comp, A.length);
        q.buildHeap();

        int n = A.length;
        for (int i = n - 1; i >= 0; i--) {
            A[i] = q.remove();
        }
    }

    void move(T[] pq, int i, T x) {
        pq[i] = x;
    }

    int lson(int i) { return 2 * i + 1; }
    int rson(int i) { return 2 * i + 2; }
    int parent(int i) { return (i - 1) / 2;  }

    public static void printHeap(Integer[] arr) {
        for (Integer i : arr) {
            System.out.printf("%s ", i);
        }
        System.out.println();
    }

    public static void main(String[] argc) {
        Comparator<Integer> comp1 = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return (o1 - o2);
            }
        };

        Comparator<Integer> comp2 = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return (o2 - o1);
            }
        };

        Integer[] arr = {3, 1, 2, 4, 5};
        heapSort(arr, comp1);
        printHeap(arr);

        heapSort(arr, comp2);
        printHeap(arr);
    }
}
