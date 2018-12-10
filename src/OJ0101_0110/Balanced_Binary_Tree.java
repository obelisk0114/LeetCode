package OJ0101_0110;

import definition.TreeNode;
import java.util.Map;
import java.util.HashMap;
import java.util.Stack;

// https://leetcode.com/problems/balanced-binary-tree/discuss/36042/Two-different-definitions-of-balanced-binary-tree-result-in-two-different-judgments

public class Balanced_Binary_Tree {
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/balanced-binary-tree/discuss/35686/Java-solution-based-on-height-check-left-and-right-node-in-every-recursion-to-avoid-further-useless-search
	 * 
	 * This function will always complete in O(nodes) since it just calculates the 
	 * max depth of each subtree and when the recursion unwinds it checks to see if 
	 * the restriction on the height has been broken. If it has, it sets the height to 
	 * -1, indicating that the restrictions has been broken. Essentially, this just 
	 * gets around returning two values, one for whether the restriction has been 
	 * broken and one for the max height of tree.
	 * 
	 * Rf : https://leetcode.com/problems/balanced-binary-tree/discuss/35863/Java-1ms-Solution
	 */
	public boolean isBalanced(TreeNode root) {
		return height(root) != -1;
	}
	public int height(TreeNode node) {
		if (node == null) {
			return 0;
		}
		
		int lH = height(node.left);
		if (lH == -1) {
			return -1;
		}
		
		int rH = height(node.right);
		if (rH == -1) {
			return -1;
		}
		
		if (Math.abs(lH - rH) > 1) {
			return -1;
		}
		
		return Math.max(lH, rH) + 1;
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/balanced-binary-tree/discuss/35943/JAVA-O(n)-solution-based-on-Maximum-Depth-of-Binary-Tree
	 * 
	 */
	private boolean result = true;
	public boolean isBalanced_global(TreeNode root) {
		maxDepth(root);
		return result;
	}
	public int maxDepth(TreeNode root) {
		if (root == null)
			return 0;
		
		int l = maxDepth(root.left);
		int r = maxDepth(root.right);
		if (Math.abs(l - r) > 1)
			result = false;
		
		return 1 + Math.max(l, r);
	}
	
	/*
	 * The following 2 functions are by myself.
	 * 
	 * Other code:
	 * https://leetcode.com/problems/balanced-binary-tree/discuss/36027/Shortest-recursive-solution
	 */
	public boolean isBalanced_self(TreeNode root) {
        if (root == null)
            return true;
        
        int leftHeight = getHeight_self(root.left);
        int rightHeight = getHeight_self(root.right);
        
        if (Math.abs(leftHeight - rightHeight) > 1) {
            return false;
        }
        else {
            return (isBalanced_self(root.left) && isBalanced_self(root.right));
        }
    }
    private int getHeight_self(TreeNode root) {
        if (root == null) {
            return 0;
        }
        
        return (Math.max(getHeight_self(root.left), getHeight_self(root.right)) + 1);
    }
    
    // https://leetcode.com/problems/balanced-binary-tree/discuss/36008/A-Iterative-PostOrder-Traversal-Java-Solution
	public boolean isBalanced_iterative_postorder(TreeNode root) {
		if (root == null)
			return true;
		
		Stack<TreeNode> stack = new Stack<TreeNode>();
		Map<TreeNode, Integer> map = new HashMap<TreeNode, Integer>();
		stack.push(root);
		
		while (!stack.isEmpty()) {
			TreeNode node = stack.pop();
			if ((node.left == null || node.left != null && map.containsKey(node.left))
					&& (node.right == null || node.right != null && map.containsKey(node.right))) {
				int left = node.left == null ? 0 : map.get(node.left);
				int right = node.right == null ? 0 : map.get(node.right);
				if (Math.abs(left - right) > 1)
					return false;
				map.put(node, Math.max(left, right) + 1);
			} 
			else {
				if (node.left != null && !map.containsKey(node.left)) {
					stack.push(node);
					stack.push(node.left);
				} 
				else {
					stack.push(node);
					stack.push(node.right);
				}
			}
		}
		return true;
	}
    
    /**
     * Python collections
     * 
     * https://leetcode.com/problems/balanced-binary-tree/discuss/157645/Python-Tree-tm
     * 
     * https://leetcode.com/problems/balanced-binary-tree/discuss/35708/VERY-SIMPLE-Python-solutions-(iterative-and-recursive)-both-beat-90
     * https://leetcode.com/problems/balanced-binary-tree/discuss/128678/Python-3-iterative-and-recursive-solution
     */
	
	/**
	 * C++ collections
	 * 
	 * https://leetcode.com/problems/balanced-binary-tree/discuss/35691/The-bottom-up-O(N)-solution-would-be-better
	 * https://leetcode.com/problems/balanced-binary-tree/discuss/35975/My-C++-solution-in-15ms
	 * https://leetcode.com/problems/balanced-binary-tree/discuss/35734/C++-code-with-short-cut
	 */

}
