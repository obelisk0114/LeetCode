package OJ0511_0520;

import definition.TreeNode;

import java.util.LinkedList;
import java.util.ArrayDeque;
import java.util.Queue;

public class Find_Bottom_Left_Tree_Value {
	/*
	 * https://leetcode.com/problems/find-bottom-left-tree-value/discuss/98779/Right-to-Left-BFS-(Python-+-Java)
	 * 
	 * Doing BFS right-to-left means we can simply return the last node's value and 
	 * don't have to keep track of the first node in the current row or even care 
	 * about rows at all. 
	 */
	public int findBottomLeftValue(TreeNode root) {
		Queue<TreeNode> queue = new LinkedList<>();
		queue.add(root);
		while (!queue.isEmpty()) {
			root = queue.poll();
			if (root.right != null)
				queue.add(root.right);
			if (root.left != null)
				queue.add(root.left);
		}
		return root.val;
	}
	
	// https://leetcode.com/problems/find-bottom-left-tree-value/discuss/98827/standard-BFS-in-java
	public int findBottomLeftValue_bfs(TreeNode root) {
		int result = -1;
		ArrayDeque<TreeNode> queue = new ArrayDeque<>();
		queue.offer(root);
		
		while (!queue.isEmpty()) {
			result = queue.peek().val;
			int size = queue.size();
			
			for (int i = 0; i < size; i++) {
				TreeNode treeNode = queue.poll();
				
				if (treeNode.left != null) {
					queue.offer(treeNode.left);
				}
				if (treeNode.right != null) {
					queue.offer(treeNode.right);
				}
			}
		}
		return result;
	}
	
	// https://leetcode.com/problems/find-bottom-left-tree-value/discuss/98786/Verbose-Java-Solution-Binary-tree-level-order-traversal
	public int findBottomLeftValue_bfs2(TreeNode root) {
		if (root == null)
			return 0;

		int result = 0;
		Queue<TreeNode> queue = new LinkedList<>();
		queue.add(root);

		while (!queue.isEmpty()) {
			int size = queue.size();
			for (int i = 0; i < size; i++) {
				TreeNode node = queue.poll();
				if (i == 0)
					result = node.val;
				if (node.left != null)
					queue.add(node.left);
				if (node.right != null)
					queue.add(node.right);
			}
		}
		return result;
	}
	
	/*
	 * The following 2 functions are by myself.
	 * 
	 * Rf : 
	 * https://leetcode.com/problems/find-bottom-left-tree-value/discuss/98805/Simple-Java-recursion-beats-98
	 * https://leetcode.com/problems/find-bottom-left-tree-value/discuss/98843/C++-recursive-solution-(beats-100)-with-basic-explanation
	 * 
	 * Other code:
	 * https://leetcode.com/problems/find-bottom-left-tree-value/discuss/98801/BFS-and-DFS-Java-solution
	 * https://leetcode.com/problems/find-bottom-left-tree-value/discuss/98806/C++-Clean-Code-DFS-Recursion-with-Explanation
	 */
	public int findBottomLeftValue_self_dfs(TreeNode root) {
        int[] bottom = {root.val, 0};
        dfs_self_dfs(root, 0, bottom);
        return bottom[0];
    }
    
    private void dfs_self_dfs(TreeNode root, int depth, int[] bottom) {
        if (root.left == null && root.right == null) {
            if (depth > bottom[1]) {
                bottom[0] = root.val;
                bottom[1] = depth;
            }
        }
        
        if (root.left != null) {
            dfs_self_dfs(root.left, depth + 1, bottom);
        }
        if (root.right != null) {
            dfs_self_dfs(root.right, depth + 1, bottom);
        }
    }
    
    /*
     * The following 2 functions are from this link.
     * https://leetcode.com/problems/find-bottom-left-tree-value/discuss/98802/Simple-Java-Solution-beats-100.0!
     */
	public int findBottomLeftValue_dfs_value(TreeNode root) {
		return findBottomLeftValue_dfs_value(root, 1, new int[] { 0, 0 });
	}

	public int findBottomLeftValue_dfs_value(TreeNode root, int depth, int[] res) {
		if (res[1] < depth) {
			res[0] = root.val;
			res[1] = depth;
		}
		
		if (root.left != null)
			findBottomLeftValue_dfs_value(root.left, depth + 1, res);
		if (root.right != null)
			findBottomLeftValue_dfs_value(root.right, depth + 1, res);
		
		return res[0];
	}
    
    /*
     * The following 2 variables and 2 functions are from this link.
     * https://leetcode.com/problems/find-bottom-left-tree-value/discuss/154469/Simple-Java-solution-using-recursion-4ms-beats-99.7
     */
	int maxDepth_dfs = Integer.MIN_VALUE, result_dfs = -1;
	public int findBottomLeftValue_dfs_global(TreeNode root) {
		helper_dfs_global(root, 1);
		return result_dfs;
	}

	public void helper_dfs_global(TreeNode root, int level) {
		if (root == null) {
			return;
		}
		if ((root.left == null) && (root.right == null) && (level > maxDepth_dfs)) {
			maxDepth_dfs = level;
			result_dfs = root.val;
		}
		
		helper_dfs_global(root.left, level + 1);
		helper_dfs_global(root.right, level + 1);
	}
	
	// Tim provided
	public int findBottomLeftValue_tim(TreeNode root) {
        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.offerLast(root);
        int ans = root.val;
        
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode cur = queue.pollFirst();
                ans = cur.val;
                
                if (cur.right != null) {
                    queue.offerLast(cur.right);
                }
                if (cur.left != null) {
                    queue.offerLast(cur.left);
                }
            }
        }
        return ans;
    }
	
	// by myself2
	public int findBottomLeftValue_self2(TreeNode root) {
        LinkedList<TreeNode> queue = new LinkedList<>();
        LinkedList<TreeNode> store = new LinkedList<>();
        queue.offerLast(root);
        store.offerLast(root);
        
        while (!queue.isEmpty()) {
            int size = queue.size();
            boolean level = false;
            for (int i = 0; i < size; i++) {
                TreeNode cur = queue.pollFirst();
                if (!level) {
                    store.offerLast(cur);
                    level = true;
                }
                
                if (cur.left != null) {
                    queue.offerLast(cur.left);
                }
                if (cur.right != null) {
                    queue.offerLast(cur.right);
                }
            }
        }
        return store.peekLast().val;
    }
	
	// by myself
	public int findBottomLeftValue_self(TreeNode root) {
        LinkedList<TreeNode> queue = new LinkedList<>();
        LinkedList<TreeNode> store = new LinkedList<>();
        queue.offerLast(root);
        store.offerLast(root);
        
        while (!queue.isEmpty()) {
            int size = queue.size();
            boolean level = false;
            for (int i = 0; i < size; i++) {
                TreeNode cur = queue.pollFirst();
                
                if (cur.left != null) {
                    queue.offerLast(cur.left);
                    
                    if (!level) {
                        store.offerLast(cur.left);
                        level = true;
                    }
                }
                if (cur.right != null) {
                    queue.offerLast(cur.right);
                    
                    if (!level) {
                        store.offerLast(cur.right);
                        level = true;
                    }
                }
            }
        }
        return store.peekLast().val;
    }
	
	// https://leetcode.com/problems/find-bottom-left-tree-value/discuss/98822/Java-solution-by-post-order-traversal-(beats-54)

}
