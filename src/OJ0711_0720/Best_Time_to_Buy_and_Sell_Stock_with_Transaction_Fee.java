package OJ0711_0720;

import java.util.Stack;

// https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/discuss/108870/Most-consistent-ways-of-dealing-with-the-series-of-stock-problems
// https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/discuss/108878/Finally-understood-this-type-of-problem

public class Best_Time_to_Buy_and_Sell_Stock_with_Transaction_Fee {
	/*
	 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/discuss/108891/Java-O(n)-time-and-O(1)-space-solution
	 * 
	 * We need to make sure the nearest difference of peak and valley points
	 * (peak is in the front of valley) is larger than fee to sell the current stock. 
	 * Otherwise, we may wait another larger peak to see current stock, and in this 
	 * case, we may cancel the previous stock if the valley is less than the moment 
	 * when we buy the current stock.
	 */
	public int maxProfit(int[] prices, int fee) {
		if (prices.length <= 1)
			return 0;
		
		int localMin = prices[0], localMax = prices[0];
		int sum = 0;
		for (int j = 1; j < prices.length;) {
			while (j < prices.length && prices[j] >= prices[j - 1])
				j++;
			if (prices[j - 1] > localMax)
				localMax = prices[j - 1];      // Update localMax

			while (j < prices.length && prices[j] <= prices[j - 1])
				j++;
			if (localMax > prices[j - 1] + fee) {
				sum += Math.max(0, localMax - localMin - fee);   // for large fee
				localMin = prices[j - 1];
				localMax = prices[j - 1];
			} 
			else if (localMin > prices[j - 1]) {
				localMin = prices[j - 1];
				localMax = prices[j - 1];       // localMax should come after localMin
			}
		}
		sum += Math.max(0, localMax - localMin - fee);
		// to make sure the ending increasing sequence, if it has, is considered.
		return sum;
	}
	
	/*
	 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/discuss/108873/faster-than-the-%22max%22-solution-and-very-clear-answer
	 * 
	 * Sell whenever you can profit, and if we find later that we sold too early, 
	 * as in at 9 of [2,10,9,11], the previous state = fee is like a transaction fee 
	 * refund, that cancels the previous decision of sell at 10.
	 * 
	 * 9 - 10 in the case of [2, 10, 9, 11] does not entirely cancel out the 
	 * state = fee = 2 so this is a successful refund, and we still hold the share of 
	 * stock. Future calculation of state will still include previous price 
	 * differences: state = (10 - 2 - 2) + 2 + (9 - 10) = 9 - 2 at this point.
	 * 
	 * 2 - 10 in the case of [2, 10, 2, 11] makes state reset to 0, thus the refund 
	 * attempt is annulled.
	 */
	public int maxProfit_diff(int[] prices, int fee) {
		int state = 0, profit = 0, prev = prices[0];
		for (int price : prices) {
			state += price - prev;
			if (state > fee) {
				profit += state - fee;
				state = fee;
			} 
			else if (state < 0) {
				state = 0;
			}
			prev = price;
		}
		return profit;
	}
	
	/*
	 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/discuss/108902/O(n)-using-two-stacks.
	 * 
	 * The idea is to have two stacks. One for holding the buying prices and one for 
	 * holding the selling prices. One price in the buy queue corresponds to a sell 
	 * prices in the sell queue of the transaction is complete.
	 * 
	 * 1. If we did not do anything yet (i.e., the buy stack is empty), buy it.
	 * 2. If we have no unsold stock. That is, buyQueue.size() == sellQueue.size(), 
	 *    we have the option to regret a low sell if current price is higher than the 
	 *    price we sold the last share.
	 *    
	 *    2.1 If current price is higher than the price we sold the last share, 
	 *        regret the last sell and sell today.
	 *    2.2 If current price is lower than the price we sold the last share - fee, 
	 *        buy it. Buying a share with price higher than the last sold price - fee 
	 *        is not optimal as we will need a price that is at least greater than the 
	 *        last sold price to make profit for this purchase in the future. If that 
	 *        is the case, why not sell the previous share on that day? 
	 *        Doing that, we save the fee.
	 * 3. If we have unsold stock, that is buyQueue.size() > sellQueue.size():
	 * 
	 *    3.1 If current price is lower than the price we bought for the last share, 
	 *        we regret that purchase and instead buy today.
	 *    3.2 If selling the unsold share at current price have profit, sell it.
	 *    
	 * Finally, we will end up either each buy has a corresponding sell OR we have an 
	 * unsold share, which should simply be discarded. 
	 * Then the profit is the difference - fee for each transaction.
	 */
	public int maxProfit_Stack(int[] prices, int fee) {
        Stack<Integer> buy = new Stack<>();
        Stack<Integer> sell = new Stack<>();
        
        for (int p : prices) {
            if (buy.isEmpty()) {  //have nothing, buy it
                buy.push(p);
            } 
            else if (buy.size() == sell.size()) {  //no unsold stock
                if (p < sell.peek() - fee) {  //current price needs a price lower than the previous sold price to sell in the future, buy it.
                    buy.push(p);
                } 
                else if (p > sell.peek()) {  //selling at current price for the last transaction can have higher profit, regret the last sell.
                    sell.pop();
                    sell.push(p);
                }
            } 
            else {  // have an unsold share
                if (p > buy.peek() + fee) {  //if selling at current price have profit, do it.
                    sell.push(p);
                } 
                else if (p < buy.peek()) {  //if current price is lower than the most recent purchase, regret it and buy today.
                    buy.pop();
                    buy.push(p);
                }
            }
        }
        if (buy.size() > sell.size()) {  //discard the unsold buy attempt
            buy.pop();
        }
        
        int amount = 0;
        while (!buy.isEmpty()) {
            amount += sell.pop() - buy.pop() - fee;
        }
        return amount;
    }
	
	/*
	 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/discuss/108877/My-method-beats-95-without-using-DP!!!-Have-specific-explanation!
	 * 
	 * If the current price is lower than last buy price (prices[start]), then we 
	 * update the latest buying time to i. Another condition is we found we can get 
	 * better profit by starting a new transaction.
	 */
	public int maxProfit_no_DP(int[] prices, int fee) {
		if (prices.length <= 1)
			return 0;
		
		int maxProfit = 0, maxPrice = prices[0], res = 0, start = 0;
		for (int i = 1; i < prices.length; i++) {
			// Have higher profit, sell.
			if (prices[i] - prices[start] - fee > maxProfit) {
				maxPrice = prices[i];
				maxProfit = prices[i] - prices[start] - fee;
			}
			// Have lower cost or new transaction
			else if (prices[i] < maxPrice - fee || prices[i] < prices[start]) {
				start = i;
				res += maxProfit;
				maxProfit = 0;
				maxPrice = prices[i];
			}
		}
		
		if (maxProfit != 0)  // Last run
			res += Math.max(maxProfit, prices[prices.length - 1] - prices[start] - fee);
		return res;
	}
	
	/*
	 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/discuss/108884/JavaC++-Clean-Code-(DPGreedy)
	 * 
	 * 1. buy in - when current price higher than previous lowest point by more than 
	 *    amount of transaction fee, and set current price as highest point;
	 * 2. sell out - when current price lower than previous highest point by more than 
	 *    amount of transaction fee, and reset lowest, highest
	 * 3. update highest - only if highest is set;
	 * 4. update lowest - every day
	 */
	public int maxProfit_no_DP2(int[] p, int fee) {
		int profit = 0;
		int lo = -1, hi = -1, n = p.length;
		for (int i = 0; i < n; i++) {
			if (lo != -1 && hi == -1 && p[i] - lo > fee)       // buy in
				hi = p[i];
			if (hi != -1 && p[i] > hi)                         // update highest
				hi = p[i];
			if (hi != -1 && (hi - p[i] > fee || i == n - 1)) { // sell out
				profit += hi - lo - fee;
				hi = -1;
				lo = -1;
			}

			lo = lo != -1 ? Math.min(lo, p[i]) : p[i];         // update lowest
		}
		return profit;
	}
	
	/*
	 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/discuss/108892/Java-DP-solution-O(n)-to-O(1)-space
	 * 
	 * Hold stock:
	 * (1) We do nothing on day i: hold[i - 1];
	 * (2) We buy stock on day i: notHold[i - 1] - prices[i];
	 * 
	 * Not hold stock:
	 * (1) We do nothing on day i: notHold[i - 1];
	 * (2) We sell stock on day i: hold[i - 1] + prices[i] - fee;
	 */
	public int maxProfit_hold_notHold(int[] prices, int fee) {
        if (prices == null || prices.length <= 1) {
            return 0;
        }
        
        int hold = -prices[0];
        int notHold = 0;
        
        for (int i = 1; i < prices.length; i++) {
            hold = Math.max(hold, notHold - prices[i]);
            notHold = Math.max(notHold, hold + prices[i] - fee);
        }
        
        return notHold;
    }
	
	/*
	 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/discuss/108892/Java-DP-solution-O(n)-to-O(1)-space
	 * 
	 * hold[i] : The maximum profit of holding stock at day i;
	 * notHold[i] : The maximum profit of not hold stock at day i;
	 * 
	 * For day i, we have two situations:
	 * 
	 * Hold stock:
	 * (1) We do nothing on day i: hold[i - 1];
	 * (2) We buy stock on day i: notHold[i - 1] - prices[i];
	 * 
	 * Not hold stock:
	 * (1) We do nothing on day i: notHold[i - 1];
	 * (2) We sell stock on day i: hold[i - 1] + prices[i] - fee;
	 */
	public int maxProfit_hold_notHold_array(int[] prices, int fee) {
        if (prices == null || prices.length <= 1) {
            return 0;
        }
        
        int len = prices.length;
        int[] hold = new int[len];     // At day i, the max profit is hold[i] if I hold the stock.
        int[] notHold = new int[len];  // At day i, the max profit is notHold[i] if I do not hold the stock.
        
        hold[0] = -prices[0];
        notHold[0] = 0;
        
        for (int i = 1; i < prices.length; i++) {
            hold[i] = Math.max(hold[i - 1], notHold[i - 1] - prices[i]);
            notHold[i] = Math.max(notHold[i - 1], hold[i - 1] - fee + prices[i]);
        }
        
        return notHold[len - 1];
    }
	
	/*
	 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/discuss/108868/Java-simple-DP-solutions.-O(n)
	 * 
	 * 1. buy stock
	 * 2. hold stock
	 * 3. do nothing with empty portfolio
	 * 4. sell stock
	 * 
	 * We have 4 arrays with the length of # of the days, 
	 * recording the max profit at given day if we do given operation.
	 */
	public int maxProfit_DP_array_4(int[] prices, int fee) {
		if (prices.length <= 1)
			return 0;
		
		int[] buy = new int[prices.length];
		int[] hold = new int[prices.length];
		int[] skip = new int[prices.length];
		int[] sell = new int[prices.length];
		// the moment we buy a stock, our balance should decrease
		buy[0] = 0 - prices[0];
		// assume if we have stock in the first day, we are still in deficit
		hold[0] = 0 - prices[0];
		
		for (int i = 1; i < prices.length; i++) {
			// We can only buy on today if we sold stock
			// or skipped with empty portfolio yesterday
			buy[i] = Math.max(skip[i - 1], sell[i - 1]) - prices[i];
			// Can only hold if we bought or already holding stock yesterday
			hold[i] = Math.max(buy[i - 1], hold[i - 1]);
			// Can skip only if we skipped, or sold stock yesterday
			skip[i] = Math.max(skip[i - 1], sell[i - 1]);
			// Can sell only if we bought, or held stock yesterday
			sell[i] = Math.max(buy[i - 1], hold[i - 1]) + prices[i] - fee;
		}
		// Get the max of all the 4 actions on the last day.
		int max = Math.max(buy[prices.length - 1], hold[prices.length - 1]);
		max = Math.max(skip[prices.length - 1], max);
		max = Math.max(sell[prices.length - 1], max);
		return Math.max(max, 0);
	}
	
	/*
	 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/discuss/108871/2-solutions-2-states-DP-solutions-clear-explanation!
	 * 
	 * buy[i] represents the max profit at day i in buy status, given that the last 
	 * action you took is a buy action at day K, where K<=i. And you have the right to 
	 * sell at day i+1, or do nothing.
	 * 
	 * sell[i] represents the max profit at day i in sell status, given that the last 
	 * action you took is a sell action at day K, where K<=i. And you have the right to 
	 * buy at day i+1, or do nothing.
	 * 
	 * We can start from buy status, which means we buy stock at day 0.
	 * buy[0] = -prices[0];
	 * Or we can start from sell status, which means we sell stock at day 0.
	 * Given that we don't have any stock at hand in day 0, we set sell status to be 0.
	 * sell[0] = 0;
	 * 
	 * At day i, we may buy stock (from previous sell status) or do nothing 
	 * (from previous buy status):
	 * buy[i] = Math.max(buy[i - 1], sell[i - 1] - prices[i]);
	 * At day i, we may sell stock (from previous buy status) or keep holding 
	 * (from previous sell status):
	 * sell[i] = Math.max(sell[i - 1], buy[i - 1] + prices[i]);
	 * 
	 * We will return sell[last_day] as our result, which represents the max profit at 
	 * the last day, given that you took sell action at any day before the last day.
	 */
	public int maxProfit_buy_sell_array(int[] prices, int fee) {
		if (prices.length <= 1)
			return 0;
		
		int days = prices.length, buy[] = new int[days], sell[] = new int[days];
		buy[0] = -prices[0];
        for (int i = 1; i<days; i++) {
        	// keep the same as day i-1, or buy from sell status at day i-1
            buy[i] = Math.max(buy[i - 1], sell[i - 1] - prices[i]);
            
            // keep the same as day i-1, or sell from buy status at day i-1
            sell[i] = Math.max(sell[i - 1], buy[i - 1] + prices[i] - fee);
        }
        return sell[days - 1];
    }

}
