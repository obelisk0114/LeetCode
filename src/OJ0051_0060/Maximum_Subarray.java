package OJ0051_0060;

/*
 * https://en.wikipedia.org/wiki/Maximum_subarray_problem
 * 
 * http://blog.csdn.net/hcbbt/article/details/10454947
 * http://emn178.pixnet.net/blog/post/88907691-³Ì¤j¤l§Ç¦C%28maximum-subarray%29
 * https://my.oschina.net/itblog/blog/267860
 * 
 * https://stackoverflow.com/questions/16605991/number-of-subarrays-divisible-by-k
 */

public class Maximum_Subarray {
	/*
	 * https://discuss.leetcode.com/topic/34670/o-n-time-o-1-space-dynamic-programming-8-line-java-solution-with-comment
	 * 
	 * from i --> i+1:
	 * 1. if sum[i] >= 0, it gives non-negative contribution, sum[i+1] = sum[i] + a[i+1]
	 * 2. if sum[i] < 0, it gives negative contribution, sum[i+1] = a[i+1]
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/7447/o-n-java-solution
	 * https://discuss.leetcode.com/topic/27151/my-concise-o-n-dp-java-solution
	 */
	public int maxSubArray(int[] nums) {
		if (nums == null || nums.length == 0) {
			return 0;
		}
		int max = nums[0], sum = nums[0];
		for (int i = 1; i < nums.length; ++i) {
			if (sum >= 0) {
				sum += nums[i];
			} else {
				sum = nums[i];
			}
			max = Math.max(max, sum);
		}
		return max;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/5000/accepted-o-n-solution-in-java
	 * 
	 * Suppose we've solved the problem for A[1 .. i - 1]; 
	 * how can we extend that to A[1 .. i]? 
	 * The maximum sum in the first i elements is either the maximum sum in the first 
	 * i - 1 elements (which we'll call MaxSoFar), 
	 * or it is that of a subvector that ends in position i 
	 * (which we'll call MaxEndingHere).
	 * 
	 * MaxEndingHere is either A[i] plus the previous MaxEndingHere, or just A[i], 
	 * whichever is larger.
	 */
	public int maxSubArray_original(int[] A) {
		int maxSoFar = A[0], maxEndingHere = A[0];
		for (int i = 1; i < A.length; ++i) {
			maxEndingHere = Math.max(maxEndingHere + A[i], A[i]);
			maxSoFar = Math.max(maxSoFar, maxEndingHere);
		}
		return maxSoFar;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/27151/my-concise-o-n-dp-java-solution
	 * 
	 * Rf : https://discuss.leetcode.com/topic/6413/dp-solution-some-thoughts
	 */
	public int maxSubArray_DP(int[] A) {
		int dp[] = new int[A.length]; int max = A[0]; dp[0] = A[0]; 
		for (int i = 1; i < A.length; i++) {			
			dp[i] = Math.max(dp[i-1] + A[i] ,A[i]);
			max = Math.max(max, dp[i]);
		}
		return max;
	}
	
	/*
	 * The following 3 functions are from this link.
	 * https://discuss.leetcode.com/topic/42213/my-divide-and-conquer-solution-in-java-under-instruction-of-clrs-o-nlogn
	 * 
	 * Rf : https://discuss.leetcode.com/topic/426/how-to-solve-maximum-subarray-by-using-the-divide-and-conquer-approach/2
	 */
	public int maxSubArray_CLRS(int[] nums) {
		return Subarray(nums, 0, nums.length - 1);
	}
	public int Subarray(int[] A, int left, int right) {
		if (left == right) {
			return A[left];
		}
		int mid = left + (right - left) / 2;
		int leftSum = Subarray(A, left, mid);              // left part
		int rightSum = Subarray(A, mid + 1, right);        // right part
		int crossSum = crossSubarray(A, left, right);      // cross part
		if (leftSum >= rightSum && leftSum >= crossSum) {  // left part is max
			return leftSum;
		}
		if (rightSum >= leftSum && rightSum >= crossSum) { // right part is max
			return rightSum;
		}
		return crossSum;                                   // cross part is max
	}
	public int crossSubarray(int[] A, int left, int right) {
		int leftSum = Integer.MIN_VALUE;
		int rightSum = Integer.MIN_VALUE;
		int sum = 0;
		int mid = left + (right - left) / 2;
		for (int i = mid; i >= left; i--) {
			sum = sum + A[i];
			if (leftSum < sum) {
				leftSum = sum;
			}
		}
		sum = 0;
		for (int j = mid + 1; j <= right; j++) {
			sum = sum + A[j];
			if (rightSum < sum) {
				rightSum = sum;
			}
		}
		return leftSum + rightSum;
	}

	// Self test
	public void maxSubArray_test(int[] nums) {
		int max = nums[0], sum = nums[0];
		int start = 0, end = 0;
		int start_tmp = 0, end_tmp = 0;
		for (int i = 1; i < nums.length; ++i) {
			if (sum >= 0) {
				sum += nums[i];
				end_tmp = i;
			} 
			else {
				sum = nums[i];
				start_tmp = i;
			}
			
			if (sum > max) {				
				max = sum;
				start = start_tmp;
				end = end_tmp;
			}
		}
		
		if (start > end)
			end = start;
		System.out.println("\nTest : \nMax value : " + max);
		System.out.println("Start at : " + start + " ; End at : " + end);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Maximum_Subarray maxSub = new Maximum_Subarray();
		//int[] a = { -2, 1, -3, 4, -1, 2, 1, -5, 4 };   // 6
		//int[] a = {-1, 0};
		int[] a = {-1, 0, -5};
		System.out.println(maxSub.maxSubArray(a));
		System.out.println(maxSub.maxSubArray_original(a));
		System.out.println(maxSub.maxSubArray_DP(a));
		System.out.println(maxSub.maxSubArray_CLRS(a));
		maxSub.maxSubArray_test(a);

	}

}
