package OJ0561_0570;

import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

public class Permutation_in_String {
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/permutation-in-string/discuss/102588/Java-Solution-Sliding-Window
	 * 
	 * We just need to create a sliding window with length of s1, move from beginning 
	 * to the end of s2. When a character moves in from right of the window, we 
	 * subtract 1 to that character count from the map. When a character moves out 
	 * from left of the window, we add 1 to that character count. So once we see all 
	 * zeros in the map, meaning equal numbers of every characters between s1 and the 
	 * substring in the sliding window.
	 */
	public boolean checkInclusion(String s1, String s2) {
		int len1 = s1.length(), len2 = s2.length();
		if (len1 > len2)
			return false;

		int[] count = new int[26];
		for (int i = 0; i < len1; i++) {
			count[s1.charAt(i) - 'a']++;
			count[s2.charAt(i) - 'a']--;
		}
		if (allZero(count))
			return true;

		for (int i = len1; i < len2; i++) {
			count[s2.charAt(i) - 'a']--;
			count[s2.charAt(i - len1) - 'a']++;
			if (allZero(count))
				return true;
		}

		return false;
	}
	private boolean allZero(int[] count) {
		for (int i = 0; i < 26; i++) {
			if (count[i] != 0)
				return false;
		}
		return true;
	}
	
	/*
	 * https://leetcode.com/problems/permutation-in-string/discuss/117839/Java-O(n)-time-and-O(1)-space
	 * 
	 * Other code:
	 * https://leetcode.com/problems/permutation-in-string/discuss/102640/Simple-and-clear-Java-solution
	 */
	public boolean checkInclusion_2_array(String s1, String s2) {
		if (s1.length() > s2.length()) {
			return false;
		}
		
		int[] s1Freq = new int[26];
		Arrays.fill(s1Freq, 0);
		for (int i = 0; i < s1.length(); i++) {
			char c = s1.charAt(i);
			s1Freq[c - 'a'] += 1;
		}
		
		int[] runningFreq = new int[26];
		Arrays.fill(runningFreq, 0);
		for (int i = 0; i < s1.length(); i++) {
			char c = s2.charAt(i);
			runningFreq[c - 'a'] += 1;
		}
		
		if (Arrays.equals(runningFreq, s1Freq)) {
			return true;
		}
		
		for (int i = s1.length(); i < s2.length(); i++) {
			char cToAdd = s2.charAt(i);
			char cToRemove = s2.charAt(i - s1.length());
			runningFreq[cToAdd - 'a'] += 1;
			runningFreq[cToRemove - 'a'] -= 1;
			if (Arrays.equals(runningFreq, s1Freq)) {
				return true;
			}
		}
		return false;
	}
	
	/*
	 * https://leetcode.com/problems/permutation-in-string/discuss/102590/8-lines-slide-window-solution-in-Java
	 * 
	 * The first "for" loop counts all chars we need to find in a way like digging 
	 * holes on the ground. We scan each one char of the string s2 
	 * (by moving index r in above code) and put it in the right hole:
	 * 
	 * Any time we encounter a sticking out block - meaning a block with value 1 - 
	 * we stop scanning (that is moving "r"). Now, we have an invalid substring with 
	 * either invalid char or invalid number of chars.
	 * 
	 * We use a left index ("l" in above code) to remove chars in the holes in the 
	 * same order we filled them into the holes. We stop removing chars until the only 
	 * sticking out block is fixed - it has a value of 0 after fixing.
	 * 
	 * Other code:
	 * https://leetcode.com/problems/permutation-in-string/discuss/102642/Java-Solution-Two-pointers
	 */
	public boolean checkInclusion2(String s1, String s2) {
		int[] count = new int[128];
		for (int i = 0; i < s1.length(); i++)
			count[s1.charAt(i)]--;
		
		for (int l = 0, r = 0; r < s2.length(); r++) {
			if (++count[s2.charAt(r)] > 0)
				while (--count[s2.charAt(l++)] != 0) {
					/* do nothing */
				}
			else if ((r - l + 1) == s1.length())
				return true;
		}
		return s1.length() == 0;
	}
	
	/*
	 * by myself
	 * 
	 * Other code: 
	 * https://leetcode.com/problems/permutation-in-string/discuss/102598/Sliding-Window-in-Java-very-similar-to-Find-All-Anagrams-in-a-String
	 */
	public boolean checkInclusion_self2(String s1, String s2) {
        Map<Character, Integer> map = new HashMap<Character, Integer>();
        for (int i = 0; i < s1.length(); i++) {
            int val = map.getOrDefault(s1.charAt(i), 0);
            map.put(s1.charAt(i), val + 1);
        }
        
        int count = 0;
        int first = -1;
        for (int i = 0; i < s2.length(); i++) {
            char c = s2.charAt(i);
            if (map.containsKey(c)) {
                if (count == 0)
                    first = i;
                
                int val = map.get(c);
                if (val > 0) {
                    map.put(c, val - 1);
                    count++;
                    
                    if (count == s1.length())
                        return true;
                }
                else {
                    while (s2.charAt(first) != c) {
                        char cur = s2.charAt(first);
                        map.put(cur, map.get(cur) + 1);
                        first++;
                    }
                    count = i - first;
                    first++;
                }
            }
            else {
                count = 0;
                while (first != -1 && first < i) {
                    char cur = s2.charAt(first);
                    map.put(cur, map.get(cur) + 1);
                    first++;
                }
                first = -1;
            }
        }
        return false;
    }
	
	// by myself
	public boolean checkInclusion_self(String s1, String s2) {
        Map<Character, Integer> map = new HashMap<Character, Integer>();
        for (int i = 0; i < s1.length(); i++) {
            int val = map.getOrDefault(s1.charAt(i), 0);
            map.put(s1.charAt(i), val + 1);
        }
        
        int count = 0;
        int first = -1;
        Map<Character, LinkedList<Integer>> process = new HashMap<>();
        for (int i = 0; i < s2.length(); i++) {
            char c = s2.charAt(i);
            if (map.containsKey(c)) {
                if (count == 0)
                    first = i;
                
                LinkedList<Integer> list = process.getOrDefault(c, new LinkedList<Integer>());
                if (map.get(c) > list.size()) {
                    list.add(i);
                    process.put(c, list);
                    count++;
                    
                    if (count == s1.length())
                        return true;
                }
                else {
                    int remove = list.removeFirst();
                    count = i - remove;
                    list.add(i);
                    //process.put(c, list);
                    
                    while (first < remove) {
                        char cur = s2.charAt(first);
                        LinkedList<Integer> list2 = process.get(cur);
                        list2.pollFirst();
                        //process.put(cur, list2);
                        first++;
                    }
                    first++;
                }
            }
            else {
                count = 0;
                first = -1;
                process.clear();
            }
        }
        return false;
    }

}
