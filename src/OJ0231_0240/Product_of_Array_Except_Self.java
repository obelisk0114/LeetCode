package OJ0231_0240;

public class Product_of_Array_Except_Self {
	/*
	 * https://discuss.leetcode.com/topic/18864/simple-java-solution-in-o-n-without-extra-space
	 * 
	 * The product basically is calculated using the numbers before the current number
	 * and the numbers after the current number.
	 * 
	 * First, we calculate the running product of the part before the current number. 
	 * Second, we calculate the running product of the part after the current number 
	 *  through scanning from the end of the array.
	 *  
	 * Rf : https://discuss.leetcode.com/topic/22301/my-solution-beats-100-java-solutions
	 */
	public int[] productExceptSelf_twoPass(int[] nums) {
	    int n = nums.length;
	    int[] res = new int[n];
	    res[0] = 1;
	    for (int i = 1; i < n; i++) {
	        res[i] = res[i - 1] * nums[i - 1];
	    }
	    int right = 1;
	    for (int i = n - 1; i >= 0; i--) {
	        res[i] *= right;
	        right *= nums[i];
	    }
	    return res;
	}
	
	// https://discuss.leetcode.com/topic/41177/my-one-pass-java-solution-without-extra-spaces
	public int[] productExceptSelf_onePass(int[] nums) {
		int[] result = new int[nums.length];
		for (int i = 0; i < result.length; i++)
			result[i] = 1;
		int left = 1, right = 1;
		for (int i = 0, j = nums.length - 1; i < nums.length - 1; i++, j--) {
			left *= nums[i];
			right *= nums[j];
			result[i + 1] *= left;
			result[j - 1] *= right;
		}
		return result;
	}
	
	// Self
	public int[] productExceptSelf(int[] nums) {
        int total = 1;
        int zeroposition = -1;
        int[] out = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == 0) {
                if (zeroposition != -1)
                    return out;
                else {
                    zeroposition = i;
                    continue;
                }
            }
            total *= nums[i];
        }
        if (zeroposition != -1) {
            out[zeroposition] = total;
            return out;
        }
        for (int i = 0; i < nums.length; i++) {
            out[i] = total / nums[i];
        }
        return out;
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Product_of_Array_Except_Self ProductExceptSelf = new Product_of_Array_Except_Self();
		int[] a = {1, 0, 5, 8, 0};
		int[] b = ProductExceptSelf.productExceptSelf(a);
		for (int i = 0; i < b.length; i++) {
			System.out.print(b[i] + " ");
		}

	}

}
