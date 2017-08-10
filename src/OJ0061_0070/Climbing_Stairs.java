package OJ0061_0070;

import java.util.HashMap;

public class Climbing_Stairs {
	/*
	 * https://discuss.leetcode.com/topic/5371/basically-it-s-a-fibonacci
	 * 
	 * "all_ways" corresponds to the number of solutions to get to the point [n].
     * "one_step_before" refers to the number of solutions until the point [n-1],
     * "two_steps_before" refers to the number of solution until the point [n-2].

     * From the point [n-1], we take one step to reach the point [n].
     * From the point [n-2], we take a two-steps leap to reach the point [n].
     * the total number of solution to reach the point [n] should be [n-1] + [n-2].
	 * 
	 * There is NO overlapping between these two solution sets, 
	 * because we differ in the final step.
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/25262/accepted-java-solution
	 * https://discuss.leetcode.com/topic/5825/my-solution-considering-odd-and-even
	 */
	public int climbStairs(int n) {
		// base cases
		if (n <= 0)
			return 0;
		if (n == 1)
			return 1;
		if (n == 2)
			return 2;

		int one_step_before = 2;
		int two_steps_before = 1;
		int all_ways = 0;

		for (int i = 2; i < n; i++) {
			all_ways = one_step_before + two_steps_before;
			two_steps_before = one_step_before;
			one_step_before = all_ways;
		}
		return all_ways;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/955/easy-solutions-for-suggestions
	 * 
	 * Ideas: Use Dynamic Programming
     * for each step, the stair could either combine with the previous one or as a single step.
     * Ways to climb to ith stair is W(i) = W(i-1) + W(i-2)
     * where W(i-1) is when the ith stair is as a single step
     * and W(i-2) is when the ith stair is paired with the previous one.
	 */
	public int climbStairs_array(int n) {
		if (n == 0 || n == 1 || n == 2) {
			return n;
		}
		int[] mem = new int[n];
		mem[0] = 1;
		mem[1] = 2;
		for (int i = 2; i < n; i++) {
			mem[i] = mem[i - 1] + mem[i - 2];
		}
		return mem[n - 1];
	}
	
	/*
	 * The following 2 variables and function are from this link.
	 * https://discuss.leetcode.com/topic/34916/1ms-java-solution-easy-to-understand-with-dp
	 * 
	 * Rf : https://discuss.leetcode.com/topic/42447/both-bottom-up-and-top-down-dynamic-programming-style-java-code-good-for-learners
	 */
	int result_map;
	HashMap<Integer, Integer> memo_map = new HashMap<Integer, Integer>();
	public int climbStairs_map(int n) {
		if (n < 2) {
			return 1;
		}
		if (memo_map.containsKey(n)) {
			return memo_map.get(n);
		}
		result_map = climbStairs_map(n - 1) + climbStairs_map(n - 2);
		memo_map.put(n, result_map);
		return result_map;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/30638/using-the-fibonacci-formular-to-get-the-answer-directly
	 * 
	 * Other :
	 * https://discuss.leetcode.com/topic/30638/using-the-fibonacci-formular-to-get-the-answer-directly/3
	 */
	public int climbStairs_formula(int n) {
        n++;
        double phi = (1 + Math.sqrt(5))/ 2;
        double ans = (Math.pow(phi, n) - Math.pow(phi * -1, n * -1)) / Math.sqrt(5);
        return (int) ans;
    }
	
	public int climbStairs_self(int n) {
        if (n == 1)
            return 1;
        int count = n;
        double[] factorial = new double[n - 1];
        factorial[0] = 1;
        for (int i = 1; i < n - 1; i++) {
            factorial[i] = factorial[i - 1] * i;
        }
        
        for (int i = 2; i <= n / 2; i++) {
            double cur = factorial[n - i] / factorial[i] / factorial[n - i * 2];
            
            count = count + (int) (Math.round(cur));
            if (((long) cur) - Math.round(cur) > 0.9) {
                count++;
            }
        }
        return count;
    }
	
	/*
	 * The following 3 functions are from this link.
	 * https://discuss.leetcode.com/topic/3379/o-log-n-solution-with-matrix-multiplication
	 * 
	 * Rf : https://zh.wikipedia.org/wiki/%E6%96%90%E6%B3%A2%E9%82%A3%E5%A5%91%E6%95%B0%E5%88%97#.E6.A7.8B.E5.BB.BA.E4.B8.80.E5.80.8B.E7.9F.A9.E9.99.A3.E6.96.B9.E7.A8.8B
	 */
	public int climbStairs_matrix_multiplication(int n) {
	    int[][] a = {{0, 1}, {1, 1}};
	    int[][] m = pow(a, n - 1);
	    return m[0][1] + m[1][1];
	}
	private int[][] pow(int[][] a, int n) {
	    int[][] ret = {{1, 0}, {0, 1}};
	    while (n > 0) {
	        if ((n & 1) == 1) {
	            ret = multiply(ret, a);
	        }
	        n >>= 1;
	        a = multiply(a, a);
	    }
	    return ret;
	}
	private int[][] multiply(int[][] a, int[][] b) {
	    int[][] c = new int[2][2];
	    for (int i = 0; i < 2; i++) {
	        for (int j = 0; j < 2; j++) {
	            c[i][j] = a[i][0] * b[0][j] + a[i][1] * b[1][j];
	        }
	    }
	    return c;
	}
	
	// https://discuss.leetcode.com/topic/17002/3-4-short-lines-in-every-language
	
	// https://discuss.leetcode.com/topic/5475/my-divide-and-conquer-way-to-solve-this-problem-java

}
