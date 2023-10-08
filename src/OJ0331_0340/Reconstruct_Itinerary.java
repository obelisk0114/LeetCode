package OJ0331_0340;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.Map;
import java.util.HashMap;

public class Reconstruct_Itinerary {
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/reconstruct-itinerary/discuss/78766/Share-my-solution
	 * 
	 * Hierholzer's algorithm
	 * 
	 * 當你從 source 出發第一次發現死路的時候，那個結點就是 destination. (在 cycle 的情況下 src 就是 dst). 
	 * 因為只有兩個結點的 degree 會是奇數. 其他結點如果有一條邊進去，就肯定有一條邊出來. 因此這就是為什麼發現
	 * 死路之後可以直接把當前結點加入到 result list 的尾部. 繼續回溯之後也是一樣，如果回溯到的結點沒有更多的
	 * unvisited edges 的話, 也就可以加入 result list 了.
	 * 
	 * In Eulerian paths, there must exist a start node and a end node.
	 * + end node is start node iff all nodes has even degree.
	 * + end node is another node iff there is another odd degree node and start node 
	 *   has an odd degree.
	 * 
	 * The reason we got stuck is because that we hit the exit. Since every node 
	 * except for the first and the last node has even number of edges, when we enter 
	 * a node we can always get out.
	 * 
	 * Now we are at the destination and if all edges are visited, we are done, and 
	 * the dfs returns to the very first state. Otherwise we need to "insert" the 
	 * unvisited loop into corresponding position, and in the dfs method, it returns 
	 * to the node with extra edges, starts another recursion and adds the result 
	 * before the next path. This process continues until all edges are visited.
	 * 
	 * The algorithm is to find the end node first and delete the path to this node
	 * (backtrack), meanwhile using PriorityQueue to guarantee lexical order. Remaining 
	 * tickets form cycles which are found on the way back and get merged into that 
	 * main path.
	 * 
	 * Rf :
	 * http://reeestart.me/2018/04/08/LeetCode-332-Reconstruct-Itinerary/
	 * https://leetcode.com/problems/reconstruct-itinerary/discuss/78766/Share-my-solution/83493
	 * https://leetcode.com/problems/reconstruct-itinerary/discuss/78768/Short-Ruby-Python-Java-C++/163964
	 * https://leetcode.com/problems/reconstruct-itinerary/discuss/78768/Short-Ruby-Python-Java-C++/83576
	 * https://leetcode.com/problems/reconstruct-itinerary/discuss/78830/10-ms-short-Java-Solution-using-DFStopological-sort-Beats-96-.-Well-explained
	 * https://leetcode.com/problems/reconstruct-itinerary/discuss/78766/Share-my-solution/83508
	 * http://www.csie.ntnu.edu.tw/~u91029/Circuit.html#3
	 * https://en.wikipedia.org/wiki/Eulerian_path
	 * 
	 * Other code :
	 * https://leetcode.com/problems/reconstruct-itinerary/discuss/297787/Java-Topological-Sort
	 */
	public List<String> findItinerary(List<List<String>> tickets) {
		Map<String, PriorityQueue<String>> flights = new HashMap<>();
		LinkedList<String> path = new LinkedList<>();
		for (List<String> ticket : tickets) {
			flights.putIfAbsent(ticket.get(0), new PriorityQueue<>());
			flights.get(ticket.get(0)).add(ticket.get(1));
		}
		
		dfs("JFK", flights, path);
		return path;
	}

	public void dfs(String departure, Map<String, PriorityQueue<String>> flights, 
			LinkedList<String> path) {
		
		PriorityQueue<String> arrivals = flights.get(departure);
		while (arrivals != null && !arrivals.isEmpty())
			dfs(arrivals.poll(), flights, path);
		
		path.addFirst(departure);// instead of reversing, add to the head of linkelist
	}
	
	/*
	 * https://leetcode.com/problems/reconstruct-itinerary/discuss/78846/Very-Short-Iterative-Java-Solution
	 * 
	 * finding Euler path using Hierholzer's Algorithm
	 * 1. Start with an empty stack and an empty circuit (eulerian path).
	 *    a. If all vertices have same out-degrees as in-degrees - choose any of them.
	 *    b. If all but 2 vertices have same out-degree as in-degree, and one of those 
	 *       2 vertices has out-degree with one greater than its in-degree, and the 
	 *       other has in-degree with one greater than its out-degree - then choose 
	 *       the vertex that has its out-degree with one greater than its in-degree.
	 *    c. Otherwise no euler circuit or path exists.
	 * 2. If current vertex has no out-going edges (i.e. neighbors) - add it to 
	 *    circuit, remove the last vertex from the stack and set it as the current 
	 *    one. Otherwise (in case it has out-going edges, i.e. neighbors) - add the 
	 *    vertex to the stack, take any of its neighbors, remove the edge between that 
	 *    vertex and selected neighbor, and set that neighbor as the current vertex.
	 * 3. Repeat step 2 until the current vertex has no more out-going edges 
	 *    (neighbors) and the stack is empty.
	 *    
	 *    Note that obtained circuit will be in reverse order - from end vertex to 
	 *    start vertex. 
	 * 
	 * Rf :
	 * http://www.graph-magics.com/articles/euler.php
	 * https://leetcode.com/problems/reconstruct-itinerary/discuss/78780/Java-Stack-Solution-(Hierholzeru2019s-Algorithm)
	 * 
	 * Other code :
	 * https://leetcode.com/problems/reconstruct-itinerary/discuss/78851/Two-Java-solution-(DFS%2BStack-andand-Backtrace%2BRecursion)-very-easy-to-understand
	 */
	public List<String> findItinerary_iterative(List<List<String>> tickets) {
		LinkedList<String> ret = new LinkedList<String>();
		Map<String, PriorityQueue<String>> map = new HashMap<>();
		Stack<String> stack = new Stack<String>();
		for (List<String> t : tickets) {
			if (!map.containsKey(t.get(0)))
				map.put(t.get(0), new PriorityQueue<String>());
			map.get(t.get(0)).offer(t.get(1));
		}
		
		stack.push("JFK");
		while (!stack.isEmpty()) {
			String next = stack.peek();
			if (map.containsKey(next) && map.get(next).size() > 0)
				stack.push(map.get(next).poll());
			else
				ret.addFirst(stack.pop());
		}
		return ret;
	}
	
	/*
	 * https://leetcode.com/problems/reconstruct-itinerary/discuss/78841/Share-Solution-Java-Greedy-Stack-15ms-with-explanation
	 * 
	 * The passenger choose his/her flight greedy as the lexicographical order. Once 
	 * he figures out go to an airport without departure with more tickets at hand, 
	 * the passenger will push current ticket in a stack and look at whether it is 
	 * possible for him to travel to other places from the airport on his way.
	 * 
	 * "ans" means the first part of flights order from JFK which is strictly follow 
	 * lexicographical order.
	 * 
	 * There can be several loops in the route, but only one leg in the route. So the 
	 * stack stores the order of the leg, and the leg is the last part of the whole 
	 * route.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/reconstruct-itinerary/discuss/78841/Share-Solution-Java-Greedy-Stack-15ms-with-explanation/83686
	 * https://leetcode.com/problems/reconstruct-itinerary/discuss/78841/Share-Solution-Java-Greedy-Stack-15ms-with-explanation/83685
	 */
	public List<String> findItinerary_separate(List<List<String>> tickets) {
		List<String> ans = new ArrayList<String>();
		if (tickets == null || tickets.size() == 0)
			return ans;
		
		Map<String, PriorityQueue<String>> ticketsMap = new HashMap<>();
		for (int i = 0; i < tickets.size(); i++) {
			if (!ticketsMap.containsKey(tickets.get(i).get(0)))
				ticketsMap.put(tickets.get(i).get(0), new PriorityQueue<String>());
			ticketsMap.get(tickets.get(i).get(0)).add(tickets.get(i).get(1));
		}

		String curr = "JFK";
		Stack<String> drawBack = new Stack<String>();
		for (int i = 0; i < tickets.size(); i++) {
			while (!ticketsMap.containsKey(curr) || ticketsMap.get(curr).isEmpty()) {
				drawBack.push(curr);
				curr = ans.remove(ans.size() - 1);
			}
			
			ans.add(curr);
			curr = ticketsMap.get(curr).poll();
		}
		ans.add(curr);
		while (!drawBack.isEmpty())
			ans.add(drawBack.pop());
		return ans;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/reconstruct-itinerary/discuss/138641/Logical-Thinking-with-Clear-Java-Code
	 * 
	 * airports are nodes, and tickets are directed edges.
	 * 
	 * Calculate Euler path. For each point, try to DFS its out-going point. 
	 * There is chance that a DFS won't get a result. So, we do backtrack. 
	 * Out-going points should keep ascending order.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/reconstruct-itinerary/discuss/78789/Java-14ms.-DFS-backtrack
	 * https://leetcode.com/problems/reconstruct-itinerary/discuss/78799/Very-Straightforward-DFS-Solution-with-Detailed-Explanations
	 */
	public List<String> findItinerary_dfs(List<List<String>> tickets) {
		List<String> result = new ArrayList<>();
		Map<String, List<String>> map = new HashMap<>();
		
		for (List<String> ticket : tickets) {
			if (!map.containsKey(ticket.get(0))) {
				map.put(ticket.get(0), new ArrayList<>());
			}
			map.get(ticket.get(0)).add(ticket.get(1));
		}
		// 3. To ensure smallest lexical order, we sort its arrival airports.
		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			Collections.sort(entry.getValue());
		}
		
		findItineraryFrom("JFK", map, new ArrayList<>(), tickets.size(), result);
		result.add(0, "JFK");
		return result;
	}

	private void findItineraryFrom(String start, Map<String, List<String>> map, 
			List<String> curRes, int numTickets, List<String> result) {
		// 1. all tickets are used up
		if (curRes.size() == numTickets) {
			result.addAll(curRes);
			return;
		}
		
		// 2. the path can not use up all tickets
		if (!map.containsKey(start) || map.get(start).isEmpty()) {
			return;
		}
		
		for (int i = 0; i < map.get(start).size(); i++) {
			String dest = map.get(start).get(i);
			map.get(start).remove(i);
			curRes.add(dest);
			
			findItineraryFrom(dest, map, curRes, numTickets, result);
			
			// 4. the first valid path is the final answer
			if (result.size() > 0) {
				return;
			}
			
			map.get(start).add(i, dest);
			curRes.remove(curRes.size() - 1);
		}
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/reconstruct-itinerary/discuss/78772/Python-Dfs-Backtracking
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/reconstruct-itinerary/discuss/78832/Short-C%2B%2B-DFS-iterative-44ms-solution-with-explanation.-No-recursive-calls-no-backtracking.
     * https://leetcode.com/problems/reconstruct-itinerary/discuss/78842/C%2B%2B-non-recursive-O(N)-time-O(N)-space-solution-with-detail-explanations
     * https://leetcode.com/problems/reconstruct-itinerary/discuss/78835/28ms-C%2B%2B-beats-100-Short-and-Elegant.
     */

}
