package OJ0621_0630;

import java.util.Arrays;

public class Maximum_Product_of_Three_Numbers {
	/*
	 * https://discuss.leetcode.com/topic/93690/java-easy-ac
	 * https://leetcode.com/articles/maximmum-product-of-three-numbers/
	 * 
	 * +, +, + (largest 3) OR -, -, + (smallest 2 and largest 1)
	 */
	public int maximumProduct(int[] nums) {
		Arrays.sort(nums);
		// One of the Three Numbers is the maximum value in the array.
		int a = nums[nums.length - 1] * nums[nums.length - 2] * nums[nums.length - 3];
		int b = nums[0] * nums[1] * nums[nums.length - 1];     // For negative numbers
		return a > b ? a : b;
	}
	
	// https://discuss.leetcode.com/topic/93804/java-o-1-space-o-n-time-solution-beat-100
	public int maximumProduct_OnePass(int[] nums) {
        int min1 = Integer.MAX_VALUE, min2 = Integer.MAX_VALUE;
        int max1 = Integer.MIN_VALUE, max2 = Integer.MIN_VALUE, max3 = Integer.MIN_VALUE;
        for (int n: nums) {
            if (n <= min1) {
                min2 = min1;
                min1 = n;
            } else if (n <= min2) {     // n lies between min1 and min2
                min2 = n;
            }
            if (n >= max1) {            // n is greater than max1, max2 and max3
                max3 = max2;
                max2 = max1;
                max1 = n;
            } else if (n >= max2) {     // n lies betweeen max1 and max2
                max3 = max2;
                max2 = n;
            } else if (n >= max3) {     // n lies betwen max2 and max3
                max3 = n;
            }
        }
        return Math.max(min1 * min2 * max1, max1 * max2 * max3);
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Maximum_Product_of_Three_Numbers maximum = new Maximum_Product_of_Three_Numbers();
		int[] a = {-100, -200, -300, 5, 6, 1000};
		System.out.println(maximum.maximumProduct(a));
		System.out.println(maximum.maximumProduct_OnePass(a));

	}

}
