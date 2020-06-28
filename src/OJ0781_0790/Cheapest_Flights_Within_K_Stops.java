package OJ0781_0790;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Comparator;

public class Cheapest_Flights_Within_K_Stops {
	/*
	 * https://leetcode.com/problems/cheapest-flights-within-k-stops/discuss/361711/Java-DFSBFSBellman-Ford-Dijkstra's
	 * 
	 * Much like BFS, but use a PriorityQueue based on the cheapest cost. 
	 * Incorporate the stop limit to individual paths to traverse up to k stops.
	 * 
	 * The key difference with the classic Dijkstra algo is, we don't maintain the 
	 * global optimal distance to each node, i.e. ignore below optimization:
	 *    alt ¡ö dist[u] + length(u, v)
	 *    if alt < dist[v]:
	 * 
	 * Because there could be routes which their length is shorter but pass more 
	 * stops, and those routes don't necessarily constitute the best route in the end. 
	 * To deal with this, rather than maintain the optimal routes with 0..K stops for 
	 * each node, the solution simply put all possible routes into the priority queue, 
	 * so that all of them has a chance to be processed. 
	 * And the solution simply returns the first qualified route
	 * 
	 * (0, src, k + 1) stands for (price, station, numOfStops + 1). During BFS, he 
	 * uses price to keep track of the current price, and use station to determine if 
	 * the current node we are visiting is the destination, and also use the 
	 * numOfStops to keep track of how many stops are there in order to reach to this 
	 * station
	 * 
	 * "K" limited the time we can visit a single node that it won't go into an 
	 * infinite loop.
	 * 
	 * If we do not do so, we can not reach destination within K stops. So visiting a 
	 * visited city is sometimes required in the final solution.
	 * 
	 * 4
	 * [[0,1,1],[0,2,5],[1,2,1],[2,3,1]]
	 * 0
	 * 3
	 * 1
	 * 
	 * Rf :
	 * https://leetcode.com/problems/cheapest-flights-within-k-stops/discuss/115541/JavaPython-Priority-Queue-Solution/222415
	 * https://leetcode.com/problems/cheapest-flights-within-k-stops/discuss/115541/JavaPython-Priority-Queue-Solution/285918
	 * https://leetcode.com/problems/cheapest-flights-within-k-stops/discuss/115541/JavaPython-Priority-Queue-Solution/259612
	 * https://leetcode.com/problems/cheapest-flights-within-k-stops/discuss/115541/JavaPython-Priority-Queue-Solution/297694
	 * https://leetcode.com/problems/cheapest-flights-within-k-stops/discuss/115541/JavaPython-Priority-Queue-Solution/266047
	 * https://leetcode.com/problems/cheapest-flights-within-k-stops/discuss/115541/JavaPython-Priority-Queue-Solution/173983
	 * https://leetcode.com/problems/cheapest-flights-within-k-stops/discuss/115541/JavaPython-Priority-Queue-Solution/154616
	 * 
	 * Other code:
	 * https://leetcode.com/problems/cheapest-flights-within-k-stops/discuss/686774/SUGGESTION-FOR-BEGINNERS-SIMPLE-STEPS-BFS-or-DIJKSHTRA-or-DP-DIAGRAM
	 */
	public int findCheapestPrice_Dijkstra(int n, int[][] flights, int src, int dst, 
			int K) {
		
		// source, destination, price
		Map<Integer, List<int[]>> map = new HashMap<>();
		for (int[] f : flights) {
			map.putIfAbsent(f[0], new ArrayList<>());
			map.get(f[0]).add(new int[] { f[1], f[2] });
		}
		
		PriorityQueue<int[]> q = new PriorityQueue<>(new Comparator<int[]>() {
			@Override
			public int compare(int[] o1, int[] o2) {
				return Integer.compare(o1[0], o2[0]);
			}
		});
		q.offer(new int[] { 0, src, K + 1 });
		
		while (!q.isEmpty()) {
			int[] c = q.poll();
			int cost = c[0];
			int curr = c[1];
			int stop = c[2];
			
			if (curr == dst)
				return cost;
			
			if (stop > 0) {
				if (!map.containsKey(curr))
					continue;
				
				for (int[] next : map.get(curr)) {
					q.add(new int[] { cost + next[1], next[0], stop - 1 });
				}
			}
		}
		return -1;
	}
	
	/*
	 * https://leetcode.com/problems/cheapest-flights-within-k-stops/discuss/361711/Java-DFSBFSBellman-Ford-Dijkstra's
	 * 
	 * Simultaneously traverse all the possible path going out from source for up to 
	 * k steps. If the ans is found in between, we store the min of the current ans 
	 * with the newly found one. A modification to the standard bfs design, we pass 
	 * the starting cost a 0 to the queue as well and go on adding to it.
	 * 
	 * Other code:
	 * https://leetcode.com/problems/cheapest-flights-within-k-stops/discuss/166443/AC.-Simple-BFS-(5ms)-No-PQ.-Beats-99-of-submissions
	 */
	public int findCheapestPrice_BFS(int n, int[][] flights, int src, int dst, int K) {
		Map<Integer, List<int[]>> map = new HashMap<>();
		for (int[] i : flights) {
			map.putIfAbsent(i[0], new ArrayList<>());
			map.get(i[0]).add(new int[] { i[1], i[2] });
		}
		
		Queue<int[]> q = new LinkedList<>();
		q.offer(new int[] { src, 0 });
		
		int step = 0;
		int ans = Integer.MAX_VALUE;
		while (!q.isEmpty()) {
			int size = q.size();
			for (int i = 0; i < size; i++) {
				int[] curr = q.poll();
				if (curr[0] == dst)
					ans = Math.min(ans, curr[1]);
				
				if (!map.containsKey(curr[0]))
					continue;
				
				for (int[] f : map.get(curr[0])) {
					if (curr[1] + f[1] > ans) // Pruning
						continue;
					
					q.offer(new int[] { f[0], curr[1] + f[1] });
				}
			}
			
			if (step++ > K)
				break;
		}
		return ans == Integer.MAX_VALUE ? -1 : ans;
	}
	
	/*
	 * The following variable and 3 functions are from this link.
	 * https://leetcode.com/problems/cheapest-flights-within-k-stops/discuss/686810/Easy-Approach-BFS-DFS-or-Detailed-Steps-or-Digram-Flow-or-Well-Commented
	 * 
	 * Keep a global answer and traverse all the children of the source up to k stops. 
	 * If at any point we reach the destination, store the min of the answer and the 
	 * current cost.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/cheapest-flights-within-k-stops/discuss/361711/Java-DFSBFSBellman-Ford-Dijkstra's
	 */
	int minPathCost_dfs;

	public int findCheapestPrice_dfs(int n, int[][] flights, int src, int dst, int K) {
		// initialize the minPathCost with Integer.MAX_VALUE
		minPathCost_dfs = Integer.MAX_VALUE;
		
		// create graph
		Map<Integer, List<int[]>> graph = createGraph_dfs(flights);

		// K stop => K + 1 edges
		dfs(graph, src, dst, K + 1, 0);
		
		return minPathCost_dfs == Integer.MAX_VALUE ? -1 : minPathCost_dfs;
	}

	private void dfs(Map<Integer, List<int[]>> graph, int src, int dst, int k, 
			int currentCost) {
		
		// if covered k stops and not reached destination
		if (k < 0)
			return;
		
		// if reached destination currentCost is minPathCost
		if (src == dst) {
			minPathCost_dfs = currentCost;
			return;
		}
		
		if (!graph.containsKey(src))
			return;
		
		for (int[] flight : graph.get(src)) {
			// price + current flight cost > minPathCost discard that path ~ pruning
			if (currentCost + flight[1] > minPathCost_dfs)
				continue;
			
			dfs(graph, flight[0], dst, k - 1, currentCost + flight[1]);
		}
	}

	private Map<Integer, List<int[]>> createGraph_dfs(int[][] flights) {
		// direct stops + cost to reach that stop
		Map<Integer, List<int[]>> graph = new HashMap<>();
		for (int[] flight : flights) {
			graph.putIfAbsent(flight[0], new ArrayList<>());
			graph.get(flight[0]).add(new int[] { flight[1], flight[2] });
		}
		return graph;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/cheapest-flights-within-k-stops/discuss/361711/Java-DFSBFSBellman-Ford-Dijkstra's/579959
	 * 
	 * We could essentially use an array to store final cost and current cost. 
	 * Note, precomputed current cost values that lead to an error case would need to 
	 * be discarded. This can be simplified by removing the computation cost overhead 
	 * after every recursive call
	 */
	public int findCheapestPrice_dfs2(int n, int[][] flights, int src, int dst, int K) {
		if (n == 0 || flights.length == 0)
			return -1;
		
		Map<Integer, List<int[]>> map = new HashMap<>();
		for (int[] i : flights) {
			map.putIfAbsent(i[0], new ArrayList<>());
			map.get(i[0]).add(new int[] { i[1], i[2] });
		}
		
		int[] costs = new int[2];
		costs[1] = Integer.MAX_VALUE;
		
		int ans = dfs2(map, src, dst, K + 1, costs);
		
		return ans == Integer.MAX_VALUE ? -1 : ans;
	}

	public int dfs2(Map<Integer, List<int[]>> map, int src, int dst, 
			int k, int[] cost) {
		
		if (k < 0)
			return Integer.MAX_VALUE;
		
		if (src == dst) {
			cost[1] = cost[0];
			return cost[1];
		}
		
		if (!map.containsKey(src))
			return Integer.MAX_VALUE;
		
		int min = Integer.MAX_VALUE;
		for (int[] i : map.get(src)) {
			if (cost[0] + i[1] >= cost[1])
				continue;
			
			cost[0] += i[1];
			
			int val = dfs2(map, i[0], dst, k - 1, cost);
			
			// We do not want non path based computations, so remove them once dfs
			// processing is done 
			cost[0] -= i[1];
			
			min = Math.min(val, min);
		}
		return min;
	}
	
	/*
	 * https://leetcode.com/problems/cheapest-flights-within-k-stops/discuss/207128/Two-Java-Solutions-one-is-DP-and-the-other-is-Dijkstra
	 * 
	 * Bellman Ford is a space optimized version of 2D Dynamic Programming Solution.
	 * 
	 * dp[k, v] = min(dp[k - 1, u] + w(u -> v))
	 * dp[k-1, u] because we have to reach to u using at most k-1 edges. 
	 * As we have to reach v using at most k edges.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/cheapest-flights-within-k-stops/discuss/686774/SUGGESTION-FOR-BEGINNERS-SIMPLE-STEPS-BFS-or-DIJKSHTRA-or-DP-DIAGRAM
	 */
	public int findCheapestPrice_dp(int n, int[][] flights, int src, int dst, int K) {
		// dp[i][j] denotes the cheapest price within i-1 stops, stop in j city
		long[][] dp = new long[K + 2][n];
		for (long[] d : dp)
			Arrays.fill(d, Integer.MAX_VALUE);
		
		dp[0][src] = 0;
		for (int i = 1; i < K + 2; i++) {
			dp[i][src] = 0;
			for (int[] f : flights) {
				dp[i][f[1]] = Math.min(dp[i][f[1]], dp[i - 1][f[0]] + f[2]);
			}
		}
		return dp[K + 1][dst] == Integer.MAX_VALUE ? -1 : (int) dp[K + 1][dst];
	}
	
	/*
	 * https://leetcode.com/problems/cheapest-flights-within-k-stops/discuss/361711/Java-DFSBFSBellman-Ford-Dijkstra's
	 * 
	 * temp array is needed here since in (i)th iteration, we only want to relax nodes 
	 * that are reachable from previous (i-1) iteration.
	 * 
	 * BF runs V-1 iterations since that's the longest possible path from src to dest 
	 * - one that uses every vertex. In this problem, since the longest path is given 
	 * as K+1, you only need to run it that many iterations.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/cheapest-flights-within-k-stops/discuss/163698/easy-java-Bellman-Ford/328419
	 * https://leetcode.com/problems/cheapest-flights-within-k-stops/discuss/163698/easy-java-Bellman-Ford/191573
	 * https://leetcode.com/problems/cheapest-flights-within-k-stops/discuss/686774/SUGGESTION-FOR-BEGINNERS-SIMPLE-STEPS-BFS-or-DIJKSHTRA-or-DP-DIAGRAM
	 * 
	 * Other code:
	 * https://leetcode.com/problems/cheapest-flights-within-k-stops/discuss/163698/easy-java-Bellman-Ford
	 */
	public int findCheapestPrice_Bellman_Ford(int n, int[][] flights, int src, 
			int dst, int K) {
		
		int[] cost = new int[n];
		Arrays.fill(cost, Integer.MAX_VALUE);
		
		cost[src] = 0;
		
		// Relax all edges for K times
		for (int i = 0; i <= K; i++) {
			int[] temp = Arrays.copyOf(cost, n);
			
			// Go over all edges
			for (int[] f : flights) {
				int curr = f[0], next = f[1], price = f[2];
				if (cost[curr] == Integer.MAX_VALUE)
					continue;
				
				// relax function
				temp[next] = Math.min(temp[next], cost[curr] + price);
			}
			cost = temp;
		}
		return cost[dst] == Integer.MAX_VALUE ? -1 : cost[dst];
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/cheapest-flights-within-k-stops/discuss/317262/2-Clean-Python-Solution-(BFS-Dijkstra-Explained)
     * https://leetcode.com/problems/cheapest-flights-within-k-stops/discuss/686906/Python-Multipass-BFS-O(V2)-%2B-Dijkstra-with-SortedList-explained
     * https://leetcode.com/problems/cheapest-flights-within-k-stops/discuss/267200/Python-Dijkstra
     * https://leetcode.com/problems/cheapest-flights-within-k-stops/discuss/115623/10-line-python-DP-solution-O(N2*K)-time-O(N)-space
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/cheapest-flights-within-k-stops/discuss/686829/C%2B%2B-Easy-Beginner-Friendly
     * https://leetcode.com/problems/cheapest-flights-within-k-stops/discuss/115596/c%2B%2B-8-line-bellman-ford
     * https://leetcode.com/problems/cheapest-flights-within-k-stops/discuss/340911/Understanding-Bellman-Ford
     * https://leetcode.com/problems/cheapest-flights-within-k-stops/discuss/115548/C%2B%2B-solution-using-Dynamic-Programming
     * https://leetcode.com/problems/cheapest-flights-within-k-stops/discuss/662812/C%2B%2B-BFS-or-Bellman-Ford-Algo-or-Dijkstra-Algo
     * https://leetcode.com/problems/cheapest-flights-within-k-stops/discuss/626208/Detailed-C%2B%2B-Standard-Approaches-DP-Approach-(DFS-with-memoization)-BFS
     */
	
	/**
	 * JavaScript collections
	 * 
	 * https://leetcode.com/problems/cheapest-flights-within-k-stops/discuss/181943/Readable-Javascript-Solution
	 */

}
