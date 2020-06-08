package OJ0881_0890;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Queue;

public class Possible_Bipartition {
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/possible-bipartition/discuss/159224/Java-Solution%3A-DFS-using-Set-(Very-Easy-to-understand)
	 * 
	 * 1. Create an undirected graph and add 2 edges to graph. 
	 *    Source(dislikes[i][0]) to Target(dislikes[i][1]) and Target to Source
	 * 2. Create an another integer array called colors. Used for bipartition.
	 *    Uncolored nodes(0), Green Nodes(1), Red Nodes(-1)
	 * 3. Now traverse the graph using any of the graph traversal algorithms(DFS/BFS). 
	 *    If at any point in time we find a case where one of the Green team member 
	 *    needs to be moved to Red or vice versa, then it's not possible to partition 
	 *    nodes into 2 groups. So, return false. Otherwise return true.
	 *    
	 * Time Complexity: O(N + E) 
	 * where N = Number of People, E = length of dislikes array
	 * 
	 * Rf :
	 * https://leetcode.com/problems/possible-bipartition/discuss/655148/BFS-and-DFS-based-detailed-explanation-with-clean-code-diagram-and-video
	 * https://leetcode.com/problems/possible-bipartition/discuss/158957/Java-DFS-solution/556311
	 * 
	 * Other code:
	 * https://leetcode.com/problems/possible-bipartition/discuss/655414/Java-DFS-Clean-code
	 * https://leetcode.com/problems/possible-bipartition/discuss/158957/Java-DFS-solution
	 * https://leetcode.com/problems/possible-bipartition/discuss/654808/java-clear-code-using-bipartite-graph-algorithm
	 */
	public boolean possibleBipartition_DFS(int N, int[][] dislikes) {
		// HashSet used as adjacencySet dataStructure for Graph.
		// If duplicate nodes are allowed then use List.
		List<Set<Integer>> graph = new ArrayList<>(N + 1);
		
		// Vertex 0 has no data in this graph as per problem statement. So start at 1.
		for (int i = 0; i <= N; ++i) {
			// Initialize HashSet
			graph.add(new HashSet<>());
		}

		for (int i = 0; i < dislikes.length; ++i) {
			int source = dislikes[i][0], target = dislikes[i][1];
			
			// Add edges in both direction to create undirected graph
			graph.get(source).add(target);
			graph.get(target).add(source);
		}

		int colors[] = new int[N + 1];
		
		// paint the vertices of the graph, start by assigning the first person a
        // random color use dfs, when an element isn't colored, we can randomly 
		// assign a color
		for (int v = 1; v < graph.size(); ++v) {
			// Start dfs only if current node is not part of any group. If node is not
			// assigned to group and dfs returns false then it's not possible to
			// bi-partition this graph. So, answer is false.
			if (colors[v] == 0 && !dfs(graph, v, colors, 1))
				return false;
		}
		
		// if we are able to visit all nodes then answer is yes. We are able to split
		// the nodes into 2 different groups.
		return true;
	}

	// DFS returns true only 2 conditions.
	// 1. We reached all nodes from the current source vertex - s
	// 2. We reached the already visited and that belongs the the same team we expect.
	boolean dfs(List<Set<Integer>> graph, int s, int[] colors, int color) {
		if (colors[s] != 0) {
			return colors[s] == color;
		}
		
		colors[s] = color;
		for (int w : graph.get(s)) {
			if (!dfs(graph, w, colors, -color))
				return false;
		}
		return true;
	}
	
	/*
	 * https://leetcode.com/problems/possible-bipartition/discuss/159085/java-graph
	 * 
	 * For ease of understanding, we can represent this problem as a graph, with 
	 * people being the vertices and dislike-pairs being the edges of this graph.
	 * 
	 * We try to color the two sets of vertices, with RED and BLUE colors. There 
	 * must NOT be any edge connecting two vertices of the same color. Such an edge 
	 * will be a conflict edge.
	 * 
	 * The graph may consist of many connected components. For each connected 
	 * component, we run our BFS algorithm to find conflict edges, if any. For each 
	 * component, we start by coloring one vertex RED, and all it's neighbors BLUE. 
	 * Then we visit the BLUE vertices and color all their neighbors as RED, and so 
	 * on. During this process, if we come across any RED-RED edge or BLUE-BLUE edge, 
	 * we have found a conflict edge and we immediately return false, as bipartition 
	 * will not be possible.
	 * 
	 * If no conflict edges are found at the end of the algorithm, it means 
	 * bipartition is possible, hence we return true.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/possible-bipartition/discuss/654887/C++-BFS-with-detailed-explanation/
	 * 
	 * Other code:
	 * https://leetcode.com/problems/possible-bipartition/discuss/655148/BFS-and-DFS-based-detailed-explanation-with-clean-code-diagram-and-video
	 * https://leetcode.com/problems/possible-bipartition/discuss/654887/C++-BFS-with-detailed-explanation/558850
	 */
	public boolean possibleBipartition_BFS(int N, int[][] dislikes) {
		int[] color = new int[N + 1];
		
		List<List<Integer>> adj = new ArrayList<>(N + 1);
		for (int i = 0; i <= N; i++)
			adj.add(new ArrayList<Integer>());
		
		for (int[] d : dislikes) {
			adj.get(d[0]).add(d[1]);
			adj.get(d[1]).add(d[0]);
		}

		for (int i = 1; i <= N; i++) {
			// If color is not set, it is not connected with previous section. 
			if (color[i] == 0) {
				// randomly assign a color
				color[i] = 1;
				
				// Not fully connected, set queue here
				Queue<Integer> q = new LinkedList<>();
				q.add(i);
				while (!q.isEmpty()) {
					int cur = q.poll();
					for (int nb : adj.get(cur)) {
						if (color[nb] == 0) {
							color[nb] = color[cur] == 1 ? 2 : 1;
							q.add(nb);
						} 
						else {
							if (color[nb] == color[cur])
								return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/possible-bipartition/discuss/195303/Java-Union-Find
	 * 
	 * union find
	 */
	public boolean possibleBipartition_uf(int N, int[][] dislikes) {
		int[] parent = new int[N + 1];
		for (int i = 0; i <= N; i++)
			parent[i] = i;
		
		Map<Integer, List<Integer>> map = new HashMap<>();
		for (int[] pair : dislikes) {
			int a = pair[0];
			int b = pair[1];
			map.putIfAbsent(a, new ArrayList<>());
			map.putIfAbsent(b, new ArrayList<>());
			map.get(a).add(b);
			map.get(b).add(a);
		}
		
		for (int i = 1; i <= N; i++) {
			if (map.containsKey(i)) {
				int parent1 = find_uf(parent, i);
				List<Integer> opponents = map.get(i);
				
				int parent2 = find_uf(parent, opponents.get(0));
				if (parent1 == parent2)
					return false;
				
				for (int j = 1; j < opponents.size(); j++) {
					int opponentParent = find_uf(parent, opponents.get(j));
					if (parent1 == opponentParent)
						return false;
					
					parent[opponentParent] = parent2;
				}
			}
		}
		return true;
	}

	private int find_uf(int[] parent, int i) {
		while (i != parent[i]) {
			i = parent[parent[i]];
		}
		return i;
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/possible-bipartition/discuss/654955/Python-sol-by-DFS-and-coloring.-w-Graph
     * https://leetcode.com/problems/possible-bipartition/discuss/159009/Python-DFS-with-explanation
     * https://leetcode.com/problems/possible-bipartition/discuss/160371/Python-Decide-if-a-graph-is-bipartite-by-checking-the-existence-of-odd-cycles.
     * https://leetcode.com/problems/possible-bipartition/discuss/655026/Using-two-sets-with-out-graph
     * https://leetcode.com/problems/possible-bipartition/discuss/655181/Python-BFSDFS-Solution
     * https://leetcode.com/problems/possible-bipartition/discuss/655842/Python-(Union-Find-DFS-BFS)-with-explanation
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/possible-bipartition/discuss/654887/C%2B%2B-BFS-with-detailed-explanation
     * https://leetcode.com/problems/possible-bipartition/discuss/654830/C%2B%2B-dfs-using-lambda-function-or-Simple-and-short
     * https://leetcode.com/problems/possible-bipartition/discuss/655432/C%2B%2B-BFS-as-well-as-DFS-Best-simple-and-cute-solution
     * https://leetcode.com/problems/possible-bipartition/discuss/644973/C%2B%2B-Union-Find-Easy-to-Understand-Solution
     */

}
