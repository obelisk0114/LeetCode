package OJ0071_0080;

public class Remove_Duplicates_from_Sorted_Array_II {
	/*
	 * https://discuss.leetcode.com/topic/46519/short-and-simple-java-solution-easy-to-understand
	 * 
	 * Rf : https://discuss.leetcode.com/topic/17180/3-6-easy-lines-c-java-python-ruby
	 */
	public int removeDuplicates(int[] nums) {
		int i = 0;
		for (int n : nums)
			if (i < 2 || n > nums[i - 2])
				nums[i++] = n;
		return i;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/38644/share-my-o-n-time-and-o-1-space-solution-with-duplicates-are-allowed-at-most-n-times
	 * 
	 * if N duplicates are allowed, change the nums[dif-2] to nums[dif-N]
	 */
	public int removeDuplicates_see_forward_dif(int[] nums) {
		if (nums == null || nums.length < 3) {
			return nums == null ? 0 : nums.length;
		}
		int dif = 2;
		for (int i = 2; i < nums.length; i++) {
			if (nums[i] != nums[dif - 2]) {
				nums[dif++] = nums[i];
			}
		}
		return dif;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/40369/o-n-time-and-o-1-java-solution-when-allowed-at-most-k-times-of-duplicates
	 * 
	 * Others : 
	 * https://discuss.leetcode.com/topic/7673/share-my-o-n-time-and-o-1-solution-when-duplicates-are-allowed-at-most-k-times
	 * https://discuss.leetcode.com/topic/28798/java-1ms-solution
	 * https://discuss.leetcode.com/topic/14769/java-solution-generalized-to-maximal-k-time-occurences
	 */
	public int removeDuplicates_see_forward_1(int[] nums) {
		// define at most k times of duplicate numbers
		final int k = 2;

		// check if it is an empty array
		if (nums.length == 0)
			return 0;

		// start pointer of new array
		int m = 1;

		// count the time of duplicate numbers occurence
		int count = 1;

		for (int i = 1; i < nums.length; ++i) {
			if (nums[i] == nums[i - 1]) {
				if (count < k) {
					nums[m++] = nums[i];
				}
				count++;
			} else {
				count = 1;
				nums[m++] = nums[i];
			}
		}
		return m;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/24906/simple-java-solution-with-explanation
	 * 
	 * The variable flag is to show if this number (nums[i]) has appeared more or 
	 * equals to third times. The variable pointer is the location that each 
	 * number (nums[i]) should appeared in. The special case is when the array is 
	 * empty.
	 */
	public int removeDuplicates_store_forward(int[] nums) {
		if (nums.length == 0) {
			return 0;
		}
		int pointer = 0, flag = 0;
		for (int i = 1; i < nums.length; i++) {
			if (nums[i] == nums[i - 1] && flag == 0) {
				flag = 1;
				pointer++;
			} else if (nums[i] != nums[i - 1]) {
				flag = 0;
				pointer++;
			}
			nums[pointer] = nums[i];
		}
		return pointer + 1;
	}

}
