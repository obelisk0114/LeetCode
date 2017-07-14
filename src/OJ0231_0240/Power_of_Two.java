package OJ0231_0240;

public class Power_of_Two {
	/*
	 * https://discuss.leetcode.com/topic/18365/one-line-java-solution
	 * 
	 * Rf : https://discuss.leetcode.com/topic/17870/one-line-java-solution-using-n-n-1
	 */
	public boolean isPowerOfTwo_bit(int n) {
		return ((n & (n - 1)) == 0 && n > 0);
	}
	
	// https://discuss.leetcode.com/topic/18522/one-line-java-solution-using-bitcount
	public boolean isPowerOfTwo(int n) {
		return n > 0 && Integer.bitCount(n) == 1;
    }
	
	// Self
	public boolean isPowerOfTwo_iterative2(int n) {
        if (n < 1) {
            return false;
        }
        while (n > 1) {
            if (n % 2 == 1)
                return false;
            n /= 2;
        }
        return true;
    }
	
	// https://discuss.leetcode.com/topic/18245/possible-solutions
	public boolean isPowerOfTwo_shiftCount(int n) {
		int count = 0;
		for (int ind = 0; ind < 32; ind++) {
			if ((n & (1 << ind)) != 0) {
				count++;
			}
		}
		return n > 0 && count == 1;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/47195/4-different-ways-to-solve-iterative-recursive-bit-operation-math
	 * 
	 * range of an integer = -2147483648 (-2^31) ~ 2147483647 (2^31-1)
	 * If n is the power of two, let n = 2^k, where k is an integer.
	 */
	public boolean isPowerOfTwo_math(int n) {
		return n > 0 && (1073741824 % n == 0);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Power_of_Two power2 = new Power_of_Two();
		int a = 4098;
		System.out.println(power2.isPowerOfTwo_bit(a));
		System.out.println(power2.isPowerOfTwo(a));
		System.out.println(power2.isPowerOfTwo_iterative2(a));
		System.out.println(power2.isPowerOfTwo_shiftCount(a));
		System.out.println(power2.isPowerOfTwo_math(a));

	}

}
