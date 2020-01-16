package egrep;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * A compact prefix tree that maps a key (string) to a value (generic). Null
 * Values are not accepted, as null is reserved to denote the non-existence of
 * the key.
 *
 * @param <A> The generic type of the values
 */
public class RadixTree<A> {

	private Node<A> root;

	public RadixTree() {
		this.root = new Node<A>();
	}

	public Node<A> getRoot() {
		return root;
	}

	/**
	 * Checks the existence of the prefix (is possibly a key). Returns true whether
	 * the prefix is intermediary (with no value) or a key (with a value).
	 * 
	 * @param key The prefix to search for.
	 * @return True if the prefix exists.
	 */
	public boolean exist(String key) {
		if (key == null) {
			return false;
		}

		Node<A> curr = root; // traversing node

		while (true) {
			Iterator<String> itr = curr.children.keySet().iterator();

			String key2 = null;

			int commonPrefix = 0; // length of common prefix

			while (itr.hasNext()) { // loop through children
				key2 = itr.next();
				commonPrefix = RadixTree.commonPrefix(key, key2);
				if (commonPrefix > 0) {
					break;
				}
			}

			if (commonPrefix == 0) { // no match
				return false;
			}

			if (key.length() == key2.length() && key.length() == commonPrefix) { // exact match
				return true;
			}

			if (commonPrefix == key2.length()) { // 'key2' is a prefix to 'key'
				curr = curr.children.get(key2);
				key = key.substring(commonPrefix);
				continue;
			}

			// 'key' and 'key2' share a common prefix (including 'key' itself)
			return false;
		}
	}

	/**
	 * find others keys who has current key as prefix
	 * 
	 * @param key
	 * @return The ArrayList of key. Null if not found.
	 */
	public ArrayList<String> findNeignbours(String key, Node<A> curr) {
		ArrayList<String> neignbours = new ArrayList<String>();
		String key2 = null;
		if (curr.children.isEmpty())
			return neignbours;
		for (Map.Entry<String, Node<A>> entry : curr.children.entrySet()) {
			key2 = entry.getKey();
			if (key.startsWith(key2)) {
				ArrayList<String> sou_neignbours = findNeignbours(key.substring(key2.length()),
						curr.children.get(key2));
				if (!sou_neignbours.isEmpty())
					neignbours.addAll(sou_neignbours);
			}
			if (key2.startsWith(key)) {
				neignbours.add(key2.substring(key.length()));
			}
			// System.out.println("current key =>"+key);
			// System.out.println("Parse tree =>"+key2);
		}

		return neignbours;
	}

	/**
	 * Maps the key to a value if the key exists. recursive
	 * 
	 * @param key
	 * @return The value mapped by key. Null if the key does not exist.
	 */
	public A getV2(String key, Node<A> curr) {
		String key2 = null;
		if (curr.children.isEmpty())
			return null;
		for (Map.Entry<String, Node<A>> entry : curr.children.entrySet()) {
			key2 = entry.getKey();
			if (key2.equals(key)) {
				Node<A> old = curr.children.get(key);
				if (old instanceof WithValue<?>) {
					return ((WithValue<A>) old).val;
				}
				return null;
			}
			if (key.startsWith(key2)) {
				return getV2(key.substring(key2.length()), curr.children.get(key2));
			}
		}
		return null;
	}

	
	/**
	 * Inserts a node with the specified key/value pair. If the Node exists, the
	 * value is updated.
	 * recursive
	 * @param key
	 * @param val
	 * @return True if the operation succeeded. False otherwise.
	 */
	public boolean setV2(String key, A val, Node<A> curr) {
		String key2 = null;
		if(curr.children.isEmpty()){
			WithValue<A> update = new WithValue<A>(val);
			curr.children.put(key, update);
		}
		for (Map.Entry<String, Node<A>> entry : curr.children.entrySet()) {
			key2 = entry.getKey();
			if (key2.equals(key)) {
				WithValue<A> update = new WithValue<A>(val);
				curr.children.put(key,update);
				return true;
			}
			if (key.startsWith(key2)) {
				if(!curr.children.isEmpty()) {
				return setV2(key.substring(key2.length()), val,curr.children.get(key2));
				}else {
					WithValue<A> update = new WithValue<A>(val);
					curr.children.put(key, update);
				}
			}
		}
		return false;
	}
	/**
	 * Maps the key to a value if the key exists.
	 * 
	 * @param key
	 * @return The value mapped by key. Null if the key does not exist.
	 */
	public A get(String key) {
		if (key == null) {
			return null;
		}

		Node<A> curr = root; // traversing node

		while (true) {
			Iterator<String> itr = curr.children.keySet().iterator();

			String key2 = null;

			int commonPrefix = 0; // length of common prefix

			while (itr.hasNext()) { // loop through children
				key2 = itr.next();
				commonPrefix = RadixTree.commonPrefix(key, key2);
				if (commonPrefix > 0) {
					break;
				}
			}

			if (commonPrefix == 0) { // no match
				return null;
			}

			if (key.length() == key2.length() && key.length() == commonPrefix) { // exact match
				Node<A> old = curr.children.get(key);
				if (old instanceof WithValue<?>) {
					return ((WithValue<A>) old).val;
				}
				return null;
			}

			if (commonPrefix == key2.length()) { // 'key2' is a prefix to 'key'
				curr = curr.children.get(key2);
				key = key.substring(commonPrefix);
				continue;
			}

			// 'key' and 'key2' share a common prefix (including 'key' itself)
			return null;
		}
	}

	/**
	 * Inserts a node with the specified key/value pair. If the Node exists, the
	 * value is updated.
	 * 
	 * @param key
	 * @param val
	 * @return True if the operation succeeded. False otherwise.
	 */
	public boolean set(String key, A val) {
		if (key == null) {
			return false;
		}

		Node<A> curr = root; // traversing node

		while (true) {
			Iterator<String> itr = curr.children.keySet().iterator();

			String key2 = null;

			int commonPrefix = 0; // length of common prefix

			while (itr.hasNext()) { // loop through children
				key2 = itr.next();
				commonPrefix = RadixTree.commonPrefix(key, key2);
				if (commonPrefix > 0) {
					break;
				}
			}

			if (commonPrefix == 0) { // no match
				WithValue<A> child = new WithValue<A>(val);
				curr.children.put(key, child);
				break;
			}

			if (key.length() == key2.length() && key.length() == commonPrefix) { // exact match
				Node<A> old = curr.children.get(key);
				if (old instanceof WithValue<?>) {
					((WithValue<A>) old).val = val;
				} else {
					WithValue<A> update = new WithValue<A>(val);
					update.children = old.children;
					curr.children.put(key, update);
					// GC takes care of old
				}
				break;
			}

			if (commonPrefix == key2.length()) { // 'key2' is a prefix to 'key'
				curr = curr.children.get(key2);
				key = key.substring(commonPrefix);
				continue;
			}

			// 'key' and 'key2' share a common prefix (including 'key' itself)
			Node<A> old = curr.children.remove(key2);

			if (commonPrefix == key.length()) { // 'key' is a prefix to 'key2'
				WithValue<A> common = new WithValue<A>(val);
				curr.children.put(key, common);

				common.children.put(key2.substring(commonPrefix), old);
			} else { // 'key' and 'key2' share a common prefix
				Node<A> common = new Node<A>();
				curr.children.put(key.substring(0, commonPrefix), common);

				common.children.put(key2.substring(commonPrefix), old);

				WithValue<A> neW = new WithValue<A>(val);
				common.children.put(key.substring(commonPrefix), neW);
			}

			break;

		}

		return true;
	}

	/**
	 * Upon deleting a key, subsequent calls to 'get' for this 'key' will return
	 * null (key non existing). Subsequent calls to 'exist' may still return true,
	 * if the key is an intermediate prefix to other keys.
	 * 
	 * @param key
	 * @return True if deletion occurred.
	 */
	public boolean del(String key) {
		if (key == null) {
			return false;
		}

		Node<A> prev = null;
		Node<A> curr = root; // traversing node
		String keyPrefix = null;

		while (true) {
			Iterator<String> itr = curr.children.keySet().iterator();

			String key2 = null;

			int commonPrefix = 0; // length of common prefix

			while (itr.hasNext()) { // loop through children
				key2 = itr.next();
				commonPrefix = RadixTree.commonPrefix(key, key2);
				if (commonPrefix > 0) {
					break;
				}
			}

			if (commonPrefix == 0) { // no match
				return false;
			}

			if (key.length() == key2.length() && key.length() == commonPrefix) { // exact match
				Node<A> del = curr.children.get(key);
				if (del instanceof WithValue<?>) {
					curr.children.remove(key); // delete node holding a value
					if (del.children.size() != 0) {
						Node<A> neW = new Node<A>();
						neW.children = del.children;
						curr.children.put(key, neW);
					} else if (curr.children.size() == 1) { // possibility to merge
						String onlyKey = curr.children.firstKey();
						if (curr instanceof WithValue<?>) {
						} else {
							prev.children.remove(keyPrefix);
							prev.children.put(keyPrefix + onlyKey, curr.children.get(onlyKey));
						}
					}
					return true;
				}

				return false;
			}

			if (commonPrefix == key2.length()) { // 'key2' is a prefix to 'key'
				prev = curr;
				curr = curr.children.get(key2);
				keyPrefix = key.substring(0, commonPrefix);
				key = key.substring(commonPrefix);
				continue;
			}

			// 'key' and 'key2' share a common prefix (including 'key' itself)
			return false;
		}
	}

	private static <A> int commonPrefix(String str1, String str2) {
		int common = 0;
		int i = -1;
		while (++i < str1.length() && i < str2.length() && str1.charAt(i) == str2.charAt(i)) {
			common++;
		}
		return common;
	}
}