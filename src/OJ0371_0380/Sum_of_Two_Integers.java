package OJ0371_0380;

// https://discuss.leetcode.com/topic/49789/just-want-to-share-some-references
// https://discuss.leetcode.com/topic/50315/a-summary-how-to-use-bit-manipulation-to-solve-problems-easily-and-efficiently

public class Sum_of_Two_Integers {
	/*
	 * https://discuss.leetcode.com/topic/49771/java-simple-easy-understand-solution-with-explanation
	 * 
	 * a ^ b : sum of a and b bitwise without carrier
	 * a & b : carry bit
	 *  << 1 : set the carry bit to the correct bit
	 *  
	 * When the carrier is equal to 0, the recursion terminate
	 */
	public int getSum(int a, int b) {
		if (a == 0)
			return b;
		if (b == 0)
			return a;

		while (b != 0) {
			int carry = a & b;
			a = a ^ b;
			b = carry << 1;
		}

		return a;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/49870/one-liner-with-detailed-explanation
	 * 
	 * a ^ b : sum of a and b bitwise without carrier
	 * a & b : carry bit
	 *  << 1 : set the carry bit to the correct bit
	 *  
	 * When the carrier is equal to 0, the recursion terminate
	 * 
	 * Rf : https://discuss.leetcode.com/topic/49764/0ms-ac-java-solution
	 */
	public int getSum_recursive(int a, int b) {
		if (b == 0)
			return a;
		int carry = (a & b) << 1;
		int sum = a ^ b;
		return getSum_recursive(sum, carry);
	}
	
	/*
	 * https://discuss.leetcode.com/topic/49870/one-liner-with-detailed-explanation
	 * 
	 * a ^ b : sum of a and b bitwise without carrier
	 * a & b : carry bit
	 *  << 1 : set the carry bit to the correct bit
	 *  
	 * When the carrier is equal to 0, the recursion terminate
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/49820/one-line-java-code
	 */
	public int getSum_recursive2(int a, int b) {
		return b == 0 ? a : getSum_recursive2(a ^ b, (a & b) << 1);
	}
	
	// by myself
	public int getSum_self(int a, int b) {
        int carry = 0;
        //StringBuilder sb = new StringBuilder();
        int ans = 0;
        int index = 0;
        while (a != 0 || b != 0) {
            int curA = a & 1;
            int curB = b & 1;
            int S = curA ^ curB ^ carry;
            //sb.insert(0, S);
            ans = ans ^ (S << index);
            carry = (curA & curB) | (curA & carry) | (curB & carry);
            a >>>= 1;
            b >>>= 1;
            index++;
        }
        
        if (carry != 0 && index < 32) {
            ans ^= (1 << index);
        }
        return ans;
    }

}
