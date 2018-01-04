package OJ0031_0040;

import java.util.Arrays;

public class Search_Insert_Position {
	/*
	 * https://discuss.leetcode.com/topic/7874/my-8-line-java-solution
	 * 
	 * Recursive : https://discuss.leetcode.com/topic/26766/simple-java-solution-binary-search
	 */
	public int searchInsert(int[] A, int target) {
		int low = 0, high = A.length - 1;
		while (low <= high) {
			int mid = (low + high) / 2;
			if (A[mid] == target)
				return mid;
			else if (A[mid] > target)
				high = mid - 1;
			else
				low = mid + 1;
		}
		return low;
	}
	
	// https://discuss.leetcode.com/topic/31499/very-concise-and-efficient-solution-in-java
	public int searchInsert2(int[] nums, int target) {
		int low = 0, high = nums.length;
		while (low < high) {
			int mid = low + (high - low) / 2;
			if (nums[mid] < target)
				low = mid + 1;
			else
				high = mid;
		}
		return low;
	}
	
	// myself
	public int searchInsert_self(int[] nums, int target) {
        int start = 0;
        int end = nums.length - 1;
        if (nums[end] < target)
            return nums.length;
        
        while (start < end) {
            int mid = start + (end - start) / 2;
            if (nums[mid] == target) {
                return mid;
            }
            else if (nums[mid] > target) {
                end = mid;
            }
            else {
                start = mid + 1;
            }
        }
        
        if (nums[start] > target) {
            return start;
        }
        else {
            return end;
        }
    }
	
	// https://discuss.leetcode.com/topic/10824/6-line-o-logn-java-solution
	public int searchInsert_search_for_a_range(int[] A, int target) {
		int i = 0, j = A.length - 1;
		while (i < j) {
			int mid = i + (j - i) / 2;
			if (A[mid] < target)
				i = mid + 1;
			else
				j = mid;
		}
		return (A[i] < target) ? i + 1 : i;
	}
	
	// https://docs.oracle.com/javase/7/docs/api/java/util/Arrays.html#binarySearch(int[],%20int)
	public int searchInsert_cheat(int[] nums, int target) {
        int pos = Arrays.binarySearch(nums, target);
        if (pos < 0) {
            pos = -(pos + 1);
        }
        return pos;
    }

}
