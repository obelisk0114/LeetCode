package OJ0231_0240;

import definition.TreeNode;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

public class Lowest_Common_Ancestor_of_a_Binary_Tree {
	/*
	 * https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/discuss/65226/My-Java-Solution-which-is-easy-to-understand
	 * 
	 * If right branch doesn¡¦t have second node then it¡¦s somewhere below in the left 
	 * sub tree where we found first p or q and it¡¦s below the node we already found 
	 * so the node we found on the left is LCA.
	 * 
	 * if both p and q exist in Tree rooted at root, then return their LCA
	 * if neither p and q exist in Tree rooted at root, then return null
	 * if only one of pq (NOT both of them), exists in Tree rooted at root, return it
	 * 
	 * Rf :
	 * https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/discuss/65228/5-lines-Java-solution/66940
	 * https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/discuss/65341/Share-my-5-line-Java-code-with-brief-explanation
	 * https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/discuss/65226/My-Java-Solution-which-is-easy-to-understand/112902
	 * https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/discuss/65225/4-lines-C%2B%2BJavaPythonRuby
	 */
	public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
		if (root == null || root == p || root == q)
			return root;
		
		TreeNode left = lowestCommonAncestor(root.left, p, q);
		TreeNode right = lowestCommonAncestor(root.right, p, q);
		
		if (left != null && right != null)
			return root;
		return left != null ? left : right;
	}
	
	/*
     * https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/discuss/65335/Java-iterative-and-recursive-solutions.
     * 
     * Rf :
     * https://leetcode.com/articles/lowest-common-ancestor-of-a-binary-tree/
     * https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/discuss/65278/My-solution-may-be-not-so-goodbut-clear
     */
	public TreeNode lowestCommonAncestor_BFS(TreeNode root, TreeNode p, TreeNode q) {
		Map<TreeNode, TreeNode> parent = new HashMap<>();
		Queue<TreeNode> queue = new LinkedList<TreeNode>();
		
		parent.put(root, null);
		queue.offer(root);
		while (!parent.containsKey(p) || !parent.containsKey(q)) {
			TreeNode t = queue.poll();
			
			if (t.left != null) {
				parent.put(t.left, t);
				queue.offer(t.left);
			}
			if (t.right != null) {
				parent.put(t.right, t);
				queue.offer(t.right);
			}
		}
		
		Set<TreeNode> set = new HashSet<>();
	    while (p != null) {
	        set.add(p);
	        p = parent.get(p);
	    }
	    while (!set.contains(q)) {
	        q = parent.get(q);
	    }
	    return q;
	}
	
	
	/*
	 * https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/discuss/65236/JavaPython-iterative-solution
	 * 
	 * To find the lowest common ancestor, we need to find where is p and q and a way 
	 * to track their ancestors. A parent pointer for each node found is good for the 
	 * job. After we found both p and q, we create a set of p's ancestors. Then we 
	 * travel through q's ancestors, the first one appears in p's is our answer.
	 * 
	 * Rf : 
	 * https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/discuss/65236/JavaPython-iterative-solution/66954
	 * https://www.geeksforgeeks.org/iterative-preorder-traversal/
	 */
	public TreeNode lowestCommonAncestor_iter(TreeNode root, TreeNode p, TreeNode q) {
		Map<TreeNode, TreeNode> parent = new HashMap<>();
		Deque<TreeNode> stack = new ArrayDeque<>();
		parent.put(root, null);
		stack.push(root);

		while (!parent.containsKey(p) || !parent.containsKey(q)) {
			TreeNode node = stack.pop();
			
			// Not preorder traversal. It always traverses the right subtree first.
			if (node.left != null) {
				parent.put(node.left, node);
				stack.push(node.left);
			}
			if (node.right != null) {
				parent.put(node.right, node);
				stack.push(node.right);
			}
		}
		
		Set<TreeNode> ancestors = new HashSet<>();
		while (p != null) {
			ancestors.add(p);
			p = parent.get(p);
		}
		
		while (!ancestors.contains(q))
			q = parent.get(q);
		return q;
	}
	
	/*
     * The following 2 functions are from this link.
     * https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/discuss/65387/Java-build-the-paths-in-stacks-from-nodes-to-root-with-DFS-return-the-first-crossing-point-on-paths/255290
     * 
     * Other code :
     * https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/discuss/65387/Java-build-the-paths-in-stacks-from-nodes-to-root-with-DFS-return-the-first-crossing-point-on-paths/67138
     */
	public TreeNode lowestCommonAncestor_DFS(TreeNode root, TreeNode p, TreeNode q) {
		List<TreeNode> path1 = new ArrayList<>();
		List<TreeNode> path2 = new ArrayList<>();

		traversePath(root, p, path1);
		traversePath(root, q, path2);

		TreeNode LCA = null;

		int i = 0;
		while (i < path1.size() && i < path2.size()) {
			if (path1.get(i) == path2.get(i)) {
				LCA = path1.get(i);
			} 
			else {
				break;
			}
			i++;
		}
		return LCA;
	}

	public boolean traversePath(TreeNode root, TreeNode dest, List<TreeNode> path) {
		if (root == null) {
			return false;
		}

		path.add(root);

		if (root == dest) {
			return true;
		}

		if (traversePath(root.left, dest, path)) {
			return true;
		}
		if (traversePath(root.right, dest, path)) {
			return true;
		}

		// Not found, backtrack:
		path.remove(path.size() - 1);

		return false;
	}
	
	// The following 2 functions are by myself.
	public TreeNode lowestCommonAncestor_self(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || p == null || q == null)
            return root;
        
        Map<TreeNode, TreeNode> parents = new HashMap<>();
        preOrder(root, parents, p, q);
        
        Set<TreeNode> set = new HashSet<>();
        TreeNode cur = p;
        while (cur != null) {
            set.add(cur);
            cur = parents.getOrDefault(cur, null);
        }
        
        cur = q;
        while (cur != null) {
            if (set.contains(cur)) {
                return cur;
            }
            else {
                cur = parents.getOrDefault(cur, null);
            }
        }
        return null;
    }
    
    private void preOrder(TreeNode root, Map<TreeNode, TreeNode> parents, 
    		TreeNode p, TreeNode q) {
        if (root == null)
            return;
        
        if (root.left != null)
            parents.put(root.left, root);
        if (root.right != null)
            parents.put(root.right, root);
        
        if (!parents.containsKey(p) || !parents.containsKey(q)) {
            preOrder(root.left, parents, p, q);
            preOrder(root.right, parents, p, q);
        }
    }
    
	/*
	 * https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/discuss/65304/Java-iterative-solution-with-1-stack
	 * 
	 * First time finding p or q, current stack must contain LCA. 
	 * (inorder traversal iterative)
	 * When stack size is decreased, the pop node could be the LCA, so check every
	 * node.
	 * If another node (p or q) is found under it, res is the LCA.
	 */
	public TreeNode lowestCommonAncestor_path_backtrack
	(TreeNode root, TreeNode p, TreeNode q) {
		if (root == null || root == p || root == q)
			return root;

		TreeNode n = root;
		TreeNode res = null;
		
		int size = -1;
		Stack<TreeNode> s = new Stack<TreeNode>();
		while (n != null || !s.empty()) {
			while (n != null) {
				s.push(n);
				
				if (n == p || n == q) {
					if (res == null) { // find p or q the first time
						res = n;
						size = s.size();
					} 
					else
						return res; // find both p and q
				}
				
				n = n.left;
			}
			n = s.pop();
			
			if (s.size() < size) {
				size = s.size();
				res = n;
			}
			
			n = n.right;
		}
		return res;
	}
    
    /**
     * Python collections
     * 
     * https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/discuss/65245/Iterative-Solutions-in-PythonC%2B%2B
     * https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/discuss/158060/Python-DFS-tm
     * https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/discuss/152682/Python-simple-recursive-solution-with-detailed-explanation
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/discuss/65369/Short-and-clean-C%2B%2B-solution
     * https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/discuss/65345/Concise-4-line-c%2B%2B
     */

}
