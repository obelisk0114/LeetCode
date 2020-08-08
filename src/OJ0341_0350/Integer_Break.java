package OJ0341_0350;

// https://stackoverflow.com/questions/32418731/java-math-powa-b-time-complexity

public class Integer_Break {
	/*
	 * https://discuss.leetcode.com/topic/51239/a-clear-math-proof-of-the-algorithm
	 * 
	 * If an optimal product contains a factor f >= 4, then you can replace it with 
	 * factors 2 and f-2 without losing optimality, as 2*(f-2) = 2f-4 >= f. So you 
	 * never need a factor greater than or equal to 4, meaning you only need factors 
	 * 1, 2 and 3 (and 1 is of course wasteful and you'd only use it for n=2 and n=3, 
	 * where it's needed).
	 * 
	 * 3*3 is simply better than 2*2*2, so you'd never use 2 more than twice.
	 * 
	 * =============
	 * 
	 * a1 + a2 +...+ am = n. By the Inequality of Arithmetic and Geometric Means,
	 * (a1 + a2 +...+ am) / m >= (a1 * a2 *...* am)^(1 / m)
	 * (n / m) ^ m >= a1 * a2 *...* am ; equality holds iff a1 = a2 =...= am
	 * 
	 * x = n / m, and f(x) = x ^ (n / x). Let y = ln[f(x)] = (n / x) * ln(x)
	 * y' = -(n / x^2) * ln(x) + (n/x) * (1/x) = (n / x^2) * [ln(e) - ln(x)]
	 * y' = 0, x = e. 
	 * f(x) is increasing iff y increasing iff y'>0 iff [ln(e) - ln(x)] > 0 iff x < e
     * and is decreasing iff x > e; and is maximized when x = e.
	 * But x is integer, therefore, Max value appears in 3.
	 * 
	 * n = r * 3 + s * 2, r,s >= 0. Hence s = (n - 3r) / 2
	 * P(r) = (3^r)(2^s) = (3^r)(2^( (n-3r)/2) ). ln(P) = rln(3) + [(n-3r) ln(2)]/2
	 * Let z = ln(P), then z' = ln(3)- 3 ln(2) / 2 > 0
	 * Use as much r as possible.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/integer-break/discuss/80721/Why-factor-2-or-3-The-math-behind-this-problem./85320
	 * https://discuss.leetcode.com/topic/45341/a-simple-explanation-of-the-math-part-and-a-o-n-solution
	 * https://discuss.leetcode.com/topic/43055/why-factor-2-or-3-the-math-behind-this-problem
	 */
	public int integerBreak_math(int n) {
		if (n < 3)
			return 1;
		if (n == 3)
			return 2;
		if (n % 3 == 0)
			return (int) Math.pow(3, n / 3);
		if (n % 3 == 1)
			return 2 * 2 * ((int) Math.pow(3, (n - 4) / 3));

		return 2 * ((int) Math.pow(3, (n - 2) / 3));
	}
	
	/*
	 * https://discuss.leetcode.com/topic/42978/java-dp-solution/2
	 * 
	 * Rf :
	 * https://leetcode.com/problems/integer-break/discuss/80694/Java-DP-solution/204792
	 */
	public int integerBreak_dp(int n) {
		int[] dp = new int[n + 1];
		dp[1] = 1;
		for (int i = 2; i <= n; i++) {
			// i can only be broken into 2 parts,       => j
			// but these numbers can be further broken. => dp[j]
			// Math.max(not broken, broken) => Math.max(j, dp[j])
			for (int j = 1; 2 * j <= i; j++) {
				dp[i] = Math.max(dp[i], (Math.max(j, dp[j])) * (Math.max(i - j, dp[i - j])));
			}
		}
		return dp[n];
	}
	
	/*
	 * The following 2 functions are by myself.
	 * 
	 * 衡砃キА计 >= 碭キА计, 单腹Θミ兵ン–计
	 * ぃだ澄よΑ程
	 */
	public int integerBreak_self(int n) {
        int splitCount = 2;
        int max = (n / splitCount) * (n - n / splitCount);
        while (splitCount * 2 < n) {
            splitCount++;
            max = Math.max(max, getMultiply(n, splitCount));
        }
        return max;
    }
    
    private int getMultiply(int n, int splitCount) {
        int ans = 1;
        int remain = n % splitCount;
        for (int j = 0; j < splitCount; j++) {
            if (j < remain) {
                ans = ans * (n / splitCount + 1);
            }
            else {
                ans = ans * (n / splitCount);
            }
        }
        //System.out.println("i = " + i + " ; ans = " + ans);
        return ans;
    }

}
