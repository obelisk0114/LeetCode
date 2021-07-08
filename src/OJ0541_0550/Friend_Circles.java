package OJ0541_0550;

import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Arrays;

/*
 * µ¥¦P 323. Number of Connected Components in an Undirected Graph
 */

public class Friend_Circles {
	/*
	 * The following class and function are from this link.
	 * https://leetcode.com/problems/friend-circles/discuss/101336/Java-solution-Union-Find
	 * 
	 * Checking all elements in the matrix is redundant. It's given that M[x][x] = 1 
	 * always (self-friending is a given) and M[x][y] = M[y][x] (matrix is symmetric).
	 * 
	 * Rf : https://leetcode.com/problems/friend-circles/discuss/101336/Java-solution-Union-Find/210804
	 * 
	 * Other code:
	 * https://leetcode.com/problems/friend-circles/discuss/101336/Java-solution-Union-Find/105135
	 * https://leetcode.com/problems/friend-circles/discuss/101336/Java-solution-Union-Find/105134
	 */
	class UnionFind {
		private int count = 0;
		private int[] parent, rank;

		public UnionFind(int n) {
			count = n;
			parent = new int[n];
			rank = new int[n];
			
			for (int i = 0; i < n; i++) {
				parent[i] = i;
			}
		}

		public int find(int p) {
			while (p != parent[p]) {
				parent[p] = parent[parent[p]]; // path compression by halving
				p = parent[p];
			}
			return p;
		}

		public void union(int p, int q) {
			int rootP = find(p);
			int rootQ = find(q);
			if (rootP == rootQ)
				return;
			
			if (rank[rootQ] > rank[rootP]) {
				parent[rootP] = rootQ;
			} 
			else {
				parent[rootQ] = rootP;
				if (rank[rootP] == rank[rootQ]) {
					rank[rootP]++;
				}
			}
			count--;
		}

		public int count() {
			return count;
		}
	}

	public int findCircleNum_uf(int[][] M) {
		int n = M.length;
		UnionFind uf = new UnionFind(n);
		for (int i = 0; i < n - 1; i++) {
			for (int j = i + 1; j < n; j++) {
				if (M[i][j] == 1)
					uf.union(i, j);
			}
		}
		return uf.count();
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/friend-circles/discuss/101387/Easy-Java-Union-Find-Solution
	 */
	public int findCircleNum_findRoot(int[][] M) {
		int count = M.length;
		int[] root = new int[M.length];
		for (int i = 0; i < M.length; i++) {
			root[i] = i;
		}
		
		for (int i = 0; i < M.length; i++) {
			for (int j = 0; j < M[0].length; j++) {
				if (M[i][j] == 1) {
					int rooti = findRoot(root, i);
					int rootj = findRoot(root, j);
					if (rooti != rootj) {
						root[rooti] = rootj;
						count--;
					}
				}
			}
		}
		return count;
	}

	public int findRoot(int[] roots, int id) {
		while (roots[id] != id) {
			roots[id] = roots[roots[id]];
			id = roots[id];
		}
		return id;
	}
	
	/*
	 * The following 3 functions are from this link.
	 * https://leetcode.com/problems/number-of-provinces/solution/
	 * Approach 3: Using Union-Find Method
	 * 
	 * We make use of a parent array of size N. For every node traversed, we traverse
	 * over all the nodes directly connected to it and assign them to a single group
	 * which is represented by their parent node.
	 * 
	 * For every new pair of nodes found, we look for the parents of both the nodes.
	 * If the parents nodes are the same, it indicates that they have already been
	 * united into the same group. If the parent nodes differ, it means they are yet
	 * to be united. Thus, for the pair of nodes (x, y), while forming the union, we
	 * assign parent[parent[x]] = parent[y], which ultimately combines them into
	 * the same group
	 */
	int find_uf2(int parent[], int i) {
        if (parent[i] == -1)
            return i;
        return find_uf2(parent, parent[i]);
    }

    void union_uf2(int parent[], int x, int y) {
        int xset = find_uf2(parent, x);
        int yset = find_uf2(parent, y);
        if (xset != yset)
            parent[xset] = yset;
    }
    public int findCircleNum_uf2(int[][] M) {
        int[] parent = new int[M.length];
        Arrays.fill(parent, -1);
        for (int i = 0; i < M.length; i++) {
            for (int j = 0; j < M.length; j++) {
                if (M[i][j] == 1 && i != j) {
                    union_uf2(parent, i, j);
                }
            }
        }
        int count = 0;
        for (int i = 0; i < parent.length; i++) {
            if (parent[i] == -1)
                count++;
        }
        return count;
    }
	
	/*
	 * The following function and class are by myself.
	 * 
	 * Other code:
	 * https://leetcode.com/problems/friend-circles/discuss/172333/Java-very-standard-union-find
	 */
	public int findCircleNum_self(int[][] M) {
        UnionFind_self uf = new UnionFind_self(M.length);
        for (int i = 0; i < M.length; i++) {
            for (int j = 0; j < M[i].length; j++) {
                if (M[i][j] == 1) {
                    uf.union(i, j);
                }
            }
        }
        
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < M.length; i++) {
            int parent = uf.find(i);
            set.add(parent);
        }
        return set.size();
    }
    
    class UnionFind_self {
        private int[] parents;
        private int[] ranks;
        
        public UnionFind_self(int n) {
            parents = new int[n];
            for (int i = 0; i < n; i++) {
                parents[i] = i;
            }
            ranks = new int[n];
        }
        
        public int find(int root) {
            if (root != parents[root]) {
                parents[root] = find(parents[root]);
            }
            return parents[root];
        }
        
        public void union(int a, int b) {
            int rootA = find(a);
            int rootB = find(b);
            
            if (rootA == rootB)
                return;
            
            if (ranks[rootA] > ranks[rootB]) {
                parents[rootB] = rootA;
            }
            else if (ranks[rootA] < ranks[rootB]) {
                parents[rootA] = rootB;
            }
            else {
                parents[rootA] = rootB;
                ranks[rootB]++;
            }
        }
    }
    
    /*
     * The following 2 functions are from this link.
     * https://leetcode.com/problems/friend-circles/discuss/101338/Neat-DFS-java-solution/105148
     * 
     * DFS starting from every node that isn't visited
     * 
     * Other code :
     * https://leetcode.com/problems/friend-circles/discuss/141185/Simple-Java-DFS-beats-99.25-(10ms)
     */
	public int findCircleNum_dfs(int[][] M) {
		// visited[i] means if ith person is visited in this algorithm
		boolean[] visited = new boolean[M.length];
		int count = 0;
		
		for (int i = 0; i < M.length; i++) {
			if (!visited[i]) {
				dfs(M, visited, i);
				count++;
			}
		}
		return count;
	}

	private void dfs(int[][] M, boolean[] visited, int person) {
		for (int other = 0; other < M.length; other++) {
			if (M[person][other] == 1 && !visited[other]) {
				// We found an unvisited person in the current friend cycle
				visited[other] = true;
				dfs(M, visited, other); // Start DFS on this new found person
			}
		}
	}
	
	/*
	 * The following 2 functions are by myself.
	 * 
	 * Rf : https://leetcode.com/problems/accounts-merge/discuss/109158/Java-Solution-(Build-graph-+-DFS-search)/111223
	 */
	public int findCircleNum_BFS_self(int[][] M) {
        int count = 0;
		boolean[] visited = new boolean[M.length];
		for (int i = 0; i < M.length; i++) {
			if (M[i][i] == 1 && !visited[i]) {
				count++;
				BFS_self(i, M, visited);
			}
        }
		return count;
	}

	public void BFS_self(int student, int[][] M, boolean[] visited) {
		Queue<Integer> queue = new LinkedList<>();
		queue.add(student);
		
		while (queue.size() > 0) {
			int j = queue.poll();
				
			for (int k = 0; k < M[j].length; k++) {
				if (M[j][k] == 1 && !visited[k]) {
					queue.add(k);
					visited[k] = true;
                }
            }
		}
	}
	
	/*
	 * We start from a particular node and visit all its directly connected node 
	 * first. After all the direct neighbors have been visited, we apply the same
	 * process to the neighbor nodes as well. Thus, we exhaust the nodes of a graph
	 * on a level by level basis.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/number-of-provinces/solution/
	 */
	public int findCircleNum_BFS3_modified(int[][] isConnected) {
		Deque<Integer> queue = new ArrayDeque<>();
        boolean[] visited = new boolean[isConnected.length];
        
        int count = 0;
        for (int i = 0; i < isConnected.length; i++) {
            if (!visited[i]) {
                queue.offerLast(i);
                visited[i] = true;
                
                while (!queue.isEmpty()) {
                    int s = queue.pollFirst();
                    
                    for (int j = 0; j < isConnected.length; j++) {
                        if (isConnected[s][j] == 1 && !visited[j]) {
                            queue.offerLast(j);
                            visited[s] = true;
                        }
                    }
                }
                
                count++;
            }
        }
        
        return count;
    }
	
	/*
	 * https://leetcode.com/problems/number-of-provinces/solution/
	 * Approach 2: Using Breadth First Search
	 * 
	 * We start from a particular node and visit all its directly connected node 
	 * first. After all the direct neighbors have been visited, we apply the same
	 * process to the neighbor nodes as well. Thus, we exhaust the nodes of a graph
	 * on a level by level basis.
	 * 
	 * We apply BFS starting from one of the nodes. We make use of a visited array
	 * to keep a track of the already visited nodes. We increment the count of
	 * connected components whenever we need to start off with a new node as the root
	 * node for applying BFS which haven't been already visited. 
	 */
	public int findCircleNum_BFS3(int[][] isConnected) {
        boolean[] visited = new boolean[isConnected.length];
        int count = 0;
        
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < isConnected.length; i++) {
            if (!visited[i]) {
                queue.add(i);
                while (!queue.isEmpty()) {
                    int s = queue.remove();
                    visited[s] = true;
                    
                    for (int j = 0; j < isConnected.length; j++) {
                        if (isConnected[s][j] == 1 && !visited[j]) {
                            queue.add(j);
                        }
                    }
                }
                
                count++;
            }
        }
        
        return count;
    }
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/friend-circles/discuss/101344/Java-BFS-Equivalent-to-Finding-Connected-Components-in-a-Graph
	 * 
	 * Rf :
	 * https://leetcode.com/problems/friend-circles/discuss/101344/Java-BFS-Equivalent-to-Finding-Connected-Components-in-a-Graph/105159
	 * https://leetcode.com/problems/friend-circles/discuss/101344/Java-BFS-Equivalent-to-Finding-Connected-Components-in-a-Graph/105160
	 */
	public int findCircleNum_BFS2(int[][] M) {
		int count = 0;
		
		for (int i = 0; i < M.length; i++)
			if (M[i][i] == 1) {
				count++;
				BFS2(i, M);
			}
		
		return count;
	}

	public void BFS2(int student, int[][] M) {
		Queue<Integer> queue = new LinkedList<>();
		queue.add(student);
		
		while (queue.size() > 0) {
			int queueSize = queue.size();
			for (int i = 0; i < queueSize; i++) { // Can be omitted, we don't need level
				int j = queue.poll();
				M[j][j] = 2;         // marks as visited
				
				for (int k = 0; k < M[0].length; k++)   // k can start from "student"
					if (M[j][k] == 1 && M[k][k] == 1)
						queue.add(k);
			}
		}
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/friend-circles/discuss/201875/CLASSIC-DFS-JAVA
	 * 
	 * Step 1 : Build graph.
	 * Step 2: Create visited nodes array
	 * Step 3 : Iterate visited array and Engage DFS to mark visited
	 * Step 3.5 : If visited array has been marked true already, friend is already 
	 *            part of circle. else it's a new circle.
	 * Step 4 : return circle
	 */
	public int findCircleNum_HashMap(int[][] M) {
		HashMap<Integer, List<Integer>> graph = new HashMap<>();
		for (int i = 0; i < M.length; i++) { // Step 1
			for (int j = 0; j < M[0].length; j++) {
				if (M[i][j] == 1) {
					if (!graph.containsKey(i))
						graph.put(i, new ArrayList<>());
					graph.get(i).add(j);
				}
			}
		}
		
		boolean[] visited = new boolean[M.length]; // Step 2
		int circles = 0;
		for (int i = 0; i < visited.length; i++) { // Step 3
			if (visited[i]) { // Step 3.5
				continue;
			} 
			else {
				circles++;
				dfs_HashMap(graph, i, visited);
			}
		}
		return circles; // Step 4
	}

	private void dfs_HashMap(HashMap<Integer, List<Integer>> graph, int student, 
			boolean[] visited) {
		if (visited[student])
			return;
		
		visited[student] = true;
		for (int friend : graph.get(student)) {
			dfs_HashMap(graph, friend, visited);
		}
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/friend-circles/discuss/152441/Python-Union-Find-solution
     * https://leetcode.com/problems/friend-circles/discuss/101349/Python-Simple-Explanation
     * https://leetcode.com/problems/friend-circles/discuss/101340/Oneliners-%3A-P
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/friend-circles/discuss/101354/C%2B%2B-Clean-Code-DFSorUnionFind
     */

}
