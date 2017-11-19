
CS 6301.502. Implementation of advanced data structures and algorithms
Fall 2017
Short Project 6: Priority queues and their applications
Fri, Sep 29, 2017

Version 1.0: Initial description (Fri, Sep 29).

Due: 11:59 PM, Sun, Oct 8.

Solve as many problems as you wish.  Maximum score: 50
For Q1-Q4, use Java's PriorityQueue.

1. [30 points]
   Sort an array using k-way merge (normally used for external sorting):
   Split the array A into k fragments, sort them recursively, and merge them
   using a priority queue (of size k).
   [In external sorting applications, intermediate sorted subarrays are
    written to disk.  The algorithm sorts A by using buffers of size O(k).]

2. [30 points]
   Implement Huffman Coding algorithm.  Create a class for representing coding
   trees.  Use a priority queue to hold the trees.  In each step, the algorithm
   removes two trees with the smallest frequencies, merges them, and inserts it
   back into the priority queue.  At the end, there is a single coding tree. 
   Traverse the tree and output the binary codes for each symbol.

3. [20 points]
   Perfect powers: Write an algorithm to output exact powers (numbers of the
   form a^b, b>1), in the range 2 to n. 
   Given an array of prime numbers, output numbers in [2,n] all of whose
   prime factors are only from the given set of prime numbers.
   For example, given {3,7}, the program outputs {3,7,9,21,27,49,63,...}.
   Make sure that your program outputs each number only once, in sorted order.

4. [20 points]
   Implement Prim's algorithm for MST using priority queue of edges (Prim1).
   Starter code is provided.

5. [30 points]
   Implement binary heap and heap sort.  Starter code is provided.
   Include in your submission, a driver that uses heap sort to sort an array
   in ascending order, and then in descending order.

6. [30 points] (Solving Q6 requires that you do Q5 also.)
   Extend binary heaps to indexed binary heaps.  Implement Prim's algorithm
   for MST using indexed heaps of vertices (Prim2).  Starter code is provided.

7. [30 points]
   Implement Kruskal's algorithm for MST.  Implement the disjoint set ADT
   as a separate class that can be used in other applications.

8. [20 points]
   Compare the running times of the following two algorithms for the problem of
   finding the k largest elements of a stream:
   (a) Use Java's priority queue to keep track of the k largest elements seen
   (b) Use your priority queue implementation (problem 5) using the replace()
       operation in that implementation, instead of delete+add to update PQ.
   Try large inputs that are randomly ordered and other inputs in increasing order.

======================================================================================
/**
 *  Group 36
 *
 * Team Member:
 * @author Wenjie Li
 * @author Meng-Ju Lu
 * @author Xin Tong
 */

 @SP6
 Version 1
 Q1: KWayMerge.java
 Q2: HuffmanCoding.java
 Q3: PerfectPowers
 Q4: PrimMST.java
 Q5: BinaryHeap.java
 Q6: IndexedHeap.java && PrimeMST.java
 Q7: KruskalMST.java && UnionFind.java
 Q8: SP6Q8.java && SelectAlgorithm.java && IndexedHeap.java



 Version 2
 Just for the Purpose of Code efficiency improvement, not for grading purpose

 @Update PrimMST
 Rational: use Inheritance and Upcasting and Downcasting which are more Object Oriented way
 Benefit: avoid using extra array to store PrimVertex nodes

 extend Graph to PGraph
 extend Graph.Vertex to PGraph.PrimVertex
 Refactor Graph's constructor method such that we can override some Graph's construct methods like buildGraph
 Override Graph.readGraph

 Reusing Graph's Graph.Vertex[] to store PGraph.PrimVertex by using upcasting
 When running Prim algorith, downcast Graph.Vertex back to PGraph.PrimVertex

 Performance improvement:
 Benchmark : big graph file with 500000 vertex and 5249924 edges given by lp2

 Using GraphALgorithm:
         prim1:
         Time: 2230 msec.
         Memory: 1458 MB / 1801 MB.
         prim2:
         Time: 1830 msec.
         Memory: 1384 MB / 1799 MB.

 Using Inheritance and Upcasting & downcasting way:
        Prime1:
        Time: 1054 msec.
        Memory: 473 MB / 1835 MB.
        Prim2:
        Time: 930 msec.
        Memory: 440 MB / 1838 MB.




