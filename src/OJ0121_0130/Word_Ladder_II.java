package OJ0121_0130;

import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Queue;
import java.util.ArrayDeque;
import java.util.Stack;
import java.util.Iterator;

public class Word_Ladder_II {
	/*
	 * The following 3 functions are from this link.
	 * https://leetcode.com/problems/word-ladder-ii/discuss/40475/My-concise-JAVA-solution-based-on-BFS-and-DFS/182616
	 * 
	 * two-end BFS + DFS
	 * 
	 * We still need to rule out all node that we have searched previously. Meanwhile 
	 * if two nodes have a same child node, we need to add that child node to both 
	 * node's children list as we need to backtrack all valid paths.
	 * 
	 * Unlike a regular BFS, we can't use a "seen" set but a more like "explored" set. 
	 * Otherwise, e.g. tree: {x->z, y->z}, z won't be added to y's children list 
	 * if x is visited first and z is already seen in x's search.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/word-ladder-ii/discuss/269012/Python-BFS%2BBacktrack-Greatly-Improved-by-bi-directional-BFS
	 * 
	 * Other code:
	 * https://leetcode.com/problems/word-ladder-ii/discuss/40475/My-concise-JAVA-solution-based-on-BFS-and-DFS/222218
	 */
	public List<List<String>> findLadders_2end_BFS(String beginWord, String endWord, 
			List<String> wordList) {
		
		Set<String> dict = new HashSet<>(wordList);
		List<List<String>> res = new ArrayList<>();
		if (!dict.contains(endWord))
			return res;

		// initialization for two-end BFS
		Set<String> start = new HashSet<>();
		Set<String> end = new HashSet<>();
		start.add(beginWord);
		end.add(endWord);
		
		// map the list of neighbors to each node
		Map<String, List<String>> map = new HashMap<>();

		bfs_2end_BFS(start, end, map, dict);
		dfs_2end_BFS(res, map, new ArrayList<>(Arrays.asList(beginWord)), endWord);

		return res;
    }
    
    // use bfs to build the neighbor graph
	public void bfs_2end_BFS(Set<String> start, Set<String> end, 
			Map<String, List<String>> map, Set<String> dict) {

		boolean found = false;
		boolean reverse = false;

		while (!start.isEmpty()) {
			dict.removeAll(start);
			dict.removeAll(end);      // 可以移除，可能會變慢
			
			if (start.size() > end.size()) {
				reverse = !reverse;
				
				Set<String> temp = start;
				start = end;
				end = temp;
			}

			Set<String> next = new HashSet<>();
			for (String word : start) {
				char[] ch = word.toCharArray();
				
				for (int i = 0; i < ch.length; i++) {
					char old = ch[i];
					for (char c = 'a'; c <= 'z'; c++) {
						if (old == c)
							continue;
						
						ch[i] = c;
						String newStr = String.valueOf(ch);

						// if the new string is contained in end set or dict set,
						// add it to neighbor map 
						// the graph should direct from startWord to endWord, 
						// therefore, if the start set has been exchanged with end 
						// set, we should reverse the order in which the
						// word and newStr are put into the map
						if (end.contains(newStr) || dict.contains(newStr)) {
							String key = reverse ? newStr : word;
							String val = reverse ? word : newStr;
							
							map.putIfAbsent(key, new ArrayList<>());
							map.get(key).add(val);
							
							// mark the shortest ladder has been found
							// notice: CANNOT break here, because we need to build 
							// the graph for all shortest ladders.
							if (end.contains(newStr))
								found = true;
							else
								next.add(newStr);
						}
					}
					ch[i] = old;
				}
			}
			
			start = next;
			
			// after find all shortest ladders, break the loop
			if (found)
				break;
		}
	}

	// use dfs to find all the shortest paths
	public void dfs_2end_BFS(List<List<String>> res, Map<String, List<String>> map, 
			List<String> subList, String endWord) {
		
		String lastWord = subList.get(subList.size() - 1);
		
		if (lastWord.equals(endWord)) {
			res.add(new ArrayList<>(subList));
			return;
		}

		// if not contained, that means this path is not shortest
		if (!map.containsKey(lastWord))
			return;
		
		for (String s : map.get(lastWord)) {
			subList.add(s);
			dfs_2end_BFS(res, map, subList, endWord);
			subList.remove(subList.size() - 1);
		}
	}
	
	/*
	 * Bidirectional BFS
	 * 
	 * 我們需要知道這個 word 是否在另一端的末端
	 * 相同的末端 word 有可能對應多條 path
	 * 所以使用 "末端 word" 作為 HashMap 的 key，"其所對應的所有 path" 作為 value
	 * 
	 * test case:
	 * 
	 * "a"
	 * "c"
	 * ["a","b","c"]
	 */
	public List<List<String>> findLadders_2end_BFS_self(String start, String end, 
			List<String> wordList) {
		
		Set<String> dict = new HashSet<>(wordList);
		if (!dict.contains(end)) {
			return new ArrayList<>();
		}
		
		// Bidirectional BFS，從 start 開始、從 end 開始
		// 末端 word：所對應的每條 path
		Map<String, List<List<String>>> startMap = new HashMap<>();
		Map<String, List<List<String>>> endMap = new HashMap<>();

        List<List<String>> list1 = new ArrayList<>();
        list1.add(new ArrayList<>(Arrays.asList(start)));
        
        List<List<String>> list2 = new ArrayList<>();
        list2.add(new ArrayList<>(Arrays.asList(end)));
        
        // key 為末端 word，value 為所對應的所有 path
        // 將初始的 start 和 end path 放進去
		startMap.put(start, list1);
		endMap.put(end, list2);
		
		// 將 start 和 end 從 dict 中移除
		// 這部分可以移除，可能會變慢
		dict.remove(start);
		dict.remove(end);

		List<List<String>> res = new ArrayList<List<String>>();
        
		// false：現在處理的是 start。 true：現在處理的是 end
		boolean flip = false;
		
		// 若找到則為 true
        boolean done = false;
        
        while (!startMap.isEmpty() && !endMap.isEmpty()) {
            if (startMap.size() > endMap.size()) {
                Map<String, List<List<String>>> temp = endMap;
                endMap = startMap;
                startMap = temp;
                
                flip = !flip;
            }
            
            Map<String, List<List<String>>> midMap = new HashMap<>();
            Set<String> set = new HashSet<>();
            for (Map.Entry<String, List<List<String>>> entry : startMap.entrySet()) {
                String word = entry.getKey();
                for (List<String> list : entry.getValue()) {
                    char[] chs = word.toCharArray();
                    
                    for (int i = 0; i < chs.length; i++) {
                        char old = chs[i];
                        
                        for (char c = 'a'; c <= 'z'; c++) {
                            if (old == c) {
                                continue;
                            }
                            
                            chs[i] = c;
                            String target = String.valueOf(chs);
                            
                            if (endMap.containsKey(target)) {
                                if (!flip) {
                                    for (List<String> way : endMap.get(target)) {
                                        List<String> path = new ArrayList<>(list);
                                        
                                        for (int j = way.size() - 1; j >= 0; j--) {
                                            path.add(way.get(j));
                                        }
                                        
                                        res.add(path);
                                    }
                                }
                                else {
                                    for (List<String> way : endMap.get(target)) {                                        
                                        List<String> path = new ArrayList<>(way);
                                        
                                        for (int j = list.size() - 1; j >= 0; j--) {
                                            path.add(list.get(j));
                                        }
                                        
                                        res.add(path);
                                    }
                                }
                                
                                done = true;
                            }
                            
                            if (!done && dict.contains(target)) {
                                List<String> way = new ArrayList<>(list);
                                way.add(target);
                                
                                midMap.putIfAbsent(target, new ArrayList<>());
                                midMap.get(target).add(way);
                                
                                // 處理完這一層再將 target 刪除
                                // 若立即刪除，則會造成其他可以走到 target 的路徑沒被記錄到
                                set.add(target);
                            }
                        }
                        
                        chs[i] = old;
                    }
                }
            }
            
            if (done) {
                break;
            }
            
            // 進到下一層
            startMap = midMap;
            
            // 刪除這一層走過的 word
            dict.removeAll(set);
        }

		return res;
	}
	
	/*
	 * The following 3 functions are from this link.
	 * https://leetcode.com/problems/word-ladder-ii/discuss/40477/Super-fast-Java-solution-(two-end-BFS)
	 * 
	 * We still need to rule out all node that we have searched previously. Meanwhile 
	 * if two nodes have a same child node, we need to add that child node to both 
	 * node's children list as we need to backtrack all valid paths.
	 * 
	 * Unlike a regular BFS, we can't use a "seen" set but a more like "explored" set. 
	 * Otherwise, e.g. tree: {x->z, y->z}, z won't be added to y's children list 
	 * if x is visited first and z is already seen in x's search.
	 * 
	 * Couple of things that make this solution fast:
	 * 
	 * 1. We use Bidirectional BFS which always expand from direction with less nodes
	 * 2. We use char[] to build string so it would be fast
	 * 3. Instead of scanning dict each time, we build new string from existing 
	 *    string and check if it is in dict
	 * 
	 * Rf :
	 * https://leetcode.com/problems/word-ladder-ii/discuss/269012/Python-BFS%2BBacktrack-Greatly-Improved-by-bi-directional-BFS
	 * https://leetcode.com/problems/word-ladder-ii/discuss/40572/My-30ms-bidirectional-BFS-and-DFS-based-Java-solution
	 * https://leetcode.com/problems/word-ladder-ii/discuss/40477/Super-fast-Java-solution-(two-end-BFS)/197832
	 */
	public List<List<String>> findLadders_Bi_BFS(String start, String end, 
			List<String> wordList) {
		
		// We use bi-directional BFS to find shortest path
		
		Set<String> dict = new HashSet<>(wordList);
		if (!dict.contains(end)) {
			return new ArrayList<>();
		}
		
		// hash set for both ends
		Set<String> forward = new HashSet<String>();
		Set<String> backward = new HashSet<String>();

		// initial words in both ends
		forward.add(start);
		backward.add(end);

		// we use a map to help construct the final result
		Map<String, List<String>> map = new HashMap<String, List<String>>();

		// build the map
		bfs_Bi_BFS(dict, forward, backward, map, false);

		List<List<String>> res = new ArrayList<List<String>>();
		List<String> sol = new ArrayList<String>(Arrays.asList(start));

		// recursively build the final result
		generateList_Bi_BFS(start, end, map, sol, res);

		return res;
	}

	void bfs_Bi_BFS(Set<String> dict, Set<String> forward, Set<String> backward, 
			Map<String, List<String>> map, boolean flip) {
		
		if (forward.isEmpty()) {
			return;
		}

		// We always do BFS on direction with less nodes
	    // Here we assume forward set has less nodes, if not, we swap them
		if (forward.size() > backward.size()) {
			bfs_Bi_BFS(dict, backward, forward, map, !flip);
			return;
		}

		// remove words on current both ends from the dict to avoid duplicate addition
		// 可以只 dict.removeAll(forward);
		dict.removeAll(forward);
		dict.removeAll(backward);   // 可以移除

		// as we only need the shortest paths
		// we use a boolean value help early termination
		boolean done = false;

		// set for the next level
		Set<String> next = new HashSet<String>();

		// do BFS on every node of forward direction
		for (String str : forward) {
			for (int i = 0; i < str.length(); i++) {
				char[] chars = str.toCharArray();

				// change one character with every chars from a to z 
				for (char ch = 'a'; ch <= 'z'; ch++) {
					chars[i] = ch;

					String word = new String(chars);

					// make sure we construct the tree in the correct direction
					String key = flip ? word : str;
					String val = flip ? str : word;

					if (!map.containsKey(key)) {
						map.put(key, new ArrayList<String>());
					}
					List<String> list = map.get(key);

					// word is in backward set, then it will connect two parts
					if (backward.contains(word)) {
						list.add(val);

						done = true;
					}

					// word is in dict, we can add it to set
					// as new nodes in next layer
					if (!done && dict.contains(word)) {
						list.add(val);

						next.add(word);
					}
				}
			}
		}

		// early terminate if done is true
		if (!done) {			
			bfs_Bi_BFS(dict, backward, next, map, !flip);
		}
	}

	void generateList_Bi_BFS(String start, String end, Map<String, List<String>> map, 
			List<String> sol, List<List<String>> res) {
		
		if (start.equals(end)) {
			res.add(new ArrayList<String>(sol));
			return;
		}

		// need this check in case the diff between start and end happens to be one
		// e.g "a", "c", {"a", "b", "c"}
		if (!map.containsKey(start)) {
			return;
		}

		for (String word : map.get(start)) {
			sol.add(word);
			generateList_Bi_BFS(word, end, map, sol, res);
			sol.remove(sol.size() - 1);
		}
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/word-ladder-ii/discuss/40447/Share-two-similar-Java-solution-that-Accpted-by-OJ./238867
	 * 
	 * Constructing an adjacent graph in the BFS process, each pointing from achieved 
	 * word to original word. After built, backtrace from endWord to startWord.
	 * 
	 * 1. Use a SET to store the words visited in current ladder, when the current 
	 *    ladder was completed, delete the visited words from unvisited.
	 * 2. Use Character iteration to find all possible paths.
	 * 3. One word is allowed to be inserted into the queue only ONCE.
	 * 
	 * It is a Directed Graph, all the path starts from the 'end' and ends at 'start'. 
	 * All of them are the shortest, otherwise it will not be added to the graph.
	 * 
	 * Removing `found` won't impact the correctness, as we are doing DFS for the 
	 * second round. Even with more nodes in the distance map, we will still output 
	 * the solution once we meet endWord.
	 * 
	 * 雙向 BFS + DFS 需要 `found` 來提早結束
	 * 
	 * Rf :
	 * https://leetcode.com/problems/word-ladder-ii/discuss/40447/Share-two-similar-Java-solution-that-Accpted-by-OJ.
	 * https://leetcode.com/problems/word-ladder-ii/discuss/40447/Share-two-similar-Java-solution-that-Accpted-by-OJ./38202
	 * https://leetcode.com/problems/word-ladder-ii/discuss/40475/My-concise-JAVA-solution-based-on-BFS-and-DFS/38231
	 * https://leetcode.com/problems/word-ladder-ii/discuss/40475/My-concise-JAVA-solution-based-on-BFS-and-DFS/312916
	 * 
	 * Other code:
	 * https://leetcode.com/problems/word-ladder-ii/discuss/40475/My-concise-JAVA-solution-based-on-BFS-and-DFS/367320
	 */
    public List<List<String>> findLadders_BFS_DFS2(String beginWord, String endWord, 
    		List<String> wordList) {
    	
    	List<List<String>> res = new ArrayList<>();
    	Map<String, List<String>> map = new HashMap<>();
    	
		if (wordList.size() == 0)
			return res;

		Queue<String> q = new ArrayDeque<>();
		q.add(beginWord);
		
		Set<String> visited = new HashSet<>();
		Set<String> unvisited = new HashSet<>(wordList);
		unvisited.remove(beginWord);
		
		boolean found = false;

		// bfs
		while (!q.isEmpty()) {
			int size = q.size();
			
			// for each string in the queue
			for (int k = size - 1; k >= 0; k--) {
				String word = q.poll();
				
				for (int i = 0; i < word.length(); i++) {
					char chs[] = word.toCharArray();
					
					for (char c = 'a'; c <= 'z'; c++) {
						chs[i] = c;
						String newStr = new String(chs);
						
						if (unvisited.contains(newStr)) {
							
							// Avoid Duplicate queue insertion
							if (!visited.contains(newStr)) {
								visited.add(newStr);
								q.add(newStr);
							}
							
							// build adjacent graph
							if (map.containsKey(newStr))
								map.get(newStr).add(word);
							else {
								List<String> l = new ArrayList<>();
								l.add(word);
								
								map.put(newStr, l);
							}
							
							if (newStr.equals(endWord))
								found = true;
						}
					}
				}
			}
			
			if (found)
				break;
			
			unvisited.removeAll(visited);
			visited.clear();
		}

		// back trace based on the adjacent graph that we have built
		List<String> list = new LinkedList<>();
		backTrace_BFS_DFS2(endWord, beginWord, map, list, res);
		
		return res;
	}

	private void backTrace_BFS_DFS2(String cur, String start, 
			Map<String, List<String>> map, List<String> list, 
			List<List<String>> res) {
		
		if (cur.equals(start)) {
			List<String> path = new ArrayList<String>(list);
			path.add(0, start);
			
			res.add(path);
			return;
		}
		
		list.add(0, cur);
		
		if (map.get(cur) != null) {
			
			// for each neighbors
			for (String s : map.get(cur)) {
				backTrace_BFS_DFS2(s, start, map, list, res);
			}
		}
		
		list.remove(0);
	}
	
	/*
	 * https://leetcode.com/problems/word-ladder-ii/discuss/40452/C++-very-easy-read-and-understand-solution-compared-to-most-voted!/38206
	 * 
	 * We are essentially building a graph, from start. 
	 * At each level, we find all reachable words from parent.
	 * We stop if the current level contains end, and we return any path whose last 
	 * node is end.
	 *  
	 * A key improvement is to remove all the words we already reached in 
	 * PREVIOUS LEVEL; we don't need to try visit them again in subsequent level, 
	 * that is guaranteed to be non-optimal solution.
	 * At each new level, we removeAll() words reached in previous level from dict.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/word-ladder-ii/discuss/40487/Java-Solution-with-Iteration
	 * https://leetcode.com/problems/word-ladder-ii/discuss/40640/Run-Time-Analysis-For-Word-Ladder-II-(Java)
	 * 
	 * Other code:
	 * https://leetcode.com/problems/word-ladder-ii/discuss/40477/Super-fast-Java-solution-(two-end-BFS)/236362
	 * https://leetcode.com/problems/word-ladder-ii/discuss/40487/Java-Solution-with-Iteration/38243
	 */
	public List<List<String>> findLadders_BFS(String beginWord, String endWord, 
			List<String> wordList) {
		
		List<List<String>> res = new ArrayList<>();
		
		// instead of storing words we are at, we store the paths.
		Queue<List<String>> queue = new LinkedList<>();
		queue.offer(new ArrayList<>(Arrays.asList(beginWord)));
		
		Set<String> visited = new HashSet<>();
		Set<String> word_list = new HashSet<>(wordList);
		
		boolean found = false;
		if (!word_list.contains(endWord))
			return res;
		
		while (!queue.isEmpty() && !word_list.isEmpty()) {
			for (int i = queue.size(); i > 0; --i) {
				
				// try to find next word to reach, continuing from the path
				List<String> path = queue.poll();
				String back_word = path.get(path.size() - 1);
				
				char[] chs = back_word.toCharArray();
				for (int j = 0; j < back_word.length(); j++) {
					char origin = chs[j];
					
					for (char c = 'a'; c <= 'z'; c++) {
						if (chs[j] == c)
							continue;
						
						chs[j] = c;
						String next_word = String.valueOf(chs);
						
						if (next_word.equals(endWord)) {
							path.add(next_word);
							res.add(path);
							
							// current level is the last level
							found = true;
							continue;
						}
						
						if (word_list.contains(next_word)) {
							visited.add(next_word);
							List<String> new_path = new ArrayList<>(path);
							new_path.add(next_word);
							
							queue.offer(new_path);
						}
					}
					
					chs[j] = origin;
				}
			}
			
			if (found)
				break;
			
			word_list.removeAll(visited);
			visited.clear();
		}
		return res;
	}
	
	/*
	 * The following 3 functions are from this link.
	 * https://leetcode.com/problems/word-ladder-ii/discuss/40640/Run-Time-Analysis-For-Word-Ladder-II-(Java)
	 * 
	 * Use a set to collect words for current layer and then delete the set from dict 
	 * after finishing current layer.
	 * 
	 * 1. minimize the size of the searching graph ( # of nodes and # of edges)
	 * 2. avoid add edges connecting to strings from the previous level 
	 *    (e.g avoid any back-track path)
	 * 
	 * BFS to construct a child->parents map, DFS on the map
	 * 
	 * Since you always need to get a word's parent/children list/set, using a map 
	 * can achieve O(1) run time, map beats other data structure in storing the 
	 * relationship.
	 * 
	 * Storing all the children for a word will always get MLE because a parent can 
	 * have length*26 children while a child can only have at most 2 or 3 parents. 
	 * Parent select children first!
	 * 
	 * Removing `found` won't impact the correctness, as we are doing DFS for the 
	 * second round. Even with more nodes in the distance map, we will still output 
	 * the solution once we meet endWord.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/word-ladder-ii/discuss/40475/My-concise-JAVA-solution-based-on-BFS-and-DFS/38231
	 * https://leetcode.com/problems/word-ladder-ii/discuss/40475/My-concise-JAVA-solution-based-on-BFS-and-DFS/177427
	 */
	public List<List<String>> findLadders_BFS_DFS(String start, String end, 
			List<String> wordList) {
		
		List<List<String>> rslt = new ArrayList<List<String>>();
		Map<String, List<String>> parents = new HashMap<String, List<String>>();
		boolean found = false;

		// initialize
		Set<String> cur_layer = new HashSet<String>();
		cur_layer.add(start);

		Set<String> dict = new HashSet<>(wordList);
		if (dict.contains(start))
			dict.remove(start);

		// BFS construct map
		while (!found && !cur_layer.isEmpty()) {
			Set<String> new_layer = new HashSet<String>();
			Iterator<String> iter = cur_layer.iterator();
			
			while (iter.hasNext()) {
				String s = iter.next();
				
				for (String t : neighbors_BFS_DFS(s, dict)) {
					new_layer.add(t);
					
					if (!parents.containsKey(t)) {
						List<String> list = new ArrayList<String>();
						list.add(s);
						parents.put(t, list);
					} 
					else {
						List<String> list = parents.get(t);
						list.add(s);
					}
					
					if (t.equals(end))
						found = true;
				}
			}
			
			dict.removeAll(new_layer);
			cur_layer = new_layer;
		}

		// DFS construct paths
		Stack<String> path = new Stack<String>();
		path.push(end);
		dfs_BFS_DFS(start, end, path, parents, rslt);

		return rslt;
	}

	private void dfs_BFS_DFS(String start, String s, Stack<String> path, 
			Map<String, List<String>> parents, List<List<String>> rslt) {
		
		// base case
		if (s.equals(start)) {
			List<String> list = new ArrayList<String>();
			list.addAll(path);
			Collections.reverse(list);
			
			rslt.add(list);
			return;
		}
		
		// edge case
		if (!parents.containsKey(s))
			return;
		
		// recursion
		for (String t : parents.get(s)) {
			path.push(t);
			dfs_BFS_DFS(start, t, path, parents, rslt);
			path.pop();
		}
	}

	private List<String> neighbors_BFS_DFS(String s, Set<String> dict) {
		List<String> list = new ArrayList<String>();
		char[] chars = s.toCharArray();
		for (int j = 0; j < s.length(); j++) {
			char original = chars[j];
			
			for (char c = 'a'; c <= 'z'; c++) {
				chars[j] = c;
				String t = new String(chars);
				
				if (!t.equals(s) && dict.contains(t))
					list.add(t);
			}
			
			chars[j] = original;
		}
		return list;
	}

	/*
	 * The following 2 variables and 2 functions are from this link.
	 * https://leetcode.com/problems/word-ladder-ii/discuss/40447/Share-two-similar-Java-solution-that-Accpted-by-OJ.
	 * 
	 * The solution contains two steps 
	 * 1. Use BFS to construct a graph. 
	 * 2. Use DFS to construct the paths from end to start.
	 * 
	 * The first step BFS is quite important. I summarized three tricks
	 * 1. Using a Map to store the min ladder of each word, or use a SET to store the 
	 *    words visited in current ladder, when the current ladder was completed, 
	 *    delete the visited words from unvisited.
	 * 2. Use Character iteration to find all possible paths. Do not compare one word 
	 *    to all the other words and check if they only differ by one character.
	 * 3. One word is allowed to be inserted into the queue only ONCE.
	 * 
	 * It is a Directed Graph, all the path starts from the 'end' and ends at 'start'. 
	 * All of them are the shortest, otherwise it will not be added to the graph.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/word-ladder-ii/discuss/40447/Share-two-similar-Java-solution-that-Accpted-by-OJ./38184
	 * https://leetcode.com/problems/word-ladder-ii/discuss/40447/Share-two-similar-Java-solution-that-Accpted-by-OJ./38202
	 * 
	 * Other code:
	 * https://leetcode.com/problems/word-ladder-ii/discuss/40475/My-concise-JAVA-solution-based-on-BFS-and-DFS
	 * https://leetcode.com/problems/word-ladder-ii/discuss/40475/My-concise-JAVA-solution-based-on-BFS-and-DFS/38228
	 */
	Map<String, List<String>> map_BFS_DFS3;
	List<List<String>> results_BFS_DFS3;

	public List<List<String>> findLadders_BFS_DFS3(String start, String end, 
			List<String> dict) {
		
		results_BFS_DFS3 = new ArrayList<List<String>>();
		if (dict.size() == 0)
			return results_BFS_DFS3;

		int min = Integer.MAX_VALUE;

		Queue<String> queue = new ArrayDeque<String>();
		queue.add(start);

		// Neighbors for every node
		map_BFS_DFS3 = new HashMap<String, List<String>>();

		// Distance of every node from the start node
		Map<String, Integer> ladder = new HashMap<String, Integer>();
		ladder.put(start, 0);
		for (String string : dict)
			ladder.put(string, Integer.MAX_VALUE);

		// BFS: Dijisktra search
		while (!queue.isEmpty()) {
			String word = queue.poll();

			// 'step' indicates how many steps are needed to travel to one word.
			int step = ladder.get(word) + 1;

			if (step > min)
				break;

			for (int i = 0; i < word.length(); i++) {
				StringBuilder builder = new StringBuilder(word);
				
				for (char ch = 'a'; ch <= 'z'; ch++) {
					builder.setCharAt(i, ch);
					String new_word = builder.toString();
					
					if (ladder.containsKey(new_word)) {

						// Check if it is the shortest path to one word.
						
						// If one word already appeared in one ladder,
						// Do not insert the same word inside the queue twice.
						if (step > ladder.get(new_word))
							continue;
						else if (step < ladder.get(new_word)) {
							queue.add(new_word);
							ladder.put(new_word, step);
						}
						
						// Build adjacent Graph
						if (map_BFS_DFS3.containsKey(new_word))
							map_BFS_DFS3.get(new_word).add(word);
						else {
							List<String> list = new LinkedList<String>();
							list.add(word);
							map_BFS_DFS3.put(new_word, list);
						}

						if (new_word.equals(end))
							min = step;
					}
				}
			}
		}

		// BackTracking
		LinkedList<String> result = new LinkedList<String>();
		backTrace_BFS_DFS3(end, start, result);

		return results_BFS_DFS3;
	}

	private void backTrace_BFS_DFS3(String word, String start, List<String> list) {
		if (word.equals(start)) {
			List<String> tmp = new ArrayList<String>(list); 
			tmp.add(0, start);
			
			results_BFS_DFS3.add(tmp);
			return;
		}
		
		list.add(0, word);
		
		if (map_BFS_DFS3.get(word) != null)
			for (String s : map_BFS_DFS3.get(word))
				backTrace_BFS_DFS3(s, start, list);
		
		list.remove(0);
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/word-ladder-ii/discuss/269012/Python-BFS%2BBacktrack-Greatly-Improved-by-bi-directional-BFS
     * https://leetcode.com/problems/word-ladder-ii/discuss/490116/Three-Python-solutions%3A-Only-BFS-BFS%2BDFS-biBFS%2B-DFS
     * https://leetcode.com/problems/word-ladder-ii/discuss/40549/FAST-AND-CLEAN-PythonC%2B%2B-Solution-using-Double-BFS-beats-98
     * https://leetcode.com/problems/word-ladder-ii/discuss/40595/Share-my-130-ms-Python-solution
     * https://leetcode.com/problems/word-ladder-ii/discuss/241584/Python-solution
     * https://leetcode.com/problems/word-ladder-ii/discuss/40482/Python-simple-BFS-layer-by-layer
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/word-ladder-ii/discuss/40540/88ms!-Accepted-c%2B%2B-solution-with-two-end-BFS.-68ms-for-Word-Ladder-and-88ms-for-Word-Ladder-II
     * https://leetcode.com/problems/word-ladder-ii/discuss/40591/The-fastest-C%2B%2B-Solution-56ms!!
     * https://leetcode.com/problems/word-ladder-ii/discuss/40452/C%2B%2B-very-easy-read-and-understand-solution-compared-to-most-voted!
     * https://leetcode.com/problems/word-ladder-ii/discuss/241927/C%2B%2B-BFS-%2B-DFS
     * https://leetcode.com/problems/word-ladder-ii/discuss/683008/C%2B%2B-BFS-with-detailed-explanation-and-illustration
     */
	
	/**
	 * JavaScript collections
	 * 
	 * https://leetcode.com/problems/word-ladder-ii/discuss/143559/the-simplest-javascript-bfs-solution
	 */
	
	/**
	 * C# collections
	 * 
	 * https://leetcode.com/problems/word-ladder-ii/discuss/379124/C-creative-idea-to-create-a-graph-and-then-construct-shortest-path-map-practice-in-2019
	 */

}
