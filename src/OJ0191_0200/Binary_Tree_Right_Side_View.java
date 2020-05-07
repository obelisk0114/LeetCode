package OJ0191_0200;

import definition.TreeNode;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Binary_Tree_Right_Side_View {
	// by myself
	public List<Integer> rightSideView_self(TreeNode root) {
        List<Integer> ans = new ArrayList<>();
        if (root == null) {
            return ans;
        }
        
        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.offerLast(root);
        while (!queue.isEmpty()) {
            ans.add(queue.getLast().val);
            
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode cur = queue.pollFirst();
                
                if (cur.left != null) {
                    queue.offerLast(cur.left);
                }
                if (cur.right != null) {
                    queue.offerLast(cur.right);
                }
            }
        }
        return ans;
    }
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/binary-tree-right-side-view/discuss/56012/My-simple-accepted-solution(JAVA)
	 * 
	 * 1. Each depth of the tree only select one node.
	 * 2. View depth is current size of result list.
	 * 
	 * (1) the traverse of the tree is NOT standard pre-order traverse. 
	 *     It checks the RIGHT node first and then the LEFT
	 * (2) the line to check currDepth == result.size() makes sure the first element 
	 *     of that level will be added to the result list
	 * (3) if reverse the visit order, that is first LEFT and then RIGHT, it will 
	 *     return the left view of the tree.
	 * 
	 * Time: O(n), n is nunber of nodes in the binary tree
	 * Space: O(h), h is heigh of the binary tree
	 * 
	 * Rf :
	 * https://leetcode.com/problems/binary-tree-right-side-view/discuss/56012/My-simple-accepted-solution(JAVA)/57608
	 * https://leetcode.com/problems/binary-tree-right-side-view/discuss/56263/JAVA-solution-using-recursion
	 * https://leetcode.com/problems/binary-tree-right-side-view/discuss/56012/My-simple-accepted-solution(JAVA)/249990
	 * 
	 * Other code:
	 * https://leetcode.com/problems/binary-tree-right-side-view/discuss/56292/Recursive-solution
	 */
	public List<Integer> rightSideView_dfs(TreeNode root) {
		List<Integer> result = new ArrayList<Integer>();
		rightView_dfs(root, result, 0);
		return result;
	}

	public void rightView_dfs(TreeNode curr, List<Integer> result, int currDepth) {
		if (curr == null) {
			return;
		}
		if (currDepth == result.size()) {
			result.add(curr.val);
		}

		rightView_dfs(curr.right, result, currDepth + 1);
		rightView_dfs(curr.left, result, currDepth + 1);
	}
	
	/*
	 * https://leetcode.com/problems/binary-tree-right-side-view/discuss/559238/Java-BFSDFS-Solutions
	 * 
	 * Time: O(n), n is number of nodes in the binary tree
	 * Space: O(w), w is the width of binary tree
	 */
	public List<Integer> rightSideView_BFS(TreeNode root) {
		List<Integer> ans = new ArrayList<>();
		if (root == null)
			return ans;
		
		Queue<TreeNode> q = new LinkedList<>();
		q.offer(root);
		while (!q.isEmpty()) {
			TreeNode curr = null;
			for (int size = q.size(); size > 0; size--) {
				curr = q.poll();
				
				if (curr.left != null)
					q.offer(curr.left);
				if (curr.right != null)
					q.offer(curr.right);
			}
			ans.add(curr.val);
		}
		return ans;
	}
	
	/*
	 * https://leetcode.com/problems/binary-tree-right-side-view/discuss/56230/Share-my-Java-iterative-solution-based-on-level-order-traversal
	 * 
	 * Other code:
	 * https://leetcode.com/problems/binary-tree-right-side-view/discuss/56076/Reverse-Level-Order-Traversal-java
	 */
	public List<Integer> rightSideView_BFS2(TreeNode root) {
		List<Integer> ret = new ArrayList<Integer>();
		if (root == null)
			return ret;
		
		Queue<TreeNode> q = new LinkedList<TreeNode>();
		q.offer(root);
		while (!q.isEmpty()) {
			int cnt = q.size();
			for (int i = 0; i < cnt; i++) {
				TreeNode cur = q.poll();
				
				if (i == cnt - 1) {
					ret.add(cur.val);
				}
				if (cur.left != null) {
					q.offer(cur.left);
				}
				if (cur.right != null) {
					q.offer(cur.right);
				}
			}
		}
		return ret;
	}
	
	/*
	 * https://leetcode.com/problems/binary-tree-right-side-view/discuss/56062/Java-Solution-using-Divide-and-Conquer
	 * 
	 * Rf :
	 * https://leetcode.com/problems/binary-tree-right-side-view/discuss/56062/Java-Solution-using-Divide-and-Conquer/57645
	 */
	public List<Integer> rightSideView_divide_and_conquer(TreeNode root) {
		if (root == null)
			return new ArrayList<Integer>();
		
		List<Integer> left = rightSideView_divide_and_conquer(root.left);
		List<Integer> right = rightSideView_divide_and_conquer(root.right);
		
		List<Integer> re = new ArrayList<Integer>();
		re.add(root.val);
		
		// Right first, and then left
		for (int i = 0; i < Math.max(left.size(), right.size()); i++) {
			if (i >= right.size())
				re.add(left.get(i));
			else
				re.add(right.get(i));
		}
		return re;
	}
	
	/*
	 * https://leetcode.com/problems/binary-tree-right-side-view/discuss/56012/My-simple-accepted-solution(JAVA)/57627
	 * 
	 * Rf :
	 * https://leetcode.com/problems/binary-tree-right-side-view/discuss/56012/My-simple-accepted-solution(JAVA)/57602
	 */
	public List<Integer> rightSideView_stack(TreeNode root) {
        if(root == null){
            return new ArrayList<Integer>();
        }
        
        List<Integer> result = new ArrayList<Integer>();
        
        Stack<TreeNode> nodeStack = new Stack<TreeNode>();
        Stack<Integer> depthStack = new Stack<Integer>();
        
        nodeStack.push(root);
        depthStack.push(0);
        
        while(!nodeStack.isEmpty()){
            TreeNode curr = nodeStack.pop();
            int depth = depthStack.pop();
            
            if(depth == result.size()){
                result.add(curr.val);
            }
            if(curr.left != null){
                nodeStack.push(curr.left);
                depthStack.push(depth + 1);
            }
            if(curr.right != null){
                nodeStack.push(curr.right);
                depthStack.push(depth + 1);
            }
        }
        return result;
    }
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/binary-tree-right-side-view/discuss/56064/5-9-Lines-Python-48%2B-ms
     * https://leetcode.com/problems/binary-tree-right-side-view/discuss/171119/Python-or-BFS-%2B-DFS-tm
     * https://leetcode.com/problems/binary-tree-right-side-view/discuss/56248/Python-solutions-(DFS-recursively-DFS%2Bstack-BFS%2Bqueue)
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/binary-tree-right-side-view/discuss/56142/9ms-C%2B%2B-BFS-O(n)-time-concise-with-explanation
     * https://leetcode.com/problems/binary-tree-right-side-view/discuss/56003/My-C%2B%2B-solution-modified-preorder-traversal
     */

}
