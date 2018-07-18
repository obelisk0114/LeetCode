package OJ0441_0450;

import definition.TreeNode;

public class Delete_Node_in_a_BST {
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/delete-node-in-a-bst/discuss/93296/Recursive-Easy-to-Understand-Java-Solution
	 * 
	 * 1. Recursively find the node that has the same value as the key, while setting 
	 *    the left/right nodes equal to the returned subtree
	 * 2. Once the node is found, have to handle the below 4 cases
	 *    2.1 node doesn't have left or right - return null
	 *    2.2 node only has left subtree- return the left subtree
	 *    2.3 node only has right subtree- return the right subtree
	 *    2.4 node has both left and right - find the minimum value in the right 
	 *        subtree, set that value to the currently found node, then recursively 
	 *        delete the minimum value in the right subtree
	 * 
	 * Other code:
	 * https://leetcode.com/problems/delete-node-in-a-bst/discuss/93328/Java-Easy-to-Understand-Solution
	 */
	public TreeNode deleteNode(TreeNode root, int key) {
		if (root == null) {
			return null;
		}
		
		if (key < root.val) {
			root.left = deleteNode(root.left, key);
		} 
		else if (key > root.val) {
			root.right = deleteNode(root.right, key);
		} 
		else {
			if (root.left == null) {
				return root.right;
			} 
			else if (root.right == null) {
				return root.left;
			}

			TreeNode minNode = findMin(root.right);
			root.val = minNode.val;
			root.right = deleteNode(root.right, root.val);
		}
		return root;
	}

	private TreeNode findMin(TreeNode node) {
		while (node.left != null) {
			node = node.left;
		}
		return node;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/delete-node-in-a-bst/discuss/93298/Iterative-solution-in-Java-O(h)-time-and-O(1)-space
	 * 
	 * Find the node to be removed and its parent using binary search, and then use 
	 * deleteRootNode to delete the root node of the subtree and return the new root 
	 * node.
	 * 
	 * Transplanting the successor can keep the height of the tree almost unchanged, 
	 * while transplanting the whole left subtree could increase the height and thus 
	 * making the tree more unbalanced.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/delete-node-in-a-bst/discuss/93378/An-easy-understanding-O(h)-time-O(1)-space-Java-solution.
	 * 
	 * Other code:
	 * https://leetcode.com/problems/delete-node-in-a-bst/discuss/93344/Simplify-corner-cases-via-a-dummy-parent-of-the-root
	 */
	private TreeNode deleteRootNode(TreeNode root) {
		if (root == null) {
			return null;
		}
		if (root.left == null) {
			return root.right;
		}
		if (root.right == null) {
			return root.left;
		}
		
		TreeNode next = root.right;
		TreeNode pre = null;
		for (; next.left != null; pre = next, next = next.left)
			;
		
		next.left = root.left;
		if (root.right != next) {
			pre.left = next.right;
			next.right = root.right;
		}
		return next;
	}

	public TreeNode deleteNode_iterative(TreeNode root, int key) {
		TreeNode cur = root;
		TreeNode pre = null;
		while (cur != null && cur.val != key) {
			pre = cur;
			if (key < cur.val) {
				cur = cur.left;
			} 
			else if (key > cur.val) {
				cur = cur.right;
			}
		}
		
		if (pre == null) {
			return deleteRootNode(cur);
		}
		if (pre.left == cur) {
			pre.left = deleteRootNode(cur);
		} 
		else {
			pre.right = deleteRootNode(cur);
		}
		return root;
	}

}
