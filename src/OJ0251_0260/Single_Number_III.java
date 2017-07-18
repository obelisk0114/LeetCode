package OJ0251_0260;

import java.util.Set;
import java.util.HashSet;

public class Single_Number_III {
	/*
	 * https://discuss.leetcode.com/topic/21605/accepted-c-java-o-n-time-o-1-space-easy-solution-with-detail-explanations
	 * 
	  By XORing all the numbers, we actually get the XOR of the two target numbers.
	  If some bit (ith bit) of the XOR result is 1, 
	  it means that the two target numbers differ at that location.
	  Thus, all the numbers can be partitioned into two groups according to their bits at location i.
         the first group consists of all numbers whose bits at i is 0.
         the second group consists of all numbers whose bits at i is 1.
         
      if a duplicate number has bit i as 0, two copies of it will belong to the first group.
      by XoRing all numbers in the first group, we can get the first number.
      by XoRing all numbers in the second group, we can get the second number.
	 
	  If XOR = aaaa1000(a = 1 or 0)
      We could get the first(from low to high) bit equals 1 as follow:
      first: ~XOR = bbbb0111 (b = ~a)
      then add 1, with carrying bits ~XOR + 1 = bbbb1000
      then, XOR & (~XOR + 1) = 00001000
      so we can write as XOR & (-XOR) also. (two's compliment)
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/25382/sharing-explanation-of-the-solution
	 * https://discuss.leetcode.com/topic/25382/sharing-explanation-of-the-solution/18
	 */
	public int[] singleNumber(int[] nums) {
        // Pass 1 : 
        // Get the XOR of the two numbers we need to find
		int diff = 0;
		for (int num : nums) {
			diff ^= num;
		}
        // Get its last set bit
        diff &= -diff;
        
        // Pass 2 :
        int[] rets = {0, 0}; // this array stores the two numbers we will return
        for (int num : nums)
        {
            if ((num & diff) == 0) // the bit is not set
            {
                rets[0] ^= num;
            }
            else // the bit is set
            {
                rets[1] ^= num;
            }
        }
        return rets;
    }
	
	// https://discuss.leetcode.com/topic/21883/my-java-solution-adapted-from-the-commonest-solution-here
	public int[] singleNumber_highest1(int[] nums) {
		int diff = 0;
		for (int num : nums) {
			diff ^= num;
		}
		diff = Integer.highestOneBit(diff);

		int[] result = new int[2];
		// Arrays.fill(result,0);
		for (int num : nums) {
			if ((diff & num) == 0) {
				result[0] ^= num;
			} else {
				result[1] ^= num;
			}
		}
		return result;
	}
	
	// https://discuss.leetcode.com/topic/35800/java-easy-to-understand-solution-with-no-confusing-x-x
	int[] singleNumber_loopFind(int[] nums) {
		int[] result = new int[2];
		int xorResult = 0;
		// find the xor of two numbers
		for (int i = 0; i < nums.length; i++) {
			xorResult ^= nums[i];
		}

		// find the last bit where it is 1
		// a bit of 1 means, at this bit
		// either num1 == 0 && num2 == 1 || num1 == 1 && num2 == 0

		int lastBitOne = 0;
		for (int i = 0; i < 32; i++) {
			if ((xorResult & 1) == 1) {
				lastBitOne = i;
				break;
			}
			xorResult >>>= 1;
		}

		// now knowing the lastBitOne
		// we can split the elements into two groups
		// one group with that bit = 1, one group with that bit = 0
		// We know one result has to be in group1 and the other has to be in group2
		// We just xor elements in two group

		for (int i = 0; i < nums.length; i++) {
			if (((nums[i] >>> lastBitOne) & 1) == 0) {
				result[0] ^= nums[i];
			} else {
				result[1] ^= nums[i];
			}
		}
		return result;
	}
	
	// space complexity : O(n)
	public int[] singleNumber_hash(int[] nums) {
		Set<Integer> set = new HashSet<>();
		for (int i : nums) {
			if (set.contains(i)) {
				set.remove(i);
			}
			else {
				set.add(i);
			}
		}
		int[] out = new int[2];
		int index = 0;
		for (Integer i : set) {
			out[index++] = i;
		}
		return out;
	}

}
