package OJ0121_0130;

import definition.TreeNode;

public class Binary_Tree_Maximum_Path_Sum {
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/binary-tree-maximum-path-sum/discuss/39775/Accepted-short-solution-in-Java/2
	 */
	public int maxPathSum(TreeNode root) {
		int[] max = { Integer.MIN_VALUE };
		maxPathSum(max, root);
		return max[0];
	}
	private int maxPathSum(int[] max, TreeNode root) {
		if (root == null)
			return 0;
		int leftMax = Math.max(0, maxPathSum(max, root.left));
		int rightMax = Math.max(0, maxPathSum(max, root.right));
		max[0] = Math.max(max[0], root.val + leftMax + rightMax);
		return root.val + Math.max(leftMax, rightMax);
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/binary-tree-maximum-path-sum/discuss/39775/Accepted-short-solution-in-Java
	 * 
	 * A path from start to end, goes up on the tree for 0 or more steps, 
	 * then goes down for 0 or more steps. Once it goes down, it can¡¦t go up. 
	 * Each path has a highest node, which is also the lowest common ancestor of 
	 * all other nodes on the path.
	 * 
	 * A recursive method maxPathDown(TreeNode node) 
	 * (1) computes the maximum path sum with highest node is the input node, 
	 * update maximum if necessary 
	 * (2) returns the maximum sum of the path that can be extended to 
	 * input node's parent.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/binary-tree-maximum-path-sum/discuss/39933/Accepted-O(n)-solution
	 * https://leetcode.com/problems/binary-tree-maximum-path-sum/discuss/39875/Elegant-Java-solution
	 */
	int maxValue;
	public int maxPathSum1(TreeNode root) {
		maxValue = root.val;
		maxPathDown(root);
		return maxValue;
	}
	private int maxPathDown(TreeNode node) {
		if (node == null)
			return 0;
		int left = Math.max(0, maxPathDown(node.left));
		int right = Math.max(0, maxPathDown(node.right));
		maxValue = Math.max(maxValue, left + right + node.val);
		return Math.max(left, right) + node.val;
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/binary-tree-maximum-path-sum/discuss/39888/A-recursive-solution-with-comment
	 */
    int max2 = Integer.MIN_VALUE;            // global max
    public int maxPathSum2(TreeNode root) {
        dfs(root);
        return max2;
    }
    private int dfs(TreeNode root) {
        if (root == null) 
        	return 0;
        
        // 2 possible choices:
        //   1. Already calculated in left or right child
        //   2. left max path + right max path + root
        int lMax = dfs(root.left);
        int rMax = dfs(root.right);
        if (lMax + rMax + root.val > max2) 
        	max2 = lMax + rMax + root.val;
        
        // if the below path is negative, just make it 0 so that we could 'ignore' it
        return Math.max(0, root.val + Math.max(lMax, rMax));
    }
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/binary-tree-maximum-path-sum/discuss/39879/My-AC-java-recursive-solution
	 * 
	 * max1 is the max value of the current node to pass to the upper level node.
	 * 
	 * max is the global max value that could be max1 
	 * or the sum of root and left max and right max
	 */
	int max_separate = Integer.MIN_VALUE;
	public int maxPathSum_separate_max(TreeNode root) {
		maxPathSumR(root);
		return max_separate;
	}
	public int maxPathSumR(TreeNode root) {
		if (root == null)
			return 0;
		
		int left = maxPathSumR(root.left);
		int right = maxPathSumR(root.right);

		int max1 = Math.max(root.val, Math.max(root.val + left, root.val + right));
		max_separate = Math.max(max_separate, Math.max(max1, left + right + root.val));
		return max1;
	}

}
