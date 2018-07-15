package OJ0301_0310;

public class Range_Sum_Query_2D_Immutable {
	/*
	 * The following variable and 2 functions are from this link.
	 * https://leetcode.com/problems/range-sum-query-2d-immutable/discuss/75358/Clean-and-easy-to-understand-java-solution
	 * 
	 * Rf :
	 * https://leetcode.com/articles/range-sum-query-2d-immutable/
	 * https://leetcode.com/problems/range-sum-query-2d-immutable/discuss/75350/Clean-C++-Solution-and-Explaination-O(mn)-space-with-O(1)-time
	 * https://leetcode.com/problems/range-sum-query-2d-immutable/discuss/75400/Summed-Area-Table-a.k.a.-Integral-Image
	 * 
	 * Other code:
	 * https://leetcode.com/problems/range-sum-query-2d-immutable/discuss/75398/My-simple-Java-solution
	 */
	private int[][] dp;

	public Range_Sum_Query_2D_Immutable(int[][] matrix) {
	    if(   matrix           == null
	       || matrix.length    == 0
	       || matrix[0].length == 0   ){
	        return;   
	    }
	    
		int m = matrix.length;
		int n = matrix[0].length;

		dp = new int[m + 1][n + 1];
		for (int i = 1; i <= m; i++) {
			for (int j = 1; j <= n; j++) {
				dp[i][j] = dp[i - 1][j] + dp[i][j - 1] - dp[i - 1][j - 1] + matrix[i - 1][j - 1];
			}
		}
	}

	public int sumRegion(int row1, int col1, int row2, int col2) {
		return dp[row2 + 1][col2 + 1] - dp[row1][col2 + 1] - dp[row2 + 1][col1] + dp[row1][col1];
	}
	
	/*
	 * The following class and functions are from this link.
	 * https://leetcode.com/problems/range-sum-query-2d-immutable/discuss/75357/Very-clean-and-fast-java-solution
	 */
	class NumMatrix {
		private int[][] sumRegion;

		public NumMatrix(int[][] matrix) {
			if (matrix.length != 0)
				sumRegion = new int[matrix.length + 1][matrix[0].length + 1];

			for (int i = 0; i < matrix.length; i++) {
				int sum = 0;
				for (int j = 0; j < matrix[0].length; j++) {
					sum += matrix[i][j];
					sumRegion[i + 1][j + 1] = sum + sumRegion[i][j + 1];
				}
			}
		}

		public int sumRegion(int row1, int col1, int row2, int col2) {
			return sumRegion[row2 + 1][col2 + 1] - sumRegion[row1][col2 + 1] 
					- sumRegion[row2 + 1][col1] + sumRegion[row1][col1];
		}
	}
	
	/*
	 * The following class and functions are by myself.
	 * 
	 * Other code :
	 * https://leetcode.com/articles/range-sum-query-2d-immutable/
	 * https://leetcode.com/problems/range-sum-query-2d-immutable/discuss/75395/Simple-Java-solution
	 */
	class NumMatrix_self {
	    private int[][] prefixSum;

	    public NumMatrix_self(int[][] matrix) {
	        if (matrix.length == 0 || matrix[0].length == 0)
	            return;
	        
	        prefixSum = new int[matrix.length][matrix[0].length + 1];
	        for (int i = 0; i < prefixSum.length; i++) {
	            for (int j = 1; j < prefixSum[i].length; j++) {
	                prefixSum[i][j] = prefixSum[i][j - 1] + matrix[i][j - 1];
	            }
	        }
	    }
	    
	    public int sumRegion(int row1, int col1, int row2, int col2) {
	        int sum = 0;
	        for (int i = row1; i <= row2; i++) {
	            sum = sum + prefixSum[i][col2 + 1] - prefixSum[i][col1];
	        }
	        return sum;
	    }
	}
	
	// https://leetcode.com/problems/range-sum-query-2d-immutable/discuss/75381/C++-with-helper

	/**
	 * Your NumMatrix object will be instantiated and called as such:
	 * NumMatrix obj = new NumMatrix(matrix);
	 * int param_1 = obj.sumRegion(row1,col1,row2,col2);
	 */

}
