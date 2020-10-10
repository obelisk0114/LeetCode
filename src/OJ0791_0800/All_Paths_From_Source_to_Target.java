package OJ0791_0800;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Queue;
import java.util.Deque;
import java.util.ArrayDeque;

public class All_Paths_From_Source_to_Target {
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/all-paths-from-source-to-target/discuss/118713/Java-DFS-Solution
	 * 
	 * Traverse the graph from start node to the end, and keep track of each node 
	 * along the path. Each node can be visited many times when it has multiple 
	 * indegree. (Backtracking)
	 * 
	 * 可以省略 node，用 path 的最後一個值來代替
	 * 
	 * The visited set records whether a node has already been visited in the current 
	 * path, therefore used to prevent going in circles per path. The reason we don't 
	 * need to use a visited set is because we know it is a DAG; we don't need to use 
	 * auxiliary data to track it.
	 * 
	 * Time Complexity: O(2^N)
	 * 
	 * Rf :
	 * https://leetcode.com/problems/all-paths-from-source-to-target/discuss/118713/Java-DFS-Solution/522527
	 * https://leetcode.com/problems/all-paths-from-source-to-target/discuss/297408/Java-DFS-solution-Easy-to-understand-and-Explanation
	 * https://leetcode.com/problems/all-paths-from-source-to-target/discuss/118691/C%2B%2BPython-Backtracking
	 * 
	 * Other code:
	 * https://leetcode.com/problems/all-paths-from-source-to-target/discuss/753282/JAVA-Clean-Code-DFS-Technique-2-ms-Time-98-Faster
	 * https://leetcode.com/problems/all-paths-from-source-to-target/discuss/880269/DFS-and-BFS-Algorithm-Well-Commented-Code
	 */
	public List<List<Integer>> allPathsSourceTarget_dfs(int[][] graph) {
		List<List<Integer>> res = new ArrayList<>();
		List<Integer> path = new ArrayList<>();

		path.add(0);
		dfsSearch_dfs(graph, 0, res, path);

		return res;
	}

	private void dfsSearch_dfs(int[][] graph, int node, List<List<Integer>> res, 
			List<Integer> path) {
		
		// 若是沒有 node，如下
		// dfsSearch_dfs(int[][] graph, List<List<Integer>> res, List<Integer> path)
		// 
		// 需要加上
		// int node = path.get(path.size() - 1);
		if (node == graph.length - 1) {
			res.add(new ArrayList<Integer>(path));
			return;
		}

		for (int nextNode : graph[node]) {
			path.add(nextNode);
			dfsSearch_dfs(graph, nextNode, res, path);
			path.remove(path.size() - 1);
		}
	}
	
	/*
	 * https://leetcode.com/problems/all-paths-from-source-to-target/discuss/118857/Java-short-and-easy-BFS
	 * 
	 * Other code:
	 * https://leetcode.com/problems/all-paths-from-source-to-target/discuss/752532/JAVA-solution-BFS-or-Explained-code-or-7ms
	 */
	public List<List<Integer>> allPathsSourceTarget_BFS(int[][] graph) {
		List<List<Integer>> res = new ArrayList<>();
		Queue<List<Integer>> q = new ArrayDeque<>();
		q.add(Arrays.asList(0));

		while (!q.isEmpty()) {
			List<Integer> e = q.remove();
			if (e.get(e.size() - 1) == graph.length - 1) {
				res.add(e);
				continue;
			}

			for (int n : graph[e.get(e.size() - 1)]) {
				List<Integer> next = new ArrayList<>(e);
				next.add(n);
				q.add(next);
			}
		}

		return res;
	}

	/*
	 * The following 2 functions are by myself.
	 */
	public List<List<Integer>> allPathsSourceTarget_self(int[][] graph) {
        List<List<Integer>> res = new ArrayList<>();
        List<Integer> list = new ArrayList<>();
        list.add(0);
        
        dfs_self(graph, new HashSet<Integer>(), list, res);
        return res;
    }
    
    private void dfs_self(int[][] graph, Set<Integer> visited, 
    		List<Integer> list, List<List<Integer>> res) {
    	
        int source = list.get(list.size() - 1);
        
        for (int dest : graph[source]) {
            if (visited.contains(dest)) {
                continue;
            }
            
            if (dest == graph.length - 1) {
                List<Integer> reach = new ArrayList<>(list);
                reach.add(dest);
                res.add(reach);
                
                continue;
            }
            
            visited.add(dest);
            list.add(dest);
            
            dfs_self(graph, visited, list, res);
            
            list.remove(list.size() - 1);
            visited.remove(dest);
        }
    }
    
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/all-paths-from-source-to-target/discuss/118713/Java-DFS-Solution
	 * 
	 * Use memorization. Each node will be only visited once since the sub result 
	 * from this node has already been recorded. Memorization increases space cost as 
	 * well as time cost to record existing paths.
	 * 
	 * The visited set records whether a node has already been visited in the current 
	 * path, therefore used to prevent going in circles per path. The reason we don't 
	 * need to use a visited set is because we know it is a DAG; we don't need to use 
	 * auxiliary data to track it.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/all-paths-from-source-to-target/discuss/118713/Java-DFS-Solution/522527
	 */
	public List<List<Integer>> allPathsSourceTarget_dfs2(int[][] graph) {
		Map<Integer, List<List<Integer>>> map = new HashMap<>();

		return dfsSearch_dfs2(graph, 0, map);
	}

	private List<List<Integer>> dfsSearch_dfs2(int[][] graph, int node, 
			Map<Integer, List<List<Integer>>> map) {
		
		if (map.containsKey(node)) {
			return map.get(node);
		}

		List<List<Integer>> res = new ArrayList<>();
		if (node == graph.length - 1) {
			List<Integer> path = new LinkedList<>();
			path.add(node);
			res.add(path);
		} 
		else {
			for (int nextNode : graph[node]) {
				List<List<Integer>> subPaths = dfsSearch_dfs2(graph, nextNode, map);
				
				for (List<Integer> path : subPaths) {
					LinkedList<Integer> newPath = new LinkedList<>(path);
					newPath.addFirst(node);
					res.add(newPath);
				}
			}
		}

		map.put(node, res);
		return res;
	}

    /*
     * Modified by myself
     * 
     * Rf :
     * https://leetcode.com/problems/all-paths-from-source-to-target/discuss/412377/Java-Iterative-with-Stack-and-Queue-and-statistics-comparing-Iterative-vs-Recursive-speeds
     */
    public List<List<Integer>> allPathsSourceTarget_stack_modify(int[][] graph) {
		int end = graph.length - 1;
		List<List<Integer>> result = new ArrayList<>();

		Deque<List<Integer>> stack = new ArrayDeque<>();
        
		stack.push(Arrays.asList(0));

		while (!stack.isEmpty()) {
			List<Integer> current = stack.pollLast();
            int last = current.get(current.size() - 1);
			
			if (last == end) {
				result.add(current);
				continue;
			}
			
			for (int neighbor : graph[last]) {
				List<Integer> next = new ArrayList<>(current);
                next.add(neighbor);
                stack.offerLast(next);
			}
		}
		return result;
	}
    
    /*
     * https://leetcode.com/problems/all-paths-from-source-to-target/discuss/412377/Java-Iterative-with-Stack-and-Queue-and-statistics-comparing-Iterative-vs-Recursive-speeds
     * 
     * The 'trick' is that contrary to a regular DFS where we pop a node off the stack 
     * and process it, we leave the node on the stack and only remove it once we have 
     * finished exploring all the paths the node generates
     * 
     * Time: O(2^n), Space: O(n)
     * 
     * Other code:
     * https://leetcode.com/problems/all-paths-from-source-to-target/discuss/118713/Java-DFS-Solution/118423
     */
	public List<List<Integer>> allPathsSourceTarget_stack2(int[][] graph) {
		Integer end = graph.length - 1;
		List<List<Integer>> result = new ArrayList<>();

		Deque<Integer> stack = new ArrayDeque<>(); // keep track of nodes to process
		Deque<Integer> path = new ArrayDeque<>(); // path generated so far

		// prime the stack with the start node
		stack.push(0);

		while (!stack.isEmpty()) {
			/*
			 * 'hack' alert we need an Integer object and not an int because 
			 * path.peekLast() will return null the very first time it is called. 
			 * you cannot compare null with an int but you can with an Integer
			 */
			Integer current = stack.peek(); /* peek, don't pop */
			if (current == path.peekLast()) {
				/*
				 * when the top of the stack and the top of queue match it means we 
				 * have processes all this node's connections there are no more paths 
				 * to explore so remove the node from the list of nodes to process, 
				 * and from the path We are backtracking to the previous point in the 
				 * path, and the list of nodes to process
				 */
				stack.pop(); // remove the end from the path
				path.removeLast(); // remove the end from the path
				continue;
			}
			
			path.offer(current);
			
			if (current == end) {
				/* We've found a path */
				result.add(new ArrayList<>(path)); // add the path Queue to the result
				path.removeLast(); /* go back one step in the path */
				stack.pop(); /* remove the from the nodes to process */
				continue;
			}
			
			/*
			 * we haven't seen this node before. add all of this node's neighbors to 
			 * the stack of nodes to process
			 */
			for (int neighbor : graph[current]) {
				stack.push(neighbor);
			}
		}
		return result;
	}
    
    /**
     * Python collections
     * 
     * https://leetcode.com/problems/all-paths-from-source-to-target/discuss/118691/C%2B%2BPython-Backtracking
     * https://leetcode.com/problems/all-paths-from-source-to-target/discuss/752481/Python-Simple-dfs-backtracking-explained
     * https://leetcode.com/problems/all-paths-from-source-to-target/discuss/189059/Python-BFS-Solution
     * https://leetcode.com/problems/all-paths-from-source-to-target/discuss/118765/short-python-iterative-dfs
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/all-paths-from-source-to-target/discuss/118647/C%2B%2B-DFS-Recursive-Easy-to-Understand
     * https://leetcode.com/problems/all-paths-from-source-to-target/discuss/752625/C%2B%2B-DFS-based-Simple-Solution-Explained-100-Time-~80-Space
     * https://leetcode.com/problems/all-paths-from-source-to-target/discuss/282335/C%2B%2B-BacktrackingDPSBFS
     * https://leetcode.com/problems/all-paths-from-source-to-target/discuss/382479/797-All-path-from-source-to-target-C%2B%2B-Iterative-DFS-%2B-Backtracking-beats-98.8-in-time
     */
    
    /**
     * JavaScript collections
     * 
     * https://leetcode.com/problems/all-paths-from-source-to-target/discuss/137888/JavaScript-solution
     * https://leetcode.com/problems/all-paths-from-source-to-target/discuss/845257/Javascript-DFS-%2B-BFS
     */

}
