package OJ0321_0330;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Queue;

public class Number_of_Connected_Components_in_an_Undirected_Graph {
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/discuss/77574/Easiest-2ms-Java-Solution
	 * 
	 * 1. n points = n islands = n trees = n roots.
	 * 2. With each edge added, check which island is e[0] or e[1] belonging to.
	 * 3. If e[0] and e[1] are in same islands, do nothing.
	 * 4. Otherwise, union two islands, and reduce islands count by 1.
	 * 5. Bonus: path compression can reduce time by 50%.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/discuss/77574/Easiest-2ms-Java-Solution/81604
	 * 
	 * Other code:
	 * https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/discuss/516491/Java-Union-Find-DFS-BFS-Solutions-Complexity-Explain-Clean-code
	 */
	public int countComponents_uf(int n, int[][] edges) {
		int[] roots = new int[n];
		for (int i = 0; i < n; i++)
			roots[i] = i;

		for (int[] e : edges) {
			int root1 = find_uf(roots, e[0]);
			int root2 = find_uf(roots, e[1]);
			if (root1 != root2) {
				roots[root1] = root2;  // union
				n--;
			}
		}
		return n;
	}

	public int find_uf(int[] roots, int id) {
		while (roots[id] != id) {
			roots[id] = roots[roots[id]];  // optional: path compression
			id = roots[id];
		}
		return id;
	}
	
	/*
	 * The following function and class are by myself.
	 * 
	 * Other code:
	 * https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/discuss/77574/Easiest-2ms-Java-Solution/81610
	 * https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/discuss/516491/Java-Union-Find-DFS-BFS-Solutions-Complexity-Explain-Clean-code
	 */
	public int countComponents_self(int n, int[][] edges) {
        unionFind_self uf = new unionFind_self(n);
        for (int[] edge : edges) {
            uf.union(edge[0], edge[1]);
        }
        return uf.getN();
    }
    
    class unionFind_self {
        private int[] parents;
        private int[] ranks;
        private int n;
        
        public unionFind_self(int n) {
            this.n = n;
            ranks = new int[n];
            parents = new int[n];
            for (int i = 0; i < n; i++) {
                parents[i] = i;
            }
        }
        
        public int find(int x) {
            if (x != parents[x]) {
                parents[x] = find(parents[x]);
            }
            
            return parents[x];
        }
        
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            
            if (rootX == rootY) {
                return;
            }
            
            if (ranks[rootX] < ranks[rootY]) {
                parents[rootX] = rootY;
            }
            else {
                parents[rootY] = rootX;
                
                if (ranks[rootX] == ranks[rootY]) {
                    ranks[rootX]++;
                }
            }
            
            n--;
        }
        
        public int getN() {
            return this.n;
        }
    }
    
    /*
     * The following 2 functions are from this link.
     * https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/discuss/77578/Java-concise-DFS
     * 
     * start dfsVisit with sources 0-n-1, count number of unvisited sources.
     * 
     * If you run DFS from each of the nodes, all connected components will be 
     * visited if they are a part of the initial node to be explored. If not then 
     * there is some other connected component to be found from another node. Once 
     * the DFS finishes we increment the count because this means we're exploring 
     * another set of connected components.
     * 
     * Rf :
     * https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/discuss/77578/Java-concise-DFS/198679
     */
	public int countComponents_dfs_Set(int n, int[][] edges) {
		if (n <= 1)
			return n;

		Map<Integer, List<Integer>> map = new HashMap<>();
		for (int i = 0; i < n; i++) {
			map.put(i, new ArrayList<>());
		}
		for (int[] edge : edges) {
			map.get(edge[0]).add(edge[1]);
			map.get(edge[1]).add(edge[0]);
		}

		Set<Integer> visited = new HashSet<>();
		int count = 0;

		for (int i = 0; i < n; i++) {
			if (visited.add(i)) {
				dfsVisit_dfs_Set(i, map, visited);
				count++;
			}
		}
		return count;
	}

	private void dfsVisit_dfs_Set(int i, Map<Integer, List<Integer>> map, 
			Set<Integer> visited) {
		
		for (int j : map.get(i)) {
			if (visited.add(j))
				dfsVisit_dfs_Set(j, map, visited);
		}
	}

    /*
     * The following 2 functions are from this link.
     * https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/discuss/77651/Standard-BFS-and-DFS-Solution-JAVA
     * 
     * If you start it from any node you may only find that one connected component, 
     * so instead you start it from all nodes and visit the max number of nodes using 
     * BFS and only increment the count once you visit a new node from the adjacency 
     * list, meaning you only increment the count once you're exploring a new 
     * connected component
     * 
     * Rf :
     * https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/discuss/77578/Java-concise-DFS/198679
     */
	public int countComponents_BFS_Set(int n, int[][] edges) {
		// building graph
		List<List<Integer>> graph = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			graph.add(new ArrayList<>());
		}
		for (int[] item : edges) {
			graph.get(item[1]).add(item[0]);
			graph.get(item[0]).add(item[1]);
		}

		HashSet<Integer> visited = new HashSet<>();
		int count = 0;
		for (int i = 0; i < n; i++) {
			if (!visited.contains(i)) {
				count++;
				bfs_BFS_Set(graph, i, visited);
			}
		}
		
		return count;
	}

	public void bfs_BFS_Set(List<List<Integer>> graph, int i, 
			HashSet<Integer> visited) {
		
		Queue<Integer> q = new LinkedList<>();
		q.offer(i);
		visited.add(i);
		
		while (!q.isEmpty()) {
			int curr = q.poll();
			
			for (int neighbor : graph.get(curr)) {
				if (!visited.contains(neighbor)) {
					q.offer(neighbor);
					visited.add(neighbor);
				}
			}
		}
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/discuss/77583/Java-Union-find-and-DFS-and-BFS-Code-(very-clean)
	 */
	public int countComponents_uf4(int n, int[][] edges) {
		if (n <= 1) {
			return n;
		}

		int[] roots = new int[n];
		for (int i = 0; i < n; i++) {
			roots[i] = i;
		}

		for (int[] edge : edges) {
			int x = find_uf4(roots, edge[0]);
			int y = find_uf4(roots, edge[1]);

			if (x != y) {
				roots[x] = y;
				n--;
			}
		}

		return n;
	}

	public int find_uf4(int[] roots, int id) {
		int x = id;
		while (roots[id] != id) {
			id = roots[id];
		}
		
		while (roots[x] != id) {
			int fa = roots[x];
			roots[x] = id;
			x = fa;
		}

		return id;
	}

	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/discuss/77596/Concise-Java-Union-Find-with-path-compression
	 * 
	 * Each node is in a set of size 1 initially. Every edge would connect 2 separate 
	 * sets if the nodes the edge incident to are in different sets. After processing 
	 * all nodes, we count the number of nodes whose root is itself.
	 */
	public int countComponents_uf2(int n, int[][] edges) {
		int[] root = new int[n];
		for (int i = 0; i < n; i++)
			root[i] = i;
		
		for (int[] edge : edges) {
			int root1 = findRoot_uf2(root, edge[0]), 
				root2 = findRoot_uf2(root, edge[1]);
			
			// Union
			if (root1 != root2)
				root[root2] = root1;
		}
		
		// Count components
		int count = 0;
		for (int i = 0; i < n; i++)
			if (root[i] == i)
				count++;
		
		return count;
	}

	// Find with path compression
	private int findRoot_uf2(int[] root, int i) {
		while (root[i] != i) {
			root[i] = root[root[i]];
			i = root[i];
		}
		return i;
	}
	
	/*
	 * The following variable and 3 functions are from this link.
	 * https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/discuss/77584/AC-JAVA-code-Union-Find
	 */
	private int[] father_uf3;

	public int countComponents_uf3(int n, int[][] edges) {
		Set<Integer> set = new HashSet<Integer>();
		
		father_uf3 = new int[n];
		for (int i = 0; i < n; i++) {
			father_uf3[i] = i;
		}
		
		for (int i = 0; i < edges.length; i++) {
			union_uf3(edges[i][0], edges[i][1]);
		}

		for (int i = 0; i < n; i++) {
			set.add(find_uf3(i));
		}
		
		return set.size();
	}

	int find_uf3(int node) {
		if (father_uf3[node] == node) {
			return node;
		}
		
		father_uf3[node] = find_uf3(father_uf3[node]);
		
		return father_uf3[node];
	}

	void union_uf3(int node1, int node2) {
		father_uf3[find_uf3(node1)] = find_uf3(node2);
	}

	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/discuss/77583/Java-Union-find-and-DFS-and-BFS-Code-(very-clean)
	 * 
	 * Other code:
	 * https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/discuss/516491/Java-Union-Find-DFS-BFS-Solutions-Complexity-Explain-Clean-code
	 */
	public int countComponents_dfs2(int n, int[][] edges) {
		if (n <= 1) {
			return n;
		}

		List<List<Integer>> adjList = new ArrayList<List<Integer>>();
		for (int i = 0; i < n; i++) {
			adjList.add(new ArrayList<Integer>());
		}
		for (int[] edge : edges) {
			adjList.get(edge[0]).add(edge[1]);
			adjList.get(edge[1]).add(edge[0]);
		}

		boolean[] visited = new boolean[n];
		int count = 0;

		for (int i = 0; i < n; i++) {
			if (!visited[i]) {
				count++;
				dfs_dfs2(visited, i, adjList);
			}
		}

		return count;
	}

	public void dfs_dfs2(boolean[] visited, int index, List<List<Integer>> adjList) {
		visited[index] = true;
		
		for (int i : adjList.get(index)) {
			if (!visited[i]) {
				dfs_dfs2(visited, i, adjList);
			}
		}
	}
	
	/*
	 * https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/discuss/77583/Java-Union-find-and-DFS-and-BFS-Code-(very-clean)
	 * 
	 * mark the node as visited when you add it to the queue, not when it's popped. 
	 * Otherwise you might add nodes multiple times to the queue. The result will 
	 * still be OK but it's unnecessary computation.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/discuss/77583/Java-Union-find-and-DFS-and-BFS-Code-(very-clean)/81628
	 * 
	 * Other code:
	 * https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/discuss/516491/Java-Union-Find-DFS-BFS-Solutions-Complexity-Explain-Clean-code
	 */
	public int countComponents_bfs2(int n, int[][] edges) {
        if (n <= 1) {
            return n;
        }
        
        List<List<Integer>> adjList = new ArrayList<List<Integer>>();
        for (int i = 0; i < n; i++) {
            adjList.add(new ArrayList<Integer>());
        }
        for (int[] edge : edges) {
            adjList.get(edge[0]).add(edge[1]);
            adjList.get(edge[1]).add(edge[0]);
        }
        
        boolean[] visited = new boolean[n];
        int count = 0;
        
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                count++;
                Queue<Integer> queue = new LinkedList<Integer>();
                queue.offer(i);
                
                while (!queue.isEmpty()) {
                    int index = queue.poll();
                    visited[index] = true;
                    
                    for (int next : adjList.get(index)) {
                        if (!visited[next]) {
                            queue.offer(next);
                        }
                    }
                }
            }
        }
        
        return count;
    }

    /**
     * Python collections
     * 
     * https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/discuss/319459/Python3-UnionFindDFSBFS-solution
     * https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/discuss/77638/Python-DFS-BFS-Union-Find-solutions
     * https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/discuss/266228/Python-DFSBFSUF
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/discuss/77625/Short-Union-Find-in-Python-Ruby-C%2B%2B
     */

}
