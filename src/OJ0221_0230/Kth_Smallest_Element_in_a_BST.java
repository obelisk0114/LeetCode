package OJ0221_0230;

import definition.TreeNode;
import java.util.ArrayList;
import java.util.LinkedList;

public class Kth_Smallest_Element_in_a_BST {
	// Following two functions are DFS recursion.
	private int kthSmallest_DFS_recursive(TreeNode root, int k) {
		if (root == null) {
			return -1;
		}
		ArrayList<Integer> nodeValue = new ArrayList<Integer>();
		kthSmallest_recursive(root, nodeValue);
		return nodeValue.get(k-1);
	}
	
	private void kthSmallest_recursive(TreeNode root, ArrayList<Integer> node) {
		if (root.left != null) {
			kthSmallest_recursive(root.left, node);
		}
		node.add(root.val);
		if (root.right != null) {			
			kthSmallest_recursive(root.right, node);
		}
	} 
	
	// DFS iteration
	private int kthSmallest_DFS_iteration (TreeNode root, int k) {
		if (root == null) {
			return -1;
		}
		LinkedList<TreeNode> stack = new LinkedList<TreeNode>();
		TreeNode cur = root;
		int count = 0;
		while (cur != null || !stack.isEmpty()) {
			while (cur != null) {
				stack.add(cur);
				cur = cur.left;
			}
			cur = stack.removeLast();
			count++;
			if (count == k) {
				return cur.val;
			}
			cur = cur.right;
		}
		
		return -1;
	}
	
	// Binary search
	// https://discuss.leetcode.com/topic/17810/3-ways-implemented-in-java-binary-search-in-order-iterative-recursive
	private int kthSmallest(TreeNode root, int k) {
        int count = countNodes(root.left);
        if (k <= count) {
            return kthSmallest(root.left, k);
        } else if (k > count + 1) {
            return kthSmallest(root.right, k-1-count); // 1 is counted as current node
        }
        
        return root.val;
    }
    
    private int countNodes(TreeNode n) {
        if (n == null) return 0;
        return 1 + countNodes(n.left) + countNodes(n.right);
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Kth_Smallest_Element_in_a_BST kthMinBST = new Kth_Smallest_Element_in_a_BST();
				
		TreeNode n1 = new TreeNode(30);
		TreeNode n2 = new TreeNode(20);
		TreeNode n3 = new TreeNode(40);
		//TreeNode n4 = new TreeNode(15);
		//TreeNode n5 = new TreeNode(22);
		TreeNode n6 = new TreeNode(33);
		TreeNode n7 = new TreeNode(50);
		
		n1.left = n2;     n1.right = n3;    // n2.left = n4;     n2.right = n5;
		n3.left = n6;     n3.right = n7;
		
		int weWant = 3;
		System.out.println("Recursion kth BST : " + kthMinBST.kthSmallest_DFS_recursive(n1, weWant));
		System.out.println("Iteration kth BST : " + kthMinBST.kthSmallest_DFS_iteration(n1, weWant));
		System.out.println("Binary search kth BST : " + kthMinBST.kthSmallest(n1, weWant));

	}

}
