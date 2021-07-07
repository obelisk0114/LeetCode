package OJ0091_0100;

import definition.TreeNode;

import java.util.LinkedList;
import java.util.Deque;
import java.util.Stack;

public class Validate_Binary_Search_Tree {
	// https://discuss.leetcode.com/topic/7179/my-simple-java-solution-in-3-lines
	// Left subtree must be smaller than root.
	// Right subtree must be larger than root.
	public boolean isValidBST_range(TreeNode root) {
		return isValidBST_range(root, Long.MIN_VALUE, Long.MAX_VALUE);
	}

	public boolean isValidBST_range(TreeNode root, long minValue, long maxValue) {
		if (root == null) {
			return true;
		}
		if (root.val >= maxValue || root.val <= minValue) {
			return false;
		}
		
		return isValidBST_range(root.left, minValue, root.val) 
				&& isValidBST_range(root.right, root.val, maxValue);
	}
	
	// Use null to replace min & max value
	public boolean isValid_BST(TreeNode root) {
	    return isValid_BST(root, null, null);
	}

	public boolean isValid_BST(TreeNode root, Integer min, Integer max) {
	    if (root == null) return true;
	    if (min != null && root.val <= min) return false;
	    if (max != null && root.val >= max) return false;
	    
	    return isValid_BST(root.left, min, root.val) 
	    		&& isValid_BST(root.right, root.val, max);
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/validate-binary-search-tree/solution/
	 * Approach 3: Recursive Inorder Traversal
	 * 
	 * DFS Inorder: Left -> Node -> Right
	 * For BST, each element should be smaller than the next one.
	 * 
	 * + Compute inorder traversal list `inorder`.
	 * + Check if each element in `inorder` is smaller than the next one.
	 * 
	 * The last added inorder element is enough to ensure at each step that the tree
	 * is BST (or not). Hence one could merge both steps into one.
	 */
	// We use Integer instead of int as it supports a null value.
	private Integer prev_inOrder;
	
	public boolean isValidBST_inOrder(TreeNode root) {
		prev_inOrder = null;
		return inorder_inOrder(root);
	}
	
	private boolean inorder_inOrder(TreeNode root) {
		if (root == null) {
			return true;
		}
		
		if (!inorder_inOrder(root.left)) {
			return false;
		}
		
		if (prev_inOrder != null && root.val <= prev_inOrder) {
			return false;
		}
		prev_inOrder = root.val;
		
		return inorder_inOrder(root.right);
	}
	
	/*
	 * https://leetcode.com/problems/validate-binary-search-tree/discuss/32112/Learn-one-iterative-inorder-traversal-apply-it-to-multiple-tree-questions-(Java-Solution)
	 * 
	 * Use iteration
	 * 
	 * Other code:
	 * https://leetcode.com/problems/validate-binary-search-tree/discuss/32112/Learn-one-iterative-inorder-traversal-apply-it-to-multiple-tree-questions-(Java-Solution)/122619
	 */
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
	
	/*
	 * https://leetcode.com/problems/validate-binary-search-tree/discuss/32101/My-java-inorder-iteration-solution
	 * 
	 * the idea is to do a inorder Traversal and keep the value
	 */
	public boolean isValidBST_iterative2(TreeNode root) {
		Stack<TreeNode> stack = new Stack<TreeNode>();
		
		TreeNode cur = root;
		TreeNode pre = null;
		while (!stack.isEmpty() || cur != null) {
			if (cur != null) {
				stack.push(cur);
				cur = cur.left;
			} 
			else {
				TreeNode p = stack.pop();
				
				if (pre != null && p.val <= pre.val) {
					return false;
				}
				pre = p;
				cur = p.right;
			}
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
    
    /*
     * The following 3 variables and 2 functions are from this link.
     * https://leetcode.com/problems/validate-binary-search-tree/solution/
     * Approach 2: Iterative Traversal with Valid Range
     */
    private Deque<TreeNode> stack_iterative_range = new LinkedList<>();
    private Deque<Integer> upperLimits_iterative_range = new LinkedList<>();
    private Deque<Integer> lowerLimits_iterative_range = new LinkedList<>();

    public void update_iterative_range(TreeNode root, Integer low, Integer high) {
        stack_iterative_range.add(root);
        lowerLimits_iterative_range.add(low);
        upperLimits_iterative_range.add(high);
    }

	public boolean isValidBST_iterative_range(TreeNode root) {
		Integer low = null, high = null, val;
		update_iterative_range(root, low, high);

		while (!stack_iterative_range.isEmpty()) {
			root = stack_iterative_range.poll();
			low = lowerLimits_iterative_range.poll();
			high = upperLimits_iterative_range.poll();

			if (root == null)
				continue;
			
			val = root.val;
			
			if (low != null && val <= low) {
				return false;
			}
			if (high != null && val >= high) {
				return false;
			}
			
			update_iterative_range(root.right, val, high);
			update_iterative_range(root.left, low, val);
		}
        return true;
    }
	
	// driver
	
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
		
		System.out.println("Use MIN.value & MAX.value : " + validBST.isValidBST_range(n1));
		System.out.println("Use null : " + validBST.isValid_BST(n1));
		System.out.println("Recursion + in-order traversal : " + validBST.isValidBST2(n1));
		System.out.println("In-order traversal : " + validBST.isValidBST_InOrderTraversal(n1));
		System.out.println("Iteration : " + validBST.isValidBST_iteration(n1));
	}
}
