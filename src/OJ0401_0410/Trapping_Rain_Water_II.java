package OJ0401_0410;

import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.TreeSet;

public class Trapping_Rain_Water_II {
	/*
	 * The following class and function are from this link.
	 * https://leetcode.com/problems/trapping-rain-water-ii/discuss/89461/Java-solution-using-PriorityQueue
	 * https://github.com/awangdev/LintCode/blob/master/Java/Trapping%20Rain%20Water%20II.java
	 * 
	 * Min Heap：用 PriorityQueue 把選中的 height 排序，為走位，創建 class Cell (x,y, height).
	 * 
	 * 注意幾個理論
	 * - 1. 從 matrix 四周開始考慮，發現 matrix 能 Hold 住的水，取決於 height 低的 block
	 * - 2. 必須從外圍開始考慮，因為水是被包裹在裡面，外面至少需要現有一層
	 * - 以上兩點就促使我們用 min-heap: 也就是 natural order 的 PriorityQueue<Cell>.
	 * 
	 * Steps
	 * - 1. process 的時候，畫圖可以搞清楚: 就是四個方向都走走，用 curr cell 的高度減去周圍 cell 的高度.
	 * - 2. 若大於零，那麼周圍的 cell 就有積水: 因為 curr cell 已經是外圍最低, 所以內部更低的, 一定有積水.
	 * - 3. 每個 visited 的 cell 都要 mark, avoid revisit
	 * - 4. 根據 4 個方向的走位 (mX, mY) 創建新 cell 加進 queue 裡面: cell(mX, mY) 已經計算過積水後, 
	 *      外圍牆小時, (mX, mY) 就會變成牆.
	 * - 5. 因為做的是縮小一圈的新圍牆, height = Math.max(cell.h, neighbor.h);
	 * - 和 trapping water I 想法一樣。剛剛從外圍，只是能加到跟外圍 cell 高度一致的水平面。往裡面，很可能 
	 *   cell 高度變化。
	 * - 這裡要附上 curr cell 和  move-to cell 的最大高度。
	 * 
	 * 為什麼想到用 Heap (min-heap - priorityQueue)
	 * - 要找到 bucket 的最短板
	 * - 每次需要最先處理最短的那條 (on top)
	 * 
	 * 為什麼從外向裡遍歷
	 * - 木桶理論, 包水, 是從外面包住裡面
	 * - 洋蔥剝皮, 用完丟掉 => BFS
	 * 
	 * [[0, 0, 3, 0, 0], [0, 0, 2, 0, 0], [3, 2, 1, 2, 3], [0, 0, 2, 0, 0], [0, 0, 3, 0, 0]]
	 * 
	 * From the borders, pick the shortest cell visited and check its neighbors:
	 * 1. if the neighbor is shorter, collect the water it can trap and update its 
	 *    height as its height plus the water trapped
	 * 2. add all its neighbors to the queue.
	 * 
	 * The amount of water that can be stored above a grid element depends on its 
	 * neighboring rows and columns as well.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/trapping-rain-water-ii/discuss/89461/Java-solution-using-PriorityQueue/94107
	 * https://leetcode.com/problems/trapping-rain-water-ii/discuss/89461/Java-solution-using-PriorityQueue/239260
	 * https://leetcode.com/problems/trapping-rain-water-ii/discuss/89461/Java-solution-using-PriorityQueue/94113
	 * https://leetcode.com/problems/trapping-rain-water-ii/discuss/89461/Java-solution-using-PriorityQueue/190445
	 * https://leetcode.com/problems/trapping-rain-water-ii/discuss/89461/Java-solution-using-PriorityQueue/94101
	 * https://leetcode.com/problems/trapping-rain-water-ii/discuss/89461/Java-solution-using-PriorityQueue/202680
	 * https://leetcode.com/problems/trapping-rain-water-ii/discuss/89461/Java-solution-using-PriorityQueue/533036
	 * https://leetcode.com/problems/trapping-rain-water-ii/discuss/89461/Java-solution-using-PriorityQueue/94124
	 * https://leetcode.com/problems/trapping-rain-water-ii/discuss/89461/Java-solution-using-PriorityQueue/303122
	 * https://leetcode.com/problems/trapping-rain-water-ii/discuss/89461/Java-solution-using-PriorityQueue/175490
	 * https://leetcode.com/problems/trapping-rain-water-ii/discuss/89472/Visualization-No-Code
	 * https://leetcode.com/problems/trapping-rain-water-ii/discuss/409507/No-code.-Explanation-of-priority-queue-solution
	 * 
	 * Other code:
	 * https://leetcode.com/problems/trapping-rain-water-ii/discuss/276249/Java-AC-Solution-using-PriorityQueue-with-Detailed-explanations
	 * https://leetcode.com/problems/trapping-rain-water-ii/discuss/302435/Java-PriorityQueue-Solution
	 * https://leetcode.com/problems/trapping-rain-water-ii/discuss/521596/Java-Clean-Priority-Queue-solution-with-visualization
	 */
	public class Cell {
		int row;
		int col;
		int height;

		public Cell(int row, int col, int height) {
			this.row = row;
			this.col = col;
			this.height = height;
		}
	}

	public int trapRainWater_BFS(int[][] heights) {
		if (heights == null || heights.length == 0 || heights[0].length == 0)
			return 0;

		PriorityQueue<Cell> queue = new PriorityQueue<>(new Comparator<Cell>() {
			public int compare(Cell a, Cell b) {
				return a.height - b.height;
			}
		});

		int m = heights.length;
		int n = heights[0].length;
		boolean[][] visited = new boolean[m][n];

		// Initially, add all the Cells which are on borders to the queue. 
		// LEFT/RIGHT
		for (int i = 0; i < m; i++) {
			visited[i][0] = true;
			visited[i][n - 1] = true;
			queue.offer(new Cell(i, 0, heights[i][0]));
			queue.offer(new Cell(i, n - 1, heights[i][n - 1]));
		}

		// TOP/BOTTOM
		// for (int i = 1; i < n-1; i++)
		for (int i = 0; i < n; i++) {
			visited[0][i] = true;
			visited[m - 1][i] = true;
			queue.offer(new Cell(0, i, heights[0][i]));
			queue.offer(new Cell(m - 1, i, heights[m - 1][i]));
		}

		// from the borders, pick the shortest cell visited and check its neighbors:
		// if the neighbor is shorter, collect the water it can trap and update its
		// height as its height plus the water trapped
		// add all its neighbors to the queue.
		int[][] dirs = new int[][] { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
		int res = 0;
		while (!queue.isEmpty()) {
			Cell cell = queue.poll();
			
			for (int[] dir : dirs) {
				int row = cell.row + dir[0];
				int col = cell.col + dir[1];
				
				if (row >= 0 && row < m && col >= 0 && col < n 
						&& !visited[row][col]) {
					
					visited[row][col] = true;
					
					// cell is lowest, so any lower should contain water
					res += Math.max(0, cell.height - heights[row][col]);
					
					queue.offer(new Cell(row, col, 
							Math.max(cell.height, heights[row][col])));
				}
			}
		}

		return res;
	}
	
	/*
	 * https://leetcode.com/problems/trapping-rain-water-ii/discuss/89495/How-to-get-the-solution-to-2-D-%22Trapping-Rain-Water%22-problem-from-1-D-case
	 * 
	 * The initial boundary will be the four edges of the matrix, which forms a 
	 * rectangle.
	 * 
	 * Second, finding the minimum of the boundary. PriorityQueue can help find the 
	 * minimum in log(queue_size) time.
	 * 
	 * Lastly, how do we replace the boundaries? We have 2-D elevation map now so each 
	 * cell can have at most four neighbors. Also there are possibilities that one 
	 * cell is neighbor of multiple cells on the boundary so we need to keep track of 
	 * which cell has been visited to avoid repetition.
	 * 
	 * 四面都是圍牆，從最低的往裡走；
	 * 如果裡面有更低的，當然就可以蓄水，蓄水的量就是圍牆最低 減去 此處的高度；
	 * 如果裡面的比當前圍牆高，那這個方向的圍牆高度就增加了。
	 * 然後永遠圍牆最低的地方開始搜，最後就能把整個水池搜一遍。
	 * 
	 * Rf :
	 * https://leetcode.com/problems/trapping-rain-water-ii/discuss/89495/How-to-get-the-solution-to-2-D-"Trapping-Rain-Water"-problem-from-1-D-case/380142
	 * https://leetcode.com/problems/trapping-rain-water-ii/discuss/89495/How-to-get-the-solution-to-2-D-"Trapping-Rain-Water"-problem-from-1-D-case/386092
	 */
	public int trapRainWater_BFS2(int[][] heightMap) {
		int[][] dirs = new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

		int m = heightMap.length;
		int n = (m == 0 ? 0 : heightMap[0].length);
		int res = 0;

		// row number, column number and the height
		PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[2] - b[2]);
		boolean[][] visited = new boolean[m][n];

		// build the initial boundary - outmost row
		for (int i = 0; i < m; i++) {
			pq.offer(new int[] { i, 0, heightMap[i][0] });
			pq.offer(new int[] { i, n - 1, heightMap[i][n - 1] });
			visited[i][0] = visited[i][n - 1] = true;
		}

		// build the initial boundary - outmost column
		for (int j = 1; j < n - 1; j++) {
			pq.offer(new int[] { 0, j, heightMap[0][j] });
			pq.offer(new int[] { m - 1, j, heightMap[m - 1][j] });
			visited[0][j] = visited[m - 1][j] = true;
		}

		while (!pq.isEmpty()) {
			int[] cell = pq.poll();

			for (int[] d : dirs) {
				int i = cell[0] + d[0], j = cell[1] + d[1];
				if (i < 0 || i >= m || j < 0 || j >= n || visited[i][j])
					continue;
				
				res += Math.max(0, cell[2] - heightMap[i][j]);
				pq.offer(new int[] { i, j, Math.max(heightMap[i][j], cell[2]) });
				visited[i][j] = true;
			}
		}

		return res;
	}
	
	/*
	 * The following class, 2 variables and 3 functions are from this link.
	 * https://leetcode.com/problems/trapping-rain-water-ii/discuss/89477/Java-solution-beating-100
	 * 
	 * Still based on PriorityQueue(heap), but it is combined with flood fill.
	 * Maintain a heap which contains the current walls, the boundary of the water 
	 * pool.
	 * Every time we pick the lowest wall as a bar, then recursively travel from this 
	 * wall to its neighbors to find if they can trap the water.
	 * 
	 * If we meet a position which is lower than the bar, we trap some water, else we 
	 * meet a new wall and put it into the heap.
	 * We can use a 2-D array to memorize the visited position and a member variable 
	 * to records the water we have trapped.
	 * 
	 * Other code:
	 * https://leetcode.com/problems/trapping-rain-water-ii/discuss/89461/Java-solution-using-PriorityQueue/94120
	 */
	private class Cell_dfs implements Comparable<Cell_dfs> {
		private int row;
		private int col;
		private int value;

		public Cell_dfs(int r, int c, int v) {
			this.row = r;
			this.col = c;
			this.value = v;
		}

		@Override
		public int compareTo(Cell_dfs other) {
			return value - other.value;
		}
	}

	private int water_dfs;
	private boolean[][] visited1_dfs;

	public int trapRainWater_dfs(int[][] heightMap) {
		if (heightMap.length == 0)
			return 0;
		
		int rows = heightMap.length, cols = heightMap[0].length;
		water_dfs = 0;
		visited1_dfs = new boolean[rows][cols];
		PriorityQueue<Cell_dfs> walls = new PriorityQueue<Cell_dfs>();
		
		// build wall;
		for (int c = 0; c < cols; c++) {
			walls.add(new Cell_dfs(0, c, heightMap[0][c]));
			walls.add(new Cell_dfs(rows - 1, c, heightMap[rows - 1][c]));
			visited1_dfs[0][c] = true;
			visited1_dfs[rows - 1][c] = true;
		}
		for (int r = 1; r < rows - 1; r++) {
			walls.add(new Cell_dfs(r, 0, heightMap[r][0]));
			walls.add(new Cell_dfs(r, cols - 1, heightMap[r][cols - 1]));
			visited1_dfs[r][0] = true;
			visited1_dfs[r][cols - 1] = true;
		}
		
		// end build wall;
		while (!walls.isEmpty()) {
			Cell_dfs min = walls.poll();
			visit_dfs(heightMap, min, walls);
		}
		return water_dfs;
	}

	private void visit_dfs(int[][] height, Cell_dfs start, 
			PriorityQueue<Cell_dfs> walls) {
		
		fill_dfs(height, start.row + 1, start.col, walls, start.value);
		fill_dfs(height, start.row - 1, start.col, walls, start.value);
		fill_dfs(height, start.row, start.col + 1, walls, start.value);
		fill_dfs(height, start.row, start.col - 1, walls, start.value);
	}

	private void fill_dfs(int[][] height, int row, int col, 
			PriorityQueue<Cell_dfs> walls, int min) {
		
		if (row < 0 || col < 0)
			return;
		if (row >= height.length || col >= height[0].length)
			return;
		if (visited1_dfs[row][col])
			return;
		
		if (height[row][col] >= min) {
			walls.add(new Cell_dfs(row, col, height[row][col]));
			visited1_dfs[row][col] = true;
			return;
		}
		
		water_dfs += min - height[row][col];
		visited1_dfs[row][col] = true;
		
		fill_dfs(height, row + 1, col, walls, min);
		fill_dfs(height, row - 1, col, walls, min);
		fill_dfs(height, row, col + 1, walls, min);
		fill_dfs(height, row, col - 1, walls, min);
	}
	
	/*
	 * The following 4 variables and 2 functions are from this link.
	 * https://leetcode.com/problems/trapping-rain-water-ii/discuss/89460/Alternative-approach-using-Dijkstra-in-O(rc-max(log-r-log-c))-time
	 * 
	 * This problem can also be solved in a more general approach way using Dijkstra.
	 * 
	 * Construct a graph G = (V, E) as follows:
	 * V = all cells plus a dummy vertex, v, corresponding to the outside region.
	 * 
	 * If cell(i, j) is adjacent to cell(i', j'), then add an direct edge from (i, j) 
	 * to (i', j') with weight height(i', j').
	 * Add an edge with zero weight from any boundary cell to the dummy vertex v.
	 * 
	 * The weight of a path is defined as the weight of the heaviest edge along it. 
	 * Then, for any cell (i, j), the height of water it can save is equal to the 
	 * weight, denoted by dist(i, j), of the shortest path from (i, j) to v. (If the 
	 * weight is less than or equal to height(i, j), no water can be accumulated at 
	 * that particular position.)
	 * 
	 * We want to compute the dist(i, j) for all pairs of (i, j). Here, we have 
	 * multiple sources and one destination, but this problem essentially can be 
	 * solved using one pass of Dijkstra algorithm if we reverse the directions of 
	 * all edges. The graph is sparse, i.e., there are O(rc) edges, resulting an 
	 * O(rc log(rc)) = O(rc max(log r, log c)) runtime and using O(rc) space.
	 * 
	 * For each single cell, we need to know that for all the possible paths to 
	 * outside world (where the water will escape to), what is the minimum of all 
	 * path's weight, and the path's weight should be defined as the highest height 
	 * value along the path.
	 * 
	 * The heap solution only adds the boundary into heap, and always picks the 
	 * smallest one, and sums the water along the process. But the dijkstra solution 
	 * adds every node into "priorityqueue" first, and calculate everyone's "distance" 
	 * (which is essentially the same metric with heap solution, the highest place in 
	 * the path), then in the end, add the water together.
	 * 
	 * Java PriorityQueue does not have update operation, so we use TreeSet here.
	 * 
	 * Even though dijkstra algorithm adds every node into heap, but the internal 
	 * nodes' distance are always +\infinity, so it might not be necessary to add them 
	 * into heap, instead, we only need to add the boundary into heap. It's just like 
	 * truncate the +\infinity out of the heap.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/trapping-rain-water-ii/discuss/89460/Alternative-approach-using-Dijkstra-in-O(rc-max(log-r-log-c))-time/94097
	 * https://leetcode.com/problems/trapping-rain-water-ii/discuss/89460/Alternative-approach-using-Dijkstra-in-O(rc-max(log-r-log-c))-time/94093
	 * https://leetcode.com/problems/trapping-rain-water-ii/discuss/89460/Alternative-approach-using-Dijkstra-in-O(rc-max(log-r-log-c))-time/94091
	 */
	int[] dx_dijkstra = { 0, 0, 1, -1 };
	int[] dy_dijkstra = { 1, -1, 0, 0 };

	List<List<int[]>> g_dijkstra;
	int start_dijkstra;

	private int[] dijkstra() {
		int[] dist = new int[g_dijkstra.size()];
		
		Arrays.fill(dist, Integer.MAX_VALUE / 2);
		dist[start_dijkstra] = 0;
		
		// When heights are equal, use position to sort (just for a fix order)
		// Also work
		//
		// TreeSet<int[]> tree = 
		// new TreeSet<>((u, v) -> u[1] == v[1] ? v[0] - u[0] : u[1] - v[1]);
		TreeSet<int[]> tree = new TreeSet<>((u, v) -> u[1] == v[1] ? 
				u[0] - v[0] : u[1] - v[1]);
		
		tree.add(new int[] { start_dijkstra, 0 });
		
		while (!tree.isEmpty()) {
			int u = tree.first()[0], d = tree.pollFirst()[1];
			for (int[] e : g_dijkstra.get(u)) {
				int v = e[0], w = e[1];
				
				if (Math.max(d, w) < dist[v]) {
					tree.remove(new int[] { v, dist[v] });
					dist[v] = Math.max(d, w);
					tree.add(new int[] { v, dist[v] });
				}
			}
		}
		return dist;
	}

	public int trapRainWater_dijkstra(int[][] a) {
		if (a == null || a.length == 0 || a[0].length == 0)
			return 0;
		
		int r = a.length, c = a[0].length;

		start_dijkstra = r * c;
		
		g_dijkstra = new ArrayList<>();
		for (int i = 0; i < r * c + 1; i++)
			g_dijkstra.add(new ArrayList<>());
		
		for (int i = 0; i < r; i++)
			for (int j = 0; j < c; j++) {
				if (i == 0 || i == r - 1 || j == 0 || j == c - 1)
					g_dijkstra.get(start_dijkstra).add(new int[] { i * c + j, 0 });
				
				for (int k = 0; k < 4; k++) {
					int x = i + dx_dijkstra[k], y = j + dy_dijkstra[k];
					if (x >= 0 && x < r && y >= 0 && y < c)
						g_dijkstra.get(i * c + j)
						          .add(new int[] { x * c + y, a[i][j] });
				}
			}

		int ans = 0;
		int[] dist = dijkstra();
		
		for (int i = 0; i < r; i++)
			for (int j = 0; j < c; j++) {
				int cb = dist[i * c + j];
				if (cb > a[i][j])
					ans += cb - a[i][j];
			}

		return ans;
	}
	
	/*
	 * https://leetcode.com/problems/trapping-rain-water-ii/discuss/89467/Why-reinvent-the-wheel-An-easy-understood-commented-solution-based-on-trapping-rain-1/94134
	 * 
	 * For every spot that gets wet, the water can possibly spill.
	 * We need to check the water height against it's 4 neighbors.
	 * 
	 * If the water height is taller than any one of its 4 neighbors, we need to 
	 * spill the extra water.
	 * If we spill any water from any slot, then its 4 neighbors needs to check 
	 * themselves again. For example, if we spill some water in the current slot b/c 
	 * its bottom neighbor's height, current slot's top neighbor's height might need 
	 * to be updated again.
	 * we keep checking until there is no water to be spilled.
	 * 
	 * We don't have to use the solution from trap rain 1. The spill water is 
	 * already good enough by itself. Instead, we can add an arbitrarily big number to 
	 * all the inner slots, and let the water spill. (This big number shouldn't be 
	 * too big to cause us integer overflow issue)
	 * 
	 * O((rc)^2)
	 * 
	 * Rf :
	 * https://leetcode.com/problems/trapping-rain-water-ii/discuss/89467/Why-reinvent-the-wheel-An-easy-understood-commented-solution-based-on-trapping-rain-1/94136
	 * https://leetcode.com/problems/trapping-rain-water-ii/discuss/89467/Why-reinvent-the-wheel-An-easy-understood-commented-solution-based-on-trapping-rain-1/94138
	 */
	public int trapRainWater_spill(int[][] heightMap) {
		if (heightMap.length == 0)
			return 0;
		
		int[][] wetMap = new int[heightMap.length][heightMap[0].length];
		int sum = 0;

		/*
		 * Step 1, Make it rain. We add a big number to simulate that we had a heavy
		 * rain
		 */
		for (int i = 1; i < wetMap.length - 1; i++) {
			for (int j = 1; j < wetMap[i].length - 1; j++) {
				wetMap[i][j] = 200000;
				sum += wetMap[i][j];
			}
		}
		
		/* step 2, spill the extra water */
		boolean spillWater = true;
		int[] rowOffset = { -1, 1, 0, 0 };
		int[] colOffset = { 0, 0, 1, -1 };
		
		while (spillWater) {
			spillWater = false;
			for (int i = 1; i < heightMap.length - 1; i++) {
				for (int j = 1; j < heightMap[0].length - 1; j++) {
					// If this slot has ever gotten wet, examine its 4 neighbors
					if (wetMap[i][j] != 0) {
						for (int m = 0; m < 4; m++) {
							int neighborRow = i + rowOffset[m];
							int neighborCol = j + colOffset[m];
							int currentHeight = wetMap[i][j] + heightMap[i][j];
							int neighborHeight = wetMap[neighborRow][neighborCol] 
									+ heightMap[neighborRow][neighborCol];
							
							if (currentHeight > neighborHeight) {
								int spilledWater = currentHeight 
										- Math.max(neighborHeight, heightMap[i][j]);
								
								wetMap[i][j] -= spilledWater;
								sum -= spilledWater;
								spillWater = true;
							}
						}
					}
				}
			}
		}
		return sum;
	}

	/**
     * Python collections
     * 
     * https://leetcode.com/problems/trapping-rain-water-ii/discuss/89466/python-solution-with-heap
     * https://leetcode.com/problems/trapping-rain-water-ii/discuss/421873/Python-Clear-Solution
     * https://leetcode.com/problems/trapping-rain-water-ii/discuss/507302/Python-PriorityQueue-Solution
     * https://leetcode.com/problems/trapping-rain-water-ii/discuss/185437/python3-BFS-%2B-Heap-1D-case-and-2D-case
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/trapping-rain-water-ii/discuss/515264/C%2B%2B-or-88.70-faster-or-Using-priority-queue
     * https://leetcode.com/problems/trapping-rain-water-ii/discuss/89496/Concise-C%2B%2B-method-with-explanation
     * https://leetcode.com/problems/trapping-rain-water-ii/discuss/89464/C%2B%2B-No-Priority-Queue-Just-Naive-BFS-Solution.-Beats-83
     * https://leetcode.com/problems/trapping-rain-water-ii/discuss/288891/C%2B%2B-8ms-solution%3A-min-heap-%2B-flood-fill-for-lakes
     */
	
	/**
	 * C# collections
	 * 
	 * https://leetcode.com/problems/trapping-rain-water-ii/discuss/89471/Just-a-c-solution.
	 */
	
	/**
	 * JavaScript collections
	 * 
	 * https://leetcode.com/problems/trapping-rain-water-ii/discuss/89469/JavaScript-Solution-Summary
	 */

}
