package OJ0661_0670;

import definition.TreeNode;

import java.util.AbstractMap;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Deque;
import java.util.Queue;
import java.util.Map;
import java.util.HashMap;

public class Maximum_Width_of_Binary_Tree {
	/*
	 * https://leetcode.com/problems/maximum-width-of-binary-tree/discuss/151967/Easy-Java-iteration-using-Queues
	 * 
	 * Rf : https://leetcode.com/problems/maximum-width-of-binary-tree/discuss/106648/JAVALevel-TraversalUsing-Two-Queue
	 * 
	 * Other code:
	 * https://leetcode.com/problems/maximum-width-of-binary-tree/discuss/106653/Java-One-Queue-Solution-with-HashMap
	 */
	public int widthOfBinaryTree_2_queue(TreeNode root) {
		if (root == null)
			return 0;
		
		Queue<TreeNode> q = new LinkedList<>();
		Queue<Integer> nums = new LinkedList<>();
		q.offer(root);
		nums.offer(0);
		int res = 0;
		
		while (!q.isEmpty()) {
			int left = nums.peek(), len = q.size(), right = 0;
			for (int i = 0; i < len; i++) {
				TreeNode temp = q.poll();
				right = nums.poll();
				
				if (temp.left != null) {
					q.offer(temp.left);
					nums.offer(2 * right);
				}
				if (temp.right != null) {
					q.offer(temp.right);
					nums.offer(2 * right + 1);
				}
			}
			res = Math.max(res, right - left + 1);
		}
		return res;
	}
	
	/*
	 * https://leetcode.com/problems/maximum-width-of-binary-tree/discuss/106645/C++Java-*-BFSDFS3liner-Clean-Code-With-Explanation
	 * 
	 * 1. Record the id of left most node when first time at each level of the tree 
	 *    during an pre-order run.(you can tell by check the size of the container to 
	 *    hold the first nodes);
	 * 2. At each node, compare the distance from it the left most node with the 
	 *    current max width;
	 */
	public int widthOfBinaryTree(TreeNode root) {
		if (root == null)
			return 0;
		
		int max = 0;
		Queue<Map.Entry<TreeNode, Integer>> q = new LinkedList<Map.Entry<TreeNode, Integer>>();
		q.offer(new AbstractMap.SimpleEntry<TreeNode, Integer>(root, 1));

		while (!q.isEmpty()) {
			int l = q.peek().getValue(), r = l; // right started same as left
			for (int i = 0, n = q.size(); i < n; i++) {
				TreeNode node = q.peek().getKey();
				r = q.poll().getValue();
				
				if (node.left != null)
					q.offer(new AbstractMap.SimpleEntry<TreeNode, Integer>
					(node.left, r * 2));
				if (node.right != null)
					q.offer(new AbstractMap.SimpleEntry<TreeNode, Integer>
					(node.right, r * 2 + 1));
			}
			max = Math.max(max, r + 1 - l);
		}

		return max;
	}
	
	/*
	 * The following function and class are from this link.
	 * https://leetcode.com/problems/maximum-width-of-binary-tree/discuss/106671/Concise-and-clear-solution!
	 * 
	 * Rf : https://leetcode.com/problems/maximum-width-of-binary-tree/discuss/150004/Simple-java-BFS-solution..-Beats-99.13-java-solutions
	 */
	public int widthOfBinaryTree_class_index(TreeNode root) {
		if (root == null)
			return 0;
		
		int maxwidth = 0;
		Queue<NodeIndex> queue = new LinkedList<>();
		queue.offer(new NodeIndex(root, 0));

		while (!queue.isEmpty()) {
			int size = queue.size();
			int start = 0;
			int end = 0;
			for (int i = 0; i < size; i++) {
				NodeIndex nodeIndex = queue.poll();
				TreeNode node = nodeIndex.node;
				int index = nodeIndex.index;
				
				if (i == 0)
					start = index;
				if (i == size - 1)
					end = index;
				
				if (node.left != null)
					queue.offer(new NodeIndex(node.left, index * 2));
				if (node.right != null)
					queue.offer(new NodeIndex(node.right, index * 2 + 1));
			}
			maxwidth = Math.max(maxwidth, end - start + 1);
		}
		return maxwidth;
	}

	class NodeIndex {
		TreeNode node = null;
		int index = 0;

		public NodeIndex(TreeNode node, int index) {
			this.node = node;
			this.index = index;
		}
	}
	
	/*
	 * https://leetcode.com/problems/maximum-width-of-binary-tree/discuss/106666/My-Accepted-Java-Solution-using-Deque
	 * 
	 * 1. We add all nodes from next level no matter it is a null node or not.
	 * 2. Get rid of the null nodes that appear before the first non-null node and 
	 *    after the last non-null node.
	 * 3. Compare it with the existing width.
	 * 
	 * Rf : https://leetcode.com/problems/maximum-width-of-binary-tree/discuss/106659/JAVA-Easy-to-understand-solution.-(BFS)
	 */
	public int widthOfBinaryTree_Add_null(TreeNode root) {
		if (root == null)
			return 0;

		Deque<TreeNode> q = new LinkedList<>();
		q.add(root);
		int max = 0;
		
		while (!q.isEmpty()) {
			int count = 0;
			max = Math.max(max, q.size());
			int size = q.size();
			
			while (count < size) {
				TreeNode node = q.poll();
				if (node == null) {
					q.add(null);
					q.add(null);
				} 
				else {
					q.add(node.left);
					q.add(node.right);
				}
				count++;
			}
			
			while (!q.isEmpty() && q.peekFirst() == null)
				q.pollFirst();
			while (!q.isEmpty() && q.peekLast() == null)
				q.pollLast();
		}
		return max;
	}
	
	// https://leetcode.com/problems/maximum-width-of-binary-tree/discuss/106663/Java-O(n)-BFS-one-queue-clean-solution
	public int widthOfBinaryTree_no_val(TreeNode root) {
		if (root == null) {
			return 0;
		}
		
		LinkedList<TreeNode> queue = new LinkedList<>();
		queue.offer(root);
		root.val = 1;
		
		int max = 1;
		
		while (!queue.isEmpty()) {
			int size = queue.size();
			max = Math.max(max, queue.peekLast().val - queue.peekFirst().val + 1);
			
			for (int i = 0; i < size; i++) {
				root = queue.poll();
				
				if (root.left != null) {
					root.left.val = root.val * 2;
					queue.offer(root.left);
				}
				if (root.right != null) {
					root.right.val = root.val * 2 + 1;
					queue.offer(root.right);
				}
			}
		}
		return max;
	}
	
	// by myself
	public int widthOfBinaryTree_self(TreeNode root) {
        if (root == null)
            return 0;
        
        Queue<TreeNode> queue = new LinkedList<>();
        root.val = 1;
        queue.offer(root);
        
        int max = 1;
        int count = 0;
        
        while (!queue.isEmpty()) {
            int size = queue.size();
            int[] pos = {0, 0};
            for (int i = 0; i < size; i++) {
                TreeNode cur = queue.poll();
                int index = cur.val;
                
                if (pos[0] == 0) {
                    pos[0] = index;
                }
                else {
                    pos[1] = index;
                }
                
                if (cur.left != null) {
                    cur.left.val = 2 * index;
                    queue.offer(cur.left);
                }
                if (cur.right != null) {
                    cur.right.val = 2 * index + 1;
                    queue.offer(cur.right);
                }
            }
            if (pos[1] == 0)
                count = 1;
            else
                count = pos[1] - pos[0] + 1;
            
            max = Math.max(max, count);
        }
        return max;
    }
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/maximum-width-of-binary-tree/discuss/106655/Java-solution-node-position-of-binary-tree.
	 */
	Map<Integer, int[]> map = new HashMap<>();

	public int widthOfBinaryTree_PreOrder(TreeNode root) {
		if (root == null)
			return 0;

		findMax(root, 0, 0);

		int res = 1;
		for (int[] rec : map.values()) {
			res = Math.max(res, rec[1] - rec[0] + 1);
		}

		return res;
	}

	private void findMax(TreeNode root, int level, int pos) {
		if (root == null)
			return;

		int[] rec = map.get(level);
		if (rec == null) {
			rec = new int[2];
			rec[0] = Integer.MAX_VALUE;
			rec[1] = Integer.MIN_VALUE;
		}

		rec[0] = Math.min(rec[0], pos);
		rec[1] = Math.max(rec[1], pos);
		map.put(level, rec);

		findMax(root.left, level + 1, 2 * pos);
		findMax(root.right, level + 1, 2 * pos + 1);
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/maximum-width-of-binary-tree/discuss/106645/C++Java-*-BFSDFS3liner-Clean-Code-With-Explanation
	 * 
	 * Rf : https://leetcode.com/problems/maximum-width-of-binary-tree/discuss/106654/JavaC++-Very-simple-dfs-solution
	 */
	public int widthOfBinaryTree_dfs(TreeNode root) {
		List<Integer> lefts = new ArrayList<Integer>(); // left most nodes at each level;
		int[] res = new int[1]; // max width
		dfs(root, 1, 0, lefts, res);
		return res[0];
	}

	private void dfs(TreeNode node, int id, int depth, List<Integer> lefts, int[] res) {
		if (node == null)
			return;
		
		if (depth >= lefts.size())
			lefts.add(id); // add left most node
		
		res[0] = Integer.max(res[0], id + 1 - lefts.get(depth));
		dfs(node.left, id * 2, depth + 1, lefts, res);
		dfs(node.right, id * 2 + 1, depth + 1, lefts, res);
	}

}
