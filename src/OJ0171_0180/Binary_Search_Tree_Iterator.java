package OJ0171_0180;

import java.util.List;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Iterator;

import definition.TreeNode;

public class Binary_Search_Tree_Iterator {
	/*
	 * https://leetcode.com/problems/binary-search-tree-iterator/discuss/52592/My-Solution-with-less-than-10-lines-of-code
	 * 
	 * Once you get to a TreeNode, in order to get the smallest, you need to go all 
	 * the way down its left branch. First step is to point to the left most TreeNode.
	 * when we are traversal to the left most TreeNode, we store each TreeNode we met.
	 * 
	 * After returning the smallest TreeNode, I need to point to the next smallest 
	 * TreeNode. When the current TreeNode has a right branch, we need to jump to its 
	 * right child first and then traversal to its right child's left most TreeNode.
	 * 
	 * When the current TreeNode doesn't have a right branch, it means there cannot be a 
	 * node with value smaller than itself father node, point at its father node.
	 * 
	 * The overall thinking leads to Stack to simulate inorder traversal (iterative)
	 * 
	 * Each time we keep pushing the left children into the stack until we reach a 
	 * null node. Then the top node will always have the minimum value. It's kind of 
	 * like the in-order traversal, except that we keep the "root" nodes in the stack 
	 * for future use (such as adding the right children to the stack).
	 * 
	 * Rf :
	 * https://leetcode.com/problems/binary-search-tree-iterator/discuss/52526/Ideal-Solution-using-Stack-(Java)
	 * https://leetcode.com/articles/binary-search-tree-iterator/
	 * https://leetcode.com/problems/binary-search-tree-iterator/discuss/189561/Clean-and-fast-Java-solution
	 * https://leetcode.com/problems/binary-search-tree-iterator/discuss/52525/My-solutions-in-3-languages-with-Stack
	 */
	public class BSTIterator {

		private Stack<TreeNode> stack = new Stack<TreeNode>();

		public BSTIterator(TreeNode root) {
			pushAllNodes(root);
		}

		/** @return whether we have a next smallest number */
		public boolean hasNext() {
			return !stack.isEmpty();
		}

		/** @return the next smallest number */
		public int next() {
			TreeNode minNode = stack.pop();
			pushAllNodes(minNode.right);
			return minNode.val;
		}

		/** @add the current node and all its left child to the stack */
		private void pushAllNodes(TreeNode node) {
			while (node != null) {
				stack.push(node);
				node = node.left;
			}
		}
	}
	
	// https://leetcode.com/problems/binary-search-tree-iterator/discuss/52653/Java-concise-solution./53615
	public class BSTIterator_Iterator {

		Iterator<Integer> iterator;

		public BSTIterator_Iterator(TreeNode root) {
			List<Integer> inorder = new ArrayList<>();
			inOrder(root, inorder);
			iterator = inorder.iterator();
		}

		private void inOrder(TreeNode node, List<Integer> inOrder) {
			if (node != null) {
				inOrder(node.left, inOrder);
				inOrder.add(node.val);
				inOrder(node.right, inOrder);
			}
		}

		/** @return whether we have a next smallest number */
		public boolean hasNext() {
			return iterator.hasNext();
		}

		/** @return the next smallest number */
		public int next() {
			return iterator.next();
		}
	}

	/*
	 * https://leetcode.com/problems/binary-search-tree-iterator/discuss/52584/My-java-accepted-solution
	 * 
	 * the idea is same as using stack to do Binary Tree Inorder Traversal
	 * 1. Some initialization.
	 * 2. A while-loop with a condition that tells whether there is more.
	 * 3. The loop body gets the next value and does something with it.
	 * 
	 * Put the three parts of that iterative solution into our three iterator methods
	 * 
	 * Rf :
	 * https://leetcode.com/problems/binary-search-tree-iterator/discuss/52647/Nice-Comparison-(and-short-Solution)
	 */
	public class BSTIterator_later {

		private Stack<TreeNode> stack = null;
		private TreeNode current = null;

		public BSTIterator_later(TreeNode root) {
			current = root;
			stack = new Stack<>();
		}

		/** @return whether we have a next smallest number */
		public boolean hasNext() {
			return !stack.isEmpty() || current != null;
		}

		/** @return the next smallest number */
		public int next() {
			while (current != null) {
				stack.push(current);
				current = current.left;
			}
			TreeNode t = stack.pop();
			current = t.right;
			return t.val;
		}
	}
	
	// by myself
	class BSTIterator_self {
	    private List<Integer> list;
	    private int count;

	    public BSTIterator_self(TreeNode root) {
	        list = new ArrayList<>();
	        count = 0;
	        
	        inorder(root);
	    }
	    
	    private void inorder(TreeNode root) {
	        if (root == null)
	            return;
	        
	        inorder(root.left);
	        list.add(root.val);
	        inorder(root.right);
	    }
	    
	    /** @return the next smallest number */
	    public int next() {
	        int ans = list.get(count);
	        count++;
	        return ans;
	    }
	    
	    /** @return whether we have a next smallest number */
	    public boolean hasNext() {
	        return count < list.size();
	    }
	}
	
	/*
	 * Morris Traversal
	 * 
	 * https://leetcode.com/problems/binary-search-tree-iterator/discuss/52701/My-Java-Solution-with-O(1)-space-and-O(1)-amortized-time-using-Morris-Tree-Traversal
	 * https://leetcode.com/problems/binary-search-tree-iterator/discuss/52693/My-5ms-JAVA-code-with-explanation-which-beats-95
	 * https://leetcode.com/problems/binary-search-tree-iterator/discuss/52644/Java-with-Morris-method-4ms-99without-stack
	 */
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/binary-search-tree-iterator/discuss/52642/Two-Python-solutions-stack-and-generator
     * https://leetcode.com/problems/binary-search-tree-iterator/discuss/52675/My-python-solution
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/binary-search-tree-iterator/discuss/52519/My-Solution-in-C%2B%2B-in-average-O(1)-time-and-uses-O(h)-memory
     * https://leetcode.com/problems/binary-search-tree-iterator/discuss/52716/Using-stack-with-explanations
     * https://leetcode.com/problems/binary-search-tree-iterator/discuss/52705/Morris-traverse-solution
     */

	/**
	 * Your BSTIterator object will be instantiated and called as such:
	 * BSTIterator obj = new BSTIterator(root);
	 * int param_1 = obj.next();
	 * boolean param_2 = obj.hasNext();
	 */

}
