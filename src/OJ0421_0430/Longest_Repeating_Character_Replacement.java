package OJ0421_0430;

import java.util.Map;
import java.util.HashMap;

public class Longest_Repeating_Character_Replacement {
	/*
	 * Modified by myself
	 * 
	 * 使用 sliding window 來尋找。map 是當前 window 中所有 character 各自對應的數量
	 * window size = 次數最多的 character 的數量  + k
	 * right 一直走，直到 window size = right - left + 1 > k + maxCount，表示 invalid
	 * 
	 * 這裡的 maxCount 是 [0, right] 所有 character 的最大數量
	 * 不一定是當前 window 中 character 的最大數量
	 * maxCount 每次都和現在 character 在當前 window 中的數量做比較
	 * 因此 maxCount 每次更新都表示當前 window 中數量最多的 character 的數量
	 * 
	 * 因為我們要找最大的 window size，所以不用 while 來 shrink，使用 if 來平移整個 sliding window
	 * 平移後的 sliding window 有可能 invalid，但是不重要
	 * 因為 window size 若是繼續 expand 則表示 valid
	 * 換言之，若 windowSize 被更新過，則表示有某一段 valid substring 滿足條件來更新過
	 * 
	 * 在 if 內不用修改、重設 maxCount 
	 * 因為當 window 是 valid 時， window size = k + maxCount
	 * 在 k 不變下，只有 maxCount 增加才會更新 window size
	 * 因此不用特地減少 maxCount
	 * 只要外面 maxCount 因為 window 出現數量更多的 character 而更新時，之後才會在後面更新 window size
	 * 
	 * 若用 if 表示 window 只會平移，不會 shrink
	 * 因此最大的 window size 其實是 s.length() - left
	 * 
	 * Rf :
	 * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/91271/Java-12-lines-O(n)-sliding-window-solution-with-explanation/95833
	 * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/91271/Java-12-lines-O(n)-sliding-window-solution-with-explanation/95815
	 * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/591288/Java-O(n)-solution-using-Sliding-Window-with-Explanation
	 * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/91271/Java-12-lines-O(n)-sliding-window-solution-with-explanation/137008
	 */
	public int characterReplacement_sliding_window_HashMap_self(String s, int k) {
		Map<Character, Integer> map = new HashMap<>();
		
		int left = 0;
		int maxCount = 0;
		int windowSize = 0;
		
		for (int right = 0; right < s.length(); right++) {
			int count = map.getOrDefault(s.charAt(right), 0);
			map.put(s.charAt(right), count + 1);
			
			maxCount = Math.max(maxCount, map.get(s.charAt(right)));
			
			if (right - left + 1 - maxCount > k) {
				int val = map.get(s.charAt(left));
				map.put(s.charAt(left), val - 1);
				left++;
			}
			
			windowSize = Math.max(windowSize, right - left + 1);
		}
		
		// 其實可以用 s.length() - left
		return windowSize;
	}
	
	/*
	 * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/91271/Java-12-lines-O(n)-sliding-window-solution-with-explanation
	 * 
	 * The initial step is to extend the window to the longest we can get to with 
	 * maximum number of modifications. Until then start will remain at 0.
	 * 
	 * Then as end increase, the whole substring from 0 to end will violate the rule, 
	 * so we need to update start accordingly (slide the window). We move start to the 
	 * right until the whole string satisfy the constraint again. Then each time we 
	 * reach such situation, we update our max length.
	 * 
	 * The length of valid substring = Max Occurrence + k
	 * We only need to update max occurrence when it becomes larger, because only that 
	 * can we generate a longer valid substring.
	 * If we can't surpass the historic max occurrence, then we can not generate a 
	 * longer valid substring from current "start", I mean the new windows's left 
	 * bound. To some extend, this becomes a game to find max occurrence.
	 * 
	 * maxCount may be invalid at some points, but this doesn't matter, because it was 
	 * valid earlier in the string, and all that matters is finding the max window 
	 * that occurred anywhere in the string. Additionally, it will expand if and only 
	 * if enough repeating characters appear in the window to make it expand. So 
	 * whenever it expands, it's a valid expansion.
	 * 
	 * Since we are only interested in the longest valid substring, our sliding 
	 * windows need not shrink, even if a window may cover an invalid substring. We 
	 * either grow the window by appending one char on the right, or shift the whole 
	 * window to the right by one. And we only grow the window when the count of the 
	 * new char exceeds the historical max count (from a previous window that covers 
	 * a valid substring).
	 * 
	 * That is, we do not need the accurate max count of the current window; we only 
	 * care if the max count exceeds the historical max count; and that can only 
	 * happen because of the new char.
	 * 
	 * When it became invalid, we shift the whole window rightwards by one unit. So 
	 * that the length is unchanged. Because any index smaller than original "start", 
	 * will never have the chance to lead a longer valid substring than current length 
	 * of our window.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/91271/Java-12-lines-O(n)-sliding-window-solution-with-explanation/95833
	 * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/91271/Java-12-lines-O(n)-sliding-window-solution-with-explanation/137008
	 * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/91271/Java-12-lines-O(n)-sliding-window-solution-with-explanation/95815
	 * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/91271/Java-12-lines-O(n)-sliding-window-solution-with-explanation/140492
	 * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/91271/Java-12-lines-O(n)-sliding-window-solution-with-explanation/95828
	 * 
	 * Other code:
	 * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/641911/Java-O(n)-Easy-To-understand-Also-answering-a-hidden-question.
	 */
	public int characterReplacement_sliding_window_while(String s, int k) {
		int len = s.length();
		
		int[] count = new int[26];
		int start = 0, maxCount = 0, maxLength = 0;
		
		for (int end = 0; end < len; end++) {
			maxCount = Math.max(maxCount, ++count[s.charAt(end) - 'A']);
			
			while (end - start + 1 - maxCount > k) {
				count[s.charAt(start) - 'A']--;
				start++;
			}
			
			maxLength = Math.max(maxLength, end - start + 1);
		}
		return maxLength;
	}
	
	/*
	 * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/358879/Java-Solution-Explained-and-Easy-to-Understand-for-Interviews
	 * 
	 * To find out the lettersToReplace = (right - left + 1) - mostFreqLetter;
	 * Take the size of the sliding window minus the most freq letter that is in the 
	 * current window.
	 * 
	 * if lettersToReplace > k than the window is invalid and we decrease the window 
	 * size from the left.
	 * 
	 * maxCount may be invalid at some points, but this doesn't matter, because it was 
	 * valid earlier in the string, and all that matters is finding the max window 
	 * that occurred anywhere in the string. Additionally, it will expand if and only 
	 * if enough repeating characters appear in the window to make it expand. So 
	 * whenever it expands, it's a valid expansion.
	 *
	 * Only when we finally reach a possible sliding window that is 1 or more elements 
	 * greater than our prev max_count, do we fulfill the condition and update our 
	 * answer.
	 * 
	 * If lettersToChange > k, we got an invalid window, we should skip it until 
	 * window is valid again, but only expands window size, never shrink (because 
	 * even if we got a smaller window thats valid, it doesn't matter because we 
	 * already found a window thats bigger and valid)
	 * 
	 * test case:
	 * 
	 * S="AAEBAE" and k=2, we increase the window till right reaches end of the string 
	 * and left will increment from 0 to 2. So while loop executes twice to be valid.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/91271/Java-12-lines-O(n)-sliding-window-solution-with-explanation/137008
	 * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/91311/Sliding-Window-Java-Easy-Explanation-15-lines/95890
	 * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/91301/Awesome-python-solution/550192
	 * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/181382/Java-Sliding-Window-with-Explanation
	 * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/181382/Java-Sliding-Window-with-Explanation/371945
	 * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/91272/Consise-Python-sliding-window/306764
	 * 
	 * Other code:
	 * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/503011/Java-sliding-window-O(n)-with-detailed-explanation
	 */
	public int characterReplacement_sliding_window2(String s, int k) {
		int[] freq = new int[26];
		int mostFreqLetter = 0;
		int left = 0;
		int max = 0;

		for (int right = 0; right < s.length(); right++) {
			freq[s.charAt(right) - 'A']++;
			mostFreqLetter = Math.max(mostFreqLetter, freq[s.charAt(right) - 'A']);

			int lettersToChange = (right - left + 1) - mostFreqLetter;
			if (lettersToChange > k) {
				freq[s.charAt(left) - 'A']--;
				left++;
			}

			max = Math.max(max, right - left + 1);
		}
        
        return max;
    }
	
	/*
	 * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/591288/Java-O(n)-solution-using-Sliding-Window-with-Explanation
	 * 
	 * Key Idea: Adding characters to the window and use the map to track the number 
	 * of dominant char(meaning the character that counts the most in the window). 
	 * Expanding the window as wide as it can be until 
	 * window size - number of dominant character > k which means there are at least 
	 * k characters are not same as the dominant character, so we need shrink the 
	 * window from the left side and also update the character count in the map.
	 * 
	 * One key point that causes confusion is when we remove the left side character 
	 * in the map, the maxRepeat becomes inaccurate. But in this particular case, we 
	 * do not care about the maxRepeat gets smaller because it won't affect the max 
	 * window size, we only care about when maxRepeat gets greater.
	 * 
	 * Only when we finally reach a possible sliding window that is 1 or more elements 
	 * greater than our prev max_count, do we fulfill the condition and update our 
	 * answer.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/91301/Awesome-python-solution/550192
	 */
	public int characterReplacement_sliding_window_HashMap(String s, int k) {
		Map<Character, Integer> map = new HashMap<>();

		int left = 0, maxRepeat = 0, maxWindow = 0;

		for (int right = 0; right < s.length(); right++) {
			char ch = s.charAt(right);
			if (!map.containsKey(ch)) {
				map.put(ch, 0);
			}
			map.put(ch, map.get(ch) + 1);

			// IMPORTANT: maxRepeat is not the accurate number of dominant character, 
			// It is the historical maximum count
			// We do not care about it because unless it gets greater, it won't affect 
			// our final max window size.
			maxRepeat = Math.max(maxRepeat, map.get(ch));

			if (right - left + 1 - maxRepeat > k) {
				char remove = s.charAt(left);
				map.put(remove, map.get(remove) - 1);
				left++;
			}

			maxWindow = Math.max(maxWindow, right - left + 1);
		}

		return maxWindow;
	}
	
	/*
	 * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/278271/JavaC%2B%2BPython-Sliding-Window-O(N)
	 * 
	 * j = n in the end, and j - i is the size of sliding window.
	 * 
	 * It never shrinks the window, the window size at the end is the biggest.
	 * 
	 * If lettersToChange > k, we got an invalid window, we should skip it until 
	 * window is valid again, but only expands window size, never shrink (because 
	 * even if we got a smaller window thats valid, it doesn't matter because we 
	 * already found a window thats bigger and valid)
	 * 
	 * If s[start] is the majority character, we won't get a better result if the new 
	 * max_occur doesn't exceed the former one. Window size is increased only because 
	 * we end++ every time. So if the new character coming into the window (s[end+1]) 
	 * is not the majority character, we won’t get a qualified sequence since we 
	 * introduce a new character. So we will only get a new result if the majority 
	 * character count increase further or there comes a new majority character and 
	 * its count exceeds the former one.
	 * 
	 * Only when we finally reach a possible sliding window that is 1 or more elements 
	 * greater than our prev max_count, do we fulfill the condition and update our 
	 * answer.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/91285/Sliding-window-similar-to-finding-longest-substring-with-k-distinct-characters/95868
	 * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/181382/Java-Sliding-Window-with-Explanation
	 * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/278271/JavaC++Python-Sliding-Window-O(N)/761367
	 * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/91271/Java-12-lines-O(n)-sliding-window-solution-with-explanation/468847
	 * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/91301/Awesome-python-solution/550192
	 * 
	 * Other code:
	 * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/865723/Simple-java-solution
	 */
	public int characterReplacement_sliding_window_sub(String s, int k) {
		int maxf = 0, i = 0, n = s.length(), count[] = new int[26];
		for (int j = 0; j < n; ++j) {
			maxf = Math.max(maxf, ++count[s.charAt(j) - 'A']);
			
			if (j - i + 1 > maxf + k)
				--count[s.charAt(i++) - 'A'];
		}
		return n - i;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/91286/Java-Sliding-Window-Easy-to-Understand/192717
	 * 
	 * based on basic classic sliding window
	 * 
	 * Each time we count the different characters. If it is bigger than k we 
	 * shrink the sliding window.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/91286/Java-Sliding-Window-Easy-to-Understand
	 */
	public int characterReplacement_sliding_count_max2(String s, int k) {
		if (s == null || s.length() == 0) {
			return 0;
		}
		
		int max = 0;
		int[] counts = new int[26];
		int start = 0;
		
		char[] str = s.toCharArray();
		for (int end = 0; end < str.length; end++) {
			counts[str[end] - 'A']++;

			// shrink
			while (diffHelper_sliding_count_max2(counts) > k) {
				counts[str[start] - 'A']--;
				start++;
			}

			max = Math.max(max, end - start + 1); // update max anyway
		}
		return max;
	}

	public int diffHelper_sliding_count_max2(int[] ch) {
		int max = 0;
		int sum = 0;
		
		for (int val : ch) {
			sum += val;
			max = Math.max(max, val);
		}

		return sum - max;
	}

	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/91286/Java-Sliding-Window-Easy-to-Understand
	 * 
	 * Each time we count the different characters. If it is not bigger than k we 
	 * extend the sliding window.
	 * Since we only have 26 characters, keep the count in a integer array is enough.
	 */
	public int characterReplacement_sliding_count_max(String s, int k) {
		if (s == null || s.length() == 0) {
			return 0;
		}
		
		int max = 0;
		int[] ch = new int[26];
		
		char[] str = s.toCharArray();
		for (int i = 0, j = 0; i < s.length(); i++) {
			while (j < s.length()) {
				ch[str[j] - 'A']++;
				
				// If exceed k, break
				if (count_sliding_count_max(ch) > k) {
					ch[str[j] - 'A']--;
					break;
				}
				
				j++;
			}
			
			max = Math.max(max, j - i);
			ch[str[i] - 'A']--;
		}
		return max;
	}

	// Count the number of character that is different to the longest character
	public int count_sliding_count_max(int[] ch) {
		int max = 0;
		int sum = 0;
		
		for (int val : ch) {
			sum += val;
			max = Math.max(max, val);
		}
		
		return sum - max;
	}
	
	/*
	 * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/91323/Java-O(n)-solution-using-sliding-window
	 * 
	 * The idea is to find maximum valid substring with repeated character 'A' to 'Z' 
	 * respectively. For each case, use sliding window to determine its maximum length, 
	 * update the global maximum length if needed.
	 */
	public int characterReplacement_26_sliding_window(String s, int k) {
		int maxLen = 0;
		for (int l = 0; l < 26; l++) {
			
			// repeated char we are looking for
			char c = (char) ('A' + l);
			
			int i = 0, j = 0, count = 0;
			
			while (j < s.length()) {
				char cur = s.charAt(j);
				if (cur != c)
					count++;

				// make the substring valid again
				while (count > k) {
					if (s.charAt(i) != c)
						count--;
					
					i++;
				}

				// update maximun len
				maxLen = Math.max(maxLen, j - i + 1);
				
				j++;
			}
		}
		return maxLen;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/91314/Binary-search.-Slower-but-still-interesting.
	 * 
	 * If we can convert a substring of length x into a valid one (a string with all 
	 * unique letters) using no more than k replacements, then it is clear that we can 
	 * also convert a substring of length no more than x into a valid one.
	 * 
	 * Use binary search to answer the decision problem:
	 * Is there a substring of length x such that we can make it consist of some 
	 * unique letter with no more than k replacements?
	 * 
	 * The solution to this question is simple. We enumerate all substring of length 
	 * x. For each substring, we denote the frequency of the most frequent letters in 
	 * it as mode. Then, if x - mode <= k, the answer is yes. If x - mode > k holds 
	 * for all substrings of length x, the answer is no. This process can be done via 
	 * a sliding-window in O(26 * n) = O(n) time.
	 * 
	 * Therefore, the total runtime is O(n log n).
	 */
	private boolean ok_binarySearch(char[] ch, int k, int len) {
		int[] cnt = new int[26];
		for (int i = 0; i < ch.length; i++) {
			if (i >= len)
				cnt[ch[i - len] - 'A']--;
			
			cnt[ch[i] - 'A']++;
			
			if (i >= len - 1) {
				int max = 0;
				
				for (int j : cnt)
					max = Math.max(max, j);
				
				if (len - max <= k)
					return true;
			}
		}
		return false;
	}

	public int characterReplacement_binarySearch(String s, int k) {
		if (s.length() == 0 || k >= s.length() - 1)
			return s.length();
		
		int left = 1, right = s.length() + 1;
		char[] ch = s.toCharArray();
		
		while (left + 1 < right) {
			int mid = (left + right) / 2;
			
			if (ok_binarySearch(ch, k, mid))
				left = mid;
			else
				right = mid;
		}
		return left;
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/91301/Awesome-python-solution
     * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/287688/Python-Sliding-Window-Time%3A-O(N)-Space%3A-O(1)-With-Explanation
     * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/838635/Python-sliding-window-solution-with-explanation
     * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/765776/Python%3A-Two-Pointers-%2B-Process-for-coding-interviews
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/91285/Sliding-window-similar-to-finding-longest-substring-with-k-distinct-characters
     * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/208284/C%2B%2B-Sliding-Window-With-Detailed-Explanation-and-Thinking-Process
     * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/328058/C%2B%2B-0ms-100-concise-sliding-windows-solution-with-explanation
     * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/91278/7-lines-C%2B%2B
     */
	
	/**
	 * JavaScript collections
	 * 
	 * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/370281/JavaScript-sliding-window-solution-with-comments
	 * https://leetcode.com/problems/longest-repeating-character-replacement/discuss/872604/99-Javascript-Solution-with-Explanation
	 */

}
