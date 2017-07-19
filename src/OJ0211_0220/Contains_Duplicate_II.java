package OJ0211_0220;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class Contains_Duplicate_II {
	// https://discuss.leetcode.com/topic/25491/short-ac-java-solution
	public boolean containsNearbyDuplicate(int[] nums, int k) {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (int i = 0; i < nums.length; i++) {
			if (map.containsKey(nums[i])) {
				if (i - map.get(nums[i]) <= k)
					return true;
			}
			map.put(nums[i], i);
		}
		return false;
	}

	/*
	 * https://discuss.leetcode.com/topic/15305/simple-java-solution
	 * 
	 * Rf :
	 * https://discuss.leetcode.com/topic/15162/java-solution-using-set-and-sliding-window
	 * https://discuss.leetcode.com/topic/34103/short-hashset-solution
	 */
	public boolean containsNearbyDuplicate_slidingWindow(int[] nums, int k) {
		Set<Integer> set = new HashSet<Integer>();
		for (int i = 0; i < nums.length; i++) {
			if (i > k)
				set.remove(nums[i - k - 1]);
			if (!set.add(nums[i]))
				return true;
		}
		return false;
	}

	/*
	 * Other Map trick
	 * 
	 * https://discuss.leetcode.com/topic/15095/only-one-test-per-element
	 * https://discuss.leetcode.com/topic/15250/java-solution-using-hashmap-s-put
	 */

}
