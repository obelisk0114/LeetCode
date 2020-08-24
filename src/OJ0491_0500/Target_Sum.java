package OJ0491_0500;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

public class Target_Sum {
	/*
	 * The following variable and 2 functions are by myself.
	 * Rf : https://leetcode.com/problems/target-sum/discuss/97333/java-simple-dfs-with-memorization
	 */
	public int findTargetSumWays_map_self2(int[] nums, int S) {
        Map<String, Integer> map = new HashMap<>();
        return dfsCalculate_map_self2(nums, 0, 0, S, map);
    }
    
    private int dfsCalculate_map_self2(int[] nums, int start, int sum, int S, 
    		Map<String, Integer> map) {
    	
        if (start == nums.length) {
            if (sum == S) {
                return 1;
            }
            else {
                return 0;
            }
        }
        
        String key = start + "_" + sum;
        if (map.containsKey(key)) {
            return map.get(key);
        }
        
        int add = dfsCalculate_map_self2(nums, start + 1, sum + nums[start], S, map);
        int sub = dfsCalculate_map_self2(nums, start + 1, sum - nums[start], S, map);
        
        int res = add + sub;
        map.put(key, res);
        return res;
    }
    
    /*
     * https://leetcode.com/problems/target-sum/discuss/97335/Short-Java-DP-Solution-with-Explanation/101899
     * 
     * This is a classic knapsack problem
     * In knapsack, we decide whether we choose this element or not
     * In this question, we decide whether we add this element or minus it
     * 
     * So start with a two dimensional array dp[i][j] which means the number of ways 
     * for first i-th element to reach a sum j
     * 
     * we can easily observe that dp[i][j] = dp[i-1][j+nums[i]] + dp[i-1][j-nums[i],
     * 
     * Here we return dp[sum+S], because dp's range starts from -sum --> 0 --> +sum
     * so we need to add sum first, then the total starts from 0, then we add S
     * Actually most of Sum problems can be treated as knapsack problem
     * 
     * dp[0][0+sum] = 1 means at 0th step, the solution number to sum = 0 is 1.
     * The range of sum of all elements is [-sum, sum]. To initialize the dp array, we 
     * want that at 0th step, for the case that sum = 0, we have one solution. So, we 
     * shift all elements 'sum' elements to make the index start from 0.
     * 
     * Rf :
     * https://leetcode.com/problems/target-sum/discuss/97335/Short-Java-DP-Solution-with-Explanation/549087
     */
	public int findTargetSumWays_dp_knapsack(int[] nums, int S) {
		int sum = 0;
		for (int n : nums) {
			sum += n;
		}
		if (S < -sum || S > sum) {
			return 0;
		}

		int[][] dp = new int[nums.length + 1][2 * sum + 1];
		dp[0][0 + sum] = 1; // 0 + sum means 0, 0 means -sum
		
		for (int i = 1; i <= nums.length; i++) {
			for (int j = 0; j < 2 * sum + 1; j++) {
				if (j + nums[i - 1] < 2 * sum + 1)
					dp[i][j] += dp[i - 1][j + nums[i - 1]];
				if (j - nums[i - 1] >= 0)
					dp[i][j] += dp[i - 1][j - nums[i - 1]];
			}
		}
		return dp[nums.length][sum + S];
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
        } 
        else {
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
     * https://leetcode.com/problems/target-sum/discuss/97335/short-java-dp-solution-with-explanation
     * 
     * if we calculate total sum of all candidate numbers, then the range of possible 
     * calculation result will be in the range [-sum, sum]. So we can define an dp 
     * array with size sum * 2 + 1 to calculate number of possible ways to reach every 
     * target value between -sum to sum, and save results to dp array. dp[sum + S] 
     * will be our final result. (because dp[sum] or less represents number of 
     * possible ways to reach a number in range [-sum, 0])
     *
     * sub-problem: dp[i] represents number of possible ways to reach target i
     * base case: dp[sum] = 1 // started at 0 (sum)
     * recurrence relation: when doing inner loop iterations, we should create another 
     * temp dp array to store temp target array. Because if we use dp array to store 
     * temp results directly, we may have array boundary exception
     * eg: for input [1,1,1,1,1], we will never reach dp[6] or d[-6]. However, if we 
     * use dp[j + nums[i]] to store temp results, we may proceed dp[5 + 1] += dp[5], 
     * which is considered incorrect case
     * 
     * Rf :
     * https://leetcode.com/problems/target-sum/discuss/97335/Short-Java-DP-Solution-with-Explanation/239358
     */
	public int findTargetSumWays_dp2(int[] nums, int s) {
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
				// WARNING: DO NOT FORGET to check condition whether dp[i] is 0 or not
	            // if it is NOT 0, it means we at least have one possible way to reach 
				// target j. Otherwise, we may have array out of bound exception
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
