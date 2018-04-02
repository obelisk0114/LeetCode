package OJ0071_0080;

import java.util.Arrays;

public class Set_Matrix_Zeroes {
	/*
	 * https://leetcode.com/problems/set-matrix-zeroes/discuss/26008/My-AC-java-O(1)-solution-(easy-to-read)
	 * 
	 * Use the first column and the first row as marker
	 * 
	 * Rf :
	 * https://leetcode.com/problems/set-matrix-zeroes/discuss/26037/O(1)-JAVA-straightforward-idea
	 * https://leetcode.com/problems/set-matrix-zeroes/discuss/26014/Any-shorter-O(1)-space-solution
	 */
	public void setZeroes(int[][] matrix) {
		// fr = first row; fc = first col
		boolean fr = false, fc = false;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if (matrix[i][j] == 0) {
					if (i == 0)
						fr = true;
					if (j == 0)
						fc = true;
					matrix[0][j] = 0;
					matrix[i][0] = 0;
				}
			}
		}
		
		for (int i = 1; i < matrix.length; i++) {
			for (int j = 1; j < matrix[0].length; j++) {
				if (matrix[i][0] == 0 || matrix[0][j] == 0) {
					matrix[i][j] = 0;
				}
			}
		}
		
		if (fr) {
			for (int j = 0; j < matrix[0].length; j++) {
				matrix[0][j] = 0;
			}
		}
		if (fc) {
			for (int i = 0; i < matrix.length; i++) {
				matrix[i][0] = 0;
			}
		}

	}
	
	/*
	 * https://leetcode.com/problems/set-matrix-zeroes/discuss/26087/Java-easy-to-understand-O(1)-space-solution-with-2-passes
	 * 
	 * a b b
	 * b c c
	 * b c c
	 * 
	 * Step1: Determine row1 and col1. Need to go through the first col and first row. 
	 *        Use two vars to store that information.
	 * Step2: Use "c" to determine "b". Need to go through the entire matrix. 
	 *        Once "c" is zero, set its corresponding two "b"s to zero.
	 * Step3: Use "b" to set "c". If "b" is zero, 
	 *        its corresponding row or col are set to all zero.
	 * Step4: Use previous row1 and col1 information to set col1 and row1.
	 * 
	 * Rf : https://leetcode.com/problems/set-matrix-zeroes/discuss/26151/Constant-Space-Java-solution
	 */
	public void setZeroes2(int[][] matrix) {
		if (matrix == null || matrix.length == 0) {
			return;
		}

		boolean setFirstRowToZeroes = false;
		boolean setFirstColumnToZeroes = false;

		// check if first column needs to be set to zero
		for (int row = 0; row < matrix.length; row++) {
			if (matrix[row][0] == 0) {
				setFirstColumnToZeroes = true;
				break;
			}
		}

		// check if first row needs to be set to zero
		for (int col = 0; col < matrix[0].length; col++) {
			if (matrix[0][col] == 0) {
				setFirstRowToZeroes = true;
				break;
			}
		}

		// mark columns and rows to be set to zero
		for (int row = 1; row < matrix.length; row++) {
			for (int col = 1; col < matrix[0].length; col++) {
				if (matrix[row][col] == 0) {
					matrix[row][0] = 0;
					matrix[0][col] = 0;
				}
			}
		}

		// make rows zero
		for (int row = 1; row < matrix.length; row++) {
			if (matrix[row][0] == 0) {
				for (int col = 1; col < matrix[0].length; col++) {
					matrix[row][col] = 0;
				}
			}
		}

		// make columns zero
		for (int col = 1; col < matrix[0].length; col++) {
			if (matrix[0][col] == 0) {
				for (int row = 1; row < matrix.length; row++) {
					matrix[row][col] = 0;
				}
			}
		}

		// zero out first row (if needed)
		if (setFirstRowToZeroes) {
			for (int col = 0; col < matrix[0].length; col++) {
				matrix[0][col] = 0;
			}
		}

		// zero out first column (if needed)
		if (setFirstColumnToZeroes) {
			for (int row = 0; row < matrix.length; row++) {
				matrix[row][0] = 0;
			}
		}

	}
	
	// https://leetcode.com/problems/set-matrix-zeroes/discuss/26115/JavaPython-O(1)-space-11-lines-solution
	public void setZeroes_go_first_row(int[][] matrix) {
		int m = matrix.length, n = matrix[0].length, k = 0;
		
		// First row has zero?
		while (k < n && matrix[0][k] != 0)
			++k;
		
		// Use first row/column as marker, scan the matrix
		for (int i = 1; i < m; ++i)
			for (int j = 0; j < n; ++j)
				if (matrix[i][j] == 0)
					matrix[0][j] = matrix[i][0] = 0;
		
		// Set the zeros
		for (int i = 1; i < m; ++i)
			for (int j = n - 1; j >= 0; --j)
				if (matrix[0][j] == 0 || matrix[i][0] == 0)
					matrix[i][j] = 0;
		
		// Set the zeros for the first row
		if (k < n)
			Arrays.fill(matrix[0], 0);
	}

}
