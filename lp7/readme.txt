
/**
 *  Group 36
 *
 * Team Member:
 * @author Wenjie Li
 * @author Meng-Ju Lu
 * @author Xin Tong
 */



CS 6301.502. Implementation of advanced data structures and algorithms
Fall 2017
Long Project 7: Maximum flow problem
Thu, Nov 9, 2017

Version 1.0: Initial description (Thu, Nov 9).

Due: 1st deadline: 11:59 PM, Sun, Nov 26. Final deadline: 11:59 PM, Sun, Dec 10.

Max marks: 100 (100 points for each part). 
Maximum excellence credit: 10 (5 points for each part).
Criteria for allocation of EC: 1st deadline submission, good design,
good-quality code, correct outputs, fast RT, code reuse from previous projects.
Best 15 projects can earn up to 10 EC each.

Starter code is provided.
Do not change the name of the class or move it away from cs6301/gXX.
You can keep other source files in subfolders, if you wish to do so.
Do not change the signatures of public methods in the starter code.


Part a. Implement Dinitz's algorithm for maximum flow.

Part b. Implement Relabel-to-front algorithm for maximum flow.
 

dinitzMaxFlow():

	lp7-in0: output:
		2
Time: 4 msec.
Memory: 2 MB / 184 MB.

	lp7-in1: output:
		35
Time: 7 msec.
Memory: 2 MB / 184 MB.

	lp7-in2: output:
		994
Time: 50 msec.
Memory: 23 MB / 184 MB.

	lp7-in3: output:
		9924
Time: 279 msec.
Memory: 54 MB / 184 MB.

	lp7-in4: output:
		14
Time: 4 msec.
Memory: 2 MB / 184 MB.

	lp7-in5: output:
		23
Time: 3 msec.
Memory: 2 MB / 184 MB.

	lp7-in6: output:
521
Time: 39 msec.
Memory: 22 MB / 184 MB.

	lp7-in7: output:
		1344
Time: 499 msec.
Memory: 66 MB / 232 MB.

	lp7-in8: output:
		1843
Time: 1576 msec.
Memory: 255 MB / 528 MB.






relabelToFront():

	lp7-in0: output:
		2
Time: 12 msec.
Memory: 2 MB / 184 MB.

	lp7-in1: output:
		35
Time: 10 msec.
Memory: 3 MB / 184 MB.

	lp7-in2: output:
		994
Time: 32868 msec.
Memory: 11 MB / 170 MB.

	lp7-in3: output:
		9924
Time: Infinity msec.
Memory: Infinity MB.

	lp7-in4: output:
		14
Time: 4 msec.
Memory: 2 MB / 184 MB.

	lp7-in5: output:
		23
Time: 5 msec.
Memory: 2 MB / 184 MB.

	lp7-in6: output:
		521
Time: 187 msec.
Memory: 24 MB / 184 MB.

	lp7-in7: output:
		1344
Time: 13341 msec.
Memory: 26 MB / 324 MB.
		
	lp7-in8: output:
		1843
Time: 141664 msec.
Memory: 127 MB / 732 MB.