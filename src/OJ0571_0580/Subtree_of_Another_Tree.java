package OJ0571_0580;

import definition.TreeNode;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Deque;
import java.util.Stack;

public class Subtree_of_Another_Tree {
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/subtree-of-another-tree/discuss/102724/Java-Solution-tree-traversal
	 * 
	 * For each node during pre-order traversal of s, use a recursive function 
	 * isSame to validate if sub-tree started with this node is the same with t.
	 * 
	 * The reason to check if (s == null) is we will call s.left & s.right in the 
	 * last line
	 * 
	 * Rf :
	 * https://leetcode.com/problems/subtree-of-another-tree/discuss/529262/Java-Recursive-Solution
	 * 
	 * Other code:
	 * https://leetcode.com/problems/subtree-of-another-tree/discuss/474425/Java-Naive-and-Optimized-Preorder-Traversal-Solutions
	 * https://leetcode.com/problems/subtree-of-another-tree/discuss/433199/Java-Recursion-(Node-Comparison)-and-Preorder-Sequence-Comparison-(StringBuilder)
	 * https://leetcode.com/problems/subtree-of-another-tree/discuss/102724/Java-Solution-tree-traversal/148144
	 * https://leetcode.com/problems/subtree-of-another-tree/discuss/378660/6-line-Java-Solution-that-beats-100-both-space-and-runtime
	 */
	public boolean isSubtree_recursive(TreeNode s, TreeNode t) {
		if (s == null)
			return false;
		if (isSame_recursive(s, t))
			return true;
		
		return isSubtree_recursive(s.left, t) || isSubtree_recursive(s.right, t);
	}

	private boolean isSame_recursive(TreeNode s, TreeNode t) {
		if (s == null && t == null)
			return true;
		if (s == null || t == null)
			return false;

		if (s.val != t.val)
			return false;

		return isSame_recursive(s.left, t.left) && isSame_recursive(s.right, t.right);
	}
	
	/*
	 * The following 3 functions are from this link.
	 * https://leetcode.com/problems/subtree-of-another-tree/discuss/102736/Java-Concise-O(n%2Bm)-Time-O(n%2Bm)-Space
	 * 
	 * We can find the preorder traversal of the tree s and t, and compare the 
	 * preorder sequence. If the sequence S_t is a substring of S_s, t is a subtree 
	 * of s.
	 * 
	 * Also, in order to use this method, we need to treat children of leaf nodes as 
	 * null value, and other values should have a prior # character to distinguish 
	 * numbers such as 3 and 23.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/subtree-of-another-tree/solution/
	 * https://leetcode.com/problems/subtree-of-another-tree/discuss/433199/Java-Recursion-(Node-Comparison)-and-Preorder-Sequence-Comparison-(StringBuilder)
	 * https://leetcode.com/problems/subtree-of-another-tree/discuss/102736/Java-Concise-O(n+m)-Time-O(n+m)-Space/106072
	 * https://leetcode.com/problems/subtree-of-another-tree/discuss/818393/C%2B%2B-real-O(S-%2B-T)-algorithm-with-Hash-on-Tree
	 * 
	 * Other code:
	 * https://leetcode.com/problems/subtree-of-another-tree/discuss/102755/Java-Solution-postorder-traversal-without-using-contains-O(N)
	 */
	public boolean isSubtree_serialize(TreeNode s, TreeNode t) {
		// Java uses a naive contains algorithm so to ensure linear time, 
		// replace with KMP algorithm
		return serialize(s).contains(serialize(t));
	}

	public String serialize(TreeNode root) {
		StringBuilder res = new StringBuilder();
		serialize(root, res);
		return res.toString();
	}

	private void serialize(TreeNode cur, StringBuilder res) {
		if (cur == null) {
			res.append(",#");
			return;
		}
		
		res.append("," + cur.val);
		serialize(cur.left, res);
		serialize(cur.right, res);
	}
	
	/*
	 * The following variable and 3 functions are modified from this link.
	 * https://leetcode.com/problems/subtree-of-another-tree/discuss/102734/19ms-C%2B%2B-solution-beats-99.9
	 * 
	 * Brute force solution would be recursively compare each node in s with t to 
	 * check for identical. Better solution is to only compare nodes in s with the 
	 * same max depth as t. First get max depth of t, then recursively check each 
	 * node in s, if depth equals, push to a List. Then compare each node in the List 
	 * with t.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/subtree-of-another-tree/discuss/102734/19ms-C++-solution-beats-99.9/106065
	 */
	private List<TreeNode> nodes_depth = new ArrayList<>();

	public boolean isSubtree_depth(TreeNode s, TreeNode t) {
		if (s == null && t == null)
			return true;
		if (s == null || t == null)
			return false;

		getDepth_depth(s, getDepth_depth(t, -1));

		for (TreeNode n : nodes_depth)
			if (identical_depth(n, t))
				return true;

		return false;
	}

	private int getDepth_depth(TreeNode r, int d) {
		if (r == null)
			return -1;

		int depth = 
				Math.max(getDepth_depth(r.left, d), getDepth_depth(r.right, d)) + 1;

		// Check if depth equals required value
		// Require depth is -1 for tree t (only return the depth, no push)
		if (depth == d)
			nodes_depth.add(r);

		return depth;
	}

	private boolean identical_depth(TreeNode a, TreeNode b) {
		if (a == null && b == null)
			return true;
		if (a == null || b == null || a.val != b.val)
			return false;

		return identical_depth(a.left, b.left) && identical_depth(a.right, b.right);
	}

	/*
	 * The following 2 functions are by myself.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/subtree-of-another-tree/discuss/102724/Java-Solution-tree-traversal/106046
	 */
	public boolean isSubtree_self(TreeNode s, TreeNode t) {
        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.offerLast(s);
        
        while (!queue.isEmpty()) {
            TreeNode cur = queue.pollFirst();
            if (sameTree_self(cur, t)) {
                return true;
            }
            
            if (cur.left != null) {
                queue.offerLast(cur.left);
            }
            
            if (cur.right != null) {
                queue.offerLast(cur.right);
            }
        }
        
        return false;
    }
    
    private boolean sameTree_self(TreeNode s, TreeNode t) {
        if (s == null && t == null) {
            return true;
        }
        
        if (s == null || t == null) {
            return false;
        }
        
        if (s.val != t.val) {
            return false;
        }
        
        return sameTree_self(s.left, t.left) && sameTree_self(s.right, t.right);
    }
    
    /*
     * The following 2 functions are from this link.
     * https://leetcode.com/problems/subtree-of-another-tree/discuss/664582/Intuitive-Java-Fully-Iterative-Solution-Accepted
     * 
     * "isSame()" method takes two nodes and returns if they are the "same" tree or 
     * not. Now, we loop over the first tree to find a matching node with the second 
     * tree's root. we call isSame method from isSubTree only when 2 nodes match i.e. 
     * we have a partial match of "roots" and now we can pass these 2 roots to isSame 
     * method to compare if they are the "same" tree.
     * 
     * If isSame method returns "true", we know that we have found a match. In other 
     * words, the two nodes that we passed, form the "same" tree and that also means 
     * that second tree is a subtree of the first.
     * 
     * If we have found that, the "same" flag is "true" and we know that there's no 
     * need to look any further. The "while" loop in "isSubTree" method breaks and we 
     * return the result - "same".
     * 
     * On the other hand, if "isSame" method returns false, we don't need to stop and 
     * look for another node from the first tree which could match the root of second 
     * tree. In the end when loop breaks because there are no nodes left i.e. 
     * "stack.isEmpty()", we return the same boolean flag - "same" which is "false".
     * 
     * Rf :
     * https://leetcode.com/problems/subtree-of-another-tree/discuss/664582/Intuitive-Java-Fully-Iterative-Solution-Accepted/589463
     */
	public boolean isSubtree_iterative(TreeNode s, TreeNode t) {
		Deque<TreeNode> stack = new LinkedList<>();
		stack.push(s);
		
		boolean same = false;
		while (!stack.isEmpty() && !same) {
			TreeNode node = stack.pop();

			if (node.val == t.val) {
				same = isSame_iterative(node, t);
			}

			if (node.left != null) {				
				stack.push(node.left);
			}
			if (node.right != null) {				
				stack.push(node.right);
			}
		}
		return same;
	}

	private boolean isSame_iterative(TreeNode p, TreeNode q) {
		Deque<TreeNode> stack = new LinkedList<>();
		stack.push(p);
		stack.push(q);

		while (!stack.isEmpty()) {
			TreeNode a = stack.pop();
			TreeNode b = stack.pop();

			if (a == null && b == null) {				
				continue;
			}

			if (a == null || b == null) {				
				return false;
			}

			if (a.val != b.val) {
				return false;
			}

			stack.push(a.left);
			stack.push(b.left);
			stack.push(a.right);
			stack.push(b.right);
		}

		return true;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/subtree-of-another-tree/discuss/102760/Easy-O(n)-java-solution-using-preorder-traversal
	 * 
	 * you need to push null
	 * 
	 * Rf :
	 * https://leetcode.com/problems/subtree-of-another-tree/discuss/102760/Easy-O(n)-java-solution-using-preorder-traversal/296402
	 * https://leetcode.com/problems/subtree-of-another-tree/discuss/102760/Easy-O(n)-java-solution-using-preorder-traversal/106105
	 * https://leetcode.com/problems/subtree-of-another-tree/discuss/102760/Easy-O(n)-java-solution-using-preorder-traversal/118446
	 */
	public boolean isSubtree_hash_iterative(TreeNode s, TreeNode t) {
		String spreorder = generatepreorderString_hash_iterative(s);
		String tpreorder = generatepreorderString_hash_iterative(t);

		return spreorder.contains(tpreorder);
	}

	public String generatepreorderString_hash_iterative(TreeNode s) {
		StringBuilder sb = new StringBuilder();
		
		Stack<TreeNode> stacktree = new Stack<>();
		stacktree.push(s);
		
		while (!stacktree.isEmpty()) {
			TreeNode popelem = stacktree.pop();
			
			// Appending # inorder to handle same values but not subtree cases
			if (popelem == null)
				sb.append(",#");
			else
				sb.append("," + popelem.val);
			
			if (popelem != null) {
				stacktree.push(popelem.right);
				stacktree.push(popelem.left);
			}
		}
		return sb.toString();
	}

	/**
     * Python collections
     * 
     * https://leetcode.com/problems/subtree-of-another-tree/discuss/102741/Python-Straightforward-with-Explanation-(O(ST)-and-O(S%2BT)-approaches)
     * https://leetcode.com/problems/subtree-of-another-tree/discuss/265239/Python-Easy-to-Understand
     * https://leetcode.com/problems/subtree-of-another-tree/discuss/663827/AC-simply-readable-Python-2-solutions
     * https://leetcode.com/problems/subtree-of-another-tree/discuss/102729/Short-Python-by-converting-into-strings
     * https://leetcode.com/problems/subtree-of-another-tree/discuss/386209/Python-98-speed-with-comments
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/subtree-of-another-tree/discuss/102734/19ms-C%2B%2B-solution-beats-99.9
     * https://leetcode.com/problems/subtree-of-another-tree/discuss/102767/c%2B%2B-recursive
     * https://leetcode.com/problems/subtree-of-another-tree/discuss/458803/99.74-faster-c%2B%2B-solution-dfs-optimized-4-approaches
     */
    
    /**
     * JavaScript collections
     * 
     * https://leetcode.com/problems/subtree-of-another-tree/discuss/102810/Concise-JavaScript-solution-using-DFS
     * https://leetcode.com/problems/subtree-of-another-tree/discuss/444343/JavaScript-Solution
     * https://leetcode.com/problems/subtree-of-another-tree/discuss/212524/javascript-4-linesmaybe-it's-cheating
     */

}
