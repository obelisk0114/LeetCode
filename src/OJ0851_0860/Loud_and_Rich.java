package OJ0851_0860;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

public class Loud_and_Rich {
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/loud-and-rich/discuss/137943/Easy-and-Readable-DFS-with-Memorilization
	 * 
	 * dfs(person) is the quietest person in the subtree at person. Now dfs(person) is 
	 * either person, or min(dfs(child) for child in person). That is to say, the 
	 * quietest person in the subtree is either the person itself, or the quietest 
	 * person in some subtree of a child of person.
	 * 
	 * We can cache values of dfs(person) as answer[person], when performing our 
	 * post-order traversal of the graph.
	 * 
	 * Rf : https://leetcode.com/articles/loud-and-rich/
	 */
	public int[] loudAndRich_dfs_cache(int[][] richer, int[] quiet) {
		Map<Integer, List<Integer>> map = new HashMap<>();
		for (int[] row : richer) {
			map.putIfAbsent(row[1], new ArrayList<Integer>());
			map.get(row[1]).add(row[0]);
		}
		
		int[] res = new int[quiet.length];
		Arrays.fill(res, -1);
		
		for (int i = 0; i < quiet.length; i++) {
			res[i] = dfs_cache(map, i, quiet, res);
		}
		return res;
	}

	private int dfs_cache(Map<Integer, List<Integer>> adj, int i, int[] quiet, 
			int[] res) {
		
		if (res[i] != -1)
			return res[i];
		
		res[i] = i;
		if (adj.containsKey(i)) {
			for (int elem : adj.get(i)) {
				int n = dfs_cache(adj, elem, quiet, res);
				
				if (quiet[n] < quiet[res[i]])
					res[i] = n;
			}
		}
		return res[i];
	}
	
	/*
	 * The following 2 variables and 2 functions are from this link.
	 * https://leetcode.com/problems/loud-and-rich/discuss/137918/C%2B%2BJavaPython-Concise-DFS
	 * 
	 * For every people, call a sub function dfs to compare the quiet with others, 
	 * who is richer than him.
	 * Also we will note this answer to avoid repeated calculation.
	 * 
	 * Time Complexity: O(richer.length),
	 * Sub function dfs traverse every people only once, and every richer is 
	 * traversed only once.
	 */
	HashMap<Integer, List<Integer>> richer2_dfs_cache2 = new HashMap<>();
	int res_dfs_cache2[];

	public int[] loudAndRich_dfs_cache2(int[][] richer, int[] quiet) {
		int n = quiet.length;
		for (int i = 0; i < n; ++i)
			richer2_dfs_cache2.put(i, new ArrayList<Integer>());
		for (int[] v : richer)
			richer2_dfs_cache2.get(v[1]).add(v[0]);
		
		res_dfs_cache2 = new int[n];
		Arrays.fill(res_dfs_cache2, -1);
		
		for (int i = 0; i < n; i++)
			dfs_cache2(i, quiet);
		return res_dfs_cache2;
	}

	int dfs_cache2(int i, int[] quiet) {
		if (res_dfs_cache2[i] >= 0)
			return res_dfs_cache2[i];
		
		res_dfs_cache2[i] = i;
		for (int j : richer2_dfs_cache2.get(i))
			if (quiet[res_dfs_cache2[i]] > quiet[dfs_cache2(j, quiet)])
				res_dfs_cache2[i] = res_dfs_cache2[j];
		return res_dfs_cache2[i];
	}
	
	/*
	 * The following 2 functions are modified from this link.
	 * https://leetcode.com/problems/loud-and-rich/discuss/517173/Java-clean-BFS-with-memoization-O(nth-triangular-number)-time-O(n)-space
	 * 
	 * Consider the directed graph with edge x -> y if y is richer than x.
	 * For each person x, we want the quietest person in the subtree at x.
	 * 
	 * Construct the graph described above, and say bfs(person) is the quietest person 
	 * in the subtree at person. Notice because the statements are logically 
	 * consistent, the graph must be a DAG - a directed graph with no cycles.
	 * 
	 * Now bfs(person) is either person, or min(bfs(child) for child in person). That 
	 * is to say, the quietest person in the subtree is either the person itself, or 
	 * the quietest person in some subtree of a child of person.
	 * 
	 * We can cache values of bfs(person) as output[person], when performing our 
	 * post-order traversal of the graph. That way, we don't repeat work. This 
	 * technique reduces a quadratic time algorithm down to linear time.
	 * 
	 * Rf : https://leetcode.com/articles/loud-and-rich/
	 */
	public int[] loudestPersonAmongRicher(int[][] richer, int[] quiet) {
		Map<Integer, Set<Integer>> richerThan = new HashMap<Integer, Set<Integer>>();
		for (int[] conn : richer) {
			if (!richerThan.containsKey(conn[1])) {
				richerThan.put(conn[1], new HashSet<Integer>());
			}
			richerThan.get(conn[1]).add(conn[0]);
		}
        
		int[] output = new int[quiet.length];
		Arrays.fill(output, -1);
		
        for (int i = 0; i < output.length; i++) {
			output[i] = bfs(richerThan, i, output, quiet);
		}
		return output;
	}

	private int bfs(Map<Integer, Set<Integer>> richerThan, int start, 
			int[] output, int[] quiet) {
		
		Queue<Integer> todo = new LinkedList<Integer>();
		todo.offer(start);
		
		int result = start;
        
		while (!todo.isEmpty()) {
			int current = todo.poll();
			if (output[current] > -1) {
				if (quiet[result] > quiet[output[current]]) {
					result = output[current];
				}
			} 
            else {
				if (quiet[result] > quiet[current]) {
					result = current;
				}
                    
				for (Integer next : richerThan.getOrDefault(current, 
						new HashSet<Integer>())) {
					
					todo.offer(next);
				}
			}
		}
		return result;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/loud-and-rich/discuss/137987/Java-BFS
	 */
	public int[] loudAndRich_bfs(int[][] richer, int[] quiet) {
		int n = quiet.length;
		
		// construct "adjacent list" , record richer people
		List<List<Integer>> list = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			list.add(new ArrayList<>());
		}
		for (int i = 0; i < richer.length; i++) {
			list.get(richer[i][1]).add(richer[i][0]);
		}

		int[] result = new int[n];
		for (int i = 0; i < n; i++) {
			// no one is richer than i
			if (list.get(i).size() == 0) {
				result[i] = i;
			}
			// otherwise, do BFS
			else {				
				result[i] = bfs2(list, quiet, i);
			}
		}
		return result;
	}

	private int bfs2(List<List<Integer>> list, int[] quiet, int index) {
		int result = index;
		int q = quiet.length; // least quiet
		
		boolean[] visited = new boolean[quiet.length];
		Queue<Integer> queue = new LinkedList<>();
		visited[index] = true;
		queue.offer(index);

		while (!queue.isEmpty()) {
			int curr = queue.poll();
			if (quiet[curr] < q) {
				q = quiet[curr];
				result = curr;
			}
			
			for (int next : list.get(curr)) {
				if (!visited[next]) {
					queue.offer(next);
					visited[next] = true;
				}
			}
		}
		return result;
	}
	
	/*
	 * The following 2 functions are modified from this link.
	 * https://leetcode.com/problems/loud-and-rich/discuss/555133/Simple-DFS-Java
	 */
	void dfs2(boolean[][] rich, boolean[] visited, int src, int[] quiet, 
			int[] least) {
		
		visited[src] = true;
		for (int i = 0; i < visited.length; i++) {
			if (rich[src][i] && !visited[i]) {
				if (quiet[least[0]] >= quiet[i]) {
					least[0] = i;
				}
				dfs2(rich, visited, i, quiet, least);
			}
		}
	}

	public int[] loudAndRich_dfs2(int[][] richer, int[] quiet) {
		int len = quiet.length;
		boolean[][] rich = new boolean[len][len];

		for (int i = 0; i < richer.length; i++) {
			int x = richer[i][0];
			int y = richer[i][1];
			rich[y][x] = true;
		}
		
		int ans[] = new int[len];
		for (int i = 0; i < len; i++) {
			boolean[] visited = new boolean[len];
			int[] least = { i };
			
			dfs2(rich, visited, i, quiet, least);

			ans[i] = least[0];
		}
		return ans;
	}
	
	// by myself
	public int[] loudAndRich_self(int[][] richer, int[] quiet) {
        int[] ans = new int[quiet.length];
        Map<Integer, boolean[]> map = new HashMap<>();
        for (int i = 0; i < richer.length; i++) {
            map.putIfAbsent(richer[i][1], new boolean[quiet.length]);
            map.get(richer[i][1])[richer[i][0]] = true;
        }
        
        for (Map.Entry<Integer, boolean[]> entry : map.entrySet()) {
            boolean[] cur = entry.getValue();
            for (int i = 0; i < cur.length; i++) {
                if (cur[i] && map.containsKey(i)) {
                    boolean[] next = map.get(i);
                    for (int j = 0; j < next.length; j++) {
                        if (next[j]) {
                            cur[j] = true;
                        }
                    }
                }
            }
        }
        
        for (int i = 0; i < quiet.length; i++) {
            if (map.containsKey(i)) {
                int min = i;
                boolean[] run = map.get(i);
                for (int j = 0; j < quiet.length; j++) {
                    if (run[j] && quiet[j] < quiet[min]) {
                        min = j;
                    }
                }
                ans[i] = min;
            }
            else {
                ans[i] = i;
            }
        }
        
        return ans;
    }
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/loud-and-rich/discuss/266859/Python-Cached-DFS
     * https://leetcode.com/problems/loud-and-rich/discuss/403311/Standard-Python-BFS-solution-(similar-to-topological-sorting)
     * https://leetcode.com/problems/loud-and-rich/discuss/138830/python-topology-sort-solution
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/loud-and-rich/discuss/137918/C%2B%2BJavaPython-Concise-DFS
     * https://leetcode.com/problems/loud-and-rich/discuss/510945/C%2B%2B-DFS-Clean-Code-with-Comments
     * https://leetcode.com/problems/loud-and-rich/discuss/138125/DFS-with-memory
     * https://leetcode.com/problems/loud-and-rich/discuss/138088/C%2B%2B-with-topological-sorting
     */

}
