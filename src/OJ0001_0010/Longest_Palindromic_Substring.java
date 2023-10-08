package OJ0001_0010;

public class Longest_Palindromic_Substring {
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/articles/longest-palindromic-substring/
	 * 
	 * A palindrome can be expanded from its center, and there are only 2n - 1 centers.
	 * The reason is the center of a palindrome can be in between two letters. Such 
	 * palindromes have even number of letters (such as "abba") and its center are 
	 * between the two 'b's.
	 * 
	 * Rf : 
	 * https://leetcode.com/problems/longest-palindromic-substring/discuss/2928/Very-simple-clean-java-solution
	 * https://leetcode.wang/leetCode-5-Longest-Palindromic-Substring.html
	 */
	public String longestPalindrome_expand(String s) {
		if (s == null || s.length() < 1)
			return "";
		
		int start = 0, end = 0;
		for (int i = 0; i < s.length(); i++) {
			int len1 = expandAroundCenter(s, i, i);       // assume odd length
			int len2 = expandAroundCenter(s, i, i + 1);   // assume even length
			
			int len = Math.max(len1, len2);
			if (len > end - start) {
				start = i - (len - 1) / 2;
				end = i + len / 2;
			}
		}
		return s.substring(start, end + 1);
	}

	private int expandAroundCenter(String s, int left, int right) {
		int L = left, R = right;
		while (L >= 0 && R < s.length() && s.charAt(L) == s.charAt(R)) {
			L--;
			R++;
		}
		return R - L - 1;
	}
	
	/*
	 * The following 2 functions are by myself
	 * 
	 * 看 647. Palindromic Substrings
	 * 
	 * Rf :
	 * https://leetcode.wang/leetCode-5-Longest-Palindromic-Substring.html
	 */
	public String longestPalindrome_Manacher2(String s) {
        String t = transform_Manacher(s);
        
        // P 為回文半徑，不包含自己
        int[] P = new int[t.length()];
        
        // C 為當前回文中心，R 為當前回文半徑
        int C = 0, R = 0;
        
        // 頭尾是標記符號，所以去除頭尾
        for (int i = 1; i < t.length() - 1; i++) {
            int i_mirror = 2 * C - i;
            
            // 防止超出 R
            if (R > i) {
                P[i] = Math.min(P[i_mirror], R - i);
            }
            // R == i
            else {
                P[i] = 0;
            }
            
            // 例外情況，使用中心擴展法
			// P[i] 為回文半徑，P[i] + 1 為下一個要比較的字元
			// 因為插入不同的頭尾標示符號 (^$)，所以會因為字元不同而自動跳出 while loop
            while (t.charAt(i + P[i] + 1) == t.charAt(i - P[i] - 1)) {
                P[i]++;
            }
            
            // 若生成區域超過 R，更新 C、R
            if (i + P[i] > R) {
                C = i;
                R = i + P[i];
            }
        }
        
        int pos = 0;
        int length = P[0];
        for (int i = 1; i < t.length(); i++) {
            if (P[i] > length) {
                pos = i;
                length = P[i];
            }
        }
        
        int start = (pos - length) / 2;
        return s.substring(start, start + length);
    }
    
    private String transform_Manacher(String s) {
        
        // ^、_、$ 為原字串所沒有的字元
		// 使用 _ 插入每個字元，並同時添加在頭尾
		// $ 代表頭部起始。 ^ 代表尾部結束
        
        if (s == null || s.length() == 0) {
            return "$^";
        }
        
        StringBuilder sb = new StringBuilder("$");
        char[] ch = s.toCharArray();
        
        for (int i = 0; i < ch.length; i++) {
            sb.append("_" + ch[i]);
        }
        
        sb.append("_^");
        
        return sb.toString();
    }
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/longest-palindromic-substring/discuss/3060/(AC)-relatively-short-and-very-clear-Java-solution
	 * 
	 * Every time we move to right, we only need to consider whether using this new 
	 * character as tail could produce new palindrome string of length 
	 * (current length +1) or (current length +2)
	 * 
	 * Suppose max is the longest palindrome substring of s[0..i). Then for s[0..i+1) 
	 * we only need to consider palindromes ending at s[i] (inclusive)
	 * 
	 * If we were able to find a palindrome of length max + k (k > 2) ending at i, 
	 * then it would mean that there is a palindrome of length max + k - 2 > max 
	 * ending at i - 1, which contradicts the assumption that max is the maximum 
	 * length of a palindrome found so far.
	 * 
	 * Rf : https://leetcode.com/problems/longest-palindromic-substring/discuss/3003/Java-easy-understanding-solution.-Beats-97/3755
	 * 
	 * Other code :
	 * https://leetcode.com/problems/longest-palindromic-substring/discuss/3003/Java-easy-understanding-solution.-Beats-97
	 */
	public String longestPalindrome_check(String s) {
		String res = "";
		int currLength = 0;
		for (int i = 0; i < s.length(); i++) {
			if (isPalindrome(s, i - currLength - 1, i)) {
				res = s.substring(i - currLength - 1, i + 1);
				currLength = currLength + 2;
			} 
			else if (isPalindrome(s, i - currLength, i)) {
				res = s.substring(i - currLength, i + 1);
				currLength = currLength + 1;
			}
		}
		return res;
	}

	public boolean isPalindrome(String s, int begin, int end) {
		if (begin < 0)
			return false;
		while (begin < end) {
			if (s.charAt(begin++) != s.charAt(end--))
				return false;
		}
		return true;
	}
	
	/*
	 * https://leetcode.com/problems/longest-palindromic-substring/discuss/110375/Java-DP-solution-with-optimization-in-space-from-O(n2)-to-O(n)-time-O(n2)-with-very-clear-explanations
	 * 
	 * dp(i, j) represents whether s(i ... j) can form a palindromic substring, 
	 * dp(i, j) is true when s(i) equals to s(j) and s(i+1 ... j-1) is a palindromic 
	 * substring. When we found a palindrome, check if it's the longest one.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/longest-palindromic-substring/discuss/2921/Share-my-Java-solution-using-dynamic-programming
	 * https://leetcode.com/problems/longest-palindromic-substring/discuss/2921/Share-my-Java-solution-using-dynamic-programming/243271
	 * https://leetcode.com/problems/longest-palindromic-substring/discuss/2921/Share-my-Java-solution-using-dynamic-programming/3567
	 * 
	 * Other code :
	 * https://leetcode.com/problems/longest-palindromic-substring/discuss/151144/Bottom-up-DP-Logical-Thinking
	 */
	public String longestPalindrome_dp(String s) {
		int n = s.length();
		String result = "";
		boolean[][] dp = new boolean[n][n];

		// i goes from n - 1 to 0, j goes from i to n - 1, 
		// to make sure i occurs before j
		for (int i = n - 1; i >= 0; i--) {
			for (int j = i; j < n; j++) {
				// if you have a word whose number is less than 2, and 
				// dp[i+1][j-1] will not success
				dp[i][j] = s.charAt(i) == s.charAt(j) && 
						(j - i < 2 || dp[i + 1][j - 1]);
				
				// if dp[i][j] is true，update result
				if (dp[i][j] && (result.equals("") || j - i + 1 > result.length())) {
					result = s.substring(i, j + 1); // substring includes j
				}
			}
		}
		return result;
	}
	
	/*
	 * https://leetcode.com/problems/longest-palindromic-substring/discuss/128054/Java-(Beats-98)-and-JavaScript-(Beats-100)-O(n)-Manacher's-Algorithm-with-Detailed-Explanation
	 * 
	 * Rf :
	 * https://leetcode.wang/leetCode-5-Longest-Palindromic-Substring.html
	 */
	public String longestPalindrome_Manacher(String s) {
		
        /* Preprocess s: insert '#' between characters, so we don't need to worry about 
         * even or odd length palindromes. */
        char[] newStr = new char[s.length() * 2 + 1];
        newStr[0] = '#';
        for (int i = 0; i < s.length(); i++) {
            newStr[2 * i + 1] = s.charAt(i);
            newStr[2 * i + 2] = '#';
        }
        
        /* Process newStr */
        /* dp[i] is the length of LPS centered at i */
        int[] dp = new int[newStr.length];
        
        /**
         * For better understanding, here we define "friend substring", or "friend":
         * "friend substring" has the largest end-index in all checked substrings that
         * are palindromes. We start at friendCenter = 0 and update it in each cycles.
         */
        int friendCenter = 0, friendRadius = 0, lpsCenter = 0, lpsRadius = 0;
        
        /* j is the symmetry of i with respect to friendCenter */
        int j;
        for (int i = 0; i < newStr.length; i++) {
            /* Calculate dp[i] */
            if (friendCenter + friendRadius > i) {
                /**
                 * This is the most important part of the algorithm.
                 * 
                 * Normally we start from dp[i] = 1 and then try to expand dp[i] by 
                 * doing brute-force palindromic checks. However, if i is in the range 
                 * of friend (friendCenter + friendRadius > i), we can expect
                 * dp[i] = dp[j] because friend is a palindrome. This only works within 
                 * the range of friend, so the
                 * max value of dp[i] we can trust is (friendEnd - i).
                 * 
                 * Here is an example:
                 *
                 *     friendStart   j             friendCenter  i     friendEnd                         
                 *               |   |             |             |     |
                 * String: - - d c b a b c d - - - - - - - d c b a b c ? - - - - - - - -
                 *               [--------friend (palindrome)--------]
                 *
                 * In this example, (friendEnd - i) = 3, so we can only be certain that 
                 * radius <= 3 part around i is a palindrome (i.e. "cbabc" part). We 
                 * still need to check the character at "?".
                 */
                j = friendCenter - (i - friendCenter);
                dp[i] = Math.min(dp[j], (friendCenter + friendRadius) - i);
            }
            else {
                /* Calculate from scratch */
                dp[i] = 1;
            }
            
            /* Check palindrome and expand dp[i] */
			while (i + dp[i] < newStr.length && i - dp[i] >= 0 
					&& newStr[i + dp[i]] == newStr[i - dp[i]])
				dp[i]++;

            /* Check if i should become the new friend */
            if (friendCenter + friendRadius < i + dp[i]) {
                friendCenter = i;
                friendRadius = dp[i];
            }
            
            /* Update longest palindrome */
            if (lpsRadius < dp[i]) {
                lpsCenter = i;
                lpsRadius = dp[i];
            }
        }
        return s.substring((lpsCenter - lpsRadius + 1) / 2, 
        		(lpsCenter + lpsRadius - 1) / 2);
    }
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/longest-palindromic-substring/discuss/2954/Python-easy-to-understand-solution-with-comments-(from-middle-to-two-ends).
     * https://leetcode.com/problems/longest-palindromic-substring/discuss/2925/Python-O(n2)-method-with-some-optimization-88ms.
     * https://leetcode.com/problems/longest-palindromic-substring/discuss/121496/Python-DP-solution
     * https://leetcode.com/problems/longest-palindromic-substring/discuss/3337/Manacher-algorithm-in-Python-O(n)
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/longest-palindromic-substring/discuss/3098/My-simple-c%2B%2B-solution
     * https://leetcode.com/problems/longest-palindromic-substring/discuss/2923/Simple-C%2B%2B-solution-(8ms-13-lines)
     * https://leetcode.com/problems/longest-palindromic-substring/discuss/147548/Direct-c%2B%2B-DP
     * https://leetcode.com/problems/longest-palindromic-substring/discuss/2967/22-line-C%2B%2B-Manacheru2019s-Algorithm-O(n)-Solution
     */
}
