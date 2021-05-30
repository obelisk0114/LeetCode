package OJ0291_0300;

import definition.TreeNode;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.Queue;
import java.util.Stack;

public class Binary_Tree_Longest_Consecutive_Sequence {
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/binary-tree-longest-consecutive-sequence/discuss/74468/Easy-Java-DFS-is-there-better-time-complexity-solution/251091
	 */
	public int longestConsecutive_bottomUp2(TreeNode root) {
		int[] max = { 0 };
		longestConsecutive_bottomUp2(root, max);
		return max[0];
	}

	private int longestConsecutive_bottomUp2(TreeNode root, int[] max) {
		if (root == null) {
			return 0;
		}

		int curMax = 1;
		int left = longestConsecutive_bottomUp2(root.left, max);
		int right = longestConsecutive_bottomUp2(root.right, max);
		
		if (root.left != null && root.val == root.left.val - 1) {
			curMax = Math.max(left + 1, curMax);
		}
		if (root.right != null && root.val == root.right.val - 1) {
			curMax = Math.max(right + 1, curMax);
		}
		
		max[0] = Math.max(max[0], curMax);
		return curMax;
	}

	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/binary-tree-longest-consecutive-sequence/solution/
	 * Approach #2 (Bottom Up Depth-first Search)
	 * 
	 * The bottom-up approach is similar to a post-order traversal. We return the 
	 * consecutive path length starting at current node to its parent. Then its 
	 * parent can examine if its node value can be included in this consecutive path.
	 * 
	 * Other code:
	 * https://leetcode.com/problems/binary-tree-longest-consecutive-sequence/discuss/74605/Two-java-recursive-solution-top-down-and-bottom-up-and-one-Iteration-solution-using-stack
	 */
	private int maxLength_bottomUp = 0;

	public int longestConsecutive_bottomUp(TreeNode root) {
		dfs_bottomUp(root);
		return maxLength_bottomUp;
	}

	private int dfs_bottomUp(TreeNode p) {
		if (p == null)
			return 0;
		
		int L = dfs_bottomUp(p.left) + 1;
		int R = dfs_bottomUp(p.right) + 1;
		
		if (p.left != null && p.val + 1 != p.left.val) {
			L = 1;
		}
		if (p.right != null && p.val + 1 != p.right.val) {
			R = 1;
		}
		
		int length = Math.max(L, R);
		maxLength_bottomUp = Math.max(maxLength_bottomUp, length);
		return length;
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/binary-tree-longest-consecutive-sequence/discuss/74589/Both-iterative-and-recursive-methods-with-explanations
	 */
	private int max_topDown6;

	public int longestConsecutive_topDown6(TreeNode root) {
		if (root == null)
			return 0;
		
		max_topDown6 = 1;
		helper_topDown6(root, 1);
		return max_topDown6;
	}

	private void helper_topDown6(TreeNode root, int maxCurrent) {
		if (root == null)
			return;
		
		if (root.left != null) {
			if (root.left.val == root.val + 1) {
				max_topDown6 = maxCurrent + 1 > max_topDown6 ? 
						maxCurrent + 1 : max_topDown6;
				
				helper_topDown6(root.left, maxCurrent + 1);
			} 
			else
				helper_topDown6(root.left, 1);
		}
		
		if (root.right != null) {
			if (root.right.val == root.val + 1) {
				max_topDown6 = maxCurrent + 1 > max_topDown6 ? 
						maxCurrent + 1 : max_topDown6;
				
				helper_topDown6(root.right, maxCurrent + 1);
			} 
			else
				helper_topDown6(root.right, 1);
		}
	}

	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/binary-tree-longest-consecutive-sequence/discuss/74517/1ms-easy-to-understand-java-solution-just-traverse-the-tree-once
	 */
	public int longestConsecutive_topDown(TreeNode root) {
		int[] ret = { 0 };
		int curLen = 0;

		helper_topDown(null, root, curLen, ret);

		return ret[0];
	}

	private void helper_topDown(TreeNode parent, TreeNode cur, 
			int curLen, int[] ret) {
		
		if (cur == null) {
			return;
		}

		curLen = parent == null || cur.val != parent.val + 1 ? 1 : curLen + 1;

		ret[0] = Math.max(ret[0], curLen);

		helper_topDown(cur, cur.left, curLen, ret);
		helper_topDown(cur, cur.right, curLen, ret);
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/binary-tree-longest-consecutive-sequence/solution/
	 * Approach #1 (Top Down Depth-first Search)
	 * 
	 * We use a variable length to store the current consecutive path length and pass 
	 * it down the tree. As we traverse, we compare the current node with its parent 
	 * node to determine if it is consecutive. If not, we reset the length.
	 * 
	 * Other code:
	 * https://leetcode.com/problems/binary-tree-longest-consecutive-sequence/discuss/74602/Java-recursive-solution
	 */
	public int longestConsecutive_topDown3(TreeNode root) {
		return dfs_topDown3(root, null, 0);
	}

	private int dfs_topDown3(TreeNode p, TreeNode parent, int length) {
		if (p == null)
			return length;
		
		length = (parent != null && p.val == parent.val + 1) ? length + 1 : 1;
	    return Math.max(length, Math.max(dfs_topDown3(p.left, p, length),
	                                     dfs_topDown3(p.right, p, length)));
	}
	
	/*
	 * https://leetcode.com/problems/binary-tree-longest-consecutive-sequence/discuss/74589/Both-iterative-and-recursive-methods-with-explanations
	 * 
	 * Use BFS and update max level by level until reaching the bottom level. 
	 * One detail is that I used another queue q2 to store the length of the 
	 * sequence ending at current node.
	 */
	public int longestConsecutive_BFS(TreeNode root) {
		if (root == null)
			return 0;
		
		Queue<TreeNode> q = new LinkedList<>();
		Queue<Integer> q2 = new LinkedList<>();
		q.add(root);
		q2.add(1);
		
		int max = 1;
		while (!q.isEmpty()) {
			int size = q.size();
			for (int i = 0; i < size; i++) {
				TreeNode current = q.poll();
				int count = q2.poll();
				
				if (current.left != null) {
					q.add(current.left);
					
					if (current.left.val == current.val + 1) {
						q2.add(count + 1);
						max = count + 1 > max ? count + 1 : max;
					} 
					else
						q2.add(1);
				}
				
				if (current.right != null) {
					q.add(current.right);
					
					if (current.right.val == current.val + 1) {
						q2.add(count + 1);
						max = count + 1 > max ? count + 1 : max;
					} 
					else
						q2.add(1);
				}
			}
		}
		return max;
	}

	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/binary-tree-longest-consecutive-sequence/solution/
	 * Approach #1 (Top Down Depth-first Search)
	 * 
	 * A top down approach is similar to an pre-order traversal. We use a variable 
	 * length to store the current consecutive path length and pass it down the tree. 
	 * As we traverse, we compare the current node with its parent node to determine 
	 * if it is consecutive. If not, we reset the length.
	 */
	private int maxLength_topDown2 = 0;

	public int longestConsecutive_topDown2(TreeNode root) {
		dfs_topDown2(root, null, 0);
		return maxLength_topDown2;
	}

	private void dfs_topDown2(TreeNode p, TreeNode parent, int length) {
		if (p == null)
			return;
		
		length = (parent != null && p.val == parent.val + 1) ? length + 1 : 1;
		maxLength_topDown2 = Math.max(maxLength_topDown2, length);
		
		dfs_topDown2(p.left, p, length);
		dfs_topDown2(p.right, p, length);
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/binary-tree-longest-consecutive-sequence/discuss/74468/Easy-Java-DFS-is-there-better-time-complexity-solution
	 * 
	 * Just very intuitive depth-first search, send cur node value to the next level 
	 * and compare it with the next level node.
	 */
	private int max_topDown7 = 0;

	public int longestConsecutive_topDown7(TreeNode root) {
		if (root == null)
			return 0;
		
		helper_topDown7(root, 0, root.val);
		return max_topDown7;
	}

	public void helper_topDown7(TreeNode root, int cur, int target) {
		if (root == null)
			return;
		
		if (root.val == target)
			cur++;
		else
			cur = 1;
		
		max_topDown7 = Math.max(cur, max_topDown7);
		
		helper_topDown7(root.left, cur, root.val + 1);
		helper_topDown7(root.right, cur, root.val + 1);
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/binary-tree-longest-consecutive-sequence/discuss/74467/Simple-Recursive-DFS-without-global-variable/77536
	 * 
	 * Take max of the length till now and the max of left and right length.
	 */
	public int longestConsecutive_topDown5(TreeNode root) {
		if (root == null)
			return 0;

		return longestConsecutive_topDown5(root, null, 0);
	}

	public int longestConsecutive_topDown5(TreeNode root, TreeNode prev, int len) {
		// reached the leaf node.
		if (root == null)
			return len;

		int leftLen = 0, rightLen = 0;

		// Increasing node found
		if (prev != null && root.val == prev.val + 1) {
			leftLen = longestConsecutive_topDown5(root.left, root, len + 1);
			rightLen = longestConsecutive_topDown5(root.right, root, len + 1);
		}
		// This node breaks the increasing property. So start again from here.
		else {
			leftLen = Math.max(len, 
					longestConsecutive_topDown5(root.left, root, 1));
			rightLen = Math.max(len, 
					longestConsecutive_topDown5(root.right, root, 1));
		}

		return Math.max(leftLen, rightLen);
	}

	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/binary-tree-longest-consecutive-sequence/discuss/74467/Simple-Recursive-DFS-without-global-variable
	 * 
	 * In the main function, you do not need to call dfs() to both left and right, 
	 * just call return (root==null)? 0 : dfs(root, 1, root.val);
	 * since the last recursion will automatically return the bigger one between the 
	 * left and right node of the root.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/binary-tree-longest-consecutive-sequence/discuss/74467/Simple-Recursive-DFS-without-global-variable/77532
	 * 
	 * Other code:
	 * https://leetcode.com/problems/binary-tree-longest-consecutive-sequence/discuss/74468/Easy-Java-DFS-is-there-better-time-complexity-solution/77551
	 * https://leetcode.com/problems/binary-tree-longest-consecutive-sequence/discuss/74605/Two-java-recursive-solution-top-down-and-bottom-up-and-one-Iteration-solution-using-stack
	 * https://leetcode.com/problems/binary-tree-longest-consecutive-sequence/discuss/74468/Easy-Java-DFS-is-there-better-time-complexity-solution/792821
	 */
	public int longestConsecutive_topDown4(TreeNode root) {
		// 原始版本, 較差
		// return (root == null) ? 0
		//		: Math.max(dfs_topDown4(root.left, 1, root.val), 
		//				dfs_topDown4(root.right, 1, root.val));
		
		return (root == null) ? 0 : dfs_topDown4(root, 1, root.val);
	}

	public int dfs_topDown4(TreeNode root, int count, int val) {
		// 也可以 return 0; 若是原始版本, 一定要 return count;
		if (root == null)
			return count;
		
		count = (root.val - val == 1) ? count + 1 : 1;
		
		int left = dfs_topDown4(root.left, count, root.val);
		int right = dfs_topDown4(root.right, count, root.val);
		
		return Math.max(Math.max(left, right), count);
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/binary-tree-longest-consecutive-sequence/discuss/74468/Easy-Java-DFS-is-there-better-time-complexity-solution/77547
	 */
	public int longestConsecutive_topDown8(TreeNode root) {
		if (root == null)
			return 0;
		
		return dfs_topDown8(root, 1);
	}

	private int dfs_topDown8(TreeNode root, int len) {
		int left = 0, right = 0;
		
		if (root.left != null)
			left = dfs_topDown8(root.left, 
					root.val + 1 == root.left.val ? len + 1 : 1);
		if (root.right != null)
			right = dfs_topDown8(root.right, 
					root.val + 1 == root.right.val ? len + 1 : 1);
		
		return Math.max(len, Math.max(left, right));
	}
	
	/*
	 * The following 2 functions are by myself.
	 */
	public int longestConsecutive_self(TreeNode root) {
        if (root == null) {
            return 0;
        }
        
		int[] res = { 1 };
		dfs_self(root, res);
		return res[0];
	}
	
	private int dfs_self(TreeNode root, int[] res) {
		if (root == null) {
			return 0;
		}
		
		int left = dfs_self(root.left, res);
		int right = dfs_self(root.right, res);
		
		int max = 1;
		
		if (root.left != null) {
			if (root.left.val - root.val == 1) {
				max = left + 1;
				res[0] = Math.max(res[0], max);
			}
		}
		
		if (root.right != null) {
			if (root.right.val - root.val == 1) {
				max = Math.max(max, right + 1);
				res[0] = Math.max(res[0], max);
			}
		}
		
		return max;
	}
	
	/*
	 * https://leetcode.com/problems/binary-tree-longest-consecutive-sequence/discuss/74605/Two-java-recursive-solution-top-down-and-bottom-up-and-one-Iteration-solution-using-stack
	 * 
	 * Use a map and stack to keep track the max path
	 */
	public int longestConsecutive_stack(TreeNode root) {
		if (root == null) {
			return 0;
		}
		
		int max = 1;
		
		Stack<TreeNode> stack = new Stack<>();
		HashMap<TreeNode, Integer> map = new HashMap<>();
		stack.push(root);
		map.put(root, 1);
		
		while (!stack.isEmpty()) {
			TreeNode cur = stack.pop();
			
			int left = cur.left != null && cur.left.val - cur.val == 1 ? 
					map.get(cur) + 1 : 1;
			int right = cur.right != null && cur.right.val - cur.val == 1 ? 
					map.get(cur) + 1 : 1;
			
			max = Math.max(max, Math.max(left, right));
			
			if (cur.right != null) {
				stack.push(cur.right);
				map.put(cur.right, right);
			}
			if (cur.left != null) {
				stack.push(cur.left);
				map.put(cur.left, left);
			}
		}
		return max;
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/binary-tree-longest-consecutive-sequence/discuss/74576/13-lines-of-Python-DFS-solution
     * https://leetcode.com/problems/binary-tree-longest-consecutive-sequence/discuss/74513/Two-simple-iterative-solutions-BFS-and-DFS
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/binary-tree-longest-consecutive-sequence/discuss/74548/C%2B%2B-solution-in-4-lines
     */

}
