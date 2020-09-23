package OJ0971_0980;

import java.util.Map;
import java.util.HashMap;

public class Unique_Paths_III {
	/*
	 * The following 2 functions are modified from this link.
	 * https://leetcode.com/problems/unique-paths-iii/discuss/221946/JavaPython-Brute-Force-Backstracking
	 * 
	 * First find out where the start is.
	 * Also We need to know the number of empty cells.
	 * 
	 * When we try to explore a cell,
	 * it will change 0 to -2 and do a dfs in 4 direction.
	 * 
	 * If we hit the target and pass all empty cells, increment the result.
	 * 
	 * Initially int empty = 1;, it represents the starting point. Because during 
	 * backtracking, both grid[x][y] == 0 && grid[x][y] == 1 will be set to -2 firstly 
	 * and reset back to grid[x][y] = 0, i.e. grid[x][y] == 1 is also counted as 0 
	 * during DFS + Backtracking.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/unique-paths-iii/solution/
	 * https://leetcode.com/problems/unique-paths-iii/discuss/221946/JavaPython-Brute-Force-Backstracking/698837
	 * https://leetcode.com/problems/unique-paths-iii/discuss/221946/JavaPython-Brute-Force-Backstracking/464457
	 */
	public int uniquePathsIII_dfs(int[][] grid) {
		int m = grid.length, n = grid[0].length;

		// after you go through the last zero, you need one more to reach end.
		int empty = 1; 
		
		int sx = -1, sy = -1;
		int[] res = { 0 };

		for (int i = 0; i < m; ++i) {
			for (int j = 0; j < n; ++j) {
				if (grid[i][j] == 0) {					
					empty++;
				}
				else if (grid[i][j] == 1) {
					sx = i;
					sy = j;
				}
			}
		}

		dfs_dfs(grid, sx, sy, empty, res);
		return res[0];
	}

	public void dfs_dfs(int[][] grid, int x, int y, int empty, int[] res) {
		if (x < 0 || x >= grid.length || y < 0 || y >= grid[0].length 
				|| grid[x][y] < 0)
			return;
		
		if (grid[x][y] == 2) {
			if (empty == 0)
				res[0]++;
			return;
		}
		
		grid[x][y] = -2;
		
		dfs_dfs(grid, x + 1, y, empty - 1, res);
		dfs_dfs(grid, x - 1, y, empty - 1, res);
		dfs_dfs(grid, x, y + 1, empty - 1, res);
		dfs_dfs(grid, x, y - 1, empty - 1, res);
		
		grid[x][y] = 0;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/unique-paths-iii/discuss/289954/Java-Solution-DFS-With-backtracking
	 * 
	 * Since the requirement is to touch every empty cell exactly once, we need "dfs" 
	 * and maintain a step count "count" for the recursive path and when we reach 
	 * destination, we compare how many empty cells we covered.
	 * To avoid cycles in the path, we mark the cells in the current dfs path as 
	 * blockers and remove them while coming back.
	 */
	public int uniquePathsIII_dfs2(int[][] g) {
		int x = 0, y = 0, empty = 0;
		for (int i = 0; i < g.length; i++) {			
			for (int j = 0; j < g[0].length; j++) {
				if (g[i][j] == 0)
					++empty;
				else if (g[i][j] == 1) {
					x = i;
					y = j;
				}
			}
		}

		return dfs2(g, x, y, -1, empty);
	}

	private int dfs2(int[][] g, int i, int j, int count, int need) {
		if (i < 0 || i == g.length || j < 0 || j == g[0].length || g[i][j] == -1)
			return 0;
		
		if (g[i][j] == 2) {
			if (count == need)
				return 1;
			else
				return 0;
		}
		
		g[i][j] = -1;
		
		int total = dfs2(g, i - 1, j, count + 1, need);
		total += dfs2(g, i, j + 1, count + 1, need);
		total += dfs2(g, i + 1, j, count + 1, need);
		total += dfs2(g, i, j - 1, count + 1, need);
		
		g[i][j] = 0;
		
		return total;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/unique-paths-iii/discuss/829668/Java-Backtracking.-100-Time
	 */
	public int uniquePathsIII_visited_dfs(int[][] grid) {
		int numberOfEmptySquares = 0;
		int x = 0;
		int y = 0;
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if (grid[i][j] == 0 || grid[i][j] == 2) {
					numberOfEmptySquares++;
				}
				
				if (grid[i][j] == 1) {
					x = i;
					y = j;
				}
			}
		}
		
		return helper_visited_dfs(grid, new boolean[grid.length][grid[0].length], 
				x, y, numberOfEmptySquares);
	}

	private int helper_visited_dfs(int[][] grid, boolean[][] visited, int x, int y, 
			int numberOfEmptySquares) {
		
		if (grid[x][y] == 2) {
			if (numberOfEmptySquares == 0) {
				return 1;
			} 
			else {
				return 0;
			}
		}
		
		// top
		int result = 0;
		
		visited[x][y] = true;
		
		if (x - 1 >= 0 && grid[x - 1][y] != -1 && !visited[x - 1][y]) {
			result = result + 
					helper_visited_dfs(grid, visited, x - 1, y, 
							numberOfEmptySquares - 1);
		}
		if (x + 1 < grid.length && grid[x + 1][y] != -1 && !visited[x + 1][y]) {
			result = result + 
					helper_visited_dfs(grid, visited, x + 1, y, 
							numberOfEmptySquares - 1);
		}
		if (y - 1 >= 0 && grid[x][y - 1] != -1 && !visited[x][y - 1]) {
			result = result + 
					helper_visited_dfs(grid, visited, x, y - 1, 
							numberOfEmptySquares - 1);
		}
		if (y + 1 < grid[x].length && grid[x][y + 1] != -1 && !visited[x][y + 1]) {
			result = result + 
					helper_visited_dfs(grid, visited, x, y + 1, 
							numberOfEmptySquares - 1);
		}
		
		visited[x][y] = false;
		return result;
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/unique-paths-iii/discuss/825590/Java-DFS-Backtracking-with-Explanation-or-Easy-to-Understand
	 * 
	 * 1. Iterate through the grid once, record the start coordinate and count the 
	 *    number of total "non-obstacle" cells.
	 * 2. Using a boolean visited array, we could simply mark the obstacles as 
	 *    "visited" so that in helper method, if we meet the obstacle, return directly.
	 * 
	 * In helper method, the two base cases are:
	 * 1. first check if the index i,j is still within boundary or if the cell has been 
	 *    visited. If not, return directly.
	 * 2. Otherwise, check when reach the end 2 if remain equals 1, if so, we've found 
	 *    a new path. count += 1 and return.
	 * 
	 * Recursive case is:
	 * 1. mark current cell as visited, visit all four directions.
	 * 2. after recursion, put the cell back to unvisited.
	 * 
	 * Other code:
	 * https://leetcode.com/problems/unique-paths-iii/discuss/252187/Java-2ms-100-faster-DFS
	 */
	int count_dfs3 = 0;

	public int uniquePathsIII_dfs3(int[][] grid) {
		int m = grid.length, n = grid[0].length;
		boolean[][] visited = new boolean[m][n];
		
		int r = 0, c = 0; // start point
		int remain = 0;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (grid[i][j] >= 0) {
					remain++;
					
					if (grid[i][j] == 1) {
						r = i;
						c = j;
					}
				} 
				else {
					visited[i][j] = true;
				}
			}
		}
		
		helper_dfs3(grid, r, c, remain, visited);
		return count_dfs3;
	}

	private void helper_dfs3(int[][] grid, int i, int j, int remain, 
			boolean[][] visited) {
		
		if (i < 0 || j < 0 || i >= visited.length || j >= visited[0].length 
				|| visited[i][j] == true) {
			
			return;
		}
		
		if (grid[i][j] == 2 && remain == 1) {
			this.count_dfs3 += 1;
			return;
		}
		
		visited[i][j] = true;

		helper_dfs3(grid, i + 1, j, remain - 1, visited);
		helper_dfs3(grid, i - 1, j, remain - 1, visited);
		helper_dfs3(grid, i, j + 1, remain - 1, visited);
		helper_dfs3(grid, i, j - 1, remain - 1, visited);

		visited[i][j] = false;
	}
	
	/*
	 * The following 3 functions are from this link.
	 * https://leetcode.com/problems/unique-paths-iii/discuss/221965/Java-DFS-with-Memo
	 * 
	 * dfs(): returns the number of valid paths from current status to the destination.
	 * cur: the number of visited squares.
	 * total: the number of squares need to be visited in the grid.
	 */
	public int uniquePathsIII_memo(int[][] grid) {
		int x = -1, y = -1, m = grid.length, n = grid[0].length, total = 0;
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (grid[i][j] == 1) {
					x = i;
					y = j;
				}
				if (grid[i][j] == 0) {
					total++;
				}
			}
		}
		
		return dfs_memo(grid, x, y, 0, total + 1, new HashMap<>());
	}

	private int dfs_memo(int[][] grid, int x, int y, int cur, int total, 
			Map<String, Integer> memo) {
		
		int m = grid.length, n = grid[0].length;
		if (x < 0 || x >= m || y < 0 || y >= n || grid[x][y] == -1) {
			return 0;
		}
		
		if (grid[x][y] == 2) {
			if (total == cur) {
				return 1;
			} 
			else {
				return 0;
			}
		}
		
		grid[x][y] = -1;
		
		String key = getKey_memo(grid) + " " + x + " " + y;
		if (!memo.containsKey(key)) {
			int[][] dirs = new int[][] { { 1, 0 }, { 0, 1 }, { 0, -1 }, { -1, 0 } };
			
			int result = 0;
			for (int[] dir : dirs) {
				result += 
						dfs_memo(grid, x + dir[0], y + dir[1], cur + 1, total, memo);
			}
			
			memo.put(key, result);
		}
		
		grid[x][y] = 0;
		return memo.get(key);
	}

	private int getKey_memo(int[][] grid) {
		int result = 0;
		for (int[] row : grid) {
			for (int a : row) {
				result <<= 1;
				
				if (a != 0) {
					result ^= 1;
				}
			}
		}
		return result;
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/unique-paths-iii/discuss/267094/Python3-Solution-using-backtracking.
     * https://leetcode.com/problems/unique-paths-iii/discuss/360510/Python-Yet-Another-Backtracking
     * https://leetcode.com/problems/unique-paths-iii/discuss/390360/Python-%3A-Well-Explained-and-Simple
     * https://leetcode.com/problems/unique-paths-iii/discuss/221913/python-solution-using-bit(with-extra-detailed-Chinese-explanation)
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/unique-paths-iii/discuss/221941/C%2B%2B-brute-force-DFS
     * https://leetcode.com/problems/unique-paths-iii/discuss/466439/c%2B%2B-solution-using-backtracking
     */

}
