package OJ0361_0370;

public class Valid_Perfect_Square {
	// https://discuss.leetcode.com/topic/49372/java-three-solutions-1-3-5-sequence-binary-search-newton
	
	// https://discuss.leetcode.com/topic/49325/a-square-number-is-1-3-5-7-java-code
	public boolean isPerfectSquare(int num) {
		int i = 1;
		while (num > 0) {
			num -= i;
			i += 2;
		}
		return num == 0;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/49342/3-4-short-lines-integer-newton-most-languages
	 * 
	 * Rf : https://discuss.leetcode.com/topic/49511/is-the-newton-s-iteration-really-o-1
	 */
	public boolean isPerfectSquare_Newton(int x) {
		long r = x;
		while (r * r > x)
			r = (r + x / r) / 2;
		return r * r == x;
	}
	
	// https://discuss.leetcode.com/topic/49347/java-binary-search-solution-the-obvious-one
	public boolean isPerfectSquare_long_type(int num) {
		if (num == 1)
			return true;

		long low = 1, high = num / 2, mid = 0;
		long nums = (long) num;
		while (low <= high) {
			mid = low + (high - low) / 2;

			if ((mid * mid) == nums)
				return true;
			else if ((mid * mid) < nums)
				low = mid + 1;
			else
				high = mid - 1;
		}
		return false;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/49727/0ms-binary-search-based-solution
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/51169/java-binary-search-o-lgn-solution-without-using-long-type-to-avoid-overflow
	 * https://discuss.leetcode.com/topic/54206/java-binary-search-without-using-long
	 */
	public boolean isPerfectSquare_without_long(int x) {
		if (x <= 0)
			throw new IllegalArgumentException();
		if (x == 1)
			return true;
		int hi = x;
		int lo = 1;
		while (lo <= hi) {
			int mid = lo + (hi - lo) / 2;
			if (mid * mid == x)
				return true;
			int div = x / mid;
			if (div >= mid) {
				lo = mid + 1;
			}
			else {              // if (div < mid)
				hi = mid - 1;
			}
		}
		return false;
	}
	
	// https://discuss.leetcode.com/topic/51620/o-1-java-solution
	public boolean isPerfectSquare_bit_operation(int num) {
		int root = 0, bit = 1 << 15;
		while (bit > 0) {
			root |= bit;
			if (root > num / root) {    // if root * root > num
				root ^= bit;            // set the bit back to 0
			}
			bit >>= 1;
		}
		return root * root == num;
	}
	
	// https://discuss.leetcode.com/topic/49893/is-it-ok-to-use-log
	public boolean isPerfectSquare_log(int num) {
		int sqrt = (int) Math.exp(Math.log(num) / 2);
		return sqrt * sqrt == num || (sqrt + 1) * (sqrt + 1) == num;
	}
	
	// cheat
	public boolean isPerfectSquare_cheat(int num) {
        int root = (int) Math.sqrt(num);
        if (root * root == num)
            return true;
        else
            return false;
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Valid_Perfect_Square perfectSquare = new Valid_Perfect_Square();
		int n = 2147395600;
		System.out.println("Answer = " + perfectSquare.isPerfectSquare_cheat(n));
		System.out.println("Newton = " + perfectSquare.isPerfectSquare_Newton(n));
		System.out.println("int = " + perfectSquare.isPerfectSquare_without_long(n));
		System.out.println("long type = " + perfectSquare.isPerfectSquare_long_type(n));
		System.out.println("bit = " + perfectSquare.isPerfectSquare_bit_operation(n));
		System.out.println("log = " + perfectSquare.isPerfectSquare_log(n));

	}

}
