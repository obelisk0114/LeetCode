package OJ0521_0530;

public class Longest_Uncommon_Subsequence_I {
	/*
	 * https://discuss.leetcode.com/topic/85020/java-1-liner
	 * 
	 * 1. a = b. If both the strings are identical, it is obvious that no subsequence 
	 * will be uncommon. Hence, return -1.
	 * 
	 * 2. length(a) = length(b) and a ¡Ú b. Example: abc and abd. In this case we can 
	 * consider any string i.e. abc or abd as a required subsequence, as out of these 
	 * two strings one string will never be a subsequence of other string. Hence, 
	 * return length(a) or length(b).
	 * 
	 * 3. length(a) ¡Ú length(b). Example: abcd and abc. In this case we can consider 
	 * bigger string as a required subsequence because bigger string can't be a 
	 * subsequence of smaller string. Hence, return max(length(a), length(b)).
	 * 
	 * Rf : 
	 * https://leetcode.com/articles/longest-uncommon-subsequence-i/
	 * https://discuss.leetcode.com/topic/86253/read-and-think-before-coding-clean-and-clear-java-solution
	 */
	public int findLUSlength(String a, String b) {
	    return a.equals(b) ? -1 : Math.max(a.length(), b.length());
	}

}
