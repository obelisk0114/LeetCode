package OJ1271_1280;

public class Count_Square_Submatrices_with_All_Ones {
	/*
	 * https://leetcode.com/problems/count-square-submatrices-with-all-ones/discuss/643380/Java-3-Lines-algorithm-solution-%2B-easy-explanation!-O(N*M)-DP
	 * 
	 * We can do Dynamic Programming by saving how many squares can be formed using 
	 * the bottom right corner element.
	 * After summing all indices, now we get the correct answer!
	 * 
	 * This is similar to the problem of finding the max size of square having all 1s
	 * 
	 * mat[i][j] = min(mat[i][j - 1], mat[i - 1][j], mat[i - 1][j - 1]) + 1;
	 * mat[i][j]: the max side length of square that bottom-right corner is (i, j)
	 * 
	 * For n x n square matrix, each of its neighbors at the top, left, and top-left 
	 * corner should at-least have size of (n-1)x(n-1).
	 * 
	 * Final result is sum of all the values in the dp matrix
	 * 
	 * If the current value is 3, it means that the current position is utilized 3 
	 * times - for squares of sizes 1, 2 and 3
	 * So we get the sum of the matrix to find the total no of squares of all sizes 
	 * having all 1s
	 * 
	 * Rf :
	 * https://leetcode.com/problems/count-square-submatrices-with-all-ones/discuss/441431/Python-DP-Solution-with-Explaination
	 * https://leetcode.com/problems/count-square-submatrices-with-all-ones/discuss/447967/JAVA-DP-7ms-with-explanation-O(m*n)-time-100-space
	 * https://leetcode.com/problems/count-square-submatrices-with-all-ones/discuss/443314/Java-DP-with-explanation
	 * 
	 * Other code:
	 * https://leetcode.com/problems/count-square-submatrices-with-all-ones/discuss/441312/Java-Simple-DP-solution
	 */
	public int countSquares_matrix(int[][] matrix) {
		int mat[][] = new int[matrix.length + 1][matrix[0].length + 1];
		int sum = 0;

		for (int i = 1; i <= matrix.length; i++)
			for (int j = 1; j <= matrix[0].length; j++)
				if (matrix[i - 1][j - 1] != 0) {
					// Find the max size of squares that can be formed using the 
					// current square
					mat[i][j] = Math.min(Math.min(mat[i - 1][j], mat[i][j - 1]), 
							mat[i - 1][j - 1]) + 1;
					
					sum += mat[i][j];
				}

		return sum;
	}
	
	/*
	 * https://leetcode.com/problems/count-square-submatrices-with-all-ones/discuss/441306/JavaC%2B%2BPython-DP-solution
	 * 
	 * dp[i][j] means the size of biggest square with A[i][j] as bottom-right corner.
	 * dp[i][j] also means the number of squares with A[i][j] as bottom-right corner.
	 * 
	 * If A[i][j] == 0, no possible square.
	 * If A[i][j] == 1,
	 * we compare the size of square dp[i-1][j-1], dp[i-1][j] and dp[i][j-1].
	 * min(dp[i-1][j-1], dp[i-1][j], dp[i][j-1]) + 1 is the maximum size of square 
	 * that we can find.
	 * 
	 * For example:
	 * 1, 1, 1     dp array    1 1 1
	 * 1, 1, 1    ==========>  1 2 2
	 * 1, 1, 1                 1 2 3
	 * 
	 * dp[0][0] = 1
	 * dp[1][1] = 2 (the square including A[1][1] can be square with element A[1][1] 
	 *               with size: 2 * 2, 1 * 1)
	 * dp[2][2] = 3 (the square including A[2][2] can be square with element A[2][2] 
	 *               with size: 3 * 3, 2 * 2, 1 * 1)
	 * 
	 * Rf :
	 * https://leetcode.com/problems/count-square-submatrices-with-all-ones/discuss/441306/JavaC++Python-DP-solution/472392
	 * https://leetcode.com/problems/count-square-submatrices-with-all-ones/discuss/441306/JavaC++Python-DP-solution/549695
	 * https://leetcode.com/problems/count-square-submatrices-with-all-ones/discuss/459963/Java-explained-DP-O(m*n)
	 */
	public int countSquares2(int[][] A) {
		int res = 0;
		for (int i = 0; i < A.length; ++i) {
			for (int j = 0; j < A[0].length; ++j) {
				if (A[i][j] > 0 && i > 0 && j > 0) {
					A[i][j] = Math.min(A[i - 1][j - 1], 
							Math.min(A[i - 1][j], A[i][j - 1])) + 1;
				}
				res += A[i][j];
			}
		}
		return res;
	}
	
	/**
     * Python collections
     * 
     * https://leetcode.com/problems/count-square-submatrices-with-all-ones/discuss/441620/DP-with-figure-explanation
     * https://leetcode.com/problems/count-square-submatrices-with-all-ones/discuss/643429/Python-DP-Solution-%2B-Thinking-Process-Diagrams-(O(mn)-runtime-O(1)-space)
     * https://leetcode.com/problems/count-square-submatrices-with-all-ones/discuss/518072/Python-O(-m*n-)-sol-by-DP-93%2B-w-Demo
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/count-square-submatrices-with-all-ones/discuss/643692/C%2B%2B-DP-%2B-Iterative-Easy-solution
     * https://leetcode.com/problems/count-square-submatrices-with-all-ones/discuss/441414/C%2B%2B-Intuitive-Solution-Recursion-With-Memoization
     * https://leetcode.com/problems/count-square-submatrices-with-all-ones/discuss/553716/C%2B%2B-solution-DP
     */
	
	/**
	 * JavaScript collections
	 * 
	 * https://leetcode.com/problems/count-square-submatrices-with-all-ones/discuss/442184/JavaScript-Easy-to-understand-2-solutions
	 * https://leetcode.com/problems/count-square-submatrices-with-all-ones/discuss/644067/Javascript-DP-solution-with-explanation-O(N*M)
	 */

}
