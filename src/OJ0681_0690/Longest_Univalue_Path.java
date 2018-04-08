package OJ0681_0690;

import definition.TreeNode;

public class Longest_Univalue_Path {
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/longest-univalue-path/discuss/108136/JavaC++-Clean-Code
	 * 
	 * 1. Longest-Univalue-Path of a tree is among those Longest-Univalue-Path-Across 
	 *    at each node;
	 * 2. Longest-Univalue-Path-Across a node is sum of 
	 *    { Longest-Univalue-Path-Start-At each child with same value } + 1
	 * 3. Let an dfs to return Longest-Univalue-Path-Start-At each node, 
	 *    the Longest-Univalue-Path-Across node can be calculated by combine the 
	 *    Longest-Univalue-Path-Start-At of its 2 child; and we can use an global 
	 *    variable res to hold the max value and compare with each intermediate result.
	 */
	public int longestUnivaluePath(TreeNode root) {
		int[] res = new int[1];
		if (root != null)
			dfs(root, res);
		return res[0];
	}
	private int dfs(TreeNode node, int[] res) {
		int l = node.left != null ? dfs(node.left, res) : 0; // Longest-Univalue-Path-Start-At - left child
		int r = node.right != null ? dfs(node.right, res) : 0; // Longest-Univalue-Path-Start-At - right child
		
		// single direction Longest-Univalue-Path- Start from parent, and go left
		int resl = node.left != null && node.left.val == node.val ? l + 1 : 0;
		// single direction Longest-Univalue-Path- Start from parent, and go right
		int resr = node.right != null && node.right.val == node.val ? r + 1 : 0;
		
		res[0] = Math.max(res[0], resl + resr); // Longest-Univalue-Path-Across - node
		return Math.max(resl, resr);
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/longest-univalue-path/discuss/108154/5-lines-Java-Solution-Easy-to-Understand
	 * 
	 * Other code:
	 * https://leetcode.com/problems/longest-univalue-path/discuss/108177/java-easy-understand-solution
	 */
	public int longestUnivaluePath_DFS(TreeNode root) {
		if (root == null)
			return 0;
		int sub = Math.max(longestUnivaluePath_DFS(root.left), 
				longestUnivaluePath_DFS(root.right));
		return Math.max(sub, 
				helper_DFS(root.left, root.val) + helper_DFS(root.right, root.val));
	}
	private int helper_DFS(TreeNode node, int val) {
		if (node == null || node.val != val)
			return 0;
		return 1 + Math.max(helper_DFS(node.left, val), helper_DFS(node.right, val));
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/longest-univalue-path/discuss/108175/java-solution-with-global-variable
	 */
	int len = 0; // global variable
	public int longestUnivaluePath2(TreeNode root) {
		if (root == null)
			return 0;
		len = 0;
		getLen(root, root.val);
		return len;
	}
	private int getLen(TreeNode node, int val) {
		if (node == null)
			return 0;
		int left = getLen(node.left, node.val);
		int right = getLen(node.right, node.val);
		len = Math.max(len, left + right);
		if (val == node.val)
			return Math.max(left, right) + 1;
		return 0;
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/longest-univalue-path/discuss/119811/short-and-clean-java-solution-and-faster
	 */
	int max = 0;
	public int longestUnivaluePath_3(TreeNode root) {
		if (root == null)
			return max;
		
		int res = pathFrom(root.left, root.val) + pathFrom(root.right, root.val);
		max = Math.max(max, res);
		return max;
	}

	private int pathFrom(TreeNode root, int val) {// num of nodes with same val
		if (root == null)
			return 0;
		
		// go to child only when root.val != val, which means the current path is end,
		// which will avoid some duplicate calculate
		if (root.val != val) {
			longestUnivaluePath_3(root);
			return 0;
		}
		
		// only pick the child with longer path
		return 1 + Math.max(pathFrom(root.left, val), pathFrom(root.right, val));
	}
	
	// https://leetcode.com/problems/longest-univalue-path/discuss/108139/Java-code-easy-to-understand

}
