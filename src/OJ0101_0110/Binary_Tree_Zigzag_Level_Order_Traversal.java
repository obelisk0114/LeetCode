package OJ0101_0110;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Stack;

public class Binary_Tree_Zigzag_Level_Order_Traversal {
	private class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;
		TreeNode(int x) { val = x; }
	}
	
	/*
	 * https://leetcode.com/problems/binary-tree-zigzag-level-order-traversal/discuss/33814/A-concise-and-easy-understanding-Java-solution
	 * 
	 * Use a queue to implement BFS. Each time when I poll a node, I add this node 
	 * value to level. I use a variable order to indicate whether add from left to 
	 * right or right to left. If order == true, it is from left to right; 
	 * if order == false, it is from right to left.
	 * And from right to left just need to use ArrayList.add(0, value) method
	 * 
	 * Rf :
	 * https://leetcode.com/problems/binary-tree-zigzag-level-order-traversal/discuss/33815/My-accepted-JAVA-solution/32401
	 * https://leetcode.com/problems/binary-tree-zigzag-level-order-traversal/discuss/33814/A-concise-and-easy-understanding-Java-solution/245037
	 * https://leetcode.com/problems/binary-tree-zigzag-level-order-traversal/discuss/33814/A-concise-and-easy-understanding-Java-solution/32380
	 */
	public List<List<Integer>> zigzagLevelOrder_add(TreeNode root) {
		List<List<Integer>> res = new ArrayList<>();
		if (root == null)
			return res;

		Queue<TreeNode> q = new LinkedList<>();
		q.add(root);
		boolean order = true;

		while (!q.isEmpty()) {
			List<Integer> tmp = new ArrayList<>();
			int size = q.size();
			for (int i = 0; i < size; i++) {
				TreeNode n = q.poll();
				if (order) {
					tmp.add(n.val);
				} 
				else {
					tmp.add(0, n.val);
				}
				
				if (n.left != null)
					q.add(n.left);
				if (n.right != null)
					q.add(n.right);
			}
			res.add(tmp);
			order = !order;
		}
		return res;
	}

	// by myself
	public List<List<Integer>> zigzagLevelOrder_self(TreeNode root) {
        List<List<Integer>> ans = new ArrayList<>();
        if (root == null)
            return ans;
        
        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.offerLast(root);
        
        boolean reverse = false;
        while (!queue.isEmpty()) {
            List<Integer> list = new ArrayList<>();
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode cur = queue.pollFirst();
                list.add(cur.val);
                
                if (cur.left != null) {
                    queue.offerLast(cur.left);
                }
                if (cur.right != null) {
                    queue.offerLast(cur.right);
                }
            }
            
            if (reverse) {
                Collections.reverse(list);
            }
            ans.add(list);
            reverse = !reverse;
        }
        return ans;
    }
	
	/*
	 * https://leetcode.com/problems/binary-tree-zigzag-level-order-traversal/discuss/33815/My-accepted-JAVA-solution/189559
	 * 
	 * Keep changing the direction.
	 */
	public List<List<Integer>> zigzagLevelOrder_changing_direction(TreeNode root) {
		Deque<TreeNode> dq = new ArrayDeque<>();
		if (root != null)
			dq.add(root);
		
		List<List<Integer>> res = new ArrayList<>();
		boolean dir = true;
		while (!dq.isEmpty()) {
			List<Integer> level = new ArrayList<>();
			int pop_num = dq.size();
			for (int i = 0; i < pop_num; ++i) {
				TreeNode tmp = null;
				if (dir) {
					tmp = dq.pollFirst();
					if (tmp.left != null)
						dq.addLast(tmp.left);
					if (tmp.right != null)
						dq.addLast(tmp.right);
				} 
				else {
					tmp = dq.pollLast();
					if (tmp.right != null)
						dq.addFirst(tmp.right);
					if (tmp.left != null)
						dq.addFirst(tmp.left);
				}
				level.add(tmp.val);
			}
			
			res.add(level);
			dir = !dir;
		}
		return res;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/binary-tree-zigzag-level-order-traversal/discuss/33815/My-accepted-JAVA-solution
	 * 
	 * Other code:
	 * https://leetcode.com/problems/binary-tree-zigzag-level-order-traversal/discuss/34033/Java-iterative-and-recursive-solutions.
	 */
	public List<List<Integer>> zigzagLevelOrder_recursive(TreeNode root) {
		List<List<Integer>> sol = new ArrayList<>();
		travel(root, sol, 0);
		return sol;
	}

	private void travel(TreeNode curr, List<List<Integer>> sol, int level) {
		if (curr == null)
			return;

		if (sol.size() == level) {
			List<Integer> newLevel = new LinkedList<>();
			sol.add(newLevel);
		}

		List<Integer> collection = sol.get(level);
		if (level % 2 == 0)
			collection.add(curr.val);
		else
			collection.add(0, curr.val);

		travel(curr.left, sol, level + 1);
		travel(curr.right, sol, level + 1);
	}
	
	/*
	 * https://leetcode.com/problems/binary-tree-zigzag-level-order-traversal/discuss/33904/JAVA-Double-Stack-Solution
	 * 
	 * Because of the FILO property of stack, when you fill up a stack with one level 
	 * of node, the output order of stack is reversed order of the input order. So you 
	 * do it level by level, you will get zigzag order.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/binary-tree-zigzag-level-order-traversal/discuss/33904/JAVA-Double-Stack-Solution/32444
	 * https://leetcode.com/problems/binary-tree-zigzag-level-order-traversal/discuss/33904/JAVA-Double-Stack-Solution/32447
	 * 
	 * Other code:
	 * https://leetcode.com/problems/binary-tree-zigzag-level-order-traversal/discuss/34162/My-AC-Java-code
	 */
	public List<List<Integer>> zigzagLevelOrder_2_stack(TreeNode root) {
		TreeNode c = root;
		List<List<Integer>> ans = new ArrayList<List<Integer>>();
		if (c == null)
			return ans;
		
		Stack<TreeNode> s1 = new Stack<TreeNode>();
		Stack<TreeNode> s2 = new Stack<TreeNode>();
		s1.push(root);
		
		// while(!s1.isEmpty()) is enough. when s1 is empty, s2 must be empty
		while (!s1.isEmpty() || !s2.isEmpty()) {
			List<Integer> tmp = new ArrayList<Integer>();
			while (!s1.isEmpty()) {
				c = s1.pop();
				tmp.add(c.val);
				
				if (c.left != null)
					s2.push(c.left);
				if (c.right != null)
					s2.push(c.right);
			}
			ans.add(tmp);
			
			tmp = new ArrayList<Integer>();
			while (!s2.isEmpty()) {
				c = s2.pop();
				tmp.add(c.val);
				
				if (c.right != null)
					s1.push(c.right);
				if (c.left != null)
					s1.push(c.left);
			}
			
			if (!tmp.isEmpty())
				ans.add(tmp);
		}
		return ans;
	}

    // 
	List<List<Integer>> zigzagLevelOrder_whileCount(TreeNode root) {
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
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/binary-tree-zigzag-level-order-traversal/discuss/33834/Python-simple-BFS
     * https://leetcode.com/problems/binary-tree-zigzag-level-order-traversal/discuss/33832/8-liner-Python
     * https://leetcode.com/problems/binary-tree-zigzag-level-order-traversal/discuss/33833/Simple-and-clear-python-solution-with-explain
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/binary-tree-zigzag-level-order-traversal/discuss/33825/c%2B%2B-5ms-version%3A-one-queue-and-without-reverse-operation-by-using-size-of-each-level
     * https://leetcode.com/problems/binary-tree-zigzag-level-order-traversal/discuss/33931/Clear-iterative-solution-with-deque-no-reverse
     * https://leetcode.com/problems/binary-tree-zigzag-level-order-traversal/discuss/174644/c%2B%2B-Easy-to-understand-one-approach-to-solve-Zigzag-and-Level-order-traversal
     */
	
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
		
		zLevelOrder.printTraversal(zLevelOrder.zigzagLevelOrder_self(p1));

	}

}
