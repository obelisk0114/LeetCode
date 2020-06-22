package OJ0041_0050;

/*
 * https://discuss.leetcode.com/topic/315/why-most-people-solve-this-problem-under-the-assumption-a-i-n/5
 * https://discuss.leetcode.com/topic/3300/as-o-n-solution-and-o-1-space-is-must-only-way-is-to-sort-array-in-o-n-time-with-optimal-space-and-time-overheads
 */

public class First_Missing_Positive {
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/25441/beat-100-fast-elegant-java-index-based-solution-with-explanation
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/10351/o-1-space-java-solution
	 * https://discuss.leetcode.com/topic/1361/a-very-nice-solution-from-ants-aasma-stackoverflow
	 * https://discuss.leetcode.com/topic/6704/concise-o-n-solution
	 */
	public int firstMissingPositive(int[] nums) {

		int i = 0, n = nums.length;
		while (i < n) {
			// If the current value is in the range of (0,length) and it's not
			// at its correct position,
			// swap it to its correct position.
			// Else just continue;
			if (nums[i] >= 0 && nums[i] < n && nums[nums[i]] != nums[i])
				swap(nums, i, nums[i]);
			else
				i++;
		}
		int k = 1;

		// Check from k=1 to see whether each index and value can be corresponding.
		while (k < n && nums[k] == k)
			k++;

		// If it breaks because of empty array or reaching the end. K must be
		// the first missing number.
		if (n == 0 || k < n)
			return k;
		else // If k is hiding at position 0, K+1 is the number.
			return nums[0] == k ? k + 1 : k;   // 1, 2, 3, 4, ..., n => return k + 1

	}
	private void swap(int[] nums, int i, int j) {
		int temp = nums[i];
		nums[i] = nums[j];
		nums[j] = temp;
	}
	
	/*
	 * The following 3 functions are from this link.
	 * https://discuss.leetcode.com/topic/2633/share-my-o-n-time-o-1-space-solution
	 * 
	 * for any k positive numbers (duplicates allowed), 
	 * the first missing positive number must be within [1,k+1]
	 * 
	 * 1. Using partition technique to put all positive numbers together in one side.
	 * 2. All the positive numbers lying within A[0,k-1]. A[i] are all positive numbers,
	 *  so I can set them to negative to indicate the existence of (i+1) 
	 *  and I can still use abs(A[i]) to get the original information A[i] holds.
	 *  
	 *  Rf : https://discuss.leetcode.com/topic/46687/java-simple-solution-with-documentation
	 */
	public int firstMissingPositive_move(int[] A) {
		int n = A.length;
		if (n == 0)
			return 1;
		
		int k = partition(A) + 1;
		int temp = 0;
		int first_missing_Index = k;
		for (int i = 0; i < k; i++) {
			temp = Math.abs(A[i]);
			if (temp <= k)
				A[temp - 1] = (A[temp - 1] < 0) ? A[temp - 1] : -A[temp - 1];
		}
		
		for (int i = 0; i < k; i++) {
			if (A[i] > 0) {
				first_missing_Index = i;
				break;
			}
		}
		return first_missing_Index + 1;
	}
	public int partition(int[] A) {           // used in quick sort
		int n = A.length;
		int q = -1;
		for (int i = 0; i < n; i++) {
			if (A[i] > 0) {
				q++;
				swap1(A, q, i);
			}
		}
		return q;
	}
	public void swap1(int[] A, int i, int j) {
		if (i != j) {
			A[i] ^= A[j];
			A[j] ^= A[i];
			A[i] ^= A[j];
		}
	}
	
	/*
	 * https://discuss.leetcode.com/topic/38570/java-accepted-no-swap-just-use-push
	 * 
	 * keep pushing number A into its right place, 
	 * and push out the existing number B from this place 
	 * and continue push number B into its right place again.
	 */
	public int firstMissingPositive_push(int[] nums) {
        // nums[i] -> i+1
        int next;
        for (int i = 0 ; i < nums.length; i++) {
            int curr = nums[i];
            if (curr > 0 && curr != i+1 && curr <= nums.length) {
                do {
                    next = nums[curr-1];
                    nums[curr-1] = curr;
                    curr = next;
                } while (curr > 0 && curr <= nums.length && nums[curr-1] != curr);
            }
        }
        int j;
        for (j = 0; j < nums.length; j++) {
            if (nums[j] != j+1)
                break;
        }
        return j+1;
    }
	
	// https://discuss.leetcode.com/topic/41172/java-solution-with-integers-encode-trick-explained

}
