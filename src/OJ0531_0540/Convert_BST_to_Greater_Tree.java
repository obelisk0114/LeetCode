package OJ0531_0540;

import definition.TreeNode;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;

public class Convert_BST_to_Greater_Tree {
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/convert-bst-to-greater-tree/discuss/100543/Java-Solution-7-liner-reversed-traversal/142181
	 * 
	 * We can get correct sum if we do reversed inorder traversal - visit right child 
	 * node, then node and then visit left child. When we visit the node we 
	 * accumulate the sum and use it on next step.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/convert-bst-to-greater-tree/discuss/378399/Java-recursion-in-order-traversal-1ms-explained
	 * 
	 * Other code:
	 * https://leetcode.com/problems/convert-bst-to-greater-tree/discuss/100543/Java-Solution-7-liner-reversed-traversal/275372
	 */
	public TreeNode convertBST_recursive3(TreeNode root) {
		int[] rightSum = new int[1];
		dfsHelper_recursive3(root, rightSum);
		return root;
	}

	public void dfsHelper_recursive3(TreeNode root, int[] rightSum) {
		if (root == null)
			return;
		
		dfsHelper_recursive3(root.right, rightSum);
		
		root.val += rightSum[0];
		rightSum[0] = root.val;
		
		dfsHelper_recursive3(root.left, rightSum);
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/convert-bst-to-greater-tree/discuss/100506/Java-Recursive-O(n)-time/104622
	 * 
	 * a "reversed" version of inorder traversal. right --> root --> left.
	 * 
	 * sum : all nodes which we have traversed thus far
	 * helper return : root.val + sum of all nodes greater than root
	 * 
	 * Rf :
	 * https://leetcode.com/problems/convert-bst-to-greater-tree/discuss/100506/Java-Recursive-O(n)-time/104625
	 * 
	 * Other code:
	 * https://leetcode.com/problems/convert-bst-to-greater-tree/discuss/100506/Java-Recursive-O(n)-time/104632
	 * https://leetcode.com/problems/convert-bst-to-greater-tree/discuss/148848/JAVA-9ms-Beats-99.96-without-extra-space
	 */
	public TreeNode convertBST_recursive(TreeNode root) {
		int sum = 0;
		helper_recursive(root, sum);
		return root;
	}

	public int helper_recursive(TreeNode root, int sum) {
		if (root == null) {
			return sum;
		}
		
		int right = helper_recursive(root.right, sum);
		
		root.val += right;
		
		int left = helper_recursive(root.left, root.val);
		return left;
	}
	
	/*
	 * The following variable and function are from this link.
	 * https://leetcode.com/problems/convert-bst-to-greater-tree/discuss/100543/Java-Solution-7-liner-reversed-traversal
	 * 
	 * Reversely traverse the tree and keep a sum of all previously visited values. 
	 * Because it's a BST, values seen before are all greater than current node.val. 
	 * 
	 * We can get correct sum if we do reversed inorder traversal - visit right child 
	 * node, then node and then visit left child. When we visit the node we 
	 * accumulate the sum and use it on next step.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/convert-bst-to-greater-tree/discuss/378399/Java-recursion-in-order-traversal-1ms-explained
	 * 
	 * Other code:
	 * https://leetcode.com/problems/convert-bst-to-greater-tree/discuss/1173272/Java-Recursive-Inorder-solution-beats-100-users-in-time-and-99-in-memory.
	 * https://leetcode.com/problems/convert-bst-to-greater-tree/discuss/100516/Java-Three-O(n)-Methods%3A-Recursive-Iterative-and-Morris-Traversal
	 */
	int sum_reverse_inOrder = 0;

	public TreeNode convertBST_reverse_inOrder(TreeNode root) {
		// base case
		if (root == null)
			return null;

		// do the inversive inorder traversal - check right, center and left nodes
		convertBST_reverse_inOrder(root.right);

		root.val += sum_reverse_inOrder;
		sum_reverse_inOrder = root.val;

		convertBST_reverse_inOrder(root.left);

		return root;
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/convert-bst-to-greater-tree/discuss/100506/Java-Recursive-O(n)-time
	 * 
	 * Since this is a BST, we can do a reverse inorder traversal to traverse the 
	 * nodes of the tree in descending order. In the process, we keep track of the 
	 * running sum of all nodes which we have traversed thus far.
	 */
	int sum_recursive4 = 0;

	public TreeNode convertBST_recursive4(TreeNode root) {
		convert_recursive4(root);
		return root;
	}

	public void convert_recursive4(TreeNode cur) {
		if (cur == null)
			return;
		
		convert_recursive4(cur.right);
		
		cur.val += sum_recursive4;
		sum_recursive4 = cur.val;
		
		convert_recursive4(cur.left);
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/convert-bst-to-greater-tree/discuss/100619/Java-6-lines
	 * 
	 * Reversed inorder traversal.
	 */
	public TreeNode convertBST_recursive2(TreeNode root) {
		if (root == null)
			return null;
		
		DFS_recursive2(root, 0);
		return root;
	}

	public int DFS_recursive2(TreeNode root, int preSum) {
		if (root.right != null)
			preSum = DFS_recursive2(root.right, preSum);
		
		root.val = root.val + preSum;
		
		return (root.left != null) ? DFS_recursive2(root.left, root.val) : root.val;
	}
	
	/*
	 * https://leetcode.com/problems/convert-bst-to-greater-tree/discuss/100516/Java-Three-O(n)-Methods%3A-Recursive-Iterative-and-Morris-Traversal
	 * 
	 * The basic idea is to do a reversed inorder traversal. When we visit a node 
	 * we add the sum of all previous nodes (to the right) to its value and also 
	 * update the sum.
	 * 
	 * So long as there are unvisited nodes in the stack or node does not point to 
	 * null, we push all of the nodes along the path to the rightmost leaf onto the 
	 * stack. This is equivalent to always processing the right subtree first in the 
	 * recursive solution, and is crucial for the guarantee of visiting nodes in 
	 * order of decreasing value. Next, we visit the node on the top of our stack, 
	 * and consider its left subtree. This is just like visiting the current node 
	 * before recursing on the left subtree in the recursive solution. Eventually, 
	 * our stack is empty and node points to the left null child of the tree's 
	 * minimum value node, so the loop terminates.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/convert-bst-to-greater-tree/solution/
	 * https://leetcode.com/problems/convert-bst-to-greater-tree/discuss/100516/Java-Three-O(n)-Methods:-Recursive-Iterative-and-Morris-Traversal/104635
	 * 
	 * Other code:
	 * https://leetcode.com/problems/convert-bst-to-greater-tree/discuss/100506/Java-Recursive-O(n)-time/104630
	 */
	public TreeNode convertBST_iterative2(TreeNode root) {
		if (root == null)
			return null;
		
		int sum = 0;
		Stack<TreeNode> stack = new Stack<>();
		TreeNode cur = root;
		
		while (!stack.isEmpty() || cur != null) {
			// push all nodes up to (and including) this subtree's maximum on
            // the stack.
			while (cur != null) {
				stack.push(cur);
				cur = cur.right;
			}
			
			cur = stack.pop();
			
			int tmp = cur.val;
			cur.val += sum;
			sum += tmp;
			
			// all nodes with values between the current and its parent lie in
            // the left subtree. 
			cur = cur.left;
		}
		return root;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/convert-bst-to-greater-tree/solution/
	 * Approach #3 Reverse Morris In-order Traversal
	 * 
	 * The key to such a solution would be a way to visit nodes in descending order, 
	 * keeping a sum of all values that we have already visited and adding that sum 
	 * to the node's values as we traverse the tree. This method for tree traversal 
	 * is known as a reverse in-order traversal.
	 * 
	 * First, we initialize node, which points to the root. Until node points to null 
	 * (the left null of the tree's minimum-value node), we repeat the following.
	 * 
	 * First, consider whether the current node has a right subtree. If it does not 
	 * have a right subtree, then there is no unvisited node with a greater value, so 
	 * we can visit this node and move into the left subtree. If it does have a right 
	 * subtree, then there is at least one unvisited node with a greater value, and 
	 * thus we must visit first go to the right subtree. 
	 * 
	 * To do so, we obtain a reference to the in-order successor (the smallest-value 
	 * node larger than the current) via our helper function getSuccessor. This 
	 * successor node is the node that must be visited immediately before the current 
	 * node, so it by definition has a null left pointer (otherwise it would not be 
	 * the successor). Therefore, when we first find a node's successor, we 
	 * temporarily link it (via its left pointer) to the node and proceed to the 
	 * node's right subtree. Then, when we finish visiting the right subtree, the 
	 * leftmost left pointer in it will be our temporary link that we can use to 
	 * escape the subtree. After following this link, we have returned to the 
	 * original node that we previously passed through, but did not visit. This time, 
	 * when we find that the successor's left pointer loops back to the current node, 
	 * we know that we have visited the entire right subtree, so we can now erase the 
	 * temporary link and move into the left subtree.
	 * 
	 * First, note that getSuccessor is called at most twice per node. On the first 
	 * invocation, the temporary link back to the node in question is created, and on 
	 * the second invocation, the temporary link is erased. Then, the algorithm steps 
	 * into the left subtree with no way to return to the node. Therefore, each edge 
	 * can only be traversed 3 times: once when we move the node pointer, and once 
	 * for each of the two calls to getSuccessor.
	 */
	private TreeNode getSuccessor_Morris2(TreeNode node) {
        TreeNode succ = node.right;
        while (succ.left != null && succ.left != node) {
            succ = succ.left;
        }
        return succ;
    }

    public TreeNode convertBST_Morris2(TreeNode root) {
        int sum = 0;
        TreeNode node = root;

        while (node != null) {
            /* 
             * If there is no right subtree, then we can visit this node and
             * continue traversing left.
             */
            if (node.right == null) {
                sum += node.val;
                node.val = sum;
                
                node = node.left;
            }
            /* 
             * If there is a right subtree, then there is at least one node that
             * has a greater value than the current one. therefore, we must
             * traverse that subtree first.
             */
            else {
            	// Get the node with the smallest value greater than this one.
                TreeNode succ = getSuccessor_Morris2(node);
                
                /* 
                 * If the left subtree is null, then we have never been here before.
                 */
                if (succ.left == null) {
                    succ.left = node;
                    node = node.right;
                }
                /* 
                 * If there is a left subtree, it is a link that we created on a
                 * previous pass, so we should unlink it and visit this node.
                 */
                else {
                    succ.left = null;
                    
                    sum += node.val;
                    node.val = sum;
                    
                    node = node.left;
                }
            }
        }
        
        return root;
    }
	
	/*
	 * https://leetcode.com/problems/convert-bst-to-greater-tree/discuss/100516/Java-Three-O(n)-Methods%3A-Recursive-Iterative-and-Morris-Traversal
	 * 
	 * The basic idea is to do a reversed inorder traversal. When we visit a node 
	 * we add the sum of all previous nodes (to the right) to its value and also 
	 * update the sum.
	 * 
	 * First, we initialize node, which points to the root. Until node points to null 
	 * (the left null of the tree's minimum-value node), we repeat the following.
	 * 
	 * First, consider whether the current node has a right subtree. If it does not 
	 * have a right subtree, then there is no unvisited node with a greater value, so 
	 * we can visit this node and move into the left subtree. If it does have a right 
	 * subtree, then there is at least one unvisited node with a greater value, and 
	 * thus we must visit first go to the right subtree. 
	 * 
	 * To do so, we obtain a reference to the in-order successor (the smallest-value 
	 * node larger than the current) via our helper function getSuccessor. This 
	 * successor node is the node that must be visited immediately before the current 
	 * node, so it by definition has a null left pointer (otherwise it would not be 
	 * the successor). Therefore, when we first find a node's successor, we 
	 * temporarily link it (via its left pointer) to the node and proceed to the 
	 * node's right subtree. Then, when we finish visiting the right subtree, the 
	 * leftmost left pointer in it will be our temporary link that we can use to 
	 * escape the subtree. After following this link, we have returned to the 
	 * original node that we previously passed through, but did not visit. This time, 
	 * when we find that the successor's left pointer loops back to the current node, 
	 * we know that we have visited the entire right subtree, so we can now erase the 
	 * temporary link and move into the left subtree.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/convert-bst-to-greater-tree/solution/
	 * https://leetcode.com/problems/convert-bst-to-greater-tree/discuss/100516/Java-Three-O(n)-Methods:-Recursive-Iterative-and-Morris-Traversal/104635
	 * 
	 * Other code:
	 * https://leetcode.com/problems/convert-bst-to-greater-tree/discuss/100506/Java-Recursive-O(n)-time/121434
	 */
	public TreeNode convertBST_Morris(TreeNode root) {
		TreeNode cur = root;
		int sum = 0;
		while (cur != null) {
			if (cur.right == null) {
				int tmp = cur.val;
				cur.val += sum;
				sum += tmp;
				
				cur = cur.left;
			} 
			else {
				TreeNode prev = cur.right;
				while (prev.left != null && prev.left != cur)
					prev = prev.left;
				
				if (prev.left == null) {
					prev.left = cur;
					cur = cur.right;
				} 
				else {
					prev.left = null;
					
					int tmp = cur.val;
					cur.val += sum;
					sum += tmp;
					
					cur = cur.left;
				}
			}
		}
		return root;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/convert-bst-to-greater-tree/discuss/148848/JAVA-9ms-Beats-99.96-without-extra-space
	 */
	public TreeNode convertBST_iterative(TreeNode root) {
		Queue<TreeNode> queue = new LinkedList<TreeNode>();
		queue = insert_iterative(root, queue);
		
		int total = 0;
		while (queue.size() > 0) {
			TreeNode tree = queue.poll();
			tree.val += total;
			total = tree.val;
		}
		return root;
	}

	public Queue<TreeNode> insert_iterative(TreeNode root, Queue<TreeNode> queue) {
		if (root == null) {
			return queue;
		}
		
		queue = insert_iterative(root.right, queue);
		queue.offer(root);
		queue = insert_iterative(root.left, queue);
		
		return queue;
	}
	
	/*
	 * The following 2 functions are by myself.
	 * 
	 * process = false，累加 root.val 到 sum
	 * process = true，sum - root.val 是 node.val > root.val 的總和，所以 root.val 加上他
	 */
	public TreeNode convertBST_self2(TreeNode root) {
        int[] sum = { 0 };
        calculate_self2(root, sum, false);
        calculate_self2(root, sum, true);
        return root;
    }
    
    private void calculate_self2(TreeNode root, int[] sum, boolean process) {
        if (root == null) {
            return;
        }
        
        calculate_self2(root.left, sum, process);
        
        if (process) {
            sum[0] -= root.val;
            root.val += sum[0];
            
            // 上面 2 行其實是下面這 3 行
            // int tmp = root.val;
            // root.val = sum[0];
            // sum[0] -= tmp;
        }
        else {
            sum[0] += root.val;
        }
        
        calculate_self2(root.right, sum, process);
    }
	
	/*
	 * The following 2 functions are by myself.
	 */
	public TreeNode convertBST_self(TreeNode root) {
        if (root == null) {
            return root;
        }
        
        List<Integer> list = new ArrayList<>();
        Map<TreeNode, Integer> map = new HashMap<>();
        dfs_self(root, list, map);
        
        int[] suffixSum = new int[list.size()];
        for (int i = suffixSum.length - 2; i >= 0; i--) {
            suffixSum[i] = suffixSum[i + 1] + list.get(i + 1);
        }
        
        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.offerLast(root);
        
        while (!queue.isEmpty()) {
            TreeNode cur = queue.pollFirst();
            int curPosition = map.get(cur);
            cur.val += suffixSum[curPosition];
            
            if (cur.left != null) {
                queue.offerLast(cur.left);
            }
            
            if (cur.right != null) {
                queue.offerLast(cur.right);
            }
        }
        
        return root;
    }
    
    private void dfs_self(TreeNode root, List<Integer> list, 
    		Map<TreeNode, Integer> map) {
    	
        if (root == null) {
            return;
        }
        
        dfs_self(root.left, list, map);
        
        list.add(root.val);
        map.put(root, list.size() - 1);
        
        dfs_self(root.right, list, map);
    }
    
    /**
     * Python collections
     * 
     * https://leetcode.com/problems/convert-bst-to-greater-tree/discuss/1057484/Python-O(n)-inorder-traversal-explained
     * https://leetcode.com/problems/convert-bst-to-greater-tree/discuss/206689/python-simple-recursive-solution-without-global-variable
     * https://leetcode.com/problems/convert-bst-to-greater-tree/discuss/513712/Python-O(n)-by-DFS-approach.-80%2B-With-explanation
     * https://leetcode.com/problems/convert-bst-to-greater-tree/discuss/1057429/Python.-faster-than-100.00.-Explained-clear-and-Easy-understanding-solution.-O(n).-Recursive
     * https://leetcode.com/problems/convert-bst-to-greater-tree/discuss/100555/Python-Simple-with-Explanation
     * https://leetcode.com/problems/convert-bst-to-greater-tree/discuss/134313/python-simple-iterative-solution
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/convert-bst-to-greater-tree/discuss/1057416/C%2B%2B-or-Reverse-Inorder-Traversal-or-O(n)-0ms-Beats-100-or-Explanation
     * https://leetcode.com/problems/convert-bst-to-greater-tree/discuss/100610/c%2B%2B-solution-beats-100
     * https://leetcode.com/problems/convert-bst-to-greater-tree/discuss/1057507/C%2B%2B.-faster-than-97.80.-Explained-clear-and-Easy-understanding-solution.-O(n).-Recursive.
     * https://leetcode.com/problems/convert-bst-to-greater-tree/discuss/442707/C%2B%2B-Beats-90-8-Line-Solution-(with-Explanation)
     */
    
    /**
     * JavaScript collections
     * 
     * https://leetcode.com/problems/convert-bst-to-greater-tree/discuss/197556/Javascript-very-fast-solution
     * https://leetcode.com/problems/convert-bst-to-greater-tree/discuss/423908/Commented-or-Recursive-or-Simple-or-JavaScript-Solution
     */

}
