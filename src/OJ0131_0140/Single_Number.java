package OJ0131_0140;

import java.util.Map;
import java.util.HashMap;

public class Single_Number {
	/*
	 * https://en.wikipedia.org/wiki/Exclusive_or
	 * a ^ b = b ^ a
	 * a ^ (b ^ c) = (a ^ b) ^ c
	 * a ^ 0 = a
	 * a ^ a = 0
	 * a ^ b ^ b = a ^ 0 = a
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/47560/xor-java-solution
	 * https://discuss.leetcode.com/topic/41157/my-java-solution-in-o-n-time-complexity-and-o-1-space-complexity-using-xor/2
	 */
	int singleNumber(int[] nums) {
        int out = nums[0];
        for (int i = 1; i < nums.length; i++) {
            out ^= nums[i];
        }
        return out;
    }
	
	// space complexity : O(n)
	int singleNumber_map(int[] nums) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(nums[i])) {
            	map.put(nums[i], 2);
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
