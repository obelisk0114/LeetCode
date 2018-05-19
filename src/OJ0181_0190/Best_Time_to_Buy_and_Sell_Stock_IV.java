package OJ0181_0190;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;
import java.util.Stack;
import java.util.PriorityQueue;

// https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/discuss/108870/Most-consistent-ways-of-dealing-with-the-series-of-stock-problems

public class Best_Time_to_Buy_and_Sell_Stock_IV {
	/*
	 * by myself
	 * 
	 * when you sell your stock this is a transaction but when you buy a stock, it is 
	 * not considered as a full transaction. so this is why the two equation look a 
	 * little different.
	 * 
	 * Rf : https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iv/discuss/54114/Easy-understanding-and-can-be-easily-modified-to-different-situations-Java-Solution
	 */
	public int maxProfit_self(int k, int[] prices) {
        if (prices.length == 0 || k == 0)
            return 0;
        
        if (k >= prices.length / 2) {
            int sum = 0;
            for (int i = 1; i < prices.length; i++) {
                sum += Math.max(prices[i] - prices[i - 1], 0);
            }
            return sum;
        }
        
        int[][] buy = new int[k][prices.length];
        for (int i = 0; i < k; i++) {
            buy[i][0] = -prices[0];
        }
        int[][] sell = new int[k][prices.length];
        for (int i = 1; i < prices.length; i++) {
            buy[0][i] = Math.max(buy[0][i - 1], -prices[i]);
            sell[0][i] = Math.max(sell[0][i - 1], buy[0][i - 1] + prices[i]);
        }
        
        for (int i = 1; i < buy.length; i++) {
            for (int j = 1; j < buy[0].length; j++) {
                buy[i][j] = Math.max(buy[i][j - 1], sell[i - 1][j - 1] - prices[j]);
                sell[i][j] = Math.max(sell[i][j - 1], buy[i][j - 1] + prices[j]);
            }
        }
        return sell[k - 1][prices.length - 1];
    }
	
	/*
	 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iv/discuss/54117/Clean-Java-DP-solution-with-comment
	 * 
	 * dp[i, j] represents the max profit up until prices[j] using at most i transactions.
	 * dp[i, j] = max(dp[i, j-1], prices[j] - prices[jj] + dp[i-1, jj - 1]) { jj in range of [0, j-1] }
	 *          = max(dp[i, j-1], prices[j] + max(dp[i-1, jj] - prices[jj]))
	 * 
	 * On day j
	 * 1. Do nothing (or buy) which doesn't change the acquired profit : 
	 *    dp[i][j] = dp[i][j-1]
	 * 2. Sell the stock, you must've bought it on a day t=[0..j-1]
	 * dp[i-1, jj - 1] means the maximum profit of just doing at most i-1 transactions, 
	 * using at most first j-1 prices, and buying the stock at price[j] - 
	 * this is used for the next loop. In this case you didn't touch price[jj] before 
	 * you bought it. so what's the max profit before you bought it on day jj, well 
	 * obviously it's dp[i-1][jj-1]. it's also dp[i-1][jj] because in this case you 
	 * can only buy it on day jj and to keep the profit maximum you will do nothing 
	 * on day jj. prices[j] - prices[jj] is the profit from buying on day jj and 
	 * selling on day j.
	 * 
	 * dp[0, j] = 0; 0 transactions makes 0 profit
	 * dp[i, 0] = 0; if there is only one price data point you can't make any transaction.
	 * 
	 * Rf : 
	 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iv/discuss/54113/A-Concise-DP-Solution-in-Java
	 * leetcode.com/problems/best-time-to-buy-and-sell-stock-iv/discuss/54113/A-Concise-DP-Solution-in-Java/55595
	 */
	public int maxProfit_dp(int k, int[] prices) {
		int n = prices.length;
		if (n <= 1)
			return 0;

		// if k >= n/2, then you can make maximum number of transactions.
		if (k >= n / 2) {
			int maxPro = 0;
			for (int i = 1; i < n; i++) {
				if (prices[i] > prices[i - 1])
					maxPro += prices[i] - prices[i - 1];
			}
			return maxPro;
		}

		int[][] dp = new int[k + 1][n];
		for (int i = 1; i <= k; i++) {
			int localMax = dp[i - 1][0] - prices[0];     // -prices[0];
			for (int j = 1; j < n; j++) {
				dp[i][j] = Math.max(dp[i][j - 1], prices[j] + localMax);
				localMax = Math.max(localMax, dp[i - 1][j - 1] - prices[j]);
			}
		}
		return dp[k][n - 1];
	}
	
	/* 
	 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iv/discuss/54125/Very-understandable-solution-by-reusing-Problem-III-idea
	 * 
	 * The only edge case is the first buy which has no previous sell. So here we 
	 * create two int[k + 1] array to use sell[0] as a buffer region.
	 * It's necessary to add a special case handling to pass large test case. 
	 */
	public int maxProfit_1D_dp(int k, int[] prices) {
		// if k >= n/2, then you can make maximum number of transactions
		if (k >= prices.length / 2) {
			int profit = 0;
			for (int i = 1; i < prices.length; i++)
				if (prices[i] > prices[i - 1])
					profit += prices[i] - prices[i - 1];
			return profit;
		}

		int[] buy = new int[k + 1], sell = new int[k + 1];
		Arrays.fill(buy, Integer.MIN_VALUE);
		for (int price : prices) {
			for (int i = 1; i <= k; i++) {
				sell[i] = Math.max(sell[i], buy[i] + price);
				buy[i] = Math.max(buy[i], sell[i - 1] - price);
			}
		}
		return sell[k];
	}
	
	// by myself
	public int maxProfit_self2(int k, int[] prices) {
        if (prices.length == 0 || k == 0)
            return 0;
        
        if (k >= prices.length / 2) {
            int sum = 0;
            for (int i = 1; i < prices.length; i++) {
                sum += Math.max(prices[i] - prices[i - 1], 0);
            }
            return sum;
        }
        
        int[] buy = new int[prices.length];
        buy[0] = -prices[0];
        int[] sell = new int[prices.length];
        for (int i = 1; i < prices.length; i++) {
            buy[i] = Math.max(buy[i - 1], -prices[i]);
            sell[i] = Math.max(sell[i - 1], buy[i - 1] + prices[i]);
        }
        
        int[] oldSell = new int[prices.length];
        
        for (int i = 1; i < k; i++) {
            System.arraycopy(sell, 0, oldSell, 0, sell.length);
            
            for (int j = 1; j < prices.length; j++) {
                buy[j] = Math.max(buy[j - 1], oldSell[j - 1] - prices[j]);
                sell[j] = Math.max(sell[j - 1], buy[j - 1] + prices[j]);
            }
        }
        return sell[prices.length - 1];
    }
	
	// by myself
	public int maxProfit_self3(int k, int[] prices) {
        if (prices.length == 0 || k <= 0)
            return 0;
        
        if (k >= prices.length / 2) {
            int sum = 0;
            for (int i = 1; i < prices.length; i++) {
                sum += Math.max(prices[i] - prices[i - 1], 0);
            }
            return sum;
        }
        
        int[] buy = new int[k];
        int[] sell = new int[k];
        Arrays.fill(buy, Integer.MIN_VALUE);
        
        for (int i = 0; i < prices.length; i++) {
            for (int j = k - 1; j > 0; j--) {
                sell[j] = Math.max(sell[j], buy[j] + prices[i]);
                buy[j] = Math.max(buy[j], sell[j - 1] - prices[i]);
            }
            sell[0] = Math.max(sell[0], buy[0] + prices[i]);
            buy[0] = Math.max(buy[0], -prices[i]);
        }
        return sell[k - 1];
    }
	
	/*
	 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iv/discuss/54118/C++-Solution-with-O(n-+-klgn)-time-using-Max-Heap-and-Stack
	 * 
	 * consider two consecutive non-decreasing v/p pairs, (v1,p1) and (v2,p2)
	 * there are 4 possible relations between (v1,p1) and (v2,p2)
	 * 1. no overlap p2 < v1
     * 2. overlap v1 <= p2 <= p1 && (v2 >= v1 || v2 < v1)
     * 3. overlap v1 <= v2 <= p1 && p2 >= p1
     * 4. no overlap p1 < v2 (doesn't seem possible to happen in this problem)
     * what's the max profit if you do only ONE transaction?
     * for case 1/2, we just pick one of the two transactions with max profit
     * for case 3, however, (v1,p2) would be the max profit
     * what's the max profit if you do exactly TWO transactions?
     * for case 1/2, we can maintain top 2 most profit transactions with a  priority
     *  queue, and poll() twice
     * for case 3, if we want to uniformly handle this and previous problems, we need
     *  to re-think (v1,p1) and (v2,p2) as (v1,p2) and (v2,p1)
     *  
     * 1. prices[v2] <= prices[v1]
     * it's obvious that we'd better make two transactions: p1-v1 and p2-v2, and v2 
     *   is more possible to make bigger profit. So, we pop out (v1,p1) from the stack.
     * 2. prices[v2] > prices[v1]
     * prices[p2] < prices[p1] We'd better use two transactions p1-v1 and p2-v2, and 
     *   v1 is still more possible to make bigger profit. So, we keep (v1,p1) in the 
     *   stack.
     * prices[p2] >= prices[p1] We are not sure about using one or two transactions to
     *   get higher profit, so we push two possible results into the profits array. 
     *   Here comes the key point: we push p2-v1 and p1-v2 into profits array. Why? 
     *   If we could only use one transaction, p2-v1 is what we want, and if with two 
     *   transactions, p2-v1 + p1-v2 is also the max profit.
     *  
     * Rf : 
	 * leetcode.com/problems/best-time-to-buy-and-sell-stock-iv/discuss/54118/C++-Solution-with-O(n-+-klgn)-time-using-Max-Heap-and-Stack/55674
	 * leetcode.com/problems/best-time-to-buy-and-sell-stock-iv/discuss/54118/C++-Solution-with-O(n-+-klgn)-time-using-Max-Heap-and-Stack/55671
	 */
	public int maxProfit(int k, int[] prices) {
        if(k < 1 || prices == null || prices.length == 0)
            return 0;
        
        int result = 0;
        List<Integer> profits = new LinkedList<>();
        Stack<Integer> starts = new Stack<>(); // hold valley, top element has largest valley value of the whole stack
        Stack<Integer> ends = new Stack<>(); // hold peak, top element has smallest peak value of the whole stack
        
		int s, e = 0;
		while (e < prices.length) {
			s = e;
			while (s + 1 < prices.length && prices[s] >= prices[s + 1])
				s++;
			
			e = s + 1;
			while (e + 1 < prices.length && prices[e] <= prices[e + 1])
				e++;
			
			if (s < e && s < prices.length && e < prices.length) {
				while (!starts.isEmpty() && prices[s] < prices[starts.peek()])
					profits.add(prices[ends.pop()] - prices[starts.pop()]);

				while (!starts.isEmpty() && prices[e] >= prices[ends.peek()]) {
					profits.add(prices[ends.pop()] - prices[s]);
					s = starts.pop();
				}
				
				starts.push(s);
				ends.push(e);
			}
		}
		while (!starts.isEmpty())
			profits.add(prices[ends.pop()] - prices[starts.pop()]);
		
		PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
		pq.addAll(profits);
		
		for (int i = 0; i < k && !pq.isEmpty(); i++)
			result += pq.poll();
		return result;
	}
	
	// https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iv/discuss/54145/O(n)-time-8ms-Accepted-Solution-with-Detailed-Explanation-(C++)

}
