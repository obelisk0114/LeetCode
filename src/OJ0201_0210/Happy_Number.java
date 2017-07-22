package OJ0201_0210;

import java.util.Set;
import java.util.HashSet;

/*
 * https://discuss.leetcode.com/topic/30520/explanation-of-why-those-posted-algorithms-are-mathematically-valid
 * https://discuss.leetcode.com/topic/42746/all-you-need-to-know-about-testing-happy-number
 */

public class Happy_Number {
	// https://discuss.leetcode.com/topic/25026/beat-90-fast-easy-understand-java-solution-with-brief-explanation
	public boolean isHappy(int n) {
		Set<Integer> inLoop = new HashSet<Integer>();
		int squareSum, remain;
		while (inLoop.add(n)) {
			squareSum = 0;
			while (n > 0) {
				remain = n % 10;
				squareSum += remain * remain;
				n /= 10;
			}
			if (squareSum == 1)
				return true;
			else
				n = squareSum;
		}
		return false;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/12742/o-1-space-java-solution
	 * 
	 * Rf : https://discuss.leetcode.com/topic/12587/my-solution-in-c-o-1-space-and-no-magic-math-property-involved
	 * 
	 */
	public boolean isHappy_cycle(int n) {
		int x = n;
		int y = n;
		while (x > 1) {
			x = cal(x);
			if (x == 1)
				return true;
			y = cal(cal(y));
			if (y == 1)
				return true;

			if (x == y)
				return false;
		}
		return true;
	}
	public int cal(int n) {
		int x = n;
		int s = 0;
		while (x > 0) {
			s = s + (x % 10) * (x % 10);
			x = x / 10;
		}
		return s;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/12710/my-java-solution-find-1-or-7-when-happy-sum-is-a-single-digit
	 * 
	 * Rf :
	 * https://discuss.leetcode.com/topic/12710/my-java-solution-find-1-or-7-when-happy-sum-is-a-single-digit/4
	 */
	public boolean isHappy_1_7(int n) {
		if (n <= 0)
			return false;
		while (true) {
			int sum = 0;
			while (n != 0) {
				sum += (n % 10) * (n % 10);
				n = n / 10;
			}
			if (sum / 10 == 0) {
				if (sum == 1 || sum == 7)
					return true;
				else
					return false;
			}
			n = sum;
		}
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/22265/fastest-java-code-so-far-with-comments
	 */
	public boolean isHappy_wiki(int n) {
        /* Base cases */
        
        // A negative or zero value is not a valid input. Return grumpily
        if (n < 1) {
            return false;
        }
        
        // You have entered the chain of despair. You will never be happy again.
        if (n == 4
        ||  n == 16
        ||  n == 20
        ||  n == 37
        ||  n == 42
        ||  n == 58
        ||  n == 89
        ||  n == 145) {
            return false;
        }
        // Check Wikipedia for the explanation of despairing numbers
        // You don't need to remember all of them.Just remembering one of them will do
        
        // You have achieved bliss
        if (n == 1) {
            return true;
        }
        
        // If none of the above. Keep searching
        return isHappy(getSumOfSquares(n));
    }
    private int getSumOfSquares(int num) {
        int sum = 0;
        while (num > 0) {
            int digit = num % 10;
            sum += digit * digit;
            num /= 10;
        }
        
        return sum;
    }
    
    // https://discuss.leetcode.com/topic/31726/1ms-java-solution

}
