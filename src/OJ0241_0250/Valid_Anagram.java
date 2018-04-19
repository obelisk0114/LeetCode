package OJ0241_0250;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

public class Valid_Anagram {
	/*
	 * https://leetcode.com/problems/valid-anagram/discuss/66550/Share-my-java-solution
	 * 
	 * Other code:
	 * https://leetcode.com/problems/valid-anagram/discuss/66484/Accepted-Java-O(n)-solution-in-5-lines
	 * https://leetcode.com/problems/valid-anagram/discuss/66798/My-Java-Solution-(8ms)
	 * https://leetcode.com/problems/valid-anagram/discuss/66758/The-3-ms-fastest-AC-for-alphabets-and-6-ms-universal-AC-for-Unicode-in-Java
	 */
	public boolean isAnagram(String s, String t) {
		if (s.length() != t.length()) {
			return false;
		}
		
		int[] count = new int[26];
		for (int i = 0; i < s.length(); i++) {
			count[s.charAt(i) - 'a']++;
			count[t.charAt(i) - 'a']--;
		}
		for (int i : count) {
			if (i != 0) {
				return false;
			}
		}
		return true;
	}
	
	// by myself
	public boolean isAnagram_self2(String s, String t) {
        if (s.length() != t.length())
            return false;
        
        Map<Character, Integer> map = new HashMap<Character, Integer>();
        for (char c : s.toCharArray()) {
            int val = map.getOrDefault(c, 0);
            map.put(c, val + 1);
        }
        
        for (char c : t.toCharArray()) {
            int val = map.getOrDefault(c, 0);
            if (val == 0)
                return false;
            
            if (val == 1)
                map.remove(c);
            else
                map.put(c, val - 1);
        }
        return true;
    }
	
	// by myself
	public boolean isAnagram_self(String s, String t) {
        Map<Character, Integer> map = new HashMap<Character, Integer>();
        for (char c : s.toCharArray()) {
            int val = map.getOrDefault(c, 0);
            map.put(c, val + 1);
        }
        
        for (char c : t.toCharArray()) {
            int val = map.getOrDefault(c, 0);
            if (val == 0)
                return false;
            
            if (val == 1)
                map.remove(c);
            else
                map.put(c, val - 1);
        }
        return map.isEmpty();
    }
	
	/*
	 * https://leetcode.com/problems/valid-anagram/discuss/66651/Java-solution-using-sort
	 * 
	 * Other code:
	 * https://leetcode.com/problems/valid-anagram/discuss/66776/My-easy-ac-JAVA-solution
	 */
	public boolean isAnagram_sort(String s, String t) {
		char[] sChar = s.toCharArray();
		char[] tChar = t.toCharArray();

		Arrays.sort(sChar);
		Arrays.sort(tChar);

		return Arrays.equals(sChar, tChar);
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/valid-anagram/discuss/66828/3-solutions:-sort-hash-array-and-prime.
	 */
	private final int[] PRIMES = {3, 5, 7, 11 ,13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 
			53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 107};
	public boolean isAnagram_primeHash(String s, String t) {
	    return hash(s) == hash(t);
	}
	private long hash(String s) {
	    long hash = 1;
	    for (int i = 0; i < s.length(); i++) {
	        hash *= PRIMES[s.charAt(i) - 'a'];
	    }
	    return hash;
	}
	
	// https://leetcode.com/problems/valid-anagram/discuss/66490/My-solutions-in-C++-Java-Python-C-C-JavaScript-and-Ruby

}
