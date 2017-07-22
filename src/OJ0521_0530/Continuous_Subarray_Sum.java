package OJ0521_0530;

import java.util.Map;
import java.util.HashMap;

/*
 * LeetCode 560. Subarray Sum Equals K
 */

public class Continuous_Subarray_Sum {
	/*
	 * https://discuss.leetcode.com/topic/80793/java-o-n-time-o-k-space
	 * 
	 * map.put(0, -1) means there is non element in a array so far.
	 * 
	 * Rf : https://discuss.leetcode.com/topic/80817/need-to-pay-attention-to-a-lot-of-corner-cases
	 */
	public boolean checkSubarraySum(int[] nums, int k) {
	    //Map<Integer, Integer> map = new HashMap<Integer, Integer>(){{put(0,-1);}};;
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		map.put(0, -1);
		int runningSum = 0;
		for (int i = 0; i < nums.length; i++) {
			runningSum += nums[i];
			if (k != 0)
				runningSum %= k;
			Integer prev = map.get(runningSum);
			if (prev != null) {
				if (i - prev > 1)
					return true;
			} else
				map.put(runningSum, i);
		}
		return false;
	}
	
	// self
	public boolean checkSubarraySum_self(int[] nums, int k) {
        if (nums.length <= 1) {
            return false;
        }
        if (k == 1)
            return true;
        for (int i = 0; i < nums.length; i++) {
            int sum = 0;
            for (int j = i; j < nums.length; j++) {
                sum += nums[j];
                if (sum == 0 && j > i)
                    return true;
                if (k != 0) {
                    if (sum % k == 0 && j > i)
                        return true;
                }
            }
        }
        return false;
    }
	
	/*
	 * https://discuss.leetcode.com/topic/80820/not-smart-solution-but-easy-to-understand
	 * 
	 * Rf : https://discuss.leetcode.com/topic/80798/share-my-ac-solution
	 */
	public boolean checkSubarraySum_Cummulative_sum(int[] nums, int k) {
		if (nums == null || nums.length == 0)
			return false;

		int[] preSum = new int[nums.length + 1];

		for (int i = 1; i <= nums.length; i++) {
			preSum[i] = preSum[i - 1] + nums[i - 1];
		}

		for (int i = 0; i < nums.length; i++) {
			for (int j = i + 2; j <= nums.length; j++) {
				if (k == 0) {
					if (preSum[j] - preSum[i] == 0) {
						return true;
					}
				} else if ((preSum[j] - preSum[i]) % k == 0) {
					return true;
				}
			}
		}
		return false;
	}

}
