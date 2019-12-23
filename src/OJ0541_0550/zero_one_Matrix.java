package OJ0541_0550;

import java.util.Queue;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

public class zero_one_Matrix {
	// Rf : https://leetcode.com/problems/01-matrix/discuss/248525/Java-BFS-solution-with-comments
	public int[][] updateMatrix_self2(int[][] matrix) {
		int m = matrix.length, n = matrix[0].length;
		int[][] res = new int[m][n];

		Queue<int[]> queue = new LinkedList<>();
		boolean[][] visited = new boolean[m][n];

		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (matrix[i][j] == 0) {
					queue.offer(new int[] { i, j });
					visited[i][j] = true;
				}
			}
		}

		int level = 0;
		while (!queue.isEmpty()) {
			int size = queue.size();
			for (int i = 0; i < size; i++) {
				int[] cur = queue.poll();
				int row = cur[0];
				int col = cur[1];
				res[row][col] = level;
				
				if (row - 1 >= 0 && !visited[row - 1][col]) {
					queue.offer(new int[] { row - 1, col });
					visited[row - 1][col] = true;
				}
				if (row + 1 < m && !visited[row + 1][col]) {
					queue.offer(new int[] { row + 1, col });
					visited[row + 1][col] = true;
				}
				if (col - 1 >= 0 && !visited[row][col - 1]) {
					queue.offer(new int[] { row, col - 1 });
					visited[row][col - 1] = true;
				}
				if (col + 1 < n && !visited[row][col + 1]) {
					queue.offer(new int[] { row, col + 1 });
					visited[row][col + 1] = true;
				}
			}
			level++;
		}
		return res;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/01-matrix/discuss/140716/java-clear-solution
	 */
	public int[][] updateMatrix_dfs(int[][] matrix) {
        boolean[][] mark = new boolean[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] != 0) {
                    matrix[i][j] = getDistance(matrix, i, j, mark);
                }
            }
        }
        return matrix;
    }

    private int getDistance(int[][] matrix, int i, int j, boolean[][] mark) {
        if (i < 0 || i >= matrix.length || j < 0 || j >= matrix[0].length 
        		|| mark[i][j]) {
            return 100000;
        }

        if (i - 1 >= 0 && matrix[i - 1][j] == 0) {
            return 1;
        }
        if (j - 1 >= 0 && matrix[i][j - 1] == 0) {
            return 1;
        }
        if (i + 1 < matrix.length && matrix[i + 1][j] == 0) {
            return 1;
        }
        if (j + 1 < matrix[0].length && matrix[i][j + 1] == 0) {
            return 1;
        }
        
        mark[i][j] = true;
        
        int searchUp = 1 + getDistance(matrix, i - 1, j, mark);
        int searchLeft = 1 + getDistance(matrix, i, j - 1, mark);
        int searchDown = 1 + getDistance(matrix, i + 1, j, mark);
        int searchRight = 1 + getDistance(matrix, i, j + 1, mark);
        
        mark[i][j] = false;
        
        return Math.min(Math.min(searchDown, searchLeft), 
        		Math.min(searchRight, searchUp));
    }
	
	/*
	 * https://leetcode.com/problems/01-matrix/discuss/101021/Java-Solution-BFS
	 * 
	 * 1. At beginning, set cell value to Integer.MAX_VALUE if it is not 0.
	 * 2. If newly calculated distance >= current distance, then we don't need to 
	 *    explore that cell again.
	 * 
	 * We popped out the cell (cell(0),cell(1)) and now looking at all its four 
	 * adjacent cells. Since we are sure that the cell has its minimum distance from 
	 * zero, in case any of its four cells value(calling it child cell) (which is 
	 * child's distance from zero cell except when its Max Integer value) has value 
	 * more than the {value in cell} + 1 , we update the child cell to 
	 * {value in cell} + 1. "+ 1" is used because when add +1 as going from a cell to 
	 * next adjacent cell increases path by 1
	 * 
	 * Can stop exploring the cell as long as it value is not INT_MAX.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/01-matrix/discuss/101021/Java-Solution-BFS/396178
	 * https://leetcode.com/problems/01-matrix/discuss/101021/Java-Solution-BFS/104842
	 * 
	 * Other code:
	 * https://leetcode.com/problems/01-matrix/discuss/184374/Java-Easy-BFS
	 */
	public int[][] updateMatrix_BFS(int[][] matrix) {
		int m = matrix.length;
		int n = matrix[0].length;

		Queue<int[]> queue = new LinkedList<>();
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (matrix[i][j] == 0) {
					queue.offer(new int[] { i, j });
				} 
				else {
					matrix[i][j] = Integer.MAX_VALUE;
				}
			}
		}

		int[][] dirs = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

		while (!queue.isEmpty()) {
			int[] cell = queue.poll();
			for (int[] d : dirs) {
				int r = cell[0] + d[0];
				int c = cell[1] + d[1];
				if (r < 0 || r >= m || c < 0 || c >= n 
						|| matrix[r][c] <= matrix[cell[0]][cell[1]] + 1)
					continue;
				
				queue.add(new int[] { r, c });
				matrix[r][c] = matrix[cell[0]][cell[1]] + 1;
			}
		}

		return matrix;
	}
	
	/*
	 * https://leetcode.com/problems/01-matrix/discuss/101051/Simple-Java-solution-beat-99-(use-DP)
	 * 
	 * In the first sweep, we visit each entry in natural order and 
	 *   dis[i][j] = min(Integer.MAX_VALUE, min(dis[i - 1][j], dis[i][j - 1]) + 1).
	 * In the second sweep, we visit each entry in reverse order and 
	 *   dis[i][j] = min(dis[i][j], min(dis[i + 1][j], dis[i][j + 1]) + 1).
	 * 
	 * Suppose there is a directed path p from a 0 to an e(i, j) in the matrix and 
	 * suppose this path is shortest, so that dp[i, j] = length(p) after the code 
	 * terminates. The top-down & left-right pass will optimize any subpath of p that 
	 * has directions down or right, the bottom-up & right-left pass will optimize 
	 * any subpath of p that has direction up or left.
	 * 
	 * If 0 lies at top right, the correct value will be able to be brought to each 
	 * element along the "edges" of two sweeps.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/01-matrix/discuss/101023/18-line-C++-DP-Solution-O(n)-Easy-to-Understand/104851
	 * https://leetcode.com/problems/01-matrix/discuss/101039/Java-33ms-solution-with-two-sweeps-in-O(n)/104871
	 * https://leetcode.com/problems/01-matrix/discuss/101039/Java-33ms-solution-with-two-sweeps-in-O(n)
	 * 
	 * Other code:
	 * https://leetcode.com/problems/01-matrix/discuss/101039/Java-33ms-solution-with-two-sweeps-in-O(n)/104872
	 * https://leetcode.com/problems/01-matrix/discuss/101039/Java-33ms-solution-with-two-sweeps-in-O(n)/104876
	 */
	public int[][] updateMatrix_dp(int[][] matrix) {
		if (matrix.length == 0 || matrix[0].length == 0) {
			return matrix;
		}
		
		int[][] dis = new int[matrix.length][matrix[0].length];
		int range = matrix.length + matrix[0].length;

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (matrix[i][j] == 0) {
					dis[i][j] = 0;
				} 
				else {
					int upCell = (i - 1 >= 0) ? dis[i - 1][j] : range;
					int leftCell = (j - 1 >= 0) ? dis[i][j - 1] : range;
					dis[i][j] = Math.min(upCell, leftCell) + 1;
				}
			}
		}

		for (int i = matrix.length - 1; i >= 0; i--) {
			for (int j = matrix[i].length - 1; j >= 0; j--) {
				if (matrix[i][j] == 0) {
					dis[i][j] = 0;
				} 
				else {
					int downCell = (i + 1 < matrix.length) ? dis[i + 1][j] : range;
					int rightCell = (j + 1 < matrix[0].length) ? dis[i][j + 1] : range;
					dis[i][j] = Math.min(Math.min(downCell, rightCell) + 1, 
							dis[i][j]);
				}
			}
		}
		return dis;
	}
	
	/*
	 * https://leetcode.com/problems/01-matrix/discuss/101021/Java-Solution-BFS/104825
	 * 
	 * Used an array to keep track of unreached cells.
	 * This question is very similar with 286 Walls and Gates.
	 * 
	 * 1. We choose BFS because we need to visit the neighbors
	 * 2. Mark each 0 as visited in another boolean array as TRUE
	 * 3. Now let's go all four directions, and jump to non zero neighbor(having 
	 *    value 1) and not visited ones, assign this cell's value to "cell value from 
	 *    where we jumped" + 1
	 * 4. Add this incremented row and column to Queue to visit it's neighbors as 
	 *    well to follow the same pattern
	 * 
	 * Rf: https://leetcode.com/problems/01-matrix/discuss/101021/Java-Solution-BFS/244369
	 * 
	 * Other code:
	 * https://leetcode.com/problems/01-matrix/discuss/418262/Simple-Java-solution-using-BFS-for-slow-learners-like-myself
	 */
	public int[][] updateMatrix_boolean(int[][] matrix) {
		int m = matrix.length, n = matrix[0].length;

		Queue<int[]> queue = new LinkedList<>();
		boolean[][] visited = new boolean[m][n];

		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (matrix[i][j] == 0) {
					queue.offer(new int[] { i, j });
					visited[i][j] = true;
				}
			}
		}

		int[][] dir = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
		while (!queue.isEmpty()) {
			int[] cur = queue.poll();
			for (int i = 0; i < 4; i++) {
				int row = cur[0] + dir[i][0];
				int col = cur[1] + dir[i][1];
				if (row < 0 || row >= m || col < 0 || col >= n || visited[row][col]) {
					continue;
				}
				
				visited[row][col] = true;
				matrix[row][col] = matrix[cur[0]][cur[1]] + 1;
				queue.offer(new int[] { row, col });
			}
		}
		return matrix;
	}
	
    /*
     * The following 3 functions are modified from this link.
     * https://leetcode.com/problems/01-matrix/discuss/101060/Java-DFS-solution-beat-95
     * 
     * Using DFS method.
     * 1. Assigned a large value to all the positions with value 1 and don't have 0 
     *    neighbors
     * 2. Start dfs search from positions whose value is 1
     * 
     * Rf :
     * https://leetcode.com/problems/01-matrix/discuss/101060/Java-DFS-solution-beat-95/165293
     */
	public int[][] updateMatrix_bad_dfs(int[][] matrix) {
		if (matrix.length == 0)
			return matrix;

		// Initialize, only "1" who neighbors "0" is determined.
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[0].length; j++)
				if (matrix[i][j] == 1 && !hasNeiberZero(i, j, matrix))
					matrix[i][j] = matrix.length + matrix[0].length + 1;

		// Start from the determined "1"
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[0].length; j++)
				if (matrix[i][j] == 1)
					dfs(matrix, i, j, 1);

		return matrix;
	}

	private void dfs(int[][] matrix, int x, int y, int val) {
		if (x < 0 || y < 0 || y >= matrix[0].length || x >= matrix.length 
				|| (matrix[x][y] < val)) {
			return;
		}

		matrix[x][y] = val;

		dfs(matrix, x + 1, y, matrix[x][y] + 1);
		dfs(matrix, x - 1, y, matrix[x][y] + 1);
		dfs(matrix, x, y + 1, matrix[x][y] + 1);
		dfs(matrix, x, y - 1, matrix[x][y] + 1);
	}

	private boolean hasNeiberZero(int x, int y, int[][] matrix) {
		if (x - 1 >= 0 && matrix[x - 1][y] == 0)
			return true;
		if (x + 1 < matrix.length && matrix[x + 1][y] == 0)
			return true;
		if (y - 1 >= 0 && matrix[x][y - 1] == 0)
			return true;
		if (y + 1 < matrix[0].length && matrix[x][y + 1] == 0)
			return true;

		return false;
	}
	
	// by myself
	public int[][] updateMatrix_self(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return new int[0][0];
        }
        
        int[][] res = new int[matrix.length][matrix[0].length];
        LinkedList<int[]> queue = new LinkedList<>();
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 0) {
                    queue.offerLast(new int[] {i, j});
                    set.add(i * matrix[0].length + j);
                }
            }
        }
        
        int level = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] cur = queue.pollFirst();
                int y = cur[0];
                int x = cur[1];
                
                res[y][x] = level;
                
                if (y - 1 >= 0 && !set.contains((y - 1) * matrix[0].length + x)) {
                    queue.offerLast(new int[] {y - 1, x});
                    set.add((y - 1) * matrix[0].length + x);
                }
                if (y + 1 < res.length && !set.contains((y + 1) * matrix[0].length + x)) {
                    queue.offerLast(new int[] {y + 1, x});
                    set.add((y + 1) * matrix[0].length + x);
                }
                if (x + 1 < res[0].length && !set.contains(y * matrix[0].length + (x + 1))) {
                    queue.offerLast(new int[] {y, x + 1});
                    set.add(y * matrix[0].length + (x + 1));
                }
                if (x - 1 >= 0 && !set.contains(y * matrix[0].length + (x - 1))) {
                    queue.offerLast(new int[] {y, x - 1});
                    set.add(y * matrix[0].length + (x - 1));
                }
            }
            level++;
        }
        return res;
    }
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/01-matrix/discuss/101034/python-BFS-solution
     * https://leetcode.com/problems/01-matrix/discuss/142584/Python-simple-and-readable-solution-beats-100
     * https://leetcode.com/problems/01-matrix/discuss/101102/Short-solution-Each-path-needs-at-most-one-turn
     * https://leetcode.com/problems/01-matrix/discuss/101080/Python-Simple-with-Explanation
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/01-matrix/discuss/101023/18-line-C%2B%2B-DP-Solution-O(n)-Easy-to-Understand
     */

}
