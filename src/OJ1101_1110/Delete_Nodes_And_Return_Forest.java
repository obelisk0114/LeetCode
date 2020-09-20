package OJ1101_1110;

import definition.TreeNode;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Queue;

public class Delete_Nodes_And_Return_Forest {
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/delete-nodes-and-return-forest/discuss/328860/Simple-Java-Sol
	 * 
	 * To remove a node, the child need to notify its parent about the child's 
	 * existance.
	 * To determine whether a node is a root node in the final forest, we need to know 
	 * [1] whether the node is removed (which is trivial), and 
	 * [2] whether its parent is removed (which requires the parent to notify the 
	 *     child)
	 * 
	 * passing info downwards -- by arguments
	 * passing info upwards -- by return value
	 * 
	 * Rf :
	 * https://leetcode.com/problems/delete-nodes-and-return-forest/discuss/328854/Python-Recursion-with-explanation-Question-seen-in-a-2016-interview
	 * https://leetcode.com/problems/delete-nodes-and-return-forest/discuss/328860/Simple-Java-Sol/303070
	 */
	public List<TreeNode> delNodes_dfs2(TreeNode root, int[] to_delete) {
		List<TreeNode> forest = new ArrayList<>();
		if (root == null)
			return forest;
		
		Set<Integer> set = new HashSet<>();
		for (int i : to_delete) {
			set.add(i);
		}
		
		deleteNodes_dfs2(root, set, forest);
		
		if (!set.contains(root.val)) {
			forest.add(root);
		}
		return forest;
	}

	private TreeNode deleteNodes_dfs2(TreeNode node, Set<Integer> set, 
			List<TreeNode> forest) {
		
		if (node == null)
			return null;

		node.left = deleteNodes_dfs2(node.left, set, forest);
		node.right = deleteNodes_dfs2(node.right, set, forest);

		if (set.contains(node.val)) {
			if (node.left != null)
				forest.add(node.left);
			if (node.right != null)
				forest.add(node.right);
			
			return null;
		}

		return node;
	}
	
	/*
	 * The following 2 variables and 2 functions are from this link.
	 * https://leetcode.com/problems/delete-nodes-and-return-forest/discuss/328853/JavaC%2B%2BPython-Recursion-Solution
	 * 
	 * If a node is root (has no parent) and isn't deleted,
	 * when will we add it to the result.
	 * 
	 * is_root: The node's parent is deleted. The node is the root node of the tree 
	 *          in the forest.
	 * !deleted: The node is not in the to_delete array.
	 * 
	 * 下游需要的訊息由根結點用參數傳下去
	 * 返回上游需要的資訊
	 * 
	 * To remove a node, the child need to notify its parent about the child's 
	 * existance.
	 * To determine whether a node is a root node in the final forest, we need to know 
	 * [1] whether the node is removed (which is trivial), and 
	 * [2] whether its parent is removed (which requires the parent to notify the 
	 *     child)
	 * 
	 * passing info downwards -- by arguments
	 * passing info upwards -- by return value
	 * 
	 * Rf :
	 * https://www.youtube.com/watch?v=_adniHPvQyE   19:17
	 * https://leetcode.com/problems/delete-nodes-and-return-forest/discuss/328854/Python-Recursion-with-explanation-Question-seen-in-a-2016-interview
	 * https://leetcode.com/problems/delete-nodes-and-return-forest/discuss/328853/JavaC++Python-Recursion-Solution/302279
	 * 
	 * Other code:
	 * https://leetcode.com/problems/delete-nodes-and-return-forest/discuss/329421/Java-recursive-beats-100
	 * https://leetcode.com/problems/delete-nodes-and-return-forest/discuss/530389/Java-Recursion
	 */
	Set<Integer> to_delete_set_dfs;
	List<TreeNode> res_dfs;

	public List<TreeNode> delNodes_dfs(TreeNode root, int[] to_delete) {
		to_delete_set_dfs = new HashSet<>();
		res_dfs = new ArrayList<>();
		for (int i : to_delete)
			to_delete_set_dfs.add(i);
		
		helper_dfs(root, true);
		return res_dfs;
	}

	private TreeNode helper_dfs(TreeNode node, boolean is_root) {
		if (node == null)
			return null;
		
		boolean deleted = to_delete_set_dfs.contains(node.val);
		if (is_root && !deleted)
			res_dfs.add(node);
		
		node.left = helper_dfs(node.left, deleted);
		node.right = helper_dfs(node.right, deleted);
		
		return deleted ? null : node;
	}
	
	/*
	 * https://leetcode.com/problems/delete-nodes-and-return-forest/discuss/629417/Java-Iterative-Solution
	 * 
	 * 1. initialize a queue and offer the root
	 * 2. If the root is not in the delete list, then add to the result
	 * 3. For every node in the queue, check if the left or right node is in the 
	 *    delete list
	 * 4. If yes then unlink the current node with that node.
	 * 5. If the current node is in the delete list, then check the right and left 
	 *    nodes. If not null are independent trees now and should be added to the 
	 *    result.
	 * 
	 * 和 delNodes_BFS2 的差異在於這裡先檢查 node.left 和 node.right 是否在 to_delete 裡面
	 * 若在 to_delete 裡面則和 node 斷開連結
	 * 
	 * 之後再檢查 node 是否在 to_delete 裡面
	 * 若在裡面且 node.left 和 node.right 存在，就將他們放入 result (新的 root)
	 * 
	 * 因為前面已經檢查過 node.left 和 node.right 所以還存在的 child 表示不在 to_delete，
	 * 可以直接放入 result
	 * 
	 * Other code:
	 * https://leetcode.com/problems/delete-nodes-and-return-forest/discuss/328851/Java-Iterative-solution-w-explanation.
	 */
	public List<TreeNode> delNodes_BFS(TreeNode root, int[] to_delete) {
		if (root == null)
			return null;
		
		Set<Integer> set = new HashSet<>();
		for (int i : to_delete) {
			set.add(i);
		}
		
		List<TreeNode> result = new ArrayList<>();
		if (!set.contains(root.val)) {
			result.add(root);
		}

		Queue<TreeNode> q = new LinkedList<>();
		q.offer(root);

		while (!q.isEmpty()) {
			TreeNode node = q.poll();

			if (node.left != null) {
				q.offer(node.left);
				
				if (set.contains(node.left.val)) {
					node.left = null;
				}
			}

			if (node.right != null) {
				q.offer(node.right);
				
				if (set.contains(node.right.val)) {
					node.right = null;
				}
			}

			if (set.contains(node.val)) {
				if (node.left != null)
					result.add(node.left);
				if (node.right != null)
					result.add(node.right);
			}
		}
		return result;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/delete-nodes-and-return-forest/discuss/564176/Java-98-Time-100-Memory
	 */
	public List<TreeNode> delNodes_dfs3(TreeNode root, int[] to_delete) {
		List<TreeNode> result = new ArrayList<>();
		Set<Integer> toDeleteSet = new HashSet<>();
		for (int i = 0; i < to_delete.length; i++) {
			toDeleteSet.add(to_delete[i]);
		}

		dfs3(root, toDeleteSet, result);
		if (!toDeleteSet.contains(root.val)) {
			result.add(root);
		}
		return result;
	}

	private void dfs3(TreeNode root, Set<Integer> to_delete, List<TreeNode> result) {
		if (root == null) {
			return;
		}
		
		dfs3(root.left, to_delete, result);
		dfs3(root.right, to_delete, result);

		if (root.left != null && to_delete.contains(root.left.val)) {
			root.left = null;
		}
		if (root.right != null && to_delete.contains(root.right.val)) {
			root.right = null;
		}
		if (to_delete.contains(root.val)) {
			if (root.left != null) {
				result.add(root.left);
			}
			if (root.right != null) {
				result.add(root.right);
			}
			root = null;
		}
	}
	
	/*
	 * https://leetcode.com/problems/delete-nodes-and-return-forest/discuss/554346/JAVA-non-recursive-BFS-solution
	 * 
	 * node.left 和 node.right 有可能在 to_delete 裡面，但是我們在發現 node 在 to_delete 裡面
	 * 就已經先將 node.left 和 node.right 加入 resSet，然後才放入 queue。
	 * 
	 * 所以在將 node.left 和 node.right 放入 resSet 時，並未檢查他們是否在 to_delete 裡面
	 * 
	 * 之後檢查由 queue 取出的 node 時，才檢查 node 是否在 to_delete 裡面，因此若發現在裡面就要移除
	 */
	public List<TreeNode> delNodes_BFS2(TreeNode root, int[] to_delete) {
		if (root == null)
			return new ArrayList<>();

		Set<TreeNode> resSet = new HashSet<>();
		resSet.add(root);
		if (to_delete.length == 0)
			return new ArrayList<>(resSet);

		Set<Integer> toDelete = new HashSet<>();
		for (int val : to_delete)
			toDelete.add(val);

		Queue<TreeNode> q = new LinkedList<>();
		q.offer(root);
		
		while (!q.isEmpty()) {
			TreeNode node = q.poll();
			
			if (toDelete.contains(node.val)) {
				resSet.remove(node);
				
				if (node.left != null)
					resSet.add(node.left);
				if (node.right != null)
					resSet.add(node.right);
			}
			
			if (node.left != null) {
				q.offer(node.left);
				
				if (toDelete.contains(node.left.val))
					node.left = null;
			}
			if (node.right != null) {
				q.offer(node.right);
				
				if (toDelete.contains(node.right.val))
					node.right = null;
			}
		}
		return new ArrayList<>(resSet);
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/delete-nodes-and-return-forest/discuss/328854/Python-Recursion-with-explanation-Question-seen-in-a-2016-interview
     * https://leetcode.com/problems/delete-nodes-and-return-forest/discuss/345009/Python-BFS-Solution
     * https://leetcode.com/problems/delete-nodes-and-return-forest/discuss/624937/Python-beats-99.37-easy-to-understand
     * https://leetcode.com/problems/delete-nodes-and-return-forest/discuss/656106/python-recursive-clean-explained-in-details-with-tips-10-lines-fast
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/delete-nodes-and-return-forest/discuss/680083/Recursively-deleting-nodes-bottom-up-C%2B%2B-(with-comments)-Faster-than-99.96
     * https://leetcode.com/problems/delete-nodes-and-return-forest/discuss/330895/C%2B%2B-Using-Hash-tables-for-parents.-Postorder-O(N)
     * https://leetcode.com/problems/delete-nodes-and-return-forest/discuss/330647/Recursive-C%2B%2B-Solution
     */
	
	/**
	 * JavaScript collections
	 * 
	 * https://leetcode.com/problems/delete-nodes-and-return-forest/discuss/456313/JavaScript-Solution
	 * https://leetcode.com/problems/delete-nodes-and-return-forest/discuss/415075/Clean-JavaScript-solution
	 */

}
