package OJ0341_0350;

/*
 * https://discuss.leetcode.com/topic/42960/one-of-my-favorite-tricks
 */

public class Power_of_Four {
	/*
	 * https://discuss.leetcode.com/topic/42860/java-1-line-cheating-for-the-purpose-of-not-using-loops
	 * 0x5 = 0101
	 * 0x55555555 is to get rid of those power of 2 but not power of 4
	 * so that the single 1 bit always appears at the odd position
	 * 
	 * First, greater than 0. 
	 * Second, only have one '1' bit in their binary notation,
	 * so we use x&(x-1) to delete the lowest '1', and if then it becomes 0, 
	 * it prove that there is only one '1' bit. 
	 * Third, the only '1' bit should be locate at the odd location,
	 * for example, 16. It's binary is 00010000. 
	 * So we can use '0x55555555' to check if the '1' bit is in the right place.
	 * 
	 * Rf : https://discuss.leetcode.com/topic/42855/o-1-one-line-solution-without-loops
	 */
	public boolean isPowerOfFour(int num) {
        return (num > 0) && ((num & (num - 1)) == 0) && ((num & 0x55555555) == num);
    }
	
	// https://discuss.leetcode.com/topic/53621/java-one-line-without-loops-by-regular-expression
	public boolean isPowerOfFour_regex(int num) {
		return Integer.toBinaryString(num).matches("1(00)*");
	}
	
	/*
	 * https://discuss.leetcode.com/topic/42857/java-one-line-solution-using-bitcount-numberoftrailingzeros
	 * 
	 * Rf : 
	 * https://docs.oracle.com/javase/7/docs/api/java/lang/Integer.html#bitCount(int)
	 * https://docs.oracle.com/javase/7/docs/api/java/lang/Integer.html#numberOfTrailingZeros(int)
	 */
	public boolean isPowerOfFour_bitCount_trailing(int num) {
		return num >= 1 && Integer.bitCount(num) == 1 && Integer.numberOfTrailingZeros(num) % 2 == 0;
	}

	// by myself
	public boolean isPowerOfFour_self(int n) {
        if (n == 1) {
            return true;
        }
        if (n < 1) {
            return false;
        }

        int idx = 0;
        while (n != 1) {
            if ((n & 1) == 1) {
                return false;
            }

            n >>= 1;
            idx++;
        }

        if ((idx & 1) == 0) {
            return true;
        }
        else {
            return false;
        }
    }
	
	/*
	 * The first two conditions are for power of 2. 
	 * One additional condition: (num-1) can be divided by 3.
	 * 
	 * 4^n - 1 = 4^n - 1^n = (4-1)(4^(n-1) + 4^(n-2) + ...+ 4 + 1)
	 * 
	 * https://discuss.leetcode.com/topic/43240/one-line-in-java-without-loops-recursion-which-is-extensible
	 */
	public boolean isPowerOfFour_math(int num) {
		return (num & (num - 1)) == 0 && num > 0 && (num - 1) % 3 == 0;
	}
	
	// https://discuss.leetcode.com/topic/42858/plain-and-easy-understanding-with-loops-to-count-trailing-zeros-java
	public boolean isPowerOfFour_loopTrailingZero(int num) {
        if (num <= 0) return false;
        int count = 0;
        int temp = num;
        while (temp > 0) {
            if ((temp & 1) == 0) {
                count++;
            } else {
                break;
            }
            temp = temp >> 1;
        }
        return (count % 2 == 0) && ((num & (num - 1)) == 0);
    }
	
	/*
	 * https://discuss.leetcode.com/topic/42883/java-1-line-of-code-and-can-be-extended-to-any-radix-solution
	 * 
	 * Rf : https://docs.oracle.com/javase/7/docs/api/java/lang/Integer.html#toString(int,%20int)
	 */
	public boolean isPowerOfFour_toStringRadix(int num) {
	    return Integer.toString(num, 4).matches("10*");
	}
	
	// Self
	public boolean isPowerOfFour2(int num) {
        if (num == 0)
            return false;
        if (num == 1)
            return true;
        if ((num & (num - 1)) != 0)
            return false;
        String s = Integer.toBinaryString(num);
        if ((s.length()-1) % 2 == 0)
            return true;
        else
            return false;
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Power_of_Four power4 = new Power_of_Four();
		//int a = 2048;
		int a = 4096;
		System.out.println(power4.isPowerOfFour(a));
		System.out.println(power4.isPowerOfFour_regex(a));
		System.out.println(power4.isPowerOfFour_toStringRadix(a));
		System.out.println(power4.isPowerOfFour2(a));

	}

}
