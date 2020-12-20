package OJ0641_0650;

public class Palindromic_Substrings {
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/palindromic-substrings/discuss/105689/Java-solution-8-lines-extendPalindrome/144768
	 * 
	 * Start with one letter and two letter strings and expand on both sides. 
	 * These are the smallest substrings and further checks are built on top of them. 
	 * start with single letter strings, build up for odd length strings
	 * start with two lettered strings, build up for even length strings
	 * 
	 * Rf :
	 * https://leetcode.com/problems/palindromic-substrings/discuss/105688/Very-Simple-Java-Solution-with-Detail-Explanation
	 * https://leetcode.com/problems/palindromic-substrings/discuss/339383/Java-or-Short-and-Clean-or-Time-and-memory-beats-100-or-2-methods
	 * https://leetcode.com/problems/palindromic-substrings/discuss/105739/Java-O(n2)-time-O(1)-space-solution-with-comments.
	 * https://leetcode.com/problems/palindromic-substrings/solution/
	 * 
	 * Other code:
	 * https://leetcode.com/problems/palindromic-substrings/discuss/105689/Java-solution-8-lines-extendPalindrome/223769
	 * https://leetcode.com/problems/palindromic-substrings/discuss/105691/JavaC%2B%2B-6-lines-solution-NO-DP
	 */
	public int countSubstrings_expand2(String s) {
		int count = 0;
		
		// Loop across different middle points.
		for (int i = 0; i < s.length(); i++) {
			count += extractPalindrome_expand2(s, i, i);     // odd length mid i
			count += extractPalindrome_expand2(s, i, i + 1); // even length mid i, i+1
		}
		return count;
	}

	// Expend from the current mid point to all of its low and high positions.
	public int extractPalindrome_expand2(String s, int left, int right) {
		int count = 0;
		while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
			left--;
			right++;
			count++;
		}
		return count;
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/palindromic-substrings/discuss/105689/Java-solution-8-lines-extendPalindrome
	 * 
	 * start from each index and try to extend palindrome for both odd and even length.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/palindromic-substrings/discuss/105688/Very-Simple-Java-Solution-with-Detail-Explanation
	 */
	int count_expand = 0;

	public int countSubstrings_expand(String s) {
		if (s == null || s.length() == 0)
			return 0;

		for (int i = 0; i < s.length(); i++) { // i is the mid point
			extendPalindrome_expand(s, i, i); // odd length;
			extendPalindrome_expand(s, i, i + 1); // even length
		}

		return count_expand;
	}

	private void extendPalindrome_expand(String s, int left, int right) {
		while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
			count_expand++;
			left--;
			right++;
		}
	}
	
	/*
	 * https://leetcode.com/problems/palindromic-substrings/discuss/105749/Java-O(n2)-DP-solution
	 * 
	 * If a substring with size less than or equal to 3, it will always be a 
	 * palindrome if first and last characters are the same.
	 * 
	 * Once you are in inside the condition: if (s[i] == s[j]), you have 2 options:
	 * - the substring size <= 3: we know it is true as explained above.
	 * - the substring size is greater than 3: we use memorization
	 * 
	 * Rf :
	 * https://leetcode.com/problems/palindromic-substrings/discuss/258917/Java-Simple-Code%3A-DP-short
	 * https://leetcode.com/problems/palindromic-substrings/discuss/258917/Java-Simple-Code:-DP-short/273269
	 * https://leetcode.com/problems/palindromic-substrings/discuss/339383/Java-or-Short-and-Clean-or-Time-and-memory-beats-100-or-2-methods
	 * https://leetcode.com/problems/palindromic-substrings/solution/
	 */
	public int countSubstrings_dp2(String s) {
		int sLen = s.length();
		char[] cArr = s.toCharArray();

		int totalPallindromes = 0;
		boolean[][] dp = new boolean[sLen][sLen];

		// Single length pallindroms
		for (int i = 0; i < sLen; i++) {
			dp[i][i] = true;
			totalPallindromes++;
		}

		// 2 length pallindromes
		for (int i = 0; i < sLen - 1; i++) {
			if (cArr[i] == cArr[i + 1]) {
				dp[i][i + 1] = true;
				totalPallindromes++;
			}
		}

		// Lengths > 3
		for (int subLen = 2; subLen < sLen; subLen++) {
			for (int i = 0; i < sLen - subLen; i++) {
				int j = i + subLen;

				if (dp[i + 1][j - 1] && cArr[i] == cArr[j]) {
					dp[i][j] = true;
					totalPallindromes++;
				}
			}
		}
		return totalPallindromes;
	}
	
	/*
	 * https://leetcode.com/problems/palindromic-substrings/discuss/105707/Java-Python-DP-solution-based-on-longest-palindromic-substring
	 * 
	 * i, i+1, ... j-1, j is a palindrome only if i+1, ..., j-1 is a palindrome and 
	 * a[i] == a[j]
	 * For 1 element and 2 element cases j - i + 1 < 3, its enough to check just for 
	 * a[i] == a[j] [where i == j for 1 element and j == i+1 for 2 elements]
	 * 
	 * The outer for loop has to start from n - 1 (the end). Because the recursion is 
	 * looking forward, referring to i+1.
	 * The inner forward can run normally, but since we are only interested in one 
	 * half of the diagonal, we start at i, every time.
	 * 
	 * For j-i < 3 part: dp[i + 1][j - 1] implies that i+1 <= j-1. Then it's easy to 
	 * get j-i >= 2. That's why we need a special case here when j-i is not >= 2, 
	 * that is when j-i < 3.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/palindromic-substrings/discuss/105707/Java-Python-DP-solution-based-on-longest-palindromic-substring/395332
	 * 
	 * Other code:
	 * https://leetcode.com/problems/palindromic-substrings/discuss/144443/Bottom-up-DP
	 * https://leetcode.com/problems/palindromic-substrings/discuss/160765/Short-Java-REAL-DP-solution
	 */
	public int countSubstrings_dp(String s) {
		int n = s.length();
		int res = 0;
		boolean[][] dp = new boolean[n][n];
		for (int i = n - 1; i >= 0; i--) {
			for (int j = i; j < n; j++) {
				dp[i][j] = s.charAt(i) == s.charAt(j) 
						&& (j - i + 1 < 3 || dp[i + 1][j - 1]);
				
				if (dp[i][j])
					++res;
			}
		}
		return res;
	}
	
	// by myself
	public int countSubstrings_self(String s) {
        int ans = 0;
        
        boolean[][] dp = new boolean[s.length()][s.length()];
        for (int i = 0; i < s.length(); i++) {
            dp[i][i] = true;
            ans++;
        }
        
        for (int i = 1; i < s.length(); i++) {
            for (int j = 0; j + i < s.length(); j++) {
                if (s.charAt(j) == s.charAt(i + j)) {
                    if (dp[j + 1][i + j - 1] || i == 1) {
                        dp[j][i + j] = true;
                        ans++;
                    }
                }
            }
        }
        
        return ans;
    }
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/palindromic-substrings/discuss/753488/Evolve-from-brute-force-to-dp
	 */
	public int countSubstrings_Memo(String s) {
		int n = s.length(), count = n;
		
		Boolean[][] mem = new Boolean[n][n];
		for (int i = 0; i < n; i++)
			for (int j = i + 1; j < n; j++)
				if (isPal_Memo(i, j, s, mem))
					count++;
		return count;
	}

	private boolean isPal_Memo(int i, int j, String s, Boolean[][] mem) {
		if (i >= j)
			return true;
		if (mem[i][j] != null)
			return mem[i][j];
		
		return mem[i][j] = 
				s.charAt(i) == s.charAt(j) && isPal_Memo(i + 1, j - 1, s, mem);
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/palindromic-substrings/solution/
	 * Approach #1: Check All Substrings
	 * 
	 * Other code:
	 * https://leetcode.com/problems/palindromic-substrings/discuss/753488/Evolve-from-brute-force-to-dp
	 */
	public int countSubstrings_brute_force(String s) {
		int ans = 0;

        for (int start = 0; start < s.length(); ++start)
            for (int end = start; end < s.length(); ++end) 
                ans += isPal_brute_force(s, start, end) ? 1 : 0;

        return ans;
	}

	private boolean isPal_brute_force(String s, int start, int end) {
		while (start < end) {
            if (s.charAt(start) != s.charAt(end)) 
                return false;

            ++start;
            --end;
        }

        return true;
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/palindromic-substrings/discuss/392119/Solution-in-Python-3-(beats-~94)-(six-lines)-(With-Detaiiled-Explanation)
     * https://leetcode.com/problems/palindromic-substrings/discuss/128581/Easy-to-understand-Python-DP-solution
     * https://leetcode.com/problems/palindromic-substrings/discuss/105694/Oneliner-Python
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/palindromic-substrings/discuss/495037/C%2B%2B-91-Runtime-Easy-Solution-Expand-from-Center
     * https://leetcode.com/problems/palindromic-substrings/discuss/475745/C%2B%2B-dp-solution%3A-recursive-greater-memoization-greater-tabulation
     * https://leetcode.com/problems/palindromic-substrings/discuss/598236/C%2B%2B-DP-solution-with-detailed-explanation
     * https://leetcode.com/problems/palindromic-substrings/discuss/495284/Using-2D-DP-or-Time%3A-O(N2)-or-Space%3A-O(N2)
     */
	
	/**
	 * JavaScript collections
	 * 
	 * https://leetcode.com/problems/palindromic-substrings/discuss/311492/Super-Easy-Understand-JavaScript-Solution-with-explanation-beat-95
	 */

}
