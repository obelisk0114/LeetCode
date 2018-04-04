package OJ0111_0120;

import definition.TreeNode;

import java.util.Stack;

public class Path_Sum {
	/*
	 * https://leetcode.com/problems/path-sum/discuss/36370/A-Java-Concise-solution
	 * 
	 * Rf : https://leetcode.com/problems/path-sum/discuss/36378/AcceptedMy-recursive-solution-in-Java
	 */
	public boolean hasPathSum(TreeNode root, int sum) {
		if (root == null) {
			return false;
		}
		if (root.left == null && root.right == null) {
			return (root.val == sum);
		}
		return hasPathSum(root.left, sum - root.val) || 
				hasPathSum(root.right, sum - root.val);
	}
	
	// By myself
	public boolean hasPathSum_self(TreeNode root, int sum) {
        if (root == null)
            return false;
        
        if (root.left == null && root.right == null) {
        	if (sum - root.val == 0) {
        		return true;
        	}
        	else {
        		return false;
        	}
        }
        
        if (root.left == null) {
        	return hasPathSum_self(root.right, sum - root.val);
        }
        
        if (root.right == null) {
        	return hasPathSum_self(root.left, sum - root.val);
        }
        //System.out.println("root = " + root.val + "; sum = " + sum + "; ans = " + ans);
        
        return (hasPathSum_self(root.right, sum - root.val) || 
        		hasPathSum_self(root.left, sum - root.val));
    }
        
    // https://leetcode.com/problems/path-sum/discuss/36543/Share-my-easy-and-clean-recursion-Java-solution-with-explanation
	public boolean hasPathSum2(TreeNode root, int sum) {

		// check if root is null
		if (root == null)
			return false;

		// if the current node is not a leaf node, do recursion.
		if (root.left != null || root.right != null)
			return hasPathSum2(root.left, sum - root.val) || 
					hasPathSum2(root.right, sum - root.val);

		// now the current node is a leaf node
		return sum - root.val == 0;
	}
	
	/*
	 * https://leetcode.com/problems/path-sum/discuss/36534/My-java-no-recursive-method
	 * 
	 * the idea is preorder traverse , instead of using recursive call, 
	 * I am using a stack. the only problem is that I changed TreeNode value
	 * 
	 * Other code :
	 * https://leetcode.com/problems/path-sum/discuss/36580/Java-solution-both-recursion-and-iteration
	 */
	public boolean hasPathSum_iterative(TreeNode root, int sum) {
		Stack<TreeNode> stack = new Stack<>();
		stack.push(root);
		while (!stack.isEmpty() && root != null) {
			TreeNode cur = stack.pop();
			if (cur.left == null && cur.right == null) {
				if (cur.val == sum)
					return true;
			}
			if (cur.right != null) {
				cur.right.val = cur.val + cur.right.val;
				stack.push(cur.right);
			}
			if (cur.left != null) {
				cur.left.val = cur.val + cur.left.val;
				stack.push(cur.left);
			}
		}
		return false;
	}

}
