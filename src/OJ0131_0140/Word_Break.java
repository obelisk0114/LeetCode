package OJ0131_0140;

import java.util.List;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.HashSet;

public class Word_Break {
	/*
	 * by myself
	 * 
	 * Rf :
	 * https://leetcode.com/problems/word-break/discuss/43908/4-different-ways-to-solve-this-with-detailed-explanation.
	 */
	public boolean wordBreak_self(String s, List<String> wordDict) {
        Queue<String> queue = new LinkedList<String>();
        queue.offer("");
        
        Set<String> visited = new HashSet<String>();
        
        while(!queue.isEmpty()){
            String candidate = queue.poll();
            String remain = s.substring(candidate.length());
            
            for (String word : wordDict) {
                if (remain.startsWith(word)) {
                    String prefix = candidate + word;
                    if (prefix.equals(s))
                        return true;
                    
                    if (visited.add(prefix)) {
                        queue.offer(prefix);
                    }
                }
            }
        }
        return false;
    }
	
	/*
	 * https://leetcode.com/problems/word-break/discuss/43797/A-solution-using-BFS/43053
	 * 
	 * The vertices of the graph are simply the positions of the first characters of 
	 * the words and each edge actually represents a word.
	 * 
	 * Other code :
	 * https://leetcode.com/problems/word-break/discuss/43797/A-solution-using-BFS/118921
	 */
	public boolean wordBreak_BFS(String s, List<String> dict) {
		if (dict.contains(s))
			return true;
		
		Queue<Integer> queue = new LinkedList<Integer>();
		queue.offer(0);
		
		// use a set to record checked index to avoid repeated work.
		// This is the key to reduce the running time to O(N^2).
		Set<Integer> visited = new HashSet<Integer>();
		visited.add(0);
		
		while (!queue.isEmpty()) {
			int curIdx = queue.poll();
			for (int i = curIdx + 1; i <= s.length(); i++) {
				if (visited.contains(i))
					continue;
				if (dict.contains(s.substring(curIdx, i))) {
					if (i == s.length())
						return true;
					
					queue.offer(i);
					visited.add(i);
				}
			}
		}
		return false;
	}
	
	/*
	 * https://leetcode.com/problems/word-break/discuss/43790/Java-implementation-using-DP-in-two-ways
	 * 
	 * f[i] stores whether the substring s.substring(0, i) is breakable or not. 
	 * f[0] means whether substring(0, 0) (which is an empty string) can be segmented, 
	 * and of course the answer is yes.
	 * 
	 * The DP equation is as follows:
	 * f[i + 1] = f[j] && dict.contains(s.substring(j, i + 1)), j >= 0 && j < i + 1
	 * 
	 * Rf :
	 * https://leetcode.com/problems/word-break/discuss/44054/Java-DP-solution/251191
	 * https://leetcode.com/problems/word-break/discuss/43790/Java-implementation-using-DP-in-two-ways/43003
	 * https://leetcode.com/problems/word-break/discuss/43796/Accepted-Java-Solution/43027
	 * https://leetcode.com/problems/word-break/discuss/43790/Java-implementation-using-DP-in-two-ways/177908
	 * https://leetcode.com/problems/word-break/discuss/43790/Java-implementation-using-DP-in-two-ways/43007
	 * https://leetcode.com/problems/word-break/discuss/43790/Java-implementation-using-DP-in-two-ways/173773
	 * 
	 * Other code :
	 * https://leetcode.com/problems/word-break/discuss/44054/Java-DP-solution
	 */
	public boolean wordBreak_dp(String s, List<String> dict) {
		boolean[] f = new boolean[s.length() + 1];
		f[0] = true;

		/* First DP
        for(int i = 1; i <= s.length(); i++){
            for(String str: dict){
                if(str.length() <= i){
                    if(f[i - str.length()]){
                        if(s.substring(i-str.length(), i).equals(str)){
                            f[i] = true;
                            break;
                        }
                    }
                }
            }
        }*/

		// Second DP
		for (int i = 1; i <= s.length(); i++) {
			for (int j = 0; j < i; j++) {
				if (f[j] && dict.contains(s.substring(j, i))) {
					f[i] = true;
					break;
				}
			}
		}

		return f[s.length()];
	}
	
	/*
	 * The following variable and function are from this link.
	 * https://leetcode.com/problems/word-break/discuss/148450/Clean-Java-Code-11-lines-5ms
	 * 
	 * Use a set to keep a memo of what all Strings are not breakable into 
	 * constituent words.
	 * 
	 * Rf : 
	 * https://leetcode.com/problems/word-break/discuss/148450/Clean-Java-Code-11-lines-5ms/164400
	 * https://leetcode.com/problems/word-break/discuss/43970/Concise-DFS-(backtracking)-solution
	 */
	Set<String> cannotBreak = new HashSet<>();
	public boolean wordBreak_global_recur(String s, List<String> wordDict) {
		if (wordDict.contains(s))
			return true;
		if (cannotBreak.contains(s))
			return false;
		
		for (String word : wordDict) {
			if (s.startsWith(word)) {
				if (wordBreak_global_recur(s.substring(word.length()), wordDict))
					return true;
			}
		}
		
		cannotBreak.add(s);
		return false;
	}
	
	/*
	 * https://leetcode.com/problems/word-break/discuss/44103/A-Java-solution-with-similar-DP-idea
	 * 
	 * 1. keep all positions which could form substring contained in the List in a 
	 *    linkedlist
	 * 2. Iterate the target string, check substring between current position and 
	 *    stored positions. If new substring hits the dictionary, add it the front 
	 *    of linkedlist
	 * 3. After iteration, check if the front element of linkedlist equals to the 
	 *    length of string.
	 */
	public boolean wordBreak_list_check(String s, List<String> dict) {
		if (s == null || s.length() == 0)
			return false;
		if (dict.contains(s))
			return true;

		List<Integer> starts = new LinkedList<Integer>();
		starts.add(0);

		for (int end = 1; end <= s.length(); end++)
			for (int i = 0; i < starts.size(); i++)
				if (dict.contains(s.substring(starts.get(i), end))) {
					starts.add(0, end);
					break;
				}
		return (starts.get(0) == s.length());
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/word-break/discuss/43819/DFS-with-Path-Memorizing-Java-Solution
	 * 
	 * Use a set to record all position that cannot find a match in dict.
	 */
	public boolean wordBreak_dfs(String s, List<String> dict) {
		// DFS
		Set<Integer> set = new HashSet<Integer>();
		return dfs(s, 0, new HashSet<String>(dict), set);
	}

	private boolean dfs(String s, int index, Set<String> dict, Set<Integer> set) {
		// base case
		if (index == s.length())
			return true;
		// check memory
		if (set.contains(index))
			return false;
		
		// recursion
		for (int i = index + 1; i <= s.length(); i++) {
			String t = s.substring(index, i);
			if (dict.contains(t))
				if (dfs(s, i, dict, set))
					return true;
				else
					set.add(i);
		}
		
		set.add(index);
		return false;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/word-break/discuss/169383/The-Time-Complexity-of-The-Brute-Force-Method-Should-Be-O(2n)-and-Prove-It-Below
	 * 
	 * time complexity is O(2^n), TLE
	 */
	public boolean wordBreak_recursive(String s, List<String> wordDict) {
		// put all words into a hashset
		Set<String> set = new HashSet<>(wordDict);
		return wb_recursive(s, set);
	}

	private boolean wb_recursive(String s, Set<String> set) {
		int len = s.length();
		if (len == 0) {
			return true;
		}
		
		for (int i = 1; i <= len; ++i) {
			if (set.contains(s.substring(0, i)) && wb_recursive(s.substring(i), set)) {
				return true;
			}
		}
		return false;
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/word-break/discuss/43995/A-Simple-Python-DP-solution
     * https://leetcode.com/problems/word-break/discuss/43808/Simple-DP-solution-in-Python-with-description
     * https://leetcode.com/problems/word-break/discuss/43788/4-lines-in-Python
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/word-break/discuss/43814/C%2B%2B-Dynamic-Programming-simple-and-fast-solution-(4ms)-with-optimization
     * https://leetcode.com/problems/word-break/discuss/43954/Clean-DP-solution
     * https://leetcode.com/problems/word-break/discuss/43886/Evolve-from-brute-force-to-optimal-a-review-of-all-solutions
     */

}
