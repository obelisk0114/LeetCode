package OJ0471_0480;

import java.util.Arrays;

public class Ones_and_Zeroes {
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/ones-and-zeroes/discuss/95807/0-1-knapsack-detailed-explanation.
	 * 
	 * we have two types of recourse('0' and '1'), so we keep a two dimension array to 
	 * represent the content for these two resources.
	 * 
	 * dp[i][j][k] means the maximum number of strings we can get from the first i 
	 * argument strs using limited j number of '0's and k number of '1's.
	 * For dp[i][j][k], we can get it by fetching the current string i or discarding 
	 * the current string.
	 * 
	 * Rf : https://leetcode.com/problems/ones-and-zeroes/discuss/95857/java-DP-solution-0-1Knapsack-problem.
	 */
	public int findMaxForm(String[] strs, int m, int n) {
		int l = strs.length;
		int[][][] dp = new int[l + 1][m + 1][n + 1];

		for (int i = 0; i < l + 1; i++) {
			int[] nums = { 0, 0 };
			if (i > 0) {
				nums = calculate(strs[i - 1]);
			}
			
			for (int j = 0; j < m + 1; j++) {
				for (int k = 0; k < n + 1; k++) {
					if (i == 0) {
						dp[i][j][k] = 0;
					} 
					else if (j >= nums[0] && k >= nums[1]) {
						dp[i][j][k] = Math.max(dp[i - 1][j][k], dp[i - 1][j - nums[0]][k - nums[1]] + 1);
					} 
					else {
						dp[i][j][k] = dp[i - 1][j][k];
					}
				}
			}
		}

		return dp[l][m][n];
	}
	private int[] calculate(String str) {
		int[] res = new int[2];
		Arrays.fill(res, 0);

		for (char ch : str.toCharArray()) {
			if (ch == '0') {
				res[0]++;
			} 
			else if (ch == '1') {
				res[1]++;
			}
		}
		return res;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/ones-and-zeroes/discuss/95811/Java-Iterative-DP-Solution-O(mn)-Space
	 * 
	 * Time Complexity: O(kl + kmn), where k is the length of input string array and 
	 * l is the average length of a string within the array.
	 * 
	 * dp[i][j] stands for max number of str can we pick from strs with limitation of 
	 * i "0"s and j "1"s. For each str, assume it has a "0"s and b "1"s, we update the 
	 * dp array iteratively and set dp[i][j] = Math.max(dp[i][j], dp[i - a][j - b] + 1). 
	 * So at the end, dp[m][n] is the answer.
	 * 
	 * Catch: have to go from bottom right to top left
	 * If a cell in the dp is updated(because s is selected), we should be adding 1 
	 * to dp[i][j] from the previous iteration (when we were not considering s).
	 * If we go from top left to bottom right, we would be using results from this 
	 * iteration => overcounting
	 * 
	 * Rf : 
	 * https://leetcode.com/problems/ones-and-zeroes/discuss/95814/c++-DP-solution-with-comments
	 * leetcode.com/problems/ones-and-zeroes/discuss/95814/c++-DP-solution-with-comments/100380
	 * 
	 * Other code:
	 * https://leetcode.com/problems/ones-and-zeroes/discuss/95872/Java-28ms-Solution
	 */
	public int findMaxForm2(String[] strs, int m, int n) {
		int[][] dp = new int[m + 1][n + 1];
		for (String s : strs) {
			int[] count = count(s);
			for (int i = m; i >= count[0]; i--)
				for (int j = n; j >= count[1]; j--)
					dp[i][j] = Math.max(1 + dp[i - count[0]][j - count[1]], dp[i][j]);
		}
		return dp[m][n];
	}
	private int[] count(String str) {
		int[] res = new int[2];
		for (int i = 0; i < str.length(); i++)
			res[str.charAt(i) - '0']++;
		return res;
	}
	
	/*
	 * https://leetcode.com/problems/ones-and-zeroes/discuss/95860/Java-DP-solution-with-explanation
	 * 
	 * The idea is to build up the solution for 0..m zeros and 0..n ones, from only 
	 * knowing 1 string, 2 strings, ..., up to n strings.
	 */
	public int findMaxForm_exchange(String[] strs, int m, int n) {
		int[][] max = new int[m + 1][n + 1];
		for (int i = 0; i < strs.length; i++) {
			String str = strs[i];

			int neededZero = 0;
			int neededOne = 0;
			for (int j = 0; j < str.length(); j++) {
				if (str.charAt(j) == '0') {
					neededZero++;
				} 
				else {
					neededOne++;
				}
			}

			int[][] newMax = new int[m + 1][n + 1];

			for (int zero = 0; zero <= m; zero++) {
				for (int one = 0; one <= n; one++) {
					if (zero >= neededZero && one >= neededOne) {
						int zeroLeft = zero - neededZero;
						int oneLeft = one - neededOne;
						newMax[zero][one] = Math.max(1 + max[zeroLeft][oneLeft], max[zero][one]);
					} 
					else {
						newMax[zero][one] = max[zero][one];
					}
				}
			}

			max = newMax;
		}
		return max[m][n];
	}
	
	// The following 2 functions are by myself.
	public int findMaxForm_self(String[] strs, int m, int n) {
        int[][][] dp = new int[strs.length + 1][m + 1][n + 1];
        for (int i = 1; i <= strs.length; i++) {
            int[] count = getZerosOnes_self(strs[i - 1]);
            for (int j = 0; j <= m; j++) {
                for (int k = 0; k <= n; k++) {
                    if (j >= count[0] && k >= count[1]) {
                        dp[i][j][k] = Math.max(dp[i - 1][j][k], dp[i - 1][j - count[0]][k - count[1]] + 1);
                    }
                    else {
                        dp[i][j][k] = dp[i - 1][j][k];
                    }
                }
            }
        }
        return dp[strs.length][m][n];
    }
    
    private int[] getZerosOnes_self(String s) {
        int[] result = new int[2];
        char[] ch = s.toCharArray();
        for (char c : ch) {
            if (c == '0') {
                result[0]++;
            }
            else {
                result[1]++;
            }
        }
        return result;
    }
	
	/*
	 * The following variable and 3 functions are from this link.
	 * https://leetcode.com/problems/ones-and-zeroes/discuss/95845/Easy-to-understand-Recursive-Solutions-in-Java-with-Explanation
	 * 
	 * 1-1. use remaining 0s and 1s (if there are enough of them) and count that string
	 * 1-2. do not use any 0s and 1s and skip that string entirely
	 * 
	 * 2-1. return the result if we have solved for these parameters before
	 * 2-2. store the result for further access
	 * 
	 * We count the zeroes in it by countZeroesIn(String str).
	 */
	private int[][][] dpTable;
	public int findMaxForm_recursive(String[] strs, int m, int n) {
		dpTable = new int[m + 1][n + 1][strs.length];
		return findMaxFormStartingWith(strs, m, n, 0);
	}
	private int findMaxFormStartingWith(String[] strs, int m, int n, int begin) {
		if ((begin == strs.length) || (m + n == 0)) {
			return 0;
		}
		
		// return the result if we have solved for these parameters before
		if (dpTable[m][n][begin] > 0) {
			return dpTable[m][n][begin];
		}
		
		int countByAddingString = 0;
		String current = strs[begin];
		int zeroes = countZeroesIn(current);
		int ones = current.length() - zeroes;
		
		if (m >= zeroes && n >= ones) {
			countByAddingString = findMaxFormStartingWith(strs, m - zeroes, n - ones, begin + 1) + 1;
		}
		int countBySkippingString = findMaxFormStartingWith(strs, m, n, begin + 1);
		
		// store the result for further access
		if (countByAddingString > countBySkippingString) {
			dpTable[m][n][begin] = countByAddingString;
		} 
		else {
			dpTable[m][n][begin] = countBySkippingString;
		}
		return dpTable[m][n][begin];
	}
	private int countZeroesIn(String str) {
		int count = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '0') {
				count++;
			}
		}
		return count;
	}
	
	// https://leetcode.com/problems/ones-and-zeroes/discuss/95863/Java-memoization-and-accepted-DP-solutions-with-explanations

}
