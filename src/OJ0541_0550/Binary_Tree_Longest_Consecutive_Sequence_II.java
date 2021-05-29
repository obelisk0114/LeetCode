package OJ0541_0550;

import definition.TreeNode;

public class Binary_Tree_Longest_Consecutive_Sequence_II {
	/*
	 * The following 2 functions are modified by myself.
	 * 
	 * increase = the length of the longest incrementing branch below the current 
	 *            node including itself 
	 * decrease = the length of the longest decrementing branch below the current 
	 *            node including itself.
	 * longestPath_self_modify return [increase, decrease]
	 * 
	 * We start off by assigning both `increase` and `decrease` as 1 for the current 
	 * node. This is because the node itself always forms a consecutive increasing as 
	 * well as decreasing path of length 1.
	 * 
	 * if the left child is just smaller than the current node, it forms a decreasing 
	 * sequence with the current node. Thus, the `decrease` value for the current 
	 * node is stored as the left child's `decrease` value + 1.
	 * 
	 * if the left child is just larger than the current node, it forms an 
	 * incrementing sequence with the current node. Thus, we update the current 
	 * node's `increase` value as left_child(increase) + 1
	 * 
	 * For current node's `increase` and `decrease`, we need to consider the maximum 
	 * value out of the two values obtained from the left and the right child for 
	 * both `increase` and `decrease`, since we need to consider the longest sequence 
	 * possible.
	 * 
	 * we update the length of the longest consecutive path found so far as 
	 * max = Math.max(max, decrease + increase - 1)
	 * 
	 * Rf :
	 * https://leetcode.com/problems/binary-tree-longest-consecutive-sequence-ii/solution/
	 * https://leetcode.com/problems/binary-tree-longest-consecutive-sequence-ii/discuss/101519/Neat-Java-Solution-Single-pass-O(n)
	 */
	public int longestConsecutive_self_modify(TreeNode root) {
        int[] max = { 0 };
		longestPath_self_modify(root, max);
		return max[0];
	}

	public int[] longestPath_self_modify(TreeNode root, int[] max) {
		if (root == null)
			return new int[] { 0, 0 };
		
        int[] left = longestPath_self_modify(root.left, max);
        int[] right = longestPath_self_modify(root.right, max);
        
		int increase = 1, decrease = 1;
		if (root.left != null) {
			if (root.val == root.left.val + 1)
				decrease = left[1] + 1;
			else if (root.val == root.left.val - 1)
				increase = left[0] + 1;
		}
		
		if (root.right != null) {
			if (root.val == root.right.val + 1)
				decrease = Math.max(decrease, right[1] + 1);
			else if (root.val == root.right.val - 1)
				increase = Math.max(increase, right[0] + 1);
		}
		
		max[0] = Math.max(max[0], decrease + increase - 1);
		return new int[] { increase, decrease };
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/binary-tree-longest-consecutive-sequence-ii/discuss/101519/Neat-Java-Solution-Single-pass-O(n)
	 * 
	 * For each subtree we recursively compute the length of longest ascending and 
	 * descending path starting from the subtree root. Then we can efficiently check 
	 * if we could join the two subtree together to get a longer child-parent-child 
	 * path. In another word, for each subtree, the longest child-parent-child 
	 * consecutive (with root being the parent) is dec + inc - 1 since both the 
	 * ascending and descending path start from root.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/binary-tree-longest-consecutive-sequence-ii/discuss/101509/Java-recursively-compute-ascending-and-descending-sequence
	 * 
	 * Other code:
	 * https://leetcode.com/problems/binary-tree-longest-consecutive-sequence-ii/discuss/101510/Java-solution-Binary-Tree-Post-Order-Traversal
	 */
	int maxval_global = 0;

	public int longestConsecutive_global(TreeNode root) {
		longestPath_global(root);
		return maxval_global;
	}

	public int[] longestPath_global(TreeNode root) {
		if (root == null)
			return new int[] { 0, 0 };
		
		int inr = 1, dcr = 1;
		if (root.left != null) {
			int[] l = longestPath_global(root.left);
			
			if (root.val == root.left.val + 1)
				dcr = l[1] + 1;
			else if (root.val == root.left.val - 1)
				inr = l[0] + 1;
		}
		
		if (root.right != null) {
			int[] r = longestPath_global(root.right);
			
			if (root.val == root.right.val + 1)
				dcr = Math.max(dcr, r[1] + 1);
			else if (root.val == root.right.val - 1)
				inr = Math.max(inr, r[0] + 1);
		}
		
		maxval_global = Math.max(maxval_global, dcr + inr - 1);
		return new int[] { inr, dcr };
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/binary-tree-longest-consecutive-sequence-ii/discuss/101519/Neat-Java-Solution-Single-pass-O(n)/105241
	 * 
	 * we can use positive or negative number to represent the increasing or 
	 * decreasing order.
	 */
	private int max_sign = 0;

	public int longestConsecutive_sign(TreeNode root) {
		h_sign(root, 0);
		return max_sign;
	}

	private int h_sign(TreeNode root, int pre) {
		if (root == null) {
			return 0;
		}
		
		int left = h_sign(root.left, root.val);
		int right = h_sign(root.right, root.val);

		if (left * right < 0) {
			max_sign = Math.max(max_sign, 1 + Math.abs(left) + Math.abs(right));
		} 
		else {
			max_sign = Math.max(max_sign, 
					1 + Math.max(Math.abs(left), Math.abs(right)));
		}

		if (root.val - pre == 1) {
			return 1 + Math.max(0, Math.max(left, right));
		}
		if (root.val - pre == -1) {
			return -1 + Math.min(0, Math.min(left, right));
		}
		return 0;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/binary-tree-longest-consecutive-sequence-ii/discuss/343806/Powerful-Java-Preorder-traverse.
	 * 
	 * inc - dec - res
	 */
	public int longestConsecutive_global2(TreeNode root) {
		int[] res = helper_global2(root);
		return res[2];
	}

	private int[] helper_global2(TreeNode node) {
		if (node == null)
			return new int[3];

		int[] left = helper_global2(node.left);
		int[] right = helper_global2(node.right);
		
		// inc - dec - res		
		int[] res = new int[3];

		int leftInc = (left[0] == 0 || node.val != node.left.val + 1) ? 
				1 : left[0] + 1;
		int rightInc = (right[0] == 0 || node.val != node.right.val + 1) ? 
				1 : right[0] + 1;

		int leftDec = (left[1] == 0 || node.val != node.left.val - 1) ? 
				1 : left[1] + 1;
		int rightDec = (right[1] == 0 || node.val != node.right.val - 1) ? 
				1 : right[1] + 1;

		res[0] = Math.max(leftInc, rightInc);
		res[1] = Math.max(leftDec, rightDec);
		res[2] = Math.max(Math.max(left[2], right[2]), 
				Math.max(leftInc + rightDec - 1, leftDec + rightInc - 1));

		return res;
	}

	/*
	 * The following 2 functions are by myself.
	 */
	public int longestConsecutive_self(TreeNode root) {
		if (root == null) {
			return 0;
		}
		
		int[] res = { 1 };
		dfs_self(root, res);
		return res[0];
	}
	
	private int[] dfs_self(TreeNode root, int[] res) {
		if (root == null) {
			return new int[] { 0, 0 };
		}
		
		int[] left = dfs_self(root.left, res);
		int[] right = dfs_self(root.right, res);
		
		// { asc, dsc }
		int[] max = { 1, 1 };
		
		if (root.left != null) {
			// root.left > root > root.right
			if (root.left.val - root.val == 1) {
				max[0] = left[0] + 1;
				res[0] = Math.max(res[0], max[0]);
				
				if (root.right != null && root.val - root.right.val == 1) {
					res[0] = Math.max(res[0], left[0] + 1 + right[1]);
				}
			}
			// root.right > root > root.left
			else if (root.val - root.left.val == 1) {
				max[1] = left[1] + 1;
				res[0] = Math.max(res[0], max[1]);
				
				if (root.right != null && root.right.val - root.val == 1) {
					res[0] = Math.max(res[0], left[1] + 1 + right[0]);
				}
			}
		}
		
		if (root.right != null) {
			// root.right > root > root.left
			if (root.right.val - root.val == 1) {
				max[0] = Math.max(max[0], right[0] + 1);
				res[0] = Math.max(res[0], max[0]);

				if (root.left != null && root.val - root.left.val == 1) {
					res[0] = Math.max(res[0], right[0] + 1 + left[1]);
				}
			}
			// root.left > root > root.right
			else if (root.val - root.right.val == 1) {
				max[1] = Math.max(max[1], right[1] + 1);
				res[0] = Math.max(res[0], max[1]);
				
				if (root.left != null && root.left.val - root.val == 1) {
					res[0] = Math.max(res[0], right[1] + 1 + left[0]);
				}
			}
		}
		
		return max;
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/binary-tree-longest-consecutive-sequence-ii/discuss/521677/Post-Order-Traversal-Java-Clean-Code
	 * 
	 * post-order traversal
	 * The helper function calculates the longest consecutive sequence, both 
	 * increasing and decreasing, including the current node, along the 
	 * parent-to-child path. A global variable, maxLen, records the longest path 
	 * during the traversal.
	 * 
	 * maxLen = Math.max(maxLen, Math.max(res[0] + res[1] - 1)); No need to insert 
	 * Math.max(res[0], res[1]) because res[0] + res[1] - 1 always greater than or 
	 * equals either res[0] or res[1].
	 * 
	 * res[0] = longest decreasing path from root to leaf;
	 * res[1] = longest increasing path from root to leaf;
	 * 
	 * At any node, the longest increasing/decreasing path PASSING the root is 
	 * (res[0] + res[1] - 1). "-1" because both res[0] and res[1] count the root so 
	 * the root is counted twice.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/binary-tree-longest-consecutive-sequence-ii/discuss/521677/Post-Order-Traversal-Java-Clean-Code/472857
	 * https://leetcode.com/problems/binary-tree-longest-consecutive-sequence-ii/discuss/521677/Post-Order-Traversal-Java-Clean-Code/472826
	 * 
	 * Other code:
	 * https://leetcode.com/problems/binary-tree-longest-consecutive-sequence-ii/discuss/101523/Easy-O(n)-Java-Solution-for-2-problems
	 */
	public int longestConsecutive_postOrder(TreeNode root) {
		helper_postOrder(root);
		return maxLen_postOrder;
	}

	int maxLen_postOrder = 0;

	public int[] helper_postOrder(TreeNode root) {
		if (root == null)
			return new int[] { 0, 0 };

		int[] left = helper_postOrder(root.left), 
				right = helper_postOrder(root.right);

		left[0] = (root.left != null && root.val == root.left.val + 1) ? 
				left[0] + 1 : 1;
		left[1] = (root.left != null && root.val == root.left.val - 1) ? 
				left[1] + 1 : 1;
		right[0] = (root.right != null && root.val == root.right.val + 1) ? 
				right[0] + 1 : 1;
		right[1] = (root.right != null && root.val == root.right.val - 1) ? 
				right[1] + 1 : 1;

		int[] res = new int[2];
		res[0] = Math.max(left[0], right[0]);
		res[1] = Math.max(left[1], right[1]);

		// Math.max(res[0], res[1]) ¥i¥H¬Ù²¤
		maxLen_postOrder = Math.max(maxLen_postOrder, Math.max(res[0] + res[1] - 1, 
				Math.max(res[0], res[1])));

		return res;
	}

	/**
     * Python collections
     * 
     * https://leetcode.com/problems/binary-tree-longest-consecutive-sequence-ii/discuss/389210/Python-bottom-up-DFS-solution-(56-ms-beat-93.37)
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/binary-tree-longest-consecutive-sequence-ii/discuss/101520/DFS-C%2B%2B-Python-solutions
     * https://leetcode.com/problems/binary-tree-longest-consecutive-sequence-ii/discuss/101511/c%2B%2B-Short-Beautiful-Code
     */

}
