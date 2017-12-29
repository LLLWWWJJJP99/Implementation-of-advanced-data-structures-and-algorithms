// starter code for Tries

package cs6301.g36;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class Trie<T, V> {
	private class Entry {
		V value;
		HashMap<T, Entry> child;

		Entry(V value) {
			this.value = value;
			child = new HashMap<>();
		}
	}

	private Entry root;

	Trie() {
		root = new Entry(null);
	}

	/**
	 * add a string with given value into trie in form of iterator
	 */
	public V add(Iterator<T> iter, V value) {
		return add(iter, value, root);
	}
	
	/**
	 * @param iter, iterator that visit every element of given input
	 * @param value, value assigned for given input
	 * @param cur, current node that is being visited
	 * @return null if added iter already exists else value if iter does not exists
	 */
	private V add(Iterator<T> iter, V value, Entry cur) {
		if(!iter.hasNext()) {
			if(cur.value != null) {
				return null;
			}
			cur.value = value;
			return value;
		}
		T ch = iter.next();
		Entry node = cur.child.get(ch);
		if(node == null) {
			node = new Entry(null);
			cur.child.put(ch, node);
		}
		return add(iter, value, node);
	}
	
	/**
	 * get value that is assigned to the string in trie
	 */
	public V get(Iterator<T> iter) {
		return get(iter, root);
	}
	
	/**
	 * @param iter, iterator that visit every element of given input
	 * @param cur, current node that is being visited
	 * @return null if get given iter does not exists ,else value if iter exists
	 */
	private V get(Iterator<T> iter, Entry cur) {
		if(!iter.hasNext()) {
			return cur.value;
		}
		T ch = iter.next();
		Entry node = cur.child.get(ch);
		if(node == null) {
			return null;
		}
		
		return get(iter, node);
	}
	
	/**
	 * remove a string with given value from trie
	 */
	public V remove(Iterator<T> iter) {
		return remove(iter, root);
	}
	
	
	/**
	 * @param iter, iterator that visit every element of given input
	 * @param cur, current node that is being visited
	 * @return null if get given iter does not exists ,else value if iter exists and get removed
	 */
	private V remove(Iterator<T> iter, Entry cur) {
		if(!iter.hasNext()) {
			V val = cur.value;
			if(val == null) return null;
			cur.value = null;
			return cur.child.size() == 0 ? val : null;
		}
		
		T ch = iter.next();
		Entry node = cur.child.get(ch);
		if(node == null) {
			return null;
		}
		V val = remove(iter, node);
		boolean shoudRemoveEntry = val == null ? false : true;
		if(shoudRemoveEntry) {
			cur.child.remove(ch);
		}
		return cur.child.size() == 0 && cur.value == null ? val : null;
	}

	// How many words in the dictionary start with this prefix?
	public int prefixCount(Iterator<T> iter) {
		Entry start = prefixCount(iter, root);
		return dfs(start);
	}
	
	/**
	 * @param cur, current node that is being visited
	 * @return number of words that are stored in a tree whose root is cur
	 */
	private int dfs(Entry cur) {
		if(cur.child.size() == 0) {
			return cur.value == null ? 0 : 1;
		}
		int res = 0;
		for(Entry entry : cur.child.values()) {
			res += dfs(entry);
		}
		return res;
	}
	
	/**
	 * @param iter, iterator that visit every element of given input
	 * @param cur, current node that is being visited
	 * @return null if get given prefix does not exists ,else entry that match the prefix
	 */
	private Entry prefixCount(Iterator<T> iter, Entry cur) {
		if(!iter.hasNext()) {
			return cur;
		}
		T ch = iter.next();
		Entry node = cur.child.get(ch);
		if(node == null) {
			return null;
		}
		return prefixCount(iter, node);
	}
	
	public static class StringIterator implements Iterator<Character> {
		char[] arr;
		int index;
		int length;
		public StringIterator(String s) {
			arr = s.toCharArray();
			index = 0;
			length = arr.length;
		}

		public boolean hasNext() {
			return index < arr.length;
		}

		public Character next() {
			return arr[index++];
		}

		public void remove() {
			throw new java.lang.UnsupportedOperationException();
		}
	}

	public static void main(String[] args) {
		Trie<Character, Integer> trie = new Trie<>();
		int wordno = 0;
		Scanner in = new Scanner(System.in);
		while (in.hasNext()) {
			String s = in.next();
			if (s.equals("End")) {
				break;
			}
			wordno++;
			trie.add(new StringIterator(s), wordno);
		}

		while (in.hasNext()) {
			String s = in.next();
			Integer val = trie.get(new StringIterator(s));
			System.out.println(s + "\t" + val);
		}
		in.close();
	}
}