package OJ0651_0660;

import definition.TreeNode;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

public class Two_Sum_IV_Input_is_a_BST {
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/two-sum-iv-input-is-a-bst/discuss/106129/Java-solution-tree-traversal-and-two-pointers
	 * 
	 * 1. Get a sorted array by in-order traversing a BST.
	 * 2. Solve Two Sum problem in a sorted array. (2 pointers)
	 */
	public boolean findTarget(TreeNode root, int k) {
		if (root == null)
			return false;

		List<Integer> list = new ArrayList<>();
		inOrder(root, list);

		int i = 0, j = list.size() - 1;
		while (i < j) {
			int sum = list.get(i) + list.get(j);
			if (sum == k)
				return true;
			if (sum < k) {
				i++;
			} 
			else {
				j--;
			}
		}

		return false;
	}

	private void inOrder(TreeNode root, List<Integer> list) {
		if (root == null)
			return;

		inOrder(root.left, list);
		list.add(root.val);
		inOrder(root.right, list);
	}

	/*
	 * The following 3 functions are from this link.
	 * https://leetcode.com/problems/two-sum-iv-input-is-a-bst/discuss/106061/Java-Simple-AC-with-Time-O(n)-Space-O(log-n)-in-Average
	 * 
	 * Use the stack and search just like 2sum without dumping all the value out in 
	 * the beginning.
	 * -- Time/Space: n/logn in average
	 */
	public boolean findTarget_Stack(TreeNode root, int k) {
		if (root == null)
			return false;
		
		Stack<TreeNode> l_stack = new Stack<>();
		Stack<TreeNode> r_stack = new Stack<>();
		
		stackAdd(l_stack, root, true);
		stackAdd(r_stack, root, false);
		while (l_stack.peek() != r_stack.peek()) {
			int n = l_stack.peek().val + r_stack.peek().val;
			if (n == k) {
				return true;
			} 
			else if (n > k) {
				stackNext(r_stack, false);
			} 
			else {
				stackNext(l_stack, true);
			}
		}
		return false;
	}

	private void stackAdd(Stack<TreeNode> stack, TreeNode node, boolean isLeft) {
		while (node != null) {
			stack.push(node);
			node = (isLeft) ? node.left : node.right;
		}
	}

	private void stackNext(Stack<TreeNode> stack, boolean isLeft) {
		TreeNode node = stack.pop();
		if (isLeft) {
			stackAdd(stack, node.right, isLeft);
		} 
		else {
			stackAdd(stack, node.left, isLeft);
		}
	}
	
	// The following 2 functions are by myself.
	public boolean findTarget_self(TreeNode root, int k) {
        List<Integer> list = new ArrayList<Integer>();
        addList(root, list);
        int start = 0;
        int end = list.size() - 1;
        while (start < end) {
            if (list.get(start) + list.get(end) == k)
                return true;
            else if (list.get(start) + list.get(end) > k) {
                end--;
            }
            else {
                start++;
            }
        }
        return false;
    }
    private void addList(TreeNode root, List<Integer> list) {
        if (root.right == null && root.left == null) {
            list.add(root.val);
        }
        else if (root.left == null) {
            list.add(root.val);
            addList(root.right, list);
        }
        else if (root.right == null) {
            addList(root.left, list);
            list.add(root.val);
        }
        else {
            addList(root.left, list);
            list.add(root.val);
            addList(root.right, list);
        }
    }
    
    /*
     * The following 2 functions are from this link.
     * https://leetcode.com/problems/two-sum-iv-input-is-a-bst/discuss/106059/JavaC++-Three-simple-methods-choose-one-you-like
     * 
     * The idea is to use a hashtable to save the values of the nodes in the BST. 
     * Each time when we insert the value of a new node into the hashtable, we check 
     * if the hashtable contains k - node.val.
     * 
     * Time Complexity: O(n), Space Complexity: O(n).
     */
	public boolean findTarget_HashSet(TreeNode root, int k) {
		HashSet<Integer> set = new HashSet<>();
		return dfs(root, set, k);
	}
	private boolean dfs(TreeNode root, HashSet<Integer> set, int k) {
		if (root == null)
			return false;
		if (set.contains(k - root.val))
			return true;
		set.add(root.val);
		return dfs(root.left, set, k) || dfs(root.right, set, k);
	}
    
    /*
     * https://leetcode.com/problems/two-sum-iv-input-is-a-bst/discuss/106110/Java-Code-O(n)-time-O(lg(n))-space-using-DFS-+-Stack
     * 
     * We only need O(lg(n)) space, by implement two iterators using stack. With those 
     * two iterators in hand, now we can use two pointers to solve the problem.
     * So the overall performace is: O(n)/O(lg(n)).
     * 
     * We start from the 2 sides of the numbers, in this case, the 2 sides would be 
     * the leftmost TreeNode and the rightmost TreeNode.
     * During traversal, we have to keep the parent status of the nodes.
     * 
     * A tricky point of this idea is how do we decide the stop point. Since there is 
     * no redundant values in the tree, we can use left.val == right.val to indicates 
     * that we have searched every node in the tree since now the pointers have met 
     * each other.
     * 
     * Rf : https://leetcode.com/problems/two-sum-iv-input-is-a-bst/discuss/106071/Iterative-solution-with-O(n)-time-O(logn)-space-with-detailed-explanation.-Only-traverse-the-binary-tree-once!!!
     */
    public boolean findTarget_stack2(TreeNode root, int k) {
        Stack<TreeNode> stackL = new Stack<TreeNode>();  // iterator 1 that gets next smallest value
        Stack<TreeNode> stackR = new Stack<TreeNode>();  // iterator 2 that gets next largest value
        
        for(TreeNode cur = root; cur != null; cur = cur.left)  
            stackL.push(cur);
        for(TreeNode cur = root; cur != null; cur = cur.right)  
            stackR.push(cur);
            
		while (stackL.size() != 0 && stackR.size() != 0 && stackL.peek() != stackR.peek()) {
			int tmpSum = stackL.peek().val + stackR.peek().val;
			if (tmpSum == k)
				return true;
			else if (tmpSum < k)
				for (TreeNode cur = stackL.pop().right; cur != null; cur = cur.left)
					stackL.push(cur);
			else
				for (TreeNode cur = stackR.pop().left; cur != null; cur = cur.right)
					stackR.push(cur);
		}
        
        return false;
    }
    
    /*
     * The following 3 functions are from this link.
     * https://leetcode.com/problems/two-sum-iv-input-is-a-bst/discuss/106059/JavaC++-Three-simple-methods-choose-one-you-like
     * 
     * The idea is to use binary search method. 
     * For each node, we check if k - node.val exists in this BST.
     * 
     * Time Complexity: O(nlogn), Space Complexity: O(h). 
     * h is the height of the tree, which is logn at best case, and n at worst case.
     */
    public boolean findTarget_search(TreeNode root, int k) {
        return dfs(root, root,  k);
    }
    
    public boolean dfs(TreeNode root,  TreeNode cur, int k) {
        if (cur == null)
        	return false;
        
        return search(root, cur, k - cur.val) || dfs(root, cur.left, k) || dfs(root, cur.right, k);
    }
    
    public boolean search(TreeNode root, TreeNode cur, int value) {
        if (root == null)
        	return false;
        
        return (root.val == value) && (root != cur) 
            || (root.val < value) && search(root.right, cur, value) 
                || (root.val > value) && search(root.left, cur, value);
    }
    
}
