package OJ0101_0110;

import definition.TreeNode;
import java.util.LinkedList;
import java.util.Queue;

public class Symmetric_Tree {
	private boolean isSymmetric(TreeNode root) {
		if (root == null) {
			return true;
		}
		return isMirror(root.left, root.right);
	}
	
	private boolean isMirror(TreeNode p, TreeNode q) {
		if (p == null && q == null) {
			return true;
		}
		if (p == null || q == null) {
			return false;
		}
		if (p.val != q.val) {
			return false;
		}
		return isMirror(p.left, q.right) && isMirror(p.right, q.left);
	}
	
	// Iterative
	private boolean isSymmetric2(TreeNode root) {
		if (root == null) {
			return true;
		}
		Queue<TreeNode> q = new LinkedList<TreeNode>();
		q.add(root);
		q.add(root);
		while (!q.isEmpty()) {
			TreeNode t1 = q.poll();
			TreeNode t2 = q.poll();
			if (t1 == null && t2 == null)
				continue;
			if (t1 == null || t2 == null)
				return false;
			if (t1.val != t2.val)
				return false;
			q.add(t1.left);
			q.add(t2.right);
			q.add(t1.right);
			q.add(t2.left);
		}
		return true;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Symmetric_Tree symmetric = new Symmetric_Tree();
		
		TreeNode n1 = new TreeNode(1);
		TreeNode n2 = new TreeNode(2);
		TreeNode n3 = new TreeNode(2);
		TreeNode n4 = new TreeNode(3);
		TreeNode n5 = new TreeNode(4);
		TreeNode n6 = new TreeNode(4);
		TreeNode n7 = new TreeNode(3);
		
		n1.left = n2;     n1.right = n3;    n2.left = n4;      n2.right = n5;
		n3.left = n6;     n3.right = n7;
		
		System.out.println(symmetric.isSymmetric(n1));
		System.out.println(symmetric.isSymmetric2(n1));

	}

}
