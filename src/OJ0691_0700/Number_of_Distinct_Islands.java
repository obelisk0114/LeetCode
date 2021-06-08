package OJ0691_0700;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Queue;

public class Number_of_Distinct_Islands {
	/*
	 * The following 2 functions are modified by myself.
	 * 
	 * The key to the solution is to find a way to represent a distinct shape. 
	 * 
	 * Rf :
	 * https://leetcode.com/problems/number-of-distinct-islands/discuss/108474/JavaC%2B%2B-Clean-Code
	 * https://leetcode.com/problems/number-of-distinct-islands/discuss/108474/JavaC++-Clean-Code/114958
	 * https://leetcode.com/problems/number-of-distinct-islands/discuss/150037/DFS-with-Explanations
	 */
	public int numDistinctIslands_self_edit(int[][] grid) {
		Set<List<List<Integer>>> islands = new HashSet<>();

		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if (grid[i][j] == 1) {
					List<List<Integer>> island = new ArrayList<>();

					dfs_self_edit(i, j, i, j, grid, island);
					
					islands.add(island);
				}
			}
		}
		return islands.size();
	}

	private void dfs_self_edit(int i0, int j0, int i, int j, int[][] grid, 
			List<List<Integer>> island) {

		if (i < 0 || i == grid.length || j < 0 || j == grid[0].length 
				|| grid[i][j] <= 0) {
			
			return;
		}

		// 若用 int[] 需要實作 equals 和 hashcode 去比較內部元素
		// 直接使用 list, 因為 list 已經實作好了
		island.add(Arrays.asList(i - i0, j - j0));
		grid[i][j] *= -1;

		dfs_self_edit(i0, j0, i + 1, j, grid, island);
        dfs_self_edit(i0, j0, i - 1, j, grid, island);
        dfs_self_edit(i0, j0, i, j + 1, grid, island);
        dfs_self_edit(i0, j0, i, j - 1, grid, island);
	}
	
	/*
	 * The following 2 functions are modified by myself.
	 * 
	 * The key to the solution is to find a way to represent a distinct shape. 
	 * 
	 * Rf :
	 * https://leetcode.com/problems/number-of-distinct-islands/discuss/108474/JavaC%2B%2B-Clean-Code
	 * https://leetcode.com/problems/number-of-distinct-islands/discuss/108474/JavaC++-Clean-Code/114958
	 * https://leetcode.com/problems/number-of-distinct-islands/discuss/150037/DFS-with-Explanations
	 */
	public int numDistinctIslands_dfs3(int[][] grid) {
		int m = grid.length, n = grid[0].length;
		Set<List<List<Integer>>> islands = new HashSet<>();

		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (grid[i][j] == 1) {
					List<List<Integer>> island = new ArrayList<>();

					dfs_dfs3(i, j, i, j, grid, m, n, island);
					
					islands.add(island);
				}
			}
		}
		return islands.size();
	}

	private void dfs_dfs3(int i0, int j0, int i, int j, int[][] grid, 
			int m, int n, List<List<Integer>> island) {

		if (i < 0 || m <= i || j < 0 || n <= j || grid[i][j] <= 0)
			return;

		int[][] delta = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };
		
		island.add(Arrays.asList(i - i0, j - j0));
		grid[i][j] *= -1;

		for (int d = 0; d < 4; d++) {
			dfs_dfs3(i0, j0, i + delta[d][0], j + delta[d][1], grid, m, n, island);
		}
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/number-of-distinct-islands/discuss/108474/JavaC++-Clean-Code/110649
	 * 
	 * we can avoid the need to worry about order by ensuring that two islands of the 
	 * same shape are initially discovered from the same relative cell. Then from 
	 * there, the DFS will always visit the cells in the same relative order.
	 * 
	 * We can simply search for islands by iterating left to right, top to bottom. 
	 * This way, each island will always be discovered from the leftmost cell in its 
	 * top row. 
	 * 
	 * When we start a depth-first search on the top-left square of some island, the 
	 * path taken by our depth-first search will be the same if, and only if, the 
	 * shape is the same.
	 * 
	 * Each time we discover the first cell in a new island, we initialize an empty 
	 * string. Then each time dfs is called for that island, we firstly determine 
	 * whether or not the cell being entered is un-visited land. If it is, then we 
	 * append the direction we entered it from to the string.
	 * 
	 * we also need to record where we backtracked. This occurs each time we exit a 
	 * call to the dfs(...) function. We'll do this by appending a 'b' to the string.
	 * 
	 * ----------------------------------------------------------------------
	 * 
	 * use the direction string and hash it.
	 * 
	 * [[1,1,0],[0,1,1],[0,0,0],[1,1,1],[0,1,0]]
	 * 
	 * DO NOT FORGET to add path for backtracking, otherwise, we may have same result 
	 * when we count two distinct islands in some cases
	 * 
	 * eg:              1 1 1   and   1 1 0
     *                  0 1 0         0 1 1
     * with b:          rdbrbb        rdrbbb (add 'b' for each "level" (char visited))
     * without b:       rdr           rdr
	 * 
	 * -----------------------------------------------------------------------
	 * 
	 * The key to the solution is to find a way to represent a distinct shape. 
	 * To describe the shape, in fact, is to describe its moving directions
	 * 
	 * -----------------------------------------------------------------------
	 * 
	 * The execution order was like:
	 * 1). Append direction 2). Recursive call 3). Skip if visited.
	 * and in such case no "_" is needed.
	 * The order is like:
	 * 1). Skip if visited 2). Append direction 3). Recursive call.
	 * and in such case we need a "_" to distinguish the shape
	 * 
	 * So it's the order that made the difference. 
	 * 
	 * -----------------------------------------------------------------------
	 * 
	 * There is a chance of collision in the hashcode, giving a false positive. All 
	 * constant time hashsets fall prey. To avoid this:
	 * 
	 * 1. Use a BitSet. Each node needs two bits to represent u,d,l,r and one for 
	 *    back. We must then build a list of bitsets, and compare each one each time. 
	 *    (Or you can string compare, but note massively more overhead here than 
	 *    long[] (which back bitset).)
	 * 2. HashMap<Long, Object> for (note: jdk<1.8 has no BitSet). Each long would 
	 *    represent 64 bits/3 bits per node=21 nodes worth of information. If we 
	 *    would overflow the current long, we must add another - the key is the long, 
	 *    whose value is HashMap<Long, Object> (to hold all the other islands who 
	 *    match the key). We don't iterate over nearly as many items as BitSet, but 
	 *    the code is nasty enough here that I must relegate to second place... but 
	 *    your use case may vary.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/number-of-distinct-islands/solution/
	 * https://leetcode.com/problems/number-of-distinct-islands/discuss/108475/Java-very-Elegant-and-concise-DFS-Solution(Beats-100)/252839
	 * https://leetcode.com/problems/number-of-distinct-islands/discuss/108475/Java-very-Elegant-and-concise-DFS-Solution(Beats-100)/544976
	 * https://leetcode.com/problems/number-of-distinct-islands/discuss/150037/DFS-with-Explanations/194107
	 * https://leetcode.com/problems/number-of-distinct-islands/discuss/108475/Java-very-Elegant-and-concise-DFS-Solution(Beats-100)/177902
	 * https://leetcode.com/problems/number-of-distinct-islands/discuss/108475/Java-very-Elegant-and-concise-DFS-Solution(Beats-100)/383731
	 * 
	 * Other code:
	 * https://leetcode.com/problems/number-of-distinct-islands/discuss/108474/JavaC++-Clean-Code/175273
	 * https://leetcode.com/problems/number-of-distinct-islands/discuss/108474/JavaC++-Clean-Code/151889
	 * https://leetcode.com/problems/number-of-distinct-islands/discuss/150037/DFS-with-Explanations
	 */
	public int numDistinctIslands_dfs4(int[][] grid) {
		Set<String> set = new HashSet<>();
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if (grid[i][j] != 0) {
					StringBuilder sb = new StringBuilder();
					dfs_dfs4(grid, i, j, sb, "o"); // origin
					set.add(sb.toString());
				}
			}
		}
		return set.size();
	}

	private void dfs_dfs4(int[][] grid, int i, int j, StringBuilder sb, String dir) {
		if (i < 0 || i == grid.length || j < 0 || j == grid[i].length 
				|| grid[i][j] == 0)
			return;
		
		sb.append(dir);
		grid[i][j] = 0;
		
		dfs_dfs4(grid, i - 1, j, sb, "u"); // up
		dfs_dfs4(grid, i + 1, j, sb, "d"); // down
		dfs_dfs4(grid, i, j - 1, sb, "l"); // left
		dfs_dfs4(grid, i, j + 1, sb, "r"); // right
		
		sb.append("b"); // back
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://gist.github.com/BiruLyu/807f3960d6ea16f933a7de5bd4058a06
	 */
	public int numDistinctIslands_dfs_Set(int[][] grid) {
		if (grid == null || grid.length < 1 || grid[0].length < 1)
			return 0;
		
		int m = grid.length, n = grid[0].length;
		Set<String> res = new HashSet<>();
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				Set<String> set = new HashSet<>();
				
				if (grid[i][j] == 1) {
					dfs_dfs_Set(grid, i, j, i, j, set);
					res.add(set.toString());
				}
			}
		}
		
		return res.size();
	}

	public void dfs_dfs_Set(int[][] grid, int i, int j, int baseX, int baseY, 
			Set<String> set) {
		
		int m = grid.length, n = grid[0].length;
		if (i < 0 || i >= m || j < 0 || j >= n || grid[i][j] == 0)
			return;
		
		set.add((i - baseX) + "_" + (j - baseY));
		grid[i][j] = 0;
		
		dfs_dfs_Set(grid, i + 1, j, baseX, baseY, set);
		dfs_dfs_Set(grid, i - 1, j, baseX, baseY, set);
		dfs_dfs_Set(grid, i, j - 1, baseX, baseY, set);
		dfs_dfs_Set(grid, i, j + 1, baseX, baseY, set);
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/number-of-distinct-islands/discuss/426166/Java-DFS-with-Hash-Set-of-Shape-and-Path-Signature-(detailed-explanation)
	 * 
	 * The idea is to convert each island's lands to a form that is comparable.
	 * 
	 * 0 0 0
	 * 0 0 0
	 * 0 1 0
	 * 0 1 1 // for this shape we have [(2, 1), (3, 1), (3, 2)].
	 * // we should convert these coordinates to the following coordinates.
	 * 
	 * 1 0 0 // for this shape we have [(0, 0), (1, 0), (1, 1)].
	 * 1 1 0
	 * 0 0 0
	 * 0 0 0
	 * // then each "L" island has the same shape.
	 * 
	 * We also notice that a hash set list [(0, 0), (1, 0), (1, 1)] has the same hash 
	 * code with a set list [(1, 0), (1, 1), (0, 0)]. The order does not count here.
	 * 
	 * We also need to convert a coordinate (x, y) to a unique integer number, say 
	 * shapeId. If conversion: x * n + y
	 * 
	 * 0 0 1 1 1  // shape 1
	 * 1 1 1 0 0
	 * 0 0 0 0 0
	 * 0 1 1 1 1  // shape 2
	 * 1 1 0 0 0
	 * 
	 * Unfortunately, two shapes are hashed to the hash code. It occurs because 
	 * negative results exist. Since we take (0, 2) as the first coordinate, 
	 * (1, 0), (1, 1) in the second row would be transformed to negative coordinates, 
	 * which are then hashed to shapeIds that collide with the 1 in the previous row. 
	 * To solve this problem, we use this conversion: x * n * 2 + y.
	 * 
	 * ----------------------------------------------------------------
	 * 
	 * (x, y) -> x * n + y
	 * 
	 * Because we are dealing with local coordinates, y >= 0 is not guaranteed: it 
	 * could be anywhere inside the interval [-num_columns + 1, num_columns - 1]. 
	 * However, x >= 0 is guaranteed because of how we found the shape (we looked for 
	 * land from top to bottom, left to right.)
	 * 
	 * In the end, we chose the function (x, y) -> (x * (2 * num_columns) + y). No 
	 * two local coordinates will have the same result, which is all we needed.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/number-of-distinct-islands/solution/131129
	 */
	int[][] direction_dfs5 = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

	public int numDistinctIslands_dfs5(int[][] grid) {
		if (grid == null || grid.length == 0) {
			return 0;
		}
		
		int m = grid.length;
		int n = grid[0].length;
		
		Set<Set<Integer>> allShapes = new HashSet<>();
		boolean[][] marked = new boolean[m][n];
		
		// dfs
		for (int i = 0; i < m; ++i) {
			for (int j = 0; j < n; ++j) {
				if (!marked[i][j] && grid[i][j] == 1) {
					Set<Integer> shape = new HashSet<>();
					dfs_dfs5(i, j, i, j, grid, marked, shape);
					
					// allShapes would do the checking for us
					if (shape.size() > 0) {
						allShapes.add(shape);
					}
				}
			}
		}
		return allShapes.size();
	}

	private void dfs_dfs5(int i, int j, int i0, int j0, int[][] grid, 
			boolean[][] marked, Set<Integer> shape) {
		
		int m = grid.length;
		int n = grid[0].length;
		
		marked[i][j] = true; // visit
		
		// transform to a top-left position
		int tx = i - i0, ty = j - j0;
		
		int shapeId = tx * n * 2 + ty; // critical!
		shape.add(shapeId);
		
		// for each neighbor
		for (int[] dir : direction_dfs5) {
			int x = i + dir[0];
			int y = j + dir[1];
			
			if (x >= 0 && x < m && y >= 0 && y < n 
					&& !marked[x][y] && grid[x][y] == 1) {
				
				dfs_dfs5(x, y, i0, j0, grid, marked, shape);
			}
		}
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/number-of-distinct-islands/discuss/426166/Java-DFS-with-Hash-Set-of-Shape-and-Path-Signature-(detailed-explanation)
	 * 
	 * It is based on path signature. Let's define something like directional id as 
	 * follows:
	 * 
	 *         1/up
	 *          |
	 * 3/left -   - 4/right
	 *          |
	 *         2/down
	 * 
	 * 0 1 1 0
	 * 0 0 1 0
	 * 0 0 1 1
	 * // Notice that the order of examining each direction does not matter, but it 
	 *    should be consistent throughout the process.
	 * // The path ID is [0, 4, 2, 2, 4]. (the first element is always assigned as 0)
	 * 
	 * 1 1 0 // Path ID: [0, 4, 2, 4]
	 * 0 1 1
	 * 0 0 0
	 * 1 1 1 // Path ID: [0, 4, 2, 4]
	 * 0 1 0
	 * 
	 * // Add path.add(0)
	 * 
	 * 1 1 0 // Path ID: [0, 4, 2, 4, 0, 0, 0]
	 * 0 1 1
	 * 0 0 0
	 * 1 1 1 // Path ID: [0, 4, 2, 0, 4, 0, 0]
	 * 0 1 0
	 * 
	 * This happens because of the order that we visit each node.
	 * The solution is to add path.add(0) to distinguish each call stack.
	 * 
	 * Last but not least, the path ID should be contained in an array or array list 
	 * since the ordering matters.
	 */
	int[][] direction_dfs6 = { { -1, 0, 1 }, { 1, 0, 2 }, { 0, -1, 3 }, { 0, 1, 4 } };

	public int numDistinctIslands_dfs6(int[][] grid) {
		if (grid == null || grid.length == 0) {
			return 0;
		}
		
		int m = grid.length;
		int n = grid[0].length;
		
		Set<List<Integer>> allPaths = new HashSet<>();
		boolean[][] marked = new boolean[m][n];
		
		// dfs
		for (int i = 0; i < m; ++i) {
			for (int j = 0; j < n; ++j) {
				if (!marked[i][j] && grid[i][j] == 1) {
					List<Integer> path = new ArrayList<>();
					dfs_dfs6(i, j, 0, grid, marked, path);
					
					// allPaths would do the checking for us
					if (path.size() > 0) {
						allPaths.add(path);
					}
				}
			}
		}
		return allPaths.size();
	}

	private void dfs_dfs6(int i, int j, int pid, int[][] grid, 
			boolean[][] marked, List<Integer> path) {
		
		int m = grid.length;
		int n = grid[0].length;
		
		marked[i][j] = true; // visit
		path.add(pid); // add pid

		// for each neighbor
		for (int[] dir : direction_dfs6) {
			int x = i + dir[0];
			int y = j + dir[1];
			int nextPid = dir[2];
			
			if (x >= 0 && x < m && y >= 0 && y < n 
					&& !marked[x][y] && grid[x][y] == 1) {
				
				dfs_dfs6(x, y, nextPid, grid, marked, path);
			}
		}
		
		path.add(0); // critical!
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/number-of-distinct-islands/discuss/108475/Java-very-Elegant-and-concise-DFS-Solution(Beats-100)/110670
	 */
	public int numDistinctIslands_dfs8(int[][] grid) {
		Set<String> set = new HashSet<>();
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				if (grid[i][j] == 1) {
					StringBuilder path = new StringBuilder();
					dfs_dfs8(grid, i, j, path);
					set.add(path.toString());
				}
			}
		}
		return set.size();
	}

	private final int[][] dirs_dfs8 = { { 1, 0, 1 }, { -1, 0, 2 }, { 0, 1, 3 }, { 0, -1, 4 } };

	private void dfs_dfs8(int[][] grid, int x, int y, StringBuilder path) {
		if (x < 0 || x >= grid.length || y < 0 || y >= grid[0].length 
				|| grid[x][y] == 0) {
			
			return;
		}
		
		grid[x][y] = 0;
		
		for (int[] dir : dirs_dfs8) {
			int nx = x + dir[0], ny = y + dir[1];
			path.append(dir[2]);
			dfs_dfs8(grid, nx, ny, path);
		}
	}

	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/number-of-distinct-islands/discuss/108474/JavaC%2B%2B-Clean-Code
	 */
	private static int[][] delta_dfs2 = { {0, 1}, {1, 0}, {0, -1}, {-1, 0} };

	public int numDistinctIslands_dfs2(int[][] grid) {
		int m = grid.length, n = grid[0].length;
		Set<List<List<Integer>>> islands = new HashSet<>();
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				List<List<Integer>> island = new ArrayList<>();
				
				if (dfs_dfs2(i, j, i, j, grid, m, n, island))
					islands.add(island);
			}
		}
		return islands.size();
	}

	private boolean dfs_dfs2(int i0, int j0, int i, int j, int[][] grid, 
			int m, int n, List<List<Integer>> island) {
		
		if (i < 0 || m <= i || j < 0 || n <= j || grid[i][j] <= 0)
			return false;
		
		island.add(Arrays.asList(i - i0, j - j0));
		grid[i][j] *= -1;
		
		for (int d = 0; d < 4; d++) {
			dfs_dfs2(i0, j0, i + delta_dfs2[d][0], j + delta_dfs2[d][1], grid, 
					m, n, island);
		}
		
		return true;
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/number-of-distinct-islands/discuss/108475/Java-very-Elegant-and-concise-DFS-Solution(Beats-100)
	 * 
	 * Other code:
	 * https://leetcode.com/problems/number-of-distinct-islands/discuss/108475/Java-very-Elegant-and-concise-DFS-Solution(Beats-100)/311183
	 */
	int[][] dirs_dfs7 = new int[][] { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 } };

	public int numDistinctIslands_dfs7(int[][] grid) {
		Set<String> set = new HashSet<>();
		int res = 0;

		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				if (grid[i][j] == 1) {
					StringBuilder sb = new StringBuilder();
					helper_dfs7(grid, i, j, 0, 0, sb);
					String s = sb.toString();
					
					if (!set.contains(s)) {
						res++;
						set.add(s);
					}
				}
			}
		}
		return res;
	}

	public void helper_dfs7(int[][] grid, int i, int j, int xpos, int ypos, 
			StringBuilder sb) {
		
		grid[i][j] = 0;
		sb.append(xpos + "" + ypos);
		
		for (int[] dir : dirs_dfs7) {
			int x = i + dir[0];
			int y = j + dir[1];
			
			if (x < 0 || y < 0 || x >= grid.length || y >= grid[0].length 
					|| grid[x][y] == 0)
				continue;
			
			helper_dfs7(grid, x, y, xpos + dir[0], ypos + dir[1], sb);
		}
	}
	
	/*
	 * Modified from this link
	 * https://leetcode.com/problems/number-of-distinct-islands/discuss/231041/C%2B%2B-BFSDSS
	 * 
	 * Each time when I see a 1, I visited all its 4 neighbors and record the two 
	 * offsets with respect to the positions of the initial 1 for all 1 neighbors in 
	 * a string.
	 */
	public int numDistinctIslands_bfs(int[][] grid) {
		int m = grid.length, n = m > 0 ? grid[0].length : 0;
		
		int[] offsets = { 0, 1, 0, -1, 0 };
		Set<String> islands = new HashSet<>();
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (grid[i][j] == 1) {
					grid[i][j] = 0;
					
					String island = "";
					Queue<int[]> todo = new LinkedList<>();
					todo.offer(new int[] { i, j });
					
					while (!todo.isEmpty()) {
						int[] p = todo.poll();
						
						for (int k = 0; k < 4; k++) {
							int r = p[0] + offsets[k], c = p[1] + offsets[k + 1];
							
							if (r >= 0 && r < m && c >= 0 && c < n 
									&& grid[r][c] == 1) {
								
								grid[r][c] = 0;
								todo.offer(new int[] { r, c });
								island += Integer.toString(r - i) 
										+ Integer.toString(c - j);
							}
						}
					}
					
					islands.add(island);
				}
			}
		}
		return islands.size();
	}
	
	/*
	 * The following 2 variables and 2 functions are from this link.
	 * https://leetcode.com/problems/number-of-distinct-islands/discuss/108475/Java-very-Elegant-and-concise-DFS-Solution(Beats-100)/186826
	 */
	public int numDistinctIslands_bfs2(int[][] grid) {
		if (grid == null || grid.length == 0 || grid[0].length == 0)
			return 0;
		
		Set<String> codeSet = new HashSet<String>();
		
		int m = grid.length;
		int n = grid[0].length;
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (grid[i][j] == 1) {
					codeSet.add(bfs_bfs2(grid, i, j));
				}
			}
		}
		return codeSet.size();
	}

	int[] rm_bfs2 = new int[] { 1, -1, 0, 0 };
	int[] cm_bfs2 = new int[] { 0, 0, 1, -1 };

	private String bfs_bfs2(int[][] grid, int i, int j) {
		StringBuilder sb = new StringBuilder();
		
		Queue<Integer> rq = new LinkedList<Integer>();
		Queue<Integer> cq = new LinkedList<Integer>();
		rq.offer(i);
		cq.offer(j);
		
		grid[i][j] = 0;
		
		while (!rq.isEmpty()) {
			int r = rq.poll();
			int c = cq.poll();
			
			for (int k = 0; k < 4; k++) {
				int nr = r + rm_bfs2[k];
				int nc = c + cm_bfs2[k];
				
				if (nr >= 0 && nc >= 0 && nr < grid.length && nc < grid[0].length 
						&& grid[nr][nc] == 1) {
					
					rq.offer(nr);
					cq.offer(nc);
					
					grid[nr][nc] = 0;
					
					sb.append("" + (nr - i) + (nc - j));
				}
			}
		}
		return sb.toString();
	}
	
	/*
	 * The following 4 variables and 4 functions are from this link.
	 * https://leetcode.com/problems/number-of-distinct-islands/solution/
	 * Approach 1: Brute Force
	 * 
	 * Since two islands are the same if one can be translated to the other, we can 
	 * translate each island so that it is as pushed as near into the top left as 
	 * possible.
	 * 
	 * 1. Use DFS to make a list of islands, where each island is a list of 
	 *    coordinates.
	 * 2. Initialize a count of the number of unique islands to 0.
	 * 3. For each island, compare it cell-by-cell to every other island. 
	 *    If it is found to be unique, increment count by 1.
	 * 4. Return the value of count.
	 * 
	 * Order doesn't matter, so the two islands [(0, 1), (0, 2)] and [(0, 2), (0, 1)] 
	 * should be considered as identical. However, we can avoid the need to worry 
	 * about order by ensuring that two islands of the same shape are initially 
	 * discovered from the same relative cell. Then from there, the DFS will always 
	 * visit the cells in the same relative order.
	 * 
	 * We can simply search for islands by iterating left to right, top to bottom. 
	 * This way, each island will always be discovered from the leftmost cell in its 
	 * top row. 
	 * 
	 * Islands of the same shape are first discovered from the same relative cell.
	 * We can simply translate each island so that the first cell of the island that 
	 * is discovered is on (0, 0). If, for example, an island contains the cells 
	 * [(2, 3), (2, 4), (2, 5), (3, 5)], we subtract (2, 3) off each cell to get 
	 * [(0, 0), (0, 1), (0, 2), (1, 2)].
	 */
	private List<List<int[]>> uniqueIslands_brute = new ArrayList<>(); // All known unique islands.
    private List<int[]> currentIsland_brute = new ArrayList<>(); // Current Island
    private int[][] grid_brute; // Input grid
    private boolean[][] seen_brute; // Cells that have been explored. 
     
    public int numDistinctIslands_brute(int[][] grid) {   
        this.grid_brute = grid;
        this.seen_brute = new boolean[grid.length][grid[0].length];
        
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                dfs_brute(row, col);
                if (currentIsland_brute.isEmpty()) {
                    continue;
                }
                
                // Translate the island we just found to the top left.
                int minCol = grid[0].length - 1;
                for (int i = 0; i < currentIsland_brute.size(); i++) {
                    minCol = Math.min(minCol, currentIsland_brute.get(i)[1]);
                }
                for (int[] cell : currentIsland_brute) {
                    cell[0] -= row;
                    cell[1] -= minCol;
                }
                
                // If this island is unique, add it to the list.
                if (currentIslandUnique_brute()) {
                    uniqueIslands_brute.add(currentIsland_brute);
                }
                
                currentIsland_brute = new ArrayList<>();
            }
        }
        return uniqueIslands_brute.size();
    }
    
	private void dfs_brute(int row, int col) {
		if (row < 0 || col < 0 || row >= grid_brute.length 
				|| col >= grid_brute[0].length) {
			
			return;
		}
		if (seen_brute[row][col] || grid_brute[row][col] == 0)
			return;
		
		seen_brute[row][col] = true;
		currentIsland_brute.add(new int[] { row, col });
		
		dfs_brute(row + 1, col);
		dfs_brute(row - 1, col);
		dfs_brute(row, col + 1);
		dfs_brute(row, col - 1);
	}
    
	private boolean currentIslandUnique_brute() {
		for (List<int[]> otherIsland : uniqueIslands_brute) {
			if (currentIsland_brute.size() != otherIsland.size())
				continue;
			
			if (equalIslands_brute(currentIsland_brute, otherIsland)) {
				return false;
			}
		}
		return true;
	}
    
    private boolean equalIslands_brute(List<int[]> island1, List<int[]> island2) {
        for (int i = 0; i < island1.size(); i++) {
            if (island1.get(i)[0] != island2.get(i)[0] 
            		|| island1.get(i)[1] != island2.get(i)[1]) {
            	
                return false;
            }
        }
        return true;
    }

	/**
     * Python collections
     * 
     * https://leetcode.com/problems/number-of-distinct-islands/discuss/108480/Simple-Python-169ms
     * https://leetcode.com/problems/number-of-distinct-islands/discuss/161867/Python-Very-Clean-and-Easy-to-Understand
     * https://leetcode.com/problems/number-of-distinct-islands/discuss/108511/Simple-Python-Code-using-BFS-and-HashSet-with-Explanation
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/number-of-distinct-islands/discuss/231041/C%2B%2B-BFSDSS
     * https://leetcode.com/problems/number-of-distinct-islands/discuss/214619/C%2B%2B.-Use-string-to-represent-shape-of-island.
     */

}
