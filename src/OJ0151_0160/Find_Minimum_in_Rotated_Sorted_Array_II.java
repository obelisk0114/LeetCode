package OJ0151_0160;

public class Find_Minimum_in_Rotated_Sorted_Array_II {
	/*
	 * https://discuss.leetcode.com/topic/25248/super-simple-and-clean-java-binary-search
	 * 
	 * Rf : https://discuss.leetcode.com/topic/6468/my-pretty-simple-code-to-solve-it
	 */
	public int findMin(int[] nums) {
		int l = 0, r = nums.length - 1;
		while (l <= r) {
			int mid = (l + r) / 2;
			if (nums[mid] < nums[r]) {
				r = mid;
			} 
			else if (nums[mid] > nums[r]) {
				l = mid + 1;
			} 
			else {
				r--; // nums[mid] = nums[r] no idea, but we can eliminate nums[r];
			}
		}
		return nums[l];
	}
	
	/*
	 * Modify from this link
	 * https://discuss.leetcode.com/topic/4249/my-accepted-java-code
	 */
	public int findMin2_1(int[] num) {
        if (num == null || num.length == 0) {
			return -1; // throw an exception
		}
        
		int l = 0;
		int r = num.length - 1;
		while (l < r) {
			if (num[l] < num[r]) {
				return num[l];
			}
			int m = l + (r - l) / 2;
			if (num[l] > num[m]) {
				r = m;
			} 
			else if (num[l] < num[m]) {
				l = m + 1;
			} 
			else {           // num[l] == num[m]
				l++;
			}
		}
		return num[l];
    }
	
	// https://discuss.leetcode.com/topic/4249/my-accepted-java-code
	public int findMin2_2(int[] num) {
		if (num == null || num.length == 0) {
			return -1; // throw an exception
		}
		int l = 0;
		int r = num.length - 1;
		while (l < r) {
			if (num[l] < num[r]) {
				return num[l];
			}
			int m = l + (r - l) / 2;
			if (num[l] > num[m]) {
				r = m;
			} 
			else if (num[l] < num[m]) {
				l = m + 1;
			} 
			else {           // num[l] == num[m]
				if (num[l] == num[r]) {
					l++;
					r--;
				} 
				else {       // only the num[l] == num[m] > num[r] case left
					l = m + 1;
				}
			}
		}
		return num[l];
	}
	
	// https://discuss.leetcode.com/topic/28388/only-two-more-lines-code-on-top-of-the-solution-for-part-i
	public int findMin_addLoop(int[] nums) {
        if (nums == null || nums.length == 0) {
            return Integer.MIN_VALUE;
        }
        
        int start = 0, end = nums.length - 1;
        
        // Only need to add the following while loop on top of the solution 
        // for Part I
        // if two line segments have overlap, remove the overlap.
        // so, the problem can be solved as Part I
        while (nums[end] == nums[start] && end > start) {
            end--;     // Or "start++;"
        }
        
        while (start < end) {
            //if the linear monotonically increasing in [start, end]
            if (nums[start] < nums[end]) {
                return nums[start];
            }
            
            int mid = start + (end - start) / 2;
            if (nums[mid] >= nums[start]) {
                start = mid + 1;
            }
            else {
                end = mid;
            }
        }
        
        return nums[start];
    }
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/6423/accepted-solution-and-time-complexity
	 */
	public int findMin_recursive(int[] num) {
		int low = 0;
		int high = num.length - 1;
		if (num.length == 1)
			return num[0];

		return findMin_recursive(num, low, high);
	}
	public int findMin_recursive(int[] num, int low, int high) {
		if (low == high)
			return num[low];

		int mid = (low + high) / 2;
		if (num[mid] < num[high]) {
			return findMin_recursive(num, low, mid);
		} 
		else if (num[mid] > num[high]) {
			return findMin_recursive(num, mid + 1, high);
		} 
		else { // num[mid] == num[high]
			// in this case, the minimal element can be on either side.
			int minLeft = findMin_recursive(num, low, mid);
			int minRight = findMin_recursive(num, mid + 1, high);

			return Math.min(minLeft, minRight);
		}

	}
	
	// self
	public int findMin_linear(int[] nums) {
        int min = nums[0];
        for (int element : nums) {
            min = Math.min(min, element);
        }
        return min;
    }

}
