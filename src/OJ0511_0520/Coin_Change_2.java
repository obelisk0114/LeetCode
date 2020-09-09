package OJ0511_0520;

public class Coin_Change_2 {
	/*
	 * by myself
	 * 
	 * dp[i][j] : the number of combinations to make up amount j by using the 
	 *            first i types of coins
	 *            
	 * dp[i - 1][j]: 完全不用當前硬幣組成 j 有多少種組合
	 * dp[i][j - coins[i - 1]] : 使用 "至少" 一個當前硬幣（與上面一條是互斥事件）組成 j 有多少組合
	 * 一般的背包問題是 "不取" 或是 "取一個"，所以用 dp[i - 1][j - ...]
	 * 這裡是 "不取" 或是 "`至少`取一個"。因此用 dp[i][j - ...]，表示在 j - ... 情況下所有組合
	 * 
	 * Initialization: dp[i][0] = 1
	 * 
	 * Rf :
	 * https://leetcode.com/problems/coin-change-2/discuss/176706/Beginner-Mistake:-Why-an-inner-loop-for-coins-doensn't-work-Java-Soln/306232
	 * https://leetcode.com/problems/coin-change-2/discuss/99212/Knapsack-problem-Java-solution-with-thinking-process-O(nm)-Time-and-O(m)-Space/259771
	 * https://leetcode.com/problems/coin-change-2/discuss/99212/Knapsack-problem-Java-solution-with-thinking-process-O(nm)-Time-and-O(m)-Space
	 */
	public int change_2D_self(int amount, int[] coins) {
		int[][] dp = new int[amount + 1][coins.length + 1];
		
		for (int i = 0; i < dp.length; i++) {
			dp[i][0] = 0;
		}
		
		for (int i = 0; i < dp[0].length; i++) {
			dp[0][i] = 1;
		}
		
		for (int i = 1; i <= amount; i++) {
			for (int j = 1; j <= coins.length; j++) {
				if (i >= coins[j - 1]) {
					dp[i][j] = dp[i][j - 1] + dp[i - coins[j - 1]][j];
				}
				else {
					dp[i][j] = dp[i][j - 1];
				}
			}
		}
		return dp[amount][coins.length];
	}
	
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
	public int change_2D2(int amount, int[] coins) {
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
	
	/*
	 * https://leetcode.com/problems/coin-change-2/discuss/99212/Knapsack-problem-Java-solution-with-thinking-process-O(nm)-Time-and-O(m)-Space
	 * 
	 * To get the correct answer, the correct dp definition should be 
	 * dp[i][j] = "number of ways to get sum 'j' using 'first i' coins". Now when we 
	 * try to traverse the 2-d array row-wise by keeping only previous row array (to 
	 * reduce space complexity), we preserve the above dp definition as 
	 * dp[j] = "number of ways to get sum 'j' using 'previous /first i coins' " 
	 * but when we try to traverse the 2-d array column-wise by keeping only the 
	 * previous column, the meaning of dp array changes to 
	 * dp[j] = "number of ways to get sum 'j' using 'all' coins".
	 * 
	 * Rf :
	 * https://leetcode.com/problems/coin-change-2/discuss/176706/Beginner-Mistake:-Why-an-inner-loop-for-coins-doensn't-work-Java-Soln/306232
	 * https://leetcode.com/problems/coin-change-2/discuss/99212/Knapsack-problem-Java-solution-with-thinking-process-O(nm)-Time-and-O(m)-Space/103319
	 */
	public int change_1D(int amount, int[] coins) {
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
