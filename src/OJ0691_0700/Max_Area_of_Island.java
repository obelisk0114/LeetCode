package OJ0691_0700;

import java.util.Arrays;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Stack;

public class Max_Area_of_Island {
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/max-area-of-island/discuss/669173/5-line-DFS-code-JAVA-100
	 * 
	 * The idea is to count the area of each island using dfs. During the dfs, we set 
	 * the value of each point in the island to 0. The time complexity is O(mn).
	 * 
	 * Rf :
	 * https://leetcode.com/problems/max-area-of-island/discuss/108533/JavaC%2B%2B-Straightforward-dfs-solution
	 */
	public int maxAreaOfIsland_dfs(int[][] grid) {
		int max = 0;
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				max = Math.max(max, dfs(grid, i, j));
			}
		}
		return max;
	}

	int dfs(int[][] grid, int row, int column) {
		if (row < 0 || row >= grid.length || column < 0 || column >= grid[0].length 
				|| grid[row][column] == 0)
			return 0;

		grid[row][column] = 0;

		return 1 + dfs(grid, row + 1, column)
		         + dfs(grid, row - 1, column) 
		         + dfs(grid, row, column + 1)
				 + dfs(grid, row, column - 1);
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/max-area-of-island/discuss/108576/Easy-to-understand-Java-DFS-Solution-Just-another-Number-of-Island-problem.
	 * 
	 * Set a count to get the size of island then compare with a globalmax (max), 
	 * remember to reset it back to 0 every time when you find a new island.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/max-area-of-island/discuss/108576/Easy-to-understand-Java-DFS-Solution-Just-another-Number-of-Island-problem./190113
	 */
	public int maxAreaOfIsland_DFS3(int[][] grid) {
		if (grid.length == 0 || grid[0].length == 0) {
			return 0;
		}
		
		int m = grid.length, n = grid[0].length;
		
		int max = 0;
		int[] count = new int[1];
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (grid[i][j] == 1) {
					count[0] = 0;
					dfs3(grid, i, j, m, n, count);
					max = Math.max(count[0], max);
				}
			}
		}
		return max;
	}

	private void dfs3(int[][] grid, int i, int j, int m, int n, int[] count) {
		if (i < 0 || j < 0 || i >= m || j >= n || grid[i][j] != 1) {
			return;
		}
		
		// marked visited;
		grid[i][j] = -1;
		
		count[0]++;
		
		dfs3(grid, i + 1, j, m, n, count);
		dfs3(grid, i - 1, j, m, n, count);
		dfs3(grid, i, j + 1, m, n, count);
		dfs3(grid, i, j - 1, m, n, count);
	}
    
    /*
     * The following variable and 2 functions are from this link.
     * https://leetcode.com/problems/max-area-of-island/discuss/186891/Java.-BFSDFSUnion-Find.
     */
	private int[][] DIRECTIONS_BFS = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };

	public int maxAreaOfIsland_BFS(int[][] grid) {
		if (grid == null || grid.length == 0 || grid[0].length == 0)
			return 0;
		
		int M = grid.length;
		int N = grid[0].length;
		
		boolean[][] visited = new boolean[M][N];
		int res = 0;
		
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < N; j++) {
				if (grid[i][j] == 1 && !visited[i][j]) {
					res = Math.max(res, bfs(grid, visited, i, j));
				}
			}
		}
		return res;
	}

	private int bfs(int[][] grid, boolean[][] visited, int i, int j) {
		Queue<int[]> q = new LinkedList<>();
		q.add(new int[] { i, j });
		visited[i][j] = true;
		
		int res = 0;
		
		while (!q.isEmpty()) {
			int[] curr = q.poll();
			res++;
			
			for (int[] dir : DIRECTIONS_BFS) {
				int x = curr[0] + dir[0];
				int y = curr[1] + dir[1];
				if (x < 0 || y < 0 || x >= grid.length || y >= grid[0].length || visited[x][y] || grid[x][y] != 1)
					continue;
				
				q.add(new int[] { x, y });
				visited[x][y] = true;
			}
		}
		return res;
	}

    /*
     * The following variable, 2 functions and class are from this link.
     * https://leetcode.com/problems/max-area-of-island/discuss/186891/Java.-BFSDFSUnion-Find.
     */
	private int[][] DIRECTIONS_UF = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };

	public int maxAreaOfIsland_UF(int[][] grid) {
		if (grid == null || grid.length == 0 || grid[0].length == 0)
			return 0;
		
		int M = grid.length;
		int N = grid[0].length;
		
		int res = 0;
		UnionFind uf = new UnionFind(grid);
		
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < N; j++) {
				if (grid[i][j] == 1) {
					res = Math.max(res, link_UF(grid, i, j, uf, M, N));
				}
			}
		}
		return res;
	}

	private int link_UF(int[][] grid, int i, int j, UnionFind uf, int M, int N) {
		int pre = i * N + j;
		int res = uf.getSize(pre);
		
		for (int[] dir : DIRECTIONS_UF) {
			int x = i + dir[0];
			int y = j + dir[1];
			
			if (x < 0 || y < 0 || x >= M || y >= N || grid[x][y] != 1)
				continue;
			
			res = Math.max(res, uf.union(pre, x * N + y));
		}
		return res;
	}

	class UnionFind {
		int[] parent;
		int[] size;
		int[] rank;

		UnionFind(int[][] grid) {
			int M = grid.length;
			int N = grid[0].length;
			
			this.parent = new int[M * N];
			for (int i = 0; i < M * N; i++)
				this.parent[i] = i;
			
			this.rank = new int[M * N];
			
			this.size = new int[M * N];
			Arrays.fill(this.size, 1);
		}

		int find(int x) {
			if (this.parent[x] != x) {
				this.parent[x] = find(this.parent[x]);
			}
			return this.parent[x];
		}

		int union(int x, int y) {
			int px = find(x);
			int py = find(y);

			if (px == py)
				return this.size[px];
			if (this.rank[px] > this.rank[py]) {
				this.parent[py] = px;
				this.size[px] = this.size[px] + this.size[py];
				return this.size[px];
			} 
			else if (this.rank[px] < this.rank[py]) {
				this.parent[px] = py;
				this.size[py] = this.size[px] + this.size[py];
				return this.size[py];
			} 
			else {
				this.parent[px] = py;
				this.rank[py]++;
				this.size[py] = this.size[px] + this.size[py];
				return this.size[py];
			}
		}

		int getSize(int x) {
			return this.size[find(x)];
		}
	}
	
	/*
	 * The following 2 functions are by myself.
	 */
	public int maxAreaOfIsland_self(int[][] grid) {
		int[] ans = { 0 };

		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if (grid[i][j] == 1) {
					dfs_self(grid, i, j, ans);
				}
			}
		}

		return ans[0];
	}
    
    private int dfs_self(int[][] grid, int i, int j, int[] ans) {
        if (i < 0 || i == grid.length || j < 0 || j == grid[0].length 
        		|| grid[i][j] != 1) {
        	
            return 0;
        }
        
        grid[i][j] = -1;
        
        int local = 1;
        local += dfs_self(grid, i + 1, j, ans);
        local += dfs_self(grid, i - 1, j, ans);
        local += dfs_self(grid, i, j + 1, ans);
        local += dfs_self(grid, i, j - 1, ans);
        
        if (local > ans[0]) {
            ans[0] = local;
        }
        
        return local;
    }
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/max-area-of-island/discuss/108529/Very-simple-DFS-Java-solution
	 * 
	 * Other code:
	 * https://leetcode.com/problems/max-area-of-island/discuss/529049/Java-99.64-speed-85.19-storage-DFS
	 */
	public int maxAreaOfIsland_dfs2(int[][] grid) {
		if (grid == null || grid.length == 0) {
			return 0;
		}
		
		int m = grid.length;
		int n = grid[0].length;
		
		int max = 0;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (grid[i][j] == 1) {
					int area = dfs2(grid, i, j, m, n, 0);
					max = Math.max(area, max);
				}
			}
		}
		return max;
	}

	int dfs2(int[][] grid, int i, int j, int m, int n, int area) {
		if (i < 0 || i >= m || j < 0 || j >= n || grid[i][j] == 0) {
			return area;
		}
		
		grid[i][j] = 0;
		area++;
		
		area = dfs2(grid, i + 1, j, m, n, area);
		area = dfs2(grid, i, j + 1, m, n, area);
		area = dfs2(grid, i - 1, j, m, n, area);
		area = dfs2(grid, i, j - 1, m, n, area);
		
		return area;
	}
	
	/*
	 * https://leetcode.com/articles/max-area-of-island/
	 * Approach #2: Depth-First Search (Iterative)
	 * 
	 * Here, "seen" will represent squares that have either been visited or are added 
	 * to our list of squares to visit ("stack"). For every starting land square that 
	 * hasn't been visited, we will explore 4-directionally around it, adding land 
	 * squares that haven't been added to "seen" to our "stack".
	 * 
	 * On the side, we'll keep a count "shape" of the total number of squares seen 
	 * during the exploration of this shape. We'll want the running max of these 
	 * counts.
	 */
	public int maxAreaOfIsland_iterative(int[][] grid) {
        boolean[][] seen = new boolean[grid.length][grid[0].length];
        int[] dr = new int[]{1, -1, 0, 0};
        int[] dc = new int[]{0, 0, 1, -1};

        int ans = 0;
        for (int r0 = 0; r0 < grid.length; r0++) {
            for (int c0 = 0; c0 < grid[0].length; c0++) {
                if (grid[r0][c0] == 1 && !seen[r0][c0]) {
                    int shape = 0;
                    
                    Stack<int[]> stack = new Stack<>();
                    stack.push(new int[]{r0, c0});
                    seen[r0][c0] = true;
                    
                    while (!stack.empty()) {
                        int[] node = stack.pop();
                        int r = node[0], c = node[1];
                        shape++;
                        
                        for (int k = 0; k < 4; k++) {
                            int nr = r + dr[k];
                            int nc = c + dc[k];
                            if (0 <= nr && nr < grid.length &&
                                    0 <= nc && nc < grid[0].length &&
                                    grid[nr][nc] == 1 && !seen[nr][nc]) {
                            	
                                stack.push(new int[]{nr, nc});
                                seen[nr][nc] = true;
                            }
                        }
                    }
                    ans = Math.max(ans, shape);
                }
            }
        }
        return ans;
    }

    /**
     * Python collections
     * 
     * https://leetcode.com/problems/max-area-of-island/discuss/185446/Simple-DFS-Pytohn
     * https://leetcode.com/problems/max-area-of-island/discuss/242183/Python-DFS-Solution-runtime-52ms-and-memory-usage-12.6-MB
     * https://leetcode.com/problems/max-area-of-island/discuss/108541/easy-python
     * https://leetcode.com/problems/max-area-of-island/discuss/151481/Python3-DFSBFS-solution
     * https://leetcode.com/problems/max-area-of-island/discuss/395168/Solution-in-Python-3-(BFS-and-DFS)-(beats-~-90-and-82)
     * https://leetcode.com/problems/max-area-of-island/discuss/180281/python3-union-find-solution
     * https://leetcode.com/problems/max-area-of-island/discuss/108565/4-lines
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/max-area-of-island/discuss/251369/C%2B%2B-BFS-and-DFS
     * https://leetcode.com/problems/max-area-of-island/discuss/289337/short-and-clean-C%2B%2B-solution
     */
    
    /**
     * JavaScript collections
     * 
     * https://leetcode.com/problems/max-area-of-island/discuss/108536/JavaScript-solution-using-DFS
     * https://leetcode.com/problems/max-area-of-island/discuss/108532/JavaScript-DFS-solution-beats-92
     */

}
