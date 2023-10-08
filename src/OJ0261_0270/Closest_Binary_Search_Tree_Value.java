package OJ0261_0270;

import definition.TreeNode;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

public class Closest_Binary_Search_Tree_Value {
	/*
	 * https://leetcode.com/problems/closest-binary-search-tree-value/discuss/70392/Simple-iterative-Java-solution-with-explaination
	 * 
	 * binary search: go left if target is smaller than current root value, and go 
	 * right otherwise. Choose the closest to target value at each step.
	 * 
	 * If a target value less than root could be closer to the right subtree value, 
	 * then it is the closest to the root first not the right subtree value. 
	 * 
	 * Rf :
	 * https://leetcode.com/problems/closest-binary-search-tree-value/solution/
	 * https://leetcode.com/problems/closest-binary-search-tree-value/solution/621500
	 * 
	 * Other code:
	 * https://leetcode.com/problems/closest-binary-search-tree-value/discuss/70331/Clean-and-concise-java-solution
	 */
	public int closestValue_iterative2(TreeNode root, double target) {
		int closestVal = root.val;
		while (root != null) {
			
			// update closestVal if the current value is closer to target
			closestVal = Math.abs(target - root.val) < Math.abs(target - closestVal) ? 
					root.val : closestVal;
			
			// already find the best result
			if (closestVal == target) {
				return closestVal;
			}
			
			// binary search
			root = (root.val > target) ? root.left : root.right;
		}
		return closestVal;
	}
	
	/*
	 * by myself
	 * 
	 * Other code:
	 * https://leetcode.com/problems/closest-binary-search-tree-value/discuss/70386/Java-iterative-solution
	 */
	public int closestValue_self(TreeNode root, double target) {
        int res = root.val;
        
        TreeNode pre = null;
        TreeNode cur = root;
        while (cur != null) {
            pre = cur;
            if (Math.abs(target - pre.val) < Math.abs(target - res)) {
                res = pre.val;
            }
            
            if (cur.val > target) {
                cur = cur.left;
            }
            else if (cur.val < target) {
                cur = cur.right;
            }
            else {
                return cur.val;
            }
        }
        
        return res;
    }
	
	/*
	 * https://leetcode.com/problems/closest-binary-search-tree-value/discuss/70380/Java-iterative-and-recursive-solutions
	 * 
	 * This problem is basically finding the predecessor and successor of a given 
	 * value in the BST, and returning the one that has the smaller difference. 
	 */
	public int closestValue_iterative(TreeNode root, double target) {
		TreeNode greater = null;
		TreeNode smaller = null;
		while (root != null) {
			if (root.val == target) {
				return root.val;
			} 
			else if (root.val < target) {
				smaller = root;
				root = root.right;
			} 
			else {
				greater = root;
				root = root.left;
			}
		}
		
		if (greater == null) {
			return smaller.val;
		}
		if (smaller == null) {
			return greater.val;
		}
		return (greater.val - target) < (target - smaller.val) ? 
				greater.val : smaller.val;
	}
	
	/*
	 * https://leetcode.com/problems/closest-binary-search-tree-value/discuss/70327/4-7-lines-recursiveiterative-RubyC%2B%2BJavaPython
	 * 
	 * Closest is either the root's value (a) or the closest in the appropriate 
	 * subtree (b).
	 * 
	 * Other code:
	 * https://leetcode.com/problems/closest-binary-search-tree-value/discuss/70380/Java-iterative-and-recursive-solutions
	 */
	public int closestValue_recursive2(TreeNode root, double target) {
		int a = root.val;
		TreeNode kid = target < a ? root.left : root.right;
		
		if (kid == null)
			return a;
		
		int b = closestValue_recursive2(kid, target);
		return Math.abs(a - target) < Math.abs(b - target) ? a : b;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/closest-binary-search-tree-value/discuss/70322/Super-clean-recursive-Java-solution
	 */
	public int closestValue_recursive(TreeNode root, double target) {
		return closest_recursive(root, target, root.val);
	}

	private int closest_recursive(TreeNode node, double target, int val) {
		if (node == null)
			return val;
		
		if (Math.abs(node.val - target) < Math.abs(val - target))
			val = node.val;
		
		if (node.val < target)
			val = closest_recursive(node.right, target, val);
		else if (node.val > target)
			val = closest_recursive(node.left, target, val);
		
		return val;
	}
	
	/*
	 * https://leetcode.com/problems/closest-binary-search-tree-value/solution/
	 * Approach 2: Iterative Inorder, O(k) time
	 * 
	 * First, one could merge both steps by traversing the tree and searching the 
	 * closest value at the same time.
	 * 
	 * Second, one could stop just after identifying the closest value, there is no 
	 * need to traverse the whole tree. The closest value is found if the target 
	 * value is in-between of two inorder array elements 
	 * nums[i] <= target < nums[i + 1]. The closest value is one of these elements.
	 * 
	 * + Initiate stack as an empty array and predecessor value as a very small number.
	 * + While root is not null:
	 *   + To build an inorder traversal iteratively, go left as far as you can and 
	 *     add all nodes on the way into stack.
	 *   + Pop the last element from stack root = stack.pop().
	 *   + If target is in-between of pred and root.val, return the closest between 
	 *     these two elements.
	 *   + Set predecessor value to be equal to root.val and go one step right: 
	 *     root = root.right. 
	 * + We're here because during the loop one couldn't identify the closest value. 
	 *   That means that the closest value is the last value in the inorder 
	 *   traversal, i.e. current predecessor value. Return it.
	 */
	public int closestValue_inOrder_cut(TreeNode root, double target) {
		LinkedList<TreeNode> stack = new LinkedList<>();
		long pred = Long.MIN_VALUE;

		while (!stack.isEmpty() || root != null) {
			while (root != null) {
				stack.add(root);
				root = root.left;
			}
			root = stack.removeLast();

			if (pred <= target && target < root.val) {				
				return Math.abs(pred - target) < Math.abs(root.val - target) ? 
						(int) pred : root.val;
			}

			pred = root.val;
			root = root.right;
		}
		return (int) pred;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/closest-binary-search-tree-value/solution/
	 * Approach 1: Recursive Inorder + Linear search, O(N) time
	 * 
	 * build inorder traversal and then find the closest element in a sorted array 
	 * with built-in function
	 * 
	 * use buillt in Double.compare method in Java instead of deciding 1, -1
	 * `return Double.compare(Math.abs(o1 - target), Math.abs(o2 - target));`
	 * 
	 * or even better using Java 8+
	 * `Collections.min(nums, Comparator.comparingDouble(o -> Math.abs(o - target)));`
	 * 
	 * This will also take care of returning 0 in case of equal values.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/closest-binary-search-tree-value/solution/306513
	 */
	public void inorder_inOrder(TreeNode root, List<Integer> nums) {
		if (root == null)
			return;
		
		inorder_inOrder(root.left, nums);
		nums.add(root.val);
		inorder_inOrder(root.right, nums);
	}

	public int closestValue_inorder(TreeNode root, double target) {
		List<Integer> nums = new ArrayList<>();
		inorder_inOrder(root, nums);
		
		// return 下面這個較好
		// Collections.min(nums, Comparator.comparingDouble(o -> Math.abs(o - target)));
		return Collections.min(nums, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return Math.abs(o1 - target) < Math.abs(o2 - target) ? -1 : 1;
			}
		});
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/closest-binary-search-tree-value/discuss/70321/Clean-python-code
     * https://leetcode.com/problems/closest-binary-search-tree-value/discuss/183172/Elegant-Recursive-Python-solution
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/closest-binary-search-tree-value/discuss/70327/4-7-lines-recursiveiterative-RubyC%2B%2BJavaPython
     */
	
	/**
	 * JavaScript collections
	 * 
	 * https://leetcode.com/problems/closest-binary-search-tree-value/discuss/70326/Concise-JavaScript-recursive-and-iterative-solutions
	 */

}
