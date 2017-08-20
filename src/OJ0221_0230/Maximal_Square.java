package OJ0221_0230;

// https://leetcode.com/articles/maximal-square/

public class Maximal_Square {
	/*
	 * https://discuss.leetcode.com/topic/20801/extremely-simple-java-solution
	 * 
	 * Top, Left, and Top Left decides the size of the square. 
	 * If all of them are same, then the size of square increases by 1. 
	 * If they're not same, they can increase by 1 to the minimal square.
	 * 
	 * b[i][j] represent the edge length of the largest square ending at (i, j)
	 * 
	 * Rf : https://discuss.leetcode.com/topic/18482/accepted-clean-java-dp-solution
	 */
	public int maximalSquare(char[][] a) {
		if (a.length == 0)
			return 0;
		int m = a.length, n = a[0].length, result = 0;
		int[][] b = new int[m + 1][n + 1];
		for (int i = 1; i <= m; i++) {
			for (int j = 1; j <= n; j++) {
				if (a[i - 1][j - 1] == '1') {
					// b(i, j) = min{ b(i-1, j-1), b(i-1, j), b(i, j-1) }
					b[i][j] = Math.min(Math.min(b[i][j - 1], b[i - 1][j - 1]), b[i - 1][j]) + 1;
					result = Math.max(b[i][j], result); // update result
				}
			}
		}
		return result * result;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/15445/my-java-dp-ac-solution-simple-and-easy-to-understand-with-explanation
	 * 
	 * if I am the most right-bottom guy of a square, how big the square I can build.
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/28487/java-dp-simple-solution
	 */
	public int maximalSquare2(char[][] matrix) {
		//illegal check - no square can be formed
		if (matrix == null || matrix.length == 0)
			return 0;
		
		int result = 0;
		int[][] count = new int[matrix.length][matrix[0].length];
		
		//initialize first row and first column
		for (int i = 0; i < matrix.length; i++) {
			count[i][0] = matrix[i][0] == '0' ? 0 : 1;
			result = Math.max(result, count[i][0]);
		}
		for (int i = 0; i < matrix[0].length; i++) {
			count[0][i] = matrix[0][i] == '0' ? 0 : 1;
			result = Math.max(result, count[0][i]);
		}

		for (int i = 1; i < matrix.length; i++) {
			for (int j = 1; j < matrix[0].length; j++) {
				if (matrix[i][j] == '1') {
					count[i][j] = 1 + Math.min(Math.min(count[i - 1][j - 1], count[i - 1][j]), count[i][j - 1]);
					result = Math.max(result, count[i][j]);
				}
			}
		}
		return result * result;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/15729/my-accepted-java-solution-time-o-mn-space-o-min-m-n
	 * 
	 * We assume here that columns are less than rows, otherwise we can put 
	 * the row loop inside of column loop to archive the O(min(m,n)), 
	 * tmp is to be used for remember the left-up conner value.

     * Note: we have to set the tmp1 to 0 before inside loop, otherwise the 
     * left-up value might not be zero for calculating the first point of each row, 
     * because the tmp1 has the carry-over value from previous row.
	 */
	public int maximalSquare_1D(char[][] matrix) {
		if (matrix == null || matrix.length == 0 || matrix[0].length == 0)
			return 0;

		int[] dp = new int[matrix[0].length + 1];
		int max = 0;
		int tmp1 = 0;
		int tmp2 = 0;

		for (int i = 0; i < matrix.length; i++) {
			tmp1 = 0;
			for (int j = 0; j < matrix[0].length; j++) {
				tmp2 = dp[j + 1];

				if (matrix[i][j] == '1') {
					dp[j + 1] = Math.min(tmp1, Math.min(dp[j], dp[j + 1])) + 1;
					max = Math.max(max, dp[j + 1]);
				} 
				else {
					dp[j + 1] = 0;
				}

				tmp1 = tmp2;
			}
		}
		return max * max;
	}
	
	// cs141 quiz2 solution
	public int maximalSquare_141_quiz2_sol(char[][] matrix) {
		if (matrix.length == 0)
			return 0;
		int[][] l = new int[matrix.length][matrix[0].length];
		int max = 0;
		for (int i = 0; i < matrix.length; i++) {
			l[i][matrix[0].length - 1] = matrix[i][matrix[0].length - 1] - '0';
			max = Math.max(max, l[i][matrix[0].length - 1]);
		}
		for (int i = 0; i < matrix[0].length; i++) {
			l[matrix.length - 1][i] = matrix[matrix.length - 1][i] - '0';
			max = Math.max(max, l[matrix.length - 1][i]);
		}
		for (int i = matrix.length - 2; i >= 0; i--) {
			for (int j = matrix[0].length - 2; j >= 0; j--) {
				if (matrix[i][j] == '0') {
					l[i][j] = 0;
				} 
				else {
					l[i][j] = 1 + Math.min(l[i + 1][j], Math.min(l[i][j + 1], l[i + 1][j + 1]));
					max = Math.max(max, l[i][j]);
				}
			}
		}
		return max * max;
	}

}
