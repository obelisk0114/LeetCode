package OJ0201_0210;

import java.util.List;
import java.util.ArrayList;

public class Bitwise_AND_of_Numbers_Range {
	/*
	 * https://discuss.leetcode.com/topic/28538/java-python-easy-solution-with-explanation
	 * 
	 * n = xyz1abc, m = xyz0def
	 * There is a p = xyz1000, which m < p <= n
	 * Hence, we "and" all the number between m and n, the result will be xyz0000.
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/12133/bit-operation-solution-java
	 * https://discuss.leetcode.com/topic/42968/java-7-ms-solution/3
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/12093/my-simple-java-solution-3-lines
	 */
	public int rangeBitwiseAnd(int m, int n) {
		int count = 0;
		while (m != n) {
			m >>= 1;
			n >>= 1;
			count++;
		}
		return m << count;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/20176/2-line-solution-with-detailed-explanation
	 * 
	 * reduce n by removing the rightmost '1' bit until n <= m;
	 * 
	   // given m < n
	   m := common bits + 0 + remaining bits of m
       n := common bits + 1 + remaining bits of n.
       // thus repeatedly clear last bit of n, until n <= m
	 * 
	 * Rf : https://stackoverflow.com/questions/12380478/bits-counting-algorithm-brian-kernighan-in-an-integer-time-complexity
	 */
	public int rangeBitwiseAnd_remove_rightmost_1(int m, int n) {
		while (m < n)
			n = n & (n - 1);
		return n;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/13508/one-line-c-solution
	 * 
	 * if n > m, the lowest bit will be 0, and then we could transfer the problem 
	 * to sub-problem: rangeBitwiseAnd(m>>1, n>>1).
	 */
	public int rangeBitwiseAnd_recursive(int m, int n) {
	    return (n > m) ? (rangeBitwiseAnd_recursive(m/2, n/2) << 1) : m;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/35932/java-8-ms-one-liner-o-1-no-loop-no-log
	 * 
	 * When we go from m to n some higher part may remain the same. The lower part 
	 * changes. If we take the highest bit that is different, then it must be true 
	 * that it is 1 in n and 0 in m.
	 * 
	 * That means that at some point it went from 0 to 1, and at that very point the 
	 * lower digits must have all turned to zeroes.
	 * 
	 * create a mask that fills the whole thing with 1 to the right, then we subtract 
	 * 1 so that bit goes to zero and everything to the right turns into ones. 
	 * Then we bit-reverse the mask and apply it to m (the highest differing bit is 0).
	 */
	public int rangeBitwiseAnd_xor_highestOneBit(int m, int n) {
		return m == n ? m : m & ~(Integer.highestOneBit(m ^ n) - 1);
	}
	
	/*
	 * https://discuss.leetcode.com/topic/13239/my-o-1-solution-using-bitwise-xor-and
	 * 
	 * m = xxxx0yyyyy and n = xxxx1zzzzz, where the x part are the same and the 
	 * y/z part can be anything.
	 * 
	 * The goal of this problem is to calculate all the bitwise and of the numbers 
	 * between m and n, which must include k = xxxx100000, so obviously the answer 
	 * must be xxxx000000.
	 */
	int rangeBitwiseAnd_xor_and(int m, int n) {
		int xmask = m ^ n;     // xmask extracts the x part out
		int mlen = 0;
		int mask;

		// count the position of highest 1 of xmask
		if ((xmask >> (mlen + 16)) > 0)
			mlen += 16;
		if ((xmask >> (mlen + 8)) > 0)
			mlen += 8;
		if ((xmask >> (mlen + 4)) > 0)
			mlen += 4;
		if ((xmask >> (mlen + 2)) > 0)
			mlen += 2;
		if ((xmask >> (mlen + 1)) > 0)
			mlen++;

		mask = ~0 << mlen;
		return m & mask;
	}
	
	// https://discuss.leetcode.com/topic/13710/a-math-solution
	public int rangeBitwiseAnd_xor_log(int m, int n) {
		if (m == n) {
			return m;
		}
		// The highest bit of 1 in diff is the highest changed bit.
		int diff = m ^ n;
		// Index is the index of the highest changed bit. Starting at 1.
		int index = (int) (Math.log(diff) / Math.log(2)) + 1;
		// Eliminate the changed part.
		m = m >> index;
		return m << index;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/12154/java-solution-using-math-log
	   
	   From right to left, the ith bit (start from 0) will flip every pow(2,i)
	   numbers. We can know that some bits of result will always be 0 resulting
	   from such bit flip. The number of that kind of bits is log2(n-m)+1.
	   (Exception is n-m=0). And these bits are adjacent, starting from right.
	   Let L = log2(n-m)+1. (n-m!=0) We can create a mask that all bits are '1'.
	   And set L bits counting from right to left to '0'. Use this mask to do
	   AND operation with (m & n).
	   
	   For example m=6(0000 0110) n=12(0000 1100). L = 3 mask = (1111 1000)
	   result = m & n & mask = 0
	 */
	public int rangeBitwiseAnd_log(int m, int n) {
		if (m == n)
			return m;
		int dif = n - m;
		int log = (int) (Math.log(dif) / Math.log(2));    // log = log2(dif)
		int base = 0xffffffff;
		int mask = base << (log + 1);
		return m & n & mask;
	}
	
	// by myself
	public int rangeBitwiseAnd_myself(int m, int n) {
        List<Integer> list = new ArrayList<>();
        int tmp = n;
        while (tmp != 0) {
            list.add(tmp % 2);
            tmp /= 2;
        }
        
        int bound = 1 << (list.size() - 1);
        if (m < bound) {
            return 0;
        }
        if (m == bound || m == n) {
            return m;
        }
        
        List<Integer> list2 = new ArrayList<>();
        int tmp2 = n - m;
        while (tmp2 != 0) {
            list2.add(tmp2 % 2);
            tmp2 /= 2;
        }
        
        int i = list2.size() - 1;
        int dif = n % (1 << (i + 1));
        while (list.get(i) != 1 || (dif < (n - m)) ) {
            i++;
            dif = n % (1 << (i + 1));
        }
        int res = n - n % (1 << (i + 1));
        return res;
    }
	
	// O(n) loop
	public int rangeBitwiseAnd_loop(int m, int n) {
		if (m == 0 || n >= (2 * (long) m))
            return 0;
        int res = n;
        for (int i = m; i < n; i++) {
            res &= i;
        }
        return res;
    }

}
