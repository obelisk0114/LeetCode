package OJ0841_0850;

import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Queue;
import java.util.PriorityQueue;

public class Hand_of_Straights {
	/*
	 * https://leetcode.com/problems/hand-of-straights/discuss/135598/C%2B%2BJavaPython-O(MlogM)-Complexity
	 * 
	 * 1. Count number of different cards to a map c
	 * 2. Loop from the smallest card number.
	 * 3. Every time we meet a new card i, we cut off i to i + W - 1 from the counter.
	 * 
	 * The reverse order in range(W)[::-1], it's because if you do it in the normal 
	 * order, c[i] will become 0 first, then the rest will be all wrong. However you 
	 * can preserve c[i] in a variable first and do it in the normal order.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/hand-of-straights/discuss/135598/C++JavaPython-O(MlogM)-Complexity/192467
	 * 
	 * Other code:
	 * https://leetcode.com/problems/hand-of-straights/discuss/135700/Short-Java-solution!
	 */
	public boolean isNStraightHand_TreeMap(int[] hand, int W) {
		Map<Integer, Integer> c = new TreeMap<>();
		for (int i : hand)
			c.put(i, c.getOrDefault(i, 0) + 1);
		
		for (int it : c.keySet()) {			
			if (c.get(it) > 0) {				
				for (int i = W - 1; i >= 0; --i) {
					if (c.getOrDefault(it + i, 0) < c.get(it))
						return false;
					
					c.put(it + i, c.get(it + i) - c.get(it));
				}
			}
		}
		return true;
	}
	
	/*
	 * https://leetcode.com/problems/hand-of-straights/discuss/557287/O(n)-JAVA-solution-no-sorting-with-Explanation
	 * 
	 * We always process from start of each group, and repeat this for 
	 * (hand.length / W) groups.
	 * 
	 * (num) will be the start of a group if (num - 1) does not exist.
	 * 
	 * Once we have a start, we can start to remove (start), (start + 1), (start + 2), 
	 * ..., (start + W - 1) from our hand of cards, and repeat this for 
	 * (hand.length / W) groups.
	 */
	public boolean isNStraightHand_Queue_first(int[] hand, int W) {
		if (hand.length % W != 0)
			return false;
		if (W == 1)
			return true;

		Map<Integer, Integer> map = new HashMap<>();
		for (int i : hand)
			map.put(i, map.getOrDefault(i, 0) + 1);
		
		Queue<Integer> queue = new LinkedList<>();
		for (int i : map.keySet()) {
			if (!map.containsKey(i - 1))
				queue.offer(i);
		}
		
		while (!queue.isEmpty()) {
			int start = queue.poll();
			for (int i = start; i < start + W; i++) {
				if (!map.containsKey(i))
					return false;
				
				if (map.get(i) - 1 > 0) {
					map.put(i, map.get(i) - 1);
					
					if (!map.containsKey(i - 1))
						queue.offer(i);
				} 
				else {
					map.remove(i);
				}
			}
			
			// start, start + 1, ..., start + W - 1, start + W, start + W + 1...
			// After removing (start) to (start + W - 1), (start + W) might become
			// the head
			if (!map.containsKey(start + W - 1) && map.containsKey(start + W))
				queue.offer(start + W);
		}
		return true;
	}
	
	/*
	 * https://leetcode.com/problems/hand-of-straights/discuss/589627/Java-O(N)-solution%3A-15ms-beats-95
	 * 
	 * O(n) time with a similar approach as approach #2 for problem 659.
	 * https://leetcode.com/problems/split-array-into-consecutive-subsequences/solution/
	 */
	public boolean isNStraightHand_HashMap(int[] hand, int W) {
		Map<Integer, Integer> counter = new HashMap<>();
		for (int num : hand)
			counter.put(num, counter.getOrDefault(num, 0) + 1);
		
		for (int num : hand) {
			// pick a starting point for the sequence
			// since the array is not sorted we check if there is a previous number of
			// 'num' which has not been processed yet.
			// if there is then we skip this 'num' as it will be processed in the
			// sequence that will start at num-1 (or smaller).
			if (counter.getOrDefault(num - 1, 0) > 0 
					|| counter.getOrDefault(num, 0) == 0) {
				
				continue;
			} 
			else {
				int currNum = num;
				// while the current starting point has occurrences left
				while (counter.getOrDefault(currNum, 0) > 0) {
					int count = 0;
					int newStartingNum = -1;
					
					// build sequence of size W
					while (count < W) {
						if (counter.getOrDefault(currNum, 0) == 0) {
							return false;
						}
						
						counter.put(currNum, counter.get(currNum) - 1);
						
						// check if we need to start a new sequence with 
						// the current currNum
						if (newStartingNum == -1 && counter.get(currNum) > 0) {
							newStartingNum = currNum;
						}
						
						currNum += 1;
						count++;
					}
					currNum = (newStartingNum == -1 ? currNum : newStartingNum);
				}
			}
		}
		return true;
	}
	
	/*
	 * https://leetcode.com/problems/hand-of-straights/discuss/135598/C%2B%2BJavaPython-O(MlogM)-Complexity
	 * 
	 * 1. Count number of different cards to a map "c"
	 * 2. "Cur" represent current open straight groups.
	 * 3. In a deque "start", we record the number of opened a straight group.
	 * 4. Loop from the smallest card number.
	 * 5. return if no more open groups.
	 * 
	 * opened is the absolute number of opened groups, start is the new opened groups 
	 * compared to the last visited key. start groups grows and as long as it has W 
	 * members, opened groups should close number of opened groups that opened W ago 
	 * which is the head of the start, hence opened -= start.remove().
	 * 
	 * Rf :
	 * https://leetcode.com/problems/hand-of-straights/discuss/135598/C++JavaPython-O(MlogM)-Complexity/294621
	 */
	public boolean isNStraightHand_Queue2(int[] hand, int W) {
		Map<Integer, Integer> c = new TreeMap<>();
		for (int i : hand)
			c.put(i, c.getOrDefault(i, 0) + 1);
		
		Queue<Integer> start = new LinkedList<>();
		int last_checked = -1, opened = 0;
		
		for (int i : c.keySet()) {
			if (opened > 0 && i > last_checked + 1 || opened > c.get(i))
				return false;
			
			start.add(c.get(i) - opened);
			
			last_checked = i;
			opened = c.get(i);

			if (start.size() == W)
				opened -= start.remove();
		}
		return opened == 0;
	}
	
	/*
	 * https://leetcode.com/problems/hand-of-straights/discuss/690900/Java-simple-O(n-*-W)-loop
	 * 
	 * 1. find all number's count, min, max;
	 * 2. loop from min, remove every k set from the pool, if we find any lack or 
	 *    extra, return false;
	 *    Same as https://leetcode.com/problems/divide-array-in-sets-of-k-consecutive-numbers/discuss/690898/Java-simple-O(n-*-k)-loop
	 *    
	 * Other code:
	 * https://leetcode.com/problems/hand-of-straights/discuss/539382/Linear-Java-O(n)-Easy-to-Understand
	 */
	public boolean isPossibleDivide(int[] nums, int k) {
		int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
		if (nums.length % k != 0)
			return false;
		
		Map<Integer, Integer> map = new HashMap<>();
		for (int n : nums) {
			min = Math.min(min, n);
			max = Math.max(max, n);
			map.put(n, map.getOrDefault(n, 0) + 1);
		}
		
		// [1,1,2,2,3,3]
		// 2
		for (int i = min; i <= max; i++) {
			// lacks
			if (map.getOrDefault(i, 0) < 0)
				return false;
			
			if (map.getOrDefault(i, 0) == 0)
				continue;
			
			// extras, won't make k subset;
			if (max - i + 1 < k)
				return false;
			
			int lowest = map.get(i);
			for (int j = i; j <= i + k - 1; j++) {
				map.put(j, map.getOrDefault(j, 0) - lowest);
			}
		}
		return true;
	}
	
	/*
	 * https://leetcode.com/problems/hand-of-straights/discuss/136200/Simple-Java-solution-using-priority-queue/149280
	 * 
	 * Rf :
	 * https://leetcode.com/problems/hand-of-straights/discuss/136200/Simple-Java-solution-using-priority-queue
	 * https://leetcode.com/articles/hand-of-straights/
	 */
	public boolean isNStraightHand_TreeMap_remove(int[] hand, int W) {
		int len = hand.length;
		if (len % W != 0)
			return false;
		
		TreeMap<Integer, Integer> treeMap = new TreeMap<>();
		for (int num : hand) {
			treeMap.put(num, treeMap.getOrDefault(num, 0) + 1);
		}

		while (!treeMap.isEmpty()) {
			int first = treeMap.firstKey();
			for (int j = 1; j < W; j++) {
				int next = first + j;
				if (treeMap.containsKey(next)) {
					treeMap.put(next, treeMap.get(next) - 1);
					
					if (treeMap.get(next) == 0)
						treeMap.remove(next);
				} 
				else {
					return false;
				}
			}
			
			treeMap.put(first, treeMap.get(first) - 1);
			if (treeMap.get(first) == 0)
				treeMap.remove(first);
		}
		return true;
	}
	
	/*
	 * https://leetcode.com/problems/hand-of-straights/discuss/136200/Simple-Java-solution-using-priority-queue/226840
	 * 
	 * Since the numbers need to consecutive we immediately can figure out the next 
	 * W - 1 numbers that must occur after a given number. By using a priority queue we 
	 * can poll the smallest number and remove the next W - 1 consecutive numbers. If 
	 * any of the consecutive numbers are not in the priority queue, it implies the 
	 * hand is invalid and thus returns false.
	 * 
	 * remove() is lgN but remove(Object o) is O(N) because you need to search for 
	 * the element being removed.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/hand-of-straights/discuss/136200/Simple-Java-solution-using-priority-queue
	 * https://leetcode.com/problems/hand-of-straights/discuss/136200/Simple-Java-solution-using-priority-queue/178251
	 * 
	 * Other code:
	 * https://leetcode.com/problems/hand-of-straights/discuss/136200/Simple-Java-solution-using-priority-queue/181547
	 */
	public boolean isNStraightHand_PriorityQueue(int[] hand, int W) {
		PriorityQueue<Integer> minHeap = new PriorityQueue<>();
		for (int num : hand)
			minHeap.add(num);
		
		while (!minHeap.isEmpty()) {
			int smallest = minHeap.peek();
			for (int i = 0; i < W; i++) {
				if (!minHeap.contains(smallest + i))
					return false;
				else
					minHeap.remove(smallest + i);
			}
		}
		return true;
	}
	
	// by myself
	public boolean isNStraightHand_self(int[] hand, int W) {
        TreeMap<Integer, Integer> treemap = new TreeMap<>();
        for (int i : hand) {
            if (treemap.containsKey(i)) {
                int count = treemap.get(i);
                treemap.put(i, count + 1);
            }
            else {
                treemap.put(i, 1);
            }
        }
        
        for (Map.Entry<Integer, Integer> entry : treemap.entrySet()) {
            int key = entry.getKey();
            int val = entry.getValue();
            
            if (val > 0) {
                for (int i = key + 1; i < key + W; i++) {
                    if (treemap.containsKey(i)) {
                        int future = treemap.get(i);
                        if (future < val) {
                            return false;
                        }
                        
                        treemap.put(i, future - val);
                    }
                    else {
                        return false;
                    }
                }
            }
        }
        return true;
    }
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/hand-of-straights/discuss/611940/Python-Solution-O(NlogN-%2B-NW)-with-explanations
     * https://leetcode.com/problems/hand-of-straights/discuss/135655/Python-O(nlgn)-simple-solution-with-intuition
     * https://leetcode.com/problems/hand-of-straights/discuss/135600/short-and-clear-Python-solution-%2B-B
     * https://leetcode.com/problems/hand-of-straights/discuss/137794/Python-true-O(N)-solution
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/hand-of-straights/discuss/641984/Simple-C%2B%2B-solution
     * https://leetcode.com/problems/hand-of-straights/discuss/547092/Simple-cpp-solution-for-beginners.-O(N2)-time-complexity-with-detailed-explanation.
     */
	
	/**
	 * JavaScript collections
	 * 
	 * https://leetcode.com/problems/hand-of-straights/discuss/189815/Readable-Javascript-Solution
	 * https://leetcode.com/problems/hand-of-straights/discuss/680582/JavaScript-functional-programming-beats-97-runtime
	 */

}
