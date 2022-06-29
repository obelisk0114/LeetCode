package OJ1191_1200;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

public class Critical_Connections_in_a_Network {
	/*
	 * https://leetcode.com/problems/critical-connections-in-a-network/discuss/451733/ICU-For-Struggling-People-Like-Me-Java-Tarjan-based-solution
	 * 
	 * 0 -> [1, 2]
	 * 1 -> [0, 2]
	 * 2 -> [0, 1, 3]
	 * 3 -> [2, 4, 5]
	 * 4 -> [3, 5]
	 * 5 -> [3, 4]
	 * 
	 * We can recognize edge (2,3) as a critical connection is we find out that 
	 * without this edge, vertex 3, 4 and 5 can't connect to any of vertex 0, 1 and 2 
	 * anymore and vice versa.
	 * 
	 * When we reach to vertex A and see an edge (A, B), if vertex B and any vertexes 
	 * that connect to B (except vertex A) have no route to connect to vertex A and 
	 * any vertexes that connect to A, then edge (A, B) is the critical connection 
	 * we're looking for.
	 * 
	 * Since we'll traverse vertexes based on chronological order, each vertex will 
	 * have a timestamp (you can call it rank, discovered order or whatever that 
	 * helps you understand) to mark the moment we reach to it.
	 * 
	 * Because each vertex has at least one edge connects to it, therefore, when we 
	 * reach to the `currentVertex` through an edge, we must have visited the 
	 * `previousVertex` except that the scenario that the `currentVertex` is the 
	 * first vertex we start with. So, we assume we have visited some dummy vertex 
	 * (use non-existing vertex id, e.g. -1 as the vertex id) before we reach to the 
	 * first vertex that we actually visit.
	 * 
	 * Assume we have visited a bunch of vertexes and then we visited vertex A and 
	 * finally reaches to vertex B through edge (A, B), if vertex B has other edge 
	 * that can connect to vertexes that we have visited (except vertex A) then it's 
	 * like vertex B has a time tunnel that can travel back to history. As a result 
	 * of the existence of the "time tunnel" and edge (A, B), neither the 
	 * "time tunnel" nor edge (A, B) is critical connection anymore. 
	 * 
	 * Essentially, the "backward time travel" capability will help us identify 
	 * critical connection. we use the an array lowLink[] to track the earliest 
	 * moment that each vertex can travel back. lowLink[i] means the earliest moment 
	 * that vertex i can travel back to. Obviously, the initial value of lowLink[i] 
	 * is the timestamp that we reach to them.
	 * 
	 * The visited[] array is to make things clear but it isn't necessary as we can 
	 * initialize lowLink[] array with all -1 to indicate that a vertex hasn't been 
	 * visited yet.
	 */
	class Solution2 {
		public List<List<Integer>> criticalConnections(int n, 
				List<List<Integer>> connections) {
			
			List<List<Integer>> result = new LinkedList<>();

			// build adjacency list, boilerplate code for graph problems
			List<List<Integer>> graph = new ArrayList<>();
			for (int i = 0; i < n; i++) {
				graph.add(new ArrayList<>());
			}

			for (List<Integer> connection : connections) {
				int vertex1 = connection.get(0);
				int vertex2 = connection.get(1);
				graph.get(vertex1).add(vertex2);
				graph.get(vertex2).add(vertex1);
			}
	
			// flag to show if we're reached a vertex or not
			boolean[] visited = new boolean[n];
			
			// the earliest moment that vertexes can travel back to
			int[] lowLink = new int[n];
	        
			dfs(graph, visited, lowLink, result, 0, -1, 0);
			return result;
		}

		private void dfs(List<List<Integer>> graph, boolean[] visited, int[] lowLink, 
				List<List<Integer>> result, int timeStamp, int previousVertex, 
				int currentVertex) {
			
			// now, we have visited the "currentVertex"
			visited[currentVertex] = true;
			
			// record the time timestamp that we visit the "currentVertex"
			lowLink[currentVertex] = timeStamp;
	        
			// iterate over vertexes that in the edges 
			// that starts with "currentVertex"
			for (int nextVertex : graph.get(currentVertex)) {
				
			    // we walk along edge (previousVertex, currentVertex) to
				// reach "currentVertex" so we don't want to repeat
				if (nextVertex == previousVertex) {					
					continue;
				}

				if (!visited[nextVertex]) {
					dfs(graph, visited, lowLink, result, timeStamp + 1, 
							currentVertex, nextVertex);
				}
				
				// since (currentVertex, nextVertex) is an edge, 
				// "currentVertex" can travel back to any earlier moment 
				// that nextVertex can travel to
				lowLink[currentVertex] = 
						Math.min(lowLink[currentVertex], lowLink[nextVertex]);

				// walk along edge (currentVertex, nextVertex) will arrive at 
				// "nextVertex" at the moment timeStamp + 1
				// so if "nextVertex" has no chance to travel to any moment that is 
				// earlier than timeStamp + 1
				// then edge (currentVertex, nextVertex) is a critical connection
				if (lowLink[nextVertex] >= timeStamp + 1) {
					result.add(Arrays.asList(currentVertex, nextVertex));
				}
			}
		}
	}
	
	/*
	 * https://leetcode.com/problems/critical-connections-in-a-network/discuss/399827/Java-DFS-Solution-similar-to-Tarjan-maybe-easier-to-understand
	 * 
	 * We record the timestamp that we visit each node. For each node, we check every 
	 * neighbor except its parent and return a smallest timestamp in all its 
	 * neighbors. If this timestamp is strictly less than the node's timestamp, we 
	 * know that this node is somehow in a cycle. Otherwise, this edge from the 
	 * parent to this node is a critical connection.
	 * 
	 * --------------------------------------------------------------------------
	 * 
	 * Timestamp is exactly the time that we visit a node, and the timestamp for each 
	 * node is unique and it is always increasing like the clock. Imagine that the 
	 * first time we visit a node, the timestamp is 5. Then if there exists a circle, 
	 * we will visit it again, probably at timestamp 10. Then we know that there 
	 * exists a cycle.
	 * 
	 * However, the timestamp in this problem works in a slightly different way. 
	 * First, we keep track of every edge (parent->child). You can tell from the 'dfs' 
	 * function that we record the 'parent' in the parameters. Second, when we first 
	 * visit a node, we will assign it with a unique timestamp. And we check what's 
	 * the smallest timestamp in all the sub-graph of this node. Now here comes three 
	 * cases:
	 * 1. If the smallest timestamp is the child itself, then we know that there 
	 *    exists a cycle and the child is in the cycle. But the parent is not in the 
	 *    cycle because the parent's timestamp must be smaller than the child's.
	 * 2. If the smallest timestamp is less than the child's timestamp, then we know 
	 *    that there exists a cycle and both the child and the parent are in the 
	 *    cycle. (The edge is in the cycle)
	 * 3. If the smallest timestamp is larger than the child's timestamp, the we know 
	 *    that there does not exist a cycle.
	 * 
	 * Since we only care about the first and the third case, we only add the edge 
	 * into the critical connection set when the smallest timestamp >= child's 
	 * timestamp.
	 * 
	 * --------------------------------------------------------------------------
	 * 
	 * The dfs method is basically trying to find the node with the minimum timestamp 
	 * the current node is able to reach. By calling the dfs method with all of the 
	 * current node's neighbors, we get a number of results representing the 
	 * timestamps the current node can reach and we choose the minimal value from 
	 * these results. Our purpose is to find out the cycle and the minimal value is 
	 * used to do so. Taking a step back, if there is a cycle existing in the graph, 
	 * we will eventually go back to a previous timestamp through the cycle; in 
	 * another word, we will find a node that can reach to a previous timestamp. When 
	 * we find such node, we will return the previous timestamp as the result of the 
	 * dfs method and the caller (the parent node) who is calling the dfs method will 
	 * get the previous timestamp as well. The minimal value among the results of the 
	 * dfs invocations for a node's all child nodes will be larger than the previous 
	 * timestamp until we get back to the beginning of the loop (technically there is 
	 * no beginning of a loop but the beginning here is referring to the node that 
	 * firstly detected the cycle). All the connections within a loop are not 
	 * critical connections; in another word, we just need to return all the 
	 * connections outside of any loops.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/critical-connections-in-a-network/discuss/399827/Java-DFS-Solution-similar-to-Tarjan-maybe-easier-to-understand/363970
	 * https://leetcode.com/problems/critical-connections-in-a-network/discuss/399827/Java-DFS-Solution-similar-to-Tarjan-maybe-easier-to-understand/1135764
	 * 
	 * Other codes:
	 * https://leetcode.com/problems/critical-connections-in-a-network/discuss/399827/Java-DFS-Solution-similar-to-Tarjan-maybe-easier-to-understand/368211
	 */
	class Solution_list_global_time {
		int T = 1;

		public List<List<Integer>> criticalConnections(int n, 
				List<List<Integer>> connections) {
			
			// use a timestamp, for each node, check the smallest timestamp 
			// that can reach from the node
			// construct the graph first
			List<List<Integer>> graph = new ArrayList<>(n);
			for (int i = 0; i < n; i++) {
				graph.add(new ArrayList<Integer>());
			}
			for (List<Integer> conn : connections) {
				graph.get(conn.get(0)).add(conn.get(1));
				graph.get(conn.get(1)).add(conn.get(0));
			}

			// an array to save the timestamp that we meet a certain node
			int[] timestamp = new int[n];

			// for each node, we need to run dfs for it, and return the smallest
			// timestamp in all its children except its parent
			List<List<Integer>> criticalConns = new ArrayList<>();
			dfs(n, graph, timestamp, 0, -1, criticalConns);
			return criticalConns;
		}

		// return the minimum timestamp it ever visited in all the neighbors
		private int dfs(int n, List<List<Integer>> graph, int[] timestamp, int i, 
				int parent, List<List<Integer>> criticalConns) {
			
			if (timestamp[i] != 0)
				return timestamp[i];
			
			timestamp[i] = T++;

			int minTimestamp = Integer.MAX_VALUE;
			for (int neighbor : graph.get(i)) {
				// no need to check the parent
				if (neighbor == parent)
					continue;
				
				int neighborTimestamp = dfs(n, graph, timestamp, neighbor, 
						i, criticalConns);
				
				minTimestamp = Math.min(minTimestamp, neighborTimestamp);
			}

			if (minTimestamp >= timestamp[i]) {
				if (parent >= 0) {					
					criticalConns.add(Arrays.asList(parent, i));
				}
			}
			
			return Math.min(timestamp[i], minTimestamp);
		}
	}
	
	/*
	 * https://leetcode.com/problems/critical-connections-in-a-network/discuss/382910/Tarjan's-algorithm%3A-use-HashMap-TLE-but-use-Array-beats-100.-Why
	 * 
	 * Implementation using HashMap
	 * 
	 * The solution uses Tarjan's algorithm.
	 * 
	 * Complexity analysis of array-based implementation
	 * E = total number of edges = connections.size()
	 * V = total number of vertices = n
	 * 
	 * time complexity: O(V + E)
	 * I build the graph explicitly. The 1st for-loop loops over each vertices, which 
	 * costs O(V). The 2nd for-loop loops over each edge, which costs O(E).
	 * Then I perform DFS that visits each vertices and edge once, which costs 
	 * O(V + E). Therefore, the total time complexity is O(V + E)
	 * 
	 * extra space complexity: O(V + E)
	 * The variable "graph" is an hash map representation of the graph, which 
	 * costs O(V + E) space.
	 * The stack usage of DFS costs O(V + E) space in worst case (which occurs when 
	 * the graph is a stick). Therefore, the total extra space complexity is O(V + E)
	 * 
	 * Rf :
	 * https://leetcode.com/problems/critical-connections-in-a-network/discuss/382910/Tarjan's-algorithm:-use-HashMap-TLE-but-use-Array-beats-100.-Why/361170
	 */
	class Solution_HashMap {
	    private int time = 0; // current time of discovery

		public List<List<Integer>> criticalConnections(int n, 
				List<List<Integer>> connections) {

	    	// node -> neighbors
			Map<Integer, List<Integer>> graph = new HashMap<>();

			for (List<Integer> conn : connections) {
				int n1 = conn.get(0);
				int n2 = conn.get(1);
				
				graph.putIfAbsent(n1, new ArrayList<>());
				graph.putIfAbsent(n2, new ArrayList<>());
				
				graph.get(n1).add(n2);
				graph.get(n2).add(n1);
			}

	        // discovery time of each node
			int[] disc = new int[n];

			// earliest discovered node reachable from this node in DFS
			int[] low = new int[n];

			// whether this node has been visited in DFS
			boolean[] visited = new boolean[n];

			List<List<Integer>> out = new ArrayList<>();

			// arbitrarily pick a node to start DFS
			dfs(0, -1, disc, low, visited, graph, out);

			return out;
		}

	    // root = current node under consideration
	    // parent = parent of current node
		private void dfs(int root, int parent, int[] disc, int[] low, 
				boolean[] visited,
				Map<Integer, List<Integer>> graph, List<List<Integer>> out) {
	    	
			visited[root] = true;
			disc[root] = time++;

			// we don't have to initialize low[] to inf because of this line
			low[root] = disc[root];

			List<Integer> neighbors = graph.get(root);
			if (neighbors == null) {
				return;
			}

			for (Integer nei : neighbors) {
				if (nei == parent) {
					continue;
				}

				if (!visited[nei]) {
					dfs(nei, root, disc, low, visited, graph, out);
					low[root] = Math.min(low[root], low[nei]);
					
					if (disc[root] < low[nei]) {
						out.add(Arrays.asList(root, nei));
					}
				}
				else {
					low[root] = Math.min(low[root], disc[nei]);
				}
			}
		}
	}
	
	/*
	 * https://leetcode.com/problems/critical-connections-in-a-network/discuss/1174196/JS-Python-Java-C%2B%2B-or-Tarjan's-Algorithm-Solution-w-Explanation
	 * 
	 * If we think of the network and its connections like an undirected graph and 
	 * its edges, then a critical connection, as defined by this problem, is the same 
	 * as a bridge in the graph. To find out which edges are bridges, we can employ 
	 * Tarjan's Bridge-Finding Algorithm (TBFA).
	 * 
	 * TBFA is a bit like a combination of a depth-first search (DFS) approach with 
	 * recursion and a union-find. In TBFA, we do a recursive DFS on our graph and 
	 * for each node we keep track of the earliest node that we can circle back 
	 * around to reach. By doing this, we can identify whether a given edge is a 
	 * bridge because the far node doesn't lead back to any other earlier node.
	 * 
	 * To implement our TBFA, the first thing we have to do is to construct an edge 
	 * map (edgeMap) from the connections list. Each key in our edge map should 
	 * correspond to a specific node, and its value should be an array of each 
	 * adjacent node to the key node.
	 * 
	 * We'll also need separate arrays to store the discovery time (disc) and the 
	 * lowest future node (low) for each node, as well as a time counter to use with 
	 * disc.
	 * 
	 * For our recursive DFS helper (dfs), each newly-visited node should set its 
	 * initial value for both disc and low to the current value of time before time 
	 * is incremented. (Note: If we start time at 1 instead of 0, we can use either 
	 * disc or low as a visited array, rather than having to keep a separate array 
	 * for the purpose. Any non-zero value in the chosen array will then represent a 
	 * visited state for the given node.)
	 * 
	 * Then we recursively call dfs on each of the unvisited adjacent nodes (next) of 
	 * the current node (curr). If one of the possible next nodes is an earlier node 
	 * (disc[next] < curr[low]), then we've found a loop and we should update the low 
	 * value for the current node. As each layer of the recursive function 
	 * backtracks, it will propagate this value of low back down the chain.
	 * 
	 * If, after backtracking, the value of low[next] is still higher than disc[curr], 
	 * then there is no looped connection, meaning that the edge between curr and 
	 * next is a bridge, so we should add it to our answer array (ans).
	 * 
	 * Once the dfs helper function has completed its traversal, we can return ans.
	 * 
	 * --------------------------------------------------------------------
	 * 
	 * In order for a path to be a loop, you have to reach a node you've seen before 
	 * without going backwards along the path. Since you're checking the entire list 
	 * of adjacent nodes in edgeMap for a given node, there will always be one of 
	 * those nodes (except for the very beginning of your DFS traversal) which 
	 * represents going backwards. This is why we store and send the previous node id 
	 * as an argument to the next recursion of dfs, so that we can avoid going 
	 * backwards.
	 * 
	 * In a normal graph traversal, we'd rely on a visited array for the same thing, 
	 * but in this case, we want to find a visited node, just not the most recently 
	 * visited one.
	 * 
	 * --------------------------------------------------------------------
	 * 
	 * So the recursion naturally happens when dfs is called. The first thing that 
	 * happens after the backtracking occurs is the following line, where the value 
	 * of low[curr] will possibly be updated to low[next] if a loop was detected. But 
	 * if two loops backtrack to the same node, it's possible for low[curr] to 
	 * already be lower from the first loop than the low[next] returning from the 
	 * second loop. In that case, there is no bridge, so using low[next] > low[curr] 
	 * would result in the wrong answer.
	 * 
	 * --------------------------------------------------------------------
	 * 
	 * If you start time at 0 and use if not low[next]: instead of if not disc[next]: 
	 * for the "visited" conditional, then the solution will fail. If you use disc 
	 * with a start time of 0, you'll only ever risk repeating the very first node, 
	 * which won't actually cause a fail, but is still an unnecessary extra cycle.
	 * 
	 * --------------------------------------------------------------------
	 * 
	 * SCC's are more a function of directed graphs, not undirected graphs.
	 * 
	 * while all nodes sharing the same low value are part of the same component, the 
	 * reverse is not always true, as there might be several smaller loops that form 
	 * a larger component.
	 * 
	 * Consider the case: [[0,1],[1,2],[2,0],[2,3],[3,4],[4,2]]
	 * 
	 * Starting from 0 in this graph will lead to nodes 0, 1, & 2 having a low value 
	 * of 1, while nodes 3 & 4 will have a low value of 3, yet there are no bridges. 
	 * This is because dfs goes all the way out to node 4, then finds node 2 (low 3) 
	 * and backtracks back to node 2 while setting nodes 4 and 3 to the same low 
	 * value of 3. Then from node 2, it finds node 0 (low 1), before backtracking 
	 * again to nodes 1 and 0, setting the low values to 1.
	 * 
	 * So because it satisfies the [2,4] loop before finding and and satisfying the 
	 * [0,2] loop, you'll have different low values while still being interconnected.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/critical-connections-in-a-network/discuss/1174200/Critical-Connections-in-a-Network-or-JS-Python-Java-C++-or-Tarjan's-Algorithm-Solution-w-Explan./917072
	 * https://leetcode.com/problems/critical-connections-in-a-network/discuss/1174196/JS-Python-Java-C++-or-Tarjan's-Algorithm-Solution-w-Explanation/918927
	 * https://leetcode.com/problems/critical-connections-in-a-network/discuss/1174196/JS-Python-Java-C++-or-Tarjan's-Algorithm-Solution-w-Explanation/918215
	 * https://leetcode.com/problems/critical-connections-in-a-network/discuss/1174196/JS-Python-Java-C++-or-Tarjan's-Algorithm-Solution-w-Explanation/916634
	 */
	class Solution_Map_global {
		int[] disc, low;
		int time = 1;
		
		List<List<Integer>> ans = new ArrayList<>();
		Map<Integer, List<Integer>> edgeMap = new HashMap<>();

		public List<List<Integer>> criticalConnections(int n, 
				List<List<Integer>> connections) {
			
			disc = new int[n];
			low = new int[n];
			
			for (int i = 0; i < n; i++)
				edgeMap.put(i, new ArrayList<Integer>());
			for (List<Integer> conn : connections) {
				edgeMap.get(conn.get(0)).add(conn.get(1));
				edgeMap.get(conn.get(1)).add(conn.get(0));
			}
			
			dfs(0, -1);
			return ans;
		}

		public void dfs(int curr, int prev) {
			disc[curr] = low[curr] = time++;
			
			for (int next : edgeMap.get(curr)) {
				if (disc[next] == 0) {
					dfs(next, curr);
					low[curr] = Math.min(low[curr], low[next]);
				} 
				else if (next != prev) {					
					low[curr] = Math.min(low[curr], disc[next]);
				}
				
				if (low[next] > disc[curr]) {					
					ans.add(Arrays.asList(curr, next));
				}
			}
		}
	}
	
	/*
	 * https://leetcode.com/problems/critical-connections-in-a-network/discuss/540656/Java-Clean-Code-DFS-O(V%2BE)
	 * 
	 * Tarjan's Algorithm provides a very effective way to find these bridges and 
	 * articulation points in linear time.
	 * 
	 * step 1:
	 * Start at any node and do a Depth First Search (DFS) traversal, labeling nodes 
	 * with an increasing id value as you go.
	 * 
	 * step 2:
	 * Keep track the id of each node and the smallest low link value.
	 * Low Link Value of a node is defined as the smallest id reachable from that 
	 * node when doing a Depth First Search (DFS), including itself.
	 * 
	 * Initially, all low link values can be initialized to the each node id.
	 * If we inspect node 1 and node 2, we will notice that there exist a path going 
	 * from node 1 and node 2 to node 0.
	 * So, we should update both node 1 and node 2 low link values to 0.
	 * 
	 * However, node 3, node 4 and node 5 are already at their optimal low link value 
	 * because there are no other node they can reach with a smaller id.
	 * 
	 * For node 6, node 7 and node 8, there is a path from node 6, node 7 and node 8 
	 * to node 5.
	 * So, we should update node 6, node 7 and node 8 low link values to 5.
	 * 
	 * step 3:
	 * During the Depth First Search (DFS), bridges will be found where the id of 
	 * node your edge is coming from is less than the low link value of the node your 
	 * edge is going to.
	 * 
	 * Rf :
	 * https://emre.me/algorithms/tarjans-algorithm/
	 */
	class Solution_List_global {
		List<List<Integer>> bridges;
		ArrayList<ArrayList<Integer>> graph;
		int[] ids, low;
		int id;

		public List<List<Integer>> criticalConnections(int n, 
				List<List<Integer>> connections) {
			
			// 1. initialize global variables;
			bridges = new ArrayList<>();
			graph = new ArrayList<>(n);
			ids = new int[n];
			low = new int[n];
			id = 0;

			// 2. build graph;
			buildGraph(connections, n);

			// 3. find bridges;
			boolean[] visited = new boolean[n];
			dfs(visited, -1, 0);

			return bridges;
		}

		private void dfs(boolean[] visited, int parent, int i) {
			visited[i] = true;
			ids[i] = low[i] = id++;

			for (int node : graph.get(i)) {
				if (node == parent)
					continue;
				
				if (!visited[node]) {
					dfs(visited, i, node);

					// things below happen during backtracking
					low[i] = Math.min(low[i], low[node]);
					
					if (ids[i] < low[node])
						bridges.add(Arrays.asList(i, node));
				} 
				else {
					low[i] = Math.min(low[i], ids[node]);
				}
			}
		}

		private void buildGraph(List<List<Integer>> connections, int n) {
			for (int i = 0; i < n; i++)
				graph.add(new ArrayList<>());
			
			for (int i = 0; i < connections.size(); i++) {
				int a = connections.get(i).get(0), b = connections.get(i).get(1);
				graph.get(a).add(b);
				graph.get(b).add(a);
			}
		}
	}

	/**
     * Python collections
     * 
     * https://leetcode.com/problems/critical-connections-in-a-network/discuss/1173966/Python-Find-all-bridges-explained
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/critical-connections-in-a-network/discuss/634999/C%2B%2B-solution-finding-bridges
     * https://leetcode.com/problems/critical-connections-in-a-network/discuss/382386/C%2B%2B-DFS-O(n)-with-explanation
     * https://leetcode.com/problems/critical-connections-in-a-network/discuss/880970/C%2B%2B-or-Tarjan's-or-DFS-or-With-Explanation-or-93.86-faster
     * https://leetcode.com/problems/critical-connections-in-a-network/discuss/382515/readable-C%2B%2B-code
     * https://leetcode.com/problems/critical-connections-in-a-network/discuss/382375/I-dont-like-this-question
     */
	
	/**
     * JavaScript collections
     * 
     * https://leetcode.com/problems/critical-connections-in-a-network/discuss/550837/Step-by-step-explanation-for-dummies
     * https://leetcode.com/problems/critical-connections-in-a-network/discuss/391348/Javascript-solution.-Detailed-Explanation
     */
	
	/**
     * C# collections
     * 
     * https://leetcode.com/problems/critical-connections-in-a-network/discuss/397045/C-Trajan's-Algorithm-%2B-unit-test
     */

}
