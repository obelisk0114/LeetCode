package OJ0911_0920;

import java.util.Deque;
import java.util.ArrayDeque;

public class Maximum_Sum_Circular_Subarray {
	/*
	 * https://leetcode.com/problems/maximum-sum-circular-subarray/discuss/633058/Java-or-C%2B%2B-or-Python3-or-With-detailed-explanation-or-O(N)-time-or-O(1)
	 * 
	 * 1. max subarray in middle: return maxTotal
	 * 2. max subarray take a part of head and a part of tail: return sum-minTotal
	 *    --> this is only possible for at least one negative number in array!
	 *    --> 2nd case is derived from the fact, that minTotal array can be excluded 
	 *        by wraparound, since it is contiguous (it measures the maximum negative 
	 *        contiguous contribution/share)
	 * 
	 * There is a special case, if sum == minTotal: min array spans whole array! As we 
	 * can not exclude whole array, we just return maxTotal (only relevant in case 
	 * maxTotal < 0, which means all numbers negative, which means maxTotal = max 
	 * element, as otherwise maxTotal >= sum - minTotal = 0 )
	 * 
	 * 1. Find maximum subarray sum using kadane's algorithm (max) 
	 * 2. Find minimum subarray sum using kadane's algorithm (min)
	 * 3. Find total sum of the array (sum)
	 * 4. Now, if sum == min return max
	 * 5. Otherwise, return maximum ( max, sum - min )
	 * 
	 * Rf :
	 * https://leetcode.com/problems/maximum-sum-circular-subarray/discuss/178422/One-Pass
	 * https://leetcode.com/problems/maximum-sum-circular-subarray/discuss/633058/Java-or-C++-or-Python3-or-With-detailed-explanation-or-O(N)-time-or-O(1)/541208
	 * 
	 * Other code:
	 * https://leetcode.com/problems/maximum-sum-circular-subarray/discuss/178444/JAVA-ONE-PASS-O(n)-with-very-detailed-chinese-explanation
	 * https://leetcode.com/problems/maximum-sum-circular-subarray/discuss/634506/Java-easy-solution-with-explanation
	 */
	public int maxSubarraySumCircular_min(int[] A) {
		if (A.length == 0)
			return 0;
		
		int sum = A[0];
		int maxSoFar = A[0];
		int maxTotal = A[0];
		int minTotal = A[0];
		int minSoFar = A[0];
		
		for (int i = 1; i < A.length; i++) {
			int num = A[i];
			maxSoFar = Math.max(num, maxSoFar + num);
			maxTotal = Math.max(maxSoFar, maxTotal);

			minSoFar = Math.min(num, minSoFar + num);
			minTotal = Math.min(minTotal, minSoFar);

			sum += num;
		}
		
		if (sum == minTotal)
			return maxTotal;
		return Math.max(sum - minTotal, maxTotal);
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/maximum-sum-circular-subarray/discuss/633401/Kadane-Algorithm-Trick-beats-100-Java-Explained
	 * 
	 * The maximum subarray sum in circular array is maximum of following
	 * 1. Maximum subarray sum in non circular array
	 * 2. the sum of all elements except for one continuous subarray (this selected 
	 *    (not removed) array has to include the first and the last elements of the 
	 *    array. Arrays that don't are contained in case 1).
	 * 
	 * Case-1 is the standard maximum subarray sum solution.
	 * Case-2 can be computed by finding and removing the minimum subarray sum 
	 * 
	 * The max subarray sum for circular array = Total Sum - Minimum subarray Sum
	 * That is the same as the array with the maximum subarray sum when all numbers 
	 * are multiplied by -1. Then add it with the total sum.
	 * 
	 * If all the array elements are negative, circular sum is equal to 0. It means 
	 * that it did not include any element in max subarray.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/maximum-sum-circular-subarray/discuss/633401/Kadane-Algorithm-Trick-beats-100-Java-Explained/541467
	 * https://leetcode.com/problems/maximum-sum-circular-subarray/discuss/633401/Kadane-Algorithm-Trick-beats-100-Java-Explained/541851
	 * https://leetcode.com/problems/maximum-sum-circular-subarray/discuss/392225/Simplest-solution
	 */
	public int maxSubarraySumCircular_invert_sign(int[] A) {
		int nonCircularSum = kadaneMaxSum_invert_sign(A);
		
		int totalSum = 0;
		for (int i = 0; i < A.length; i++) {
			totalSum += A[i];
			A[i] = -A[i];
		}

		int circularSum = totalSum + kadaneMaxSum_invert_sign(A);
		
		if (circularSum == 0)   // if all the array elements are negative
			return nonCircularSum;
		else
			return Math.max(circularSum, nonCircularSum);
	}

	int kadaneMaxSum_invert_sign(int[] A) {
		int currentSum = A[0];
		int maxSum = A[0];
		for (int i = 1; i < A.length; i++) {
			if (currentSum < 0)
				currentSum = 0;
			
			currentSum = A[i] + currentSum;
			maxSum = Math.max(maxSum, currentSum);
		}
		return maxSum;
	}
	
	/*
	 * The following 2 functions are from this link. 
	 * Approach 3: Kadane's (Sign Variant)
	 * https://leetcode.com/articles/maximum-sub-circular-subarray/
	 * 
	 * Subarrays of circular arrays can be classified as either as one-interval 
	 * subarrays, or two-interval subarrays.
	 * 
	 * Using Kadane's algorithm for finding the maximum sum of non-empty subarrays, 
	 * the answer for one-interval subarrays is kadane(A).
	 * 
	 * N = A.length. For a two-interval subarray like:
	 * (A_0 + A_1 + ... + A_i) + (A_j + A_j+1 + ... + A_N-1)
	 * = (A_0 + A_1 + ... + A_N-1) - (A_i+1 + A_i+2 + ... + A_j-1)
	 * 
	 * For two-interval subarrays, let B be the array A with each element multiplied 
	 * by -1. Then the answer for two-interval subarrays is sum(A) + kadane(B).
	 * 
	 * Except, this isn't quite true, as if the subarray of B we choose is the entire 
	 * array, the resulting two interval subarray [0, i] + [j, N-1] would be empty.
	 * 
	 * We can remedy this problem by doing Kadane twice: once on B with the first 
	 * element removed, and once on B with the last element removed.
	 */
	public int maxSubarraySumCircular_Sign_Variant(int[] A) {
		if (A.length == 1) {
			return A[0];
		}
		
		int S = 0; // S = sum(A)
		for (int x : A)
			S += x;

		int ans1 = kadane_Sign_Variant(A, 0, A.length - 1, 1);
		int ans2 = S + kadane_Sign_Variant(A, 1, A.length - 1, -1);
		int ans3 = S + kadane_Sign_Variant(A, 0, A.length - 2, -1);
		return Math.max(ans1, Math.max(ans2, ans3));
	}

    public int kadane_Sign_Variant(int[] A, int i, int j, int sign) {
        // The maximum non-empty subarray for array
        // [sign * A[i], sign * A[i+1], ..., sign * A[j]]
		int ans = Integer.MIN_VALUE;
		int cur = Integer.MIN_VALUE;
		for (int k = i; k <= j; ++k) {
			cur = sign * A[k] + Math.max(cur, 0);
			ans = Math.max(ans, cur);
		}
		return ans;
	}
	
	/*
	 * Approach 1: Next Array
	 * https://leetcode.com/articles/maximum-sub-circular-subarray/
	 * 
	 * Subarrays of circular arrays can be classified as either as one-interval 
	 * subarrays, or two-interval subarrays
	 * 
	 * For example, if A = [0, 1, 2, 3, 4, 5, 6] is the underlying buffer of our 
	 * circular array, we could represent the subarray [2,3,4] as one interval [2,4], 
	 * but we would represent the subarray [5,6,0,1] as two intervals [5,6], [0,1].
	 * 
	 * Using Kadane's algorithm, we know how to get the maximum of one-interval 
	 * subarrays, so it only remains to consider two-interval subarrays.
	 * 
	 * Let's say the intervals are [0,i], [j, A.length - 1]. Let's try to compute the 
	 * i-th candidate: the largest possible sum of a two-interval subarray for a given 
	 * i. Computing the [0, i] part of the sum is easy.
	 * 
	 * T_j = A[j] + A[j+1] + ... + A[A.length-1] and R_j = max_{k >= j} T_k
	 * 
	 * so that the desired i-th candidate is:
	 * (A[0] + A[1] + ... + A[i]) + R_i+2
	 * 
	 * Time Complexity: O(N), Space Complexity: O(N), where N is the length of A.
	 */
	public int maxSubarraySumCircular_next_array(int[] A) {
        int N = A.length;

        int ans = A[0], cur = A[0];
        for (int i = 1; i < N; ++i) {
            cur = A[i] + Math.max(cur, 0);
            ans = Math.max(ans, cur);
        }

        // ans is the answer for 1-interval subarrays.
        // Now, let's consider all 2-interval subarrays.
        // For each i, we want to know
        // the maximum of sum(A[j:]) with j >= i+2

        // rightsums[i] = A[i] + A[i+1] + ... + A[N-1]
		int[] rightsums = new int[N];
		rightsums[N - 1] = A[N - 1];
		for (int i = N - 2; i >= 0; --i)
			rightsums[i] = rightsums[i + 1] + A[i];

        // maxright[i] = max_{j >= i} rightsums[j]
		int[] maxright = new int[N];
		maxright[N - 1] = A[N - 1];
		for (int i = N - 2; i >= 0; --i)
			maxright[i] = Math.max(maxright[i + 1], rightsums[i]);

		int leftsum = 0;
		for (int i = 0; i < N - 2; ++i) {
			leftsum += A[i];
			ans = Math.max(ans, leftsum + maxright[i + 2]);
		}

        return ans;
    }
	
	/*
	 * Approach 2: Prefix Sums + Monoqueue
	 * https://leetcode.com/articles/maximum-sub-circular-subarray/
	 * 
	 * Use a mono-increasing queue to hold the minimum prefix-sum value. The first 
	 * element of the queue is the minimum pSum value from (i - n) to i
	 * 
	 * We can consider any subarray of the circular array with buffer A, to be a 
	 * subarray of the fixed array A+A.
	 * 
	 * For example, if A = [0,1,2,3,4,5] represents a circular array, then the 
	 * subarray [4,5,0,1] is also a subarray of fixed array [0,1,2,3,4,5,0,1,2,3,4,5]. 
	 * Let B = A+A be this fixed array.
	 * 
	 * Now say N = A.length, and consider the prefix sums
	 * P_k = B[0] + B[1] + ... + B[k - 1]
	 * 
	 * Then, we want the largest P_j - P_i where j - i <= N.
	 * 
	 * Now, consider the j-th candidate answer: the best possible P_j - P_i for a 
	 * fixed j. We want the i so that P_i is smallest, with j - N <= i < j. Let's call 
	 * this the optimal i for the j-th candidate answer. We can use a monoqueue to 
	 * manage this.
	 * 
	 * Iterate forwards through j, computing the j-th candidate answer at each step. 
	 * We'll maintain a queue of potentially optimal i's.
	 * 
	 * The main idea is that if i1 < i2 and P_i1 >= P_i2, then we don't need to 
	 * remember i1 
	 * 
	 * Time Complexity: O(N), Space Complexity: O(N), where N is the length of A.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/maximum-sum-circular-subarray/discuss/189299/Java-2-approaches
	 * 
	 * Other code:
	 * https://leetcode.com/problems/maximum-sum-circular-subarray/discuss/192796/Java-O(N)-Sliding-Window
	 */
	public int maxSubarraySumCircular_increasing_queue(int[] A) {
		int N = A.length;

        // Compute P[j] = B[0] + B[1] + ... + B[j-1]
        // for fixed array B = A+A
		int[] P = new int[2 * N + 1];
		for (int i = 0; i < 2 * N; ++i)
			P[i + 1] = P[i] + A[i % N];

        // Want largest P[j] - P[i] with 1 <= j-i <= N
        // For each j, want smallest P[i] with i >= j-N
        int ans = A[0];
        
        // deque: i's, increasing by P[i]
		Deque<Integer> deque = new ArrayDeque<>();
		deque.offer(0);

		for (int j = 1; j <= 2 * N; ++j) {
			// If the smallest i is too small, remove it.
			if (deque.peekFirst() < j - N)
				deque.pollFirst();

            // The optimal i is deque[0], for cand. answer P[j] - P[i].
			ans = Math.max(ans, P[j] - P[deque.peekFirst()]);

            // Remove any i1's with P[i2] <= P[i1].
			while (!deque.isEmpty() && P[j] <= P[deque.peekLast()])
				deque.pollLast();

			deque.offerLast(j);
		}

		return ans;
	}
	
	// by myself 2
	public int maxSubarraySumCircular_self2(int[] A) {
        int max = A[0];
        int sum = A[0];
        int total = A[0];
        for (int i = 1; i < A.length; i++) {
            total += A[i];
            if (sum > 0) {
                sum += A[i];
            }
            else {
                sum = A[i];
            }
            
            max = Math.max(max, sum);
        }
        
        sum = A[0];
        int min = A[0];
        for (int i = 1; i < A.length; i++) {
            if (sum < 0) {
                sum += A[i];
            }
            else {
                sum = A[i];
            }
            
            min = Math.min(min, sum);
        }
        
        int circle = total - min;
        if (total == min) {
            return max;
        }
        else {
            return Math.max(max, circle);
        }
    }
	
	// by myself
	public int maxSubarraySumCircular_self(int[] A) {
        int[] cut = new int[A.length * 2];
        for (int i = 0; i < A.length; i++) {
            cut[i] = A[i];
            cut[i + A.length] = A[i];
        }
        
        int max = cut[0];
        int sum = cut[0];
        int counter = 1;
        for (int i = 1; i < cut.length; i++) {
            if (sum >= 0) {
                sum += cut[i];
                counter++;
            }
            else {
                sum = cut[i];
                counter = 1;
            }
            
            // Minus minimum sum subarray that starts from the head of current subarray
            if (counter > A.length) {
                int pos = i - counter + 1;
                int min = cut[i - counter + 1];
                int tmp = min;
                
                for (int j = i - counter + 2; j < i; j++) {
                    tmp += cut[j];
                    
                    if (tmp <= min) {
                        min = tmp;
                        pos = j;
                    }
                }
                
                sum -= min;
                counter = i - pos;
            }
            
            if (sum > max) {
                max = sum;
            }
        }
        return max;
    }
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/maximum-sum-circular-subarray/discuss/633550/Python-one-pass-solution-easy-to-understand
     * https://leetcode.com/problems/maximum-sum-circular-subarray/discuss/635487/Python-Kadane's-solution
     * https://leetcode.com/problems/maximum-sum-circular-subarray/discuss/242169/Short-Python-DP-beats-93
     * https://leetcode.com/problems/maximum-sum-circular-subarray/discuss/178521/Python-solution-using-deque
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/maximum-sum-circular-subarray/discuss/178435/C%2B%2B-O(N)-solution
     * https://leetcode.com/problems/maximum-sum-circular-subarray/discuss/427127/Easy-Solution!
     * https://leetcode.com/problems/maximum-sum-circular-subarray/discuss/633621/C%2B%2B-O(N)-DP-solution
     */

}
