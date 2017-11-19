

CS 6301.502. Implementation of advanced data structures and algorithms
Fall 2017
Short Project 1: Sorting, BFS.
Wed, Aug 23, 2017

Version 1.0: Initial description (Thu, Aug 24).

Due: 11:59 PM, Sun, Sep 3.

Submission procedure:

Create a folder named cs6301 (no spaces) and inside that a folder whose
name is your group number (e.g., g00).  Place all files you are submitting
in that folder.  There is no need to submit binary files created by your
IDE (such as class files).  Make sure there is a "readme" file explaining
the contents of the files being submitted.  Zip the contents into a single
zip or rar file.  If the zip file is bigger than 1 MB, you have included
unnecessary files.  Delete them and create the zip file again.  If you
unzip, it should start with creation of the cs6301 folder.

Upload the zip or rar file on elearning.  Submission can be revised before
the deadline.  Only the final submission before the deadline will be graded.
Only one member of each group needs to submit project.  Include the names
of all team members in ALL files.

Solve as many problems as you wish.  Maximum score: 50


1. [30 points]
   Implement the merge sort algorithm on generic arrays and on an int array
   and compare their running times on arrays sizes from 1M-16M, and with
   an O(n^2) time algorithm, such as Insertion sort.  Write a report
   with a table and/or chart showing the times for different sizes.

   Signatures:
   // tmp array is used to store values during the merge operation.
   static<T extends Comparable<? super T>> void mergeSort(T[] arr, T[] tmp)
   static void mergeSort(int[] arr, int[] tmp)
   static<T extends Comparable<? super T>> void nSquareSort(T[] arr)

2. [30 points]
   Implement breadth-first search (BFS), and solve the problem of
   finding the diameter of a tree that works as follows:
   Run BFS, starting at an arbitrary node as root.  Let u be a node
   at maximum distance from the root.  Run BFS again, with u as the root.
   Output diameter: path from u to a node at maximum distance from u.

   Signature:

   // Return a longest path in g.  Algorithm is correct only if g is a tree.
   static LinkedList<Graph.Vertex> diameter(Graph g) { ... }


=====================================================
The Sort folder contains MergeSort.java which is the code to compare the running time of merge sort, insertion sort on generic or int array whose size ranges from 1000000 to 16000000.And the problem1_report.pdf are records of detailed running time comparison.

The Graph folder contains ArrayIterator.java and Graph.java which are the dependency class. Solution2.java includes the code to get the diameter.
Then the graphResult.txt contains some test result by ourselves. 