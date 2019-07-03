package OJ0431_0440;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

public class Find_All_Anagrams_in_a_String {
	/*
	 * Match with this link
	 * https://leetcode.com/problems/minimum-window-substring/discuss/26810/Java-solution.-using-two-pointers-+-HashMap
	 */
	public List<Integer> findAnagrams(String s, String p) {
        List<Integer> list = new ArrayList<>();
        if (s == null || s.length() < p.length())
            return list;
        
        Map<Character, Integer> map = new HashMap<>();
        for (char c : p.toCharArray()) {
            int val = map.getOrDefault(c, 0);
            map.put(c, val + 1);
        }
        
        int count = 0;
        int left = 0;
        
        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            if (map.containsKey(c)) {
                int val = map.get(c) - 1;
                map.put(c, val);
                
                if (val >= 0) {
                    count++;
                }
                
                while (count == p.length()) {
                    if (right - left + 1 == p.length()) {
                        list.add(left);
                    }
                    
                    if (map.containsKey(s.charAt(left))) {
                        map.put(s.charAt(left), map.get(s.charAt(left)) + 1);
                        
                        if (map.get(s.charAt(left)) > 0) {
                            count--;
                        }
                    }
                    
                    left++;
                }
            }
        }
        
        return list;
    }
	
	// https://leetcode.com/problems/find-all-anagrams-in-a-string/discuss/92007/Sliding-Window-algorithm-template-to-solve-all-the-Leetcode-substring-search-problem.
	public List<Integer> findAnagrams2(String s, String t) {
		List<Integer> result = new LinkedList<>();
		if (t.length() > s.length())
			return result;
		
		Map<Character, Integer> map = new HashMap<>();
		for (char c : t.toCharArray()) {
			map.put(c, map.getOrDefault(c, 0) + 1);
		}
		
		int counter = map.size();  // the number of keys contained in hash table for string p
		int begin = 0;

		for (int end = 0; end < s.length(); end++) {
			char c = s.charAt(end);
			if (map.containsKey(c)) {
				map.put(c, map.get(c) - 1);
				if (map.get(c) == 0)
					counter--;
			}

			while (counter == 0) {
				char tempc = s.charAt(begin);
				if (map.containsKey(tempc)) {
					map.put(tempc, map.get(tempc) + 1);
					if (map.get(tempc) == 1) {      // if (map.get(tempc) > 0)
						counter++;
					}
				}
				if (end - begin + 1 == t.length()) {
					result.add(begin);
				}
				begin++;
			}
		}
		return result;
	}
	
	// https://leetcode.com/problems/find-all-anagrams-in-a-string/discuss/92076/java-O(n)-using-HashMap-easy-understanding
	public List<Integer> findAnagrams_map(String s, String p) {
		List<Integer> result = new ArrayList<>();
		if (s == null || s.length() == 0 || p.length() > s.length()) {
			return result;
		}
		
		Map<Character, Integer> map = new HashMap<>();
		for(char c : p.toCharArray()){
            map.put(c, map.getOrDefault(c, 0) + 1);
        }
		
		int match = 0;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (map.containsKey(c)) {
				map.put(c, map.get(c) - 1);
				if (map.get(c) == 0) {
					match++;
				}
			}
			
			if (i >= p.length()) {
				c = s.charAt(i - p.length());
				if (map.containsKey(c)) {
					map.put(c, map.get(c) + 1);
					if (map.get(c) == 1) {
						match--;
					}
				}
			}
			
			if (match == map.size()) {
				result.add(i - p.length() + 1);
			}
		}
		return result;
	}
	
	/*
	 * https://leetcode.com/problems/find-all-anagrams-in-a-string/discuss/92068/17ms-Java-sliding-window
	 * 
	 * Rf :
	 * https://leetcode.com/problems/find-all-anagrams-in-a-string/discuss/92015/ShortestConcise-JAVA-O(n)-Sliding-Window-Solution
	 * 
	 * Other code:
	 * https://leetcode.com/problems/find-all-anagrams-in-a-string/discuss/92059/O(n)-Sliding-Window-JAVA-Solution-Extremely-Detailed-Explanation
	 */
	public List<Integer> findAnagrams_array(String s, String p) {
		int[] chars = new int[26];
		List<Integer> result = new ArrayList<>();
		if (s == null || p == null || s.length() < p.length())
			return result;
		
		for (char c : p.toCharArray())
			chars[c - 'a']++;

		int start = 0, end = 0, count = p.length();
		// Go over the string
		while (end < s.length()) {
			// If the char at start appeared in p, we increase count
			if (end - start == p.length()) {
				if (chars[s.charAt(start) - 'a'] >= 0) {
					count++;
				}
				chars[s.charAt(start) - 'a']++;
				start++;
			}

			// If the char at end appeared in p, we decrease count
			if (chars[s.charAt(end) - 'a'] >= 1) {
				count--;
			}
			chars[s.charAt(end) - 'a']--;
			end++;
			
			if (count == 0)
				result.add(start);
		}

		return result;
	}
	
	/*
	 * https://leetcode.com/problems/find-all-anagrams-in-a-string/discuss/92029/Easy-to-understand-Java-sliding-window-solution-(greater95)
	 * 
	 * When a valid anagram occurs, 
	 * (1) slide length = p.length(); (2) all hash counts ==0.
	 * 
	 * The inner while loop makes sure all hash counts >=0. 
	 * All needed to check is if slide length == p.length().
	 */
	public List<Integer> findAnagrams_no_count(String s, String p) {
		int[] hash = new int[128];
		for (int i = 0; i < p.length(); i++) {
			hash[p.charAt(i)]++;
		}
		
		List<Integer> res = new ArrayList<>();
		int start = 0;
		
		// The goal is to make sure the whole hash array is >= 0.
		// When hash[i] < 0, move start to get back to 0.
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			hash[c]--;
			
			while (hash[c] < 0) {
				hash[s.charAt(start)]++;
				start++;
			}
			
			if (i - start + 1 == p.length()) {
				res.add(start);
			}
		}
		return res;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/find-all-anagrams-in-a-string/discuss/92123/Simplest-AC-java-sliding-window-solution-with-comments
	 */
	List<Integer> findAnagrams_2_slide_array(String s, String p) {
        List<Integer> res = new ArrayList<>();
        int[] win = new int[256];
        int[] pFixWin = new int[256];

        if (s.length() == 0 || p.length() == 0 || p.length() > s.length()) {
            return res;
        }

        // pre-load the pWin
        for (int i = 0; i < p.length(); i++) {
            pFixWin[p.charAt(i)]++;
        }

        // pre-load the moving  window
        for (int i = 0; i < p.length(); i++) {
            win[s.charAt(i)]++;
        }

        for (int i = 0; i < s.length(); i++) {
            // for each position check if the numbers of each letter
            // coincide in the window and the fixed window
            if (isPContainedInS(pFixWin, win)) {
                res.add(i);
            }

            // evict from the window the char we just passed in S
            // add from the S window the next character
            if ((i + p.length()) < s.length()) {
                win[s.charAt(i)]--;
                win[s.charAt(i + p.length())]++;
            } 
            else {
                break;
            }
        }
        return res;
    }
    private boolean isPContainedInS(int[] pFixWin, int[] win) {
        for (int i = 0; i < pFixWin.length; i++) {
            if (pFixWin[i] != win[i]) {
                return false;
            }
        }
        return true;
    }
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/find-all-anagrams-in-a-string/discuss/92032/Java-using-isAnagram()-helper-function-easy-to-understand
	 */
	public List<Integer> findAnagrams_all_anagrams(String s, String p) {
		List<Integer> res = new ArrayList<>();
		if (p == null || s == null || s.length() < p.length())
			return res;
		
		int m = s.length(), n = p.length();
		for (int i = 0; i < m - n + 1; i++) {
			String cur = s.substring(i, i + n);
			if (helper(cur, p))
				res.add(i);
		}
		return res;
	}

	public boolean helper(String a, String b) {
		if (a == null || b == null || a.length() != b.length())
			return false;
		
		int[] dict = new int[26];
		for (int i = 0; i < a.length(); i++) {
			char ch = a.charAt(i);
			dict[ch - 'a']++;
		}
		for (int i = 0; i < b.length(); i++) {
			char ch = b.charAt(i);
			dict[ch - 'a']--;
			if (dict[ch - 'a'] < 0)
				return false;
		}
		return true;
	}

}
