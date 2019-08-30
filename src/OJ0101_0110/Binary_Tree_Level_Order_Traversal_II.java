package OJ0101_0110;

import definition.TreeNode;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Binary_Tree_Level_Order_Traversal_II {
	/*
	 * by myself
	 * 
	 * Other code:
	 * https://leetcode.com/problems/binary-tree-level-order-traversal-ii/discuss/35026/JAVA-SOLUTION-WITH-LINKEDLIST
	 */
	public List<List<Integer>> levelOrderBottom_self(TreeNode root) {
        List<List<Integer>> ans = new ArrayList<>();
        if (root == null) {
            return ans;
        }
        
        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.offerLast(root);
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
            ans.add(0, list);
        }
        return ans;
    }
	
	/*
	 * https://leetcode.com/problems/binary-tree-level-order-traversal-ii/discuss/35089/Java-Solution.-Using-Queue
	 * 
	 * Rf :
	 * https://leetcode.com/problems/binary-tree-level-order-traversal-ii/discuss/34981/My-DFS-and-BFS-java-solution
	 */
	public List<List<Integer>> levelOrderBottom2(TreeNode root) {
		List<List<Integer>> result = new ArrayList<List<Integer>>();
		if (root == null)
			return result;
		
		Queue<TreeNode> q = new LinkedList<>();
		q.add(root);
		while (q.size() > 0) {
			List<Integer> list = new ArrayList<>();
			int size = q.size();
			for (int i = 0; i < size; i++) {
				TreeNode node = q.poll();
				list.add(node.val);
				
				if (node.left != null)
					q.add(node.left);
				if (node.right != null)
					q.add(node.right);
			}
			result.add(0, list);
		}
		return result;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/binary-tree-level-order-traversal-ii/discuss/35045/Simple-Java-solution-with-LinkedList.
	 * 
	 * Rf : https://leetcode.com/problems/binary-tree-level-order-traversal-ii/discuss/201266/topic
	 * 
	 * Other code:
	 * https://leetcode.com/problems/binary-tree-level-order-traversal-ii/discuss/35106/Java-solution-that-beats-80
	 * https://leetcode.com/problems/binary-tree-level-order-traversal-ii/discuss/35045/Simple-Java-solution-with-LinkedList./33335
	 * https://leetcode.com/problems/binary-tree-level-order-traversal-ii/discuss/35085/Java-bfs-and-dfs-solutions.
	 */
	public List<List<Integer>> levelOrderBottom_recursive(TreeNode root) {
		LinkedList<List<Integer>> list = new LinkedList<List<Integer>>();
		addLevel(list, 0, root);
		return list;
	}

	private void addLevel(LinkedList<List<Integer>> list, int level, TreeNode node) {
		if (node == null)
			return;
		
		// 判斷當前 level 是否創建了集合，每一層對應一個集合
		if (list.size() == level)
			list.addFirst(new LinkedList<Integer>());
		
		list.get(list.size() - 1 - level).add(node.val);
		addLevel(list, level + 1, node.left);
		addLevel(list, level + 1, node.right);
	}
	
	// https://leetcode.com/problems/binary-tree-level-order-traversal-ii/discuss/35006/Java-2ms-BFS-solution
	public List<List<Integer>> levelOrderBottom_2_List(TreeNode root) {
		List<List<Integer>> retValue = new ArrayList<>();
		List<TreeNode> toUse = new ArrayList<>();
		if (root != null) {
			toUse.add(root);
		}

		while (toUse.size() != 0) {
			List<Integer> result = new ArrayList<>();
			List<TreeNode> next = new ArrayList<>();
			
			for (TreeNode node : toUse) {
				result.add(node.val);

				if (node.left != null) {
					next.add(node.left);
				}

				if (node.right != null) {
					next.add(node.right);
				}
			}
			
			retValue.add(result);
			toUse = next;
		}
		Collections.reverse(retValue);
		return retValue;
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/binary-tree-level-order-traversal-ii/discuss/34978/Python-solutions-(dfs-recursively-dfs%2Bstack-bfs%2Bqueue).
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/binary-tree-level-order-traversal-ii/discuss/35172/My-8ms-BFS-C%2B%2B-solution.
     * https://leetcode.com/problems/binary-tree-level-order-traversal-ii/discuss/34970/Is-there-any-better-idea-than-doing-regular-level-order-traversal-and-reverse-the-result
     * https://leetcode.com/problems/binary-tree-level-order-traversal-ii/discuss/35108/C%2B%2B-4ms-solution!
     * https://leetcode.com/problems/binary-tree-level-order-traversal-ii/discuss/34993/4ms-C-language-Solution
     */

}
