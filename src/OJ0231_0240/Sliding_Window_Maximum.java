package OJ0231_0240;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Deque;
import java.util.ArrayDeque;

public class Sliding_Window_Maximum {
	/*
	 * https://discuss.leetcode.com/topic/19055/java-o-n-solution-using-deque-with-explanation
	 * 
	 * Sliding window minimum/maximum = monotonic queue
	 * We only maintain a "descending list" for window. Each time we add a element, 
	 * we pop up all the elements smaller than it since they would never influence 
	 * the MAX. So the 1st element in the deque is always the maximum in [i-(k-1),i].
	 * The algorithm is amortized O(n) as each element is put and polled once.
	 * 
	 * Rf : 
	 * https://docs.oracle.com/javase/7/docs/api/java/util/Deque.html
	 * http://blog.csdn.net/Lirx_Tech/article/details/51530420
	 * https://discuss.leetcode.com/topic/22317/short-java-o-n-solution-just-using-linkedlist
	 * https://discuss.leetcode.com/topic/19055/java-o-n-solution-using-deque-with-explanation/11
	 * https://discuss.leetcode.com/topic/19297/this-is-a-typical-monotonic-queue-problem
	 * https://discuss.leetcode.com/topic/45770/important-to-talk-about-the-solution-brute-force-vs-deque-method-in-java
	 */
	public int[] maxSlidingWindow(int[] a, int k) {
		if (a == null || k <= 0) {
			return new int[0];
		}
		
		int n = a.length;
		int[] r = new int[n - k + 1];
		int ri = 0;
		// q contains index... r contains content
		// store index
		Deque<Integer> q = new ArrayDeque<>();
		for (int i = 0; i < a.length; i++) {
			// remove numbers out of range k
			if (!q.isEmpty() && q.peek() < i - k + 1) {
				q.poll();
			}
			// Remove numbers smaller than a[i] from right(a[i-1]) to left, to make 
			// the first number in the deque the largest one in the window
			while (!q.isEmpty() && a[q.peekLast()] < a[i]) {
				q.pollLast();
			}
			// Offer the current index to the deque's tail
			q.offer(i);
			// Starts recording when i is big enough to make the window has k elements
			if (i >= k - 1) {
				r[ri++] = a[q.peek()];
			}
		}
		return r;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/26480/o-n-solution-in-java-with-two-simple-pass-in-the-array
	 * 
	 * 1. partition the array in blocks of size w=4. The last block may have less than w.
	 *    2, 1, 3, 4 | 6, 3, 8, 9 | 10, 12, 56|
	 * 2. Traverse the list from start to end and calculate max_so_far. 
	 *    Reset max after each block boundary (of w elements).
	 *    left_max[] = 2, 2, 3, 4 | 6, 6, 8, 9 | 10, 12, 56
	 *    
	 *    For each element 'ai' in block we will find maximum till that element 'ai' 
	 *    starting from START of Block to END of that block.
	 * 3. Similarly calculate max in future by traversing from end to start.
	 *    right_max[] = 4, 4, 4, 4 | 9, 9, 9, 9 | 56, 56, 56
	 *    
	 *    For each element 'ai' in block we will find maximum till that element 'ai' 
	 *    starting from END of Block to START of that block.
	 * 4. Sliding max at each position i in current window, 
	 *    sliding-max[i] = max{ right_max[i], left_max[i + w - 1] }
	 *    sliding_max = 4, 6, 6, 8, 9, 10, 12, 56
	 * 
	 * Because of the property of sliding window, you can find all these "anchor" 
	 * points, and then each sliding window can be divided into two parts. 
	 * The idea is to find the maximum of the left part and the maximum of the right 
	 * part, and then obtain the maxima by comparing the two maximums. 
	 * All the left maximums can be found by a right-to-left transverse, 
	 * while all the right maximums can be found by a left-to-right transverse.
	 * 
	 * In summary, all the "pop" action in deque solution corresponds to the 
	 * "left-to-right" transverse in OP's solution and all the "push" action 
	 * corresponds to the "right-to-left" transverse in OP's solution.
	 * 
	 * Rf :
	 * https://discuss.leetcode.com/topic/26480/o-n-solution-in-java-with-two-simple-pass-in-the-array/7
	 * https://discuss.leetcode.com/topic/26480/o-n-solution-in-java-with-two-simple-pass-in-the-array/24
	 * https://discuss.leetcode.com/topic/20165/my-java-solution-using-priorityqueue/7
	 */
	public int[] maxSlidingWindow_leftMax_rightMax(int[] in, int w) {
        if (in == null || w == 0)
            return new int[] {};
        
		int[] max_left = new int[in.length];
		int[] max_right = new int[in.length];

		max_left[0] = in[0];
		max_right[in.length - 1] = in[in.length - 1];

		for (int i = 1; i < in.length; i++) {
			max_left[i] = (i % w == 0) ? in[i] : Math.max(max_left[i - 1], in[i]);

			int j = in.length - i - 1;
			max_right[j] = (j % w == 0) ? in[j] : Math.max(max_right[j + 1], in[j]);
		}

		int[] sliding_max = new int[in.length - w + 1];
		for (int i = 0; i + w <= in.length; i++) {
			sliding_max[i] = Math.max(max_right[i], max_left[i + w - 1]);
		}

		return sliding_max;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/20165/my-java-solution-using-priorityqueue
	 * 
	 * Rf :
	 * https://discuss.leetcode.com/topic/20165/my-java-solution-using-priorityqueue/4
	 * https://stackoverflow.com/questions/8031939/finding-maximum-for-every-window-of-size-k-in-an-array
	 */
	public int[] maxSlidingWindow_PriorityQueue(int[] nums, int k) {
		int len = nums.length;
		int[] result = new int[len - k + 1];
		if (nums.length == 0)
			return new int[0];
		Queue<Integer> queue = new PriorityQueue<Integer>(new Comparator<Integer>() {
			@Override
			public int compare(Integer i1, Integer i2) {
				return Integer.compare(i2, i1);
			}
		});

		for (int i = 0; i < k; i++) {
			queue.add(nums[i]);
		}
		result[0] = queue.peek();
		for (int i = k; i < len; i++) {
			queue.remove(nums[i - k]);
			queue.add(nums[i]);
			result[i - k + 1] = queue.peek();
		}

		return result;
	}
	
	// https://discuss.leetcode.com/topic/19074/treemap-solution-o-nlogk-and-deque-solution-o-n
	public int[] maxSlidingWindow_TreeMap(int[] nums, int k) {
		if (nums.length == 0)
			return nums;
		
		int[] res = new int[nums.length - k + 1];
		TreeMap<Integer, Set<Integer>> memo = new TreeMap<>();
		for (int i = 0; i < k; i++) {
			if (memo.containsKey(nums[i])) {
				memo.get(nums[i]).add(i);
			} 
			else {
				Set<Integer> temp = new HashSet<>();
				temp.add(i);
				memo.put(nums[i], temp);
			}
		}
		res[0] = memo.lastKey();
		
		for (int i = k; i < nums.length; i++) {
			if (memo.get(nums[i - k]).size() == 1) {
				memo.remove(nums[i - k]);
			} 
			else {
				memo.get(nums[i - k]).remove(i - k);
			}
			if (memo.containsKey(nums[i]))
				memo.get(nums[i]).add(i);
			else {
				Set<Integer> temp = new HashSet<>();
				temp.add(i);
				memo.put(nums[i], temp);
			}
			res[i - k + 1] = memo.lastKey();
		}
		return res;
	}
	
	// by myself
	public int[] maxSlidingWindow_self(int[] nums, int k) {
		if (nums.length == 0 || k == 0)
			return new int[0];
        
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            list.add(nums[i]);
        }
        Collections.sort(list);
        
        int n = nums.length - k + 1;
        int[] ans = new int[n];
        ans[0] = list.get(k - 1);
        
        for (int i = 1; i < n; i++) {
            int rev = Collections.binarySearch(list, nums[i - 1]);
            list.remove(rev);
            
            int pos = Collections.binarySearch(list, nums[i + k - 1]);
            if (pos < 0) {
                pos = -(pos + 1);
            }
            list.add(pos, nums[i + k - 1]);
            
            ans[i] = list.get(k - 1);
        }
        return ans;
    }
	
	// Brute force can pass the OJ.
	public int[] maxSlidingWindow_brute_force(int[] nums, int k) {
		if (nums == null || k <= 0)
			return new int[0];
		int[] arr = new int[nums.length - k + 1];
		for (int i = 0; i < nums.length - k + 1; i++) {
			int max = Integer.MIN_VALUE;
			for (int j = i; j < i + k; j++)
				max = Math.max(max, nums[j]);
			arr[i] = max;
		}
		return arr;
	}
	
	// https://discuss.leetcode.com/topic/19138/java8-functional-style-solution

}
