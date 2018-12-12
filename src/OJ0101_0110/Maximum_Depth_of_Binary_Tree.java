package OJ0101_0110;

import definition.TreeNode;
import java.util.LinkedList;
import java.util.Stack;

public class Maximum_Depth_of_Binary_Tree {
	// by myself
	public int maxDepth_self(TreeNode root) {
        if (root == null)
            return 0;
        
        int depth = 0;
        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.offerLast(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode cur = queue.pollFirst();
                
                if (cur.left != null) {
                    queue.offerLast(cur.left);
                }
                if (cur.right != null) {
                    queue.offerLast(cur.right);
                }
            }
            depth++;
        }
        return depth;
    }
	
	// https://leetcode.com/problems/maximum-depth-of-binary-tree/discuss/34216/Simple-solution-using-Java
	public int maxDepth_recursive(TreeNode root) {
		if (root == null) {
			return 0;
		}
		
		return 1 + Math.max(maxDepth_recursive(root.left), maxDepth_recursive(root.right));
	}
	
	// https://leetcode.com/problems/maximum-depth-of-binary-tree/discuss/34195/Two-Java-Iterative-solution-DFS-and-BFS
	public int maxDepth_DFS(TreeNode root) {
		if (root == null) {
			return 0;
		}

		Stack<TreeNode> stack = new Stack<>();
		Stack<Integer> value = new Stack<>();
		stack.push(root);
		value.push(1);
		
		int max = 0;
		while (!stack.isEmpty()) {
			TreeNode node = stack.pop();
			int temp = value.pop();
			max = Math.max(temp, max);
			
			if (node.left != null) {
				stack.push(node.left);
				value.push(temp + 1);
			}
			if (node.right != null) {
				stack.push(node.right);
				value.push(temp + 1);
			}
		}
		return max;
	}
	
	/**
	 * Other programming language collections
	 * 
	 * https://leetcode.com/problems/maximum-depth-of-binary-tree/discuss/34198/Python-multiple-solutions-recursion-level-order-using-stack-and-level-order-using-queue
	 * https://leetcode.com/problems/maximum-depth-of-binary-tree/discuss/34212/1-line-Ruby-and-Python
	 * 
	 * https://leetcode.com/problems/maximum-depth-of-binary-tree/discuss/34207/My-code-of-C++-Depth-first-search-and-Breadth-first-search
	 * https://leetcode.com/problems/maximum-depth-of-binary-tree/discuss/34405/8ms-RecursiveBFS-C++-Solutions
	 * https://leetcode.com/problems/maximum-depth-of-binary-tree/discuss/34224/My-C++-Solution
	 */

}
