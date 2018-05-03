package OJ0121_0130;

import java.util.LinkedList;

public class Best_Time_to_Buy_and_Sell_Stock_II {
	/*
	 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock-ii/discuss/39536/Simple-4-line-Solution-Sorry-angie-yunqi!-:D
	 * 
	 * Define D[i] = Prices[i] - Prices[i-1] (difference between 2 consecutive prices)
	 * 
	 * the profit of the transaction
	 * = Prices[Y] - Prices[X] 
	 * = Prices[Y] - Prices[Y-1] +
	 *   Prices[Y-1] - Prices[Y-2] ...
	 *   Prices[X+1] - Prices[X] 
	 * = D[Y] + D[Y-1] + ... + D[X+1]
	 * = sum of D from X+1 to Y
	 * 
	 * if D[Y+1] is positive, it means Prices[Y+1] > Prices[Y], which implies I should 
	 * sell at Prices[Y+1] instead of Prices[Y]. Basically it means I just add D[Y+1] 
	 * to D[Y] + ... + D[X+1].
	 * 
	 * Now we are allowed unlimited transactions. So if there is a negative D, we 
	 * could just break the sequence into 2, that is, into 2 transactions so as to 
	 * avoid the negative element.
	 * 
	 * Rf : https://leetcode.com/problems/best-time-to-buy-and-sell-stock-ii/discuss/39404/Shortest-and-fastest-solution-with-explanation.-You-can-never-beat-this.
	 */
	public int maxProfit(int[] prices) {
		int result = 0;
		for (int i = 1; i < prices.length; i++)
			result += Math.max(prices[i] - prices[i - 1], 0);
		return result;
	}
	
	/*
	 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock-ii/discuss/39402/Is-this-question-a-joke
	 * 
	 * (a[i] - a[i-1]) + (a[i-1] - a[i-2]) = a[i] - a[i-2] 
	 * which is the profits created by i and i-2
	 * 
	 * continually calculate the difference of i and i-1,
	 * we only sum those positive profits then the final results is the maximum profits
	 * 
	 * Rf : https://leetcode.com/problems/best-time-to-buy-and-sell-stock-ii/discuss/39441/A-simple-solution-with-O(n)-time-and-O(1)-space
	 */
	public int maxProfit2(int[] prices) {
		int total = 0;
		for (int i = 1; i < prices.length; i++) {
			if (prices[i] > prices[i - 1])
				total += prices[i] - prices[i - 1];
		}

		return total;
	}
	
	/*
	 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock-ii/discuss/39531/Java-O(n)-solution-if-we're-not-greedy
	 * 
	 * finding next local minimum and next local maximum
	 */
	public int maxProfit_local_minimum_maximum(int[] prices) {
		int profit = 0, i = 0;
		while (i < prices.length) {
			// find next local minimum
			while (i < prices.length - 1 && prices[i + 1] <= prices[i])
				i++;
			int min = prices[i++]; // need increment to avoid infinite loop for "[1]"
			
			// find next local maximum
			while (i < prices.length - 1 && prices[i + 1] >= prices[i])
				i++;
			
			profit += i < prices.length ? prices[i++] - min : 0;
		}
		return profit;
	}
	
	// by myself
	public int maxProfit_self2(int[] prices) {
        int ans = 0;
        if (prices.length == 0)
            return ans;
        
        int post = prices[0];
        int pre = prices[0];
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > post) {
                post = prices[i];
            }
            else if (prices[i] < post) {
                ans = ans + (post - pre);
                pre = prices[i];
                post = prices[i];
            }
        }
        
        return ans + post - pre;
    }
	
	// by myself
	public int maxProfit_self(int[] prices) {
        int ans = 0;
        if (prices.length == 0)
            return ans;
        
        LinkedList<Integer> Stack = new LinkedList<Integer>();
        Stack.add(prices[0]);
        for (int i = 1; i < prices.length; i++) {
            int top = Stack.getLast();
            if (prices[i] < top) {
                int bottom = 0;
                while (!Stack.isEmpty()) {
                    bottom = Stack.removeLast();
                }
                ans = ans + (top - bottom);
            }
            Stack.add(prices[i]);
        }
        
        if (Stack.size() > 1) {
            int lastTurn = Stack.getLast() - Stack.getFirst();
            ans = Math.max(ans, ans + lastTurn);
        }
        return ans;
    }

}
