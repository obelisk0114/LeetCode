package OJ0091_0100;

import definition.TreeNode;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

public class Unique_Binary_Search_Trees_II {
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/3079/a-simple-recursive-solution
	 * 
	 * Start by noting that 1..n is the in-order traversal for any BST with nodes 1 
	 * to n. If I pick ith node as my root, the left subtree will contain elements 1 
	 * to (i-1), and the right subtree will contain elements (i+1) to n. 
	 * Use recursive calls to get back all possible trees for left and right subtrees 
	 * and combine them in all possible ways with the root.
	 * 
	 * Rf : https://discuss.leetcode.com/topic/12559/java-dp-solution-and-brute-force-recursive-solution
	 */
	public List<TreeNode> generateTrees(int n) {
		if (n == 0)
			return new ArrayList<TreeNode>();
		return genTrees(1, n);
	}
	public List<TreeNode> genTrees(int start, int end) {
		List<TreeNode> list = new ArrayList<TreeNode>();

		if (start > end) {
			list.add(null);
			return list;
		}
		
		/*** start : Can be commented ***/
		if (start == end) {
			list.add(new TreeNode(start));
			return list;
		}
		/*** end : Can be commented ***/

		List<TreeNode> left, right;
		for (int i = start; i <= end; i++) {
			left = genTrees(start, i - 1);
			right = genTrees(i + 1, end);

			for (TreeNode lnode : left) {
				for (TreeNode rnode : right) {
					TreeNode root = new TreeNode(i);
					root.left = lnode;
					root.right = rnode;
					list.add(root);
				}
			}
		}
		return list;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/7762/accepted-iterative-java-solution
	 * 
	 * All values in right subtree should be increased by the root value.
	 * For the left nodes, they always start from 1, which have the same values with 
	 * those in lists; While the right nodes, starting at j + 1, so they need 
	 * offset = j + 1;
	 * 
	 * Rf : https://discuss.leetcode.com/topic/2940/java-solution-with-dp
	 */
	public List<TreeNode> generateTrees_map(int n) {
		Map<Integer, List<TreeNode>> lists = new HashMap<Integer, List<TreeNode>>();

		List<TreeNode> list = new LinkedList<TreeNode>();
		if (n == 0)
			return list;
		list.add(null);
		lists.put(0, list);

		list = new LinkedList<TreeNode>();
		TreeNode root = new TreeNode(1);
		list.add(root);
		lists.put(1, list);

		for (int i = 2; i <= n; i++) {
			list = new LinkedList<TreeNode>();
			for (int j = 1; j <= i; j++) {
				for (TreeNode left : lists.get(j - 1)) {
					for (TreeNode right : lists.get(i - j)) {
						root = new TreeNode(j);
						root.left = left;
						root.right = greaterCopy(right, j);
						list.add(root);
					}
				}
			}
			lists.put(i, list);
		}
		return list;
	}
	private TreeNode greaterCopy(TreeNode node, int add) {
		if (node == null)
			return null;
		TreeNode copy = new TreeNode(node.val + add);
		copy.left = greaterCopy(node.left, add);
		copy.right = greaterCopy(node.right, add);
		return copy;
	}

}
