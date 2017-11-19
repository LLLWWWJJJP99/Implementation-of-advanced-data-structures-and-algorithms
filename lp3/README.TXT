CS 6301.502.  Implementation of advanced data structures and algorithms
Fall 2017;  Fri, Oct 6.
Long Project LP3: Optimal branchings (directed MST) in directed graphs

Ver 1.0: Initial description (Fri, Oct 6).
Ver 1.1: Added missing information about max score of 100 (Fri, Oct 27).

Due: 11:59 PM, Sun, Oct 29 (1st deadline), Sun, Nov 12 (2nd deadline).

Max score: 100.  Max excellence credits: 10.

Initial code base: ArrayIterator, Graph, GraphAlgorithm, XGraph, LP3, Timer
You can also use code from other examples shown by the instructor in this class


Submit before the first deadline to be eligible for excellence credit. 
For each group, only its last submission is kept and earlier submissions are discarded. 
Your code must be of good quality, and pass all test cases to earn excellence credits.
Only the first 15 groups satisfying above conditions will be assigned excellence credits.

Project Description

Implement the algorithm discussed in class for finding minimum spanning trees in directed graphs (optimal branchings). The version that you need to implement was discussed in class as Tarjan's algorithm for optimal branchings, that was an improvement over the algorithms of Chu and Liu, and, Edmonds. A starter driver (LP3.java) is provided. Do not change the input format used by that program. It reads an integer corresponding to the root vertex, and then an input graph is provided in the format expected by Graph.readDirectedGraph(). You need to write the code for directedMST() in LP3.java. This function should return the weight of the MST it finds. In addition, the list dmst, passed as a parameter to this function, should be filled with the edges of that MST, in the order stated in the file LP3.java. It is recommended that you create separate classes for your code, and add just enough code to LP3.java to execute that code. When submitting the project, LP3.java must be placed in cs6301/gXX/, where gXX is your group name. Do not place LP3.java in some other subfolder. The output of LP3.java is already set up in the starter code. Do not change it.
Excellence credits

Your submission can get up to 10 excellence credits, if your code is one of the first 15 groups that submit good quality code before the first deadline, that correctly solves the problem, and passes all test inputs. An important criterion that will be used in judging quality is whether your solution is able to use standard algorithms for graph problems, such as DFS as-is, without rewriting them specially for this algorithm. Other aspects of quality such as OOP techniques, organization of objects and code, transparency of code, etc. will also be used. Programs selected for EC will also have to complete their runs successfully, within a timeout period. The best programs will complete within 10% of the allotted time.
Input:
1
5 7
1 5 8
1 4 7
1 3 6
4 3 3
3 5 6
5 3 2
5 2 1

Output:
17
Additional lines of output with VERBOSE=1:
_________________________
null(5,2)(4,3)(1,4)(3,5)
_________________________
Timer output

================================================================================================


/**
 *  Group 36
 *
 * Team Member:
 * @author Wenjie Li
 * @author Meng-Ju Lu
 * @author Xin Tong
 */

 @Update
 1. Optimize the Edges Weight Transform
 Used to iterate all the vertex and check whether transform is needed.
 Now parse only the new superVertex to transform weight

 2. Optimize the Shrink SCC method
 Used to iterate all the vertex in graph and found edges need to be melded.
 Now pass only the newly created SuperVertex, found its outgoing and incoming edges of all its sub vertex, and meld them

 3. Optimize the expand method
 Used to count the vertex in the Result to check whether expansion is done.
 Now use a new UnexpandedList to hold unexpanded Edge, end loop when result is empty

@Self Verification result
Parameters:-Xss512m -Xmx1500m

@RT
-------------dmst-1m-10m-50450242.txt---------
wmst is 50450242
Time: 22915 msec.
Memory: 437 MB / 651 MB.
------------dmst-100k-110k-4894010.txt--------
wmst is 4894010
Time: 1500 msec.
Memory: 114 MB / 425 MB.
-----------------lp3-in0.txt------------------
wmst is 17
Time: 3 msec.
Memory: 5 MB / 238 MB.
-----------------lp3-in1.txt------------------
wmst is 180922
Time: 8 msec.
Memory: 6 MB / 238 MB.
-----------------lp3-in2.txt------------------
wmst is 178316
Time: 14 msec.
Memory: 6 MB / 238 MB.
-----------------lp3-in3.txt------------------
wmst is 244483
Time: 31 msec.
Memory: 7 MB / 238 MB.
-----------------t0.txt-----------------------
wmst is 3990
Time: 13 msec.
Memory: 6 MB / 238 MB.
-----------------t1.txt-----------------------
wmst is 3946
Time: 10 msec.
Memory: 6 MB / 238 MB.
-----------------t2.txt-----------------------
wmst is 577
Time: 19 msec.
Memory: 10 MB / 238 MB.
-----------------t3.txt-----------------------
wmst is 16295
Time: 90 msec.
Memory: 23 MB / 238 MB.
-----------------t4.txt-----------------------
wmst is 191492
Time: 114 msec.
Memory: 59 MB / 238 MB.
-----------------t5.txt-----------------------
wmst is 1934124
Time: 1824 msec.
Memory: 353 MB / 640 MB.
-----------------t6.txt-----------------------
wmst is 1099654
Time: 10965 msec.
Memory: 518 MB / 781 MB.
-----------------t7.txt-----------------------
wmst is 2191851
Time: 34697 msec.
Memory: 1010 MB / 1333 MB.
