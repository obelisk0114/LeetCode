package OJ0171_0180;

// https://leetcode.com/problems/factorial-trailing-zeroes/discuss/52367/My-explanation-of-the-Log(n)-solution

public class Factorial_Trailing_Zeroes {
	/*
	 * by myself
	 * https://leetcode.com/problems/factorial-trailing-zeroes/discuss/52506/O(log_5(N))-solution-java
	 * 
	 * Other code:
	 * https://leetcode.com/problems/factorial-trailing-zeroes/discuss/52432/3-lines-of-Java-O(logn)-time-O(1)-space
	 */
	public int trailingZeroes_self(int n) {
        int ans = 0;
        int tmp = n;
        while (tmp != 0) {
            tmp /= 5;
            ans += tmp;
        }
        return ans;
    }
	
	/*
	 * https://leetcode.com/problems/factorial-trailing-zeroes/discuss/52485/2-lines-java-solution-any-better-code
	 * 
	 * Other code:
	 * https://leetcode.com/problems/factorial-trailing-zeroes/discuss/52371/My-one-line-solutions-in-3-languages
	 * https://leetcode.com/problems/factorial-trailing-zeroes/discuss/52472/One-Liners-in-C++JavaPythonRubyCC
	 * https://leetcode.com/problems/factorial-trailing-zeroes/discuss/52440/1ms-1-line-Java-Solution
	 */
	public int trailingZeroes_recursive(int n) {
		if (n < 5)
			return 0;
		else
			return n / 5 + trailingZeroes_recursive(n / 5);
	}

}
