/**
 *  Group 36
 *
 * Team Member:
 * @author Wenjie Li
 * @author Meng-Ju Lu
 * @author Xin Tong
 */


/**
 *
 *
 (similar ro SLL) using an entry class as follows:
 static class Entry<T> {
 T element;
 Entry<T> prev, next;
 }
 You realize that this is isomorphic to the entry class for binary trees:
 static class Entry<T> {
 T element;
 Entry<T> left, right;
 }

 Write a recursive function to convert a doubly-linked list, in sorted order,
 into a height-balanced binary search tree, if we interpreted prev as left,
 and next as right.  Assume that lists are implemented with dummy headers.
 These are member functions of the list class, and do the work by rearranging
 references (pointers), without allocating additional space for elements.
 Write another function for the inverse problem: BST to sorted list.
 Signatures:

 // Precondition: list is sorted
 void sortedListToBST() { ... }

 // Precondition: data is arranged as a binary search tree
 //	using prev for left, and next for right
 void BSTtoSortedList() { ... }
 */
package cs6301.g36.sp4;

    public class DoubleLinkedList<T>{

        public static void main(String[] args){

            DoubleLinkedList<Integer> list = new DoubleLinkedList<>();
            for(int i=0;i<10000;i++){
                list.add(i);
            }
            list.SortedListToBST();
            list.BSTtoSortedList();

        }

        private static class Entry<T> {
            T element;
            Entry<T> prev, next;
            public Entry(T t){
                element=t;
            }
        }

        Entry<T> dummyHead;
        Entry<T> tail;
        Entry<T> root;  //fake root for mimic BST
        boolean isBST;

        public DoubleLinkedList(){
            dummyHead= new Entry<>(null);
            tail=dummyHead;
            isBST=false;
        }

        private void add(Entry<T> entry){
            entry.prev=tail;
            entry.next=tail.next;
            tail.next=entry;
            if(entry.next!=null)
                entry.next.prev=entry;
            tail=tail.next;
        }

        /**
         * add Generic element T into Double Linked list
         * @param t
         */
        public void add(T t){
            add(new Entry(t));
        }

        /**
         * precondition: list is sorted
         * recursively turn a sorted list into a balanced BST.
         * use prev pointer as left
         * use next pointer as right
         */
        void SortedListToBST(){
            Entry<T> head = dummyHead.next;
            dummyHead.next=null;
            tail=dummyHead;
            head.prev=null;
            root=recursivelySortedListToBST(head);
            isBST=true;
        }

        Entry<T> recursivelySortedListToBST(Entry<T> head) {
            //base condition
            if(head ==null || head.next==null){
                return head;
            }
            //keep finding the middle point which ensure the BST to be balanced
            Entry<T> fast = head,slow=head;
            while(fast!=null){
                fast=fast.next;
                if(fast!=null){
                    fast=fast.next;
                    slow=slow.next;
                }
            }
            Entry<T> leftSubTreeRightBound = slow.prev;
            if(leftSubTreeRightBound!=null){
                leftSubTreeRightBound.next=null;
            }
            slow.prev=null;
            Entry<T> rightSubTreeLeftBound = slow.next;
            if(rightSubTreeLeftBound!=null){
                rightSubTreeLeftBound.prev=null;
            }
            slow.next=null;
            // recursively generae leftsubtree
            Entry<T> leftSubTree = recursivelySortedListToBST(head);
            // recursively generae rightsubtree
            Entry<T> rightSubTree = recursivelySortedListToBST(rightSubTreeLeftBound);
            slow.prev=leftSubTree;
            slow.next=rightSubTree;
            return slow;

        }

        /**
         * Precondition: data is arranged as a binary search tree
         * using prev for left, and next for right
         * root is the current root of BST
         */
        void BSTtoSortedList(){
            recursivelyBSTtoSortedList(root);
            isBST=false;
        }

        void recursivelyBSTtoSortedList(Entry<T> root) {
            //inorder traverse
            if(root==null){
                return;
            }
            if(root.prev!=null){
                recursivelyBSTtoSortedList(root.prev);
            }
            root.prev=tail; //node's prev pointer can be rearrange as soon as it's traversed
            tail.next=root; //use tail to rearrange the next pointer
            tail=tail.next;
            if(root.next!=null){
                recursivelyBSTtoSortedList(root.next);
            }
        }




    }

