package OJ0261_0270;

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
import java.util.Stack;

/*
 * 看 validTree_UF(int n, int[][] edges) {...}
 */

public class Graph_Valid_Tree {
	/*
	 * The following 2 functions are modified by myself.
	 * 
	 * DFS should only go along each edge once, and therefore only in one direction.
	 * 
	 * First strategy: delete the opposite direction edges from the adjacency list. 
	 * In other words, when we follow an edge A → B, we should lookup Bs adjacency 
	 * list and delete A from it, effectively removing the opposite edge of B → A.
	 * 
	 * Second strategy: instead of using a seen set, to use a seen map that also 
	 * keeps track of the "parent" node that we got to a node from. We'll call this 
	 * map parent. Then, when we iterate through the neighbours of a node, we ignore 
	 * the "parent" node as otherwise it'll be detected as a trivial cycle (and we 
	 * know that the parent node has already been visited by this point anyway). The 
	 * starting node (0 in this implementation) has no "parent", so put it as -1.
	 * 
	 * we just want to avoid going along edges we've already been on (in the opposite 
	 * direction). The parent links prevent that, as each node is only entered for 
	 * exploration once.
	 * 
	 * skip putting v into the hasCycle() recursion, if itself is parent, thus we 
	 * can check directly for visited[v], since each node can be only visited once
	 * 
	 * Visit the node first, iterate through neighbors, if not visited, then do DFS.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/graph-valid-tree/solution/
	 * https://leetcode.com/problems/graph-valid-tree/discuss/69042/AC-Java-Graph-DFS-solution-with-adjacency-list
	 * https://leetcode.com/problems/graph-valid-tree/discuss/69042/AC-Java-Graph-DFS-solution-with-adjacency-list/228743
	 * https://leetcode.com/problems/graph-valid-tree/discuss/69042/AC-Java-Graph-DFS-solution-with-adjacency-list/141876
	 */
	public boolean validTree_dfs3(int n, int[][] edges) {
        // initialize adjacency list
        List<List<Integer>> adjList = new ArrayList<List<Integer>>(n);
        
        // initialize vertices
        for (int i = 0; i < n; i++)
            adjList.add(i, new ArrayList<Integer>());
        
        // add edges    
        for (int i = 0; i < edges.length; i++) {
            int u = edges[i][0], v = edges[i][1];
            adjList.get(u).add(v);
            adjList.get(v).add(u);
        }
        
        boolean[] visited = new boolean[n];
        
        // make sure there's no cycle
        if (hasCycle_dfs3(adjList, 0, visited, -1))
            return false;
        
        // make sure all vertices are connected
        for (int i = 0; i < n; i++) {
            if (!visited[i]) 
                return false;
        }
        
        return true;
    }

	boolean hasCycle_dfs3(List<List<Integer>> adjList, int u, boolean[] visited, 
			int parent) {

		visited[u] = true;

		for (int v : adjList.get(u)) {
			if (v == parent) {
				continue;
			}

			if (visited[v]) {
				return true;
			}

			if (hasCycle_dfs3(adjList, v, visited, u)) {
				return true;
			}
		}

		return false;
	}

	/*
	 * https://leetcode.com/problems/graph-valid-tree/discuss/69081/Java-Straightforward-BFS-Solution
	 * 
	 * should only go along each edge once, and therefore only in one direction.
	 * 
	 * First strategy: delete the opposite direction edges from the adjacency list. 
	 * In other words, when we follow an edge A → B, we should lookup Bs adjacency 
	 * list and delete A from it, effectively removing the opposite edge of B → A.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/graph-valid-tree/solution/
	 * https://leetcode.com/problems/graph-valid-tree/discuss/69110/Java-BFS-Solution
	 */
	public boolean validTree_BFS(int n, int[][] edges) {
		if (n <= 0) {
			return false;
		}
		
		List<Set<Integer>> graph = new ArrayList<Set<Integer>>();
		for (int i = 0; i < n; i++) {
			graph.add(new HashSet<Integer>());
		}
		for (int[] edge : edges) {
			graph.get(edge[0]).add(edge[1]);
			graph.get(edge[1]).add(edge[0]);
		}
		
		boolean[] visited = new boolean[n];
		Queue<Integer> queue = new LinkedList<Integer>();
		queue.add(0);
		visited[0] = true;
		
		while (!queue.isEmpty()) {
			Integer curr = queue.poll();
			n--;
			
			for (Integer next : graph.get(curr)) {
				if (visited[next]) {
					return false;
				}
				
				// we should remove the edge: next -> curr
				// after adding a next into set to avoid duplicate
				// since we already consider curr -> next
				graph.get(next).remove(curr);
				
				queue.offer(next);
				visited[next] = true;
			}
		}

		return n == 0;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/graph-valid-tree/discuss/69080/Simple-1ms-Java-Union-Find-Solution
	 * 
	 * For the graph to be a valid tree, it must have exactly n - 1 edges. Any less, 
	 * and it can't possibly be fully connected. Any more, and it has to contain 
	 * cycles. Additionally, if the graph is fully connected and contains exactly 
	 * n - 1 edges, it can't possibly contain a cycle, and therefore must be a tree!
	 * 
	 * considering each connected component to be a set of nodes. When an edge is 
	 * put between two separate connected components, they are merged into a single 
	 * connected component.
	 * 
	 * Each time there was no merge, it was because we were adding an edge between 
	 * two nodes that were already connected via a path. This means there is now an 
	 * additional path between them-which is the definition of a cycle.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/graph-valid-tree/solution/
	 * 
	 * Other code:
	 * https://leetcode.com/problems/graph-valid-tree/discuss/69018/AC-Java-Union-Find-solution
	 * https://leetcode.com/problems/graph-valid-tree/discuss/69018/AC-Java-Union-Find-solution/211360
	 */
	public boolean validTree_UF(int n, int[][] edges) {
		if (n <= 1)
			return true;
		
		int[] parent = new int[n];
		for (int i = 0; i < n; i++) {
			parent[i] = i;
		}
		
		// perform union find
		for (int[] edge : edges) {
			int x = find_UF(parent, edge[0]);
			int y = find_UF(parent, edge[1]);
			
			// if two vertices happen to be in the same set
            // then there's a cycle
			if (x == y)
				return false;
			
			// union
			parent[y] = x;
		}

		return edges.length == n - 1;
	}

	public int find_UF(int[] parent, int i) {
		if (parent[i] != i) {
			parent[i] = find_UF(parent, parent[i]);
		}
		return parent[i];
	}
	
	/*
	 * The following class and function are from this link.
	 * https://leetcode.com/problems/graph-valid-tree/discuss/69038/Share-my-25-line-DFS-20-line-BFS-and-clean-Union-Find-Java-solutions
	 * 
	 * Check 2 things: 
	 * 1. whether there is loop 
	 * 2. whether the number of connected components is 1
	 * 
	 * considering each connected component to be a set of nodes. When an edge is 
	 * put between two separate connected components, they are merged into a single 
	 * connected component.
	 * 
	 * Each time there was no merge, it was because we were adding an edge between 
	 * two nodes that were already connected via a path. This means there is now an 
	 * additional path between them-which is the definition of a cycle.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/graph-valid-tree/solution/
	 * 
	 * Other code:
	 * https://leetcode.com/problems/graph-valid-tree/discuss/69018/AC-Java-Union-Find-solution/218871
	 * https://leetcode.com/problems/graph-valid-tree/discuss/69018/AC-Java-Union-Find-solution/70953
	 * https://leetcode.com/problems/graph-valid-tree/discuss/69078/AC-Java-solutions%3A-Union-Find-BFS-DFS
	 */
	class UnionFind {
		int[] parent;
		int[] rank;
		int count;

		UnionFind(int n) {
			parent = new int[n];
			rank = new int[n];
			
			// number of components
			count = n;
			
			// initially, each node's parent is itself.
			for (int i = 0; i < n; ++i) {
				parent[i] = i;
			}
		}

		int find(int x) {
			// find root with path compression
			if (x != parent[x]) {
				parent[x] = find(parent[x]);
			}
			return parent[x];
		}

		boolean union(int x, int y) {
			int X = find(x), Y = find(y);
			if (X == Y) {
				return false;
			}
			
			// tree Y is lower
			if (rank[X] > rank[Y]) {
				parent[Y] = X;
			}
			// tree X is lower
			else if (rank[X] < rank[Y]) {
				parent[X] = Y;
			}
			// same height
			else {
				parent[Y] = X;
				++rank[X];
			}
			
			--count;
			return true;
		}
	}

	public boolean validTree_UnionFind(int n, int[][] edges) {
		UnionFind uf = new UnionFind(n);
		for (int[] edge : edges) {
			int x = edge[0], y = edge[1];
			
			// loop detected
			if (!uf.union(x, y)) {
				return false;
			}
		}
		return uf.count == 1;
	}

	/*
	 * https://leetcode.com/problems/graph-valid-tree/solution/
	 * Approach 1: Graph Theory + Iterative Depth-First Search
	 * 
	 * should only go along each edge once, and therefore only in one direction.
	 * 
	 * First strategy: delete the opposite direction edges from the adjacency list. 
	 * In other words, when we follow an edge A → B, we should lookup B's adjacency 
	 * list and delete A from it, effectively removing the opposite edge of B → A.
	 * 
	 * Second strategy: instead of using a seen set, to use a seen map that also 
	 * keeps track of the "parent" node that we got to a node from. We'll call this 
	 * map parent. Then, when we iterate through the neighbours of a node, we ignore 
	 * the "parent" node as otherwise it'll be detected as a trivial cycle (and we 
	 * know that the parent node has already been visited by this point anyway). The 
	 * starting node (0 in this implementation) has no "parent", so put it as -1.
	 * 
	 * we just want to avoid going along edges we've already been on (in the opposite 
	 * direction). The parent links prevent that, as each node is only entered for 
	 * exploration once.
	 */
	public boolean validTree_BFS5(int n, int[][] edges) {
	    List<List<Integer>> adjacencyList = new ArrayList<>();
	    for (int i = 0; i < n; i++) {
	        adjacencyList.add(new ArrayList<>());
	    }
	    for (int[] edge : edges) {
	        adjacencyList.get(edge[0]).add(edge[1]);
	        adjacencyList.get(edge[1]).add(edge[0]);
	    }
	    
	    Map<Integer, Integer> parent = new HashMap<>();
	    parent.put(0, -1);
	    Queue<Integer> queue = new LinkedList<>();
	    queue.offer(0);

	    while (!queue.isEmpty()) {
	        int node = queue.poll();
	        
	        for (int neighbour : adjacencyList.get(node)) {
	            if (parent.get(node) == neighbour) {
	                continue;
	            }
	            
	            if (parent.containsKey(neighbour)) {
	                return false;
	            }
	            
	            queue.offer(neighbour);
	            parent.put(neighbour, node);
	        }
	    }
	    
	    return parent.size() == n;   
	}
	
	/*
	 * https://leetcode.com/problems/graph-valid-tree/solution/
	 * Approach 2: Advanced Graph Theory + Iterative Depth-First Search
	 * 
	 * For the graph to be a valid tree, it must have exactly n - 1 edges. Any less, 
	 * and it can't possibly be fully connected. Any more, and it has to contain 
	 * cycles. Additionally, if the graph is fully connected and contains exactly 
	 * n - 1 edges, it can't possibly contain a cycle, and therefore must be a tree!
	 * 
	 * 1. Check whether or not there are n - 1 edges. If there's not, then return 
	 *    false.
	 * 2. Check whether or not the graph is fully connected. Return true if it is, 
	 *    false if otherwise.
	 * 
	 * For 2, we simply checked if all nodes were reachable from a search starting 
	 * at a single node.
	 * 
	 * We still need to use a seen set to prevent the algorithm getting caught in an 
	 * infinite loop if there are indeed cycles (and to prevent looping on the 
	 * trivial cycles).
	 */
	public boolean validTree_BFS6(int n, int[][] edges) {
		if (edges.length != n - 1)
			return false;

		// Make the adjacency list.
		List<List<Integer>> adjacencyList = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			adjacencyList.add(new ArrayList<>());
		}
		for (int[] edge : edges) {
			adjacencyList.get(edge[0]).add(edge[1]);
			adjacencyList.get(edge[1]).add(edge[0]);
		}
	    
	    Queue<Integer> queue = new LinkedList<>();
	    Set<Integer> seen = new HashSet<>();
	    queue.offer(0);
	    seen.add(0);
	    
		while (!queue.isEmpty()) {
			int node = queue.poll();

			for (int neighbour : adjacencyList.get(node)) {
				if (seen.contains(neighbour))
					continue;

				seen.add(neighbour);
				queue.offer(neighbour);
			}
		}
	    
	    return seen.size() == n;   
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/graph-valid-tree/discuss/69038/Share-my-25-line-DFS-20-line-BFS-and-clean-Union-Find-Java-solutions
	 * 
	 * Check 2 things: 
	 * 1. whether there is loop 
	 * 2. whether the number of connected components is 1
	 * 
	 * Unlike detecting cycle in a directed graph, here for the undirected tree/graph 
	 * we don't need 3 states to mark the node. Once we start from a node, we only 
	 * need two states, visited or unvisited, cause we will fully detect the whole 
	 * tree/graph if it is a connected one.
	 * 
	 * ----------------------------------------------------------
	 * 
	 * Starting from A, since there exists back edge B->A, when traversing DFS 
	 * path A-->B-->A, A is visited for a second time. Namely, A is visited AT THE 
	 * SAME TIME it is still in the DFS recursion stack. Loop detected.
	 * 
	 * ----------------------------------------------------------
	 * 
	 * He has already changed "visited[vertex]" to 1 when he started visiting the 
	 * vertex, thus changing it again right before the end of visiting the vertex to 
	 * 2 is not necessary.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/graph-valid-tree/discuss/69038/Share-my-25-line-DFS-20-line-BFS-and-clean-Union-Find-Java-solutions/161285
	 * https://leetcode.com/problems/graph-valid-tree/discuss/69038/Share-my-25-line-DFS-20-line-BFS-and-clean-Union-Find-Java-solutions/71009
	 * https://leetcode.com/problems/graph-valid-tree/discuss/69038/Share-my-25-line-DFS-20-line-BFS-and-clean-Union-Find-Java-solutions/71004
	 */
	public boolean validTree_dfs6(int n, int[][] edges) {
		int[] visited = new int[n];
		List<List<Integer>> adjList = new ArrayList<>();
		for (int i = 0; i < n; ++i) {
			adjList.add(new ArrayList<Integer>());
		}
		for (int[] edge : edges) {
			adjList.get(edge[0]).add(edge[1]);
			adjList.get(edge[1]).add(edge[0]);
		}
		
		// has cycle
		if (hasCycle_dfs6(-1, 0, visited, adjList)) {
			return false;
		}
		
		// not 1 single connected component
		for (int v : visited) {
			if (v == 0) {
				return false;
			}
		}
		return true;
	}

	private boolean hasCycle_dfs6(int pred, int vertex, int[] visited, 
			List<List<Integer>> adjList) {
		
		// current vertex is being visited
		visited[vertex] = 1;
		
		// successors of current vertex
		for (Integer succ : adjList.get(vertex)) {
			
			// exclude current vertex's predecessor
			if (succ != pred) {
				
				// back edge/loop detected!
				if (visited[succ] == 1) {
					return true;
				}
				else if (visited[succ] == 0) {
					if (hasCycle_dfs6(vertex, succ, visited, adjList)) {
						return true;
					}
				}
			}
		}
		
		visited[vertex] = 2;
		return false;
	}

	/*
	 * https://leetcode.com/problems/graph-valid-tree/discuss/69038/Share-my-25-line-DFS-20-line-BFS-and-clean-Union-Find-Java-solutions
	 * 
	 * Check 2 things: 
	 * 1. whether there is loop 
	 * 2. whether the number of connected components is 1
	 * 
	 * we can do the following thing to avoid setting visited[cur] = 2,
	 * 
	 * when visited[succ] == 1, instead of return false, just continue;
	 * we just need to make sure (1) n == edges.length - 1 (check it at the very 
	 * beginning of the code) and (2) for all the int v in visited, no 0 exist.
	 * 
	 * The reason is for a tree (1) for n nodes there must be edges.length + 1 edges 
	 * and (2) if there is a cycle in it, there is no way to connect all n nodes 
	 * with edges.length+1 edges.
	 * 
	 * -----------------------------------------------------------
	 * 
	 * Unlike detecting cycle in a directed graph, here for the undirected tree/graph 
	 * we don't need 3 states to mark the node. Once we start from a node, we only 
	 * need two states, visited or unvisited, cause we will fully detect the whole 
	 * tree/graph if it is a connected one.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/graph-valid-tree/discuss/69038/Share-my-25-line-DFS-20-line-BFS-and-clean-Union-Find-Java-solutions/71001
	 * https://leetcode.com/problems/graph-valid-tree/discuss/69038/Share-my-25-line-DFS-20-line-BFS-and-clean-Union-Find-Java-solutions/161285
	 */
	public boolean validTree_BFS4(int n, int[][] edges) {
		int[] visited = new int[n];
		
		List<List<Integer>> adjList = new ArrayList<>();
		for (int i = 0; i < n; ++i) {
			adjList.add(new ArrayList<Integer>());
		}
		for (int[] edge : edges) {
			adjList.get(edge[0]).add(edge[1]);
			adjList.get(edge[1]).add(edge[0]);
		}
		
		Deque<Integer> q = new ArrayDeque<>();
		q.addLast(0);
		
		// vertex 0 is in the queue, being visited
		visited[0] = 1;
		
		while (!q.isEmpty()) {
			Integer cur = q.removeFirst();
			
			for (Integer succ : adjList.get(cur)) {
				// loop detected
				if (visited[succ] == 1) {
					return false;
				}
				
				if (visited[succ] == 0) {
					q.addLast(succ);
					visited[succ] = 1;
				}
			}
			
			// visit completed
			visited[cur] = 2;
		}
		
		for (int v : visited) {
			// # of connected components is not 1
			// v != 2 也可以
			if (v == 0) {
				return false;
			}
		}
		return true;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/graph-valid-tree/discuss/69042/AC-Java-Graph-DFS-solution-with-adjacency-list/141876
	 * 
	 * Visit the node first, iterate through neighbors, if not visited, then do DFS.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/graph-valid-tree/discuss/69042/AC-Java-Graph-DFS-solution-with-adjacency-list/71019
	 * 
	 * Other code:
	 * https://leetcode.com/problems/graph-valid-tree/solution/
	 * https://leetcode.com/problems/graph-valid-tree/discuss/69042/AC-Java-Graph-DFS-solution-with-adjacency-list/148582
	 */
	public boolean validTree_dfs4(int n, int[][] edges) {
		List<List<Integer>> graph = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			graph.add(new ArrayList<Integer>());
		}

		// initialize adjacency list.
		for (int i = 0; i < edges.length; i++) {
			graph.get(edges[i][0]).add(edges[i][1]);
			graph.get(edges[i][1]).add(edges[i][0]);
		}
		
		Set<Integer> visited = new HashSet<>();

		// do DFS from vertex 0, after one round DFS, if there is no loop and 
		// visited contains all the vertices, it is a tree.
		boolean res = helper_dfs4(-1, 0, visited, graph);
		if (!res)
			return res;
		
		return visited.size() == n ? true : false;
	}

	public boolean helper_dfs4(int parent, int vertex, Set<Integer> visited, 
			List<List<Integer>> graph) {
		
		visited.add(vertex);
		
		List<Integer> subs = graph.get(vertex);
		for (int v : subs) {
			// if v is vertex's parent, continue.
			if (v == parent)
				continue;
			
			// if v is not vertex's parent, and v is visited. 
			// that presents there is a cycle in this graph.
			if (visited.contains(v))
				return false;
			
			boolean res = helper_dfs4(vertex, v, visited, graph);
			if (!res)
				return false;
		}
		return true;
	}
	
	/*
	 * https://leetcode.com/problems/graph-valid-tree/solution/
	 * Approach 1: Graph Theory + Iterative Depth-First Search
	 * 
	 * DFS should only go along each edge once, and therefore only in one direction.
	 * 
	 * First strategy: delete the opposite direction edges from the adjacency list. 
	 * In other words, when we follow an edge A → B, we should lookup Bs adjacency 
	 * list and delete A from it, effectively removing the opposite edge of B → A.
	 * 
	 * Second strategy: instead of using a seen set, to use a seen map that also 
	 * keeps track of the "parent" node that we got to a node from. We'll call this 
	 * map parent. Then, when we iterate through the neighbours of a node, we ignore 
	 * the "parent" node as otherwise it'll be detected as a trivial cycle (and we 
	 * know that the parent node has already been visited by this point anyway). The 
	 * starting node (0 in this implementation) has no "parent", so put it as -1.
	 * 
	 * we just want to avoid going along edges we've already been on (in the opposite 
	 * direction). The parent links prevent that, as each node is only entered for 
	 * exploration once.
	 */
	public boolean validTree_dfs_stack2(int n, int[][] edges) {
	    List<List<Integer>> adjacencyList = new ArrayList<>();
	    for (int i = 0; i < n; i++) {
	        adjacencyList.add(new ArrayList<>());
	    }
	    for (int[] edge : edges) {
	        adjacencyList.get(edge[0]).add(edge[1]);
	        adjacencyList.get(edge[1]).add(edge[0]);
	    }
	    
	    Map<Integer, Integer> parent = new HashMap<>();
	    parent.put(0, -1);
	    Stack<Integer> stack = new Stack<>();
	    stack.push(0);

	    while (!stack.isEmpty()) {
	        int node = stack.pop();
	        
	        for (int neighbour : adjacencyList.get(node)) {
	            if (parent.get(node) == neighbour) {
	                continue;
	            }
	            if (parent.containsKey(neighbour)) {
	                return false;
	            }
	            
	            stack.push(neighbour);
	            parent.put(neighbour, node);
	        }
	    }
	    
	    return parent.size() == n;   
	}
	
	/*
	 * https://leetcode.com/problems/graph-valid-tree/solution/
	 * Approach 2: Advanced Graph Theory + Iterative Depth-First Search
	 * 
	 * For the graph to be a valid tree, it must have exactly n - 1 edges. Any less, 
	 * and it can't possibly be fully connected. Any more, and it has to contain 
	 * cycles. Additionally, if the graph is fully connected and contains exactly 
	 * n - 1 edges, it can't possibly contain a cycle, and therefore must be a tree!
	 * 
	 * 1. Check whether or not there are n - 1 edges. If there's not, then return 
	 *    false.
	 * 2. Check whether or not the graph is fully connected. Return true if it is, 
	 *    false if otherwise.
	 * 
	 * For 2, we simply checked if all nodes were reachable from a search starting 
	 * at a single node.
	 * 
	 * We still need to use a seen set to prevent the algorithm getting caught in an 
	 * infinite loop if there are indeed cycles (and to prevent looping on the 
	 * trivial cycles).
	 */
	public boolean validTree_dfs_stack3(int n, int[][] edges) {
        
	    if (edges.length != n - 1) return false;
	    
	    // Make the adjacency list.
	    List<List<Integer>> adjacencyList = new ArrayList<>();
	    for (int i = 0; i < n; i++) {
	        adjacencyList.add(new ArrayList<>());
	    }
	    for (int[] edge : edges) {
	        adjacencyList.get(edge[0]).add(edge[1]);
	        adjacencyList.get(edge[1]).add(edge[0]);
	    }
	    
	    Stack<Integer> stack = new Stack<>();
	    Set<Integer> seen = new HashSet<>();
	    stack.push(0);
	    seen.add(0);
	    
	    while (!stack.isEmpty()) {
	        int node = stack.pop();
	        for (int neighbour : adjacencyList.get(node)) {
	            if (seen.contains(neighbour)) continue;
	            seen.add(neighbour);
	            stack.push(neighbour);
	        }
	    }
	    
	    return seen.size() == n;   
	}

	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/graph-valid-tree/discuss/69042/AC-Java-Graph-DFS-solution-with-adjacency-list
	 * 
	 * The (visited[v] && parent != v) means, the node v has been visited, but not 
	 * because it's the parent that's passed in from the previous recursion, so that 
	 * it means it has a cycle.
	 * The second half (!visited[v] && hasCycle(adjList, v, visited, u) means, v has 
	 * not been visited, so let's check with v, to see if there's cycle starting 
	 * from v.
	 * Both condition would mean there's a cycle in the graph, thus return true.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/graph-valid-tree/discuss/69042/AC-Java-Graph-DFS-solution-with-adjacency-list/228743
	 * https://leetcode.com/problems/graph-valid-tree/discuss/69042/AC-Java-Graph-DFS-solution-with-adjacency-list/162013
	 */
	public boolean validTree_dfs2(int n, int[][] edges) {
        // initialize adjacency list
        List<List<Integer>> adjList = new ArrayList<List<Integer>>(n);
        
        // initialize vertices
        for (int i = 0; i < n; i++)
            adjList.add(i, new ArrayList<Integer>());
        
        // add edges    
        for (int i = 0; i < edges.length; i++) {
            int u = edges[i][0], v = edges[i][1];
            adjList.get(u).add(v);
            adjList.get(v).add(u);
        }
        
        boolean[] visited = new boolean[n];
        
        // make sure there's no cycle
        if (hasCycle_dfs2(adjList, 0, visited, -1))
            return false;
        
        // make sure all vertices are connected
        for (int i = 0; i < n; i++) {
            if (!visited[i]) 
                return false;
        }
        
        return true;
    }
    
    // check if an undirected graph has cycle started from vertex u
    boolean hasCycle_dfs2(List<List<Integer>> adjList, int u, boolean[] visited, 
    		int parent) {
    	
        visited[u] = true;
        
        for (int i = 0; i < adjList.get(u).size(); i++) {
            int v = adjList.get(u).get(i);
            
            if ((visited[v] && parent != v) 
            		|| (!visited[v] && hasCycle_dfs2(adjList, v, visited, u)))
                return true;
        }
        
        return false;
    }
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/graph-valid-tree/discuss/69118/Java-simple-DFS-solution
	 */
	public boolean validTree_dfs(int n, int[][] edges) {
		boolean[] visited = new boolean[n];
		
		List<List<Integer>> adj = new ArrayList<>(n);
		for (int i = 0; i < n; i++)
			adj.add(new ArrayList<Integer>());
		for (int[] edge : edges) {
			adj.get(edge[0]).add(edge[1]);
			adj.get(edge[1]).add(edge[0]);
		}

		if (!dfs_dfs(0, -1, visited, adj))
			return false;
		
		for (boolean b : visited)
			if (!b)
				return false;

		return true;
	}

	// p is the pointer to 'parent' node, we ignore it to avoid infinite loop
	private boolean dfs_dfs(int v, int p, boolean[] visited, 
			List<List<Integer>> adj) {

		visited[v] = true;
		for (int i : adj.get(v)) {
			if (i == p)
				continue;
			
			if (visited[i] || !dfs_dfs(i, v, visited, adj))
				return false;
		}
		return true;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/graph-valid-tree/discuss/69042/AC-Java-Graph-DFS-solution-with-adjacency-list/186053
	 * 
	 * Whenever you come across a new node you add. When you visit a node for the 
	 * second time you ignore it. Since the input will always have the n node, 
	 * visited will contain size n unless an edge is not provided. What about the 
	 * situations when edges are provided but the cycle is present?
	 * 
	 * For the graph to be a valid tree, it must have exactly n - 1 edges. Any less, 
	 * and it can't possibly be fully connected. Any more, and it has to contain 
	 * cycles. Additionally, if the graph is fully connected and contains exactly 
	 * n - 1 edges, it can't possibly contain a cycle, and therefore must be a tree!
	 * 
	 * 1. Check whether or not there are n - 1 edges. If there's not, then return 
	 *    false.
	 * 2. Check whether or not the graph is fully connected. Return true if it is, 
	 *    false if otherwise.
	 * 
	 * For 2, we simply checked if all nodes were reachable from a search starting 
	 * at a single node.
	 * 
	 * We still need to use a seen set to prevent the algorithm getting caught in an 
	 * infinite loop if there are indeed cycles (and to prevent looping on the 
	 * trivial cycles).
	 * 
	 * Rf :
	 * https://leetcode.com/problems/graph-valid-tree/discuss/69042/AC-Java-Graph-DFS-solution-with-adjacency-list/320687
	 * https://leetcode.com/problems/graph-valid-tree/solution/
	 * https://leetcode.com/problems/graph-valid-tree/discuss/69042/AC-Java-Graph-DFS-solution-with-adjacency-list/208593
	 */
	public boolean validTree_dfs5(int n, int[][] edges) {
		if (n == 1)
			return true;
		if (edges == null || edges.length == 0)
			return false;

		Set<Integer> visited = new HashSet<Integer>();
		
		List<List<Integer>> graph = new ArrayList<>(n);
		for (int i = 0; i < n; i++)
			graph.add(i, new ArrayList<Integer>());
		for (int i = 0; i < edges.length; i++) {
			graph.get(edges[i][1]).add(edges[i][0]);
			graph.get(edges[i][0]).add(edges[i][1]);
		}
		
		helper_dfs5(graph, 0, visited);
		return visited.size() == n && edges.length == n - 1;
	}

	private void helper_dfs5(List<List<Integer>> graph, int node, 
			Set<Integer> visited) {
		
		if (visited.contains(node))
			return;
		
		visited.add(node);
		for (int edge : graph.get(node)) {
			helper_dfs5(graph, edge, visited);
		}
	}

	/*
	 * https://leetcode.com/problems/graph-valid-tree/discuss/69110/Java-BFS-Solution
	 */
	public boolean validTree_BFS2(int n, int[][] edges) {
		
		// n must be at least 1
		if (n < 1)
			return false;

		// create hashmap to store info of edges
		Map<Integer, Set<Integer>> map = new HashMap<>();
		for (int i = 0; i < n; i++)
			map.put(i, new HashSet<>());
		for (int[] edge : edges) {
			map.get(edge[0]).add(edge[1]);
			map.get(edge[1]).add(edge[0]);
		}

		// bfs starts with node in label "0"
		Set<Integer> set = new HashSet<>();
		Queue<Integer> queue = new LinkedList<>();
		queue.add(0);
		
		while (!queue.isEmpty()) {
			int top = queue.remove();
			
			// if set already contains top, then the graph has cycle
			// hence return false
			if (set.contains(top))
				return false;

			for (int node : map.get(top)) {
				queue.add(node);
				
				// we should remove the edge: node -> top
				// after adding a node into set to avoid duplicate
				// since we already consider top -> node
				map.get(node).remove(top);
			}
			
			set.add(top);
		}
		return set.size() == n;
	}
	
	/*
	 * https://leetcode.com/problems/graph-valid-tree/discuss/69078/AC-Java-solutions%3A-Union-Find-BFS-DFS
	 */
	public boolean validTree_BFS3(int n, int[][] edges) {
		
		// build the graph using adjacent list
		List<Set<Integer>> graph = new ArrayList<Set<Integer>>();
		for (int i = 0; i < n; i++)
			graph.add(new HashSet<Integer>());
		for (int[] edge : edges) {
			graph.get(edge[0]).add(edge[1]);
			graph.get(edge[1]).add(edge[0]);
		}

		// no cycle
		boolean[] visited = new boolean[n];
		Queue<Integer> queue = new ArrayDeque<Integer>();
		queue.add(0);
		
		while (!queue.isEmpty()) {
			int node = queue.poll();
			
			if (visited[node])
				return false;
			
			visited[node] = true;
			
			for (int neighbor : graph.get(node)) {
				queue.offer(neighbor);
				graph.get(neighbor).remove((Integer) node);
			}
		}

		// fully connected
		for (boolean result : visited) {
			if (!result)
				return false;
		}

		return true;
	}
	
	/*
	 * https://leetcode.com/problems/graph-valid-tree/discuss/69078/AC-Java-solutions%3A-Union-Find-BFS-DFS
	 */
	public boolean validTree_dfs_stack(int n, int[][] edges) {
		// build the graph using adjacent list
		List<Set<Integer>> graph = new ArrayList<Set<Integer>>();
		for (int i = 0; i < n; i++)
			graph.add(new HashSet<Integer>());
		for (int[] edge : edges) {
			graph.get(edge[0]).add(edge[1]);
			graph.get(edge[1]).add(edge[0]);
		}

		// no cycle
		boolean[] visited = new boolean[n];
		Deque<Integer> stack = new ArrayDeque<Integer>();
		stack.push(0);
		
		while (!stack.isEmpty()) {
			int node = stack.pop();
			if (visited[node])
				return false;
			
			visited[node] = true;
			
			for (int neighbor : graph.get(node)) {
				stack.push(neighbor);
				graph.get(neighbor).remove(node);
			}
		}

		// fully connected
		for (boolean result : visited) {
			if (!result)
				return false;
		}

		return true;
	}

	/**
     * Python collections
     * 
     * https://leetcode.com/problems/graph-valid-tree/discuss/443909/Python.-Collection-of-'easy-to-understand'-solutions.-BFS-DFS-and-Union-Find
     * https://leetcode.com/problems/graph-valid-tree/discuss/69046/Python-solution-with-detailed-explanation
     * https://leetcode.com/problems/graph-valid-tree/discuss/69020/8-10-lines-Union-Find-DFS-and-BFS
     * https://leetcode.com/problems/graph-valid-tree/discuss/69140/Python-dfs-solution.
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/graph-valid-tree/discuss/69019/Simple-and-clean-c%2B%2B-solution-with-detailed-explanation.
     * https://leetcode.com/problems/graph-valid-tree/discuss/69036/16-lines-C%2B%2B-DFS
     */

}
