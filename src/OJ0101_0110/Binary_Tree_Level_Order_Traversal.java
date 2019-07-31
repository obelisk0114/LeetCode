package OJ0101_0110;

/*
 * https://openhome.cc/Gossip/Java/Queue.html
 */

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Binary_Tree_Level_Order_Traversal {
	private class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;
		TreeNode(int x) { val = x; }
	}
	
	public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null)
            return res;
        
        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.offerLast(root);
        while (!queue.isEmpty()) {
            List<Integer> list = new ArrayList<>();
            int size = queue.size();
            
            for (int i = 0; i < size; i++) {
                TreeNode cur = queue.pollFirst();
                list.add(cur.val);
                
                if (cur.left != null)
                    queue.offerLast(cur.left);
                if (cur.right != null)
                    queue.offerLast(cur.right);
            }
            
            res.add(list);
        }
        return res;
    }
	
	public List<List<Integer>> levelOrder2(TreeNode root) {
		List<List<Integer>> result = new ArrayList<List<Integer>>();
		if (root == null) {  
            return result;  
        }
		Queue<TreeNode> store = new LinkedList<TreeNode>();  // Next level
        store.offer(root);
        int len = store.size();  
        List<Integer> curList = new ArrayList<Integer>();  
        while (!store.isEmpty()) {  
            TreeNode temp = store.poll();  
            curList.add(temp.val);  
            if (temp.left != null) {  
                store.offer(temp.left);  
            }  
            if (temp.right != null) {  
                store.offer(temp.right);  
            }  
            len--;                    // Decrease 1 by traverse every node
            if (len == 0) {           // The end of this level
                len = store.size();   // Next level length
                result.add(new ArrayList<Integer>(curList));  
                curList = new ArrayList<Integer>();  
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
		Binary_Tree_Level_Order_Traversal levelOrder = new Binary_Tree_Level_Order_Traversal();
		
		Binary_Tree_Level_Order_Traversal.TreeNode p1 = levelOrder.new TreeNode(1);
		Binary_Tree_Level_Order_Traversal.TreeNode p2 = levelOrder.new TreeNode(2);
		Binary_Tree_Level_Order_Traversal.TreeNode p3 = levelOrder.new TreeNode(3);
		Binary_Tree_Level_Order_Traversal.TreeNode p4 = levelOrder.new TreeNode(4);
		Binary_Tree_Level_Order_Traversal.TreeNode p5 = levelOrder.new TreeNode(5);
		Binary_Tree_Level_Order_Traversal.TreeNode p6 = levelOrder.new TreeNode(6);
		Binary_Tree_Level_Order_Traversal.TreeNode p7 = levelOrder.new TreeNode(7);
		Binary_Tree_Level_Order_Traversal.TreeNode p8 = levelOrder.new TreeNode(8);
		Binary_Tree_Level_Order_Traversal.TreeNode p9 = levelOrder.new TreeNode(9);
		
		p1.left = p2;     p1.right = p3;     p2.left = p4;     p2.right = p5;
		p3.left = p6;     p3.right = p7;     p4.left = p8;     p5.right = p9;
		
		levelOrder.printTraversal(levelOrder.levelOrder2(p1));
	}

}
