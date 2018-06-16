package OJ0551_0560;

import java.util.Map;
import java.util.HashMap;

public class Optimal_Division {
	/*
	 * https://leetcode.com/problems/optimal-division/discuss/125607/Java-Solution-with-explanation
	 * 
	 * X1/X2/X3/../Xn will always be equal to (X1/X2) * Y, no matter how you place 
	 * parentheses. X1 always goes to the numerator and X2 always goes to the 
	 * denominator. Hence you just need to maximize Y. And Y is maximized when it is 
	 * equal to X3 *..* Xn. So the answer is always X1/(X2/X3/../Xn) = (X1 *X3 *..*Xn)/X2
	 * 
	 * Rf : https://leetcode.com/problems/optimal-division/discuss/101687/Easy-to-understand-simple-O(n)-solution-with-explanation
	 * 
	 * Other code:
	 * https://leetcode.com/problems/optimal-division/discuss/101683/O(n)-very-easy-Java-solution.
	 */
	public String optimalDivision(int[] nums) {
		if (nums == null || nums.length == 0)
			return "";
		if (nums.length == 1)
			return nums[0] + "";
		if (nums.length == 2)
			return nums[0] + "/" + nums[1];
		
		StringBuilder s = new StringBuilder();
		s.append(nums[0] + "/(" + nums[1]);
		for (int i = 2; i < nums.length; i++) {
			s.append("/");
			s.append(nums[i]);
		}
		s.append(")");
		return s.toString();
	}
	
	/*
	 * The following 2 functions and class are from this link.
	 * https://leetcode.com/problems/optimal-division/discuss/101684/Brute-force-with-memory-in-case-of-your-interviewer-forbid-tricky-solution
	 * 
	 * For each recursion, I find the maximum result and also the minimum result. For 
	 * example, if you want to know the maximum result of A/B, where A and B are also 
	 * some expressions, then you only need to know the maximum result of A and the 
	 * minimum result of B. However, if you want to know the maximum result of C/(A/B), 
	 * then you also need to know the minimum result of A/B. That's why both maximum 
	 * and minimum should be stored.
	 * 
	 * Rf : https://leetcode.com/problems/optimal-division/discuss/101697/Java-Solution-Backtracking
	 */
	public String optimalDivision_recursive(int[] nums) {
		Map<String, pair> memory = new HashMap<>();
		pair sol = divid(nums, 0, nums.length - 1, memory);
		return sol.maxS;
	}

	public pair divid(int[] nums, int start, int end, Map<String, pair> memory) {
		String key = start + " " + end;
		if (memory.containsKey(key))
			return memory.get(key);
		if (start == end)
			return new pair(nums[start], String.valueOf(nums[start]), nums[start], String.valueOf(nums[start]));

		pair sol = new pair(Integer.MAX_VALUE, "", Integer.MIN_VALUE, "");

		for (int i = start; i < end; i++) {
			pair left = divid(nums, start, i, memory);
			pair right = divid(nums, i + 1, end, memory);

			double min = left.min / right.max;
			String minS = left.minS + "/" + (i + 1 == end ? right.maxS : "(" + right.maxS + ")");
			double max = left.max / right.min;
			String maxS = left.maxS + "/" + (i + 1 == end ? right.minS : "(" + right.minS + ")");
			
			if (min < sol.min) {
				sol.min = min;
				sol.minS = minS;
			}
			if (max > sol.max) {
				sol.max = max;
				sol.maxS = maxS;
			}
		}
		memory.put(key, sol);
		return sol;
	}

	private class pair {
		double min;
		String minS;
		double max;
		String maxS;

		public pair(double min, String minS, double max, String maxS) {
			this.min = min;
			this.minS = minS;
			this.max = max;
			this.maxS = maxS;
		}
	}
	
	/*
	 * by myself
	 * 
	 * Other code:
	 * https://leetcode.com/problems/optimal-division/discuss/101690/Is-this-question-a-joke-7ms-easy-java-solution
	 */
	public String optimalDivision_self(int[] nums) {
        int length = nums.length;
        if (length == 1)
            return Integer.toString(nums[0]);
        if (length == 2)
            return (nums[0] + "/" + nums[1]);
        
        StringBuilder sb = new StringBuilder();
        sb.append(nums[0]);
        sb.append("/(");
        
        for (int i = 1; i < length - 1; i++) {
            sb.append(nums[i]);
            sb.append("/");
        }
        
        sb.append(nums[length - 1]);
        sb.append(")");
        
        return sb.toString();
    }

}
