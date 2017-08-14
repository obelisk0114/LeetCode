package OJ0331_0340;

public class Counting_Bits {
	/*
	 * https://discuss.leetcode.com/topic/40162/three-line-java-solution
	 * 
	 * f[i] = f[i / 2] + i % 2
	 * Right shit by 1 bit, compare to previously, the number of set bit would either 
	 * reduce by 1(when number is odd) or no change(when number is even), 
	 * that is why we add ( i & 1 ) which reflects whether the number is even or odd.
	 * 
	 * Rf :
	 * https://discuss.leetcode.com/topic/40162/three-line-java-solution/16
	 * https://discuss.leetcode.com/topic/40162/three-line-java-solution/20
	 * https://discuss.leetcode.com/topic/52983/share-my-thinking-a-java-solution
	 */
	public int[] countBits(int num) {
		int[] f = new int[num + 1];
		for (int i = 1; i <= num; i++)
			f[i] = f[i >> 1] + (i & 1);
		return f;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/43202/easy-understanding-dp-bit-java-solution
	 * Get the previous result with one less 1 and plus 1 to get the result.
	 */
	public int[] countBits2(int num) {
		int[] result = new int[num + 1];
		result[0] = 0;
		for (int i = 1; i <= num; i++) {
			result[i] = result[i & (i - 1)] + 1;
		}
		return result;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/40195/how-we-handle-this-question-on-interview-thinking-process-dp-solution
	 * 
	 * Rf : https://discuss.leetcode.com/topic/41785/simple-java-o-n-solution-using-two-pointers
	 */
	public int[] countBits3(int num) {
	    int result[] = new int[num + 1];
	    int offset = 1;
	    for (int index = 1; index < num + 1; index++){
	        if (offset * 2 == index){
	            offset *= 2;
	        }
	        result[index] = result[index - offset] + 1;
	    }
	    return result;
	}

	public int[] countBits_sizeof_n(int num) {
		int[] count = new int[num + 1];
		for (int i = 1; i <= num; i++) {
            int tmp = i;
            int cur = 0;
            while (tmp != 0) {
                if ((tmp & 1) == 1) {
                    cur++;
                }
                tmp >>= 1;
            }
            count[i] = cur;
        }
        return count;
    }
	
	public int[] countBits_cheat(int num) {
        int[] count = new int[num + 1];
        for (int i = 1; i <= num; i++) {
            count[i] = Integer.bitCount(i);
        }
        return count;
    }
	
	public static void main(String[] args) {
		int n = -1;
		System.out.println("binary representation = " + Integer.toBinaryString(n));
		System.out.println("bitCount = " + Integer.bitCount(n));
	}

}
