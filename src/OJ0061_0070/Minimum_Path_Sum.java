package OJ0061_0070;

public class Minimum_Path_Sum {
	// Rf : https://discuss.leetcode.com/topic/4751/my-accepted-solution-in-java
	public int minPathSum(int[][] grid) {
        int width = grid.length;
        int length = grid[0].length;
        int[][] path = new int[width][length];
        
        path[0][0] = grid[0][0];
        for (int i = 1; i < width; i++) {
            path[i][0] = path[i - 1][0] + grid[i][0];
        }
        for (int j = 1; j < length; j++) {
            path[0][j] = path[0][j - 1] + grid[0][j];
        }
        
        for (int i = 1; i < width; i++) {
            for (int j = 1; j < length; j++) {
                path[i][j] = Math.min(path[i - 1][j], path[i][j - 1]) + grid[i][j];
            }
        }
        return path[width - 1][length - 1];
    }
	
	// https://discuss.leetcode.com/topic/5459/my-java-solution-using-dp-and-no-extra-space
	public int minPathSum_modify_input(int[][] grid) {
		int m = grid.length;// row
		int n = grid[0].length; // column
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (i == 0 && j != 0) {
					grid[i][j] = grid[i][j] + grid[i][j - 1];
				} else if (i != 0 && j == 0) {
					grid[i][j] = grid[i][j] + grid[i - 1][j];
				} else if (i == 0 && j == 0) {
					grid[i][j] = grid[i][j];
				} else {
					grid[i][j] = Math.min(grid[i][j - 1], grid[i - 1][j]) + grid[i][j];
				}
			}
		}
		return grid[m - 1][n - 1];
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/38213/my-java-solution-using-dp-with-memorization-beats-about-98-submissions
	 */
	public int minPathSum_top_down(int[][] grid) {
		int[][] memo = new int[grid.length][grid[0].length];
		return minPathSumHelper_top_down(grid, 0, 0, memo);
	}
	public int minPathSumHelper_top_down(int[][] grid, int row, int col, int[][] memo) {
		if (row == grid.length - 1 && col == grid[0].length - 1)
			return grid[row][col];
		if (memo[row][col] != 0)
			return memo[row][col];

		int rowInc = Integer.MAX_VALUE, colInc = Integer.MAX_VALUE;
		if (row < grid.length - 1)
			rowInc = minPathSumHelper_top_down(grid, row + 1, col, memo);
		if (col < grid[0].length - 1)
			colInc = minPathSumHelper_top_down(grid, row, col + 1, memo);
		memo[row][col] = Math.min(rowInc, colInc) + grid[row][col];
		return memo[row][col];
	}
	
	// https://discuss.leetcode.com/topic/3184/ac-java-dp-solution-v-s-tle-dijstra-solution

}
