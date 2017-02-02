package OJ091_100;

import java.util.ArrayList;
import java.util.LinkedList;

public class Same_Tree {
	private class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;
		TreeNode(int x) { val = x; }
	}
	
	public boolean isSameTree(TreeNode p, TreeNode q) {
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
	
	public void storeTree(TreeNode treenode) {
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		
		// Add root
		ArrayList<String> first = new ArrayList<String>();
		first.add(Integer.toString(treenode.val));
		list.add(first);
		
		// Construct a queue to record
		TreeNode nextLevel = new TreeNode(-1);    // Mark next level
		TreeNode nullNode = new TreeNode(-2);     // Mark null node
		LinkedList<TreeNode> queue = new LinkedList<TreeNode>();
		queue.addLast(treenode);
		queue.addLast(nextLevel);
		int levelMark = 1;                        // Mark level
		int levelNullNode = 0;                    // Mark null nodes in this level
		
		ArrayList<String> tmp = new ArrayList<String>();
		while (true) {
			TreeNode currentNode = queue.removeFirst();
			if (currentNode.equals(nextLevel)) {
				queue.addLast(nextLevel);
				list.add(tmp);
				if (levelNullNode == (int) Math.pow(2, levelMark)) {
					break;
				}
				tmp = new ArrayList<String>();
				levelMark++;                      // Next level
				levelNullNode = 0;                // Reset null nodes count
				continue;
			}
			if (currentNode.left == null) {
				tmp.add("null");
				queue.addLast(nullNode);
				levelNullNode++;
			}
			else {
				String tobeAdd = Integer.toString(currentNode.left.val); 
				tmp.add(tobeAdd);
				queue.addLast(currentNode.left);
			}
			if (currentNode.right == null) {
				tmp.add("null");
				queue.addLast(nullNode);
				levelNullNode++;
			}
			else {
				String tobeAdd = Integer.toString(currentNode.right.val);
				tmp.add(tobeAdd);
				queue.addLast(currentNode.right);
			}	
		}
		printTree(list);
	}
	
	public void printTree(ArrayList<ArrayList<String>> tree) {
		for (int i = 0; i < tree.size(); i++) {
			int Space = (int) Math.pow(2, tree.size() - 1 - i) - 1;
			for (int j = 0; j < tree.get(i).size(); j++) {
				for (int run = 0; run < 2; run++) {
					for (int k = 0; k < Space; k++) {
						System.out.print(" ");
					}
					
					if (j == 0)
						break;
				}
				String realOut = tree.get(i).get(j);
				if (realOut.equals("null")) {
					System.out.print("  ");
				}
				else {
					System.out.printf("%2s", realOut);
				}
			}
			System.out.println();
		}
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
		
		System.out.println("Tree p : \n");
		tree.storeTree(p1);
		System.out.println("Tree q : \n");
		tree.storeTree(q1);
		System.out.println(tree.isSameTree(p1, q1));

	}

}
