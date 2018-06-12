package OJ0331_0340;

import definition.TreeNode;

import java.util.Map;
import java.util.HashMap;

// https://leetcode.com/problems/house-robber-iii/discuss/79330/Step-by-step-tackling-of-the-problem

public class House_Robber_III {
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/house-robber-iii/discuss/79363/Easy-understanding-solution-with-dfs
	 * 
	 * dfs all the nodes of the tree, each node return two number, int[] num. 
	 * num[0] is the max value while rob this node, we cannot rob the nodes of 
	 * root.left and root.right.
	 * 
	 * num[1] is max value while not rob this value and we are free to rob its left 
	 * and right subtrees. 
	 * Current node return value only depend on its children's value.
	 * 
	 * Rf : https://leetcode.com/problems/house-robber-iii/discuss/79330/Step-by-step-tackling-of-the-problem
	 * 
	 * Other code:
	 * https://leetcode.com/problems/house-robber-iii/discuss/79437/C++-JAVA-PYTHON-and-explanation
	 */
	public int rob_dfs(TreeNode root) {
		int[] num = dfs(root);
		return Math.max(num[0], num[1]);
	}
	private int[] dfs(TreeNode x) {
		if (x == null)
			return new int[2];
		
		int[] left = dfs(x.left);
		int[] right = dfs(x.right);
		
		int[] res = new int[2];
		res[0] = left[1] + right[1] + x.val;
		res[1] = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);
		return res;
	}
	
	/*
	 * The following variable and function are from this link.
	 * https://leetcode.com/problems/house-robber-iii/discuss/79448/Java-concise-memoization-DP-solution:-7ms
	 * 
	 * Max(root + grand children, children) 
	 * 
	 * Rf : https://leetcode.com/problems/house-robber-iii/discuss/79504/Java-bottom-up-and-top-down-solutions-using-DP
	 * 
	 * Other code :
	 * https://leetcode.com/problems/house-robber-iii/discuss/79359/My-simple-Java-recursive-solution
	 */
	private Map<TreeNode, Integer> memo = new HashMap<>();
	public int rob_top_down(TreeNode root) {
		if (root == null)
			return 0;
		if (root.left == null && root.right == null)
			return root.val;
		if (memo.containsKey(root))
			return memo.get(root);

		// case 1
		int sum1 = root.val;
		if (root.left != null) {
			sum1 += rob_top_down(root.left.left) + rob_top_down(root.left.right);
		}
		if (root.right != null) {
			sum1 += rob_top_down(root.right.left) + rob_top_down(root.right.right);
		}

		// case 2
		int sum2 = rob_top_down(root.left) + rob_top_down(root.right);

		int sum = Math.max(sum1, sum2);
		memo.put(root, sum);
		return sum;
	}
	
	/*
	 * The following 3 functions are from this link.
	 * https://leetcode.com/problems/house-robber-iii/discuss/79344/Easy-to-understand(java)/144936
	 */
	public int rob_include_exclude(TreeNode root) {
		if (root == null)
			return 0;
		return Math.max(robInclude(root), robExclude(root));
	}

	public int robInclude(TreeNode node) {
		if (node == null)
			return 0;
		return robExclude(node.left) + robExclude(node.right) + node.val;
	}

	public int robExclude(TreeNode node) {
		if (node == null)
			return 0;
		return rob_include_exclude(node.left) + rob_include_exclude(node.right);
	}

	// https://leetcode.com/problems/house-robber-iii/discuss/79476/2ms-Java-AC-O(n)-solution

}
