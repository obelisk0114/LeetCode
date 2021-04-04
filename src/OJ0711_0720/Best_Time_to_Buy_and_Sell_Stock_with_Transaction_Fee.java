package OJ0711_0720;

import java.util.Stack;

/*
 *                                          p6
 *              p4                          /
 *              /                     p4   /
 *        p2   /                 p2   /\  /
 *        /\  /                  /\  /  \/
 *       /  \/                  /  \/   p5
 *      /   p3                 /   p3
 *     /                      /
 *    p1                     p1
 * 
 * (1) 若 p1 到 p4 進行一次交易：p4 - p1 - fee
 * (2) 若 p1 到 p4 進行二次交易：(p2 - p1) + (p4 - p3) - 2 * fee
 * 
 * 若合併交易比較好，表示 (1) > (2)：
 * 
 *    p4 - p1 - fee > (p2 - p1) + (p4 - p3) - 2 * fee
 * => p2 - p3 < fee
 * 
 * ---------------------------------------------------------
 * 
 * 若 p1 到 p4 進行一次交易較好，p2 - p3 < fee
 * 若加入 p5、p6，有沒有可能產生 p3 到 p6 進行一次交易而打破前面的合併？
 * 
 * (5) p1 到 p4 進行一次交易，p5 到 p6 一次交易：(p4 - p1) + (p6 - p5) - 2 * fee
 * (6) p1 到 p2 進行一次交易，p3 到 p6 一次交易：(p2 - p1) + (p6 - p3) - 2 * fee
 * (7) 若 p1 到 p6 進行一次交易：p6 - p1 - fee
 * (8) 若 p1 到 p6 進行三次交易：(p6 - p5) + (p4 - p3) + (p2 - p1) - 3 * fee
 * 
 * 將 (5) 化為 p4 - p5 - fee
 * 將 (6) 化為 p2 - p3 - fee < 0
 * 將 (7) 化為 0
 * 將 (8) 化為 p4 - p5 + p2 - p3 - 2 * fee
 * 
 * 若 (6) 比 (5) 好，則 (7) 更好
 * 若 (8) 比 (5) 好，會產生 p2 - p3 > fee 和假設矛盾
 * 因此新加入 p5、p6 不會打破原先合併
 * 
 * ----------------------------------------------------------
 * 
 * 若 p1 到 p4 進行二次交易較好，p2 - p3 > fee
 * 若加入 p5、p6，有沒有可能產生以下情況而打破前面的分開？
 * A. p1 到 p4 進行一次交易
 * B. p1 到 p6 進行一次交易
 * 
 * A.
 * p1 到 p4 進行二次交易，p5 到 p6 一次交易 = (8) 若 p1 到 p6 進行三次交易
 * p1 到 p4 進行一次交易，p5 到 p6 一次交易 = (5) 
 * 
 * 將 (8) 化為 p2 - p3 - fee
 * 將 (5) 化為 0
 * 
 * 若 (5) 比 (8) 好，會產生 p2 - p3 < fee 和假設矛盾
 * 
 * B.
 * p1 到 p6 進行一次交易 = (7)
 * p1 到 p4 進行二次交易，p5 到 p6 一次交易 = (8) 若 p1 到 p6 進行三次交易
 * 引入條件：p1 到 p2 進行一次交易，p3 到 p6 一次交易 = (6)
 * 
 * 將 (6) 化為 p2 - p3 - fee > 0
 * 將 (7) 化為 0
 * 將 (8) 化為 p4 - p5 + p2 - p3 - 2 * fee
 * 
 * 若 (7) 比 (8) 好，則 (6) 更好
 * 
 * 由 A, B 可以得知，新加入 p5、p6 不會打破原先分開
 * 
 * 結論：
 * 新的交易不會影響舊的交易結果，可以用 DP
 * 
 */

// https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/discuss/108870/Most-consistent-ways-of-dealing-with-the-series-of-stock-problems
// https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/discuss/108878/Finally-understood-this-type-of-problem

public class Best_Time_to_Buy_and_Sell_Stock_with_Transaction_Fee {
	/*
	 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/discuss/108891/Java-O(n)-time-and-O(1)-space-solution
	 * 
	 *                                          p6
	 *              p4                          /
	 *              /                     p4   /
	 *        p2   /                 p2   /\  /
	 *        /\  /                  /\  /  \/
	 *       /  \/                  /  \/   p5
	 *      /   p3                 /   p3
	 *     /                      /
	 *    p1                     p1
	 * 
	 * (1) 若 p1 到 p4 進行一次交易：p4 - p1 - fee
	 * (2) 若 p1 到 p4 進行二次交易：(p2 - p1) + (p4 - p3) - 2 * fee
	 * 
	 * 若合併交易比較好，表示 (1) > (2)：
	 * 
	 *    p4 - p1 - fee > (p2 - p1) + (p4 - p3) - 2 * fee
	 * => p2 - p3 < fee
	 * 
	 * We need to make sure the nearest difference of peak and valley points
	 * (peak is in the front of valley) is larger than fee to sell the current stock. 
	 * Otherwise, we may wait another larger peak to see current stock, and in this 
	 * case, we may cancel the previous stock if the valley is less than the moment 
	 * when we buy the current stock.
	 */
	public int maxProfit_cal_diff(int[] prices, int fee) {
		if (prices.length <= 1)
			return 0;
		
		int localMin = prices[0], localMax = prices[0];
		int sum = 0;
		for (int j = 1; j < prices.length;) {
			while (j < prices.length && prices[j] >= prices[j - 1])
				j++;
			
			// 要和前面 localMin 合併，可能是中間小上坡，若比 localMax 大，才需要更新
			if (prices[j - 1] > localMax)
				localMax = prices[j - 1];      // Update localMax

			while (j < prices.length && prices[j] <= prices[j - 1])
				j++;
			
			// 這個 price[j - 1] 是上圖中的 p3，localMax 是 p2
			// p1 到 p2 可以進行一次交易
			if (localMax > prices[j - 1] + fee) {
				
				// 雖然 p2 - p3 > fee，但是 p2 - p1 > fee 才要計入
				sum += Math.max(0, localMax - localMin - fee);   // for large fee
				
				// p1 到 p2 進行一次交易，更新 p1、p2
				localMin = prices[j - 1];
				localMax = prices[j - 1];
			} 
			// p1 和後面 p4 合併較好，但是 p3 更低，更適合做 p1
			else if (localMin > prices[j - 1]) {
				localMin = prices[j - 1];
				localMax = prices[j - 1];       // localMax should come after localMin
			}
		}
		
		// 計入最後一段
		sum += Math.max(0, localMax - localMin - fee);
		return sum;
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
	 * Modified by myself
	 * 
	 * Rf :
	 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/discuss/201603/Python.-Greedy-is-good.
	 * 
	 * prices = [1, 3, 4, 5, 4, 8], fee = 2
	 * Do you take profit of day0 to day3 (prices[3] - prices[0] = 4) and day4 to 
	 * day5(prices[5] - prices[4] = 4) ? Or do you take profit of day0 to 
	 * day5(prices[5] - prices[0] = 7) ?
	 * 
	 * Because transaction fee = 2 , (7-2) > (4-2 + 4-2), 2nd option is better.
	 * So we need to avoid greedily setting 'minimum = day4' , because sell at $5 and 
	 * buy again at $4 only give us $1 more profit yet costs $2 more transaction fee.
	 * Open a new transaction only if prices[today] < prices[yesterday] - fee. That's 
	 * why we set 'minimum = prices[i] - fee'
	 * 
	 * --------------------------------------------------
	 * 
	 * minimum = prices[i] - fee has nothing to do with buying; it is used for later 
	 * consideration if there is a better (higher) selling price compared to the 
	 * previously selling price, to cancel out the transaction fee and merge two 
	 * transactions to one.
	 * 
	 * --------------------------------------------------
	 * 
	 * As you're looping through the prices array in order, and you will maintain the 
	 * running minimum. Then when you reach the next element where 
	 * prices[i] > minimum + fee, you can subtract, calculate the initial profit 
	 * (prices[i] - minimum - fee). How do we know if there was a greater number that 
	 * comes later in the array we could have subtracted from the minimum instead?
	 * 
	 * This next number we encounter must be greater than the prices[i] we had just 
	 * sold. But since we'd actually be subtracting from the initial minimum in this, 
	 * we don't want the transaction fee to count twice so we correct for it by 
	 * saying, minimum = prices[i] - fee. Of course, if we find a smaller minimum, 
	 * then we set it accordingly.
	 * 
	 * prices = [1, 3, 4, 5, 4, 8], fee = 2
	 * 
	 * The first price where it's greater than that and the fee added is at prices[2], 
	 * 4. So profit = 4 - 1 - 2 = 1.
	 * 
	 * But what if there was another number greater than 4 that comes after we wanted 
	 * to subtract from 1 instead. We still want it to count as 1 transaction so we 
	 * set minimum to be 4 - 2 = 2. We subtract 2 because when we calculate and add to 
	 * the the profit again, we don't want it to count as another transaction. Of 
	 * course, if we find a prices[j], that is less than 2, it's more optimal to keep 
	 * whatever we sold at before and buy at this price for the next transaction.
	 * 
	 * we see that prices[3], 5 is greater than 2. Profit = 1 + (5 - 2 - 2) = 2. 
	 * It's the same as (4 - 1 - 2) + (5 - 4 + 2 - 2).
	 * 
	 * -------------------------------------------------------------------
	 * 
	 * 1. The latter stock price increases. It is equal to next stock price[i] to be 
	 *    larger than the last the price we sold the share, 
	 *    i.e. prices[i+1] > minimum (prices[i] - fee) + fee in the code.
	 * 
	 * 2. There is a new low price after we sold and earn profits. And only if the new 
	 *    low price is lower than the price[i] - fee, we can make money in the future.
	 *    (For example, [1, 3, 4, 5, 4, 8], if sell at $5, but buy at $4, then this 
	 *    deal lost $1 because of the $2 fee.
	 *    So the low price must be lower than the price[i] - fee here.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/discuss/201603/Python.-Greedy-is-good./215523
	 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/discuss/201603/Python.-Greedy-is-good./535360
	 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/discuss/201603/Python.-Greedy-is-good./421013
	 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/discuss/201603/Python.-Greedy-is-good./341871
	 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/discuss/201603/Python.-Greedy-is-good./648088
	 */
	public int maxProfit_refund_modified(int[] prices, int fee) {
		int n = prices.length;
		if (n < 2) {
			return 0;
		}
		
		int ans = 0;
		int minimum = prices[0];
		for (int i = 1; i < n; i++) {
			if (prices[i] < minimum) {
				minimum = prices[i];
			} 
			else if (prices[i] > minimum + fee) {
				ans += prices[i] - fee - minimum;
				minimum = prices[i] - fee;
			}
		}
		return ans;
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
            if (buy.isEmpty()) {  // have nothing, buy it
                buy.push(p);
            } 
            else if (buy.size() == sell.size()) {  // no unsold stock
            	
            	// current price needs a price lower than the previous sold price 
            	// to sell in the future, buy it.
                if (p < sell.peek() - fee) {
                    buy.push(p);
                }
                
                // selling at current price for the last transaction can have higher 
                // profit, regret the last sell.
                else if (p > sell.peek()) {
                    sell.pop();
                    sell.push(p);
                }
            } 
            else {  // have an unsold share
            	
            	// if selling at current price have profit, do it.
                if (p > buy.peek() + fee) {
                    sell.push(p);
                }
                
                // if current price is lower than the most recent purchase, 
                // regret it and buy today.
                else if (p < buy.peek()) {
                    buy.pop();
                    buy.push(p);
                }
            }
        }
        if (buy.size() > sell.size()) {  // discard the unsold buy attempt
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
		for (int i = 1; i < days; i++) {
        	// keep the same as day i-1, or buy from sell status at day i-1
            buy[i] = Math.max(buy[i - 1], sell[i - 1] - prices[i]);
            
            // keep the same as day i-1, or sell from buy status at day i-1
            sell[i] = Math.max(sell[i - 1], buy[i - 1] + prices[i] - fee);
        }
        return sell[days - 1];
    }

}
