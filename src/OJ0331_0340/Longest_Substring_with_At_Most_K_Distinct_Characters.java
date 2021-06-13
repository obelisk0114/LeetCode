package OJ0331_0340;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

public class Longest_Substring_with_At_Most_K_Distinct_Characters {
	/*
	 * https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/discuss/80055/Generic-solution-in-Java-that-can-be-used-for-Unicode
	 * 
	 * This problem can be solved using two pointers. The important part is 
	 * while (map.size() > k), we move left pointer to make sure the map size is less 
	 * or equal to k.
	 * 
	 * 1. Run a sliding window across the string, using a hash map to track the 
	 *    characters present and the occurrence count of each.
	 * 2. If the sliding window includes a character that brings the distinct char 
	 *    count above k, then close the window until the distinct char count is back 
	 *    to k.
	 * 3. At each step, compute whether the window size is bigger than the current max.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/discuss/152392/Java%3A-Sliding-window-%2B-HashMap
	 * 
	 * Other code:
	 * https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/discuss/132557/3-Different-Accepted-Solutions
	 */
	public int lengthOfLongestSubstringKDistinct_sliding_window(String s, int k) {
		Map<Character, Integer> map = new HashMap<>();
		int left = 0;
		int best = 0;
		
		for (int i = 0; i < s.length(); i++) {
			// character at the right pointer
			char c = s.charAt(i);
			map.put(c, map.getOrDefault(c, 0) + 1);
			
			// make sure map size is valid, no need to check left pointer less than
			// s.length()
			while (map.size() > k) {
				char leftChar = s.charAt(left);
				map.put(leftChar, map.get(leftChar) - 1);
				
				if (map.get(leftChar) == 0) {
					map.remove(leftChar);
				}
				
				left++;
			}
			
			best = Math.max(best, i - left + 1);
		}
		return best;
	}
	
	/*
	 * https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/discuss/80044/Java-O(nlogk)-using-TreeMap-to-keep-last-occurrence-Interview-"follow-up"-question!/84713
	 * 
	 * We can no longer use s.charAt(lp) because lp could be infinitely far back, in 
	 * which case storing the entire, infinite window in memory would be impossible, 
	 * which breaks s.charAt(lp).
	 * 
	 * The key idea is that you can't assume any portion of the input string is 
	 * stored in memory.
	 * 
	 * -------------------------------------------------------------------------
	 * 
	 * Map is a linkedHashMap, so its key is sorted by the insertion time.
	 * map.keySet().iterator().next() will always give us the left most index.
	 * 
	 * The left most index is actually the leftmost index among all the 
	 * latest/rightmost indices of all the unique chars in the sliding window. 
	 * It is NOT the left most index of the current sliding window.
	 * 
	 * We are only maintaining the position of the MOST RECENT occurrence of a 
	 * character in the LinkedHashMap, since we delete the previous index when we see 
	 * a new one for the same character.
	 * 
	 * -------------------------------------------------------------------
	 * 
	 * The basic approach is using sliding window. Since when the number of unique 
	 * characters reaches the boundary, what we want to do is increase the left index 
	 * to remove character until the number of unique characters reduce by 1. So we 
	 * can directly store the final position the character occurs in the previous 
	 * string and every time we remove the character with the smallest final position. 
	 * Also we are scanning from small index to large index, the character with the 
	 * smallest final position is the character we least recently encountered. This 
	 * is similar to the idea of LRU Cache. Therefore, we can use LinkedHashMap to 
	 * record the character. This solution will then solve the follow up like input 
	 * string are streamed and still solve in O(n) time.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/solution/270205
	 * https://docs.oracle.com/javase/8/docs/api/java/util/LinkedHashMap.html
	 * https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/discuss/80044/Java-O(nlogk)-using-TreeMap-to-keep-last-occurrence-Interview-"follow-up"-question!/597179
	 * https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/discuss/80044/Java-O(nlogk)-using-TreeMap-to-keep-last-occurrence-Interview-"follow-up"-question!/333069
	 * https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/discuss/80044/Java-O(nlogk)-using-TreeMap-to-keep-last-occurrence-Interview-"follow-up"-question!/380694
	 * https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/discuss/80082/Solution-to-the-follow-up
	 * 
	 * Other code:
	 * https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/solution/
	 * https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/discuss/80044/Java-O(nlogk)-using-TreeMap-to-keep-last-occurrence-Interview-"follow-up"-question!/113083
	 * https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/discuss/80044/Java-O(nlogk)-using-TreeMap-to-keep-last-occurrence-Interview-"follow-up"-question!/84717
	 */
	public int lengthOfLongestSubstringKDistinct_LinkedHashMap(String s, int k) {
		int left = 0;
		int max = 0;
		LinkedHashMap<Character, Integer> map = new LinkedHashMap<>();
		
		// 如果使用
		// Map<Character, Integer> map = new LinkedHashMap<>(k + 1, 1, true);
		//
		// 可以直接 put 而不用先 remove (update 最後位置)
		
		/*
		 * LinkedHashMap(int initialCapacity, float loadFactor, boolean accessOrder)
		 * accessOrder: the ordering mode - true for access-order, 
		 * false for insertion-order
		 * 
		 * This linked list defines the iteration ordering, which is normally the 
		 * order in which keys were inserted into the map (insertion-order).
		 * 
		 * Note that insertion order is not affected if a key is re-inserted into the 
		 * map. (A key k is reinserted into a map m if m.put(k, v) is invoked when 
		 * m.containsKey(k) would return true immediately prior to the invocation.)
		 */

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			// 可以直接 map.remove(c);
			if (map.containsKey(c)) {
                map.remove(c);
            }
			map.put(c, i);

			if (map.size() > k) {
				char key = map.keySet().iterator().next();
				left = map.get(key) + 1;
				map.remove(key);
			}

			max = Math.max(max, i - left + 1);
		}
		return max;
	}
	
	/*
	 * https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/discuss/80044/Java-O(nlogk)-using-TreeMap-to-keep-last-occurrence-Interview-"follow-up"-question!/84722
	 * 
	 * The interviewer may say that the string is given as a stream. In this situation, 
	 * we can't maintain a "left pointer" as the classical O(n) hashmap solution.
	 * 
	 * -------------------------------------------------------------------------
	 * 
	 * We can no longer use s.charAt(lp) because lp could be infinitely far back, in 
	 * which case storing the entire, infinite window in memory would be impossible, 
	 * which breaks s.charAt(lp).
	 * 
	 * The key idea is that you can't assume any portion of the input string is 
	 * stored in memory.
	 * 
	 * -------------------------------------------------------------------------
	 * 
	 * Instead of recording each char's count, we keep track of char's last occurrence.
	 * 
	 * Every time when the window is full of k distinct chars, we lookup TreeMap to 
	 * find the one with leftmost last occurrence and set left bound j to be 
	 * 1 + first to exclude the char to allow new char coming into window.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/discuss/80044/Java-O(nlogk)-using-TreeMap-to-keep-last-occurrence-Interview-"follow-up"-question!/84727
	 * https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/discuss/80044/Java-O(nlogk)-using-TreeMap-to-keep-last-occurrence-Interview-"follow-up"-question!/597179
	 * https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/discuss/80044/Java-O(nlogk)-using-TreeMap-to-keep-last-occurrence-Interview-%22follow-up%22-question!
	 * 
	 * Other code:
	 * https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/discuss/80044/Java-O(nlogk)-using-TreeMap-to-keep-last-occurrence-Interview-"follow-up"-question!/857266
	 */
	public int lengthOfLongestSubstringKDistinct_last_position2(String str, int k) {
		if (str == null || str.isEmpty() || k == 0)
			return 0;
		
		TreeMap<Integer, Character> lastOccurrence = new TreeMap<>();
		Map<Character, Integer> inWindow = new HashMap<>();
		
		int j = 0, max = 1;
		
		for (int i = 0; i < str.length(); i++) {
			char in = str.charAt(i);
			
			// update or add in's position in both maps
			if (inWindow.containsKey(in)) {
				lastOccurrence.remove(inWindow.get(in));
			}
			
			inWindow.put(in, i);
			lastOccurrence.put(i, in);
			
			// make sure the size satisfies the requirement
			if (inWindow.size() > k) {
				int first = lastOccurrence.firstKey();
				char out = lastOccurrence.get(first);
				
				inWindow.remove(out);
				lastOccurrence.remove(first);
				
				j = first + 1;
			}
			
			max = Math.max(max, i - j + 1);
		}
		return max;
	}

	/*
	 * https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/discuss/80044/Java-O(nlogk)-using-TreeMap-to-keep-last-occurrence-Interview-%22follow-up%22-question!
	 * 
	 * The interviewer may say that the string is given as a stream. In this situation, 
	 * we can't maintain a "left pointer" as the classical O(n) hashmap solution.
	 * 
	 * -------------------------------------------------------------------------
	 * 
	 * We can no longer use s.charAt(lp) because lp could be infinitely far back, in 
	 * which case storing the entire, infinite window in memory would be impossible, 
	 * which breaks s.charAt(lp).
	 * 
	 * The key idea is that you can't assume any portion of the input string is 
	 * stored in memory.
	 * 
	 * -------------------------------------------------------------------------
	 * 
	 * Instead of recording each char's count, we keep track of char's last 
	 * occurrence. If you consider k as constant, it is also a O(n) algorithm.
	 * 
	 * inWindow keeps track of each char in window and its last occurrence position
	 * lastOccurrence is used to find the char in window with left most last 
	 * occurrence. A better idea is to use a PriorityQueue, as it takes O(1) to 
	 * getMin, However Java's PQ does not support O(logn) update a internal node, it 
	 * takes O(n). TreeMap takes O(logn) to do both getMin and update.
	 * 
	 * Every time when the window is full of k distinct chars, we lookup TreeMap to 
	 * find the one with leftmost last occurrence and set left bound j to be 
	 * 1 + first to exclude the char to allow new char coming into window.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/discuss/80044/Java-O(nlogk)-using-TreeMap-to-keep-last-occurrence-Interview-"follow-up"-question!/84727
	 * https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/discuss/80044/Java-O(nlogk)-using-TreeMap-to-keep-last-occurrence-Interview-"follow-up"-question!/597179
	 * https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/discuss/80044/Java-O(nlogk)-using-TreeMap-to-keep-last-occurrence-Interview-"follow-up"-question!/84725
	 * 
	 * Other code:
	 * https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/discuss/80055/Generic-solution-in-Java-that-can-be-used-for-Unicode/613357
	 */
	public int lengthOfLongestSubstringKDistinct_last_position(String str, int k) {
        if (str == null || str.isEmpty() || k == 0) {
            return 0;
        }
        
        TreeMap<Integer, Character> lastOccurrence = new TreeMap<>();
        Map<Character, Integer> inWindow = new HashMap<>();
        
        int j = 0;
        int max = 1;
        
        for (int i = 0; i < str.length(); i++) {
            char in = str.charAt(i);
            
            // we can use if instead
            while (inWindow.size() == k && !inWindow.containsKey(in)) {
                int first = lastOccurrence.firstKey();
                char out = lastOccurrence.get(first);
                
                inWindow.remove(out);
                lastOccurrence.remove(first);
                
                j = first + 1;
            }
            
            // update or add in's position in both maps
            if (inWindow.containsKey(in)) {
                lastOccurrence.remove(inWindow.get(in));
            }
            
            inWindow.put(in, i);
            lastOccurrence.put(i, in);
            
            max = Math.max(max, i - j + 1);
        }
        return max;
    }
	
	/*
	 * https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/discuss/80047/15-lines-java-solution-using-slide-window/84733
	 */
	public int lengthOfLongestSubstringKDistinct_no_shrink(String s, int k) {
		Map<Character, Integer> map = new HashMap<>();
		int lo = 0;
		int hi = 0;
		
		while (hi < s.length()) {
			map.put(s.charAt(hi), map.getOrDefault(s.charAt(hi), 0) + 1);
			
			// need to slide
			if (map.size() > k) {
				if (map.get(s.charAt(lo)) == 1)
					map.remove(s.charAt(lo));
				else
					map.put(s.charAt(lo), map.get(s.charAt(lo)) - 1);
				
				lo++;
				hi++;
			} 
			// need to extend
			else {
				hi++;
			}
		}
		return hi - lo;
    }
	
	/*
	 * https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/discuss/132557/3-Different-Accepted-Solutions
	 * 
	 * We can keep unique list of characters up to k that we met so far and the most 
	 * recent index for each unique character while iterating. When length of 
	 * characters starts exceeding k, then we should start counting new length 
	 * starting from the character with lowest index in the unique list so far. 
	 * Otherwise just increment the current length. To maintain the uniqueness and 
	 * facilitate the find and remove methods we can employ java LinkedHashSet data 
	 * structure.
	 * Total time complexity is O(n) and space complexity is O(k)
	 * Do not forget to update the position of the characters in the list while 
	 * iterating, because the head of the list must always conform to the character 
	 * with lowest recent index so far.
	 */
	public int lengthOfLongestSubstringKDistinct_LinkedHashSet(String s, int k) {
		if (k == 0)
			return 0;
		
		LinkedHashSet<Character> list = new LinkedHashSet<>(k + 1);
		int length = 0, prevLength = 0, res = 0;

		char arr[] = s.toCharArray();
		int index[] = new int[256];

		for (int i = 0; i < arr.length; i++) {
			if (!list.add(arr[i])) {
				list.remove(arr[i]);
				list.add(arr[i]);
			}

			length = prevLength + 1;
			
			if (list.size() == k + 1) {
				char toRemove = list.iterator().next();

				list.remove(toRemove);
				length = i - index[toRemove];
			}
			
			index[arr[i]] = i;
			
			prevLength = length;
			res = Math.max(res, length);
		}

		return res;
	}

	/*
	 * https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/discuss/80047/15-lines-java-solution-using-slide-window/84739
	 * 
	 * We maintain a sliding window string that always contains at most K distinct 
	 * characters with the help of two pointers, and keep track of the one with the 
	 * longest length.
	 * `int[] count` is used to store a character and its corresponding appearance in 
	 * the current window. We update it properly according to the movements of 
	 * pointers.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/discuss/142736/Clear-Java-Code-Beats-93.27
	 */
	public int lengthOfLongestSubstringKDistinct_sliding_array(String s, int k) {

		// there are 256 ASCII characters in the world
		int[] count = new int[256];

		// i is before j
		int i = 0;

		int distinctNum = 0;
		int res = 0;

		for (int j = 0; j < s.length(); j++) {
			// if count[s.charAt(j)] == 0, we know that it is a distinct character
			if (count[s.charAt(j)]++ == 0) {
				distinctNum++;
			}

			// sliding window
			while (distinctNum > k && i < s.length()) {
				count[s.charAt(i)]--;
				if (count[s.charAt(i)] == 0) {
					distinctNum--;
				}
				i++;
			}
			
			res = Math.max(res, j - i + 1);
		}
		return res;
	}
	
	/*
	 * https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/discuss/80047/15-lines-java-solution-using-slide-window/193547
	 * 
	 * Since we don't need to consider any sliding window with size less than the 
	 * current max, we can keep sliding the current window until we find something 
	 * bigger and expand the current window.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/discuss/80047/15-lines-java-solution-using-slide-window/233710
	 */
	public int lengthOfLongestSubstringKDistinct_no_shrink2(String s, int k) {
		// because With extended ASCII codes, there are total 256 characters
		int[] charCount = new int[256];
		
		int uniqueCharactersSoFar = 0, startIndex = 0, maxLength = 0;
		
		for (int currIndex = 0; currIndex < s.length(); currIndex++) {
			if (charCount[s.charAt(currIndex)]++ == 0)
				uniqueCharactersSoFar++;
			
			if (uniqueCharactersSoFar > k) {
				if (--charCount[s.charAt(startIndex)] == 0) {
					uniqueCharactersSoFar--;
				}
				startIndex++;
			}
			
			maxLength = Math.max(maxLength, currIndex - startIndex + 1);
		}
		
		// 可以 return s.length() - startIndex;
		return maxLength;
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/discuss/132557/3-Different-Accepted-Solutions
	 * 
	 * 1. Binary search by result
	 * We know that if there exists a result with length i then result with 
	 * length j <= i exists.
	 * On the contrary, if there is no substring with at most k characters of 
	 * length j <= i then we can be sure that there is no such substring with 
	 * length i too.
	 * We also know that maximum possible result is the length of the given string, 
	 * and the minimum possible result is 0. It implies that we can employ binary 
	 * search and find appropriate answer for O(nlog(n)) time and O(k) space.
	 */
	public int lengthOfLongestSubstringKDistinct_binarySearch(String s, int k) {
		char[] arr = s.toCharArray();
		
		int r = arr.length;
		int l = 0;
		int res = 0;
		
		while (l <= r) {
			int m = l + (r - l) / 2;
			
			if (thereIsLengthOf_binarySearch(m, arr, k)) {
				res = Math.max(res, m);
				l = m + 1;
			} 
			else {
				r = m - 1;
			}
		}

		return res;
	}

	Map<Character, Integer> charMap_binarySearch = new HashMap<>();

	private boolean thereIsLengthOf_binarySearch(int m, char[] arr, int k) {
		if (arr.length <= k)
			return true;
		
		charMap_binarySearch.clear();
		
		for (int i = 0; i < arr.length; i++) {
			if (i >= m) {
				if (charMap_binarySearch.size() <= k) {
					return true;
				}
				
				int freq = charMap_binarySearch.get(arr[i - m]);
				
				if (freq == 1) {					
					charMap_binarySearch.remove(arr[i - m]);
				}
				else {
					charMap_binarySearch.put(arr[i - m], freq - 1);
				}
			}
			charMap_binarySearch.put(arr[i], 
					charMap_binarySearch.getOrDefault(arr[i], 0) + 1);
		}
		
		return charMap_binarySearch.size() <= k;
	}
	
	/*
	 * https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/solution/
	 * Approach 1: Sliding Window + Hashmap.
	 * 
	 * Use sliding window approach with two pointers left and right serving as the 
	 * window boundaries.
	 * 
	 * The idea is to set both pointers in the position 0 and then move right pointer 
	 * to the right while the window contains not more than k distinct characters. 
	 * If at some point we've got k + 1 distinct characters, let's move left pointer 
	 * to keep not more than k + 1 distinct characters in the window.
	 * 
	 * To move sliding window along the string, to keep not more than k distinct 
	 * characters in the window, and to update max substring length at each step.
	 * 
	 * Let's use hashmap containing all characters in the sliding window as keys and 
	 * their rightmost positions as values. At each moment, this hashmap could 
	 * contain not more than k + 1 elements.
	 */
	public int lengthOfLongestSubstringKDistinct_last_position3(String s, int k) {
		int n = s.length();
		if (n * k == 0) {
			return 0;
		}

		int left = 0;
		int right = 0;

		Map<Character, Integer> rightmostPosition = new HashMap<>();

		int maxLength = 1;

		while (right < n) {
			rightmostPosition.put(s.charAt(right), right++);

			if (rightmostPosition.size() == k + 1) {
				int lowestIndex = Collections.min(rightmostPosition.values());
				rightmostPosition.remove(s.charAt(lowestIndex));
				left = lowestIndex + 1;
			}

			maxLength = Math.max(maxLength, right - left);
		}
		return maxLength;
	}

	/*
	 * by myself
	 * 
	 * { character: last position } in map
	 */
	public int lengthOfLongestSubstringKDistinct_self(String s, int k) {
        Map<Character, Integer> map = new HashMap<>();
        int max = 0;
        int left = 0;
        
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (!map.containsKey(ch)) {
                map.put(ch, i);
                
                if (map.size() > k) {
                    char target = s.charAt(left);
                    for (Map.Entry<Character, Integer> entry : map.entrySet()) {
                        char key = entry.getKey();
                        int val = entry.getValue();
                        
                        if (val < map.get(target)) {
                            target = key;
                        }
                    }
                    
                    left = map.get(target) + 1;
                    map.remove(target);
                }
                
                max = Math.max(max, i - left + 1);
            }
            else {
                map.put(ch, i);
                max = Math.max(max, i - left + 1);
            }
        }
        
        return max;
    }
	
	/*
	 * The following class is from this link.
	 * https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/discuss/80044/Java-O(nlogk)-using-TreeMap-to-keep-last-occurrence-Interview-"follow-up"-question!/84719
	 */
	class Solution_use_LRU_cache {
		Map<Character, Node> map;
		Node head;
		Node tail;

		public int lengthOfLongestSubstringKDistinct(String s, int k) {
			map = new HashMap<>();
			int longest = 0;
			int start = 0;
			
			for (int i = 0; i < s.length(); i++) {
				char curr = s.charAt(i);
				
				// update node and its pos
				if (map.containsKey(curr)) {
					Node node = map.get(curr);
					node.pos = i;
					remove(node);
					append(node);
				} 
				else {
					Node node = new Node(curr, i);
					append(node);
				}
				
				if (map.size() > k) {
					start = head.pos + 1;
					remove(head);
				}
				
				longest = Math.max(i - start + 1, longest);
			}
			return longest;
		}

		private Node remove(Node node) {
			map.remove(node.c);
			
			if (node.next != null) {
				node.next.prev = node.prev;
			}
			if (node.prev != null) {
				node.prev.next = node.next;
			}
			
			if (node == head) {
				head = node.next;
			}
			if (node == tail) {
				tail = node.prev;
			}
			
			node.prev = null;
			node.next = null;
			
			return node;
		}

		private Node append(Node node) {
			map.put(node.c, node);
			
			if (tail == null) {
				head = tail = node;
			} 
			else {
				tail.next = node;
				node.prev = tail;
				tail = node;
			}
			return node;
		}
		
		class Node {
			Node next;
			Node prev;
			int pos;
			char c;
			
			Node(char c, int pos) {
				this.c = c;
				this.pos = pos;
			}
		}
	}

	/**
     * Python collections
     * 
     * https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/discuss/683029/Python3-Sliding-Window-(ELIF5%3A-Explained-Like-I'm-5-Years-Old)
     * https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/discuss/80052/10-line-Python-Solution-using-dictionary-with-easy-to-understand-explanation
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/discuss/80108/C%2B%2B-two-concise-solutions%3A-hash-%2B-two-pointers-for-short-string-and-hash-for-super-long-string
     * https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/discuss/80060/8-lines-C%2B%2B-O(n)-8ms
     */

}
