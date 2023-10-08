package OJ0281_0290;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

public class Word_Pattern {
	/*
	 * Rf :
	 * https://leetcode.com/problems/isomorphic-strings/discuss/57802/Java-solution-using-HashMap/59449
	 * https://leetcode.com/problems/isomorphic-strings/discuss/57874/Java-solution-with-1-line-core-code
	 * 
	 * Using maps to compare the position patterns.
	 */
	public boolean wordPattern_self_modify(String pattern, String str) {
        String[] strs = str.split(" ");
		if (strs.length != pattern.length())
			return false;
        
        Map<Character, Integer> patternMap = new HashMap<>();
		Map<String, Integer> strMap = new HashMap<>();
        
        for (int i = 0; i < strs.length; i++) {
            int indexPattern = patternMap.getOrDefault(pattern.charAt(i), -1);
            int indexStr = strMap.getOrDefault(strs[i], -1);
            
            if (indexPattern != indexStr)
                return false;
            
            patternMap.put(pattern.charAt(i), i);
            strMap.put(strs[i], i);
        }
        return true;
    }
	
	/*
	 * by myself
	 * 
	 * Other code:
	 * https://leetcode.com/problems/word-pattern/discuss/73533/Java-Solution-with-a-hashmap-and-a-hashset
	 * https://leetcode.com/problems/word-pattern/discuss/73654/Easy-to-understand-JAVA-solution-with-explanation
	 */
	public boolean wordPattern_self(String pattern, String str) {
        if (pattern == null && str == null)
            return true;
        if (pattern == null || str == null)
            return false;
        if (pattern.length() == 0 && str.length() == 0)
            return true;
        
        Map<String, Character> map = new HashMap<>();
        Set<Character> set = new HashSet<>();
        String[] strs = str.split(" ");
        if (pattern.length() != strs.length)
            return false;
        
        for (int i = 0; i < pattern.length(); i++) {
            String s = strs[i];
            if (map.containsKey(s)) {
                if (map.get(s) != pattern.charAt(i)) {
                    return false;
                }
            }
            else {
                if (set.contains(pattern.charAt(i))) {
                    return false;
                }
                else {
                    map.put(s, pattern.charAt(i));
                    set.add(pattern.charAt(i));
                }
            }
        }
        return true;
	}

	/*
	 * Modified from
	 * https://leetcode.com/problems/isomorphic-strings/discuss/57874/Java-solution-with-1-line-core-code
	 * 
	 * Switched from "int i = 0..." to the current version with i being an Integer 
	 * object, which allows to compare with just != because there's no 
	 * autoboxing-same-value-to-different-objects-problem anymore.
	 * 
	 * JVM is caching Integer values. == only works for numbers between -128 and 127
	 * 
	 * Rf :
	 * https://leetcode.com/problems/word-pattern/discuss/73402/8-lines-simple-Java
	 * https://leetcode.com/problems/word-pattern/discuss/73402/8-lines-simple-Java/76334
	 * https://leetcode.com/problems/word-pattern/discuss/73402/8-lines-simple-Java/76352
	 * 
	 * Other code:
	 * https://leetcode.com/problems/word-pattern/discuss/73547/JAVA-2MS-solution-hasmap
	 */
	public boolean wordPattern(String pattern, String str) {
		String[] strs = str.split(" ");
		if (strs.length != pattern.length())
			return false;
		
		Map<Character, Integer> patternMap = new HashMap<>();
		Map<String, Integer> strMap = new HashMap<>();
		for (Integer i = 0; i < pattern.length(); i++) {
			if (patternMap.put(pattern.charAt(i), i) != strMap.put(strs[i], i)) {
				return false;
			}
		}
		return true;
	}
	
	/*
	 * https://leetcode.com/problems/word-pattern/discuss/73399/Very-fast-(3ms)-Java-Solution-using-HashMap/76280
	 * 
	 * To avoid map.containsValue(arr[i]), you can add an extra map to save 
	 * <String, Character>
	 * 
	 * Other code:
	 * https://leetcode.com/problems/word-pattern/discuss/73399/Very-fast-(3ms)-Java-Solution-using-HashMap/304903
	 */
	public boolean wordPattern_pair_HashMap(String pattern, String str) {
		String[] strs = str.split(" ");
		if (pattern.length() != strs.length)
			return false;

		HashMap<Character, String> hm1 = new HashMap<Character, String>();
		HashMap<String, Character> hm2 = new HashMap<String, Character>();
		for (int i = 0; i < pattern.length(); ++i) {
			if (hm1.containsKey(pattern.charAt(i))) {
				if (!hm1.get(pattern.charAt(i)).equals(strs[i]))
					return false;
			} 
			else {
				if (hm2.containsKey(strs[i]))
					return false;
				else {
					hm1.put(pattern.charAt(i), strs[i]);
					hm2.put(strs[i], pattern.charAt(i));
				}
			}
		}
		return true;
	}
	
	// https://leetcode.com/problems/word-pattern/discuss/73399/Very-fast-(3ms)-Java-Solution-using-HashMap
	public boolean wordPattern_containsValue(String pattern, String str) {
		String[] arr = str.split(" ");
		HashMap<Character, String> map = new HashMap<Character, String>();
		if (arr.length != pattern.length())
			return false;
		
		for (int i = 0; i < arr.length; i++) {
			char c = pattern.charAt(i);
			if (map.containsKey(c)) {
				if (!map.get(c).equals(arr[i]))
					return false;
			} 
			else {
				if (map.containsValue(arr[i]))
					return false;
				
				map.put(c, arr[i]);
			}
		}
		return true;
	}
	
	// https://leetcode.com/problems/word-pattern/discuss/73414/1ms-Java-solution-beats-97-two-pointers-string-array-well-commented
	public boolean wordPattern_string_array(String pattern, String str) {
		if (pattern == null || str == null) {
			return false;
		}
		
		Set<String> usedWords = new HashSet<>();
		String[] map = new String[256];
		
		// as a cursor to traverse pattern
		int i = 0;
		
		// to denote the beginning char of current word in str
		int start = 0;
		
		// as a cursor to traverse str
		int j = 0;
		
		while (i < pattern.length() && j < str.length()) {
			// find a new word, i.e. str[start->j-1]
			while (j < str.length() && str.charAt(j) != ' ') {
				j++;
			}
			String word = j == str.length() ? 
					str.substring(start) : str.substring(start, j);
			
			// check and return false if :
			// 1. this word has been used for another character,
			// or 2. this word is not a match to previous mapping for this character
			if (map[pattern.charAt(i)] == null) {
				if (usedWords.contains(word)) {
					return false;
				} 
				else {
					map[pattern.charAt(i)] = word;
					usedWords.add(word);
				}
			} 
			else if (!map[pattern.charAt(i)].equals(word)) {
				return false;
			}
			
			i++;
			j++;
			start = j;
		}
		
		// requires that when the while loop is finished, indices i and j should both 
		// be out of bound
		return i >= pattern.length() && j >= str.length();
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/word-pattern/discuss/73599/Share-my-python-solution-with-two-dictionaries
     * https://leetcode.com/problems/word-pattern/discuss/73433/Short-in-Python
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/word-pattern/discuss/73409/Short-C%2B%2B-read-words-on-the-fly
     * https://leetcode.com/problems/word-pattern/discuss/73434/0ms-C%2B%2B-solution-using-istringstream-and-double-maps
     * https://leetcode.com/problems/word-pattern/discuss/73519/Haven't-seen-a-C-solution-post-mine-here
     */

}
