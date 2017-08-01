package OJ0401_0410;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

public class Longest_Palindrome {
	/*
	 * https://discuss.leetcode.com/topic/61454/simple-java-solution
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/61300/simple-hashset-solution-java
	 */
	public int longestPalindrome(String s) {
		if (s == null || s.length() == 0)
			return 0;
		Set<Character> set = new HashSet<>();
		int count = 0;
		char[] chars = s.toCharArray();
		for (char c : chars) {
			if (set.remove(c)) {
				count++;
			} else {
				set.add(c);
			}
		}
		return set.isEmpty() ? count * 2 : count * 2 + 1;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/63963/my-5-line-simple-java-solution
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/61732/simple-int-array-solution-java-5-lines
	 */
	public int longestPalindrome_int_array(String s) {
		int[] count = new int[Math.abs('A' - 'z') + 1];
		int ans = 0;
		for (int i = 0; i < s.length(); i++)
			count[s.charAt(i) - 'A']++;
		for (int n : count)
			ans += n / 2;
		return Math.min(ans * 2 + 1, s.length());
	}
	
	// https://discuss.leetcode.com/topic/65379/java-one-pass-solution
	public int longestPalindrome_onePass_int_array(String s) {
		int[] freq = new int[256];
		int count = 0;
		for (char ch : s.toCharArray()) {
			freq[ch - 'A']++;
			if (freq[ch - 'A'] == 2) {
				count += 2;
				freq[ch - 'A'] = 0;
			}
		}
		if (count < s.length())
			count += 1;
		return count;
	}
	
	// https://discuss.leetcode.com/topic/61442/simple-java-solution-in-one-pass
	public int longestPalindrome_boolean_array(String s) {
        boolean[] map = new boolean[256];
        int len = 0;
        for (char c : s.toCharArray()) {
			map[c] = !map[c];         // flip on each occurrence, false when seen n*2 times
			if (!map[c])
				len += 2;
        }
        if (len < s.length()) len++; // if more than len, atleast one single is present
        return len;
    }
	
	/*
	 * self
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/91600/simple-java-beat-99-67
	 */
	public int longestPalindrome_map(String s) {
        Map<Character, Integer> dict = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            int count = dict.getOrDefault(s.charAt(i), 0);
            dict.put(s.charAt(i), count + 1);
        }
        int odd = 0;
        for (Integer value : dict.values()) {
            if (value % 2 == 1)
                odd++;
        }
        int out = s.length();
        if (odd != 0)
            out = s.length() - odd + 1;
        return out;
    }

}
