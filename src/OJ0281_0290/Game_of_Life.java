package OJ0281_0290;

public class Game_of_Life {
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/game-of-life/discuss/73223/Easiest-JAVA-solution-with-explanation
	 * 
	 * We can use some dummy cell value
	 * 
	 * [2nd bit, 1st bit] = [next state, current state]
	 * 
	 * - 00  dead (next) <- dead (current)
	 * - 01  dead (next) <- live (current)  
	 * - 10  live (next) <- dead (current)  
	 * - 11  live (next) <- live (current) 
	 * 
	 * To get the current state, board[i][j] & 1
	 * To get the next state, board[i][j] >> 1
	 * 
	 * Rf :
	 * https://leetcode.com/articles/game-of-life/
	 * https://leetcode.com/problems/game-of-life/discuss/73223/Easiest-JAVA-solution-with-explanation/76146
	 * 
	 * Other code :
	 * https://leetcode.com/problems/game-of-life/discuss/73216/Java-Solution-using-2-bits%3A-beats-99.75
	 * https://leetcode.com/problems/game-of-life/discuss/73329/Java-in-place-O(mn)-solution
	 */
	public void gameOfLife(int[][] board) {
	    if (board == null || board.length == 0) return;
	    int m = board.length, n = board[0].length;

	    for (int i = 0; i < m; i++) {
	        for (int j = 0; j < n; j++) {
	            int lives = liveNeighbors(board, m, n, i, j);

	            // In the beginning, every 2nd bit is 0;
	            // So we only need to care about when will the 2nd bit become 1.
	            if (board[i][j] == 1 && (lives == 2 || lives == 3)) {  
	                board[i][j] = 3; // Make the 2nd bit 1: 01 ---> 11
	            }
	            if (board[i][j] == 0 && lives == 3) {
	                board[i][j] = 2; // Make the 2nd bit 1: 00 ---> 10
	            }
	        }
	    }

	    for (int i = 0; i < m; i++) {
	        for (int j = 0; j < n; j++) {
	            board[i][j] >>= 1;  // Get the 2nd state.
	        }
	    }
	}

	public int liveNeighbors(int[][] board, int m, int n, int i, int j) {
		int lives = 0;
		for (int x = Math.max(i - 1, 0); x <= Math.min(i + 1, m - 1); x++) {
			for (int y = Math.max(j - 1, 0); y <= Math.min(j + 1, n - 1); y++) {
				lives += board[x][y] & 1;
			}
		}
		lives -= board[i][j] & 1;
		return lives;
	}
	
	/*
	 * https://leetcode.com/problems/game-of-life/discuss/73366/Clean-O(1)-space-O(mn)-time-Java-Solution
	 * 
	 * 0 - dead, 1 - live, 2 - go die, 3 - will live.
	 * With % to deal with cells' lives
	 * 
	 * Rf : https://leetcode.com/problems/game-of-life/discuss/73366/Clean-O(1)-space-O(mn)-time-Java-Solution/229482
	 */
	public void gameOfLife2(int[][] board) {
		int[][] dir = { { 1, -1 }, { 1, 0 }, { 1, 1 }, 
				{ 0, -1 }, { 0, 1 }, 
				{ -1, -1 }, { -1, 0 }, { -1, 1 } };
		
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				int live = 0;
				for (int[] d : dir) {
					if (d[0] + i < 0 || d[0] + i >= board.length 
							|| d[1] + j < 0 || d[1] + j >= board[0].length)
						continue;
					
					if (board[d[0] + i][d[1] + j] == 1 
							|| board[d[0] + i][d[1] + j] == 2)
						live++;
				}
				
				if (board[i][j] == 0 && live == 3)
					board[i][j] = 3;
				if (board[i][j] == 1 && (live < 2 || live > 3))
					board[i][j] = 2;
			}
		}
		
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				board[i][j] %= 2;
			}
		}
	}
	
	/*
	 * The following 2 functions are from this link.
	 * https://leetcode.com/problems/game-of-life/discuss/210030/Beats-100-Java-solutions-without-tricky-%22bit%22-manipulation-in-place-and-O(1)-memory
	 * 
	 * store the current state plus the next state or the number of neighbors in place.
	 * 
	 * Number of neighbors will be in range 0 to 8. 
	 * So we can multiply that by 10 and add the current state to it. So %10 of that 
	 * will give out the current state and /10 will reveal the number of neighbors.
	 */
	public void gameOfLife_record_neighbor(int[][] board) {
		int iL = board.length;
		int jL = board.length > 0 ? board[0].length : 0;
		
		// Counting all possible neighbors
		for (int i = 0; i < board.length; i++)
			for (int j = 0; j < board[0].length; j++) {
				board[i][j] = ( ((i != 0 ? board[i - 1][j] : 0) 
						+ (i != 0 && j != jL - 1 ? board[i - 1][j + 1] : 0)
						+ (j != jL - 1 ? board[i][j + 1] : 0) 
						+ (i != iL - 1 && j != jL - 1 ? board[i + 1][j + 1] : 0)
						+ (i != iL - 1 ? board[i + 1][j] : 0) 
						+ (i != iL - 1 && j != 0 ? board[i + 1][j - 1] : 0)
						+ (j != 0 ? board[i][j - 1] : 0) 
						+ (i != 0 && j != 0 ? board[i - 1][j - 1] : 0)) % 10 ) * 10
						+ board[i][j];
			}

		for (int i = 0; i < board.length; i++)
			for (int j = 0; j < board[0].length; j++) {
				board[i][j] = updateState_neighbor(board[i][j] % 10, board[i][j] / 10);
			}
	}

	// deriving next state based on neighbor count
	private int updateState_neighbor(int cS, int n) {
		int oS = 1;
		if (cS == 1) {
			if (n < 2 || n > 3)
				oS = 0;
		} 
		else {
			if (n != 3)
				oS = 0;
		}
		return oS;
	}
	
	// https://leetcode.com/problems/game-of-life/discuss/73217/Infinite-board-solution
	// https://leetcode.com/problems/game-of-life/discuss/73217/Infinite-board-solution/201780
	
	/*
	 * The following 2 functions are by myself
	 * 
	 * Other code :
	 * https://leetcode.com/problems/game-of-life/discuss/214063/Easiest-JAVA-solution-(Self-explanatory-code)
	 */
	public void gameOfLife_self(int[][] board) {
        int[][] copy = new int[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                copy[i][j] = board[i][j];
            }
        }
        
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                update_self(board, i, j, copy);
            }
        }
    }
    
    private void update_self(int[][] board, int y, int x, int[][] copy) {
        int top = Math.max(0, y - 1);
        int bottom = Math.min(board.length - 1, y + 1);
        int left = Math.max(0, x - 1);
        int right = Math.min(board[0].length - 1, x + 1);
        
        int live = 0;
        for (int i = top; i <= bottom; i++) {
            for (int j = left; j <= right; j++) {
                if (i == y && j == x)
                    continue;
                
                if (copy[i][j] == 1)
                    live++;
            }
        }
        
        if (copy[y][x] == 1) {
            if (live < 2) {
                board[y][x] = 0;
            }
            else if (live > 3) {
                board[y][x] = 0;
            }
        }
        else {
            if (live == 3) {
                board[y][x] = 1;
            }
        }
    }
    
    /**
     * Python collections
     * 
     * https://leetcode.com/problems/game-of-life/discuss/73271/Python-Solution-with-detailed-explanation
     * https://leetcode.com/problems/game-of-life/discuss/73334/AC-Python-40-ms-solution-O(mn)-time-O(1)-extra-space
     * https://leetcode.com/problems/game-of-life/discuss/174179/python-100-with-explanation
     */
    
    /**
     * C++ collections
     * 
     * https://leetcode.com/problems/game-of-life/discuss/73230/C%2B%2B-O(1)-space-O(mn)-time
     * https://leetcode.com/problems/game-of-life/discuss/73297/0ms-c%2B%2B-solution-easy-to-understand
     * https://leetcode.com/problems/game-of-life/discuss/73335/C%2B%2B-O(mn)-time-O(1)-space-sol
     */

}
