package OJ0001_0010;

public class Regular_Expression_Matching {
	/*
	 * https://discuss.leetcode.com/topic/40371/easy-dp-java-solution-with-detailed-explanation
	 * 
	 * p.charAt(j) == s.charAt(i) :  dp[i][j] = dp[i-1][j-1];
	 * p.charAt(j) == '.' : dp[i][j] = dp[i-1][j-1];
	 * 
	 * dp[i][j] = dp[i-1][j]    //in this case, a* counts as multiple a
	 * dp[i][j] = dp[i][j-2]   // in this case, a* counts as empty
	 * 
	 * Rf :
	 * https://discuss.leetcode.com/topic/2601/my-ac-dp-solution-for-this-problem-asking-for-improvements
	 * https://discuss.leetcode.com/topic/6183/my-concise-recursive-and-dp-solutions-with-full-explanation-in-c
	 */
	public boolean isMatch(String s, String p) {
		if (s == null || p == null) {
			return false;
		}
		boolean[][] dp = new boolean[s.length() + 1][p.length() + 1];
		dp[0][0] = true;
		for (int i = 0; i < p.length(); i++) {
			if (p.charAt(i) == '*' && dp[0][i - 1]) {
				dp[0][i + 1] = true;
			}
		}
		
		for (int i = 0; i < s.length(); i++) {
			for (int j = 0; j < p.length(); j++) {
				if (p.charAt(j) == '.') {
					dp[i + 1][j + 1] = dp[i][j];
				}
				if (p.charAt(j) == s.charAt(i)) {
					dp[i + 1][j + 1] = dp[i][j];
				}
				if (p.charAt(j) == '*') {
					if (p.charAt(j - 1) != s.charAt(i) && p.charAt(j - 1) != '.') {
						dp[i + 1][j + 1] = dp[i + 1][j - 1];
					} else {
						dp[i + 1][j + 1] = (dp[i][j + 1] || dp[i + 1][j - 1]);
					}
				}
			}
		}
		return dp[s.length()][p.length()];
	}
	
	/*
	 * https://discuss.leetcode.com/topic/27988/java-solution-o-n-2-dp-with-some-explanations
	 * 
	 * P[i][j] to be true if s[0..i) matches p[0..j)
	 * 1. P[i][j] = P[i - 1][j - 1], if p[j - 1] != '*' && (s[i - 1] == p[j - 1] || p[j - 1] == '.')
	 * 2. P[i][j] = P[i][j - 2], if p[j - 1] == '*' and the pattern repeats for 0 times;
	 * 3. P[i][j] = P[i - 1][j] && (s[i - 1] == p[j - 2] || p[j - 2] == '.'), 
	 * if p[j - 1] == '*' and the pattern repeats for at least 1 times.
	 * 
	 * Rf : https://discuss.leetcode.com/topic/17852/9-lines-16ms-c-dp-solutions-with-explanations
	 */
	public boolean isMatch2(String s, String p) {
		int sL = s.length(), pL = p.length();
		boolean[][] dp = new boolean[sL + 1][pL + 1];
		dp[0][0] = true; // If s and p are "", isMathch() returns true;

		for (int i = 0; i <= sL; i++) {

			// j starts from 1, since dp[i][0] is false when i != 0;
			for (int j = 1; j <= pL; j++) {
				char c = p.charAt(j - 1);

				if (c != '*') {
					// The last character of s and p should match;
					// And, dp[i-1][j-1] is true;
					dp[i][j] = i > 0 && dp[i - 1][j - 1] && (c == '.' || c == s.charAt(i - 1));
				} 
				else {
					// Two situations:
					// (1) dp[i][j-2] is true, and there is 0 preceding element of '*';
					// (2) The last character of s should match the preceding element of '*';
					// And, dp[i-1][j] should be true;
					// j - 1 = 0 is the first element in p, and it will not be '*'.
					dp[i][j] = (dp[i][j - 2]) || (i > 0 && dp[i - 1][j]
							&& (p.charAt(j - 2) == '.' || p.charAt(j - 2) == s.charAt(i - 1)));
				}
			}
		}

		return dp[sL][pL];
	}
	
	// https://discuss.leetcode.com/topic/31974/java-4ms-dp-solution-with-o-n-2-time-and-o-n-space-beats-95
	public boolean isMatch_1D(String s, String p) {
		/**
		 * This solution is assuming s has no regular expressions.
		 * 
		 * dp: res[i][j]=is s[0,...,i-1] matched with p[0,...,j-1];
		 * 
		 * If p[j-1]!='*', res[i][j] = res[i-1][j-1] &&
		 * (s[i-1]==p[j-1]||p[j-1]=='.'). Otherwise, res[i][j] is true if
		 * res[i][j-1] or res[i][j-2] or
		 * res[i-1][j]&&(s[i-1]==p[j-2]||p[j-2]=='.'), and notice the third
		 * 'or' case includes the first 'or'.
		 * 
		 * 
		 * Boundaries: res[0][0]=true;//s=p="". res[i][0]=false, i>0.
		 * res[0][j]=is p[0,...,j-1] empty, j>0, and so res[0][1]=false,
		 * res[0][j]=p[j-1]=='*'&&res[0][j-2].
		 * 
		 * O(n) space is enough to store a row of res.
		 */

		int m = s.length(), n = p.length();
		boolean[] res = new boolean[n + 1];
		res[0] = true;

		int i, j;
		for (j = 2; j <= n; j++)
			res[j] = res[j - 2] && p.charAt(j - 1) == '*';

		char pc, sc, tc;
		boolean pre, cur; // pre=res[i - 1][j - 1], cur=res[i-1][j]

		for (i = 1; i <= m; i++) {
			pre = res[0];
			res[0] = false;
			sc = s.charAt(i - 1);

			for (j = 1; j <= n; j++) {
				cur = res[j];
				pc = p.charAt(j - 1);
				if (pc != '*')
					res[j] = pre && (sc == pc || pc == '.');
				else {
					// pc == '*' then it has a preceding char, i.e. j>1
					tc = p.charAt(j - 2);
					res[j] = res[j - 2] || (res[j] && (sc == tc || tc == '.'));
				}
				pre = cur;
			}
		}

		return res[n];
	}
	
	// https://discuss.leetcode.com/topic/9555/dp-java-solution-detail-explanation-from-2d-space-to-1d-space
	
	// https://discuss.leetcode.com/topic/12289/clean-java-solution
	public boolean isMatch_backtrack(String s, String p) {
		if (p.isEmpty()) {
			return s.isEmpty();
		}

		if (p.length() == 1 || p.charAt(1) != '*') {
			if (s.isEmpty() || (p.charAt(0) != '.' && p.charAt(0) != s.charAt(0))) {
				return false;
			} else {
				return isMatch_backtrack(s.substring(1), p.substring(1));
			}
		}

		// P.length() >=2
		while (!s.isEmpty() && (s.charAt(0) == p.charAt(0) || p.charAt(0) == '.')) {
			if (isMatch_backtrack(s, p.substring(2))) {
				return true;
			}
			s = s.substring(1);
		}

		return isMatch_backtrack(s, p.substring(2));
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/7437/share-a-short-java-solution
	 */
	public boolean isMatch_recursive(String s, String p) {
		if (p.contains(".") || p.contains("*")) {
			if (p.length() == 1 || p.charAt(1) != '*')
				return comp(s, p, s.length(), 0) && isMatch_recursive(s.substring(1), p.substring(1));
			for (int i = 0; i == 0 || comp(s, p, s.length(), i - 1); i++) {
				if (isMatch_recursive(s.substring(i), p.substring(2)))
					return true;
			}
		}
		return s.equals(p);
	}
	private boolean comp(String s, String p, int sLen, int i) {
		return sLen > i && (p.charAt(0) == s.charAt(i) || p.charAt(0) == '.');
	}

}
