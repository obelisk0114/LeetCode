package OJ0761_0770;

import java.util.AbstractMap;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.PriorityQueue;
import java.util.Comparator;

public class Reorganize_String {
	/*
	 * by myself
	 * 
     * suppose the algorithm created a conflict, let the indexes of failures to be 2k, 2k-1. For the 
     * odd one to reach there, we count start from 2k, 2k + 2, ... 2 ceil(n/2), 1, 3, ..., 2k - 1
     * 
     * The numbers of characters is (ceil(n/2) - k + 1) + (k) = ceil(n/2) + 1, so it is more than 
     * half ceiling.
     * 
	 * Rf :
     * https://leetcode.com/problems/reorganize-string/editorial/comments/2026587 
     * https://leetcode.com/problems/reorganize-string/discuss/146583/Easy-to-Understand-Solution-Using-Priority-Queues-in-Java
	 */
	public String reorganizeString_self2(String S) {
		Map<Character, Integer> map = new HashMap<>();
        for (char c : S.toCharArray()) {
            int number = map.getOrDefault(c, 0);
            map.put(c, number + 1);
        }
        
        PriorityQueue<Map.Entry<Character, Integer>> pq = new PriorityQueue<>(
        		new Comparator<Map.Entry<Character, Integer>>() {
        			public int compare(Map.Entry<Character, Integer> a, Map.Entry<Character, Integer> b) {
        				return b.getValue() - a.getValue();
        			}
        });
        pq.addAll(map.entrySet());
        
        StringBuilder sb = new StringBuilder();
        Map.Entry<Character, Integer> pre = pq.poll();
        if (pre.getValue() > (S.length() + 1) / 2)
            return "";
        
        for (int i = 0; i < pre.getValue(); i++) {
            sb.append(pre.getKey());
        }
        
        int i = 1;
        while (pq.size() != 0) {
            Map.Entry<Character, Integer> cur = pq.poll();
            for (int j = 0; j < cur.getValue(); j++) {
                if (i == sb.length()) {
                    sb.append(cur.getKey());
                    i = 1;
                }
                else {
                    sb.insert(i, cur.getKey());
                    i += 2;
                }
            }
        }
        return sb.toString();
	}
	
    /**
     * https://leetcode.com/problems/reorganize-string/editorial/
     * Approach 2: Counting and Odd/Even
     * 
     * 奇數群內必不相鄰, 偶數群內也是
     * 先填偶數群, 到達或是超過字串長度則從奇數群的頭部開始填
     * 
     * 從偶數尾部填到奇數頭部的 character 絕對不會碰到
     * 因為這樣就表示這個 character 的次數超過一半, 會先 return 空字串
     * 
     * When arranging the string `s` to avoid adjacent repeated characters, we can adopt a strategy 
     * based on organizing the characters into two groups: even and odd indices. By filling all the 
     * even indices first, we create a structure where no adjacent characters are the same within 
     * this group. Similarly, we proceed to fill the odd indices, ensuring that adjacent characters 
     * within this group are also different from each other.
     * 
     * To begin, we need to determine the frequencies of each character in `s`.
     * 
     * To guarantee a valid rearrangement, we need to ensure that the frequency of the most frequent 
     * letter does not exceed half the length of `s`, rounded up. If it does, it implies that it is 
     * not possible to arrange the string without adjacent repetitions, and we can return an empty 
     * string as the result.
     * 
     * We must start by placing the most frequent character of string `s` in the even positions 
     * (0, 2, 4, ...) to ensure the following case doesn't occur: baa
     * 
     * After the count for the most frequent character has exhausted we can place the remaining 
     * characters in the remaining positions. Once we have finished populating all even indices, 
     * we move on to the first odd index and then fill in the odd indices.
     * 
     * 1. Create a counter `char_counts` to store the counts of each character in the input string s.
     * 2. Find the character with the maximum count (`max_count`) in `char_counts`. Set `letter` as 
     *    the corresponding character.
     * 3. Check if `max_count` is greater than half of the length of the string rounded up. If so, it 
     *    is not possible to rearrange the characters. Return an empty string.
     * 4. Initialize a list `ans` of length equal to `s`.
     * 5. Set the starting index `index` as 0.
     *    + Place the most frequent character `letter` in the `ans` list at every second index until 
     *      its count becomes zero. Increment `index` by 2 for each placement and decrease the count 
     *      of `letter` in `char_counts`.
     * 6. Iterate through the remaining characters and their counts in `char_counts`.
     *    + While the count is greater than zero:
     *      + If `index` exceeds the length of `s`, set `index` as 1 to place all future characters at 
     *        odd indices.
     *      + Place the current character at `index` in the `ans` list and increment `index` by 2.
     *      + Decrease the count of the character by 1.
     * 7. Return the rearranged characters as a string by joining the elements in `ans`.
     * 
     * Let N be the total characters in the string.
     * Let k be the total unique characters in the string.
     * 
     * Time complexity: O(N). We will have to iterate over the entire string once to gather the 
     * counts of each character. Then, we we place each character in the answer which costs O(N).
     * 
     * Space complexity: O(k). The counter used to count the number of occurrences will incur a 
     * space complexity of O(k). Again, one could argue that because k <= 26, the space complexity is 
     * constant.
     * 
     * Ref :
     * https://leetcode.com/problems/reorganize-string/editorial/comments/2026587
     * https://leetcode.com/problems/reorganize-string/editorial/comments/2027267
     */
    public String reorganizeString_array_odd_even_insert(String s) {
        var charCounts = new int[26];
        for (char c : s.toCharArray()) {
            charCounts[c - 'a']++;
        }
        int maxCount = 0, letter = 0;
        for (int i = 0; i < charCounts.length; i++) {
            if (charCounts[i] > maxCount) {
                maxCount = charCounts[i];
                letter = i;
            }
        }
        if (maxCount > (s.length() + 1) / 2) {
            return "";
        }
        var ans = new char[s.length()];
        int index = 0;

        // Place the most frequent letter
        while (charCounts[letter] != 0) {
            ans[index] = (char) (letter + 'a');
            index += 2;
            charCounts[letter]--;
        }

        // Place rest of the letters in any order
        for (int i = 0; i < charCounts.length; i++) {
            while (charCounts[i] > 0) {
                if (index >= s.length()) {
                    index = 1;
                }

                ans[index] = (char) (i + 'a');
                index += 2;
                charCounts[i]--;
            }
        }

        return String.valueOf(ans);
    }

    /*
     * by myself
     * 
     * Rf : https://leetcode.com/problems/reorganize-string/discuss/139281/Java-code-beats-100-Time-O(n)-Space-O(n)
     */
    public String reorganizeString_n_self(String S) {
        int n = S.length();
        int[] cnt = new int[128];
        char mc = 'a';     // The most frequently
        for (char c : S.toCharArray()) {
            cnt[c]++;
            mc = (cnt[c] > cnt[mc]) ? c : mc;
        }
        
        if (cnt[mc] > (n + 1) / 2) {
            return "";
        }
        
        char[] newCh = new char[n];
        int k = 0;  // Use the most frequently character to build string from leftmost
        for (int i = 0; i < cnt[mc]; i++) {
            newCh[k] = mc;
            k += 2;
        }
        cnt[mc] = 0;
        if (k >= n)  // If k is larger than string length, change to odd position.
            k = 1;
        
        for (int i = 0; i < cnt.length; i++) {
            while (cnt[i] > 0) {
                newCh[k] = (char) i;
                cnt[i]--;
                k += 2;
                
                // If k is larger than string length, change to odd position.
                if (k >= n)
                    k = 1;
            }
        }
        String str = new String(newCh);
        return str;
    }
	
	// The following function and class are by myself.
	public String reorganizeString_self(String S) {
        Map<Character, Integer> map = new HashMap<>();
        for (char c : S.toCharArray()) {
            int number = map.getOrDefault(c, 0);
            map.put(c, number + 1);
        }
        
        PriorityQueue<countCharacter_self> pq = new PriorityQueue<>(map.size(), new Comparator<countCharacter_self>() {
            public int compare(countCharacter_self c1, countCharacter_self c2) {
                return c2.count - c1.count;
            }
        });
        for (Map.Entry<Character, Integer> entry : map.entrySet()) {
            pq.offer(new countCharacter_self(entry.getKey(), entry.getValue()));
        }
        
        StringBuilder sb = new StringBuilder();
        countCharacter_self pre = pq.poll();
        if (pre.count > (S.length() + 1) / 2)
            return "";
        
        for (int i = 0; i < pre.count; i++) {
            sb.append(pre.ch);
        }
        
        int i = 1;
        while (pq.size() != 0) {
            countCharacter_self cur = pq.poll();
            for (int j = 0; j < cur.count; j++) {
                if (i == sb.length()) {
                    sb.append(cur.ch);
                    i = 1;
                }
                else {
                    sb.insert(i, cur.ch);
                    i += 2;
                }
            }
        }
        return sb.toString();
    }
    
    class countCharacter_self {
        private char ch;
        private int count;
        
        public countCharacter_self(char ch, int count) {
            this.ch = ch;
            this.count = count;
        }
    }
    
    /*
     * https://leetcode.com/problems/reorganize-string/discuss/113440/Java-solution-PriorityQueue
     * 
     * Rf :
     * leetcode.com/problems/reorganize-string/discuss/113440/Java-solution-PriorityQueue/141459
     * https://leetcode.com/articles/reorganized-string/
     * leetcode.com/problems/reorganize-string/discuss/113440/Java-solution-PriorityQueue/114521
     */
	public String reorganizeString_pop_2_character(String S) {
		// Create map of each char to its count
		Map<Character, Integer> map = new HashMap<>();
		for (char c : S.toCharArray()) {
			int count = map.getOrDefault(c, 0) + 1;
			// Impossible to form a solution
			if (count > (S.length() + 1) / 2)
				return "";
			map.put(c, count);
		}
		
		// Greedy: fetch char of max count as next char in the result.
		// Use PriorityQueue to store pairs of (char, count) and sort by count DESC.
		PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> b[1] - a[1]);
		for (char c : map.keySet()) {
			pq.add(new int[] { c, map.get(c) });
		}
		
		// Build the result.
		StringBuilder sb = new StringBuilder();
		while (!pq.isEmpty()) {
			int[] first = pq.poll();
			if (sb.length() == 0 || first[0] != sb.charAt(sb.length() - 1)) {
				sb.append((char) first[0]);
				if (--first[1] > 0) {
					pq.add(first);
				}
			} 
			else {
				int[] second = pq.poll();
				sb.append((char) second[0]);
				if (--second[1] > 0) {
					pq.add(second);
				}
				pq.add(first);
			}
		}
		return sb.toString();
	}

    /**
     * https://leetcode.com/problems/reorganize-string/editorial/
     * Approach 1: Counting and Priority Queue
     * 
     * 每個 character 計算數目, 之後存到 priority queue
     * priority queue 彈出數量最多的 character c1, 準備 append 到新的 string
     * 
     * if 新的 string 是空的 (初始狀態) 或是 string 的末尾 character 和彈出的 character c1 不同
     *   直接將彈出的 character c1 接到 string 尾端, 並將這個 character c1 的次數 - 1
     *     if 仍然有剩 (次數 > 0), 將新的狀態放入 priority queue
     * else 
     *   因為彈出的 character c1 和 string 的末尾 character 相同,
     *   不能直接 append, 需要找不同的 character
     *   再次 pop 出 priority queue 中最多的 character c2, 將 c2 append 到 string 末端
     *     if priority queue 沒辦法找到這個 c2 (也就是 priority queue 是空的)
     *       直接將 string 設為空字串, return 它
     *   將 c1 重新放入 priority queue, c2 的次數 - 1
     *     if c2 仍然有剩 (次數 > 0), 將新的狀態放入 priority queue
     * 
     * If the count of any character exceeds half the length of the string (i.e. if it appears more 
     * than ceil(length/2) times), it is not possible to rearrange the characters, and the function 
     * should return an empty string.
     * 
     * To rearrange a given string s such that no two adjacent characters are the same we repeatedly 
     * place the most frequent characters until all characters are placed in the rearranged string.
     * 
     * To begin, we need to determine the frequencies of each character in s. This can be achieved by 
     * counting the characters using a hashmap or an array of size 26, with each index representing a 
     * specific character.
     * 
     * Once we have the character frequencies, we can proceed with the rearrangement process. The key 
     * idea is to repeatedly select the most frequent character that isn't the one previously placed. 
     * This ensures that no two adjacent characters in the rearranged string are the same.
     * 
     * To efficiently identify the most frequent characters and ensure proper ordering, we can use a 
     * priority queue. The priority queue allows us to retrieve the character with the highest 
     * frequency count in an efficient manner. We can find the most frequent character in O(1) and 
     * perform updates in O(logk) where k is the size of the priority queue.
     * 
     * With the priority queue in place, we can now start placing the characters in the rearranged 
     * string. We can iteratively select the most frequent character from the priority queue and 
     * append it to the rearranged string. However, we need to ensure that the selected character is 
     * different from the last character appended, avoiding any adjacent repetitions.
     * 
     * 1. Initialize an empty list `ans` to store the rearranged characters.
     * 2. Create a priority queue `pq` using a heap data structure. Each element in `pq` is a tuple 
     *    containing the count of a character and the character itself. The priority queue is ordered 
     *    in a way such that elements with higher counts have higher priority.
     *    + Pop the element with the highest priority from `pq`. Assign its count and character to 
     *      `count_first` and `char_first` respectively.
     *    + If `ans` is empty or the current character `char_first` is different from the last 
     *      character in `ans`, append `char_first` to `ans`. If the count of `char_first` is not 
     *      zero, update its count by decreasing it by one. If the updated count is larger than zero, 
     *      push it back to `pq`. Continue to the next iteration.
     *    + Otherwise, if `char_first` is the same as the last character in `ans`, it means we need 
     *      to choose a different character. If `pq` is empty, return an empty string as it is 
     *      impossible to rearrange the characters.
     *    + Pop the next element from `pq`, assigning its count and character to `count_second` and 
     *      `char_second` respectively. Append `char_second` to `ans`.
     *    + If the count of `char_second` is not zero, update its count by decreasing it by one. 
     *      If the updated count is larger than zero, push it back to `pq`.
     *    + Finally, push the original `char_first` back to `pq`.
     * 3. Return the rearranged characters as a string by joining the elements in `ans`.
     * 
     * Let `N` be the total characters in the string.
     * Let `k` be the total unique characters in the string.
     * 
     * Time complexity: O(Nlog(k)). We add one character to the string per iteration, so there are 
     * O(N) iterations. In each iteration, we perform a maximum of 3 priority queue operations. 
     * Each priority queue operation costs log(k). For this problem, k is bounded by 26, so one 
     * could argue that the time complexity is actually O(N).
     * 
     * Space complexity: O(k). The counter used to count the number of occurrences will incur a space 
     * complexity of O(k). Similarly, the maximum size of the priority queue will also be O(k). 
     * Given that k <= 26 in this problem, one could argue the space complexity is O(1).
     * 
     * Ref :
     * https://leetcode.com/problems/reorganize-string/editorial/comments/2026570
     */
    public String reorganizeString_array_append_different(String s) {
        var charCounts = new int[26];
        for (char c : s.toCharArray()) {
            charCounts[c - 'a'] = charCounts[c - 'a'] + 1;
        }

        // Max heap ordered by character counts
        var pq = new PriorityQueue<int[]>((a, b) -> Integer.compare(b[1], a[1]));
        for (int i = 0; i < 26; i++) {
            if (charCounts[i] > 0) {
                pq.offer(new int[] {i + 'a', charCounts[i]});
            }
        }
            
        var sb = new StringBuilder();
        while (!pq.isEmpty()) {
            var first = pq.poll();
            if (sb.length() == 0 || first[0] != sb.charAt(sb.length() - 1)) {
                sb.append((char) first[0]);
                if (--first[1] > 0) {
                    pq.offer(first);
                }
            } else {
                if (pq.isEmpty()) {
                    return "";
                }
                
                var second = pq.poll();
                sb.append((char) second[0]);
                if (--second[1] > 0) {
                    pq.offer(second);
                }
                
                pq.offer(first);
            }
        }
        
        return sb.toString();
    }
	
	// https://leetcode.com/problems/reorganize-string/discuss/146583/Easy-to-Understand-Solution-Using-Priority-Queues-in-Java
	public String reorganizeString_Map_Entry(String S) {
		if (S == null || S.isEmpty() || S.length() == 1)
			return S;
		
		Map<Character, Integer> hmap = new HashMap<Character, Integer>();
		for (char ch : S.toCharArray()) {
			hmap.put(ch, hmap.getOrDefault(ch, 0) + 1);
		}
		
		PriorityQueue<Map.Entry<Character, Integer>> pq = new PriorityQueue<>(
				new Comparator<Map.Entry<Character, Integer>>() {
					@Override
					public int compare(Map.Entry<Character, Integer> a, Map.Entry<Character, Integer> b) {
						return b.getValue() - a.getValue();
					}
				});
		pq.addAll(hmap.entrySet());
		
		Map.Entry<Character, Integer> prev = new AbstractMap.SimpleEntry<Character, Integer>('#', -1);
		StringBuilder res = new StringBuilder();
		while (!pq.isEmpty()) {
			Map.Entry<Character, Integer> e = pq.poll();
			Character K = (Character) e.getKey();
			res.append(K);
			
			if (prev.getValue() > 0) {
				pq.add(prev);
			}
			
			int freq = (int) (e.getValue()) - 1;
			e.setValue(freq);
			prev = e;
		}
		if (res.length() == S.length())
			return res.toString();
		return "";
	}
    
    // https://leetcode.com/problems/reorganize-string/discuss/113451/7-ms-Java-O(n)-Solution.-no-Sorting
    public String reorganizeString_n(String S) {
        int n = S.length();
        int[] cnt = new int[128];
        char mc = 'a';
        for (char c : S.toCharArray()) {
            cnt[c]++;
            mc = (cnt[c] > cnt[mc]) ? c : mc;
        }
        
        if (cnt[mc] == 1) {
            return S;
        }
        if (n - cnt[mc] <= cnt[mc] - 2) {
            return "";
        }
        
        StringBuilder[] sb = new StringBuilder[cnt[mc]];
        for (int i = 0; i < sb.length; i++) {
            sb[i] = new StringBuilder();
            sb[i].append(mc);
        }
        
        int k = 0;
        for (char c = 'a'; c <= 'z'; c++) {
            while (c != mc && cnt[c] > 0) {
                sb[k++].append(c);
                cnt[c]--;
                k %= sb.length;
            }
        }
        
        for (int i = 1; i < sb.length; i++) {
            sb[0].append(sb[i]);
        }
        return sb[0].toString();
    }
    
    // https://leetcode.com/problems/reorganize-string/discuss/139281/Java-code-beats-100-Time-O(n)-Space-O(n)
    public String reorganizeString_n2(String S) {
        char[] ch = S.toCharArray();
        int n = S.length();
        //'a':97 'z':122
        int[] counts = new int[123];
        for (int i = 0; i < n; i++) {
            counts[ch[i]]++;
        }

        int mark = 0;
        for (int i = 97; i < 123; i++) {
            if (counts[i] > (n + 1) / 2) {
                return "";
            } 
            else if (counts[i] == (n + 1) / 2) {
                mark = i;
            }
        }

        char[] newCh = new char[n];
        int j = 0, k = 1;
        
        if (mark == 0) {
            for (int i = 97; i < 123; i++) {
                char temp = (char) i;
                while (counts[i] != 0 && j < n) {
                    newCh[j] = temp;
                    j += 2;
                    counts[i]--;
                }
                while (counts[i] != 0 && k < n) {
                    newCh[k] = temp;
                    k += 2;
                    counts[i]--;
                }
            }
        } 
        else {
            char temp = (char) mark;
            while (j < n) {
                newCh[j] = temp;
                j += 2;
            }
            for (int i = 97; i < 123; i++) {
                temp = (char) i;
                while (i != mark && counts[i] != 0) {
                    newCh[k] = temp;
                    k += 2;
                    counts[i]--;
                }
            }
        }

        // construct a String from char[]
        String str = new String(newCh);
        return str;
    }
    
    /*
     * The following function and class are from this link.
     * https://leetcode.com/problems/reorganize-string/discuss/148071/Java-Greedy-Solution-using-TreeMap
     * 
     * The intution/greediness here is to pair a most frequent character with least 
     * frequent character, update their counts and add back to treemap and continue 
     * the process until the treemap is empty.
     * 
     * If we end up with a case where a single character count is > 1 left to be 
     * arranged, then its impossible.
     * Complexity is O(NLogN), space is O(N)
     * 
     * Rf : https://leetcode.com/problems/reorganize-string/discuss/113435/4-lines-Python
     */
	public String reorganizeString_sort_value(String S) {
		if (S == null || S.length() == 0)
			return "";
		
		final Map<Character, Integer> freqMap = new HashMap<>();
		for (int i = 0; i < S.length(); i++) {
			int val = freqMap.getOrDefault(S.charAt(i), 0);
			freqMap.put(S.charAt(i), val + 1);
		}
		
		final TreeMap<Character, Integer> sortedMap = new TreeMap<Character, Integer>(new ValComparator(freqMap));
		sortedMap.putAll(freqMap);
		StringBuilder res = new StringBuilder();
		
		while (sortedMap.size() > 0) {
			char firstKey = sortedMap.firstKey();
			int firstVal = sortedMap.get(firstKey);
			char lastKey = sortedMap.lastKey();
			int lastVal = sortedMap.get(lastKey);
			
			if (sortedMap.size() == 1) {
				if (firstVal > 1)
					return "";
				else {
					res.append(firstKey);
					return res.toString();
				}
			}
			
			res.append(firstKey).append(lastKey);
			
			sortedMap.remove(lastKey);
			sortedMap.remove(firstKey);
			freqMap.remove(lastKey);
			freqMap.remove(firstKey);
			
			if (firstVal - 1 > 0) {
				freqMap.put(firstKey, firstVal - 1);
				sortedMap.put(firstKey, firstVal - 1);
			}
			if (lastVal - 1 > 0) {
				freqMap.put(lastKey, lastVal - 1);
				sortedMap.put(lastKey, lastVal - 1);
			}
		}
		return res.toString();
	}

	class ValComparator implements Comparator<Character> {
		private Map<Character, Integer> freqMap;

		public ValComparator(Map<Character, Integer> freqMap) {
			this.freqMap = freqMap;
		}

		@Override
		public int compare(Character o1, Character o2) {
			int compare = freqMap.get(o2) - freqMap.get(o1);
			if (compare == 0)
				return o2 - o1;
			return compare;
		}
	}

}
