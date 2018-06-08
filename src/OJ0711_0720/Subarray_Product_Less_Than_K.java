package OJ0711_0720;

public class Subarray_Product_Less_Than_K {
	// modify by myself
	public int numSubarrayProductLessThanK_modify(int[] nums, int k) {
		int count = 0;
		int product = 1;
        int start = 0;
		for (int j = 0; j < nums.length; j++) {
            if (nums[j] >= k) {
                start = j + 1;
                continue;
            }
            
			product *= nums[j];
			
			while (start < j && product >= k) {
				product /= nums[start++];
			}
			
			count += (j - start + 1);
		}
		return count;
	}
	
	/*
	 * https://leetcode.com/problems/subarray-product-less-than-k/discuss/108861/JavaC++-Clean-Code-with-Explanation
	 * 
	 * 1. The idea is always keep an max-product-window less than K;
	 * 2. Every time shift window by adding a new number on the right(j), 
	 *    if the product is greater than k, then try to reduce numbers on the left(i), 
	 *    until the subarray product fit less than k again, (subarray could be empty);
	 *    
	 * i is the smallest value so that the product in the window 
	 * pro = nums[i] * nums[i + 1] * ... * nums[j] is less than k.
	 * For every j, we update i and pro to maintain this invariant. 
	 * Then, the number of intervals with subarray product less than k and with 
	 * right-most coordinate j, is j - i + 1. 
	 * 
	 * Rf : https://leetcode.com/articles/subarray-product-less-than-k/
	 */
	public int numSubarrayProductLessThanK(int[] nums, int k) {
		if (k == 0)
			return 0;
		
		int cnt = 0;
		int pro = 1;
		for (int i = 0, j = 0; j < nums.length; j++) {
			pro *= nums[j];
			
			while (i <= j && pro >= k) {
				pro /= nums[i++];
			}
			
			cnt += j - i + 1;
		}
		return cnt;
	}
	
	/*
	 * https://leetcode.com/problems/subarray-product-less-than-k/discuss/122362/Most-elegant-Java-O(n)-solution!
	 * 
	 * Other code:
	 * https://leetcode.com/problems/subarray-product-less-than-k/discuss/108832/Straightforward-Solution
	 */
	public int numSubarrayProductLessThanK2(int[] nums, int k) {
		int start = 0, product = 1, count = 0;
		for (int i = 0; i < nums.length; i++) {
			product *= nums[i];
			
			while (start < nums.length && product >= k)
				product /= nums[start++];
			
			if (start <= i)
				count += (i - start + 1);
		}
		return count;
	}
	
	/*
	 * https://leetcode.com/problems/subarray-product-less-than-k/discuss/117755/Simpler-solution
	 * 
	 * Shrink the window so that product becomes less than k.
	 * 
	 * Rf : https://leetcode.com/problems/subarray-product-less-than-k/discuss/108834/Java-Two-Pointers-O(n)-time-O(1)-space
	 */
	public int numSubarrayProductLessThanK_2_while(int[] nums, int k) {
		int count = 0, l = 0, r = 0;
		long p = 1;
		while (r < nums.length) {
			// for every iteration, we try to get the longest subarray 
			// with product < k ending with nums[r];
			p *= nums[r];
			
			// try to make the subarray valid by removing the left elements from l
			// l <= r to prevent nums[r] >= k
			while (p >= k && l <= r) {
				p /= nums[l];
				l++;
			}

			// The length of valid subarray is r - l + 1, and since the array have 
			// only positive numbers. All subarrays with length 1 ~ r - l + 1 ending
			// with nums[r] are valid, and there are r - l + 1 of them.
			count += (r - l + 1);
			r++;
		}
		return count;
	}
	
	/*
	 * https://leetcode.com/problems/subarray-product-less-than-k/discuss/108865/Java-O(n)-solution-with-brief-explaination
	 * 
	 * For subarray start at position i, if the subarray ends at i + 1, i + 2 ...
	 * i + ct - 1 is correct. Then for the next start point i + 1, you don't have to 
	 * check these ends again, because these subarrays products are all smaller than 
	 * nums[i] * nums[i + 1] * ...* nums[i + ct - 1]. 
	 * You can directly start from checking the end of i + ct.
	 */
	public int numSubarrayProductLessThanK_fixed_left(int[] nums, int k) {
		int ct = 0;
		int prod = 1;
		int sol = 0;
		for (int i = 0; i < nums.length; i++) {
			if (ct == 0) {
				prod = 1;
			} 
			else {
				ct--;
				prod /= nums[i - 1];
			}
			
			for (int j = i + ct; j < nums.length; j++) {
				if (prod * nums[j] < k) {
					ct++;
					prod *= nums[j];
				} 
				else
					break;
			}
			sol += ct;
		}
		return sol;
	}
	
	/*
	 * https://leetcode.com/articles/subarray-product-less-than-k/
	 * 
	 * Use log to reduce the problem to subarray sums instead of subarray products. 
	 * After this transformation where every value x becomes log(x), let us take 
	 * prefix sums prefix[i+1] = nums[0] + nums[1] + ... + nums[i]. Now we are left 
	 * with the problem of finding, for each i, the largest j so that 
	 * nums[i] + ... + nums[j] = prefix[j] - prefix[i] < k.
	 * 
	 * Rf : https://leetcode.com/problems/subarray-product-less-than-k/discuss/108846/Python-solution-with-detailed-explanation
	 */
	public int numSubarrayProductLessThanK_log(int[] nums, int k) {
        if (k == 0) return 0;
        double logk = Math.log(k);
        double[] prefix = new double[nums.length + 1];
        for (int i = 0; i < nums.length; i++) {
            prefix[i+1] = prefix[i] + Math.log(nums[i]);
        }

        int ans = 0;
        for (int i = 0; i < prefix.length; i++) {
            int lo = i + 1, hi = prefix.length;
            while (lo < hi) {
                int mi = lo + (hi - lo) / 2;
                if (prefix[mi] < prefix[i] + logk - 1e-9) lo = mi + 1;
                else hi = mi;
            }
            ans += lo - i - 1;
        }
        return ans;
    }

}
