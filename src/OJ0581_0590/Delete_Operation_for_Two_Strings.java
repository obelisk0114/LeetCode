package OJ0581_0590;

import java.util.Arrays;

public class Delete_Operation_for_Two_Strings {
	/*
	 * https://leetcode.com/problems/delete-operation-for-two-strings/discuss/433264/Java-DP-solution-easy-understand
	 * 
	 * dp[i][j] represent the solution for the subproblem word1[1..i) and word2[1..j)
	 * if (word1[i-1] == word2[j-1])  // doesn't need to be deleted
	 *     dp[i][j] = dp[i-1][j-1] 
	 * else                           // delete 1 from minimum out of these two values
	 *     dp[i][j] = 1 + Math.min(dp[i-1][j], dp[i][j-1])
	 * 
	 * Rf :
	 * https://leetcode.com/problems/delete-operation-for-two-strings/discuss/103276/Simple-Java-DP-Solution
	 * 
	 * Other code:
	 * https://leetcode.com/problems/delete-operation-for-two-strings/discuss/305191/JAVA-DP-and-Recursive-Solution
	 * https://leetcode.com/problems/delete-operation-for-two-strings/discuss/103232/JavaC%2B%2B-Clean-Code
	 */
	public int minDistance_2D(String word1, String word2) {
		if (word1.length() == 0)
			return word2.length();
		if (word2.length() == 0)
			return word1.length();

		// dp[][] stores current minimum step of deletion
		int[][] dp = new int[word1.length() + 1][word2.length() + 1];

		// initialize
		// eg. compare abc and acdef
		//   a c d e f
		// 0 1 2 3 4 5
		// a 1
		// b 2
		// c 3
		// before filling in the dp[][] by comparing characters one by one, we first
		// initialize as above
		// it means if another string s2 is null, how many deletions should be done
		// from current string s1(from 0 to i/j)
		for (int i = 0; i <= word1.length(); i++) {
			dp[i][0] = i;
		}
		for (int i = 0; i <= word2.length(); i++) {
			dp[0][i] = i;
		}

		// filling in dp[][] by comparing 2 characters
		for (int i = 1; i <= word1.length(); i++) {
			for (int j = 1; j <= word2.length(); j++) {
				// if 2 characters match, no need to do deletion
				if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
					dp[i][j] = dp[i - 1][j - 1];
				} 
				else {
					// if no match, we need to delete one character from either word1
					// or word2 choose the minimum number of deletion by so far and
					// add 1 for this deletion
					dp[i][j] = Math.min(dp[i][j - 1], dp[i - 1][j]) + 1;
				}
			}
		}
		return dp[word1.length()][word2.length()];
	}
	
	/*
	 * https://leetcode.com/problems/delete-operation-for-two-strings/discuss/155823/583.-Delete-Operation-for-Two-Strings-Solution-in-C%2B%2B-and-Java-and-Python
	 * 
	 * Get the longest common subsequence
	 * dp[i][j]: the longest subsequence between word1[0:i) and word2[0:j)
	 * 
	 * 1. word1[i-1] == word2[j-1]: dp[i][j] = dp[i-1][j-1] + 1
	 * 2. word1[i-1] != word2[j-1]: dp[i][j] = max(dp[i-1][j], dp[i][j-1])
	 * 
	 * In order to get the subsequence, each word has to delete 
	 * length - dp[n1][n2] chars
	 * 
	 * Rf : https://leetcode.com/problems/delete-operation-for-two-strings/discuss/103259/Longest-Common-Subsequence-DP-Java-Solution
	 * 
	 * Other code:
	 * https://leetcode.com/problems/delete-operation-for-two-strings/discuss/103214/Java-DP-Solution-(Longest-Common-Subsequence)
	 * https://leetcode.com/problems/delete-operation-for-two-strings/discuss/495734/2D-DP-Solution-using-LCS-or-Space%3A-O(N2)-or-Time%3A-O(N2)-or-Faster-than-99.17
	 * https://leetcode.com/problems/delete-operation-for-two-strings/discuss/103236/Java-solution-longest-common-subsequence
	 */
	public int minDistance_LCS_2D(String word1, String word2) {
		int n1 = word1.length();
		int n2 = word2.length();
		int[][] dp = new int[n1 + 1][n2 + 1];
		
		for (int i = 1; i <= n1; i++) {
			for (int j = 1; j <= n2; j++) {
				if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
					dp[i][j] = dp[i - 1][j - 1] + 1;
				} 
				else {
					dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
				}
			}
		}
		return n1 - dp[n1][n2] + n2 - dp[n1][n2];
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/articles/delete-operation-for-two-strings/
	 * 
	 * Approach #2 Longest Common Subsequence with Memoization
	 * 
	 * If there is a common sequence among the two strings of length lcs, we need to 
	 * do lcs lesser deletions in both the strings leading to a total of 2lcs lesser 
	 * deletions. We make use of a recursive function lcs(s1,s2,i,j) which returns 
	 * the length of the longest common sequence among the strings s1 and s2 
	 * considering their lengths upto i and j. 
	 */
	public int minDistance_LCS_recursive(String s1, String s2) {
		int[][] memo = new int[s1.length() + 1][s2.length() + 1];
		return s1.length() + s2.length() 
		        - 2 * lcs(s1, s2, s1.length(), s2.length(), memo);
	}

	public int lcs(String s1, String s2, int m, int n, int[][] memo) {
		if (m == 0 || n == 0)
			return 0;
		if (memo[m][n] > 0)
			return memo[m][n];
		
		if (s1.charAt(m - 1) == s2.charAt(n - 1))
			memo[m][n] = 1 + lcs(s1, s2, m - 1, n - 1, memo);
		else
			memo[m][n] = Math.max(lcs(s1, s2, m, n - 1, memo), 
					lcs(s1, s2, m - 1, n, memo));
		return memo[m][n];
	}
	
	/*
	 * https://leetcode.com/problems/delete-operation-for-two-strings/discuss/103217/Java-DP-Solution-same-as-Edit-Distance
	 * 
	 * Rf :
	 * https://leetcode.com/problems/delete-operation-for-two-strings/discuss/103217/Java-DP-Solution-same-as-Edit-Distance/106404
	 * https://leetcode.com/problems/delete-operation-for-two-strings/discuss/103217/Java-DP-Solution-same-as-Edit-Distance/113510
	 */
	public int minDistance2_2D(String word1, String word2) {
		int len1 = word1.length(), len2 = word2.length();
		if (len1 == 0)
			return len2;
		if (len2 == 0)
			return len1;

		// dp[i][j] stands for distance of first i chars of word1 and first j chars of
		// word2
		int[][] dp = new int[len1 + 1][len2 + 1];
		for (int i = 0; i <= len1; i++)
			dp[i][0] = i;
		for (int j = 0; j <= len2; j++)
			dp[0][j] = j;

		for (int i = 1; i <= len1; i++) {
			for (int j = 1; j <= len2; j++) {
				if (word1.charAt(i - 1) == word2.charAt(j - 1))
					dp[i][j] = dp[i - 1][j - 1];
				else
					/*
					 * Since we're permitted to operate only one character at a time, 
					 * checking to delete both characters is not required, although it 
					 * produces same result since you add 2.
					 * dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]) + 1;
					 */
					dp[i][j] = Math.min(Math.min(dp[i - 1][j - 1] + 2, 
							dp[i - 1][j] + 1), dp[i][j - 1] + 1);
			}
		}

		return dp[len1][len2];
	}
	
	/*
	 * https://leetcode.com/problems/delete-operation-for-two-strings/discuss/370710/Runtime%3A-4-ms-faster-than-98.82-Memory-Usage%3A-36.2-MB-less-than-100.00
	 * 
	 * Other code:
	 * https://leetcode.com/problems/delete-operation-for-two-strings/discuss/103214/Java-DP-Solution-(Longest-Common-Subsequence)/345043
	 * https://leetcode.com/problems/delete-operation-for-two-strings/discuss/103223/Short-java-DP-solution-O(n)-space-(48ms)
	 */
	public int minDistance_LCS_1D(String word1, String word2) {
		if (word1.length() == 0) {
			return word2.length();
		}
		if (word2.length() == 0) {
			return word1.length();
		}

		int n = word1.length();
		int m = word2.length();
		int[][] dp = new int[2][m + 1];
		
		for (int i = 1; i <= n; i++) {
			Arrays.fill(dp[1], 0);
			
			for (int j = 1; j <= m; j++) {
				if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
					dp[1][j] = dp[0][j - 1] + 1;
				} 
				else {
					dp[1][j] = Math.max(dp[0][j], dp[1][j - 1]);
				}
			}
			
			System.arraycopy(dp[1], 0, dp[0], 0, m + 1);
		}
		return m + n - 2 * dp[1][m];
	}
	
	/*
	 * https://leetcode.com/problems/delete-operation-for-two-strings/discuss/103229/Java-DP-solution-with-optimized-O(N)-space
	 * 
	 * Rf : https://leetcode.com/articles/delete-operation-for-two-strings/
	 * Approach #5 1-D Dynamic Programming
	 * 
	 * Other code:
	 * https://leetcode.com/problems/delete-operation-for-two-strings/discuss/103217/Java-DP-Solution-same-as-Edit-Distance/458394
	 */
	public int minDistance2_1D(String word1, String word2) {
		int len1 = word1.length();
		int len2 = word2.length();
		int[] map = new int[len2 + 1];

		for (int j = 0; j <= len2; j++) {
			map[j] = j;
		}

		for (int i = 0; i < len1; i++) {
			int[] newmap = new int[len2 + 1];
			newmap[0] = i + 1;
			
			for (int j = 0; j < len2; j++) {
				if (word1.charAt(i) == word2.charAt(j)) {
					newmap[j + 1] = map[j];
				} 
				else {
					newmap[j + 1] = Math.min(map[j + 1], newmap[j]) + 1;
				}
			}
			map = newmap;
		}
		return map[len2];
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/delete-operation-for-two-strings/discuss/103267/Python-Straightforward-with-Explanation
     * https://leetcode.com/problems/delete-operation-for-two-strings/discuss/103246/Python-DP-solution
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/delete-operation-for-two-strings/discuss/103211/JavaC%2B%2B-Clean-Code
     */

}
