package OJ0231_0240;

import definition.TreeNode;

public class Lowest_Common_Ancestor_of_a_Binary_Search_Tree {
	/*
	 * https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/discuss/198717/5-lines-simple-Java-solution-faster-than-100O(1)-space-complexity
	 * 
	 * We don't need to backtrace to find the LCA node.
	 * It just wants us to find the split point.
	 * 
	 * Walk down from the whole tree's root as long as both p and q are in the same 
	 * subtree (meaning their values are both smaller or both larger than root's). 
	 * This walks straight from the root to the LCA, not looking at the rest of the tree.
	 * 
	 * Rf :
	 * https://leetcode.com/articles/lowest-common-ancestor-of-a-binary-search-tree/
	 * https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/discuss/64963/3-lines-with-O(1)-space-1-Liners-Alternatives
	 */
	public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
		while (root != null) {
			if (p.val < root.val && q.val < root.val)
				root = root.left;
			else if (p.val > root.val && q.val > root.val)
				root = root.right;
			else
				return root;
		}
		return root;
	}
	
	/*
	 * https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/discuss/64954/My-Java-Solution
	 * 
	 * If the given both nodes values are less than that of root, then both the nodes 
	 * must be on the left side of the root, we only check left tree of the root.
	 * 
	 * If the given both nodes values are greater than that of root, then both the 
	 * nodes must be on the right side, we only check right tree of the root.
	 * 
	 * Otherwise, both the nodes will be on the either side of the root, this implies 
	 * the lowest common ancestor is root.
	 * 
	 * Rf : 
	 * https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/discuss/65093/Simple-Java-Solution
	 * https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/discuss/64954/My-Java-Solution/66556
	 * 
	 * Other code :
	 * https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/discuss/65130/9ms-java-solution
	 */
	public TreeNode lowestCommonAncestor2(TreeNode root, TreeNode p, TreeNode q) {
		if (root.val > p.val && root.val > q.val) {
			return lowestCommonAncestor2(root.left, p, q);
		} 
		else if (root.val < p.val && root.val < q.val) {
			return lowestCommonAncestor2(root.right, p, q);
		} 
		else {
			return root;
		}
	}
	
	/*
	 * https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/discuss/65008/11ms-java-solution-3-lines
	 * 
	 * Rf :
	 * https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/discuss/65008/11ms-java-solution-3-lines/66653
	 * https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/discuss/64995/Java-method-spends-10ms
	 * 
	 * Other code :
	 * https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/discuss/65091/My-java-solution-to-share
	 */
	public TreeNode lowestCommonAncestor_min_max(TreeNode root, TreeNode p, TreeNode q) {
		if (root.val < Math.min(p.val, q.val))
			return lowestCommonAncestor_min_max(root.right, p, q);
		if (root.val > Math.max(p.val, q.val))
			return lowestCommonAncestor_min_max(root.left, p, q);
		return root;
	}
	
	/*
	 * https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/discuss/65070/2-line-JAVA-solution
	 * 
	 * Other code :
	 * https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/discuss/65157/My-java-solution%3A)
	 */
	public TreeNode lowestCommonAncestor_sign(TreeNode root, TreeNode p, TreeNode q) {
		if ((p.val - root.val) * (root.val - q.val) >= 0)
			return root;
		return (p.val > root.val) ? lowestCommonAncestor_sign(root.right, p, q) 
				: lowestCommonAncestor_sign(root.left, p, q);
	}
	
	// https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/discuss/65184/No-Comparison-needed!-(Java)
	public TreeNode lowestCommonAncestor_tree(TreeNode root, TreeNode p, TreeNode q) {
		if (root == null || root == p || root == q)
			return root;

		TreeNode left = lowestCommonAncestor_tree(root.left, p, q);
		TreeNode right = lowestCommonAncestor_tree(root.right, p, q);

		if (left == null)
			return right;
		if (right == null)
			return left;

		return root;
	}
	
	// by myself
	public TreeNode lowestCommonAncestor_self(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null)
            return root;
        
        if ((p.val > root.val && root.val > q.val) || (q.val > root.val && root.val > p.val))
            return root;
        else if (p.val == root.val)
            return p;
        else if (q.val == root.val)
            return q;
        else if (root.val > p.val)
            return lowestCommonAncestor_self(root.left, p, q);
        else if (p.val > root.val)
            return lowestCommonAncestor_self(root.right, p, q);
        else
            return null;
    }
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/discuss/65074/Python-Iterative-Solution
     * https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/discuss/158059/Python-or-DFS-tm
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/discuss/64980/C%2B%2B-Recursive-and-Iterative
     */

}
