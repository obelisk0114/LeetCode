package OJ0081_0090;

import java.util.Arrays;
import java.util.Stack;

public class Maximal_Rectangle {
	/*
	 * Get largest rectangle in every row
	 * 
	 * Rf :
	 * https://leetcode.wang/leetCode-85-Maximal-Rectangle.html
	 * https://leetcode.com/problems/largest-rectangle-in-histogram/discuss/28953/java-on-leftright-arrays-solution-4ms-beats-96
	 */
	public int maximalRectangle_self(char[][] matrix) {
        if (matrix == null || matrix.length == 0)
            return 0;
        
        int[] h = new int[matrix[0].length];
        int ans = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == '1') {
                    h[j]++;
                }
                else {
                    h[j] = 0;
                }
            }
            ans = Math.max(ans, histogram(h));
        }
        return ans;
    }
    
    private int histogram(int[] h) {
        int[] left = new int[h.length];
        int[] right = new int[h.length];
        
        left[0] = 0;
        for (int i = 1; i < left.length; i++) {
            int current = i - 1;
            
            while (current >= 0 && h[current] >= h[i]) {
                current = left[current] - 1;
            }
            
            left[i] = current + 1;
        }
        
        right[right.length - 1] = right.length - 1;
        for (int i = right.length - 2; i >= 0; i--) {
            int current = i + 1;
            
            while (current < right.length && h[current] >= h[i]) {
                current = right[current] + 1;
            }
            
            right[i] = current - 1;
        }
        
        int result = 0;
        for (int i = 0; i < h.length; i++) {
            result = Math.max(result, (right[i] - left[i] + 1) * h[i]);
        }
        return result;
    }
	
	/*
	 * https://discuss.leetcode.com/topic/1634/a-o-n-2-solution-based-on-largest-rectangle-in-histogram
	 * 
	 * Maintain a row length of Integer array H recorded its height of '1's, 
	 * and scan and update row by row to find out the largest rectangle of each row.
	 * 
	 * For each row, if matrix[row][i] == '1'. H[i] +=1, or reset the H[i] to zero.
	 */
	public int maximalRectangle_stack(char[][] matrix) {
		if (matrix == null || matrix.length == 0 || matrix[0].length == 0)
			return 0;
		int cLen = matrix[0].length; // column length
		int rLen = matrix.length; // row length
		// height array
		int[] h = new int[cLen + 1];
		h[cLen] = 0;
		int max = 0;

		for (int row = 0; row < rLen; row++) {
			Stack<Integer> s = new Stack<Integer>();
			for (int i = 0; i < cLen + 1; i++) {
				if (i < cLen)
					if (matrix[row][i] == '1')
						h[i] += 1;
					else
						h[i] = 0;

				if (s.isEmpty() || h[s.peek()] <= h[i])
					s.push(i);
				else {
					while (!s.isEmpty() && h[i] < h[s.peek()]) {
						int top = s.pop();
						int area = h[top] * (s.isEmpty() ? i : (i - s.peek() - 1));
						if (area > max)
							max = area;
					}
					s.push(i);
				}
			}
		}
		return max;
	}
	
	/*
	 * The following 3 functions are from this link.
	 * https://discuss.leetcode.com/topic/21772/my-java-solution-based-on-maximum-rectangle-in-histogram-with-explanation
	 * 
	 * First initiate the height array as 1 1 0 1 0 1, which is just a copy of the 
	 * first row. Then we can easily calculate the max area is 2.
	 * Then update the array. We scan the second row, when the matrix[1][i] is 0, 
	 * set the height[i] to 0; else height[i] += 1, which means the height has 
	 * increased by 1. So the height array again becomes 0 2 0 0 1 2.
	 */
	public int maximalRectangle(char[][] matrix) {
		if (matrix == null || matrix.length == 0 || matrix[0].length == 0)
			return 0;

		int[] height = new int[matrix[0].length];
		for (int i = 0; i < matrix[0].length; i++) {
			if (matrix[0][i] == '1')
				height[i] = 1;
		}
		int result = largestInLine(height);
		for (int i = 1; i < matrix.length; i++) {
			resetHeight(matrix, height, i);
			result = Math.max(result, largestInLine(height));
		}

		return result;
	}
	private void resetHeight(char[][] matrix, int[] height, int idx) {
		for (int i = 0; i < matrix[0].length; i++) {
			if (matrix[idx][i] == '1')
				height[i] += 1;
			else
				height[i] = 0;
		}
	}
	public int largestInLine(int[] height) {
		if (height == null || height.length == 0)
			return 0;
		int len = height.length;
		Stack<Integer> s = new Stack<Integer>();
		int maxArea = 0;
		for (int i = 0; i <= len; i++) {
			int h = (i == len ? 0 : height[i]);
			if (s.isEmpty() || h >= height[s.peek()]) {
				s.push(i);
			} else {
				int tp = s.pop();
				maxArea = Math.max(maxArea, height[tp] * (s.isEmpty() ? i : i - 1 - s.peek()));
				i--;
			}
		}
		return maxArea;
	}
	
	/*
	 * https://discuss.leetcode.com/topic/36200/o-n-2-dp-java-solution
	 * 
	 * Rf : https://discuss.leetcode.com/topic/6650/share-my-dp-solution
	 */
	public int maximalRectangle_3_arrays(char[][] matrix) {
		if (matrix.length == 0)
			return 0;
		int m = matrix.length;
		int n = matrix[0].length;
		int[] left = new int[n]; // left boundary of histogram columns.
		int[] right = new int[n]; // right boundary of histogram columns.
		int[] height = new int[n]; // height of histogram columns.
		Arrays.fill(right, n);
		int area = 0;
		for (int i = 0; i < m; i++) {
			int l = 0, r = n;
			for (int j = 0; j < n; j++) {
				if (matrix[i][j] == '1') {
					height[j]++;
					left[j] = Math.max(l, left[j]);
				} else {
					l = j + 1;
					height[j] = 0;
					left[j] = 0;
					right[j] = n;
				}
			}
			for (int j = n - 1; j >= 0; j--) {
				if (matrix[i][j] == '1') {
					right[j] = Math.min(r, right[j]);
					area = Math.max(area, height[j] * (right[j] - left[j]));
				} else {
					r = j;
				}
			}
		}
		return area;
	}
	
	// https://discuss.leetcode.com/topic/42761/java-7ms-solution-beats-100-using-largest-rectangle-in-histogram-solved-by-stack-simulation
	
	// https://discuss.leetcode.com/topic/1122/my-o-n-3-solution-for-your-reference/3
	public int maximalRectangle_n_3(char[][] matrix) {
		int area = 0;
		int numRows = matrix.length;
		if (numRows == 0)
			return 0;
		int numCols = matrix[0].length;
		if (numCols == 0)
			return 0;
		int[][] rowArea = new int[numRows][numCols];
		
		for (int i = 0; i < numRows; i++) { // y
			for (int j = 0; j < numCols; j++) {
				if (matrix[i][j] == '0')
					continue;
				else {
					if (j == 0)
						rowArea[i][j] = 1;  // bottom right
					else {
						rowArea[i][j] = rowArea[i][j - 1] + 1;
					}
					int y = 1;
					int x = numCols;
					while (i - y + 1 >= 0 && rowArea[i - y + 1][j] > 0) {  // climb up
						x = Math.min(x, rowArea[i - y + 1][j]);
						area = Math.max(area, x * y);
						y++;
					}
				}
			}
		}
		return area;
	}
	
	// https://discuss.leetcode.com/topic/20902/my-solution-on-java-using-dp
	public int maximalRectangle_check_0_reset(char[][] matrix) {
		int area = 0, new_area, r, l;
		if (matrix.length > 0) {
			int[] line = new int[matrix[0].length];
			boolean[] is_processed = new boolean[matrix[0].length];
			for (int i = 0; i < matrix.length; i++) {
				for (int j = 0; j < matrix[i].length; j++) {
					if (matrix[i][j] == '1') {
						line[j]++;
						is_processed[j] = false;
					} else {
						line[j] = 0;
						is_processed[j] = true;
					}
				}
				for (int j = 0; j < matrix[i].length; j++) {
					if (is_processed[j])
						continue;
					r = l = 1;
					while ((j + r < line.length) && (line[j + r] >= line[j])) {
						if (line[j + r] == line[j])
							is_processed[j + r] = true;
						r++;
					}
					while ((j - l >= 0) && (line[j - l] >= line[j]))
						l++;
					new_area = (r + l - 1) * line[j];
					if (new_area > area)
						area = new_area;
				}
			}
		}
		return area;
	}

}
