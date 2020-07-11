package OJ0461_0470;

public class Island_Perimeter {
	/*
	 * https://leetcode.com/problems/island-perimeter/discuss/95001/clear-and-easy-java-solution/117773
	 * 
	 * Counting the number of edges that adjacent to the water
	 * 
	 * Rf :
	 * https://leetcode.com/problems/island-perimeter/discuss/95001/clear-and-easy-java-solution/99445
	 * 
	 * Other code:
	 * https://leetcode.com/problems/island-perimeter/discuss/95122/Clean-java-solution-O(mn)
	 * https://leetcode.com/problems/island-perimeter/discuss/608148/java%3A-99.8-time-efficient-and-100-memory-efficient
	 */
	public int islandPerimeter_BFS(int[][] grid) {
		int rows = grid.length;
		int cols = grid[0].length;
		int num = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (grid[i][j] == 1) {
					
					// UP
					if (i == 0 || grid[i - 1][j] == 0)
						num++;
					
					// LEFT
					if (j == 0 || grid[i][j - 1] == 0)
						num++;
					
					// DOWN
					if (i == rows - 1 || grid[i + 1][j] == 0)
						num++;
					
					// RIGHT
					if (j == cols - 1 || grid[i][j + 1] == 0)
						num++;
				}
			}
		}
		return num;
	}
	
	/*
	 * https://leetcode.com/problems/island-perimeter/discuss/95001/clear-and-easy-java-solution
	 * 
	 * 1. loop over the matrix and count the number of islands;
	 * 2. if the current dot is an island, count if it has any right neighbor or 
	 *    down neighbor;
	 * 3. the result is islands * 4 - neighbors * 2, since every adjacent islands 
	 *    made two sides disappeared
	 * 
	 * +--+     +--+                    +--+--+
	 * |  |  +  |  |           ->       |     |
	 * +--+     +--+                    +--+--+
	 *  4    +   4    - ? = 6  -> ? = 2
	 * 
	 * Only check right and down, because left and up will get checked by a previous 
	 * element. (prevents checking a side twice)
	 * A neighbor subtracts a side from the perimeter. But since only count right and 
	 * down, we have to double count. thus -2*neighbors.
	 * 
	 * Checking neighbor: The side is shared by two islands and so we have to 
	 * multiply the neighbors by two.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/island-perimeter/discuss/95001/clear-and-easy-java-solution/99459
	 * https://leetcode.com/problems/island-perimeter/discuss/95001/clear-and-easy-java-solution/225675
	 * https://leetcode.com/problems/island-perimeter/discuss/95001/clear-and-easy-java-solution/99457
	 * 
	 * Other code:
	 * https://leetcode.com/problems/island-perimeter/discuss/94992/Java-9-line-solution-add-4-for-each-land-and-remove-2-for-each-internal-edge
	 */
	public int islandPerimeter_BFS_calculate(int[][] grid) {
		int islands = 0, neighbours = 0;

		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if (grid[i][j] == 1) {
					// count islands
					islands++;
					
					// count down neighbors
					if (i < grid.length - 1 && grid[i + 1][j] == 1)
						neighbours++;
					
					// count right neighbors
					if (j < grid[i].length - 1 && grid[i][j + 1] == 1)
						neighbours++;
				}
			}
		}

		return islands * 4 - neighbours * 2;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/island-perimeter/discuss/95004/Java-solution-with-DFS
	 * 
	 * He is counting the grids who surround the island. When DFS to grid[i][j] who is 
	 * out of bound, count++; DFS to grid[i][j] == 1, it will be set to -1 and never 
	 * considered in the future; DFS to grid[i][j] == 0, count++ (solved the problem 
	 * that grid[i][j] == 0 should be counted two times in the corner). So rather than 
	 * count the '1', this algorithm is counting '0's around the island.
	 * 
	 * One dfs call is just one direction updated, so count will just update +1 most, 
	 * which is the boundary and next to water (two cases).
	 * 
	 * Rf :
	 * https://leetcode.com/problems/island-perimeter/discuss/95004/Java-solution-with-DFS/148786
	 * https://leetcode.com/problems/island-perimeter/discuss/95004/Java-solution-with-DFS/150990
	 * https://leetcode.com/problems/island-perimeter/discuss/95004/Java-solution-with-DFS/512033
	 * https://leetcode.com/problems/island-perimeter/discuss/95004/Java-solution-with-DFS/265652
	 */
	public int islandPerimeter_DFS(int[][] grid) {
		if (grid == null)
			return 0;
		
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				if (grid[i][j] == 1) {
					return getPerimeter_DFS(grid, i, j);
				}
			}
		}
		return 0;
	}

	public int getPerimeter_DFS(int[][] grid, int i, int j) {
		// reached boundary
		if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length) {
			return 1;
		}
		// reached water
		if (grid[i][j] == 0) {
			return 1;
		}
		// visited point
		if (grid[i][j] == -1)
			return 0;

		int count = 0;
		grid[i][j] = -1;

		count += getPerimeter_DFS(grid, i - 1, j);
		count += getPerimeter_DFS(grid, i, j - 1);
		count += getPerimeter_DFS(grid, i, j + 1);
		count += getPerimeter_DFS(grid, i + 1, j);

		return count;
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/island-perimeter/discuss/95003/Easy-to-read-Python-solution
     * https://leetcode.com/problems/island-perimeter/discuss/723842/Python-O(mn)-simple-loop-solution-explained
     * https://leetcode.com/problems/island-perimeter/discuss/144321/Python-counting-solution
     * https://leetcode.com/problems/island-perimeter/discuss/95007/Short-Python
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/island-perimeter/discuss/95126/C%2B%2B-solution-with-explanation
     * https://leetcode.com/problems/island-perimeter/discuss/624178/cpp-very-easy-solution-with-explaination-No-dfs
     * https://leetcode.com/problems/island-perimeter/discuss/95273/Easy-DFS-solution-%2B-explaination-without-visited-array
     */

}
