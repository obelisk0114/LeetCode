package OJ0031_0040;

import java.util.Arrays;

/*
 * https://en.wikipedia.org/wiki/Permutation#Generation_in_lexicographic_order
 * http://blog.csdn.net/linhuanmars/article/details/20434115
 */

public class Next_Permutation {
	/*
	 * https://discuss.leetcode.com/topic/30212/easiest-java-solution
	 * 
	   1. scan from right to left, find first pair where a[i] > a[i-1]. 
	      note that a[i:] is non-ascending.
       2. scan from right to left again, find first element a[j] where a[j] > a[i-1]. 
          since a[i:] is non-ascending, a[j] is the smallest number that > a[i-1].
       3. swap a[i-1] with a[j]. note that after swap, a[i:] remains non-ascending.
       4. reverse a[i:] so that a[i:] becomes non-descending.
	 * 
	 * Rf :
	 * https://discuss.leetcode.com/topic/2542/share-my-o-n-time-solution
	 * https://discuss.leetcode.com/topic/15216/a-simple-algorithm-from-wikipedia-with-c-implementation-can-be-used-in-permutations-and-permutations-ii
	 * https://discuss.leetcode.com/topic/36534/20-line-1ms-in-place-java-code-with-expalantion
	 * https://discuss.leetcode.com/topic/14124/sharing-my-clean-and-easy-understand-java-code-with-explanation
	 * https://discuss.leetcode.com/topic/30688/readable-code-without-confusing-i-j-and-with-explanation
	 */
	public void nextPermutation(int[] A) {
		if (A == null || A.length <= 1)
			return;
		int i = A.length - 2;
		while (i >= 0 && A[i] >= A[i + 1]) // Find 1st id i that breaks descending order
			i--; 
		if (i >= 0) {              // If not entirely descending
			int j = A.length - 1;  // Start from the end
			while (A[j] <= A[i])   // Find rightmost first larger id j
				j--; 
			swap(A, i, j); // Switch i and j
		}
		reverse(A, i + 1, A.length - 1); // Reverse the descending sequence
	}

	public void reverse(int[] A, int i, int j) {
		while (i < j)
			swap(A, i++, j--);
	}

	void swap(int[] A, int i, int j) {
		int temp = A[i];
		A[i] = A[j];
		A[j] = temp;
	}
	
	// https://discuss.leetcode.com/topic/57881/simple-o-n-java-solution-with-explanation-improved-using-binary-search
	public void nextPermutation_binarySearch(int[] nums) {
        /* 1. Reverse find first number which breaks descending order. */
		int i = nums.length - 1;
		for (; i >= 1; i--)
			if (nums[i - 1] < nums[i])
				break;
        
		/* if no break found in step 1 */
		if (i == 0) {
			/* for case "1" and "1111" */
			if (nums.length == 1 || nums[0] == nums[1])
				return;
			/* for case "54321" */
			int lo = i, hi = nums.length - 1;
			while (lo < hi)
				swap(nums, lo++, hi--);
			return;
		}
        
        /* 2. Exchange this number with the least number that's greater than this number. */
        /* 2.1 Find the least number that's greater using binary search, O(log(nums.length-i)) */
		int j = binarySearchLeastGreater(nums, i, nums.length - 1, nums[i - 1]);

		/* 2.2 Exchange the numbers */
		if (j != -1)
			swap(nums, i - 1, j);

		/* 3. Reverse sort the numbers after the exchanged number. */
		int lo = i, hi = nums.length - 1;
		while (lo < hi)
			swap(nums, lo++, hi--);
	}
    
	public int binarySearchLeastGreater(int[] nums, int lo, int hi, int key) {
		while (lo <= hi) {
			int mid = lo + (hi - lo) / 2;
			if (nums[mid] > key) {
				lo = mid + 1;
			} else {
				hi = mid - 1;
			}
		}
		return hi;
	}
	
	// https://discuss.leetcode.com/topic/21291/simple-java-solution-for-your-reference
	public void nextPermutation_sort(int[] num) {
		for (int i = num.length - 1; i > 0; i--) {
			if (num[i - 1] < num[i]) {
				Arrays.sort(num, i, num.length);
				for (int j = i; j < num.length; j++) {
					if (num[j] > num[i - 1]) {
						// swap num[i-1] and num[j]
						num[i - 1] = num[i - 1] + num[j];
						num[j] = num[i - 1] - num[j];
						num[i - 1] = num[i - 1] - num[j];
						return;
					}
				}
			}
		}
		Arrays.sort(num);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
