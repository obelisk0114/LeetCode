package OJ0261_0270;

public class Ugly_Number {
	/*
	 * https://leetcode.com/problems/ugly-number/discuss/69235/Share-my-simple-Java-solution
	 * 
	 * Continually divide the number by 2,3,5. If it's ugly, the result must be 1.
	 * 
	 * Other code:
	 * https://leetcode.com/problems/ugly-number/discuss/69296/Java-Solution(-with-explanation-in-code)
	 */
	public boolean isUgly_positive(int num) {
		if (num <= 0)
			return false;
		
		while (num % 2 == 0)
			num /= 2;
		while (num % 3 == 0)
			num /= 3;
		while (num % 5 == 0)
			num /= 5;
		
		return num == 1;
	}
	
	/*
	 * https://leetcode.com/problems/ugly-number/discuss/69308/Java-solution-greatest-divide-by-2-3-5
	 * 
	 * Just divide by 2, 3 and 5 as often as possible and then 
	 * check whether we arrived at 1.
	 * 
	 * Other code:
	 * https://leetcode.com/problems/ugly-number/discuss/69214/2-4-lines-every-language
	 * https://leetcode.com/problems/ugly-number/discuss/69279/Java-clean-solution-is-this-O(logn)-time
	 * https://leetcode.com/problems/ugly-number/discuss/69286/Simple-Java-solution-using-factors'-method
	 */
	public boolean isUgly2(int num) {
		if (num <= 0) {
			return false;
		}

		int[] divisors = { 2, 3, 5 };

		for (int d : divisors) {
			while (num % d == 0) {
				num /= d;
			}
		}
		return num == 1;
    }
	
	/*
	 * by myself
	 * 
	 * Other code:
	 * https://leetcode.com/problems/ugly-number/discuss/69342/Simplest-java-solution
	 * https://leetcode.com/problems/ugly-number/discuss/69225/My-2ms-java-solution
	 * https://leetcode.com/problems/ugly-number/discuss/69329/Simple-Java-solution-for-Ugly-Number-problem
	 */
	public boolean isUgly_self(int num) {
        if (num == 0) {
            return false;
        }
        
        while (num % 2 == 0) {
            num /= 2;
        }
        
        while (num % 3 == 0) {
            num /= 3;
        }
        
        while (num % 5 == 0) {
            num /= 5;
        }
        
        return num == 1;
    }
	
	/*
	 * https://leetcode.com/problems/ugly-number/discuss/69332/Simple-java-solution-with-explanation
	 * 
	 * (1) basic cases: <= 0 and == 1
	 * (2) other cases: since the number can contain the factors of 2, 3, 5, I just 
	 *     remove those factors. Now I have a number without any factors of 2, 3, 5.
	 * (3) after the removing, the number (new number) can contain 
	 *     a) the factor that is prime and meanwhile it is >= 7, or 
	 *     b) the factor that is not the prime and the factor is not comprised of 
	 *        2, 3 or 5. In both cases, it is false (not ugly number).
	 */
	public boolean isUgly_recur(int num) {
		if (num <= 0) {
			return false;
		}
		if (num == 1) {
			return true;
		}
		if (num % 2 == 0) {
			return isUgly_recur(num / 2);
		}
		if (num % 3 == 0) {
			return isUgly_recur(num / 3);
		}
		if (num % 5 == 0) {
			return isUgly_recur(num / 5);
		}
		return false;
	}
	
	/*
	 * https://leetcode.com/problems/ugly-number/discuss/69225/My-2ms-java-solution/121483
	 * 
	 * Other code:
	 * https://leetcode.com/problems/ugly-number/discuss/69316/Java-Python-C%2B%2B-solutions-for-you-guyz-!!!
	 */
	public boolean isUgly_while(int num) {
		if (num == 0)
			return false;
		if (num == 1)
			return true;

		while (num != 1) {
			if (num % 2 == 0)
				num /= 2;
			else if (num % 3 == 0)
				num /= 3;
			else if (num % 5 == 0)
				num /= 5;
			else
				return false;
		}
		return true;
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/ugly-number/discuss/69305/My-python-solution
     * https://leetcode.com/problems/ugly-number/discuss/511887/Python-O(-log-n-)-sol-by-integer-division.-93%2B-with-Hint-and-Comment
     * https://leetcode.com/problems/ugly-number/discuss/69232/Python%3A-1-line-solution
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/ugly-number/discuss/69353/Simple-C%2B%2B-solution
     * https://leetcode.com/problems/ugly-number/discuss/69297/4ms-Solution-in-C%2B%2B
     * https://leetcode.com/problems/ugly-number/discuss/352587/C%2B%2B-Solution-0ms-(100-faster)-and-8MB-(100-less)
     * https://leetcode.com/problems/ugly-number/discuss/69302/Recursive-solution-is-faster-than-iterative-solution
     */
	
	/**
	 * JavaScript collections
	 * 
	 * https://leetcode.com/problems/ugly-number/discuss/69298/Javascript-solution-5-lines.-Clean-and-short
	 */

}
