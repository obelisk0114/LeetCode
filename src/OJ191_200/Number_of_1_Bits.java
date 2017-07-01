package OJ191_200;

// you need to treat n as an unsigned value
public class Number_of_1_Bits {
	/*
	 * https://discuss.leetcode.com/topic/9962/concise-java-solution-x-x-1
	 * x & (x-1) helps to remove right most 1 for x.
	 */
	public int hammingWeight(int n) {
        int count = 0;
        while(n != 0){
            n = n & (n-1);
            count++;
        }
        return count;
    }
	
	/*
	 * Self
	 * We need to use bit shifting unsigned operation >>> 
	 * (while >> depends on sign extension)
	 * 
	 * https://docs.oracle.com/javase/tutorial/java/nutsandbolts/op3.html
	 * 
	 * Rf : https://discuss.leetcode.com/topic/11385/simple-java-solution-bit-shifting
	 */
	public int hammingWeight2(int n) {
		int count = 0;
		while (n != 0) {
			// Also can use "count = count + (n & 1);" to replace if condition
			if ((n & 1) == 1)
				count++;
			n >>>= 1;
		}
		return count;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/27382/java-3-liner-only-iterates-for-1-bits
	 * n &=(n-1) removes the smallest 1 bit in n.
	 */
	public int hammingWeight_forloop(int n) {
	    int i;
		for (i = 0; n != 0; i++, n &= (n - 1))
			;
	    return i;
	}
	
	// https://discuss.leetcode.com/topic/11429/one-line-and-clearly-solution-java
	public int hammingWeight_recursive(int n) {
        return n == 0 ? 0 : 1 + hammingWeight(n & (n - 1));
    }
	
	// https://discuss.leetcode.com/topic/33232/3-line-java-solution
	public int hammingWeight_binaryReplace(int n) {
	    String integer = Integer.toBinaryString(n);
	    integer = integer.replaceAll("0","");
	    return integer.length();
	}
	
	// https://discuss.leetcode.com/topic/12409/super-simple-java-solution
	public int hammingWeight_cheat(int n) {
        return Integer.bitCount(n);
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Number_of_1_Bits number1 = new Number_of_1_Bits();
		int a = 7;
		System.out.println(number1.hammingWeight(a));
		System.out.println(number1.hammingWeight2(a));
		System.out.println(number1.hammingWeight_recursive(a));
		System.out.println(number1.hammingWeight_binaryReplace(a));
		System.out.println(number1.hammingWeight_cheat(a));

	}

}
