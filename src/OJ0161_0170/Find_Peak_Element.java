package OJ0161_0170;

public class Find_Peak_Element {
	/*
	 * by myself
	 * 
	 * https://leetcode.com/problems/find-peak-element/discuss/50439/Binary-search-solution
	 */
	public int findPeakElement_self(int[] nums) {
        if (nums.length == 1 || nums[0] > nums[1])
            return 0;
        if (nums[nums.length - 1] > nums[nums.length - 2])
            return nums.length - 1;
        
        int start = 0;
        int end = nums.length - 1;
        while (start < end) {
            int mid = start + (end - start) / 2;
            if (nums[mid] > nums[mid + 1] && nums[mid] > nums[mid - 1])
                return mid;
            else if (nums[mid] < nums[mid + 1])
                start = mid + 1;
            else
                end = mid - 1;
        }
        return start;
    }
	
	/*
	 * https://leetcode.com/problems/find-peak-element/discuss/50327/General-binary-search-in-Java
	 * 
	 * We want to check mid and mid+1 elements. if(nums[mid] < nums[mid+1]), 
	 * lo = mid + 1, otherwise hi = mid. The reason is that when there are even or 
	 * odd number of elements, the mid element is always going to have a next one 
	 * mid+1. We don't need to consider the case when there is less than 1 element as 
	 * it is not valid case for this problem. Finally we return lo as it will always 
	 * be a solution since it goes to mid+1 element in the first case, which is larger.
	 * 
	 * Rf : https://leetcode.com/problems/find-peak-element/discuss/50239/Java-solution-and-explanation-using-invariants
	 * 
	 * Other code:
	 * https://leetcode.com/problems/find-peak-element/discuss/50433/My-O(log(n))-Java-solution-using-binary-search
	 */
	public int findPeakElement2(int[] nums) {
		int n = nums.length;
		int lo = 0, hi = n - 1;
		while (lo < hi) {
			int mid = lo + (hi - lo) / 2;
			if (nums[mid] < nums[mid + 1]) {
				lo = mid + 1;
			} 
			else {
				hi = mid;
			}
		}
		return lo;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/find-peak-element/discuss/50236/O(logN)-Solution-JavaCode
	 * 
	 * If num[i-1] < num[i] > num[i+1], then num[i] is peak
	 * If num[i-1] < num[i] < num[i+1], then num[i+1...n-1] must contains a peak
	 * If num[i-1] > num[i] > num[i+1], then num[0...i-1] must contains a peak
	 * If num[i-1] > num[i] < num[i+1], then both sides have peak
	 * (n is num.length)
	 * 
	 * Rf : https://leetcode.com/articles/find-peak-element/
	 * 
	 * Other code:
	 * https://leetcode.com/problems/find-peak-element/discuss/50370/Accepted-Java-binary-search-solution
	 */
	public int findPeakElement_recursive(int[] num) {
		return helper(num, 0, num.length - 1);
	}

	public int helper(int[] num, int start, int end) {
		if (start == end) {
			return start;
		} 
		else if (start + 1 == end) {
			if (num[start] > num[end])
				return start;
			return end;
		} 
		else {
			int m = (start + end) / 2;

			if (num[m] > num[m - 1] && num[m] > num[m + 1]) {
				return m;
			} 
			else if (num[m - 1] > num[m] && num[m] > num[m + 1]) {
				return helper(num, start, m - 1);
			} 
			else {
				return helper(num, m + 1, end);
			}
		}
	}
	
	// https://leetcode.com/problems/find-peak-element/discuss/50232/Find-the-maximum-by-binary-search-(recursion-and-iteration)
	public int findPeakElement_Sequential(int[] nums) {
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] < nums[i - 1])
                return i - 1;
        }
        return nums.length - 1;
    }
	
	// by myself. time complexity: O(n)
	public int findPeakElement_self_n(int[] nums) {
        if (nums.length == 1 || nums[0] > nums[1])
            return 0;
        for (int i = 1; i < nums.length - 1; i++) {
            if (nums[i] > nums[i - 1] && nums[i] > nums[i + 1])
                return i;
        }
        return nums.length - 1;
    }

}
