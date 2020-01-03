package OJ0021_0030;

public class Remove_Element {
	/*
	 * by myself
	 * 
	 * Rf :
	 * https://leetcode.com/articles/remove-element/
	 * https://leetcode.com/problems/remove-element/discuss/12677/Really-concise-one-pass-Java-code
	 * https://leetcode.com/problems/remove-element/discuss/12289/My-solution-for-your-reference.
	 */
	public int removeElement_self(int[] nums, int val) {
        if (nums == null || nums.length == 0)
            return 0;
        
        // Track the position to put the next "non-val" integer.
        int i = 0;
        for (int j = 0; j < nums.length; j++) {
            if (nums[j] != val) {
                nums[i] = nums[j];
                i++;
            }
        }
        return i;
    }
	
	/*
	 * https://leetcode.com/problems/remove-element/discuss/12289/My-solution-for-your-reference./12795
	 * 
	 * It scans numbers from the left to the right, one by one. Once it finds a number 
	 * that equals to val, it swaps current element with the last element in the array 
	 * and then dispose the last. The swapping can be optimized as overwrite current 
	 * element by the last one and dispose the last.
	 * 
	 * Now, we have removed the current number, and the length of the array is reduced 
	 * by 1. To ensure we do not make wrong choices, we will continue scanning from 
	 * the (new) current element. So it won't fail if the same number is swapped to 
	 * the current position.
	 * 
	 * Rf : https://leetcode.com/articles/remove-element/
	 * 
	 * Other code:
	 * https://leetcode.com/problems/remove-element/discuss/12545/Accepted-Java-Solution
	 */
	public int removeElement_2_pointer(int[] nums, int val) {
		int start = 0, end = nums.length - 1;
		while (start <= end) {
			if (nums[start] == val) {
				nums[start] = nums[end];
				end--;
			}
			else {
				start++;
			}
		}
		return start;
	}
	
	/*
	 * https://leetcode.com/problems/remove-element/discuss/12307/Beat-90-Fast-Java-Simple-Solution-with-Explanation
	 * 
	 * The basic idea is to use two pointers, one left and one right. Left pointer 
	 * starts from the beginning and right pointer starts from ending. While right 
	 * pointer is pointing to the target value, move it to left until it is not 
	 * pointing the target value or meets the left pointer. If the left pointer is 
	 * pointing to the target value, change the value to what the right pointer is 
	 * pointing, and move right pointer one step to left. Then move left pointer to 
	 * right. Repeat that process until the two pointers meet.
	 */
	public int removeElement_2_pointer2(int[] nums, int val) {
		if (nums.length < 1)
			return 0;

		int left = 0, right = nums.length - 1;
		while (left <= right) {
			while (right > left && nums[right] == val)
				right--;
			
			if (nums[left] == val) {
				nums[left] = nums[right];
				right--;
			}
			left++;
		}
		return right + 1;
	}
	
	
	/*
	 * https://leetcode.com/problems/remove-element/discuss/12282/Fast-and-Short-Java-Solution
	 * 
	 * When elem is found at index i, let A[i] = the last element in the modifying 
	 * array, then repeat searching until elem is not found.
	 * 
	 * Rf : https://leetcode.com/problems/remove-element/discuss/12405/9-line-java-solution
	 * 
	 * Other code:
	 * https://leetcode.com/problems/remove-element/discuss/12621/0ms-and-clean-Java-solution
	 */
	public int removeElement_2_pointer3(int[] A, int elem) {
		int l = A.length;
		for (int i = 0; i < l; i++) {
			if (A[i] == elem) {
				A[i--] = A[--l];
			}
		}
		return l;
	}
	
	// https://leetcode.com/problems/remove-element/discuss/12592/6lines-Java-solution
	public int removeElement2(int[] nums, int val) {
		int len = 0;
		for (int num : nums) {
			if (num != val) {
				nums[len] = num;
				len++;
			}
		}
		return len;
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/remove-element/discuss/12584/6-line-Python-solution-48-ms
     * https://leetcode.com/problems/remove-element/discuss/12306/Simple-Python-O(n)-two-pointer-in-place-solution
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/remove-element/discuss/12757/6-lines-of-c%2B%2B-solution
     * https://leetcode.com/problems/remove-element/discuss/12582/Share-my-3-lines-c%2B%2B-code
     * https://leetcode.com/problems/remove-element/discuss/12299/Very-simple-and-optimal-c%2B%2B-solution.
     */

}
