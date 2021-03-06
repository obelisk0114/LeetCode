package OJ0021_0030;

public class Remove_Duplicates_from_Sorted_Array {
	// https://leetcode.com/problems/remove-duplicates-from-sorted-array-ii/discuss/27987/Short-and-Simple-Java-solution-(easy-to-understand)
	public int removeDuplicates(int[] nums) {
        int i = 0;
		for (int n : nums)
			if (i < 1 || n > nums[i - 1])
				nums[i++] = n;
		return i;
    }
	
	// https://discuss.leetcode.com/topic/29252/simple-1ms-java-solution
	public int removeDuplicates3(int[] nums) {
		int dupes = 0;
		for (int i = 1; i < nums.length; i++) {
			if (nums[i] == nums[i - 1])
				dupes++;

			nums[i - dupes] = nums[i];
		}
		return nums.length - dupes;
	}
	
	/*
	 * https://leetcode.com/articles/remove-duplicates-sorted-array/
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/17252/5-lines-c-java-nicer-loops
	 * https://discuss.leetcode.com/topic/30160/java-solution-with-explanation
	 */
	public int removeDuplicates2(int[] nums) {
	    if (nums.length == 0) return 0;
	    int i = 0;
	    for (int j = 1; j < nums.length; j++) {
	        if (nums[j] != nums[i]) {
	            i++;
	            nums[i] = nums[j];
	        }
	    }
	    return i + 1;
	}

}
