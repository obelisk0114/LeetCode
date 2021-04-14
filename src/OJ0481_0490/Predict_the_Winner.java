package OJ0481_0490;

import java.util.Arrays;

/*
 * 參考 877. Stone Game 最上面
 * 
 * test cases: 
 * [3,2,10,4]
 * [1,5,2,4,6]
 * [1,3,1]
 * 
 * 若 nums.length 是偶數，先手必勝 (若平手也算先手勝利)
 * 比較 sum(偶數) 和 sum(奇數)，先手可以選擇永遠只取奇數 或是 永遠只取偶數
 * 後手則只能挑另一個，因此最後會變成一方拿走所有奇數，另一方拿走所有偶數
 * 所以可以只以 sum(偶數) 和 sum(奇數) 來判斷先手要如何下
 * 
 * 但是只取 odd 或 even 不一定是 optimal
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

public class Predict_the_Winner {
	/*
	 * https://leetcode.com/problems/predict-the-winner/discuss/155217/From-Brute-Force-to-Top-down-DP/552174
	 * 
	 * First player has option to choose i or j. 
	 * 
	 * If he chooses i then 2nd player to choose in (i + 1, j) 
	 * - if 2nd player chooses i + 1, then player 1 will next choose from (i + 2, j) 
	 * - if 2nd player chooses j, then player 1 will next choose from (i + 1, j - 1) 
	 * 
	 * If he chooses j then 2nd player to choose in (i, j - 1) 
	 * - if 2nd player chooses i, then player 1 will next choose from (i + 1, j - 1) 
	 * - if 2nd player chooses j - 1, then player 1 will next choose from (i, j - 2)
	 * 
	 * We know that player 2 would have played wisely and player 1 will get the 
	 * minimum in the next move. We choose the best(Max) of the above 2 scenarios
	 * 
	 * can return dp[0][n - 1] >= dp[1][n - 1] || dp[0][n - 1] >= dp[0][n - 2]
	 * 
	 * Rf :
	 * https://leetcode.com/problems/predict-the-winner/discuss/96828/JAVA-9-lines-DP-solution-easy-to-understand-with-improvement-to-O(N)-space-complexity./197677
	 * https://leetcode.com/problems/predict-the-winner/discuss/96828/JAVA-9-lines-DP-solution-easy-to-understand-with-improvement-to-O(N)-space-complexity./303331
	 * https://leetcode.com/problems/predict-the-winner/discuss/96829/DP-O(n2)-+-MIT-OCW-solution-explanation/101362
	 * 
	 * Other code:
	 * https://leetcode.com/problems/predict-the-winner/discuss/96828/JAVA-9-lines-DP-solution-easy-to-understand-with-improvement-to-O(N)-space-complexity./101344
	 */
	public boolean PredictTheWinner_dp_len(int[] nums) {
		int n = nums.length;

		// dp[i][j] -> score of the first player for picks between nums[i..j]
		int[][] dp = new int[n][n];

		// total of nums
		int total = 0;
		for (int i = 0; i < n; i++) {
			total += nums[i];
		}

		for (int len = 1; len <= n; len++) {
			for (int i = 0; i <= n - len; i++) {
				int j = i + len - 1;

				int a = (i + 1 < n && j - 1 >= 0) ? dp[i + 1][j - 1] : 0;
				int b = (i + 2 < n) ? dp[i + 2][j] : 0;
				int c = (j - 2 >= 0) ? dp[i][j - 2] : 0;

				dp[i][j] = Math.max(nums[i] + Math.min(a, b), 
									nums[j] + Math.min(a, c));
			}
		}

		/*
		 * dp[0][n-1] will have the score for the first player.
		 */
		int player1Score = dp[0][n - 1];
		
		// player2Score = Math.min(dp[1][n - 1], dp[0][n - 2]);
		int player2Score = total - player1Score;
		return player1Score >= player2Score;
	}
	
	/*
	 * Modify by myself
	 * 
	 * Alex and Lee take turns, with Alex starting first.
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
	public boolean PredictTheWinner_dp4_self_modify(int[] piles) {
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
	 * The following 4 functions are from this link.
	 * https://leetcode.com/problems/predict-the-winner/discuss/155217/From-Brute-Force-to-Top-down-DP
	 * 
	 * 1st with choosable i, j,
	 * 1. if 1st picks nums[i], 2nd can pick either ends of nums[i + 1, j]
	 *    a. if 2nd picks nums[i + 1], 1st can pick either ends of nums[i + 2, j]
	 *    b. if 2nd picks nums[j], 1st can pick either ends of nums[i + 1, j - 1]
	 *    since 2nd plays to maximize his score, 1st can get nums[i] + min(1.a, 1.b)
	 *    
	 * 2.if 1st picks nums[j], 2nd can pick either ends of nums[i, j - 1]
	 *    a. if 2nd picks nums[i], 1st can pick either ends of nums[i + 1, j - 1];
	 *    b. if 2nd picks nums[j - 1], 1st can pick either ends of nums[i, j - 2];
	 *    since 2nd plays to maximize his score, 1st can get nums[j] + min(2.a, 2.b)
	 *    
	 * since the 1st plays to maximize his score, 1st can get 
	 * max(nums[i] + min(1.a, 1.b), nums[j] + min(2.a, 2.b));
	 * 
	 * Rf :
	 * https://leetcode.com/problems/predict-the-winner/discuss/96829/DP-O(n2)-+-MIT-OCW-solution-explanation/101362
	 * https://leetcode.com/problems/predict-the-winner/discuss/155217/From-Brute-Force-to-Top-down-DP/654074
	 * https://leetcode.com/problems/predict-the-winner/discuss/96828/JAVA-9-lines-DP-solution-easy-to-understand-with-improvement-to-O(N)-space-complexity./197677
	 * 
	 * Other code:
	 * https://leetcode.com/problems/predict-the-winner/discuss/96829/DP-O(n2)-+-MIT-OCW-solution-explanation/101358
	 */
	public boolean PredictTheWinner_topDown(int[] nums) {
		int[][] memo = buildMemo_topDown(nums.length);

		int scoreFirst = predictTheWinnerFrom_topDown(nums, 0, nums.length - 1, memo);
		int scoreTotal = getTotalScores_topDown(nums);

		return scoreFirst >= scoreTotal - scoreFirst;
	}

	private int predictTheWinnerFrom_topDown(int[] nums, int i, int j, int[][] memo) {
		// If the current player is in a situation i > j, i.e. no choice for him, 
		// the game ends already; he simply gets 0 score for this round.
		if (i > j) {
			return 0;
		}

		// If the current player is in a situation i == j, i.e. only one choice 
		// for him, he simply gets `nums[i]` score.
		if (i == j) {
			return nums[i];
		}

		if (memo[i][j] != -1) {
			return memo[i][j];
		}

		int curScore = Math.max(
				nums[i] + Math.min(predictTheWinnerFrom_topDown(nums, i + 2, j, memo),
						predictTheWinnerFrom_topDown(nums, i + 1, j - 1, memo)),
				nums[j] + Math.min(predictTheWinnerFrom_topDown(nums, i, j - 2, memo),
						predictTheWinnerFrom_topDown(nums, i + 1, j - 1, memo)));

		return memo[i][j] = curScore;
	}

	private int getTotalScores_topDown(int[] nums) {
		int scoreTotal = 0;
		
		for (int num : nums) {
			scoreTotal += num;
		}

		return scoreTotal;
	}

	private int[][] buildMemo_topDown(int n) {
		int[][] memo = new int[n][n];

		for (int[] memoRow : memo)
			Arrays.fill(memoRow, -1);
		
		return memo;
	}

	/*
	 * The following 2 functions are modify by myself.
	 * 
	 * Assume that P(i, j) denotes the maximum amount of money you can win when the 
	 * remaining coins are { A_i, …, A_j }, and it is your turn now. 
	 * 
	 * 1. A solution
	 * 
	 * You have two choices, either take A_i or A_j. First, let us focus on the case 
	 * where you take A_i, so that the remaining coins become { A_i+1 … A_j }. Since 
	 * the opponent is as smart as you, he must choose the best way that yields the 
	 * maximum for him, where the maximum amount he can get is denoted by P(i+1, j).
	 * 
	 * + if you choose A_i, the maximum amount you can get is:
	 *   P1 = Sum{A_i ... A_j} - P(i+1, j)
	 * + if you choose A_j, the maximum amount you can get is:
	 *   P2 = Sum{A_i ... A_j} - P(i, j-1)
	 * 
	 * P(i, j) = max { P1, P2 } 
	 * = max { Sum{A_i ... A_j} - P(i+1, j), Sum{A_i ... A_j} - P(i, j-1) }
	 * = Sum{A_i ... A_j} - min { P(i+1, j), P(i, j-1) }
	 * 
	 * We can store values of Sum{A_i … A_j} in a table and avoid re-computations by 
	 * computing in a certain order.
	 * 
	 * 2. Another better solution
	 * 
	 * Let us rewind back to the case where you take A_i, and the remaining coins 
	 * become { A_i+1 … A_j }.
	 * 
	 * Let us look one extra step ahead this time by considering the two coins the 
	 * opponent will possibly take, A_i+1 and A_j. If the opponent takes A_i+1, the 
	 * remaining coins are { A_i+2 … A_j }, which our maximum is denoted by P(i+2, j). 
	 * On the other hand, if the opponent takes A_j, our maximum is P(i+1, j-1). 
	 * Since the opponent is as smart as you, he would have chosen the choice that 
	 * yields the minimum amount to you.
	 * 
	 * + the maximum amount you can get when you choose A_i is:
	 *   P1 = A_i + min { P(i+2, j), P(i+1, j-1) }
	 * + the maximum amount you can get when you choose A_j is:
	 *   P2 = A_j + min { P(i+1, j-1), P(i, j-2) }
	 * 
	 * P(i, j) = max { P1, P2 }
	 * = max { A_i + min { P(i+2, j),   P(i+1, j-1) },
     *         A_j + min { P(i+1, j-1), P(i,   j-2) } }
	 * 
	 * Each recursive call branches into a total of four separate recursive calls, and 
	 * it could be n levels deep from the very first call
	 * 
	 * Rf :
	 * http://web.archive.org/web/20171114154153/http://articles.leetcode.com/coins-in-line
	 */
	public boolean PredictTheWinner_article_coin_in_a_line(int[] nums) {
        int N = nums.length;
        int[][] P = new int[N][N];
        
        for (int i = 0; i < N; i++) {
            for (int m = 0, n = i; n < N; m++, n++) {
                int a = (m+2 <= N-1)             ? P[m+2][n] : 0;
                int b = (m+1 <= N-1 && n-1 >= 0) ? P[m+1][n-1] : 0;
                int c = (n-2 >= 0)               ? P[m][n-2] : 0;
                P[m][n] = Math.max(nums[m] + Math.min(a,b), nums[n] + Math.min(b,c));
            }
        }
        
        /*
        int sum = 0;
        for (int i = 0; i < N; i++) {
            System.out.println(Arrays.toString(P[i]));
            sum += nums[i];
        }
        */
        
        return printMoves(P, nums, N);
    }
    
    public boolean printMoves(int[][] P, int[] A, int N) {
        int sum1 = 0, sum2 = 0;
        int m = 0, n = N-1;
        boolean myTurn = true;
        
        while (m <= n) {
            int P1 = (m+1 < N) ? P[m+1][n] : 0; // If take A[m], opponent can get...
            int P2 = (n-1 >= 0) ? P[m][n-1] : 0; // If take A[n]
            System.out.print( (myTurn ? "I" : "You") + " take coin no. " );
            
            if (P1 <= P2) {
                System.out.print( (m+1) + " (" + A[m] + ")" );
                
                if (myTurn) {
                	sum1 += A[m];
                }
                else {
                	sum2 += A[m];
                }
                
                m++;
            } 
            else {
                System.out.print( (n+1) + " (" + A[n] + ")" );
                
                if (myTurn) {
                	sum1 += A[n];
                }
                else {
                	sum2 += A[n];
                }
                
                n--;
            }
            
            System.out.print( myTurn ? ", " : ".\n" );
            myTurn = !myTurn;
        }
        
        System.out.print( "\n\nThe total amount of money (maximum) I get is " 
        		+ P[0][N-1] + ".\n" );
        System.out.print( "The total amount of money (maximum) You get is " 
        		+ sum2 + ".\n" );
        
        if (sum1 >= sum2) {
        	return true;
        }
        else {
        	return false;
        }
    }
    
    /*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/stone-game/discuss/154660/Java-This-is-minimax-%2B-dp-(fully-detailed-explanation-%2B-generalization-%2B-easy-understand-code)
	 * 
	 * Alex and Lee take turns, with Alex starting first.
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

	public boolean PredictTheWinner_topDown5(int[] piles) {
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
	 * Modify by myself
	 * 
     * dp[i, j] = 剩下 i, i+1, ..., j 時，Max(第一個人分數 - 第二個人分數) 
	 * 
	 * 輪到第一個人時, 他可以拿 piles[i] 或 piles[j], 此時 dp[i][j] 增加該數量 
     * dp[i][j] = piles[i] + dp(i+1, j), or piles[j] + dp(i, j-1);
     * 他希望最大化 (第一個人分數 - 第二個人分數)
     * 
     * 輪到第二個人時, 他可以拿 piles[i] 或 piles[j], 此時 dp[i][j] 減少該數量. 
     * dp[i][j] = -piles[i] + dp(i+1, j), or -piles[j] + dp(i, j-1);
     * 他希望最小化 (第一個人分數 - 第二個人分數)
	 * 
	 * Rf :
	 * https://leetcode.com/problems/stone-game/solution/
     * Approach 1: Dynamic Programming
	 */
	public boolean PredictTheWinner_stoneGame_solution_modify(int[] piles) {
		int N = piles.length;

		int[][] dp = new int[N][N];
		
		// 輪到第一個人取
        boolean first = N % 2 == 0 ? false : true;
		
		for (int diff = 0; diff < N; diff++) {
			for (int i = 0; i + diff < N; i++) {
				int j = i + diff;
				
				if (first) {
                    if (diff == 0) {
                        dp[i][j] = piles[i];
                    }
                    else {
                        dp[i][j] = 
                        		Math.max(piles[i] + dp[i + 1][j], 
                        				piles[j] + dp[i][j - 1]);
                    }
				}
				else {
                    if (diff == 0) {
                        dp[i][j] = -piles[i];
                    }
                    else {
                        dp[i][j] = 
                        		Math.min(-piles[i] + dp[i + 1][j], 
                        				-piles[j] + dp[i][j - 1]);
                    }
				}
			}
            
            first = !first;
        }

		return dp[0][N - 1] >= 0;
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
	public boolean PredictTheWinner_dp_pair(int[] piles) {
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
		
		return memo[0][n - 1].first >= memo[0][n - 1].second;
	}

	class Pair_dp_pair {
		int first = 0, second = 0;

		public String toString() {
			return first + " " + second;
		}
	}
	
	/*
     * https://leetcode.com/problems/stone-game/solution/
     * Approach 1: Dynamic Programming
     * 
     * Alex and Lee take turns, with Alex starting first.
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
	public boolean PredictTheWinner_stoneGame_solution(int[] piles) {
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

		return dp[1][N] >= 0;
	}
	
	/*
	 * https://leetcode.com/problems/predict-the-winner/discuss/96828/JAVA-9-lines-DP-solution-easy-to-understand-with-improvement-to-O(N)-space-complexity.
	 * 
	 * Alex and Lee take turns, with Alex starting first.
	 * 
	 * if (j - i) is odd, now Alex pick, dp[i][j] means max(alex stone - lee stone)
	 * if (j - i) is even, now Lee pick, dp[i][j] means max(lee stone - alex stone)
	 * 
	 * ---------------------------------------------------------
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
	 * ---------------------------------------------------------
	 * 
	 * The dp[i][j] saves how much more scores that the first-in-action player will 
	 * get from i to j than the second player. First-in-action means whomever moves 
	 * first. 
	 * 
	 * Suppose they are picking up numbers from position i to j in the array and it 
	 * is player A's turn to pick the number now. If player A picks position i, 
	 * player A will earn nums[i] score instantly. Then player B will choose from 
	 * i + 1 to j. dp[i + 1][j] already saves how much more score that the 
	 * first-in-action player will get from i + 1 to j than the second player. So it 
	 * means that player B will eventually earn dp[i + 1][j] more score from i + 1 
	 * to j than player A. So if player A picks position i, eventually player A will 
	 * get nums[i] - dp[i + 1][j] more score than player B after they pick up all 
	 * numbers. Similarly, if player A picks position j, player A will earn 
	 * nums[j] - dp[i][j - 1] more score than player B after they pick up all 
	 * numbers. Since A is smart, A will always choose the max in the two options, so:
	 * dp[i][j] = Math.max(nums[i] - dp[i + 1][j], nums[j] - dp[i][j - 1]);
	 * 
	 * We can start from dp[i][i], where dp[i][i] = nums[i].
	 * The outer loop use len to denote how far the block is away from the diagonal. 
	 * So len ranges from 1 to n - 1. The inner loop is all valid i positions.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/stone-game/discuss/154610/DP-or-Just-return-true/160075
	 * https://leetcode.com/problems/stone-game/discuss/154610/DP-or-Just-return-true/160366
	 */
	public boolean PredictTheWinner_dp(int[] nums) {
		int n = nums.length;
		int[][] dp = new int[n][n];
		
		for (int i = 0; i < n; i++) {
			dp[i][i] = nums[i];
		}
		
		for (int len = 1; len < n; len++) {
			for (int i = 0; i < n - len; i++) {
				int j = i + len;
				dp[i][j] = Math.max(nums[i] - dp[i + 1][j], nums[j] - dp[i][j - 1]);
			}
		}
		return dp[0][n - 1] >= 0;
	}
	
	/*
	 * https://leetcode.com/problems/predict-the-winner/discuss/96828/JAVA-9-lines-DP-solution-easy-to-understand-with-improvement-to-O(N)-space-complexity.
	 * 
	 * The dp[i][j] saves how much more scores that the first-in-action player will 
	 * get from i to j than the second player. First-in-action means whomever moves 
	 * first. 
	 * 
	 * Pick up numbers from position i to j, player A's turn to pick the number now. 
	 * If player A picks i, player A will earn nums[i] score instantly. Then player B 
	 * will choose from i + 1 to j. dp[i + 1][j] already saves how much more score 
	 * that the first-in-action player will get from i + 1 to j than the second player. 
	 * So player B will eventually earn dp[i + 1][j] more score from i + 1 to j than 
	 * player A. So if player A picks i, eventually player A will get 
	 * nums[i] - dp[i + 1][j] more score than player B after they pick up all numbers. 
	 * Similarly, if player A picks j, player A will earn nums[j] - dp[i][j - 1] more 
	 * score than player B after they pick up all numbers. Since A is smart, A will 
	 * always choose the max in those two options, so:
	 * dp[i][j] = Math.max(nums[i] - dp[i + 1][j], nums[j] - dp[i][j - 1]);
	 * 
	 * We can start from dp[i][i], where dp[i][i] = nums[i].
	 * The outer loop use len to denote how far the block is away from the diagonal. 
	 * So len ranges from 1 to n - 1. The inner loop is all valid i positions.
	 * 
	 * We may use only 1D dp[i] and we start to fill the table at the bottom right 
	 * corner where dp[4] = nums[4]. On the next step, we start to fill the second to 
	 * the last line, where it starts from dp[3] = nums[3]. Then 
	 * dp[4] = Math.max(nums[4] - dp[3], nums[3] - dp[4]). Then we fill the third to 
	 * the last line where dp[2] = nums[2] and so on... Eventually after we fill the 
	 * first line and after the filling, dp[4] will be the answer.
	 */
	public boolean PredictTheWinner_dp_n(int[] nums) {
		if (nums == null) {
			return true;
		}
		
		int n = nums.length;
		if ((n & 1) == 0) {
			return true;
		}
		
		int[] dp = new int[n];
		for (int i = n - 1; i >= 0; i--) {
			for (int j = i; j < n; j++) {
				if (i == j) {
					dp[i] = nums[i];
				} 
				else {
					dp[j] = Math.max(nums[i] - dp[j], nums[j] - dp[j - 1]);
				}
			}
		}
		
		return dp[n - 1] >= 0;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/predict-the-winner/discuss/96838/Java-'1-Line'-Recursive-Solution-O(n2)-Time-and-O(n)-Space
	 * 
	 * Assuming the sum of the array is SUM, so eventually player1 and player2 will 
	 * split the SUM between themselves. For player1 to win, he/she has to get more 
	 * than what player2 gets. If we think from the prospective of one player, then 
	 * what he/she gains each time is a plus, while, what the other player gains each 
	 * time is a minus. Eventually if player1 can have a >0 total, player1 can win.
	 * 
	 * Helper function simulate this process. In each round:
	 * if e == s, there is no choice but have to select nums[s]
	 * otherwise, this current player has 2 options:
	 * --> nums[s] - helper(nums, s + 1, e): this player select the front item, 
	 *     leaving the other player a choice from s + 1 to e
	 * --> nums[e] - helper(nums, s, e - 1): this player select the tail item, 
	 *     leaving the other player a choice from s to e - 1
	 * Then take the max of these two options as this player's selection, return it.
	 * 
	 * -------------------------------------------------------------
	 * 
	 * If the current turn belongs to Player 1, we pick up an element x, from either 
	 * end, and give the turn to Player 2. Thus, if we obtain the score for the 
	 * remaining elements(leaving x), this score, now belongs to Player 2. Since 
	 * Player 2 is competing against Player 1, this score should be subtracted from 
	 * Player 1's current(local) score(x) to obtain the effective score of Player 1 
	 * at the current instant.
	 * 
	 * We can subtract Player 1's score for the remaining subarray from Player 2's 
	 * current score to obtain its effective score.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/predict-the-winner/solution/
	 * Approach #2 Similar Approach
	 */
	public boolean PredictTheWinner_top_down2(int[] nums) {
		return helper_top_down2(nums, 0, nums.length - 1, 
				new Integer[nums.length][nums.length]) >= 0;
	}

	private int helper_top_down2(int[] nums, int s, int e, Integer[][] mem) {
		if (mem[s][e] == null)
			mem[s][e] = s == e ? nums[e]
					: Math.max(nums[e] - helper_top_down2(nums, s, e - 1, mem),
							nums[s] - helper_top_down2(nums, s + 1, e, mem));
			
		return mem[s][e];
	}
	
	/*
	 * https://leetcode.com/problems/predict-the-winner/discuss/96901/java-DP-solution-with-explanation
	 * 
	 * If the first player choose nums[0], the max he can get is 
	 * sum(nums) - max[1, end] (which is the max for the second player). 
	 * If the first player choose the last element in nums, then the max he can get is 
	 * sum(nums) - max[0, end-1] (which is the max for the second player). 
	 * Thus, the DP formula is 
	 * DP[start][end] = Max(sum - dp[start][end - 1], sum - dp[start + 1][end]).
	 * 
	 * sum in the DP formula is not the total sum for nums, but the sum for 
	 * nums[start, end].
	 */
	public boolean PredictTheWinner_max_current_player(int[] nums) {
		int length = nums.length;

		int sum = 0;
		for (int num : nums)
			sum += num;

		int[][] dp = new int[length][length];

		for (int j = 0; j < length; j++) {
			int curSum = 0;
			for (int i = j; i >= 0; i--) {
				curSum += nums[i];
				
				if (i == j)
					dp[i][j] = nums[j];
				else {
					dp[i][j] = Math.max(curSum - dp[i][j - 1], curSum - dp[i + 1][j]);
				}
			}
		}
		return dp[0][length - 1] * 2 >= sum;
	}

	/*
	 * https://leetcode.com/problems/predict-the-winner/discuss/96843/Java-DP-solution-with-explanation
	 * 
	 * dp[i][j] = the maximum score of player1 on subarray nums[i..j]
	 * 
	 * Player1 can choose either nums[i] or nums[j]. If nums[i] is chosen, player2 
	 * will also make best effort to get dp[i+1][j]. So for the subarray 
	 * nums[i+1] ... nums[j], player1 can get:
	 * 
	 * nums[i + 1] + nums[i + 2] + ... + nums[j] - dp[i+1][j], which is
	 * sum(nums[i+1] to nums[j]) - dp[i+1][j]
	 * 
	 * sum[0] to 0, sum[i] is the sum of all elements in nums before index i.
	 * 
	 * dp[i][j] = max { sum[j+1] - sum[i+1] - dp[i+1][j] + nums[i], 
	 *                  sum[j] - sum[i] - dp[i][j-1] + nums[j] }
	 * 
	 * => max { sum[j+1] - sum[i] - dp[i+1][j], sum[j+1] - sum[i] - dp[i][j-1] }
	 * 
	 * sum[j+1] - sum[i] is the sum of the rest elements, since dp[i+1][j] and 
	 * dp[i][j-1] is two choices that player 2 can make, the larger one between 
	 * (sum[j+1] - sum[i] - dp[i+1][j]) and (sum[j+1] - sum[i] - dp[i][j-1]) is the 
	 * best move player 1 can take.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/predict-the-winner/discuss/96843/Java-DP-solution-with-explanation/101408
	 * 
	 * Other code:
	 * https://leetcode.com/problems/predict-the-winner/discuss/96901/java-DP-solution-with-explanation/116623
	 */
	public boolean PredictTheWinner_max_current_player_prefix_sum(int[] nums) {
		if (nums.length <= 2)
			return true;
		
		int n = nums.length;
		
		int[] sum = new int[n + 1];
		sum[0] = 0;
		for (int i = 1; i <= n; i++) {
			sum[i] = sum[i - 1] + nums[i - 1];
		}

		int[][] dp = new int[n][n];
		for (int len = 1; len < n; len++) {
			for (int i = 0; i + len < n; i++) {
				int j = i + len;
				
				// if (len == 0) 
				//     dp[i][j] = nums[i];
				if (len == 1)
					dp[i][j] = Math.max(nums[i], nums[j]);
				else {
					int can1 = sum[j + 1] - sum[i + 1] - dp[i + 1][j] + nums[i];
					int can2 = sum[j] - sum[i] - dp[i][j - 1] + nums[j];
					dp[i][j] = Math.max(can1, can2);
				}
			}
		}
		
		return sum[n] - dp[0][n - 1] <= dp[0][n - 1];
	}
	
	/*
	 * The following 3 functions are from this link.
	 * https://leetcode.com/problems/predict-the-winner/discuss/155217/From-Brute-Force-to-Top-down-DP
	 * 
	 * 1st with choosable i, j,
	 * 1. if 1st picks nums[i], 2nd can pick either ends of nums[i + 1, j]
	 *    a. if 2nd picks nums[i + 1], 1st can pick either ends of nums[i + 2, j]
	 *    b. if 2nd picks nums[j], 1st can pick either ends of nums[i + 1, j - 1]
	 *    since 2nd plays to maximize his score, 1st can get nums[i] + min(1.a, 1.b)
	 *    
	 * 2.if 1st picks nums[j], 2nd can pick either ends of nums[i, j - 1]
	 *    a. if 2nd picks nums[i], 1st can pick either ends of nums[i + 1, j - 1];
	 *    b. if 2nd picks nums[j - 1], 1st can pick either ends of nums[i, j - 2];
	 *    since 2nd plays to maximize his score, 1st can get nums[j] + min(2.a, 2.b)
	 *    
	 * since the 1st plays to maximize his score, 1st can get 
	 * max(nums[i] + min(1.a, 1.b), nums[j] + min(2.a, 2.b));
	 */
	public boolean PredictTheWinner_recur4(int[] nums) {
		int scoreFirst = predictTheWinnerFrom_recur4(nums, 0, nums.length - 1);
		int scoreTotal = getTotalScores_recur4(nums);

		// Compare final scores of two players.
		return scoreFirst >= scoreTotal - scoreFirst;
	}

	private int predictTheWinnerFrom_recur4(int[] nums, int i, int j) {
		if (i > j) {
			return 0;
		}
		if (i == j) {
			return nums[i];
		}

		int curScore = Math.max(
				nums[i] + Math.min(predictTheWinnerFrom_recur4(nums, i + 2, j), 
									predictTheWinnerFrom_recur4(nums, i + 1, j - 1)),
				nums[j] + Math.min(predictTheWinnerFrom_recur4(nums, i, j - 2), 
									predictTheWinnerFrom_recur4(nums, i + 1, j - 1)));
		
		return curScore;
	}

	private int getTotalScores_recur4(int[] nums) {
		int scoreTotal = 0;
		
		for (int num : nums) {
			scoreTotal += num;
		}

		return scoreTotal;
	}

	/*
	 * The following 2 functions are modified from this link.
	 * https://leetcode.com/problems/predict-the-winner/discuss/96884/Java-7ms-recursion-Solution-with-explaination-easy-to-understand
	 * 
	 * test cases: [1,5,2,4,6]
	 * Expected: true
	 */
	public boolean PredictTheWinner_recur2(int[] nums) {
		if (nums.length <= 1) {
			return true;
		}
		
		return canWin_recur2(nums, 0, nums.length - 1, 0, 0, true);
	}

	private boolean canWin_recur2(int[] nums, int left, int right, 
			int fistScore, int secondScore, boolean first) {
		
		// assume fistScore is the score of current player (to pick in this round)
		if (left > right) {
			if (first) {
				return fistScore >= secondScore;
			}
			else {
				return fistScore > secondScore;
			}
		}
		
		// pick left
		fistScore += nums[left++];
		if (!canWin_recur2(nums, left, right, secondScore, fistScore, !first)) {
			
			// check if next player can win. 
			// if next player cannot win, return true,
			// which means the current player can win
			return true;
		}
		
		// backtrack
		left--;
		fistScore -= nums[left];
		
		// pick right;
		fistScore += nums[right--];
		if (!canWin_recur2(nums, left, right, secondScore, fistScore, !first)) {
			
			// check if next player can win
			return true;
		}
		
		right++;
		fistScore -= nums[right];
		return false;
	}

	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/predict-the-winner/discuss/704868/Java-Simple-Recursion
	 * 
	 * p1 is player 1 score minus player 2
	 * 
	 * Rf :
	 * https://leetcode.com/problems/predict-the-winner/discuss/704868/Java-Simple-Recursion/781333
	 */
	public boolean PredictTheWinner_recur3(int[] nums) {
		return dfs_recur3(0, nums.length - 1, 0, nums, true) >= 0;
	}

	public int dfs_recur3(int start, int end, int p1, int[] nums, boolean p1Turn) {
		/*
		 * 這段 base condition 可以改成這樣
		 * 
		if (start == end) {
			if (p1Turn) {
				return p1 + nums[start];
			}
			else {
				return p1 - nums[start];
			}
		}
		*/
		if (start > end) {
			return p1;
		}

		if (p1Turn) {
			int bottom = dfs_recur3(start + 1, end, p1 + nums[start], nums, false);
			int top = dfs_recur3(start, end - 1, p1 + nums[end], nums, false);

			// this is the best play at this score
			return Math.max(top, bottom);
		} 
		else {
			int bottom = dfs_recur3(start + 1, end, p1 - nums[start], nums, true);
			int top = dfs_recur3(start, end - 1, p1 - nums[end], nums, true);
			
			return Math.min(bottom, top);
		}
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/predict-the-winner/solution/
	 * 
	 * Approach #1 Using Recursion
	 * 
	 * For the Player 1 to be the winner, we need score_Player1 >= score_Player2. Or 
	 * in other terms, score_Player1 - score_Player2 >= 0.
	 * 
	 * For the turn of Player 1, we can add its score obtained to the total score and 
	 * for Player 2's turn, we can subtract its score from the total score. At the 
	 * end, we can check if the total score is greater than or equal to zero(equal 
	 * score of both players)
	 * 
	 * Recursive function winner(nums, s, e, turn) which predicts the winner for the 
	 * nums array as the score array with the elements in the range of indices [s, e] 
	 * currently being considered, given a particular player's turn, indicated by 
	 * turn = 1 being Player 1's turn and turn = -1 being the Player 2's turn.
	 * 
	 * Since both the players are assumed to be playing smartly and making the best 
	 * move at every step, both will tend to maximize their scores. Thus, we can make 
	 * use of the same function winner to determine the maximum score possible for 
	 * any of the players. At every step of the recursive process, we determine the 
	 * maximum score possible for the current player.
	 * 
	 * If it is Player 1's turn, we add the current number's score to the total 
	 * score, otherwise, we need to subtract the same.
	 * 
	 * Thus, at every step, we need update the search space appropriately based on 
	 * the element chosen and also invert the turn's value to indicate the turn 
	 * change among the players and either add or subtract the current player's score 
	 * from the total score available to determine the end result.
	 * 
	 * Further, note that the value returned at every step is given by 
	 * turn * max(turn * a, turn * b). This is equivalent to the statement max(a, b) 
	 * for Player 1's turn and min(a, b) for Player 2's turn.
	 * 
	 * Looking from Player 1's perspective, for any move made by Player 1, it tends to 
	 * leave the remaining subarray in a situation which minimizes the best score 
	 * possible for Player 2, even if it plays in the best possible manner. But, when 
	 * the turn passes to Player 1 again, for Player 1 to win, the remaining subarray 
	 * should be left in a state such that the score obtained from this subarrray is 
	 * maximum(for Player 1).
	 */
	public boolean PredictTheWinner_recur5(int[] nums) {
		return winner_recur5(nums, 0, nums.length - 1, 1) >= 0;
	}

	public int winner_recur5(int[] nums, int s, int e, int turn) {
		if (s == e)
			return turn * nums[s];
		
		int a = turn * nums[s] + winner_recur5(nums, s + 1, e, -turn);
		int b = turn * nums[e] + winner_recur5(nums, s, e - 1, -turn);
		return turn * Math.max(turn * a, turn * b);
	}

	/*
	 * The following 2 functions are modified from this link.
	 * https://leetcode.com/problems/predict-the-winner/discuss/210085/Two-C%2B%2B-Solution
	 * 
	 * If it is player 1's turn, use ||, if it is player 2s', use &&.
	 * 
	 * It means in 1's turn, if there is one solution, it is good enough to win, 
	 * because you have the chance to pick that solution, however, in 2's turn, you 
	 * don't have a chance to pick, so only if "1 will win at this round no matter 
	 * what 2 picks", 1 will win in the final, so use &&.
	 */
	public boolean PredictTheWinner_and_or(int[] nums) {
		if (nums.length < 3)
			return true;
		
		return helper_and_or(0, nums.length - 1, 1, 0, nums);
	}

	private boolean helper_and_or(int left, int right, int turn, 
			int val, int[] nums) {
		
		if (left == right)
			return (turn * nums[left] + val) >= 0;
			
		if (turn == 1) {
			return helper_and_or(left + 1, right, -turn, 
							val + turn * nums[left], nums)
					|| helper_and_or(left, right - 1, -turn, 
							val + turn * nums[right], nums);
		} 
		else {
			return helper_and_or(left + 1, right, -turn, 
							val + turn * nums[left], nums)
					&& helper_and_or(left, right - 1, -turn, 
							val + turn * nums[right], nums);
		}
	}
	
	/*
	 * The following 3 functions are from this link.
	 * https://leetcode.com/problems/predict-the-winner/discuss/96875/Share-my-9ms-recursive-solution-with-explanation
	 * 
	 * 對手只要會輸，就表示我可以選擇那個方法，因此 return true;
	 * 
	 * If there exist that my opponent will lose the game (false), return true, 
	 * that's why I use '!' in the return;
	 * 
	 * [1, 5, 2]
	 * if (player1 pick 1) the rest is [5, 2];
	 *     if (player2 pick 2) player1 win(true),
	 *     if (player2 pick 5) player1 lose(false),
	 * -> because player2 play optimally, so he choose to pick 5,
	 *    player2 = !(pick 2) || !(pick 5)  = !true || !false = true;
	 *    so when player1 first choose 1, he always loses.
	 */
	public boolean PredictTheWinner_recur(int[] nums) {
		return first_recur(0, 0, nums, 0, nums.length - 1);
	}

	private boolean first_recur(int s1, int s2, int[] nums, int start, int end) {
		// 全部選完，先手獲勝
		if (start > end) {
			if (s1 >= s2)
				return true;
			else
				return false;
		}
		
		return !second_recur(s1 + nums[start], s2, nums, start + 1, end)
				|| !second_recur(s1 + nums[end], s2, nums, start, end - 1);
	}

	private boolean second_recur(int s1, int s2, int[] nums, int start, int end) {
		// 全部選完，後手獲勝
		if (start > end) {
			if (s1 < s2)
				return true;
			else
				return false;
		}
		
		return !first_recur(s1, s2 + nums[start], nums, start + 1, end)
				|| !first_recur(s1, s2 + nums[end], nums, start, end - 1);
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/predict-the-winner/discuss/414803/Python-AC-98-Both-Recursion-and-DP-with-detailed-explanation.
     * https://leetcode.com/problems/predict-the-winner/discuss/171774/Easy-recursive-Python-solution-that-beats-90
     * https://leetcode.com/problems/predict-the-winner/discuss/96833/Python-DP-solution-32ms-bottom-up-with-explanation
     * https://leetcode.com/problems/predict-the-winner/discuss/478727/Python-game-theory-%2B-dp-faster-than-100-with-some-game-theoretic-explanation
     * https://leetcode.com/problems/predict-the-winner/discuss/151676/Python-dp-with-memorization
     * https://leetcode.com/problems/predict-the-winner/discuss/96841/easy-python-dp-solution-without-dict
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/predict-the-winner/discuss/96829/DP-O(n2)-%2B-MIT-OCW-solution-explanation
     * https://leetcode.com/problems/predict-the-winner/discuss/600194/Simple-c%2B%2B-Solution-Thoroughly-Explained
     * https://leetcode.com/problems/predict-the-winner/discuss/210085/Two-C%2B%2B-Solution
     * https://leetcode.com/problems/predict-the-winner/discuss/96832/C%2B%2B-DP-solution-with-explanation
     * https://leetcode.com/problems/predict-the-winner/discuss/96835/Clean-3ms-C%2B%2B-DP-solution-with-detailed-explanation
     * https://leetcode.com/problems/predict-the-winner/discuss/96857/C-1-line-recursive-solution
     */

}
