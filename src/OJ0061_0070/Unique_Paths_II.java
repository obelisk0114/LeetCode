package OJ0061_0070;

public class Unique_Paths_II {
	// https://discuss.leetcode.com/topic/10974/short-java-solution
	public int uniquePathsWithObstacles(int[][] obstacleGrid) {
	    int width = obstacleGrid[0].length;
	    int[] dp = new int[width];
	    dp[0] = 1;
	    for (int[] row : obstacleGrid) {
	        for (int j = 0; j < width; j++) {
	            if (row[j] == 1)
	                dp[j] = 0;
	            else if (j > 0)
	                dp[j] += dp[j - 1];
	        }
	    }
	    return dp[width - 1];
	}
	
	/*
	 * https://discuss.leetcode.com/topic/24370/java-simple-and-clean-dp-solution-easy-to-understand
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/33885/simple-java-dp-solution
	 */
	public int uniquePathsWithObstacles_extra_space(int[][] obstacleGrid) {
		int m = obstacleGrid.length, n = obstacleGrid[0].length;
		int[][] path = new int[m][n];

		for (int i = 0; i < m; i++) {
			if (obstacleGrid[i][0] == 1) {
				path[i][0] = 0;
				// on the first column, if there is an obstacle, the rest are blocked.
				// no need to continue.
				break;
			} else
				path[i][0] = 1;
		}

		for (int j = 0; j < n; j++) {
			if (obstacleGrid[0][j] == 1) {
				path[0][j] = 0;
				// First row, once obstacle found, the rest are blocked.
				break;
			} else
				path[0][j] = 1;
		}

		for (int i = 1; i < m; i++) {
			for (int j = 1; j < n; j++) {
				if (obstacleGrid[i][j] == 1)
					path[i][j] = 0;
				else
					path[i][j] = path[i - 1][j] + path[i][j - 1];
			}
		}
		return path[m - 1][n - 1];
	}
	
	/*
	 * https://discuss.leetcode.com/topic/36406/easy-to-understand-java-solution
	 * 
	 * Rf : https://discuss.leetcode.com/topic/3858/share-my-java-solution-o-m-n-time-complexity-no-extra-space
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/9687/easy-java-solution-in-place-dp
	 */
	public int uniquePathsWithObstacles_modify_input(int[][] obstacleGrid) {
		int m = obstacleGrid.length;
		int n = obstacleGrid[0].length;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (obstacleGrid[i][j] == 1) {
					obstacleGrid[i][j] = 0;
				} else {
					if (i == 0 && j == 0)
						obstacleGrid[i][j] = 1;
					else if (i == 0 && j > 0)
						obstacleGrid[i][j] = obstacleGrid[i][j - 1];
					else if (i > 0 && j == 0)
						obstacleGrid[i][j] = obstacleGrid[i - 1][j];
					else
						obstacleGrid[i][j] = obstacleGrid[i - 1][j] + obstacleGrid[i][j - 1];
				}
			}
		}
		return obstacleGrid[m - 1][n - 1];
	}

}
