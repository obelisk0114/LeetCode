package OJ0201_0210;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

public class Isomorphic_Strings {
	/*
	 * https://leetcode.com/problems/isomorphic-strings/discuss/57802/Java-solution-using-HashMap/59449
	 * 
	 * Two strings are isomorphic if the positions of the characters follow the same 
	 * pattern. So I'm using maps to compare the position patterns. Whether the two 
	 * strings are isomorphic can be judged by the index patterns.
	 */
	public boolean isIsomorphic_Map_position(String s, String t) {
		if (s == null || t == null)
			return false;
		if (s.length() != t.length())
			return false;

		Map<Character, Integer> mapS = new HashMap<Character, Integer>();
		Map<Character, Integer> mapT = new HashMap<Character, Integer>();

		for (int i = 0; i < s.length(); i++) {
			int indexS = mapS.getOrDefault(s.charAt(i), -1);
			int indexT = mapT.getOrDefault(t.charAt(i), -1);

			if (indexS != indexT) {
				return false;
			}

			mapS.put(s.charAt(i), i);
			mapT.put(t.charAt(i), i);
		}

		return true;
	}
	
	/*
	 * https://leetcode.com/problems/isomorphic-strings/discuss/57807/Java-3ms-beats-99.25
	 * 
	 * Since all the test cases use ASCII characters, you can use small arrays as a 
	 * lookup tables. It uses the arrays to mimic two hash table
	 * 
	 * When u encounter the char for the first time then u map it with some char of 
	 * second string, now if u encounter that char for 2nd time then you go and fetch 
	 * the prev mapping and compare it to the current char of second string , if 
	 * (match) then fine else not isomorphic !
	 * 
	 * Rf :
	 * https://leetcode.com/problems/isomorphic-strings/discuss/57807/Java-3ms-beats-99.25/140379
	 * https://leetcode.com/problems/isomorphic-strings/discuss/57807/Java-3ms-beats-99.25/59464
	 */
	public boolean isIsomorphic_array(String sString, String tString) {
		char[] s = sString.toCharArray();
		char[] t = tString.toCharArray();

		int length = s.length;
		if (length != t.length)
			return false;

		char[] sm = new char[256];
		char[] tm = new char[256];

		for (int i = 0; i < length; i++) {
			char sc = s[i];
			char tc = t[i];
			if (sm[sc] == 0 && tm[tc] == 0) {
				sm[sc] = tc;
				tm[tc] = sc;
			} 
			else {
				if (sm[sc] != tc || tm[tc] != sc) {
					return false;
				}
			}
		}
		return true;
	}
	
	/*
	 * https://leetcode.com/problems/isomorphic-strings/discuss/57810/Short-Java-solution-without-maps
	 * 
	 * The main idea is to store the last seen positions of current (i-th) characters 
	 * in both strings. If previously stored positions are different then we know that 
	 * the fact they're occurring in the current i-th position simultaneously is a 
	 * mistake.
	 * 
	 * As the default value of array is zero, we cannot distinguish default value and 
	 * the last seen position if the last seen position is at 0. So, we should use 
	 * one-based index instead of zero-based. As array uses zero-based index, we 
	 * should use i+1 as the last seen position.
	 * 
	 * Rf : https://leetcode.com/problems/isomorphic-strings/discuss/57810/Short-Java-solution-without-maps/59485
	 * 
	 * Other code:
	 * https://leetcode.com/problems/isomorphic-strings/discuss/57810/Short-Java-solution-without-maps/59484
	 */
	public boolean isIsomorphic_one_array(String s1, String s2) {
		int[] m = new int[512];
		for (int i = 0; i < s1.length(); i++) {
			if (m[s1.charAt(i)] != m[s2.charAt(i) + 256])
				return false;
			
			m[s1.charAt(i)] = m[s2.charAt(i) + 256] = i + 1;
		}
		return true;
	}
	
	// by myself
	public boolean isIsomorphic_self2(String s, String t) {
        if (s == null && t == null)
            return true;
        if (s == null || t == null)
            return false;
        if (s.length() == 0 && t.length() == 0)
            return true;
        if (s.length() != t.length())
            return false;
        
        Map<Character, Character> map = new HashMap<>();
        Set<Character> set = new HashSet<>();
        for (int i = 0; i < s.length(); i++) {
            char cS = s.charAt(i);
            char cT = t.charAt(i);
            if (map.containsKey(cS)) {
                if (map.get(cS) != cT) {
                    return false;
                }
            }
            else {
                if (set.contains(cT)) {
                    return false;
                }
                else {
                    map.put(cS, cT);
                    set.add(cT);
                }
            }
        }
        return true;
    }
	
	/*
	 * by myself
	 * 
	 * Other code:
	 * https://leetcode.com/problems/isomorphic-strings/discuss/57802/Java-solution-using-HashMap
	 * https://leetcode.com/problems/isomorphic-strings/discuss/58067/AC-HashMap-Clear-code-(JAVA)
	 */
	public boolean isIsomorphic_self1(String s, String t) {
        if (s == null && t == null)
            return true;
        if (s == null || t == null)
            return false;
        if (s.length() == 0 && t.length() == 0)
            return true;
        if (s.length() != t.length())
            return false;
        
        Map<Character, Character> map = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            char cS = s.charAt(i);
            char cT = t.charAt(i);
            if (map.containsKey(cS)) {
                if (map.get(cS) != cT) {
                    return false;
                }
            }
            else {
                if (map.containsValue(cT)) {
                    return false;
                }
                else {
                    map.put(cS, cT);
                }
            }
        }
        return true;
    }
	
	/*
	 * https://leetcode.com/problems/isomorphic-strings/discuss/57874/Java-solution-with-1-line-core-code
	 * 
	 * Map.put return: the previous value associated with key, or null if there was 
	 * no mapping for key. 
	 * 
	 * When you put int in the two maps, two Integer objects are created from it 
	 * (or taken from that cache), and they might not be the same.
	 * 
	 * Rf :
	 * https://docs.oracle.com/javase/7/docs/api/java/util/HashMap.html#put(K,%20V)
	 * https://leetcode.com/problems/isomorphic-strings/discuss/57874/Java-solution-with-1-line-core-code/59513
	 * https://leetcode.com/problems/isomorphic-strings/discuss/57874/Java-solution-with-1-line-core-code/59518
	 * 
	 * Other code:
	 * https://leetcode.com/problems/isomorphic-strings/discuss/57809/5-lines-simple-Java
	 */
	public boolean isIsomorphic_put_return(String s1, String s2) {
		Map<Character, Integer> m1 = new HashMap<>();
		Map<Character, Integer> m2 = new HashMap<>();

		for (Integer i = 0; i < s1.length(); i++) {
			if (m1.put(s1.charAt(i), i) != m2.put(s2.charAt(i), i)) {
				return false;
			}
		}
		return true;
	}
		
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/isomorphic-strings/discuss/57941/Python-different-solutions-(dictionary-etc).
     * https://leetcode.com/problems/isomorphic-strings/discuss/57861/1-line-Python-Solution-95
     * https://leetcode.com/problems/isomorphic-strings/discuss/57838/1-line-in-Python
     * https://leetcode.com/problems/isomorphic-strings/discuss/57892/1-liner-in-Python
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/isomorphic-strings/discuss/57796/My-6-lines-solution
     * https://leetcode.com/problems/isomorphic-strings/discuss/57996/4ms-Accept-C-code
     * https://leetcode.com/problems/isomorphic-strings/discuss/58104/C%2B%2B-8ms-simple-solution
     */

}
