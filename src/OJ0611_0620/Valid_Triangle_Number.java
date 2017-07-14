package OJ0611_0620;

import java.util.Arrays;

public class Valid_Triangle_Number {
	/*
     * https://leetcode.com/articles/valid-triangle-number/
     * Approach #3 Linear Scan
     * We can find this right limit by simply traversing the index k's values starting 
     * from the index k = j + 1 for a pair (i,j) chosen and stopping at the first 
     * value of k not satisfying the above inequality.
     * Range (j+1, k-1) (both included). The count = (k-1)-(j+1)+1 = k-j-1
     * 
     * When we choose a higher value of index j for a particular i chosen, we need not 
     * start from the index j + 1. 
     * We can start off directly from the value of k where we left for the last index j
     */
    public int triangleNumber(int[] nums) {
        int count = 0;
        Arrays.sort(nums);
        for (int i = 0; i < nums.length - 2; i++) {
            int k = i + 2;
            for (int j = i + 1; j < nums.length - 1 && nums[i] != 0; j++) {
                while (k < nums.length && nums[i] + nums[j] > nums[k])
                    k++;
                count += k - j - 1;
            }
        }
        return count;
    }
    
	/*
	 * https://leetcode.com/articles/valid-triangle-number/
	 * Approach #2 Using Binary Search
	 * a <= b <= c; We only need to find a "c" such that a + b > c, and all the 
	 * elements between (b+1, c-1) satisfy the condition.
	 * We can start off from the k_i,j directly where we left off for the last j chosen
	 */
	int binarySearch(int nums[], int l, int r, int x) {
        while (r >= l && r < nums.length) {
            int mid = (l + r) / 2;
            if (nums[mid] >= x)
                r = mid - 1;
            else
                l = mid + 1;
        }
        return l;
    }
    public int triangleNumber_binarySearch(int[] nums) {
        int count = 0;
        Arrays.sort(nums);
        for (int i = 0; i < nums.length - 2; i++) {
            int k = i + 2;
            for (int j = i + 1; j < nums.length - 1 && nums[i] != 0; j++) {
                k = binarySearch(nums, k, nums.length - 1, nums[i] + nums[j]);
                count += k - j - 1;
            }
        }
        return count;
    }

}
