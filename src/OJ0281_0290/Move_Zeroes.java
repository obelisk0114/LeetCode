package OJ0281_0290;

public class Move_Zeroes {
	/*
	 * https://leetcode.com/problems/move-zeroes/discuss/72000/1ms-Java-solution
	 * 
	 * All elements before the slow pointer are non-zeroes.
	 * All elements between the current and slow pointer are zeroes.
	 * 
	 * Rf : https://leetcode.com/articles/move-zeroes/
	 * 
	 * Other code :
	 * https://leetcode.com/problems/move-zeroes/discuss/72419/Java-Short-and-Swappy-1ms/74867
	 */
	public void moveZeroes(int[] nums) {
		int j = 0;
		for (int i = 0; i < nums.length; i++) {
			if (nums[i] != 0) {
				int temp = nums[j];
				nums[j] = nums[i];
				nums[i] = temp;
				j++;
			}
		}
	}
	
	/*
	 * https://leetcode.com/problems/move-zeroes/discuss/172432/THE-EASIEST-but-UNUSUAL-snowball-JAVA-solution-BEATS-100-(O(n))-%2B-clear-explanation
	 * 
	 * go through the array and gather all zeros on our road.
	 * In ELSE block we just swap i and i-snowBallSize (which is presumably 0) elements.
	 */
	public void moveZeroes_gather(int[] nums) {
		int snowBallSize = 0;
		for (int i = 0; i < nums.length; i++) {
			if (nums[i] == 0) {
				snowBallSize++;
            }
            else if (snowBallSize > 0) {
				int t = nums[i];
				nums[i] = nums[i - snowBallSize];
				nums[i - snowBallSize] = t;
			}
        }
    }
	
	/*
	 * https://leetcode.com/problems/move-zeroes/discuss/72011/Simple-O(N)-Java-Solution-Using-Insert-Index
	 * 
	 * Shift non-zero values as far forward as possible
	 * Fill remaining space with zeros
	 * 
	 * Other code :
	 * https://leetcode.com/problems/move-zeroes/discuss/72295/Java-solution-in-0ms-beat-s87.69
	 */
	public void moveZeroes_fill_zero(int[] nums) {
		if (nums == null || nums.length == 0)
			return;

		int insertPos = 0;     // Index of none-zero number 
		for (int num : nums) {
			if (num != 0)
				nums[insertPos++] = num;
		}

		while (insertPos < nums.length) {
			nums[insertPos++] = 0;
		}
	}
	
	// by myself
	public void moveZeroes_self(int[] nums) {
        int left = 0;
        int right = 0;
        while (right < nums.length) {
            if (nums[left] == 0) {
                while (right < nums.length - 1 && nums[right] == 0) {
                    right++;
                }
                nums[left] = nums[right];
                nums[right] = 0;
                left++;
                right++;
            }
            else {
                left++;
                right++;
            }
        }
    }
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/move-zeroes/discuss/72099/Very-simple-python-solutions
     * https://leetcode.com/problems/move-zeroes/discuss/72012/Python-short-in-place-solution-with-comments.
     * https://leetcode.com/problems/move-zeroes/discuss/72074/Share-my-one-line-python-solution
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/move-zeroes/discuss/72045/C%2B%2B-Accepted-Code
     * https://leetcode.com/problems/move-zeroes/discuss/72005/My-simple-C%2B%2B-solution
     * https://leetcode.com/problems/move-zeroes/discuss/72132/One-line-c%2B%2B-code-20ms
     */

}
