package OJ0341_0350;

import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Map;
import java.util.HashMap;

// https://discuss.leetcode.com/topic/48158/3-java-solution-using-array-maxheap-treemap

public class Top_K_Frequent_Elements {
	// https://discuss.leetcode.com/topic/44237/java-o-n-solution-bucket-sort
	public List<Integer> topKFrequent(int[] nums, int k) {
		List<Integer>[] bucket = new List[nums.length + 1];
		Map<Integer, Integer> frequencyMap = new HashMap<Integer, Integer>();
		for (int n : nums) {
			frequencyMap.put(n, frequencyMap.getOrDefault(n, 0) + 1);
		}

		for (int key : frequencyMap.keySet()) {
			int frequency = frequencyMap.get(key);
			if (bucket[frequency] == null) {
				bucket[frequency] = new ArrayList<>();
			}
			bucket[frequency].add(key);
		}

		List<Integer> res = new ArrayList<>();
		for (int pos = bucket.length - 1; pos >= 0 && res.size() < k; pos--) {
			if (bucket[pos] != null) {
				res.addAll(bucket[pos]);
			}
		}
		return res;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/44307/java-straightforward-o-n-n-k-lg-k-solution
	 * 
	 * Rf : https://discuss.leetcode.com/topic/46088/java-solution-use-hashmap-and-priorityqueue
	 */
	public List<Integer> topKFrequent_PriorityQueue(int[] nums, int k) {
		Map<Integer, Integer> counterMap = new HashMap<>();
		for (int num : nums) {
			int count = counterMap.getOrDefault(num, 0);
			counterMap.put(num, count + 1);
		}

		PriorityQueue<Map.Entry<Integer, Integer>> pq = new PriorityQueue<>((a, b) -> a.getValue() - b.getValue());
		for (Map.Entry<Integer, Integer> entry : counterMap.entrySet()) {
			pq.offer(entry);
			if (pq.size() > k)
				pq.poll();
		}

		List<Integer> res = new LinkedList<>();
		while (!pq.isEmpty()) {
			res.add(0, pq.poll().getKey());
		}
		return res;
	}
	
	// Self
	public List<Integer> topKFrequent_self(int[] nums, int k) {
		List<Integer> res = new ArrayList<>();
		Map<Integer, Integer> map = new HashMap<>();
		for (int element : nums) {
			int count = map.getOrDefault(element, 0);
			map.put(element, count + 1);
		}

		List<Map.Entry<Integer, Integer>> list = new ArrayList<Map.Entry<Integer, Integer>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {
			public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});
		for (int i = 0; i < k; i++) {
			res.add(list.get(i).getKey());
		}
		return res;
	}
	
	/*
	 * http://www.cnblogs.com/hxsyl/p/3331095.html
	 * https://www.mkyong.com/java/how-to-sort-a-map-in-java/
	 * https://www.mkyong.com/java8/java-8-lambda-comparator-example/
	 */
	public List<Integer> topKFrequent_self2(int[] nums, int k) {
		List<Integer> res = new ArrayList<>();
		Map<Integer, Integer> map = new HashMap<>();
		for (int element : nums) {
			map.putIfAbsent(element, 1);
			map.computeIfPresent(element, (key, oldVal) -> oldVal + 1);
		}

		List<Map.Entry<Integer, Integer>> list = new ArrayList<Map.Entry<Integer, Integer>>(map.entrySet());
		list.sort((Map.Entry<Integer, Integer> i1, Map.Entry<Integer, Integer> i2) -> i2.getValue() - i1.getValue());
		for (int i = 0; i < k; i++) {
			res.add(list.get(i).getKey());
		}
		return res;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/45263/java8-functional-solution
	 * 
	 * Rf : 
	 * http://www.baeldung.com/java-8-collectors
	 * https://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values-java
	 */
	public List<Integer> topKFrequent_java8(int[] nums, int k) {
	    Map<Integer, Integer> counter = new HashMap<>();
	    for (int num : nums) {
	        counter.putIfAbsent(num, 0);
	        counter.computeIfPresent(num, (key, oldVal) -> oldVal + 1);
	    }
	    return counter.entrySet()
	            .stream()
	            .sorted(Comparator.comparing(Map.Entry<Integer, Integer>::getValue).reversed())
	            .limit(k)
	            .map(Map.Entry::getKey)
	            .collect(Collectors.toList());
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
