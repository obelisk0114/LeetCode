package OJ0041_0050;

/*
 * https://discuss.leetcode.com/topic/15295/seven-short-solutions-1-to-7-lines
 * 
 * Also see the RotateMatrix in amazon package in Coding_Interview
 * https://stackoverflow.com/questions/42519/how-do-you-rotate-a-two-dimensional-array
 */

public class Rotate_Image {
	/*
	 * https://discuss.leetcode.com/topic/6796/a-common-method-to-rotate-the-image/20
	 */
	public void rotate(int[][] matrix) {
		// step1. upside down
		int s = 0, e = matrix.length - 1;
		while (s < e) {
			int[] temp = matrix[s];
			matrix[s] = matrix[e];
			matrix[e] = temp;
			s++;
			e--;
		}

		// step2. transpose
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < i; j++) {
				int temp = matrix[i][j];
				matrix[i][j] = matrix[j][i];
				matrix[j][i] = temp;
			}
		}
	}
	
	/*
	 * https://discuss.leetcode.com/topic/25989/a-simple-and-in-place-solution-in-java
	 * 
	 * Rf : https://discuss.leetcode.com/topic/9744/ac-java-in-place-solution-with-explanation-easy-to-understand/2
	 */
	public void rotate2(int[][] matrix) {
		if (matrix == null || matrix.length <= 1) {
			return;
		}
		int n = matrix.length;
		for (int i = 0; i < n; i++) {
			for (int j = i; j < n; j++) {
				int temp = matrix[i][j];
				matrix[i][j] = matrix[j][i];
				matrix[j][i] = temp;
			}
		}
		for (int i = 0; i < n; i++) {
			int head = 0;
			int tail = n - 1;
			while (head < tail) {
				int temp = matrix[i][head];
				matrix[i][head] = matrix[i][tail];
				matrix[i][tail] = temp;
				head++;
				tail--;
			}
		}
	}
	
	// https://discuss.leetcode.com/topic/8137/share-my-java-solution-192ms
	
	/*
	 * https://discuss.leetcode.com/topic/20212/clear-java-solution
	 * 
	 * (i,j)->(j,n-i)->(n-i,n-j)->(n-j,i)->(i,j)
	 * 
	 * Rf : https://discuss.leetcode.com/topic/5876/my-in-place-solution-in-java
	 */
	public void rotate_rotate(int[][] matrix) {
		int n = matrix.length;
		for (int i = 0; i < n / 2; i++)
			for (int j = i; j < n - i - 1; j++) {
				int tmp = matrix[i][j];
				matrix[i][j] = matrix[n - j - 1][i];
				matrix[n - j - 1][i] = matrix[n - i - 1][n - j - 1];
				matrix[n - i - 1][n - j - 1] = matrix[j][n - i - 1];
				matrix[j][n - i - 1] = tmp;
			}
	}
	
	/*
	 * https://discuss.leetcode.com/topic/12925/8-lines-java-solution
	 * 
	 * The idea is to loop through the top-left quadrant of the matrix (including the 
	 * middle column for odd N), and for each element make 4-element swap (rotation).
	 */
	public void rotate_rotate2(int[][] M) {
		for (int i = 0; i < (M.length + 1) / 2; i++) {
			for (int j = 0; j < M.length / 2; j++) {
				int tmp = M[i][j];
				M[i][j] = M[M.length - j - 1][i];
				M[M.length - j - 1][i] = M[M.length - i - 1][M.length - j - 1];
				M[M.length - i - 1][M.length - j - 1] = M[j][M.length - i - 1];
				M[j][M.length - i - 1] = tmp;
			}
		}
	}
	
	/*
	 * by myself
	 * 
	 * Rf : https://discuss.leetcode.com/topic/1032/in-place-solution/6
	 */
	public void rotate_self(int[][] matrix) {
        for (int i = 0; i < matrix.length / 2; i++) {
            for (int j = i; j < matrix[0].length - 1 - i; j++) {
                int row1 = i;
                int col1 = j;
                int row2 = j;
                int col2 = matrix[0].length - 1 - i;
                swap(matrix, row1, col1, row2, col2);
                row2 = matrix[0].length - 1 - i;
                col2 = matrix[0].length - 1 - j;
                swap(matrix, row1, col1, row2, col2);
                row2 = matrix[0].length - 1 - j;
                col2 = i;
                swap(matrix, row1, col1, row2, col2);
            }
        }
    }
    
    private void swap(int[][] matrix, int row1, int col1, int row2, int col2) {
        int tmp = matrix[row1][col1];
        matrix[row1][col1] = matrix[row2][col2];
        matrix[row2][col2] = tmp;
    }

}
