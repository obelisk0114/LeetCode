package OJ0031_0040;

/*
 * https://discuss.leetcode.com/topic/5891/clean-iterative-solution-with-two-binary-searches-with-explanation
 */

public class Search_for_a_Range {
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/6327/a-very-simple-java-solution-with-only-one-binary-search-algorithm
	 * 
	 * Rf : https://discuss.leetcode.com/topic/5891/clean-iterative-solution-with-two-binary-searches-with-explanation
	 */
	public int[] searchRange(int[] A, int target) {
		int start = firstGreaterEqual(A, target);
		if (start == A.length || A[start] != target) {
			return new int[]{-1, -1};
		}
		return new int[]{start, firstGreaterEqual(A, target + 1) - 1};
	}

	//find the first number that is greater than or equal to target.
	//could return A.length if target is greater than A[A.length-1].
	//actually this is the same as lower_bound in C++ STL.
	private int firstGreaterEqual(int[] A, int target) {
		int low = 0, high = A.length;
		while (low < high) {
			int mid = low + ((high - low) >> 1);
			//low <= mid < high
			if (A[mid] < target) {
				low = mid + 1;
			} else {
				//should not be mid-1 when A[mid]==target.
				//could be mid even if A[mid] > target because mid < high.
				high = mid;
			}
		}
		return low;
	}
	
	/*
	 * The following 3 functions are from this link.
	 * https://discuss.leetcode.com/topic/21783/easy-java-o-logn-solution
	 * 
	 * Rf : https://discuss.leetcode.com/topic/44031/easy-to-understand-java-ac-solution
	 */
	public int[] searchRange_2_search(int[] nums, int target) {
		int[] result = new int[2];
		result[0] = findFirst(nums, target);
		result[1] = findLast(nums, target);
		return result;
	}
	private int findFirst(int[] nums, int target) {
		int idx = -1;
		int start = 0;
		int end = nums.length - 1;
		while (start <= end) {
			int mid = (start + end) / 2;
			if (nums[mid] >= target) {
				end = mid - 1;
			} else {
				start = mid + 1;
			}
			if (nums[mid] == target)
				idx = mid;
		}
		return idx;
	}
	private int findLast(int[] nums, int target) {
		int idx = -1;
		int start = 0;
		int end = nums.length - 1;
		while (start <= end) {
			int mid = (start + end) / 2;
			if (nums[mid] <= target) {
				start = mid + 1;
			} else {
				end = mid - 1;
			}
			if (nums[mid] == target)
				idx = mid;
		}
		return idx;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/10692/simple-and-strict-o-logn-solution-in-java-using-recursion
	 */
	public int[] searchRange_recursive(int[] A, int target) {
		int[] range = { A.length, -1 };
		searchRange(A, target, 0, A.length - 1, range);
		if (range[0] > range[1])
			range[0] = -1;
		return range;
	}
	public void searchRange(int[] A, int target, int left, int right, int[] range) {
		if (left > right)
			return;
		int mid = left + (right - left) / 2;
		if (A[mid] == target) {
			if (mid < range[0]) {
				range[0] = mid;
				searchRange(A, target, left, mid - 1, range);
			}
			if (mid > range[1]) {
				range[1] = mid;
				searchRange(A, target, mid + 1, right, range);
			}
		} else if (A[mid] < target) {
			searchRange(A, target, mid + 1, right, range);
		} else {
			searchRange(A, target, left, mid - 1, range);
		}
	}
	
	// https://discuss.leetcode.com/topic/40559/share-my-concise-java-o-logn-solution-just-1-time-binary-search-easy-to-understand
	public int[] searchRange_1_loop(int[] nums, int target) {
		int[] res = { -1, -1 };
		if (nums.length == 0)
			return res;
		int lo = 0, hi = nums.length - 1;

		// lo is the start index of target
		// hi is the end index of target
		while (nums[lo] < nums[hi]) {
			int mid = lo + (hi - lo) / 2;
			if (nums[mid] > target) {// target is in the left half
				hi = mid - 1;
			} else if (nums[mid] < target) {// target is in the right half
				lo = mid + 1;
			} else {// find target, then need to find the start and end point
				if (nums[lo] == nums[mid]) {
					hi--;
				} else {
					lo++;
				}
			}
		}
		// check whether find the target number
		if (nums[lo] == nums[hi] && nums[lo] == target) {
			res[0] = lo;
			res[1] = hi;
		}

		return res;
	}
	
	// https://discuss.leetcode.com/topic/3364/my-2-binary-search-solution-without-too-many-if-checks

}
