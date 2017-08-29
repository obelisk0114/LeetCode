package OJ0061_0070;

public class Unique_Paths {
	/*
	 * https://discuss.leetcode.com/topic/31724/java-solution-0ms-4lines
	 * 
	 * Rf : https://discuss.leetcode.com/topic/19613/math-solution-o-1-space
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/38828/0ms-java-10-line-code
	 */
	public int uniquePaths(int m, int n) {
		long result = 1;
		for (int i = 0; i < Math.min(m - 1, n - 1); i++)
			result = result * (m + n - 2 - i) / (i + 1);
		return (int) result;
	}
	
	// by myself
	public int uniquePaths_self(int m, int n) {
		m--;
		n--;
		long sum = 1;
		for (int i = 1; i <= n; i++) {
			sum = sum * (m + i) / i;
		}
		return (int) sum;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/8009/java-dp-with-o-n-space
	 * 
	 * We only need to store the previous row/column to perform the calculation 
	 * for the next one.
	 * We could know matrix[i][j] = matrix[i-1][j] + matrix[i][j-1], 
	 * because if we can get the position (i-1, j) or (i, j-1), then we only have one 
	 * way to reach the destination -- move down or move right
	 * 
	 * Rf : https://discuss.leetcode.com/topic/41166/sharing-my-0ms-java-solution-with-detailed-exploration
	 */
	public int uniquePaths_1D_array(int m, int n) {
		int[] arr = new int[m];
		for (int i = 0; i < m; i++) {
			arr[i] = 1;
		}
		for (int i = 1; i < n; i++) {
			for (int j = 1; j < m; j++) {
				arr[j] = arr[j] + arr[j - 1];
			}
		}
		return arr[m - 1];
	}
	
	/*
	 * https://discuss.leetcode.com/topic/5623/java-dp-solution-with-complexity-o-n-m
	 * 
	 * For all other cells. The result = uniquePaths(m-1,n) + uniquePaths(m,n-1)
	 * 
	 * Rf : https://discuss.leetcode.com/topic/15265/0ms-5-lines-dp-solution-in-c-with-explanations
	 */
	public int uniquePaths_2D_array(int m, int n) {
		int[][] map = new int[m][n];
		for (int i = 0; i < m; i++) {
			map[i][0] = 1;
		}
		for (int j = 0; j < n; j++) {
			map[0][j] = 1;
		}
		for (int i = 1; i < m; i++) {
			for (int j = 1; j < n; j++) {
				map[i][j] = map[i - 1][j] + map[i][j - 1];
			}
		}
		return map[m - 1][n - 1];
	}

}
