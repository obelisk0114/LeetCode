package OJ0051_0060;

public class Spiral_Matrix_II {
	/*
	 * https://discuss.leetcode.com/topic/4362/my-super-simple-solution-can-be-used-for-both-spiral-matrix-i-and-ii
	 * 
	 * Rf : https://discuss.leetcode.com/topic/15400/share-my-java-solution
	 */
	public int[][] generateMatrix(int n) {
		// Declaration
		int[][] matrix = new int[n][n];

		// Edge Case
		if (n == 0) {
			return matrix;
		}

		// Normal Case
		int rowStart = 0;
		int rowEnd = n - 1;
		int colStart = 0;
		int colEnd = n - 1;
		int num = 1; // change

		while (rowStart <= rowEnd && colStart <= colEnd) {
			for (int i = colStart; i <= colEnd; i++) {
				matrix[rowStart][i] = num++; // change
			}
			rowStart++;

			for (int i = rowStart; i <= rowEnd; i++) {
				matrix[i][colEnd] = num++; // change
			}
			colEnd--;

			for (int i = colEnd; i >= colStart; i--) {
				if (rowStart <= rowEnd)
					matrix[rowEnd][i] = num++; // change
			}
			rowEnd--;

			for (int i = rowEnd; i >= rowStart; i--) {
				if (colStart <= colEnd)
					matrix[i][colStart] = num++; // change
			}
			colStart++;
		}

		return matrix;
	}
	
	// Self
	public int[][] generateMatrix2(int n) {
		int[][] out = new int[n][n];
		if (n <= 0)
			return out;
		int top = 0;
		int bottom = n-1;
		int left = 0;
		int right = n-1;
		int count = 1;
		int direction = 0;
		
		while (top <= bottom && left <= right) {
			if (direction == 0) {
				for (int i = left; i <= right; i++) {
					out[top][i] = count;
					count++;
				}
				top++;
			}
			else if (direction == 1) {
				for (int i = top; i <= bottom; i++) {
					out[i][right] = count;
					count++;
				}
				right--;
			}
			else if (direction == 2) {
				for (int i = right; i >= left; i--) {
					out[bottom][i] = count;
					count++;
				}
				bottom--;
			}
			else {
				for (int i = bottom; i >= top; i--) {
					out[i][left] = count;
					count++;
				}
				left++;
			}
			direction = (direction + 1) % 4;
		}
		
		return out;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/3628/a-better-solution-than-switching-directions
	 * https://discuss.leetcode.com/topic/2204/any-better-solution-than-switching-directions
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Spiral_Matrix_II generateSpiral = new Spiral_Matrix_II();
		int a = 5;
		int[][] ans = generateSpiral.generateMatrix(a);
		for (int i = 0; i < a; i++) {
			for (int j = 0; j < a; j++) {
				System.out.print(ans[i][j] + " ");
			}
			System.out.println();
		}
	}

}
