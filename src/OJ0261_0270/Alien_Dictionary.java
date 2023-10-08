package OJ0261_0270;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Queue;
import java.awt.Point;

/*
 * https://leetcode.com/problems/alien-dictionary/discuss/70111/The-description-is-wrong/72174
 * 
 * The description said the `words` are sorted lexicographically, not the individual 
 * letters. As an example, consider these words from the English dictionary:
 * 
 * game, zebra, zoo
 * 
 * The word zoo doesn't imply the ordering `z < o`.
 * 
 * https://leetcode.com/problems/alien-dictionary/discuss/70207/Clarification-about-prefix-problem.-%22abc%22-and-%22ab%22
 * 
 * e.g. 1 "abc" "ab" expected answer ==> ""
 * e.g. 2 "ab" "abc" expected answer ==> "abc"
 * 
 * there is a "rule" or "assumption" untold to us is that if one string is another 
 * string's prefix, the "prefix string" must be ahead of the other one to make it a 
 * valid order, otherwise return "".
 * 
 * https://leetcode.com/problems/alien-dictionary/solution/
 * 
 * In a valid alphabet, prefixes are always first
 * Your output string must contain all unique letters that were within the input 
 * list, including those that could be in any position within the ordering. It 
 * should not contain any additional letters that were not in the input.
 */

public class Alien_Dictionary {
	/*
	 * https://leetcode.com/problems/alien-dictionary/solution/
	 * Approach 1: Breadth-First Search
	 * 
	 * Where two words are adjacent, we need to look for the first difference between 
	 * them. That difference tells us the relative order between two letters.
	 * 
	 * Start from letters that there are no letters that have to be before any of 
	 * these. Add them and remove these letters and edges from the graph.
	 * 
	 * Instead of keeping track of all the other letters that must be before a 
	 * particular letter, we only need to keep track of how many of them there are! 
	 * We call the number of incoming edges the indegree of a node.
	 * 
	 * instead of removing an edge from a reverse adjacency list, we can simply 
	 * decrement the count by 1. Once the count reaches 0, this is equivalent to 
	 * there being no incoming edges left in the reverse adjacency list.
	 * 
	 * do a BFS for all letters that are reachable, adding each letter to the output 
	 * as soon as it's reachable. A letter is reachable once all of the letters that 
	 * need to be before it have been added to the output.
	 * 
	 * We should initially put all letters with an in-degree of 0 onto that queue. 
	 * Each time a letter gets down to an in-degree of 0, it is added to the queue.
	 * 
	 * we check whether or not all letters were put in the output list. If some are 
	 * missing, this is because we got to a point where all remaining letters had at 
	 * least one edge going in; this means there must be a cycle!
	 * 
	 * In these cases that a word is followed by its own prefix, it is impossible to 
	 * come up with a valid ordering and so we should return "".
	 */
	public String alienOrder_bfs4(String[] words) {
	    
	    // Step 0: Create data structures and find all unique letters.
	    Map<Character, List<Character>> adjList = new HashMap<>();
	    Map<Character, Integer> counts = new HashMap<>();
	    for (String word : words) {
	        for (char c : word.toCharArray()) {
	            counts.put(c, 0);
	            adjList.put(c, new ArrayList<>());
	        }
	    }
	    
	    // Step 1: Find all edges.
	    for (int i = 0; i < words.length - 1; i++) {
	        String word1 = words[i];
	        String word2 = words[i + 1];
	        
	        // Check that word2 is not a prefix of word1.
	        if (word1.length() > word2.length() && word1.startsWith(word2)) {
	            return "";
	        }
	        
	        // Find the first non match and insert the corresponding relation.
	        for (int j = 0; j < Math.min(word1.length(), word2.length()); j++) {
	            if (word1.charAt(j) != word2.charAt(j)) {
	                adjList.get(word1.charAt(j)).add(word2.charAt(j));
	                counts.put(word2.charAt(j), counts.get(word2.charAt(j)) + 1);
	                break;
	            }
	        }
	    }
	    
	    // Step 2: Breadth-first search.
	    StringBuilder sb = new StringBuilder();
	    Queue<Character> queue = new LinkedList<>();
	    for (Character c : counts.keySet()) {
	        if (counts.get(c).equals(0)) {
	            queue.add(c);
	        }
	    }
	    while (!queue.isEmpty()) {
	        Character c = queue.remove();
	        sb.append(c);
	        
	        for (Character next : adjList.get(c)) {
	            counts.put(next, counts.get(next) - 1);
	            
	            if (counts.get(next).equals(0)) {
	                queue.add(next);
	            }
	        }
	    }
	    
	    if (sb.length() < counts.size()) {
	        return "";
	    }
	    
	    return sb.toString();
	}
	
	/*
	 * The following 3 variables and 2 functions are from this link.
	 * https://leetcode.com/problems/alien-dictionary/solution/
	 * Approach 2: Depth-First Search
	 * 
	 * Where two words are adjacent, we need to look for the first difference between 
	 * them. That difference tells us the relative order between two letters.
	 * 
	 * In a depth-first search, nodes are returned once they either have no outgoing 
	 * links left, or all their outgoing links have been visited. Therefore, the 
	 * order in which nodes are returned by the depth-first search will be the 
	 * reverse of a valid alphabet order.
	 * 
	 * If we made a reverse adjacency list instead of a forward one, the output order 
	 * would be correct (without needing to be reversed).
	 * 
	 * In directed graphs, we often detect cycles by using graph coloring. All nodes 
	 * start as white, and then once they're first visited they become grey, and then 
	 * once all their outgoing nodes have been fully explored, they become black. We 
	 * know there is a cycle if we enter a node that is currently grey (it works 
	 * because all nodes that are currently on the stack are grey. Nodes are changed 
	 * to black when they are removed from the stack).
	 */
	private Map<Character, List<Character>> reverseAdjList_dfsSol = new HashMap<>();
    private Map<Character, Boolean> seen_dfsSol = new HashMap<>();
    private StringBuilder output_dfsSol = new StringBuilder();
    
    public String alienOrder_dfsSol(String[] words) {
        
        // Step 0: Put all unique letters into reverseAdjList as keys.
        for (String word : words) {
            for (char c : word.toCharArray()) {
                reverseAdjList_dfsSol.putIfAbsent(c, new ArrayList<>());
            }
        }
        
        // Step 1: Find all edges and add reverse edges to reverseAdjList.
        for (int i = 0; i < words.length - 1; i++) {
            String word1 = words[i];
            String word2 = words[i + 1];
            
            // Check that word2 is not a prefix of word1.
            if (word1.length() > word2.length() && word1.startsWith(word2)) {
                return "";
            }
            
            // Find the first non match and insert the corresponding relation.
            for (int j = 0; j < Math.min(word1.length(), word2.length()); j++) {
                if (word1.charAt(j) != word2.charAt(j)) {
                    reverseAdjList_dfsSol.get(word2.charAt(j)).add(word1.charAt(j));
                    break;
                }
            }
        }
        
        // Step 2: DFS to build up the output list.
		for (Character c : reverseAdjList_dfsSol.keySet()) {
			boolean result = dfs_dfsSol(c);
			
			if (!result)
				return "";
		}

		if (output_dfsSol.length() < reverseAdjList_dfsSol.size()) {
			return "";
		}
		return output_dfsSol.toString();
    }
    
    // Return true iff no cycles detected.
    private boolean dfs_dfsSol(Character c) {
    	// If this node was grey (false), a cycle was detected.
        if (seen_dfsSol.containsKey(c)) {
            return seen_dfsSol.get(c);
        }
        
		seen_dfsSol.put(c, false);
		
		for (Character next : reverseAdjList_dfsSol.get(c)) {
			boolean result = dfs_dfsSol(next);
			
			if (!result)
				return false;
		}
		
		seen_dfsSol.put(c, true);
		output_dfsSol.append(c);
		
		return true;
    }
	
	/*
	 * https://leetcode.com/problems/alien-dictionary/discuss/70119/Java-AC-solution-using-BFS
	 * 
	 * the first character that is different between two adjacent words reflect the 
	 * lexicographical order.
	 * 
	 * x -> set: y,z,t,w means x comes before all the letters in the set.
	 * 
	 * HashMap "degree", the number means "how many letters come before the key"
	 * 
	 * only from adjacent two words can you tell the immediate order of letters. A 
	 * counter example, first word is wrt, last word is rftt, if compare this pair, 
	 * we know that r is after w, but r may not IMMEDIATELY after w.
	 * 
	 * One edge case is when the second word is the prefix of the first word, 
	 * for example: ["abc", "ab"]
	 * Because the prefix should always be at the front.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/alien-dictionary/discuss/70119/Java-AC-solution-using-BFS/72252
	 * https://leetcode.com/problems/alien-dictionary/discuss/70119/Java-AC-solution-using-BFS/72247
	 * https://leetcode.com/problems/alien-dictionary/discuss/70119/Java-AC-solution-using-BFS/513865
	 */
	public String alienOrder_bfs3(String[] words) {
		Map<Character, Set<Character>> map = new HashMap<Character, Set<Character>>();
		Map<Character, Integer> degree = new HashMap<Character, Integer>();
		String result = "";
		
		if (words == null || words.length == 0)
			return result;
		
		for (String s : words) {
			for (char c : s.toCharArray()) {
				degree.put(c, 0);
			}
		}

		for (int i = 0; i < words.length - 1; i++) {
			String cur = words[i];
			String next = words[i + 1];
			
			// In a valid alphabet, prefixes are always first
			if (cur.length() > next.length() && cur.startsWith(next)) {
				return "";
			}
			
			int length = Math.min(cur.length(), next.length());
			for (int j = 0; j < length; j++) {
				char c1 = cur.charAt(j);
				char c2 = next.charAt(j);
				
				if (c1 != c2) {
					Set<Character> set = new HashSet<Character>();
					if (map.containsKey(c1))
						set = map.get(c1);
					
					if (!set.contains(c2)) {
						set.add(c2);
						map.put(c1, set);
						degree.put(c2, degree.get(c2) + 1);
					}
					break;
				}
			}
		}
		
		Queue<Character> q = new LinkedList<Character>();
		for (char c : degree.keySet()) {
			if (degree.get(c) == 0)
				q.add(c);
		}
		
		while (!q.isEmpty()) {
			char c = q.remove();
			result += c;
			
			if (map.containsKey(c)) {
				for (char c2 : map.get(c)) {
					degree.put(c2, degree.get(c2) - 1);
					
					if (degree.get(c2) == 0)
						q.add(c2);
				}
			}
		}
		
		if (result.length() != degree.size())
			return "";
		
		return result;
	}

	/*
	 * https://leetcode.com/problems/alien-dictionary/discuss/70113/Java-BFS-Solution
	 * 
	 * It is enough to build graph based on adjacent words. Because if two words are 
	 * not adjacent, the words between them are enough to pass their topological 
	 * order.
	 * 
	 * For example, "wrta","wrbv","wrf". we know that t ranks higher than b and b 
	 * ranks higher than f, do we have to construct the pair t ranks higher than f? 
	 * That is not necessary in this case.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/alien-dictionary/discuss/70113/Java-BFS-Solution/72177
	 * https://leetcode.com/problems/alien-dictionary/discuss/70113/Java-BFS-Solution/187468
	 */
	public String alienOrder_bfs2(String[] words) {
		List<Set<Integer>> adj = new ArrayList<>();
		for (int i = 0; i < 26; i++) {
			adj.add(new HashSet<Integer>());
		}
		
		int[] degree = new int[26];
		Arrays.fill(degree, -1);

		for (int i = 0; i < words.length; i++) {
			for (char c : words[i].toCharArray()) {
				if (degree[c - 'a'] < 0) {
					degree[c - 'a'] = 0;
				}
			}
			
			if (i > 0) {
				String w1 = words[i - 1], w2 = words[i];
				int len = Math.min(w1.length(), w2.length());
				
				for (int j = 0; j < len; j++) {
					int c1 = w1.charAt(j) - 'a', c2 = w2.charAt(j) - 'a';
					
					if (c1 != c2) {
						if (!adj.get(c1).contains(c2)) {
							adj.get(c1).add(c2);
							degree[c2]++;
						}
						break;
					}

					// "abcd" -> "ab"
					if (j == w2.length() - 1 && w1.length() > w2.length()) {
						return "";
					}
				}
			}
		}

		Queue<Integer> q = new LinkedList<>();
		for (int i = 0; i < degree.length; i++) {
			if (degree[i] == 0) {
				q.add(i);
			}
		}

		StringBuilder sb = new StringBuilder();
		while (!q.isEmpty()) {
			int i = q.poll();
			sb.append((char) ('a' + i));
			
			for (int j : adj.get(i)) {
				degree[j]--;
				
				if (degree[j] == 0) {
					q.add(j);
				}
			}
		}

		for (int d : degree) {
			if (d > 0) {
				return "";
			}
		}

		return sb.toString();
	}

	/*
	 * The following variable and 3 functions are from this link.
	 * https://leetcode.com/problems/alien-dictionary/discuss/70115/3ms-Clean-Java-Solution-(DFS)
	 * 
	 * The key to this problem is:
	 * A topological ordering is possible if and only if the graph has no directed 
	 * cycles
	 * 
	 * Let's build a graph and perform a DFS. The following states made things easier.
	 * 1. visited[i] = -1. Not even exist.
	 * 2. visited[i] = 0. Exist. Non-visited.
	 * 3. visited[i] = 1. Visiting.
	 * 4. visited[i] = 2. Visited.
	 * 
	 * --------------------------------------------------------------
	 * 
	 * It compares the orders between words but not the order of alphabets within 
	 * each word.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/alien-dictionary/discuss/70115/3ms-Clean-Java-Solution-(DFS)/72195
	 * https://leetcode.com/problems/alien-dictionary/discuss/70115/3ms-Clean-Java-Solution-(DFS)/483406
	 * https://leetcode.com/problems/alien-dictionary/discuss/70115/3ms-Clean-Java-Solution-(DFS)/296591
	 * https://leetcode.com/problems/alien-dictionary/discuss/70115/3ms-Clean-Java-Solution-(DFS)/198341
	 */
	private final int N_dfs = 26;

	public String alienOrder_dfs(String[] words) {
		boolean[][] adj = new boolean[N_dfs][N_dfs];
		int[] visited = new int[N_dfs];
		buildGraph_dfs(words, adj, visited);

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < N_dfs; i++) {
			// unvisited
			if (visited[i] == 0) {
				if (!dfs_dfs(adj, visited, sb, i))
					return "";
			}
		}
		return sb.reverse().toString();
	}

	public boolean dfs_dfs(boolean[][] adj, int[] visited, StringBuilder sb, int i) {
		// 1 = visiting
		visited[i] = 1;
		
		for (int j = 0; j < N_dfs; j++) {
			// connected
			if (adj[i][j]) {
				// 1 => 1, cycle
				if (visited[j] == 1)
					return false;
				
				// 0 = unvisited
				if (visited[j] == 0) {
					if (!dfs_dfs(adj, visited, sb, j))
						return false;
				}
			}
		}
		
		// 2 = visited
		visited[i] = 2;
		
		sb.append((char) (i + 'a'));
		return true;
	}

	public void buildGraph_dfs(String[] words, boolean[][] adj, int[] visited) {
		// -1 = not even existed
		Arrays.fill(visited, -1);
		
		for (int i = 0; i < words.length; i++) {
			for (char c : words[i].toCharArray())
				visited[c - 'a'] = 0;
			
			if (i > 0) {
				String w1 = words[i - 1], w2 = words[i];
				
				// ["abc","ab"] => expect: ""
				if (!w1.equals(w2) && w1.startsWith(w2)) {
					Arrays.fill(visited, 2);
					return;
				}

				int len = Math.min(w1.length(), w2.length());				
				for (int j = 0; j < len; j++) {
					char c1 = w1.charAt(j), c2 = w2.charAt(j);
					
					if (c1 != c2) {
						adj[c1 - 'a'][c2 - 'a'] = true;
						break;
					}
				}
			}
		}
	}
	
	/*
	 * The following 3 functions are from this link.
	 * https://leetcode.com/problems/alien-dictionary/discuss/329248/Topological-Sort-Explained-With-Video-(Chinese-)
	 * https://www.youtube.com/watch?v=RIrTuf4DfPE&t=8s
	 */
	public String alienOrder_bfs(String[] words) {
		Map<Character, Set<Character>> graph = new HashMap<>();
		int[] inDegree = new int[26];

		buildGraph_bfs(graph, inDegree, words);

		return bfs_bfs(graph, inDegree);
	}

    private void buildGraph_bfs(Map<Character, Set<Character>> graph, 
    		int[] inDegree, String[] words) {
    	
        // !!! Attention: 這個地方一定要先把所有出現的 char 放到 graph 裡，
        // 這樣才能完全涵蓋所有的 char
		for (String s : words) {
			for (char c : s.toCharArray()) {
				graph.putIfAbsent(c, new HashSet<>());
			}
		}

		for (int i = 1; i < words.length; i++) {
			String first = words[i - 1];
			String second = words[i];
			int len = Math.min(first.length(), second.length());
			
			for (int j = 0; j < len; j++) {
				char out = first.charAt(j);
				char in = second.charAt(j);
				
				if (out != in) {
					if (!graph.get(out).contains(in)) {
						graph.get(out).add(in);
						inDegree[in - 'a']++;
					}
					break;
				}
				
				if (j + 1 == len && first.length() > second.length()) {
					graph.clear();
					return;
				}
			}
		}
	}

	private String bfs_bfs(Map<Character, Set<Character>> graph, int[] inDegree) {
		StringBuilder sb = new StringBuilder();
		
		Queue<Character> q = new LinkedList<>();
		for (char c : graph.keySet()) {
			if (inDegree[c - 'a'] == 0) {
				q.offer(c);
			}
		}

		while (!q.isEmpty()) {
			char out = q.poll();
			sb.append(out);
			
			for (char in : graph.get(out)) {
				inDegree[in - 'a']--;
				
				if (inDegree[in - 'a'] == 0) {
					q.offer(in);
				}
			}
		}

		// find those characters that will never be used (invalid order e.g. cycle)
		return sb.length() == graph.size() ? sb.toString() : "";
	}
	
	/*
	 * https://leetcode.com/problems/alien-dictionary/discuss/70169/My-Concise-JAVA-solution-based-on-Topological-Sorting
	 * 
	 * 1. Convert characters to a graph: Adjacency lists
	 * 2. Topological sorting: keep adding elements whose in-degree is 0
	 * 
	 * In-degree: The amount of dependencies of the element E, which means the amount 
	 * of E's predecessors. We have to fulfill E's predecessors before executing E, 
	 * when E's in-degree = 0.
	 * 
	 * Rf :
	 * https://docs.oracle.com/javase/8/docs/api/java/awt/Point.html
	 * https://leetcode.com/problems/alien-dictionary/discuss/70169/My-Concise-JAVA-solution-based-on-Topological-Sorting/499806
	 */
	public String alienOrder_bfs_point(String[] words) {
		// Adjacency list: pair = (node, node's predecessor)
		List<Point> pairs = new LinkedList<Point>();

		// All distinct characters
		Set<Character> chs = new HashSet<Character>();

		// 1. Convert characters to a graph: Adjacency lists
		for (int i = 0; i < words.length; i++) {
			String word = words[i];

			// Only set one pair where the characters at the same position differs
			// in two neighbor rows. e.g. "wrtk" < "wrfp"=> 't' < 'f'
			boolean alreadySet = false;

			int j;
			for (j = 0; j < words[i].length(); j++) {
				// Set dependency of two characters by comparing two neighbor rows.
				if (!alreadySet && i > 0 && j < words[i - 1].length() 
						&& words[i].charAt(j) != words[i - 1].charAt(j)) {
					
					pairs.add(new Point(words[i].charAt(j), words[i - 1].charAt(j)));
					alreadySet = true;
				}

				// Add distinct character to chs set
				chs.add(word.charAt(j));
			}

			// handle the second string is a prefix of the first string
			if (!alreadySet && i > 0 && j < words[i - 1].length())
				return "";
		}

		// 2. Topological sorting: keep adding elements whose in-degree is 0
		String res = "";
		int indegree[] = new int[256];
		Arrays.fill(indegree, Integer.MIN_VALUE);

		// Initialize in-degree of the distinct characters in the words list
		for (Character ch : chs)
			indegree[ch] = 0;

		// Increase in-degree according to the dependency of pairs list
		for (int i = 0; i < pairs.size(); i++)
			indegree[pairs.get(i).x]++;

		Queue<Character> queue = new LinkedList<Character>();
		for (int i = 0; i < 256; i++) {
			// Add the character whose in-degree = 0, which means it doesn't 
			// have any predecessor
			if (indegree[i] == 0) {
				res += (char) i;
				queue.offer((char) i);
			}
		}

		while (!queue.isEmpty()) {
			// Dequeue the character whose in-degree = 0 from queue
			Character predecessor = queue.poll();

			for (int i = 0; i < pairs.size(); i++) {
				// Update in-degree: decrease 1 to the successors of the character
				// whose in-degree = 0
				if (pairs.get(i).y == predecessor) {
					indegree[pairs.get(i).x]--;

					// If in-degree = 0, add the character to the queue, and append 
					// it to the result string
					if (indegree[pairs.get(i).x] == 0) {
						res += (char) pairs.get(i).x;
						queue.offer((char) pairs.get(i).x);
					}
				}
			}
		}

		// NOTE: res.length should equal the size of distinct characters, 
		// otherwise a cycle must exist
		return res.length() == chs.size() ? res : "";
	}

	/**
     * Python collections
     * 
     * https://leetcode.com/problems/alien-dictionary/discuss/70173/Python-Solution-with-Detailed-Explanation
     * https://leetcode.com/problems/alien-dictionary/discuss/156130/Python-Solution-with-Detailed-Explanation-(91)
     * https://leetcode.com/problems/alien-dictionary/discuss/170878/Simple-Python-solution-using-Kahn's-topological-sort-BFS-algo
     * https://leetcode.com/problems/alien-dictionary/discuss/70281/Python-topological-sort-wo-BFSGFS
     * https://leetcode.com/problems/alien-dictionary/discuss/70137/1618-lines-Python-30-lines-C%2B%2B
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/alien-dictionary/discuss/157298/C%2B%2B-BFS-and-Topoligical-Sort-with-explanation
     * https://leetcode.com/problems/alien-dictionary/discuss/70157/Straightforward-C%2B%2B-solution
     */	

}
