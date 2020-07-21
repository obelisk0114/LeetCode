package OJ0341_0350;

import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;

// https://discuss.leetcode.com/topic/48158/3-java-solution-using-array-maxheap-treemap

public class Top_K_Frequent_Elements {
	/*
	 * The following 2 variables and 4 functions are from this link.
	 * https://leetcode.com/articles/top-k-frequent-elements/
	 * 
	 * Approach 2: Quickselect
	 * 
	 * Quickselect is typically used to solve the problems "find kth something": 
	 * kth smallest, kth largest, kth most frequent, kth less frequent, etc. 
	 * 
	 * As an output, we have an array where the pivot is on its perfect position in 
	 * the ascending sorted array, sorted by the frequency. All elements on the left 
	 * of the pivot are less frequent than the pivot, and all elements on the right 
	 * are more frequent or have the same frequency.
	 * 
	 * 1. Build a hash map "element -> its frequency" and convert its keys into the 
	 *    array "unique" of unique elements. Note that elements are unique, but their 
	 *    frequencies are not. That means we need a partition algorithm that works 
	 *    fine with duplicates.
	 * 2. Work with "unique" array. Use a partition scheme to place the pivot into its 
	 *    perfect position "pivot_index" in the sorted array, move less frequent 
	 *    elements to the left of pivot, and more frequent or of the same frequency - 
	 *    to the right.
	 * 3. Compare "pivot_index" and "N - k".
	 *    3.1 If "pivot_index == N - k", the pivot is "N - k"th most frequent element, 
	 *        and all elements on the right are more frequent or of the same 
	 *        frequency. Return these top k frequent elements.
	 *    3.2 Otherwise, choose the side of the array to proceed recursively.
	 */
	int[] unique;
    Map<Integer, Integer> count_quickselect;

    public void swap(int a, int b) {
        int tmp = unique[a];
        unique[a] = unique[b];
        unique[b] = tmp;
    }

    public int partition(int left, int right, int pivot_index) {
        int pivot_frequency = count_quickselect.get(unique[pivot_index]);
        // 1. move pivot to end
        swap(pivot_index, right);
        int store_index = left - 1;

        // 2. move all less frequent elements to the left
        for (int i = left; i < right; i++) {
            if (count_quickselect.get(unique[i]) < pivot_frequency) {
            	store_index++;
                swap(store_index, i);
            }
        }

        // 3. move pivot to its final place
        store_index++;
        swap(store_index, right);

        return store_index;
    }
    
    public void quickselect(int left, int right, int k_smallest) {
        /*
        Sort a list within left..right till kth less frequent element
        takes its place. 
        */

        // base case: the list contains only one element
		if (left == right)
			return;

        // select a random pivot_index
        Random random_num = new Random();
        int pivot_index = left + random_num.nextInt(right - left); 

        // find the pivot position in a sorted list
        pivot_index = partition(left, right, pivot_index);

        // if the pivot is in its final sorted position
        if (k_smallest == pivot_index) {
            return;    
        } 
        else if (k_smallest < pivot_index) {
            // go left
            quickselect(left, pivot_index - 1, k_smallest);     
        } 
        else {
            // go right 
            quickselect(pivot_index + 1, right, k_smallest);  
        }
    }
    
    public int[] topKFrequent_quickselect(int[] nums, int k) {
        // build hash map : character and how often it appears
        count_quickselect = new HashMap<>();
        for (int num: nums) {
            count_quickselect.put(num, count_quickselect.getOrDefault(num, 0) + 1);
        }
        
        // array of unique elements
        int n = count_quickselect.size();
        unique = new int[n]; 
        int i = 0;
        for (int num: count_quickselect.keySet()) {
            unique[i] = num;
            i++;
        }
        
        // kth top frequent element is (n - k)th less frequent.
        // Do a partial sort: from less frequent to the most frequent, till
        // (n - k)th less frequent element takes its place (n - k) in a sorted array. 
        // All element on the left are less frequent.
        // All the elements on the right are more frequent. 
        quickselect(0, n - 1, n - k);
        // Return top k frequent elements
        return Arrays.copyOfRange(unique, n - k, n);
    }
	
	// https://discuss.leetcode.com/topic/44237/java-o-n-solution-bucket-sort
	public int[] topKFrequent_bucket(int[] nums, int k) {
		List<List<Integer>> bucket = new ArrayList<>();
		for (int i = 0; i < nums.length + 1; i++) {
			bucket.add(new ArrayList<>());
		}
		
		Map<Integer, Integer> frequencyMap = new HashMap<Integer, Integer>();
		for (int n : nums) {
			frequencyMap.put(n, frequencyMap.getOrDefault(n, 0) + 1);
		}

		for (int key : frequencyMap.keySet()) {
			int frequency = frequencyMap.get(key);
			bucket.get(frequency).add(key);
		}

		List<Integer> res = new ArrayList<>();
		for (int pos = bucket.size() - 1; pos >= 0 && res.size() < k; pos--) {
			if (!bucket.get(pos).isEmpty()) {
				res.addAll(bucket.get(pos));
			}
		}
		
		int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            result[i] = res.get(i);
        }
        return result;
	}
	
	/*
	 * Modified by myself
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/44307/java-straightforward-o-n-n-k-lg-k-solution
	 * https://discuss.leetcode.com/topic/46088/java-solution-use-hashmap-and-priorityqueue
	 */
	public int[] topKFrequent_PriorityQueue_self(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i : nums) {
            int value = map.getOrDefault(i, 0);
            map.put(i, value + 1);
        }
        
        PriorityQueue<Map.Entry<Integer, Integer>> pq = 
        		new PriorityQueue<>(new Comparator<Map.Entry<Integer, Integer>>() {
        			public int compare(Map.Entry<Integer, Integer> a, 
        					Map.Entry<Integer, Integer> b) {
        				
        				return a.getValue() - b.getValue();
        			}
        		}
        );
        
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            pq.offer(entry);
            if (pq.size() > k) {
                pq.poll();
            }
        }
        
        int[] ans = new int[k];
        int i = k - 1;
        for (Map.Entry<Integer, Integer> entry : pq) {
            ans[i] = entry.getKey();
            i--;
        }
        return ans;
    }
	
	/*
	 * https://leetcode.com/articles/top-k-frequent-elements/
	 * Approach 1: Heap
	 */
	public int[] topKFrequent_heap(int[] nums, int k) {
        // O(1) time
		if (k == nums.length) {
			return nums;
		}
        
        // 1. build hash map : character and how often it appears
        // O(N) time
		Map<Integer, Integer> count = new HashMap<>();
		for (int n : nums) {
			count.put(n, count.getOrDefault(n, 0) + 1);
		}

        // init heap 'the less frequent element first'
        Queue<Integer> heap = new PriorityQueue<>(
            (n1, n2) -> count.get(n1) - count.get(n2));

        // 2. keep k top frequent elements in the heap
        // O(N log k) < O(N log N) time
		for (int n : count.keySet()) {
			heap.add(n);
			if (heap.size() > k)
				heap.poll();
		}

        // 3. build an output array
        // O(k log k) time
		int[] top = new int[k];
		for (int i = k - 1; i >= 0; --i) {
			top[i] = heap.poll();
		}
		return top;
    }
	
	/*
	 * https://discuss.leetcode.com/topic/44307/java-straightforward-o-n-n-k-lg-k-solution
	 * 
	 * Rf : https://discuss.leetcode.com/topic/46088/java-solution-use-hashmap-and-priorityqueue
	 */
	public List<Integer> topKFrequent_PriorityQueue2(int[] nums, int k) {
		Map<Integer, Integer> counterMap = new HashMap<>();
		for (int num : nums) {
			int count = counterMap.getOrDefault(num, 0);
			counterMap.put(num, count + 1);
		}

		PriorityQueue<Map.Entry<Integer, Integer>> pq = 
				new PriorityQueue<>((a, b) -> a.getValue() - b.getValue());
		
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

		List<Map.Entry<Integer, Integer>> list = 
				new ArrayList<Map.Entry<Integer, Integer>>(map.entrySet());
		
		Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {
			public int compare(Map.Entry<Integer, Integer> o1, 
					Map.Entry<Integer, Integer> o2) {
				
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

		List<Map.Entry<Integer, Integer>> list = 
				new ArrayList<Map.Entry<Integer, Integer>>(map.entrySet());
		
		list.sort((Map.Entry<Integer, Integer> i1, Map.Entry<Integer, Integer> i2) 
				-> i2.getValue() - i1.getValue());
		
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
