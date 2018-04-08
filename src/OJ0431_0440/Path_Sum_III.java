package OJ0431_0440;

import definition.TreeNode;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

public class Path_Sum_III {
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/path-sum-iii/discuss/91889/Simple-Java-DFS
	 * 
	 * Other code:
	 * https://leetcode.com/problems/path-sum-iii/discuss/91971/Java:-Never-Start-or-Stick-to-the-End
	 */
	public int pathSum(TreeNode root, int sum) {
		if (root == null)
			return 0;
		return pathSumFrom(root, sum) 
				+ pathSum(root.left, sum) + pathSum(root.right, sum);
	}
	private int pathSumFrom(TreeNode node, int sum) {
		if (node == null)
			return 0;
		return (node.val == sum ? 1 : 0) 
				+ pathSumFrom(node.left, sum - node.val)
				+ pathSumFrom(node.right, sum - node.val);
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/path-sum-iii/discuss/91878/17-ms-O(n)-java-Prefix-sum-method
	 * 
	 * 1. The prefix stores the sum from the root to the current node in the recursion
	 * 2. The map stores <prefix sum, frequency> pairs before getting to the current 
	 *    node. The sum from any node in the middle of the path to the current node 
	 *    = the difference between the sum from the root to the current node and 
	 *    the prefix sum of the node in the middle.
	 * 3. We are looking for some consecutive nodes that sum up to the given target 
	 *    value, which means the difference discussed in 2. should equal to the target 
	 *    value. In addition, we need to know how many differences are equal to the 
	 *    target value.
	 * 4. If the difference between the current sum and the target value exists in the 
	 *    map, there must exist a node in the middle of the path, such that from this 
	 *    node to the current node, the sum is equal to the target value.
	 * 5. In each recursion, the map stores all information we need to calculate the 
	 *    number of ranges that sum up to target. Note that each range starts from a 
	 *    middle node, ended by the current node.
	 * 6. To get the total number of path count, we add up the number of valid paths 
	 *    ended by EACH node in the tree.
	 * 7. Each recursion returns the total count of valid paths in the subtree rooted 
	 *    at the current node. And this sum can be divided into three parts:
	 *    - the total number of valid paths in the subtree rooted at the current 
	 *      node's left child
	 *    - the total number of valid paths in the subtree rooted at the current 
	 *      node's right child
	 *    - the number of valid paths ended by the current node
	 * 
	 * This method only keeps track of 1 branch at a time. Because we're doing a 
	 * depth first search, we will iterate all the way to the end of a single branch 
	 * before coming back up. However, as we're coming back up, we're removing the 
	 * nodes at the bottom of the branch from our hash table.
	 * 
	 * Rf : https://leetcode.com/problems/path-sum-iii/discuss/91884/Simple-AC-Java-Solution-DFS
	 */
	public int pathSum_HashMap(TreeNode root, int sum) {
		Map<Integer, Integer> map = new HashMap<>(); // stores the sum from the root to the current node
		map.put(0, 1);                          // Default sum = 0 has one count
		return backtrack(root, 0, sum, map);    // BackTrack one pass
	}
	public int backtrack(TreeNode root, int sum, int target, Map<Integer, Integer> map) {
		if (root == null)
			return 0;
		
		sum += root.val;
		
		int res = map.getOrDefault(sum - target, 0); // See if there is a subarray sum equals to target
		map.put(sum, map.getOrDefault(sum, 0) + 1);
		
		// Extend to left and right child
		res += backtrack(root.left, sum, target, map) + backtrack(root.right, sum, target, map);
		
		map.put(sum, map.get(sum) - 1); // Remove the current node so it won't affect other path
		return res;
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/path-sum-iii/discuss/91987/JAVA-solution-by-using-DFS-two-times
	 */
	int count = 0;
    public int pathSum_2DFS(TreeNode root, int sum) {
        if (root != null){
            fun(root,sum);
            pathSum_2DFS(root.left,sum);
            pathSum_2DFS(root.right,sum);
        }
        return count;
    }
    public void fun(TreeNode root,int sum){
        if (root != null){
            if (sum - root.val == 0){
                count++;
            }
            fun(root.left,sum - root.val);
            fun(root.right,sum - root.val);
        }
    }
    
    /*
     * The following 2 variables and 2 functions are from this link.
     * https://leetcode.com/problems/path-sum-iii/discuss/91996/Easy-to-understand-Java-solution-with-comment.
     * 
     * for each parent node in the tree, we have 2 choices:
     * 1. include it in the path to reach sum.
     * 2. not include it in the path to reach sum. 
     * 
     * for each child node in the tree, we have 2 choices:
     * 1. take what your parent left you.
     * 2. start from yourself to form the path.
     * 
     * one little thing to be careful:
     * every node in the tree can only try to be the start point once.
     */
    int target;
	Set<TreeNode> visited;   // to store the nodes that have already tried to start path by themselves.
	public int pathSum_Set(TreeNode root, int sum) {
		target = sum;
		visited = new HashSet<TreeNode>();
		return pathSumHelper(root, sum, false);
	}
	public int pathSumHelper(TreeNode root, int sum, boolean hasParent) {
		if (root == null)
			return 0;
		
		// the hasParent flag is used to handle the case when parent path sum is 0.
		// in this case we still want to explore the current node.
		if (sum == target && visited.contains(root) && !hasParent)
			return 0;
		if (sum == target && !hasParent)
			visited.add(root);
		
		int count = (root.val == sum) ? 1 : 0;
		count += pathSumHelper(root.left, sum - root.val, true);
		count += pathSumHelper(root.right, sum - root.val, true);
		count += pathSumHelper(root.left, target, false);
		count += pathSumHelper(root.right, target, false);
		return count;
	}

}
