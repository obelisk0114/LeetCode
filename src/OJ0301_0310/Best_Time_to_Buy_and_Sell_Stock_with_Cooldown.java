package OJ0301_0310;

// https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-cooldown/discuss/75924/Most-consistent-ways-of-dealing-with-the-series-of-stock-problems

public class Best_Time_to_Buy_and_Sell_Stock_with_Cooldown {
	/*
	 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-cooldown/discuss/75930/Very-Easy-to-Understand-One-Pass-O(n)-Solution-with-No-Extra-Space
	 * 
	 * First, think about what we can do on day i? You either have one stock or you 
	 * don't on day i. For each case, you have two options, making a total of four 
	 * possible actions on day i:
	 * 
	 * you have 1 stock and you sell it
	 * you have 1 stock and you do nothing
	 * you have 0 stock and you buy stock i
	 * you have 0 stock and you do nothing
	 * 
	 * if you take action 1 on day i ==> 
	 *    you have either taken action 2 or 3 on day i-1
	 * if you take action 2 on day i ==> 
	 *    you have either taken action 2 or 3 on day i-1
	 * if you take action 3 on day i ==> 
	 *    you must have taken action 4 on day i-1 (you can not sell on day i-1 due to cool down)
	 * if you take action 4 on day i ==> 
	 *    you have either taken action 1 or 4 on day i-1
	 * 
	 * Now you want to maximize your total profit, but you don't know what action to 
	 * take on day i such that you get the total maximum profit, so you try all 4 
	 * actions on every day.
	 * 
	 * Before coding, one detail to emphasize is that the initial value on day 0 is 
	 * important. You basically cannot take action 1, so the corresponding profits 
	 * should be 0. You cannot take action 2 in practice, but you cannot set up the 
	 * profit to 0, because that means you don't have a stock to sell on day 1. 
	 * Therefore, the initial profit should be negative value of the first stock. 
	 * You can also think of it as you buy the stock on day -1 and do nothing on day 0.
	 */
	public int maxProfit(int[] prices) {
		if (prices.length < 2)
			return 0;

		int has1_doNothing = -prices[0];
		int has1_Sell = 0;
		int has0_doNothing = 0;
		int has0_Buy = -prices[0];
		for (int i = 1; i < prices.length; i++) {
			int p1 = has0_Buy;
			int p2 = has0_doNothing;
			int p3 = has1_Sell;
			int p4 = has1_doNothing;

			has0_Buy = p2 + -prices[i];
			has0_doNothing = Math.max(p3, p2);
			has1_Sell = Math.max(p1, p4) + prices[i];
			has1_doNothing = Math.max(p1, p4);
		}
		return Math.max(has0_doNothing, has1_Sell);
	}
	
	/*
	 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-cooldown/discuss/75929/7-line-Java:-only-consider-sell-and-cooldown
	 * 
	 * profit1[i] = max profit on day i if I sell
	 * profit2[i] = max profit on day i if I do nothing (historic max profit)
	 * 
	 * 1. profit1[i+1] means I must sell on day i+1, and there are 2 cases:
	 * a. Bought on day i and sell on day i+1
	 * b. Bought before day i and did nothing on day i.
	 * Taking both cases into account, 
	 * profit1[i+1] = max(profit1[i] + prices[i+1] - prices[i], profit2[i])
	 * 
	 * 2. profit2[i+1] means I do nothing on day i+1, so it will be 
	 * max(profit1[i], profit2[i])
	 */
	public int maxProfit_DP_2(int[] prices) {
		int profit1 = 0, profit2 = 0;
		for (int i = 1; i < prices.length; i++) {
			int copy = profit1;
			profit1 = Math.max(profit1 + prices[i] - prices[i - 1], profit2);
			profit2 = Math.max(copy, profit2);
		}
		return Math.max(profit1, profit2);
	}
	
	/*
	 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-cooldown/discuss/75931/Easiest-JAVA-solution-with-explanations
	 * 
	 * buy[i]: Max profit till index i. The series of transaction is ending with a buy.
	 * sell[i]: Max profit till index i. The series of transaction is ending with a sell.
	 * 
	 * Till index i, the buy / sell action must happen and must be the last action. 
	 *     It may not happen at index i. It may happen at i - 1, i - 2, ... 0.
	 * In the end n - 1, return sell[n - 1]. Apparently we cannot finally end up with 
	 *     a buy. In that case, we would rather take a rest at n - 1.
	 * For special case no transaction at all, classify it as sell[i], so that in the 
	 *     end, we can still return sell[n - 1].
	 * 
	 * buy[i]: To make a decision whether to buy at i, we either take a rest, by just 
	 *     using the old decision at i - 1, or sell at/before i - 2, then buy at i, We 
	 *     cannot sell at i - 1, then buy at i, because of cooldown.
	 * sell[i]: To make a decision whether to sell at i, we either take a rest, by just 
	 *     using the old decision at i - 1, or buy at/before i - 1, then sell at i.
	 *     
	 * buy[i] = Math.max(buy[i - 1], sell[i - 2] - prices[i]);   
	 * sell[i] = Math.max(sell[i - 1], buy[i - 1] + prices[i]);
	 * 
	 * Let b2, b1, b0 represent buy[i - 2], buy[i - 1], buy[i]
	 * Let s2, s1, s0 represent sell[i - 2], sell[i - 1], sell[i]
	 * b0 = Math.max(b1, s2 - prices[i]);
	 * s0 = Math.max(s1, b1 + prices[i]);
	 * 
	 * We can buy. The max profit at i = 0 ending with a buy is -prices[0].
	 * We cannot sell. The max profit at i = 0 ending with a sell is 0.
	 * 
	 * Rf : 
	 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-cooldown/discuss/126065/Two-DP-methods-using-graph-for-easy-to-understand-with-detail-explanation
	 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-cooldown/discuss/75927/Share-my-thinking-process
	 */
	public int maxProfit2(int[] prices) {
		if (prices == null || prices.length <= 1)
			return 0;

		int b0 = -prices[0], b1 = b0;
		int s0 = 0, s1 = 0, s2 = 0;

		for (int i = 1; i < prices.length; i++) {
			b0 = Math.max(b1, s2 - prices[i]);
			s0 = Math.max(s1, b1 + prices[i]);
			b1 = b0;
			s2 = s1;
			s1 = s0;
		}
		return s0;
	}
	
	/*
	 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-cooldown/discuss/75998/Very-simple-explanation-based-basic-math-(no-state-machine-etc.-)-O(N)-DP-code-attached
	 * 
	 * profit[i] - maximum profit can be made on day i following the cool down rule
	 * 
	 * profit[i] = Max(prices[i] - prices[j] + profit[j-2], profit[i-1]) for all j < i (1)
	 * prices[i] - prices[j] buying on j and sell on i (1.1)
	 * profit[j-2] accumulated profit from 0 to j-2 (1.2)
	 * profit[i-1] means doing nothing on day i
	 * 
	 * let's simplify using linearity
	 * profit[i] = Max(prices[i] + max(profit[j-2] - prices[j]), profit[i-1]) for all j < i (2)
	 */
	public int maxProfit_DP(int[] prices) {
		int n = prices.length;
		if (n <= 1)
			return 0;
		
		int[] dp = new int[n + 1];
		int max = -prices[0];
		for (int i = 1; i < n; i++) {
			dp[i + 1] = Math.max(dp[i], max + prices[i]);
			max = Math.max(dp[i - 1] - prices[i], max);
		}
		return dp[n];
	}
	
	// https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-cooldown/discuss/76000/Easy-Understand-Java-DP-solution-with-comments
	public int maxProfit_DP_array2(int[] prices) {
        if(prices == null || prices.length < 2){
            return 0;
        }
        
        int len = prices.length;
        int[] sell = new int[len];      // sell[i] means must sell at day i
        int[] cooldown = new int[len];  // cooldown[i] means day i is cooldown day
        sell[1] = prices[1] - prices[0];
        for(int i = 2; i < prices.length; ++i){
            cooldown[i] = Math.max(sell[i - 1], cooldown[i - 1]);
            sell[i] = prices[i] - prices[i - 1] + Math.max(sell[i - 1], cooldown[i - 2]);
        }
        return Math.max(sell[len - 1], cooldown[len - 1]);
    }

}
