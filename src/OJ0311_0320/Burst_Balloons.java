package OJ0311_0320;

public class Burst_Balloons {
	/*
	 * https://discuss.leetcode.com/topic/30746/share-some-analysis-and-explanations
	 * 
	 * It is very similar to the "matrix chain multiplication" problem described in 
	 * "Introduction to algorithms third edition".
	 * 
	 * 1. Burst all the zeros to avoid hassles. Pad 1 on the both sides of the array
	 * 2. The dp[left][right] represents the maximum coins when you burst and only 
	 *    burst all the elements in the range of (left, right), exclusive.
	 * 3. "i" is the last balloon to burst in this range. The coins you get for a 
	 *    balloon does not depend on the balloons already burst.
	 * 4. The coins become 
	 *    dp[left][i] + nums[left] * nums[i] * nums[right] + dp[i][right]
	 * 
	 * + Base-case is: when left+1 == right, then return 0. A.k.a. there are no 
	 *   balloons in the middle to pop. So these base case values are already stored 
	 *   in our DP array!
	 * + The next case is when we have only 1 balloon between left and right. k 
	 *   represents the distance between left and right, and k = 2 means that there is 
	 *   one balloon between left and right
	 * + So first we iterate through the entire array and find every result when there 
	 *   is just 1 ballon between left and right, and when that's finished, increase 
	 *   the distance k between left and right
	 * + Notice that dp line assigns dp[left][right] to 
	 *   nums[left] * nums[i] * nums[right] + dp[left][i] + dp[i][right])
	 * + Keep increasing the number of balloons in between left and right, until we 
	 *   reach the final case left = 0 and right = n-1
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/37646/my-understanding-of-the-n-3-dp-solution-comments-explanation
	 * https://leetcode.com/problems/burst-balloons/discuss/76228/Share-some-analysis-and-explanations/141530
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/30745/java-dp-solution-with-detailed-explanation-o-n-3
	 * https://discuss.leetcode.com/topic/35965/neat-java-solution
	 */
	public int maxCoins(int[] iNums) {
		int[] nums = new int[iNums.length + 2];
		int n = 1;
		for (int x : iNums)
			//if (x > 0)
				nums[n++] = x;
		
		nums[0] = nums[n++] = 1;

		int[][] dp = new int[n][n];
		for (int k = 2; k < n; k++)
			for (int left = 0; left + k < n; left++) {
				int right = left + k;
				for (int i = left + 1; i < right; i++)
					dp[left][right] = Math.max(dp[left][right],
						nums[left] * nums[i] * nums[right] + dp[left][i] + dp[i][right]);
			}

		return dp[0][n - 1];
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/30746/share-some-analysis-and-explanations
	 * 
	 * Balloons 0, 1, ..., n - 1
	 * Let's first consider bursting [start, end]
	 * Imagine we burst index i at the end
	 * [start, ... i - 1, (i), i + 1 ... end]
	 * Before the end, we already bursted [start, i - 1] and [i + 1, end]
	 * Before the end, boundaries start - 1, i, end + 1 are always there
	 * Calculate coins without worrying about details inside [start, i - 1] and [i + 1, end]
	 * So the range can be divided into
	 * start - 1, maxCoin(start, i - 1), i, maxCoins(i + 1, end), end + 1
	 * 
	 * Pad the beginning+end of the array with 1, since the problem defines it this way,
	 * it won't affect the final value, and most importantly it eliminates the need to 
	 * deal with these special cases.
	 * Base case is when there are no more balloons between the left and right balloon 
	 * indexes (left+1 == right)
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/67631/easiest-java-solution
	 * https://leetcode.com/problems/burst-balloons/discuss/76228/Share-some-analysis-and-explanations/141530
	 */
	public int maxCoins_divided_conquer(int[] iNums) {
		int[] nums = new int[iNums.length + 2];
		int n = 1;
		for (int x : iNums)
			if (x > 0)
				nums[n++] = x;
		
		nums[0] = nums[n++] = 1;

		int[][] memo = new int[n][n];
		return burst_divided_conquer(memo, nums, 0, n - 1);
	}
	public int burst_divided_conquer(int[][] memo, int[] nums, int left, int right) {
		if (left + 1 == right)
			return 0;
		if (memo[left][right] > 0)
			return memo[left][right];
		
		int ans = 0;
		for (int i = left + 1; i < right; ++i)
			ans = Math.max(ans, nums[left] * nums[i] * nums[right] + 
					burst_divided_conquer(memo, nums, left, i) + 
					burst_divided_conquer(memo, nums, i, right));
		
		memo[left][right] = ans;
		return ans;
	}

}
