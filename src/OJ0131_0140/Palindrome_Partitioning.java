package OJ0131_0140;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

public class Palindrome_Partitioning {
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/37756/java-dp-dfs-solution
	 */
	public List<List<String>> partition(String s) {
		List<List<String>> res = new ArrayList<>();
		boolean[][] dp = new boolean[s.length()][s.length()];
		for (int i = 0; i < s.length(); i++) {
			for (int j = 0; j <= i; j++) {
				if (s.charAt(i) == s.charAt(j) && (i - j <= 2 || dp[j + 1][i - 1])) {
					dp[j][i] = true;
				}
			}
		}
		helper(res, new ArrayList<>(), dp, s, 0);
		return res;
	}
	private void helper(List<List<String>> res, List<String> path, boolean[][] dp, String s, int pos) {
		if (pos == s.length()) {
			res.add(new ArrayList<>(path));
			return;
		}

		for (int i = pos; i < s.length(); i++) {
			if (dp[pos][i]) {
				path.add(s.substring(pos, i + 1));
				helper(res, path, dp, s, i + 1);
				path.remove(path.size() - 1);
			}
		}
	}
	
	/*
	 * The following 3 functions are from this link.
	 * https://discuss.leetcode.com/topic/46162/a-general-approach-to-backtracking-questions-in-java-subsets-permutations-combination-sum-palindrome-partioning
	 * 
	 * Rf : https://discuss.leetcode.com/topic/6186/java-backtracking-solution
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/1524/shouldn-t-we-use-dp-in-addition-to-dfs
	 */
	public List<List<String>> partition_backtrack(String s) {
		List<List<String>> list = new ArrayList<>();
		backtrack(list, new ArrayList<>(), s, 0);
		return list;
	}
	public void backtrack(List<List<String>> list, List<String> tempList, String s, int start) {
		if (start == s.length())
			list.add(new ArrayList<>(tempList));
		else {
			for (int i = start; i < s.length(); i++) {
				if (isPalindrome(s, start, i)) {
					tempList.add(s.substring(start, i + 1));
					backtrack(list, tempList, s, i + 1);
					tempList.remove(tempList.size() - 1);
				}
			}
		}
	}
	public boolean isPalindrome(String s, int low, int high) {
		while (low < high)
			if (s.charAt(low++) != s.charAt(high--))
				return false;
		return true;
	}
	
	/*
	 * The following 3 functions are from this link.
	 * https://discuss.leetcode.com/topic/16800/concise-java-solution
	 */
	public List<List<String>> partition_dfs(String s) {
		List<List<String>> res = new ArrayList<List<String>>();
		if (s.length() == 0)
			return res;
		recur(res, new ArrayList<String>(), s);
		return res;
	}
	public void recur(List<List<String>> res, List<String> temp, String s) {
		if (s.length() == 0) {
			res.add(new ArrayList<String>(temp));
			return;
		}
		for (int i = 0; i < s.length(); i++) {
			if (isPalin(s.substring(0, i + 1))) {
				temp.add(s.substring(0, i + 1));
				recur(res, temp, s.substring(i + 1));
				temp.remove(temp.size() - 1);
			}
		}
	}
	public boolean isPalin(String s) {
		for (int i = 0; i < s.length() / 2; i++) {
			if (s.charAt(i) != s.charAt(s.length() - 1 - i))
				return false;
		}
		return true;
	}
	
	/*
	 * The following 4 functions are from this link.
	 * https://discuss.leetcode.com/topic/2918/classic-recursive-solution-in-java
	 * 
	 * The init method computes the isPal[][] array, 
	 * where isPal[i][j] is true if s[i..j] is palindrome. 
	 * 
	 * The helper method is doing the actual recursion, 
	 * where the cut[] array records the cut positions, 
	 * and the construct method reconstructs the result from the cut[] array.
	 */
	private void init(boolean[][] isPal, String s) {
		int len = isPal.length;
		for (int i = 0; i < len; i++)
			isPal[i][i] = true;
		for (int k = 1; k < len; k++)
			for (int i = 0; i + k < len; i++) {
				if (s.charAt(i) != s.charAt(i + k))
					continue;
				isPal[i][i + k] = (i + 1 <= i + k - 1) ? isPal[i + 1][i + k - 1] : true;
			}

	}
	private void construct(String s, boolean[] cut, List<List<String>> ans) {
		List<String> tmp = new ArrayList<String>();
		int cur = 0;
		for (int i = 0; i < cut.length; i++) {
			if (cut[i]) {
				tmp.add(s.substring(cur, i + 1));
				cur = i + 1;
			}
		}
		ans.add(tmp);
	}
	private void helper_cut(String s, int start, int end, boolean[] cut, List<List<String>> ans, boolean[][] isPal) {
		if (start > end)
			construct(s, cut, ans);
		for (int i = start; i <= end; i++) {
			if (isPal[start][i]) {
				cut[i] = true;
				helper_cut(s, i + 1, end, cut, ans, isPal);
				cut[i] = false;
			}
		}
	}
	public List<List<String>> partition_cut(String s) {
		int len = s.length();
		boolean[][] isPal = new boolean[len][len];
		boolean[] cut = new boolean[len];

		init(isPal, s);
		List<List<String>> ans = new ArrayList<List<String>>();
		helper_cut(s, 0, len - 1, cut, ans, isPal);
		return ans;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/24222/simple-backtracking-java-solution-with-95-performance
	 */
	public List<List<String>> partition_List_of_List(String s) {
		List<List<String>> res = new ArrayList<List<String>>();
		if (s.equals("")) {
			res.add(new ArrayList<String>());
			return res;
		}
		for (int i = 0; i < s.length(); i++) {
			if (isPalindrome_List_of_List(s, i + 1)) {   // first half is palindrome
				for (List<String> list : partition_List_of_List(s.substring(i + 1))) {  // combine second half
					list.add(0, s.substring(0, i + 1));   // Add first half to the start
					res.add(list);
				}
			}
		}
		return res;
	}
	public boolean isPalindrome_List_of_List(String s, int n) {
		for (int i = 0; i < n / 2; i++) {
			if (s.charAt(i) != s.charAt(n - i - 1))
				return false;
		}
		return true;
	}
	
	/*
	 * The following 3 variables and 3 functions are from this link.
	 * https://discuss.leetcode.com/topic/2907/my-graph-like-solution
	 */
	Map<String, Boolean> palindromes_map = new HashMap<String, Boolean>();
	Map<Integer, List<Integer>> adj_map = new HashMap<Integer, List<Integer>>();
	List<List<String>> results_map;
	public List<List<String>> partition_map(String s) {
		results_map = new LinkedList<List<String>>();
		if (s == null || s.length() == 0)
			return results_map;

		for (int i = 0; i < s.length(); i++)
			adj_map.put(i, new LinkedList<Integer>());

		for (int i = 1; i <= s.length(); i++) {
			for (int j = i - 1; j >= 0; j--) {
				if (isPalindromes_map(s.substring(j, i))) {
					adj_map.get(j).add(i);// Constructing the Adjacent Graph
				}
			}
		}

		resultBuilder_map(0, s.length(), s, new LinkedList<String>());
		return results_map;
	}
	private void resultBuilder_map(int start, int end, String s, List<String> result) {// DFS builder
		if (start == end) {// Finished
			results_map.add(new ArrayList<String>(result));
			return;
		}
		for (Integer i : adj_map.get(start)) {
			result.add(s.substring(start, i));
			resultBuilder_map(i, end, s, result);
			result.remove(result.size() - 1);
		}
	}
	public boolean isPalindromes_map(String s) {
		if (palindromes_map.containsKey(s))
			return palindromes_map.get(s);

		for (int i = 0, j = s.length() - 1; j > i; i++, j--) {
			if (s.charAt(i) != s.charAt(j)) {
				palindromes_map.put(s, false);
				return false;
			}
		}
		palindromes_map.put(s, true);
		return true;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/2884/my-java-dp-only-solution-without-recursion-o-n-2
	 * 
	 * if pair[i][j] is true, that means sub string from i to j is palindrome.
	 * The result[i], is to store from beginning until current index i (Non inclusive), all possible partitions.
	 * 
	 * result[0..right] = result[0..left-1] + s[left..right] if s[left..right] is a palindrome
	 * p[left,right] = true if right-left<=1 or s[left] == s[right] && p[left+1,right-1]
	 * 
	 * Rf : https://discuss.leetcode.com/topic/2884/my-java-dp-only-solution-without-recursion-o-n-2/17
	 */
	public List<List<String>> partition_iterative(String s) {
		int len = s.length();
		List<List<List<String>>> result = new ArrayList<>(len + 1);
		
		for (int i = 0; i < len + 1; i++) {			
			result.add(new ArrayList<List<String>>());
		}
		result.get(0).add(new ArrayList<String>());
		
		boolean[][] pair = new boolean[len][len];
		for (int i = 0; i < s.length(); i++) {
			for (int left = 0; left <= i; left++) {
				if (s.charAt(left) == s.charAt(i) && (i-left <= 1 || pair[left + 1][i - 1])) {
					pair[left][i] = true;
					String str = s.substring(left, i + 1);
					for (List<String> r : result.get(left)) {
						List<String> ri = new ArrayList<String>(r);
						ri.add(str);
						result.get(i + 1).add(ri);
					}
				}
			}
		}
		return result.get(len);
	}
	
}
