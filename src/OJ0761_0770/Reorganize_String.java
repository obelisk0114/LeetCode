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
	 * Rf : https://leetcode.com/problems/reorganize-string/discuss/146583/Easy-to-Understand-Solution-Using-Priority-Queues-in-Java
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
