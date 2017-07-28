package OJ0021_0030;

import java.util.List;
import java.util.ArrayList;

public class Generate_Parentheses {
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/30026/2ms-ac-java-solution-using-recursive-call
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/23229/java-dfs-way-solution
	 * https://discuss.leetcode.com/topic/30640/1-ms-beats-92-of-submissions-easy-java-space-optimized-solution
	 */
	public List<String> generateParenthesis(int n) {
		ArrayList<String> m = new ArrayList<>();
		generate(m, "", n, n);
		return m;
	}
	private void generate(ArrayList<String> m, String s, int l, int r) {
		if (r == 0) {                       // if (l == 0 && r == 0)
			m.add(s);
			//return;
		}
		if (l > 0)
			generate(m, s + "(", l - 1, r);
		if (r > l)
			generate(m, s + ")", l, r - 1);
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/8724/easy-to-understand-java-backtracking-solution
	 * 
	 * Once we add a '(' we will then discard it and try a ')' 
	 * which can only close a valid '('.
	 */
	public List<String> generateParenthesis2(int n) {
		List<String> list = new ArrayList<String>();
		backtrack(list, "", 0, 0, n);
		return list;
	}
	public void backtrack(List<String> list, String str, int open, int close, int max) {
		if (str.length() == max * 2) {
			list.add(str);
			return;
		}

		if (open < max)
			backtrack(list, str + "(", open + 1, close, max);
		if (close < open)
			backtrack(list, str + ")", open, close + 1, max);
	}
	
	/*
	 * https://discuss.leetcode.com/topic/5866/my-accepted-java-solution
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/33127/java-easy-to-understand-recursive-dp-method-with-explanations
	 * https://discuss.leetcode.com/topic/3474/an-iterative-method
	 */
	public List<String> generateParenthesis_iterative(int n) {
		List<String> result = new ArrayList<String>();
		if (n == 0) {
			result.add("");
		} 
		else {
			for (int i = n - 1; i >= 0; i--) {
				List<String> insertSub = generateParenthesis(i);
				List<String> tailSub = generateParenthesis(n - 1 - i);
				for (String insert : insertSub) {
					for (String tail : tailSub) {
						result.add("(" + insert + ")" + tail);
					}
				}

			}
		}
		return result;
	}

}
