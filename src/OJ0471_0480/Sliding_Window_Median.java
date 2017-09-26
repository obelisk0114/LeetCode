package OJ0471_0480;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.TreeMap;
import java.util.function.Supplier;
import java.util.Comparator;
import java.util.PriorityQueue;

// https://discuss.leetcode.com/topic/75021/summary-of-ideas-and-an-accepting-solution

public class Sliding_Window_Median {
	/*
	 * The following 2 functions are by myself.
	 * Rf : 
	 * https://discuss.leetcode.com/topic/82372/java-beats-98-o-nlgk-bst-solution
	 * https://discuss.leetcode.com/topic/76304/my-space-saving-java-solution
	 */
	public double[] medianSlidingWindow_self(int[] nums, int k) {
        int length = nums.length - k + 1;
        double[] res = new double[length];
        List<Integer> window = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            window.add(nums[i]);
        }
        
        Collections.sort(window);
        res[0] = getMedian_self(window, k);
        
        for (int i = 1; i < length; i++) {
        	window.remove(Collections.binarySearch(window, nums[i - 1])); // window.remove((Integer) nums[i - 1]);
            int pos = Collections.binarySearch(window, nums[i + k - 1]);
            if (pos < 0) {
                pos = -(pos + 1);
            }
            window.add(pos, nums[i + k - 1]);
            res[i] = getMedian_self(window, k);
        }
        return res;
    }
    
    private double getMedian_self(List<Integer> window, int k) {
        double median = (double) window.get(k / 2);
        if (k % 2 != 1) {
            median = ((double) window.get(k / 2) + window.get(k / 2 - 1)) / 2;
        }
        return median;
    }
    
    /*
     * The following 5 functions are from this link.
     * https://discuss.leetcode.com/topic/74874/easy-to-understand-o-nlogk-java-solution-using-treemap
     * 
     * In this problem, I use two Ordered MultiSets as Heaps. One heap maintains the 
     * lowest 1/2 of the elements, and the other heap maintains the higher 1/2 of elements.
     * 
     * This implementation is faster than the usual implementation that uses 2 
     * PriorityQueues, because unlike PriorityQueue, TreeMap can remove arbitrary 
     * element in logarithmic time.
     * 
     * Rf :
     * https://docs.oracle.com/javase/8/docs/api/java/util/TreeMap.html#put-K-V-
     * https://docs.oracle.com/javase/8/docs/api/java/util/TreeMap.html#firstKey--
     * https://discuss.leetcode.com/topic/74874/easy-to-understand-o-nlogk-java-solution-using-treemap/7
     * https://discuss.leetcode.com/topic/75893/java-o-n-log-k-solution-with-a-wrapped-up-treemap
     */
	public double[] medianSlidingWindow(int[] nums, int k) {
		double[] res = new double[nums.length - k + 1];
		TreeMap<Integer, Integer> minHeap = new TreeMap<Integer, Integer>();
		TreeMap<Integer, Integer> maxHeap = new TreeMap<Integer, Integer>(Collections.reverseOrder());

		int minHeapCap = k / 2; // smaller heap when k is odd.
		// int maxHeapCap = k - minHeapCap;

		for (int i = 0; i < k; i++) {
			maxHeap.put(nums[i], maxHeap.getOrDefault(nums[i], 0) + 1);
		}
		int[] minHeapSize = new int[] { 0 };
		int[] maxHeapSize = new int[] { k };
		for (int i = 0; i < minHeapCap; i++) {
			move1Over(maxHeap, minHeap, maxHeapSize, minHeapSize);
		}

		res[0] = getMedian(maxHeap, minHeap, maxHeapSize, minHeapSize);
		int resIdx = 1;

		for (int i = 0; i < nums.length - k; i++) {
			int addee = nums[i + k];
			if (addee <= maxHeap.firstKey()) {
				add(addee, maxHeap, maxHeapSize);
			} else {
				add(addee, minHeap, minHeapSize);
			}

			int removee = nums[i];
			if (removee <= maxHeap.firstKey()) {
				remove(removee, maxHeap, maxHeapSize);
			} else {
				remove(removee, minHeap, minHeapSize);
			}

			// rebalance
			if (minHeapSize[0] > minHeapCap) {
				move1Over(minHeap, maxHeap, minHeapSize, maxHeapSize);
			} else if (minHeapSize[0] < minHeapCap) {
				move1Over(maxHeap, minHeap, maxHeapSize, minHeapSize);
			}

			res[resIdx] = getMedian(maxHeap, minHeap, maxHeapSize, minHeapSize);
			resIdx++;
		}
		return res;
	}
	public double getMedian(TreeMap<Integer, Integer> bigHeap, 
			TreeMap<Integer, Integer> smallHeap, 
			int[] bigHeapSize, int[] smallHeapSize) {
		return bigHeapSize[0] > smallHeapSize[0] ? (double) bigHeap.firstKey()
				: ((double) bigHeap.firstKey() + (double) smallHeap.firstKey()) / 2.0;
	}
	// move the top element of heap1 to heap2
	public void move1Over(TreeMap<Integer, Integer> heap1, 
			TreeMap<Integer, Integer> heap2, 
			int[] heap1Size, int[] heap2Size) {
		int peek = heap1.firstKey();
		add(peek, heap2, heap2Size);
		remove(peek, heap1, heap1Size);
	}
	public void add(int val, TreeMap<Integer, Integer> heap, int[] heapSize) {
		heap.put(val, heap.getOrDefault(val, 0) + 1);
		heapSize[0]++;
	}
	public void remove(int val, TreeMap<Integer, Integer> heap, int[] heapSize) {
		if (heap.put(val, heap.get(val) - 1) == 1)
			heap.remove(val);
		heapSize[0]--;
	}
    
    /*
     * The following 2 variables and 4 functions are from this link.
     * https://discuss.leetcode.com/topic/74724/java-solution-using-two-priorityqueues
     * 
     * 1. Use two Heaps to store numbers. 
     * maxHeap for numbers smaller than current median, 
     * minHeap for numbers bigger than and equal to current median. 
     * A small trick I used is always make size of minHeap equal (when there are 
     * even numbers) or 1 element more (when there are odd numbers) than the size of 
     * maxHeap. Then it will become very easy to calculate current median.
     * 
     * 2. Keep adding number from the right side of the sliding window and remove 
     * number from left side of the sliding window. And keep adding current median to 
     * the result.
     * 
     * Other code :
     * https://discuss.leetcode.com/topic/74625/java-using-two-priorityqueues-to-conquer
     */
	PriorityQueue<Integer> minHeap_queue = new PriorityQueue<Integer>();
	PriorityQueue<Integer> maxHeap_queue = new PriorityQueue<Integer>(new Comparator<Integer>() {
		public int compare(Integer i1, Integer i2) {
			return i2.compareTo(i1);
		}
	});
	public double[] medianSlidingWindow_2_priority_queue(int[] nums, int k) {
		int n = nums.length - k + 1;
		if (n <= 0)
			return new double[0];
		double[] result = new double[n];

		for (int i = 0; i <= nums.length; i++) {
			if (i >= k) {
				result[i - k] = getMedian();
				remove(nums[i - k]);
			}
			if (i < nums.length) {
				add(nums[i]);
			}
		}

		return result;
	}
	private void add(int num) {
		if (num < getMedian()) {
			maxHeap_queue.add(num);
		} else {
			minHeap_queue.add(num);
		}
		if (maxHeap_queue.size() > minHeap_queue.size()) {
			minHeap_queue.add(maxHeap_queue.poll());
		}
		if (minHeap_queue.size() - maxHeap_queue.size() > 1) {
			maxHeap_queue.add(minHeap_queue.poll());
		}
	}
	private void remove(int num) {
		if (num < getMedian()) {
			maxHeap_queue.remove(num);
		} else {
			minHeap_queue.remove(num);
		}
		if (maxHeap_queue.size() > minHeap_queue.size()) {
			minHeap_queue.add(maxHeap_queue.poll());
		}
		if (minHeap_queue.size() - maxHeap_queue.size() > 1) {
			maxHeap_queue.add(minHeap_queue.poll());
		}
	}
	private double getMedian() {
		if (maxHeap_queue.isEmpty() && minHeap_queue.isEmpty())
			return 0;

		if (maxHeap_queue.size() == minHeap_queue.size()) {
			return ((double) maxHeap_queue.peek() + (double) minHeap_queue.peek()) / 2.0;
		} else {
			return (double) minHeap_queue.peek();
		}
	}
	
	// https://discuss.leetcode.com/topic/79642/short-and-clear-o-nlogk-java-solutions
    
    /*
     * https://discuss.leetcode.com/topic/79165/java-using-two-tree-sets-o-n-logk
     * 
     * However instead of using two priority queue's we use two Tree Sets as we 
     * want O(logk) for remove(element). 
     * Priority Queue would have been O(k) for remove(element) giving us an overall 
     * time complexity of O(nk) instead of O(nlogk).
     * 
     * However there is an issue when it comes to duplicate values so to get around 
     * this we store the index into nums in our Tree Set. 
     * To break ties in our Tree Set comparator we compare the index.
     * 
     * Rf :
     * https://docs.oracle.com/javase/8/docs/api/java/util/function/Supplier.html
     * https://openhome.cc/Gossip/Java/ConsumerFunctionPredicateSupplier.html
     */
    public double[] medianSlidingWindow_TreeSet(int[] nums, int k) {
        Comparator<Integer> comparator = (a, b) -> nums[a] != nums[b] ? Integer.compare(nums[a], nums[b]) : a - b;
        TreeSet<Integer> left = new TreeSet<>(comparator.reversed());
        TreeSet<Integer> right = new TreeSet<>(comparator);
        
        Supplier<Double> median = (k % 2 == 0) ?
            () -> ((double) nums[left.first()] + nums[right.first()]) / 2 :
            () -> (double) nums[right.first()];
        
        // balance lefts size and rights size (if not equal then right will be larger by one)
		Runnable balance = () -> {
			while (left.size() > right.size())
				right.add(left.pollFirst());
		};

		double[] result = new double[nums.length - k + 1];

		for (int i = 0; i < k; i++)
			left.add(i);
		balance.run();
		result[0] = median.get();
        
        for (int i = k, r = 1; i < nums.length; i++, r++) {
            // remove tail of window from either left or right
			if (!left.remove(i - k))
				right.remove(i - k);

            // add next num, this will always increase left size
			right.add(i);
			left.add(right.pollFirst());
            
            // rebalance left and right, then get median from them
			balance.run();
			result[r] = median.get();
        }
        
        return result;
    }

}
