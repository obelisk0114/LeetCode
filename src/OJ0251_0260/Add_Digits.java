package OJ0251_0260;

public class Add_Digits {
	/*
	 * https://leetcode.com/problems/add-digits/discuss/68667/Simple-Java-Solution-No-recursion-loop
	 * 
	 * If an integer is like 100a + 10b + c, then 
	 * (100a + 10b + c) % 9 = (a + 99a + b + 9b + c) % 9 = (a + b + c) % 9
	 * 
	 * Rf :
	 * https://leetcode.com/problems/add-digits/discuss/68796/O(1)-solution-with-mod-operation
	 * https://leetcode.com/problems/add-digits/discuss/68667/Simple-Java-Solution-No-recursion-loop/70441
	 * 
	 * Other code:
	 * https://leetcode.com/problems/add-digits/discuss/68622/Java-Code-with-Explanation
	 * https://leetcode.com/problems/add-digits/discuss/68588/1-line-Java-Solution
	 */
	public int addDigits(int num) {
		if (num == 0) {
			return 0;
		}
		
		if (num % 9 == 0) {
			return 9;
		} 
		else {
			return num % 9;
		}
	}
	
	/*
	 * https://leetcode.com/problems/add-digits/discuss/68720/Java-O(1)-Solution-for-Positive-and-Negative-numbers
	 * 
	 * For base b (decimal case b = 10), the digit root of an integer is:
	 * dr(n) = 0 if n == 0
	 * dr(n) = (b-1) if n != 0 and n % (b-1) == 0
	 * dr(n) = n mod (b-1) if n % (b-1) != 0
	 * 
	 * or
	 * 
	 * dr(n) = 1 + (n - 1) % 9
	 * 
	 * when n = 0, since (n - 1) % 9 = -1, the return value is zero (correct).
	 * From the formula, we can find that the result of this problem is imminently 
	 * periodic, with period (b-1)
	 * 
	 * ~input: 0 1 2 3 4 ...
	 * output: 0 1 2 3 4 5 6 7 8 9 1 2 3 4 5 6 7 8 9 1 2 3 ....
	 * 
	 * b is 10. int x, y; // y is [1, 8]
	 * n != 0 and n % (b-1) == 0, means n is 9x (x != 0)
	 * upper: (10 - 1) == 9
	 * lower: 1 + (9x - 1)%9 == 1 + (9*(x-1) + 8)%9 == 1 + 8
	 * 
	 * n % (b-1) != 0, means n is 9x + y
	 * upper: (9x + y) mod 9 == y
	 * lower: 1 + (9*x + y - 1)%9 == 1 + y - 1 == y
	 * 
	 * --------------------------------------------------------------------------
	 * 
	 * N = (a[0] * 1 + a[1] * 10 + .. + a[n] * 10^n), and a[0]...a[n] are [0,9]. 
	 * we set M = a[0] + a[1] + ..a[n]
	 * 
	 * 1 % 9 = 1; 10 % 9 = 1; 100 % 9 = 1
	 * so N % 9 = a[0] + a[1] + ..a[n], means N % 9 = M
	 * so N % 9 = (a[0] + ... + a[n]) % 9
	 * 
	 * as 9 % 9 = 0, so we can make (n - 1) % 9 + 1 to help us solve the problem when 
	 * n is 9. as N is 9, (9 - 1) % 9 + 1 = 9
	 * 
	 * Rf :
	 * https://leetcode.com/problems/add-digits/discuss/68580/Accepted-C%2B%2B-O(1)-time-O(1)-space-1-Line-Solution-with-Detail-Explanations
	 * https://en.wikipedia.org/wiki/Digital_root#Congruence_formula
	 * https://leetcode.com/problems/add-digits/discuss/68580/Accepted-C++-O(1)-time-O(1)-space-1-Line-Solution-with-Detail-Explanations/202244
	 * https://leetcode.com/problems/add-digits/discuss/68572/3-methods-for-python-with-explains
	 * https://leetcode.com/problems/add-digits/discuss/68572/3-methods-for-python-with-explains/70348
	 * https://leetcode.com/problems/add-digits/discuss/68694/O(1)-Java-solution/70461
	 * 
	 * Other code:
	 * https://leetcode.com/problems/add-digits/discuss/68793/256ms-Java-one-line-solution
	 */
	public int addDigits2(int num) {
		return 1 + (num - 1) % 9;
	}
	
	// https://leetcode.com/problems/add-digits/discuss/68689/Java-solution-without-using-any-tricks/320690
	public int addDigits_while(int num) {
		while (num > 9) {
			num = num % 10 + num / 10;
		}
		return num;
	}
	
	// by myself
	public int addDigits_self(int num) {
        while (num >= 10) {
            int tmp = num;
            int sum = 0;
            while (tmp > 0) {
                sum = sum + tmp % 10;
                tmp /= 10;
            }
            num = sum;
        }
        return num;
    }
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/add-digits/discuss/68572/3-methods-for-python-with-explains
     * https://leetcode.com/problems/add-digits/discuss/68796/O(1)-solution-with-mod-operation
     * https://leetcode.com/problems/add-digits/discuss/373886/Python-O(1)
     * https://leetcode.com/problems/add-digits/discuss/68732/No-looprecursion-O(1)-runtime-just-one-line-python-code
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/add-digits/discuss/68580/Accepted-C%2B%2B-O(1)-time-O(1)-space-1-Line-Solution-with-Detail-Explanations
     * https://leetcode.com/problems/add-digits/discuss/68776/Two-lines-C-code-with-explanation
     */

}
