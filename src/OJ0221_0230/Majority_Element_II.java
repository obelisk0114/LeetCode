package OJ0221_0230;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/*
 * http://www.jianshu.com/p/dfd676b71ef0
 * https://segmentfault.com/a/1190000004905350
 */

public class Majority_Element_II {
	/*
	 * https://discuss.leetcode.com/topic/32510/java-easy-version-to-understand
	 * 
	 * Rf : 
	 * https://gregable.com/2013/10/majority-vote-algorithm-find-majority.html
	 * https://discuss.leetcode.com/topic/52182/java-o-n-solution
	 */
	public List<Integer> majorityElement(int[] nums) {
		if (nums == null || nums.length == 0)
			return new ArrayList<Integer>();
		
		List<Integer> result = new ArrayList<Integer>();
		int number1 = nums[0], number2 = nums[0]; 
		int	count1 = 0, count2 = 0;
		int len = nums.length;
		for (int i = 0; i < len; i++) {
			if (nums[i] == number1)
				count1++;
			else if (nums[i] == number2)
				count2++;
			else if (count1 == 0) {
				number1 = nums[i];
				count1 = 1;
			} else if (count2 == 0) {
				number2 = nums[i];
				count2 = 1;
			} else {
				count1--;
				count2--;
			}
		}
		count1 = 0;
		count2 = 0;
		for (int i = 0; i < len; i++) {
			if (nums[i] == number1)
				count1++;
			else if (nums[i] == number2)
				count2++;
		}
		if (count1 > len / 3)
			result.add(number1);
		if (count2 > len / 3)
			result.add(number2);
		return result;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/29390/concise-java-solution-based-on-moore-s-voting-algorithm
	 * 
	 * Rf : https://discuss.leetcode.com/topic/42463/java-solution-for-generalized-n-k-case
	 */
	public List<Integer> majorityElement2(int[] nums) {
		ArrayList<Integer> res = new ArrayList<Integer>();
		if (nums.length == 0)
			return res;

		int count[] = new int[2];
		int x[] = new int[2];

		x[0] = 0;
		x[1] = 1;
		for (int i = 0; i < nums.length; i++) {
			if (x[0] == nums[i])
				count[0]++;
			else if (x[1] == nums[i])
				count[1]++;
			else if (count[0] == 0) {
				x[0] = nums[i];
				count[0] = 1;
			} else if (count[1] == 0) {
				x[1] = nums[i];
				count[1] = 1;
			} else {
				count[0]--;
				count[1]--;
			}
		}

		Arrays.fill(count, 0);
		for (int i : nums) {// Count again for x1, x2
			if (i == x[0])
				count[0]++;
			else if (i == x[1])
				count[1]++;
		}
		for (int j = 0; j < 2; j++) {
			if (count[j] > nums.length / 3 && !res.contains(x[j]))
				res.add(x[j]);
		}
		return res;
	}

}
