package OJ0061_0070;

import java.util.Arrays;

public class Plus_One {
	/*
	 * https://discuss.leetcode.com/topic/24288/my-simple-java-solution
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/18570/simple-java-solution-no-copying-arrays
	 * https://discuss.leetcode.com/topic/30101/0ms-java-solution
	 */
	public int[] plusOne_onePass(int[] digits) {
		int n = digits.length;
		for (int i = n - 1; i >= 0; i--) {
			if (digits[i] < 9) {
				digits[i]++;
				return digits;
			}

			digits[i] = 0;
		}

		int[] newNumber = new int[n + 1];
		newNumber[0] = 1;
		return newNumber;
	}
	
	public int[] plusOne(int[] digits) {
        if (digits.length == 1 && digits[0] == 0)
            return new int[] {1};
        int k = digits.length - 1;
        while (k >= 0 && digits[k] == 9) {
            k--;
        }
        if (k < 0) {
            int[] res = new int[digits.length + 1];
            res[0] = 1;
            for (int i = 1; i < digits.length + 1; i++) {
                res[i] = 0;
            }
            return res;
        }
        int[] res = Arrays.copyOf(digits, digits.length);
        res[k] = digits[k] + 1;
        for (int i = k+1; i < digits.length; i++) {
            res[i] = 0;
        }
        return res;
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Plus_One plusone = new Plus_One();
		int[] a = {1, 2, 3, 5, 6, 9, 9};
		int[] b = plusone.plusOne(a);
		for (int i = 0; i < b.length; i++) {
			System.out.print(b[i] + " ");
		}

	}

}
