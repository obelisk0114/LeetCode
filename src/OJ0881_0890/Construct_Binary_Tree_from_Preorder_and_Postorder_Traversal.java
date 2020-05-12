package OJ0881_0890;

import definition.TreeNode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Stack;

public class Construct_Binary_Tree_from_Preorder_and_Postorder_Traversal {
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-postorder-traversal/discuss/168714/JAVA-recursive-Time%3A-O(n)-Space%3A-O(n)-solution.
	 * 
	 * 1: preprocessing. 2: Find the correct index
	 * preorder: [root], left, right. postorder: left, right, [root]
	 * 
	 * Rf :
	 * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-postorder-traversal/discuss/161281/Clean-Java-O(N)
	 * 
	 * Other code:
	 * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-postorder-traversal/discuss/210913/Java-Solutions
	 */
	public TreeNode constructFromPrePost_Map(int[] pre, int[] post) {
		if (pre == null || pre.length == 0 || post == null || post.length == 0 
				|| pre.length != post.length) {
			return null;
		}

		HashMap<Integer, Integer> postMap = new HashMap<>();
		for (int i = 0; i < post.length; i++) {
			postMap.put(post[i], i);
		}

		return buildTreeHelper_Map(pre, 0, pre.length - 1, post, 0, postMap);
	}

	public TreeNode buildTreeHelper_Map(int[] pre, int preStart, int preEnd, 
			int[] post, int postStart, HashMap<Integer, Integer> postMap) {
		
		if (preStart > preEnd) {
			return null;
		}

		TreeNode node = new TreeNode(pre[preStart]);

		if (preStart == preEnd) {
			return node;
		}

		int leftChildPostIndex = postMap.get(pre[preStart + 1]);
		int numLeft = leftChildPostIndex - postStart + 1;

		node.left = buildTreeHelper_Map(pre, preStart + 1, preStart + numLeft, 
				post, postStart, postMap);
		node.right = buildTreeHelper_Map(pre, preStart + numLeft + 1, preEnd, 
				post, leftChildPostIndex + 1, postMap);
		return node;
	}
	
	/*
	 * The following 3 functions are from this link.
	 * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-postorder-traversal/discuss/161372/Java-Python-Divide-and-Conquer-with-Explanation
	 * 
	 * Given preorder : 1 2 4 5 3 6;     postorder: 4 5 2 6 3 1.
	 * 1 is root and 2 is its left child. Since 2 is the root of the left subtree, all 
	 * elements in front of 2 in post[] must be in the left subtree also.
	 * 
	 * We recursively follow the above approach.
	 * Please note that pre[preStart + 1] may also be the root of the right subtree 
	 * if there is no left subtree at all in the orginal tree. Since we are asked to 
	 * generate one possible original tree, I assume pre[preStart + 1] to be the left 
	 * subtree root always.
	 * 
	 * Other code:
	 * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-postorder-traversal/discuss/161372/Java-Python-Divide-and-Conquer-with-Explanation/179456
	 * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-postorder-traversal/discuss/416491/Intuitive-Java-Solution-With-Explanation
	 * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-postorder-traversal/discuss/161268/C++JavaPython-One-Pass-Real-O(N)/174387
	 */
	public TreeNode constructFromPrePost_divide(int[] pre, int[] post) {
        return constructFromPrePost_divide(pre, 0, pre.length - 1, 
        		post, 0, pre.length - 1);
    }
    
    private TreeNode constructFromPrePost_divide(int[] pre, int preStart, int preEnd, 
    		int[] post, int postStart, int postEnd) {
    	
        // Base cases.
        if (preStart > preEnd) {
            return null;
        }
        if (preStart == preEnd) {
            return new TreeNode(pre[preStart]);
        }
        
        // Build root.
        TreeNode root = new TreeNode(pre[preStart]);
        
        // Locate left subtree.
        int leftSubRootInPre = preStart + 1; 
        int leftSubRootInPost = findLeftSubRootInPost_divide(pre[leftSubRootInPre], 
        		post, postStart, postEnd);
        int leftSubEndInPre = leftSubRootInPre + (leftSubRootInPost - postStart);
        
        // Divide.
        TreeNode leftSubRoot = 
        		constructFromPrePost_divide(pre, leftSubRootInPre, leftSubEndInPre, 
                                     post, postStart, leftSubRootInPost);  
        TreeNode rightSubRoot = 
        		constructFromPrePost_divide(pre, leftSubEndInPre + 1, preEnd, 
                                     post, leftSubRootInPost + 1, postEnd - 1);
        
        // Conquer.      
        root.left = leftSubRoot;
        root.right = rightSubRoot;
        
        return root;
    }
    
    private int findLeftSubRootInPost_divide(int leftSubRootVal, int[] post, 
    		int postStart, int postEnd) {
    	
        for (int i = postStart; i <= postEnd; i++) {
            if (post[i] == leftSubRootVal) {
                return i;
            }
        }
        
        throw new IllegalArgumentException();
    }
    
    /*
     * The following 2 variables and function are from this link.
     * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-postorder-traversal/discuss/161268/C%2B%2BJavaPython-One-Pass-Real-O(N)
     * 
     * Create a node TreeNode(pre[preIndex]) as the root.
     * 
     * Because root node will be lastly iterated in post order,
     * if root.val == post[posIndex],
     * it means we have constructed the whole tree,
     * 
     * If we haven't completed constructed the whole tree,
     * So we recursively constructFromPrePost for left sub tree and right sub tree.
     * 
     * And finally, we'll reach the posIndex that root.val == post[posIndex].
     * We increment posIndex and return our root node.
     * 
     * Time O(N), as we iterate both pre index and post index only once.
     * Space O(height), depending on the height of constructed tree.
     * 
     * When we traverse the post and visit post[i], it means all child nodes in the 
     * tree of post[i] is visited already.
     * Thus, we can use pre to construct the tree while use post to figure out 
     * whether the node we visit still has unvisited children.
     * 
     * Rf :
     * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-postorder-traversal/discuss/161268/C++JavaPython-One-Pass-Real-O(N)/189303
     * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/discuss/34543/Simple-O(n)-without-map
     * 
     * Other code:
     * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-postorder-traversal/discuss/446833/Java-1-ms-recursive-solution
     */
    int preIndex_point_stop = 0, posIndex_point_stop = 0;

	public TreeNode constructFromPrePost_point_stop(int[] pre, int[] post) {
		TreeNode root = new TreeNode(pre[preIndex_point_stop++]);
		
		if (root.val != post[posIndex_point_stop])
			root.left = constructFromPrePost_point_stop(pre, post);
		if (root.val != post[posIndex_point_stop])
			root.right = constructFromPrePost_point_stop(pre, post);
		
		posIndex_point_stop++;
		return root;
	}
	
	/*
	 * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-postorder-traversal/discuss/164015/Straight-forward-recursion-in-java.
	 * 
	 * preorder: (root) (left branch) (right branch)
	 * postorder: (left branch) (right branch) (root)
	 * 
	 * Let's say the left branch has L nodes. We know the head node of that left 
	 * branch is pre[1], but it also occurs last in the postorder representation of 
	 * the left branch. So pre[1] = post[L-1] (because of uniqueness of the node 
	 * values.) Hence, L = post.indexOf(pre[1]) + 1.
	 * 
	 * Now in our recursion step, the left branch is represnted by pre[1 : L+1] and 
	 * post[0 : L], while the right branch is represented by pre[L+1 : N] and 
	 * post[L : N-1].
	 * 
	 * Rf :
	 * https://leetcode.com/articles/construct-binary-tree-from-preorder-and-postorder-/
	 */
	public TreeNode constructFromPrePost_copy(int[] pre, int[] post) {
		int n = pre.length;
		if (n == 0)
			return null;
		
		TreeNode root = new TreeNode(pre[0]);
		if (n > 1) {
			int x = pre[1];
			int j = 0;
			while (post[j] != x)
				j++;
			
			root.left = constructFromPrePost_copy(Arrays.copyOfRange(pre, 1, j + 2), 
					Arrays.copyOfRange(post, 0, j + 1));
			root.right = constructFromPrePost_copy(Arrays.copyOfRange(pre, j + 2, n),
					Arrays.copyOfRange(post, j + 1, n - 1));
		}
		return root;
	}
	
	/*
	 * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-postorder-traversal/discuss/161268/C%2B%2BJavaPython-One-Pass-Real-O(N)
	 * 
	 * We will preorder generate TreeNodes, push them to stack and postorder pop them 
	 * out.
	 * 
	 * 1. Iterate on pre array and construct node one by one.
	 * 2. stack save the current path of tree.
	 * 3. node = new TreeNode(pre[i]), if not left child, add node to the left. 
	 *    otherwise add it to the right.
	 * 4. If we meet a same value in the pre and post, it means we complete the 
	 *    construction for current subtree. We pop it from stack.
	 *    
	 * When we meet a node value pre[i] equals to the current post[j], it means we 
	 * have completed building the subtree of pre[i]. So we should not continue to 
	 * add child nodes to that subtree. Instead, we should pop that subtree and 
	 * continues to the path where we can add child nodes.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-postorder-traversal/discuss/161268/C++JavaPython-One-Pass-Real-O(N)/311443
	 * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-postorder-traversal/discuss/161268/C++JavaPython-One-Pass-Real-O(N)/189303
	 * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-postorder-traversal/discuss/161268/C++JavaPython-One-Pass-Real-O(N)/249280
	 */
	public TreeNode constructFromPrePost_iterative(int[] pre, int[] post) {
		Deque<TreeNode> s = new ArrayDeque<>();
		s.offer(new TreeNode(pre[0]));
		for (int i = 1, j = 0; i < pre.length; ++i) {
			TreeNode node = new TreeNode(pre[i]);
			while (s.getLast().val == post[j]) {
				s.pollLast();
				j++;
			}
			
			if (s.getLast().left == null)
				s.getLast().left = node;
			else
				s.getLast().right = node;
			
			s.offer(node);
		}
		return s.getFirst();
	}
	
	/*
	 * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-postorder-traversal/discuss/161312/Simple-Java-using-Stack-with-explanation
	 * 
	 * 1. We know that root always comes first in PreOrder but Last in Post Order. So, 
	 *    if we read PreOrder list from start, and check the position of the node in 
	 *    Post Order list, whatever nodes come before its index in PostOrder list is 
	 *    sub Node of this node (this node is the parent node for sure).
	 * 2. PreOrder always start from ROOT, LEFT, and RIGHT. So if the current node in 
	 *    PreOrder list has no left node, the next node in the list is its left node 
	 *    for sure (please refer to the NOTE), unless the next node index in PostOrder 
	 *    list is higher than the current node, since we know that in PostOrder we 
	 *    have LEFT, RIGHT, ROOT. So what ever node comes after the current node in 
	 *    our PreOrder list is a sub node of the current node as long as it's index is 
	 *    smaller than the current node index in our PostOrder list.
	 *    
	 * Note: We assume it is the left node, as it may have a right node which comes 
	 * later. Since PreOrder is Root, Left, Right, and we start reading the list from 
	 * 0 index, so we do not know whether the right node exists or not. What if it has 
	 * a right node and we set the first sub node as its right node.
	 */
	public TreeNode constructFromPrePost_stack(int[] pre, int[] post) {
		if (post == null || post.length == 0 || pre == null || pre.length == 0)
			return null;
		
		int len = post.length;
		
		// record the indexes
		HashMap<Integer, Integer> index = new HashMap<>();
		for (int i = 0; i < len; i++)
			index.put(post[i], i);
		
		Stack<TreeNode> stack = new Stack<>();
		int i = 0;
		
		// the first node in our PreOrder list is always the ROOT Node
		TreeNode root = new TreeNode(pre[i++]);
		stack.push(root);
		
		// start reading the PreOrder list and check the indexes with PostOrder list
		while (i < len) {
			TreeNode next = new TreeNode(pre[i]);
			
			// we remove nodes with smaller index from stack as they cannot be 
			// the next nodes' parent
			while (index.get(stack.peek().val) < index.get(next.val))
				stack.pop();
			
			TreeNode existing = stack.pop();
			
			// left always come first
			if (existing.left == null) {
				existing.left = next;
				
				// we push the existing node again into the stack 
				// as it may have a right node
				stack.push(existing);
				stack.push(next);
			} 
			else {
				existing.right = next;
				
				// we do not push the existing node anymore, 
				// as it's left and right nodes already found
				stack.push(next);
			}
			
			i++;
		}
		return root;
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-postorder-traversal/discuss/161651/Easy-Python-Recursive-Solution-with-Explanation
     * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-postorder-traversal/discuss/256466/Python-Recursion-DandC
     * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-postorder-traversal/discuss/177793/Python-Easy-to-understand-Recursion
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-postorder-traversal/discuss/161286/C%2B%2B-O(N)-recursive-solution
     * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-postorder-traversal/discuss/161287/Recursive-C%2B%2B-O(N-log-N)
     * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-postorder-traversal/discuss/469702/C%2B%2B-4ms-98-O(N)-solution-O(N)-space
     * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-postorder-traversal/discuss/478961/Summary-of-reconstruction-from-traversal
     */
	
	/**
	 * C# collections
	 * 
	 * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-postorder-traversal/discuss/161379/Recursive-easy-to-understand-with-explanation-C
	 * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-postorder-traversal/discuss/165409/One-Pass-O(N)-Recursive-solution-beats-100-C-submissions
	 * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-postorder-traversal/discuss/371224/C-Implementation
	 */
	
	/**
	 * JavaScript collections
	 * 
	 * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-postorder-traversal/discuss/161282/javascript-solution%3A-use-recursion
	 */

}
