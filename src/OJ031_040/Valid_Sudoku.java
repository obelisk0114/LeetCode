package OJ031_040;

/*
 * 
 * Using bit map. Just for reference
 * https://discuss.leetcode.com/topic/34412/my-one-pass-o-1-space-solution-using-java
 */

import java.util.Set;
import java.util.HashSet;

public class Valid_Sudoku {
	// https://discuss.leetcode.com/topic/27436/short-simple-java-using-strings
	public boolean isValidSudoku_set1(char[][] board) {
		Set<String> seen = new HashSet<String>();
		for (int i = 0; i < 9; ++i) {
			for (int j = 0; j < 9; ++j) {
				char number = board[i][j];
				if (number != '.')
					if (!seen.add(number + " in row " + i) || !seen.add(number + " in column " + j)
							|| !seen.add(number + " in block " + i / 3 + "-" + j / 3))
						return false;
			}
		}
		return true;
	}
	
	/*
	 * '4' in row 7 is encoded as "(4)7".
     * '4' in column 7 is encoded as "7(4)".
     * '4' in the top-right block is encoded as "0(4)2".
	 * https://discuss.leetcode.com/topic/27436/short-simple-java-using-strings
	 */
	public boolean isValidSudoku_set2(char[][] board) {
		Set<String> seen = new HashSet<String>();
		for (int i = 0; i < 9; ++i) {
			for (int j = 0; j < 9; ++j) {
				if (board[i][j] != '.') {
					String b = "(" + board[i][j] + ")";
					if (!seen.add(b + i) || !seen.add(j + b) || !seen.add(i / 3 + b + j / 3))
						return false;
				}
			}
		}
		return true;
	}
	
	// https://discuss.leetcode.com/topic/9748/shared-my-concise-java-code
	public boolean isValidSudoku_hashSet(char[][] board) {
		for (int i = 0; i < 9; i++) {
			HashSet<Character> rows = new HashSet<Character>();
			HashSet<Character> columns = new HashSet<Character>();
			HashSet<Character> cube = new HashSet<Character>();
			for (int j = 0; j < 9; j++) {
				if (board[i][j] != '.' && !rows.add(board[i][j]))
					return false;
				if (board[j][i] != '.' && !columns.add(board[j][i]))
					return false;
				// https://discuss.leetcode.com/topic/9748/shared-my-concise-java-code/5
				int RowIndex = 3 * (i / 3);
				int ColIndex = 3 * (i % 3);
				if (board[RowIndex + j / 3][ColIndex + j % 3] != '.'
						&& !cube.add(board[RowIndex + j / 3][ColIndex + j % 3]))
					  return false;
	        }
	    }
	    return true;
	}
	
	
	// https://discuss.leetcode.com/topic/18606/java-solution-easy-to-understand
	public boolean isValidSudoku_2D_array(char[][] board) {
	    boolean[][] col = new boolean[9][9];
	    boolean[][] row = new boolean[9][9];
	    boolean[][] block = new boolean[9][9];
	    for (int i = 0; i < 9; i++) {
	        for (int j = 0; j < 9; j++) {
	            if (board[i][j] == '.') {
	                continue;
	            }
	            int digit = board[i][j] - '1';
	            int blockId = i / 3 * 3 + j / 3;
	            if (col[j][digit]) {
	                return false;
	            } else {
	                col[j][digit] = true;
	            }
	            if (row[i][digit]) {
	                return false;
	            } else {
	                row[i][digit] = true;
	            }
	            if (block[blockId][digit]) {
	                return false;
	            } else {
	                block[blockId][digit] = true;
	            }
	        }
	    }
	    return true;
	}
	
	// https://discuss.leetcode.com/topic/40078/yet-another-java-2ms-solution
	boolean isValidSudoku_bit_operation(char[][] board) {
	    int [] vset = new int [9];
	    int [] hset = new int [9];
	    int [] bckt = new int [9];
	    int idx = 0;
	    for (int i = 0; i < 9; i++) {
	        for (int j = 0; j < 9; j++) {
	            if (board[i][j] != '.') {
	            	// Shift the number of bits. '1' shift 1, '2' shift 2, 'n' shift n
	                idx = 1 << (board[i][j] - '0') ;
	                /*
	                 * If the number had ever appeared, it would have been stored in the
	                 * sets with the bit equal to the number. 
	                 * Therefore, we just AND the sets and idx. 
	                 * If we get a nonzero value, it means that it had appeared and 
	                 * it is not valid. 
	                 */
	                if ((hset[i] & idx) != 0 ||
	                    (vset[j] & idx) != 0 ||
	                    (bckt[(i / 3) * 3 + j / 3] & idx) != 0) return false;
	                // Use OR to store the idx into the sets
	                hset[i] |= idx;
	                vset[j] |= idx;
	                bckt[(i / 3) * 3 + j / 3] |= idx;
	            }
	        }
	    }
	    return true;
	}

}
