package OJ0191_0200;

public class House_Robber {
	/*
	 * https://leetcode.com/problems/house-robber/discuss/55695/JAVA-DP-Solution-O(n)-runtime-and-O(1)-space-with-inline-comment
	 * 
	 * f(0) = nums[0]
	 * f(1) = max(num[0], num[1])
	 * f(k) = max( f(k-2) + nums[k], f(k-1) )
	 * 
	 * Rf :
	 * https://leetcode.com/problems/house-robber/discuss/55838/DP-O(N)-time-O(1)-space-with-easy-to-understand-explanation
	 * https://leetcode.com/problems/house-robber/discuss/55696/Python-solution-3-lines.
	 * https://leetcode.com/problems/house-robber/discuss/55681/Java-O(n)-solution-space-O(1)
	 */
	public int rob(int[] num) {
		int rob = 0; // max monney can get if rob current house
		int notrob = 0; // max money can get if not rob current house
		for (int i = 0; i < num.length; i++) {
			int currob = notrob + num[i]; // if rob current value, previous house must not be robbed
			notrob = Math.max(notrob, rob); // if not rob ith house, take the max value of robbed (i-1)th house and not rob (i-1)th house
			rob = currob;
		}
		return Math.max(rob, notrob);
	}
	
	/*
	 * by myself
	 * 
	 * Rf : 
	 * https://leetcode.com/problems/house-robber/discuss/55799/Easy-java-solution-using-DP
	 * https://leetcode.com/problems/house-robber/discuss/55761/Java-0ms-solution-using-Dynamic-Programming
	 */
	public int rob_self(int[] nums) {
        if (nums.length == 0)
            return 0;
        
        if (nums.length == 1)
            return nums[0];
        
        int[] sum = new int[nums.length];
        
        sum[0] = nums[0];
        sum[1] = Math.max(nums[0], nums[1]);
        
        for (int i = 2; i < nums.length; i++) {
            sum[i] = Math.max(sum[i - 1], nums[i] + sum[i - 2]);
        }
        return sum[sum.length - 1];
    }
	
	/*
	 * https://leetcode.com/problems/house-robber/discuss/55679/O(1)-space-Java-and-C++
	 * 
	 * pre is the max value at i-2 and cur is the max value at i-1. 
	 * Update pre and cur after you get temp, which is the max value at i.
	 */
	public int rob_pre_cur(int[] nums) {
        int pre = 0, cur = 0;
        for (int num : nums) {
            final int temp = Integer.max(pre + num, cur);
            pre = cur;
            cur = temp;
        }
        return cur;
    }
	
	/*
	 * https://leetcode.com/problems/house-robber/discuss/55693/C-1ms-O(1)space-very-simple-solution
	 * 
	 * you could consider a as previous max num at even, b as previous max num at odd.
	 * If we rob the even one, than we can not rob the odd one, and vice versa. 
	 * So 'a' is even, and 'b' is odd.
	 * 
	 * Rf :
	 * leetcode.com/problems/house-robber/discuss/55693/C-1ms-O(1)space-very-simple-solution/57408
	 * leetcode.com/problems/house-robber/discuss/55693/C-1ms-O(1)space-very-simple-solution/57412
	 * leetcode.com/problems/house-robber/discuss/55693/C-1ms-O(1)space-very-simple-solution/57383
	 */
	public int rob_even_odd(int[] nums) {
		int a = 0;
		int b = 0;
		for (int i = 0; i < nums.length; i++) {
			if (i % 2 == 0) {
				a = Math.max(a + nums[i], b);
			} 
			else {
				b = Math.max(a, b + nums[i]);
			}
		}
		return Math.max(a, b);
	}

}
