package OJ0101_0110;

import definition.TreeNode;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Stack;

public class Construct_Binary_Tree_from_Inorder_and_Postorder_Traversal {
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/discuss/34910/Concise-recursive-Java-code-by-making-slight-modification-to-the-previous-problem.
	 */
	public TreeNode buildTree_recursive(int[] inorder, int[] postorder) {
		return helper_recursive(postorder, postorder.length - 1, 
				inorder, 0, inorder.length - 1);
	}

	public TreeNode helper_recursive(int[] postorder, int postStart, 
			int[] inorder, int inStart, int inEnd) {
		
		// inStart > inEnd also works
		if (postStart < 0 || inStart > inEnd)
			return null;
		
		TreeNode root = new TreeNode(postorder[postStart]);
		
		int target = inStart;
		while (inorder[target] != postorder[postStart])
			target++;
		
		root.left = helper_recursive(postorder, postStart - inEnd + target - 1, 
				inorder, inStart, target - 1);
		root.right = helper_recursive(postorder, postStart - 1, 
				inorder, target + 1, inEnd);

		return root;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/discuss/34782/My-recursive-Java-code-with-O(n)-time-and-O(n)-space
	 * 
	 * The basic idea is to take the last element in postorder array as the root, 
	 * find the position of the root in the inorder array; then locate the range for 
	 * left sub-tree and right sub-tree and do recursion. Use a HashMap to record the 
	 * index of root in the inorder array.
	 * 
	 * InOrder is (left subtree) node (right subtree)
	 * PostOrder is (left subtree)(right subtree) (node)
	 * From post order array we get the root which will be at index postEnd
	 * while from in order we can get the number of children in the left subtree 
	 * ie. inIndex - inStart
	 * 
	 * 1. The post order array will give you the root, the last one.
	 * 2. With the root, you can go to the in order array, notice the traverse 
	 *    sequence: left, root, right.
	 *    Then we know the left child array size, right child array size.
	 * 3. With the size, we can then divide the post order array: left, right, root.
	 *    Then, we have everything!
	 * 
	 * When we find the root index inIndex, we can get the number of nodes in the 
	 * left subtree, that is, (inIndex - 1) - inStart (Actually it is the number of 
	 * nodes in the left subtree - 1, but that number will be added to postStart + 1. 
	 * So 1 is canceled.)
	 * 
	 * The postorder array will be cut into two subarrays whose index range are 
	 * [postStart, postStart + ((inIndex - 1) - inStart)] and 
	 * [(postStart + (inIndex - 1) - inStart) + 1, postEnd - 1]. 
	 * Simplify: [postStart, postStart + inIndex - inStart - 1], 
	 * [postStart + inIndex - inStart, postEnd - 1].
	 * 
	 * Rf :
	 * https://leetcode.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/discuss/34782/My-recursive-Java-code-with-O(n)-time-and-O(n)-space/33100
	 * https://leetcode.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/discuss/34782/My-recursive-Java-code-with-O(n)-time-and-O(n)-space/33088
	 * https://leetcode.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/discuss/34782/My-recursive-Java-code-with-O(n)-time-and-O(n)-space/181162
	 */
	public TreeNode buildTreePostIn(int[] inorder, int[] postorder) {
		if (inorder == null || postorder == null 
				|| inorder.length != postorder.length)
			return null;
		
		HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
		for (int i = 0; i < inorder.length; ++i)
			hm.put(inorder[i], i);
		
		return buildTreePostIn(inorder, 0, inorder.length - 1, 
				postorder, 0, postorder.length - 1, hm);
	}

	private TreeNode buildTreePostIn(int[] inorder, int inStart, int inEnd, 
			int[] postorder, int postStart, int postEnd, HashMap<Integer, Integer> hm) {
		
		if (postStart > postEnd || inStart > inEnd)
			return null;
		
		TreeNode root = new TreeNode(postorder[postEnd]);
		int inIndex = hm.get(postorder[postEnd]);
		
		TreeNode leftchild = buildTreePostIn(inorder, inStart, inIndex - 1, 
				postorder, postStart, postStart + inIndex - inStart - 1, hm);
		TreeNode rightchild = buildTreePostIn(inorder, inIndex + 1, inEnd, 
				postorder, postStart + inIndex - inStart, postEnd - 1, hm);
		root.left = leftchild;
		root.right = rightchild;
		return root;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/discuss/34787/Simple-and-clean-Java-solution-with-comments-recursive.
	 * 
	 * Other code:
	 * https://leetcode.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/discuss/34851/My-JAVA-recursive-answer-BEAT-92.9-2ms
	 */
	public TreeNode buildTree_recursive2(int[] inorder, int[] postorder) {
		return buildTree_recursive2(inorder, inorder.length - 1, 0, 
				postorder, postorder.length - 1);
	}

	private TreeNode buildTree_recursive2(int[] inorder, int inStart, int inEnd, 
			int[] postorder, int postStart) {
		
		// inStart < inEnd also works
		if (postStart < 0 || inStart < inEnd)
			return null;

		// The last element in postorder is the root.
		TreeNode root = new TreeNode(postorder[postStart]);

		// find the index of the root from inorder. Iterating from the end.
		int rIndex = inStart;
		for (int i = inStart; i >= inEnd; i--) {
			if (inorder[i] == postorder[postStart]) {
				rIndex = i;
				break;
			}
		}
		
		// build right and left subtrees. Again, scanning from the end to find the
		// sections.
		root.right = buildTree_recursive2(inorder, inStart, rIndex + 1, 
				postorder, postStart - 1);
		root.left = buildTree_recursive2(inorder, rIndex - 1, inEnd, 
				postorder, postStart - (inStart - rIndex) - 1);
		return root;
	}
	
	/*
	 * The following 2 variables and 2 functions are from this link.
	 * https://leetcode.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/discuss/338524/Java-using-HashMap
	 * 
	 * Other code:
	 * https://leetcode.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/discuss/166210/Super-simple-Java-solution-beat-100
	 */
	private int n_Map2 = 0;
	Map<Integer, Integer> hm_Map2 = new HashMap<>();

	public TreeNode buildTree_Map2(int[] inorder, int[] postorder) {
		n_Map2 = postorder.length - 1;
		for (int i = 0; i < inorder.length; i++) {
			hm_Map2.put(inorder[i], i);
		}
		
		return buildTree_Map2(postorder, inorder, 0, inorder.length - 1);
	}

	private TreeNode buildTree_Map2(int[] postorder, int[] inorder, 
			int start, int end) {
		
		if (start > end)
			return null;

		TreeNode root = new TreeNode(postorder[n_Map2]);
		n_Map2--;

		if (start == end)
			return root;

		int index = hm_Map2.get(root.val);

		root.right = buildTree_Map2(postorder, inorder, index + 1, end);
		root.left = buildTree_Map2(postorder, inorder, start, index - 1);

		return root;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/discuss/34782/My-recursive-Java-code-with-O(n)-time-and-O(n)-space/196579
	 * 
	 * Pass TreeNode to each helper method to indicate whether to finish
	 * 
	 * Other code:
	 * https://leetcode.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/discuss/34782/My-recursive-Java-code-with-O(n)-time-and-O(n)-space/33107
	 */
	public TreeNode buildTree_noMap(int[] inorder, int[] postorder) {
		// use array so it can pass by reference
		int[] inIdx = new int[1], postIdx = new int[1];

		postIdx[0] = postorder.length - 1;
		inIdx[0] = inorder.length - 1;

		return helper_noMap(inorder, inIdx, postorder, postIdx, null);
	}

	// finish node means end of current subtree
	// for example, postorder = [9,15,7,20,3], numbers just before 3 is it's right
	// subtree (20, 7, 15), and their finish node is 3.
	private TreeNode helper_noMap(int[] inorder, int[] inIdx, 
			int[] postorder, int[] postIdx, TreeNode finish) {
		
		if (postIdx[0] < 0 || (finish != null && inorder[inIdx[0]] == finish.val)) {
			return null;
		}

		TreeNode root = new TreeNode(postorder[postIdx[0]--]);

		root.right = helper_noMap(inorder, inIdx, postorder, postIdx, root);

		inIdx[0]--;

		root.left = helper_noMap(inorder, inIdx, postorder, postIdx, finish);

		return root;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/discuss/462619/Easy-to-understand-Java-solution
	 */
	public TreeNode buildTree_Map3(int[] inorder, int[] postorder) {
		Map<Integer, Integer> inMap = new HashMap<Integer, Integer>();
		for (int i = 0; i < inorder.length; i++) {
			inMap.put(inorder[i], i);
		}
		
		ArrayList<Integer> post = new ArrayList<>();
		for (int i = 0; i < postorder.length; i++) {
			post.add(postorder[i]);
		}
		
		TreeNode root = buildTree_Map3(post, 0, inorder.length - 1, inMap);
		return root;
	}

	public TreeNode buildTree_Map3(ArrayList<Integer> post, int inStart, 
			int inEnd, Map<Integer, Integer> inMap) {
		
		if (inStart > inEnd)
			return null;

		TreeNode root = new TreeNode(post.get(post.size() - 1));
		int inRoot = inMap.get(root.val);
		post.remove(post.size() - 1);

		root.right = buildTree_Map3(post, inRoot + 1, inEnd, inMap);
		root.left = buildTree_Map3(post, inStart, inRoot - 1, inMap);

		return root;
	}
	
	/*
	 * https://leetcode.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/discuss/34807/Java-iterative-solution-with-explanation
	 * 
	 * Instead of scanning the preorder array from beginning to end and using inorder 
	 * array as a kind of mark, in this question, the key point is to scanning the 
	 * postorder array from end to beginning and also use inorder array from end to 
	 * beginning as a mark. 
	 * The core idea is: Starting from the last element of the postorder and inorder 
	 * array, we put elements from postorder array to a stack and each one is the 
	 * right child of the last one until an element in postorder array is equal to 
	 * the element on the inorder array. Then, we pop as many as elements we can from 
	 * the stack and decrease the mark in inorder array until the peek() element is 
	 * not equal to the mark value or the stack is empty. Then, the new element that 
	 * we are gonna scan from postorder array is the left child of the last element 
	 * we have popped out from the stack.
	 * 
	 * Other code:
	 * https://leetcode.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/discuss/34807/Java-iterative-solution-with-explanation/33119
	 * https://leetcode.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/discuss/34807/Java-iterative-solution-with-explanation/122507
	 */
	public TreeNode buildTree_iterative(int[] inorder, int[] postorder) {
		if (inorder.length == 0 || postorder.length == 0)
			return null;
		
		int ip = inorder.length - 1;
		int pp = postorder.length - 1;

		Stack<TreeNode> stack = new Stack<TreeNode>();
		TreeNode prev = null;
		TreeNode root = new TreeNode(postorder[pp]);
		stack.push(root);
		
		pp--;

		while (pp >= 0) {
			while (!stack.isEmpty() && stack.peek().val == inorder[ip]) {
				prev = stack.pop();
				ip--;
			}
			
			TreeNode newNode = new TreeNode(postorder[pp]);
			if (prev != null) {
				prev.left = newNode;
			} 
			else if (!stack.isEmpty()) {
				TreeNode currTop = stack.peek();
				currTop.right = newNode;
			}
			
			stack.push(newNode);
			prev = null;
			pp--;
		}
		return root;
	}

	/**
     * Python collections
     * 
     * https://leetcode.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/discuss/221681/Don't-use-top-voted-Python-solution-for-interview-here-is-why.
     * https://leetcode.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/discuss/34814/A-Python-recursive-solution
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/discuss/34803/Sharing-my-straightforward-recursive-solution
     * https://leetcode.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/discuss/34911/My-C%2B%2B-Solution
     * https://leetcode.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/discuss/34799/C%2B%2B-O(n)-DFS-solution-beath-91-submissions
     * https://leetcode.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/discuss/34905/O(n)-c%2B%2B-recursive-solution-23ms-with-comments
     * https://leetcode.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/discuss/34849/My-comprehension-of-O(n)-solution-from-%40hongzhi
     */

}
