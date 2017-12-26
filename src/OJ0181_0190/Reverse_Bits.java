package OJ0181_0190;

import java.util.List;
import java.util.ArrayList;

// you need treat n as an unsigned value
public class Reverse_Bits {
	/*
	 * https://discuss.leetcode.com/topic/42572/sharing-my-2ms-java-solution-with-explanation
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/9764/java-solution-and-optimization/15
	 * https://discuss.leetcode.com/topic/9764/java-solution-and-optimization
	 */
	public int reverseBits(int n) {
		if (n == 0)
			return 0;

		int result = 0;
		for (int i = 0; i < 32; i++) {
			result <<= 1;
			if ((n & 1) == 1)
				result++;
			n >>= 1;
		}
		return result;
	}
	
	// https://discuss.leetcode.com/topic/12236/concise-java-solution
	public int reverseBits_or_first(int n) {
		int result = 0;
		for (int i = 0; i < 32; ++i) {
			result = result << 1 | (n & 1);
			n >>>= 1;
		}
		return result;
	}
	
	// https://discuss.leetcode.com/topic/32962/2ms-java-solution
	public int reverseBits_shift_i(int n) {
        int answer = 0; // initializing answer
        for (int i = 0; i < 32; i++) { // 32 bit integers
            answer <<= 1; // shifts answer over 1 to open a space
            answer |= ((n >> i) & 1); // inserts bits from n
        }
        return answer;
    }
	
	// by myself
	public int reverseBits_self2(int n) {
        int ans = 0;
        int index = 31;
        int num = n;
        while (num != 0) {
            ans = ans | ((num & 1) << index);
            num >>>= 1;
            index--;
        }
        
        return ans;
    }
	
	/*
	 * https://discuss.leetcode.com/topic/45463/java-two-methods-using-string-or-bit-operation-6ms-and-2ms-easy-understand
	 * 
	 * 利用高低位交換實現逆序
     * 兩位一組，高低位互換，方法是（取奇數位，偶數位補 0，右移 1 位）| （取偶數位，奇數位補 0，左移 1 位）
     * 依次四位一組，八位一組，十六位一組，三十二位一組
     * 由於是無符號位，所以注意得是邏輯右移
     * 
     * Rf :
     * https://discuss.leetcode.com/topic/9764/java-solution-and-optimization/15
     * https://discuss.leetcode.com/topic/9764/java-solution-and-optimization/20
	 */
	public int reverseBits2(int n) {
		n = ((n & 0xAAAAAAAA) >>> 1) | ((n & 0x55555555) << 1);
		n = ((n & 0xCCCCCCCC) >>> 2) | ((n & 0x33333333) << 2);
		n = ((n & 0xf0f0f0f0) >>> 4) | ((n & 0x0f0f0f0f) << 4);
		n = ((n & 0xff00ff00) >>> 8) | ((n & 0x00ff00ff) << 8);
		n = ((n & 0xffff0000) >>> 16) | ((n & 0x0000ffff) << 16);
		return n;
	}
	
	// by myself
	public int reverseBits_self(int n) {
        List<Integer> list = new ArrayList<>(32);
        int num = n;
        while (num != 0) {
            list.add(num & 1);
            num >>>= 1;
        }
        
        while (list.size() != 32) {
            list.add(0);
        }
        
        int ans = 0;
        for (int j = list.size() - 1; j >= 0; j--) {
            ans = ans | (list.get(j) << (list.size() - 1 - j)) ;
        }
        return ans;
    }

}
