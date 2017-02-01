package OJ091_100;

public class Same_Tree {
	private class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;
		TreeNode(int x) { val = x; }
	}
	
	public static boolean isSameTree(TreeNode p, TreeNode q) {
		if ((p == null) && (q == null)) {
			return true;
		}
		if ((p == null) || (q == null)) {
			return false;
		}
		if (p.val != q.val) {
			return false;
		}
		return (isSameTree(p.left, q.left) && isSameTree(p.right, q.right));
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Same_Tree tree = new Same_Tree();
		
		Same_Tree.TreeNode p1 = tree.new TreeNode(1);
		Same_Tree.TreeNode p2 = tree.new TreeNode(2);
		Same_Tree.TreeNode p3 = tree.new TreeNode(3);
		Same_Tree.TreeNode p4 = tree.new TreeNode(4);
		Same_Tree.TreeNode p5 = tree.new TreeNode(5);
		Same_Tree.TreeNode p6 = tree.new TreeNode(6);
		Same_Tree.TreeNode p7 = tree.new TreeNode(7);
		
		p1.left = p2;   p1.right = p3;   p2.left = p4;   p2.right = p5;
		p3.left = p6;   p3.right = p7;
		
		Same_Tree.TreeNode q1 = tree.new TreeNode(1);
		Same_Tree.TreeNode q2 = tree.new TreeNode(2);
		Same_Tree.TreeNode q3 = tree.new TreeNode(5);
		Same_Tree.TreeNode q4 = tree.new TreeNode(4);
		Same_Tree.TreeNode q5 = tree.new TreeNode(5);
		Same_Tree.TreeNode q6 = tree.new TreeNode(6);
		Same_Tree.TreeNode q7 = tree.new TreeNode(7);
		
		q1.left = q2;   q1.right = q3;   q2.left = q4;   q2.right = q5;
		q3.left = q6;   q3.right = q7;
		
		System.out.println(isSameTree(p1, q1));

	}

}
