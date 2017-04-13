package OJ001_010;

public class Reverse_Integer {
	/*
	 * https://discuss.leetcode.com/topic/15134/very-short-7-lines-and-elegant-solution
	 * 
	 * Ref : 
	 * https://discuss.leetcode.com/topic/45453/java-2ms-9-lines-of-code-easy-understand
	 * https://discuss.leetcode.com/topic/58174/java-2ms-and-simple-using-long-for-overflow-check
	 */
	public int reverse_long(int x) {
        long rev= 0;
        while( x != 0){
            rev= rev*10 + x % 10;
            x= x/10;
            if( rev > Integer.MAX_VALUE || rev < Integer.MIN_VALUE)
                return 0;
        }
        return (int) rev;
    }
	/*
	 * https://discuss.leetcode.com/topic/6104/my-accepted-15-lines-of-code-for-java
	 */
	public int reverse2(int x) {
	    int result = 0;

	    while (x != 0) {
	        int tail = x % 10;
	        int newResult = result * 10 + tail;
	        if ((newResult - tail) / 10 != result) { 
	        	return 0; 
	        }
	        result = newResult;
	        x = x / 10;
	    }

	    return result;
	}
	
	public int reverse(int x) {
		int out = 0;
		while (x != 0) {
			int insert = x % 10;
			//System.out.println(insert);
			System.out.println(out);
			if ((Integer.MAX_VALUE - insert) / 10 < out && insert > 0) {
				return 0;
			}
			else if ((Integer.MIN_VALUE - insert) / 10 > out && insert < 0) {
				return 0;
			}
			out = out * 10 + insert;
			x /= 10;
		}
		return out;
	}
	
	public int reverse_String(int x) {
		StringBuilder reverseNumber = new StringBuilder(x + "");
		if (x >= 0) {
			reverseNumber.reverse();
		}
		else {
			reverseNumber.reverse();
			reverseNumber.deleteCharAt(reverseNumber.length() - 1);
			reverseNumber.insert(0, "-");
		}
		long out = Long.parseLong(reverseNumber.toString());
		if (out > Integer.MAX_VALUE || out < Integer.MIN_VALUE) {
			out = 0;
		}
		return (int)out;
	}
	
	public static void main(String[] args) {
		Reverse_Integer rev = new Reverse_Integer();
		//int x = -123;
		//int x = 1534236469;
		//int x = 2147483647;
		int x = -2147483648;
		//System.out.println(rev.reverse_String(x));
		System.out.println(rev.reverse(x));
	}

}
