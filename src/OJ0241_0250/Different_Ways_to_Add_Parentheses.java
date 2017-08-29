package OJ0241_0250;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Different_Ways_to_Add_Parentheses {
	/*
	 * https://discuss.leetcode.com/topic/25490/share-a-clean-and-short-java-solution
	 * 
	 * Other code : 
	 * https://discuss.leetcode.com/topic/31622/my-recursive-java-solution
	 * https://discuss.leetcode.com/topic/19901/a-recursive-java-solution-284-ms
	 */
	public List<Integer> diffWaysToCompute(String input) {
		List<Integer> res = new ArrayList<Integer>();
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c == '-' || c == '+' || c == '*') {
				String a = input.substring(0, i);
				String b = input.substring(i + 1);
				List<Integer> al = diffWaysToCompute(a);
				List<Integer> bl = diffWaysToCompute(b);
				for (int x : al) {
					for (int y : bl) {
						if (c == '-') {
							res.add(x - y);
						} else if (c == '+') {
							res.add(x + y);
						} else if (c == '*') {
							res.add(x * y);
						}
					}
				}
			}
		}
		if (res.size() == 0)     // It is integer.
			res.add(Integer.valueOf(input));
		return res;
	}
	
	/*
	 * The following variable and function are from this link.
	 * https://discuss.leetcode.com/topic/42550/java-simple-solution-beats-95
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/19901/a-recursive-java-solution-284-ms/15
	 * https://discuss.leetcode.com/topic/19908/java-recursive-solution-with-memorization
	 * https://discuss.leetcode.com/topic/21362/divide-and-conquer-cache-___java__260ms
	 */
	Map<String, List<Integer>> map = new HashMap<>();
	public List<Integer> diffWaysToCompute_map(String input) {
		if (map.containsKey(input))
			return map.get(input);
		List<Integer> res = new ArrayList<>();
		for (int i = 0; i < input.length(); ++i) {
			char c = input.charAt(i);
			if (c == '+' || c == '-' || c == '*') {
				List<Integer> list1 = diffWaysToCompute_map(input.substring(0, i));
				List<Integer> list2 = diffWaysToCompute_map(input.substring(i + 1));
				for (int v1 : list1) {
					for (int v2 : list2) {
						switch (c) {
						case '+':
							res.add(v1 + v2);
							break;
						case '-':
							res.add(v1 - v2);
							break;
						case '*':
							res.add(v1 * v2);
							break;
						}
					}
				}
			}
		}
		if (res.isEmpty())
			res.add(Integer.parseInt(input));
		map.put(input, res);
		return res;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/26166/easy-to-understand-java-solution-using-divide-and-conquer
	 */
	public List<Integer> diffWaysToCompute_try_catch(String input) {
		if (input == null) {
			return new ArrayList<Integer>();
		}
		return helper_try_catch(input, 0, input.length() - 1);
	}
	private List<Integer> helper_try_catch(String input, int start, int end) {
		List<Integer> result = new ArrayList<Integer>();

		// if the currrent substring is a number, return
		try {
			result.add(Integer.parseInt(input.substring(start, end + 1)));
			return result;
		}
		// if not, do the parsing by the following
		catch (NumberFormatException e) {

		}

		for (int operatorIndex = start; operatorIndex <= end; operatorIndex++) {
			char currChar = input.charAt(operatorIndex);
			if (currChar == '+' || currChar == '-' || currChar == '*') {
				// recursively compute all possible results from other sides of current operator
				List<Integer> left = helper_try_catch(input, start, operatorIndex - 1);
				List<Integer> right = helper_try_catch(input, operatorIndex + 1, end);

				// combine all possible results
				for (int leftValue : left) {
					for (int rightValue : right) {
						int newValue;
						char operator = input.charAt(operatorIndex);
						if (operator == '+') {
							newValue = leftValue + rightValue;
						} else if (operator == '-') {
							newValue = leftValue - rightValue;
						} else {
							newValue = leftValue * rightValue;
						}
						result.add(newValue);
					}
				}
			}
		}
		return result;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/26076/java-recursive-9ms-and-dp-4ms-solution
	 */
	public List<Integer> diffWaysToCompute_recur(String input) {
		List<Integer> result = new ArrayList<>();
		if (input == null || input.length() == 0)
			return result;
		List<String> ops = new ArrayList<>();
		for (int i = 0; i < input.length(); i++) {
			int j = i;
			while (j < input.length() && Character.isDigit(input.charAt(j)))
				j++;
			String num = input.substring(i, j);
			ops.add(num);
			if (j != input.length())
				ops.add(input.substring(j, j + 1));
			i = j;
		}
		result = compute(ops, 0, ops.size() - 1);
		return result;
	}
	private List<Integer> compute(List<String> ops, int lo, int hi) {
		List<Integer> result = new ArrayList<>();
		if (lo == hi) {
			Integer num = Integer.valueOf(ops.get(lo));
			result.add(num);
			return result;
		}
		for (int i = lo + 1; i <= hi - 1; i = i + 2) {
			String operator = ops.get(i);
			List<Integer> left = compute(ops, lo, i - 1), right = compute(ops, i + 1, hi);
			for (int leftNum : left)
				for (int rightNum : right) {
					if (operator.equals("+"))
						result.add(leftNum + rightNum);
					else if (operator.equals("-"))
						result.add(leftNum - rightNum);
					else
						result.add(leftNum * rightNum);
				}
		}
		return result;
	}
	
	// https://discuss.leetcode.com/topic/26076/java-recursive-9ms-and-dp-4ms-solution

}
