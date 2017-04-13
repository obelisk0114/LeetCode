package OJ141_150;

/*
 * https://discuss.leetcode.com/topic/64675/three-ways-of-iterative-preorder-traversing-easy-explanation
 * 
 * Morris Traversal
 * http://www.geeksforgeeks.org/morris-traversal-for-preorder/
 * http://www.cnblogs.com/AnnieKim/archive/2013/06/15/MorrisTraversal.html
 */

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

import definition.TreeNode;

public class Binary_Tree_Preorder_Traversal {
	/*
	 * https://discuss.leetcode.com/topic/12515/3-different-solutions
	 * 
	 * Ref : https://discuss.leetcode.com/topic/19196/java-solution-both-recursion-and-iteration
	 */
	public List<Integer> preorderTraversal_recursive(TreeNode root) {
		List<Integer> pre = new LinkedList<Integer>();
		if (root == null) return pre;
		pre.add(root.val);
		pre.addAll(preorderTraversal_recursive(root.left));
		pre.addAll(preorderTraversal_recursive(root.right));
		return pre;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/12515/3-different-solutions
	 * 
	 * Ref : https://discuss.leetcode.com/topic/47765/4-java-solutions-include-recursive-morris-and-two-other-iterative
	 */
	public List<Integer> preorderTraversal_recursive_helper(TreeNode root) {
		List<Integer> pre = new LinkedList<Integer>();
		preHelper(root,pre);
		return pre;
	}
	public void preHelper(TreeNode root, List<Integer> pre) {
		if (root == null) return;
		pre.add(root.val);
		preHelper(root.left,pre);
		preHelper(root.right,pre);
	}
	
	/*
	 * https://discuss.leetcode.com/topic/12515/3-different-solutions
	 * 
	 * Ref : 
	 * https://discuss.leetcode.com/topic/19196/java-solution-both-recursion-and-iteration
	 * https://discuss.leetcode.com/topic/6493/accepted-iterative-solution-in-java-using-stack/3
	 */
	public List<Integer> preorder_Iterative(TreeNode root) {
		List<Integer> pre = new LinkedList<Integer>();
		if (root == null)
			return pre;
		Stack<TreeNode> tovisit = new Stack<TreeNode>();
		tovisit.push(root);
		while (!tovisit.empty()) {
			TreeNode visiting = tovisit.pop();
			pre.add(visiting.val);
			if (visiting.right != null)
				tovisit.push(visiting.right);
			if (visiting.left != null)
				tovisit.push(visiting.left);
		}
		return pre;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/47765/4-java-solutions-include-recursive-morris-and-two-other-iterative
	 * 
	 * Ref :
	 * https://discuss.leetcode.com/topic/25756/java-solution-preorder-morris-traversal
	 */
	public List<Integer> preorderTraversal_Morris(TreeNode root) {
		List<Integer> l = new ArrayList<Integer>();
		if (root == null)
			return l;
		TreeNode pre, cur = root;
		while (cur != null) {
			if (cur.left == null) {
				l.add(cur.val);
				cur = cur.right;
			} else {
				pre = cur.left;
				while (pre.right != null && pre.right != cur) {
					pre = pre.right;
				}
				if (pre.right == null) {
					pre.right = cur;
					l.add(cur.val);
					cur = cur.left;
				} else {
					pre.right = null;
					cur = cur.right;
				}
			}
		}
		return l;
	}

}
