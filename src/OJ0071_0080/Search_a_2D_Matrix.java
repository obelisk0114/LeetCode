package OJ0071_0080;

public class Search_a_2D_Matrix {
	// https://leetcode.com/problems/search-a-2d-matrix/discuss/26219/Binary-search-on-an-ordered-matrix
	public boolean searchMatrix1(int[][] matrix, int target) {
		if (matrix.length == 0 || matrix[0].length == 0)
			return false;

		int row_num = matrix.length;
		int col_num = matrix[0].length;

		int begin = 0, end = row_num * col_num - 1;

		while (begin <= end) {
			int mid = (begin + end) / 2;
			int mid_value = matrix[mid / col_num][mid % col_num];

			if (mid_value == target) {
				return true;
			} 
			else if (mid_value < target) {
				// Should move a bit further, otherwise dead loop.
				begin = mid + 1;
			} 
			else {
				end = mid - 1;
			}
		}
		return false;
	}
	
	// modify by myself
	public boolean searchMatrix_self_modify(int[][] matrix, int target) {
        if (matrix.length == 0 || matrix[0].length == 0)
			return false;

		int row_num = matrix.length;
		int col_num = matrix[0].length;

		int begin = 0, end = row_num * col_num;

		while (begin < end) {
			int mid = (begin + end) / 2;
			int mid_value = matrix[mid / col_num][mid % col_num];

			if (mid_value == target) {
				return true;
			} 
			else if (mid_value < target) {
				// Should move a bit further, otherwise dead loop.
				begin = mid + 1;
			} 
			else {
				end = mid;
			}
		}
		return false;
    }
	
	// by myself
	public boolean searchMatrix_self(int[][] matrix, int target) {
        if (matrix.length == 0 || matrix[0].length == 0)
            return false;
        
        int start = 0;
        int end = matrix.length * matrix[0].length - 1;
        while (start <= end) {
            int mid = start + (end - start) / 2;
            int row = mid / matrix[0].length;
            int col = mid % matrix[0].length;
            
            if (matrix[row][col] == target) {
                return true;
            }
            else if (matrix[row][col] > target) {
                end = mid - 1;
            }
            else {
                start = mid + 1;
            }
        }
        return false;
    }
	
	/*
	 * https://leetcode.com/problems/search-a-2d-matrix/discuss/26353/CLEAR-O(lgm-+-lgn)-JAVA-Solution-based-on-Binary-Search
	 * 
	 * Imagine a one-dimension sorted array composed of row by row of the matrix.
	 * Runtime = O(lg(m*n)) = O(lgm + lgn)
	 * 
	 * Rf : https://leetcode.com/problems/search-a-2d-matrix/discuss/26220/Don't-treat-it-as-a-2D-matrix-just-treat-it-as-a-sorted-list
	 */
	public boolean searchMatrix_binarySearch(int[][] matrix, int target) {
		if (matrix.length == 0 || matrix[0].length == 0)
			return false;
		
		int n = matrix.length, m = matrix[0].length, start = 0, end = m * n - 1;
		while (start < end) {
			int mid = (start + end) / 2;
			if (matrix[mid / m][mid % m] == target)
				return true;
			else if (matrix[mid / m][mid % m] > target)
				end = mid;
			else
				start = mid + 1;
		}
		return (matrix[start / m][start % m] == target);
	}
	
	/*
	 * https://leetcode.com/problems/search-a-2d-matrix/discuss/26292/Java-clear-solution
	 * 
	 * The basic idea is from right corner, if the current number greater than target 
	 * col - 1 in same row, else if the current number less than target, row + 1 in 
	 * same column, finally if they are same, we find it, and return true.
	 */
	public boolean searchMatrix_step(int[][] matrix, int target) {
		if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
			return false;
		}
		
		int i = 0, j = matrix[0].length - 1;
		while (i < matrix.length && j >= 0) {
			if (matrix[i][j] == target) {
				return true;
			} 
			else if (matrix[i][j] > target) {
				j--;
			} 
			else {
				i++;
			}
		}
		return false;
	}
	
	// https://leetcode.com/problems/search-a-2d-matrix/discuss/26216/Test-case-may-be-too-weak-!!!
	public boolean searchMatrix_brute_forEach(int[][] matrix, int target) {
		for (int[] row : matrix) {
			for (int item : row)
				if (item == target)
					return true;
		}
		return false;
	}
	
	// https://leetcode.com/problems/search-a-2d-matrix/discuss/26216/Test-case-may-be-too-weak-!!!
	public boolean searchMatrix_brute(int[][] matrix, int target) {
		int m = matrix.length;
		if (m < 1)
			return false;
		int n = matrix[0].length;

		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++)
				if (matrix[i][j] == target)
					return true;
		}
		return false;
	}

}
