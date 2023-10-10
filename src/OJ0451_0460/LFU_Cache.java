package OJ0451_0460;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeSet;
import java.util.LinkedHashSet;
import java.util.Comparator;

/*
 * The following class and methods are from this links.
 * https://leetcode.com/problems/lfu-cache/discuss/94660/Short-Java-O(1)-solution-using-LinkedHashMap-and-HashMap-with-explaination
 * 
 * The basic idea is to keep two maps:
 * 1. keyToFreq: key -> frequency
 * 2. freqToEntry: frequency -> (key,value) pairs with the same frequency, 
 *                 in LRU order (using LinkedHashMap)
 * 
 * Another important idea is to store and update the current minimum frequency. When it 
 * reaches the capacity, I remove the oldest entry from freqToEntry.get(min), and the 
 * next minimum frequency will be 1.
 * 
 * Rf : https://yikun.github.io/2015/04/02/Java-LinkedHashMap%E5%B7%A5%E4%BD%9C%E5%8E%9F%E7%90%86%E5%8F%8A%E5%AE%9E%E7%8E%B0/
 */
class LFUCache_LinkedHashMap {

	Map<Integer, LinkedHashMap<Integer, Integer>> freqToEntry;
	Map<Integer, Integer> keyToFreq;
	int capacity;
	int min;

	public LFUCache_LinkedHashMap(int capacity) {
		freqToEntry = new HashMap<>();
		keyToFreq = new HashMap<>();
		this.capacity = capacity;
		this.min = 0;
	}

	public int get(int key) {
		if (!keyToFreq.containsKey(key))
			return -1;

		int freq = keyToFreq.get(key);
		int value = freqToEntry.get(freq).get(key);
		keyToFreq.put(key, freq + 1);
		freqToEntry.get(freq).remove(key);
		freqToEntry.computeIfAbsent(freq + 1, x -> new LinkedHashMap<>()).put(key, value);
		if (freq == min && freqToEntry.get(freq).size() == 0)
			min = min + 1;
		return value;
	}

	public void put(int key, int value) {
		if (capacity == 0)
			return;
		if (keyToFreq.containsKey(key)) {
			int freq = keyToFreq.get(key);
			keyToFreq.put(key, freq + 1);
			freqToEntry.get(freq).remove(key);
			freqToEntry.computeIfAbsent(freq + 1, x -> new LinkedHashMap<>()).put(key, value);
			if (freq == min && freqToEntry.get(freq).size() == 0)
				min = min + 1;
		} 
		else {
			if (keyToFreq.size() == capacity)
				removeOldest();
			keyToFreq.put(key, 1);
			freqToEntry.computeIfAbsent(1, x -> new LinkedHashMap<>()).put(key, value);
			min = 1;
		}
	}

	private void removeOldest() {
		int rmKey = freqToEntry.get(min).keySet().iterator().next();
		keyToFreq.remove(rmKey);
		freqToEntry.get(min).remove(rmKey);
	}
}

/*
 * The following class and methods are from this links.
 * https://leetcode.com/problems/lfu-cache/discuss/94521/JAVA-O(1)-very-easy-solution-using-3-HashMaps-and-LinkedHashSet
 * 
 * 1. the least recently+frequently used value to be removed is the first element in 
 *    LinkedHashSet with the lowest count/frequency.
 * 2. min is used to track the group of elements with least frequency
 * 3. lists maps frequency to groups, each element in same group has the same count.
 * 
 * min will always reset to 1 when adding a new item. min can never jump forward more 
 * than 1 since updating an item only increments it's count by 1.
 * 
 * Rf :
 * leetcode.com/problems/lfu-cache/discuss/94521/JAVA-O(1)-very-easy-solution-using-3-HashMaps-and-LinkedHashSet/99084
 * leetcode.com/problems/lfu-cache/discuss/94521/JAVA-O(1)-very-easy-solution-using-3-HashMaps-and-LinkedHashSet/99074
 * 
 * Other code : 
 * https://leetcode.com/problems/lfu-cache/discuss/94515/Java-O(1)-Accept-Solution-Using-HashMap-DoubleLinkedList-and-LinkedHashSet
 */
class LFUCache {
	HashMap<Integer, Integer> vals;
	HashMap<Integer, Integer> counts;
	HashMap<Integer, LinkedHashSet<Integer>> lists;
	int cap;
	int min = -1;

	public LFUCache(int capacity) {
		cap = capacity;
		vals = new HashMap<>();
		counts = new HashMap<>();
		lists = new HashMap<>();
		lists.put(1, new LinkedHashSet<>());
	}

	public int get(int key) {
		if (!vals.containsKey(key))
			return -1;
		int count = counts.get(key);
		counts.put(key, count + 1);
		lists.get(count).remove(key);
		if (count == min && lists.get(count).size() == 0)
			min++;
		if (!lists.containsKey(count + 1))
			lists.put(count + 1, new LinkedHashSet<>());
		lists.get(count + 1).add(key);
		return vals.get(key);
	}

	public void put(int key, int value) {
		if (cap <= 0)
			return;
		if (vals.containsKey(key)) {
			vals.put(key, value);
			get(key);
			return;
		}
		
		if (vals.size() >= cap) {
			int evit = lists.get(min).iterator().next();
			lists.get(min).remove(evit);
			vals.remove(evit);
			counts.remove(evit);
		}
		vals.put(key, value);
		counts.put(key, 1);
		min = 1;
		lists.get(1).add(key);
	}
}

/*
 * The following classes and methods are from this links.
 * https://leetcode.com/problems/lfu-cache/discuss/94547/Java-O(1)-Solution-Using-Two-HashMap-and-One-DoubleLinkedList
 */
public class LFU_Cache {
	class Node {
		int key, val, cnt;
		Node prev, next;

		Node(int key, int val) {
			this.key = key;
			this.val = val;
			cnt = 1;
		}
	}

	class DLList {
		Node head, tail;
		int size;

		DLList() {
			head = new Node(0, 0);
			tail = new Node(0, 0);
			head.next = tail;
			tail.prev = head;
		}

		void add(Node node) {
			head.next.prev = node;
			node.next = head.next;
			node.prev = head;
			head.next = node;
			size++;
		}

		void remove(Node node) {
			node.prev.next = node.next;
			node.next.prev = node.prev;
			size--;
		}

		Node removeLast() {
			if (size > 0) {
				Node node = tail.prev;
				remove(node);
				return node;
			} 
			else
				return null;
		}
	}

	int capacity, size, min;
	Map<Integer, Node> nodeMap;        // key to node
	Map<Integer, DLList> countMap;     // count to DLList

	public LFU_Cache(int capacity) {
		this.capacity = capacity;
		nodeMap = new HashMap<>();
		countMap = new HashMap<>();
	}

	public int get(int key) {
		Node node = nodeMap.get(key);
		if (node == null)
			return -1;
		update(node);
		return node.val;
	}

	public void put(int key, int value) {
		if (capacity == 0)
			return;
		
		Node node;
		if (nodeMap.containsKey(key)) {
			node = nodeMap.get(key);
			node.val = value;
			update(node);
		} 
		else {
			node = new Node(key, value);
			nodeMap.put(key, node);
			if (size == capacity) {
				DLList lastList = countMap.get(min);
				nodeMap.remove(lastList.removeLast().key);
				size--;
			}
			size++;
			min = 1;
			DLList newList = countMap.getOrDefault(1, new DLList());
			newList.add(node);
			countMap.put(1, newList);
		}
	}

	private void update(Node node) {
		DLList oldList = countMap.get(node.cnt);
		oldList.remove(node);
		if (node.cnt == min && oldList.size == 0)
			min++;
		node.cnt++;
		DLList newList = countMap.getOrDefault(node.cnt, new DLList());
		newList.add(node);
		countMap.put(node.cnt, newList);
	}
}

/*
 * The following classes and methods are from this links.
 * https://leetcode.com/problems/lfu-cache/discuss/94536/Java-solution-using-PriorityQueue-with-detailed-explanation
 * 
 * The trick is, just override equals(), in order to use remove.
 * 
 * Rf :
 * https://leetcode.com/problems/lfu-cache/discuss/94657/Java-solutions-of-three-different-ways.-PriorityQueue-:-O(capacity)-TreeMap-:-O(log(capacity))-DoubleLinkedList-:-O(1)
 * leetcode.com/problems/lfu-cache/discuss/94536/Java-solution-using-PriorityQueue-with-detailed-explanation/99094
 * leetcode.com/problems/lfu-cache/discuss/94536/Java-solution-using-PriorityQueue-with-detailed-explanation/99098
 */
class LFUCache_TreeSet {
    
	// a class to remember frequency and recentness
	class Cache{
		int key, value, freq, recent;

		public Cache(int key, int value, int freq, int recent) {
			this.key = key;
			this.value = value;
			this.freq = freq;
			this.recent = recent;
		}

		// override equals()
		public boolean equals(Object object) {
			if (!(object instanceof Cache))
				return false;
			return key == ((Cache) object).key;
		}
		
		/*
		public int hashCode() {
			return key;
		}

		// sort by frequency and recentness
		public int compareTo(Cache o) {
			return key == o.key ? 0 : freq == o.freq ? recent - o.recent : freq - o.freq;
		}
		*/
	}

	int capacity, id;
	HashMap<Integer, Cache> caches;
	TreeSet<Cache> treeSet;

	public LFUCache_TreeSet(int capacity) {
		this.capacity = capacity;
		id = 0;
		caches = new HashMap<>();
		treeSet = new TreeSet<>(new Comparator<Cache>() {
			public int compare(Cache c1, Cache c2) {
				int res = c1.freq == c2.freq ? c1.recent - c2.recent : c1.freq - c2.freq;
				return res;
			}
		});
	}

	public int get(int key) {
		if (!caches.containsKey(key)) {
			return -1;
		}
		
		id++;
		int result = caches.get(key).value;
		update(key, result);
		return result;
	}

	public void put(int key, int value) {
		if (capacity < 1)
			return;
		
		id++;
		if (caches.containsKey(key)) {
			update(key, value);
			return;
		}
		
		if (caches.size() == capacity) {
			Cache first = treeSet.pollFirst(); // find the smallest one, and remove it
			caches.remove(first.key);
		}
		Cache cur = new Cache(key, value, 1, id);
		caches.put(key, cur);
		treeSet.add(cur);
	}

	// update the TreeSet
	private void update(int key, int value) {
		Cache old = caches.get(key);
		Cache cur = new Cache(key, value, old.freq + 1, id);
		treeSet.remove(old);
		treeSet.add(cur);
		caches.put(key, cur);
	}

}

/*
 * By myself2
 * https://leetcode.com/submissions/detail/159856896/
 * 
 * By myself
 * https://leetcode.com/submissions/detail/159840972/
 */

/**
 * Your LFUCache object will be instantiated and called as such:
 * LFUCache obj = new LFUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */
