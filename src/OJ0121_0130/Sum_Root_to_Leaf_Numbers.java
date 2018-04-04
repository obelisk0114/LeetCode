package OJ0121_0130;

import definition.TreeNode;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Sum_Root_to_Leaf_Numbers {
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/sum-root-to-leaf-numbers/discuss/41578/Simple-java-solution-accepted
	 * 
	 * Rf : https://leetcode.com/problems/sum-root-to-leaf-numbers/discuss/41363/Short-Java-solution.-Recursion.
	 */
	public int sumNumbers(TreeNode root) {
		return sumNumbersUtil(root, 0);
	}
	public int sumNumbersUtil(TreeNode root, int path) {
		if (root == null) {
			return 0;
		}

		path = path * 10 + root.val;

		if (root.left == null && root.right == null) {
			return path;
		}

		return sumNumbersUtil(root.left, path) + sumNumbersUtil(root.right, path);
	}
	
	/*
	 * The following 2 functions are modified from this link.
	 * https://leetcode.com/problems/sum-root-to-leaf-numbers/discuss/41400/Can-you-improve-this-algorithm
	 */
	public int sumNumbers2(TreeNode root) {
		if (root == null)
			return 0;
		return sumR(root, 0);
	}

	public int sumR(TreeNode root, int x) {
		if (root.right == null && root.left == null)
			return 10 * x + root.val;
		
		if (root.right == null)
			return sumR(root.left, 10 * x + root.val);
		
		if (root.left == null)
			return sumR(root.right, 10 * x + root.val);
		
		return (sumR(root.left, 10 * x + root.val) + sumR(root.right, 10 * x + root.val));
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/sum-root-to-leaf-numbers/discuss/41513/Super-simple-DFS-Solution
	 */
	public int sumNumbers_preCalculate(TreeNode root) {
		return sumNumbers_preCalculate(root, 0);
	}
	private int sumNumbers_preCalculate(TreeNode root, int sum) {
		if (root == null)
			return 0;
		if (root.left == null && root.right == null)
			return sum + root.val;

		return sumNumbers_preCalculate(root.left, (sum + root.val) * 10) + sumNumbers_preCalculate(root.right, (sum + root.val) * 10);
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/sum-root-to-leaf-numbers/discuss/41531/Clean-Java-DFS-solution-(preorder-traversal)
	 */
	int total;
	public int sumNumbers_global(TreeNode root) {
		total = 0;
		helper(root, 0);
		return total;
	}
	void helper(TreeNode root, int sum) {
		if (root == null)
			return;

		sum = sum * 10 + root.val;

		if (root.left == null && root.right == null) {
			total += sum;
			return;
		}

		helper(root.left, sum);
		helper(root.right, sum);
	}
	
	/*
	 * https://leetcode.com/problems/sum-root-to-leaf-numbers/discuss/41367/Non-recursive-preorder-traverse-Java-solution
	 * 
	 * Rf : https://leetcode.com/problems/sum-root-to-leaf-numbers/discuss/41474/Java-iterative-and-recursive-solutions.
	 */
	public int sumNumbers_dfs_iterative(TreeNode root) {
		if (root == null) {
			return 0;
		}
		int sum = 0;
		TreeNode curr;
		Stack<TreeNode> ws = new Stack<TreeNode>();
		ws.push(root);

		while (!ws.empty()) {
			curr = ws.pop();

			if (curr.right != null) {
				curr.right.val = curr.val * 10 + curr.right.val;
				ws.push(curr.right);
			}

			if (curr.left != null) {
				curr.left.val = curr.val * 10 + curr.left.val;
				ws.push(curr.left);
			}

			if (curr.left == null && curr.right == null) { // leaf node
				sum += curr.val;
			}
		}
		return sum;
	}
	
	/*
	 * https://leetcode.com/problems/sum-root-to-leaf-numbers/discuss/41575/Simple-no-recursive-using-queue-java
	 * 
	 * Rf : https://leetcode.com/problems/sum-root-to-leaf-numbers/discuss/41474/Java-iterative-and-recursive-solutions.
	 */
	public int sumNumbers_bfs_iterative(TreeNode root) {
		if (root == null)
			return 0;
		Queue<TreeNode> node = new LinkedList<TreeNode>();
		Queue<Integer> sum = new LinkedList<Integer>();
		node.add(root);
		sum.add(root.val);
		int res = 0;
		while (!node.isEmpty()) {
			TreeNode cur = node.poll();
			Integer num = sum.poll();

			if (cur.left != null) {
				node.offer(cur.left);
				sum.offer((Integer) ((int) (num) * 10 + cur.left.val));
			}
			if (cur.right != null) {
				node.offer(cur.right);
				sum.offer((Integer) ((int) (num) * 10 + cur.right.val));
			}
			if (cur.left == null && cur.right == null) {
				res = (int) num + res;
			}

		}
		return res;
	}

}
