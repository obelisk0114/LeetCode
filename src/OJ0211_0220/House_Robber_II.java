package OJ0211_0220;

public class House_Robber_II {
	/*
	 * https://leetcode.com/problems/house-robber-ii/discuss/60044/Good-performance-DP-solution-using-Java
	 * 
	 * This problem can simply be decomposed into two House Robber problems.
	 * 
	 * Suppose there are n houses, since house 0 and n - 1 are now neighbors, 
	 * we cannot rob them together and thus the solution is now the maximum of
	 * 1. Rob houses 0 to n - 2;
	 * 2. Rob houses 1 to n - 1.
	 * 
	 * Rf : https://leetcode.com/problems/house-robber-ii/discuss/59921/9-lines-0ms-O(1)-Space-C++-solution
	 * 
	 * Other code: https://leetcode.com/problems/house-robber-ii/discuss/60019/Java-solution-using-two-DP
	 */
	public int rob(int[] nums) {
		if (nums.length == 0)
			return 0;
		if (nums.length == 1)
			return nums[0];

		int[] startFromFirstHouse = new int[nums.length + 1];
		int[] startFromSecondHouse = new int[nums.length + 1];

		startFromFirstHouse[0] = 0;
		startFromFirstHouse[1] = nums[0];
		startFromSecondHouse[0] = 0;
		startFromSecondHouse[1] = 0;

		for (int i = 2; i <= nums.length; i++) {
			startFromFirstHouse[i] = Math.max(startFromFirstHouse[i - 1], startFromFirstHouse[i - 2] + nums[i - 1]);
			startFromSecondHouse[i] = Math.max(startFromSecondHouse[i - 1], startFromSecondHouse[i - 2] + nums[i - 1]);
		}

		return Math.max(startFromFirstHouse[nums.length - 1], startFromSecondHouse[nums.length]);
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/house-robber-ii/discuss/60020/Jave-O(1)-space-O(n)-time-optimal-solution
	 * 
	 * Helper method returns DP solution from 0 ~ n-2 and 1 ~ n-1. 
	 * Final answer is the max between two.
	 * 
	 * Other code:
	 * https://leetcode.com/problems/house-robber-ii/discuss/60042/Simple-Java-Solution
	 */
	public int rob_dp(int[] nums) {
		if (nums == null || nums.length == 0)  // Can be removed
			return 0;
		int n = nums.length;
		if (n == 1) {
			return nums[0];
		}
		return Math.max(robHelper(nums, 0, n - 2), robHelper(nums, 1, n - 1));
	}

	private int robHelper(int[] nums, int start, int end) {
		int curr, prev, prev2;
		curr = prev = prev2 = 0;
		for (int i = start; i <= end; i++) {
			curr = Math.max(prev2 + nums[i], prev);
			prev2 = prev;
			prev = curr;
		}
		return curr;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/house-robber-ii/discuss/59934/Simple-AC-solution-in-Java-in-O(n)-with-explanation
	 * 
	 * Extending from the logic that if house i is not robbed, then you are free to 
	 * choose whether to rob house i + 1, you can break the circle by assuming a 
	 * house is not robbed.
	 * 
	 * Since every house is either robbed or not robbed and at least half of the houses 
	 * are not robbed, the solution is simply the larger of two cases with consecutive 
	 * houses, i.e. house i not robbed, break the circle, solve it, or house i + 1 not 
	 * robbed. Chose i = n and i + 1 = 0 for simpler coding. But, you can choose 
	 * whichever two consecutive ones.
	 * 
	 * Rf :
	 * leetcode.com/problems/house-robber-ii/discuss/59934/Simple-AC-solution-in-Java-in-O(n)-with-explanation/61018
	 */
	public int rob_include_exclude(int[] nums) {
		if (nums.length == 1)
			return nums[0];
		return Math.max(rob_include_exclude(nums, 0, nums.length - 2), rob_include_exclude(nums, 1, nums.length - 1));
	}
	private int rob_include_exclude(int[] num, int lo, int hi) {
		int include = 0, exclude = 0;
		for (int j = lo; j <= hi; j++) {
			int i = include, e = exclude;
			include = e + num[j];
			exclude = Math.max(e, i);
		}
		return Math.max(include, exclude);
	}
	
	// by myself
	public int rob_self(int[] nums) {
        int n = nums.length;
        if (n == 0)
            return 0;
        
        if (n == 1)
            return nums[0];
        
        if (n == 2)
            return Math.max(nums[0], nums[1]);
        
        int[] maxProfit1 = new int[n - 1];
        int[] maxProfit2 = new int[n - 1];
        maxProfit1[0] = nums[0];
        maxProfit1[1] = Math.max(nums[0], nums[1]);
        maxProfit2[0] = nums[n - 1];
        maxProfit2[1] = Math.max(nums[n - 1], nums[n - 2]);
        
        for (int i = 2; i < n - 1; i++) {
            maxProfit1[i] = Math.max(maxProfit1[i - 1], maxProfit1[i - 2] + nums[i]);
            maxProfit2[i] = Math.max(maxProfit2[i - 1], maxProfit2[i - 2] + nums[n - i - 1]);
        }
        
        return Math.max(maxProfit1[n - 2], maxProfit2[n - 2]);
    }

}
