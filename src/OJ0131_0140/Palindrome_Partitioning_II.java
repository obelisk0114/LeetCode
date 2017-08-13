package OJ0131_0140;

import java.util.Arrays;

public class Palindrome_Partitioning_II {
	/*
	 * https://discuss.leetcode.com/topic/32575/easiest-java-dp-solution-97-36
	 * 
	   1. cut[i] is the minimum of cut[j - 1] + 1 (j <= i), if [j, i] is palindrome.
       2. If [j, i] is palindrome, [j + 1, i - 1] is palindrome, and c[j] == c[i].
       
       since every single character is a palindrome, 
       maximum no. of cuts for substring [0...i] will be i 
       hence initiating cuts[i] with maximum possible value.
       
       since dp[j][i] is a palindrome, cuts[j]+1 equals no. of cuts required in [0..i]
       if we include the current word [j..i]; 
       New cuts[i] will be equal to min of previous cuts[i] and 
       the newly calculated cuts[i] i.e. cuts[j]+1
       
     * Other code :
     * https://discuss.leetcode.com/topic/18073/java-o-n-2-dp-solution
	 */
	public int minCut(String s) {
		char[] c = s.toCharArray();
		int n = c.length;
		int[] cut = new int[n];
		boolean[][] pal = new boolean[n][n];

		for (int i = 0; i < n; i++) {
			int min = i;
			for (int j = 0; j <= i; j++) {
				if (c[j] == c[i] && (j + 1 > i - 1 || pal[j + 1][i - 1])) {
					pal[j][i] = true;
					min = j == 0 ? 0 : Math.min(min, cut[j - 1] + 1);
				}
			}
			cut[i] = min;
		}
		return cut[n - 1];
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/33685/java-dp-recursive-5ms-solution
	 */
	int[] cut_recursive;
	public int minCut_recursive(String s) {
		int len = s.length();
		char[] tmp = s.toCharArray();
		cut_recursive = new int[s.length()];
		Arrays.fill(cut_recursive, Integer.MAX_VALUE);
		for (int i = 0; i < len; i++) {
			re(tmp, i, i);
			re(tmp, i - 1, i);
		}
		return cut_recursive[len - 1];
	}
	public void re(char[] s, int start, int end) {
		if (start < 0 || end >= s.length)
			return;
		if (s[start] == s[end]) {
			re(s, start - 1, end + 1);
			int tmp = start == 0 ? 0 : cut_recursive[start - 1] + 1;
			cut_recursive[end] = Math.min(tmp, cut_recursive[end]);
		}
	}
	
	/*
	 * https://discuss.leetcode.com/topic/2840/my-solution-does-not-need-a-table-for-palindrome-is-it-right-it-uses-only-o-n-space
	 * 
	 * Initialize the 'cut' array: For a string with n characters s[0, n-1], 
	 * it needs at most n-1 cut. The 'cut' array is initialized as cut[i] = i-1
	 * 
	 * The external loop variable 'i' represents the center of the palindrome. 
	 * The internal loop variable 'j' represents the 'radius' of the palindrome.
	 * 
	 * This palindrome can then be represented as s[i-j, i+j]. 
	 * If this string is indeed a palindrome, then one possible value of cut[i+j] is 
	 * cut[i-j] + 1, where cut[i-j] corresponds to s[0, i-j-1] and 
	 * 1 correspond to the palindrome s[i-j, i+j];
	 * 
	 * Rf :
	 * https://discuss.leetcode.com/topic/2840/my-solution-does-not-need-a-table-for-palindrome-is-it-right-it-uses-only-o-n-space/46
	 * https://discuss.leetcode.com/topic/2840/my-solution-does-not-need-a-table-for-palindrome-is-it-right-it-uses-only-o-n-space/15
	 * https://discuss.leetcode.com/topic/19298/two-c-versions-given-one-dp-28ms-one-manancher-like-algorithm-10-ms/2
	 * https://discuss.leetcode.com/topic/12000/my-accepted-java-solution
	 */
	public int minCut_manancher(String s) {
		int n = s.length();
		int[] cut = new int[n + 1]; // number of cuts for the first k characters
		char[] c = s.toCharArray();
		for (int i = 0; i <= n; i++)
			cut[i] = i - 1;
		for (int i = 0; i < n; i++) {
			// odd length palindrome
			for (int j = 0; i - j >= 0 && i + j < n && c[i - j] == c[i + j]; j++)
				cut[i + j + 1] = Math.min(cut[i + j + 1], 1 + cut[i - j]);

			// even length palindrome
			for (int j = 1; i - j + 1 >= 0 && i + j < n && c[i - j + 1] == c[i + j]; j++) 
				cut[i + j + 1] = Math.min(cut[i + j + 1], 1 + cut[i - j + 1]);
		}
		return cut[n];
	}

}
