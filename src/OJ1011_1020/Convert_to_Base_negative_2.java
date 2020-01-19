package OJ1011_1020;

public class Convert_to_Base_negative_2 {
	/*
	 * https://leetcode.com/problems/convert-to-base-2/discuss/268787/Java-easy-understanding-solution-with-detailed-explanation
	 * 
	 * When N is a positive number, for example N = 2, 2 / (-2) = -1, 2 % (-2) = 0, as 
	 * 2 = (-2) * (-1) + 0, the remainder is either 0 or 1 which satisfy our problem.
	 * When N is a negative number, for example N = -1, -1 / (-2) = 0, -1 % (-2) = -1, 
	 * as -1 = (-2) * 0 + (-1), the remainder is -1 which doesn't satisfy our problem. 
	 * 
	 * we add -2 and subtract -2, so the equation still remains accurate, but we 
	 * convert negative remainder to postive.
	 * -1 = (-2) * 0 + (-1)
	 * -1 = (-2) * 0 + (-2) + (-1) - (-2)
	 * -1 = (-2) * (0 + 1) + (-1) - (-2)
	 * 
	 * Other code:
	 * https://leetcode.com/problems/convert-to-base-2/discuss/465074/Java-Solution-with-explanation-for-any-base
	 */
	public String baseNeg2_divide_neg2_reminder_positive(int N) {
		if (N == 0)
			return "0";
		
		int base = -2;
		StringBuilder sb = new StringBuilder();
		while (N != 0) {
			int r = N % base;
			N = N / base;
			
			if (r < 0) {
				r += -base;
				N += 1;
			}
			sb.append(r);
		}
		return sb.reverse().toString();
	}
	
	/*
	 * https://leetcode.com/problems/convert-to-base-2/discuss/302237/Easy-Understand-Java-Solution-Based-on-%40bupt_wc's-Post
	 * 
	 * We know that (-2) ^ (1 || 3 || 5 ...) is negative.
	 * eg. 7 = 111, but if we use -2 as base, the second '1' will cause (-2) ^ 1 = -2, 
	 * so the sum will be (-2) ^ 2 + (-2) ^ 1 + (-2) ^ 0 = 3, so we just need to add 
	 * the subtracted number until all subtracted numbers are added, 7 + 2 * 2 = 11, 
	 * which is 1011, but this brings another (-2) ^ 3 = -8(that 3 is the 4th '1' 
	 * from the right), so we need to add 16, 11 + 16 = 27, which is 11011, that's 
	 * the result.
	 */
	public String baseNeg2_add_negative(int N) {
		if (N == 0)
			return "0";
		
		for (int i = 1; i < 33; i = i + 2) {
			if ((N & (1 << i)) == (1 << i))
				N += 2 * (1 << i);
		}
		
		StringBuilder res = new StringBuilder();
		while (N != 0) {
			res.append((N & 1));
			N = N >> 1;
		}
		return res.reverse().toString();
	}
	
	/*
	 * https://leetcode.com/problems/convert-to-base-2/discuss/265507/JavaC++Python-2-lines-Exactly-Same-as-Base-2/256310
	 * 
	 * N is even, N = -(N / 2)
	 * N is odd, N = -( (N - 1) / 2 )
	 * 
	 * ex: N = 101
	 * 
	 * Rf :
	 * https://leetcode.com/problems/convert-to-base-2/discuss/265507/JavaC%2B%2BPython-2-lines-Exactly-Same-as-Base-2
	 * 
	 * Other code:
	 * https://leetcode.com/problems/convert-to-base-2/discuss/266760/Java-simple-3-lines
	 */
	public String baseNeg2_bit_set_to_even(int n) {
		if (n == 0)
			return "0";
		
		StringBuilder sb = new StringBuilder();
		while (n != 0) {
			sb.append(n & 1);
			n = -(n >> 1);
		}
		return sb.reverse().toString();
	}
	
	/*
	 * https://leetcode.com/problems/convert-to-base-2/discuss/287017/JavaPython-Easy-One-Line
	 * 
	 * First we find a big number x with its binary value 10101...10101
	 * x needs to be bigger than 10^9 so it can cover all input N.
	 * Here I choose mask = 2^32 / 3 = 1431655765 and we have mask > 10^9
	 * 
	 * 101010...10101 (31 digits) . We set it as M.
	 * M << 1 = 101010...101010 (32 digits) = 2 * M
	 * 2 * M + M = 111111...111111 (32 digits) = 10000...0 (33 digits) - 1 = 2^32 - 1
	 * M = (2^32 - 1) / 3 = 1431655765
	 * 
	 * 1. Because it has 0 on all even bits, which are negative bits in base -2,
	 *    it has same value in base 2 and base -2.
	 * 2. When we bitwise operation mask ^ x, in the view of base -2, we actually 
	 *    mask - x. So mask ^ x in base 2 is same as mask - x in base -2.
	 *    
	 * Odd: 1 -> 0 (smaller); Even: 0 -> 1 (smaller, in base -2)
	 * XOR only affect mask in every 1 position in x => mask - x
	 * 
	 * Take advantage of this observation:
	 * mask ^ (mask - x) in base 2 is same as mask - (mask - x) = x in base -2.
	 * So we get the result of this problem.
	 * 
	 * Other code:
	 * https://leetcode.com/problems/convert-to-base-2/discuss/282237/Java-3-lines-solution
	 */
	public String baseNeg2_bitwise(int N) {
		return Integer.toBinaryString(1431655765 ^ (1431655765 - N));
	}
	
	// https://leetcode.com/problems/convert-to-base-2/discuss/265507/JavaC%2B%2BPython-2-lines-Exactly-Same-as-Base-2
	public String baseNeg2_bit_set_to_even_recursiv(int N) {
		if (N == 0 || N == 1)
			return Integer.toString(N);
		return baseNeg2_bit_set_to_even_recursiv(-(N >> 1)) + Integer.toString(N & 1);
	}
	
	// by myself
	public String baseNeg2_self(int N) {
		if (N < 2)
			return Integer.toString(N);

		int base = -2;
		StringBuilder sb = new StringBuilder();
		while (N != 1 && N != -1) {
			int r = N % base;
			int q = N / base;
			if (r < 0) {
				q++;
				r -= base;
			}

			// System.out.println("q = " + q + " ; r = " + r);
			sb.insert(0, r);
			N = q;
		}

		if (N == -1) {
			sb.insert(0, 11);
		} 
		else {
			sb.insert(0, 1);
		}
		return sb.toString();
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/convert-to-base-2/discuss/354519/-2
     * https://leetcode.com/problems/convert-to-base-2/discuss/265567/Python-Recursion-with-Detailed-Explanations
     * https://leetcode.com/problems/convert-to-base-2/discuss/265688/4-line-python-clear-solution-with-explanation
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/convert-to-base-2/discuss/265544/C%2B%2B-Geeks4Geeks
     */

}
