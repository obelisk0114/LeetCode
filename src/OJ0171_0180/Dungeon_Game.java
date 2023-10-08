package OJ0171_0180;

import java.util.Arrays;

public class Dungeon_Game {
	/*
	 * https://discuss.leetcode.com/topic/6858/my-ac-java-version-suggestions-are-welcome
	 * 
	 * In the reference answer provided with the question, dp[i][j] is defined as 
	 * 'the minimum hp required to REACH (n_rows-1, n_cols-1) from (i, j)'. 
	 * Here dp[0][0] is the final answer so we must fill from (n_rows-1, n_cols-1).
	 * 
	 * In order to compute HP[i][j], you will need to make sure of two things:
	 * 1. your HP[i][j] + dungeon[i][j] should be > 0
	 * 2. your HP[i][j] + dungeon[i][j] should be large enough, so that it will be 
	 *    sufficient to cover the minimum requirement on HP for the next step, be 
	 *    it right or down (take whichever requires smaller HP).
	 * 
	 * Because of the second requirement, your calculation of HP[i][j] will depend on 
	 * later steps that you could take. This is why you have to know these later 
	 * steps ahead of time, thus calculating from the bottom right.
	 * 
	 * Rf :
	 * https://discuss.leetcode.com/topic/6906/who-can-explain-why-from-the-bottom-right-corner-to-left-top/2
	 * https://discuss.leetcode.com/topic/6906/who-can-explain-why-from-the-bottom-right-corner-to-left-top/6
	 */
	public int calculateMinimumHP(int[][] dungeon) {
		if (dungeon == null || dungeon.length == 0 || dungeon[0].length == 0)
			return 0;

		int m = dungeon.length;
		int n = dungeon[0].length;
		int[][] health = new int[m][n];

		health[m - 1][n - 1] = Math.max(1 - dungeon[m - 1][n - 1], 1);

		for (int i = m - 2; i >= 0; i--) {
			health[i][n - 1] = Math.max(health[i + 1][n - 1] - dungeon[i][n - 1], 1);
		}
		
		for (int j = n - 2; j >= 0; j--) {
			health[m - 1][j] = Math.max(health[m - 1][j + 1] - dungeon[m - 1][j], 1);
		}

		for (int i = m - 2; i >= 0; i--) {
			for (int j = n - 2; j >= 0; j--) {
				int down = Math.max(health[i + 1][j] - dungeon[i][j], 1);
				int right = Math.max(health[i][j + 1] - dungeon[i][j], 1);
				health[i][j] = Math.min(right, down);
				
				// 上面三行也可以換成下面這樣
				/*
				 * health[i][j] = Math.max(1, 
				 * 						Math.min(health[i + 1][j], health[i][j + 1]) 
				 * 							- dungeon[i][j]);
				 * 
				 */
			}
		}

		return health[0][0];
	}
	
	/*
	 * https://discuss.leetcode.com/topic/26040/simple-java-dp-code
	 * 
	 * If we define: dp[i][j] = minimum cost from (0, 0) to (i, j)
	 * It won't help solving the problem, because the result of dp[i + 1][j + 1] 
	 * does not depends only on previous solve subproblems, but also future unsolved 
	 * subproblems.
	 * 
	 * So, how about let's define the subproblem from the other end of the puzzle?
	 * dp[i][j] = minimum hp required to reach the princess when entering (i, j)
	 * 
	 * So, what is dp[i + 1][j + 1] then? It depends on the minimum between 
	 * dp[i][j + 1] and dp[i + 1][j], because we want to choose the cheapest way to go. 
	 * Of course we also need to add or deduct the value from dungeon matrix. 
	 * But be careful, if we find that the minimum required hp is less that 0, 
	 * we need to set it to 0, because we are not allowed to overdraft health. 
	 * With that said:
	 * dp[i + 1][j + 1] = max(min(dp[i][j + 1], dp[i + 1][j]) - dungeon[i + 1][j + 1], 0);
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/52942/a-very-clean-and-intuitive-solution-with-explanation
	 * https://discuss.leetcode.com/topic/6912/c-dp-solution
	 */
	public int calculateMinimumHP_dummy_array(int[][] dungeon) {
		int m = dungeon.length;
		int n = dungeon[0].length;
	    //dp[i][j] represents the minimum health points needed at position (i,j) and
	    //the last row or column is just dummy (outside of the range).
		int[][] dp = new int[m + 1][n + 1];

	    //Initialize the matrix to maximum possible.
		for (int i = 0; i <= m; i++)
			Arrays.fill(dp[i], Integer.MAX_VALUE);

	    //initializing the boundary.
		dp[m][n - 1] = 1;
		dp[m - 1][n] = 1;

		for (int i = m - 1; i >= 0; i--) {
			for (int j = n - 1; j >= 0; j--) {
				int minHp = Math.min(dp[i + 1][j], dp[i][j + 1]) - dungeon[i][j];
				dp[i][j] = (minHp <= 0) ? 1 : minHp;
			}
		}
		return dp[0][0];
	}
	
	/*
	 * https://discuss.leetcode.com/topic/23856/java-dp-solution
	 * 
	 * Rf : https://discuss.leetcode.com/topic/7633/best-solution-i-have-found-with-explanations
	 */
	public int calculateMinimumHP2(int[][] dungeon) {
		if (dungeon == null || dungeon.length == 0)
			return 0;
		int m = dungeon.length, n = dungeon[0].length;
		int dp[][] = new int[m][n];
		for (int i = m - 1; i >= 0; --i) {
			for (int j = n - 1; j >= 0; --j) {
				if (i == m - 1 && j == n - 1)
					dp[i][j] = Math.max(1, 1 - dungeon[i][j]);
				else if (i == m - 1)
					dp[i][j] = Math.max(1, dp[i][j + 1] - dungeon[i][j]);
				else if (j == n - 1)
					dp[i][j] = Math.max(1, dp[i + 1][j] - dungeon[i][j]);
				else
					dp[i][j] = Math.max(1, Math.min(dp[i][j + 1], dp[i + 1][j]) - dungeon[i][j]);
			}
		}
		return dp[0][0];
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/6858/my-ac-java-version-suggestions-are-welcome/5
	 */
	int[][] mem;
	private int helper(int[][] dungeon, int i, int j) {
		if (i >= dungeon.length || j >= dungeon[0].length)
			return Integer.MAX_VALUE;
		if (mem[i][j] > 0)
			return mem[i][j];
		
		int health = Integer.MAX_VALUE;
		
		health = Math.min(health, helper(dungeon, i + 1, j));
		health = Math.min(health, helper(dungeon, i, j + 1));
		health = health == Integer.MAX_VALUE ? 1 : health; // this only happens
															// with i, j is P already
		int ret = health > dungeon[i][j] ? (health - dungeon[i][j]) : 1;
		mem[i][j] = ret;
		return ret;
	}
	public int calculateMinimumHP_top_down(int[][] dungeon) {
		if (dungeon.length == 0)
			return 0;
		mem = new int[dungeon.length][dungeon[0].length];
		return helper(dungeon, 0, 0);
	}

}
