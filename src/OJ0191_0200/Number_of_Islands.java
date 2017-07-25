package OJ0191_0200;

import java.util.LinkedList;

public class Number_of_Islands {
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/43565/java-concise-dfs-solution
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/13248/very-concise-java-ac-solution
	 * https://discuss.leetcode.com/topic/16562/sink-the-island-java-solution
	 */
	public int numIslands(char[][] grid) {
		int ret = 0;
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				if (grid[i][j] == '1') {
					dfs(grid, i, j);
					ret++;
				}
			}
		}
		return ret;
	}
	private void dfs(char[][] grid, int m, int n) {
	    if (m < 0 || m >= grid.length || n < 0 || n >= grid[0].length || grid[m][n] != '1') {
	        return;
	    }
		grid[m][n] = '#';
		dfs(grid, m + 1, n);
		dfs(grid, m - 1, n);
		dfs(grid, m, n + 1);
		dfs(grid, m, n - 1);
	}
	
	/*
	 * The following 2 variables and 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/11590/simple-java-solution
	 * 
	   1. Scan each cell in the grid.
       2. If the cell value is '1' explore that island.
       3. Mark the explored island cells with 'x'.
       4. Once finished exploring that island, increment islands counter.
       
       The arrays dx[], dy[] store the possible moves from the current cell. 
       
       Two land cells ['1'] are considered from the same island 
       if they are horizontally or vertically adjacent 
       (possible moves (-1,0),(0,1),(0,-1),(1,0)). 
       Two '1' diagonally adjacent are not considered from the same island.
     *
	 */
	static int[] dx = { -1, 0, 0, 1 };
	static int[] dy = { 0, 1, -1, 0 };
	public static int numIslands2(char[][] grid) {
		if (grid == null || grid.length == 0)
			return 0;
		int islands = 0;
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if (grid[i][j] == '1') {
					explore(grid, i, j);
					islands++;
				}
			}
		}
		return islands;
	}
	public static void explore(char[][] grid, int i, int j) {
		grid[i][j] = 'x';
		for (int d = 0; d < dx.length; d++) {
			if (i + dy[d] < grid.length && i + dy[d] >= 0 && 
					j + dx[d] < grid[0].length && j + dx[d] >= 0
					&& grid[i + dy[d]][j + dx[d]] == '1') {
				explore(grid, i + dy[d], j + dx[d]);
			}
		}
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/16546/java-dfs-and-bfs-solution
	 */
	public int numIslands_BFS(char[][] grid) {
		int count = 0;
		for (int i = 0; i < grid.length; i++)
			for (int j = 0; j < grid[0].length; j++) {
				if (grid[i][j] == '1') {
					bfsFill(grid, i, j);
					count++;
				}
			}
		return count;
	}
	private void bfsFill(char[][] grid, int x, int y) {
		grid[x][y] = '0';
		int n = grid.length;
		int m = grid[0].length;
		LinkedList<Integer> queue = new LinkedList<Integer>();
		int code = x * m + y;
		queue.offer(code);
		while (!queue.isEmpty()) {
			code = queue.poll();
			int i = code / m;
			int j = code % m;
			if (i > 0 && grid[i - 1][j] == '1') // search upward and mark
												// adjacent '1's as '0'.
			{
				queue.offer((i - 1) * m + j);
				grid[i - 1][j] = '0';
			}
			if (i < n - 1 && grid[i + 1][j] == '1') // down
			{
				queue.offer((i + 1) * m + j);
				grid[i + 1][j] = '0';
			}
			if (j > 0 && grid[i][j - 1] == '1') // left
			{
				queue.offer(i * m + j - 1);
				grid[i][j - 1] = '0';
			}
			if (j < m - 1 && grid[i][j + 1] == '1') // right
			{
				queue.offer(i * m + j + 1);
				grid[i][j + 1] = '0';
			}
		}
	}
	
	/*
	 * Union-Find
	 * https://discuss.leetcode.com/topic/33947/java-union-find-solution
	 * https://discuss.leetcode.com/topic/11969/ac-java-solution-using-union-find-with-explanations
	 */
	
	/*
	 * The followings are all from this link.
	 * https://discuss.leetcode.com/topic/39980/1d-union-find-java-solution-easily-generalized-to-other-problems
	 */
	int[][] distance = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

	public int numIslands_unionFind(char[][] grid) {
		if (grid == null || grid.length == 0 || grid[0].length == 0) {
			return 0;
		}
		UnionFind uf = new UnionFind(grid);
		int rows = grid.length;
		int cols = grid[0].length;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (grid[i][j] == '1') {
					for (int[] d : distance) {
						int x = i + d[0];
						int y = j + d[1];
						if (x >= 0 && x < rows && y >= 0 && y < cols && grid[x][y] == '1') {
							int id1 = i * cols + j;
							int id2 = x * cols + y;
							uf.union(id1, id2);
						}
					}
				}
			}
		}
		return uf.count;
	}
	class UnionFind {
		int[] father;
		int m, n;
		int count = 0;

		UnionFind(char[][] grid) {
			m = grid.length;
			n = grid[0].length;
			father = new int[m * n];
			for (int i = 0; i < m; i++) {
				for (int j = 0; j < n; j++) {
					if (grid[i][j] == '1') {
						int id = i * n + j;
						father[id] = id;
						count++;
					}
				}
			}
		}
		
		public void union(int node1, int node2) {
			int find1 = find(node1);
			int find2 = find(node2);
			if (find1 != find2) {
				father[find1] = find2;
				count--;
			}
		}
		
		public int find(int node) {
			if (father[node] == node) {
				return node;
			}
			father[node] = find(father[node]);
			return father[node];
		}
	}

}
