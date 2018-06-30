package OJ0301_0310;

import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.HashSet;

public class Minimum_Height_Trees {
	/*
	 * https://leetcode.com/problems/minimum-height-trees/discuss/76142/35ms-Concise-Java-Solution
	 * 
	 * keep deleting leaves layer-by-layer, until reach the root.
	 * 
	 * For leaf nodes, their degree = 1, which means each of them is only connected 
	 * to one node. First find all the leaves, then remove them. After removing, some 
	 * nodes will become new leaves. So we can continue remove them. Eventually, there 
	 * is only 1 or 2 nodes left. If there is only one node left, it is the root. If 
	 * there are 2 nodes, either of them could be a possible root.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/minimum-height-trees/discuss/76104/C++-Solution.-O(n)-Time-O(n)-Space
	 * https://leetcode.com/problems/minimum-height-trees/discuss/76129/Share-my-BFS-JAVA-code-using-degree-with-explanation-which-beats-more-than-95
	 */
	public List<Integer> findMinHeightTrees(int n, int[][] edges) {
		if (n == 1) {
			return Arrays.asList(0);
		}

		List<List<Integer>> adj = new ArrayList<>();
		int[] inlinks = new int[n];
		for (int i = 0; i < n; i++) {
			adj.add(new ArrayList<Integer>());
		}
		for (int[] e : edges) {
			adj.get(e[0]).add(e[1]);
			adj.get(e[1]).add(e[0]);
			inlinks[e[0]]++;
			inlinks[e[1]]++;
		}

		List<Integer> leaves = new LinkedList<>();
		for (int i = 0; i < inlinks.length; i++) {
			if (inlinks[i] == 1) {
				leaves.add(i);
			}
		}

		while (n > 2) {
			List<Integer> newLeaves = new ArrayList<>();
			for (int leave : leaves) {
				for (int nb : adj.get(leave)) {
					inlinks[nb]--;
					if (inlinks[nb] == 1) {
						newLeaves.add(nb);
					}
				}
			}
			n -= leaves.size();
			leaves = newLeaves;
		}

		return leaves;
	}

	/*
	 * https://leetcode.com/problems/minimum-height-trees/discuss/76055/Share-some-thoughts
	 * 
	 * For a path graph of n nodes, find the minimum height trees is trivial. Just 
	 * designate the middle point(s) as roots.
	 * 
	 * Suppose we don't know n, nor do we have random access of the nodes. We have to 
	 * traversal. It is very easy to get the idea of two pointers. One from each end 
	 * and move at the same speed. When they meet or they are one step away, (depends 
	 * on the parity of n), we have the roots we want.
	 * 
	 * For a tree we can do some thing similar. We start from every end, by end we 
	 * mean vertex of degree 1 (aka leaves). We let the pointers move the same speed. 
	 * When two pointers meet, we keep only one of them, until the last two pointers 
	 * meet or one step away we then find the roots.
	 * 
	 * The actual implementation is similar to the BFS topological sort. Remove the 
	 * leaves, update the degrees of inner vertexes. Then remove the new leaves. Doing 
	 * so level by level until there are 2 or 1 nodes left. What's left is our answer!
	 * 
	 * The time complexity and space complexity are both O(n).
	 */
	public List<Integer> findMinHeightTrees2(int n, int[][] edges) {
		if (n == 1)
			return Collections.singletonList(0);

		List<Set<Integer>> adj = new ArrayList<>(n);
		for (int i = 0; i < n; ++i)
			adj.add(new HashSet<>());
		for (int[] edge : edges) {
			adj.get(edge[0]).add(edge[1]);
			adj.get(edge[1]).add(edge[0]);
		}

		List<Integer> leaves = new ArrayList<>();
		for (int i = 0; i < n; ++i)
			if (adj.get(i).size() == 1)
				leaves.add(i);

		while (n > 2) {
			n -= leaves.size();
			List<Integer> newLeaves = new ArrayList<>();
			for (int i : leaves) {
				int j = adj.get(i).iterator().next();
				adj.get(j).remove(i);
				if (adj.get(j).size() == 1)
					newLeaves.add(j);
			}
			leaves = newLeaves;
		}
		return leaves;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * leetcode.com/problems/minimum-height-trees/discuss/76052/Two-O(n)-solutions/79411
	 * 
	 * The root of an MHT has to be the middle point (or two middle points) of the 
	 * longest path of the tree. Though multiple longest paths can appear in an 
	 * unrooted tree, they must share the same middle point(s).
	 * 
	 * For convenience, let's consider the case where the longest path consists of odd 
	 * number vertices. (Even case can be done via a similar analysis.) Define the 
	 * longest path as x ... o ... y, where x and y are the two endpoints and o is the 
	 * middle vertex.
	 * 
	 * For a contradiction, if the root of MHT is not o but some other vertex o', then 
	 * at least one of the following two statements must be true.
	 * 1. path xo' is strictly longer than path xo
	 * 2. path yo' is strictly longer than path yo
	 * This means o' is worse than o, i.e., o' cannot be the root of MHT, which is a 
	 * contradiction.
	 * 
	 * "Randomly select a node x as the root, do a dfs/bfs to find the node y that has 
	 * the longest distance from x. Then y must be one of the endpoints on some 
	 * longest path."
	 * 
	 * x is root of the tree.
	 * Longest path in a tree = max(height(node.left)+height(node.right)+1) for all nodes.
	 * 
	 * Let pathLen(node) = height(node.left) + height(node.right) + 1
	 * Let m be the node which has maximum pathLen(node) amongst all nodes.
	 * 
	 * Can m's longest path avoid 'y'?
	 * 1. m's subtree doesn't contain y. Then find the Lowest Common Ancestor of m and 
	 *    y. pathLen(LCA) > pathLen(m), because
	 *    pathLen(LCA) >= height(LCA) + height(m) >= height(m)*2 >= pathLen(m). Contradiction!
	 * 2. m's subtree contains y. y has the greatest depth out of any leaf node, so 
	 *    it's almost trivial. eg. if y is in m.left.subtree, then the longest path 
	 *    from m to a leaf node in m.left.subtree obviously goes to y.
	 * 
	 * Let y the new root, and do another dfs/bfs. Find the node z that has the 
	 * longest distance from y.
	 * Now, the path from y to z is the longest one, and thus its middle point(s) is 
	 * the answer.
	 * 
	 * Rf : 
	 * https://leetcode.com/problems/minimum-height-trees/discuss/76052/Two-O(n)-solutions
	 * leetcode.com/problems/minimum-height-trees/discuss/76052/Two-O(n)-solutions/79412
	 * leetcode.com/problems/minimum-height-trees/discuss/76052/Two-O(n)-solutions/79426
	 */
	public List<Integer> findMinHeightTrees3(int n, int[][] edges) {
		List<List<Integer>> edgeList = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			edgeList.add(new ArrayList<>());
		}
		for (int[] edge : edges) {
			edgeList.get(edge[0]).add(edge[1]);
			edgeList.get(edge[1]).add(edge[0]);
		}

		int[] prev = new int[n];
		Arrays.fill(prev, -1);
		int p0 = bfsEdge(edgeList, 0, prev);

		prev = new int[n];
		Arrays.fill(prev, -1);
		int p1 = bfsEdge(edgeList, p0, prev);

		List<Integer> path = new ArrayList<>();
		int i = p1;
		while (i != p0) {
			path.add(i);
			i = prev[i];
		}
		path.add(p0);

		List<Integer> res = new ArrayList<>();
		res.add(path.get(path.size() / 2));
		if (path.size() % 2 == 0)
			res.add(path.get(path.size() / 2 - 1));
		return res;
	}

	private int bfsEdge(List<List<Integer>> edgeList, int start, int[] prev) {
		Queue<Integer> q = new LinkedList<>();
		q.add(start);
		prev[start] = start;
		int ret = start;
		while (!q.isEmpty()) {
			ret = q.poll();
			for (int i : edgeList.get(ret)) {
				if (prev[i] == -1) {
					q.add(i);
					prev[i] = ret;
				}
			}
		}
		return ret;
	}

}
