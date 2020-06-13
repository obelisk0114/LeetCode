package OJ1021_1030;

import java.util.Arrays;

public class Two_City_Scheduling {
	/*
	 * https://leetcode.com/problems/two-city-scheduling/discuss/668155/Two-Approach-or-Detailed-Explanation-or-Clean-Code-or-1ms-or
	 * 
	 * Example 1: [[10,20],[30,200],[400,50],[30,20]]
	 * 
	 * How much money can we save if we fly a person to A vs. B? To minimize the total 
	 * cost, we should fly the person with the maximum saving to A, and with the 
	 * minimum - to B.
	 * 
	 * P1 [10, 20] -> 10 - 20 = -10 
	 * P2 [30, 200] -> 30 - 200 = -170 
	 * P3 [400, 50] -> 400 - 50 = 350 
	 * P4 [30, 20] -> 30 - 20 = 10 
	 * 
	 * Sort based on the cost of diff of sending A to B -> -170, -10, 10, 350 
	 * ([[30, 200], [10, 20], [30, 20], [400, 50]])
	 * P2 P1 P4 P3 -> A = P2, P1 -> 30 + 10 = 40 (first N to A)
     *                B = P4, P3 -> 20 + 50 = 70 (last N to B)
	 *    		      A + B = 40 + 70 = 110
	 * 
	 * Time Complexity: O(NlogN), where N is the length of costs array.
	 * 
	 * p0 and p1, if p0[0] - p0[1] < p1[0] - p1[1], it equals to 
	 * p0[0] + p1[1] < p1[0] + p0[1], which means p0 to A and p1 to B is cheaper than 
	 * vice verse.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/two-city-scheduling/discuss/278716/C++-O(n-log-n)-sort-by-savings/265369
	 * https://leetcode.com/problems/two-city-scheduling/discuss/282746/The-Reason-why-simple-sort-work.
	 * https://leetcode.com/problems/two-city-scheduling/discuss/278716/C++-O(n-log-n)-sort-by-savings/339547
	 * https://leetcode.com/problems/two-city-scheduling/discuss/278898/Java-2ms-sorting-solution-with-explanation
	 * 
	 * Other code:
	 * https://leetcode.com/problems/two-city-scheduling/discuss/278771/Java-sort-solution
	 * https://leetcode.com/problems/two-city-scheduling/discuss/667797/Simple-Java-solution-with-sort
	 * https://leetcode.com/problems/two-city-scheduling/discuss/292976/Java-DP-and-Greedy-Explained
	 */
	public int twoCitySchedCost_sort(int[][] costs) {
        Arrays.sort(costs, (p1, p2) -> (p1[0] - p1[1]) - (p2[0] - p2[1]));
        int minCost = 0;
        for (int i = 0; i < costs.length / 2; i++) {
            minCost += costs[i][0];                      // send first N to A
            minCost += costs[costs.length / 2 + i][1];   // send last N to B
        }
        return minCost;
    }
	
	/*
	 * https://leetcode.com/problems/two-city-scheduling/discuss/278731/Java-DP-Easy-to-Understand
	 * 
	 * dp[i][j] represents the cost when considering first (i + j) people in which i 
	 * people assigned to city A and j people assigned to city B.
	 * 
	 * for (i + j)th people, he can be assigned either to A city or B city,
	 * A: dp[i-1][j] + costs[i+j-1][0]; //because it is to A, so we should use i-1
	 * B: dp[i][j-1] + costs[i+j-1][1]; //because it is to B, so we should use j-1
	 * dp[i][j] = Math.min(dp[i-1][j] + costs[i+j-1][0], dp[i][j-1] + costs[i+j-1][1]);
	 * 
	 * another way to represent the dp equation is: dp[totalPerson][personToA], 
	 * toatalPerson is the number of people have been assigned, and personToA of them 
	 * are assigned to city A, so the the equation:
	 * 
	 * dp[totalPerson][personToA] = Math.min(
	 * dp[totalPerson-1][personToA] + costs[totalPerson-1][1], //the last one to B
	 * dp[totalPerson-1][personToA-1] + costs[totalPerson-1][0]);//the last one to A
	 * 
	 * Rf :
	 * https://leetcode.com/problems/two-city-scheduling/discuss/278731/Java-DP-Easy-to-Understand/265607
	 * https://leetcode.com/problems/two-city-scheduling/discuss/278731/Java-DP-Easy-to-Understand/407870
	 */
	public int twoCitySchedCost_dp(int[][] costs) {
		int N = costs.length / 2;
		int[][] dp = new int[N + 1][N + 1];
		for (int i = 1; i <= N; i++) {
			dp[i][0] = dp[i - 1][0] + costs[i - 1][0];
		}
		for (int j = 1; j <= N; j++) {
			dp[0][j] = dp[0][j - 1] + costs[j - 1][1];
		}
		
		for (int i = 1; i <= N; i++) {
			for (int j = 1; j <= N; j++) {
				dp[i][j] = Math.min(dp[i - 1][j] + costs[i + j - 1][0], 
						dp[i][j - 1] + costs[i + j - 1][1]);
			}
		}
		return dp[N][N];
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/two-city-scheduling/discuss/279548/Beats-100-Average-O(n)-Complexity-solution
	 * 
	 * 1. Similar to Find K closest points
	 * 2. here the K is length/2
	 * 3. Actual Array is the difference of the city costs
	 * 
	 * p0 and p1, if p0[0] - p0[1] < p1[0] - p1[1], it equals to 
	 * p0[0] + p1[1] < p1[0] + p0[1], which means p0 to A and p1 to B is cheaper than 
	 * vice verse.
	 * 
	 * We do not need to perfectly sort all cost differences, we just need the biggest 
	 * savings (to fly to A) to be in the first half of the array. So, we can use the 
	 * quick select algorithm and use the middle of the array as a pivot.
	 * 
	 * It is faster than a full quicksort because you only sort one side, so it often 
	 * performs better than O(NlogN) on average - O(N) on random data, but with poor 
	 * choices of pivot it can easily do worse. O(N^2) on sorted data.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/two-city-scheduling/discuss/278716/C++-O(n-log-n)-sort-by-savings/265369
	 * https://leetcode.com/problems/two-city-scheduling/discuss/278716/C%2B%2B-O(n-log-n)-sort-by-savings
	 * https://leetcode.com/problems/two-city-scheduling/discuss/279548/Beats-100-Average-O(n)-Complexity-solution/265922
	 */
	public int twoCitySchedCost_quickSelect(int[][] costs) {
		int cost = 0;
		int N = costs.length / 2;
		
		quickSelect(costs, 0, costs.length - 1, N);
		
		for (int i = 0; i < costs.length; ++i)
			cost += i < N ? costs[i][0] : costs[i][1];
		return cost;
	}

	private void quickSelect(int[][] costs, int start, int end, int N) {
		if (start == end)
			return;
		
		int j = start - 1;
		int pivot = end;
		
		// "=" is used to move end to its place
		for (int i = start; i <= end; i++) {
			// "=" is used to move end to its place
			if (costs[i][0] - costs[i][1] <= costs[pivot][0] - costs[pivot][1]) {
			    j++;
			
				int[] temp = costs[i];
				costs[i] = costs[j];
				costs[j] = temp;
			}
		}
		
		if (j > N)
			quickSelect(costs, start, j - 1, N);
		else if (j < N)
			quickSelect(costs, j + 1, end, N);
	}
	
	/*
	 * https://leetcode.com/problems/two-city-scheduling/discuss/668155/Two-Approach-or-Detailed-Explanation-or-Clean-Code-or-1ms-or
	 * 
	 * Example 1: [[10,20],[30,200],[400,50],[30,20]]
	 * 
	 * 1. Send all person to city A, Cost  = 10 + 30 + 400 + 30 = 470 
	 * 2. Check how much diff we will get if we send person to city B instead of A
	 *    Diff for  person1, D1 20 - 10 = 10 
	 *    Diff for  person2, D2 200 - 30 = 170 
	 *    Diff for  person3, D3 50 - 400 = -350 
	 *    Diff for  person4, D4 20 - 30 = -10 
	 * 3. Sort the diff cost of persons - D3, D4, D1, D2 
	 * 4. Now we will pick N person which giving more diff to sending them city B 
	 *    instead of city A, P3, P4 we will send them to city B
	 * 5. Add the diff cost of N person in Cost = 470 - 350 - 10 = 110 
	 * 
	 * Time Complexity: O(NlogN), where N is the length of costs array.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/two-city-scheduling/discuss/667786/Java-or-C%2B%2B-or-Python3-or-With-detailed-explanation
	 * 
	 * Other code:
	 * https://leetcode.com/problems/two-city-scheduling/discuss/285099/Java-1ms-solution-with-explanation
	 */
	public int twoCitySchedCost_sort2(int[][] costs) {
        int minCost = 0;
        int N = costs.length / 2;
        for (int i = 0; i < 2 * N; i++)
            minCost += costs[i][0]; // get cost to send everyone to city A

        int[] diff = new int[2 * N];
        // compute the diff of sending B instead of A
        for (int i = 0; i < 2 * N; i++)
            diff[i] = costs[i][1] - costs[i][0];

        Arrays.sort(diff); // sort the diff Array
        for (int i = 0; i < N; i++)
            minCost += diff[i]; // send first N to city B

        return minCost;
    }
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/two-city-scheduling/discuss/278799/Java-Easy-to-understand-DFS-with-cache-solution
	 * 
	 * 2N = nums.length. We want N people to visit city A and N people to visit city B 
	 * so we start with an initial target of N people for both city A and city B. 
	 * 
	 * We terminate a side of the recursion when numA or numB reaches 0 and decrement 
	 * the appropriate counter as we choose particular city. From there, the rest of 
	 * the recursion should make sense. 
	 * 
	 * We essentially compute the cost of choosing to go to city A for person i and 
	 * the minimum cost of that decision for [i+1...N] and do the same for city B. We 
	 * then pick the minimum of the two costs. 
	 * 
	 * Once we come up with the recursion, we can very easily come up with an O(n^3) 
	 * solution by caching (numA, numB, idx) and we can optimize further once we 
	 * realize that (N - numA) + (N - numB) = idx so ultimately we only need to cache 
	 * (numA, numB) which results in a O(n^2) solution.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/two-city-scheduling/discuss/668253/O(n2)-DP/567060
	 * 
	 * Other code:
	 * https://leetcode.com/problems/two-city-scheduling/discuss/292976/Java-DP-and-Greedy-Explained
	 */
	public int twoCitySchedCost_recursive(int[][] costs) {
		int n = costs.length;
		Integer[][] dp = new Integer[n / 2 + 1][n / 2 + 1];
		return rec_recursive(costs, 0, n / 2, n / 2, dp);
	}

	private int rec_recursive(int[][] costs, int i, int na, int nb, Integer[][] dp) {
		if (i == costs.length)
			return 0;
		if (dp[na][nb] != null)
			return dp[na][nb];
		
		int a = Integer.MAX_VALUE, b = Integer.MAX_VALUE;
		if (na > 0)
			a = costs[i][0] + rec_recursive(costs, i + 1, na - 1, nb, dp);
		if (nb > 0)
			b = costs[i][1] + rec_recursive(costs, i + 1, na, nb - 1, dp);
		
		dp[na][nb] = Math.min(a, b);
		return dp[na][nb];
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/two-city-scheduling/discuss/667781/Python-3-Lines-O(n-log-n)-with-sort-explained
     * https://leetcode.com/problems/two-city-scheduling/discuss/300784/4-lines-of-Python-with-explanation
     * https://leetcode.com/problems/two-city-scheduling/discuss/667876/Python-Greedy-with-Prove
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/two-city-scheduling/discuss/278716/C%2B%2B-O(n-log-n)-sort-by-savings
     * https://leetcode.com/problems/two-city-scheduling/discuss/285566/C%2B%2B-DP-solution-with-detailed-explaination
     * https://leetcode.com/problems/two-city-scheduling/discuss/669218/c%2B%2B-I-am-come-up-with-4-solution-pick-up-Best-one
     */

}
