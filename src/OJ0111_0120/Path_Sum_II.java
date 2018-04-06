package OJ0111_0120;

import definition.TreeNode;

import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

public class Path_Sum_II {
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/path-sum-ii/discuss/36698/Another-accepted-Java-solution
	 * 
	 * Other code :
	 * https://leetcode.com/problems/path-sum-ii/discuss/36683/DFS-with-one-LinkedList-accepted-java-solution
	 * https://leetcode.com/problems/path-sum-ii/discuss/36824/My-simple-java-solution
	 */
	public List<List<Integer>> pathSum(TreeNode root, int sum) {
        List<List<Integer>> res = new ArrayList<List<Integer>>();
        pathSum(root, sum, new ArrayList<Integer>(), res);
        return res;
    }
    void pathSum(TreeNode root, int sum, List<Integer> sol, List<List<Integer>> res) {
        if (root == null) {
            return;
        }
        
        sol.add(root.val);
        
        if (root.left == null && root.right == null && sum == root.val) {
            res.add(new ArrayList<Integer>(sol));
        } 
        else {
            pathSum(root.left, sum - root.val, sol, res);
            pathSum(root.right, sum - root.val, sol, res);
        }
        
        sol.remove(sol.size() - 1);
    }
	
	/*
	 * The following 2 functions are by myself.
	 * 
	 * Other code :
	 * https://leetcode.com/problems/path-sum-ii/discuss/36673/Simple-DFS-Java-Solution
	 */
	public List<List<Integer>> pathSum_self(TreeNode root, int sum) {
        List<List<Integer>> path = new ArrayList<List<Integer>>();
        if (root == null)
            return path;
        
        pathSum_self(root, sum, path, new ArrayList<Integer>());
        return path;
    }
    private void pathSum_self(TreeNode root, int sum, List<List<Integer>> path, List<Integer> list) {
        if (root.left == null && root.right == null) {
            if (sum - root.val == 0) {
                List<Integer> list2 = new ArrayList<Integer>(list);
                list2.add(root.val);
                path.add(list2);
                return;
            }
        }
        
        if (root.left != null) {
            list.add(root.val);
            pathSum_self(root.left, sum - root.val, path, list);
            list.remove(list.size() - 1);
        }
        
        if (root.right != null) {
            list.add(root.val);
            pathSum_self(root.right, sum - root.val, path, list);
            list.remove(list.size() - 1);
        }
    }
    
    /*
     * https://leetcode.com/problems/path-sum-ii/discuss/36906/My-accepted-JAVA-solution
     * 
     * The basic idea is to find every correct path and create a list for them. 
     * After searching the left child and right child of the root, merge two lists.
     */
	public List<List<Integer>> pathSum_merge(TreeNode root, int sum) {
		List<List<Integer>> pathList = new ArrayList<List<Integer>>();
		if (root == null) {
			return pathList;
		}
		
		// if find a path, create a new list and add to the pathList.
		if (root.left == null && root.right == null) {
			if (root.val == sum) {
				List<Integer> list = new ArrayList<Integer>();
				list.add(root.val);
				pathList.add(list);
			}
			return pathList;
		}
		
		// find path left and right child and merge two list together.
		pathList = pathSum_merge(root.left, sum - root.val);
		List<List<Integer>> pathList_right = pathSum_merge(root.right, sum - root.val);
		for (List<Integer> l : pathList_right) {
			pathList.add(l);
		}
		
		// add current root to every list in path list.
		for (List<Integer> l : pathList) {
			l.add(0, root.val);
		}
		return pathList;
	}
	
	// https://leetcode.com/problems/path-sum-ii/discuss/36695/Java-Solution:-iterative-and-recursive
	public List<List<Integer>> pathSum_iterative(TreeNode root, int sum) {
		List<List<Integer>> res = new ArrayList<>();
		List<Integer> path = new ArrayList<>();
		Stack<TreeNode> stack = new Stack<TreeNode>();
		
		int SUM = 0;
		TreeNode cur = root;
		TreeNode pre = null;
		while (cur != null || !stack.isEmpty()) {
			while (cur != null) {
				stack.push(cur);
				path.add(cur.val);
				SUM += cur.val;
				cur = cur.left;
			}
			cur = stack.peek();
			if (cur.right != null && cur.right != pre) {
				cur = cur.right;
				continue;
			}
			if (cur.left == null && cur.right == null && SUM == sum)
				res.add(new ArrayList<Integer>(path));

			pre = cur;
			stack.pop();
			path.remove(path.size() - 1);
			SUM -= cur.val;
			cur = null;

		}
		return res;
	}

}
