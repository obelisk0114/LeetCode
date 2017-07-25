package OJ0161_0170;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

/*
 * https://discuss.leetcode.com/topic/28601/java-solutions-sorting-hashmap-moore-voting-bit-manipulation
 */

public class Majority_Element {
	/*
	 * https://discuss.leetcode.com/topic/8692/o-n-time-o-1-space-fastest-solution
	 * 
	 * Even when the input sequence has no majority, 
	 * the algorithm will report one of the sequence elements as its result. 
	 * 
	 * However, it is possible to perform a second pass over the same input sequence 
	 * in order to count the number of times the reported element occurs 
	 * and determine whether it is actually a majority.
	 * 
	 * Rf : 
	 * http://www.jianshu.com/p/dfd676b71ef0
	 * https://en.wikipedia.org/wiki/Boyer%E2%80%93Moore_majority_vote_algorithm
	 * http://www.cnblogs.com/zhonghuasong/p/6536665.html
	 */
	public int majorityElement(int[] num) {
		int major = num[0], count = 1;
		for (int i = 1; i < num.length; i++) {
			if (count == 0) {
				count++;
				major = num[i];
			} else if (major == num[i]) {
				count++;
			} else
				count--;
		}
		return major;
	}
	
	// https://discuss.leetcode.com/topic/6286/share-my-solution-java-count-bits
	public int majorityElement_bit_manipulatin(int[] num) {
		int ret = 0;
		for (int i = 0; i < 32; i++) {
			int ones = 0, zeros = 0;

			for (int j = 0; j < num.length; j++) {
				if ((num[j] & (1 << i)) != 0) {
					++ones;
				} else
					++zeros;
			}

			if (ones > zeros)
				ret |= (1 << i);
		}
		return ret;
	}
	
	// space : O(n)
	public int majorityElement_HashMap(int[] nums) {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		int max = 0;
		int out = 0;
		for (int element : nums) {
			int value = map.getOrDefault(element, 0) + 1;
			map.put(element, value);
			
			if (value > max) {
				max = value;
				out = element;
			}
		}
		return out;
	}
	
	// https://discuss.leetcode.com/topic/6807/my-answer-to-majority-element-java
	public int majorityElement_sort(int[] num) {
	    Arrays.sort(num);
	    return num[num.length / 2];
	}

}
