package OJ101_110;

import java.util.List;
import java.util.LinkedList;

public class Binary_Tree_Zigzag_Level_Order_Traversal {
	private class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;
		TreeNode(int x) { val = x; }
	}
	
	List<List<Integer>> zigzagLevelOrder(TreeNode root) {
		List<List<Integer>> result = new LinkedList<List<Integer>>();
		if (root == null) {  
            return result;  
        }
		LinkedList<TreeNode> queue = new LinkedList<TreeNode>();    // Next level
		
		queue.addLast(root);
		int len = queue.size();
		boolean right = false;
		LinkedList<Integer> tmp = new LinkedList<Integer>();
		while (!queue.isEmpty()) {
			TreeNode current = queue.removeFirst();
			if (right) {
				tmp.addFirst(current.val);			// From right to left	
			}
			else {
				tmp.addLast(current.val);           // From left to right
			}
			if (current.left != null) {
				queue.addLast(current.left);
			}
			if (current.right != null) {
				queue.addLast(current.right);
			}				
			len--;                      // Decrease 1 by traverse every node
			
			if (len == 0) {             // The end of this level
				len = queue.size();     // Next level length
				right = !right;         // Change direction of next level
				result.add(tmp);
				tmp = new LinkedList<Integer>();
			}
		}
		return result;
	}
	
	void printTraversal(List<List<Integer>> treenode) {
		for (int i = 0; i < treenode.size(); i++) {
			for (int j = 0; j < treenode.get(i).size(); j++) {
				int target = treenode.get(i).get(j);
				System.out.print(target + " ");
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Binary_Tree_Zigzag_Level_Order_Traversal zLevelOrder = new Binary_Tree_Zigzag_Level_Order_Traversal();
		
		Binary_Tree_Zigzag_Level_Order_Traversal.TreeNode p1 = zLevelOrder.new TreeNode(1);
		Binary_Tree_Zigzag_Level_Order_Traversal.TreeNode p2 = zLevelOrder.new TreeNode(2);
		Binary_Tree_Zigzag_Level_Order_Traversal.TreeNode p3 = zLevelOrder.new TreeNode(3);
		Binary_Tree_Zigzag_Level_Order_Traversal.TreeNode p4 = zLevelOrder.new TreeNode(4);
		Binary_Tree_Zigzag_Level_Order_Traversal.TreeNode p5 = zLevelOrder.new TreeNode(5);
		Binary_Tree_Zigzag_Level_Order_Traversal.TreeNode p6 = zLevelOrder.new TreeNode(6);
		Binary_Tree_Zigzag_Level_Order_Traversal.TreeNode p7 = zLevelOrder.new TreeNode(7);
		Binary_Tree_Zigzag_Level_Order_Traversal.TreeNode p8 = zLevelOrder.new TreeNode(8);
		Binary_Tree_Zigzag_Level_Order_Traversal.TreeNode p9 = zLevelOrder.new TreeNode(9);
		
		p1.left = p2;     p1.right = p3;     p2.left = p4;     p2.right = p5;
		p3.left = p6;     p3.right = p7;     p4.left = p8;     p5.right = p9;
		
		zLevelOrder.printTraversal(zLevelOrder.zigzagLevelOrder(p1));

	}

}
