package OJ0321_0330;

import java.util.Arrays;

public class Coin_Change {
	// https://leetcode.com/articles/coin-change/
	public int coinChange(int[] coins, int amount) {
		int max = amount + 1;
		int[] dp = new int[amount + 1];
		Arrays.fill(dp, max);
		dp[0] = 0;
		for (int i = 1; i <= amount; i++) {
			for (int j = 0; j < coins.length; j++) {
				if (coins[j] <= i) {
					dp[i] = Math.min(dp[i], dp[i - coins[j]] + 1);
				}
			}
		}
		return dp[amount] > amount ? -1 : dp[amount];
	}
	
	/*
	 * Modified by myself
	 * 
	 * Rf :
	 * https://leetcode.com/problems/coin-change-2/discuss/176706/Beginner-Mistake:-Why-an-inner-loop-for-coins-doensn't-work-Java-Soln/306232
	 * https://leetcode.com/problems/coin-change/discuss/77373/6-7-lines-2-ways
	 * https://leetcode.com/problems/coin-change/discuss/77385/DP-AC-JAVA-Solution-18ms-Beating-95
	 */
	public int coinChange_self_modify(int[] coins, int amount) {
		int[] dp = new int[amount + 1];
		Arrays.fill(dp, amount + 1);
		dp[0] = 0;
		
		for (int i = 0; i < coins.length; i++) {
			for (int j = 0; j <= amount; j++) {
				if (j >= coins[i]) {
					dp[j] = Math.min(dp[j], dp[j - coins[i]] + 1);
				}
			}
		}
		
		if (dp[amount] > amount) {
			return -1;
		}
		else {
			return dp[amount];
		}
	}
	
	/*
	 * https://leetcode.com/problems/coin-change/discuss/77385/DP-AC-JAVA-Solution-18ms-Beating-95
	 * 
	 * for each iteration, the dp result represents that each amount can be expressed 
	 * by all the coins used before.
	 * 
	 * Other code :
	 * https://leetcode.com/problems/coin-change/discuss/77404/JAVA-Easy-Version-To-Understand!!!!!
	 */
	public int coinChange_bottom_up_reverse(int[] coins, int amount) {
		if (amount < 1)
			return 0;
		int[] dp = new int[amount + 1];
		Arrays.fill(dp, Integer.MAX_VALUE);
		dp[0] = 0;
		for (int coin : coins) {
			for (int i = coin; i <= amount; i++) {
				if (dp[i - coin] != Integer.MAX_VALUE) {
					dp[i] = Math.min(dp[i], dp[i - coin] + 1);
				}
			}
		}
		return dp[amount] == Integer.MAX_VALUE ? -1 : dp[amount];
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/coin-change/discuss/77368/*Java*-Both-iterative-and-recursive-solutions-with-explanations
	 * 
	 * Suppose we have already found out the best way to sum up to amount a, then for 
	 * the last step, we can choose any coin type which gives us a remainder r where 
	 * r = a-coins[i] for all i's. For every remainder, go through exactly the same 
	 * process as before until either the remainder is 0 or less than 0 
	 * (meaning not a valid solution).
	 * 
	 * Other code :
	 * https://leetcode.com/problems/coin-change/discuss/77378/Easy-To-Understand-Recursive-DP-solution-using-Java-(with-explanations)
	 */
	public int coinChange_top_down(int[] coins, int amount) {
		if (amount < 1)
			return 0;
		return helper(coins, amount, new int[amount]);
	}
	// rem: remaining coins after the last step; 
	// count[rem]: minimum number of coins to sum up to rem
	private int helper(int[] coins, int rem, int[] count) {
		if (rem < 0)
			return -1; // not valid
		if (rem == 0)
			return 0; // completed
		if (count[rem - 1] != 0)
			return count[rem - 1]; // already computed, so reuse
		
		int min = Integer.MAX_VALUE;
		for (int coin : coins) {
			int res = helper(coins, rem - coin, count);
			if (res >= 0 && res < min)
				min = 1 + res;
		}
		count[rem - 1] = (min == Integer.MAX_VALUE) ? -1 : min;
		return count[rem - 1];
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/coin-change/discuss/77438/Java-recursive-solution-3ms
	 */
	int total = Integer.MAX_VALUE;
	public int coinChange_top_down_sort(int[] coins, int amount) {
		if (amount == 0)
			return 0;
		Arrays.sort(coins);
		count(amount, coins.length - 1, coins, 0);
		return total == Integer.MAX_VALUE ? -1 : total;
	}
	void count(int amount, int index, int[] coins, int count) {
		if (index < 0 || count >= total - 1)
			return;
		int c = amount / coins[index];
		for (int i = c; i >= 0; i--) {
			int newCount = count + i;
			int rem = amount - i * coins[index];

			if (rem > 0 && newCount < total)
				count(rem, index - 1, coins, newCount);
			else if (newCount < total)
				total = newCount;
			else
				break;
		}
	}
	
	// by myself
	public int coinChange_self1(int[] coins, int amount) {
		if (amount == 0)
			return 0;

		Arrays.sort(coins);
		if (coins[0] > amount)
			return -1;

		int[] total = new int[amount + 1];
		outer: for (int i = 0; i < total.length; i++) {
			if (i < coins[0]) {
				total[i] = -1;
				continue;
			}
			total[i] = Integer.MAX_VALUE - 1;
			for (int j = 0; j < coins.length; j++) {
				if (i == coins[j]) {
					total[i] = 1;
					continue outer;
				}

				if (i < coins[j])
					break;

				if (total[i - coins[j]] != -1) {
					total[i] = Math.min(total[i], total[i - coins[j]] + 1);
				}
			}
			if (total[i] == Integer.MAX_VALUE - 1)
				total[i] = -1;
		}

		return total[total.length - 1];
	}
	
	// by myself 2
	public int coinChange_self2(int[] coins, int amount) {
		if (amount == 0)
			return 0;

		Arrays.sort(coins);
		if (coins[0] > amount)
			return -1;

		int[] total = new int[amount - coins[0] + 1];
		for (int i = 0; i < coins.length && coins[i] - coins[0] < total.length; i++) {
			total[coins[i] - coins[0]] = 1;
		}

		for (int i = 1; i < total.length; i++) {
			if (total[i] == 1)
				continue;
			total[i] = Integer.MAX_VALUE - 1;
			for (int j = 0; j < coins.length; j++) {
				if (i < coins[j])
					break;

				if (total[i - coins[j]] != -1)
					total[i] = Math.min(total[i], total[i - coins[j]] + 1);
			}

			if (total[i] == Integer.MAX_VALUE - 1)
				total[i] = -1;
		}

		return total[total.length - 1];
	}

}
