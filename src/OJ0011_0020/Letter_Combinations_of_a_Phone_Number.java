package OJ0011_0020;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

public class Letter_Combinations_of_a_Phone_Number {
	/*
	 * The following variable and 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/6380/my-recursive-solution-using-java
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/6380/my-recursive-solution-using-java/7
	 */
	private static final String[] KEYS = { "", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz" };
	public List<String> letterCombinations(String digits) {
		List<String> ret = new LinkedList<String>();
		if (digits == null || digits.length() == 0)
			return ret;
		combination("", digits, 0, ret);
		return ret;
	}
	private void combination(String prefix, String digits, int offset, List<String> ret) {
		if (offset >= digits.length()) {
			ret.add(prefix);
			return;
		}
		String letters = KEYS[(digits.charAt(offset) - '0')];
		for (int i = 0; i < letters.length(); i++) {
			combination(prefix + letters.charAt(i), digits, offset + 1, ret);
		}
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/21582/my-simple-java-solution
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/37976/fast-backtracking-easy-to-understand-with-explanations
	 * https://discuss.leetcode.com/topic/31397/java-easy-version-to-understand
	 */
	String[][] refer = { {}, {}, { "a", "c", "b" }, { "d", "e", "f" }, { "g", "h", "i" }, { "j", "k", "l" },
			{ "m", "n", "o" }, { "p", "q", "r", "s" }, { "t", "u", "v" }, { "w", "x", "y", "z" } };
	public List<String> letterCombinations_2Darray(String digits) {
		List<String> list = new ArrayList<String>();
		if (!digits.equals("")) {
			helper(list, digits, "");
			return list;
		}
		return list;
	}
	private void helper(List<String> list, String digits, String s) {
		if (digits.length() == 0) {
			list.add(s);
			return;
		}
		int idx = Integer.parseInt(digits.substring(0, 1));
		for (String k : refer[idx]) {
			helper(list, digits.substring(1, digits.length()), s + k);
		}
		return;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/10777/easy-understand-java-solution
	 * 
	 * Rf : https://discuss.leetcode.com/topic/25657/simple-java-solution-using-recursion
	 */
	public static List<String> letterCombinations_2forLoops(String digits) {
		String digitletter[] = { "", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz" };
		List<String> result = new ArrayList<String>();
		if (digits.length() == 0)
			return result;

		result.add("");
		for (int i = 0; i < digits.length(); i++)
			result = combine(digitletter[digits.charAt(i) - '0'], result);

		return result;
	}
	public static List<String> combine(String digit, List<String> l) {
		List<String> result = new ArrayList<String>();

		for (int i = 0; i < digit.length(); i++)
			for (String x : l)
				result.add(x + digit.charAt(i));

		return result;
	}
	
	/*
	 * The following 2 functions are by myself.
	 */
	public List<String> letterCombinations_self(String digits) {
		String[] ss = { "", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz" };
		List<String> out = new ArrayList<>();
		if (digits.length() == 0)
			return out;
		addTo_self(0, out, digits, ss, "");
		return out;
	}
	private void addTo_self(int pos, List<String> out, String digits, String[] ss, String combine) {
		if (pos == digits.length()) {
			out.add(combine);
			return;
		}
		int index = digits.charAt(pos) - '0';
		for (int i = 0; i < ss[index].length(); i++) {
			String sss = combine + ss[index].charAt(i);
			// System.out.println("sss = " + sss);
			addTo_self(pos + 1, out, digits, ss, sss);
		}
	}
	
	/*
	 * https://discuss.leetcode.com/topic/36593/concise-15-line-java-iterative-solution-very-straight-forward-with-brief-explanation/3
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/8465/my-java-solution-with-fifo-queue
	 * https://discuss.leetcode.com/topic/3396/my-iterative-sollution-very-simple-under-15-lines
	 */
	public List<String> letterCombinations_iterative(String digits) {
		String[] data = new String[] { " ", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz" };
		List<String> res = new ArrayList<String>();
		for (int i = 0; i < digits.length(); i++) {
			char[] letters = data[digits.charAt(i) - '0'].toCharArray();

			List<String> sub = new ArrayList<String>();
			if (res.isEmpty()) {
				res.add("");
			}
			for (String s : res) {
				for (int j = 0; j < letters.length; j++) {
					sub.add(s + letters[j]);
				}
			}
			res = sub;
		}
		return res;
	}

}
