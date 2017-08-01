package OJ0111_0120;

import java.util.List;

public class Triangle {
	/*
	 * https://discuss.leetcode.com/topic/22254/7-lines-neat-java-solution
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
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/44676/java-solution-using-o-n-space-without-modify-triangle
	 */
	public int minimumTotal(List<List<Integer>> triangle) {
		int[] A = new int[triangle.size() + 1];
		for (int i = triangle.size() - 1; i >= 0; i--) {
			for (int j = 0; j < triangle.get(i).size(); j++) {
				A[j] = Math.min(A[j], A[j + 1]) + triangle.get(i).get(j);
			}
		}
		return A[0];
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
	  1. Go from bottom to top.
      2. We start form the row above the bottom row [size()-2].
      3. Each number add the smaller number of two numbers that below it.
      4. And finally we get to the top we the smallest sum.
     *
	 */
	public int minimumTotal_modify_input(List<List<Integer>> triangle) {
		for (int i = triangle.size() - 2; i >= 0; i--)
			for (int j = 0; j <= i; j++)
				triangle.get(i).set(j,
						triangle.get(i).get(j) + Math.min(triangle.get(i + 1).get(j), triangle.get(i + 1).get(j + 1)));
		return triangle.get(0).get(0);
	}
	
	// TLE
	public int minimumTotal_recursive_self(List<List<Integer>> triangle) {
        return minimumRecursive(0, 0, triangle);
    }
    private int minimumRecursive(int order, int level, List<List<Integer>> triangle) {
        if (level == triangle.size())
            return 0;
        return triangle.get(level).get(order) + Math.min(minimumRecursive(order, level + 1, triangle), minimumRecursive(order + 1, level + 1, triangle)); 
    }

}
