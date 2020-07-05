package OJ0261_0270;

import java.util.LinkedList;
import java.util.Queue;
import java.util.PriorityQueue;

public class Ugly_Number_II {
	/*
	 * https://leetcode.com/problems/ugly-number-ii/discuss/69362/on-java-solution
	 * 
	 * (1) 1กั2, 2กั2, 3กั2, 4กั2, 5กั2, 6กั2, 8กั2, กK
	 * (2) 1กั3, 2กั3, 3กั3, 4กั3, 5กั3, 6กั3, 8กั3, กK
	 * (3) 1กั5, 2กั5, 3กั5, 4กั5, 5กั5, 6กั5, 8กั5, กK
	 * 
	 * We can find that every subsequence is the ugly-sequence itself 
	 * (1, 2, 3, 4, 5, 6, 8, กK) multiply 2, 3, 5.
	 * 
	 * Every step we choose the smallest one, and move one step after, including nums 
	 * with same value.
	 * 
	 * factor2 and factor3 may both have value = 6, but we bump both "6"s together, 
	 * thus the duplicated 6 won't cause any problem. 
	 * I initially put it as "else if (factor3==min)", that fell to the trap.
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/33968/shortest-o-n-java-dp-solution
	 * https://discuss.leetcode.com/topic/22982/java-easy-understand-o-n-solution
	 */
	public int nthUglyNumber(int n) {
		int[] ugly = new int[n];
		ugly[0] = 1;
		int index2 = 0, index3 = 0, index5 = 0;
		int factor2 = 2, factor3 = 3, factor5 = 5;
		for (int i = 1; i < n; i++) {
			int min = Math.min(Math.min(factor2, factor3), factor5);
			ugly[i] = min;
			
			if (factor2 == min)
				factor2 = 2 * ugly[++index2];
			if (factor3 == min)
				factor3 = 3 * ugly[++index3];
			if (factor5 == min)
				factor5 = 5 * ugly[++index5];
		}
		return ugly[n - 1];
	}
	
	/*
	 * https://discuss.leetcode.com/topic/25088/java-solution-using-priorityqueue
	 * 
	 * Rf : https://discuss.leetcode.com/topic/25088/java-solution-using-priorityqueue/15
	 */
	public int nthUglyNumber2(int n) {
		if (n == 1)
			return 1;
		PriorityQueue<Long> q = new PriorityQueue<>();  // Use "long" prevent overflow
		q.add(1l);

		for (long i = 1; i < n; i++) {
			long tmp = q.poll();
			while (!q.isEmpty() && q.peek() == tmp)     // We don't need this if we use
				tmp = q.poll();                         // "TreeSet"

			q.add(tmp * 2);
			q.add(tmp * 3);
			q.add(tmp * 5);
		}
		return q.poll().intValue();
	}
	
	/*
	 * https://discuss.leetcode.com/topic/22150/using-three-queues-java-solution
	 * 
	 * Rf : https://discuss.leetcode.com/topic/21789/java-solution-with-three-queues
	 */
	public int nthUglyNumber3(int n) {
		Queue<Long> q2 = new LinkedList<Long>();
		Queue<Long> q3 = new LinkedList<Long>();
		Queue<Long> q5 = new LinkedList<Long>();
		Long res = 1L;
		q2.add(2L);
		q3.add(3L);
		q5.add(5L);
		while (n-- > 1) {
			if (q2.peek() < q3.peek() && q2.peek() < q5.peek()) {
				res = q2.poll();
				q2.add(res * 2);
				q3.add(res * 3);
				q5.add(res * 5);
			} else if (q3.peek() < q2.peek() && q3.peek() < q5.peek()) {
				res = q3.poll();
				q3.add(res * 3);
				q5.add(res * 5);
			} else {
				res = q5.poll();
				q5.add(res * 5);
			}
		}
		return res.intValue();
	}

}
