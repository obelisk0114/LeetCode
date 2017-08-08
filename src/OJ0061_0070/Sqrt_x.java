package OJ0061_0070;

/*
 * https://en.wikipedia.org/wiki/Methods_of_computing_square_roots
 * https://en.wikipedia.org/wiki/Integer_square_root
 * https://discuss.leetcode.com/topic/31150/3-java-solutions-with-explanation
 */

public class Sqrt_x {
	/*
	 * https://discuss.leetcode.com/topic/24532/3-4-short-lines-integer-newton-every-language
	 * 
	 * use Newton's method to find a solution for the equation :
	 *  x^2 - n = 0
	 */
	public int mySqrt(int x) {
		long r = x;
		while (r * r > x)
			r = (r + x / r) / 2;
		return (int) r;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/3246/using-binary-search-accepted-but-one-question
	 * 
	 * If (m * m) overflows the 32 bit int limit, you might end up with an infinite loop.
	 * 
	 * Rf : https://discuss.leetcode.com/topic/3246/using-binary-search-accepted-but-one-question/2
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/8680/a-binary-search-solution
	 */
	public int mySqrt_binarySearch(int x) {
		if (x == 0 || x == 1) {
			return x;
		}
		int l = 1, r = x, res = 0;
		while (l <= r) {
			int m = (l + r) / 2;
			if (m == x / m) {
				return m;
			} else if (m > x / m) {
				r = m - 1;
			} else {
				l = m + 1;
				res = m;
			}
		}
		return res;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/2671/share-my-o-log-n-solution-using-bit-manipulation
	 * 
	 * sqrt(x) is composed of binary bits, calculate sqrt(x) by 
	 * deciding every bit from the most significant to least significant.
	 * 
	 * Rf : https://discuss.leetcode.com/topic/2671/share-my-o-log-n-solution-using-bit-manipulation/4
	 */
	public int mySqrt_bit_operation(int x) {
		if (x == 0)
			return 0;
		int h = 0;
		while ((long) (1 << h) * (long) (1 << h) <= x) // firstly, find the most significant bit
			h++;
		h--;
		int b = h - 1;
		int res = (1 << h);
		while (b >= 0) { // find the remaining bits
			if ((long) (res | (1 << b)) * (long) (res | (1 << b)) <= x)
				res |= (1 << b);
			b--;
		}
		return res;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/35357/share-my-2ms-and-4lines-java-code
	 * 
	 * sqrt(N) = 2/2 * sqrt(N) = 2 * sqrt(1/4) * sqrt(N) = 2 * sqrt(N/4). 
	 * For the Ns that are not multiple of 4, for example, 9, 25 or 49, 
	 * the actual result should be 1 + 2 * sqrt(N/4), because we need to take remainders.
	 */
	public int mySqrt_recursive(int x) {
		if (x < 4)
			return x == 0 ? 0 : 1;
		int res = 2 * mySqrt_recursive(x / 4);
		if ((res + 1) * (res + 1) <= x && (res + 1) * (res + 1) >= 0) // avoid overflow
			return res + 1;
		return res;
	}
	
	// https://en.wikipedia.org/wiki/Methods_of_computing_square_roots#Babylonian_method
	public int mySqrt_Babylonian_method(int x) {
		if (x <= 1)
			return x;
		int tmp = x;
		int n = 0;
		int digit = 1;
		while (tmp >= 100) {
			tmp /= 100;
			n++;
			digit *= 100;
		}
		int a = 2;
		if (x / digit >= 10)
			a = 6;
		digit = 1;
		while (n > 0) {
			digit *= 10;
			n--;
		}
		// System.out.println("a = " + a);
		// System.out.println("digit = " + digit);

		double pre = a * digit;
		double cur = -1;
		while (true) {
			cur = (pre + x / pre) / 2;
			// System.out.println("pre = " + pre);
			// System.out.println("cur = " + cur);
			if (Math.abs(pre - cur) < 1) {
				break;
			}
			pre = cur;
		}
		return (int) cur;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://stackoverflow.com/questions/11513344/how-to-implement-the-fast-inverse-square-root-in-java
	 * 
	 * Rf :
	 * https://en.wikipedia.org/wiki/Fast_inverse_square_root
	 * https://github.com/Fishrock123/Optimized-Java/blob/master/src/com/fishrock123/math/RootMath.java
	 * https://stackoverflow.com/questions/16551140/why-is-fast-inverse-square-root-so-odd-and-slow-on-java
	 * 
	 * C++ Code : https://discuss.leetcode.com/topic/14695/4ms-c-solution-using-carmack-s-method
	 */
	public int mySqrt_fast_inverse_sqrt_root(int x) {
		double inv = 1 / invSqrt((double) x);
		return (int) inv;
	}
	public double invSqrt(double x) {
		double xhalf = 0.5d * x;
		long i = Double.doubleToLongBits(x);
		i = 0x5fe6ec85e7de30daL - (i >> 1);
		x = Double.longBitsToDouble(i);
		x *= (1.5d - xhalf * x * x);
		x *= (1.5d - xhalf * x * x);
		x *= (1.5d - xhalf * x * x);
		return x;
	}
	
	// myself
	public int mySqrt_cheat(int x) {
        double sqrt = Math.sqrt(x);
        return (int) sqrt;
    }

}
