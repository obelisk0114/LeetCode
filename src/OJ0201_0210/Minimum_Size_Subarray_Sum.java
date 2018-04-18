package OJ0201_0210;

public class Minimum_Size_Subarray_Sum {
	// https://leetcode.com/problems/minimum-size-subarray-sum/discuss/59223/Java-concise-solution-with-O(n)-complexity
	public int minSubArrayLen(int s, int[] nums) {
	    if (nums == null || nums.length == 0) {
		    return 0;
	    }
	    int minLen = nums.length + 1;
	    int start = 0;
	    int localSum = 0;
	    for (int end = 0; end < nums.length; end++) {
		    localSum += nums[end];
		    while (localSum >= s) {
			    minLen = Math.min(minLen, end - start + 1);
			    localSum -= nums[start++];
		    }
	    }
	    return minLen == (nums.length + 1) ? 0 : minLen;
	}
	
	/*
	 * https://leetcode.com/problems/minimum-size-subarray-sum/discuss/59209/Easy-JAVA-O(n)-Solution!
	 * 
	 * Rf : https://leetcode.com/problems/minimum-size-subarray-sum/discuss/59078/Accepted-clean-Java-O(n)-solution-(two-pointers)
	 */
	public int minSubArrayLen2(int s, int[] nums) {
		if (nums == null || nums.length < 1)
			return 0;

		int start = 0, end = 0, sum = 0, min = Integer.MAX_VALUE;

		while (end < nums.length) {
			sum += nums[end];

			// optimize the array
			while (sum >= s) {
				min = Math.min(min, end - start + 1);
				sum -= nums[start++];
			}
			end++;
		}
		return min == Integer.MAX_VALUE ? 0 : min;
	}
	
	/*
	 * https://leetcode.com/problems/minimum-size-subarray-sum/discuss/59225/290-ms-super-simple-java-solution
	 * 
	 * Other code:
	 * https://leetcode.com/problems/minimum-size-subarray-sum/discuss/59144/1-ms-O(n)-time-solution-in-Java-dynamic-sliding-window
	 */
	public int minSubArrayLen3(int targetSum, int[] nums) {
		int minLength = Integer.MAX_VALUE;

		int left = 0, right = 0;
		int slidingSum = 0;
		int n = nums.length;

		while (right < n) {
			if (slidingSum + nums[right] < targetSum) {
				slidingSum += nums[right];
				right += 1;
			} else {
				minLength = Math.min(minLength, right - left + 1);
				slidingSum -= nums[left];
				left += 1;
			}
		}

		return minLength == Integer.MAX_VALUE ? 0 : minLength;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/minimum-size-subarray-sum/discuss/59123/O(N)O(NLogN)-solutions-both-O(1)-space
	 * 
	 * O(NLogN) - search if a window of size k exists that satisfy the condition
	 * Only change mid => Keep shrinking the window size
	 */
	public int minSubArrayLen_window(int s, int[] nums) {
		int i = 1, j = nums.length, min = 0;
		while (i <= j) {
			int mid = (i + j) / 2;
			if (windowExist(mid, nums, s)) {
				j = mid - 1;
				min = mid;
			} else
				i = mid + 1;
		}
		return min;
	}

	private boolean windowExist(int size, int[] nums, int s) {
		int sum = 0;
		for (int i = 0; i < nums.length; i++) {
			if (i >= size)
				sum -= nums[i - size];
			
			sum += nums[i];
			if (sum >= s)
				return true;
		}
		return false;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/minimum-size-subarray-sum/discuss/59103/Two-AC-solutions-in-Java-with-time-complexity-of-N-and-NLogN-with-explanation
	 * 
	 * Since all elements are positive, the cumulative sum must be strictly increasing. 
	 * A subarray sum can be expressed as the difference between two cumulative sum. 
	 * Hence, given a start index for the cumulative sum array, 
	 * the other end index can be searched using binary search.
	 * 
	 * Other code:
	 * https://leetcode.com/problems/minimum-size-subarray-sum/discuss/59233/O(nlgn)-is-not-that-easy-here-is-my-Java-code/122751?page=1
	 */
	public int solveNLogN(int s, int[] nums) {
		int[] sums = new int[nums.length + 1];
		for (int i = 1; i < sums.length; i++)
			sums[i] = sums[i - 1] + nums[i - 1];
		
		int minLen = Integer.MAX_VALUE;
		for (int i = 0; i < sums.length; i++) {
			int end = binarySearch(i + 1, sums.length - 1, sums[i] + s, sums);
			if (end == sums.length)
				break;
			if (end - i < minLen)
				minLen = end - i;
		}
		return minLen == Integer.MAX_VALUE ? 0 : minLen;
	}
	private int binarySearch(int lo, int hi, int key, int[] sums) {
		while (lo <= hi) {
			int mid = (lo + hi) / 2;
			if (sums[mid] >= key) {
				hi = mid - 1;
			}
			else if (sums[mid] == key) {
				return mid;
			}
			else {
				lo = mid + 1;
			}
		}
		return lo;
	}

}
