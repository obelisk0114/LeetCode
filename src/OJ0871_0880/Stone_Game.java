package OJ0871_0880;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

/*
 * 參考 486. Predict the Winner
 * 
 * Approach 1: 
 * You can solve the problem saving the difference of number of stones at each point.
 * Let say we have an array of piles with i,j denoting the first and last position 
 * in the array.
 * 1. If Alex choose the ith pile, then your answer is piles[i] - dp[i+1][j]
 * 2. If Alex choose the jth pile, then your answer is piles[j] - dp[i][j-1]
 * https://leetcode.com/problems/stone-game/discuss/154610/DP-or-Just-return-true
 * 
 * Approach 2: 
 * You can solve the problem by saving the number of stones at each point.
 * Let say we have an array of piles with i,j denoting the first and last position 
 * in the array.
 * 1. If Alex chooses the first pile then the optimal number of stones he can collect 
 *    would be the number of stones in the first pile + minimum number of stones he 
 *    can collect from the remaining piles i.e as Lee is playing optimally
 *    piles[i] + min(dp[i+2][j], dp[i+1][j-1])
 * 2. If Alex chooses the last pile:
 *    piles[j] + min(dp[i+1][j-1], dp[i][j-2])
 * 
 * https://leetcode.com/problems/stone-game/discuss/277753/Java-DP-solution(calculates-number-of-stones-instead-of-difference)
 * 
 * test cases: 
 * [3,2,10,4]
 * 
 * 只取 odd 或 even 不一定是 optimal
 * test case: [3,2,2,3,1,2]
 * sum of odd-numbered coins = 3 + 2 + 1 = 6
 * sum of even-numbered coins = 2 + 3 + 2 = 7
 * optimal 取法: 3 -> 2 (對手) -> 2 -> 2 或 1 (對手) -> 3 -> 1 或 2 (對手)
 * optimal 結果: 3 + 2 + 3 = 8
 * 
 * odd sum 和 even sum 相等不一定會平手
 * test case: [4,2,2,3,1,2]
 * sum of odd-numbered coins = 4 + 2 + 1 = 7
 * sum of even-numbered coins = 2 + 3 + 2 = 7
 * optimal 取法: 4 -> 2 (對手) -> 2 -> 2 或 1 (對手) -> 3 -> 1 或 2 (對手)
 * optimal 結果: 4 + 2 + 3 = 9
 */

public class Stone_Game {
	/*
	 * Modify by myself
	 * 
	 * This is a Minimax problem. Each player plays optimally to win, but you can't 
	 * greedily choose the optimal strategy so you have to try all strategies.
	 * 
	 * 雙方皆使用 optimal strategy。所以 Alex 希望他的成績最大，但是 Lee 希望 Alex 的成績最小
	 * 雙方輪流取，所以當前狀態是建立在對方所取完的結果
	 * 
	 * dp[i][j] 表示 i ~ j 被取完時，Alex 的成績
	 * 
	 * Alex 取完後是 Lee 取，而 Lee 希望 Alex 的成績最小，因此使用 min
	 * 
	 * 取 i，結果 (left) 為 piles[i] + Math.min(dp[i + 2][j], dp[i + 1][j - 1])
	 * 表示 Lee 取 (i + 1 或是 j)，選小的
	 * 
	 * 取 j，結果 (right) 為 piles[j] + Math.min(dp[i + 1][j - 1], dp[i][j - 2])
	 * 表示 Lee 取 (i + 1 或是 j)，選小的
	 * 
	 * dp[i][j] = Math.max(left, right)
	 * 
	 * Rf :
	 * https://leetcode.com/problems/stone-game/discuss/277753/Java-DP-solution(calculates-number-of-stones-instead-of-difference)
	 * https://leetcode.com/problems/stone-game/discuss/154660/Java-This-is-minimax-%2B-dp-(fully-detailed-explanation-%2B-generalization-%2B-easy-understand-code)
	 */
	public boolean stoneGame_self_modify(int[] piles) {
		int n = piles.length;

		// to get the idea to what Alex chose i.e the first pile or the last pile
		// to get the optimal solution
		boolean first = false;

		if (n <= 2)
			return true;

		int[][] dp = new int[n][n];

		// initialize values when there is only one pile
		for (int i = 0; i < n; i++) {
			dp[i][i] = piles[i];
		}

		// initialize values when there are two piles
		for (int i = 0; i < n - 1; i++) {
			dp[i][i + 1] = Math.max(piles[i], piles[i + 1]);
		}

		for (int d = 2; d < n; d++) {
			for (int left = 0; left + d < n; left++) {
                int right = left + d;
				int firstCut = Math.min(dp[left + 2][right], dp[left + 1][right - 1]);
				int lastCut = Math.min(dp[left + 1][right - 1], dp[left][right - 2]);

				dp[left][right] = 
						Math.max(piles[left] + firstCut, piles[right] + lastCut);

				if (left == 0 && right == n - 1) {
					if (piles[left] + firstCut > piles[right] + lastCut) {
						first = true;						
					}
				}
			}
		}

		if (first) {
			return dp[0][n - 1] >= dp[1][n - 1];
		} 
		else {
			return dp[0][n - 1] >= dp[0][n - 2];
		}
	}
	
	/*
	 * https://leetcode.com/problems/stone-game/discuss/277753/Java-DP-solution(calculates-number-of-stones-instead-of-difference)
	 * 
	 * You can solve the problem by saving the number of stones at each point.
	 * 
	 * Let say we have an array of piles with i,j denoting the first and last 
	 * position in the array.
	 * If Alex chooses the first pile then the optimal number of stones he can 
	 * collect would be the number of stones in the first pile + minimum number of 
	 * stones he can collect from the remaining piles 
	 * i.e as Lee is playing optimally
	 * 
	 * If Alex choose the first pile, then Lee has two options
	 * 1. Lee chooses the first pile , then answer is dp[i+2][j] 
	 *    Alex would get chance to choose from [i+2,j] as i+1 pile is taken by Lee.
	 * 2. Lee chooses the last pile, then answer is dp[i+1][j-1] 
	 *    Alex would get chance to choose from [i+1,j-1] as j pile is taken by Lee.
	 * 
	 * As we know that Lee is playing optimally he would choose from the above two 
	 * options such that Alex gets minimum number of stones, hence the answer would be 
	 * piles[i] + min(dp[i+2][j], dp[i+1][j-1])
	 * 
	 * If Alex choose the last pile
	 * 1. Lee chooses the first pile , then answer is dp[i+1][j-1] 
	 *    Alex would get chance to choose from [i+1,j-1] as i pile is taken by Lee.
	 * 2. Lee chooses the last pile, then answer is dp[i][j-2] 
	 *    Alex would get chance to choose from [i,j-2] as j-1 pile is taken by Lee.
	 * 
	 * As we know that Lee is playing optimally he would choose from the above two 
	 * options such that Alex gets minimum number of stones, hence the answer would be 
	 * piles[j] + min(dp[i+1][j-1], dp[i][j-2])
	 * 
	 * dp[i][j] = max( piles[i] + min(dp[i+2][j], dp[i+1][j-1]) , 
	 *                 piles[j] + min(dp[i+1][j-1], dp[i][j-2]) )
	 * 
	 */
	public boolean stoneGame_dp4(int[] piles) {
		int n = piles.length;
		
		// to get the idea to what Alex chose i.e the first pile or the last pile
		// to get the optimal solution
		boolean first = false;

		if (n <= 2)
			return true;

		int[][] dp = new int[n][n];

		// initialize values when there is only one pile
		for (int i = 0; i < n; i++) {
			dp[i][i] = piles[i];
		}

		for (int i = n - 2; i >= 0; i--) {
			for (int j = 0; j < n; j++) {
				if (i < j) {
					int firstCutMax = 
							Math.min(i + 2 > j ? 0 : dp[i + 2][j], 
									i + 1 > j - 1 ? 0 : dp[i + 1][j - 1]);
					
					int lastCutMax = 
							Math.min(i + 1 > j - 1 ? 0 : dp[i + 1][j - 1], 
									i > j - 2 ? 0 : dp[i][j - 2]);

					dp[i][j] = 
							Math.max(piles[i] + firstCutMax, piles[j] + lastCutMax);

					if (i == 0 && j == n - 1) {
						if (piles[i] + firstCutMax > piles[j] + lastCutMax)
							first = true;
					}
				}
			}
		}

		if (first) {
			return dp[0][n - 1] > dp[1][n - 1];
		} 
		else {
			return dp[0][n - 1] > dp[0][n - 2];
		}
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/stone-game/discuss/154660/Java-This-is-minimax-%2B-dp-(fully-detailed-explanation-%2B-generalization-%2B-easy-understand-code)
	 * 
	 * This is a Minimax problem. Each player plays optimally to win, but you can't 
	 * greedily choose the optimal strategy so you have to try all strategies, this 
	 * is DP now.
	 * 
	 * What does it mean for Alex to win? Alex will win if score(Alex) >= score(Lee), 
	 * but this also means score(Alex) - score(Lee) >= 0, so here you have a common 
	 * parameter which is a score variable. The score parameter really means 
	 * score = score(Alex) - score(Lee).
	 * 
	 * Since Alex is playing optimally, he wants to maximize the score variable 
	 * because Alex only wins if score = score(Alex) - score(Lee) >= 0 Alex should 
	 * add to the score because he wants to maximize it.
	 * 
	 * Since Lee is also playing optimally, he wants to minimize the score variable, 
	 * since if the score variable becomes negative, Lee has more individual score 
	 * than Alex. But since we have only one variable, Lee should damage the score 
	 * (or in other words, subtract from the score).
	 * 
	 * But realize the state you are in. You can easily memorize the this, the 
	 * varying parameters are l, r, ID, where ID is the player ID (1 for Alex, 0 for 
	 * Lee), hence you can make a DP/Cache table and return answer if you have 
	 * discovered the state.
	 * 
	 * Finally, in your main function you call this helper function and check if you 
	 * were able to get a score >= 0.
	 */
	int[][][] memo_topDown5;

	public boolean stoneGame_topDown5(int[] piles) {
		memo_topDown5 = new int[piles.length + 1][piles.length + 1][2];
		
		for (int[][] arr : memo_topDown5)
			for (int[] subArr : arr)
				Arrays.fill(subArr, -1);

		return (helper_topDown5(0, piles.length - 1, piles, 1) >= 0);
	}

	public int helper_topDown5(int l, int r, int[] piles, int ID) {
		if (r < l)
			return 0;
		if (memo_topDown5[l][r][ID] != -1)
			return memo_topDown5[l][r][ID];

		int next = Math.abs(ID - 1);
		if (ID == 1)
			memo_topDown5[l][r][ID] = 
				Math.max(piles[l] + helper_topDown5(l + 1, r, piles, next),
						piles[r] + helper_topDown5(l, r - 1, piles, next));
		else
			memo_topDown5[l][r][ID] = 
				Math.min(-piles[l] + helper_topDown5(l + 1, r, piles, next),
						-piles[r] + helper_topDown5(l, r - 1, piles, next));

		return memo_topDown5[l][r][ID];
	}

	/*
	 * The following 2 functions are modified by myself.
	 * 
	 * minimax algorithm for adversarial game playing
	 * 
	 * p1 tries to maximize his return by calling max. 
	 * p2 tries to minimize p1's return by calling min.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/stone-game/discuss/177041/C%2B%2B-solutin-DP
	 * https://leetcode.com/problems/stone-game/discuss/154604/Python-recursive-dp-mini-max
	 */
	public boolean stoneGame_topDown2(int[] piles) {
        int[][] table = new int[piles.length][piles.length];
        for (int i = 0; i < table.length; i++) {
            Arrays.fill(table[i], -1);
        }
        
        int sum = 0;
        for (int i : piles) {
            sum += i;
        }
        
        int Alex = recursive_topDown2(piles, 0, piles.length - 1, table);
        int Lee = sum - Alex;
        
        return Alex > Lee;
    }
    
    private int recursive_topDown2(int[] piles, int left, int right, int[][] table) {
        if (left > right) {
            return 0;
        }
        
        if (table[left][right] != -1) {
            return table[left][right];
        }
        
        int pickFirst = piles[left] + 
        		Math.min(recursive_topDown2(piles, left + 1, right - 1, table), 
        				recursive_topDown2(piles, left + 2, right, table));
        
        int pickLast = piles[right] + 
        		Math.min(recursive_topDown2(piles, left, right - 2, table), 
        				recursive_topDown2(piles, left + 1, right - 1, table));
        
        table[left][right] = Math.max(pickFirst, pickLast);
        return table[left][right];
    }
    
    /*
     * https://leetcode.com/problems/stone-game/solution/
     * Approach 1: Dynamic Programming
     * 
     * whenever Lee scores points, it deducts from Alex's score instead.
     * 
     * Let dp(i, j) be the largest score Alex can achieve where the piles remaining 
     * are piles[i], piles[i+1], ..., piles[j]. 
     * 
     * Formulate a recursion for dp(i, j) in terms of dp(i+1, j) and dp(i, j-1), and 
     * we can use dynamic programming to not repeat work in this recursion. 
     * (This approach can output the correct answer, because the states form a 
     * DAG (directed acyclic graph).)
     * 
     * The person who's turn it is can be found by comparing j-i to N modulo 2.
     * 
     * If the player is Alex, then she either takes piles[i] or piles[j], increasing 
     * her score by that amount. Afterwards, the total score is either 
     * piles[i] + dp(i+1, j), or piles[j] + dp(i, j-1); and we want the 
     * maximum possible score.
     * 
     * If the player is Lee, then he either takes piles[i] or piles[j], decreasing 
     * Alex's score by that amount. Afterwards, the total score is either 
     * -piles[i] + dp(i+1, j), or -piles[j] + dp(i, j-1); and we want the 
     * minimum possible score.
     */
	public boolean stoneGame_dp5(int[] piles) {
		int N = piles.length;

		// dp[i+1][j+1] = the value of the game [piles[i], ..., piles[j]].
		int[][] dp = new int[N + 2][N + 2];
		
		for (int size = 1; size <= N; ++size)
			for (int i = 0; i + size <= N; ++i) {
				int j = i + size - 1;
				
				// j - i - N; but +x = -x (mod 2)
				int parity = (j + i + N) % 2;
				
				if (parity == 1) {
					dp[i + 1][j + 1] = 
							Math.max(piles[i] + dp[i + 2][j + 1], 
									piles[j] + dp[i + 1][j]);
				}
				else {					
					dp[i + 1][j + 1] = 
							Math.min(-piles[i] + dp[i + 2][j + 1], 
									-piles[j] + dp[i + 1][j]);
				}
			}

		return dp[1][N] > 0;
	}
    
    /*
	 * https://leetcode.com/problems/stone-game/discuss/154610/DP-or-Just-return-true
	 * 
	 * dp[i][j] means the biggest number of stones you can get more than opponent 
	 * picking piles in piles[i] ~ piles[j].
	 * 
	 * if now Alex pick, dp[i][j] = maximum(alex stone - lee stone), (j - i) is odd
	 * if now Lee pick, dp[i][j] = maximum(lee stone - alex stone), (j - i) is even
	 * 
	 * You can first pick piles[i] or piles[j].
	 * 1. If you pick piles[i], your result will be piles[i] - dp[i + 1][j]
	 * 2. If you pick piles[j], your result will be piles[j] - dp[i][j - 1]
	 * 
	 * dp[i][j] = max(piles[i] - dp[i + 1][j], piles[j] - dp[i][j - 1])
	 * We start from smaller subarray and we use that to calculate bigger subarray.
	 * 
	 * For dp[i][j] = max(piles[i] - dp[i + 1][j], piles[j] - dp[i][j - 1]), let's 
	 * make an assumption now it's Lee choosing.
	 * 
	 * 1st part: piles[i] - dp[i+1][j], if lee picks pile i, then
	 * Lee stone - Alex stone 
	 * = piles[i] (Lee picks pile i) + {piles Lee picked in piles [i+1, j]} 
	 *   - {piles Alex picked in piles [i+1, j] }
	 * = piles[i] - ({piles Alex picked in piles [i+1, j] } 
	 *   - {piles Lee picked in piles [i+1, j]})
	 * = piles[i] - dp[i][j]
	 * 
	 * Note that take evens or take odds, it's just an easy strategy to win when the 
	 * number of stones is even.
	 * It's not the best solution!
	 * 
	 * Rf :
	 * https://leetcode.com/problems/stone-game/discuss/154610/DP-or-Just-return-true/160075
	 * https://leetcode.com/problems/stone-game/discuss/154610/DP-or-Just-return-true/160366
	 * https://leetcode.com/problems/stone-game/discuss/154610/DP-or-Just-return-true/160498
	 * 
	 * Other code:
	 * https://leetcode.com/problems/stone-game/discuss/497647/Java-DP-solution
	 * https://leetcode.com/problems/stone-game/discuss/678326/Java-Simple-DP
	 */
	public boolean stoneGame_dp2(int[] p) {
		int n = p.length;
		int[][] dp = new int[n][n];
		
		for (int i = 0; i < n; i++)
			dp[i][i] = p[i];
		
		for (int d = 1; d < n; d++)
			for (int i = 0; i < n - d; i++)
				dp[i][i + d] = 
					Math.max(p[i] - dp[i + 1][i + d], p[i + d] - dp[i][i + d - 1]);
		
		return dp[0][n - 1] > 0;
	}
	
    /*
     * https://leetcode.com/problems/stone-game/discuss/154610/DP-or-Just-return-true
     * 
     * dp[i] is the biggest number of stones you can get more than opponent, picking 
     * piles in range piles[i] ~ piles[i + d - 1]. 
     * 
     * for d = 1 - after inner loop finishes dp[i] contains the best score for 
     * range p[i, i + 1], we filled dp[i:n - 1]
     * for d = 2 - after inner loop finishes dp[i] contains the best score for 
     * range p[i, i + 2], we filled dp[i:n - 2]
     * ...
     * for d - after inner loop finishes dp[i] contains the best score for range 
     * p[i, i + d], we filled dp[i:n - d] (we fill one less element of dp every time 
     * we increase the window d by 1)
     * 
     * 1. p[i] - dp[i + 1]
     *    p[i] is the stone at the left side of the window, if we pick it up, the 
     *    opponent will take the best from dp[i + 1] which represents best score for 
     *    piles p[i + 1] ~ p[i + d]. Because in previous run of the outer loop we 
     *    calculated this for d - 1 and it is currently stored in dp[i + 1].
     * 
     * 2. p[i + d] - dp[i]
     *    p[i + d] is the stone at the right side of the window, if we pick it up, 
     *    the opponent will take the best from dp[i] which represents best score for 
     *    piles p[i] ~ p[i + d - 1]. Because in previous run of the outer loop we 
     *    calculated this for d - 1 and it is currently stored in dp[i].
     * 
     * Rf :
     * https://leetcode.com/problems/stone-game/discuss/154610/DP-or-Just-return-true/393623
     * https://leetcode.com/problems/stone-game/discuss/154610/DP-or-Just-return-true/463540
     */
	public boolean stoneGame_dp3(int[] p) {
		int[] dp = Arrays.copyOf(p, p.length);
		for (int d = 1; d < p.length; d++)
			for (int i = 0; i < p.length - d; i++)
				dp[i] = Math.max(p[i] - dp[i + 1], p[i + d] - dp[i]);
		
		return dp[0] > 0;
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/stone-game/discuss/972898/Java-short-and-crisp-or-DFS%2BMemoization-5-lines-or-DP-or-with-detailed-explanation
	 * 
	 * DFS + Memorization
	 * Here as both players play optimally and as they will be switching, so at every 
	 * instance we try to find maximum possible score and add it to score if it is 
	 * Player1 or subtract if it is Player2.
	 * 
	 * ALGO:
	 * -> At each chance we pick maximum stones by selecting from start or from end 
	 *    and passing it to next player.
	 *    Note: if only one stone left, then you have no other way but to pick it and 
	 *    game over.
	 * -> DFS will be called even no.of times for Player1 and odd no.of times for 
	 *    Player2, So value of picked stone will get added for Player1 and subtracted 
	 *    for Player2.
	 * -> By doing this we are essentially finding difference of maximum scores of 
	 *    both players.
	 * 
	 * If Difference of max scores of Player1 & 2 > ZERO, return TRUE. Else FALSE.
	 */
	HashMap<String, Integer> map_topDown = new HashMap<>();

	public boolean stoneGame_topDown(int[] piles) {
		return dfs_topDown(piles, 0, piles.length - 1) > 0;
	}

	private int dfs_topDown(int[] piles, int start, int end) {
		// Only one stone left
		if (start == end)
			return piles[start];
		
		String key = start + " " + end;
		
		if (map_topDown.containsKey(key))
			return map_topDown.get(key);
		
		/* Choose the better option, either picking from start or end */
		int val = Math.max(piles[start] - dfs_topDown(piles, start + 1, end),
				piles[end] - dfs_topDown(piles, start, end - 1));
		
		map_topDown.put(key, val);
		return val;
	}
	
	/*
	 * The following function and class are from this link.
	 * https://leetcode.com/problems/stone-game/discuss/972898/Java-short-and-crisp-or-DFS%2BMemoization-5-lines-or-DP-or-with-detailed-explanation
	 * 
	 * We pre-calculate all possibilities from length of window = 1(only one stone), 
	 * then try to find answers for increasing windows (from 2 to piles.length).
	 * 
	 * 若 first 取 i, 剩下 (i + 1 ... j)
	 * 輪到 second 取，而 second 在 (i + 1 ... j) 是第一個取
	 * 因此 second 取的最終結果是 T[i + 1][j].first
	 * first 則加上  T[i + 1][j].second。
	 * ∴ first = piles[i] + T[i + 1][j].second
	 * second = T[i + 1][j].first
	 * 
	 * 若 first 取 j, 剩下 (i ... j - 1)
	 * 輪到 second 取，而 second 在 (i ... j - 1) 是第一個取
	 * 因此 second 取的最終結果是 T[i][j - 1].first
	 * first 則加上  T[i][j - 1].second。
	 * ∴ first = piles[j] + T[i][j - 1].second
	 * second = T[i][j - 1].first
	 * 
	 * 又因為 first + second = 所取總和 (sum)
	 * 可以只儲存 first, sum - Math.min(T[i + 1][j], T[i][j - 1]) 來計算這回合的 first
	 * 
	 * T[i][j].first = Math.max(piles[i] + T[i + 1][j].second, 
	 *                          piles[j] + T[i][j - 1].second)
	 * T[i][j].second = T[i + 1][j].first or
	 *                  T[i][j - 1].first
	 * 
	 * Rf :
	 * https://www.youtube.com/watch?v=WxpIHvsu1RI
	 * 
	 * Other code:
	 * https://leetcode.com/problems/stone-game/discuss/154610/DP-or-Just-return-true/500145
	 */
	public boolean stoneGame_dp_pair(int[] piles) {
		int n = piles.length;
		if (n == 1)
			return true;
		
		Pair_dp_pair[][] memo = new Pair_dp_pair[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				memo[i][j] = new Pair_dp_pair();
				
				/*
				 * If only one stone left, then current player picks it and 
				 * other player wont get any. GAME OVER.
				 */
				if (i == j)
					memo[i][j].first = piles[i];
			}
		}
		
		for (int len = 2; len <= n; len++) {
			for (int start = 0, end = start + len - 1; end < n; start++, end++) {
				/*
				 * Choose the better option, either picking from start or end. 
				 * As players alternately select, if a current player selects now 
				 * then he will become second player to choose from remaining stones.
				 */
				if (piles[start] + memo[start + 1][end].second 
						> piles[end] + memo[start][end - 1].second) {
					
					memo[start][end].first = 
							piles[start] + memo[start + 1][end].second;
					
					memo[start][end].second = 
							memo[start + 1][end].first;
				} 
				else {
					memo[start][end].first = 
							piles[end] + memo[start][end - 1].second;
					
					memo[start][end].second = 
							memo[start][end - 1].first;
				}
			}
		}
		
		return memo[0][n - 1].first > memo[0][n - 1].second;
	}

	class Pair_dp_pair {
		int first = 0, second = 0;

		public String toString() {
			return first + " " + second;
		}
	}
	
	/*
	 * https://leetcode.com/problems/stone-game/discuss/632233/Interview-or-2D-DP-or-Brute-force-Recursion-or-Optimised-using-Bottom-up-DP-or-Explained-in-detail
	 * 
	 * Time Complexity- O(N^2) Space Complexity- O(N^2)
	 * 
	 * Alex[i][j]= Max(arr[i] + Lee[i+1][j], arr[j] + Lee[i][j-1])
	 * Lee[i][j] = Min(Alex[i+1][j], Alex[i][j-1])
	 *  
	 * Base Conditions: 
	 * 1. Since Alex gets first turn with the only pile to pick
	 *    Alex[i][i] = arr[i];
	 *  
	 * 2. Lee won't get any chance to pick any pile if there is only 1 pile
	 *    Lee[i][i] = 0;
	 */
	public boolean stoneGame_BottomUpDp(int[] arr) {
		if (arr == null || arr.length == 0)
			return false;
		
		// Minimum 1 pile guaranteed
		if (arr.length == 1)
			return true;

		int[][] Alex_Score = new int[arr.length][arr.length];
		int[][] Lee_Score = new int[arr.length][arr.length];
		
		// considering all piles individually as base case 
		// (For Lee it would always be 0, so no need to do anything)
		for (int i = 0; i < arr.length; i++)
			Alex_Score[i][i] = arr[i];

		// Now Processing diagonal wise (First picking piles in pairs then triplets
		// then so on and so forth)

		int i = 0, j = 1;

		while (((i < arr.length) && (j < arr.length))) {
			int row = i, col = j;

			for (; (row < arr.length) && (col < arr.length); row++, col++) {
				Alex_Score[row][col] = 
						Math.max(arr[row] + Lee_Score[row + 1][col], 
								arr[col] + Lee_Score[row][col - 1]);
				
				Lee_Score[row][col] = 
						Math.min(Alex_Score[row + 1][col], 
								Alex_Score[row][col - 1]);
			}
			
			j++;
		}

		return Alex_Score[0][arr.length - 1] - Lee_Score[0][arr.length - 1] > 0;
	}
	
	/*
     * The following 2 variables and 2 functions are from this link.
     * https://leetcode.com/problems/stone-game/discuss/154660/Java-This-is-minimax-+-dp-(fully-detailed-explanation-+-generalization-+-easy-understand-code)/565603
     * 
     * 若 first 取 i, 剩下 (i + 1 ... j)
	 * 輪到 second 取，而 second 在 (i + 1 ... j) 是第一個取
	 * 因此 second 取的最終結果是 T[i + 1][j].first
	 * first 則加上  T[i + 1][j].second。
	 * ∴ first = piles[i] + T[i + 1][j].second
	 * second = T[i + 1][j].first
	 * 
	 * 若 first 取 j, 剩下 (i ... j - 1)
	 * 輪到 second 取，而 second 在 (i ... j - 1) 是第一個取
	 * 因此 second 取的最終結果是 T[i][j - 1].first
	 * first 則加上  T[i][j - 1].second。
	 * ∴ first = piles[j] + T[i][j - 1].second
	 * second = T[i][j - 1].first
	 * 
	 * 又因為 first + second = 所取總和 (sum)
	 * 可以只儲存 first, sum - Math.min(T[i + 1][j], T[i][j - 1]) 來計算這回合的 first
	 * 
	 * T[i][j].first = Math.max(piles[i] + T[i + 1][j].second, 
	 *                          piles[j] + T[i][j - 1].second)
	 * T[i][j].second = T[i + 1][j].first or
	 *                  T[i][j - 1].first
	 * 
	 * Rf :
	 * https://www.youtube.com/watch?v=WxpIHvsu1RI
     */
	int[][] cache_topDown6;
	int[] sums_topDown6;

	public boolean stoneGame_topDown6(int[] piles) {
		sums_topDown6 = new int[piles.length];
		
		sums_topDown6[0] = piles[0];
		for (int i = 1; i < piles.length; i++)
			sums_topDown6[i] = sums_topDown6[i - 1] + piles[i];

		cache_topDown6 = new int[piles.length][piles.length];

		int alexGain = helper_topDown6(piles, 0, piles.length - 1);
		return alexGain > sums_topDown6[piles.length - 1] - alexGain;
	}
    
	public int helper_topDown6(int[] piles, int start, int end) {
		if (start > end)
			return 0;
		if (cache_topDown6[start][end] != 0)
			return cache_topDown6[start][end];

		int sum = sums_topDown6[end] - sums_topDown6[start] + piles[start];
		
		// minimize the gain of next player
		cache_topDown6[start][end] = sum
				- Math.min(helper_topDown6(piles, start + 1, end), 
						helper_topDown6(piles, start, end - 1));

		return cache_topDown6[start][end];
	}
	
	/*
	 * https://leetcode.com/problems/stone-game/discuss/154610/DP-or-Just-return-true
	 * 
	 * Alex is first to pick pile.
	 * 
	 * For example,
	 * If Alex wants to pick even indexed piles piles[0], piles[2], ....., piles[n-2],
	 * he picks first piles[0], then Lee can pick either piles[1] or piles[n - 1].
	 * Every turn, Alex can always pick even indexed piles and Lee can only pick odd 
	 * indexed piles.
	 * 
	 * In the description, we know that sum(piles) is odd.
	 * If sum(piles[even]) > sum(piles[odd]), Alex just picks all evens and wins.
	 * If sum(piles[even]) < sum(piles[odd]), Alex just picks all odds and wins.
	 * 
	 * So, Alex always defeats Lee in this game.
	 * 
	 * piles.length is even, which means Alex pick half and Lee pick half. So we have 
	 * many different divisions here.
	 * But to make it simple, we find the fact that sum(piles[even]) and 
	 * sum(piles[odd]) must not be equal.
	 * That's why we want to pick all even indexes or all odd indexes to get the 
	 * larger half.
	 * And Alex always have the right to choose all even indexes or all odd indexes 
	 * because he starts first.
	 * Then Alex can always to get the larger half.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/stone-game/discuss/154610/DP-or-Just-return-true/159875
	 */
	public boolean stoneGame_hack(int[] piles) {
        return true;
    }
	
	/*
	 * https://leetcode.com/problems/stone-game/discuss/261718/Step-by-Step-Recursive-TopDown-BottomUp-and-BottomUp-using-O(n)-space-in-Java
	 * 
	 * 這個解法跟 solution 的解法一樣
	 * 若要推廣到長度為 odd, 需修改 turn 和 si == ei 的賦值 
	 * turn = (piles.length % 2 == 0) ? false : true;
	 * strg[si][ei] = turn ? piles[si] : -piles[si];
	 * 
	 * --------------------------------------------------
	 * 
	 * Consider a boolean variable that will decide if the turn is of Alex or Lee.
	 * 
	 * If it is Alex's Turn then we will find the max number of stones we can get by 
	 * either consuming the first pile from the piles or by consuming the last pile.
	 * 
	 * If it is Lee's turn then we will find the min number of stones we can get by 
	 * decreasing the amount of the Alex's score.
	 * 
	 * si -> Starting Index, ei -> Ending Index
	 * turn -> If True then Alex's turn else Lee's turn
	 * 
	 * Alex will win if score(Alex) >= score(Lee), but this also means 
	 * score(Alex) - score(Lee) >= 0, so here you have a common parameter which is a 
	 * score variable. The score parameter really means 
	 * score = score(Alex) - score(Lee).
	 * 
	 * Since Alex is playing optimally, he wants to maximize the score variable 
	 * because Alex only wins if score = score(Alex) - score(Lee) >= 0. 
	 * Alex should add to the score because he wants to maximize it.
	 * 
	 * Since Lee is also playing optimally, he wants to minimize the score variable, 
	 * since if the score variable becomes negative, Lee has more individual score 
	 * than Alex. But since we have only one variable, Lee should damage the score 
	 * (or in other words, subtract from the score).
	 * 
	 * Rf :
	 * https://leetcode.com/problems/stone-game/discuss/261718/Step-by-Step-Recursive-TopDown-BottomUp-and-BottomUp-using-O(n)-space-in-Java/296926
	 */
	public boolean stoneGame_dp(int[] piles) {
		// To calculate the Answer
		int[][] strg = new int[piles.length][piles.length];
		
		// To store the turn
		boolean turn = false;

		/*
		 * elements below the main diagonal are of no use as there starting index 
		 * is greater than the ending Index
		 */
		for (int slide = 0; slide < piles.length; slide++) {
			for (int si = 0; si < piles.length - slide; si++) {
				int ei = slide + si;
				
				/*
				 * This condition ensures we are on the diagonal and only one pile 
				 * is left. As you know if there are Even Piles and Alex starts the 
				 * Game, then the Last Pile will be of Lee. So We need to subtract 
				 * the stones from the amount of Alex viz zero for now
				 */

				if (si == ei) {
					strg[si][ei] = -piles[si];
					continue;
				}

				// If its Alex's Turn
				if (turn) {
					strg[si][ei] = 
							Math.max(strg[si + 1][ei] + piles[si], 
									strg[si][ei - 1] + piles[ei]);
				} 
				
				// If it is Lee's Turn
				else {
					strg[si][ei] = 
							Math.min(strg[si + 1][ei] - piles[si], 
									strg[si][ei - 1] - piles[ei]);
				}
			}
			
			// Changing the Turn
			turn = !turn;
		}
		
		// Answer will be stored at this Index
		return strg[0][strg.length - 1] > 0;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/stone-game/discuss/154779/Java-Recursion-and-Memoization
	 * 
	 * 這個解法是錯的
	 * 
	 * You actually assume that lee will coordinate with Alex to let Alex win. 
	 * Because when it is lee's round, you are checking if either lee's choice will 
	 * let Alex win, and if yes, lee will choose that option
	 * 
	 * *************************************************************************
	 * 
	 * We know that all the even turns are Alex's and odd turns are Lee's.
	 * 
	 * We can keep two pointers one from start, and one from end. We can select one 
	 * of them first and see what is the result, then select the other one and again 
	 * check what is the result. In any case if Alex wins, we need to return true, 
	 * because he is the first guy to start game. We can do these recursively.
	 * 
	 * However, we know that we repeat same calls. For that, we can have a HashMap. 
	 * Key must be unique for each start&end pair. If you'd like to you can have a 
	 * hash function for them, or basically make the key String and put a comma , in 
	 * between start & end and merge them together.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/stone-game/discuss/154779/Java-Recursion-and-Memoization/189240
	 */
	public boolean stoneGame_topDown4(int[] piles) {
		Map<String, Boolean> map = new HashMap<>();
		return util_topDown4(piles, 0, piles.length - 1, 0, 0, map);
	}

	private Boolean util_topDown4(int[] piles, int start, int end, 
			int alex, int lee, Map<String, Boolean> map) {
		
		if (end < start) {
			return alex > lee;
		}
		
		String key = start + "," + end;
		
		if ((start + end) % 2 == 0) {
			if (!map.containsKey(key)) {
				Boolean val = 
						util_topDown4(piles, start + 1, end, 
								alex + piles[start], lee, map)
						|| util_topDown4(piles, start, end - 1, 
								alex + piles[end], lee, map);
				
				map.put(key, val);
			}
			
			return map.get(key);
		} 
		else {
			if (!map.containsKey(key)) {
				Boolean val = 
						util_topDown4(piles, start + 1, end, 
								alex, lee + piles[start], map)
						|| util_topDown4(piles, start, end - 1, 
								alex, lee + piles[end], map);
				
				map.put(key, val);
			}
			
			return map.get(key);
		}
	}

	/**
     * Python collections
     * 
     * https://leetcode.com/problems/stone-game/discuss/154647/python-solution-using-memorization-with-Chinese-explanation
     * https://leetcode.com/problems/stone-game/discuss/154604/Python-recursive-dp-mini-max
     * https://leetcode.com/problems/stone-game/discuss/169012/Easiest-top-down-DP-approach-with-memoization-(Python)
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/stone-game/discuss/177041/C%2B%2B-solutin-DP
     * https://leetcode.com/problems/stone-game/discuss/298170/C%2B%2B-very-intuitive-solution
     * https://leetcode.com/problems/stone-game/discuss/346757/C%2B%2B-Minimax-or-Game-theory-or-Stone-Game
     */

}
