package OJ0231_0240;

/*
 * https://stackoverflow.com/questions/2457792/how-do-i-search-for-a-number-in-a-2d-array-sorted-left-to-right-and-top-to-botto/2458113#2458113
 * http://twistedoakstudios.com/blog/Post5365_searching-a-sorted-matrix-faster
 * https://articles.leetcode.com/searching-2d-sorted-matrix-part-ii/
 * https://leetcode.com/problems/search-a-2d-matrix-ii/discuss/66154/Is-there's-a-O(log(m)+log(n))-solution-I-know-O(n+m)-and-O(m*log(n))
 */

public class Search_a_2D_Matrix_II {
	/*
	 * https://leetcode.com/problems/search-a-2d-matrix-ii/discuss/66160/AC-clean-Java-solution
	 * 
	 * If we stand on the top-right corner of the matrix and look diagonally, 
	 * it's kind of like a BST, we can go through this matrix to find the target 
	 * like how we traverse the BST.
	 * 
	 * If the target is greater than the value in current position, then the target 
	 * can not be in entire row of current position because the row is sorted, 
	 * if the target is less than the value in current position, then the target can 
	 * not in the entire column because the column is sorted too. 
	 * We can rule out one row or one column each time, so time complexity is O(m+n).
	 * 
	 * Rf : 
	 * https://leetcode.com/problems/search-a-2d-matrix-ii/discuss/66140/My-concise-O(m+n)-Java-solution
	 * https://leetcode.com/problems/search-a-2d-matrix-ii/discuss/66139/6-9-lines-C++Python-Solutions-with-Explanations
	 * 
	 * Other code:
	 * https://leetcode.com/problems/search-a-2d-matrix-ii/discuss/66237/O(m+n)-solution-in-Java-with-explanation
	 */
	public boolean searchMatrix(int[][] matrix, int target) {
	    if (matrix == null || matrix.length == 0 || matrix[0].length == 0)
	        return false;

	    int n = matrix.length, m = matrix[0].length;
	    int i = 0, j = m - 1;
	    
	    while (i < n && j >= 0) {
	        int num = matrix[i][j];
	        
	        if (num == target)
	            return true;
	        else if (num > target)
	            j--;
	        else
	            i++;
	    }
	    
	    return false;
	}
	
	// by myself
	public boolean searchMatrix_self(int[][] matrix, int target) {
        if (matrix.length == 0 || matrix[0].length == 0)
            return false;
        
        int count = 0;
        int start = 0;
        int end = matrix[0].length - 1;
        while (count != matrix.length) {
            while (start <= end) {
                int mid = start + (end - start) / 2;
                int value = matrix[count][mid];
                
                if (value == target) {
                    return true;
                }
                else if (value > target) {
                    end = mid - 1;
                }
                else {
                    start = mid + 1;
                }
            }
            
            start = 0;
            end = matrix[0].length - 1;
            count++;
        }
        return false;
    }
	
	/*
	 * The following 2 functions are from this link.
	 * leetcode.com/problems/search-a-2d-matrix-ii/discuss/66147/*Java*-an-easy-to-understand-divide-and-conquer-method/68215
	 * 
	 * Using 2-D binary search searching only 2 regions after every recursive call.
	 * It searches diagonally.
	 */
	public boolean searchMatrix_2D_binarySearch(int[][] matrix, int target) {
		int rs = matrix.length;
		if (rs == 0)
			return false;
		int cs = matrix[0].length;
		if (cs == 0)
			return false;

		int r1 = 0, r2 = rs;
		int c1 = 0, c2 = cs;

		return searchDiagonal(matrix, r1, r2, c1, c2, target);
	}
	boolean searchDiagonal(int[][] matrix, int r1, int r2, int c1, int c2, int target) {
		if (r1 == r2 || c1 == c2)
			return false;
		if (matrix[r2 - 1][c2 - 1] == target)
			return true;
		if (matrix[r2 - 1][c2 - 1] < target)
			return false;
		if (matrix[r1][c1] > target)
			return false;

		int d = Math.min(r2 - r1, c2 - c1);
		int lo = 0;
		int hi = d;
		while (lo < hi) {
			int mid = lo + (hi - lo) / 2;
			if (matrix[r1 + mid][c1 + mid] == target)
				return true;
			else if (matrix[r1 + mid][c1 + mid] > target) {
				hi = mid;
			} 
			else {
				lo = mid + 1;
			}
		}
		return searchDiagonal(matrix, r1, r1 + lo, c1 + lo, c2, target)
				|| searchDiagonal(matrix, r1 + lo, r2, c1, c1 + lo, target);
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/search-a-2d-matrix-ii/discuss/66147/*Java*-an-easy-to-understand-divide-and-conquer-method
	 * 
	 * First, we divide the matrix into four quarters.
	 *  zone 1  |  zone 2
	 * -------------------
	 *  zone 3  |  zone 4
	 *  
	 * We then compare the element in the center of the matrix with the target.
	 * 1. center < target. In this case, we discard zone 1 because all elements in 
	 *    zone 1 are less than target.
	 * 2. center > target. In this case, we discard zone 4.
	 * 3. center == target. return true.
	 * 
	 * T(n) = 3T(n/4) + O(1)
	 * O( (MN)^(log_4(3)) )
	 */
	public boolean searchMatrix_divide_and_conquer(int[][] matrix, int target) {
		int m = matrix.length;
		if (m < 1)
			return false;
		int n = matrix[0].length;

		return find(matrix, new int[] { 0, 0 }, new int[] { m - 1, n - 1 }, target);
	}
	private boolean find(int[][] matrix, int[] upperLeft, int[] lowerRight, int target) {
		if (upperLeft[0] > lowerRight[0] || upperLeft[1] > lowerRight[1] 
				|| lowerRight[0] >= matrix.length || lowerRight[1] >= matrix[0].length)
			return false;
		if (lowerRight[0] == upperLeft[0] && lowerRight[1] == upperLeft[1])
			return matrix[upperLeft[0]][upperLeft[1]] == target;
		
		int rowMid = (upperLeft[0] + lowerRight[0]) >> 1;
		int colMid = (upperLeft[1] + lowerRight[1]) >> 1;
		int diff = matrix[rowMid][colMid] - target;
		if (diff > 0) {
			return find(matrix, upperLeft, new int[] { rowMid, colMid }, target)
					|| find(matrix, new int[] { upperLeft[0], colMid + 1 }, new int[] { rowMid, lowerRight[1] }, target)
					|| find(matrix, new int[] { rowMid + 1, upperLeft[1] }, new int[] { lowerRight[0], colMid }, target);
		} else if (diff < 0) {
			return find(matrix, new int[] { upperLeft[0], colMid + 1 }, new int[] { rowMid, lowerRight[1] }, target)
					|| find(matrix, new int[] { rowMid + 1, upperLeft[1] }, new int[] { lowerRight[0], colMid }, target)
					|| find(matrix, new int[] { rowMid + 1, colMid + 1 }, lowerRight, target);
		} else
			return true;
	}
	
	// brute force for each
	public boolean searchMatrix_brute_forEach(int[][] matrix, int target) {
        for (int[] row : matrix) {
            for (int element : row) {
                if (element == target)
                    return true;
            }
        }
        
        return false;
    }
	
	// brute force
	public boolean searchMatrix_brute(int[][] matrix, int target) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == target)
                    return true;
            }
        }
        return false;
    }

}
