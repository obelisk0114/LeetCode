package OJ0121_0130;

import java.util.List;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.HashSet;

public class Word_Ladder {
	/*
	 * https://leetcode.com/problems/word-ladder/discuss/40711/Two-end-BFS-in-Java-31ms.
	 * 
	 * traverse the path simultaneously from start node and end node, 
	 * and merge in the middle
	 * 
	 * Rf : https://leetcode.com/problems/word-ladder/discuss/40708/Share-my-two-end-BFS-in-C++-80ms.
	 */
	public int ladderLength(String beginWord, String endWord, List<String> wordList) {
		Set<String> dict = new HashSet<>(wordList);
        if (!dict.contains(endWord)) {
            return 0;
        }
        
        Set<String> beginSet = new HashSet<>();
        Set<String> endSet = new HashSet<>();
        beginSet.add(beginWord);
        endSet.add(endWord);

        int step = 1;
        while (!beginSet.isEmpty() && !endSet.isEmpty()) {
        	// Choose the smaller one
            if (beginSet.size() > endSet.size()) {
                Set<String> set = beginSet;
                beginSet = endSet;
                endSet = set;
            }
            
            Set<String> temp = new HashSet<>();        // intermediate Set
            for (String word : beginSet) {
                char[] chs = word.toCharArray();
                for (int i = 0; i < chs.length; i++) {
                	char old = chs[i];
                    for (char c = 'a'; c <= 'z'; c++) {
                        if (chs[i] == c) {                        	
                        	continue;
                        }
                        
                        chs[i] = c;
                        String target = String.valueOf(chs);
                        
                        if (endSet.contains(target)) {
                            return step + 1;
                        }
                        if (dict.contains(target)) {
                            temp.add(target);
                            dict.remove(target);
                        }                        
                    }
                    
                    chs[i] = old;
                }
            }
            
            beginSet = temp;
            step++;
        }
        return 0;
    }
	
	/*
	 * https://leetcode.com/problems/word-ladder/discuss/40719/Updated-solution-based-on-new-requirement
	 * 
	 * The first intuition for this problem is to build a graph whose nodes represent 
	 * strings and edges connect strings that are only 1 character apart, and then we 
	 * apply BFS from the startWord node. If we find the endWord, we return the level 
	 * count of the bfs.
	 * 
	 * 1. we make changes to current string to obtain all the strings we can reach from 
	 *    current node, and see if it is in the wordList.
	 * 2. For the strings we visited, we remove it from the wordList.
	 * 3. We don't even need to build the adjacency list graph explicitly using a 
	 *    HashMap<String, ArrayList>, since we keep all the nodes we can reach in the 
	 *    queue of each level of BFS.
	 *    
	 * Rf : 
	 * https://leetcode.com/problems/word-ladder/discuss/40728/Simple-Java-BFS-solution-with-explanation
	 * https://leetcode.com/problems/word-ladder/discuss/40707/Easy-76ms-C++-Solution-using-BFS
	 * 
	 * Other code:
	 * https://leetcode.com/problems/word-ladder/discuss/40717/Another-accepted-Java-solution-(BFS)/7
	 */
	public int ladderLength2(String beginWord, String endWord, List<String> wordList) {
		Set<String> set = new HashSet<>(wordList);
		if (!set.contains(endWord))
			return 0;
		
		Queue<String> queue = new LinkedList<>();
		queue.offer(beginWord);
		int step = 1;
		
		while (!queue.isEmpty()) {
			int size = queue.size();
			for (int i = 0; i < size; i++) {
				String s = queue.poll();
				char[] arr = s.toCharArray();
				for (int j = 0; j < arr.length; j++) {
					char original = arr[j];
					for (char c = 'a'; c <= 'z'; c++) {
						if (c == arr[j])
							continue;
						
						arr[j] = c;
						String test = String.valueOf(arr); // test = new String(arr);
						
						if (test.equals(endWord))
							return step + 1;
						if (set.contains(test)) {
							queue.offer(test);
							set.remove(test);
						}
					}
					arr[j] = original;
				}
			}
			step++;
		}
		return 0;
	}
	
	// https://leetcode.com/problems/word-ladder/discuss/40704/Java-Solution-using-BFS-with-explanation/2
	public int ladderLength_visited_Set_no_remove(String beginWord, String endWord, List<String> wordList) {
		Queue<String> q = new LinkedList<>();
		q.add(beginWord);
		q.add(null);

		HashSet<String> dict = new HashSet<>();
		for (String word : wordList) {
			dict.add(word);
		}

		HashSet<String> v = new HashSet<>();
		v.add(beginWord);

		if (!dict.contains(endWord))
			return 0;

		int level = 1;
		while (!q.isEmpty()) {
			String s = q.poll();
			if (s != null) {
				for (int i = 0; i < s.length(); i++) {
					char[] chars = s.toCharArray();
					for (char c = 'a'; c <= 'z'; c++) {
						chars[i] = c;
						String word = new String(chars);
						
						if (word.equals(endWord))
							return level + 1;
						if (dict.contains(word) && !v.contains(word)) {
							q.add(word);
							v.add(word);
						}
					}
				}
			}
			else {
				level++;
				if (!q.isEmpty()) {
					q.add(null);
				}
			}
		}
		return 0;
	}
	
	/*
	 * https://leetcode.com/problems/word-ladder/discuss/40704/Java-Solution-using-BFS-with-explanation/3
	 * 
	 * In the while loop, for each word in the reached set, I give all variations and 
	 * check if it matches anything from wordDict, if it has a match, I add that word 
	 * into toAdd set, which will be my "reached" set in the next loop, and remove 
	 * the word from wordDict because I already reached it in this step.
	 * 
	 * At the end of while loop, I check the size of toAdd, which means that if I 
	 * can't reach any new String from wordDict, I won't be able to reach the endWord, 
	 * then just return 0.
	 */
	public int ladderLength_2_Set(String beginWord, String endWord, List<String> wordList) {
		Set<String> reached = new HashSet<String>();
		Set<String> wordDict = new HashSet<String>(wordList);

		if (!wordDict.contains(endWord)) {
			return 0;
		}
		reached.add(beginWord);
		// wordDict.add(endWord);

		int distance = 1;
		while (!reached.contains(endWord)) {
			Set<String> toAdd = new HashSet<String>();
			for (String each : reached) {
				for (int i = 0; i < each.length(); i++) {
					char[] chars = each.toCharArray();
					for (char ch = 'a'; ch <= 'z'; ch++) {
						chars[i] = ch;
						String word = new String(chars);
						
						if (wordDict.contains(word)) {
							toAdd.add(word);
							wordDict.remove(word);
						}
					}
				}
			}
			distance++;
			
			if (toAdd.size() == 0)
				return 0;
			reached = toAdd;
		}
		return distance;
	}

}
