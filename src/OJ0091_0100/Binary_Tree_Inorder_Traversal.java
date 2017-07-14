package OJ0091_0100;

import definition.TreeNode;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Stack;

public class Binary_Tree_Inorder_Traversal {
	// recursion
	private List<Integer> inorderTraversal_recursive(TreeNode root) {
		List<Integer> inorder = new LinkedList<Integer>();
		if (root == null) {
			return inorder;
		}
		return inorderTraversal_recursive(root, inorder);
	}
	
	private List<Integer> inorderTraversal_recursive(TreeNode root, List<Integer> ll) {
		if (root.left != null) {			
			inorderTraversal_recursive(root.left, ll);
		}
		ll.add(root.val);
		if (root.right != null) {			
			inorderTraversal_recursive(root.right, ll);
		}
		return ll;
	}
	
	// Iteration
	private List<Integer> inorderTraversal(TreeNode root) {
		List<Integer> list = new ArrayList<Integer>();
		if (root == null) {
			return list;
		}

	    Stack<TreeNode> stack = new Stack<TreeNode>();
	    TreeNode cur = root;
	    
	    while(cur!=null || !stack.empty()){
	        while(cur!=null){
	            stack.add(cur);
	            cur = cur.left;
	        }
	        cur = stack.pop();
	        list.add(cur.val);
	        cur = cur.right;
	    }

	    return list;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Binary_Tree_Inorder_Traversal BTinorder = new Binary_Tree_Inorder_Traversal();
		
		TreeNode n1 = new TreeNode(10);
		TreeNode n2 = new TreeNode(5);
		TreeNode n3 = new TreeNode(15);
		//TreeNode n4 = new TreeNode(15);
		//TreeNode n5 = new TreeNode(22);
		TreeNode n6 = new TreeNode(6);
		TreeNode n7 = new TreeNode(20);
		
		n1.left = n2;     n1.right = n3;    // n2.left = n4;     n2.right = n5;
		n3.left = n6;     n3.right = n7;
		
		List<Integer> ll = BTinorder.inorderTraversal_recursive(n1);
		System.out.println("inorderTraversal_recursive : ");
		while (!ll.isEmpty()) {
			System.out.print(ll.remove(0) + " ");
		}
		
		List<Integer> l2 = BTinorder.inorderTraversal(n1);
		System.out.println("\ninorderTraversal : ");
		while (!l2.isEmpty()) {
			System.out.print(l2.remove(0) + " ");
		}

	}

}
