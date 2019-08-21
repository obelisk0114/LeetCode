package OJ0051_0060;

import java.util.Stack;
import java.util.Set;
import java.util.HashSet;

public class N_Queens_II {
	/*
	 * Combine multiple style
	 * 
	 * Rf : https://leetcode.wang/leetCode-52-N-QueensII.html
	 */
	public int totalNQueens_ultimate(int n) {
        int count = 0;
		boolean[] cols = new boolean[n]; // columns |
		boolean[] d1 = new boolean[2 * n]; // diagonals \
		boolean[] d2 = new boolean[2 * n]; // diagonals /
		return place(0, cols, d1, d2, count);
	}
	public int place(int row, boolean[] cols, boolean[] d1, boolean[] d2, int count) {
        int n = cols.length;
		if (row == n) {
			count++;
			return count;
		}

		for (int col = 0; col < n; col++) {
			int id1 = col - row + n;
			int id2 = col + row;
			if (cols[col] || d1[id1] || d2[id2])
				continue;

			cols[col] = true;
			d1[id1] = true;
			d2[id2] = true;
			count = place(row + 1, cols, d1, d2, count);
			cols[col] = false;
			d1[id1] = false;
			d2[id2] = false;
		}
        return count;
	}
	
	/*
	 * The following 3 functions are from this link.
	 * https://discuss.leetcode.com/topic/30869/share-java-3ms-recurrsion-backtracing-very-easy-to-understand
	 */
	public int totalNQueens(int n) {
		if (n == 0)
			return 0;
		int[] q = new int[n];
		return track(q, 0);
	}
	private int track(int[] q, int row) {
		if (row == q.length)
			return 1;
		int solutions = 0;
		for (int i = 0; i < q.length; i++) {
			q[row] = i;
			if (isValid(q, row, i)) {
				solutions += track(q, row + 1);
			}
		}
		return solutions;
	}
	private boolean isValid(int[] q, int row, int col) {
		for (int i = 0; i < row; i++) {
			if (q[i] == col || Math.abs(row - i) == Math.abs(col - q[i]))
				return false;
		}
		return true;
	}
	
	/*
	 * The following 3 variables and 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/5962/accepted-java-solution
	 */
	private final Set<Integer> occupiedCols = new HashSet<Integer>();
	private final Set<Integer> occupiedDiag1s = new HashSet<Integer>();
	private final Set<Integer> occupiedDiag2s = new HashSet<Integer>();
	public int totalNQueens_3_set(int n) {
		return totalNQueensHelper_3_set(0, 0, n);
	}
	private int totalNQueensHelper_3_set(int row, int count, int n) {
		for (int col = 0; col < n; col++) {
			if (occupiedCols.contains(col))
				continue;
			int diag1 = row - col;
			if (occupiedDiag1s.contains(diag1))
				continue;
			int diag2 = row + col;
			if (occupiedDiag2s.contains(diag2))
				continue;
			// we can now place a queen here
			if (row == n - 1)
				count++;
			else {
				occupiedCols.add(col);
				occupiedDiag1s.add(diag1);
				occupiedDiag2s.add(diag2);
				count = totalNQueensHelper_3_set(row + 1, count, n);
				// recover
				occupiedCols.remove(col);
				occupiedDiag1s.remove(diag1);
				occupiedDiag2s.remove(diag2);
			}
		}

		return count;
	}
	
	/*
	 * The following variable and 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/29626/easiest-java-solution-1ms-98-22
	 */
	private int count1 = 0;
	public int totalNQueens_3_directions(int n) {
		boolean[] cols = new boolean[n]; // columns |
		boolean[] d1 = new boolean[2 * n]; // diagonals \
		boolean[] d2 = new boolean[2 * n]; // diagonals /
		backtracking(0, cols, d1, d2, n);
		return count1;
	}
	public void backtracking(int row, boolean[] cols, boolean[] d1, boolean[] d2, int n) {
		if (row == n) {
			count1++;
			return;
		}

		for (int col = 0; col < n; col++) {
			int id1 = col - row + n;
			int id2 = col + row;
			if (cols[col] || d1[id1] || d2[id2])
				continue;

			cols[col] = true;
			d1[id1] = true;
			d2[id2] = true;
			backtracking(row + 1, cols, d1, d2, n);
			cols[col] = false;
			d1[id1] = false;
			d2[id2] = false;
		}
	}
	
	/*
	 * The following variable and 3 functions are by myself.
	 * 
	 * Other code :
	 * https://discuss.leetcode.com/topic/25302/java-accepted-clean-solutions-98-73
	 */
	private int total_self = 0;
    public int totalNQueens_self(int n) {
        placeQueen_self(0, new int[n]);
        return total_self;
    }
    private void placeQueen_self(int row, int[] used) {
        if (row == used.length) {
            total_self++;
            return;
        }
        for (int col = 0; col < used.length; col++) {
            if (isSafe_self(row, col, used)) {
                used[row] = col;
                placeQueen_self(row + 1, used);
            }
        }
    }
    private boolean isSafe_self(int row, int col, int[] used) {
        for (int i = 0; i < row; i++) {
            // check columns
            if (col == used[i])
                return false;
            // check diagonal
            if (row + col == i + used[i] || row - col == i - used[i])
                return false;
        }
        return true;
    }
    
    /*
     * The following variable and 2 functions are from this link.
     * https://discuss.leetcode.com/topic/38923/share-my-java-code-beats-97-83-run-times
     * 
     * 常規 n-queens 解法, 數答案個數.
     * 用column標記此行之前的哪些column已經放置了queen. 棋盤坐標(row, col)對應column的第col位(LSB --> MSB, 下同).
     * 用diag標記此位置之前的哪些主對角線已經放置了queen. 棋盤坐標(row, col)對應diag的第(n - 1 + row - col)位.
     * 用antiDiag標記此位置之前的哪些副對角線已經放置了queen. 棋盤坐標(row, col)對應antiDiag的第(row + col)位.
     */
	int count = 0;
	public int totalNQueens_bit(int n) {
		dfs(0, n, 0, 0, 0);
		return count;
	}
	private void dfs(int row, int n, int column, int diag, int antiDiag) {
		if (row == n) {
			++count;
			return;
		}
		for (int i = 0; i < n; ++i) {
			boolean isColSafe = ((1 << i) & column) == 0;
			boolean isDiagSafe = ((1 << (n - 1 + row - i)) & diag) == 0;
			boolean isAntiDiagSafe = ((1 << (row + i)) & antiDiag) == 0;
			if (isColSafe && isDiagSafe && isAntiDiagSafe) {
				dfs(row + 1, n, (1 << i) | column, (1 << (n - 1 + row - i)) | diag, (1 << (row + i)) | antiDiag);
			}
		}
	}
    
    /*
     * https://discuss.leetcode.com/topic/2069/my-iterative-solution-for-reference-bit-wise-operation
     * 
     * Rf : https://discuss.leetcode.com/topic/436/what-s-your-solution/3
     */
	public int totalNQueens_bit_stack(int n) {
		long ans = 0, finished = 1, row = 0, leftDiag = 0, rightDiag = 0, canPlace = 1;
		finished = (finished << n) - 1;
		Stack<Long> myStack = new Stack<>();
		myStack.push(row);
		myStack.push(leftDiag);
		myStack.push(rightDiag);
		canPlace = finished & (~(row | leftDiag | rightDiag));
		myStack.push(canPlace);
		while (!myStack.empty()) {
			canPlace = myStack.pop();
			rightDiag = myStack.pop();
			leftDiag = myStack.pop();
			row = myStack.pop();
			while (canPlace != 0) {
				long pos = canPlace & (-canPlace);
				canPlace -= pos;
				myStack.push(row);
				myStack.push(leftDiag);
				myStack.push(rightDiag);
				myStack.push(canPlace);
				row = row | pos;
				if (row == finished)
					ans++;
				leftDiag = (leftDiag | pos) << 1;
				rightDiag = (rightDiag | pos) >> 1;
				canPlace = finished & (~(row | leftDiag | rightDiag));
			}
		}
		return (int) ans;
	}
    
    // https://discuss.leetcode.com/topic/34550/collection-of-solutions-in-java
	
	public static void main(String[] args) {
		N_Queens_II nqueens2 = new N_Queens_II();
		int n = 13;
		System.out.println(nqueens2.totalNQueens_ultimate(n));
	}

}
