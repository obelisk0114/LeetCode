package OJ0511_0520;

public class Coin_Change_2 {
	/*
	 * https://leetcode.com/problems/coin-change-2/discuss/99212/Knapsack-problem-Java-solution-with-thinking-process-O(nm)-Time-and-O(m)-Space
	 * 
	 * 1. not using the ith coin, only using the first i-1 coins to make up amount j, 
	 *    then we have dp[i-1][j] ways.
	 * 2. using the ith coin, since we can use unlimited same coin, 
	 *    we need to know how many way to make up amount j - coins[i] by using first i 
	 *    coins(including ith), which is dp[i][j-coins[i]]
	 * 3. Initialization: dp[i][0] = 1
	 * 
	 * Other code :
	 * https://leetcode.com/problems/coin-change-2/discuss/99246/Java-DP-solution
	 */
	public int change(int amount, int[] coins) {
		int[][] dp = new int[coins.length + 1][amount + 1];
		dp[0][0] = 1;

		for (int i = 1; i <= coins.length; i++) {
			dp[i][0] = 1;
			for (int j = 1; j <= amount; j++) {
				dp[i][j] = dp[i - 1][j] + (j >= coins[i - 1] ? dp[i][j - coins[i - 1]] : 0);
			}
		}
		return dp[coins.length][amount];
	}
	
	// https://leetcode.com/problems/coin-change-2/discuss/99212/Knapsack-problem-Java-solution-with-thinking-process-O(nm)-Time-and-O(m)-Space
	public int change_2D(int amount, int[] coins) {
		int[] dp = new int[amount + 1];
		dp[0] = 1;
		for (int coin : coins) {
			for (int i = coin; i <= amount; i++) {
				dp[i] += dp[i - coin];
			}
		}
		return dp[amount];
	}

}
