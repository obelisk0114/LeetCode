package OJ0051_0060;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class N_Queens {
	/*
	 * The following 3 functions are by myself.
	 * 
	 * Rf : https://discuss.leetcode.com/topic/19470/my-easy-understanding-java-solution
	 */
	public List<List<String>> solveNQueens_self(int n) {
        List<List<String>> ans = new ArrayList<>();
        placeQueen_self(0, n, new ArrayList<Integer>(), ans);
        return ans;
    }
	private void placeQueen_self(int y, int n, List<Integer> list, List<List<String>> ans) {
		if (y == n) {
            List<String> line = new ArrayList<>();
            int count = 0;
			for (int i = 0; i < n; i++) {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < n; j++) {
                    if (list.get(count) == j) {
                        sb.append("Q");
                    }
                    else {
                        sb.append(".");
                    }
                }
                line.add(sb.toString());
                count++;
            }
            ans.add(line);
			return;
        }
		for (int i = 0; i < n; i++) {
			if (!isSafe_self(i, y, list))
				continue;
			list.add(i);
			placeQueen_self(y + 1, n, list, ans);
			list.remove(list.size() - 1);
		}
	}
	private boolean isSafe_self (int x, int y, List<Integer> list) {
		for (int i = 0; i < y; i++) {
			// Check column
			if (list.get(i) == x) {
				return false;
			}
			// Check diagonal
			if (y - i == Math.abs(x - list.get(i))) {
				return false;
			}
		}
		return true;
	}
	
	/*
	 * The following 4 functions are modified from this link.
	 * https://discuss.leetcode.com/topic/28555/concise-java-solution-based-on-dfs
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/25301/java-clean-accepted-solution-92-3
	 */
    public List<List<String>> solveNQueens_modified(int n) {
        List<List<String>> ans = new ArrayList<>();
        int[] usedCols = new int[n];    // usedCols[i]: Column of the queen in row i
        placeQueen3(0, usedCols, ans);
        return ans;
    }
	private void placeQueen3(int row, int[] usedCols, List<List<String>> ans) {
		int n = usedCols.length;
		if (row == n) {
			ans.add(appendSolution3(usedCols));
			return;
		}
		
		// Check Possible columns for the inputed row.
		for (int col = 0; col < n; col++) {
			if (isSafe3(usedCols, row, col)) {
				usedCols[row] = col;
				placeQueen3(row + 1, usedCols, ans);     // Move on to the next row
			}
		}
	}
	// Check if the column is valid to place queen for the row.
	private boolean isSafe3 (int[] usedCols, int row, int col) {
		for (int i = 0; i < row; i++) {
			// Check column
			if (usedCols[i] == col) {
				return false;
			}
			// Check diagonal : (x2-x1)/(y2-y1) == 1 or -1
			if (row - i == Math.abs(col - usedCols[i])) {
				return false;
			}
		}
		return true;
	}
	private List<String> appendSolution3(int[] usedCols) {
        List<String> res = new ArrayList<>();
		for (int i : usedCols) {
			char[] line = new char[usedCols.length];
			Arrays.fill(line, '.');	    	
	    	line[i] = 'Q'; 
	    	res.add(String.valueOf(line));
		}
		return res;
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/25078/clean-back-tracking-java-solution-with-simple-explaination
	 * 
	   1. boolean[] cols is for check if the certain column is taken.
       2. I use two boolean[2*n] array to keep tracking of two diagonals.
       3. for the diagonal in the \ direction (from left up corner to right down corner) 
          the col - row will always be same e.g. (0,1), (1,2), (2,3) are on the same diagonal, 
          the range of col - row can be (0-(n-1)) ~ ((n-1)-0), 
          to make sure we can store the value in one array, 
          we will add n to this, it will become to keep tracking of (col - row + n).
       4. for the diagonal in the / direction (from right up corner to left down corner) 
          the col + row will always be same e.g. (0,4), (1,3), (2,2), (3,1), (4,0) are on the same diagonal, 
          the range of row + col can be 0 ~ (2*n-2)
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/24329/share-my-java-dfs-solution-very-easy-to-understand
	 * https://discuss.leetcode.com/topic/40881/93-concise-fast-and-clear-java-solution
	 */
	public List<List<String>> solveNQueens_3_direction(int n) {
		List<List<String>> result = new ArrayList<List<String>>();
		helper(result, new ArrayList<String>(), 0, new boolean[n], new boolean[2 * n], new boolean[2 * n], n);
		return result;
	}
	private void helper(List<List<String>> result, List<String> board, int row, 
			boolean[] cols, boolean[] d1, boolean[] d2, int n) {
		if (row == n) {
			result.add(new ArrayList<String>(board));
		}
		for (int col = 0; col < n; col++) {
			int id1 = col - row + n;
			int id2 = col + row;
			if (!cols[col] && !d1[id1] && !d2[id2]) {
				char[] r = new char[n];
				Arrays.fill(r, '.');
				r[col] = 'Q';
				board.add(new String(r));
				cols[col] = true;
				d1[id1] = true;
				d2[id2] = true;
				helper(result, board, row + 1, cols, d1, d2, n);
				board.remove(board.size() - 1);
				cols[col] = false;
				d1[id1] = false;
				d2[id2] = false;
			}
		}
	}

}
