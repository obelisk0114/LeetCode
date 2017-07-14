package OJ0451_0460;

public class Minimum_Moves_to_Equal_Array_Elements {
	/*
	 * https://discuss.leetcode.com/topic/66557/java-o-n-solution-short
	 * 
	 * Adding 1 to n - 1 elements is the same as subtracting 1 from one element.
	 * Best way to do this is make all the elements in the array equal to the min element.
	 * 
	 * sum(array) - n * minimum
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/66771/what-if-we-are-not-smart-enough-to-come-up-with-decrease-1-here-is-how-we-do-it
	 * https://discuss.leetcode.com/topic/66737/it-is-a-math-question
	 */
	public int minMoves(int[] nums) {
		if (nums.length == 0)
			return 0;
		int min = nums[0];
		for (int n : nums)
			min = Math.min(min, n);
		int res = 0;
		for (int n : nums)
			res += n - min;
		return res;
    }
	
	/*
	 * https://discuss.leetcode.com/topic/83312/java-one-loop-5-line-solution-beats-99
	 * 
	 * The effect of increasing n-1 elements by one is equal to 
	 * decrease the left element by one. 
	 * The goal is to decrease every element larger than the minimum element 
	 * to the minimum element. 
	 * So, the number of operation needed is sum-min*(nums.length).
	 */
	public int minMoves2(int[] nums) {
	    int total = 0, min = nums[0];
	    for (int n  : nums) {
	        total += n;
	        if (n < min) min = n;
	    }
	    return total - nums.length * min;
	}
	
	// https://discuss.leetcode.com/topic/66575/thinking-process-of-solving-problems-use-java-37ms

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Minimum_Moves_to_Equal_Array_Elements minimumMoveN = new Minimum_Moves_to_Equal_Array_Elements();
		int[] a = {0, 5, 1, 0, -9, 7, 10};
		System.out.println(minimumMoveN.minMoves(a));
		System.out.println(minimumMoveN.minMoves2(a));

	}

}
