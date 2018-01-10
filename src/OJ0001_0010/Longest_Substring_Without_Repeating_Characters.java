package OJ0001_0010;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

public class Longest_Substring_Without_Repeating_Characters {
	/*
	 * https://discuss.leetcode.com/topic/8232/11-line-simple-java-solution-o-n-with-explanation
	 * 
	 * Keep a hashmap which stores the characters in string as keys and their 
	 * positions as values, and keep two pointers which define the max substring. 
	 * Move the right pointer to scan through the string , and meanwhile update the 
	 * hashmap. If the character is already in the hashmap, then move the left 
	 * pointer to the right of the same character last found. 
	 * Note that the two pointers can only move forward.
	 * 
	 * Rf :
	 * https://discuss.leetcode.com/topic/45105/easy-java-solution-5ms
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/26483/o-n-time-o-1-space-solution-using-kadane-s-algo-in-java
	 * https://discuss.leetcode.com/topic/27794/simple-java-solution
	 */
	public int lengthOfLongestSubstring(String s) {
		if (s.length() == 0)
			return 0;
		
		HashMap<Character, Integer> map = new HashMap<Character, Integer>();
		int max = 0;
		for (int i = 0, j = -1; i < s.length(); ++i) {
			if (map.containsKey(s.charAt(i))) {
				j = Math.max(j, map.get(s.charAt(i)));
			}
			map.put(s.charAt(i), i);
			max = Math.max(max, i - j);
		}
		return max;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/25499/share-my-java-solution-using-hashset
	 * 
	 * Use a hash set to track the longest substring without repeating characters 
	 * so far, use a fast pointer j to see if character j is in the hash set or not, 
	 * if not, great, add it to the hash set, move j forward and update the max 
	 * length, otherwise, delete from the head by using a slow pointer i until we can 
	 * put character j to the hash set.
	 * 
	 * Rf : https://leetcode.com/articles/longest-substring-without-repeating-characters/
	 */
	public int lengthOfLongestSubstring_set_remove(String s) {
		int i = 0, j = 0, max = 0;
		Set<Character> set = new HashSet<>();

		while (j < s.length()) {
			if (!set.contains(s.charAt(j))) {
				set.add(s.charAt(j++));
				max = Math.max(max, set.size());
			} 
			else {
				set.remove(s.charAt(i++));
			}
		}

		return max;
	}
	
	// by myself version 2
	public int lengthOfLongestSubstring_self2(String s) {
        int max = 0;
        int now = 0;
        int start = 0;
        Map<Character, Integer> map = new HashMap<>();
        char[] chArray = s.toCharArray();
        for (int i = 0; i < chArray.length; i++) {
            char ch = chArray[i];
            if (!map.containsKey(ch)) {
                now++;
                map.put(ch, i);
                max = Math.max(max, now);
            }
            else {
                int last = map.get(ch);
                if (last < start) {
                    now++;
                    map.put(ch, i);
                    max = Math.max(max, now);
                    continue;
                }
                now = i - last;
                map.put(ch, i);
                start = last + 1;
            }
        }
        return max;
    }
	
	// https://discuss.leetcode.com/topic/22048/my-easy-solution-in-java-o-n
	public int lengthOfLongestSubstring_remove_array(String s) {
		int[] mOccur = new int[256];
		int maxL = 0;
		for (int i = 0, j = 0; i < s.length(); ++i) {
			char ch = s.charAt(i);
			++mOccur[ch];
			while (mOccur[ch] > 1) {
				--mOccur[s.charAt(j++)];
			}
			maxL = Math.max(maxL, i - j + 1);
		}
		return maxL;
	}
	
	// by myself version 1
	public int lengthOfLongestSubstring_self1(String s) {
        int max = 0;
        int now = 0;
        int start = 0;
        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (!map.containsKey(ch)) {
                now++;
                map.put(ch, i);
                if (now > max) {
                    max = now;
                    /*
                    System.out.println("i = " + i + " ; max = " + max);
                    for (Map.Entry<Character, Integer> entry : map.entrySet()) {
                        System.out.print(entry.getKey() + " : " + entry.getValue());
                        System.out.println();
                    }
                    */
                }
            }
            else {
                int last = map.get(ch);
                now = i - last;
                for (int j = start; j < last; j++) {
                    map.remove(s.charAt(j));
                }
                map.put(ch, i);
                start = last + 1;
            }
        }
        return max;
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Longest_Substring_Without_Repeating_Characters longestNoRepeat = 
				new Longest_Substring_Without_Repeating_Characters();
		String s1 = "";                // 0
		String s2 = "dvdf";            // 3
		String s3 = "tmmzuxt";         // 5
		String s4 = "kdgjkjhglfp";     // 7
		
		System.out.println("s1 = " + longestNoRepeat.lengthOfLongestSubstring_self1(s1));
		System.out.println("s2 = " + longestNoRepeat.lengthOfLongestSubstring_self1(s2));
		System.out.println("s3 = " + longestNoRepeat.lengthOfLongestSubstring_self1(s3));
		System.out.println("s4 = " + longestNoRepeat.lengthOfLongestSubstring_self1(s4));

	}

}
