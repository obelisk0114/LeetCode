package OJ0101_0110;

import definition.TreeNode;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.Stack;

public class Construct_Binary_Tree_from_Preorder_and_Inorder_Traversal {
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/discuss/34538/My-Accepted-Java-Solution
	 * 
	 * We have 2 arrays, PRE and IN.
	 * Preorder traversing implies that PRE[0] is the root node.
	 * Then we can find this PRE[0] in IN, say it's IN[5].
	 * Now we know that IN[5] is root, so we know that IN[0] - IN[4] is on the left 
	 * side, IN[6] to the end is on the right side.
	 * Recursively doing this on subarrays, we can build a tree out of it
	 * 
	 * pre order traversal always visit all the node on left branch before going to 
	 * the right ( root -> left -> ... -> right), therefore, we can get the immediate 
	 * right child index by skipping all the node on the left branches/subtrees of 
	 * current node
	 * 
	 * When we found the root in "inorder" array we immediately know how many nodes 
	 * are on the left subtree and how many are on the right subtree
	 * 
	 * Therefore the immediate right child index is preStart + numsOnLeft + 1 
	 * (in preorder traversal array root is always ahead of children nodes but you 
	 * don't know which one is the left child which one is the right, and this is why 
	 * we need inorder array)
	 * numsOnLeft = root - inStart.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/discuss/34538/My-Accepted-Java-Solution/32854
	 * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/discuss/34538/My-Accepted-Java-Solution/171300
	 */
	public TreeNode buildTree(int[] preorder, int[] inorder) {
		return helper(0, 0, inorder.length - 1, preorder, inorder);
	}

	public TreeNode helper(int preStart, int inStart, int inEnd, 
			int[] preorder, int[] inorder) {
		
		// 'preStart > preorder.length - 1' is unnecessary.
		if (preStart > preorder.length - 1 || inStart > inEnd) {
			return null;
		}
		
		TreeNode root = new TreeNode(preorder[preStart]);
		
		int inIndex = 0; // Index of current root in inorder
		for (int i = inStart; i <= inEnd; i++) {
			if (inorder[i] == root.val) {
				inIndex = i;
				break;
			}
		}
		
		root.left = helper(preStart + 1, inStart, inIndex - 1, 
				preorder, inorder);
		root.right = helper(preStart + inIndex - inStart + 1, inIndex + 1, inEnd, 
				preorder, inorder);
		return root;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/discuss/34541/5ms-Java-Clean-Solution-with-Caching
	 * 
	 * The Root of the tree is the first element in Preorder Array.
	 * Find the position of the Root in inorder Array.
	 * Elements to the left of Root element in inorder Array are the left subtree.
	 * Elements to the right of Root element in inorder Array are the right subtree.
	 * 
	 * Caching positions of inorder[] indices using a HashMap
	 * 
	 * No need to pass inorder to buildTree helper. The map has all the info.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/discuss/34541/5ms-Java-Clean-Solution-with-Caching/32882
	 * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/discuss/34541/5ms-Java-Clean-Solution-with-Caching/32880
	 * 
	 * Other code:
	 * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/discuss/34541/5ms-Java-Clean-Solution-with-Caching/32881
	 */
	public TreeNode buildTree_HashMap(int[] preorder, int[] inorder) {
	    Map<Integer, Integer> inMap = new HashMap<Integer, Integer>();
	    for(int i = 0; i < inorder.length; i++) {
	        inMap.put(inorder[i], i);
	    }

	    TreeNode root = buildTree_HashMap(preorder, 0, preorder.length - 1, 
	    		inorder, 0, inorder.length - 1, inMap);
	    return root;
	}

	public TreeNode buildTree_HashMap(int[] preorder, int preStart, int preEnd, 
			int[] inorder, int inStart, int inEnd, Map<Integer, Integer> inMap) {
		
		/*
		 * We can modify it to this:
		 * if (preStart > preEnd) return null;
		 * 
		 * Because
		 * int numsLeft = inRoot - inStart;
		 */
		if (preStart > preEnd || inStart > inEnd)
			return null;

		TreeNode root = new TreeNode(preorder[preStart]);
		int inRoot = inMap.get(root.val);
		int numsLeft = inRoot - inStart;

		root.left = buildTree_HashMap(preorder, preStart + 1, preStart + numsLeft, 
				inorder, inStart, inRoot - 1, inMap);
		root.right = buildTree_HashMap(preorder, preStart + numsLeft + 1, preEnd, 
				inorder, inRoot + 1, inEnd, inMap);

		return root;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/discuss/34651/Neat-JAVA-solution%3A-pretty-easy-to-read
	 */
	public TreeNode buildTree2(int[] preorder, int[] inorder) {
		if (preorder.length == 0) {
			return null;
		}
		return buildTreeHelper(preorder, inorder, 0, preorder.length - 1, 
				0, preorder.length - 1);
	}

	public TreeNode buildTreeHelper(int[] preorder, int[] inorder, int ps, int pe, 
			int is, int ie) {
		
		if (ps > pe || is > ie) {
			return null;
		}
		
		TreeNode root = new TreeNode(preorder[ps]);
		if (ps == pe) {
			return root;
		}
		
		int index = 0;
		for (int i = is; i <= ie; i++) {
			if (root.val == inorder[i]) {
				index = i;
				break;
			}
		}
		
		root.left = buildTreeHelper(preorder, inorder, ps + 1, ps + index - is, 
				is, index - 1);
		root.right = buildTreeHelper(preorder, inorder, ps + index - is + 1, pe, 
				index + 1, ie);
		return root;
	}
	
	/*
	 * The following 2 variables and 2 functions are from this link.
	 * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/discuss/34543/Simple-O(n)-without-map/32890
	 * 
	 * preorder: [1, 2, 4, 5, 3, 6]
	 * inorder: [4, 2, 5, 1, 6, 3]
	 * 
	 * 1. Use the first element of preorder, the 1, as root.
	 * 2. Search it in inorder.
	 * 3. Split inorder by it, here into [4, 2, 5] and [6, 3].
	 * 4. Split the rest of preorder into two parts as large as the inorder parts, 
	 *    here into [2, 4, 5] and [3, 6].
	 * 5. Use preorder = [2, 4, 5] and inorder = [4, 2, 5] to add the left subtree.
	 * 6. Use preorder =[3, 6] and inorder = [6, 3] to add the right subtree.
	 * 
	 * You can use pointers into preorder and inorder instead of splitting them. When 
	 * you're doing that, you don't need the value-to-index map.
	 * 
	 * Consider the example again. Instead of finding the 1 in inorder, splitting the 
	 * arrays into parts and recursing on them, just recurse on the full remaining 
	 * arrays and stop when you come across the 1 in inorder. That's what my above 
	 * solution does. Each recursive call gets told where to stop, and it tells its 
	 * subcalls where to stop. It gives its own root value as stopper to its left 
	 * subcall and its parent's stopper as stopper to its right subcall.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/discuss/34543/Simple-O(n)-without-map
	 */
	private int in_point_stop = 0;
    private int pre_point_stop = 0;
    
    public TreeNode buildTree_point_stop(int[] preorder, int[] inorder) {
        return build_point_stop(preorder, inorder, Integer.MIN_VALUE);
    }
    
    private TreeNode build_point_stop(int[] preorder, int[] inorder, int stop) {
		if (pre_point_stop >= preorder.length)
			return null;
		if (inorder[in_point_stop] == stop) {
			in_point_stop++;
			return null;
		}
		
        TreeNode node = new TreeNode(preorder[pre_point_stop++]);
        node.left = build_point_stop(preorder, inorder, node.val);
        node.right = build_point_stop(preorder, inorder, stop);
        return node;        
    }
	
	/*
	 * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/discuss/34744/Concise-Java-Recursive-Solution
	 * 
	 * 1. The Root of the tree is the first element in Preorder Array.
	 * 2. Find the position of the Root in Inorder Array.
	 * 3. Elements to the left of Root element in Inorder Array are the left subtree.
	 * 4. Elements to the right of Root element in Inorder Array are the right subtree.
	 * 5. Call recursively buildTree on the subarray composed by the elements in the 
	 *    left subtree. Attach returned left subtree root as left child of Root node.
	 * 6. Call recursively buildTree on the subarray composed by the elements in the 
	 *    right subtree. Attach returned right subtree root as right child of Root node.
	 * 7. return Root.
	 */
	public TreeNode buildTree_create_array(int[] preorder, int[] inorder) {
		if (preorder == null || inorder == null 
				|| inorder.length == 0 || preorder.length == 0)
			return null;
		
		TreeNode root = new TreeNode(preorder[0]);
		if (preorder.length == 1)
			return root;
		
		int breakindex = -1;
		for (int i = 0; i < inorder.length; i++) {
			if (inorder[i] == preorder[0]) {
				breakindex = i;
				break;
			}
		}
		
		int[] subleftpre = Arrays.copyOfRange(preorder, 1, breakindex + 1);
		int[] subleftin = Arrays.copyOfRange(inorder, 0, breakindex);
		int[] subrightpre = Arrays.copyOfRange(preorder, breakindex + 1, preorder.length);
		int[] subrightin = Arrays.copyOfRange(inorder, breakindex + 1, inorder.length);
		
		root.left = buildTree_create_array(subleftpre, subleftin);
		root.right = buildTree_create_array(subrightpre, subrightin);
		return root;
	}
	
	/*
	 * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/discuss/34555/The-iterative-solution-is-easier-than-you-think!/32916
	 * 
	 * 1. Keep pushing the nodes from the preorder into a stack (and keep making the 
	 *    tree by adding nodes to the left of the previous node) until the top of the 
	 *    stack matches the inorder.
	 * 2. At this point, pop the top of the stack until the top does not equal 
	 *    inorder (keep a flag to note that you have made a pop).
	 * 3. Repeat 1 and 2 until preorder is empty. The key point is that whenever the 
	 *    flag is set, insert a node to the right and reset the flag.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/discuss/34555/The-iterative-solution-is-easier-than-you-think!
	 * 
	 * Other code:
	 * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/discuss/34712/Here-is-the-iterative-solution-in-Java
	 */
	public TreeNode buildTree_iterative(int[] preorder, int[] inorder) {
		if (preorder.length == 0)
			return null;
		
		Stack<TreeNode> s = new Stack<>();
		TreeNode root = new TreeNode(preorder[0]), cur = root;
		
		for (int i = 1, j = 0; i < preorder.length; i++) {
			if (cur.val != inorder[j]) {
				cur.left = new TreeNode(preorder[i]);
				s.push(cur);
				cur = cur.left;
			} 
			else {
				j++;
				while (!s.empty() && s.peek().val == inorder[j]) {
					cur = s.pop();
					j++;
				}
				cur.right = new TreeNode(preorder[i]);
                cur = cur.right;
			}
		}
		return root;
	}
	
	// https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/discuss/34555/The-iterative-solution-is-easier-than-you-think!/117721
	public TreeNode buildTree_iterative2(int[] preorder, int[] inorder) {
		// deal with edge case(s)
		if (preorder.length == 0) {
			return null;
		}

		// build a map of the indices of the values as they appear in the inorder array
		Map<Integer, Integer> map = new HashMap<>();
		for (int i = 0; i < inorder.length; i++) {
			map.put(inorder[i], i);
		}

		// initialize the stack of tree nodes
		Stack<TreeNode> stack = new Stack<>();
		int value = preorder[0];
		TreeNode root = new TreeNode(value);
		stack.push(root);

		// for all remaining values...
		for (int i = 1; i < preorder.length; i++) {
			// create a node
			value = preorder[i];
			TreeNode node = new TreeNode(value);

			if (map.get(value) < map.get(stack.peek().val)) {
				// the new node is on the left of the last node,
				// so it must be its left child (that's the way preorder works)
				stack.peek().left = node;
			} 
			else {
				// the new node is on the right of the last node,
				// so it must be the right child of either the last node
				// or one of the last node's ancestors.
				// pop the stack until we either run out of ancestors
				// or the node at the top of the stack is to the right of the new node
				TreeNode parent = null;
				while (!stack.isEmpty() && map.get(value) > map.get(stack.peek().val)) {
					parent = stack.pop();
				}
				parent.right = node;
			}
			stack.push(node);
		}

		return root;
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/discuss/34579/Python-short-recursive-solution.
     * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/discuss/34613/A-Python-recursive-solution
     * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/discuss/34680/Python%3A-Recursion-version-and-Iteration-version-easy-to-understand
     * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/discuss/139414/Why-there-isn't-a-question-about-construct-Binary-Tree-from-preorder-and-postorder-Traversal
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/discuss/34562/Sharing-my-straightforward-recursive-solution
     * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/discuss/34557/My-neat-C%2B%2B-solution
     * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/discuss/34551/My-O(n)(19ms)-solution-without-recusion.-Hope-help-you!
     * 
     * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/discuss/34607/Deep-Understanding-on-the-Iterative-Solution
     */
	
	/**
	 * JavaScript
	 * 
	 * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/discuss/34553/Simple-JavaScript-solution
	 */

}
