package OJ0111_0120;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Triangle {
	/*
	 * https://discuss.leetcode.com/topic/22254/7-lines-neat-java-solution
	 * 
	 * Some of the cells only had one cell above them. 
	 * But every cell has two cells below it!
	 * 
	 * We start from the nodes on the bottom row; 
	 * the min pathsums for these nodes are the values of the nodes themselves. 
	 * From there, the min pathsum at the ith node on the kth row would be 
	 * the lesser of the pathsums of its two children plus the value of itself, i.e.:
	 * 
	 * minpath[k][i] = min( minpath[k+1][i], minpath[k+1][i+1]) + triangle[k][i];
	 * 
	 * Since the row minpath[k+1] would be useless after minpath[k] is computed, 
	 * we can simply set minpath as a 1D array, and iteratively update itself
	 * 
	 * minpath[i] = min( minpath[i], minpath[i+1]) + triangle[k][i]; (k level)
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/1669/dp-solution-for-triangle
	 * https://leetcode.com/problems/triangle/solution/
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/44676/java-solution-using-o-n-space-without-modify-triangle
	 */
	public int minimumTotal_dp_1D_bottom(List<List<Integer>> triangle) {
		int[] A = new int[triangle.size() + 1];
		for (int i = triangle.size() - 1; i >= 0; i--) {
			for (int j = 0; j < triangle.get(i).size(); j++) {
				A[j] = Math.min(A[j], A[j + 1]) + triangle.get(i).get(j);
			}
		}
		return A[0];
	}
	
	/*
	 * https://leetcode.com/problems/triangle/solution/
	 * Approach 2: Dynamic Programming (Bottom-up: Auxiliary Space)
	 * 
	 * 1. If row == 0: This is the top of the triangle: it stays the same.
	 * 2. If col == 0: There is only one cell above, located at (row - 1, col).
	 * 3. If col == row: There is only one cell above, located at (row - 1, col - 1).
	 * 4. In all other cases: There are two cells above, 
	 *    located at (row - 1, col - 1) and (row - 1, col).
	 * 
	 * As we worked our way down the rows of the triangle, we only ever needed to 
	 * look at the row immediately above. This means that we only need to maintain 
	 * the current row and the previous row.
	 */
	public int minimumTotal_from_top(List<List<Integer>> triangle) {
        List<Integer> prevRow = triangle.get(0);
        for (int row = 1; row < triangle.size(); row++) {
            List<Integer> currRow = new ArrayList<>();
            
            // 條件也可以用 col <= row
            for (int col = 0; col < triangle.get(row).size(); col++) {
                int smallestAbove = Integer.MAX_VALUE;           
                if (col > 0) {
                    smallestAbove = prevRow.get(col - 1);
                } 
                if (col < row) {
                    smallestAbove = Math.min(smallestAbove, prevRow.get(col));
                }
                
                currRow.add(smallestAbove + triangle.get(row).get(col));
            }
            
            prevRow = currRow;
        }
        return Collections.min(prevRow);
    }
	
	/*
	 * The following 2 variables and 2 functions are from this link.
	 * https://leetcode.com/problems/triangle/solution/
	 * Approach 4: Memoization (Top-Down)
	 * 
	 * We'll define a recursive helper function minPath_recursive2(row, col) that 
	 * returns the minimum path sum from the cell at (row, col), down to the base of 
	 * the triangle. The minimum path sum for the entire triangle, would, therefore, 
	 * be minPath(0, 0).
	 * 
	 * The recursive case is where there is still at least one row below the current 
	 * cell. We simply need to add the current cell to the minimum path sum of the 
	 * cells below it. That is:
	 * 
	 * return triangle[row][col] + min(minPath(row + 1, col), 
	 * minPath(row + 1, col + 1))
	 * 
	 * The base case is where there are no more rows below. In this case, we should 
	 * simply return the current cell's value:
	 * 
	 * return triangle[row][col]
	 * 
	 * To avoid re-calculating the same results over and over again, we can use a 
	 * memorization table.
	 */
	private Map<String, Integer> memoTable_recursive2;
    private List<List<Integer>> triangle_recursive2;
    
    private int minPath_recursive2(int row, int col) {
        String params = row + ":" + col;
        if (memoTable_recursive2.containsKey(params)) {
            return memoTable_recursive2.get(params);
        } 
        
        int path = triangle_recursive2.get(row).get(col);
        if (row < triangle_recursive2.size() - 1) {
            path += Math.min(minPath_recursive2(row + 1, col), 
            		minPath_recursive2(row + 1, col + 1));
        }
        
        memoTable_recursive2.put(params, path);
        return path;
    }
    
    public int minimumTotal_recursive2(List<List<Integer>> triangle) {
        this.triangle_recursive2 = triangle;
        memoTable_recursive2 = new HashMap<>();
        return minPath_recursive2(0, 0);
    }
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/30076/1-ms-java-dp-solution-beats-99-91-o-n-extra-space-without-modifying-the-triangle
	 */
	public int minimumTotal_recursive(List<List<Integer>> triangle) {
		if (triangle.size() == 0)
			return 0;
		if (triangle.size() == 1)
			return triangle.get(0).get(0);

		int[] dp = new int[triangle.size()];
		dp[0] = triangle.get(0).get(0);
		return minimumTotal_recursive(triangle, dp, 1);
	}
	public int minimumTotal_recursive(List<List<Integer>> triangle, int[] dp, int lvlidx) {
		/**
		 * dp: dp[i]_lvlidx = the min path sum up to current level and up to index i
		 * 
		 * dp[0]_lvlidx = this_level_list[0] + dp[0]_(lvlidx-1); 
		 * dp[end]_lvlidx = this_level_list[end] + dp[end-1]_(lvlidx-1);
		 * 
		 * dp[i]_lvlidx = this_level_list[i] + 
		 *   min{ dp[i-1]_(lvlidx-1), dp[i]_(lvlidx-1) };
		 */

		List<Integer> list = triangle.get(lvlidx);
		int pre = dp[0], temp;
		dp[0] += list.get(0);
		for (int i = 1; i < lvlidx; i++) {
			temp = dp[i];
			dp[i] = list.get(i) + Math.min(pre, dp[i]);
			pre = temp;
		}
		dp[lvlidx] = pre + list.get(lvlidx);

		if (lvlidx + 1 == triangle.size()) {
			int res = dp[0];
			for (int i = 1; i <= lvlidx; i++)
				res = Math.min(res, dp[i]);
			return res;
		}

		return minimumTotal_recursive(triangle, dp, lvlidx + 1);
	}
	
	/*
	 * https://discuss.leetcode.com/topic/8077/my-8-line-dp-java-code-4-meaningful-lines-with-o-1-space
	 * 
	 * Some of the cells only had one cell above them. 
	 * But every cell has two cells below it!
	 * 
	  1. Go from bottom to top.
      2. We start form the row above the bottom row [size()-2].
      3. Each number add the smaller number of two numbers that below it.
      4. And finally we get to the top we the smallest sum.
     *
     * When this algorithm has finished running, each cell, (row, col) of the input 
     * triangle will be overwritten with the minimal path sum from (row, col) to any 
     * cell on the bottom row.
     *
     * Rf :
     * https://leetcode.com/problems/triangle/solution/
	 */
	public int minimumTotal_modify_input(List<List<Integer>> triangle) {
		for (int i = triangle.size() - 2; i >= 0; i--) {			
			for (int j = 0; j <= i; j++) {				
				triangle.get(i).set(j, triangle.get(i).get(j) 
						+ Math.min(triangle.get(i + 1).get(j), 
								triangle.get(i + 1).get(j + 1)));
			}
		}
		return triangle.get(0).get(0);
	}
	
	/*
	 * https://leetcode.com/problems/triangle/solution/
	 * Approach 1: Dynamic Programming (Bottom-up: In-place)
	 * 
	 * 1. If row == 0: This is the top of the triangle: it stays the same.
	 * 2. If col == 0: There is only one cell above, located at (row - 1, col).
	 * 3. If col == row: There is only one cell above, located at (row - 1, col - 1).
	 * 4. In all other cases: There are two cells above, 
	 *    located at (row - 1, col - 1) and (row - 1, col).
	 * 
	 * Iterate through each row index between 1 and n - 1 inclusive (where n is the 
	 * number of rows in triangle):
	 *   Iterate through each row index between 1 and n - 1 inclusive (where n is the 
	 *   number of rows in triangle):
	 *     + Initialize a variable smallestAbove to positive infinity:
	 *     + If col > 0:
	 *         Set smallestAbove to triangle[row - 1][col - 1].
	 *     + If col < row:
	 *         Set smallestAbove to be the min out of itself and 
	 *         triangle[row - 1][col].
	 *     + Set triangle[row][col] to be itself plus smallestAbove. 
	 *     Return the minimum value in triangle[n - 1].
	 */
	public int minimumTotal_modify_input_from_top(List<List<Integer>> triangle) {
        for (int row = 1; row < triangle.size(); row++) {
        	// 條件也可以用 col <= row
            for (int col = 0; col < triangle.get(row).size(); col++) {
                int smallestAbove = Integer.MAX_VALUE;           
                if (col > 0) {
                    smallestAbove = triangle.get(row - 1).get(col - 1);
                } 
                if (col < row) {
                    smallestAbove = Math.min(smallestAbove, 
                    		triangle.get(row - 1).get(col));
                }
                
                int path = smallestAbove + triangle.get(row).get(col);
                triangle.get(row).set(col, path);
            }
        }
        return Collections.min(triangle.get(triangle.size() - 1));
    }
	
	// TLE
	public int minimumTotal_recursive_self(List<List<Integer>> triangle) {
        return minimumRecursive(0, 0, triangle);
    }
    private int minimumRecursive(int order, int level, List<List<Integer>> triangle) {
        if (level == triangle.size())
            return 0;
        
        return triangle.get(level).get(order) 
        		+ Math.min(minimumRecursive(order, level + 1, triangle), 
        				minimumRecursive(order + 1, level + 1, triangle)); 
    }

}
