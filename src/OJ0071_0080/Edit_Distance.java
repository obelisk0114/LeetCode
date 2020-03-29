package OJ0071_0080;

public class Edit_Distance {
	/*
	 * https://leetcode.com/problems/edit-distance/discuss/25914/Concise-JAVA-DP-solution-with-comments
	 * 
	 * Any adjacent value in the matrix dp can only diff by 1. So 
	 * dp[i - 1][j - 1] <= dp[i ¡V 1][j] + 1. Otherwise we can first transform to 
	 * dp[i ¡V 1][j] then do one operation to dp[i - 1][j - 1] and the dp[i-1][j-1] 
	 * would no longer be the minimum distance. The same to dp[i][j ¡V 1]
	 * 
	 * Rf :
	 * https://leetcode.com/problems/edit-distance/discuss/25846/C++-O(n)-space-DP/24843
	 */
	public int minDistance_self(String word1, String word2) {
		// dp[i][j] : minimum steps to convert i long word1 and j long word2
        int[][] dp = new int[word1.length() + 1][word2.length() + 1];
        
        for (int i = 0; i < dp.length; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j < dp[0].length; j++) {
            dp[0][j] = j;
        }
        
		for (int i = 1; i <= word1.length(); i++) {
			for (int j = 1; j <= word2.length(); j++) {
				if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                }
                else {
                	// dp[i-1][j-1] : replace word1(i) with word2(j), 
                	//                because word1(0, i-1) == word2(0, j-1);
                    // dp[i  ][j-1] : delete word(j)
                    // dp[i-1][j  ] : delete word(i), 
                	//                because word1(0, i-1) == word2(0, j)
                    dp[i][j] = Math.min(Math.min(dp[i][j - 1], dp[i - 1][j]), 
                    		dp[i - 1][j - 1]) + 1;
                }
            }
        }
        return dp[word1.length()][word2.length()];
    }
	
	/*
	 * https://leetcode.com/problems/edit-distance/discuss/25959/My-Accepted-Java-Solution
	 * 
	 * dp[i][j] = minimum cost needed to transform word1[0, i) to word2[0, j).
	 * dp[i][0] = i and dp[0][j] = j.
	 * 
	 * 1. word1[i-1] == word2[j-1] : no operation needed. dp[i][j] == dp[i - 1][j - 1]
	 * 2. word1[i-1] != word2[j-1] : either insert, delete or replace
	 * dp[i][j] = 1 + min { dp[i][j - 1], dp[i - 1][j], dp[i - 1][j - 1] }
	 * 
	 *   2-1. replace word1[i-1] with word2[j-1]. dp[i][j] = dp[i - 1][j - 1] + 1
	 *   2-2. delete word1[i-1]. dp[i][j] = dp[i - 1][j] + 1
	 *   2-3. insert word1 => delete word2[j-1]. dp[i][j] = dp[i][j - 1] + 1
	 * 
	 * Insert a new character after word1[i] that matches the jth character of word2. 
	 * So, now have to match i characters of word1 to j - 1 characters of word2.
	 * 
	 * For 2-2 and 2-3, the reason it works is that we know the optimal ways to 
	 * transform word1(0,i) to word2(0,j-1) and word1(0,i-1) to word(0,j) 
	 * ( Delete ("abc" to "ab") or Insert ("ab" to "abc") ). Now all we need to one 
	 * more operation.
	 * 
	 * Insert and delete are symmetric operations, so 2-3 is equal to insert 
	 * character to word1.
	 * 
	 * The insert operation in one direction (i.e. from word1 to word2) is same as 
	 * delete operation in other.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/edit-distance/discuss/25846/C++-O(n)-space-DP/24843
	 * https://leetcode.com/problems/edit-distance/discuss/25849/Java-DP-solution-O(nm)
	 * https://leetcode.com/problems/edit-distance/discuss/273352/java-7m-dp-solution-with-detailed-explanation
	 */
	public int minDistance_2D(String word1, String word2) {
		if (word1.equals(word2)) {
			return 0;
		}
		if (word1.length() == 0 || word2.length() == 0) {
			return Math.abs(word1.length() - word2.length());
		}
		
		int[][] dp = new int[word1.length() + 1][word2.length() + 1];
		
		for (int i = 0; i <= word1.length(); i++) {
			dp[i][0] = i;
		}
		for (int j = 0; j <= word2.length(); j++) {
			dp[0][j] = j;
		}
		
		for (int i = 1; i <= word1.length(); i++) {
			for (int j = 1; j <= word2.length(); j++) {
				if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
					dp[i][j] = dp[i - 1][j - 1];
				} 
				else {
					dp[i][j] = Math.min(dp[i - 1][j - 1], 
							Math.min(dp[i - 1][j], dp[i][j - 1])) + 1;
				}
			}
		}
		return dp[word1.length()][word2.length()];
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/edit-distance/discuss/25895/Step-by-step-explanation-of-how-to-optimize-the-solution-from-simple-recursion-to-DP
	 * 
	 * 1. Character is Matching
	 *    Skip the similar character and hunt for the dissimilar One.
	 * 2. Character is not matching
	 *    1 + min( insert, delete, replace )
	 * 
	 * The first Character of Word1 will surely become same as that of the Word2.
	 * 
	 * Base Case : any of the Word is consumed completely.
	 * Word1 is Consumed : convert the empty String into the Word2, So we need to 
	 *                     perform Insertions Equal to length of the Word2.
	 * Word2 is Consumed : convert this String into empty, So we need to perform 
	 *                     Deletions Equal to length of the Word1.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/edit-distance/discuss/261880/Java-DP-Solution-Beats-99.81-on-Runtime-BottonUp(Space-O(N))
	 * https://leetcode.com/problems/edit-distance/discuss/162330/Very-detailed-explanation-(Recursive-greater-DP-)-in-Java
	 * 
	 * Other code:
	 * https://leetcode.com/problems/edit-distance/discuss/25896/Easy-to-understand-recursiveDP-solution-beats-99.6
	 */
	public int minDistance_topDown(String word1, String word2) {
		if (word1 == null || word2 == null)
			return -1;
		if (word1.length() == 0)
			return word2.length();
		if (word2.length() == 0)
			return word1.length();

		char[] c1 = word1.toCharArray();
		char[] c2 = word2.toCharArray();

		int[][] cache = new int[c1.length][c2.length];
		for (int i = 0; i < c1.length; i++) {
			for (int j = 0; j < c2.length; j++) {
				cache[i][j] = -1;
			}
		}

		return match(c1, c2, 0, 0, cache);
	}

	private int match(char[] c1, char[] c2, int i, int j, int[][] cache) {
		// one of the string's pointer have reached the end of it
		if (c1.length == i)
			return c2.length - j;
		if (c2.length == j)
			return c1.length - i;

		// we have already calculated
		if (cache[i][j] != -1) {
			return cache[i][j];
		}

		// current position is the same.
		if (c1[i] == c2[j]) {
			cache[i][j] = match(c1, c2, i + 1, j + 1, cache);
		} 
		else {
			// Case1: insert
			int insert = match(c1, c2, i, j + 1, cache);
			// Case2: delete
			int delete = match(c1, c2, i + 1, j, cache);
			// Case3: replace
			int replace = match(c1, c2, i + 1, j + 1, cache);

			cache[i][j] = Math.min(Math.min(insert, delete), replace) + 1;
		}

		return cache[i][j];
	}
	
	// https://leetcode.com/problems/edit-distance/discuss/25969/My-clean-java-solution-with-O(n)-space-in-17-lines
	public int minDistance_1D(String word1, String word2) {
		int[] d = new int[word2.length() + 1];
		for (int i = 0; i <= word2.length(); ++i)
			d[i] = i;
		
		for (int i = 1; i <= word1.length(); ++i) {
			int prev = d[0];
			d[0] = i;
			for (int j = 1; j <= word2.length(); ++j) {
				int tmp = d[j];
				d[j] = Math.min(d[j - 1], d[j]) + 1;
				d[j] = Math.min(d[j], 
						prev + (word1.charAt(i - 1) == word2.charAt(j - 1) ? 0 : 1));
				prev = tmp;
			}
		}
		return d[word2.length()];
	}
	
	// https://leetcode.com/problems/edit-distance/discuss/25913/Good-pdf-on-edit-distance-problem.-May-be-helpful.
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/edit-distance/discuss/159295/Python-solutions-and-intuition
     * https://leetcode.com/problems/edit-distance/discuss/25879/Python-solutions-(O(m*n)-O(n)-space).
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/edit-distance/discuss/25846/C%2B%2B-O(n)-space-DP
     * https://leetcode.com/problems/edit-distance/discuss/25987/Dynamic-Programming-Solution-in-C%2B%2B-with-Algorithm-Description
     * https://leetcode.com/problems/edit-distance/discuss/25911/My-O(mn)-time-and-O(n)-space-solution-using-DP-with-explanation
     */

}
