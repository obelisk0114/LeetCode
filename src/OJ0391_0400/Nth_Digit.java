package OJ0391_0400;

import java.util.Arrays;

public class Nth_Digit {
	/*
	 * https://leetcode.com/problems/nth-digit/discuss/88391/My-JAVA-solution-beats-over-90-with-explanation-inline
	 * 
	 * Rf :
	 * https://leetcode.com/problems/nth-digit/discuss/88391/My-JAVA-solution-beats-over-90-with-explanation-inline/93275
	 * 
	 * Other code:
	 * https://leetcode.com/problems/nth-digit/discuss/334907/Just-wanted-to-share-my-Java-solution-too-with-easy-explanation.-The-submit-gives-100-fast.
	 */
	public int findNthDigit(int n) {
		int base = 1;

		// Determine the range
		// 10, 11, ..., 99: 90 * 2 digits in total, base = 2
		// 101, 102, 103, ..., 999: 900 * 3 digits in total, base = 3
		// ...
		while (n > 9 * Math.pow(10, base - 1) * base) {
			n = n - 9 * (int) Math.pow(10, base - 1) * base;
			base++;
		}

		// Now we should find out which number the answer follows. eg. if the input is
		// 15, the answer should follow on number "12", that's the variable number for
		int number = (int) Math.pow(10, base - 1) + (n - 1) / base;

		// Then we should find out which specific in the number "12".
		// that's what index for, for input 15, index = 0
		int index = (n - 1) % base;

		// The answer is the index-th digit of the variable number
		return Integer.toString(number).charAt(index) - '0';
	}
	
	/*
	 * https://leetcode.com/problems/nth-digit/discuss/88363/Java-solution
	 * 
	 * Straight forward way to solve the problem in 3 steps:
	 * 
	 * 1. find the length of the number where the nth digit is from
	 * 2. find the actual number where the nth digit is from
	 * 3. find the nth digit and return
	 * 
	 * 1 ~ 9     : 9 * 1 bit
	 * 10 ~ 99   : 9 * 10 * 2 bit
	 * 100 ~ 999 : 9 * 10 * 10 * 3 bit
	 * 
	 * Consider it as an array.
	 * We want to get a specific digit from an array arr like 
	 * [ 1, 0, 0, 1, 0, 1, 1, 0, 2...] (which is representing [100, 101, 102....])
	 * If we want to get the 3rd digit, we do arr[3-1].
	 * The n-1 is adapt the off-by-one index of the array from 1~n into 0~n-1, 
	 * so that we can index the array that begins with the start.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/nth-digit/discuss/88363/Java-solution/381478
	 * https://leetcode.com/problems/nth-digit/discuss/88363/Java-solution/93218
	 * 
	 * Other code:
	 * https://leetcode.com/problems/nth-digit/discuss/88383/java-5ms-solution-beats-94.03
	 */
	public int findNthDigit2(int n) {
		int len = 1;
		long count = 9;
		int start = 1;

		while (n > len * count) {
			n -= len * count;
			len += 1;
			count *= 10;
			start *= 10;
		}

		start += (n - 1) / len;
		String s = Integer.toString(start);
		return Character.getNumericValue(s.charAt((n - 1) % len));
	}
	
	/*
	 * https://leetcode.com/problems/nth-digit/discuss/88375/Short-Python%2BJava
	 * 
	 * Using divisions instead of multiplications to prevent overflow.
	 * 
	 * str(first + n/digits) => the string of the number
	 * n % digits => the digit position in this string
	 * 
	 * 1~9: 1*9 = 9 in total
	 * 10~99: 2*90 = 180 in total
	 * 100~999: 3*900 = 2700 in total
	 * k digits: 9 * 10^k
	 * 
	 * Rf :
	 * https://leetcode.com/problems/nth-digit/discuss/88372/Sharing-my-thinking-process
	 * https://leetcode.com/problems/nth-digit/discuss/88375/Short-Python+Java/93267
	 */
	public int findNthDigit_divide(int n) {
		n -= 1;
		int digits = 1, first = 1;
		while (n / 9 / first / digits >= 1) {
			n -= 9 * first * digits;
			digits++;
			first *= 10;
		}
		return (first + n / digits + "").charAt(n % digits) - '0';
	}
	
	// by myself
	public int findNthDigit_self2(int n) {
        double pow = 1;
        int count = 0;
        double cur = (double) n;
        
        while (cur > 0) {
            cur = cur - 9 * pow * (count + 1);
            pow *= 10;
            count++;
        }
        pow /= 10;
        count--;
        cur = cur + 9 * pow * (count + 1);
        
        int pair = (int) ((cur - 1) / (count + 1));
        int pos = (int) ((cur - 1) % (count + 1));
        
        int num = (int) pow + pair;
        char ans = Integer.toString(num).charAt(pos);
        return ans - '0';
    }
	
	// by myself
	public int findNthDigit_self(int n) {
        if (n <= 9)
            return n;
        
        int pow = 1;
        double cur = (double) n - 9;
        while (cur > 0) {
            cur = cur - 9 * Math.pow(10, pow) * (pow + 1);
            pow++;
        }
        pow--;
        cur = cur + 9 * Math.pow(10, pow) * (pow + 1);
        
        int pair = (int) ((cur - 1) / (pow + 1));
        int pos = (int) ((cur - 1) % (pow + 1));
        
        int num = (int) Math.pow(10, pow) + pair;
        char ans = Integer.toString(num).charAt(pos);
        return ans - '0';
    }
	
	/*
	 * https://leetcode.com/problems/nth-digit/discuss/146893/Java-Solution-Beats-100
	 * 
	 * We are tightening the range of our target gradually.
	 * 1. Is this number that has nth digit between 100~999, 1000~9999 or some other 
	 *    range?
	 * 2. What is this number?
	 * 3. Within thin number, which digit is the nth digit?
	 * 
	 * digits in numbers with 1 digit (1-9)    : 9                  = 9 
	 * digits in numbers with 2 digit (10-99)  : (99 - 9) * 2       = 90 * 2
	 * digits in numbers with 3 digit (100-999): (999 - 90 - 9) * 3 = 900 * 3
	 * digits in numbers with n digit          : 9 * Math.pow(10, n - 1) * n
	 * 
	 * By keep deducting the digits inside number-with-n-digit we can get a residual 
	 * (n) which means "the n th digit of number-with-k-digits". By using that we can 
	 * locate the number and the exact digit index
	 * 
	 * Other code:
	 * https://leetcode.com/problems/nth-digit/discuss/88434/Intuitive-solution-with-comments
	 */
	public int findNthDigit3(int n) {
		// Get the number of digits
		int digits = 1;

		// The interval size of current digit
		long interval = digits * 9 * (long) Math.pow(10, digits - 1);

		int start = 1;

		while (n > interval) {
			n -= interval;
			start += interval / digits;
			digits++;
			interval = digits * 9 * (long) Math.pow(10, digits - 1);
		}

		// The number we should get the digit from
		start += (n - 1) / digits;

		// How many times we need to divide to get the digit
		int time = digits - 1 - (n - 1) % digits;

		for (int i = 0; i < time; i++) {
			start /= 10;
		}
		return start % 10;
	}
	
	// https://leetcode.com/problems/nth-digit/discuss/623405/Java-O(1)-time-O(1)-space
	public int findNthDigit_binarySearch_preprocess(int n) {
		//NIDX precomputed : i-th element = max index of number with i digits
		int[] NIDX = {0, 9, 189, 2889, 38889, 488889, 5888889, 68888889, 788888889};//, 8888888889L, 20363725369L};
		
		int digits = Arrays.binarySearch(NIDX, n);
		if (digits < 0)
			digits = -digits - 1;
		
		// the exact number
		int A = (int) (Math.pow(10, digits - 1) + (n - NIDX[digits - 1] - 1) / digits);
		
		int i = (n - NIDX[digits - 1] - 1) % digits;
		return Integer.toString(A).charAt(i) - '0';
	}
	
	/*
	 * https://leetcode.com/problems/nth-digit/discuss/182837/C%2B%2BCJava-5-line-O(1)-solution-no-strings-no-loopsrecursion-with-explanation
	 * 
	 * long s(int i) { return ((9 * i - 1) * pow(10, i) + 10) / 9; }
	 * 
	 * The first 3 lines determine where the "starting point" is. The starting point 
	 * for 1-digit numbers is 1; for 2-digit numbers, it's 10; for 3-digit numbers, 
	 * it's 190; etc.
	 * 
	 * Once you know your starting point, you still have to figure out how much more 
	 * you have to go. This is completely determined by 
	 * quotient := (n - startingPoint) / numberOfDigits and 
	 * remainder := (n - startingPoint) % numberOfDigits.
	 * 
	 * Ex: Let's say n == 203. Then the first 3 lines of code will tell me that i == 2 
	 * (i.e., s[i] == 190). This means that 190 is the starting point for all 3-digit 
	 * numbers. In other words:
	 * 
	 *  1    0    0  ,  1    0    1  ,  1    0    2  ,  1    0    3  ,  1    0    4  ,  ...
	 * 190  191  192   193  194  195   196  197  198   199  200  201   202  203  204
	 * 
	 * From above, we can clearly see that the 203rd digit is 0. 
	 * (203 - 190) / 3 == 4 and (203 - 190) % 3 == 1. 
	 * That means starting from 190, we take 4 big steps (of size 3, because we're 
	 * dealing with 3-digit numbers) and 1 small step. That's basically what the last 
	 * 2 lines are doing.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/nth-digit/discuss/88434/Intuitive-solution-with-comments/93294
	 */
	public int findNthDigit_preprocess(int n) {
		long[] s = { 1, 10, 190, 2890, 38890, 488890, 5888890, 68888890, 
				788888890, 8888888890L };
		int b = (int) Math.log10(n);
		int i = n >= s[b] ? b : b - 1;
		
		long r = (n - s[i]) % (i + 1);
		return (int) ((r == 0 ? 1 : 0) + (n - s[i]) / (i + 1) / Math.pow(10, i - r)) % 10;
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/nth-digit/discuss/88417/4-liner-in-Python-and-complexity-analysis
     * https://leetcode.com/problems/nth-digit/discuss/88386/Simple-Python-Solution
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/nth-digit/discuss/488294/C%2B%2B-O(lgN)-Time-O(1)-Space
     * https://leetcode.com/problems/nth-digit/discuss/88369/0ms-C%2B%2B-Solution-with-Detail-Explanation
     * https://leetcode.com/problems/nth-digit/discuss/88408/My-C%2B%2B-short-and-clean-answer-with-explanation
     * https://leetcode.com/problems/nth-digit/discuss/88370/Share-my-0ms-C%2B%2B-solution-with-explanation
     */
	
	/**
	 * JavaScript collections
	 * 
	 * https://leetcode.com/problems/nth-digit/discuss/146820/JavaScript-Binary-Search
	 * https://leetcode.com/problems/nth-digit/discuss/443128/javascript-solution
	 */

}
