package OJ0031_0040;

/*
 * Find the minimum-remaining-values in each cell, like human
 * https://discuss.leetcode.com/topic/12239/efficient-and-clean-solution-using-minimum-remaining-values-heuristic-and-forward-checking
 * https://discuss.leetcode.com/topic/63534/beats-99-really-really-fast-and-with-explanation-and-chinese-comment
 * 
 * Dancing links
 * https://discuss.leetcode.com/topic/2145/there-is-a-dancing-links-x-algorithm
 */

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

public class Sudoku_Solver {
	/*
	 * The following 3 functions are from this link.
	 * https://discuss.leetcode.com/topic/44216/my-simple-java-back-tracking-solution-with-comments-beats-86-usecases
	 */
	public void solveSudoku(char[][] board) {
		solve(board, 0, 0);
	}
	private boolean solve(char[][] board, int row, int col) {
		if (row == 9) {
			return true; // all cells satisfy the constraints and valid, board is full
		}

		int nextRow = (col == 8) ? row + 1 : row;
		int nextCol = (col == 8) ? 0 : col + 1;
		
		// already filled, fill rest of board and return true if solution exists
		if (board[row][col] != '.') {
			return solve(board, nextRow, nextCol);
		}

		// if cur char is '.', try different numbers and see if solution possible, if
		// doesn't, backtrack
		for (int i = 1; i <= 9; i++) {
			board[row][col] = (char) ('0' + i);

			if (isValid(board, row, col) && solve(board, nextRow, nextCol)) {
				return true;
			}
		}
		
		board[row][col] = '.'; // none of the solutions fitted, so set to its initital value
		return false;
	}
	public boolean isValid(char[][] board, int row, int col) {
		char cur = board[row][col];
		for (int i = 0; i < 9; i++) {
			if (cur == board[i][col] && i != row) {
				return false;
			}
		}
		for (int i = 0; i < 9; i++) {
			if (cur == board[row][i] && i != col) {
				return false;
			}
		}

		int rowbox = (row / 3);
		int colbox = (col / 3);

		for (int i = rowbox * 3; i < rowbox * 3 + 3; i++) {
			for (int j = colbox * 3; j < colbox * 3 + 3; j++) {
				if (board[i][j] == cur && !(i == row && j == col)) {
					return false;
				}
			}
		}
		return true;
	}
	
	/*
	 * The following 3 functions and 1 variable are from this link.
	 * https://discuss.leetcode.com/topic/21112/two-very-simple-and-neat-java-dfs-backtracking-solutions/2
	 * 
	 * Ref : https://discuss.leetcode.com/topic/51938/recursive-java-solution-26ms-38-94
	 */
	private char[][] b;
	public void solveSudoku_board_totalElement_valid(char[][] board) {
	    if(board == null || board.length < 9) return;
	    b = board;
	    solve_board_totalElement_valid(0);
	}
	boolean solve_board_totalElement_valid(int ind){
		if (ind == 81)
			return true;
		int i = ind / 9, j = ind % 9;
		if (b[i][j] != '.')
			return solve_board_totalElement_valid(ind + 1);
		else {
			for (char f = '1'; f <= '9'; f++) {
				if (isValidFill(i, j, f)) {
					b[i][j] = f;
					if (solve_board_totalElement_valid(ind + 1))
						return true;
					b[i][j] = '.';
				}
			}
			return false;
		}
	}
	boolean isValidFill(int i, int j, char fill){
		for (int k = 0; k < 9; k++) {
			int r = i / 3 * 3 + j / 3; // select the block
			if (b[i][k] == fill || b[k][j] == fill || b[r / 3 * 3 + k / 3][r % 3 * 3 + k % 3] == fill)
				return false; // check row, column, block
		}
		return true;
	}
	
	/*
	 * The following 2 functions and 4 variables are from this link.
	 * https://discuss.leetcode.com/topic/21112/two-very-simple-and-neat-java-dfs-backtracking-solutions
	 * 
	 * Ref : https://discuss.leetcode.com/topic/10045/a-fast-solution-in-java-and-how-the-hashtable-can-be-used-to-solve
	 */
	private char[][] b_in;
	private boolean[][] row = new boolean[9][9];
	private boolean[][] col = new boolean[9][9];
	private boolean[][] block = new boolean[9][9];
	public void solveSudoku_store_checkArray(char[][] board) {
		b_in = board;
		int num, k;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (board[i][j] != '.') {
					num = board[i][j] - '1';
					k = i / 3 * 3 + j / 3;
					row[i][num] = col[j][num] = block[k][num] = true;
				}
			}
		}
		Helper_store_checkArray(0);
	}
	boolean Helper_store_checkArray(int ind){
		if (ind == 81)
			return true;
		int i = ind / 9, j = ind % 9, num, k;
		if (b_in[i][j] != '.')
			return Helper_store_checkArray(ind + 1);
		else {
			for (char f = '1'; f <= '9'; f++) {
				num = f - '1';
				k = i / 3 * 3 + j / 3;
				if (!row[i][num] && !col[j][num] && !block[k][num]) {
					b_in[i][j] = f;
					row[i][num] = col[j][num] = block[k][num] = true;
					if (Helper_store_checkArray(ind + 1))
						return true;
					b_in[i][j] = '.';
					row[i][num] = col[j][num] = block[k][num] = false;
				}
			}
			return false;
		}
	}
	
	/*
	 * The following 3 functions are from this link.
	 * https://discuss.leetcode.com/topic/11327/straight-forward-java-solution-using-backtracking
	 * 
	 */
	public void solveSudoku_straight_forward(char[][] board) {
		if (board == null || board.length == 0)
			return;
		solve(board);
	}
	public boolean solve(char[][] board) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (board[i][j] == '.') {
					for (char c = '1'; c <= '9'; c++) {// trial. Try 1 through 9
						if (isValid(board, i, j, c)) {
							board[i][j] = c; // Put c for this cell

							if (solve(board))
								return true; // If it's the solution return true
							else
								board[i][j] = '.'; // Otherwise go back
						}
					}

					return false;
				}
			}
		}
		return true;
	}
    private boolean isValid(char[][] board, int row, int col, char c){
        for(int i = 0; i < 9; i++) {
            if(board[i][col] != '.' && board[i][col] == c)   //check row 
            	return false;
            if(board[row][i] != '.' && board[row][i] == c)   //check column 
            	return false;
            if(board[3 * (row / 3) + i / 3][ 3 * (col / 3) + i % 3] != '.' && 
               board[3 * (row / 3) + i / 3][3 * (col / 3) + i % 3] == c) //check 3*3 block
            	return false;
        }
        return true;
    }
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/43966/easy-understand-java-solution-beat-90
	 * 
	 * Ref : https://discuss.leetcode.com/topic/36437/java-backtracking-stack-20ms
	 */
	boolean subSudoku(char[][] board, boolean[][] rows, boolean[][] cols, 
			boolean[][] secs, List<int[]> emptyCells, int idx) {
	    if (emptyCells.size() == idx) {
	        return true;
	    }

	    int[] cell = emptyCells.get(idx);
	    int x = cell[0];
	    int y = cell[1];
	    int k = x / 3 * 3 + y / 3;

	    for (int i = 0; i < 9; i ++) {
	        if (!rows[x][i] && !cols[y][i] && !secs[k][i]) {
	            board[cell[0]][cell[1]] = (char) ('1' + i);
	            rows[x][i] = true;
	            cols[y][i] = true;
	            secs[k][i] = true;
	            if (subSudoku(board, rows, cols, secs, emptyCells, idx + 1)) {
	                return true;
	            }
	            rows[x][i] = false;
	            cols[y][i] = false;
	            secs[k][i] = false;
	            board[cell[0]][cell[1]] = '.';
	        }
	    }

	    return false;
	}
	void solveSudoku_recordEmpty(char[][] board) {
	    List<int[]> emptyCells = new LinkedList<int[]>();
	    boolean[][] rows = new boolean[9][9];
	    boolean[][] cols = new boolean[9][9];
	    boolean[][] secs = new boolean[9][9];
	    
	    for (int i = 0; i < 9; i ++) {
	        for (int j = 0; j < 9; j ++) {
	            if (board[i][j] == '.') {
	                emptyCells.add(new int[]{i, j});
	            }
	            else {
	                int idx = board[i][j] - '1';
	                rows[i][idx] = true;
	                cols[j][idx] = true;
	                int k = i / 3 * 3 + j / 3;
	                secs[k][idx] = true;
	            }
	        }
	    }

	    if (!emptyCells.isEmpty()) {
	        subSudoku(board, rows, cols, secs, emptyCells, 0);
	    }
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/5269/my-clean-java-solution-by-pre-filtering-the-candidates
	 * 
	 * Use ArrayList "move" to record empty cell. Each time get the first one from move
	 * and utilize an array "filles" to find possible value that can be filled in the 
	 * cell. 
	 */
	void solveSudoku_prefilter(char[][] board) 
	{
	    ArrayList<int[]> moves = new ArrayList<int[]>();
	    for(int i = 0; i < board.length; i++)
	    {
	        for(int j = 0; j < board[0].length; j++)
	        {
	            if(board[i][j] == '.')
	            {
	                int[] r = {i,j};
	                moves.add(r);
	            }
	        }
	    }
	    finder_prefilter(moves, board, 0);
	}	
	boolean finder_prefilter(ArrayList<int[]> moves, char[][] board, int length)
	{
	    if(length >= moves.size())
	    {
	        return true;
	    }
	    int[] pos = moves.get(length);
	    boolean[] filles = new boolean[9];
	    for(int i = 0; i < filles.length; i++)
	    {
	        filles[i] = true;
	    }
	    //find possible moves
	    for(int i = 0; i < board.length; i++)            // Column
	    {
	        if(board[i][pos[1]] != '.')
	        {
	            int v = board[i][pos[1]] - '0';
	            filles[v-1] = false;
	        }
	    }
	    for(int i = 0; i < board[pos[0]].length; i++)     // Row
	    {
	        if(board[pos[0]][i] != '.')
	        {
	            int v = board[pos[0]][i] - '0';
	            filles[v-1] = false;
	        }
	    }
	    for(int i = 0 + (pos[0]/3) *3; i <= 2 + (pos[0]/3) *3; i++)  // Block
	    {
	         for(int j = 0 + (pos[1]/3) *3; j <= 2 + (pos[1]/3) *3; j++)
	         {
	             if(board[i][j] != '.')
	             {
	                 int v = board[i][j] - '0';
	                 filles[v-1] = false;
	             }
	         }
	    }
	    for(int i = 0; i < filles.length; i++)
	    {
	        if(filles[i])
	        {
	           board[pos[0]][pos[1]] = (char)((i+1)+'0');
	           boolean res = finder_prefilter(moves, board,length+1);
	           if(res)
	            return true;
	           board[pos[0]][pos[1]] = '.';
	        }
	    }
	    return false;
	}

}
