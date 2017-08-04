package OJ0071_0080;

public class Sort_Colors {
	// https://en.wikipedia.org/wiki/Dutch_national_flag_problem#Pseudocode
	public void sortColors(int[] nums) {
        int i = 0;
        int j = 0;
        int k = nums.length - 1;
        while (i <= k) {
            if (nums[i] < 1) {
                swap(nums, i, j);
                i++;
                j++;
            }
            else if (nums[i] > 1) {
                swap(nums, i, k);
                k--;
            }
            else {
                i++;
            }
        }
    }
	
	/*
	 * https://discuss.leetcode.com/topic/6968/four-different-solutions
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/10495/share-one-pass-java-solution
	 * https://discuss.leetcode.com/topic/5422/share-my-at-most-two-pass-constant-space-10-line-solution
	 */
	void sortColors2(int[] A) {
		int n0 = -1, n1 = -1, n2 = -1;
		for (int i = 0; i < A.length; ++i) {
			if (A[i] == 0) {
				A[++n2] = 2;
				A[++n1] = 1;
				A[++n0] = 0;
			} 
			else if (A[i] == 1) {
				A[++n2] = 2;
				A[++n1] = 1;
			} 
			else if (A[i] == 2) {
				A[++n2] = 2;
			}
		}
	}
	
	// https://discuss.leetcode.com/topic/6968/four-different-solutions
	void sortColors_2Pass(int[] A) {
		int num0 = 0, num1 = 0, num2 = 0;

		for (int i = 0; i < A.length; i++) {
			if (A[i] == 0)
				++num0;
			else if (A[i] == 1)
				++num1;
			else if (A[i] == 2)
				++num2;
		}

		for (int i = 0; i < num0; ++i)
			A[i] = 0;
		for (int i = 0; i < num1; ++i)
			A[num0 + i] = 1;
		for (int i = 0; i < num2; ++i)
			A[num0 + num1 + i] = 2;
	}
	
	// https://discuss.leetcode.com/topic/66075/my-template-for-this-kind-of-problem
	public void sortColors_array(int[] nums) {
		int[] cand = { 0, 1, 2 };
		int start = 0;
		for (int i = 0; i < 3; i++) {
			while (start < nums.length && nums[start] == cand[i])
				start++;
			for (int j = start; j < nums.length; j++) {
				if (nums[j] == cand[i]) {
					swap(nums, j, start++);
				}
			}
		}
		return;
	}
    
    private void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

}
