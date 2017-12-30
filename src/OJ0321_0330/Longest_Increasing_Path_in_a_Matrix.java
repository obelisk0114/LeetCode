package OJ0321_0330;

import java.util.HashSet;

public class Longest_Increasing_Path_in_a_Matrix {
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/39152/neat-java-dfs-solution-with-memoization
	 * 
	 * Starting with each element of matrix and also remember the longest length 
	 * starting from each element to remove duplicate computations.
	 * 
	 * The tricky part is a visited map is not required 
	 * (cause visited value is always smaller).
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/35962/easy-java-solution
	 */
	private int dfs(int[][] matrix, int i, int j, int[][] cache) {
		if (cache[i][j] > 0)
			return cache[i][j];
		
		int longest = 0;
		if (i > 0 && matrix[i][j] < matrix[i - 1][j])
			longest = Math.max(longest, dfs(matrix, i - 1, j, cache));
		if (j > 0 && matrix[i][j] < matrix[i][j - 1])
			longest = Math.max(longest, dfs(matrix, i, j - 1, cache));
		if (i < matrix.length - 1 && matrix[i][j] < matrix[i + 1][j])
			longest = Math.max(longest, dfs(matrix, i + 1, j, cache));
		if (j < matrix[0].length - 1 && matrix[i][j] < matrix[i][j + 1])
			longest = Math.max(longest, dfs(matrix, i, j + 1, cache));
		
		cache[i][j] = longest + 1;
		return longest + 1;
	}
	public int longestIncreasingPath(int[][] matrix) {
		if (matrix.length == 0)
			return 0;
		
		int[][] cache = new int[matrix.length][matrix[0].length];
		int longest = 0;
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[0].length; j++)
				longest = Math.max(longest, dfs(matrix, i, j, cache));
		
		return longest;
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/34835/15ms-concise-java-solution
	 * 
	 * 1. Do DFS from every cell
	 * 2. Compare every 4 direction and skip cells that are out of boundary or smaller
	 * 3. Get matrix max from every cell's max
	 * 4. Use matrix[x][y] <= matrix[i][j] so we don't need a visited[m][n] array
	 * 5. The key is to cache the distance because it's highly possible to revisit a 
	 * cell
	 * 
	 * Rf : https://discuss.leetcode.com/topic/34749/an-o-nm-dp-solution-with-explanations
	 */
	public static final int[][] dirs = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };
	public int longestIncreasingPath2(int[][] matrix) {
		if (matrix.length == 0)
			return 0;
		
		int m = matrix.length, n = matrix[0].length;
		int[][] cache = new int[m][n];
		int max = 1;
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				int len = dfs2(matrix, i, j, m, n, cache);
				max = Math.max(max, len);
			}
		}
		return max;
	}
	public int dfs2(int[][] matrix, int i, int j, int m, int n, int[][] cache) {
		if (cache[i][j] != 0)
			return cache[i][j];
		
		int max = 1;
		for (int[] dir : dirs) {
			int x = i + dir[0], y = j + dir[1];
			if (x < 0 || x >= m || y < 0 || y >= n || matrix[x][y] <= matrix[i][j])
				continue;
			int len = 1 + dfs2(matrix, x, y, m, n, cache);
			max = Math.max(max, len);
		}
		cache[i][j] = max;
		return max;
	}
	
	/*
	 * The following class and function are from this link.
	 * https://discuss.leetcode.com/topic/35021/graph-theory-java-solution-o-v-2-no-dfs
	 * 
	 * I think the core idea is treat the graph as topology sorted, and each time 
	 * we delete from the end (which means we will delete all nodes within the same 
	 * level), and increment count.
	 * 
	 * Rf :
	 * https://discuss.leetcode.com/topic/35021/graph-theory-java-solution-o-v-2-no-dfs/7
	 * https://discuss.leetcode.com/topic/35021/graph-theory-java-solution-o-v-2-no-dfs/15
	 */
	public class Point {
		int x;
		int y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	public int longestIncreasingPath_topology(int[][] matrix) {
		if (matrix == null || matrix.length == 0 || 
				matrix[0] == null || matrix[0].length == 0)
			return 0;
		
		int n = matrix.length, m = matrix[0].length, 
				count = m * n, ans = 0;
		
		while (count > 0) {
			HashSet<Point> remove = new HashSet<Point>();
	        // each round, remove the peak number.
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < m; j++) {
					if (matrix[i][j] == Integer.MIN_VALUE)
						continue;
					
					boolean up = (i == 0 || matrix[i][j] >= matrix[i - 1][j]);
					boolean bottom = (i == n - 1 || matrix[i][j] >= matrix[i + 1][j]);
					boolean left = (j == 0 || matrix[i][j] >= matrix[i][j - 1]);
					boolean right = (j == m - 1 || matrix[i][j] >= matrix[i][j + 1]);
					
					if (up && bottom && left && right)
						remove.add(new Point(i, j));
				}
			}
			
			for (Point point : remove) {
				matrix[point.x][point.y] = Integer.MIN_VALUE;
				count--;
			}
			
			ans++;
		}
		return ans;
	}

}
