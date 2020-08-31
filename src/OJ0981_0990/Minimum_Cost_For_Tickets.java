package OJ0981_0990;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.HashSet;

public class Minimum_Cost_For_Tickets {
	/*
	 * https://leetcode.com/problems/minimum-cost-for-tickets/discuss/227130/Java-DP-Solution-with-detailed-comment-and-explanation
	 * 
	 * dp[i] means up to i-th day the minimum cost of the tickets. The size of the dp 
	 * array depends on the last travel day, so we don't need it to be 365.
	 * 
	 * We do need a boolean array to mark the travel days, the reason is if it is not 
	 * a travel day we don't need a ticket. dp[i] = dp[i - 1], dp[i] = 0 for i <= 0
	 * However, if it is a travel day, we consider three scenarios 
	 * (with three types of tickets):
	 * 
	 * If a 1-day ticket on day i, dp[i] = dp[i - 1] + cost[0]
	 * If a 7-day ticket ending on day i, 
	 *   dp[i] = min(dp[i - 7], dp[i - 6] ... dp[i - 1]) + cost[1]
	 * If a 30-day ticket ending on day i, 
	 *   dp[i] = min(dp[i - 30], dp[i - 29] ... dp[i - 1]) + cost[2]
	 * 
	 * But since the value of dp array is increasing, therefore:
	 * For a 7-day ticket ending on day i, dp[i] = dp[i - 7] + cost[1]
	 * For a 30-day ticket ending on day i, dp[i] = dp[i - 30] + cost[2]
	 * 
	 * Rf :
	 * https://leetcode.com/problems/minimum-cost-for-tickets/discuss/226670/Java-DP-Solution-with-explanation-O(n)
	 * 
	 * Other code:
	 * https://leetcode.com/problems/minimum-cost-for-tickets/discuss/504403/DP-it's-not-easy-as-you-thought
	 */
	public int mincostTickets_dp(int[] days, int[] costs) {
		// length up to the last travel + 1 day is good enough (no need for 365)
		int lastDay = days[days.length - 1];
		
		// dp[i] means up to i-th day the minimum cost of the tickets
		int[] dp = new int[lastDay + 1];
		
		boolean[] isTravelDays = new boolean[lastDay + 1];
		
		// mark the travel days
		for (int day : days)
			isTravelDays[day] = true;

		for (int i = 1; i <= lastDay; i++) {
			// no need to buy ticket if it is not a travel day
			if (!isTravelDays[i]) {
				dp[i] = dp[i - 1];
				continue;
			}
			
			// select which type of ticket to buy
			dp[i] = costs[0] + dp[i - 1]; // 1-day
			dp[i] = Math.min(costs[1] + dp[Math.max(i - 7, 0)], dp[i]); // 7-day
			dp[i] = Math.min(costs[2] + dp[Math.max(i - 30, 0)], dp[i]); // 30-day
		}
		return dp[lastDay];
	}
	
	/*
	 * https://leetcode.com/problems/minimum-cost-for-tickets/discuss/226659/Two-DP-solutions-with-pictures/335444
	 * 
	 * Track travel days
	 * 
	 * We track the minimum cost for each travel day. We process only travel days and 
	 * store {day, cost} for 7-and 30-day passes in the last7 and last30 queues. After 
	 * a pass 'expires', we remove it from the queue. This way, our queues only 
	 * contains travel days for the last 7 and 30 days, and the cheapest pass prices 
	 * are in the front of the queues.
	 * 
	 * Time Complexity: O(n), where n is the number of travel days.
	 * Space Complexity: O(38). Stricter, it's a sum of duration for all pass types 
	 * (1 + 7 + 30 in our case).
	 * 
	 * The cost is increasing with respect to time and we are looking for a minimum 
	 * cost, so you want to choose as early as possible, i.e. dp[i - 7] instead of 
	 * dp[i - 6], dp[i - 5]....
	 * 
	 * Rf :
	 * https://leetcode.com/problems/minimum-cost-for-tickets/discuss/226659/Two-DP-solutions-with-pictures
	 * https://leetcode.com/problems/minimum-cost-for-tickets/discuss/226659/Two-DP-solutions-with-pictures/335702
	 * https://leetcode.com/problems/minimum-cost-for-tickets/discuss/226659/Two-DP-solutions-with-pictures/231174
	 */
	public int mincostTickets_travel_days(int[] days, int[] costs) {
		// using queue so that the oldest ticket is at the top.
		Queue<int[]> last7days = new LinkedList<>(), last30days = new LinkedList<>();

		int totalCost = 0;
		for (int i = 0; i < days.length; i++) {
			// discarding expired 7days pass
			while (!last7days.isEmpty() && last7days.peek()[0] + 7 <= days[i]) {
				last7days.poll();
			}

			last7days.offer(new int[] { days[i], totalCost + costs[1] });

			// discarding expired 30 days pass.
			while (!last30days.isEmpty() && last30days.peek()[0] + 30 <= days[i]) {
				last30days.poll();
			}

			last30days.offer(new int[] { days[i], totalCost + costs[2] });

			// taking the min of daily pass and current valid 7 days or 30 days pass.
			totalCost = Math.min(totalCost + costs[0], 
					Math.min(last30days.peek()[1], last7days.peek()[1]));
		}

		return totalCost;
	}
	
	/*
	 * https://leetcode.com/problems/minimum-cost-for-tickets/discuss/226659/Two-DP-solutions-with-pictures/299687
	 * 
	 * For each travel day, we can buy a one-day ticket, or use 7-day or 30-day pass 
	 * as if we would have purchased it 7 or 30 days ago. We need to track rolling 
	 * costs for at least 30 days back, and use them to pick the cheapest option for 
	 * the next travel day.
	 * 
	 * Track cost for all calendar days
	 * 
	 * We track the minimum cost for all calendar days in dp. For non-travel days, the 
	 * cost stays the same as for the previous day. For travel days,
	 * dp = min(yesterday + single-day ticket, 8 days ago + 7-day pass, 
	 *          31 days ago + 30-day pass)
	 * 
	 * Since we only look 30 days back, we can just store the cost for last 30 days in 
	 * a rolling array. In addition, we can only look at calendar days within our 
	 * first and last travel dates
	 * 
	 * Time Complexity: O(N), where N is the number of calendar days.
	 * Space Complexity: O(N) or O(31) for the optimized solution. Stricter, it's a 
	 * maximum duration among all pass types.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/minimum-cost-for-tickets/discuss/226659/Two-DP-solutions-with-pictures
	 */
	public int mincostTickets_dp_30Days(int[] days, int[] costs) {
		int[] dp = new int[30];
		int d = 0; // d means the index of next travel day
		int lastday = days[days.length - 1];

		for (int i = days[0]; i <= lastday; i++) {
			// we don't have this day for travel, price as yesterday
			if (i != days[d])
				dp[i % 30] = dp[(i - 1) % 30];
			
			// i == days[d]
			else {
				dp[i % 30] = Math.min(dp[(i - 1) % 30] + costs[0],
						     Math.min(dp[Math.max(i - 7, 0) % 30] + costs[1], 
							       	dp[Math.max(i - 30, 0) % 30] + costs[2]));
				d++;
			}
		}

		return dp[lastday % 30];
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/minimum-cost-for-tickets/discuss/533146/Java-Recursion-with-memoization-Time-O(n)-Space-O(n)
	 * 
	 * If you don't have to travel today, then it's strictly better to wait to buy a 
	 * pass. 
	 * 
	 * dp(i) is the cost to fulfill your travel plan from day i to the end of the plan.
	 * 
	 * For each day that you need to buy a ticket you will try the three options
	 * 
	 * 1. Buying a one-day ticket and then continuing with the rest of the days
	 * 2. Buying a seven-day ticket and skipping all the days included in this ticket
	 * 3. Buying a thirty-day ticket and skipping all the days included in this ticket
	 * 
	 * The time complexity in this case will be O(n) as each day is computed at most 
	 * once and saved in dp array to be used for later calls
	 * The space complexity will be O(n) for the dp array.
	 * The recursion stack will also never surpass O(n) calls (in the case of trying 
	 * to buy all one-day tickets)
	 * 
	 * Rf :
	 * https://leetcode.com/problems/minimum-cost-for-tickets/solution/
	 * 
	 * Other code:
	 * https://leetcode.com/problems/minimum-cost-for-tickets/discuss/227321/Top-down-DP-Logical-Thinking
	 * https://leetcode.com/problems/minimum-cost-for-tickets/discuss/504403/DP-it's-not-easy-as-you-thought/632843
	 */
	public int mincostTickets_topDown(int[] days, int[] costs) {
		int[] dp = new int[days.length];
		return minCostTickets_topDown(days, costs, 0, dp);
	}

	public int minCostTickets_topDown(int[] days, int[] costs, int day, int[] dp) {
		if (day == days.length)
			return 0;
		
		// return previously calculated day
		if (dp[day] != 0)
			return dp[day];
		
		int i;

		// Attempt to buy a one-day ticket
		int buyOneDay = minCostTickets_topDown(days, costs, day + 1, dp) + costs[0];

		// Attempt to buy a seven-day ticket and skip all days that will be
		// included in this ticket
		for (i = day; i < days.length; i++)
			if (days[i] >= days[day] + 7)
				break;
		
		int buySevenDays = minCostTickets_topDown(days, costs, i, dp) + costs[1];

		// Attempt to buy a thirty-day ticket and skip all days that will be
		// included in this ticket
		for (i = day; i < days.length; i++)
			if (days[i] >= days[day] + 30)
				break;
		
		int buyThirtyDays = minCostTickets_topDown(days, costs, i, dp) + costs[2];

		// return minimum of three options
		int result = Math.min(Math.min(buyOneDay, buySevenDays), buyThirtyDays);
		dp[day] = result;
		return result;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/minimum-cost-for-tickets/discuss/226704/Java-O(nlogn)-DP-solution-and-recursion-%2B-memorization-solution
	 * 
	 * dp[i] is the minimum cost to complete the trip for the first i entries in days 
	 * array...
	 */
	public int mincostTickets_dp_binarySearch(int[] days, int[] costs) {
		if (days == null || days.length == 0)
			return 0;
		
		int n = days.length;
		int[] dp = new int[n + 1];
		for (int i = 0; i < n; i++) {
			// previous cost + today's cost...
			int res = dp[i] + costs[0];

			// find the left most index that is larger than days[i] - 7..
			// calculate the cost by using prev cost + one-week cost...
			int index7 = binarySearch_dp_binarySearch(days, i, days[i] - 7);
			res = Math.min(res, dp[index7] + costs[1]);

			// find the left most index that is larger than days[i] - 30..
			// calculate the cost by using prev cost + one-month cost...
			int index30 = binarySearch_dp_binarySearch(days, i, days[i] - 30);
			res = Math.min(res, dp[index30] + costs[2]);
			
			dp[i + 1] = res;
		}
		return dp[n];
	}

	private int binarySearch_dp_binarySearch(int[] days, int i, int target) {
		int lo = 0, hi = i;
		while (lo < hi) {
			int mid = lo + (hi - lo) / 2;
			if (days[mid] > target) {
				hi = mid;
			} 
			else {
				lo = mid + 1;
			}
		}
		return lo;
	}
	
	/*
	 * The following 3 variables and 2 functions are from this link.
	 * https://leetcode.com/problems/minimum-cost-for-tickets/solution/
	 * 
	 * Approach 1: Dynamic Programming (Day Variant)
	 * 
	 * For each day, if you don't have to travel today, then it's strictly better to 
	 * wait to buy a pass. If you have to travel today, you have up to 3 choices: you 
	 * must buy either a 1-day, 7-day, or 30-day pass.
	 * 
	 * We can express those choices as a recursion and use dynamic programming. Let's 
	 * say dp(i) is the cost to fulfill your travel plan from day i to the end of the 
	 * plan. Then, if you have to travel today, your cost is:
	 * 
	 * dp(i) = min(dp(i+1) + costs[0], dp(i+7) + costs[1], dp(i+30) + costs[2])
	 */
	int[] costs_topDown2;
    Integer[] memo_topDown2;
    Set<Integer> dayset_topDown2;

    public int mincostTickets_topDown2(int[] days, int[] costs) {
		this.costs_topDown2 = costs;
		memo_topDown2 = new Integer[366];
		dayset_topDown2 = new HashSet<>();
		for (int d : days)
			dayset_topDown2.add(d);

		return dp_topDown2(1);
    }
    
    public int dp_topDown2(int i) {
		if (i > 365)
			return 0;
		if (memo_topDown2[i] != null)
			return memo_topDown2[i];

		int ans;
		if (dayset_topDown2.contains(i)) {
			ans = Math.min(dp_topDown2(i + 1) + costs_topDown2[0], 
					dp_topDown2(i + 7) + costs_topDown2[1]);
			
			ans = Math.min(ans, dp_topDown2(i + 30) + costs_topDown2[2]);
		} 
		else {
			ans = dp_topDown2(i + 1);
		}

		memo_topDown2[i] = ans;
		return ans;
    }
    
    /*
     * The following variable and 3 functions are from this link.
     * https://leetcode.com/problems/minimum-cost-for-tickets/discuss/226704/Java-O(nlogn)-DP-solution-and-recursion-%2B-memorization-solution
     * 
     * recursion + memorization...
     * memo[i] is the minimum cost from days[i] to days[days.length - 1];
     */
	Integer[] memo_topDown_binarySearch;

	public int mincostTickets_topDown_binarySearch(int[] days, int[] costs) {
		if (days == null || days.length == 0)
			return 0;
		
		int n = days.length;
		memo_topDown_binarySearch = new Integer[n];
		return minCost_topDown_binarySearch(days, costs, 0);
	}

	private int minCost_topDown_binarySearch(int[] days, int[] costs, int lo) {
		int n = days.length;
		if (lo >= n)
			return 0;
		if (memo_topDown_binarySearch[lo] != null)
			return memo_topDown_binarySearch[lo];
		
		int res = costs[0] + minCost_topDown_binarySearch(days, costs, lo + 1);

		// use binary search to find the right most index that has 
		// days[i] < days[lo] + 7...
		int index7 = binarySearch_topDown_binarySearch(days, lo, days[lo] + 7);
		res = Math.min(res, 
				costs[1] + minCost_topDown_binarySearch(days, costs, index7 + 1));

		// use binary search to find the right most index that has 
		// days[i] < days[lo] + 30...
		int index30 = binarySearch_topDown_binarySearch(days, lo, days[lo] + 30);
		res = Math.min(res, 
				costs[2] + minCost_topDown_binarySearch(days, costs, index30 + 1));

		memo_topDown_binarySearch[lo] = res;
		return res;
	}

	private int binarySearch_topDown_binarySearch(int[] days, int lo, int target) {
		int hi = days.length - 1;
		while (lo < hi) {
			int mid = lo + (hi - lo + 1) / 2;
			if (days[mid] < target) {
				lo = mid;
			} 
			else {
				hi = mid - 1;
			}
		}
		return lo;
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/minimum-cost-for-tickets/discuss/228421/Python-solution
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/minimum-cost-for-tickets/discuss/226659/Two-DP-solutions-with-pictures
     * https://leetcode.com/problems/minimum-cost-for-tickets/discuss/630868/explanation-from-someone-who-took-2-hours-to-solve
     * https://leetcode.com/problems/minimum-cost-for-tickets/discuss/462201/Diego's-Understandable-Explanations-C%2B%2B
     * https://leetcode.com/problems/minimum-cost-for-tickets/discuss/227106/C%2B%2B-concise-4-line-0ms
     */

}
