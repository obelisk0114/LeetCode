package OJ0081_0090;

public class Search_in_Rotated_Sorted_Array_II {
	/*
	 * https://discuss.leetcode.com/topic/47379/my-1ms-java-solution-very-easy-to-understand
	 * 
	 * Rf :
	 * https://discuss.leetcode.com/topic/310/when-there-are-duplicates-the-worst-case-is-o-n-could-we-do-better/2
	 * 
	 * Other Code
	 * https://discuss.leetcode.com/topic/29919/binary-search-java-solution
	 * https://discuss.leetcode.com/topic/61290/java-1ms-binary-search-solution-with-comments
	 */
	public boolean search(int[] nums, int target) {
		// if(nums == null || nums.length == 0) return false;

		int left = 0, right = nums.length - 1;
		while (left <= right) {
			int mid = left + (right - left) / 2;
			if (nums[mid] == target)
				return true;                   // find target
			if (nums[mid] == nums[left])
				left++;                        // find a duplicates num
			else if (nums[mid] > nums[left]) {
				if (target >= nums[left] && target < nums[mid])
					right = mid - 1;
				else
					left = mid + 1;
			} else {
				if (target > nums[mid] && target <= nums[right])
					left = mid + 1;
				else
					right = mid - 1;
			}
		}
		return false;
	}
	
	// https://discuss.leetcode.com/topic/39217/ac-answer-based-on-search-in-rotate-sorted-array-so-two-problems-consolidated-as-one
	public boolean search_modify(int[] nums, int target) {
		int start = 0;
		int end = nums.length - 1;

		while (start <= end) {
			int mid = start + (end - start) / 2;
			// System.out.format("start=%d,mid=%d,end=%d\n",start,mid,end);
			if (nums[mid] == target)
				return true;

			// need to handle: 1,3,1,1,1
			while (nums[start] == nums[mid] && start != mid) {
				start++;
			}
			while (nums[mid] == nums[end] && mid != end) {
				end--;
			}

			// the following is the same as problem I
			if (nums[start] <= nums[mid]) {
				if (nums[start] <= target && target < nums[mid]) {
					end = mid - 1;
				} else {
					start = mid + 1;
				}
			} else {
				if (nums[mid] < target && target <= nums[end]) {
					start = mid + 1;
				} else {
					end = mid - 1;
				}
			}
		}

		return false;
	}
	
	// https://discuss.leetcode.com/topic/11231/java-solution-with-comments
	public boolean search2(int[] A, int target) {
		int start = 0;
		int end = A.length - 1;
		while (start <= end) {
			int mid = start + (end - start) / 2;
			if (A[mid] == target) // case 0
				return true;
			// finally start == mid == end, if case 0, return true, else end the loop
			else if (A[start] == A[mid])
				start++;
			else if (A[end] == A[mid])
				end--;
			else if (A[start] <= target && target <= A[mid]) // case 1
				end = mid;
			else if (A[mid] < target && target <= A[end]) // case 2
				start = mid + 1;
			else if (A[start] > A[mid]) // case 2 is false, so target in this range
				end = mid;
			else // case A[mid] > A[end] and case 1 is false, similar to above
				start = mid + 1;
		}
	    return false;
	}
	
	// https://discuss.leetcode.com/topic/20473/o-lgn-with-worst-case-of-o-n
	
	// self
	public boolean search_linear(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == target)
                return true;
        }
        return false;
    }

}
