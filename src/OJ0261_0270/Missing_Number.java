package OJ0261_0270;

import java.util.Arrays;

public class Missing_Number {
	// https://discuss.leetcode.com/topic/37594/java-solution-time-o-n-space-o-1-no-xor-no-gauss-math-method
	public int missingNumber(int[] nums) {
		int sum = 0;
		for (int i = 0; i < nums.length; i++)
			sum += i - nums[i];
		return nums.length + sum;
	}
	
	// https://discuss.leetcode.com/topic/22601/swapping-numbers-to-the-same-index-cell
	public int MissingNumber_subtract(int[] nums) {
		int result = nums.length * (nums.length + 1) / 2;
		for (int i = 0; i < nums.length; i++)
			result -= nums[i];
		return result;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/23427/3-different-ideas-xor-sum-binary-search-java-code
	 * 
	 * a ^ b = b ^ a
	 * a ^ (b ^ c) = (a ^ b) ^ c
	 * a ^ 0 = a
	 * a ^ a = 0
	 * a ^ b ^ b = a ^ 0 = a
	 * In a complete array with no missing numbers, 
	 * the index and value should be perfectly corresponding (nums[index] = index) 
	 * 
	 * Rf :
	 * https://discuss.leetcode.com/topic/24535/4-line-simple-java-bit-manipulate-solution-with-explaination
	 * https://discuss.leetcode.com/topic/22696/java-bit-manipulation-solution-o-n-time-o-1-space
	 * https://en.wikipedia.org/wiki/Exclusive_or
	 */
	public int missingNumber_xor(int[] nums) {
		int res = nums.length;
		for (int i = 0; i < nums.length; i++) {
			res = res ^ i ^ nums[i];
		}
		return res;
	}
	
	// Self
	public int missingNumber_self(int[] nums) {
		int sum = 0;
		for (int i : nums)
			sum += i;
		int total = (1 + nums.length) * nums.length / 2;
		return total - sum;
	}
	
	// https://discuss.leetcode.com/topic/23427/3-different-ideas-xor-sum-binary-search-java-code
	public int missingNumber_binary_search(int[] nums) { 
		Arrays.sort(nums);
		int left = 0, right = nums.length, mid = (left + right) / 2;
		while (left < right) {
			mid = (left + right) / 2;
			if (nums[mid] > mid)
				right = mid;
			else
				left = mid + 1;
		}
		return left;
	}
	
	// https://discuss.leetcode.com/topic/22305/1-lines-ruby-python-java-c

}
