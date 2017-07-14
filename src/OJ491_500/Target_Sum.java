package OJ491_500;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

public class Target_Sum {
	/*
	 * The following variable and 2 functions are by myself.
	 * Rf : https://discuss.leetcode.com/topic/76245/java-simple-dfs-with-memorization
	 */
	Map<String, Integer> Self_map = new HashMap<>();
    public int findTargetSumWays_map(int[] nums, int S) {
        return calculate_map(nums, 0, 0, S);
    }
    public int calculate_map(int[] nums, int i, int sum, int S) {
        String s = i + "_" + sum;
        if (i == nums.length) {
            if (sum == S) {
                return 1;
            }
            else {
                return 0;
            }
        } else {
            if (Self_map.containsKey(s)) {
                return Self_map.get(s);
            }
            int add = calculate_map(nums, i + 1, sum + nums[i], S);
            int subtract = calculate_map(nums, i + 1, sum - nums[i], S);
            int cur = add + subtract;
            Self_map.put(s, cur);
            return cur;
        }
    }
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/articles/target-sum/
	 * 
	 * The sum of elements in the given array will not exceed 1000.
	 * 
	 * Since sum may be negative.
	 * The factor of 1000 has been added as an offset to the sum value to 
	 * map all the sums possible to positive integer range.
	 */
	int count_memory = 0;
    public int findTargetSumWays_memory(int[] nums, int S) {
        int[][] memo = new int[nums.length][2001];
        for (int[] row: memo)
            Arrays.fill(row, Integer.MIN_VALUE);
        return calculate_memory(nums, 0, 0, S, memo);
    }
    public int calculate_memory(int[] nums, int i, int sum, int S, int[][] memo) {
        if (i == nums.length) {
            if (sum == S)
                return 1;
            else
                return 0;
        } else {
            if (memo[i][sum + 1000] != Integer.MIN_VALUE) {
                return memo[i][sum + 1000];
            }
            int add = calculate_memory(nums, i + 1, sum + nums[i], S, memo);
            int subtract = calculate_memory(nums, i + 1, sum - nums[i], S, memo);
            memo[i][sum + 1000] = add + subtract;
            return memo[i][sum + 1000];
        }
    }
    
    // https://discuss.leetcode.com/topic/76264/short-java-dp-solution-with-explanation
	public int findTargetSumWays(int[] nums, int s) {
		int sum = 0;
		for (int i : nums)
			sum += i;
		if (s > sum || s < -sum)
			return 0;
		int[] dp = new int[2 * sum + 1];
		dp[0 + sum] = 1;
		for (int i = 0; i < nums.length; i++) {
			int[] next = new int[2 * sum + 1];
			for (int k = 0; k < 2 * sum + 1; k++) {
				if (dp[k] != 0) {
					next[k + nums[i]] += dp[k];
					next[k - nums[i]] += dp[k];
				}
			}
			dp = next;
		}
		return dp[sum + s];
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/articles/target-sum/
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/76200/my-java-solution
	 * https://discuss.leetcode.com/topic/76201/java-short-dfs-solution
	 */
	int count_brute = 0;
    public int findTargetSumWays_brute(int[] nums, int S) {
        calculate_brute(nums, 0, 0, S);
        return count_brute;
    }
    public void calculate_brute(int[] nums, int i, int sum, int S) {
        if (i == nums.length) {
            if (sum == S)
                count_brute++;
        } else {
            calculate_brute(nums, i + 1, sum + nums[i], S);
            calculate_brute(nums, i + 1, sum - nums[i], S);
        }
    }
    
    /*
     * The following variable and 2 functions are from this link.
     * https://discuss.leetcode.com/topic/76201/java-short-dfs-solution
     * 
     * If the sum of all elements left is smaller than absolute value of target, 
     * there will be no answer following the current path. Thus we can return.
     */
	int result = 0;
	public int findTargetSumWays2(int[] nums, int S) {
		if (nums == null || nums.length == 0)
			return result;

		int n = nums.length;
		int[] sums = new int[n];
		sums[n - 1] = nums[n - 1];
		for (int i = n - 2; i >= 0; i--)
			sums[i] = sums[i + 1] + nums[i];

		helper(nums, sums, S, 0);
		return result;
	}
	public void helper(int[] nums, int[] sums, int target, int pos) {
		if (pos == nums.length) {
			if (target == 0)
				result++;
			return;
		}

		if (sums[pos] < Math.abs(target))
			return;

		helper(nums, sums, target + nums[pos], pos + 1);
		helper(nums, sums, target - nums[pos], pos + 1);
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/76243/java-15-ms-c-3-ms-o-ns-iterative-dp-solution-using-subset-sum-with-explanation
	 */
	public int findTargetSumWays_subset(int[] nums, int s) {
		int sum = 0;
		for (int n : nums)
			sum += n;
		return sum < s || (s + sum) % 2 > 0 ? 0 : subsetSum(nums, (s + sum) >>> 1);
	}
	public int subsetSum(int[] nums, int s) {
		int[] dp = new int[s + 1];
		dp[0] = 1;
		for (int n : nums)
			for (int i = s; i >= n; i--)
				dp[i] += dp[i - n];
		return dp[s];
	}

}
