package OJ0391_0400;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Map;
import java.util.HashMap;

public class Integer_Replacement {
	/*
	 * https://discuss.leetcode.com/topic/58425/java-12-line-4-5-ms-iterative-solution-with-explanations-no-other-data-structures
	 * 
	  When n is odd it can be written into the form n = 2k+1 
	  (k is a non-negative integer.). That is, n+1 = 2k+2 and n-1 = 2k. 
	  (n+1)/2 = k+1 and (n-1)/2 = k. 
	  one of (n+1)/2 and (n-1)/2 is even, the other is odd. 
	  
	  The "best" case of this problem is to divide as much as possible. 
	  Because of that, always pick n+1 or n-1 based on if it can be divided by 4. 
	  The only special case of that is when n=3 you would like to pick n-1.
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/58425/java-12-line-4-5-ms-iterative-solution-with-explanations-no-other-data-structures/4
	 * https://discuss.leetcode.com/topic/58425/java-12-line-4-5-ms-iterative-solution-with-explanations-no-other-data-structures/7
	 * https://discuss.leetcode.com/topic/58839/java-3ms-bit-manipulation-solution/2
	 */
	public int integerReplacement(int n) {
		if (n == Integer.MAX_VALUE)
			return 32; // n = 2^31-1;
		int count = 0;
		while (n > 1) {
			if (n % 2 == 0)
				n /= 2;
			else {
				if ((n + 1) % 4 == 0 && (n - 1 != 2))
					n++;
				else
					n--;
			}
			count++;
		}
		return count;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/58401/my-java-solution-with-memorization-search-handling-overflow-test-case
	 * 
	 * To hand the overflow for INT.MAX, use 1 + (n - 1) / 2 instead of (n + 1) / 2.
	 */
	public int integerReplacement_map_memorization(int n) {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        map.put(1, 0);
        map.put(2, 1);

        return helper(n, map);
    }
    private int helper(int n, Map<Integer, Integer> map) {
		if (map.containsKey(n)) {
			return map.get(n);
		}

		int steps = -1;
		if (n % 2 == 0) {
			steps = helper(n / 2, map) + 1;
		} else {
			steps = Math.min(helper((n - 1), map) + 1, helper(1 + (n - 1) / 2, map) + 2);
		}

		map.put(n, steps);

		return steps;
    }
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/58308/4ms-recursive-java-solution-explained/2
	 * 
	 * If the number of trailing 1s is greater than one then we should use (n + 1). 
	 * Use (n - 1) only when there is only one trailing 1
	 *  
	 *   1. n = xxxxx0111, three trailing 1s so we use n+1 which is xxxxx1000, 
	 *   because this makes more trailing 0s, so that we can use >> to get n/2.
         2. n = xxxxx0001, one trailing 1 so we use n-1 which is xxxxx0000.
         
         1. the trailing 1s all become zero when you add one to the number
         2. when a number has k trailing zeroes, it could be divided by 2^k
         
     * Rf : 
     * https://discuss.leetcode.com/topic/58839/java-3ms-bit-manipulation-solution
     * https://discuss.leetcode.com/topic/58334/a-couple-of-java-solutions-with-explanations
	 */
	public int integerReplacement_TrailingOnes(int n) {
		if (n == 1) {
			return 0;
		} else if (n == 3) {
			return 2;
		} else if (n == 2147483647) {
			return 2 + integerReplacement_TrailingOnes((n >> 1) + 1);
		}
		if (n % 2 == 0) {
			return integerReplacement_TrailingOnes(n >> 1) + 1;
		} else {
			return numTrailingOnes(n) > 1 ? integerReplacement_TrailingOnes(n + 1) + 1 : integerReplacement_TrailingOnes(n - 1) + 1;
		}
	}
	private int numTrailingOnes(int num) {
		int shift = 0;
		while (((num >> shift) & 1) == 1) {
			shift++;
		}
		return shift;
	}
	
	// https://discuss.leetcode.com/topic/58334/a-couple-of-java-solutions-with-explanations
	public int integerReplacement_bitCount(int n) {
	    int c = 0;
	    while (n != 1) {
	        if ((n & 1) == 0) {
	            n >>>= 1;
	        } else if (n == 3 || Integer.bitCount(n + 1) > Integer.bitCount(n - 1)) {
	            --n;
	        } else {
	            ++n;
	        }
	        ++c;
	    }
	    return c;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/58422/java-bfs-solution-tail-recursion
	 */
	public int integerReplacement_QueueTraverse(int n) {
		assert n > 0;
		Queue<Long> queue = new LinkedList<>();
		queue.offer((long) n);
		return bfs(queue, 0);
	}
	private int bfs(Queue<Long> oldqueue, int level) {
		Queue<Long> newqueue = new LinkedList<>();
		while (!oldqueue.isEmpty()) {
			long n = oldqueue.poll();
			if (n == 1) {
				return level;
			}
			if (n % 2 == 0) {
				newqueue.offer(n / 2);
			} else {
				newqueue.offer(n + 1);
				newqueue.offer(n - 1);
			}
		}
		return bfs(newqueue, level + 1);
	}
	
	// https://discuss.leetcode.com/topic/58733/java-4ms-iterative-greedy-explained-in-detail

}
