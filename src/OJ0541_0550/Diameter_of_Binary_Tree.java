package OJ0541_0550;

import definition.TreeNode;

import java.util.Map;
import java.util.HashMap;
import java.util.Stack;

public class Diameter_of_Binary_Tree {
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/diameter-of-binary-tree/discuss/101130/C++-Java-Clean-Code
	 * 
	 * 1. If the longest path will include the root node, then the longest path must 
	 *    be the depth(root->right) + depth (root->left)
	 * 2. If the longest path does not include the root node, this problem is divided 
	 *    into 2 sub-problem: set left child and right child as the new root 
	 *    separately, and repeat step1.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/diameter-of-binary-tree/discuss/101132/Java-Solution-MaxDepth
	 * https://leetcode.com/problems/diameter-of-binary-tree/discuss/101118/Python-Simple-with-Explanation
	 * https://leetcode.com/problems/diameter-of-binary-tree/discuss/101115/543.-Diameter-of-Binary-Tree-C++_Recursive_with-brief-explanation
	 */
	public int diameterOfBinaryTree(TreeNode root) {
		int[] diameter = new int[1];
		height(root, diameter);
		return diameter[0];
	}

	private int height(TreeNode node, int[] diameter) {
		if (node == null) {
			return 0;
		}
		
		int lh = height(node.left, diameter);
		int rh = height(node.right, diameter);
		diameter[0] = Math.max(diameter[0], lh + rh);
		
		return 1 + Math.max(lh, rh);
	}
	
	/*
	 * https://leetcode.com/problems/diameter-of-binary-tree/discuss/124198/Iterative-Accepted-Java-Solution
	 * 
	 * The idea is to use Post order traversal which means make sure the node is there 
	 * till the left and right child are processed that's the reason you use peek 
	 * method in the stack to not pop it off without being done with the left and 
	 * right child nodes. Then for each node calculate the max of the left and right 
	 * sub trees depth and also simultaneously calculate the overall max of the left 
	 * and right subtrees count.
	 */
	public int diameterOfBinaryTree_iterative(TreeNode root) {
		if (root == null) {
			return 0;
		}

		int overallNodeMax = 0;
		
		Stack<TreeNode> nodeStack = new Stack<>();
		Map<TreeNode, Integer> nodePathCountMap = new HashMap<>();
		nodeStack.push(root);
		
		while (!nodeStack.isEmpty()) {
			TreeNode node = nodeStack.peek();
			if (node.left != null && !nodePathCountMap.containsKey(node.left)) {
				nodeStack.push(node.left);
			} 
			else if (node.right != null && !nodePathCountMap.containsKey(node.right)) {
				nodeStack.push(node.right);
			} 
			else {
				TreeNode rootNodeEndofPostOrder = nodeStack.pop();
				int leftMax = nodePathCountMap.getOrDefault(rootNodeEndofPostOrder.left, 0);
				int rightMax = nodePathCountMap.getOrDefault(rootNodeEndofPostOrder.right, 0);
				int nodeMax = 1 + Math.max(leftMax, rightMax);
				
				nodePathCountMap.put(rootNodeEndofPostOrder, nodeMax);
				overallNodeMax = Math.max(overallNodeMax, leftMax + rightMax);
			}
		}
		return overallNodeMax;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/diameter-of-binary-tree/discuss/101120/Java-easy-to-understand-solution
	 * 
	 * Rf : leetcode.com/problems/diameter-of-binary-tree/discuss/101120/Java-easy-to-understand-solution/104918
	 */
	public int diameterOfBinaryTree_multi_recursive(TreeNode root) {
		if (root == null) {
			return 0;
		}
		
		int dia = depth(root.left) + depth(root.right);
		int ldia = diameterOfBinaryTree_multi_recursive(root.left);
		int rdia = diameterOfBinaryTree_multi_recursive(root.right);
		return Math.max(dia, Math.max(ldia, rdia));
	}

	public int depth(TreeNode root) {
		if (root == null) {
			return 0;
		}
		
		return 1 + Math.max(depth(root.left), depth(root.right));
	}
	
	// https://leetcode.com/problems/diameter-of-binary-tree/discuss/101129/Solution-by-minions_

}
