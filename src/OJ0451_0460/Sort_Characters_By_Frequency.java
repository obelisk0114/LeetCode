package OJ0451_0460;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Sort_Characters_By_Frequency {
	/*
	 * https://discuss.leetcode.com/topic/66024/java-o-n-bucket-sort-solution-o-nlogn-priorityqueue-solution-easy-to-understand
	 */
	public String frequencySort(String s) {
		Map<Character, Integer> map = new HashMap<>();
		for (char c : s.toCharArray()) {
			if (map.containsKey(c)) {
				map.put(c, map.get(c) + 1);
			} else {
				map.put(c, 1);
			}
		}
		
		List<List<Character>> bucket = new ArrayList<>(s.length() + 1);
		for (int i = 0; i < s.length() + 1; i++) {
			bucket.add(new ArrayList<>());
		}
		for (char key : map.keySet()) {
			int frequency = map.get(key);
			bucket.get(frequency).add(key);
		}
		
		StringBuilder sb = new StringBuilder();
		for (int pos = bucket.size() - 1; pos >= 0; pos--) {
			if (!bucket.get(pos).isEmpty()) {
				for (char num : bucket.get(pos)) {
					for (int i = 0; i < pos; i++) {
						sb.append(num);
					}
				}
			}
		}
		return sb.toString();
	}
	
	/*
	 * https://discuss.leetcode.com/topic/66333/super-simple-o-n-bucket-sort-based-java-solution-11-ms-no-fancy-data-structure-needed-beats-96
	 * 
	 * Rf : https://discuss.leetcode.com/topic/65947/o-n-easy-to-understand-java-solution
	 */
	public String frequencySort2(String s) {
		if (s.length() < 3)
			return s;
		int max = 0;
		int[] map = new int[256];
		for (char ch : s.toCharArray()) {
			map[ch]++;
			max = Math.max(max, map[ch]);
		}
		String[] buckets = new String[max + 1]; // create max buckets
		for (int i = 0; i < 256; i++) { // join chars in the same bucket
			String str = buckets[map[i]];
			if (map[i] > 0)
				buckets[map[i]] = (str == null) ? "" + (char) i : (str + (char) i);
		}
		StringBuilder strb = new StringBuilder();
		for (int i = max; i >= 0; i--) { // create string for each bucket.
			if (buckets[i] != null)
				for (char ch : buckets[i].toCharArray())
					for (int j = 0; j < i; j++)
						strb.append(ch);
		}
		return strb.toString();
	}
	
	// Rf : https://discuss.leetcode.com/topic/65957/java-c-o-n-simple-and-easy-to-understand
	String frequencySort_self(String s) {
		Map<Character, Integer> map = new HashMap<>();
		for (int i = 0; i < s.length(); i++) {
			map.put(s.charAt(i), map.getOrDefault(s.charAt(i), 0) + 1);
		}
		
		List<Map.Entry<Character, Integer>> list = new ArrayList<>(map.entrySet());
		//Collections.sort(list, (o1, o2) -> (o2.getValue()).compareTo(o1.getValue()));
		Collections.sort(list, new Comparator<Map.Entry<Character, Integer>>() {
			public int compare(Map.Entry<Character, Integer> o1, Map.Entry<Character, Integer> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});
		
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<Character, Integer> mapping : list) {
			for (int i = 0; i < mapping.getValue(); i++) {				
				sb.append(mapping.getKey());
			}
		}
		return sb.toString();
	}
	
	// https://discuss.leetcode.com/topic/66024/java-o-n-bucket-sort-solution-o-nlogn-priorityqueue-solution-easy-to-understand
	String frequencySort_PriorityQueue(String s) {
		Map<Character, Integer> map = new HashMap<>();
		for (char c : s.toCharArray()) {
			if (map.containsKey(c)) {
				map.put(c, map.get(c) + 1);
			} else {
				map.put(c, 1);
			}
		}
		PriorityQueue<Map.Entry<Character, Integer>> pq = new PriorityQueue<>(
				new Comparator<Map.Entry<Character, Integer>>() {
					@Override
					public int compare(Map.Entry<Character, Integer> a, Map.Entry<Character, Integer> b) {
						return b.getValue() - a.getValue();
					}
				});
		pq.addAll(map.entrySet());
		StringBuilder sb = new StringBuilder();
		while (!pq.isEmpty()) {
			Map.Entry<Character, Integer> e = pq.poll();
			for (int i = 0; i < (int) e.getValue(); i++) {
				sb.append(e.getKey());
			}
		}
		return sb.toString();
	}

}
