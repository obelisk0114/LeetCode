package OJ1001_1010;

import definition.TreeNode;

import java.util.LinkedList;
import java.util.Stack;
import java.util.Arrays;
import java.util.HashMap;

public class Construct_Binary_Search_Tree_from_Preorder_Traversal {
	/*
	 * https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/discuss/252754/Java-Stack-Iterative-Solution
	 * 
	 * 1. First item in preorder list is the root to be considered.
	 * 2. For next item in preorder list, there are 2 cases to consider:
	 *   2.1 If value is less than last item in stack, it is the left child of last 
	 *       item.
	 *   2.2 If value is greater than last item in stack, pop it
	 *       The last popped item will be the parent and the item will be the right 
	 *       child of the parent.
	 * 
	 * Rf : 
	 * https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/discuss/252722/Python-stack-solution-beats-100-on-runtime-and-memory
	 * https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/solution/
	 */
	public TreeNode bstFromPreorder_stack(int[] preorder) {
		if (preorder == null || preorder.length == 0) {
			return null;
		}

		Stack<TreeNode> stack = new Stack<>();
		TreeNode root = new TreeNode(preorder[0]);
		stack.push(root);
		for (int i = 1; i < preorder.length; i++) {
			TreeNode node = new TreeNode(preorder[i]);
			if (preorder[i] < stack.peek().val) {
				stack.peek().left = node;
			} 
			else {
				TreeNode parent = stack.peek();
				while (!stack.isEmpty() && preorder[i] > stack.peek().val) {
					parent = stack.pop();
				}
				parent.right = node;
			}
			stack.push(node);
		}
		return root;
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/discuss/252232/JavaC%2B%2BPython-O(N)-Solution
	 * 
	 * Give the function a bound the maximum number it will handle.
	 * The left recursion will take the elements smaller than node.val
	 * The right recursion will take the remaining elements smaller than bound
	 * 
	 * bstFromPreorder is called exactly N times. It's same as a preorder traversal.
	 * Time Complexity: O(N)
	 * 
	 * Left node is bounded by the parent node's value.
	 * Right node is bounded by the ancestor's bound.
	 * 
	 * We don't need to care about lower bound. When we construct the tree, we try to 
	 * create left node first. If the condition fails (i.e. current number is greater 
	 * than the parent node value), then we try to create the right node which 
	 * automatically satisfies the condition, hence no lower bound is needed
	 * 
	 * The given sequence is guaranteed to be valid, we don't need to validate if it 
	 * is a binary search tree or not. We just need to test again parent value to 
	 * decide a node should be left or right child. We don't need to check lower bound
	 * 
	 * Rf :
	 * https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/discuss/589801/JAVA-3-WAYS-TO-DO-THE-PROBLEM!-O(N)-APPROACH
	 * https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/discuss/252232/JavaC++Python-O(N)-Solution/511391
	 * 
	 * Other code:
	 * https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/discuss/254844/Java-O(n)-solution-with-range
	 */
	int i_upper = 0;

	public TreeNode bstFromPreorder_upper(int[] A) {
		return bstFromPreorder_upper(A, Integer.MAX_VALUE);
	}

	public TreeNode bstFromPreorder_upper(int[] A, int bound) {
		if (i_upper == A.length || A[i_upper] > bound)
			return null;
		
		TreeNode root = new TreeNode(A[i_upper++]);
		root.left = bstFromPreorder_upper(A, root.val);
		root.right = bstFromPreorder_upper(A, bound);
		
		return root;
	}

	// by myself
	public TreeNode bstFromPreorder_self(int[] preorder) {
        LinkedList<TreeNode> stack = new LinkedList<>();
        
        TreeNode root = new TreeNode(preorder[0]);
        stack.offerLast(root);
        TreeNode cur = root;
        for (int i = 1; i < preorder.length; i++) {
            if (preorder[i] < cur.val) {
                cur.left = new TreeNode(preorder[i]);
                cur = cur.left;
                stack.offerLast(cur);
            }
            else {
                while (!stack.isEmpty() && preorder[i] > stack.peekLast().val) {
                    cur = stack.pollLast();
                }
                cur.right = new TreeNode(preorder[i]);
                cur = cur.right;
                stack.offerLast(cur);
            }
        }
        return root;
    }
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/discuss/589059/JAVA-EASIEST-SOLUTION-WITH-CLEAR-EXPLANATION-OF-LOGIC!
	 * 
	 * 1. We create the node
	 * 2. We traverse the array for values which are less than the current node!
	 *    -- these values will become our left subtree. We stop whenever we get a 
	 *       value larger than the current root of the subtree!
	 * 3. We take the rest of the array (values which are greater than the value of 
	 *    the current root) - these are the values which will make out right subtree!
	 */
	public TreeNode bstFromPreorder_traverse_separate(int[] preorder) {
		return helper_traverse_separate(preorder, 0, preorder.length - 1);
	}

	private TreeNode helper_traverse_separate(int[] preorder, int start, int end) {
		if (start > end)
			return null;

		TreeNode node = new TreeNode(preorder[start]);
		int i;
		for (i = start; i <= end; i++) {
			if (preorder[i] > node.val)
				break;
		}

		node.left = helper_traverse_separate(preorder, start + 1, i - 1);
		node.right = helper_traverse_separate(preorder, i, end);
		return node;
	}
	
	/*
	 * Modify from this link :
	 * https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/discuss/589051/Morris'-algorithm-(O(n)-time-O(n)-space)
	 */
	public TreeNode bstFromPreorder_self_Morris(int[] preorder) {
		TreeNode root = null;
        TreeNode current = root;
        
        for (int val : preorder) {
        	TreeNode node = new TreeNode(val);
            if (current != null) {
                if (node.val < current.val) {                	
                    node.right = current;
                    current.left = current = node;
                }
                else {
                    while (current.right != null && node.val > current.right.val) {
                    	TreeNode tmp = current.right;
                    	current.right = null;
                    	current = tmp;
                    }

                    node.right = current.right;
                    current.right = current = node;
                }
            }
            else {
            	root = current = node;            	
            }
        }

        while (current.right != null) {
        	TreeNode tmp = current.right;
        	current.right = null;
        	current = tmp;
        }

        return root;
	}
	
	/*
	 * Rf : https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/discuss/589051/Morris'-algorithm-(O(n)-time-O(n)-space)
	 */
	public TreeNode bstFromPreorder_self_right_record(int[] preorder) {
        TreeNode root = new TreeNode(preorder[0]);
        TreeNode cur = root;
        for (int i = 1; i < preorder.length; i++) {
            if (preorder[i] < cur.val) {
                cur.left = new TreeNode(preorder[i]);
                cur.left.right = cur;
                cur = cur.left;
            }
            else {
                TreeNode pre = cur;
                while (cur != null && preorder[i] > cur.val) {
                    pre = cur;
                    cur = cur.right;
                    pre.right = null;
                }
                
                pre.right = new TreeNode(preorder[i]);
                pre.right.right = cur;
                cur = pre.right;
            }
        }
        
        while (cur.right != null) {
            TreeNode tmp = cur.right;
            cur.right = null;
            cur = tmp;
        }
        return root;
    }

	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/discuss/588889/Simple-Java-solution
	 * 
	 * Other code:
	 * https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/discuss/432979/Java%3A-Simple-solution-to-place-the-nodes-to-tree-one-by-one-(100100)
	 * https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/discuss/265834/Simple-java-solution
	 */
	public TreeNode bstFromPreorder_naive(int[] preorder) {
		int n = preorder.length;
		if (n == 0)
			return null;
		
		TreeNode res = new TreeNode(preorder[0]);
		for (int i = 1; i < n; i++) {
			res = insert_naive(res, preorder[i]);
		}
		return res;
	}

	private TreeNode insert_naive(TreeNode root, int val) {
		if (root == null)
			return new TreeNode(val);
		
		if (val <= root.val) {
			root.left = insert_naive(root.left, val);
		} 
		else {
			root.right = insert_naive(root.right, val);
		}
		return root;
	}
	
	/*
	 * The following class is from this link.
	 * https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/solution/
	 * Approach 2: Recursion
	 * 
	 * The inorder traversal was used only to check if the element could be placed in
	 * this subtree. Since one deals with a BST here, this could be verified with the
	 * help of lower and upper limits for each element as for the validate BST.
	 * 
	 * + Initialize the lower and upper limits as negative and positive infinity 
	 *   because one could always place the root.
	 * + Start from the first element in the preorder array `idx = 0`
	 * + Return `helper(lower, upper)`:
	 *   + If the preorder array is used up `idx = n` then the tree is constructed,
	 *     return null.
	 *   + If current value `val = preorder[idx]` is smaller than lower limit, or
	 *     larger than upper limit, return null.
	 *   + If the current value is in the limits, place it here `root = TreeNode(val)`
	 *     and proceed to construct recursively left and right subtrees:
	 *     `root.left = helper(lower, val)` and `root.right = helper(val, upper)`.
	 *   + Return `root`.
	 */
	class Solution_recursion {
		int idx = 0;
		int[] preorder;
		int n;

		public TreeNode helper(int lower, int upper) {
			// if all elements from preorder are used
			// then the tree is constructed
			if (idx == n)
				return null;

			int val = preorder[idx];
			// if the current element
			// couldn't be placed here to meet BST requirements
			if (val < lower || val > upper)
				return null;

			// place the current element
			// and recursively construct subtrees
			idx++;
			TreeNode root = new TreeNode(val);
			root.left = helper(lower, val);
			root.right = helper(val, upper);
			return root;
		}

		public TreeNode bstFromPreorder(int[] preorder) {
			this.preorder = preorder;
			n = preorder.length;
			return helper(Integer.MIN_VALUE, Integer.MAX_VALUE);
		}
	}

	/*
	 * The following class is from this link.
	 * https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/solution/
	 * Approach 1: Construct binary tree from preorder and inorder traversal
	 * 
	 * 2 Facts:
	 * + Binary tree could be constructed from preorder and inorder traversal.
	 * + Inorder traversal of BST is an array sorted in the ascending order.
	 * 
	 * Algorithm:
	 * + Construct inorder traversal by sorting the preorder array.
	 * + Construct binary tree from preorder and inorder traversal: the idea is to 
	 *   peek the elements one by one from the preorder array and try to put them as
	 *   a left or as a right child if it's possible. If it's impossible - just put
	 *   `null` as a child and proceed further. The possibility to use an element as
	 *   a child is checked by an inorder array; if it contains no elements for this
	 *   subtree, then the element couldn't be used here, and one should use `null`
	 *   as a child instead.  
	 */
	class Solution_preOrder_inOrder {
		// start from first preorder element
		int pre_idx = 0;
		int[] preorder;
		HashMap<Integer, Integer> idx_map = new HashMap<Integer, Integer>();

		public TreeNode helper(int in_left, int in_right) {
			// if there is no elements to construct subtrees
			if (in_left == in_right)
				return null;

			// pick up pre_idx element as a root
			int root_val = preorder[pre_idx];
			TreeNode root = new TreeNode(root_val);

			// root splits inorder list
			// into left and right subtrees
			int index = idx_map.get(root_val);

			// recursion
			pre_idx++;
			// build left subtree
			root.left = helper(in_left, index);
			// build right subtree
			root.right = helper(index + 1, in_right);
			return root;
		}

		public TreeNode bstFromPreorder(int[] preorder) {
			this.preorder = preorder;
			int[] inorder = Arrays.copyOf(preorder, preorder.length);
			Arrays.sort(inorder);

			// build a hashmap value -> its index
			int idx = 0;
			for (Integer val : inorder)
				idx_map.put(val, idx++);
			return helper(0, inorder.length);
		}
	}

	/**
     * Python collections
     * 
     * https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/discuss/252722/Python-stack-solution-beats-100-on-runtime-and-memory
     * https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/discuss/252232/JavaC%2B%2BPython-O(N)-Solution
     * https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/discuss/589354/Python3.-Simple-clean-and-easy-to-understand-solution
     * https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/discuss/588589/Python-3-Recursive-Easy-to-understand
     * https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/discuss/589389/Python-Concise-Solution
     * https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/discuss/588633/Python-easy-to-understand-96-faster-(iterative)
     * https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/discuss/589051/Morris'-algorithm-(O(n)-time-O(n)-space)
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/discuss/252273/C%2B%2B-O(n-log-n)-and-O(n)
     * https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/discuss/588642/Explaining-the-O(N)-approach-with-C%2B%2B-code.-Beats-100-solutions.
     * https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/discuss/252719/C%2B%2B-iterative-O(n)-solution-using-decreasing-stack
     * https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/discuss/273168/C%2B%2B-Straightforward-iterative-approach-(100100)
     * https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/discuss/588733/simple-recursive-insert-in-bst-in-CPP
     * https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/discuss/324887/Less-than-10-lines-C%2B%2B-beats-100
     */
	
	/**
	 * JavaScript collections
	 * 
	 * https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/discuss/589238/JavaScript-solution-inspired-by-lee215-with-explanation-O(n)-time-O(h)-space.
	 * https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/discuss/589180/JS-easy-to-read-preorder-traversal-(92runtime80space)
	 * https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/discuss/254002/JS-recursive
	 */

}
