package OJ0111_0120;

import definition.TreeNode;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Flatten_Binary_Tree_to_Linked_List {
	// by myself
	public void flatten_self(TreeNode root) {
        if (root == null)
            return;
        
        LinkedList<TreeNode> stack = new LinkedList<>();
        TreeNode cur = root;
        while (cur != null) {
            if (cur.right != null) {
                stack.offerLast(cur.right);
            }
            
            if (cur.left != null) {
                cur.right = cur.left;
            }
            else {
                cur.right = stack.pollLast();
            }
            cur.left = null;
            cur = cur.right;
        }
    }
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/flatten-binary-tree-to-linked-list/discuss/268085/Divide-and-conquer-postorder-solution
	 * 
	 * 1. flatten left subtree, return its tail node 'leftTail'
	 * 2. flatten right subtree, return its tail node 'rightTail'
	 * 3. connect root with leftList.head and then leftList.tail to rightList.head.
	 * 4. return the tail node of this flattened tree (return rightTail if it isn't 
	 *    null, else return leftTail if it isn't null, else return root)
	 * 
	 * The key is to find the head and tail of each converted list. Head is always 
	 * root, we only need to figure out how to get the tail. The idea is that if the 
	 * tail of right subtree is not null, we've got the answer, otherwise, tail of 
	 * left subtree, or root.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/flatten-binary-tree-to-linked-list/discuss/36980/Clean-straight-forward-postorder-solution!-Easy-to-understand!
	 */
	public void flatten_getTail(TreeNode root) {
		flattenAndGetTail(root);
	}

	public TreeNode flattenAndGetTail(TreeNode root) {
		if (root == null)
			return null;

		TreeNode leftTail = flattenAndGetTail(root.left);
		TreeNode rightTail = flattenAndGetTail(root.right);

		if (leftTail != null) {
			leftTail.right = root.right;
			root.right = root.left;
			root.left = null;
		}

		if (rightTail != null)
			return rightTail;
		if (leftTail != null)
			return leftTail;
		return root;
	}
	
	/*
	 * https://leetcode.com/problems/flatten-binary-tree-to-linked-list/discuss/36991/Accepted-simple-Java-solution-iterative
	 */
	public void flatten_stack(TreeNode root) {
		if (root == null)
			return;
		
		Stack<TreeNode> stk = new Stack<TreeNode>();
		stk.push(root);
		while (!stk.isEmpty()) {
			TreeNode curr = stk.pop();
			
			if (curr.right != null)
				stk.push(curr.right);
			if (curr.left != null)
				stk.push(curr.left);
			if (!stk.isEmpty())
				curr.right = stk.peek();
			
			curr.left = null;        // don't forget this!!
		}
	}
	
	/*
	 * https://leetcode.com/problems/flatten-binary-tree-to-linked-list/discuss/37010/Share-my-simple-NON-recursive-solution-O(1)-space-complexity!/35136
	 * 
	 * Attach original right as the right child of the rightmost node of left subtree
	 * and set original left as new right child and then making the left one null.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/flatten-binary-tree-to-linked-list/discuss/37010/Share-my-simple-NON-recursive-solution-O(1)-space-complexity!/146762
	 * 
	 * Other code:
	 * https://leetcode.com/problems/flatten-binary-tree-to-linked-list/discuss/37050/Java-Solution-Recursive-and-Non-Recursive
	 */
	public void flatten_Morris(TreeNode root) {
		TreeNode cur = root;
		while (cur != null) {
			if (cur.left != null) {
				TreeNode last = cur.left;
				while (last.right != null)
					last = last.right;
				last.right = cur.right;
				cur.right = cur.left;
				cur.left = null;
			}
			cur = cur.right;
		}
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/flatten-binary-tree-to-linked-list/discuss/184711/Java-Solution-Iterative-Using-Queue
	 */
	public void flatten_queue_preOrder(TreeNode root) {
		if (root == null)
			return;

		Queue<TreeNode> q = new LinkedList<>();
		populateQueue(q, root);

		TreeNode curr = q.poll();
		while (q.size() > 0) {
			curr.right = q.poll();
			curr.left = null;
			curr = curr.right;
		}
	}

	private void populateQueue(Queue<TreeNode> q, TreeNode node) {
		if (node == null)
			return;

		q.add(node);
		populateQueue(q, node.left);
		populateQueue(q, node.right);
	}
	
	/*
	 * https://leetcode.com/problems/flatten-binary-tree-to-linked-list/discuss/36987/Straightforward-Java-Solution
	 * 
	 * 1. flatten left subtree
	 * 2. flatten right subtree
	 * 3. concatenate root -> left flatten subtree -> right flatten subtree
	 * (don't forget to set left child to null)
	 * 
	 * Rf :
	 * https://leetcode.com/problems/flatten-binary-tree-to-linked-list/discuss/37223/Share-my-accepted-recursive-solution-with-comments-Java
	 */
	public void flatten_go_right(TreeNode root) {
		if (root == null)
			return;

		TreeNode left = root.left;
		TreeNode right = root.right;

		flatten_go_right(left);
		flatten_go_right(right);

		root.left = null;
		
		// step 1: concatenate root with left flatten subtree
		root.right = left;
		
		// step 2: move to the end of new added flatten subtree
		TreeNode cur = root;
		while (cur.right != null)
			cur = cur.right;
		
		// step 3: concatenate left flatten subtree with flatten right subtree
		cur.right = right;
	}
	
	/*
	 * The following variable and function are from this link.
	 * https://leetcode.com/problems/flatten-binary-tree-to-linked-list/discuss/37244/Can-you-improve-upon-my-recursive-approach
	 * 
	 * Storing the last visited pre-order traversal node in "lastVisited" TreeNode, 
	 * and re-assigning its children.
	 * 
	 * Other code:
	 * https://leetcode.com/problems/flatten-binary-tree-to-linked-list/discuss/37074/Preorder-vs-Postorder
	 */
	private TreeNode lastVisited = null;

	public void flattenHelper(TreeNode root) {
		if (root == null)
			return;

		TreeNode savedRight = root.right;
		if (lastVisited != null) {
			lastVisited.left = null;
			lastVisited.right = root;
		}
		lastVisited = root;

		flattenHelper(root.left);
		flattenHelper(savedRight);
	}
	
	/*
	 * The following variable and function are from this link.
	 * https://leetcode.com/problems/flatten-binary-tree-to-linked-list/discuss/36977/My-short-post-order-traversal-Java-solution-for-share
	 * 
	 * Traversing order after flattening is pre order traversal in (root, left, right)
	 * If we traverse the flattened tree in the reverse way, we would notice that 
	 * [6->5->4->3->2->1] is in (right, left, root) order of the original tree.
	 * post order traversal is (left, right, root), change the order of left and right
	 * then set each node's right pointer as the previous one in [6->5->4->3->2->1], 
	 * as such the right pointer behaves similar to a link in the flattened tree and 
	 * set the left child as null before the end of one recursion
	 * 
	 * Use DFS to search the right subtree first, and construct the tree from bottom 
	 * to top
	 * 
	 * Rf :
	 * https://leetcode.com/problems/flatten-binary-tree-to-linked-list/discuss/36977/My-short-post-order-traversal-Java-solution-for-share/150699
	 * https://leetcode.com/problems/flatten-binary-tree-to-linked-list/discuss/36977/My-short-post-order-traversal-Java-solution-for-share/35035
	 * https://leetcode.com/problems/flatten-binary-tree-to-linked-list/discuss/36977/My-short-post-order-traversal-Java-solution-for-share/35027
	 * 
	 * Other code:
	 * https://leetcode.com/problems/flatten-binary-tree-to-linked-list/discuss/36977/My-short-post-order-traversal-Java-solution-for-share/184888
	 */
	private TreeNode prev_reverse_preOrder = null;

	public void flatten_reverse_preOrder(TreeNode root) {
		if (root == null)
			return;
		
		flatten_reverse_preOrder(root.right);
		flatten_reverse_preOrder(root.left);
		
		root.right = prev_reverse_preOrder;
		root.left = null;
		
		prev_reverse_preOrder = root;
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/flatten-binary-tree-to-linked-list/discuss/37065/simple-dfs-python-solution
     * https://leetcode.com/problems/flatten-binary-tree-to-linked-list/discuss/36984/An-inorder-python-solution
     * https://leetcode.com/problems/flatten-binary-tree-to-linked-list/discuss/37154/8-lines-of-python-solution-(reverse-preorder-traversal)
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/flatten-binary-tree-to-linked-list/discuss/37010/Share-my-simple-NON-recursive-solution-O(1)-space-complexity!
     * https://leetcode.com/problems/flatten-binary-tree-to-linked-list/discuss/37182/My-recursive-solution-is-easy-and-clean!
     * https://leetcode.com/problems/flatten-binary-tree-to-linked-list/discuss/37000/8ms-Non-recursive-No-stack-C%2B%2B-solution
     * https://leetcode.com/problems/flatten-binary-tree-to-linked-list/discuss/37039/My-8ms-C%2B%2B-Solution
     */

}
