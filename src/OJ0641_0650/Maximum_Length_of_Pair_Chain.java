package OJ0641_0650;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Maximum_Length_of_Pair_Chain {
	/*
	 * by myself
	 * 
	 * Rf :
	 * https://leetcode.com/problems/maximum-length-of-pair-chain/discuss/105607/4-Liner-Python-Greedy/113587
	 * https://leetcode.com/problems/maximum-length-of-pair-chain/discuss/105607/4-Liner-Python-Greedy/451184
	 * https://leetcode.com/problems/maximum-length-of-pair-chain/discuss/105610/Java-O(nlog(n))-Time-O(1)-Space/108171
	 * 
	 * Other code:
	 * https://leetcode.com/problems/maximum-length-of-pair-chain/discuss/105649/Earliest-Deadline-first-algorithm-(greedy).-Same-as-Maximum-jobs-we-can-accomplish.
	 * https://leetcode.com/problems/maximum-length-of-pair-chain/discuss/105610/Java-O(nlog(n))-Time-O(1)-Space
	 */
	public int findLongestChain_self(int[][] pairs) {
        Arrays.sort(pairs, new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return a[1] - b[1];
            }
        });
        
        int count = 1;
        int last = pairs[0][1];
        for (int i = 1; i < pairs.length; i++) {
            if (pairs[i][0] > last) {
                count++;
                last = pairs[i][1];
            }
        }
        return count;
    }
	
	// https://leetcode.com/problems/maximum-length-of-pair-chain/discuss/411258/priority-queue
	public int findLongestChain_pq(int[][] pairs) {
		if (pairs == null || pairs.length == 0)
			return 0;

		PriorityQueue<int[]> pq = new PriorityQueue<int[]>((a, b) -> {
			return Integer.compare(a[1], b[1]);
		});

		for (int[] pair : pairs) {
			pq.add(pair);
		}

		int[] current = pq.poll();
		int result = 1;
		while (pq.size() > 0) {
			int[] next = pq.poll();
			
			if (current[1] >= next[0])
				continue;
			else {
				result++;
				current = next;
			}
		}
		return result;
	}
	
	/*
	 * https://leetcode.com/problems/maximum-length-of-pair-chain/discuss/105623/Java-Very-Simple-without-DP
	 * 
	 * You need to detect the overlap between current pair and previous pair.
	 * 
	 * a. There is no collision. It only makes sense we add it to the chain. 
	 *    Update the end value.
	 * b. There is a collision, that means we have to get rid of the current chain we 
	 *    are on or get rid of the previous chain we were on. No matter which one we 
	 *    get rid of, one of them is gone, so the length doesn't change. We chose to 
	 *    get rid of the one that ends later, because it'll have a higher chance of 
	 *    colliding with a future event. We set the end value to the one that ends 
	 *    earlier.
	 * 
	 * Rf : https://leetcode.com/problems/maximum-length-of-pair-chain/discuss/403879/Easy-Java-Explanation
	 * 
	 * Other code:
	 * https://leetcode.com/problems/maximum-length-of-pair-chain/discuss/501446/Greedy-Approach-Java
	 */
	public int findLongestChain_start_overlap(int[][] pairs) {
		Arrays.sort(pairs, (p1, p2) -> p1[0] - p2[0]);
		int len = 0;
		int pre = Integer.MIN_VALUE;
		for (int[] pair : pairs) {
			if (pair[0] > pre) {      // not overlap
				len++;
				pre = pair[1];
			} 
			else if (pair[1] < pre) { // overlap but with smaller second element
				pre = pair[1];
			}
		}
		return len;
	}
	
	/*
	 * https://leetcode.com/problems/maximum-length-of-pair-chain/discuss/105602/easy-dp
	 * 
	 * When i < j and pairs[i][1] < pairs[j][0], we can extend the chain, and so we 
	 * have the candidate answer dp[j] = max(dp[j], dp[i] + 1)
	 * 
	 * Other code:
	 * https://leetcode.com/problems/maximum-length-of-pair-chain/discuss/105602/easy-dp/155950
	 * https://leetcode.com/problems/maximum-length-of-pair-chain/discuss/105613/Java-solution-10-lines-DP
	 */
	public int findLongestChain_dp(int[][] pairs) {
		if (pairs == null || pairs.length == 0)
			return 0;
		
		Arrays.sort(pairs, (a, b) -> (a[0] - b[0]));
		int[] dp = new int[pairs.length];
		Arrays.fill(dp, 1);
		
		for (int i = 0; i < dp.length; i++) {
			for (int j = 0; j < i; j++) {
				dp[i] = Math.max(dp[i], pairs[i][0] > pairs[j][1] ? dp[j] + 1 : dp[j]);
			}
		}
		return dp[pairs.length - 1];
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/maximum-length-of-pair-chain/discuss/105607/4-Liner-Python-Greedy
     * https://leetcode.com/problems/maximum-length-of-pair-chain/discuss/225801/Proof-of-the-greedy-solution
     * https://leetcode.com/problems/maximum-length-of-pair-chain/discuss/105640/O(nlogn)-Python-solution-binary-search-easy-to-understand
     * https://leetcode.com/problems/maximum-length-of-pair-chain/discuss/321213/My-thinking-process-in-40-minutes-dfs(TLE)-greaterdp(2316ms)-greatergreedy(60ms)
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/maximum-length-of-pair-chain/discuss/105617/C%2B%2B-Clean-Code
     */

}
