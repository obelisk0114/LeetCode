package OJ0731_0740;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;

public class Delete_and_Earn {
	/*
	 * https://leetcode.com/problems/delete-and-earn/discuss/109891/Sharing-my-Simple-Straight-Forward-Java-O(n)-Solution-Explanation-Included
	 * 
	 * At each step, your sum can either depend on your previous sum or the prior 
	 * plus the current.
	 * 
	 * Other code:
	 * https://leetcode.com/problems/delete-and-earn/discuss/109889/Java-Easy-DP-Solution
	 */
	public int deleteAndEarn(int[] nums) {
		int[] sum = new int[10001];

		for (int num : nums) {
			sum[num] += num;
		}

		for (int i = 2; i < sum.length; i++) {
			sum[i] = Math.max(sum[i - 1], sum[i - 2] + sum[i]);
		}
		return sum[10000];
	}
	
	/*
	 * leetcode.com/problems/delete-and-earn/discuss/109895/JavaC++-Clean-Code-with-Explanation/111626
	 * 
	 * Rf : https://leetcode.com/articles/delete-and-earn/
	 */
	public int deleteAndEarn_TreeMap(int[] nums) {
		final Map<Integer, Integer> values = new TreeMap<>();
		for (final int num : nums) {
			values.put(num, values.getOrDefault(num, 0) + num);
		}
		
		int pre = 0, cur = 0;
		for (final int num : values.keySet()) {
			if (!values.containsKey(num - 1)) {
				pre = cur;
				cur += values.get(num);
			} 
			else {
				final int temp = Math.max(pre + values.get(num), cur);
				pre = cur;
				cur = temp;
			}
		}
		return cur;
	}
	
	// https://leetcode.com/problems/delete-and-earn/discuss/109899/Consice-Java
	public int deleteAndEarn_HashMap(int[] nums) {
		if (nums == null || nums.length == 0)
			return 0;
		
		int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
		Map<Integer, Integer> map = new HashMap<>();
		for (int num : nums) {
			min = Math.min(min, num);
			max = Math.max(max, num);
			map.put(num, map.getOrDefault(num, 0) + 1);
		}
		
		int n = max - min + 1;
		int[] sums = new int[n];
		for (int i = 0; i < n; i++) {
			sums[i] = (i + min) * (map.getOrDefault(i + min, 0));
		}

		for (int i = 1; i < n; i++) {
			if (i == 1)     // Prevent nums.length = 1
				sums[i] = Math.max(sums[0], sums[1]);
			else
				sums[i] = Math.max(sums[i] + sums[i - 2], sums[i - 1]);
		}
		return sums[n - 1];
	}
	
	/*
	 * https://leetcode.com/problems/delete-and-earn/discuss/109895/JavaC++-Clean-Code-with-Explanation
	 * 
	 * 1. If we sort all the numbers into buckets indexed by these numbers, this is 
	 *    essentially asking you to repetitively take an bucket while giving up the 
	 *    2 buckets next to it.
	 * 2. The optimal final result can be derived by keep updating 2 variables 
	 *    skip_i, take_i, which stands for:
	 * skip_i : the best result for sub-problem of first (i+1) buckets from 0 to i, 
	 *          while you skip the ith bucket.
	 * take_i : the best result for sub-problem of first (i+1) buckets from 0 to i, 
	 *          while you take the ith bucket.
	 * 3. take[i] = skip[i-1] + values[i];
	 *    skip[i] = Math.max(skip[i-1], take[i-1]);
	 * take[i] can only be derived from: if you skipped the [i-1]th bucket, 
	 * and you take bucket[i].
	 * skip[i] through, can be derived from either take[i-1] or skip[i-1], 
	 * whatever the bigger; 
	 * 
	 * Rf : leetcode.com/problems/delete-and-earn/discuss/109895/JavaC++-Clean-Code-with-Explanation/111626
	 */
	public int deleteAndEarn_take_skip(int[] nums) {
		int n = 10001;
		int[] values = new int[n];
		for (int num : nums)
			values[num] += num;

		int take = 0, skip = 0;
		for (int i = 0; i < n; i++) {
			int takei = skip + values[i];
			int skipi = Math.max(skip, take);
			take = takei;
			skip = skipi;
		}
		return Math.max(take, skip);
	}
	
	/*
	 * https://leetcode.com/problems/delete-and-earn/discuss/120185/Rob-House-Questions
	 * 
	 * first of all we sum each num separately to its position, 
	 * then it turns to simple rob house question, odd and even
	 * 
	 * Rf : https://leetcode.com/problems/delete-and-earn/discuss/109871/Awesome-Python-4-liner-with-explanation-Reduce-to-House-Robbers-Question
	 */
	public int deleteAndEarn_odd_even(int[] nums) {
		if (nums.length == 0)
			return 0;

		int length = nums[0];
		for (int i : nums) {
			length = Math.max(length, i);
		}
		
		int[] cnter = new int[length + 1];
		for (int i : nums)
			cnter[i] += i;

		int even = 0, odd = 0;
		for (int i = 0; i < cnter.length; i++) {
			if (i % 2 == 0)
				even = Math.max(odd, even + cnter[i]);
			else
				odd = Math.max(even, odd + cnter[i]);
		}
		return Math.max(even, odd);
	}
	
	// https://leetcode.com/problems/delete-and-earn/discuss/109886/Java-Time-complexity-O(n-log-n)-with-O(1)-space
	public int deleteAndEarn_sort(int[] nums) {
		Arrays.sort(nums);
		int i = 0;
		int cur = 0;
		int prev = 0;
		int prev2 = 0;
		int startIndex = 0;

		while (i < nums.length) {
			cur = nums[i];
			startIndex = i - 1;
			while (i < nums.length - 1 && nums[i] == nums[i + 1])
				cur += nums[i++];

			if (startIndex >= 0 && nums[i] > nums[startIndex] + 1)
				cur += prev;
			else
				cur = Math.max(cur + prev2, prev);

			prev2 = prev;
			prev = cur;
			i++;
		}
		return cur;
	}

}
