package OJ0081_0090;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

public class Scramble_String {
	/*
	 * https://leetcode.com/problems/scramble-string/discuss/205451/JavaAnother-dp-solution-with-explanation
	 * 
	 * 1. dp[i][j][1] indiates whether s1[i] equals to s2[j] and third dimension 
	 *    represents length.
	 * 2. dp[i][j][k] indicates whether s1[i, i+k) can be changed from s2[j, j+k).
	 * 3. if dp[i][j][p] and dp[i+p][j+p][k-p] are true, dp[i][j][k] is true. You 
	 *    can understand as which s1[i, i+p) and s2[j, j+p) is scramble and 
	 *    s1[i+p, i+k) and s2[j+p, j+k) is scramble, so s1[i, i+k) and s2[j, j+k) 
	 *    is scramble.
	 * 4. There is same argument. if dp[i][j+k-p][p] and dp[i+p][j][k-p] are true, 
	 *    dp[i][j][k] is true.
	 * 
	 * Let p be the length of a cut (hence, 1 <= p < k), then we are in the following 
	 * situation:
	 * 
	 * S1 [   x1    |         x2         ]
	 *    i         i + p                i + k - 1
	 * 
	 * here we have two possibilities:
	 *      
	 * S2 [   y1    |         y2         ]
	 *    j         j + p                j + k - 1
	 *    
	 * or 
	 * 
	 * S2 [       y1        |     y2     ]
	 *    j                 j + k - p    j + k - 1
	 * 
	 * Rf :
	 * https://leetcode.com/problems/scramble-string/discuss/29396/Simple-iterative-DP-Java-solution-with-explanation
	 * https://leetcode.wang/leetCode-87-Scramble-String.html
	 * https://leetcode.com/problems/scramble-string/discuss/359319/Share-my-notes-of-this-problem
	 * https://leetcode.com/problems/scramble-string/discuss/29396/Simple-iterative-DP-Java-solution-with-explanation/542773
	 * 
	 * Other code:
	 * https://leetcode.com/problems/scramble-string/discuss/29414/Java-fast-DP-iteration-solution-and-recursion-solution
	 */
	public boolean isScramble_dp(String s1, String s2) {
		if (s1 == null || s2 == null)
			return false;
		
		int m = s1.length();
		int n = s2.length();
		if (m != n)
			return false;

		boolean[][][] dp = new boolean[m][m][m + 1];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < m; j++) {
				dp[i][j][1] = s1.charAt(i) == s2.charAt(j);
			}
		}

		for (int k = 2; k <= m; k++) {
			for (int i = 0; i <= m - k; i++) {
				for (int j = 0; j <= m - k; j++) {
					dp[i][j][k] = false;
					
					// check for all possible cuts
					for (int p = 1; p < k; p++) {
						if ((dp[i][j][p] && dp[i + p][j + p][k - p])
							|| (dp[i][j + k - p][p] && dp[i + p][j][k - p])) {
							
							dp[i][j][k] = true;
						}
					}
				}
			}
		}
		
		return dp[0][0][s1.length()];
	}
	
	/*
	 * The following 2 functions are modified by myself.
	 * 
	 * Rf :
	 * https://leetcode.wang/leetCode-87-Scramble-String.html
	 * https://leetcode.com/problems/scramble-string/discuss/832610/Java-DP-(Recursion-%2B-Memoization)
	 */
	public boolean isScramble_modified_self(String s1, String s2) {
		if (s1.length() != s2.length())
			return false;
		
		Map<String, Boolean> cache = new HashMap<>();
		return dfs_modified_self(s1, s2, cache);
	}

	private boolean dfs_modified_self(String s1, String s2, 
			Map<String, Boolean> cache) {
		
		String key = s1 + "_" + s2;
		
		if (cache.containsKey(key)) {
			return cache.get(key);
		}
		
		if (s1.equals(s2)) {
			cache.put(key, true);
			return true;
		}
		
		int[] letters = new int[26];
        for (int i = 0; i < s1.length(); i++) {
            letters[s1.charAt(i) - 'a']++;
            letters[s2.charAt(i) - 'a']--;
        }
        for (int i = 0; i < 26; i++) {        	
        	if (letters[i] != 0) {
        		cache.put(s1 + "_" + s2, false);
        		return false; 
        	}
        }
		
        int n = s1.length();
		boolean res = false;
		
		// 各點切割
		for (int i = 1; i < n; i++) {
			// 交換相同
			if (dfs_modified_self(s1.substring(0, i), s2.substring(n - i, n), cache) 
					&& dfs_modified_self(s1.substring(i, n), s2.substring(0, n - i), 
							cache)) {
				
				res = true;
                break;
			} 
			// 原先相同
			else if (dfs_modified_self(s1.substring(0, i), s2.substring(0, i), cache) 
					&& dfs_modified_self(s1.substring(i, n), s2.substring(i, n), 
							cache)) {
				
				res = true;
                break;
			}
		}
		
		cache.put(key, res);
		return res;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/scramble-string/discuss/359319/Share-my-notes-of-this-problem
	 * 
	 * true 和 false 的結果都有被利用
	 * 初始化為 0, true 為 1, false 為 2
	 */
	public boolean isScramble_boolean3(String s1, String s2) {
		if (s1.equals(s2)) {
			return true;
		}
		if (s1.length() != s2.length()) {
			return false;
		}
		
		int len = s1.length();
		int[][][] map = new int[len + 1][len + 1][len + 1];
		return helper_boolean3(s1, 0, s2, 0, len, map);
	}

	public boolean helper_boolean3(String s1, int i, String s2, int j, int len, 
			int[][][] map) {
		
		if (len == 0) {
			return true;
		}
		
		if (len == 1 && s1.charAt(i) == s2.charAt(j)) {
			map[i][j][len] = 1;
		}
		if (map[i][j][len] != 0) {
			return map[i][j][len] == 1;
		}
		
		for (int k = 1; k < len; k++) {
			if (helper_boolean3(s1, i, s2, j, k, map) 
					&& helper_boolean3(s1, i + k, s2, j + k, len - k, map)) {
				
				map[i][j][len] = 1;
				return true;
			}
			if (helper_boolean3(s1, i, s2, j + len - k, k, map) 
					&& helper_boolean3(s1, i + k, s2, j, len - k, map)) {
				
				map[i][j][len] = 1;
				return true;
			}
		}
		
		map[i][j][len] = 2;
		return false;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/scramble-string/discuss/359319/Share-my-notes-of-this-problem
	 * 
	 * 沒有利用到 false 的結果
	 * 
	 * TLE
	 * use recursive plus memory method. there still have duplicate calculation
	 */
	public boolean isScramble_boolean2(String s1, String s2) {
		if (s1.equals(s2)) {
			return true;
		}
		if (s1.length() != s2.length()) {
			return false;
		}
		
		int len = s1.length();
		boolean[][][] map = new boolean[len + 1][len + 1][len + 1];
		return helper_boolean2(s1, 0, s2, 0, len, map);
	}

	public boolean helper_boolean2(String s1, int i, String s2, int j, int len, 
			boolean[][][] map) {
		
		if (len == 0) {
			return true;
		}
		
		if (len == 1 && s1.charAt(i) == s2.charAt(j)) {
			map[i][j][len] = true;
		}
		if (map[i][j][len]) {
			return true;
		}
		
		for (int k = 1; k < len; k++) {
			if (helper_boolean2(s1, i, s2, j, k, map) 
					&& helper_boolean2(s1, i + k, s2, j + k, len - k, map)) {
				
				return map[i][j][len] = true;
			}
			if (helper_boolean2(s1, i, s2, j + len - k, k, map) 
					&& helper_boolean2(s1, i + k, s2, j, len - k, map)) {
				
				return map[i][j][len] = true;
			}
		}
		return map[i][j][len] = false;
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/scramble-string/discuss/832610/Java-DP-(Recursion-%2B-Memoization)
	 */
	Map<Integer, Boolean> cache_memorization;

	public boolean isScramble_memorization(String a, String b) {
		if (a.length() != b.length())
			return false;
		
		if (a.length() == 0)
			return false;
		
		cache_memorization = new HashMap<>();
		return isSR_memorization(a, b);
	}

	private boolean isSR_memorization(String a, String b) {
		int hash = (a + "_" + b).hashCode();
		
		if (cache_memorization.get(hash) != null) {
			return cache_memorization.get(hash);
		}
		
		int n = a.length();
		if (a.equals(b))
			return true;
		
		if (n <= 1)
			return false;

		boolean isSS = false;
		boolean swapped = false;
		boolean not_swapped = false;
		
		for (int i = 1; i < n; i++) {
			if (isSR_memorization(a.substring(0, i), b.substring(n - i, n)) 
					&& isSR_memorization(a.substring(i, n), b.substring(0, n - i))) {
				
				swapped = true;
			} 
			else if (isSR_memorization(a.substring(0, i), b.substring(0, i)) 
					&& isSR_memorization(a.substring(i, n), b.substring(i, n))) {
				
				not_swapped = true;
			}
			
			if (swapped || not_swapped) {
				isSS = true;
				break;
			}
		}
		
		cache_memorization.put(hash, isSS);
		return isSS;
	}
	
	/*
	 * https://leetcode.com/problems/scramble-string/discuss/285215/java-2ms-recursive-solution-with-explanation
	 * 
	 * We can judge whether s1 and s2 can scramble into each other through 
	 * mathematical induction:
	 * 
	 * + The base case that s1 can scramble into s2 if s1 == s2. If the frequencies 
	 *   of each characters appearing in s1 and s2 differ, then s1 can not scramble 
	 *   into s2.
	 * 
	 * + If there exist 0 <= i <= s1.length() where
	 * 
	 *   1. s1[0, i] can scramble into s2[0, i] and s1[i, length] can scramble into 
	 *      s2[i, length]; or
	 *   2. s1[0, i] can scramble into s2[length - i, length] and s1[i, length] can 
	 *      scramble into s2[0, length - i]
	 * then, s1 can scramble into s2.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/scramble-string/discuss/285215/java-2ms-recursive-solution-with-explanation/293864
	 * 
	 * Other code:
	 * https://leetcode.com/problems/scramble-string/discuss/29387/Accepted-Java-solution
	 * https://leetcode.com/problems/scramble-string/discuss/29414/Java-fast-DP-iteration-solution-and-recursion-solution
	 */
	public boolean isScramble2(String s1, String s2) {
		if (s1.equals(s2))
			return true;
		
		int s1Array[] = new int[26];
		int s2Array[] = new int[26];
		for (int i = 0; i < s1.length(); i++) {
			s1Array[s1.charAt(i) - 'a']++;
			s2Array[s2.charAt(i) - 'a']++;
		}
		
		for (int i = 0; i < 26; i++)
			if (s1Array[i] != s2Array[i])
				return false;
		
		for (int i = 1; i < s1.length(); i++) {
			if ( isScramble2(s1.substring(0, i), s2.substring(0, i))
				  && isScramble2(s1.substring(i), s2.substring(i)) )
	            return true;
			
	        if ( isScramble2(s1.substring(0, i), s2.substring(s1.length() - i))
	        	  && isScramble2(s1.substring(i), s2.substring(0, s1.length() - i)) )
	            return true;
	    }
	    return false;
	}
	
	// https://leetcode.com/problems/scramble-string/discuss/29445/Any-better-solution/28507
	public boolean isScramble_sort_recursive(String s1, String s2) {
		if (s1 == null || s2 == null || s1.length() != s2.length())
			return false;
		
		if (s1.equals(s2))
			return true;
		
		char[] c1 = s1.toCharArray();
		char[] c2 = s2.toCharArray();
		Arrays.sort(c1);
		Arrays.sort(c2);
		
		if (!Arrays.equals(c1, c2))
			return false;
		
		for (int i = 1; i < s1.length(); i++) {
			if (isScramble_sort_recursive(s1.substring(0, i), s2.substring(0, i)) 
					&& isScramble_sort_recursive(s1.substring(i), s2.substring(i)))
				return true;
			
			if (isScramble_sort_recursive(s1.substring(0, i), 
					s2.substring(s2.length() - i))
					&& isScramble_sort_recursive(s1.substring(i), 
							s2.substring(0, s2.length() - i)))
				return true;
		}
		return false;
	}

	/*
	 * The following variable and function are from this link.
	 * https://leetcode.com/problems/scramble-string/discuss/29434/2ms-Java-Recursive-solution-(beat-100)
	 * 
	 * Here, a is a custom hash of s1.substring(0,i), b is for the last i elements of 
	 * s2, c is for the first i elements of s2.
	 * 
	 * Although they are only hash code, but the hash functions are bijective due to 
	 * the prime numbers.
	 * 
	 * This is the naive solution plus hashing for optimization. One important aspect 
	 * is that the hash values depend only on the characters but not on their order.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/scramble-string/discuss/29434/2ms-Java-Recursive-solution-(beat-100)/28462
	 * https://leetcode.com/problems/scramble-string/discuss/29434/2ms-Java-Recursive-solution-(beat-100)/28461
	 */
	int[] p_hash_recur = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83,
			89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197,
			199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311, 313, 317,
			331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397, 401, 409, 419, 421, 431, 433, 439, 443, 449,
			457, 461, 463, 467, 479, 487, 491, 499, 503, 509, 521, 523, 541, 547, 557, 563, 569, 571, 577, 587, 593,
			599, 601, 607, 613, 617, 619, 631, 641, 643, 647, 653, 659, 661, 673, 677, 683, 691, 701, 709, 719, 727,
			733, 739, 743, 751, 757, 761, 769, 773, 787, 797, 809, 811, 821, 823, 827, 829, 839, 853, 857, 859, 863,
			877, 881, 883, 887, 907, 911, 919, 929, 937, 941, 947, 953, 967, 971, 977, 983, 991, 997 };

	public boolean isScramble_hash_recur(String s1, String s2) {
		int l1 = s1.length(), l2 = s2.length();

		if (l1 != l2)
			return false;
		if (l1 <= 1)
			return s1.equals(s2);
		if (s1.equals(s2))
			return true;
		
		long a = 1, b = 1, c = 1;
		for (int i = 0; i < l1; i++) {
			if (i > 0 && a == b 
					&& isScramble_hash_recur(s1.substring(0, i), s2.substring(l2 - i))
					&& isScramble_hash_recur(s1.substring(i), s2.substring(0, l2 - i)))
				return true;
			
			if (i > 0 && a == c 
					&& isScramble_hash_recur(s1.substring(0, i), s2.substring(0, i))
					&& isScramble_hash_recur(s1.substring(i), s2.substring(i)))
				return true;
			
			a *= p_hash_recur[s1.charAt(i) - 'A'];
			b *= p_hash_recur[s2.charAt(l2 - 1 - i) - 'A'];
			c *= p_hash_recur[s2.charAt(i) - 'A'];
		}
		return false;
	}

	/**
     * Python collections
     * 
     * https://leetcode.com/problems/scramble-string/discuss/29452/Python-dp-solutions-(with-and-without-memorization).
     * https://leetcode.com/problems/scramble-string/discuss/29459/Python-recursive-solution
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/scramble-string/discuss/29394/My-C%2B%2B-solutions-(recursion-with-cache-DP-recursion-with-cache-and-pruning)-with-explanation-(4ms)
     * https://leetcode.com/problems/scramble-string/discuss/29469/C%2B%2B-solutions-w-explanation.-Both-recursive-and-Top-Down-Dynamic-Programming.
     * https://leetcode.com/problems/scramble-string/discuss/29392/Share-my-4ms-c%2B%2B-recursive-solution
     * https://leetcode.com/problems/scramble-string/discuss/29411/Optimized-recursive-(0ms)-and-DP-(20ms)-solution-C-beating-100-submissions
     */
	
}
