package OJ0211_0220;

import java.util.TreeSet;
import java.util.Map;
import java.util.HashMap;

public class Contains_Duplicate_III {
	/*
	 * https://discuss.leetcode.com/topic/15199/ac-o-n-solution-in-java-using-buckets-with-explanation
	 * 
	 * No sub Integer.Min: assign both -2 and +2 to bucket 0 if the bucket size is > 2
	 * 
	 * Rf : https://discuss.leetcode.com/topic/15199/ac-o-n-solution-in-java-using-buckets-with-explanation/10
	 */
	public boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
		if (k < 1 || t < 0)
			return false;
		Map<Long, Long> map = new HashMap<>();
		for (int i = 0; i < nums.length; i++) {
			long remappedNum = (long) nums[i] - Integer.MIN_VALUE;
			long bucket = remappedNum / ((long) t + 1);
			if (map.containsKey(bucket) 
					|| (map.containsKey(bucket - 1) && remappedNum - map.get(bucket - 1) <= t)
					|| (map.containsKey(bucket + 1) && map.get(bucket + 1) - remappedNum <= t))
				return true;
			if (map.size() >= k) {
				long lastBucket = ((long) nums[i - k] - Integer.MIN_VALUE) / ((long) t + 1);
				map.remove(lastBucket);
			}
			map.put(bucket, remappedNum);
		}
		return false;
	}
	
	// https://discuss.leetcode.com/topic/28928/easy-ac-solution-using-treeset-long-in-java
	boolean containsNearbyAlmostDuplicate_TreeSet_Subset(int[] nums, int k, int t) {
		if (nums == null || nums.length == 0)
			return false;
		TreeSet<Long> set = new TreeSet<>();
		set.add((long) nums[0]);
		for (int i = 1; i < nums.length; i++) {
			if (i > k)
				set.remove((long) nums[i - k - 1]);
			long left = (long) nums[i] - t;
			long right = (long) nums[i] + t;
			if (left <= right && !set.subSet(left, right + 1).isEmpty())
				return true;
			set.add((long) nums[i]);
		}
		return false;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/15191/java-o-n-lg-k-solution
	 * 
	 * Rf :
	 * https://discuss.leetcode.com/topic/19672/java-treeset-implementation-nlogk
	 * https://discuss.leetcode.com/topic/15174/java-solution-with-treeset
	 */
	boolean containsNearbyAlmostDuplicate_TreeSet_floor(int[] nums, int k, int t) {
		if (nums == null || nums.length == 0 || k <= 0) {
			return false;
		}

		final TreeSet<Long> values = new TreeSet<>();
		for (int ind = 0; ind < nums.length; ind++) {

			final Long floor = values.floor((long) nums[ind] + t);
			final Long ceil = values.ceiling((long) nums[ind] - t);
			if ((floor != null && floor >= (long) nums[ind]) || (ceil != null && ceil <= (long) nums[ind])) {
				return true;
			}

			values.add((long) nums[ind]);
			if (ind >= k) {
				values.remove((long) nums[ind - k]);
			}
		}

		return false;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/21705/ac-java-solution-without-set-or-dictionary-sort-the-nums-and-record-the-positions
	 * 
	 * Need to use "long" type to deal with overflow
	 */
	
	public static void main(String[] args) {
		Contains_Duplicate_III contains = new Contains_Duplicate_III();
//		int[] nums = {3,6,0,4};
//		int k = 2;
//		int t = 2;
		int[] nums = {0,2147483647};
		int k = 1;
		int t = 2147483647;
		System.out.println(contains.containsNearbyAlmostDuplicate(nums, k, t));
	}

}
