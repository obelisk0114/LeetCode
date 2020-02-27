package OJ0011_0020;

import java.util.Arrays;

public class Three_Sum_Closest {
	// https://discuss.leetcode.com/topic/31117/7ms-and-o-n-2-java-solution
	public int threeSumClosest(int[] nums, int target) {
		Arrays.sort(nums);
		int closest = nums[0] + nums[1] + nums[2];
		int low, high;
		
		for (int i = 0; i < nums.length - 2; i++) {
			low = i + 1;
			high = nums.length - 1;
			
			while (low < high) {
				if (nums[low] + nums[high] == target - nums[i])
					return target;
				else if (nums[low] + nums[high] > target - nums[i]) {
					while (low < high && nums[low] + nums[high] > target - nums[i])
						high--;
					if (Math.abs(nums[i] + nums[low] + nums[high + 1] - target) < Math.abs(closest - target))
						closest = nums[i] + nums[low] + nums[high + 1];
				} 
				else {
					while (low < high && nums[low] + nums[high] < target - nums[i])
						low++;
					if (Math.abs(nums[i] + nums[low - 1] + nums[high] - target) < Math.abs(closest - target))
						closest = nums[i] + nums[low - 1] + nums[high];
				}
			}
		}
		return closest;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/5192/java-solution-with-o-n2-for-reference
	 * 
	 * Rf : https://discuss.leetcode.com/topic/17215/c-solution-o-n-2-using-sort
	 */
	public int threeSumClosest_simple(int[] num, int target) {
        int result = num[0] + num[1] + num[num.length - 1];
        Arrays.sort(num);
        for (int i = 0; i < num.length - 2; i++) {
            int start = i + 1, end = num.length - 1;
            while (start < end) {
                int sum = num[i] + num[start] + num[end];
                if (Math.abs(sum - target) < Math.abs(result - target)) {
                    result = sum;
                }
                if (sum > target) {
                    end--;
                } 
                else {
                    start++;
                }
            }
        }
        return result;
    }
	
	/*
	 * self
	 * 
	 * Rf : https://discuss.leetcode.com/topic/35892/share-my-24-line-java-code-beats-94-57-run-times
	 */
	public int threeSumClosest_self(int[] nums, int target) {
        Arrays.sort(nums);
        int ans = Integer.MAX_VALUE;
        for (int i = 0; i < nums.length - 2; i++) {
            if (i > 0 && nums[i] == nums[i - 1])
                continue;
            
            int left = i + 1;
            int right = nums.length - 1;
            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];
                if (Math.abs(sum - (double) target) < Math.abs(ans - (double) target))
                    ans = sum;
                
                if (sum == target) {
                    return target;
                }
                else if (sum > target) {
                    right--;
                    while (left < right && nums[right] == nums[right + 1])
                        right--;
                }
                else {
                    left++;
                    while (left < right && nums[left] == nums[left - 1])
                        left++;
                }
            }
        }
        
        return ans;
    }
	
	// https://discuss.leetcode.com/topic/41580/sharing-my-java-optimized-solution-5ms-beats-99-9

}
