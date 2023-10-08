package OJ0321_0330;

/*
 * https://discuss.leetcode.com/topic/33536/a-summary-of-all-solutions-new-method-included-at-15-30pm-jan-8th
 * https://stackoverflow.com/questions/1804311/how-to-check-if-an-integer-is-a-power-of-3
 */

public class Power_of_Three {
	public boolean isPowerOfThree_log_max(int n) {
		/*
		 * The expression 
		 * "(int) Math.pow(3, (int) (Math.log(Integer.MAX_VALUE) / Math.log(3.0))" 
		 * returns max integer that is "power of 3"
		 * 
		 * https://discuss.leetcode.com/topic/44183/java-one-line-solution
		 */
		return n > 0 
				&& (int) Math.pow(3, 
						(int) (Math.log(Integer.MAX_VALUE) / Math.log(3.0))) % n == 0;
	}
	
	/*
	 * https://leetcode.com/problems/power-of-three/solution/
	 * Approach 1: Loop Iteration
	 */
	public boolean isPowerOfThree_loop(int n) {
        if (n < 1) {
            return false;
        }

        while (n % 3 == 0) {
            n /= 3;
        }

        return n == 1;
    }
	
	// by myself
	public boolean isPowerOfThree_self(int n) {
        long x = 1;
        while (x < n) {
            x *= 3;
        }
        
        return x == n;
    }
	
	/*
	 * https://discuss.leetcode.com/topic/36150/1-line-java-solution-without-loop-recursion
	 * 
	 * Rf :
	 * https://leetcode.com/problems/power-of-three/solution/
	 * Approach 4: Integer Limitations
	 */
	public boolean isPowerOfThree2(int n) {
		// 1162261467 is 3^19, 3^20 is bigger than int
		return (n > 0 && 1162261467 % n == 0);
	}
	
	/*
	 * https://discuss.leetcode.com/topic/33595/ternary-number-solution/2
	 * 
	 * Rf :
	 * https://leetcode.com/problems/power-of-three/solution/
	 * Approach 2: Base Conversion
	 */
	public boolean isPowerOfThree_regex(int n) {
	    return Integer.toString(n, 3).matches("10*");
	}
	
	/*
	 * https://discuss.leetcode.com/topic/33949/simple-java-solution-without-recursion-iteration
	 * 
	 * Rf : https://discuss.leetcode.com/topic/35877/java-easy-version-to-understand
	 */
	public boolean isPowerOfThree_log(int n) {
        double a = Math.log(n) / Math.log(3);
        return Math.abs(a - Math.rint(a)) <= 0.00000000000001;
    }
	
	/*
	 * https://leetcode.com/problems/power-of-three/solution/
	 * Approach 3: Mathematics
	 * 
	 * 換底公式 + 精度修正
	 * test case: 14348906
	 */
	public boolean isPowerOfThree_log_modify(int n) {
        double epsilon = 1E-14;
        
        return (Math.log(n) / Math.log(3) + epsilon) % 1 <= 2 * epsilon;
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Power_of_Three power3 = new Power_of_Three(); 
		int a = 19683;
		System.out.println(power3.isPowerOfThree_log_max(a));
		System.out.println(power3.isPowerOfThree2(a));
		System.out.println(power3.isPowerOfThree_regex(a));
		System.out.println(power3.isPowerOfThree_log(a));

	}

}
