
CS 6301.502. Implementation of advanced data structures and algorithms
Fall 2017
Long Project 5: Skip Lists
Thu, Oct 26, 2017

Version 1.0: Initial description (Thu, Oct 26).

Due: 1st deadline: 11:59 PM, Sun, Nov 5. Final deadline: 11:59 PM, Sun, Nov 19.

Max marks: 100.  Maximum excellence credit: 2 (for rebuild() and get()).

Implement the following operations of skip lists.  Starter code is provided.
Do not change the name of the class or move it away from cs6301/gXX.
You can keep other source files in subfolders, if you wish to do so.
Do not change the signatures of public methods in the starter code.

* add(x): Add a new element x to the list. If x already exists in the
  skip list, replace it and return false.  Otherwise, insert x into the
  skip list and return true.

* ceiling(x): Find smallest element that is greater or equal to x.

* contains(x): Does list contain x?

* first(): Return first element of list.

* floor(x): Find largest element that is less than or equal to x.

* get(n): Return element at index n of list.  First element is at index 0.

* isEmpty(): Is the list empty?

* iterator(): Iterator for going through the elements of list in sorted order.

* last(): Return last element of list.

* rebuild(): Reorganize the elements of the list into a perfect skip list.

* remove(x): Remove x from the list. If successful, removed element is returned.
	     Otherwise, return null.
* size(): Return the number of elements in the list.
============================================================
LP5

- Shuffle.java
- SkipList.java
- SkipListIterator.java
- Timer.java
