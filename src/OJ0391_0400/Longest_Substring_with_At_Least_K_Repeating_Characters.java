package OJ0391_0400;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Longest_Substring_with_At_Least_K_Repeating_Characters {
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/longest-substring-with-at-least-k-repeating-characters/discuss/119700/Java-3ms-10-lines-beat-100
	 * 
	 * 跳過數量不到 k 的位置, 遞迴處理
	 * 
	 * longestSubstring_n(String s, int k) 是 O(n) 的做法
	 * 
	 * Rf : https://leetcode.com/problems/longest-substring-with-at-least-k-repeating-characters/discuss/87741/Java-divide-and-conquer(recursion)-solution
	 * 
	 * Other code :
	 * https://leetcode.com/problems/longest-substring-with-at-least-k-repeating-characters/discuss/293614/Clean-and-Easy-to-Understand-Java-Solution-Using-Divide-And-Conquer
	 */
	public int longestSubstring_left_right(String s, int k) {
		return helper(s.toCharArray(), 0, s.length(), k);
	}

	public int helper(char[] chs, int left, int right, int k) {
		if (right - left < k)
			return 0;
		
		int[] count = new int[26];
		for (int i = left; i < right; i++)
			count[chs[i] - 'a']++;
		
		for (int i = left; i < right; i++) {
			if (count[chs[i] - 'a'] < k) {
				int j = i + 1;
				while (j < right && count[chs[j] - 'a'] < k)
					j++;
				
				return Math.max(helper(chs, left, i, k), helper(chs, j, right, k));
			}
		}
		return right - left;
	}
	
	/*
	 * https://leetcode.com/problems/longest-substring-with-at-least-k-repeating-characters/discuss/262931/Java-Short-Solution-!!!
	 * 
	 * In each step, just find the infrequent elements (less than k times) as splits 
	 * since any of these infrequent elements couldn't be any part of the substring we 
	 * want.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/longest-substring-with-at-least-k-repeating-characters/discuss/87738/Java-20-lines-very-easy-solution-7ms-with-explanation
	 * 
	 * Other code :
	 * https://leetcode.com/problems/longest-substring-with-at-least-k-repeating-characters/discuss/87768/4-lines-Python/92628
	 * https://leetcode.com/problems/longest-substring-with-at-least-k-repeating-characters/discuss/87774/Simple-JAVA-O(n)-Solution
	 */
	public int longestSubstring_sub(String s, int k) {
		if (s == null || s.length() < k)
			return 0;
		
		int[] map = new int[26];
		for (char c : s.toCharArray())
			map[c - 'a']++;

		boolean valid = true;
		for (int i = 0; i < 26; i++)
			if (map[i] > 0 && map[i] < k) {
				valid = false;
				break;
			}
		if (valid)
			return s.length();

		int res = 0, start = 0, idx = 0;
		while (idx < s.length()) {
			if (map[s.charAt(idx) - 'a'] < k) {
				res = Math.max(res, longestSubstring_sub(s.substring(start, idx), k));
				start = idx + 1;
			}
			idx++;
		}
		res = Math.max(res, longestSubstring_sub(s.substring(start), k));
		return res;
	}
	
	// https://leetcode.com/problems/longest-substring-with-at-least-k-repeating-characters/discuss/87796/Simple-Java-solution
	public int longestSubstring_sub2(String s, int k) {
        int[] arr = new int[26];
        for (char c : s.toCharArray()) {
            arr[c - 'a']++;
        }
        
        int idx = 0, max = 0;
        for (int i = 0; i < s.length(); i++) {
            if (arr[s.charAt(i) - 'a'] < k) {
               max = Math.max(max, longestSubstring_sub2(s.substring(idx, i), k));
               idx = i + 1;
            }
        }
        
        return idx == 0 ? s.length() : Math.max(max, longestSubstring_sub2(s.substring(idx), k));
    }
	
	/*
	 * https://leetcode.com/problems/longest-substring-with-at-least-k-repeating-characters/discuss/170010/Java-O(n)-Solution-with-Detailed-Explanation-Sliding-Window
	 * 
	 * 在不同的 unique character 上限 (u) 下，使用 sliding window (從 0 到 s.length() - 1)
	 * 若這次 iterate 的 unique character (unique) 還沒超過 (<=) 該 iteration 所應達到的上限 (u)
	 *   expand
	 * 否則 
	 *   shrink
	 * 
	 * 並使用 kOrMore 來記錄達到 k 的 unique character 數量
	 * 
	 * 每次 iteration 時，更新 max 的條件
	 * 1. kOrMore 和 unique character 數量相同，表示所有 unique character 都 >= k
	 * 2. 達到該 iteration 所要求的 unique character 上限 (可省略)
	 * 
	 * Iterate through the number of possible unique letters 1 to 26. Lets call our 
	 * target amount of unique letters u. We search all windows of letters where the 
	 * number of unique letters <= u. This is the number of any unique letters, not 
	 * the number of unique letters that occur k or more times.
	 * We count the number of current unique letters, and the number of letters that 
	 * have a count of k or more using a sliding window.
	 * 
	 * We expand, if our number of unique letters is less than or equal to u
	 * If the count is equal to 1 we know this is a new letter so we increment unique, 
	 * and if it is equal to k we increment k or more since it just became k or more.
	 * 
	 * we need to keep expanding the letters if its at the target u because we can 
	 * still have a chance at getting more kOrMore letters and u==unique. 
	 * For example, aaabb if we stop at "a" since unique == u (1=1) we won't ever get 
	 * to "aaa" which is the answer.
	 * 
	 * We shorten, if our number of unique letters is more than u
	 * If the count is equal to 0 we decrement the number of unique letters since all 
	 * instances of this letter are gone, if it's equal to k-1 it is now not k or more 
	 * so we decrement kOrMore.
	 * 
	 * This interval is a valid candidate if the number of unique letters is u and 
	 * all the unique letters have kOrMore counts
	 * 
	 * Rf :
	 * https://leetcode.com/problems/longest-substring-with-at-least-k-repeating-characters/discuss/87739/Java-Strict-O(N)-Two-Pointer-Solution/158670
	 * https://leetcode.com/problems/longest-substring-with-at-least-k-repeating-characters/discuss/87739/Java-Strict-O(N)-Two-Pointer-Solution/200515
	 * https://leetcode.com/problems/longest-substring-with-at-least-k-repeating-characters/discuss/87739/Java-Strict-O(N)-Two-Pointer-Solution/92538
	 * https://leetcode.com/problems/longest-substring-with-at-least-k-repeating-characters/discuss/87739/Java-Strict-O(N)-Two-Pointer-Solution
	 */
	public int longestSubstring_n(String s, int k) {
		int[] counts = new int[26];
		int max = 0;
		for (int u = 1; u <= 26; ++u) {
			Arrays.fill(counts, 0);
			
			int left = 0;
			int right = 0;
			int unique = 0;
			int kOrMore = 0;
			
			while (right < s.length()) {
				if (unique <= u) {
					char c = s.charAt(right);
					int idx = (int) c - (int) 'a';
					counts[idx]++;
					
					if (counts[idx] == 1) {
						++unique;
					}
					if (counts[idx] == k) {
						++kOrMore;
					}
					++right;
				} 
				else {
					char o = s.charAt(left);
					int idx = (int) o - (int) 'a';
					counts[idx]--;
					
					if (counts[idx] == 0) {
						--unique;
					}
					if (counts[idx] == k - 1) {
						--kOrMore;
					}
					++left;
				}
				
				if (unique == u && kOrMore == unique) {
					max = Math.max(max, right - left);
				}
			}
		}
		return max;
	}
	
	/*
	 * https://leetcode.com/problems/longest-substring-with-at-least-k-repeating-characters/discuss/87761/Java-D-and-C-Solution
	 * 
	 * Find the point where we should split the string, 
	 * eg, the position of character which total count is < k, 
	 * then dfs it and find the max.
	 * 
	 * For Example: bbcddefegaghfh and 2, so we shall dfs on "bb", "ddefeg", "ghfh", 
	 * since a , c only appears once.
	 * 
	 * Other code :
	 * https://leetcode.com/problems/longest-substring-with-at-least-k-repeating-characters/discuss/87771/Java-(nlogn)-recursive-solution-by-dividing-the-string-into-substrings(15ms)
	 */
	public int longestSubstring_Divide_Conquer(String s, int k) {
		if (s == null || s.length() == 0 || k == 0)
			return 0;
		
		int[] count = new int[26];
		for (int i = 0; i < s.length(); i++) {
			count[s.charAt(i) - 'a']++;
		}
		
		List<Integer> pos = new ArrayList<Integer>();
		for (int i = 0; i < s.length(); i++) {
			if (count[s.charAt(i) - 'a'] < k)
				pos.add(i);
		}
		if (pos.size() == 0)
			return s.length();
		
		pos.add(0, -1);
		pos.add(s.length());
		int res = 0;
		for (int i = 1; i < pos.size(); i++) {
			int start = pos.get(i - 1) + 1;
			int end = pos.get(i);
			int next = longestSubstring_Divide_Conquer(s.substring(start, end), k);
			res = Math.max(res, next);
		}
		return res;
	}
	
	/*
	 * https://leetcode.com/problems/longest-substring-with-at-least-k-repeating-characters/discuss/87819/Java-Solution-using-2D-array-to-store-letter-occurrences-(17ms)
	 * 
	 * First, traverse the string to store how many times each letter has occurred 
	 * from 0 to i. Substring can be represented as [c][i] - [c][j], 0 <= j < i
	 * Then, find the longest working substring starting at each position, with the 
	 * starting point from left to right.
	 * 
	 * (1) if the long substring does not work, cut its tail to exclude the letter 
	 *     that makes it not working 
	 * (2) if the long substring works, no need to check its substrings shorter than it 
	 * (3) [most important to get "aaaaaaa....a" test case to work] 
	 *     if s.length() - start is already smaller than the known working longest 
	 *     substring, we are done.
	 * 
	 * Rf : https://leetcode.com/problems/longest-substring-with-at-least-k-repeating-characters/discuss/87775/Java-HashMap-Solution-worst-case-O((n2)*26)
	 */
	public int longestSubstring_table(String s, int k) {
		if (s == null || s.length() < 1)
			return 0;

		// letter counts for 0 to i
		int[][] letters = new int[26][s.length() + 1];
		for (int i = 0; i < s.length(); i++) {
			// copy letter counts from 0 to i - 1
			for (int c = 0; c < 26; c++) {
				letters[c][i + 1] = letters[c][i];
			}
			letters[s.charAt(i) - 'a'][i + 1] += 1;
		}
		// May also optimize by deleting letters entries with 0 at end

		int longest = 0;
		for (int start = 0; start < s.length(); start++) {
			if (longest >= s.length() - start)
				return longest;
			
			for (int end = s.length(); end > start; end--) {
				boolean works = true;
				for (int c = 0; c < 26; c++) {
					int num = letters[c][end] - letters[c][start];
					if (num < k && num > 0) {
						works = false;
						end = s.indexOf('a' + c, start) + 1;  // Cut the tail
						break;
					}
				}
				
				// No need to find shorter substring
				if (works) {
					if (end - start > longest)
						longest = end - start;
					
					break;
				}
			}
		}

		return longest;
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/longest-substring-with-at-least-k-repeating-characters/discuss/87768/4-lines-Python
     * https://leetcode.com/problems/longest-substring-with-at-least-k-repeating-characters/discuss/139609/python-iterative-and-recursive-solution
     * https://leetcode.com/problems/longest-substring-with-at-least-k-repeating-characters/discuss/235995/Python-fast-solution
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/longest-substring-with-at-least-k-repeating-characters/discuss/87736/C%2B%2B-recursive-solution
     * https://leetcode.com/problems/longest-substring-with-at-least-k-repeating-characters/discuss/87749/Two-short-C%2B%2B-solutions-(3ms-and-6ms)
     * https://leetcode.com/problems/longest-substring-with-at-least-k-repeating-characters/discuss/87742/C%2B%2B-O(n)-Divide-and-Conquer
     */

}
