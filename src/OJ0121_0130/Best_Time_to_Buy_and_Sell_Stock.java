package OJ0121_0130;

public class Best_Time_to_Buy_and_Sell_Stock {
	/*
	 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock/discuss/39060/Very-Simple-Java-Solution-with-detail-explanation-(1ms-beats-96)
	 * 
	 * for prices[0] .... prices[n], prices[n+1].....
	 * if (prices[n] < prices[0]) then, the max profit is in prices[0]...prices[n], or 
	 * begin from prices[n+1], otherwise, suppose prices[n] > prices[0], and max profit 
	 * is happened between prices[n+1] , and prices[n+k], then if we buy at day 0, and 
	 * sell at day n+k, we get a bigger profit.
	 * 
	 * Rf : https://leetcode.com/problems/best-time-to-buy-and-sell-stock/discuss/39075/A-O(1*n)-solution
	 */
	public int maxProfit(int[] prices) {
		int ans = 0;
		if (prices.length == 0) {
			return ans;
		}
		
		int bought = prices[0];
		for (int i = 1; i < prices.length; i++) {
			if (prices[i] > bought) {
				if (ans < (prices[i] - bought)) {
					ans = prices[i] - bought;
				}
			} 
			else {
				bought = prices[i];
			}
		}
		return ans;
	}
	
	// by myself
	public int maxProfit_self(int[] prices) {
        int max = 0;
        int buy = Integer.MAX_VALUE;
        
        for (int price : prices) {
            if (price < buy) {
                buy = price;
            }
            else {
                max = Math.max(price - buy, max);
            }
        }
        return max;
    }
	
	/*
	 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock/discuss/39038/Kadane's-Algorithm-Since-no-one-has-mentioned-about-this-so-far-:)-(In-case-if-interviewer-twists-the-input)
	 * 
	 * The logic is to calculate the difference (maxCur += prices[i] - prices[i-1]) of 
	 * the original array, and find a contiguous subarray giving maximum profit. 
	 * If the difference falls below 0, reset it to zero.
	 * 
	 * maxCur = current maximum value
	 * maxSoFar = maximum value found so far
	 * 
	 * Rf :
	 * leetcode.com/problems/best-time-to-buy-and-sell-stock/discuss/39038/Kadane's-Algorithm-Since-no-one-has-mentioned-about-this-so-far-:)-(In-case-if-interviewer-twists-the-input)/36813
	 * leetcode.com/problems/best-time-to-buy-and-sell-stock/discuss/39038/Kadane's-Algorithm-Since-no-one-has-mentioned-about-this-so-far-:)-(In-case-if-interviewer-twists-the-input)/116758
	 */
	public int maxProfit_max_subarray(int[] prices) {
		int maxCur = 0, maxSoFar = 0;
		for (int i = 1; i < prices.length; i++) {
			maxCur = Math.max(0, maxCur += prices[i] - prices[i - 1]);
			maxSoFar = Math.max(maxCur, maxSoFar);
		}
		return maxSoFar;
	}
	
	// https://leetcode.com/problems/best-time-to-buy-and-sell-stock/discuss/39153/Simplest-DP-solution-in-Java
	public int maxProfit3(int[] prices) {
		int min = Integer.MAX_VALUE;
		int max = 0;

		for (int p : prices) {
			min = Math.min(min, p);
			max = Math.max(p - min, max);
		}

		return max;
	}
	
	// https://leetcode.com/problems/best-time-to-buy-and-sell-stock/discuss/39171/Java-6-lines-4-lines-2-lines-solutions
	
	// brute force
	public int maxProfit_brute_force(int[] prices) {
        int ans = 0;
        for (int i = 0; i < prices.length; i++) {
            for (int j = i + 1; j < prices.length; j++) {
                ans = Math.max(ans, prices[j] - prices[i]);
            }
        }
        return ans;
    }

}
