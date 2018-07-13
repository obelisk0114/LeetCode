package OJ0141_0150;

import definition.TreeNode;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Stack;

public class Binary_Tree_Postorder_Traversal {
	// The following 2 functions are by myself.
	public List<Integer> postorderTraversal_recursive(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        recursive(root, list);
        return list;
    }
    
    private void recursive(TreeNode root, List<Integer> list) {
        if (root == null)
            return;
        
        recursive(root.left, list);
        recursive(root.right, list);
        list.add(root.val);
    }
	
	// https://leetcode.com/problems/binary-tree-postorder-traversal/discuss/45551/Preorder-Inorder-and-Postorder-Iteratively-Summarization
	public List<Integer> postorderTraversal(TreeNode root) {
		LinkedList<Integer> result = new LinkedList<>();
		Deque<TreeNode> stack = new ArrayDeque<>();
		TreeNode p = root;
		while (!stack.isEmpty() || p != null) {
			if (p != null) {
				stack.push(p);
				result.addFirst(p.val);  // Reverse the process of preorder
				p = p.right;             // Reverse the process of preorder
			} 
			else {
				TreeNode node = stack.pop();
				p = node.left;           // Reverse the process of preorder
			}
		}
		return result;
	}
	
	/*
	 * https://leetcode.com/problems/binary-tree-postorder-traversal/discuss/45793/A-Java-Simple-Solution-Based-on-Preorder-Traversal
	 * 
	 * Postorder traversal, which is in Left-Right-Root order. We can observe that the 
	 * preorder traversal is in Root-Left-Right order, which means if we swap the order 
	 * of left and right subtree when pushing into stack, we'll get Root-Right-Left, 
	 * a new traversal.
	 * 
	 * It is just the opposite way of postorder one.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/binary-tree-postorder-traversal/discuss/45559/My-Accepted-code-with-explaination.-Does-anyone-have-a-better-idea
	 * https://leetcode.com/problems/binary-tree-postorder-traversal/discuss/45556/Java-simple-and-clean
	 * https://leetcode.com/problems/binary-tree-postorder-traversal/discuss/45621/Preorder-Inorder-and-Postorder-Traversal-Iterative-Java-Solution
	 */
	public List<Integer> postorderTraversal2(TreeNode root) {
		LinkedList<Integer> result = new LinkedList<Integer>();
		if (root == null)
			return result;

		Stack<TreeNode> stack = new Stack<TreeNode>();
		stack.push(root);

		while (!stack.isEmpty()) {
			root = stack.pop();
			result.addFirst(root.val);
			
			if (root.left != null) {
				stack.push(root.left);
			}
			if (root.right != null) {
				stack.push(root.right);
			}
		}
		return result;
	}
	
	// https://leetcode.com/problems/binary-tree-postorder-traversal/discuss/45803/Java-solution-using-two-stacks
	public List<Integer> postorderTraversal_2Stack(TreeNode root) {
		List<Integer> res = new ArrayList<Integer>();

		if (root == null)
			return res;

		Stack<TreeNode> s1 = new Stack<TreeNode>();
		Stack<TreeNode> s2 = new Stack<TreeNode>();

		s1.push(root);

		while (!s1.isEmpty()) {
			TreeNode node = s1.pop();
			s2.push(node);

			if (node.left != null)
				s1.push(node.left);

			if (node.right != null)
				s1.push(node.right);
		}

		while (!s2.isEmpty())
			res.add(s2.pop().val);

		return res;
	}
	
	/*
	 * https://leetcode.com/problems/binary-tree-postorder-traversal/discuss/45674/Accepted-tiny-Java-solution.-Only-left-hand-children-in-stack.
	 * 
	 * 1. In this code I push only left-hand children in to the stack.
	 * 2. To store result I use LinkedList and addFirst() method of it.
	 *    With such approach we can save on reverse the result.
	 */
	public List<Integer> postorderTraversal_Left_Stack(TreeNode node) {
		LinkedList<Integer> result = new LinkedList<Integer>();
		Stack<TreeNode> leftChildren = new Stack<TreeNode>();
		while (node != null) {
			result.addFirst(node.val);
			if (node.left != null) {
				leftChildren.push(node.left);
			}
			
			node = node.right;
			
			if (node == null && !leftChildren.isEmpty()) {
				node = leftChildren.pop();
			}
		}
		return result;
	}
	
	// https://leetcode.com/problems/binary-tree-postorder-traversal/discuss/45550/0-ms-Clear-C++-solutions-iterative-recursive-Morris-traversal-(3-different-solutions!)
	
	// https://leetcode.com/problems/binary-tree-postorder-traversal/discuss/45866/My-accepted-JAVA-solution

}
