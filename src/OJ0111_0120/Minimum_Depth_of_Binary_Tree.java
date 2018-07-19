package OJ0111_0120;

import definition.TreeNode;

import java.util.LinkedList;

public class Minimum_Depth_of_Binary_Tree {
	/*
	 * by myself
	 * 
	 * Other code:
	 * https://leetcode.com/problems/minimum-depth-of-binary-tree/discuss/36199/Easy-Solution-Using-BFS-in-JAVA
	 * https://leetcode.com/problems/minimum-depth-of-binary-tree/discuss/36061/My-solution-used-level-order-traversal
	 */
	public int minDepth_self(TreeNode root) {
		if (root == null)
			return 0;

		int ans = 1;
		LinkedList<TreeNode> queue = new LinkedList<>();
		queue.addLast(root);

		while (!queue.isEmpty()) {
			int size = queue.size();
			for (int i = 0; i < size; i++) {
				TreeNode cur = queue.removeFirst();

				if (cur.left != null)
					queue.addLast(cur.left);
				if (cur.right != null)
					queue.addLast(cur.right);

				if (cur.left == null && cur.right == null)
					return ans;
			}
			ans++;
		}
		return ans;
	}
	
	// https://leetcode.com/problems/minimum-depth-of-binary-tree/discuss/36182/My-4-lines-java-solution
	public int minDepth(TreeNode root) {
		if (root == null)
			return 0;
		if (root.left == null)
			return minDepth(root.right) + 1;
		if (root.right == null)
			return minDepth(root.left) + 1;
		return Math.min(minDepth(root.left) + 1, minDepth(root.right) + 1);
	}
	
	/*
	 * https://leetcode.com/problems/minimum-depth-of-binary-tree/discuss/36045/My-4-Line-java-solution
	 * 
	 * root.left == null || root.right == null ? left + right + 1 means that 
	 * if root.left is null or root.right is null, then adds left, right and 1. 
	 * There are three conditions here.
	 * 
	 * [1] root.left is null, we need to pick right.
	 * [2] root.left is null, we need to pick left.
	 * [3] root.left and root.right are all 0, we need to return 1.
	 * Combined them together, it is left + right + 1
	 * 
	 * Rf : leetcode.com/problems/minimum-depth-of-binary-tree/discuss/36045/My-4-Line-java-solution/34253
	 */
	public int minDepth_recursive2(TreeNode root) {
		if (root == null)
			return 0;
		int left = minDepth_recursive2(root.left);
		int right = minDepth_recursive2(root.right);
		return (left == 0 || right == 0) ? 
				left + right + 1 : Math.min(left, right) + 1;
	}
	
	/*
	 * https://leetcode.com/problems/minimum-depth-of-binary-tree/discuss/36188/Very-easy-with-recursion-1ms-Java-solution
	 * 
	 * If a node has both left and right child, it means that the node has both left 
	 * child tree and right child tree, so we just recursively find the min height of 
	 * subtree. However, if a node has only one whichever subtree, let's say left 
	 * subtree. We should find the height of the left subtree itself, so we use max in 
	 * this condition.
	 */
	public int minDepth_recursive3(TreeNode root) {
		if (root == null)
			return 0;

		if (root.left != null && root.right != null)
			return Math.min(minDepth_recursive3(root.left), 
					minDepth_recursive3(root.right)) + 1;
		else
			return Math.max(minDepth_recursive3(root.left), 
					minDepth_recursive3(root.right)) + 1;
	}
	
	/*
	 * https://leetcode.com/problems/minimum-depth-of-binary-tree/discuss/36060/3-lines-in-Every-Language
	 * 
	 * We need to add the smaller one of the child depths - except if that's zero, 
	 * then add the larger one.
	 */
	public int minDepth_recursive4(TreeNode root) {
		if (root == null)
			return 0;
		int L = minDepth_recursive4(root.left), R = minDepth_recursive4(root.right);
		return 1 + (Math.min(L, R) > 0 ? Math.min(L, R) : Math.max(L, R));
	}

}
