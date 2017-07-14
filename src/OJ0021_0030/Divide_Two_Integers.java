package OJ0021_0030;

/*
 * Math.abs(-2147483648) will return -2147483648, 
 * but Math.abs((long)-2147483648) works fine.
 * 
 */

public class Divide_Two_Integers {
	/*
	 * https://discuss.leetcode.com/topic/45980/very-detailed-step-by-step-explanation-java-solution
	 * 
	 * If it is negative, then we apply negation ~result + 1 (two's complement)
	 */
	public int divide(int dividend, int divisor) {
	    boolean isNegative = (dividend < 0 && divisor > 0) || (dividend > 0 && divisor < 0) ? true : false;
	    long absDividend = Math.abs((long) dividend);
	    long absDivisor = Math.abs((long) divisor);
	    long result = 0;
	    while(absDividend >= absDivisor){
	        long tmp = absDivisor, count = 1;
	        while(tmp <= absDividend){
	            tmp <<= 1;
	            count <<= 1;
	        }
	        result += count >> 1;
	        absDividend -= tmp >> 1;
	    }
	    return  isNegative ? (int) ~result + 1 : result > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) result;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/23968/clean-java-solution-with-some-comment
	 * 
	 * Rf : https://discuss.leetcode.com/topic/8271/accepted-java-solution-with-comments
	 */
	public int divide_2(int dividend, int divisor) {
		//Reduce the problem to positive long integer to make it easier.
		//Use long to avoid integer overflow cases.
		int sign = 1;
		if ((dividend > 0 && divisor < 0) || (dividend < 0 && divisor > 0))
			sign = -1;
		long ldividend = Math.abs((long) dividend);
		long ldivisor = Math.abs((long) divisor);
		
		//Take care the edge cases.
		if (ldivisor == 0) return Integer.MAX_VALUE;
		if ((ldividend == 0) || (ldividend < ldivisor))	return 0;
		
		long lans = ldivide(ldividend, ldivisor);
		
		int ans;
		if (lans > Integer.MAX_VALUE){ //Handle overflow.
			ans = (sign == 1)? Integer.MAX_VALUE : Integer.MIN_VALUE;
		} else {
			ans = (int) (sign * lans);
		}
		return ans;
	}
	private long ldivide(long ldividend, long ldivisor) {
		// Recursion exit condition
		if (ldividend < ldivisor) return 0;
		
		//  Find the largest multiple so that (divisor * multiple <= dividend), 
		//  whereas we are moving with stride 1, 2, 4, 8, 16...2^n for performance reason.
		//  Think this as a binary search.
		long sum = ldivisor;
		long multiple = 1;
		while ((sum+sum) <= ldividend) {
			sum += sum;
			multiple += multiple;
		}
		//Look for additional value for the multiple from the reminder (dividend - sum) recursively.
		return multiple + ldivide(ldividend - sum, ldivisor);
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/9287/no-use-of-long-java-solution
	 */
	public int divide2(int dividend, int divisor) {
		if (dividend == Integer.MIN_VALUE && divisor == -1)
			return Integer.MAX_VALUE;
		if (dividend > 0 && divisor > 0)
			return divideHelper(-dividend, -divisor);
		else if (dividend > 0)
			return -divideHelper(-dividend, divisor);
		else if (divisor > 0)
			return -divideHelper(dividend, -divisor);
		else
			return divideHelper(dividend, divisor);
    }
    private int divideHelper(int dividend, int divisor){
        // base case
        if(divisor < dividend) return 0;
        // get highest digit of divisor
		int cur = 0, res = 0;
		// cur < 31 for (Integer.MIN_VALUE / 1), divisor << cur < 0 for overflow
		while ((divisor << cur) >= dividend && divisor << cur < 0 && cur < 31)
			cur++;
		res = dividend - (divisor << cur - 1);
		if (res > divisor)                // It doesn't matter.
			return 1 << cur - 1;
		return (1 << cur - 1) + divide(res, divisor);
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Divide_Two_Integers divide = new Divide_Two_Integers();
		int a = 241;
		int b = 17;
		System.out.println("Real divide = " + a/b);
		System.out.println("divide = " + divide.divide(a, b));
		System.out.println("divide = " + divide.divide_2(a, b));
		System.out.println("divide = " + divide.divide2(a, b));

	}

}
