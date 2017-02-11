package OJ091_100;

/*
 * Iteration in-order traversal
 * https://discuss.leetcode.com/topic/7526/my-java-inorder-iteration-solution
 * 
 * https://discuss.leetcode.com/topic/46016/learn-one-iterative-inorder-traversal-apply-it-to-multiple-tree-questions-java-solution
 */

import definition.TreeNode;
import java.util.LinkedList;
import java.util.Stack;

public class Validate_Binary_Search_Tree {
	// https://discuss.leetcode.com/topic/7179/my-simple-java-solution-in-3-lines
	// Left subtree must be smaller than root.
	// Right subtree must be larger than root.
	public boolean isValidBST(TreeNode root) {
		return isValidBST(root, Long.MIN_VALUE, Long.MAX_VALUE);
	}

	public boolean isValidBST(TreeNode root, long minValue, long maxValue) {
		if (root == null) {
			return true;
		}
		if (root.val >= maxValue || root.val <= minValue) {
			return false;
		}
		return isValidBST(root.left, minValue, root.val) && isValidBST(root.right, root.val, maxValue);
	}
	
	// Use null to replace min & max value
	public boolean isValid_BST(TreeNode root) {
	    return isValid_BST(root, null, null);
	}

	public boolean isValid_BST(TreeNode root, Integer min, Integer max) {
	    if(root == null) return true;
	    if(min != null && root.val <= min) return false;
	    if(max != null && root.val >= max) return false;
	    
	    return isValid_BST(root.left, min, root.val) && isValid_BST(root.right, root.val, max);
	}
	
	// Use iteration
	private boolean isValidBST_iteration(TreeNode root) {
		if (root == null)
			return true;
		Stack<TreeNode> stack = new Stack<TreeNode>();
		TreeNode pre = null;
		while (root != null || !stack.isEmpty()) {
			while (root != null) {
				stack.push(root);
				root = root.left;
			}
			root = stack.pop();
			if (pre != null && root.val <= pre.val)
				return false;
			pre = root;
			root = root.right;
		}
		return true;
	}

	// Combine recursion and in-order traversal
	LinkedList<TreeNode> store = new LinkedList<TreeNode>();
	private boolean isValidBST2(TreeNode root) {
		if (root == null) {
			return true;
		}
		if (root.left != null) {			
			if (root.left.val >= root.val) {
				return false;
			}
		}
		if (root.right != null) {			
			if (root.right.val <= root.val) {
				return false;
			}
		}
		
		// DFS in-order traversal
		if (isValidBST2(root.left)) {			
			if (!store.isEmpty()) {			
				if (store.getLast().val < root.val) {
					store.addLast(root);
					return isValidBST2(root.right);
				}
			}
			else {
				store.addLast(root);
				return isValidBST2(root.right);			
			}
		}
		return false;
	}
	
	// In-order traversal the BST and check whether it is sorted
	LinkedList<TreeNode> store2 = new LinkedList<TreeNode>();
	private void fillStore(TreeNode root) {
        if (root.left != null) {
            fillStore(root.left);
        }
        store2.addLast(root);
        if (root.right != null) {
            fillStore(root.right);
        }
    }
    private boolean isValidBST_InOrderTraversal(TreeNode root) {
        if (root == null) {
            return true;
        }
        
        fillStore(root);
        int first = store2.removeFirst().val;
        while(!store2.isEmpty()) {
            int second = store2.removeFirst().val;
            if (second <= first) {
                return false;
            }
            first = second;
        }
        return true;
    }
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Validate_Binary_Search_Tree validBST = new Validate_Binary_Search_Tree();
		
		TreeNode n1 = new TreeNode(10);
		TreeNode n2 = new TreeNode(5);
		TreeNode n3 = new TreeNode(15);
		//TreeNode n4 = new TreeNode(15);
		//TreeNode n5 = new TreeNode(22);
		TreeNode n6 = new TreeNode(6);
		TreeNode n7 = new TreeNode(20);
		
		n1.left = n2;     n1.right = n3;    // n2.left = n4;     n2.right = n5;
		n3.left = n6;     n3.right = n7;
		
		System.out.println("Use MIN.value & MAX.value : " + validBST.isValidBST(n1));
		System.out.println("Use null : " + validBST.isValid_BST(n1));
		System.out.println("Recursion + in-order traversal : " + validBST.isValidBST2(n1));
		System.out.println("In-order traversal : " + validBST.isValidBST_InOrderTraversal(n1));
		System.out.println("Iteration : " + validBST.isValidBST_iteration(n1));
	}
}
