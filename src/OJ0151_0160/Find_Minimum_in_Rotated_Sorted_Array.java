package OJ0151_0160;

public class Find_Minimum_in_Rotated_Sorted_Array {
	/*
	 * https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/discuss/48484/A-concise-solution-with-proof-in-the-comment
	 * 
	 * (1) loop is left < right, which means inside the loop, left always < right
	 * (2) since we use round up for mid, and left < right from (1), right would never 
	 *     be the same as mid
	 * (3) Therefore, we compare mid with right, since they will never be the same 
	 *     from (2)
	 * (4) if nums[mid] < nums[right], we will know the minimum should be in the left 
	 *     part, so we are moving right.
	 *     
	 *     (4-1) We can always make right = mid while we don't have to worry the loop 
	 *           will not ends. Since from (2), we know right would never be the same 
	 *           as mid, making right = mid will assure the interval is shrinking.
	 * (5) if nums[mid] > nums[right], minimum should be in the right part, so we are 
	 *     moving left. Since nums[mid] > nums[right], mid can't be the minimum, we 
	 *     can safely move left to mid + 1, which also assure the interval is 
	 *     shrinking
	 * 
	 * Rf :
	 * https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/discuss/48484/A-concise-solution-with-proof-in-the-comment/48551
	 * https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/discuss/158940/Beat-100%3A-Very-Simple-(Python)-Very-Detailed-Explanation
	 */
	public int findMin_compare_right_self(int[] num) {
		int left = 0, right = num.length - 1;
		while (left <= right) {
			int mid = (left + right) / 2;
			if (num[left] > num[right]) {
				if (num[right] < num[mid]) {
					left = mid + 1;
				} 
				else if (num[right] > num[mid]) {
					right = mid;
				}
			} 
			else {
				return num[left];
			}
		}
		return num[left];
	}
	
	/*
	 * https://discuss.leetcode.com/topic/5398/java-version-bisearch-solution
	 * 
	 * If the first member is less than the last member, there's no rotation in the 
	 * array. So we could directly return the first element in this subarray.
	 * 
	 * If value of the element in the middle is larger than the first element, we know 
	 * the rotation is at the second half of this array. Else, it is in the first 
	 * half in the array.
	 * 
	 * Rf :
	 * https://discuss.leetcode.com/topic/14768/4ms-simple-c-code-with-explanation
	 * https://discuss.leetcode.com/topic/4100/compact-and-clean-c-solution
	 * 
	 * Other code : 
	 * https://discuss.leetcode.com/topic/4214/my-java-solution-logn
	 */
	public int findMin_left_right_compare(int[] num) {
		int left = 0, right = num.length - 1;
		while (left <= right) {
			int mid = (left + right) / 2;
			if (num[left] > num[right]) {
				if (num[left] <= num[mid]) {
					left = mid + 1;
				} else {
					right = mid;
				}
			} else {
				return num[left];
			}
		}
		return num[left];
	}
	
	/*
	 * https://discuss.leetcode.com/topic/5170/java-solution-with-binary-search
	 */
	public int findMin(int[] num) {
		if (num == null || num.length == 0) {
			return 0;
		}
		if (num.length == 1) {
			return num[0];
		}
		int start = 0, end = num.length - 1;
		while (start <= end) {
			int mid = (start + end) / 2;
			if (mid > 0 && num[mid] < num[mid - 1]) {
				return num[mid];
			}
			if (num[start] <= num[mid] && num[mid] > num[end]) {
				start = mid + 1;
			} else {
				end = mid - 1;
			}
		}
		return num[start];
	}
	
	/*
	 * https://discuss.leetcode.com/topic/34858/9-line-java-code-beats-95-14-run-times
	 * 
	   1. a[mid] > a[left] && a[mid] > a[right], meaning we are on the bigger part, 
	      the smaller part is on our right, so go right
       2. a[mid] < a[left] && a[mid] < a[right], meaning we are on the smaller part, 
          to find the smallest element, go left
       3. if the array is not rotated (actually one rotating cycle completed), 
          we just need to go left, in this case a[mid] < a[right] always holds.
          
       if a[mid] > a[right], go right; if a[mid] < a[right], go left.
     *
	 */
	public int findMin2(int[] nums) {
		if (nums == null || nums.length == 0) {
			return Integer.MIN_VALUE;
		}
		int left = 0, right = nums.length - 1;
		while (left < right - 1) {    // while (left < right-1) is a useful technique
			int mid = left + (right - left) / 2;
			if (nums[mid] > nums[right]) {
				left = mid + 1;
			} else {
				right = mid;
			}
		}
		if (nums[left] > nums[right]) {
			return nums[right];
		}
		return nums[left];
	}
	
	/*
	 * https://discuss.leetcode.com/topic/18655/7-line-o-logn-solution
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/6112/a-concise-solution-with-proof-in-the-comment
	 * https://discuss.leetcode.com/topic/6112/a-concise-solution-with-proof-in-the-comment/5
	 */
	public int findMin3(int[] nums) {
		int left = 0, right = nums.length - 1, mid = 0;
		while (left < right) {
			mid = (left + right) >> 1;
			if (nums[mid] > nums[right])
				left = mid + 1;
			else
				right = mid;
		}
		return nums[right];
	}
	
	// self
	public int findMin_linear(int[] nums) {
        for (int i = 1; i < nums.length - 1; i++) {
            if (nums[i] < nums[i - 1] && nums[i] < nums[i + 1])
                return nums[i];
        }
        if (nums[0] < nums[nums.length - 1])
            return nums[0];
        else
            return nums[nums.length - 1];   
    }

}
