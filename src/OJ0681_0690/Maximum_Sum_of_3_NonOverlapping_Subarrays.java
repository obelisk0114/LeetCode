package OJ0681_0690;

public class Maximum_Sum_of_3_NonOverlapping_Subarrays {
	/*
	 * https://leetcode.com/problems/maximum-sum-of-3-non-overlapping-subarrays/solution/
	 * 
	 * 使用 W 來儲存 k 區間和
	 * left 是到 i 為止，左邊區間和最大的位置
	 * right 是到 i 為止，右邊區間和最大的位置
	 * 
	 * 設 3 個 subarray 的起始位置為 i, j, l
	 * 
	 * 因為 j 可以輕易求出 i、l，所以由 j 開始尋找
	 * 
	 * j 所代表的區間為 [j, j + k - 1]
	 * 左邊最大為 left[j - k]
	 * 右邊最大為 right[j + k]
	 * 
	 * 因此將 j 從 k loop 到 W.length - k 即可
	 * 
	 * Consider an array W of each interval's sum, where each interval is the given 
	 * length k. (i, j, l) with i + k <= j and j + k <= l that maximizes 
	 * W[i] + W[j] + W[l]?
	 * 
	 * If we know that i is where the largest value of W[i] occurs first on [0,5], 
	 * then on [0,6] the first occurrence of the largest W[i] must be either i or 6.
	 * 
	 * At the end, left[z] will be the first occurrence of the largest value of W[i] 
	 * on the interval 0 <= i <= z, and right[z] will be the same but on the interval 
	 * z <= i <= len(W)-1. This means that for some choice j, the candidate answer 
	 * must be (left[j - k], j, right[j + k]). We take the candidate that produces the 
	 * maximum W[i] + W[j] + W[l].
	 * 
	 * Rf :
	 * https://leetcode.com/problems/maximum-sum-of-3-non-overlapping-subarrays/solution/324303
	 */
	public int[] maxSumOfThreeSubarrays_ad_hoc(int[] nums, int k) {
		// W is an array of sums of windows
		int[] W = new int[nums.length - k + 1];
		int currSum = 0;
		for (int i = 0; i < nums.length; i++) {
			currSum += nums[i];
			
			if (i >= k) {
				currSum -= nums[i - k];
			}
			if (i >= k - 1) {
				W[i - k + 1] = currSum;
			}
		}

		int[] left = new int[W.length];
		int best = 0;
		for (int i = 0; i < W.length; i++) {
			// 字母順序最小，所以 > 才更新
			if (W[i] > W[best]) {				
				best = i;
			}
			
			left[i] = best;
		}

		int[] right = new int[W.length];
		best = W.length - 1;
		for (int i = W.length - 1; i >= 0; i--) {
			// 字母順序最小，所以 = 也要更新
			if (W[i] >= W[best]) {
				best = i;
			}
			
			right[i] = best;
		}

		int[] ans = new int[] { -1, -1, -1 };
		for (int j = k; j < W.length - k; j++) {
			int i = left[j - k], l = right[j + k];
			if (ans[0] == -1 
					|| W[i] + W[j] + W[l] > W[ans[0]] + W[ans[1]] + W[ans[2]]) {
				
				ans[0] = i;
				ans[1] = j;
				ans[2] = l;
			}
		}
		return ans;
	}
	
	/*
	 * https://leetcode.com/problems/maximum-sum-of-3-non-overlapping-subarrays/discuss/108228/Non-DP-method-Straight-forward-Clear-explanation.
	 * 
	 * 1. Since we only care about the sum of the subarray. We can create an array and 
	 *    store the subarray sum starting from each index.
	 * 2. If we start from the left index, middle index and right index, this will be 
	 *    very slow because the middle index is dependent on the left index and the 
	 *    right index is dependent on the middle index. So the optimization is 
	 *    Decoupling. Let left and right index both connect to the middle index. Then, 
	 *    the left and right index can be updated separately. By the way, the range of 
	 *    left, middle and right indices are 0 -> middle-k, k -> nums.length-2k and 
	 *    middle+k -> end
	 * 3. Updating: we need to check the situations for each middle index. With middle 
	 *    index moving forward, the range of the left index increases one while the 
	 *    one of right index decreases one for each iteration. 
	 * 
	 * For left index: we only need to check the sum of the new left index is bigger 
	 *                 than the previous max one.
	 * For right index: we only update when the previous max index is no longer in 
	 *                  the range.
	 */
	public int[] maxSumOfThreeSubarrays_run_middle(int[] nums, int k) {
		int n = nums.length;
		int m = n - k + 1;
		int[] sum = new int[m];
		for (int i = 0; i < k; i++)
			sum[0] += nums[i];
		for (int i = 1; i < m; i++)
			sum[i] = sum[i - 1] + nums[i + k - 1] - nums[i - 1];
		
		int[] res = { 0, k, 2 * k - 1 };
		int[] temp = { 0, k, 2 * k - 1 };
		int max = Integer.MIN_VALUE;
		for (int middle = k; middle <= n - 2 * k; middle++) {
			temp[1] = middle;
			
			// update the left index (temp[0]) when the new left index.
			if (sum[temp[0]] < sum[middle - k])
				temp[0] = middle - k;
			
			// update the right index(temp[1]) when previous max index is not in the 
			// range.
			if (temp[2] < middle + k) {
				int tempR = Integer.MIN_VALUE;
				for (int right = middle + k; right < m; right++) {
					if (tempR < sum[right]) {
						tempR = sum[right];
						temp[2] = right;
					}
				}
			}
			
			if (max < sum[temp[0]] + sum[temp[1]] + sum[temp[2]]) {
				max = sum[temp[0]] + sum[temp[1]] + sum[temp[2]];
				res[0] = temp[0];
				res[1] = temp[1];
				res[2] = temp[2];
			}
		}
		return res;
	}
	
	/*
	 * https://leetcode.com/problems/maximum-sum-of-3-non-overlapping-subarrays/discuss/117244/non-DP-O(N)-time-O(N)-space-beats-100
	 * 
	 * Other code:
	 * https://leetcode.com/problems/maximum-sum-of-3-non-overlapping-subarrays/discuss/108232/Java-clean-and-easy-understand-solution
	 */
	public int[] maxSumOfThreeSubarrays_left_right_sum_max(int[] nums, int k) {
		// sums is an array of sums of windows sums[i] = sum (nums[i, i + k - 1])
		final int[] sums = new int[nums.length - (k - 1)];
		for (int i = 0; i < k; i++)
			sums[0] += nums[i];
		for (int i = 1; i < sums.length; i++)
			sums[i] = sums[i - 1] - nums[i - 1] + nums[k + i - 1];
		
		// find 3 values in sums, such that sums[i] + sums[j] + sums[j2] is the max,
		// j-i >= k, j2-j >= k, j2-i >= 2*k
		// j can be in the range of [k, nums.length-2k], so create array of size
		// nums.length - 3k + 1 = sums.length - 2*k
		final int[] left = new int[sums.length - 2 * k], right = new int[left.length];
		
		// left for the index into nums that yields running left max
		for (int i = 1, index = 0; i < left.length; i++) {
			if (sums[i] > sums[index])
				index = i; // > for the sake of lexicographical
			left[i] = index;
		}
		
		right[right.length - 1] = sums.length - 1;
		// right for the index into nums that yields running right max
		for (int i = right.length - 2, index = sums.length - 1; i >= 0; i--) {
			if (sums[i + 2 * k] >= sums[index])
				index = i + 2 * k; // >= for the sake of lexicographical
			right[i] = index;
		}
		
		int max = 0;
		int index = 0;
		for (int i = 0; i < left.length; i++) {
			final int sum = sums[left[i]] + sums[k + i] + sums[right[i]];
			if (sum > max) {
				max = sum;
				index = i;
			}
		}
		return new int[] { left[index], k + index, right[index] };
	}
	
	/*
	 * https://leetcode.com/problems/maximum-sum-of-3-non-overlapping-subarrays/discuss/108239/Java-DP-Generic-Solution-for-M-subarrays
	 * 
	 * indices[i][j] array means ending in position i - 1, the starting position of 
	 * jth element we should take. We first check the last element we should take 
	 * using indices[n][3], where n is the length of array, aka the last element in 
	 * the array. From there, we get the starting position of our 3rd element, let's 
	 * call it p3. Then we backtrack to get the 2nd element by visiting pos[2][p3] 
	 * (ending in p3 -1). Let's call it p2. Last, we visit pos[1][p2] to get the 1st 
	 * element.
	 * 
	 * Rf : https://leetcode.com/problems/maximum-sum-of-3-non-overlapping-subarrays/discuss/108230/Clean-Java-DP-O(n)-Solution.-Easy-extend-to-Sum-of-K-Non-Overlapping-SubArrays.
	 */
	public int[] maxSumOfThreeSubarrays_k_non_overlap(int[] nums, int k) {
		int n = nums.length;
		int m = 3;
		
		int[] sums = new int[n + 1];
		int[][] max = new int[n + 1][m + 1]; // max[j][i] = max sum from nums[0..j-1]
		int[][] indices = new int[n + 1][m + 1];
		for (int i = 1; i <= n; i++)
			sums[i] = sums[i - 1] + nums[i - 1];
		
		for (int i = 1; i <= m; i++) {
			for (int j = i * k; j <= n; j++) {  // j is the last one in subarray
				if (max[j - k][i - 1] + sums[j] - sums[j - k] > max[j - 1][i]) {
					indices[j][i] = j - k;
					max[j][i] = max[j - k][i - 1] + sums[j] - sums[j - k];
				} 
				else {
					max[j][i] = max[j - 1][i];
					indices[j][i] = indices[j - 1][i];
				}
			}
		}
		
		int[] ret = new int[m];
		ret[m - 1] = indices[n][m];
		for (int i = m - 2; i >= 0; i--)
			ret[i] = indices[ret[i + 1]][i + 1];
		return ret;
	}
	
	/*
	 * https://leetcode.com/problems/maximum-sum-of-3-non-overlapping-subarrays/discuss/108231/C++Java-DP-with-explanation-O(n)
	 * 
	 * The question asks for three non-overlapping intervals with maximum sum of all 3 
	 * intervals. If the middle interval is [i, i+k-1], where k <= i <= n-2k, the left 
	 * interval has to be in subrange [0, i-1], and the right interval is from 
	 * subrange [i+k, n-1].
	 * 
	 * posLeft[i] is the starting index for the left interval in range [0, i];
	 * posRight[i] is the starting index for the right interval in range [i, n-1];
	 * 
	 * We test every possible starting index of middle interval, i.e. k <= i <= n-2k, 
	 * and we can get the corresponding left and right max sum intervals easily from 
	 * DP. And the run time is O(n).
	 * 
	 * In order to get lexicographical smallest order, when there are two intervals 
	 * with equal max sum, always select the left most one. So in the code, the if 
	 * condition is ">= tot" for right interval due to backward searching, 
	 * and "> tot" for left interval.
	 */
	public int[] maxSumOfThreeSubarrays_prefix_sum_DP(int[] nums, int k) {
		int n = nums.length, maxsum = 0;
		int[] sum = new int[n + 1];
		int[] posLeft = new int[n], posRight = new int[n];
		int[] ans = new int[3];
		for (int i = 0; i < n; i++)
			sum[i + 1] = sum[i] + nums[i];
		
		// DP for starting index of the left max sum interval
		for (int i = k, tot = sum[k] - sum[0]; i < n; i++) {
			if (sum[i + 1] - sum[i + 1 - k] > tot) {
				posLeft[i] = i + 1 - k;
				tot = sum[i + 1] - sum[i + 1 - k];
			} 
			else
				posLeft[i] = posLeft[i - 1];
		}
		
		// DP for starting index of the right max sum interval
		// caution: the condition is ">= tot" for right interval, and "> tot" for left interval
		posRight[n - k] = n - k;
		for (int i = n - k - 1, tot = sum[n] - sum[n - k]; i >= 0; i--) {
			if (sum[i + k] - sum[i] >= tot) {
				posRight[i] = i;
				tot = sum[i + k] - sum[i];
			} 
			else
				posRight[i] = posRight[i + 1];
		}
		
		// test all possible middle interval
		for (int i = k; i <= n - 2 * k; i++) {
			int l = posLeft[i - 1], r = posRight[i + k];
			int tot = (sum[i + k] - sum[i]) + (sum[l + k] - sum[l]) + (sum[r + k] - sum[r]);
			if (tot > maxsum) {
				maxsum = tot;
				ans[0] = l;
				ans[1] = i;
				ans[2] = r;
			}
		}
		return ans;
	}
	
	// https://leetcode.com/problems/maximum-sum-of-3-non-overlapping-subarrays/discuss/108238/Python-o(n)-time-o(1)-space.-Greedy-solution.
	public int[] maxSumOfThreeSubarrays_greedy(int[] nums, int k) {
        // Best single, double, and triple sequence found so far
		int bestSeq = 0;
		int[] bestTwoSeq = { 0, k };
		int[] bestThreeSeq = { 0, k, k * 2 };

        // Sums of each window
        int seqSum = 0;
        for (int i = 0; i < k; i++) {
            seqSum += nums[i];
        }
        int seqTwoSum = 0;
        for (int i = k; i < k * 2; i++) {
            seqTwoSum += nums[i];
        }
        int seqThreeSum = 0;
        for (int i = k * 2; i < k * 3; i++) {
            seqThreeSum += nums[i];
        }

        // Sums of combined best windows
        int bestSeqSum = seqSum;
        int bestTwoSum = seqSum + seqTwoSum;
        int bestThreeSum = seqSum + seqTwoSum + seqThreeSum;

        // Current window positions
        int seqIndex = 1;
        int twoSeqIndex = k + 1;
        int threeSeqIndex = k*2 + 1;
        while (threeSeqIndex <= nums.length - k) {
            // Update the three sliding windows
            seqSum = seqSum - nums[seqIndex - 1] + nums[seqIndex + k - 1];
            seqTwoSum = seqTwoSum - nums[twoSeqIndex - 1] + nums[twoSeqIndex + k - 1];
            seqThreeSum = seqThreeSum - nums[threeSeqIndex - 1] + nums[threeSeqIndex + k - 1];
            
            // Update best single window
            if (seqSum > bestSeqSum) {
                bestSeq = seqIndex;
                bestSeqSum = seqSum;
            }

            // Update best two windows
            if (seqTwoSum + bestSeqSum > bestTwoSum) {
                bestTwoSeq[0] = bestSeq;
                bestTwoSeq[1] = twoSeqIndex;
                bestTwoSum = seqTwoSum + bestSeqSum;
            }

            // Update best three windows
            if (seqThreeSum + bestTwoSum > bestThreeSum) {
                bestThreeSeq[0] = bestTwoSeq[0];
                bestThreeSeq[1] = bestTwoSeq[1];
                bestThreeSeq[2] = threeSeqIndex;
                bestThreeSum = seqThreeSum + bestTwoSum;
            }

            // Update the current positions
            seqIndex += 1;
            twoSeqIndex += 1;
            threeSeqIndex += 1;
        }

        return bestThreeSeq;
    }
	
	// https://leetcode.com/problems/maximum-sum-of-3-non-overlapping-subarrays/discuss/108245/My-complex-but-maybe-more-structure-solution-java-with-memo

}
