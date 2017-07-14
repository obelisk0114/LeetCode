package OJ0221_0230;

import java.util.ArrayList;
import java.util.LinkedList;

public class Invert_Binary_Tree {
	private class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;
		TreeNode(int x) { val = x; }
	}
	
	public TreeNode invertTree(TreeNode root) {
		if (root == null) {
			return root;			
		}
		TreeNode tmp = invertTree(root.left);
		root.left = invertTree(root.right);
		root.right = tmp;
		return root;
	}
	
	// BFS
	void storeTree(TreeNode treenode) {
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
				if (levelNullNode == (int) Math.pow(2, levelMark)) {
					break;
				}
				queue.addLast(nextLevel);
				list.add(tmp);
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
	
	void printTree(ArrayList<ArrayList<String>> tree) {
		for (int i = 0; i < tree.size(); i++) {
			int Space = (int) Math.pow(2, tree.size() - i) - 1;
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
		Invert_Binary_Tree invert = new Invert_Binary_Tree();
		
		Invert_Binary_Tree.TreeNode p1 = invert.new TreeNode(1);
		Invert_Binary_Tree.TreeNode p2 = invert.new TreeNode(2);
		Invert_Binary_Tree.TreeNode p3 = invert.new TreeNode(3);
		Invert_Binary_Tree.TreeNode p4 = invert.new TreeNode(4);
		Invert_Binary_Tree.TreeNode p5 = invert.new TreeNode(5);
		Invert_Binary_Tree.TreeNode p6 = invert.new TreeNode(6);
		Invert_Binary_Tree.TreeNode p7 = invert.new TreeNode(7);
		Invert_Binary_Tree.TreeNode p8 = invert.new TreeNode(8);
		Invert_Binary_Tree.TreeNode p9 = invert.new TreeNode(9);
		Invert_Binary_Tree.TreeNode p10 = invert.new TreeNode(10);
		Invert_Binary_Tree.TreeNode p11 = invert.new TreeNode(11);
		Invert_Binary_Tree.TreeNode p12 = invert.new TreeNode(12);
		Invert_Binary_Tree.TreeNode p13 = invert.new TreeNode(13);
		Invert_Binary_Tree.TreeNode p14 = invert.new TreeNode(14);
		Invert_Binary_Tree.TreeNode p15 = invert.new TreeNode(15);
		Invert_Binary_Tree.TreeNode p16 = invert.new TreeNode(16);
		Invert_Binary_Tree.TreeNode p17 = invert.new TreeNode(17);
		
		p1.left = p2;     p1.right = p3;     p2.left = p4;     p2.right = p5;
		p3.left = p6;     p3.right = p7;     p4.left = p8;     p5.right = p9;
		p6.left = p10;    p6.right = p11;    p8.right = p12;   p9.left = p13;
		p9.right = p14;   p11.left = p15;    p14.left = p16;   p14.right = p17;
		
		invert.storeTree(p1);
		/*
		ArrayList<ArrayList<String>> test = new ArrayList<ArrayList<String>>();
		ArrayList<String> content1 = new ArrayList<String>();
		content1.add("1");
		ArrayList<String> content2 = new ArrayList<String>();
		content2.add("2");
		content2.add("3");
		ArrayList<String> content3 = new ArrayList<String>();
		content3.add("4");
		content3.add("5");
		content3.add("6");
		content3.add("7");
		ArrayList<String> content4 = new ArrayList<String>();
		content4.add("8");
		content4.add("null");
		content4.add("null");
		content4.add("9");
		content4.add("10");
		content4.add("11");
		content4.add("null");
		content4.add("null");
		ArrayList<String> content5 = new ArrayList<String>();
		content5.add("null");
		content5.add("12");
		for (int i1 = 0; i1 < 2; i1++) {
			content5.add("null");			
			content5.add("null");
		}
		content5.add("13");
		content5.add("14");
		content5.add("null");			
		content5.add("null");
		content5.add("15");
		content5.add("null");
		for (int i2 = 0; i2 < 2; i2++) {			
			content5.add("null");			
			content5.add("null");
		}
		ArrayList<String> content6 = new ArrayList<String>();
		for (int i1 = 0; i1 < 7; i1++) {
			content6.add("null");			
			content6.add("null");
		}
		content6.add("16");
		content6.add("17");
		for (int i1 = 0; i1 < 8; i1++) {
			content6.add("null");			
			content6.add("null");
		}
		
		test.add(content1);
		test.add(content2);
		test.add(content3);
		test.add(content4);
		test.add(content5);
		test.add(content6);
		
		invert.printTree(test);
		*/
	}

}
