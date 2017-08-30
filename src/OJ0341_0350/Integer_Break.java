package OJ0341_0350;

// https://stackoverflow.com/questions/32418731/java-math-powa-b-time-complexity

public class Integer_Break {
	/*
	 * https://discuss.leetcode.com/topic/51239/a-clear-math-proof-of-the-algorithm
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
	 * https://discuss.leetcode.com/topic/45341/a-simple-explanation-of-the-math-part-and-a-o-n-solution
	 * https://discuss.leetcode.com/topic/43055/why-factor-2-or-3-the-math-behind-this-problem
	 */
	public int integerBreak(int n) {
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
	
	// https://discuss.leetcode.com/topic/42978/java-dp-solution/2
	public int integerBreak_dp(int n) {
		int[] dp = new int[n + 1];
		dp[1] = 1;
		for (int i = 2; i <= n; i++) {
			for (int j = 1; 2 * j <= i; j++) {
				dp[i] = Math.max(dp[i], (Math.max(j, dp[j])) * (Math.max(i - j, dp[i - j])));
			}
		}
		return dp[n];
	}

}
