package OJ0131_0140;

import java.util.Map;
import java.util.HashMap;

/*
 * https://discuss.leetcode.com/topic/11877/detailed-explanation-and-generalization-of-the-bitwise-operation-method-for-single-numbers
 */

public class Single_Number_II {
	/*
	 * https://leetcode.com/problems/single-number-ii/discuss/43296/an-general-way-to-handle-all-this-sort-of-questions
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/34725/my-own-explanation-of-bit-manipulation-method-might-be-easier-to-understand
	 * https://leetcode.com/problems/single-number-ii/discuss/43296/An-General-Way-to-Handle-All-this-sort-of-questions./42177 
	 */
	public int singleNumber(int[] nums) {
		/*
		 we need to implement a tree-time counter(base 3) that if a bit
		 appears three time, it will be zero.
		 #curent   income   ouput
		 #  ab      c/c     ab/ab
		 #  00      1/0     01/00
		 #  01      1/0     10/01
		 #  10      1/0     00/10
		 a = ~abc + a~b~c;      = (~ new_b) (a ^ c)   represent 2
		 b = ~a~bc + ~ab~c;     = ~a (b ^ c)          represent 1
		 */
		int a = 0;
		int b = 0;
		for (int c : nums) {
			// System.out.println("before a = " + a);
			// System.out.println("before b = " + b);
			int ta = (~a & b & c) | (a & ~b & ~c);
			b = (~a & ~b & c) | (~a & b & ~c);
			a = ta;
			// System.out.println("after a = " + a);
			// System.out.println("after b = " + b);
		}
		return b;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/35640/java-easy-version-to-understand
	 * 
	 * If you sum the ith bit of all numbers and mod 3, it must be either 0 or 1 
	 * due to the constraint of this problem where each number must appear 
	 * either three times or once.
	 * 
	 * Rf :
	 * https://discuss.leetcode.com/topic/455/constant-space-solution/4
	 * https://discuss.leetcode.com/topic/43166/java-o-n-easy-to-understand-solution-easily-extended-to-any-times-of-occurance
	 * https://discuss.leetcode.com/topic/9274/short-java-code-with-bit-summation-and-modulo-3
	 * https://discuss.leetcode.com/topic/33113/a-simple-solution-for-all-similar-problems
	 */
	public static int singleNumber_mod(int[] nums) {
		int len = nums.length, res = 0;
		for (int i = 0; i < 32; i++) {
			int sum = 0;
			for (int j = 0; j < len; j++) {
				sum += (nums[j] >> i) & 1;     // sum every bit of all numbers
			}
			res |= (sum % 3) << i; // res+=((sum[i]&1)<<i); recover the single number
		}
		return res;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/2926/accepted-code-with-proper-explaination-does-anyone-have-a-better-idea
	 * 
	 * Rf : https://discuss.leetcode.com/topic/2031/challenge-me-thx/17
	 */
	public int singleNumber_bit(int[] nums) {
		int ones = 0, twos = 0, threes = 0;

		for (int i = 0; i < nums.length; i++) {
			// twos holds the num that appears twice
			twos = twos | (ones & nums[i]);

			// ones holds the num that appears once
			ones = ones ^ nums[i];

			// threes holds the num that appears three times
			threes = ones & twos;

			// if num[i] appears three times
			// doing this will clear ones and twos
			ones = ones & ~threes;
			twos = twos & ~threes;
		}

		return ones;
	}
	
	// space complexity : O(n)
	int singleNumber_map(int[] nums) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(nums[i])) {
            	map.put(nums[i], map.get(nums[i]) + 1);
            }
            else {
            	map.put(nums[i], 1);
            }
        }
        for (Integer key : map.keySet()) {
        	if (map.get(key) == 1) {
        		return key;
        	}
        }
        return -1;
    }

}
