package OJ0641_0650;

import java.util.Arrays;

// https://discuss.leetcode.com/topic/98744/java-solutions-from-naive-dp-to-optimized-dp-to-non-dp

public class Two_Keys_Keyboard {
	/*
	 * https://discuss.leetcode.com/topic/97623/loop-best-case-log-n-no-dp-no-extra-space-no-recursion-with-explanation
	 * 
	 * The process of making d copies takes d steps 
	 * (1 step of Copy All and d - 1 steps of Paste)
	 * 
	 * If we look at the solution below more carefully, it is actually equivalent to 
	 * adding up the prime factors of n, since the transformation sequence of the 
	 * largest proper divisors will be (in the form of exponent array):
	 * 
	 * [e_1, e_2, ..., e_t] => [e_1 - 1, e_2, ..., e_t] => ... => [0, e_2, ..., e_t] 
	 * => [0, e_2 - 1, ..., e_t] => ... => [0, 0, ..., 0]
	 * 
	 * And for each such divisor, we add the corresponding prime factor to the 
	 * resulting number of steps. Therefore the final answer will be 
	 * 
	 * p_1 * e_1 + p_2 * e_2 + ... + p_t * e_t.
	 * 
	 * Ref : 
	 * https://discuss.leetcode.com/topic/98744/java-solutions-from-naive-dp-to-optimized-dp-to-non-dp
	 * https://leetcode.com/articles/2-keys-keyboard/
	 */
	public int minSteps(int n) {
		int s = 0;
		for (int d = 2; d <= n; d++) {
			while (n % d == 0) {
				s += d;
				n /= d;
			}
		}
		return s;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/97623/loop-best-case-log-n-no-dp-no-extra-space-no-recursion-with-explanation/6
	 * 
	 * A = a_1 * a_2 * ... * a_n. where a_1 to a_n are all prime number (may have 
	 * duplicates). let A_m = a_1 * ... * a_m. B_m is the total steps to get A_m.
	 * B_(m+1) = B_m + a_(m+1). copy all than paste (a_(m+1) - 1) times, total 
	 * a_(m+1) times.
	 * from B_m to B_m+1 is just add some constant number, there is NO addition 
	 * between different.
	 * B = a_1 + a_2 + ... + a_n. the result is a constant number correspond to A 
	 * itself. The sequence of how you use this prime number will not affect the result.
	 * 
	 * As for the part why this is optimum, you just do a factorization:
	 * A = a_1 * a_2 * ... * c_1 * ... * a_n, where c_1 = a_k * a_(k+1) *...* a_(k+l),
	 * c_1 >= a_k + a_(k+1) + ... + a_(k+l)
	 * 
	 * Rf : https://discuss.leetcode.com/topic/103829/math-facts
	 */
	public int minSteps2(int n) {
		int s = 0;
		for (int d = 2; d <= Math.sqrt(n); d++) {
			while (n % d == 0) {
				s += d;
				n /= d;
			}
		}
		
		if (n != 1) {
			s += n;
		}
		return s;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/97590/java-dp-solution/3
	 * 
	 * "In ascending order, let k_1, k_2, ..., k_m be the proper divisors of k, 
	 * then we have T(k_m) + k / k_m <= T(k_j) + k / k_j for all 1 <= j < m".
	 * 
	 * First the statement is true for the simple case when n = 2. Next we assume 
	 * it is valid for all cases with n < k, and will show it holds for n = k.
	 * 
	 * For an integer k, let [p_1, p_2, ..., p_t] be the prime numbers in ascending 
	 * order in its factorization and [e_1, e_2, ..., e_t] be the corresponding 
	 * exponent array, then k = p_1^e_1 * p_2^e_2 * ... * p_t^e_t.
	 * 
	 * If another integer k' is a divisor of k, then the prime factorization of k' 
	 * has to satisfy the following two conditions:
	 * 1. The exponents of prime numbers other than [p_1, p_2, ..., p_t] must be zero.
	 * 2. Let [e'_1, e'_2, ..., e'_t] be the corresponding exponent array for k' 
	 *    in reference to [p_1, p_2, ..., p_t], then e'_j <= e_j for all 
	 *    1 <= j <= t (Note that now e'_j is not necessarily positive but can be zero)
	 *    
	 * The exponent array for k_m will be [e_1 - 1, e_2, ..., e_t], due to the fact 
	 * that k_m is the largest proper divisor of k. 
	 * Let [e'_1, e'_2, ..., e'_t] be the exponent array for k_i, 
	 * we need to consider two cases: 1. k_m % k_i != 0 or 2. k_m % k_i == 0.
	 * 
	 * 1. k_i is not a divisor of k_m. From our conclusion above, we must have 
	 * e'_1 > e_1 - 1 >= 0. This is because e'_j <= e_j holds for all 2 <= j <= t 
	 * as k_i is a factor of k. 
	 * Let d_i be the largest proper factor of k_i, then the exponent array of d_i 
	 * will be [e'_1 - 1, e'_2, ..., e'_t]. It is easy to show that d_i will also be 
	 * a factor of k_m, since e'_1 - 1 <= e_1 - 1. Also k * d_i = k_m * k_i, since
	 * k * d_i = [e_1 + e'_1 - 1, e_2 + e'_2, ..., e_t + e'_t] and 
	 * k_m * k_i = [e_1 - 1 + e'_1, e_2 + e'_2, ..., e_t + e'_t].
	 * 
	 * T(k_m) + k/k_m <= T(d_i) + k_m/d_i + k/k_m 
	 * = T(d_i) + k/k_i + k_i/d_i = T(k_i) + k/k_i
	 * 
	 * The first inequality comes from the induction assumption: 
	 * k_m < k and k_m % d_i == 0 implies T(k_m) <= T(d_i) + k_m/d_i.
	 * The following equality takes advantage of the equation k * d_i = k_m * k_i and 
	 * the last equality uses the induction assumption: T(k_i) = T(d_i) + k_i/d_i.
	 * 
	 * 2. k_i is a proper divisor of k_m. Then by our induction assumption, 
	 * T(k_m) + k/k_m <= T(k_i) + k_m/k_i + k/k_m. 
	 * We only need to show that k_m/k_i + k/k_m <= k/k_i. Note that
	 * k = k_m * p_1, then k/k_i = p_1 * k_m/k_i = p_1 + p_1 * (k_m/k_i - 1) 
	 * >= p_1 + k_m/k_i = k/k_m + k_m/k_i, where we have used the facts that 
	 * p_1 >= 2 and k_m/k_i >= 2 to derive the inequality in the middle.
	 * 
	 * We conclude the induction assumption is also true for the case n = k, 
	 * hence validate our observation above.
	 * 
	 * Rf : https://discuss.leetcode.com/topic/98744/java-solutions-from-naive-dp-to-optimized-dp-to-non-dp
	 */
	public int minSteps_optimized_DP1(int n) {
        int[] dp = new int[n+1];

        for (int i = 2; i <= n; i++) {
            dp[i] = i;
            for (int j = i/2; j > 1; j--) {
                if (i % j == 0) {
                    dp[i] = dp[j] + (i/j);
                    break;
                }
            }
        }
        return dp[n];
    }
	
	/*
	 * https://discuss.leetcode.com/topic/97590/java-dp-solution/7
	 * 
	 * dp[i] should be thought of as sum of all prime factors 
	 * (counted with multiplicity)
	 */
	public int minSteps_optimized_DP2(int n) {
		int[] dp = new int[n + 1];
		for (int i = 2; i <= n; i++) {
			// sub-problem dp[i] := minSteps(i)
			for (int j = 2; j <= i; j++) {
				// j := use j steps to create j copies
				if (i % j == 0) {
					dp[i] = j + dp[i / j];
					break;
				}
			}
		}
		return dp[n];
	}
	
	/*
	 * https://discuss.leetcode.com/topic/97603/c-java-clean-code-with-explanation-4-lines-no-dp
	 * 
	 * It take 2 op to double, 3 ops to triple, ...
	 * if n % 2 == 0, then f(n) = f(n/2) + 2
	 * if n % 3 == 0, then f(n) = f(n/3) + 3
	 * 2 * 2 = 2 + 2, 2 * 3 > 2 + 3, 4 * 4 > 4 + 4, 
	 * so it is always better to divide whenever possible.
	 * now it became a problem for finding all possible factors;
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/97595/java-8ms-math-adding-prime-factors
	 */
	public int minSteps_recursive(int n) {
        if (n == 1) return 0;
        for (int i = 2; i < n; i++)
            if (n % i == 0) return i + minSteps_recursive(n / i);
        return n;
    }
	
	/*
	 * https://discuss.leetcode.com/topic/98744/java-solutions-from-naive-dp-to-optimized-dp-to-non-dp
	 * 
	 * From the point of the last 'Paste', it only cares about characters which are 
	 * copied last time, that is, the first 'Copy All' operation from the end of the 
	 * sequence. Assume the number of 'A' on the notepad is i when this 'Copy All' 
	 * operation is performed. It needs (k-i)/i more steps to get k 'A' on the notepad 
	 * by pasting only. The total number of steps from getting i 'A' to k 'A' is 
	 * k / i, that is, one 'Copy All' plus (k-i)/i 'Paste'. 
	 * The total number of steps getting k 'A' for this case is given by T(i) + k/i
	 * 
	 * T(k) = min(T(i) + k/i) where 1 <= i < k && k % i == 0.
	 * time complexity O(n^2) and space complexity O(n)
	 */
	public int minSteps_naive_DP(int n) {
		int[] dp = new int[n + 1];
		Arrays.fill(dp, Integer.MAX_VALUE);
		dp[1] = 0;

		for (int k = 2; k <= n; k++) {
			for (int i = 1; i < k; i++) {
				if (k % i != 0)
					continue;
				dp[k] = Math.min(dp[k], dp[i] + k / i);
			}
		}

		return dp[n];
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/97583/java-recursive-solution-with-explanation
	 * 
	 * m represents the no of chars on screen. 
	 * clip represents the no of chars in the clipboard.
	 * 
	 * At every step, we either copy the screen or paste. 
	 * We have to take the min of these two operations recursively to get the result.
	 */
	public int minSteps_recursive_DP(int n) {
        //consider the 1st step done as there is only one possible - ie copy
        return n == 1 ? 0 : 1 + minSteps_recursive_DP(1, 1, n);
    }
    private int minSteps_recursive_DP(int m, int clip, int n) {
        if(m == n) {
            return 0;
        }
        
        if(m > n) {
        	// -1 signals that the key sequence followed so far is invalid
            return -1;
        }
        
        if(m == clip) {
        	//avoid a sequence with consecutive copies
            int pasteCost = minSteps_recursive_DP(m + clip, clip, n);
            return pasteCost == -1 ? -1 : 1 + pasteCost;
        }
        
        int copyCost = minSteps_recursive_DP(m, m, n);
        int pasteCost = minSteps_recursive_DP(m + clip, clip, n);
        
        if(copyCost == -1 && pasteCost == -1) {
            return -1;
        }
        else if(copyCost == -1) {
            return 1 + pasteCost;
        }
        else if(pasteCost == -1) {
            return 1 + copyCost;
        }
        else {
            return 1 + Math.min(copyCost, pasteCost);    
        }
    }
	
	// https://discuss.leetcode.com/topic/97586/java-solution-memorized-bfs

}
