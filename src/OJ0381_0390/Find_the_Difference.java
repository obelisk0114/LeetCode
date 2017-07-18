package OJ0381_0390;

import java.util.Arrays;

public class Find_the_Difference {
	/*
	 * https://discuss.leetcode.com/topic/56216/simple-java-solution-using-xor
	 * 
	 * Rf : https://discuss.leetcode.com/topic/55912/java-solution-using-bit-manipulation
	 */
	public char findTheDifference(String s, String t) {
        char c = t.charAt(t.length()-1);
        
        for (int i = 0; i < s.length(); i++) {
            c ^= s.charAt(i) ^ t.charAt(i);
        }
        return c;
    }
	
	/*
	 * https://discuss.leetcode.com/topic/56050/simple-java-8ms-solution-4-lines
	 * 
	 * Rf : https://discuss.leetcode.com/topic/55960/two-java-solutions-using-xor-sum
	 */
	char findTheDifference_sum(String s, String t) {
		int charCode = t.charAt(s.length());
		// Iterate through both strings and char codes
		for (int i = 0; i < s.length(); ++i) {
			charCode -= (int) s.charAt(i);
			charCode += (int) t.charAt(i);
		}
		return (char) charCode;
	}
	
	// self
	public char findTheDifference_sortCharacter(String s, String t) {
        char[] ss = s.toCharArray();
        char[] tt = t.toCharArray();
        Arrays.sort(ss);
        Arrays.sort(tt);
        
        for (int i = 0; i < ss.length; i++) {
            if (ss[i] != tt[i]) {
                return tt[i];
            }
        }
        return tt[tt.length - 1];
    }
	
	// self
	char findTheDifference_arrayMap(String s, String t) {
        int[] letter = new int[26];
        int[] letter2 = new int[26];
        for (int i = 0; i < t.length(); i++) {
            if (i != s.length()) {
                letter[s.charAt(i) - 'a']++;
            }
            letter2[t.charAt(i) - 'a']++;
        }
        
        for (int i = 0; i < 26; i++) {
            if (letter[i] != letter2[i]) {
                return (char)(i + 97);
            }
        }
        return 'A';
    }
	
	// https://discuss.leetcode.com/topic/55987/java-c-1-liner
	char findTheDifference_1_line(String s, String t) {
	    return (char) (s + t).chars().reduce(0, (c, d) -> c ^ d);
	}

}
