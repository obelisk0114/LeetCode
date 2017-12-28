package OJ0011_0020;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;

/*
 * Fun fact:
 * https://discuss.leetcode.com/topic/660/any-solution-which-is-better-than-o-n-2-exists
 */

public class Three_Sum {
	/*
	 * https://discuss.leetcode.com/topic/28857/easiest-java-solution
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/8125/concise-o-n-2-java-solution
	 * https://discuss.leetcode.com/topic/8125/concise-o-n-2-java-solution/7
	 * https://discuss.leetcode.com/topic/26050/simple-o-n-2-two-pointers-java-solution
	 */
	public List<List<Integer>> threeSum(int[] nums) {
		List<List<Integer>> res = new ArrayList<>();
		Arrays.sort(nums);
		
		for (int i = 0; i + 2 < nums.length; i++) {            // nums[i] <= 0
			if (i > 0 && nums[i] == nums[i - 1]) {             // skip same result
				continue;
			}
			
			int j = i + 1, k = nums.length - 1;
			int target = 0 - nums[i];
			
			while (j < k) {
				if (nums[j] + nums[k] == target) {
					res.add(Arrays.asList(nums[i], nums[j], nums[k]));
					j++;
					k--;
					
					while (j < k && nums[j] == nums[j - 1])    // skip same result
						j++;
					while (j < k && nums[k] == nums[k + 1])    // skip same result
						k--;
				} else if (nums[j] + nums[k] > target) {
					k--;
				} else {
					j++;
				}
			}
		}
		return res;
	}
	
	// https://discuss.leetcode.com/topic/11866/my-accepted-java-solution-using-hashset-the-time-complexity-is-o-n-2
	public List<List<Integer>> threeSum_HashSet(int[] num) {
		Arrays.sort(num);
		List<List<Integer>> list = new ArrayList<List<Integer>>();
		HashSet<List<Integer>> set = new HashSet<List<Integer>>();
		
		for (int i = 0; i < num.length; i++) {
			for (int j = i + 1, k = num.length - 1; j < k;) {
				if (num[i] + num[j] + num[k] == 0) {
					List<Integer> l = new ArrayList<Integer>();
					l.add(num[i]);
					l.add(num[j]);
					l.add(num[k]);
					
					if (set.add(l))
						list.add(l);
					
					j++;
					k--;
				} else if (num[i] + num[j] + num[k] < 0)
					j++;
				else
					k--;
			}
		}
		return list;
	}

}
