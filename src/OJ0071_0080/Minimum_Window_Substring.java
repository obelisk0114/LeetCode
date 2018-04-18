package OJ0071_0080;

import java.util.Map;
import java.util.HashMap;

// https://leetcode.com/problems/minimum-window-substring/discuss/26808/Here-is-a-10-line-template-that-can-solve-most-'substring'-problems

public class Minimum_Window_Substring {
	/*
	 * https://leetcode.com/problems/minimum-window-substring/discuss/26810/Java-solution.-using-two-pointers-+-HashMap
	 * 
	 * 1. create a hashmap for each character in t and count their frequency in t 
	 *    as the value of hashmap.
	 * 2. Find the first window in S that contains T. The author uses the count.
	 * 3. Checking from the leftmost index of the window and to see if it belongs to t. 
	 *    The reason we do so is that we want to shrink the size of the window.
	 *    3-1) If the character at leftmost index does not belong to t, we can directly 
	 *         remove this leftmost value and update our window 
	 *         (its minLeft and minLen value)
	 *    3-2) If the character indeed exists in t, we still remove it, but in the 
	 *         next step, we will increase the right pointer and expect the removed 
	 *         character. If find so, repeat step 3.
	 */
	public String minWindow(String s, String t) {
		if (s == null || s.length() < t.length() || s.length() == 0) {
			return "";
		}
		
		HashMap<Character, Integer> map = new HashMap<Character, Integer>();
		for (char c : t.toCharArray()) {
            int val = map.getOrDefault(c, 0);
			map.put(c, val + 1);
		}
		
		int left = 0;
		int minLeft = 0;
		int minLen = s.length() + 1;
		int count = 0;
		
		for (int right = 0; right < s.length(); right++) {
			if (map.containsKey(s.charAt(right))) {
				map.put(s.charAt(right), map.get(s.charAt(right)) - 1);
				
				// identify if the first window is found by counting frequency of 
				// the characters of t showing up in S
				if (map.get(s.charAt(right)) >= 0) {
					count++;
				}
				
				while (count == t.length()) {
					if (right - left + 1 < minLen) {
						minLeft = left;
						minLen = right - left + 1;
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

		if (minLen > s.length()) {
			return "";
		}
		return s.substring(minLeft, minLeft + minLen);
	}
	
	// https://leetcode.com/problems/minimum-window-substring/discuss/26889/Accepted-Solution-for-your-reference
	public String minWindow2(String S, String T) {
	    int[] result = new int[] {-1, S.length()};
	    int counter = 0;
	    Map<Character, Integer> expected = new HashMap<>();
	    Map<Character, Integer> window = new HashMap<>();
	    
	    for (int i = 0; i < T.length(); i++) {
	        int val = expected.getOrDefault(T.charAt(i), 0);
	        expected.put(T.charAt(i), val + 1);
	    }
	    
		for (int i = 0, j = 0; j < S.length(); j++) {
			char cur = S.charAt(j);
			if (expected.containsKey(cur)) {
				int val = window.getOrDefault(cur, 0);
				window.put(cur, val + 1);
				if (window.get(cur) <= expected.get(cur))
					counter++;
				
				if (counter == T.length()) {
					char remove = S.charAt(i);
					while (!expected.containsKey(remove) || window.get(remove) > expected.get(remove)) {
						if (expected.containsKey(remove))
							window.put(remove, window.get(remove) - 1);
						
						remove = S.charAt(++i);
					}
					
					if (j - i < result[1] - result[0]) {
						result[0] = i;
						result[1] = j;
					}
				}
			}
		}
		return result[1] - result[0] < S.length() ? S.substring(result[0], result[1] + 1) : "";
	}
	
	/*
	 * https://leetcode.com/problems/minimum-window-substring/discuss/26814/O(n)-5ms-Java-Solution-Beats-93.18
	 * 
	 * Rf : http://articles.leetcode.com/finding-minimum-window-in-s-which/
	 */
	public String minWindow_for_end_while_start(String s, String t) {
		char[] needToFind = new char[256];
		char[] hasFound = new char[256];
		
		int sLen = s.length();
		int tLen = t.length();
		int count = 0;              // Only used to state finding or not
		
		int optLen = Integer.MAX_VALUE; // opt stands for optimal
		int optBegin = 0;
		int optEnd = 0;
		
		// gives a counter for each character in t
		for (int i = 0; i < tLen; i++) {
			needToFind[t.charAt(i)]++;
		}
		
		for (int begin = 0, end = 0; end < sLen; end++) {
			if (needToFind[s.charAt(end)] == 0) { // skips irrelevant char
				continue;
			}
			
			char currEnd = s.charAt(end); // the char at the end
			hasFound[currEnd]++;
			if (hasFound[currEnd] <= needToFind[currEnd]) {
				count++;
			}
			
			// pauses end, moves beginning to the right as much as possible
			if (count == tLen) {
				char currBegin = s.charAt(begin); // char at begin
				// include begin
				while (hasFound[currBegin] > needToFind[currBegin] || needToFind[currBegin] == 0) {
					if (hasFound[currBegin] > needToFind[currBegin]) {
						hasFound[currBegin]--;
					}
					begin++;
					currBegin = s.charAt(begin);
				}
				
				// if current length is smaller, update our optimum solution
				if (optLen > end - begin + 1) {
					optLen = end - begin + 1;
					optBegin = begin;
					optEnd = end;
				}
			}
		}
		
		if (count != tLen) {
			return "";
		}
		return s.substring(optBegin, optEnd + 1);
	}
	
	// https://leetcode.com/problems/minimum-window-substring/discuss/26811/Share-my-neat-java-solution
	public String minWindow_while2(String S, String T) {
		if (S == null || S.isEmpty() || T == null || T.isEmpty())
			return "";
		
		int i = 0, j = 0;
		
		int[] Tmap = new int[256];
		int[] Smap = new int[256];
		for (int k = 0; k < T.length(); k++) {
			Tmap[T.charAt(k)]++;
		}
		
		int found = 0;
		int length = Integer.MAX_VALUE;
		String res = "";
		
		while (j < S.length()) {
			if (found < T.length()) {
				if (Tmap[S.charAt(j)] > 0) {
					Smap[S.charAt(j)]++;
					if (Smap[S.charAt(j)] <= Tmap[S.charAt(j)]) {
						found++;
					}
				}
				j++;
			}
			
			while (found == T.length()) {
				if (j - i < length) {
					length = j - i;
					res = S.substring(i, j);
				}
				
				if (Tmap[S.charAt(i)] > 0) {
					Smap[S.charAt(i)]--;
					if (Smap[S.charAt(i)] < Tmap[S.charAt(i)]) {
						found--;
					}
				}
				
				i++;
			}
		}
		return res;
	}
	
	// https://leetcode.com/problems/minimum-window-substring/discuss/26896/The-fast-7ms-O(N)-Java-solution-use-only-one-array-without-map
	public String minWindow_while3(String s, String t) {
		if (t.length() <= 0 || s.length() < t.length())
			return "";
		
		int start = 0, end = 0, i = 0, j = 0;
		int count = t.length(), min = s.length() + 1;
		int[] table = new int[256];

		for (int k = 0; k < count; k++) {
			char c = t.charAt(k);
			table[c]++;
		}
		for (int k = 0; k < 256; k++) {
			if (table[k] < 1)
				table[k] = Integer.MIN_VALUE;
		}
		
		while (end < s.length()) {
			// expand end point
			while (end < s.length() && count > 0) {
				char c = s.charAt(end++);
				if (table[c] != Integer.MIN_VALUE) {
					if (table[c] > 0)
						count--;
					table[c]--;
				}
			}
			if (count > 0)
				break;
			
			// shrink start point
			while (start < s.length() && count <= 0) {
				char c = s.charAt(start++);
				if (table[c] != Integer.MIN_VALUE) {
					if (table[c] >= 0)
						count++;
					table[c]++;
				}
			}
			
			// update
			if (end - start + 1 < min) {
				min = end - start + 1;
				i = start - 1;
				j = end;
			}
		}
		return min == s.length() + 1 ? "" : s.substring(i, j);
	}
	
	// The following 2 functions are by myself.
	public String minWindow_self(String s, String t) {
        int i = 0;
        int start = 0;
        int end = 0;
        int length = Integer.MAX_VALUE;
        
        Map<Character, Integer> map = new HashMap<Character, Integer>();
        for (char c : t.toCharArray()) {
            int number = map.getOrDefault(c, 0);
            map.put(c, number + 1);
        }
        Map<Character, Integer> tmp = new HashMap<Character, Integer>();
        
        for (int j = 0; j < s.length(); j++) {
            int number = tmp.getOrDefault(s.charAt(j), 0);
            tmp.put(s.charAt(j), number + 1);
            
            while (check(tmp, map)) {
                if (j - i + 1 < length) {
                    start = i;
                    end = j;
                    length = j - i + 1;
                }
                int num = tmp.get(s.charAt(i));
                tmp.put(s.charAt(i), num - 1);
                
                if (num == 1) {
                    tmp.remove(s.charAt(i));
                }
                
                i++;
            }
        }
        
        if (length == Integer.MAX_VALUE) {
            return "";
        }
        return s.substring(start, end + 1);
    }
    private boolean check(Map<Character, Integer> cur, Map<Character, Integer> store) {
        for (Map.Entry<Character, Integer> entry : store.entrySet()) {
            int val = cur.getOrDefault(entry.getKey(), 0);
            if (val < entry.getValue()) {
                return false;
            }
        }
        return true;
    }
    
    /*
     * The following 2 functions are from this link.
     * https://leetcode.com/problems/minimum-window-substring/discuss/26902/O(N)-JAVA-Sliding-Window-solution-with-explanation
     * 
     * 1) Spread the right pointer until it satisfies the requirement  
     * 2) Shrink the left pointer to get the minimum range 
     * 3) Keep the above steps.
     */
    boolean sContainsT(int mapS[], int mapT[]) {   // Runtime = O(256) = O(1)
    	// s should cover all characters in t
        for (int i = 0; i < mapT.length; i++) {
            if (mapT[i] > mapS[i])
                return false; 
        }           
        return true;
    }
    public String minWindow_2_array_check(String s, String t) {
        int mapS[] = new int[256];     // Count characters in s
        int mapT[] = new int[256];     // Count characters in t      
        for (char ch : t.toCharArray())
            mapT[ch]++;

		String res = "";
		int right = 0, min = Integer.MAX_VALUE;
		
		// Two pointers of the sliding window: i(left), right
		for (int i = 0; i < s.length(); i++) {
			// Extend the right pointer of the sliding window
			while (right < s.length() && !sContainsT(mapS, mapT)) {
                mapS[s.charAt(right)]++;
                right++;
            }
			
            if (sContainsT(mapS, mapT) && min > right - i + 1) {
                res = s.substring(i, right);
                min = right - i + 1;
            }
            mapS[s.charAt(i)]--;// Shrink the left pointer from i to i + 1
        }
        return res;
    }
    
    // https://leetcode.com/problems/minimum-window-substring/discuss/26835/Java-4ms-bit-97.6
	public String minWindow_one_hash(String s, String t) {
		char[] s_array = s.toCharArray();
		char[] t_array = t.toCharArray();
		
		int[] map = new int[256];
		
		int end = 0;
		int start = 0;
		int min_length = Integer.MAX_VALUE;
		
		// store t character and its count
		for (int i = 0; i < t_array.length; i++)
			map[t_array[i]]++;
		
		int count = t_array.length;
		int min_start = 0;

		// for those Characters t doesn't have, map[thisCharacter] are at most 0
		while (end < s_array.length) {
			if (map[s_array[end]] > 0) // t has this character
			{
				count--;
			}
			
			map[s_array[end]]--;
			
			while (count == 0) // window matches
			{
				if ((end - start + 1) < min_length) {
					min_length = end - start + 1;
					min_start = start;
				}
				
				map[s_array[start]]++; // start go left
				if (map[s_array[start]] > 0) { // remove a character which t has
					count++; // Cause for those Characters t doesn't have, map[thisCharacter] are at most 0
				}
				start++;
			}
			end++;
		}
		if (min_start + min_length > s_array.length)
			return "";
		return s.substring(min_start, min_start + min_length);
	}

}
