package OJ0291_0300;

import java.util.Collections;
import java.util.PriorityQueue;

public class Find_Median_from_Data_Stream {
	/*
	 * https://discuss.leetcode.com/topic/27522/java-python-two-heap-solution-o-log-n-add-o-1-find
	 * 
	 * (1) length of (small, large) == (k, k)
     * (2) length of (small, large) == (k, k + 1)
     * 
     * After adding the number, total (n + 1) numbers, they will become:

     * (1) length of (small, large) == (k, k + 1)
     * (2) length of (small, large) == (k + 1, k + 1)
     * 
     * Here we take the first scenario for example, we know the large will gain one 
     * more item and small will remain the same size, but we cannot just push the item 
     * into large. What we should do is we push the new number into small and pop the 
     * maximum item from small then push it into large (all the pop and push here are 
     * heappop and heappush). By doing this kind of operations for the two scenarios 
     * we can keep our invariant.
	 */
	private PriorityQueue<Integer> small;
	private PriorityQueue<Integer> large;
	private boolean even;

	/** initialize your data structure here. */
	public Find_Median_from_Data_Stream() { // public MedianFinder()
		small = new PriorityQueue<>(Collections.reverseOrder());
		large = new PriorityQueue<>();
		even = true;
	}

	public void addNum(int num) {
		if (even) {
			large.offer(num);
			small.offer(large.poll());
		} else {
			small.offer(num);
			large.offer(small.poll());
		}
		even = !even;
	}

	public double findMedian() {
		if (even)
			return (small.peek() + large.peek()) / 2.0;
		else
			return small.peek();
	}
	
	/**
	 * Your MedianFinder object will be instantiated and called as such:
	 * MedianFinder obj = new MedianFinder();
	 * obj.addNum(num);
	 * double param_2 = obj.findMedian();
	 */
	
	/*
	 * https://discuss.leetcode.com/topic/27620/share-my-java-solution-logn-to-insert-o-1-to-query
	 * https://discuss.leetcode.com/topic/34461/java-easy-version-to-understand
	 */
	
	// https://discuss.leetcode.com/topic/27518/32ms-easy-to-understand-java-solution
	
	/*
	 * https://discuss.leetcode.com/topic/27521/short-simple-java-c-python-o-log-n-o-1
	 * https://discuss.leetcode.com/topic/27541/very-short-o-log-n-o-1
	 */
	
	// https://discuss.leetcode.com/topic/27506/easy-to-understand-double-heap-solution-in-java
	
	/*
	 * https://discuss.leetcode.com/topic/61789/22ms-java-solution-using-binary-tree-beats-99-82-of-submissions
	 * https://discuss.leetcode.com/topic/40917/18ms-beats-100-java-solution-with-bst
	 */

}
