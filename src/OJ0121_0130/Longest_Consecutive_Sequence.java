package OJ0121_0130;

import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;

public class Longest_Consecutive_Sequence {
	/*
	 * https://leetcode.com/problems/longest-consecutive-sequence/discuss/41067/Simple-fast-Java-solution-using-Set
	 * 
	 * The basic idea is put all integers into a set. Iterate all the integers and for 
	 * every integer try to find its consecutive numbers in the set and accumulate the 
	 * length. The trick is remove the integer whenever it has been visited, which 
	 * makes the process O(n) because every integer will only be visited once.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/longest-consecutive-sequence/discuss/41126/One-Java-solution
	 * https://leetcode.com/problems/longest-consecutive-sequence/discuss/41057/Simple-O(n)-with-Explanation-Just-walk-each-streak
	 * 
	 * Other code:
	 * https://leetcode.com/problems/longest-consecutive-sequence/discuss/41217/9LOC-Java-O(n)-solution
	 * https://leetcode.com/problems/longest-consecutive-sequence/discuss/41130/Another-accepted-Java-O(n)-solution
	 */
	public int longestConsecutive(int[] nums) {
		if (nums == null || nums.length == 0)
			return 0;

		Set<Integer> set = new HashSet<Integer>();
		for (int num : nums)
			set.add(num);
		
		int max = 1;
		for (int num : nums) {
			if (set.remove(num)) {         // num hasn't been visited
				int sum = 1;
				
				// look left
				int val = num;
				while (set.remove(val - 1)) {
					val--;
					sum++;
				}

				// look right
				val = num;
				while (set.remove(val + 1)) {
					val++;
					sum++;
				}

				max = Math.max(max, sum);
				
				/*    Early break
				if (set.isEmpty())
					break;
				*/
			}
		}
		return max;
	}
	
	/*
	 * https://leetcode.com/problems/longest-consecutive-sequence/discuss/41055/My-really-simple-Java-O(n)-solution-Accepted
	 * 
	 * The key thing is to keep track of the sequence length and store that in the 
	 * boundary points of the sequence. For example, as a result, for sequence 
	 * {1, 2, 3, 4, 5}, map.get(1) and map.get(5) should both return 5.
	 * 
	 * Whenever a new element n is inserted into the map, do two things:
	 * 
	 * 1. See if n - 1 and n + 1 exist in the map, and if so, it means there is an 
	 *    existing sequence next to n. Variables left and right will be the length 
	 *    of those two sequences, while 0 means there is no sequence and n will be 
	 *    the boundary point later. Store (left + right + 1) as the associated value 
	 *    to key n into the map.
	 * 2. Use left and right to locate the other end of the sequences to the left and 
	 *    right of n respectively, and replace the value with the new length.
	 * 
	 * Rf : 
	 * leetcode.com/problems/longest-consecutive-sequence/discuss/41055/My-really-simple-Java-O(n)-solution-Accepted/39084
	 * leetcode.com/problems/longest-consecutive-sequence/discuss/41055/My-really-simple-Java-O(n)-solution-Accepted/39058
	 * leetcode.com/problems/longest-consecutive-sequence/discuss/41055/My-really-simple-Java-O(n)-solution-Accepted/39119
	 * https://leetcode.com/problems/longest-consecutive-sequence/discuss/41088/Possibly-shortest-cpp-solution-only-6-lines.
	 * 
	 * Other code:
	 * https://leetcode.com/problems/longest-consecutive-sequence/discuss/41141/O(n)-HashMap-Java-Solution
	 * leetcode.com/problems/longest-consecutive-sequence/discuss/41055/My-really-simple-Java-O(n)-solution-Accepted/39096
	 */
	public int longestConsecutive_HashMap(int[] num) {
		int res = 0;
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (int n : num) {
			if (!map.containsKey(n)) {
				int left = (map.containsKey(n - 1)) ? map.get(n - 1) : 0;
				int right = (map.containsKey(n + 1)) ? map.get(n + 1) : 0;
				// sum: length of the sequence n is in
				int sum = left + right + 1;
				map.put(n, sum);

				// keep track of the max length
				res = Math.max(res, sum);

				// extend the length to the boundary(s)
				// of the sequence
				// will do nothing if n has no neighbors
				map.put(n - left, sum);
				map.put(n + right, sum);
			} 
			else {
				// duplicates
				continue;
			}
		}
		return res;
	}

	// https://leetcode.com/problems/longest-consecutive-sequence/discuss/41290/Simple-Java-Solution-Using-HashMap
	public int longestConsecutive_HashMap2(int[] nums) {
		if (nums == null || nums.length == 0)
			return 0;

		int longest = 1;
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (int i = 0; i < nums.length; i++)
			map.put(nums[i], nums[i] + 1);

		for (int j = 0; j < nums.length; j++) {
			int current = 1;
			int key = nums[j];
			if (map.containsKey(key - 1))
				continue;
			
			while (map.containsKey(map.get(key))) {
				current++;
				key = map.get(key);
			}
			
			if (current > longest)
				longest = current;
		}
		return longest;
	}
	
	// https://leetcode.com/problems/longest-consecutive-sequence/discuss/41062/My-Java-Solution-using-UnionFound
	
	// https://leetcode.com/articles/longest-consecutive-sequence/
	public int longestConsecutive_sort(int[] nums) {
        if (nums == null || nums.length == 0)
            return 0;
        
        Arrays.sort(nums);
        
        int max = 1;
        int cur = 1;
        
        // If the current number and the previous are equal, then our current 
        // sequence is neither extended nor broken, so we simply move on to the 
        // next number.
        for (int i = 0; i < nums.length - 1; i++) {
            if (nums[i] != nums[i + 1]) {
                if (nums[i + 1] != nums[i] + 1) {
                	cur = 1;
                }
                else {
                	cur++;
                	max = Math.max(max, cur);
                }
            }
        }
        return max;
    }

}
