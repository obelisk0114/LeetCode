package OJ0551_0560;

import java.util.HashMap;

public class Subarray_Sum_Equals_K {
	/*
	 * https://leetcode.com/articles/subarray-sum-equals-k/
	 * 
	   We make use of a hashmap which is used to store the cumulative sum 
	   up to all the indices possible along with the number of times 
	   the same sum occurs.
	   
	   Every time we encounter a new sum, we make a new entry in the hashmap 
	   corresponding to that sum. 
	   If the same sum occurs again, we increment the count corresponding to that sum 
	   in the hashmap.
	 * 
	 * Rf : https://discuss.leetcode.com/topic/87850/java-solution-presum-hashmap
	 * 
	   Each time you find a sum that the hash map contains (sum - k), 
	   this means (sum - k) is a PreSum for a array some previous elements, 
	   and there could be more than one of them, 
	   so use count += map.get(sum - k) instead of count++.
	   
	   map.put(0,1); because the sum of first 0 number is 0 
	   and this is an empty array[], which is also a subarray for nums. 
	   In other words, the number of time sum = 0 exists is 1.
	 */
	public int subarraySum(int[] nums, int k) {
		int count = 0, sum = 0;
		HashMap<Integer, Integer> map = new HashMap<>();
		map.put(0, 1);
		for (int i = 0; i < nums.length; i++) {
			sum += nums[i];
			if (map.containsKey(sum - k))
				count += map.get(sum - k);
			map.put(sum, map.getOrDefault(sum, 0) + 1);
		}
		return count;
	}
	
	/*
	 * https://leetcode.com/articles/subarray-sum-equals-k/
	 * 
	 * Approach #3 Without space
	 */
	public int subarraySum_start_end(int[] nums, int k) {
		int count = 0;
		for (int start = 0; start < nums.length; start++) {
			int sum = 0;
			for (int end = start; end < nums.length; end++) {
				sum += nums[end];
				if (sum == k)
					count++;
			}
		}
		return count;
    }
	
	/*
	 * https://leetcode.com/articles/subarray-sum-equals-k/
	 * 
	 * In order to calculate the sum of elements lying between two indices, 
	 * we can subtract the cumulative sum corresponding to the two indices 
	 * to obtain the sum directly, instead of iterating over the subarray 
	 * to obtain the sum.
	 * 
	 * Approach #2 Using Cummulative sum
	 */
	public int subarraySum_Cummulative_sum(int[] nums, int k) {
		int count = 0;
		int[] sum = new int[nums.length + 1];
		sum[0] = 0;
		for (int i = 1; i <= nums.length; i++)
			sum[i] = sum[i - 1] + nums[i - 1];
		for (int start = 0; start < nums.length; start++) {
			for (int end = start + 1; end <= nums.length; end++) {
				if (sum[end] - sum[start] == k)
					count++;
			}
		}
		return count;
	}

}
