package OJ0151_0160;

public class Maximum_Product_Subarray {
	// https://discuss.leetcode.com/topic/18203/accepted-java-solution
	public int maxProduct(int[] a) {
		if (a == null || a.length == 0)
			return 0;

		int ans = a[0], min = ans, max = ans;

		for (int i = 1; i < a.length; i++) {
			if (a[i] >= 0) {
				max = Math.max(a[i], max * a[i]);
				min = Math.min(a[i], min * a[i]);
			} else {
				int tmp = max;
				max = Math.max(a[i], min * a[i]);
				min = Math.min(a[i], tmp * a[i]);
			}

			ans = Math.max(ans, max);
		}

		return ans;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/5161/simple-java-code
	 * 
	 * We have to compare among max * A[i], min * A[i] as well as A[i], 
	 * since this is product, a negative * negative could be positive.
	 * 
	 * Rf : https://discuss.leetcode.com/topic/4417/possibly-simplest-solution-with-o-n-time-complexity
	 */
	public int maxProduct_1(int[] A) {
		if (A == null || A.length == 0) {
			return 0;
		}
		int max = A[0], min = A[0], result = A[0];
		for (int i = 1; i < A.length; i++) {
			int temp = max;
			max = Math.max(Math.max(max * A[i], min * A[i]), A[i]);
			min = Math.min(Math.min(temp * A[i], min * A[i]), A[i]);
			if (max > result) {
				result = max;
			}
		}
		return result;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/4609/a-o-n-solution-though-not-as-great-as-answer-provided-by-leetcode
	 * 
	 * 1. given an array of integers, the max product ignoring sign is the product of 
	 *    all the elements, as long there is no 0.
	 *     
	 * 2. consider the sign. so if product is negative, we have odd number of negatives
	 *    (a) product of all excluding elements on the left, up to the first negative 
	 *        element.
	 *    (b) product of all excluding elements on the right, up to the last negative 
	 *        element.
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/51712/2-passes-scan-beats-99
	 */
	public int maxProduct2(int[] A) {
		// edge case
		if (A == null || A.length == 0)
			return 0;

		int max = Integer.MIN_VALUE;
		int product = 1;
		// first go from left to right
		for (int i = 0; i < A.length; i++) {
			product *= A[i];
			if (product > max)
				max = product;
			if (product == 0)
				product = 1; // reset if encounter 0
		}

		// then go from right to left
		product = 1;
		for (int i = A.length - 1; i >= 0; i--) {
			product *= A[i];
			if (product > max)
				max = product;
			if (product == 0)
				product = 1; // reset if encounter 0
		}

		return max;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/3581/share-my-dp-code-that-got-ac
	 * 
	 * f[i] means maximum product that can be achieved ending with i
	 * g[i] means minimum product that can be achieved ending with i
	 */
	public int maxProduct_DP(int[] A) {
		if (A == null || A.length == 0) {
			return 0;
		}
		int[] f = new int[A.length];
		int[] g = new int[A.length];
		f[0] = A[0];
		g[0] = A[0];
		int res = A[0];
		for (int i = 1; i < A.length; i++) {
			f[i] = Math.max(Math.max(f[i - 1] * A[i], g[i - 1] * A[i]), A[i]);
			g[i] = Math.min(Math.min(f[i - 1] * A[i], g[i - 1] * A[i]), A[i]);
			res = Math.max(res, f[i]);
		}
		return res;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Maximum_Product_Subarray MaxProduct = new Maximum_Product_Subarray();
		int[] a = {2, 5, -9, 1, 6, 3, 0, -8, 0, -9, 2, 10, -1, 5};
		System.out.println(MaxProduct.maxProduct(a));
		System.out.println(MaxProduct.maxProduct_1(a));
		System.out.println(MaxProduct.maxProduct2(a));
		System.out.println(MaxProduct.maxProduct_DP(a));

	}

}
