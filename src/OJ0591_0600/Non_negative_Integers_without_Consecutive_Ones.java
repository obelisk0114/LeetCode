package OJ0591_0600;

import java.util.Map;
import java.util.HashMap;

public class Non_negative_Integers_without_Consecutive_Ones {
	/*
	 * https://leetcode.com/articles/non-negative-integers-without-consecutive-ones/
	 * 
	 * the number of length k string without consecutive 1 is Fibonacci sequence f(k);
	 * For example, if k = 5, the range is 00000-11111. We can consider it as two 
	 * ranges, which are 00000-01111 and 10000-10111. Any number >= 11000 is not 
	 * allowed due to consecutive 1. The first case is actually f(4), and the second 
	 * case is f(3), so f(5)= f(4) + f(3).
	 * 
	 * For example, if n is 10010110,
	 * we find first '1' at 7 digits to the right, we add range 00000000-01111111, which is f(7);
	 * second '1' at 4 digits to the right, add range 10000000-10001111, f(4);
	 * third '1' at 2 digits to the right, add range 10010000-10010011, f(2);
	 * fourth '1' at 1 digits to the right, add range 10010100-10010101, f(1);
	 * Those ranges are continuous from 00000000 to 10010101. And any greater number <= n will have consecutive 1
	 * 
	 * Rf : https://leetcode.com/problems/non-negative-integers-without-consecutive-ones/discuss/103754/C++-Non-DP-O(32)-Fibonacci-solution
	 */
	public int findIntegers(int num) {
        int[] f = new int[32];
        f[0] = 1;
        f[1] = 2;
        for (int i = 2; i < f.length; i++)
            f[i] = f[i - 1] + f[i - 2];
        
        int i = 30, sum = 0, prev_bit = 0;
        while (i >= 0) {
            if ((num & (1 << i)) != 0) {
                sum += f[i];
                if (prev_bit == 1) {
                    sum--;
                    break;
                }
                prev_bit = 1;
            } 
            else
                prev_bit = 0;
            
            i--;
        }
        return sum + 1;
    }
	
	/*
	 * https://leetcode.com/problems/non-negative-integers-without-consecutive-ones/discuss/103751/Java-O(1)-time-O(1)-space-DP-Solution
	 * 
	 * From MSB to LSB, add 1 to zero when we meet '1' (this bit can be 0, so add 1).
	 * Use isNum to record there is a consecutive one.
	 * If there is a consecutive one, we don't need to check '1', since the residual
	 * will have consecutive ones.
	 */
	public int findIntegers_Cumulative(int num) {
		// one:  all bit before cur is less than num and no continues 1 and 
		//       cur bit is at one;
		// zero: all bit before cur is less than num and no continues 1 and
		//       cur bit is at zero;
		int one = 0, zero = 0, temp;
		boolean isNum = true;
		for (int i = 31; i >= 0; i--) {
			temp = zero;
			zero += one;
			one = temp;
			
			if (isNum && ((num >> i) & 1) == 1) {
				zero += 1;
			}
			if (((num >> i) & 1) == 1 && ((num >> i + 1) & 1) == 1) {
				isNum = false;
			}
		}
		return one + zero + (isNum ? 1 : 0);
	}
	
	/*
	 * https://leetcode.com/problems/non-negative-integers-without-consecutive-ones/discuss/103749/Java-Solution-DP
	 * 
	 * Rf :
	 * leetcode.com/problems/non-negative-integers-without-consecutive-ones/discuss/103749/Java-Solution-DP/116136
	 * leetcode.com/problems/non-negative-integers-without-consecutive-ones/discuss/103749/Java-Solution-DP/106764
	 * http://www.geeksforgeeks.org/count-number-binary-strings-without-consecutive-1s/
	 */
	public int findIntegers_subtract_Fibonacci(int num) {
		StringBuilder sb = new StringBuilder(Integer.toBinaryString(num)).reverse();
		int n = sb.length();

		// a[i] means bit i is 0.
		// b[i] means bit i is 1.
		int[] a = new int[n];
		int[] b = new int[n];
		a[0] = b[0] = 1;
		for (int i = 1; i < n; i++) {
			a[i] = a[i - 1] + b[i - 1];
			b[i] = a[i - 1];
		}

		int result = a[n - 1] + b[n - 1];
		
		// subtract the solutions which is larger than num
		for (int i = n - 2; i >= 0; i--) {
			// the i-th bit in valid solutions must be 0, which are all smaller than 'num'
			if (sb.charAt(i) == '1' && sb.charAt(i + 1) == '1')
				break;
			
			/*
			 * we know a[i + 1] includes solutions of i-th == 0 (00***) and i-th 
			 * bit == 1 (01***), we know the i-th bit of num is 0, so we need to 
			 * subtract all the 01*** solutions because it is larger than num. 
			 * Therefore, b[i] is subtracted from res.
			 */
			if (sb.charAt(i) == '0' && sb.charAt(i + 1) == '0')
				result -= b[i];
		}

		return result;
	}
	
	/*
	 * https://leetcode.com/problems/non-negative-integers-without-consecutive-ones/discuss/103759/Short-O(lg(n))-java-solutions-recursive-and-iterative
	 * 
	 * If we have n bits, the number of integers without consecutive ones
	 * f(n) = "0" f(n - 1) + "10" f(n - 2)
	 * 
	 * First, we find n, which is the position of the highest set bit in our number.
	 * 
	 * If the binary representation of our number starts with "11", then all integers will 
	 * be smaller than our number, and we can just return a Fibonacci number for n bits.
	 * 
	 * If the binary representation of our number starts with "10", then all integers with 
	 * n - 1 bits will be smaller than our number. So, we will grab a Fibonacci number 
	 * for n - 1 bits. That's "0" + f(n - 1) case. Plus, we need to add "10.." case, so 
	 * we remove the highest bit from our number and recursively call our function.
	 * 
	 * Rf : https://leetcode.com/problems/non-negative-integers-without-consecutive-ones/discuss/103766/C++-4-lines-DPFibonacci-6-ms
	 */
	public int findIntegers_Fibonacci(int num) {
		// Fibonacci
		int[] oneone = { 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 
				1597, 2584, 4181, 6765, 10946, 17711, 28657, 46368, 75025, 121393, 
				196418, 317811, 514229, 832040, 1346269, 2178309, 3524578 };
		int ans = 0;
		for (int bit = 1 << 30, i = 31; i > 0; bit >>= 1, i--) {
			if ((num & bit) == 0)
				continue;
			if ((num & (bit >> 1)) != 0) {
				return ans + oneone[i];
			}
			
			ans += oneone[i - 1];
		}
		return ans + 1;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/non-negative-integers-without-consecutive-ones/discuss/103757/Java-Memoized-DP-Solution
	 */
	public int findIntegers_separate(int num) {
		return findIntegers_separate(num, new HashMap<>());
	}
	public int findIntegers_separate(int num, Map<Integer, Integer> memo) {
		if (num <= 2)
			return num + 1; // base case
		if (memo.containsKey(num))
			return memo.get(num); // check if this result has already been computed

		int msb = 31 - Integer.numberOfLeadingZeros(num); // retrieve index of most significant bit
		
		// Use the most significant bit to cut the number into two part
		int subNum = (1 << msb) - 1, subNum2 = ~(1 << msb) & num;
		if (subNum2 >= 1 << msb - 1)
			subNum2 = subNum >> 1;
		int result = findIntegers_separate(subNum, memo) + findIntegers_separate(subNum2, memo);

		memo.put(num, result); // add result to memo
		return result;
	}
	
	// https://leetcode.com/problems/non-negative-integers-without-consecutive-ones/discuss/103761/JAVA-31-ms-DP-Solution-with-Explanation

}
