package OJ0411_0420;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.HashSet;

public class Pacific_Atlantic_Water_Flow {
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/78592/java-dfs-beats-93
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/62441/cleanest-and-shortest-answer-with-inline-explanation-upvote-me
	 */
	public List<int[]> pacificAtlantic(int[][] matrix) {
		List<int[]> result = new ArrayList<>();
		int m = matrix.length;
		if (m == 0)
			return result;
		int n = matrix[0].length;

		boolean[][] pacific = new boolean[m][n];
		for (int i = 0; i < m; i++) {
			markAllValidNeighbors(matrix, pacific, i, 0);
		}
		for (int i = 1; i < n; i++) {
			markAllValidNeighbors(matrix, pacific, 0, i);
		}

		boolean[][] atlantic = new boolean[m][n];
		for (int i = 0; i < m; i++) {
			markAllValidNeighbors(matrix, atlantic, i, n - 1);
		}
		for (int i = 0; i < n - 1; i++) {
			markAllValidNeighbors(matrix, atlantic, m - 1, i);
		}

		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (pacific[i][j] && atlantic[i][j]) {
					result.add(new int[] { i, j });
				}
			}
		}
		return result;
	}

	private void markAllValidNeighbors(int[][] matrix, boolean[][] canReach, int i, int j) {
		if (i < 0 || i >= canReach.length || j < 0 || j >= canReach[0].length || canReach[i][j])
			return;

		canReach[i][j] = true;
		if (i - 1 >= 0 && matrix[i - 1][j] >= matrix[i][j])
			markAllValidNeighbors(matrix, canReach, i - 1, j);
		if (i + 1 < canReach.length && matrix[i + 1][j] >= matrix[i][j])
			markAllValidNeighbors(matrix, canReach, i + 1, j);
		if (j - 1 >= 0 && matrix[i][j - 1] >= matrix[i][j])
			markAllValidNeighbors(matrix, canReach, i, j - 1);
		if (j + 1 < canReach[0].length && matrix[i][j + 1] >= matrix[i][j])
			markAllValidNeighbors(matrix, canReach, i, j + 1);
	}
	
	/*
	 * The following 2 variables and 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/65373/java-dfs-solution
	 * 
	 * All the valid positions must have at least one path to connect to the ocean, 
	 * so we start from the ocean to find out all the paths.
	 * 
	 * Rf :
	 * https://discuss.leetcode.com/topic/62280/simple-commented-java-solution-with-thinking-progress-o-n 
	 * https://discuss.leetcode.com/topic/62585/java-17ms-solution-simple-and-clear-similar-to-number-of-islands-s-idea
	 */
	int[] dx = { -1, 0, 0, 1 };
	int[] dy = { 0, -1, 1, 0 };
	public List<int[]> pacificAtlantic_canflow(int[][] matrix) {
		List<int[]> flows = new ArrayList<>();
		if (matrix == null || matrix.length == 0 || matrix[0].length == 0)
			return flows;
		boolean[][] visitedAtlantic = new boolean[matrix.length][matrix[0].length];
		boolean[][] visitedPacific = new boolean[matrix.length][matrix[0].length];

		for (int i = 0; i < matrix.length; i++) {
			canFlow(matrix, visitedPacific, 0, i, 0);
			canFlow(matrix, visitedAtlantic, 0, i, matrix[i].length - 1);
		}
		for (int j = 0; j < matrix[0].length; j++) {
			canFlow(matrix, visitedPacific, 0, 0, j);
			canFlow(matrix, visitedAtlantic, 0, matrix.length - 1, j);
		}

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (visitedAtlantic[i][j] && visitedPacific[i][j])
					flows.add(new int[] { i, j });
			}
		}
		return flows;
	}
	public void canFlow(int[][] m, boolean[][] visited, int height, int i, int j) {
		if (i < 0 || i >= m.length || j < 0 || j >= m[i].length || visited[i][j] || m[i][j] < height)
			return;
		
		visited[i][j] = true;
		for (int d = 0; d < dx.length; d++) {
			canFlow(m, visited, m[i][j], i + dy[d], j + dx[d]);
		}
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/pacific-atlantic-water-flow/discuss/90733/java-bfs-dfs-from-ocean
	 * 
	 * 1. Two Queue and add all the Pacific border to one queue; Atlantic border to 
	 *    another queue.
	 * 2. Keep a visited matrix for each queue. In the end, add the cell visited by 
	 *    two queue to the result.
	 * BFS: Water flood from ocean to the cell. Since water can only flow from 
	 * high/equal cell to low cell, add the neighbor cell with height larger or equal 
	 * to current cell to the queue and mark as visited.
	 * 
	 * we want to check the other way around, which is to check if we can flow from 
	 * the center to both Pacific and Atlantic. Since we only start from the 
	 * boundaries, we propagate forward the BFS only when the new point can flow back 
	 * to the boundary, which means the new point is higher or equal.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/pacific-atlantic-water-flow/discuss/90733/Java-BFS-and-DFS-from-Ocean/300905
	 */
	int[][] dir_bfs = new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

	public List<List<Integer>> pacificAtlantic_bfs(int[][] matrix) {
		List<List<Integer>> res = new LinkedList<>();
		if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
			return res;
		}
		
		int n = matrix.length, m = matrix[0].length;
		
		// One visited map for each ocean
		boolean[][] pacific = new boolean[n][m];
		boolean[][] atlantic = new boolean[n][m];
		Queue<int[]> pQueue = new LinkedList<>();
		Queue<int[]> aQueue = new LinkedList<>();
		
		for (int i = 0; i < n; i++) { // Vertical border
			pQueue.offer(new int[] { i, 0 });
			aQueue.offer(new int[] { i, m - 1 });
			pacific[i][0] = true;
			atlantic[i][m - 1] = true;
		}
		for (int i = 0; i < m; i++) { // Horizontal border
			pQueue.offer(new int[] { 0, i });
			aQueue.offer(new int[] { n - 1, i });
			pacific[0][i] = true;
			atlantic[n - 1][i] = true;
		}
		
		bfs(matrix, pQueue, pacific);
		bfs(matrix, aQueue, atlantic);
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				if (pacific[i][j] && atlantic[i][j]) {
					List<Integer> list = new ArrayList<>();
					list.add(i);
					list.add(j);
					
					res.add(list);
				}
			}
		}
		return res;
	}

	public void bfs(int[][] matrix, Queue<int[]> queue, boolean[][] visited) {
		int n = matrix.length, m = matrix[0].length;
		while (!queue.isEmpty()) {
			int[] cur = queue.poll();
			
			for (int[] d : dir_bfs) {
				int x = cur[0] + d[0];
				int y = cur[1] + d[1];
				if (x < 0 || x >= n || y < 0 || y >= m 
						|| visited[x][y] || matrix[x][y] < matrix[cur[0]][cur[1]]) {
					continue;
				}
				
				visited[x][y] = true;
				queue.offer(new int[] { x, y });
			}
		}
	}
	
	/*
	 * The following 2 variables and 3 functions are from this link.
	 * https://discuss.leetcode.com/topic/62415/reverse-flow-technique-clean-java-solution-using-dfs-and-state-machine
	 * 
	 * 1. DFS from first row and first column and mark all the reachable nodes 
	 * (reverse comparison) as 1.
     *    If cell is marked as 1 or 3 already, break DFS
     *    If cell is marked as 2 already, mark it 3
     * 
     * 2. DFS from last row and last column and mark all the reachable nodes 
     * (reverse comparison) as 2.
     *    If cell is marked as 2 or 3 already, break DFS
     *    If cell is marked as 1 already, mark it 3
     *    
     * Rf : https://discuss.leetcode.com/topic/63250/java-28ms-bfs-solution-using-one-queue
	 */
	int m;
	int n;
	public List<int[]> pacificAtlantic_state(int[][] matrix) {
		List<int[]> res = new ArrayList<>();
		m = matrix.length;
		if (m == 0)
			return res;

		n = matrix[0].length;
		if (n == 0)
			return res;

		// 0 not visited
		// 1 pacific
		// 2 atlantic
		// 3 both
		int[][] touchdown = new int[m][n];
		for (int i = 0; i < m; ++i) {
			dfs_state(matrix, touchdown, i, 0, 1, res);
			dfs_state(matrix, touchdown, i, n - 1, 2, res);
		}
		for (int j = 0; j < n; ++j) {
			dfs_state(matrix, touchdown, 0, j, 1, res);
			dfs_state(matrix, touchdown, m - 1, j, 2, res);
		}
		return res;
	}
	private void dfs_state(int[][] matrix, int[][] touchdown, int i, int j, int toState, List<int[]> res) {
		if (i < 0 || j < 0 || i >= m || j >= n)
			return;
		if (!updateState(touchdown, i, j, toState, res)) {
			return;
		}

		if (i + 1 < m && matrix[i][j] <= matrix[i + 1][j]) {
			dfs_state(matrix, touchdown, i + 1, j, toState, res);
		}
		if (j + 1 < n && matrix[i][j] <= matrix[i][j + 1]) {
			dfs_state(matrix, touchdown, i, j + 1, toState, res);
		}
		if (i - 1 >= 0 && matrix[i][j] <= matrix[i - 1][j]) {
			dfs_state(matrix, touchdown, i - 1, j, toState, res);
		}
		if (j - 1 >= 0 && matrix[i][j] <= matrix[i][j - 1]) {
			dfs_state(matrix, touchdown, i, j - 1, toState, res);
		}
	}
	private boolean updateState(int[][] touchdown, int i, int j, int toState, List<int[]> res) {
		int currState = touchdown[i][j];
		if (currState == 3 || currState == toState)
			return false;
		if (currState == 0) {
			touchdown[i][j] = toState;
		} else {
			touchdown[i][j] = 3;
			res.add(new int[] { i, j });
		}
		return true;
	}
	
	/*
	 * The following variable and 4 functions are from this link.
	 * https://discuss.leetcode.com/topic/62373/simple-java-dfs-solution
	 */
	private int[][] direction = new int[][] { {1, 0}, {0, 1}, {-1, 0}, {0, -1} };
	public List<int[]> pacificAtlantic_set(int[][] matrix) {
		List<int[]> result = new ArrayList<>();
		if (matrix.length == 0)
			return result;
		Set<Integer> pacific = new HashSet<>();
		Set<Integer> atlantic = new HashSet<>();
		for (int i = 0; i < matrix[0].length; i++) {
			dfs_set(matrix, 0, i, pacific);
			dfs_set(matrix, matrix.length - 1, i, atlantic);
		}
		for (int i = 0; i < matrix.length; i++) {
			dfs_set(matrix, i, 0, pacific);
			dfs_set(matrix, i, matrix[0].length - 1, atlantic);
		}

		for (int i : pacific) {
			if (atlantic.contains(i)) {
				result.add(decode(i, matrix));
			}
		}
		return result;
	}
	private void dfs_set(int[][] matrix, int i, int j, Set<Integer> result) {
		if (!result.add(encode(i, j, matrix)))
			return;
		for (int[] dir : direction) {
			int x = dir[0] + i;
			int y = dir[1] + j;
			if (x >= 0 && x < matrix.length && y >= 0 && y < matrix[0].length && matrix[x][y] >= matrix[i][j]) {
				dfs_set(matrix, x, y, result);
			}
		}
	}
	private int[] decode(int i, int[][] matrix) {
		return new int[] { i / matrix[0].length, i % matrix[0].length };
	}
	private int encode(int i, int j, int[][] matrix) {
		return i * matrix[0].length + j;
	}

}
