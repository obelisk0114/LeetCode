package OJ411_420;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Partition_Equal_Subset_Sum {
	/*
	 * https://discuss.leetcode.com/topic/67539/0-1-knapsack-detailed-explanation
	 * 
	 * Calculating the sum of all members in the array, 
	 * and find if is there any subsets of the array has sum equal to sum/2
	 * 
	 * dp[i][j] : whether the specific sum j can be gotten from the first i numbers.
	 * dp[n][W] = dp[n - 1][W - a[n-1]] || dp[n - 1][W]
	 * we pick it, j is composed of the current value nums[i] 
	 * and the remaining composed of other previous numbers. || we don't pick it
	 * 
	 * Rf : https://discuss.leetcode.com/topic/62732/java-dynamic-programming-fastest
	 */
	public boolean canPartition(int[] nums) {
		int sum = 0;
		for (int num : nums) {
			sum += num;
		}

		if ((sum & 1) == 1) {
			return false;
		}
		sum /= 2;

		int n = nums.length;
		boolean[][] dp = new boolean[n + 1][sum + 1];
		for (int i = 0; i < dp.length; i++) {
			Arrays.fill(dp[i], false);
		}

		dp[0][0] = true;

		for (int i = 1; i < n + 1; i++) {
			dp[i][0] = true;
		}
		for (int j = 1; j < sum + 1; j++) {
			dp[0][j] = false;
		}

		for (int i = 1; i < n + 1; i++) {
			for (int j = 1; j < sum + 1; j++) {
				if (j >= nums[i - 1]) {
					dp[i][j] = (dp[i - 1][j] || dp[i - 1][j - nums[i - 1]]);
				} else {
					dp[i][j] = dp[i - 1][j];
				}
			}
		}

		return dp[n][sum];
	}
	
	/*
	 * https://discuss.leetcode.com/topic/67539/0-1-knapsack-detailed-explanation
	 * 
	 * Rf : https://discuss.leetcode.com/topic/62312/java-solution-similar-to-backpack-problem-easy-to-understand
	 */
	public boolean canPartition_1D(int[] nums) {
		int sum = 0;

		for (int num : nums) {
			sum += num;
		}

		if ((sum & 1) == 1) {
			return false;
		}
		sum /= 2;

		//int n = nums.length;
		boolean[] dp = new boolean[sum + 1];
		Arrays.fill(dp, false);
		dp[0] = true;

		for (int num : nums) {
			for (int i = sum; i > 0; i--) {
				if (i >= num) {
					dp[i] = dp[i] || dp[i - num];
				}
			}
		}

		return dp[sum];
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/62549/java-solution-dp-subsum-problem-and-backtracking-subsets-ii-problem-with-explanation
	 * 
	 * Rf : https://discuss.leetcode.com/topic/62987/java-dynamic-programming-solution-21ms-with-explanation
	 */
	public boolean canPartition_backtrack(int[] nums) {
		if (nums == null || nums.length == 0)
			return false;
		int total = 0;
		for (int i = 0; i < nums.length; i++)
			total += nums[i];
		if (total % 2 != 0)
			return false;
		List<Integer> list = new ArrayList<Integer>();
		return helper(nums, list, 0, 0, total);
	}
	public boolean helper(int[] nums, List<Integer> list, int pos, int sum, int total) {
		if (total == 2 * sum)
			return true;
		if (total < 2 * sum)
			return false;
		boolean res = false;
		for (int i = pos; i < nums.length; i++) {
			if (i == pos || nums[i - 1] != nums[i]) {
				list.add(nums[i]);
				res = res || helper(nums, list, i + 1, sum + nums[i], total);
				if (res)
					return true;
				list.remove(list.size() - 1);
			}
		}
		return false;
	}
	
	// https://discuss.leetcode.com/topic/71836/java-simple-solution-using-biginteger

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
