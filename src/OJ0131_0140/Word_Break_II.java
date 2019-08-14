package OJ0131_0140;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

public class Word_Break_II {
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/word-break-ii/discuss/44167/My-concise-JAVA-solution-based-on-memorized-DFS/43403
	 * 
	 * Find a possible break point and then recursively call the suffix of the 
	 * original string.
	 * Used HashMap to save the previous results to prune duplicated branches
	 * 
	 * Rf :
	 * https://leetcode.com/problems/word-break-ii/discuss/44167/My-concise-JAVA-solution-based-on-memorized-DFS/215095
	 * https://leetcode.com/problems/word-break-ii/discuss/44264/Java-DFS-%2B-DP-clean-solution
	 */
	public List<String> wordBreak(String s, List<String> wordDict) {
		return DFS(s, wordDict, new HashMap<String, List<String>>());
	}

	// DFS function returns an array including all substrings derived from s.
	List<String> DFS(String s, List<String> wordDict, 
			Map<String, List<String>> map) {
		if (map.containsKey(s))
			return map.get(s);

		List<String> res = new ArrayList<String>();
		for (String word : wordDict) {
			if (s.startsWith(word)) {
				String next = s.substring(word.length());
				if (next.length() == 0)
					res.add(word);
				else {
					List<String> sublist = DFS(next, wordDict, map);
					for (String sub : sublist)
						res.add(word + " " + sub);
				}
			}
		}
		map.put(s, res);
		return res;
	}
	
	/*
	 * The following variable and function are from this link.
	 * https://leetcode.com/problems/word-break-ii/discuss/44446/This-is-my-accepted-java-version-program-is-there-any-better-solution
	 * 
	 * Use DP- with a hashmap recording all the strings that are computed before.
	 * Partition the string from the first character until the last character. 
	 * Each time the string is divided into two parts: left and right. Only if the 
	 * left string exists in the dict, we will try to get all of the combinations of 
	 * the right string. And then form the list of combinations of the left and right.
	 * 
	 * Other code :
	 * https://leetcode.com/problems/word-break-ii/discuss/44291/Concise-Java-solution-(recursion-%2B-memoization)
	 * https://leetcode.com/problems/word-break-ii/discuss/44271/Java-Recursive-DP-Solution
	 * 
	 * TLE code for reference:
	 * https://leetcode.com/problems/word-break-ii/discuss/44195/Accepted-Java-solution-backtracking.-But-what's-the-difference-with-front-tracking
	 */
	Map<String, List<String>> map_recursive_global = new HashMap<>();
	public List<String> wordBreak_recursive_global(String s, List<String> dict) {
		if (map_recursive_global.containsKey(s))
			return map_recursive_global.get(s);

		List<String> list = new ArrayList<String>();
		for (int i = 1; i <= s.length(); i++) {
			String left = s.substring(0, i);
			String right = s.substring(i);
			if (dict.contains(left)) {
				if (i == s.length())
					list.add(left);
				else {
					List<String> res = wordBreak_recursive_global(right, dict);
					for (String element : res) {
						list.add(left + " " + element);
					}
				}
			}
		}

		map_recursive_global.put(s, list);
		return list;
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/word-break-ii/discuss/44179/Slightly-modified-DP-Java-solution
	 * 
	 * The approach is the same as before: we loop through all possible prefixes 
	 * checking if it in the dictionary and caching the results.
	 * 
	 * But just before jumping into recursion we could also check that the right 
	 * reminder has a prefix from the dictionary, because if it hasn't then there's 
	 * no sense in splitting the reminder into sub-strings.
	 */
	private final Map<String, List<String>> cache = new HashMap<>();
	private boolean containsSuffix(List<String> dict, String str) {
		for (int i = 0; i < str.length(); i++) {
			if (dict.contains(str.substring(i)))
				return true;
		}
		return false;
	}

	public List<String> wordBreak_front_back(String s, List<String> dict) {
		if (cache.containsKey(s))
			return cache.get(s);
		
		List<String> result = new LinkedList<>();
		if (dict.contains(s))
			result.add(s);
		
		for (int i = 1; i < s.length(); i++) {
			String left = s.substring(0, i), right = s.substring(i);
			
			if (dict.contains(left) && containsSuffix(dict, right)) {
				for (String ss : wordBreak_front_back(right, dict)) {
					result.add(left + " " + ss);
				}
			}
		}
		cache.put(s, result);
		return result;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/word-break-ii/discuss/44287/Java-beating-95.13-with-explanation%3A-DFS-%2B-memoization-to-avoid-TLE
	 * 
	 * Basically the idea is to use a boolean array to avoid repeated computation.
	 * boolean[] invalid = new boolean[s.length() + 1]
	 * invalid[i] means whether s.substring(i) is "not breakable"
	 * The helper function will return a boolean value whether the substring is 
	 * "breakable" or not.
	 * 
	 * For each call to helper function, denote current substring as 
	 * [left, s.length()). Iterate right pointer i from left + 1 to s.length(). 
	 * If [left, i) can be found in the dict, and [i, s.length()) is "breakable", 
	 * then the whole [left, s.length()) is "breakable".
	 * 
	 * Keyword: DFS, backtracking
	 * 
	 * Rf : https://leetcode.com/problems/word-break-ii/discuss/44243/Java-DP%2BDFS-Memoization%2BDFS-and-DP-Pruning-Solutions-with-Analysis
	 */
	public List<String> wordBreak2(String s, List<String> wordDict) {
		List<String> res = new ArrayList<>();
		wordBreak2(s, 0, wordDict, "", new boolean[s.length() + 1], res);
		return res;
	}

	// @param s        string to break
	// @param left     start point
	// @param wordDict dictionary
	// @param prev     previous word found
	// @param invalid  invalid[i] means whether [i, s.length()) is unbreakable
	// @param res      list to store results
	// @return true if s.substring(left) is breakable.
	private boolean wordBreak2(String s, int left, List<String> wordDict, 
			String prev, boolean[] invalid, List<String> res) {
		// Base case: successfully moved to the end, add result to the list.
		if (left == s.length()) {
			res.add(prev.trim());
			return true;
		}
		
		// whether s.substring(left) is breakable
		boolean possible = false;
		
		// iterate the pointer from left + 1 to the end, find whether [left, i) is 
		// valid
		for (int i = left + 1; i <= s.length(); i++) {
			// if s.substring(i) is unbreakable, continue
			if (invalid[i]) {
				continue;
			}
			
			String sub = s.substring(left, i);
			
			// if substring [left, i) is valid, move on and break from i
			if (wordDict.contains(sub)) {
				boolean flag = wordBreak2(s, i, wordDict, 
						prev.concat(" ").concat(sub), invalid, res);
				// as long as at least one valid substring [i, end), possible is true
				possible = flag || possible;
			}
		}
		
		// update invalid array
		invalid[left] = !possible;
		return possible;
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/word-break-ii/discuss/44299/Java-6ms-simple-solution-beating-88
	 */
	HashMap<Integer, List<String>> dp = new HashMap<>();
	public List<String> wordBreak_recur_maxLength(String s, List<String> wordDict) {
		int maxLength = -1;
		for (String ss : wordDict)
			maxLength = Math.max(maxLength, ss.length());
		
		return addSpaces(s, wordDict, 0, maxLength);
	}

	private List<String> addSpaces(String s, List<String> wordDict, 
			int start, int max) {
		List<String> words = new ArrayList<>();
		if (start == s.length()) {
			words.add("");
			return words;
		}
		
		for (int i = start + 1; i <= max + start && i <= s.length(); i++) {
			String temp = s.substring(start, i);
			if (wordDict.contains(temp)) {
				List<String> ll;
				if (dp.containsKey(i))
					ll = dp.get(i);
				else
					ll = addSpaces(s, wordDict, i, max);
				
				for (String ss : ll)
					words.add(temp + (ss.equals("") ? "" : " ") + ss);
			}

		}
		dp.put(start, words);
		return words;
	}
	
	/*
	 * The following 3 functions are from this link.
	 * https://leetcode.com/problems/word-break-ii/discuss/44191/Java-DP-solution
	 * 
	 * Rf : https://leetcode.com/problems/word-break-ii/discuss/44243/Java-DP%2BDFS-Memoization%2BDFS-and-DP-Pruning-Solutions-with-Analysis
	 */
	public List<String> wordBreak_check_recursive(String s, List<String> dict) {
		List<String> res = new ArrayList<String>();
		// Do a fast check and see if we can break the input string
		if (!canBreak(s, dict))
			return res;

		helper_check_recursive(s, dict, 0, "", res);
		return res;
	}

	void helper_check_recursive(String s, List<String> dict, int start, 
			String sol, List<String> res) {
		if (start == s.length()) {
			res.add(sol);
			return;
		}

		for (int i = start; i < s.length(); i++) {
			String sub = s.substring(start, i + 1);

			if (dict.contains(sub))
				helper_check_recursive(s, dict, i + 1, 
						sol + (sol.length() == 0 ? "" : " ") + sub, res);
		}
	}

	// Solution from "Word Break"
	boolean canBreak(String s, List<String> dict) {
		if (s == null || s.length() == 0)
			return false;

		int n = s.length();

		// dp[i] represents whether s[0...i] can be formed by dict
		boolean[] dp = new boolean[n];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j <= i; j++) {
				String sub = s.substring(j, i + 1);

				if (dict.contains(sub) && (j == 0 || dp[j - 1])) {
					dp[i] = true;
					break;
				}
			}
		}

		return dp[n - 1];
	}
	
	/*
	 * https://leetcode.com/problems/word-break-ii/discuss/44353/My-DP-solution-in-JAVA
	 * 
	 * TLE
	 * 
	 * 1. Scan the the string from the tail
	 * 2. Build possible solution for the current index based on DP results
	 * 3. Return the solution when index==0
	 */
	public List<String> wordBreak_dp(String s, List<String> dict) {
		Map<Integer, List<String>> validMap = new HashMap<Integer, List<String>>();

		// initialize the valid values
		List<String> l = new ArrayList<String>();
		l.add("");
		validMap.put(s.length(), l);

		// generate solutions from the end
		for (int i = s.length() - 1; i >= 0; i--) {
			List<String> values = new ArrayList<String>();
			for (int j = i + 1; j <= s.length(); j++) {
				if (dict.contains(s.substring(i, j))) {
					for (String word : validMap.get(j)) {
						values.add(s.substring(i, j) + 
								(word.isEmpty() ? "" : " ") + word);
					}
				}
			}
			validMap.put(i, values);
		}
		return validMap.get(0);
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/word-break-ii/discuss/44169/9-lines-Python-10-lines-C%2B%2B
     * https://leetcode.com/problems/word-break-ii/discuss/44311/Python-easy-to-understand-solution
     * https://leetcode.com/problems/word-break-ii/discuss/44368/Python-easy-to-understand-solution-(DP%2BDFS%2BBacktracking).
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/word-break-ii/discuss/44178/11ms-C%2B%2B-solution-(concise)
     * https://leetcode.com/problems/word-break-ii/discuss/44262/My-C%2B%2B-DP%2BDFS-solution-(4ms)
     * https://leetcode.com/problems/word-break-ii/discuss/194615/DP-solution-with-detailed-text-and-video-explanation
     */

}
