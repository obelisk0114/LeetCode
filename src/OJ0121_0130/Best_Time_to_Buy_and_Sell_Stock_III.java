package OJ0121_0130;

public class Best_Time_to_Buy_and_Sell_Stock_III {
	/*
	 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iii/discuss/39611/Is-it-Best-Solution-with-O(n)-O(1).
	 * 
	 * release1 means we decide to sell the stock, after selling it we have price[i] 
	 * money and we have to give back the money we owed, so we have 
	 * price[i] - |hold1| = prices[i] + hold1, we want to make this max.
	 * 
	 * hold2 means we want to buy another stock, we already have release1 money, so 
	 * after buying stock2 we have hold2 = release1 - price[i] money left, we want 
	 * more money left, so we make it max
	 * 
	 * release2 means we want to sell stock2, we can have price[i] money after 
	 * selling it, and we have hold2 money left before, so 
	 * release2 = hold2 + prices[i], we make this max.
	 * 
	 * Rf :
	 * leetcode.com/problems/best-time-to-buy-and-sell-stock-iii/discuss/39615/My-explanation-for-O(N)-solution!/37469
	 * 
	 * Other code:
	 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iii/discuss/39653/2ms-Java-DP-Solution
	 */
	public int maxProfit(int[] prices) {
        int hold1 = Integer.MIN_VALUE, hold2 = Integer.MIN_VALUE;
        int release1 = 0, release2 = 0;
		for (int i : prices) {                            // Assume we only have 0 money at first
			release2 = Math.max(release2, hold2 + i);     // The maximum if we've just sold 2nd stock so far.
			hold2    = Math.max(hold2,    release1 - i);  // The maximum if we've just buy  2nd stock so far.
			release1 = Math.max(release1, hold1 + i);     // The maximum if we've just sold 1nd stock so far.
			hold1    = Math.max(hold1,    -i);            // The maximum if we've just buy  1st stock so far. 
        }
        return release2; // Since release1 is initiated as 0, so release2 will always higher than release1.
    }
	
	/*
	 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iii/discuss/39727/A-clear-o(n)-time-and-space-Java-Solution
	 * 
	 * Go from left to right and calculate max profit for each index (i). 
	 * Go from right to left and calculate max profit for (i). 
	 * Add max right profit for (i) and max left profit for (i-1) and check if it's max profit.
	 */
	public int maxProfit_2_left_right_DP(int[] prices) {
		int len = prices.length;
		if (len < 2)
			return 0;
		
		int[] maxBefore = new int[len];
		int min = prices[0];
		for (int i = 1; i < len; i++) {
			maxBefore[i] = Math.max(maxBefore[i - 1], prices[i] - min);
			min = Math.min(min, prices[i]);
		}
		
		int max = prices[len - 1];
		int ret = 0;
		for (int i = len - 2; i >= 0; i--) {
			ret = Math.max(ret, max - prices[i] + maxBefore[i]); // Only "max - prices[i] + maxBefore[i]" will affect ret
			max = Math.max(prices[i], max);
		}
		return ret;
	}
	
	// https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iii/discuss/39711/Another-accepted-Java-solution-(scan-from-left-and-right)
	public int maxProfit_2_left_right_DP2(int[] prices) {
        if (prices == null || prices.length == 0) 
            return 0;
        
        int n = prices.length;
        int profit = 0;
        
        // scan from left
        // left[i] keeps the max profit from 0 to i
        int[] left = new int[n];
        int min = prices[0];
        
        for (int i = 1; i < n; i++) {
            left[i] = Math.max(left[i - 1], prices[i] - min);
            min = Math.min(min, prices[i]);
        }
        
        // scan from right
        // right[i] keeps the max profit from i to n - 1
        int[] right = new int[n];
        int max = prices[n - 1];
        
        for (int i = n - 2; i >= 0; i--) {
            right[i] = Math.max(right[i + 1], max - prices[i]);
            max = Math.max(max, prices[i]);
            
            profit = Math.max(profit, left[i] + right[i]);
        }
        
        return profit;
    }
	
	/*
	 * The following 3 functions are from this link.
	 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iii/discuss/39700/Don't-need-DP-to-solve-it-within-O(n)
	 * 
	 * 1. Get the max profit with one transaction to the full array. 
	 *    Keep down the start and end positions.
	 * 2. the start and end positions will be included in the result of two 
	 *    transaction. It falls into two categories:
	 * A) it is one full transaction, 
	 * B) they belong to two separate transactions(start belongs to first transaction 
	 *    and end belongs to second transaction).
	 * 3. if A)-- get max profit with one transaction to subarray from 0 to start; 
	 *            get max profit with one transaction to subarray from end to 
	 *            prices.length.
	 * 4. if B)-- get the max profit with one transaction within start and end in 
	 *            reverse order
	 * 5. return the max profit in those cases.
	 * 
	 * For B), add more explanations.
	 * 1. If it doesn't belong to any transactions, we can remove the smaller one and
	 *    add this transaction to increase the profit.
	 * 2. In all points before end point, start point must be the lowest one. If it is
	 *    not, we can remove it and add the lower one to get the lower price at buy.
	 *    In all points after start point, end point must be the highest one. If it is
	 *    not, we can remove it and add the higher one to get the higher price at sell.
	 * 3. If start point is not the start point of first transaction, we can remove it
	 *    and add the start point as the start point of first transaction to get the
	 *    lower price at buy. If the end point of the first transaction is after end
	 *    point, we can remove it and add the end point as the end point of the first
	 *    transaction to get the higher price at sell. This situation belongs to A).
	 * 4. If the start point of the second transaction is after end point, we can 
	 *    remove the end point of the first transaction and add end point as the end
	 *    point of the first transaction to get the higher price at sell. This 
	 *    situation belongs to A).
	 * 5. If the end point of the second transaction is after end point, we can remove 
	 *    it and add end point as the end point of the second transaction to get the
	 *    higher price at sell.
	 * 
	 * Combining 1-5, start belongs to first transaction and 
	 * end belongs to second transaction.
	 * 
	 * For step 4, add more explanations.
	 * Let first transaction in [start1, end1], second transaction in [start2, end2]
	 * [start1, end2] is the largest one in whole prices if we only do one transaction.
	 * 
	 * max{ (prices[end1] - prices[start1]) + (prices[end2] - prices[start2]) }
	 * = max{ (prices[end2] - prices[start1]) + (prices[end1] - prices[start2]) }
	 * = (prices[end2] - prices[start1]) + max{ prices[end1] - prices[start2] }
	 * 
	 * We can get "max{ prices[end1] - prices[start2] }" by getting the max profit 
	 * with one transaction within start and end in reverse order. 
	 */
	public int maxProfit_split(int[] prices) {
		if (prices.length < 2) {
			return 0;
		}

		int profit = 0;
		int candidateIndexLow = 0, indexLow = 0, indexHigh = 0;
		int lowestPrice = prices[0];
		int maxprofit0 = 0;
		for (int i = 1; i < prices.length; i++) {
			if (prices[i] < lowestPrice) {
				lowestPrice = prices[i];
				candidateIndexLow = i;
			}

			if (prices[i] - lowestPrice > maxprofit0) {
				maxprofit0 = prices[i] - lowestPrice;
				indexHigh = i;
				indexLow = candidateIndexLow;
			}
		}

		int leftMaxProfit = maxProfitInRange(prices, 0, indexLow - 1);
		int rightMaxProfit = maxProfitInRange(prices, indexHigh + 1, prices.length - 1);
		int insideMaxProfit = maxProfitInRange2(prices, indexLow + 1, indexHigh - 1);

		int maxOne = Math.max(leftMaxProfit, rightMaxProfit);
		maxOne = Math.max(maxOne, insideMaxProfit);
		profit = maxprofit0 + maxOne;

		return profit;
	}

	public int maxProfitInRange2(int[] prices, int startIdx, int endIdx) {
		if (startIdx >= endIdx) {
			return 0;
		}

		// reverse
		int profit = 0;
		int lowestPrice = prices[endIdx];
		for (int i = endIdx - 1; i >= startIdx; i--) {
			if (prices[i] < lowestPrice) {
				lowestPrice = prices[i];
			} 
			else {
				profit = Math.max(prices[i] - lowestPrice, profit);
			}
		}
		return profit;
	}

	public int maxProfitInRange(int[] prices, int startIdx, int endIdx) {
		if (endIdx < 0) {
			return 0;
		}
		if (startIdx >= prices.length) {
			return 0;
		}
		
		int profit = 0;
		int lowestPrice = prices[startIdx];
		for (int i = startIdx + 1; i <= endIdx; i++) {
			if (prices[i] < lowestPrice) {
				lowestPrice = prices[i];
			} 
			else {
				profit = Math.max(prices[i] - lowestPrice, profit);
			}
		}
		return profit;
	}
	
	// by myself
	public int maxProfit_self(int[] prices) {
        if (prices.length < 2)
            return 0;
        
        int[] buy1 = new int[prices.length];
        int[] sell1 = new int[prices.length];
        int[] buy2 = new int[prices.length];
        int[] sell2 = new int[prices.length];
        buy1[0] = -prices[0];
        buy2[0] = -prices[0];
        for (int i = 1; i < prices.length; i++) {
            buy1[i] = Math.max(buy1[i - 1], -prices[i]);
            sell1[i] = Math.max(sell1[i - 1], buy1[i - 1] + prices[i]);
            buy2[i] = Math.max(buy2[i - 1], sell1[i - 1] - prices[i]);
            sell2[i] = Math.max(sell2[i - 1], buy2[i - 1] + prices[i]);
        }
        return sell2[prices.length - 1];
    }
	
	// by myself
	public int maxProfit_self2(int[] prices) {
        if (prices.length < 2)
            return 0;
        
        int buy1 = -prices[0];
        int sell1 = 0;
        int buy2 = -prices[0];
        int sell2 = 0;
        for (int i = 1; i < prices.length; i++) {
            int preBuy1 = buy1;
            int preBuy2 = buy2;
            int preSell1 = sell1;
            buy1 = Math.max(buy1, -prices[i]);
            sell1 = Math.max(sell1, preBuy1 + prices[i]);
            buy2 = Math.max(buy2, preSell1 - prices[i]);
            sell2 = Math.max(sell2, preBuy2 + prices[i]);
        }
        return sell2;
    }
	
	// https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iii/discuss/39608/A-clean-DP-solution-which-generalizes-to-k-transactions
	
	// https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iii/discuss/39712/Java-solution-one-iteration-(O(n)-time)-constant-space-with-explanation

}
