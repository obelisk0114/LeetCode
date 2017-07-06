package OJ051_060;

import java.util.List;
import java.util.ArrayList;

public class Spiral_Matrix {
	// https://discuss.leetcode.com/topic/39444/java-simple-and-clean-solution
	public List<Integer> spiralOrder2(int[][] matrix) {
		List<Integer> result = new ArrayList<Integer>();
		if (matrix.length == 0)
			return result;
		int left = 0, right = matrix[0].length - 1, 
				top = 0, bottom = matrix.length - 1, direction = 0;
		while (left <= right && top <= bottom) {
			if (direction == 0) {
				for (int i = left; i <= right; i++) {
					result.add(matrix[top][i]);
				}
				top++;
			} else if (direction == 1) {
				for (int i = top; i <= bottom; i++) {
					result.add(matrix[i][right]);
				}
				right--;
			} else if (direction == 2) {
				for (int i = right; i >= left; i--) {
					result.add(matrix[bottom][i]);
				}
				bottom--;

			} else if (direction == 3) {
				for (int i = bottom; i >= top; i--) {
					result.add(matrix[i][left]);
				}
				left++;
			}
			direction = (direction + 1) % 4;
		}
		return result;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/22270/elegant-and-fast-java-solution-240ms
	 * 
	 * Rf : https://discuss.leetcode.com/topic/41205/straightforward-java-solution
	 */
	public List<Integer> spiralOrder(int[][] matrix) {
		List<Integer> spiralList = new ArrayList<>();
		if (matrix == null || matrix.length == 0)
			return spiralList;

		// declare indices
		int top = 0;
		int bottom = matrix.length - 1;
		int left = 0;
		int right = matrix[0].length - 1;

		while (true) {
			// 1. print top row
			for (int j = left; j <= right; j++) {
				spiralList.add(matrix[top][j]);
			}
			top++;
			if (boundriesCrossed(left, right, bottom, top)) // if(top > bottom) break;
				break;

			// 2. print rightmost column
			for (int i = top; i <= bottom; i++) {
				spiralList.add(matrix[i][right]);
			}
			right--;
			if (boundriesCrossed(left, right, bottom, top)) // if(right < left) break;
				break;

			// 3. print bottom row
			for (int j = right; j >= left; j--) {
				spiralList.add(matrix[bottom][j]);
			}
			bottom--;
			if (boundriesCrossed(left, right, bottom, top)) // if(bottom < top) break;
				break;

			// 4. print leftmost column
			for (int i = bottom; i >= top; i--) {
				spiralList.add(matrix[i][left]);
			}
			left++;
			if (boundriesCrossed(left, right, bottom, top)) // if(left > right) break;
				break;
		} // end while true

		return spiralList;
	}
	private boolean boundriesCrossed(int left, int right, int bottom, int top) {
		if (left > right || bottom < top)
			return true;
		else
			return false;
	}
		
	// https://discuss.leetcode.com/topic/3713/super-simple-and-easy-to-understand-solution
	public List<Integer> spiralOrder3(int[][] matrix) {

		List<Integer> res = new ArrayList<Integer>();

		if (matrix.length == 0) {
			return res;
		}

		int rowBegin = 0;
		int rowEnd = matrix.length - 1;
		int colBegin = 0;
		int colEnd = matrix[0].length - 1;

		while (rowBegin <= rowEnd && colBegin <= colEnd) {
			// Traverse Right
			for (int j = colBegin; j <= colEnd; j++) {
				res.add(matrix[rowBegin][j]);
			}
			rowBegin++;

			// Traverse Down
			for (int j = rowBegin; j <= rowEnd; j++) {
				res.add(matrix[j][colEnd]);
			}
			colEnd--;

			if (rowBegin <= rowEnd) {
				// Traverse Left
				for (int j = colEnd; j >= colBegin; j--) {
					res.add(matrix[rowEnd][j]);
				}
			}
			rowEnd--;

			if (colBegin <= colEnd) {
				// Traverse Up
				for (int j = rowEnd; j >= rowBegin; j--) {
					res.add(matrix[j][colBegin]);
				}
			}
			colBegin++;
		}

		return res;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
