package OJ0551_0560;

public class Student_Attendance_Record_II {
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/student-attendance-record-ii/discuss/101652/Java-4-lines-DP-solution-with-state-transition-table-explained
	 * 
	 * Let AxLy denote number of strings containing x A's and ending with y L's
	 * For example:
	 * 
	 * When n = 1 we have:     |     When n = 2 we have:
	 * A0L0: P                 |     A0L0: LP, PP
	 * A0L1: L                 |     A0L1: PL
	 * A0L2:                   |     A0L2: LL
	 * A1L0: A                 |     A1L0: AP, LA, PA
	 * A1L1:                   |     A1L1: AL
	 * A1L2:                   |     A1L2:
	 * Done:                   |     Done: AA
	 * 
	 * In general we have this transition table:
	 * -----+---------------
	 * INIT | A -- L -- P --
	 * -----+---------------
	 * A0L0 | A1L0 A0L1 A0L0
	 * A0L1 | A1L0 A0L2 A0L0
	 * A0L2 | A1L0 Done A0L0
	 * A1L0 | Done A1L1 A1L0
	 * A1L1 | Done A1L2 A1L0
	 * A1L2 | Done Done A1L0
	 * 
	 * From the transition table we see that:
	 * A0L0 of n can result from A0L0 + A0L1 + A0L2 of n - 1 by appending P
	 * A0L1 of n can only result from A0L0 of n - 1 by appending L
	 * and so on...
	 * 
	 * dp[0] = A0L0;
	 * dp[1] = A0L1;
	 * dp[2] = A0L2;
	 * dp[3] = A1L0;
	 * dp[4] = A1L1;
	 * dp[5] = A1L2;
	 */
	public int checkRecord2(int n) {
		// init table for n = 1
		int[] dp = { 1, 1, 0, 1, 0, 0 };
		
		// updating table for n = i
		for (int i = 2; i <= n; i++) {
			dp = new int[] { sum2(dp, 0, 2), dp[0], dp[1], 
					sum2(dp, 0, 5), dp[3], dp[4] };
		}
		return sum2(dp, 0, 5);
	}

	private int sum2(int[] arr, int l, int h) {
		int s = 0;
		for (int i = l; i <= h; i++)
			s = (s + arr[i]) % 1000000007;
		return s;
	}
	
	/*
	 * https://leetcode.com/problems/student-attendance-record-ii/discuss/520918/Java-dp-o(n)-easy-understand-with-comment
	 * 
	 * position x :
	 * Two group: have 'A', no 'A'
	 * for each L we can separate further into two group, single L and continuous LL
	 * 
	 * have 'A': end with [A, L, LL, P] => dp[x][0], dp[x][1], dp[x][2], dp[x][3]
	 * no   'A': end with [L, LL, P]    => dp[x][4], dp[x][5], dp[x][6]
	 */
	public int checkRecord3(int n) {
		if (n == 0)
			return 0;
		
		int mod = 1000000007;
		int res = 0;
		
		long dp[][] = new long[n][7];

		dp[0][0] = 1;
		dp[0][1] = 0;
		dp[0][2] = 0;
		dp[0][3] = 0;   // group with 'A'

		dp[0][4] = 1;
		dp[0][5] = 0;
		dp[0][6] = 1;   // group without 'A'

		for (int i = 1; i < n; i++) {
			// 3 possible case, end with [A, P, L]
			
			// 1. ending with A
			dp[i][0] = (dp[i - 1][4] + dp[i - 1][5] + dp[i - 1][6]) % mod;

			// 2. ending with P
			dp[i][3] = (dp[i - 1][0] + dp[i - 1][1] 
					+ dp[i - 1][2] + dp[i - 1][3]) % mod;
			dp[i][6] = (dp[i - 1][4] + dp[i - 1][5] + dp[i - 1][6]) % mod;

			// 3. ending with L
			dp[i][1] = (dp[i - 1][0] + dp[i - 1][3]) % mod;
			dp[i][2] = dp[i - 1][1];
			dp[i][4] = dp[i - 1][6];
			dp[i][5] = dp[i - 1][4];
		}

		for (int i = 0; i < 7; i++) {
			res += dp[n - 1][i];
			res = res % mod;
		}
		return res;
	}
	
	/*
	 * https://leetcode.com/problems/student-attendance-record-ii/discuss/343836/O(n)-time-O(1)-space-Java-DP-Solution-with-state-transition.
	 * 
	 * State transition
	 * state[0] : end with A
	 * state[1] : end with P and 0 A before end;
	 * state[2] : end with P and 1 A before end;
	 * state[3] : end with 1 L and 0 A before end;
	 * state[4] : end with 1 L and 1 A before end;
	 * state[5] : end with 2 L and 0 A before end;
	 * state[6] : end with 2 L and 1 A before end;
	 */
	public int checkRecord3_constant(int n) {
		int MOD = 1000000007;

		long[] prevState = new long[] { 1L, 1L, 0L, 1L, 0L, 0L, 0L };
		for (int i = 2; i <= n; i++) {
			long[] currState = new long[7];

			currState[0] = ((prevState[1] + prevState[3]) % MOD + prevState[5]) % MOD;
			currState[1] = currState[0];
			currState[2] = (((prevState[0] + prevState[2]) % MOD + prevState[4]) % MOD 
					+ prevState[6]) % MOD;
			currState[3] = prevState[1];
			currState[4] = (prevState[0] + prevState[2]) % MOD;
			currState[5] = prevState[3];
			currState[6] = prevState[4];

			prevState = currState;
		}

		long res = 0;
		for (long v : prevState) {
			res = (res + v) % MOD;
		}
		return (int) res;
	}
	
	/*
	 * https://leetcode.com/problems/student-attendance-record-ii/discuss/101665/Java-solution-easy-to-understand-(at-least-for-me)
	 * 
	 * dpPLP[]: awarded count only with P or L, end with P
	 * dpPLL[]: awarded count only with P or L, end with L
	 * 
	 * dpP[]: awarded count, ends with 'P'
	 * dpA[]: awarded count, ends with 'A'
	 * dpL[]: awarded count, ends with 'L'
	 * 
	 * Other code:
	 * https://leetcode.com/problems/student-attendance-record-ii/discuss/431896/Java-solution-with-intuitive-explanation-(easy-to-understand)
	 */
	public int checkRecord4(int n) {
		if (n == 1) {
			return 3;
		}
		if (n == 2) {
			return 8;
		}

		int M = 1000000007;
		
		long[] dpPLP = new long[n + 1];   // only with P or L, end with P
		long[] dpPLL = new long[n + 1];   // only with P or L, end with L
		
		long[] dpP = new long[n + 1];   // awarded count, ends with 'P'
		long[] dpA = new long[n + 1];   // awarded count, ends with 'A'
		long[] dpL = new long[n + 1];   // awarded count, ends with 'L'
		
		dpPLP[0] = 0;   dpPLL[0] = 0;
		dpPLP[1] = 1;   dpPLL[1] = 1;
		dpPLP[2] = 2;   dpPLL[2] = 2;
		
		dpP[0] = 0;   dpA[0] = 0;   dpL[0] = 0;
		dpP[1] = 1;   dpA[1] = 1;   dpL[1] = 1;
		dpP[2] = 3;   dpA[2] = 2;   dpL[2] = 3;

		for (int i = 3; i <= n; i++) {
			dpPLP[i] = dpPLP[i - 1] + dpPLL[i - 1];
			dpPLL[i] = dpPLP[i - 1] + dpPLP[i - 2];
			
			dpP[i] = dpP[i - 1] + dpA[i - 1] + dpL[i - 1];   // last one is 'P'
			dpA[i] = dpPLP[i - 1] + dpPLL[i - 1];   // last one is 'A'
			dpL[i] = dpP[i - 1] + dpA[i - 1] + dpP[i - 2] + dpA[i - 2]; // last one is 'L'
			
			dpPLP[i] %= M;
			dpPLL[i] %= M;
			dpP[i] %= M;
			dpA[i] %= M;
			dpL[i] %= M;
		}
		return (int) ((dpP[n] + dpA[n] + dpL[n]) % M);
	}
	
	/*
	 * The following 2 variables and 3 functions are from this link.
	 * https://leetcode.com/problems/student-attendance-record-ii/discuss/101633/Improving-the-runtime-from-O(n)-to-O(log-n)
	 * 
	 * Let f[i][j][k] denote the # of valid sequences of length i where:
	 * 
	 * 1. There can be at most j A's in the entire sequence.
	 * 2. There can be at most k trailing L's.
	 * 
	 * Give the recurrence in the following code, and the final answer is f[n][1][2].
	 * int val = f[i - 1][j][2]; // ...P
     * if (j > 0) val = (val + f[i - 1][j - 1][2]) % MOD; // ...A
     * if (k > 0) val = (val + f[i - 1][j][k - 1]) % MOD; // ...L
     * 
     * if we treat f[i][][] and f[i-1][][] as two vectors, we can represent the 
     * recurrence of f[i][j][k] as follows:
     * 
     * f[i][0][0]   | 0 0 1 0 0 0 |   f[i-1][0][0]
     * f[i][0][1]   | 1 0 1 0 0 0 |   f[i-1][0][1]
     * f[i][0][2] = | 0 1 1 0 0 0 | * f[i-1][0][2]
     * f[i][1][0]   | 0 0 1 0 0 1 |   f[i-1][1][0]
     * f[i][1][1]   | 0 0 1 1 0 1 |   f[i-1][1][1]
     * f[i][1][2]   | 0 0 1 0 1 1 |   f[i-1][1][2]
     * 
     * Let A be the matrix above, then f[n][][] = A^n * f[0][][], where 
     * f[0][][] = [1 1 1 1 1 1]. The point of this approach is that we can compute 
     * A^n using exponentiating by squaring
     * 
     * The final answer is f[n][1][2], which involves multiplying the last row of A^n 
     * and the column vector [1 1 1 1 1 1]. Interestingly, it is also equal to 
     * A^(n+1)[5][2] as the third column of A is just that vector.
     * 
     * When i = 0, there is a unique sequence, i.e., the empty sequence. Moreover, the 
     * empty sequence is a sequence with 0 A's globally and with 0 trailing L's. 
     * Therefore, we have f[0][i][j] = 1, for 0 <= i < 2 and 0 <= j < 3.
     * 
     * Rf :
     * https://leetcode.com/problems/student-attendance-record-ii/discuss/101633/Improving-the-runtime-from-O(n)-to-O(log-n)/105290
     * https://leetcode.com/problems/student-attendance-record-ii/discuss/101633/Improving-the-runtime-from-O(n)-to-O(log-n)/274735
     * https://leetcode.com/problems/student-attendance-record-ii/discuss/101633/Improving-the-runtime-from-O(n)-to-O(log-n)/105293
	 */
	final int MOD_matrix = 1000000007;
	final int M_matrix = 6;

	int[][] mul_matrix(int[][] A, int[][] B) {
		int[][] C = new int[M_matrix][M_matrix];
		for (int i = 0; i < M_matrix; i++)
			for (int j = 0; j < M_matrix; j++)
				for (int k = 0; k < M_matrix; k++)
					C[i][j] = (int) ((C[i][j] + (long) A[i][k] * B[k][j]) % MOD_matrix);
		return C;
	}

	int[][] pow_matrix(int[][] A, int n) {
		int[][] res = new int[M_matrix][M_matrix];
		for (int i = 0; i < M_matrix; i++)
			res[i][i] = 1;
		while (n > 0) {
			if (n % 2 == 1)
				res = mul_matrix(res, A);
			A = mul_matrix(A, A);
			n /= 2;
		}
		return res;
	}

	public int checkRecord_matrix(int n) {
	    int[][] A = {
	            {0, 0, 1, 0, 0, 0},
	            {1, 0, 1, 0, 0, 0},
	            {0, 1, 1, 0, 0, 0},
	            {0, 0, 1, 0, 0, 1},
	            {0, 0, 1, 1, 0, 1},
	            {0, 0, 1, 0, 1, 1},
	    };
	    return pow_matrix(A, n + 1)[5][2];
	}
	
	/*
	 * https://leetcode.com/problems/student-attendance-record-ii/discuss/101633/Improving-the-runtime-from-O(n)-to-O(log-n)
	 * 
	 * Let f[i][j][k] denote the # of valid sequences of length i where:
	 * 1. There can be at most j A's in the entire sequence.
	 * 2. There can be at most k trailing L's.
	 * 
	 * The final answer is f[n][1][2].
	 * The runtime is O(n), using linear space (which can be optimized to O(1)).
	 * 
	 * "int val = f[i - 1][j][2]; // ...P" 
	 * we want to form n_length record from (n-1)_length record by add one P to the 
	 * end of the (n-1)_length record. Because we add one P to the end, so no matter 
	 * how many trailing L in (n-1)_length record, after add one P we will get the 
	 * n_length record with 0 trailing L.
	 * So the trailing L in (n-1)_length will not affect the n_length record's 
	 * trailing L amount if we add one P to the end, we just choose the maximum 
	 * f[n-1] with j A and 2 trailing L
	 * 
	 * the same reason, "if (j > 0) val = (val + f[i - 1][j - 1][2]) % MOD; // ...A"
	 * 
	 * "if (k > 0) val = (val + f[i - 1][j][k - 1]) % MOD; // ...L" 
	 * Because we add L to the end, so (n-1)_length cannot contain more than (k-1) 
	 * trailing L
	 * 
	 * Rf :
	 * https://leetcode.com/problems/student-attendance-record-ii/discuss/101633/Improving-the-runtime-from-O(n)-to-O(log-n)/274735
	 */
	public int checkRecord_loop3(int n) {
		final int MOD = 1000000007;
		int[][][] f = new int[n + 1][2][3];

		f[0] = new int[][] { { 1, 1, 1 }, { 1, 1, 1 } };
		for (int i = 1; i <= n; i++)
			for (int j = 0; j < 2; j++)
				for (int k = 0; k < 3; k++) {
					int val = f[i - 1][j][2]; // ...P
					if (j > 0)
						val = (val + f[i - 1][j - 1][2]) % MOD; // ...A
					if (k > 0)
						val = (val + f[i - 1][j][k - 1]) % MOD; // ...L
					
					f[i][j][k] = val;
				}
		return f[n][1][2];
	}
	
	/*
	 * https://leetcode.com/problems/student-attendance-record-ii/discuss/101638/Simple-Java-O(n)-solution/113451
	 * 
	 * Rf :
	 * https://leetcode.com/problems/student-attendance-record-ii/discuss/101638/Simple-Java-O(n)-solution/105322
	 * https://leetcode.com/problems/student-attendance-record-ii/discuss/101638/Simple-Java-O(n)-solution/487120
	 * https://leetcode.com/problems/student-attendance-record-ii/discuss/101638/Simple-Java-O(n)-solution/105320
	 */
	public int checkRecord_separate_by_A(int n) {
		int mod = (int) (1e9 + 7);
		
		long[] P = new long[n + 1]; // end with P without A
		long[] L = new long[n + 1]; // end with L without A
		P[0] = P[1] = L[1] = 1;
		
		for (int i = 2; i <= n; i++) {
			P[i] = (P[i - 1] + L[i - 1]) % mod;
			L[i] = (P[i - 1] + P[i - 2]) % mod;
		}
		
		long res = (P[n] + L[n]) % mod;
		
		// inserting A into (n-1)-length strings
		for (int i = 0; i < n; i++) {
			// L[0] = 0, so P[0] + L[0] = 1 for empty string
			long s = ((P[i] + L[i]) % mod * (P[n - i - 1] + L[n - i - 1]) % mod) % mod;
			res = (res + s) % mod;
		}
		return (int) res;
	}
	
	/*
	 * The following 2 variables and 2 functions are from this link.
	 * https://leetcode.com/problems/student-attendance-record-ii/discuss/490777/Very-simple-recursive-%2B-memo-java-accepted-solution
	 */
	int M_memory = 1000000007;
	Integer[][][] dp_memory;

	public int checkRecord_memory(int n) {
		dp_memory = new Integer[n + 1][2][3];
		return rec_memory(n, 1, 0, 0);
	}

	public int rec_memory(int n, int i, int absentFlag, int lateCount) {
		if (i == n + 1) {
			return 1;
		}
		if (dp_memory[i][absentFlag][lateCount] != null) {
			return dp_memory[i][absentFlag][lateCount];
		}

		int count = 0;
		// P
		count = (count % M_memory + 
				rec_memory(n, i + 1, absentFlag, 0) % M_memory) % M_memory;
		// L
		if (lateCount < 2) {
			count = (count % M_memory + 
					rec_memory(n, i + 1, absentFlag, lateCount + 1) % M_memory) % M_memory;
		}
		// A
		if (absentFlag == 0) {
			count = (count % M_memory + 
					rec_memory(n, i + 1, 1, 0) % M_memory) % M_memory;
		}

		dp_memory[i][absentFlag][lateCount] = count;
		return count;
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/student-attendance-record-ii/discuss/101651/Python-Straightforward-DP-with-Explanation
     * https://leetcode.com/problems/student-attendance-record-ii/discuss/101645/Python-O(n)-solution-using-simple-recurrence-formula-with-explanation
     * https://leetcode.com/problems/student-attendance-record-ii/discuss/173275/Simple-8-line-O(n)-Python-with-Explanation
     * https://leetcode.com/problems/student-attendance-record-ii/discuss/101634/Python-DP-with-explanation
     * https://leetcode.com/problems/student-attendance-record-ii/discuss/415467/Python-O(log-n)-using-NumPy
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/student-attendance-record-ii/discuss/101650/552.-Student-Attendance-Record-II-C%2B%2B_with-explanation_from-DFS-to-DP
     * https://leetcode.com/problems/student-attendance-record-ii/discuss/101643/Share-my-O(n)-C%2B%2B-DP-solution-with-thinking-process-and-explanation
     * https://leetcode.com/problems/student-attendance-record-ii/discuss/369842/O(n)-C%2B%2B-DP-Solution
     * https://leetcode.com/problems/student-attendance-record-ii/discuss/101656/C%2B%2B-O(log(n))-time-O(1)-space.-Explanation-with-Diagrams!
     */
	
	/**
	 * JavaScript collections
	 * 
	 * https://leetcode.com/problems/student-attendance-record-ii/discuss/213455/JavaScript-easy-short-simple-with-diagrams
	 * https://leetcode.com/problems/student-attendance-record-ii/discuss/413847/javascript-solution
	 */

}
