package OJ0271_0280;

import java.util.Queue;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

public class Perfect_Squares {
	/**
	 * https://leetcode.com/problems/perfect-squares/discuss/71612/58ms-Java-DP-solution-beating-over-90
	 * 
	 * s[i] denotes the least number of square numbers that add up to n
	 * initial s[i] as maximum integer
	 * for i from 1 to n, 
	 *      if i is perfect square, s[i]=1, 
	 *      otherwise get the square root of the maximum perfect square smaller than i
	 * for j from 1 to square root, 
	 *      if(s[i-j*j]+1<s[i]) update s[i] as s[i-j*j]+1
	 * 
	 * Because for every square number, the best case is that the existing sum plus 
	 * the square number exactly once could result to n
	 * 
	 * */
	public int numSquares_dp(int n) {
		int[] s = new int[n + 1];
		// no need to store a list of perfect squares, knowing the square
		// root of the largest perfect square is sufficient
		for (int i = 1; i < n + 1; i++) {
			s[i] = i;
			int sqrt = (int) Math.sqrt(i);
			if (i == sqrt * sqrt) {
				s[i] = 1;
				continue;
			}
			for (int j = 1; j <= sqrt; j++) {
				s[i] = Math.min(s[i], s[i - j * j] + 1);
			}
		}
		return s[n];
	}
	
	/*
	 * https://leetcode.com/problems/perfect-squares/discuss/71632/Beautiful-8-Lines-Java-Solution
	 * 
	 * dp[n] indicates that the perfect squares count of the given n, 
	 * 
	 * dp[0] = 0 
	 * dp[1] = dp[0]+1 = 1
	 * dp[2] = dp[1]+1 = 2
	 * dp[3] = dp[2]+1 = 3
	 * dp[4] = Min{ dp[4-1*1]+1, dp[4-2*2]+1 } 
	 *       = Min{ dp[3]+1, dp[0]+1 } = 1 
     * dp[5] = Min{ dp[5-1*1]+1, dp[5-2*2]+1 } 
     *       = Min{ dp[4]+1, dp[1]+1 } = 2
     * ...
     * dp[13] = Min{ dp[13-1*1]+1, dp[13-2*2]+1, dp[13-3*3]+1 } 
     *        = Min{ dp[12]+1, dp[9]+1, dp[4]+1 } = 2
     * ...
     * dp[n] = Min{ dp[n - i*i] + 1 },  n - i*i >=0 && i >= 1; 1 for i*i  
	 * 
	 * Rf :
	 * https://leetcode.com/problems/perfect-squares/discuss/71495/An-easy-understanding-DP-solution-in-Java
	 */
	public int numSquares_dp2(int n) {
		int[] record = new int[n + 1];
		for (int i = 0; i <= n; i++) {
			record[i] = i;
			for (int j = 1; j * j <= i; j++) {
				record[i] = Math.min(record[i - j * j] + 1, record[i]);
			}
		}
		return record[n];
	}
	
	/*
	 * https://leetcode.com/problems/perfect-squares/discuss/71488/Summary-of-4-different-solutions-(BFS-DP-static-DP-and-mathematics)
	 * 
	 * Start from node 0 in queue, and keep pushing in 
	 * perfect square number + curr value, once we reach number n, we found the solution.
	 */
	public int numSquares_BFS(int n) {
		Queue<Integer> q = new LinkedList<>();
		Set<Integer> visited = new HashSet<>();
		q.offer(0);
		visited.add(0);
		int depth = 0;
		while (!q.isEmpty()) {
			int size = q.size();
			depth++;
			while (size-- > 0) {
				int u = q.poll();
				for (int i = 1; i * i <= n; i++) {
					int v = u + i * i;
					if (v == n) {
						return depth;
					}
					if (v > n) {
						break;
					}
					if (!visited.contains(v)) {
						q.offer(v);
						visited.add(v);
					}
				}
			}
		}
		return depth;
	}
	
	/*
	 * https://leetcode.com/problems/perfect-squares/discuss/71637/Java-solution-O(n12)-time-and-O(1)-space
	 * 
	 * Fermat's theorem on sums of two squares
	 * Legendre's three-square theorem: not of the form 4^a (8b+7)
	 * Lagrange's four-square theorem: every natural number can be represented as the sum of four integer squares
	 * 
	 * Rf :
	 * https://leetcode.com/problems/perfect-squares/discuss/71533/O(sqrt(n))-in-Ruby-C++-C
	 * https://leetcode.com/problems/perfect-squares/discuss/71532/O(sqrt(n))-about-0.034-ms-(and-0.018-ms)
	 */
	public int numSquares_number_theorem(int n) {
		int m = n;
		while (m % 4 == 0)
			m = m >> 2;
		if (m % 8 == 7)
			return 4;

		int sqrtOfn = (int) Math.sqrt(n);
		if (sqrtOfn * sqrtOfn == n)        // Is it a Perfect square?
			return 1;
		else {
			for (int i = 1; i <= sqrtOfn; ++i) {
				int remainder = n - i * i;
				int sqrtOfNum = (int) Math.sqrt(remainder);
				if (sqrtOfNum * sqrtOfNum == remainder)
					return 2;
			}
		}
		return 3;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/perfect-squares/discuss/71661/O(sqrt(n2))-Applying-Fermat's-theorm-with-BrahmaguptaFibonacci-identity
	 * 
	 * check if number fits Legendre's condition for 4 squares 4^a (8b+7)
	 * if all factors(or subfactors) are prime and match condtion n%4 == 1 I am 
	 * guaranteed that my number is sum of 2 squares. Otherwise it's sum of 3 squares 
	 * by Legendre's theorem
	 */
	public int numSquares_number_theorem2(int n) {
		if (Math.pow((int) Math.sqrt(n), 2) == n) {
			return 1;
		}
		while (n % 4 == 0) {
			n = n / 4;
		}
		if (n % 8 == 7) {
			return 4;
		}
		if (n % 2 == 0) {
			n = n / 2; // OK to divide by 2. If N/2 has is sum of 2 squares so will be N.
		}
		if (isMod41PrimeOrSubP(n)) {
			return 2;
		}
		return 3;
    }
    private boolean isMod41PrimeOrSubP(int num) {
		if (num % 4 != 1) {
			return false;
		}
		for (int i = 3; i * i < num; i = i + 2) {
			if (num % i == 0) {
				if (num % (i * i) == 0) {
					return isMod41PrimeOrSubP(num / (i * i));
				}
				return isMod41PrimeOrSubP(i) && isMod41PrimeOrSubP(num / i);
			}
		}
		return true;
	}

}
