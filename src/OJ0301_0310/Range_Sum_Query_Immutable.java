package OJ0301_0310;

public class Range_Sum_Query_Immutable {
	/*
	 * Rf :
	 * https://leetcode.com/articles/range-sum-query-immutable/
	 * https://leetcode.com/problems/range-sum-query-immutable/discuss/75184/5-lines-C++-4-lines-Python
	 */
	private int[] prefixSum;

    public Range_Sum_Query_Immutable(int[] nums) {
        prefixSum = new int[nums.length + 1];
        for (int i = 0; i < nums.length; i++) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }
    }
    
    public int sumRange(int i, int j) {
        return prefixSum[j + 1] - prefixSum[i];
    }
	
	/*
	 * The following class and functions are from this link.
	 * https://leetcode.com/problems/range-sum-query-immutable/discuss/75309/Java-solution-using-sum-array-built-in-constructor.
	 * 
	 * Rf :
	 * https://leetcode.com/problems/range-sum-query-immutable/discuss/75194/Explanation-with-Images 
	 * https://leetcode.com/problems/range-sum-query-immutable/discuss/75192/Java-simple-O(n)-init-and-O(1)-query-solution
	 */
	public class NumArray {
		int[] sum;

		public NumArray(int[] nums) {
			sum = new int[nums.length];
			if (nums.length > 0)
				sum[0] = nums[0];
			for (int i = 1; i < nums.length; i++) {
				sum[i] = sum[i - 1] + nums[i];
			}
		}

		public int sumRange(int i, int j) {
			if (i == 0)
				return sum[j];
			return sum[j] - sum[i - 1];
		}
	}
	
	// https://leetcode.com/problems/range-sum-query-immutable/discuss/75314/Solutions-using-Binary-Indexed-Tree-and-Segment-Tree

}
