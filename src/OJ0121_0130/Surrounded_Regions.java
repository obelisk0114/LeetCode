package OJ0121_0130;

import java.util.ArrayDeque;
import java.util.Deque;

/*
 * https://en.wikipedia.org/wiki/Connected-component_labeling
 */

public class Surrounded_Regions {
	/*
	 * The following 2 functions are from this link.
	 * https://discuss.leetcode.com/topic/25010/java-dfs-boundary-cell-turning-solution-simple-and-clean-code-commented
	 * 
	   The idea is pretty simple: a 'O' marked cell cannot be captured whether:

        1. It is in contact with the border of the board or
        2. It is adjacent to an unflippable cell.
        
       So the algorithm is straightforward:

        1. Go around the border of the board
        2. When a 'O' cell is found mark it with '*' and perform a DFS 
           on its adjacent cells looking for other 'O' marked cells.
        3. When the entire border is processed scan again the board
        
          * If a cell is marked as 'O' it wasn't connected to unflippable cell. 
            Hence capture it with 'X'
          * If a cell is marked as 'X' nothing must be done.
          * If a cell is marked as '*' mark it as 'O' 
            because it was an original 'O' marked cell 
            which satisfied one of the above conditions.
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/6496/my-java-o-n-2-accepted-solution
	 * https://discuss.leetcode.com/topic/45299/java-dfs-solution
	 */
	public void solve_DFS(char[][] board) {
		if (board.length < 2 || board[0].length < 2)
			return;
		int m = board.length, n = board[0].length;
		// Any 'O' connected to a boundary can't be turned to 'X', so ...
		// Start from first and last column, turn 'O' to '*'.
		for (int i = 0; i < m; i++) {
			if (board[i][0] == 'O')
				boundaryDFS(board, i, 0);
			if (board[i][n - 1] == 'O')
				boundaryDFS(board, i, n - 1);
		}
		// Start from first and last row, turn 'O' to '*'
		for (int j = 0; j < n; j++) {
			if (board[0][j] == 'O')
				boundaryDFS(board, 0, j);
			if (board[m - 1][j] == 'O')
				boundaryDFS(board, m - 1, j);
		}
		// post-processing, turn 'O' to 'X', '*' back to 'O', keep 'X' intact.
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (board[i][j] == 'O')
					board[i][j] = 'X';
				else if (board[i][j] == '*')
					board[i][j] = 'O';
			}
		}
	}
	// Use DFS algo to turn internal however boundary-connected 'O' to '*';
	private void boundaryDFS(char[][] board, int i, int j) {
		if (i < 0 || i > board.length - 1 || j < 0 || j > board[0].length - 1)
			return;
		if (board[i][j] == 'O')
			board[i][j] = '*';
		
		if (i > 1 && board[i - 1][j] == 'O')
			boundaryDFS(board, i - 1, j);
		if (i < board.length - 2 && board[i + 1][j] == 'O')
			boundaryDFS(board, i + 1, j);
		if (j > 1 && board[i][j - 1] == 'O')
			boundaryDFS(board, i, j - 1);
		if (j < board[i].length - 2 && board[i][j + 1] == 'O')
			boundaryDFS(board, i, j + 1);
	}
	
	/*
	 * The following 2 functions are by myself.
	 */
	public void solve_dfs2(char[][] board) {
		if (board.length < 2 || board[0].length < 2)
			return;
		int m = board.length, n = board[0].length;
		// Any 'O' connected to a boundary can't be turned to 'X', so ...
		// Start from first and last column, turn 'O' to '*'.
		for (int i = 0; i < m; i++) {
			if (board[i][0] == 'O')
				boundaryDFS2(board, i, 0);
			if (board[i][n - 1] == 'O')
				boundaryDFS2(board, i, n - 1);
		}
		// Start from first and last row, turn 'O' to '*'
		for (int j = 0; j < n; j++) {
			if (board[0][j] == 'O')
				boundaryDFS2(board, 0, j);
			if (board[m - 1][j] == 'O')
				boundaryDFS2(board, m - 1, j);
		}
		// post-processing, turn 'O' to 'X', '*' back to 'O', keep 'X' intact.
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (board[i][j] == 'O')
					board[i][j] = 'X';
				else if (board[i][j] == '*')
					board[i][j] = 'O';
			}
		}
	}
	// Use DFS algo to turn internal however boundary-connected 'O' to '*';
	private void boundaryDFS2(char[][] board, int i, int j) {
		if (i < 0 || i > board.length - 1 || j < 0 || j > board[0].length - 1 
				|| board[i][j] != 'O')
			return;
        
		board[i][j] = '*';
		
		boundaryDFS2(board, i - 1, j);
		boundaryDFS2(board, i + 1, j);
		boundaryDFS2(board, i, j - 1);
		boundaryDFS2(board, i, j + 1);
	}
	
	/*
	 * https://discuss.leetcode.com/topic/38370/bfs-based-solution-in-java
	 * 
	 * Rf : 
	 * https://discuss.leetcode.com/topic/15511/simple-bfs-solution-easy-to-understand
	 * https://discuss.leetcode.com/topic/35191/java-easy-version-to-understand
	 */
	public void solve(char[][] board) {
        if (board == null || board.length == 0 || board[0].length == 0) return;
        int height = board.length, width = board[0].length;
        Deque<int[]> queue = new ArrayDeque<>();  // int[2] as [row, col]
        for (int i = 0; i < width; ++i) {
            if (board[0][i] == 'O') {
                queue.addLast(new int[] {0, i});
                board[0][i] = 'V';  // mark as visited
            }
            if (board[height - 1][i] == 'O') {
                queue.addLast(new int[] {height - 1, i});
                board[height - 1][i] = 'V';
            }
        }
        for (int i = 1; i < height - 1; ++i) {
            if (board[i][0] == 'O') {
                queue.addLast(new int[] {i, 0});
                board[i][0] = 'V';
            }
            if (board[i][width - 1] == 'O') {
                queue.addLast(new int[] {i, width - 1});
                board[i][width - 1] = 'V';
            }
        }
        while (!queue.isEmpty()) {
            int[] cur = queue.removeFirst();
            if (cur[0] - 1 >= 0 && board[cur[0] - 1][cur[1]] == 'O') {  // up
                queue.addLast(new int[] {cur[0] - 1, cur[1]});
                board[cur[0] - 1][cur[1]] = 'V';
            }
            if (cur[0] + 1 < height && board[cur[0] + 1][cur[1]] == 'O') {  // down
                queue.addLast(new int[] {cur[0] + 1, cur[1]});
                board[cur[0] + 1][cur[1]] = 'V';
            }
            if (cur[1] - 1 >= 0 && board[cur[0]][cur[1] - 1] == 'O') {  // left
                queue.addLast(new int[] {cur[0], cur[1] - 1});
                board[cur[0]][cur[1] - 1] = 'V';
            }
            if (cur[1] + 1 < width && board[cur[0]][cur[1] + 1] == 'O') {  // right
                queue.addLast(new int[] {cur[0], cur[1] + 1});
                board[cur[0]][cur[1] + 1] = 'V';
            }
        }
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                if (board[i][j] == 'O') board[i][j] = 'X';
                else if (board[i][j] == 'V') board[i][j] = 'O';
            }
        }
    }
	
	/*
	 * The following variables and functions are from this link.
	 * https://discuss.leetcode.com/topic/1944/solve-it-using-union-find/2
	 */
	int[] unionSet; // union find set
    boolean[] hasEdgeO; // whether an union has an 'O' which is on the edge of the matrix
    
    public void solve_unionFind(char[][] board) {
        if(board.length == 0 || board[0].length == 0) return;
        
        // init, every char itself is an union
		int height = board.length, width = board[0].length;
		unionSet = new int[height * width];
		hasEdgeO = new boolean[unionSet.length];
		for (int i = 0; i < unionSet.length; i++)
			unionSet[i] = i;
		for (int i = 0; i < hasEdgeO.length; i++) {
			int x = i / width, y = i % width;
			hasEdgeO[i] = (board[x][y] == 'O' && (x == 0 || x == height - 1 || y == 0 || y == width - 1));
		}
        
        // iterate the matrix, for each char, union it + its upper char + its right char if they equals to each other
		for (int i = 0; i < unionSet.length; i++) {
			int x = i / width, y = i % width, up = x - 1, right = y + 1;
			if (up >= 0 && board[x][y] == board[up][y])
				union(i, i - width);
			if (right < width && board[x][y] == board[x][right])
				union(i, i + 1);
		}
        
        // for each char in the matrix, if it is an 'O' and its union doesn't has an 'edge O', the whole union should be setted as 'X'
		for (int i = 0; i < unionSet.length; i++) {
			int x = i / width, y = i % width;
			if (board[x][y] == 'O' && !hasEdgeO[findSet(i)])
				board[x][y] = 'X';
		}
    }
    
    private void union(int x,int y){
        int rootX = findSet(x);
        int rootY = findSet(y);
        // if there is an union has an 'edge O',the union after merge should be marked too
		boolean hasEdgeO = this.hasEdgeO[rootX] || this.hasEdgeO[rootY];
		unionSet[rootX] = rootY;
		this.hasEdgeO[rootY] = hasEdgeO;
	}

	private int findSet(int x) {
		if (unionSet[x] == x)
			return x;
		unionSet[x] = findSet(unionSet[x]);
		return unionSet[x];
	}

}
